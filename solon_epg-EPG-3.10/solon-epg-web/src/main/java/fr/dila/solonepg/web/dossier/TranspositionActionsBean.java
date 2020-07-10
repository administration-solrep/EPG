package fr.dila.solonepg.web.dossier;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.Property;
import org.nuxeo.ecm.core.api.model.impl.MapProperty;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.service.DossierBordereauService;
import fr.dila.solonepg.api.service.TranspositionDirectiveService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.core.domain.ComplexeTypeImpl;

@Name("transpositionActions")
@Scope(ScopeType.CONVERSATION)
public class TranspositionActionsBean implements Serializable {
	private static final long						serialVersionUID	= 1L;

	@In(create = true, required = true)
	protected transient CoreSession					documentManager;

	@In(create = true, required = false)
	protected transient NavigationContext			navigationContext;

	@In(create = true, required = false)
	protected transient FacesMessages				facesMessages;

	@In(create = true)
	protected transient ResourcesAccessor			resourcesAccessor;

	@In(create = true)
	protected transient DossierCreationActionsBean	dossierCreationActions;

	private static final Log						log					= LogFactory
																				.getLog(TranspositionActionsBean.class);

	private String									errorName			= null;

	private String									fieldError			= null;

	public static String							errorTransposition	= null;

	protected String								directiveAnnee		= null;

	/**
	 * Transposition en cours d'édition
	 */
	private MapProperty								currentlyEditedItem;

	/**
	 * Map des données des champs du widget en cours
	 */
	private Map<String, Serializable>				widgetMap;

	public void addTransposition(DocumentModel dossierDoc, String field) {
		String reference = widgetMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY).toString();
		errorTransposition = null;

		// champ non vide. Pour la transpositon des directives on gère ça plus loin car 2 champs obligatoires
		if (!field.equals(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY)
				&& StringUtils.isEmpty(reference)) {
			displayValidationError("epg.dossierCreation.ref.non.nulle", field);
			return;
		}

		// validation des formats
		// on vérifie les valeurs obligatoires
		// note : voir si besoin de vérifier l'expression de la référence de la transposition des directives

		if (field.equals(DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY)) {
			try {
				if (!getDossierBordereauService().isFieldReferenceLoiValid(documentManager, reference)) {
					displayValidationError("epg.dossierCreation.loi.ref.non.valide", field);
					return;
				}
			} catch (ClientException e) {
				log.error(e.getMessage());
				displayValidationError("epg.dossierCreation.loi.ref.non.valide.exception", field);
				return;
			}
			String numeroOrdre = widgetMap.get(DossierSolonEpgConstants.DOSSIER_LOI_APPLIQUEE_REF_MESURE_PROPERTY)
					.toString();
			if (StringUtils.isNotEmpty(numeroOrdre)) {
				try {
					if (!getDossierBordereauService().isFieldNumeroOrdreValid(documentManager, numeroOrdre)) {
						displayValidationError("epg.dossierCreation.numero.ordre.non.valide", field);
						return;
					}
				} catch (ClientException e) {
					log.error(e.getMessage());
					displayValidationError("epg.dossierCreation.numero.ordre.non.valide.exception", field);
					return;
				}
			}
		}

		if (field.equals(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY)) {
			try {
				if (!getDossierBordereauService().isFieldReferenceOrdonnanceValid(documentManager, reference)) {
					displayValidationError("epg.dossierCreation.ordonnance.ref.non.valide", field);
					return;
				}
			} catch (ClientException e) {
				log.error(e.getMessage());
				displayValidationError("epg.dossierCreation.numero.ordre.non.valide.exception", field);
				return;
			}
		}

