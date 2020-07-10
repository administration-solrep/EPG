package fr.dila.solonepg.web.admin.parametres;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.dto.VecteurPublicationDTO;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.core.dto.activitenormative.VecteurPublicationDTOImpl;
import fr.dila.solonepg.core.service.BulletinOfficielServiceImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.admin.organigramme.OrganigrammeManagerActionsBean;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.context.NavigationContextBean;

@Name("bulletinOfficielActions")
@Scope(ScopeType.PAGE)
public class BulletinOfficielActionsBean implements Serializable {

	/**
	 * Serial version UID
	 */
	private static final long							serialVersionUID	= 4404124658724195992L;

	private static final STLogger						LOGGER				= STLogFactory
																					.getLog(BulletinOfficielActionsBean.class);

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(create = true, required = false)
	protected transient FacesMessages					facesMessages;

	@In(create = true)
	protected transient ResourcesAccessor				resourcesAccessor;

	@In(create = true)
	protected transient OrganigrammeManagerActionsBean	organigrammeManagerActions;

	@In(create = true, required = false)
	protected transient NavigationContextBean			navigationContext;

	@In(create = true, required = true)
	protected ContentViewActions						contentViewActions;

	private List<VecteurPublicationDTO>					lstVecteurs;

	protected String									intitule;

	protected String									nor;

