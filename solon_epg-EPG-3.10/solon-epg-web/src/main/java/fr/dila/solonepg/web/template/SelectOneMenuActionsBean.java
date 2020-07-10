package fr.dila.solonepg.web.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.dto.VecteurPublicationDTO;
import fr.dila.solonepg.api.dto.tablereference.ModeParutionDTO;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.recherche.TableauDynamique;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.api.service.TableauDynamiqueService;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.solonepg.core.dto.activitenormative.VecteurPublicationDTOImpl;
import fr.dila.solonepg.core.dto.tablereference.ModeParutionDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.admin.parametres.TablesReferenceActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.organigramme.OrganigrammeNodeImpl;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;

@Name("selectOneMenuActions")
@Scope(ScopeType.CONVERSATION)
public class SelectOneMenuActionsBean implements Serializable {

	/**
	 * Serial version UID
	 */
	private static final long						serialVersionUID			= 3852952231303589949L;

	private static final STLogger					LOGGER						= STLogFactory
																						.getLog(SelectOneMenuActionsBean.class);

	private static final String						MODE_PARUTION_TYPE			= "modeParution";

	private static final String						VECTEUR_PUBLICATION_TYPE	= "vecteurPublication";

	private static final String						LISTE_LEGISLATURES_TYPE		= "listeLegislatures";

	private static final String						POSTES_UTILISATEUR			= "posteUtilisateur";

	private static final String						TABLEAU_DYNAMIQUE			= "tableauDynamique";

	@In(create = true, required = true)
	protected transient CoreSession					documentManager;

	@In(create = true, required = false)
	protected transient NavigationContext			navigationContext;

	@In(create = true, required = false)
	protected transient FacesMessages				facesMessages;

	@In(create = true)
	protected transient ResourcesAccessor			resourcesAccessor;

	@In(create = true)
	protected transient TablesReferenceActionsBean	tablesReferenceActions;

	@In(required = true, create = true)
	protected NuxeoPrincipal						currentUser;

	/**
	 * Retourne le label qui sera affiché dans le selectOneMenu
	 * 
	 * @param type
	 * @param object
	 * @return
	 */
	public String getItemLabel(String type, Object object) {
		String label = "";
		if (MODE_PARUTION_TYPE.equals(type)) {
			ModeParutionDTO mode = (ModeParutionDTO) object;
			label = mode.getMode();
		} else if (VECTEUR_PUBLICATION_TYPE.equals(type)) {
			VecteurPublicationDTO vecteur = (VecteurPublicationDTO) object;
			label = vecteur.getIntitule();
		} else if (LISTE_LEGISLATURES_TYPE.equals(type)) {
			label = object.toString();
		} else if (POSTES_UTILISATEUR.equals(type)) {
			OrganigrammeNode poste = (OrganigrammeNodeImpl) object;
			label = poste.getLabel();
		} else if (TABLEAU_DYNAMIQUE.equals(type)) {
			TableauDynamique tableauDynamique = (TableauDynamique) object;
			label = tableauDynamique.getTitle();
		}
		return label;
	}

	/**
	 * Retourne la value qui sera utilisée pour un objet pour le selectOneMenu
	 * 
	 * @param type
	 * @param object
	 * @return
	 */
	public String getItemValue(String type, Object object) {
		String value = "";
		if (MODE_PARUTION_TYPE.equals(type)) {
			ModeParutionDTO mode = (ModeParutionDTO) object;
			value = mode.getId();
		} else if (VECTEUR_PUBLICATION_TYPE.equals(type)) {
			VecteurPublicationDTO vecteur = (VecteurPublicationDTO) object;
			value = vecteur.getId();
		} else if (LISTE_LEGISLATURES_TYPE.equals(type)) {
			value = (String) object;
		} else if (POSTES_UTILISATEUR.equals(type)) {
			OrganigrammeNode poste = (OrganigrammeNodeImpl) object;
			value = poste.getId();
		} else if (TABLEAU_DYNAMIQUE.equals(type)) {
			TableauDynamique tableauDynamique = (TableauDynamique) object;
			value = tableauDynamique.getId();
		}

		return value;
	}