		if (field.equals(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY)) {
			// On retire l'année de la field map et on concatène l'année et la référence
			widgetMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY).toString();
			String annee = widgetMap.get(DossierSolonEpgConstants.DOSSIER_TRANSPO_DIRECTIVE_REF_ANNEE_PROPERTY)
					.toString();
			Boolean champsObligatoiresOK = true;
			String messageErreur = "";
			// champ non vide
			if (StringUtils.isEmpty(annee)) {
				messageErreur = resourcesAccessor.getMessages()
						.get("epg.dossierCreation.annee.transposition.non.nulle");
				champsObligatoiresOK = false;
			} else if (annee.length() != 4 || !StringUtils.isNumeric(annee)) {
				messageErreur = resourcesAccessor.getMessages().get(
						"epg.dossierCreation.annee.transposition.format.incorrect");
				champsObligatoiresOK = false;
			}
			if (StringUtils.isEmpty(reference)) {
				messageErreur += " " + resourcesAccessor.getMessages().get("epg.dossierCreation.ref.non.nulle");
				champsObligatoiresOK = false;
			} else if (reference.length() > 4) {
				messageErreur += " " + resourcesAccessor.getMessages().get("epg.dossierCreation.ref.format.incorrect");
				champsObligatoiresOK = false;
			}

