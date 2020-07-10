package fr.dila.solonepg.web.requeteuran;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.smart.query.jsf.IncrementalSmartNXQLQuery;
import org.nuxeo.ecm.platform.smart.query.jsf.SmartNXQLQueryActions;
import org.nuxeo.ecm.platform.ui.web.util.ComponentUtils;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgRequeteurConstant;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.core.util.FullTextUtil;
import fr.dila.solonepg.core.util.RequeteurUtils;
import fr.dila.solonepg.web.activitenormative.EspaceActiviteNormativeTreeBean;
import fr.dila.solonepg.web.activitenormative.EspaceActiviteNormativeTreeNode;
import fr.dila.st.api.service.RequeteurService;
import fr.dila.st.core.query.Requeteur;
import fr.dila.st.core.query.translation.TranslatedStatement;
import fr.dila.st.core.requeteur.STIncrementalSmartNXQLQuery;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.context.NavigationContextBean;
import fr.dila.st.web.event.STEventNames;
import fr.dila.st.web.requeteur.STRequeteurConverterActions;

@Name("epgANSmartNXQLQueryActions")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = Install.APPLICATION + 1)
public class EPGANSmartNXQLQueryActions extends SmartNXQLQueryActions implements Serializable {

	private static final String							AN_COMPLETE_QUERY	= "SELECT * FROM ActiviteNormative AS d"
																					+ " WHERE ";

	private static final long							serialVersionUID	= 1L;

	protected String									completeQuery;

	protected Requeteur									requeteur;

	@In(create = true)
	protected transient NavigationContextBean			navigationContext;

	@RequestParameter
	protected Integer									index;

	@In(create = true)
	protected transient ResourcesAccessor				resourcesAccessor;

	@In(create = true, required = false)
	protected transient FacesMessages					facesMessages;

	@In(create = true, required = false)
	protected transient EspaceActiviteNormativeTreeBean	espaceActiviteNormativeTree;

	private static final Log							LOGGER				= LogFactory
																					.getLog(EPGANSmartNXQLQueryActions.class);

	private EspaceActiviteNormativeTreeNode				currentRequeteurWorkingSpace;

	@In(create = true, required = false)
	protected STRequeteurConverterActions				stRequeteurConverterActions;

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	public void initCurrentSmartQuery (String existingQueryPart, boolean resetHistory) {
		LOGGER.info("Initialisation requêteur de EPG AN ");
		// Remet à zéro la recherche experte, si l'utilisateur a changé d'espace
		if (isDifferentSearch()) {
			LOGGER.info("Vide le requêteur");
			existingQueryPart = StringUtils.EMPTY;
		}
		super.initCurrentSmartQuery(existingQueryPart, resetHistory);
		requeteur = new Requeteur();
		this.completeQuery = AN_COMPLETE_QUERY + existingQueryPart;
		requeteur.setQuery(resolveSpecialKeywords(this.completeQuery));
		currentSmartQuery = new STIncrementalSmartNXQLQuery(existingQueryPart);
		requeteur.updateTranslation();
		setRequeteurWorkingSpace(espaceActiviteNormativeTree.getCurrentItem());
	}

	public String getFullQuery () {
		ActiviteNormativeEnum anEnum = espaceActiviteNormativeTree.getActiviteNormativeEnum();
		String fullQuery = RequeteurUtils.getPatternAN(documentManager, anEnum, getQueryPart());
		// la recherche NOR ne doit pas commencer par '*' : on ne veut pas que l'utilisateur puisse rechercher tous les
		// dossiers.
		if (FullTextUtil.beginWithStarQuery(fullQuery)) {
			LOGGER.info("Query before star error :" + fullQuery);
			fullQuery = DossierSolonEpgConstants.QUERY_BEGIN_STAR_ERROR;
		}
		LOGGER.info("Requête du requeteur de l'activité normative expert : " + fullQuery);
		return fullQuery;
	}

	/**
	 * Cette méthode est appelé par la classe mère lors de l'ajout d'une entrée. On veut déclencher un événement seam.
	 * 
	 */
	public void buildQueryPart (ActionEvent event) throws ClientException {
		Events.instance().raiseEvent(STEventNames.REQUETEUR_EPG_AN_QUERY_PART_ADDED);
		super.buildQueryPart(event);
	}