	public String getQuery() {
		return BulletinOfficielServiceImpl.GET_ALL_ACTIVE_QUERY;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public String getNor() {
		return nor;
	}

	public void setNor(String nor) {
		this.nor = nor;
	}

	public String saveBulletinOfficiel() throws ClientException {
		boolean error = false;
		if (nor == null || nor.isEmpty()) {
			String message = resourcesAccessor.getMessages().get("bulletin.officiel.error.nor");
			facesMessages.add(StatusMessage.Severity.WARN, message);
			error = true;
		} else {
			try {
				Integer.parseInt(nor);
				OrganigrammeNode node = STServiceLocator.getSTMinisteresService().getEntiteNode(nor);
				if (node == null) {
					String message = resourcesAccessor.getMessages().get("bulletin.officiel.error.nor");
					facesMessages.add(StatusMessage.Severity.WARN, message);
					error = true;
				} else {
					// là on renseigne le NOR qui correspond au node si le nor qu'on a actuellement est un chiffre
					EntiteNode nodeEntite = STServiceLocator.getSTMinisteresService().getEntiteNode(nor);
					if (nodeEntite != null && StringUtil.isNumeric(nor)) {
						nor = nodeEntite.getNorMinistere();
					}
				}
			} catch (NumberFormatException e) {
				// saisie libre => verification de la taille
				if (!nor.matches("^[a-zA-Z]{3}$")) {
					String message = resourcesAccessor.getMessages().get("bulletin.officiel.error.nor");
					facesMessages.add(StatusMessage.Severity.WARN, message);
					error = true;
				}
			}
		}
		if (intitule == null || intitule.isEmpty() || intitule.contains("<") || intitule.contains(">")) {
			String message = resourcesAccessor.getMessages().get("bulletin.officiel.error.intitule");
			facesMessages.add(StatusMessage.Severity.WARN, message);
			error = true;
		}
		if (!error) {
			SolonEpgServiceLocator.getBulletinOfficielService().create(documentManager, nor, intitule);
			setNor(null);
			setIntitule(null);
			organigrammeManagerActions.setSelectedNodeId(null);
			String message = resourcesAccessor.getMessages().get("bulletin.officiel.add.ok");
			facesMessages.add(StatusMessage.Severity.INFO, message);
		}
		return SolonEpgViewConstant.BULLETIN_OFFICIEL_VIEW;
	}

	public DocumentModelList getAllActiveBulletinOfficiel() throws ClientException {
		return SolonEpgServiceLocator.getBulletinOfficielService().getAllActiveBulletinOfficielOrdered(documentManager);

	}

	public String deleteBulletinOfficiel(String bulletinOfficielToDelete) throws ClientException {
		SolonEpgServiceLocator.getBulletinOfficielService().delete(documentManager, bulletinOfficielToDelete);
		String message = resourcesAccessor.getMessages().get("bulletin.officiel.delete.ok");
		facesMessages.add(StatusMessage.Severity.INFO, message);
		return SolonEpgViewConstant.BULLETIN_OFFICIEL_VIEW;
	}

	/**
	 * Controle l'accès à la vue correspondante
	 * 
	 */
	public boolean isAccessAuthorized() {
		SSPrincipal ssPrincipal = (SSPrincipal) documentManager.getPrincipal();
		return (ssPrincipal.isAdministrator() || ssPrincipal
				.isMemberOf(SolonEpgBaseFunctionConstant.ADMINISTRATION_BULLETIN_READER));
	}

	public String addVecteur() {
		if (lstVecteurs != null) {
			try {
				DocumentModel vecteurDoc = SolonEpgServiceLocator.getBulletinOfficielService().initVecteurPublication(
						documentManager);
				VecteurPublication vecteurDesired = vecteurDoc.getAdapter(VecteurPublication.class);
				getLstVecteurs().add(new VecteurPublicationDTOImpl(vecteurDesired));
				final String message = resourcesAccessor.getMessages().get("feedback.solonepg.vecteur.publication.add");
				facesMessages.add(StatusMessage.Severity.INFO, message);
			} catch (ClientException e) {
				LOGGER.error(documentManager, EpgLogEnumImpl.FAIL_ADD_VECTEUR_TEC, e);
				final String message = resourcesAccessor.getMessages().get(
						"feedback.solonepg.vecteur.publication.failAdd");
				facesMessages.add(StatusMessage.Severity.ERROR, message);
			}
		} else {
			facesMessages.add(StatusMessage.Severity.ERROR, "Liste des vecteurs non initialisée");
		}
		return null;
	}

	public String saveVecteur() {
		try {
			if (lstVecteurs != null) {
				boolean doublonIntitule = false;
				// On parcourt la liste élément par élément jusqu'à l'avant dernier
				for (int i = 0; i < lstVecteurs.size() - 1; i++) {
					// Pour chaque élément suivant l'élément en cours, on vérifie s'ils ont le même intitulé
					VecteurPublicationDTO elt = lstVecteurs.get(i);
					for (int j = i + 1 ; j < lstVecteurs.size(); j++) {
						if (elt.getIntitule() != null && elt.getIntitule().equals(lstVecteurs.get(j).getIntitule())) {
							doublonIntitule = true;							
							break;
						}
					}
					// Si on a déjà un doublon, on arrête
					if (doublonIntitule) {
						break;
					}
				}

				if (doublonIntitule) {
					final String message = resourcesAccessor.getMessages().get(
							"feedback.solonepg.vecteur.publication.doublon");
					facesMessages.add(StatusMessage.Severity.WARN, message);
				} else {
					SolonEpgServiceLocator.getBulletinOfficielService().saveVecteurPublication(documentManager,
							lstVecteurs);
					Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
					final String message = resourcesAccessor.getMessages().get(
							"feedback.solonepg.vecteur.publication.save");
					facesMessages.add(StatusMessage.Severity.INFO, message);
				}
			}
		} catch (ClientException e) {
			LOGGER.error(documentManager, EpgLogEnumImpl.FAIL_SAVE_VECTEUR_TEC, e);
			final String message = resourcesAccessor.getMessages()
					.get("feedback.solonepg.vecteur.publication.failSave");
			facesMessages.add(StatusMessage.Severity.ERROR, message);
		}
		return null;
	}

	public String cancelVecteur() {
		lstVecteurs = null;
		contentViewActions.refresh("vecteur_publication_content");
		final String message = resourcesAccessor.getMessages().get("feedback.solonepg.vecteur.publication.cancel");
		facesMessages.add(StatusMessage.Severity.INFO, message);

		return null;
	}

	public List<VecteurPublicationDTO> getLstVecteurs() {
		if (lstVecteurs == null) {
			try {
				List<DocumentModel> lstModel = SolonEpgServiceLocator.getBulletinOfficielService()
						.getAllVecteurPublication(documentManager);

				lstVecteurs = new ArrayList<VecteurPublicationDTO>();
				for (DocumentModel model : lstModel) {
					VecteurPublication vect = model.getAdapter(VecteurPublication.class);
					lstVecteurs.add(new VecteurPublicationDTOImpl(vect));
				}

			} catch (ClientException e) {
				LOGGER.error(documentManager, EpgLogEnumImpl.FAIL_GET_VECTEUR_TEC, e);
				final String message = resourcesAccessor.getMessages().get(
						"feedback.solonepg.vecteur.publication.failGet");
				facesMessages.add(StatusMessage.Severity.ERROR, message);
			}
		}
		return lstVecteurs;
	}

	public void setLstVecteurs(List<VecteurPublicationDTO> lstVecteurs) {
		this.lstVecteurs = lstVecteurs;
	}

}
