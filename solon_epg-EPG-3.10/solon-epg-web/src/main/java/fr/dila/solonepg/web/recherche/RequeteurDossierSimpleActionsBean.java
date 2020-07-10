package fr.dila.solonepg.web.recherche;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.platform.userworkspace.web.ejb.UserWorkspaceManagerActionsBean;
import org.nuxeo.ecm.webapp.contentbrowser.DocumentActions;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RechercheConstants;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.recherche.FavorisRecherche;
import fr.dila.solonepg.api.recherche.dossier.RequeteDossier;
import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.solonepg.api.service.RequeteurDossierSimpleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.util.FullTextUtil;
import fr.dila.solonepg.web.alerte.EPGAlertActionsBean;
import fr.dila.solonepg.web.recherche.RechercheWebActionsBean.TypeSearch;
import fr.dila.solonepg.web.recherche.calendar.DateDynamicActionBean;
import fr.dila.solonepg.web.requeteur.RequeteurActionsBean;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.recherche.Recherche;
import fr.dila.st.api.requeteur.RequeteExperte;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Le bean pour le requeteur de dossier "simple". ( Les 4 panneaux de recherche)
 * 
 * @author jgomez
 * 
 */
@Name("requeteurDossierSimpleActions")
@Scope(ScopeType.CONVERSATION)
public class RequeteurDossierSimpleActionsBean implements Serializable {

	private static final int							MAX_RECHERCHE_TEXTUELLE_SIZE	= 250;

	private static final long							serialVersionUID				= 1L;

	@In(create = true, required = false)
	protected transient NavigationContextBean			navigationContext;

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(create = true, required = false)
	protected transient FacesMessages					facesMessages;

	@In(create = true)
	protected DocumentModel								currentRequete;

	@In(create = true)
	protected ResourcesAccessor							resourcesAccessor;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean	routingWebActions;

	@In(create = true, required = true)
	protected transient RechercheWebActionsBean			rechercheWebActions;

	@In(create = true, required = false)
	protected FavorisRechercheActionsBean				favorisRechercheActions;

	@In(create = true, required = false)
	protected DateDynamicActionBean						dynamicDate;

	@In(create = true, required = false)
	protected RechercheArchivageActionsBean				rechercheArchivageActions;

	@In(create = true, required = false)
	protected TableauDynamiqueActionsBean				tableauDynamiqueActions;

	private static final Log							LOGGER							= LogFactory
																								.getLog(RequeteurDossierSimpleActionsBean.class);

	private String										contentViewName					= StringUtils.EMPTY;

	@In(create = true, required = true)
	protected transient EPGAlertActionsBean				alertActions;

	@In(create = true, required = true)
	protected transient RequeteurActionsBean			requeteurActions;

	@In(create = true, required = true)
	protected DocumentActions							documentActions;

	@In(create = true, required = true)
	protected transient RequeteurDossierLibreActionsBean	requeteurDossierLibreActions;

	@In(create = true, required = false)
	protected transient UserWorkspaceManagerActionsBean	userWorkspaceManagerActions;

	/**
	 * Nom du répertoire contenant les requetes expertes de l'utilisateur.
	 */
	private static final String							MESREQUETES_PATH				= "/mesrequetes/";

	/**
	 * Type de Document des Requêtes expertes stocké pour les alertes.
	 */
	public static final String							SMART_FOLDER					= "SmartFolder";

	/**
	 * Requête obtenu dans la recherche simple lorsque aucun critère sélection n'a été choisi : utilisé pour éditer une
	 * alerte
	 */
	public static final String							SIMPLE_EMPTY_REQUEST			= "ufnxql:SELECT DISTINCT d.ecm:uuid AS id FROM Dossier AS d WHERE testAcl(d.ecm:uuid) = 1";

	/**
	 * Le favoris de recherche sélectionné
	 */
	private DocumentModel								favoriRechercheDoc;