	/**
	 * Cette méthode est appelé par la classe mère lors de la modification de la requête. On veut déclencher un
	 * événement seam.
	 * 
	 */
	public void setQueryPart (ActionEvent event, String newQuery, boolean rebuildSmartQuery) throws ClientException {
		Events.instance().raiseEvent(STEventNames.REQUETEUR_EPG_AN_PART_CHANGED);
		if (!rebuildSmartQuery) {
			this.completeQuery = AN_COMPLETE_QUERY;
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Set QueryPart completeQuery = " + completeQuery);
			}
		}
		super.setQueryPart(event, newQuery, rebuildSmartQuery);
	}

	/**
	 * Met le libellé utilisateur correspondant à la portion de requête ajouté.
	 */
	@Observer(STEventNames.REQUETEUR_EPG_AN_PART_CHANGED)
	public void updateQuery () {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Requête complête : ");
		}
		updateCompleteQuery();
		requeteur.setQuery(resolveSpecialKeywords(this.completeQuery));
		requeteur.updateTranslation();
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("User info : " + this.completeQuery);
		}
	}

	/**
	 * Met à jour l'attribut completeQuery qui contient la requête.
	 */
	protected void updateCompleteQuery () {
		this.completeQuery = AN_COMPLETE_QUERY + currentSmartQuery.getExistingQueryPart();
	}

	private String resolveSpecialKeywords (String query) {
		RequeteurService service = STServiceLocator.getRequeteurService();
		return service.resolveKeywords(documentManager, query);
	}

	public List<TranslatedStatement> getUserInfo () {
		List<TranslatedStatement> statements = new ArrayList<TranslatedStatement>();
		try {
			requeteur.updateTranslation();
			statements = requeteur.getStatements();
		} catch (Exception e) {
			// Erreur de parsing qui ne devrait pas arriver dans cette méthode, l'utilisateur ne devrait pouvoir
			// constituer que des
			// requêtes valides.
			// On ne peut pas récupérer l'erreur à ce stade. On réinitialise la requête et le tableau de traduction.
			// La requête de l'utilisateur est perdue.
			LOGGER.warn("Requête invalide (" + this.completeQuery + ") " + e.getMessage());
			facesMessages.add(StatusMessage.Severity.INFO,
					resourcesAccessor.getMessages().get("error.smart.query.invalidQuery"));
			return statements;
		}
		return statements;
	}

	/**
	 * Validates the query part: throws a {@link ValidatorException} if query is not a String, or if
	 * {@link IncrementalSmartNXQLQuery#isValid(String)} returns false.
	 * 
	 * @see IncrementalSmartNXQLQuery#isValid(String)
	 */
	public void validateQueryPart (FacesContext context, UIComponent component, Object value) {
		if (value == null || !(value instanceof String)) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, ComponentUtils.translate(context,
					"error.smart.query.invalidQuery"), null);
			context.addMessage(null, message);
			throw new ValidatorException(message);
		}
	}

	@Override
	public void clearQueryPart (ActionEvent event) throws ClientException {
		super.clearQueryPart(event);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Clear requeteur");
		}
		this.completeQuery = AN_COMPLETE_QUERY;
		requeteur.setQuery(resolveSpecialKeywords(this.completeQuery));
		reinitialiseSmartQuery();
	}

	private void reinitialiseSmartQuery () {
		this.setQueryPart(requeteur.getWherePart());
		currentSmartQuery.setExistingQueryPart(requeteur.getWherePart());
		initCurrentSmartQuery(requeteur.getWherePart(), false);
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			UIViewRoot root = context.getViewRoot();
			UIComponent composant = findComponent(root, queryPartComponentId);
			EditableValueHolder queryPartComp = ComponentUtils.getComponent(composant, queryPartComponentId,
					EditableValueHolder.class);
			if (queryPartComp != null) {
				queryPartComp.setSubmittedValue(requeteur.getWherePart());
				queryPartComp.setValue(requeteur.getWherePart());
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Portion: " + this.queryPart);
		}
	}

	public void delete (ActionEvent event) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Supprimer à l'index " + index);
		}
		requeteur.remove(index);
		requeteur.updateTranslation();
		LOGGER.info("Met la requête à vide");
		reinitialiseSmartQuery();
	}

	public void up (ActionEvent event) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Monter à l'index " + index);
		}
		requeteur.up(index);
		requeteur.updateTranslation();
		reinitialiseSmartQuery();
	}

	public void down (ActionEvent event) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Descendre à l'index " + index);
		}
		requeteur.down(index);
		requeteur.updateTranslation();
		reinitialiseSmartQuery();
	}

	public Requeteur getCurrentRequeteur () {
		requeteur.setQuery(resolveSpecialKeywords(this.completeQuery));
		return requeteur;
	}

	public void setSelectedCategoryName (String categoryName) {
		if (currentSmartQuery instanceof STIncrementalSmartNXQLQuery) {
			((STIncrementalSmartNXQLQuery) currentSmartQuery).setSelectedCategoryName(categoryName);
		}
	}

	/**
	 * Finds component with the given id
	 */
	private UIComponent findComponent (UIComponent component, String identifier) {
		if (identifier.equals(component.getId())) {
			return component;
		}
		Iterator<UIComponent> kids = component.getFacetsAndChildren();
		while (kids.hasNext()) {
			UIComponent found = findComponent(kids.next(), identifier);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	/**
	 * Retourne le converter approprié à partir du searchField
	 * 
	 * @param searchField
	 * @return
	 */
	public Converter getConverter (String searchField) {
		return stRequeteurConverterActions.getConverter(searchField,
				SolonEpgRequeteurConstant.REQUETEUR_EXPERT_ACTIVITE_NORMATIVE_CONTRIBUTION);
	}

	public Requeteur getRequeteur () {
		return requeteur;
	}

	public void setRequeteur (Requeteur requeteur) {
		this.requeteur = requeteur;
	}

	private Boolean isDifferentSearch () {
		if (espaceActiviteNormativeTree.getCurrentItem() == null) {
			return false;
		}
		return !espaceActiviteNormativeTree.getCurrentItem().equals(getRequeteurWorkingSpace());
	}

	public EspaceActiviteNormativeTreeNode getRequeteurWorkingSpace () {
		return currentRequeteurWorkingSpace;
	}

	private void setRequeteurWorkingSpace (EspaceActiviteNormativeTreeNode currentNode) {
		this.currentRequeteurWorkingSpace = currentNode;
		this.setSelectedCategoryName(currentNode.getName());
	}

}
