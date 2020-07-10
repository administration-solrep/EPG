package fr.dila.solonmgpp.web.metadonnees;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.actions.Action;
import org.nuxeo.ecm.platform.ui.web.component.list.UIEditableList;
import org.nuxeo.ecm.platform.ui.web.util.ComponentUtils;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.nuxeo.runtime.transaction.TransactionHelper;
import org.richfaces.component.html.HtmlInputText;

import fr.dila.solonmgpp.api.constant.SolonMgppCorbeilleConstant;
import fr.dila.solonmgpp.api.descriptor.EvenementMetaDonneesDescriptor;
import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.descriptor.MetaDonneesDescriptor;
import fr.dila.solonmgpp.api.descriptor.PropertyDescriptor;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.TableReferenceDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.MetaDonneesService;
import fr.dila.solonmgpp.api.tree.CorbeilleNode;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.content.MetadonneesContent;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.solonmgpp.web.corbeille.CorbeilleTreeBean;
import fr.dila.solonmgpp.web.espace.NavigationWebActionsBean;
import fr.dila.solonmgpp.web.tree.InstitutionTreeBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.service.VocabularyServiceImpl;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.StringUtil;

@Name("metadonneesActions")
@Scope(ScopeType.CONVERSATION)
public class MetaDonneesActionsBean extends MetadonneesContent implements Serializable {

	private static final long						serialVersionUID				= -4721596289835489136L;

	/**
	 * Logger surcouche socle de log4j
	 */
	private static final STLogger					LOGGER							= STLogFactory
																							.getLog(MetaDonneesActionsBean.class);

	private static final String						METADONNEES_ERREUR_RECUPERATION	= "metadonnees.erreur.recuperation";
	private static final String						NIVEAU_LECTURE					= "niveau_lecture";

	@In(create = true, required = false)
	protected transient NavigationContextBean		navigationContext;

	@In(create = true, required = false)
	protected transient FacesMessages				facesMessages;

	@In(create = true, required = false)
	protected transient ResourcesAccessor			resourcesAccessor;

	@In(create = true, required = false)
	protected transient InstitutionTreeBean			institutionTree;

	@In(create = true, required = true)
	protected transient CoreSession					documentManager;

	@In(create = true, required = false)
	protected transient CorbeilleTreeBean			corbeilleTree;

	@In(create = true, required = false)
	protected transient NavigationWebActionsBean	navigationWebActions;

	@RequestParameter
	protected String								tableReference;

	@RequestParameter
	protected String								proprietaire;

	@RequestParameter
	protected String								typeOrganisme;

	private List<SelectItem>						listTypeEvenement;

	@Override
	protected EvenementDTO getCurrentEvenement() {
		return navigationContext.getCurrentEvenement();
	}