	/**
	 * La requête a exécuter : stockée dans le bean pour des raisons de performance.
	 */
	protected String									currentRequetePattern			= null;

	@PostConstruct
	public void initialize() {
		this.setContentViewName(RechercheConstants.REQUETEUR_DOSSIER_CONTENT_VIEW);
	}

	@Factory(value = "recherches")
	public List<Recherche> getRecherches() throws Exception {
		RequeteurDossierSimpleService requeteurDossierService = SolonEpgServiceLocator
				.getRequeteurDossierSimpleService();
		return requeteurDossierService.getRecherches();
	}

	public Recherche getRecherche(String rechercheName) throws Exception {
		RequeteurDossierSimpleService requeteurDossierService = SolonEpgServiceLocator
				.getRequeteurDossierSimpleService();
		Recherche recherche = requeteurDossierService.getRecherche(rechercheName);
		return recherche;
	}

	public String goToResults() throws ClientException {
		// Renseigne la vue de route des étapes de feuille de route vers les dossiers
		routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.REQUETEUR_DOSSIER_SIMPLE_RESULTATS);
		navigationContext.resetCurrentDocument();

		// reset l'ancienne requête
		currentRequetePattern = null;
		RequeteDossierSimple requete = currentRequete.getAdapter(RequeteDossierSimple.class);

		// Collecte les choix de l'utilisateur concernant les bases à utiliser
		RequeteDossier requeteDossier = requete.getDocument().getAdapter(RequeteDossier.class);
		requeteDossier.setDansBaseArchivageIntermediaire(rechercheArchivageActions.getDansBaseArchivageIntermediaire());
		requeteDossier.setDansBaseProduction(rechercheArchivageActions.getDansBaseProduction());

		if (!requeteDossier.hasBasesSelected()) {
			facesMessages.add(StatusMessage.Severity.ERROR,
					resourcesAccessor.getMessages().get("requeteurDossierSimple.feedback.nobasesselected"));
			return null;
		}

		if (!checkFullTextWithMainCriteria(requete)) {
			facesMessages.add(StatusMessage.Severity.ERROR,
					resourcesAccessor.getMessages().get("requeteurDossierSimple.feedback.nomaincriteriawithfulltext"));
			return null;
		}

		requete.init();

		// on vérifie que la requête est valide
		if (beginWithStarQuery()) {
			return null;
		}

