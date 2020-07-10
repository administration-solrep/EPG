package fr.dila.solonepg.web.contentview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.core.SeamResourceBundle;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.ClientRuntimeException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.audit.api.LogEntry;

import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.util.FullTextUtil;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.contentview.STJournalAdminPageProvider;
import fr.dila.st.web.journal.STJournalActions;

/**
 * page provider du journal affiche dans l'espace d'administration
 * 
 * @author BBY, ARN
 * @author asatre
 * 
 */
public class JournalAdminPageProvider extends STJournalAdminPageProvider {

	private static final long	serialVersionUID	= 8571408546145315786L;

	/**
	 * Récupération des identifiants du dossier à partir d'un numéro
	 * 
	 * @throws ClientException
	 */
	@Override
	protected void getDossierIdsList() throws ClientException {
		Map<String, Serializable> props = getProperties();
		// récupération de la session
		CoreSession coreSession = (CoreSession) props.get(CORE_SESSION_PROPERTY);
		if (coreSession == null) {
			throw new ClientRuntimeException("cannot find core session");
		}
		// récupération de l'identifiant technique du dossier à partir de son NOR
		String norDossier = (String) getParameters()[4];
		if(norDossier != null)
			norDossier = norDossier.trim();
		if (StringUtils.isNotBlank(norDossier)) {
			// on récupère les identifiants de dossier qui ont le numéro Nor indiqué
			NORService nORService = SolonEpgServiceLocator.getNORService();
			dossierIdList = nORService.findDossierIdsFromNOR(coreSession,
					FullTextUtil.replaceStarByPercent(norDossier.toUpperCase()));
			// On ajoute également les nors qui sont dans la table "ID_NOR_DOSSIERS_SUPPRIMES"
			DossierService dossierService = SolonEpgServiceLocator.getDossierService();
			List<String> listIds = dossierService.getIdsDossiersSupprimes(norDossier);
			// on supprime les doublons déjà présents dans la première liste (normalement le cas ne se présente pas,
			// mais au cas où...)
			listIds.removeAll(dossierIdList);
			// Puis on ajoute la seconde liste à la première
			if (!listIds.isEmpty()) {
				dossierIdList.addAll(listIds);
			}
			if (dossierIdList.isEmpty()) {
				dossierIdList = null;
			}

		}
	}

	/**
	 * Retourne un String représentant le commentaire comme il est supposé être stocké en base de données en remplaçant
	 * des parties de ce commentaire qui sont stockées dans messages.properties par leurs clés correspondantes
	 */
	protected String formatComment(Object comment) {
		if (comment == null)
			return null;
		String res = comment.toString().trim();
		res = "%" + res.replace("%", "") + "%";
		ResourceBundle bundle = SeamResourceBundle.getBundle();
		Enumeration<String> keys = bundle.getKeys();
		for (String key : Collections.list(keys)) {
			if (key.startsWith("label.journal.comment.") || key.startsWith("label.epg.feuilleRoute.etape.")) {
				String message = bundle.getString(key);
				if (res.contains(message) && !message.isEmpty()) {
					res = res.replace(message, key);
				}
			}
		}
		// Corriger les erreurs de remplacement connues
		Map<String, String> corrections = new HashMap<String, String>();
		corrections.put("label.journal.comment.chercherDossier Ce", "label.journal.comment.chercherDossierCe");
		corrections.put("label.epg.feuilleRoute.etape.pour.avis CE", "label.epg.feuilleRoute.etape.pour.avisCE");
		corrections.put("label.epg.feuilleRoute.etape.pour.attribution au SGG",
				"label.epg.feuilleRoute.etape.pour.attributionSGG");
		corrections.put("label.epg.feuilleRoute.etape.pour.attribution au secteur parlementaire",
				"label.epg.feuilleRoute.etape.pour.attributionSecteurParlementaire");
		for (Map.Entry<String, String> correction : corrections.entrySet()) {
			if (res.contains(correction.getKey()))
				res = res.replace(correction.getKey(), correction.getValue());
		}
		return res;
	}
}
