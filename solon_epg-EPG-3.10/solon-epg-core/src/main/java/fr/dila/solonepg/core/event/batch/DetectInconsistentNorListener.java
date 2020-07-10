package fr.dila.solonepg.core.event.batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.event.Event;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.core.util.ExcelUtil;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.ProfileService;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;

public class DetectInconsistentNorListener extends AbstractBatchEventListener {

	private static final STLogger	LOGGER	= STLogFactory.getLog(DetectInconsistentNorListener.class);

	public DetectInconsistentNorListener() {
		super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_DETECT_NOR_INCONSISTENT);
	}

	@Override
	protected void processEvent(CoreSession session, Event event) throws ClientException {
		LOGGER.info(session, EpgLogEnumImpl.INIT_B_NOR_INCONSISTENT_TEC);
		final List<DocumentModel> dossiersDoc = searchForDossierNorInconsistent(session);

		if (dossiersDoc.size() > 0) {
			sendMailAlertDossierFailed(session, dossiersDoc);
		} else {
			LOGGER.info(session, EpgLogEnumImpl.PROCESS_B_NOR_INCONSISTENT_TEC, "Aucun NOR incohérent détecté.");
		}
		LOGGER.info(session, EpgLogEnumImpl.END_B_NOR_INCONSISTENT_TEC);
	}

	/**
	 * recherche les dossiers dont les lettres nor ne correspondent pas au lettres de leur ministère ou direction
	 * responsable effectives
	 * 
	 * @param session
	 * @return List<Dossier> les dossiers dont le nor n'est pas cohérent
	 * @throws ClientException
	 */
	private List<DocumentModel> searchForDossierNorInconsistent(final CoreSession session) throws ClientException {
		final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
		final STUsAndDirectionService usService = STServiceLocator.getSTUsAndDirectionService();

		final ArrayList<DocumentModel> dossierDocNorInconsistentList = new ArrayList<DocumentModel>();

		// Récupération dossier inités && lancés
		final String queryDossier = "SELECT * FROM Dossier WHERE dos:statut IN (1,2) and ecm:isProxy = 0";
		final DocumentModelList listDossier = session.query(queryDossier);
		if (listDossier == null || listDossier.isEmpty()) {
			LOGGER.info(session, EpgLogEnumImpl.PROCESS_B_NOR_INCONSISTENT_TEC, "Pas de dossiers initiés ou lancés");
			return dossierDocNorInconsistentList;
		}

		for (final DocumentModel dossierDoc : listDossier) {
			// Récupération lettres NOR 3 (ministère responsable) + 1 (direction responsable)
			final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			final String nor = dossier.getNumeroNor();
			final String lettresNor = StringUtil.substring(nor, 0, 4);
			final String lettresMin = StringUtil.substring(lettresNor, 0, 3);
			final String lettreDir = StringUtil.substring(lettresNor, 3);

			final String minRespId = dossier.getMinistereResp();
			final String dirRespId = dossier.getDirectionResp();

			// Entite ministere
			final EntiteNode ministere = ministeresService.getEntiteNode(minRespId);

			if (ministere == null) {
				continue;
			}
			final String norMinistere = ministere.getNorMinistere();

			// US direction
			final UniteStructurelleNode direction = usService.getUniteStructurelleNode(dirRespId);

			if (direction == null || !direction.getType().equals(OrganigrammeType.DIRECTION)) {
				continue;
			}
			final String norDirection = direction.getNorDirection(minRespId);

			// Comparaison avec ministère responsable et direction responsable effective
			if (!lettresMin.equals(norMinistere) || !lettreDir.equals(norDirection)) {
				dossierDocNorInconsistentList.add(dossierDoc);
			}
		}
		return dossierDocNorInconsistentList;
	}

	/**
	 * Méthode pour envoyer un mail aux administrateurs fonctionnels contenant une pièce jointe excel avec les données
	 * des dossiers au nor incohérent.
	 * 
	 * @param session
	 * @param idsDossiers
	 * @throws ClientException
	 */
	protected void sendMailAlertDossierFailed(final CoreSession session, final List<DocumentModel> dossiersDocList)
			throws ClientException {
		final STParametreService paramService = STServiceLocator.getSTParametreService();
		final ProfileService profileService = STServiceLocator.getProfileService();

		String content = paramService.getParametreValue(session,
				SolonEpgParametreConstant.TEXTE_MAIL_NOR_DOSSIERS_INCOHERENT);
		final String object = paramService.getParametreValue(session,
				SolonEpgParametreConstant.OBJET_MAIL_NOR_DOSSIERS_INCOHERENT);
		final String nomFichier = "Resultat_dossier_nor_incoherent.xls";
		final List<STUser> users = profileService
				.getUsersFromBaseFunction(STBaseFunctionConstant.ADMIN_FONCTIONNEL_EMAIL_RECEIVER);
		final List<String> addresses = new ArrayList<String>();

		// Récuperation des adresses e-mail des administrateurs fonctionnels
		for (final STUser user : users) {
			final String mail = user.getEmail();
			if (mail != null && !"".equals(mail)) {
				addresses.add(mail);
			}
		}

		// On change la variable nb_resultats pour afficher le nombre dans le mail
		final Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("nb_resultats", String.valueOf(dossiersDocList.size()));
		content = StringUtil.renderFreemarker(content, paramMap);

		// Création du fichier excel qui sera joint au mail
		final DataSource fichierResultat = generateData(session, dossiersDocList);

		final STMailService mailService = STServiceLocator.getSTMailService();
		try {
			if (fichierResultat == null) {
				// Si le fichier excel n'a pas été créé, on envoie malgré tout le mail
				mailService.sendTemplateMail(addresses, object, content, paramMap);
			} else {
				mailService.sendMailWithAttachement(addresses, object, content, nomFichier, fichierResultat);
			}
		} catch (final Exception exc) {
			LOGGER.error(session, STLogEnumImpl.FAIL_SEND_MAIL_TEC, exc);
		}
	}

	/**
	 * Méthode pour générer un fichier qui pourra être envoyé par mail. Utilisation de la classe ExcelUtil
	 * 
	 * @return DataSource si on a pu la créer, null sinon
	 */
	private DataSource generateData(final CoreSession session, final List<DocumentModel> dossiersDoc) {
		DataSource fichierExcelResultat = null;
		fichierExcelResultat = ExcelUtil.creationListDossierExcel(dossiersDoc);
		return fichierExcelResultat;
	}
}