		return SolonEpgViewConstant.REQUETEUR_DOSSIER_SIMPLE_RESULTATS;
	}

	private boolean checkFullTextWithMainCriteria(RequeteDossierSimple requete) {
		if (StringUtil.isBlank(requete.getTexteIntegral())) {
			return true;
		}

		if ((!requete.getIdStatutDossier().isEmpty()
				|| StringUtil.isNotBlank(requete.getNumeroNor())
				|| StringUtil.isNotBlank(requete.getTitreActe()) || StringUtil.isNotBlank(requete.getIdAuteur())
				|| !requete.getIdMinistereResponsable().isEmpty()
				|| StringUtil.isNotBlank(requete.getIdDirectionResponsable())
				|| !requete.getIdTypeActe().isEmpty())
				&& !requete.getFiletypeIds().isEmpty()) {
			return true;
		}
		return false;
	}

	public String reset() {
		currentRequetePattern = null;
		currentRequete.reset();
		rechercheArchivageActions.init();
		dynamicDate.clearAll();
		RequeteDossierSimple requete = currentRequete.getAdapter(RequeteDossierSimple.class);
		requete.init();
		return null;
	}

	/**
	 * Renseigne le document du favori de recherche.
	 * 
	 * @param favoriRechercheDoc
	 *            Document du favori de recherche
	 */
	public void setFavoriRechercheDoc(DocumentModel favoriRechercheDoc) {
		this.favoriRechercheDoc = favoriRechercheDoc;
	}

	public FavorisRecherche getFavoriRechercheDoc() {
		return favoriRechercheDoc.getAdapter(FavorisRecherche.class);
	}

	/**
	 * Retourne la chaine de caractère représentant la requête.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public String getCurrentRequetePattern() throws ClientException {
		final RequeteurDossierSimpleService requeteurService = SolonEpgServiceLocator
				.getRequeteurDossierSimpleService();
		RequeteDossierSimple requete = currentRequete.getAdapter(RequeteDossierSimple.class);
		// Collecte les choix de l'utilisateur concernant les bases à utiliser
		RequeteDossier requeteDossier = requete.getDocument().getAdapter(RequeteDossier.class);
		requeteDossier.setDansBaseArchivageIntermediaire(rechercheArchivageActions.getDansBaseArchivageIntermediaire());
		requeteDossier.setDansBaseProduction(rechercheArchivageActions.getDansBaseProduction());
		currentRequetePattern = requeteurService.getFullQuery(requete);
		// la recherche NOR ne doit pas commencer par '*' : on ne veut pas que l'utilisateur puisse rechercher tous les
		// dossiers.
		if (FullTextUtil.beginWithStarQuery(currentRequetePattern)) {
			LOGGER.debug("Query before star error :" + currentRequetePattern);
			currentRequetePattern = DossierSolonEpgConstants.QUERY_BEGIN_STAR_ERROR;
		}
		return currentRequetePattern;
	}

	@Factory(value = "currentRequete")
	public DocumentModel getCurrentRequete() throws ClientException {
		final RequeteurDossierSimpleService requeteur = SolonEpgServiceLocator.getRequeteurDossierSimpleService();
		RequeteDossierSimple requete = requeteur.getRequete(documentManager);
		currentRequete = requete.getDocument();
		return currentRequete;
	}

	public String navigateToAddFavoris() {
		try {
			if (beginWithStarQuery()) {
				return null;
			}
			rechercheWebActions.setSelectedTypeSearch(TypeSearch.DOSSIER_SIMPLE);
			routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER);
			return favorisRechercheActions.navigateToAddFavoris();
		} catch (ClientException e) {
			LOGGER.error(
					"La naviguation vers l'ajout de favoris à partir de la recherche de dossier simple a rencontré une erreur",
					e);
			facesMessages.add(StatusMessage.Severity.ERROR, "Impossible d'accéder à l'ajout de favoris");
			return null;
		}
	}

	public String navigateToAddTableauDynamique() {
		try {
			if (beginWithStarQuery()) {
				return null;
			} else if (StringUtils.isEmpty(getCurrentRequetePattern())
					|| SIMPLE_EMPTY_REQUEST.equals(getCurrentRequetePattern())) {
				facesMessages.add(StatusMessage.Severity.WARN,
						resourcesAccessor.getMessages().get("error.generation_tableau.query.empty"));
				return null;
			}
			rechercheWebActions.setSelectedTypeSearch(TypeSearch.DOSSIER_SIMPLE);
			routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.ESPACE_RECHERCHE_DOSSIER);
			return tableauDynamiqueActions.navigateToAddTableauDynamique();
		} catch (ClientException e) {
			LOGGER.error(
					"La naviguation vers l'ajout de tableau dynamique à partir de la recherche de dossier simple a rencontré une erreur",
					e);
			facesMessages.add(StatusMessage.Severity.ERROR, "Impossible d'accéder à l'ajout de tableau dynamique");
			return null;
		}
	}

	public String generateAlert() {
		try {
			// la requête doit avoir au moins un critère de recherche ou avoir un critère de recherche qui commence par
			// "*".
			if (beginWithStarQuery()) {
				return null;
			} else if (StringUtils.isEmpty(getCurrentRequetePattern())
					|| SIMPLE_EMPTY_REQUEST.equals(getCurrentRequetePattern())) {
				facesMessages.add(StatusMessage.Severity.WARN,
						resourcesAccessor.getMessages().get("error.generation_alert.query.empty"));
				return null;
			}
			RequeteExperte requeteExperte = saveQueryAsRequeteExperte(getCurrentRequetePattern());
			if(requeteExperte == null) {
				facesMessages.add(StatusMessage.Severity.WARN,
						resourcesAccessor.getMessages().get("error.generation_alert.query.empty"));
				return null;
			}
			return alertActions.createAlertFromRequete(requeteExperte.getDocument());
		} catch (ClientException e) {
			LOGGER.error("Erreur lors de la génération de l'alerte", e);
			facesMessages.add(StatusMessage.Severity.ERROR,
					resourcesAccessor.getMessages().get("error.generation_alert.failed"));
			return null;
		}
	}

	/**
	 * Va vers la racine des requêtes utilisateurs.
	 * 
	 * @throws ClientException
	 */
	public void navigateToUserRequeteRoot() throws ClientException {
		userWorkspaceManagerActions.navigateToCurrentUserPersonalWorkspace();
		String requeteRootPath = userWorkspaceManagerActions.getCurrentUserPersonalWorkspace().getPathAsString()
				+ MESREQUETES_PATH;
		navigationContext.navigateToRef(new PathRef(requeteRootPath));
	}

	private RequeteExperte saveQueryAsRequeteExperte(String currentRequetePattern) {
		try {
			// récupération du chemin du répertoire parent
			navigateToUserRequeteRoot();
			DocumentModel doc = documentManager.createDocumentModel(SMART_FOLDER);
			RequeteExperte requeteExperte = doc.getAdapter(RequeteExperte.class);
			String queryPart = currentRequetePattern;
			requeteExperte.setWhereClause(queryPart);
			navigationContext.setChangeableDocument(doc);
			// création du document
			documentActions.saveDocument();
			// récupération du document
			doc = navigationContext.getChangeableDocument();
			return doc.getAdapter(RequeteExperte.class);
		} catch (ClientException e) {
			LOGGER.error("La sauvegarde de la requête courante a echoué", e);
			facesMessages.add(StatusMessage.Severity.ERROR, "La sauvegarde de la requête a echoué");
			return null;
		}
	}

	/**
	 * Signale si la requête experte contient un champ complexe (numéro NOR) commençant par le caractère '*'
	 * 
	 * @return
	 * @throws ClientException
	 */
	protected boolean beginWithStarQuery() throws ClientException {
		getCurrentRequetePattern();
		// TODO voir si besoin de traiter le cas où la requête est vide
		if (currentRequetePattern != null
				&& currentRequetePattern.equals(DossierSolonEpgConstants.QUERY_BEGIN_STAR_ERROR)) {
			// si la recherche NOR commence par le caractère '*', on envoie un avertissement à l'utilisateur.
			facesMessages.add(StatusMessage.Severity.WARN, resourcesAccessor.getMessages().get("feedback.search.star"));
			return true;
		}
		return false;
	}

	public void setContentViewName(String contentViewName) {
		this.contentViewName = contentViewName;
	}

	public String getContentViewName() {
		return contentViewName;
	}

	public String searchWithFavori() {
		return SolonEpgViewConstant.REQUETEUR_DOSSIER_SIMPLE_FAVORIS_RESULTATS;
	}

	/**
	 * Valide le champ de texte intégral. Si le contenu de la recherche dépasse les 256 bytes, elle provoque une
	 * exception Oracle.
	 * 
	 * @param facesContext
	 * @param uIComponent
	 * @param object
	 * @throws ValidatorException
	 */
	public void validateTextIntegral(FacesContext facesContext, UIComponent uIComponent, Object object)
			throws ValidatorException {
		String inputTextIntegral = (String) object;

		int length = inputTextIntegral.getBytes().length;
		if (length > MAX_RECHERCHE_TEXTUELLE_SIZE) {
			FacesMessage message = new FacesMessage();
			message.setSummary("Votre recherche comporte trop de lettres, veuillez la reformuler.");
			throw new ValidatorException(message);
		}
	}
}