	@Override
	protected void logErrorRecuperation(final EvenementDTO evenement, ClientException exception) {
		String message = resourcesAccessor.getMessages().get(METADONNEES_ERREUR_RECUPERATION);
		LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_META_DONNEE_TEC, message + evenement.toString(), exception);
		facesMessages.add(StatusMessage.Severity.WARN, message);
		TransactionHelper.setTransactionRollbackOnly();
	}

	/**
	 * ajout d'un text dans un field
	 * 
	 * @param field
	 */
	@SuppressWarnings("unchecked")
	public void addText(String widgetId, String listId, String property) {
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();
		UIComponent uiComponent = findComponent(root, widgetId);

		String text = (String) ((HtmlInputText) uiComponent).getSubmittedValue();

		if (StringUtils.isNotBlank(text)) {
			EvenementDTO evenementDTO = getCurrentEvenement();

			if (evenementDTO != null) {
				List<String> selectionList = (List<String>) evenementDTO.get(property);
				if (selectionList == null) {
					selectionList = new ArrayList<String>();
				}
				if (!selectionList.contains(text)) {
					selectionList.add(text);
					evenementDTO.put(property, (Serializable) selectionList);

				}
			}

			UIComponent uiComponent2 = findComponent(root, listId);

			UIComponent base = ComponentUtils.getBase(uiComponent2);
			UIEditableList list = ComponentUtils.getComponent(base, listId, UIEditableList.class);

			if (list != null) {
				// add selected value to the list
				List<Object> dataList = (List<Object>) list.getEditableModel().getWrappedData();
				if (!dataList.contains(text)) {
					list.addValue(text);
				}
			}

			// clear input
			((HtmlInputText) uiComponent).setSubmittedValue(null);
		}
	}

	@SuppressWarnings("unchecked")
	public void removeText(String listId, String index, String value, String property) {
		EvenementDTO evenementDTO = getCurrentEvenement();
		if (evenementDTO != null) {
			List<String> listString = (List<String>) evenementDTO.get(property);
			listString.remove(value);
			evenementDTO.put(property, (Serializable) listString);
		} else {
			FacesContext context = FacesContext.getCurrentInstance();
			UIViewRoot root = context.getViewRoot();
			UIComponent uiComponent = findComponent(root, listId);

			UIComponent base = ComponentUtils.getBase(uiComponent);
			UIEditableList list = ComponentUtils.getComponent(base, listId, UIEditableList.class);

			if (list != null) {
				// remove selected value from the list
				if (StringUtils.isNotBlank(index)) {
					list.getEditableModel().removeValue(Integer.valueOf(index).intValue());
				}
			}
		}
	}

	/**
	 * ajout d'un text dans un field
	 * 
	 * @param field
	 */
	@SuppressWarnings("unchecked")
	public void addDate(String widgetId, String schema, String property, String dateFormat) {
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();
		UIComponent uiComponent = findComponent(root, widgetId);

		String dateToParse = (String) ((HtmlInputText) uiComponent).getSubmittedValue();

		if (StringUtils.isNotBlank(dateToParse)) {
			EvenementDTO evenementDTO = getCurrentEvenement();

			List<Date> selectionList = (List<Date>) evenementDTO.get(property);

			if (selectionList != null) {

				SimpleDateFormat sdf = DateUtil.simpleDateFormat("dd/MM/yyyy");
				Date date;
				try {
					date = sdf.parse(dateToParse);
				} catch (ParseException e) {
					LOGGER.error(documentManager, STLogEnumImpl.FAIL_CONVERT_DATE_TEC,
							"Impossible de parser au format dd/MM/yyyy la date " + dateToParse);
					return;
				}

				if (!selectionList.contains(date)) {
					selectionList.add(date);
					evenementDTO.put(property, (Serializable) selectionList);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void removeDate(Date value, String schema, String property) {
		EvenementDTO evenementDTO = getCurrentEvenement();
		List<Date> listString = (List<Date>) evenementDTO.get(property);
		listString.remove(value);
		evenementDTO.put(property, (Serializable) listString);
	}

	/**
	 * Validation qu'une valeur est bien un int
	 * 
	 * @param facesContext
	 * @param uIComponent
	 * @param object
	 * @throws ValidatorException
	 */
	public void validateIntValue(FacesContext facesContext, UIComponent uIComponent, Object object)
			throws ValidatorException {
		if (object instanceof Integer) {
			return;
		}
		String value = (String) object;
		try {
			Integer.parseInt(value);
		} catch (NumberFormatException e) {
			// ce n'est pas un numero
			FacesMessage message = new FacesMessage();
			message.setSummary("Cette valeur est incorrecte. Nombre entier attendu.");
			throw new ValidatorException(message);
		}
	}

	/**
	 * 
	 * @param tableRef
	 * @param identifiant
	 * @param horodatage
	 * @param skipDate
	 * @return
	 */
	public String getTitleFromTableReference(String tableRef, String identifiant, Date horodatage) {
		return getTitleFromTableReference(tableRef, identifiant, horodatage, false);
	}

	/**
	 * retourne la description de l'element de la table de reference
	 * 
	 * @param tableRef
	 * @param identifiant
	 * @return
	 */
	public String getTitleFromTableReference(String tableRef, String identifiant, Date horodatage, boolean skipDate) {
		if (StringUtils.isEmpty(identifiant)) {
			return null;
		}

		TableReferenceDTO tableReferenceDTO = null;
		try {
			tableReferenceDTO = SolonMgppServiceLocator.getTableReferenceService().findTableReferenceByIdAndType(
					identifiant, tableRef, horodatage, documentManager, skipDate);
		} catch (ClientException e) {
			TransactionHelper.setTransactionRollbackOnly();
		}
		if (tableReferenceDTO != null) {
			return tableReferenceDTO.getTitle();
		} else {
			return "**inconnu** " + identifiant;
		}
	}
	
	/**
	 * retourne l'erreur associée à l'affichage de l'element de la table de reference (null si pas d'erreur)
	 * 
	 * @param tableRef
	 * @param identifiant
	 * @return
	 */
	public String getErrorFromTableReference(String tableRef, String identifiant, Date horodatage, boolean skipDate) {
		if (StringUtils.isEmpty(identifiant)) {
			return null;
		}

		try {
			SolonMgppServiceLocator.getTableReferenceService().findTableReferenceByIdAndType(
					identifiant, tableRef, horodatage, documentManager, skipDate);
		} catch (ClientException e) {
			TransactionHelper.setTransactionRollbackOnly();
			return e.getMessage();
		}
		return null;
	}

	/**
	 * retourne la description de l'element de la table de reference
	 * 
	 * @param tableRef
	 * @param identifiant
	 * @param dateMandat
	 *            : indique si on souhaite retourner la date du mandat
	 * @return
	 */
	public String getTitleFromTableReference(String tableRef, String identifiant, Date horodatage, boolean skipDate,
			boolean dateMandat) {
		if (StringUtils.isEmpty(identifiant)) {
			return null;
		}
		Date date = Calendar.getInstance().getTime();
		TableReferenceDTO tableReferenceDTO = null;
		try {
			tableReferenceDTO = SolonMgppServiceLocator.getTableReferenceService().findTableReferenceByIdAndType(
					identifiant, tableRef, horodatage, documentManager, skipDate);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_TABLE_REFERENCE_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			TransactionHelper.setTransactionRollbackOnly();
		}
		if (tableReferenceDTO != null) {
			if (dateMandat) {
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				String dateDebut = "";
				if (tableReferenceDTO.getDateDebutMandat() != null
						&& tableReferenceDTO.getDateDebutMandat().toString().trim() != "") {
					dateDebut = formatter.format(tableReferenceDTO.getDateDebutMandat());
				}
				String dateFin = "";
				if (tableReferenceDTO.getDateFinMandat() != null
						&& tableReferenceDTO.getDateFinMandat().toString().trim() != "") {
					dateFin = formatter.format(tableReferenceDTO.getDateFinMandat());
				}
				// Construction du titre avec la date de début et la date de fin du mandat pour les mandats qui ont une
				// date de fin qui est antérieure à la date du jour
				if (StringUtil.isNotEmpty(dateFin) && date.compareTo(tableReferenceDTO.getDateFinMandat()) > 0) {
					return tableReferenceDTO.getTitle() + " (" + dateDebut + " - " + dateFin + ")";
				} else {
					return tableReferenceDTO.getTitle();
				}
			} else {
				return tableReferenceDTO.getTitle();
			}
		} else {
			return "**inconnu** " + identifiant;
		}
	}

	/**
	 * Recherche de suggestion dans une table de reference
	 * 
	 * @param input
	 * @return
	 */
	public Object getSuggestions(Object input) {
		try {
			return SolonMgppServiceLocator.getTableReferenceService().searchTableReference((String) input,
					tableReference, documentManager, proprietaire, typeOrganisme);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_TABLE_REFERENCE_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			TransactionHelper.setTransactionRollbackOnly();
		}
		return null;
	}

	/**
	 * Recherche de suggestion dans une table de reference pour les auteurs
	 * 
	 * @param input
	 * @return
	 */
	public Object getSuggestionsAuteur(Object input) {
		try {
			return SolonMgppServiceLocator.getTableReferenceService().searchTableReferenceAuteurSuggestion(
					(String) input, tableReference, documentManager, proprietaire, typeOrganisme);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_TABLE_REFERENCE_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
			TransactionHelper.setTransactionRollbackOnly();
		}
		return null;
	}

	public void removeInstitution(String valueToRemove, String property) {

		if (EvenementDTO.DESTINATAIRE_COPIE.equals(property)) {
			// hack ecart xsd webservice et xsd epp
			property = EvenementDTO.COPIE;
		}

		EvenementDTO evenementDTO = getCurrentEvenement();

		if (evenementDTO == null) {
			return;
		}

		Object value = evenementDTO.get(property);
		if (value instanceof List<?>) {
			List<Object> list = new ArrayList<Object>();
			list.remove(valueToRemove);
			evenementDTO.put(property, (Serializable) list);
			// cas destinataire rien a faire
		} else {
			evenementDTO.put(property, null);
		}

	}

	public void addInstitution(String valueToAdd, String property) {

		if (EvenementDTO.DESTINATAIRE_COPIE.equals(property)) {
			// hack ecart xsd webservice et xsd epp
			property = EvenementDTO.COPIE;
		}

		EvenementDTO evenementDTO = getCurrentEvenement();

		if (evenementDTO == null) {
			return;
		}

		try {
			Object value = evenementDTO.get(property);
			if (value instanceof List<?>) {
				List<Object> list = new ArrayList<Object>();
				list.add(valueToAdd);
				evenementDTO.put(property, (Serializable) list);
				// cas destinataire rien a faire
			} else {
				evenementDTO.put(property, valueToAdd);
			}

			// check des valeurs pour pas qu'il y est 2 fois les mêmes dans emetteur/destinataire/copie
			final MetaDonneesService metaDonneesService = SolonMgppServiceLocator.getMetaDonneesService();

			MetaDonneesDescriptor metaDonneesDescriptor = metaDonneesService.getEvenementType(evenementDTO
					.getTypeEvenementName());

			EvenementMetaDonneesDescriptor evenementMetaDonneesDescriptor = metaDonneesDescriptor.getEvenement();
			if (evenementMetaDonneesDescriptor != null) {

				String emetteur = evenementDTO.getEmetteur();
				String destinataire = evenementDTO.getDestinataire();

				if (emetteur.equals(destinataire)) {
					evenementDTO.setDestinataire(null);
				}

				if (StringUtils.isEmpty(destinataire)) {
					PropertyDescriptor propertyDescriptor = evenementMetaDonneesDescriptor.getProperty().get(
							EvenementDTO.DESTINATAIRE);
					// recuperation et settage des valeurs par defaut si possible du destinataire
					if (propertyDescriptor != null) {

						List<String> list = propertyDescriptor.getListInstitutions();
						list.remove(emetteur);
						if (list.size() == 1) {
							// il en reste qu'un
							evenementDTO.setDestinataire(list.get(0));
						}
					}

				}

				if (StringUtils.isNotEmpty(evenementDTO.getDestinataire())) {
					PropertyDescriptor propertyDescriptor = evenementMetaDonneesDescriptor.getProperty().get(
							EvenementDTO.DESTINATAIRE_COPIE);
					// recuperation et settage des valeurs par defaut si possible
					if (propertyDescriptor != null) {
						List<String> list = propertyDescriptor.getListInstitutions();
						list.remove(emetteur);
						list.remove(evenementDTO.getDestinataire());
						if (list.size() == 1) {
							// il en reste qu'un
							evenementDTO.setCopie(list);
						}

					}
				}

				List<String> destinataireCopie = evenementDTO.getCopie();
				if (destinataireCopie != null) {
					destinataireCopie.remove(emetteur);
					destinataireCopie.remove(destinataire);
				}

				evenementDTO.setCopie(destinataireCopie);

			}

		} catch (ClientException e) {
			LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_CREATE_INSTITUTION_TEC, e);
		} finally {
			// indique a la popup de pas faire son rendu
			institutionTree.setVisible(Boolean.FALSE);
		}
	}

	public String getCurrentLayoutMode() {
		return currentLayoutMode;
	}

	public void setCurrentLayoutMode(String currentLayoutMode) {
		this.currentLayoutMode = currentLayoutMode;
	}

	public String formatDate(Date date) {
		return DateUtil.formatWithHour(date);
	}

	public Boolean isReallyEditable(String columnName, List<String> list, String value) {
		if (LAYOUT_MODE_COMPLETER.equals(currentLayoutMode)) {

			final EvenementDTO evt = getCurrentEvenement();
			if (evt != null) {
				try {
					PropertyDescriptor propertyDescriptor = getPropertyDescriptor(columnName,
							evt.getTypeEvenementName());
					if (propertyDescriptor != null && propertyDescriptor.isObligatoire()) {
						return !list.contains(value);
					}
				} catch (Exception e) {
					return Boolean.TRUE;
				}
			}
		}

		return Boolean.TRUE;

	}

	public boolean isModifie(String metaName) throws ClientException {
		EvenementDTO evenementDTO = getCurrentEvenement();

		if (evenementDTO == null || StringUtils.isBlank(metaName)) {
			return false;
		}

		List<String> metasModifiees = evenementDTO.getMetasModifiees();

		if (metasModifiees != null) {
			PropertyDescriptor propertyDescriptor = getPropertyDescriptor(metaName, evenementDTO.getTypeEvenementName());
			String nameWs = propertyDescriptor.getNameWS();
			return (nameWs.contains(NIVEAU_LECTURE) && this.isNiveauLectureModifie(metasModifiees))
					|| (metasModifiees.contains(nameWs));
		}

		return false;
	}

	private boolean isNiveauLectureModifie(List<String> metasModifiees) {
		for (String str : metasModifiees) {
			if (str.contains(NIVEAU_LECTURE)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected ResourcesAccessor getResourcesAccessor() {
		return resourcesAccessor;
	}

	public List<SelectItem> getEtatEvenement() {
		List<SelectItem> listEtatEvenement = new ArrayList<SelectItem>();

		List<String> list = SolonMgppServiceLocator.getMetaDonneesService().findAllEtatEvenement();

		for (String etatEvenement : list) {
			listEtatEvenement.add(new SelectItem(etatEvenement, getResourcesAccessor().getMessages().get(
					"label.mgpp.etat." + etatEvenement)));
		}

		// tri par value
		Collections.sort(listEtatEvenement, new Comparator<SelectItem>() {

			@Override
			public int compare(SelectItem item1, SelectItem item2) {
				return ((String) item1.getValue()).compareTo(((String) item2.getValue()));
			}

		});
		return listEtatEvenement;
	}

	public List<SelectItem> getEtatMessage() {
		List<SelectItem> listEtatMessage = new ArrayList<SelectItem>();

		CorbeilleNode corbeilleNode = corbeilleTree.getCurrentItem();

		List<String> list = SolonMgppServiceLocator.getMetaDonneesService().findAllEtatMessage(
				corbeilleNode != null && corbeilleNode.getId().endsWith(SolonMgppCorbeilleConstant.HISTORIQUE_SUFFIXE));

		for (String etatMessage : list) {
			listEtatMessage.add(new SelectItem(etatMessage, getResourcesAccessor().getMessages().get(
					"label.mgpp.etat." + etatMessage)));
		}

		// tri par value
		Collections.sort(listEtatMessage, new Comparator<SelectItem>() {

			@Override
			public int compare(SelectItem item1, SelectItem item2) {
				return ((String) item1.getValue()).compareTo(((String) item2.getValue()));
			}

		});
		return listEtatMessage;
	}

	public List<SelectItem> getTypeEvenement() {
		if (listTypeEvenement == null) {
			listTypeEvenement = new ArrayList<SelectItem>();
			for (EvenementTypeDescriptor evenementTypeDescriptor : SolonMgppServiceLocator.getEvenementTypeService()
					.findEvenementType()) {
				listTypeEvenement.add(new SelectItem(evenementTypeDescriptor.getName(), evenementTypeDescriptor
						.getLabel()));
			}

			// tri par value
			Collections.sort(listTypeEvenement, new Comparator<SelectItem>() {

				@Override
				public int compare(SelectItem item1, SelectItem item2) {
					return ((String) item1.getValue()).compareTo(((String) item2.getValue()));
				}

			});
		}
		return listTypeEvenement;
	}

	public String getNiveauLectureLabel(String niveauLectureCode) {
		String label = STServiceLocator.getVocabularyService().getEntryLabel("niveau_lecture", niveauLectureCode);
		if (VocabularyServiceImpl.UNKNOWN_ENTRY.equals(label)) {
			return niveauLectureCode;
		} else {
			return label;
		}
	}

	public String getLabelNatureVersion() {

		StringBuilder label = new StringBuilder();
		List<String> nature = null;
		EvenementDTO evt = getCurrentEvenement();
		if (evt != null) {
			nature = evt.getNature();
		}

		if (nature != null) {

			for (String s : nature) {
				if (!"VERSION_COURANTE".equals(s)) {
					label.append(resourcesAccessor.getMessages().get("metadonnees.evenement.nature." + s));
				}
			}
			if (nature.contains("VERSION_COURANTE")) {
				if (label.length() != 0) {
					label.append(" - ");
				}
				label.append(resourcesAccessor.getMessages().get("metadonnees.evenement.nature.VERSION_COURANTE"));
			}
		}
		return label.toString();
	}

	@Override
	protected String getCurrentProcedure() {
		Action action = navigationWebActions.getCurrentSecondMenuAction();
		if (action != null) {
			return action.getId();
		}
		return null;
	}
}