			String titre = widgetMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY).toString();
			String titreDirective = titre;
			if (champsObligatoiresOK) {
				// Vérification de l'existence de la directive
				TranspositionDirectiveService transpositionDirectiveService = SolonEpgServiceLocator
						.getTranspositionDirectiveService();
				titreDirective = transpositionDirectiveService.findDirectiveEurlexWS(reference, annee, titre);
				if (titreDirective == null) {
					// Erreur -> Directive non trouvée
					if (titre == null || titre.isEmpty()) {
						messageErreur = resourcesAccessor.getMessages().get(
								"La directive " + " http://data.europa.eu /eli /dir/ " + annee + "/" + reference + " /"
										+ "oj" + " n'existe pas. Veuillez renseigner un titre");
						champsObligatoiresOK = false;
					} else {

						errorTransposition = resourcesAccessor.getMessages().get(
								"La directive " + " http://data.europa.eu /eli /dir/ " + annee + "/" + reference + " /"
										+ "oj" + " n'existe pas");

						titreDirective = titre + " http://data.europa.eu/eli/dir/" + annee + "/" + reference + "/"
								+ "oj";
					}
				} else {
					titreDirective += " http://data.europa.eu/eli/dir/" + annee + "/" + reference + "/" + "oj";
				}
			}

			if (!champsObligatoiresOK) {
				errorName = messageErreur;
				fieldError = field;
				return;
			}
			widgetMap.remove(DossierSolonEpgConstants.DOSSIER_TRANSPO_DIRECTIVE_REF_ANNEE_PROPERTY);
			widgetMap.remove(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY);
			String ref = annee + "/" + reference;
			widgetMap.put(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY, ref);

			widgetMap.remove(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY);
			widgetMap.put(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY, titreDirective);
		}
		// Permet de gérer le cas où on a une instabilité
		// et où on retrouve un directive ref annee
		// alors que pas une transposition de directive
		if (widgetMap.containsKey(DossierSolonEpgConstants.DOSSIER_TRANSPO_DIRECTIVE_REF_ANNEE_PROPERTY)
				&& !field.equals(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY)) {
			widgetMap.remove(DossierSolonEpgConstants.DOSSIER_TRANSPO_DIRECTIVE_REF_ANNEE_PROPERTY);
		}
		Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		List<ComplexeType> transpositions = getTranspositions(dossier, field);
		ComplexeType complexeType = new ComplexeTypeImpl(widgetMap);
		transpositions.add(complexeType);
		setTranspositions(dossier, field, transpositions);

		reset();
	}

	public void editTransposition(DocumentModel dossierDoc, String field, MapProperty item) throws Exception {
		errorTransposition = null;
		if (field.equals(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY)) {
			String[] reference = item.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY).getValue()
					.toString().split("/");
			if (reference.length > 1) {
				directiveAnnee = reference[0];
				String ref = reference[1];
				item.setValue(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY, ref);
			}
		}
		currentlyEditedItem = item;
	}

	public void validateEditTransposition(DocumentModel dossierDoc, String field, MapProperty item) throws Exception {
		String reference = item.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY).getValue().toString();
		Boolean champsObligatoiresOK = true;
		String messageErreur = "";
		errorTransposition = null;
		// validation des formats
		// on vérifie les valeurs obligatoires
		// note : voir si besoin de vérifier l'expression de la référence de la transposition des directives

		if (field.equals(DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY)) {
			try {
				if (!getDossierBordereauService().isFieldReferenceLoiValid(documentManager, reference)) {
					messageErreur = resourcesAccessor.getMessages().get("epg.dossierCreation.loi.ref.non.valide");
					champsObligatoiresOK = false;
				}
			} catch (ClientException e) {
				messageErreur = resourcesAccessor.getMessages().get("epg.dossierCreation.loi.ref.non.valide.exception");
				champsObligatoiresOK = false;
			}
			String numeroOrdre = widgetMap.get(DossierSolonEpgConstants.DOSSIER_LOI_APPLIQUEE_REF_MESURE_PROPERTY)
					.toString();
			if (StringUtils.isNotEmpty(numeroOrdre)) {
				try {
					if (!getDossierBordereauService().isFieldNumeroOrdreValid(documentManager, numeroOrdre)) {
						messageErreur = resourcesAccessor.getMessages().get(
								"epg.dossierCreation.numero.ordre.non.valide");
						champsObligatoiresOK = false;
					}
				} catch (ClientException e) {
					messageErreur = resourcesAccessor.getMessages().get(
							"epg.dossierCreation.numero.ordre.non.valide.exception");
					champsObligatoiresOK = false;
				}
			}
		}

		if (field.equals(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY)) {
			try {
				if (!getDossierBordereauService().isFieldReferenceOrdonnanceValid(documentManager, reference)) {
					messageErreur = resourcesAccessor.getMessages()
							.get("epg.dossierCreation.ordonnance.ref.non.valide");
					champsObligatoiresOK = false;
				}
			} catch (ClientException e) {
				messageErreur = resourcesAccessor.getMessages().get(
						"epg.dossierCreation.numero.ordre.non.valide.exception");
				champsObligatoiresOK = false;
			}
		}
		if (field.equals(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY)) {
			if (!champsObligatoiresOK) {
				messageErreur = resourcesAccessor.getMessages().get("epg.dossierCreation.ref.non.nulle");
			} else if (reference.length() > 4 || reference.isEmpty()) {
				messageErreur += " " + resourcesAccessor.getMessages().get("epg.dossierCreation.ref.format.incorrect");
				champsObligatoiresOK = false;
			}
			if (this.directiveAnnee.trim().isEmpty()) {
				champsObligatoiresOK = false;
				messageErreur += " "
						+ resourcesAccessor.getMessages().get("epg.dossierCreation.annee.transposition.non.nulle");
			} else if (this.directiveAnnee.length() != 4 || !StringUtils.isNumeric(this.directiveAnnee)) {
				messageErreur = resourcesAccessor.getMessages().get(
						"epg.dossierCreation.annee.transposition.format.incorrect");
				champsObligatoiresOK = false;
			}
			if (champsObligatoiresOK) {
				String titreRenseigne = item.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY)
						.getValue().toString();
				TranspositionDirectiveService transpositionDirectiveService = SolonEpgServiceLocator
						.getTranspositionDirectiveService();
				String titreDirective = transpositionDirectiveService.findDirectiveEurlexWS(reference,
						this.directiveAnnee, titreRenseigne);
				if (titreDirective == null) {
					// Erreur -> Directive non trouvée
					if (titreRenseigne == null || titreRenseigne.isEmpty()) {
						champsObligatoiresOK = false;
						messageErreur = resourcesAccessor.getMessages().get(
								"La directive " + " http://data.europa.eu /eli /dir/ " + this.directiveAnnee + "/"
										+ reference + " /" + "oj" + " n'existe pas. veuillez renseigner un titre");
					} else {
						errorTransposition = resourcesAccessor.getMessages().get(
								"La directive " + " http://data.europa.eu /eli /dir/ " + this.directiveAnnee + "/"
										+ reference + " /" + "oj" + " n'existe pas");
						titreDirective = item.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY)
								.getValue().toString();
						if (!titreDirective.contains("http://data.europa.eu/eli/dir/")) {
							titreDirective += " http://data.europa.eu/eli/dir/" + this.directiveAnnee + "/" + reference
									+ "/" + "oj";
						}
					}
				} else {
					if (!titreDirective.contains("http://data.europa.eu/eli/dir/")) {
						titreDirective += " http://data.europa.eu/eli/dir/" + this.directiveAnnee + "/" + reference
								+ "/" + "oj";
					}
				}
				if (champsObligatoiresOK) {
					item.setValue(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY, titreDirective);
					// Modification de l'item concaténer année et transposition
					reference = this.directiveAnnee + "/" + reference;
					item.setValue(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY, reference);
				}
			}

		}
		if (champsObligatoiresOK) {
			currentlyEditedItem = null;
			errorName = null;
			fieldError = null;
		} else {
			errorName = messageErreur;
			fieldError = field;
		}
	}

	public Boolean isEditWidgetItem(MapProperty item) throws Exception {
		return (currentlyEditedItem == item);
	}

	public void removeTransposition(DocumentModel dossierDoc, String field, MapProperty item) throws Exception {
		errorTransposition = null;
		if (item != null && item.getChildren() != null) {
			Collection<Property> properties = item.getChildren();
			Map<String, Serializable> serializableMap = new HashMap<String, Serializable>();
			for (Property property : properties) {
				serializableMap.put(property.getName(), property.getValue());
			}
			ComplexeType remove = new ComplexeTypeImpl(serializableMap);

			Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			List<ComplexeType> transpositions = getTranspositions(dossier, field);
			transpositions.remove(remove);
			setTranspositions(dossier, field, transpositions);
		}
	}

	private List<ComplexeType> getTranspositions(Dossier dossier, String field) {
		if (field.equals(DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY)) {
			return dossier.getApplicationLoi();
		}
		if (field.equals(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY)) {
			return dossier.getTranspositionDirective();
		}
		if (field.equals(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY)) {
			return dossier.getTranspositionOrdonnance();
		}
		return null;
	}

	private void setTranspositions(Dossier dossier, String field, List<ComplexeType> transpositions) {
		if (field.equals(DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY)) {
			dossier.setApplicationLoi(transpositions);
		}
		if (field.equals(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY)) {
			dossier.setTranspositionDirective(transpositions);
		}
		if (field.equals(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY)) {
			dossier.setTranspositionOrdonnance(transpositions);
		}
	}

	public Map<String, Serializable> getWidgetMap() {
		if (widgetMap == null) {
			widgetMap = new HashMap<String, Serializable>();
		}
		return widgetMap;
	}

	public void setWidgetMap(Map<String, Serializable> widgetMap) {
		this.widgetMap = widgetMap;
	}

	public DossierBordereauService getDossierBordereauService() {
		// note performance : on utilise un bean avec un scope conversation pour récupérer le service pour éviter de le
		// recharger à chaque EVENT
		// le DossierBordereauService fait une requête BDD pour récupérer les expressions régulière
		return dossierCreationActions.getDossierBordereauService();
	}

	public void reset() {
		widgetMap = new HashMap<String, Serializable>();
		errorName = null;
		fieldError = null;
		directiveAnnee = null;
	}

	public void displayValidationError(String errorMsg, String field) {
		widgetMap = new HashMap<String, Serializable>();
		errorName = resourcesAccessor.getMessages().get(errorMsg);
		fieldError = field;
	}

	public String getErrorName() {
		return errorName;
	}

	public String getFieldError() {
		return fieldError;
	}

	public String getDirectiveAnnee() {
		return directiveAnnee;
	}

	public void setDirectiveAnnee(String directiveAnnee) {
		this.directiveAnnee = directiveAnnee;
	}

	public String getErrorTransposition() {
		return errorTransposition;
	}

	public void setErrorTransposition(String errorTransposition) {
		this.errorTransposition = errorTransposition;
	}
}