	/**
	 * Retourne la liste des objets pour peupler le selectOneMenu
	 * 
	 * @param type
	 * @return
	 */
	public List<Object> getValues(String type) {
		List<Object> values = new ArrayList<Object>();
		if (MODE_PARUTION_TYPE.equals(type)) {
			TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
			List<DocumentModel> modeParutionDocList = new ArrayList<DocumentModel>();
			try {
				modeParutionDocList.addAll(tableReferenceService.getModesParutionActifsList(documentManager));
			} catch (ClientException exc) {
				final String message = resourcesAccessor.getMessages().get(
						"feedback.solonepg.table.reference.failGetModeParution");
				facesMessages.add(StatusMessage.Severity.WARN, message);
				LOGGER.error(documentManager, EpgLogEnumImpl.FAIL_GET_MODE_PARUTION_FONC, exc);
			}
			for (DocumentModel modeDoc : modeParutionDocList) {
				values.add(new ModeParutionDTOImpl(modeDoc.getAdapter(ModeParution.class)));
			}
		} else if (VECTEUR_PUBLICATION_TYPE.equals(type)) {
			BulletinOfficielService bulletinService = SolonEpgServiceLocator.getBulletinOfficielService();
			List<DocumentModel> vecteursDocList = new ArrayList<DocumentModel>();
			try {
				vecteursDocList.addAll(bulletinService.getAllActifVecteurPublication(documentManager));
			} catch (ClientException exc) {
				final String message = resourcesAccessor.getMessages().get(
						"feedback.solonepg.table.reference.failGetModeParution");
				facesMessages.add(StatusMessage.Severity.WARN, message);
				LOGGER.error(documentManager, EpgLogEnumImpl.FAIL_GET_MODE_PARUTION_FONC, exc);
			}
			for (DocumentModel vectDoc : vecteursDocList) {
				values.add(new VecteurPublicationDTOImpl(vectDoc.getAdapter(VecteurPublication.class)));
			}
		} else if (LISTE_LEGISLATURES_TYPE.equals(type)) {
			try {
				SolonEpgParametreService paramEPGservice = SolonEpgServiceLocator.getSolonEpgParametreService();
				ParametrageAN myDoc = paramEPGservice.getDocAnParametre(documentManager);
				if (myDoc != null) {
					values.addAll(myDoc.getLegislatures());
				}
			} catch (ClientException e) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC,
						"Echec de récupération du paramétrage", e);
			}
		} else if (POSTES_UTILISATEUR.equals(type)) {
			try {
				values.addAll(STServiceLocator.getSTPostesService().getAllPostesForUser(currentUser.getName()));
			} catch (ClientException e) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_POSTE_TEC,
						"Échec de récupération des postes de l'utilisateur connecté : " + currentUser.getName(), e);
			}
		} else if (TABLEAU_DYNAMIQUE.equals(type)) {
			TableauDynamiqueService tableauDynamiqueService = SolonEpgServiceLocator.getTableauDynamiqueService();
			List<DocumentModel> tableauDynamiquesDocList = new ArrayList<DocumentModel>();

			try {
				tableauDynamiquesDocList.addAll(tableauDynamiqueService.findMine(documentManager));
			} catch (ClientException e) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_TABLEAU_DYNAMIQUE,
						"Échec de récupération des tableaux dynamiques : ", e);
			}
			for (DocumentModel modeDoc : tableauDynamiquesDocList) {
				values.add(modeDoc.getAdapter(TableauDynamique.class));
			}
		}

		return values;
	}

	/**
	 * Retourne une chaine de caractère lisible pour l'utilisateur d'une donnée en base pour l'affichage
	 * 
	 * @param type
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getItemLabelFromId(String type, Object object) {
		List<String> ids = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		if (object instanceof String) {
			ids.add((String) object);
		} else if (object instanceof List) {
			ids.addAll((List<String>) object);
		}
		for (String id : ids) {
			if (MODE_PARUTION_TYPE.equals(type)) {
				final IdRef docRef = new IdRef(id);
				try {
					if (documentManager.exists(docRef)) {
						ModeParution mode = documentManager.getDocument(docRef).getAdapter(ModeParution.class);
						values.add(mode.getMode());
					}
				} catch (ClientException ce) {
					LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOCUMENT_FONC, id, ce);
				}
			} else if (VECTEUR_PUBLICATION_TYPE.equals(type)) {
				final IdRef docRef = new IdRef(id);
				try {
					if (documentManager.exists(docRef)) {
						VecteurPublication vecteur = documentManager.getDocument(docRef).getAdapter(
								VecteurPublication.class);
						values.add(vecteur.getIntitule());
					} else {
						// Dans le cadre des bulletins, on a pas un id mais un libellé : on affiche la valeur
						values.add(id);
					}
				} catch (ClientException ce) {
					LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOCUMENT_FONC, id, ce);
				}
			} else if (LISTE_LEGISLATURES_TYPE.equals(type)) {
				return (String) object;
			} else if (POSTES_UTILISATEUR.equals(type)) {
				try {
					values.add(STServiceLocator.getSTPostesService().getPoste(id).getLabel());
				} catch (ClientException e) {
					LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_POSTE_TEC, "Échec de récupération du poste "
							+ id, e);
				}
			} else if (TABLEAU_DYNAMIQUE.equals(type)) {
				String nomTableauDynamique;
				try {
					nomTableauDynamique = documentManager.getDocument(new IdRef(id)).getName();
					values.add(nomTableauDynamique);
				} catch (ClientException e) {
					LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_TABLEAU_DYNAMIQUE,
							"Échec de récupération du tableau dynamique " + id, e);
				}
			}

		}
		return StringUtil.join(values, ", ", "");
	}

}