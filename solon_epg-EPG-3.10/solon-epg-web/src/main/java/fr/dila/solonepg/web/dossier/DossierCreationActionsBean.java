package fr.dila.solonepg.web.dossier;

import static org.jboss.seam.annotations.Install.FRAMEWORK;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.nuxeo.ecm.platform.contentview.seam.ContentViewActions;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.ui.web.directory.VocabularyEntry;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.cm.web.mailbox.CaseManagementMailboxActionsBean;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgANEventConstants;
import fr.dila.solonepg.api.constant.SolonEpgContentViewConstant;
import fr.dila.solonepg.api.constant.TypeActe;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.CorbeilleService;
import fr.dila.solonepg.api.service.DossierBordereauService;
import fr.dila.solonepg.api.service.DossierDistributionService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.HistoriqueMajMinisterielleService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.api.service.TranspositionDirectiveService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.espace.CorbeilleActionsBean;
import fr.dila.solonepg.web.refresh.RefreshActionsBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.api.exception.LocalizedClientException;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.NorDirection;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.MailboxService;
import fr.dila.st.core.domain.ComplexeTypeImpl;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.web.context.NavigationContextBean;

@Name("dossierCreationActions")
@Scope(ScopeType.CONVERSATION)
@Install(precedence = FRAMEWORK)
public class DossierCreationActionsBean implements Serializable {
	private static final long									serialVersionUID		= 1L;

	@In(create = true, required = true)
	protected transient CoreSession								documentManager;

	@In(required = true, create = true)
	protected SSPrincipal										ssPrincipal;

	@In(create = true, required = false)
	protected transient NavigationContextBean					navigationContext;

	@In(create = true, required = false)
	protected transient FacesMessages							facesMessages;

	@In(create = true, required = false)
	protected transient CorbeilleActionsBean					corbeilleActions;

	@In(create = true)
	protected transient ResourcesAccessor						resourcesAccessor;

	@In(create = true, required = false)
	protected transient CaseManagementMailboxActionsBean		cmMailboxActions;

	@In(create = true, required = false)
	protected transient ContentViewActions						contentViewActions;

	@In(create = true, required = false)
	protected transient RefreshActionsBean						refreshActions;

	private static final Log									log						= LogFactory
																								.getLog(DossierCreationActionsBean.class);

	/**
	 * Vues des étapes
	 */
	private static final String									VIEW_ESPACE_CREATION	= "view_espace_creation";
	private static final String									VIEW_CREATION_INITIALE	= "view_creation_dossier_100";

	private static final String									VIEW_DECRET				= "view_creation_dossier_101";

	private static final String									VIEW_ARRETE				= "view_creation_dossier_102";

	/**
	 * Vue pour la transposition des directives.
	 */
	private static final String									VIEW_TRANSPO_DIRECTIVE	= "view_creation_dossier_103";

	/**
	 * Vue pour l'application des lois.
	 */
	private static final String									VIEW_APPLI_LOI			= "view_creation_dossier_104";

	/**
	 * Vue pour l'application des ordonnances.
	 */
	private static final String									VIEW_TRANSPO_ORDONNANCE	= "view_creation_dossier_105";

	private static final String									VIEW_ORDONNANCE			= "view_creation_dossier_106";

	/**
	 * Vue pour les dispositions d'habilitations.
	 */
	private static final String									VIEW_HABILITATION38C	= "view_creation_dossier_107";
	private static final String									VIEW_LOI				= "view_creation_dossier_108";
	private static final String									VIEW_RECTIFICATIF		= "view_creation_dossier_109";
	protected static final String								VIEW_SELECT_POSTE		= "view_creation_dossier_select_poste";

	/**
	 * Vue courante
	 */
	private String												currentView;

	/**
	 * Données sur le dossier en cours de création
	 */

	/**
	 * Map des données des champs du widget en cours
	 */
	private Map<String, String>									widgetMap;

	/**
	 * Liste des données du widget en cours
	 */
	private List<List<DossierCreationWidgetItem>>				widgetList;

	/**
	 * Map des données de tous les widgets
	 */
	private Map<String, List<List<DossierCreationWidgetItem>>>	widgetsMap;

	/**
	 * Service contenant les expressions règulières à vérifier pour les champs références et numéro d'ordre
	 */
	private DossierBordereauService								dossierBordereauService;
	/**
	 * Etape ECR_SOL_DOS_100 (Création d'un dossier)
	 */
	private String												typeActe;
	private String												typeActeLabel;
	private String												ministereResp;
	private String												directionResp;
	/** Contient la lettre de NOR associée à la direction sélectionnée. */ 
	private String												currentNor;
	private String												norDossierCopieFdr;

	/**
	 * Etapes intermédiaires
	 */

	/**
	 * nature de l'acte : réglementaire ou non réglementaire.
	 */
	private String												nature;

	/**
	 * Indique si le dossier peut contenir l'un de ces type de champ
	 */
	private boolean												transpositionDirective;
	private boolean												applicationLoi;
	private boolean												transpositionOrdonnance;
	private boolean												dispositionHabilitation;

	/**
	 * Champs sur l'écran Disposition d'habilitation (107)
	 */
	private String												refLoi;
	private String												numArticle;
	private String												commentaire;
	private String												numeroOrdre;

	/**
	 * Etape ECR_SOL_DOS_109 (Rectificatif)
	 */
	private String												numeroNor;

	/**
	 * Etape ECR_SOL_DOS_109 (Rectificatif) : message d'erreur affiché si le numéro NOR n'est pas valide ou si le
	 * dossier sélectionné n'est pas
	 */
	private String												erreurNumeroNor;
	/**
	 * Choix manuel du poste pour la première étape de la feuille de route.
	 */
	private String												posteId;

	/**
	 * ECR_SOL_DOS_100 : Liste des clé identifiants-nor des directions "Premier ministre" accessible à l'utilisateur.
	 */
	private List<VocabularyEntry>								norPrmList;

	/**
	 * ECR_SOL_DOS_100 : id de la direction "Premier Ministre" sélectionnée : utilisé pour l'affichage du norPrm dans le
	 * menu déroulant
	 */
	private String												norPrm;

	/**
	 * ECR_SOL_DOS_100 : id de la direction "Premier Ministre" sélectionnée : utilisé pour stocker le norPrm dans le
	 * bean (n'est pas remis à nulle lorsque on recharge la page suite à une erreur de validation)
	 */
	private String												currentNorPrm;

	/**
	 * ECR_SOL_DOS_100 : Boolean qui définit si l'utilisateur choisit une direction Ministère ou une direction Premier
	 * Ministère. Par défaut, on prend en compte la direction Ministère
	 */
	private String												choixDirectionMinistere	= "true";

	/**
	 * ecran selection du poste : Boolean qui définit si l'utilisateur choisit un poste dans l'organigramme ou bien un
	 * de ceux définis dans son profil utilisateur
	 */
	private String												choixPoste				= "true";

	/**
	 * ECR_SOL_DOS_100 : boolean qui définit si l'on affiche les erreurs de validation.
	 */
	private boolean												displayValidationErreur	= false;

	private Long												resultCount;

	public String navigateTo(Boolean fullReset) throws ClientException {
		if (fullReset) {
			fullReset();
		}
		return navigateTo();
	}

	/**
	 * Methode permettant de nettoyer les beans fils
	 */
	protected void fullReset() {
		// par defaut fait rien
	}

	public String navigateTo() throws ClientException {
		reset();
		return VIEW_CREATION_INITIALE;
	}

	/**
	 * Message d'erreur concernant les champs de transposition et d'application
	 */

	private static final String	MSG_ERROR_VALIDATION_REF_LOI					= "epg.dossierCreation.loi.ref.non.valide";

	private static final String	MSG_ERROR_EXCEPTION_VALIDATION_REF_LOI			= "epg.dossierCreation.loi.ref.non.valide.exception";

	private static final String	MSG_ERROR_VALIDATION_NUMERO_ORDRE				= "epg.dossierCreation.numero.ordre.non.valide";

	private static final String	MSG_ERROR_EXCEPTION_VALIDATION_NUMERO_ORDRE		= "epg.dossierCreation.numero.ordre.non.valide.exception";

	private static final String	MSG_ERROR_VALIDATION_REF_ORDONNANCE				= "epg.dossierCreation.ordonnance.ref.non.valide";

	private static final String	MSG_ERROR_EXCEPTION_VALIDATION_REF_ORDONNANCE	= "epg.dossierCreation.ordonnance.ref.non.valide.exception";

	/**
	 * Passage à l'étape précédente de l'assistant
	 * 
	 * @return Vue précédente
	 * @throws ClientException
	 */
	public String goPrecedent() throws ClientException {

		String precedent = null;
		// on reset l'affichage des erreurs de validation
		displayValidationErreur = false;

		if (currentView.equals(VIEW_TRANSPO_DIRECTIVE)) { // > transposition d'une directive
			saveWidgetList(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY);
			precedent = goSuivant100();
		} else if (currentView.equals(VIEW_APPLI_LOI)) { // > application d'une loi
			saveWidgetList(DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY);
			if (transpositionDirective) {
				restoreWidgetList(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY);
				precedent = setCurrentView(VIEW_TRANSPO_DIRECTIVE);
			} else {
				precedent = goSuivant100();
			}
		} else if (currentView.equals(VIEW_TRANSPO_ORDONNANCE)) { // > application d'une ordonnance
			saveWidgetList(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY);
			if (applicationLoi) {
				restoreWidgetList(DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY);
				precedent = setCurrentView(VIEW_APPLI_LOI);
			} else if (transpositionDirective) {
				restoreWidgetList(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY);
				precedent = setCurrentView(VIEW_TRANSPO_DIRECTIVE);
			}
			precedent = goSuivant100();
		} else if (currentView.equals(VIEW_HABILITATION38C)) { // > disposition d'habilitation
			if (transpositionDirective) {
				restoreWidgetList(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY);
				precedent = setCurrentView(VIEW_TRANSPO_DIRECTIVE);
			} else {
				precedent = setCurrentView(VIEW_ORDONNANCE);
			}
		} else {
			precedent = setCurrentView(VIEW_CREATION_INITIALE);
		}
		return precedent;

	}

	/**
	 * Passage à l'étape suivante de l'assistant
	 * 
	 * @return Vue suivante
	 * @throws ClientException
	 */
	public String goSuivant() throws Exception {
		String suivant;
		if (currentView.equals(VIEW_CREATION_INITIALE)) { // départ
			suivant = goSuivant100();
		} else if (currentView.equals(VIEW_DECRET)) { // création d'un décret
			suivant = goSuivant101();
		} else if (currentView.equals(VIEW_ARRETE)) { // création d'un arrêté
			suivant = goSuivant102();
		} else if (currentView.equals(VIEW_TRANSPO_DIRECTIVE)) { // transposition d'une directive
			suivant = goSuivant103();
		} else if (currentView.equals(VIEW_APPLI_LOI)) { // application d'une loi
			suivant = goSuivant104();
		} else if (currentView.equals(VIEW_TRANSPO_ORDONNANCE)) { // transposition d'une ordonnance
			suivant = goSuivant105();
		} else if (currentView.equals(VIEW_ORDONNANCE)) { // création d'une ordonnance
			suivant = goSuivant106();
		} else if (currentView.equals(VIEW_LOI)) { // création d'un loi
			suivant = goSuivant108();
		} else if (currentView.equals(VIEW_RECTIFICATIF)) { // création d'un rectificatif
			suivant = setCurrentView(VIEW_RECTIFICATIF);
		} else {
			suivant = setCurrentView(VIEW_CREATION_INITIALE);
		}
		return suivant;
	}

	/**
	 * Crée le dossier avec les données enregistrées au cours de l'assistant
	 * 
	 * @return Vue de l'espace création
	 * @throws Exception
	 */
	public String goTerminer() throws Exception {

		// on vérifie que tous les champs requis sont remplis
		if (!isRequiredFieldsFilled()) {
			return null;
		}

		displayValidationErreur = false;

		// Sauvegarde du dernier widget
		if (currentView.equals(VIEW_TRANSPO_DIRECTIVE)) {
			saveWidgetList(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY);
		}
		if (currentView.equals(VIEW_APPLI_LOI)) {
			saveWidgetList(DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY);
		}
		if (currentView.equals(VIEW_TRANSPO_ORDONNANCE)) {
			saveWidgetList(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY);
		}

		// Enregistrement du dossier

		final DossierDistributionService dossierDistributionService = SolonEpgServiceLocator
				.getDossierDistributionService();
		final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
		final NORService norService = SolonEpgServiceLocator.getNORService();

		// Récupération des listes de transposition
		List<ComplexeType> applicationLoiList = new ArrayList<ComplexeType>();
		if (applicationLoi) {
			applicationLoiList = getTranspositionList(DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY);
		}
		List<ComplexeType> transpositionDirectiveList = new ArrayList<ComplexeType>();

		if (transpositionDirective) {
			transpositionDirectiveList = getTranspositionList(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY);
		}
		List<ComplexeType> transpositionOrdonnanceList = new ArrayList<ComplexeType>();
		if (transpositionOrdonnance) {
			transpositionOrdonnanceList = getTranspositionList(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY);
		}

		DocumentModel oldDossierModel = navigationContext.getCurrentDocument();
		Boolean estRectificatif = oldDossierModel != null && typeActe.equals(TypeActeConstants.TYPE_ACTE_RECTIFICATIF);

		// données du dossier à remplir
		String ministereRespFinal = null;
		String directionRespFinal = null;
		Dossier oldDossier = null;
		String norMinistere = null;
		String norDirection = null;
		String numeroNorDossier = null;

		// récupération du ministère et de la direction
		if (estRectificatif) {
			// on récupère l'ancien dossier
			oldDossier = oldDossierModel.getAdapter(Dossier.class);

			// on définit le ministère et la direction responsable à partir du dossier
			ministereRespFinal = oldDossier.getMinistereResp();
			directionRespFinal = oldDossier.getDirectionResp();

			// on définit les nors des ministeres et direction
			numeroNorDossier = oldDossier.getNumeroNor();
			norMinistere = numeroNorDossier.substring(0, 3);
			norDirection = numeroNorDossier.substring(3, 4);
		} else {

			if (choixDirectionMinistere == null || choixDirectionMinistere.equals("true")) {

				// on récupère le ministere et la direction sélectionnée
				ministereRespFinal = ministereResp;
				directionRespFinal = directionResp;
			} else {

				// on récupère le ministere et la direction à partir du NOR ministère
				TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
				ministereRespFinal = tableReferenceService.getMinisterePrm(documentManager);
				directionRespFinal = getNorPrm();
			}

			// Détermine les lettres de NOR du ministère
			EntiteNode ministereNode = STServiceLocator.getSTMinisteresService().getEntiteNode(ministereRespFinal);
			norMinistere = ministereNode.getNorMinistere();
			UniteStructurelleNode directionNode = STServiceLocator.getSTUsAndDirectionService()
					.getUniteStructurelleNode(directionRespFinal);
			updateCurrentNorFromDirectionAndMinistere(ministereNode, directionNode);
			
			if (currentNor == null || currentNor.isEmpty()) {
				// Détermine les lettres de NOR de la direction
				norDirection = directionNode.getNorDirectionForMinistereId(ministereRespFinal);
				if ("".equals(norDirection)  && directionNode.getUniteStructurelleParentList().size() == 1
						&& OrganigrammeType.DIRECTION.equals(directionNode.getUniteStructurelleParentList().get(0).getType())) {
					// On a une direction sous une direction et on n'a pas réussi à trouver le NOR
					// On prend donc le NOR de la direction parente
					norDirection = directionNode.getUniteStructurelleParentList().get(0)
							.getNorDirectionForMinistereId(ministereRespFinal);
				} else {
					norDirection = currentNor;
				}
			} else {
				norDirection = currentNor;
			}
		}

		// Si un poste est sélectionné manuellement, on affecte ce poste
		if (StringUtils.isEmpty(posteId)) {
			// Tente d'affecter le poste automatiquement
			final Set<String> posteIdSet = ssPrincipal.getPosteIdSet();
			posteId = dossierDistributionService.getPosteForNor(posteIdSet, norMinistere, norDirection);
			if (StringUtils.isEmpty(posteId)) {
				// Le poste ne peut pas être affecté automatiquement, affiche l'écran de choix du poste
				return setCurrentView(VIEW_SELECT_POSTE);
			}
		}

		Dossier dossier = null;
		// Crée le dossier
		try {

			if (estRectificatif) {
				numeroNorDossier = norService.createRectificatifNOR(oldDossier);
			} else {
				// Détermine les lettres de NOR du type d'acte
				final ActeService acteService = SolonEpgServiceLocator.getActeService();
				TypeActe acte = acteService.getActe(typeActe);
				String norActe = acte.getNor();
				// si le type d'acte est de type individuel, l'acte est non réglementaire
				if (acteService.isNonReglementaire(typeActe)) {
					nature = VocabularyConstants.NATURE_NON_REGLEMENTAIRE;
				}

				boolean hasError = Boolean.FALSE;

				// Vérification des arguments
				if (norActe == null || norActe.length() != 1) {
					facesMessages.add(StatusMessage.Severity.INFO, "Lettres de NOR acte invalides");
					hasError = Boolean.TRUE;
				}
				if (norMinistere == null || norMinistere.length() != 3) {
					facesMessages.add(StatusMessage.Severity.INFO, "Lettres de NOR ministère invalides");
					hasError = Boolean.TRUE;
				}
				if (norDirection == null || norDirection.length() != 1) {
					facesMessages.add(StatusMessage.Severity.INFO, "Lettres de NOR direction invalides");
					hasError = Boolean.TRUE;
				}

				if (hasError) {
					return null;
				}

				// Attribution du NOR
				numeroNorDossier = norService.createNOR(norActe, norMinistere, norDirection);
			}

			// Création du dossier
			DocumentModel dossierDoc = documentManager
					.createDocumentModel(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
			DublincoreSchemaUtils.setTitle(dossierDoc, numeroNorDossier);
			dossier = dossierDoc.getAdapter(Dossier.class);

			// Ajout des informations de création information du dossier
			dossier.setNumeroNor(numeroNorDossier);
			dossier.setTypeActe(typeActe);
			dossier.setMinistereResp(ministereRespFinal);
			dossier.setDirectionResp(directionRespFinal);
			if (!estRectificatif) {
				// Ajout des informations de création
				dossier.setCategorieActe(nature);
				dossier.setApplicationLoi(applicationLoiList);
				dossier.setTranspositionDirective(transpositionDirectiveList);
				dossier.setTranspositionOrdonnance(transpositionOrdonnanceList);
				dossier.setDispositionHabilitation(dispositionHabilitation);
				dossier.setHabilitationRefLoi(refLoi);
				dossier.setHabilitationNumeroArticles(numArticle);
				dossier.setHabilitationCommentaire(commentaire);
				dossier.setHabilitationNumeroOrdre(numeroOrdre);

			}

			// Create dossier à lancer avant la maj du pan pour avoir l'id du dossier
			dossier = createDossier(dossierService, dossier, norDossierCopieFdr);

			// on regarde si les modifications ont eu lieu sur les applications des loi, les transpositions ou les
			// ordonances
			// afin de mettre à jour l'historique des maj ministérielles
			final HistoriqueMajMinisterielleService historiqueMajService = SolonEpgServiceLocator
					.getHistoriqueMajMinisterielleService();
			historiqueMajService.registerMajDossier(documentManager, dossier.getDocument());

			if (estRectificatif) {
				// met à jour le nombre de rectification du dossier d'origine
				if (oldDossier.getNbDossierRectifie() == null) {
					oldDossier.setNbDossierRectifie(1L);
				} else {
					oldDossier.setNbDossierRectifie(oldDossier.getNbDossierRectifie() + 1);
				}
				oldDossier.save(documentManager);
			}

		} catch (LocalizedClientException e) {
			String message = resourcesAccessor.getMessages().get(e.getMessage());
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return VIEW_ESPACE_CREATION;
		}

		// Event de rattachement de l'activite normative (post commit)
		EventProducer eventProducer = STServiceLocator.getEventProducer();
		Map<String, Serializable> eventProperties = new HashMap<String, Serializable>();
		eventProperties.put(SolonEpgANEventConstants.ACTIVITE_NORMATIVE_ATTACH_DOSSIER_DOCID_PARAM, dossier
				.getDocument().getId());
		InlineEventContext inlineEventContext = new InlineEventContext(documentManager, documentManager.getPrincipal(),
				eventProperties);
		eventProducer.fireEvent(inlineEventContext.newEvent(SolonEpgANEventConstants.ACTIVITE_NORMATIVE_ATTACH_EVENT));

		log.info("Création d'un dossier de type : " + typeActe);
		// évenement spécifique à l'affichge du dossier dans l'application

		// Affichage du message de création
		String message = null;
		if (StringUtil.isNotEmpty(norDossierCopieFdr)) {
			message = resourcesAccessor.getMessages().get("feedback.solonepg.dossier.creation.from.fdrDossier");
			facesMessages.add(StatusMessage.Severity.INFO, MessageFormat.format(message, dossier.getNumeroNor(), norDossierCopieFdr));
		} else {
			message = resourcesAccessor.getMessages().get("feedback.solonepg.dossier.creation");
			facesMessages.add(StatusMessage.Severity.INFO, MessageFormat.format(message, dossier.getNumeroNor()));
		}

		// Charge le dossier s'il est affecté à un poste de l'utilisateur connecté
		final CorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
		DocumentModel dossierLinkDoc = corbeilleService.getCaseLinkPourInitialisation(documentManager,
				dossier.getDocument());
		if (dossierLinkDoc != null) {
			corbeilleActions.setCurrentDossierLink(dossierLinkDoc.getAdapter(DossierLink.class));
			navigationContext.setCurrentDocument(dossier.getDocument());
		} else {
			navigationContext.resetCurrentDocument();
		}
		reset();

		resultCount = null;
		norDossierCopieFdr = null;

		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
		// Retour à l'espace de création
		return VIEW_ESPACE_CREATION;
	}
	
	/**
	 * Cas particulier pour résoudre le ticket M155774.<br/>
	 * L'objectif est de s'assurer que seuls les admins fonctionnels puissent
	 * sélectionner une entité et une direction incompatibles via leur première
	 * lettre. Dans tous les cas, on s'assure de travailler avec les bonnes
	 * valeurs de currentNor, entite, direction.<br/>
	 * currentNor est d'abord réinitialisé à NULL.<br/>
	 * Si le ministère et/ou la direction n'ont pas été renseignés, on ne met
	 * pas currentNor à jour.<br/>
	 * Sinon :
	 * <ul>
	 * <li>S'il y a correspondance entité-direction, on met currentNor à
	 * jour.</li>
	 * <li>Sinon, si l'utilisateur est admin fonctionnel et s'il n'y a pas
	 * d'ambiguité sur la direction (ie. elle n'est liée qu'à une entité) aors
	 * on met currentNor à jour.</li>
	 * </ul>
	 */
	private void updateCurrentNorFromDirectionAndMinistere(EntiteNode ministereNode, UniteStructurelleNode directionNode) {
		currentNor = null;
		if (ministereNode != null && directionNode != null) {
			String norDirection = directionNode.getNorDirectionForMinistereId(ministereNode.getId());
			if (StringUtil.isNotEmpty(norDirection)) {
				// Il y a correspondance entre l'entité et le ministère !
				currentNor = norDirection;
			} else if (ssPrincipal.isMemberOf(STBaseFunctionConstant.ADMIN_FONCTIONNEL_GROUP_NAME)) {
				List<NorDirection> norDirections = directionNode.getNorDirectionList();
				if (norDirections.size() == 1) {
					currentNor = norDirections.get(0).getNor();
				}
			}
		}
	}
	
	protected Dossier createDossier(final DossierService dossierService, Dossier dossier, String norDossierCopieFdr) throws ClientException {
		MailboxService mailboxService = STServiceLocator.getMailboxService();
		Mailbox mailbox = mailboxService.getUserPersonalMailboxUFNXQL(documentManager, ssPrincipal.getName());
		return dossierService.createDossier(documentManager, dossier.getDocument(), posteId, mailbox, norDossierCopieFdr);
	}

	/**
	 * Verifie que tous les champs obligatoires sont remplis
	 * 
	 * return vrai si tous les champs obligatoires sont remplis
	 * @throws ClientException 
	 */
	protected boolean isRequiredFieldsFilled() throws ClientException {
		// Cas du rectificatif
		if (!StringUtils.isEmpty(typeActe) && typeActe.equals(TypeActeConstants.TYPE_ACTE_RECTIFICATIF)) {
			return true;
		}
		if (!norDossierCopieFdr.isEmpty() && !isNorFdrCopieValide()) {
			return false;
		}
		// on vérifie que tous les champs requis sont remplis : titre de l'acte + ministère et direction si l'option PRM
		// n'a pas été choisie
		if (typeActe == null
				|| typeActe.isEmpty()
				|| (choixDirectionMinistere == null || choixDirectionMinistere.equals("true"))
				&& (ministereResp == null || ministereResp.isEmpty() || directionResp == null || directionResp
						.isEmpty())) {
			// on signale que l'on doit afficher les erreurs de validation
			displayValidationErreur = true;
			return false;
		}
		return true;
	}
	
	/**
	 * Verifie que le nor saisie pour la copie de la fdr est valide
	 * 
	 * return vrai si le nor est valide
	 * @throws ClientException 
	 */
	protected boolean isNorFdrCopieValide() throws ClientException {
		NORService norService = SolonEpgServiceLocator.getNORService();
		if (norService.getDossierFromNORWithACL(documentManager, norDossierCopieFdr) != null) {
			return true;
		} else {
			final String message = resourcesAccessor.getMessages().get("epg.dossierCreation.copier.fdr.error.message");
			facesMessages.add(StatusMessage.Severity.ERROR, message);
			return false;
		}
	}


	/**
	 * Annule la création de dossier et retourne à l'espace de création
	 * 
	 * @return Vue de l'espace création
	 * @throws ClientException
	 */
	public String goAnnuler() throws ClientException {
		reset();
		return VIEW_ESPACE_CREATION;
	}

	/**
	 * Renvoie vrai si on affiche le bouton Suivant (utilisé pour les vues 100, 103, 104 et 105 de l'espace de creation)
	 * 
	 * @return
	 */
	public boolean displayGoSuivant() {
		boolean result = false;

		if (currentView.equals(VIEW_CREATION_INITIALE)) {

			final ActeService acteService = SolonEpgServiceLocator.getActeService();

			if (typeActe == null || typeActe.isEmpty()) {
				result = false;
			} else if (!acteService.isNonReglementaire(typeActe)
					&& (acteService.isDecret(typeActe) || acteService.isArrete(typeActe)
							|| acteService.isOrdonnance(typeActe) || acteService.isLoi(typeActe) || acteService
							.isRectificatif(typeActe))) {
				result = true;
			}
		} else if (currentView.equals(VIEW_TRANSPO_DIRECTIVE)) {
			if ((applicationLoi || transpositionOrdonnance || dispositionHabilitation)
					&& (widgetList != null && widgetList.size() > 0)) {
				// si l'on a ajouté au moins une directive, et si il reste encore des champs à remplir, on affiche le
				// bouton
				result = true;
			}
		} else if (currentView.equals(VIEW_APPLI_LOI)) {
			if (transpositionOrdonnance && (widgetList != null && widgetList.size() > 0)) {
				// si l'on a ajouté au moins une loi, et si il l'on doit aussi remplir les ordonnances, on affiche le
				// bouton
				result = true;
			}
		} else if (currentView.equals(VIEW_TRANSPO_ORDONNANCE)) {
			if (dispositionHabilitation && (widgetList != null && widgetList.size() > 0)) {

				// si l'on a ajouté au moins une ordonnance, et si il l'on doit aussi remplir l'habilitation, on affiche
				// le bouton
				result = true;
			}
		}
		return result;
	}

	/**
	 * Renvoie vrai si on affiche le bouton Précedent (utilisé pour les vues 100, 103, 104 et 105 de l'espace de
	 * creation)
	 * 
	 * @return
	 */
	public boolean displayGoPrecedent() {
		if (currentView.equals(VIEW_CREATION_INITIALE)) {
			return false;
		}
		return true;
	}

	/**
	 * Renvoie vrai si on cache tous les champs de création du dossier.
	 * 
	 * @return
	 */
	public boolean hideAllField() {
		if (typeActe == null || typeActe.isEmpty()) {
			return false;
		}
		final ActeService acteService = SolonEpgServiceLocator.getActeService();
		if (acteService.isRectificatif(typeActe)) {
			return true;
		}
		return false;
	}

	/**
	 * Renvoie vrai si on affiche le bouton Terminer (utilisé pour les vues 100, 103, 104 et 105 de l'espace de
	 * creation)
	 * 
	 * @return vrai si on affiche le bouton Terminer (utilisé pour les vues 100, 103, 104 et 105 de l'espace de
	 *         creation)
	 */
	public boolean displayGoTerminer() {
		if (currentView.equals(VIEW_CREATION_INITIALE)) {
			return !displayGoSuivant();
		} else if (currentView.equals(VIEW_TRANSPO_DIRECTIVE)) {
			if (!applicationLoi && !transpositionOrdonnance && !dispositionHabilitation
					&& (widgetList != null && widgetList.size() > 0)) {
				// si l'on a ajouté au moins une directive et si il ne reste aucun champs à remplir, on affiche le
				// bouton
				return true;
			}
		} else if (currentView.equals(VIEW_APPLI_LOI)) {
			if (!transpositionOrdonnance && (widgetList != null && widgetList.size() > 0)) {
				// si l'on a ajouté au moins une loi et si il ne reste aucun champs à remplir, on affiche le bouton
				return true;
			}
		} else if (currentView.equals(VIEW_TRANSPO_ORDONNANCE)) {
			if (!dispositionHabilitation && (widgetList != null && widgetList.size() > 0)) {
				// si l'on a ajouté au moins une ordonnance et si il ne reste aucun champs à remplir, on affiche le
				// bouton
				return true;
			}
		}
		return false;
	}

	/**
	 * Retourne vrai si l'on doit afficher une erreur de validation pour le champ
	 * 
	 * @param property
	 * @return
	 */
	public boolean displayFieldValidationMessage(String property, boolean isRequiredField) {

		boolean result = false;
		// on verifie que l'on doit afficher les messages de validations et que le champ est un champ obligatoire
		if (!displayValidationErreur || !isRequiredField) {
			result = false;
			// on regarde si le champ est vide
		} else if (property == null) {
			result = false;
		} else if ("typeActe".equals(property) && (typeActe == null || typeActe.isEmpty())) {
			result = true;
		} else if ("ministereResp".equals(property) && (ministereResp == null || ministereResp.isEmpty())) {
			result = true;
		} else if ("directionResp".equals(property) && (directionResp == null || directionResp.isEmpty())) {
			result = true;
		}
		return result;
	}

	/**
	 * Renvoie vrai si la vue de création est le vue du rectificatif (vue 109)
	 * 
	 * @return vrai si la vue de création est le vue du rectificatif (vue 109)
	 */
	public boolean isInViewRectificatif() {
		if (currentView != null && !currentView.isEmpty() && currentView.equals(VIEW_RECTIFICATIF)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Charge le Dossier en session
	 * 
	 * @param dossierDoc
	 * @param contentView
	 * @throws ClientException
	 */
	public String consulterDossier() throws ClientException {
		if (numeroNor == null || numeroNor.isEmpty()) {
			return VIEW_RECTIFICATIF;
		}
		// mise en forme : on enleve les espace et on met en majuscule le NOR saisi par l'utilisateur
		numeroNor = numeroNor.replaceAll(" ", "").toUpperCase();

		// on récupère le dossier dont le nor existe et dont le type d'acte n'est pas rectificatif, dont le statut est
		// publié
		NORService nORService = SolonEpgServiceLocator.getNORService();
		DocumentModel dossier = nORService.getDossierFromNOR(documentManager, numeroNor);

		boolean hasError = false;
		if (dossier == null) {
			erreurNumeroNor = "Numéro NOR '" + numeroNor
					+ "' non valide : il n'existe pas de dossier ayant ce numéro NOR !";
			hasError = true;
		} else if (!documentManager.hasPermission(dossier.getRef(), SecurityConstants.READ)) {
			erreurNumeroNor = "Vous n'avez pas les droits pour voir le dossier " + numeroNor + " !";
			hasError = true;
		} else {
			Dossier dossierDoc = dossier.getAdapter(Dossier.class);
			Long nbDossierRectifie = dossierDoc.getNbDossierRectifie();
			if (!dossierDoc.getStatut().equals(VocabularyConstants.STATUT_PUBLIE)) {
				erreurNumeroNor = "Le dossier '" + numeroNor + "' n'est pas à l'état publié !";
				hasError = true;
			} else if (dossierDoc.getTypeActe().equals(TypeActeConstants.TYPE_ACTE_RECTIFICATIF)) {
				erreurNumeroNor = "Le dossier '" + numeroNor + "' est de type Rectificatif !";
				hasError = true;
			} else if (nbDossierRectifie != null && nbDossierRectifie == 3L) {
				erreurNumeroNor = "Le dossier '" + numeroNor + "' ne peut pas avoir plus de 3 Rectificatifs !";
				hasError = true;
			} else if (nbDossierRectifie != null && nbDossierRectifie > 0L) {

				// le dernier rectificatif du dossier doit être publié
				Dossier rectificatifDoc = nORService.getRectificatifFromNORAndNumRect(documentManager, numeroNor,
						nbDossierRectifie);
				if (rectificatifDoc != null && !rectificatifDoc.getStatut().equals(VocabularyConstants.STATUT_PUBLIE)) {
					erreurNumeroNor = "Le dernier rectificatif du dossier  '" + numeroNor
							+ "' n'est pas à l'état publié !";
					hasError = true;
				}
			}
		}
		if (hasError) {
			numeroNor = null;
			navigationContext.resetCurrentDocument();
			return VIEW_RECTIFICATIF;
		}

		erreurNumeroNor = null;
		// Navigation vers le Dossier
		navigationContext.setCurrentDocument(dossier);

		// Retour à la vue courante
		return VIEW_RECTIFICATIF;
	}

	private String goSuivant100() throws ClientException {
		final ActeService acteService = SolonEpgServiceLocator.getActeService();

		String suivant;
		if (acteService.isRectificatif(typeActe)) {
			suivant = setCurrentView(VIEW_RECTIFICATIF);
		} else if (!isRequiredFieldsFilled()) {
			// on vérifie que tous le ministere et la direction ne sont pas nul
			suivant = null;
		} else if (acteService.isDecret(typeActe)) {
			suivant = setCurrentView(VIEW_DECRET);
		} else if (acteService.isArrete(typeActe)) {
			suivant = setCurrentView(VIEW_ARRETE);
		} else if (acteService.isOrdonnance(typeActe)) {
			dispositionHabilitation = true;
			suivant = setCurrentView(VIEW_ORDONNANCE);
		} else if (acteService.isLoi(typeActe)) {
			nature = VocabularyConstants.NATURE_NON_REGLEMENTAIRE;
			suivant = setCurrentView(VIEW_LOI);
		} else {
			suivant = setCurrentView(VIEW_CREATION_INITIALE);
		}
		return suivant;
	}

	private String goSuivant101() {
		if (nature.equals(VocabularyConstants.NATURE_REGLEMENTAIRE)) {
			if (transpositionDirective) {
				return setCurrentView(VIEW_TRANSPO_DIRECTIVE);
			}
			if (applicationLoi) {
				return setCurrentView(VIEW_APPLI_LOI);
			}
			if (transpositionOrdonnance) {
				return setCurrentView(VIEW_TRANSPO_ORDONNANCE);
			}
		}

		return setCurrentView(VIEW_CREATION_INITIALE);
	}

	private String goSuivant102() {
		if (nature.equals(VocabularyConstants.NATURE_REGLEMENTAIRE)) {
			if (transpositionDirective) {
				return setCurrentView(VIEW_TRANSPO_DIRECTIVE);
			}
		}

		return setCurrentView(VIEW_CREATION_INITIALE);
	}

	private String goSuivant103() throws Exception {
		saveWidgetList(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY);
		if (applicationLoi) {
			return setCurrentView(VIEW_APPLI_LOI);
		}
		if (transpositionOrdonnance) {
			return setCurrentView(VIEW_TRANSPO_ORDONNANCE);
		}
		if (dispositionHabilitation) {
			return setCurrentView(VIEW_HABILITATION38C);
		}
		return goTerminer();
	}

	private String goSuivant104() throws Exception {
		saveWidgetList(DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY);
		if (transpositionOrdonnance) {
			return setCurrentView(VIEW_TRANSPO_ORDONNANCE);
		}
		return goTerminer();

	}

	private String goSuivant105() throws Exception {
		saveWidgetList(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY);
		if (dispositionHabilitation) {
			return setCurrentView(VIEW_HABILITATION38C);
		}
		return goTerminer();
	}

	private String goSuivant106() {
		if (transpositionDirective) {
			return setCurrentView(VIEW_TRANSPO_DIRECTIVE);
		}
		if (dispositionHabilitation) {
			return setCurrentView(VIEW_HABILITATION38C);
		}

		return setCurrentView(VIEW_CREATION_INITIALE);
	}

	private String goSuivant108() {
		if (transpositionDirective) {
			return setCurrentView(VIEW_TRANSPO_DIRECTIVE);
		}

		return setCurrentView(VIEW_CREATION_INITIALE);
	}

	private void saveWidgetList(String widgetType) {
		ArrayList<List<DossierCreationWidgetItem>> copy = new ArrayList<List<DossierCreationWidgetItem>>();
		for (List<DossierCreationWidgetItem> item : widgetList) {
			copy.add(item);
		}

		widgetsMap.put(widgetType, copy);
		widgetMap.clear();
		widgetList.clear();
	}

	private void restoreWidgetList(String widgetType) {
		if (widgetsMap.containsKey(widgetType)) {
			widgetList = widgetsMap.get(widgetType);
		}
	}

	/**
	 * Réinitialise l'assistant de création de dossiers
	 * 
	 * @throws ClientException
	 */
	public void reset() throws ClientException {

		ministereResp = null;
		directionResp = null;
		currentNor = null;

		Set<String> postesId = ssPrincipal.getPosteIdSet();
		if (!postesId.isEmpty()) {
			String posteId = postesId.iterator().next();

			List<EntiteNode> ministereNodes = STServiceLocator.getSTMinisteresService().getMinistereParentFromPoste(
					posteId);

			if (ministereNodes != null && !ministereNodes.isEmpty()) {

				// Sélectionne le premier ministère de l'utilisateur
				ministereResp = ministereNodes.get(0).getId();

				List<OrganigrammeNode> directionList = STServiceLocator.getSTUsAndDirectionService()
						.getDirectionFromPoste(posteId);
				if (directionList != null && !directionList.isEmpty()) {
					UniteStructurelleNode directionNode = (UniteStructurelleNode) directionList.get(0);
					directionResp = directionNode.getId();
				}
			}
		}

		currentView = VIEW_CREATION_INITIALE;
		typeActe = null;
		typeActeLabel = null;
		nature = VocabularyConstants.NATURE_REGLEMENTAIRE;
		transpositionDirective = false;
		applicationLoi = false;
		transpositionOrdonnance = false;
		dispositionHabilitation = false;
		refLoi = null;
		numArticle = null;
		commentaire = null;
		numeroOrdre = null;
		posteId = null;
		norPrmList = null;
		norPrm = null;
		currentNorPrm = null;
		numeroNor = null;
		erreurNumeroNor = null;
		choixDirectionMinistere = null;
		choixPoste = null;
		displayValidationErreur = false;
		dossierBordereauService = null;
		widgetsMap = new HashMap<String, List<List<DossierCreationWidgetItem>>>();
		widgetMap = new HashMap<String, String>();
		widgetList = new ArrayList<List<DossierCreationWidgetItem>>();
		norDossierCopieFdr = null;
	}

	/**
	 * Retourne la liste des champs pour le widget courant
	 * 
	 * @param widgetType
	 * @return liste des champs
	 */
	public List<String> getWidgetFields(String widgetType) {
		List<String> fields = new ArrayList<String>();
		fields.add(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY);
		if (DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY.equals(widgetType)) {
			fields.add(DossierSolonEpgConstants.DOSSIER_TRANSPO_DIRECTIVE_REF_ANNEE_PROPERTY);
		}
		fields.add(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY);
		fields.add(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_NUMERO_PROPERTY);
		if (DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY.equals(widgetType)
				|| DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY.equals(widgetType)) {
			fields.add(DossierSolonEpgConstants.DOSSIER_LOI_APPLIQUEE_REF_MESURE_PROPERTY);
		}
		fields.add(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_COMMENTAIRE_PROPERTY);

		return fields;
	}

	public List<String> getWidgetFieldsRead(String widgetType) {
		List<String> fields = new ArrayList<String>();
		fields.add(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY);
		fields.add(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY);
		fields.add(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_NUMERO_PROPERTY);
		if (DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY.equals(widgetType)
				|| DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY.equals(widgetType)) {
			fields.add(DossierSolonEpgConstants.DOSSIER_LOI_APPLIQUEE_REF_MESURE_PROPERTY);
		}
		fields.add(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_COMMENTAIRE_PROPERTY);

		return fields;
	}

	/**
	 * Ajoute une transposition au widget courant
	 */
	public void addWidgetItem() {
		List<DossierCreationWidgetItem> copy = new ArrayList<DossierCreationWidgetItem>();

		String reference = widgetMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY);

		if (widgetMap.containsKey(DossierSolonEpgConstants.DOSSIER_TRANSPO_DIRECTIVE_REF_ANNEE_PROPERTY)) {
			if (StringUtils.isEmpty(reference)) {
				String message = resourcesAccessor.getMessages().get("epg.dossierCreation.ref.non.nulle");
				facesMessages.add(StatusMessage.Severity.WARN, message);
				return;
			} else if (reference.length() > 4) {
				String message = resourcesAccessor.getMessages().get("epg.dossierCreation.ref.format.incorrect");
				facesMessages.add(StatusMessage.Severity.WARN, message);
				return;
			}
			String annee = widgetMap.get(DossierSolonEpgConstants.DOSSIER_TRANSPO_DIRECTIVE_REF_ANNEE_PROPERTY);
			if (annee.isEmpty()) {
				String message = resourcesAccessor.getMessages().get(
						"epg.dossierCreation.annee.transposition.non.nulle");
				facesMessages.add(StatusMessage.Severity.WARN, message);
				return;
			} else if (annee.length() != 4 || !StringUtils.isNumeric(annee)) {
				String message = resourcesAccessor.getMessages().get(
						"epg.dossierCreation.annee.transposition.format.incorrect");
				facesMessages.add(StatusMessage.Severity.WARN, message);
				return;
			}
			String titre = widgetMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY);
			// Vérification de l'existence de la directive
			TranspositionDirectiveService transpositionDirectiveService = SolonEpgServiceLocator
					.getTranspositionDirectiveService();
			String titreDirective = transpositionDirectiveService.findDirectiveEurlexWS(reference, annee, titre);
			if (titreDirective == null) {
				// Erreur -> Directive non trouvée
				String message = resourcesAccessor.getMessages().get(
						"La directive " + " http://data.europa.eu /eli /dir/ " + annee + "/" + reference + " /" + "oj"
								+ " n'existe pas");
				if (titre == null || titre.isEmpty()) {
					message += " Veuillez renseigner un titre !";
					facesMessages.add(StatusMessage.Severity.WARN, message);
					return;
				}
				facesMessages.add(StatusMessage.Severity.WARN, message);
				titreDirective = titre + " http://data.europa.eu/eli/dir/" + annee + "/" + reference + "/" + "oj";
			} else {
				titreDirective += " http://data.europa.eu/eli/dir/" + annee + "/" + reference + "/" + "oj";
			}
			copy.add(new DossierCreationWidgetItem(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY,
					titreDirective));
		}
		// on vérifie les valeurs obligatoires
		// note : voir si besoin de vérifier l'expression de la référence pour la transposition d'une directive

		if (currentView.equals(VIEW_APPLI_LOI)) { // > application d'une loi
			try {
				if (!getDossierBordereauService().isFieldReferenceLoiValid(documentManager, reference)) {
					String message = resourcesAccessor.getMessages().get(MSG_ERROR_VALIDATION_REF_LOI);
					facesMessages.add(StatusMessage.Severity.WARN, message);
					return;
				}
			} catch (ClientException e) {
				log.error(e.getMessage());
				String message = resourcesAccessor.getMessages().get(MSG_ERROR_EXCEPTION_VALIDATION_REF_LOI);
				facesMessages.add(StatusMessage.Severity.WARN, message);
				return;
			}
			String numeroOrdre = widgetMap.get(DossierSolonEpgConstants.DOSSIER_LOI_APPLIQUEE_REF_MESURE_PROPERTY);
			if (StringUtils.isNotEmpty(numeroOrdre)) {
				try {
					if (!getDossierBordereauService().isFieldNumeroOrdreValid(documentManager, numeroOrdre)) {
						String message = resourcesAccessor.getMessages().get(MSG_ERROR_VALIDATION_NUMERO_ORDRE);
						facesMessages.add(StatusMessage.Severity.WARN, message);
						return;
					}
				} catch (ClientException e) {
					log.error(e.getMessage());
					String message = resourcesAccessor.getMessages().get(MSG_ERROR_EXCEPTION_VALIDATION_NUMERO_ORDRE);
					facesMessages.add(StatusMessage.Severity.WARN, message);
					return;
				}
			}
		}

		if (currentView.equals(VIEW_TRANSPO_ORDONNANCE)) { // > application d'une ordonnance
			try {
				if (!getDossierBordereauService().isFieldReferenceOrdonnanceValid(documentManager, reference)) {
					String message = resourcesAccessor.getMessages().get(MSG_ERROR_VALIDATION_REF_ORDONNANCE);
					facesMessages.add(StatusMessage.Severity.WARN, message);
					return;
				}
			} catch (ClientException e) {
				log.error(e.getMessage());
				String message = resourcesAccessor.getMessages().get(MSG_ERROR_EXCEPTION_VALIDATION_REF_ORDONNANCE);
				facesMessages.add(StatusMessage.Severity.WARN, message);
				return;
			}
		}
		if (widgetMap.containsKey(DossierSolonEpgConstants.DOSSIER_TRANSPO_DIRECTIVE_REF_ANNEE_PROPERTY)) {
			copy.add(new DossierCreationWidgetItem(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY,
					widgetMap.get(DossierSolonEpgConstants.DOSSIER_TRANSPO_DIRECTIVE_REF_ANNEE_PROPERTY) + "/"
							+ widgetMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY)));
		} else {
			copy.add(new DossierCreationWidgetItem(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY,
					widgetMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY)));
		}

		if (!widgetMap.containsKey(DossierSolonEpgConstants.DOSSIER_TRANSPO_DIRECTIVE_REF_ANNEE_PROPERTY)) {
			copy.add(new DossierCreationWidgetItem(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY,
					widgetMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY)));
		}
		copy.add(new DossierCreationWidgetItem(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_NUMERO_PROPERTY,
				widgetMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_NUMERO_PROPERTY)));
		if (widgetMap.containsKey(DossierSolonEpgConstants.DOSSIER_LOI_APPLIQUEE_REF_MESURE_PROPERTY)) {
			copy.add(new DossierCreationWidgetItem(DossierSolonEpgConstants.DOSSIER_LOI_APPLIQUEE_REF_MESURE_PROPERTY,
					widgetMap.get(DossierSolonEpgConstants.DOSSIER_LOI_APPLIQUEE_REF_MESURE_PROPERTY)));
		}
		copy.add(new DossierCreationWidgetItem(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_COMMENTAIRE_PROPERTY,
				widgetMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_COMMENTAIRE_PROPERTY)));

		widgetList.add(copy);
		widgetMap.clear();
	}

	/**
	 * Supprime une transposition du widget courant
	 * 
	 * @param remove
	 */
	public void removeWidgetItem(Object remove) {
		widgetList.remove(remove);
	}

	private List<ComplexeType> getTranspositionList(String typeTransposition) {
		List<ComplexeType> list = new ArrayList<ComplexeType>();

		if (widgetsMap.containsKey(typeTransposition)) {
			List<List<DossierCreationWidgetItem>> subList = widgetsMap.get(typeTransposition);
			for (List<DossierCreationWidgetItem> items : subList) {
				ComplexeType complexeType = new ComplexeTypeImpl();
				Map<String, Serializable> serializableMap = new HashMap<String, Serializable>();
				for (DossierCreationWidgetItem item : items) {
					serializableMap.put(item.getKey(), item.getValue());
				}
				complexeType.setSerializableMap(serializableMap);
				list.add(complexeType);
			}
		}

		return list;
	}

	/**
	 * Retourne le nombre de dossiers en cours de création à partir de l'espace de création
	 * 
	 * @throws ClientException
	 */
	public String getDossiersCreationCount() throws ClientException {
		if (resultCount == null) {
			PageProvider<?> pageProvider = contentViewActions.getContentView(
					SolonEpgContentViewConstant.ESPACE_CREATION_DOSSIERS_CONTENT_VIEW).getPageProvider();
			resultCount = pageProvider.getResultsCount();
		}
		if (resultCount == null) {
			log.error("Le compteur de résultats est nul, retour de la chaîne vide");
			return "";
		}
		return resultCount.toString();
	}

	public String getCurrentView() {
		return currentView;
	}

	public String setCurrentView(String currentView) {
		this.currentView = currentView;

		if (currentView.equals(VIEW_TRANSPO_DIRECTIVE)) {
			restoreWidgetList(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_DIRECTIVE_PROPERTY);
		}
		if (currentView.equals(VIEW_APPLI_LOI)) {
			restoreWidgetList(DossierSolonEpgConstants.DOSSIER_APPLICATION_LOI_PROPERTY);
		}
		if (currentView.equals(VIEW_TRANSPO_ORDONNANCE)) {
			restoreWidgetList(DossierSolonEpgConstants.DOSSIER_TRANSPOSITION_ORDONNANCE_PROPERTY);
		}

		if (currentView.equals(VIEW_RECTIFICATIF)) {
			erreurNumeroNor = null;
		}

		return this.currentView;
	}

	public void validateNatureActe(FacesContext facesContext, UIComponent uIComponent, Object object)
			throws ValidatorException {
		String enteredNature = (String) object;
		final ActeService acteService = SolonEpgServiceLocator.getActeService();

		boolean valid = true;

		if (VocabularyConstants.NATURE_NON_REGLEMENTAIRE.equals(enteredNature)
				&& !acteService.isNonReglementaire(typeActe)) {
			valid = false;
		}

		if (VocabularyConstants.NATURE_REGLEMENTAIRE.equals(enteredNature) && acteService.isNonReglementaire(typeActe)) {
			valid = false;
		}

		if (VocabularyConstants.NATURE_CONVENTION_COLLECTIVE.equals(enteredNature)
				&& !acteService.isConventionCollective(typeActe)) {
			valid = false;
		}

		if (!valid) {
			FacesMessage message = new FacesMessage();
			message.setSummary("Nature de l'acte non cohérente avec le type d'acte choisi.");
			throw new ValidatorException(message);
		}
	}

	/**
	 * Validation utilisée pour le numéro d'ordre de l'habilitation
	 * 
	 * @param facesContext
	 * @param uIComponent
	 * @param object
	 * @throws ValidatorException
	 */
	public void validateNumeroOrdre(FacesContext facesContext, UIComponent uIComponent, Object object)
			throws ValidatorException {
		String numeroOrdre = (String) object;
		if (StringUtils.isEmpty(numeroOrdre)) {
			return;
		}
		try {
			if (!getDossierBordereauService().isFieldNumeroOrdreValid(documentManager, numeroOrdre)) {
				FacesMessage message = new FacesMessage();
				message.setSummary(resourcesAccessor.getMessages().get(MSG_ERROR_VALIDATION_NUMERO_ORDRE));
				throw new ValidatorException(message);
			}
		} catch (ClientException e) {
			FacesMessage message = new FacesMessage();
			message.setSummary(resourcesAccessor.getMessages().get(MSG_ERROR_EXCEPTION_VALIDATION_NUMERO_ORDRE));
			throw new ValidatorException(message);
		}
	}

	/**
	 * Validation utilisée pour la référence de la loi de l'habilitation
	 * 
	 * @param facesContext
	 * @param uIComponent
	 * @param object
	 * @throws ValidatorException
	 */
	public void validateReferenceLoi(FacesContext facesContext, UIComponent uIComponent, Object object)
			throws ValidatorException {
		String refLoi = (String) object;
		if (StringUtils.isEmpty(refLoi)) {
			return;
		}
		try {
			if (!getDossierBordereauService().isFieldReferenceLoiValid(documentManager, refLoi)) {
				FacesMessage message = new FacesMessage();
				message.setSummary(resourcesAccessor.getMessages().get(MSG_ERROR_VALIDATION_REF_LOI));
				throw new ValidatorException(message);
			}
		} catch (ClientException e) {
			FacesMessage message = new FacesMessage();
			message.setSummary(resourcesAccessor.getMessages().get(MSG_ERROR_EXCEPTION_VALIDATION_REF_LOI));
			throw new ValidatorException(message);
		}
	}

	public DossierBordereauService getDossierBordereauService() {
		if (dossierBordereauService == null) {
			dossierBordereauService = SolonEpgServiceLocator.getDossierBordereauService();
		}
		return dossierBordereauService;
	}

	public Map<String, String> getWidgetMap() {
		return widgetMap;
	}

	public List<List<DossierCreationWidgetItem>> getWidgetList() {
		return widgetList;
	}

	public String getTypeActe() {
		return typeActe;
	}

	public void setTypeActe(String typeActe) {
		// on met à jour le label du type d'acte
		if (typeActe == null || typeActe.isEmpty()) {
			typeActeLabel = null;
		} else {
			final ActeService acteService = SolonEpgServiceLocator.getActeService();
			TypeActe acte = acteService.getActe(typeActe);
			typeActeLabel = acte.getLabel();
		}
		this.typeActe = typeActe;
	}

	public String getTypeActeLabel() {
		return typeActeLabel;
	}

	public void setTypeActeLabel(String typeActeLabel) {
		this.typeActeLabel = typeActeLabel;
	}

	public String getMinistereResp() {
		return ministereResp;
	}

	public void setMinistereResp(String ministereResp) {
		this.ministereResp = ministereResp;
		this.directionResp = null;
	}

	public String getDirectionResp() {
		return directionResp;
	}

	public void setDirectionResp(String directionResp) {
		this.directionResp = directionResp;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public boolean isTranspositionDirective() {
		return transpositionDirective;
	}

	public void setTranspositionDirective(boolean transpositionDirective) {
		this.transpositionDirective = transpositionDirective;
	}

	public boolean isApplicationLoi() {
		return applicationLoi;
	}

	public void setApplicationLoi(boolean applicationLoi) {
		this.applicationLoi = applicationLoi;
	}

	public boolean isTranspositionOrdonnance() {
		return transpositionOrdonnance;
	}

	public void setTranspositionOrdonnance(boolean transpositionOrdonnance) {
		this.transpositionOrdonnance = transpositionOrdonnance;
	}

	public boolean isDispositionHabilitation() {
		return dispositionHabilitation;
	}

	public void setDispositionHabilitation(boolean dispositionHabilitation) {
		this.dispositionHabilitation = dispositionHabilitation;
	}

	public String getRefLoi() {
		return refLoi;
	}

	public void setRefLoi(String refLoi) {
		this.refLoi = refLoi;
	}

	public String getNumArticle() {
		return numArticle;
	}

	public void setNumArticle(String numArticle) {
		this.numArticle = numArticle;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public String getNumeroOrdre() {
		return numeroOrdre;
	}

	public void setNumeroOrdre(String numeroOrdre) {
		this.numeroOrdre = numeroOrdre;
	}

	/**
	 * Getter de numeroNor.
	 * 
	 * @return numeroNor
	 */
	public String getNumeroNor() {
		return numeroNor;
	}

	/**
	 * Setter de numeroNor.
	 * 
	 * @param numeroNor
	 *            numeroNor
	 */
	public void setNumeroNor(String numeroNor) {
		this.numeroNor = numeroNor;
	}

	/**
	 * Getter de erreurNumeroNor.
	 * 
	 * @return erreurNumeroNor
	 */
	public String getErreurNumeroNor() {
		return erreurNumeroNor;
	}

	/**
	 * Setter de erreurNumeroNor.
	 * 
	 * @param erreurNumeroNor
	 *            erreurNumeroNor
	 */
	public void setErreurNumeroNor(String erreurNumeroNor) {
		this.erreurNumeroNor = erreurNumeroNor;
	}

	/**
	 * Getter de posteId.
	 * 
	 * @return posteId
	 */
	public String getPosteId() {
		return posteId;
	}

	/**
	 * Setter de posteId.
	 * 
	 * @param posteId
	 *            posteId
	 */
	public void setPosteId(String posteId) {
		this.posteId = posteId;
	}

	/**
	 * Getter de NorPrmList.
	 * 
	 * @return norPrmList.
	 * @throws ClientException
	 */
	public List<VocabularyEntry> getNorPrmList() throws ClientException {
		if (norPrmList == null) {
			TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
			norPrmList = tableReferenceService.getNorDirectionsForCreation(documentManager);
		}
		return norPrmList;
	}

	/**
	 * Getter de norPrm.
	 * 
	 * @return norPrm
	 * @throws ClientException
	 */
	public String getNorPrm() throws ClientException {
		// par défaut on le nor premier ministre sélectionné est le premier de la liste
		if (norPrm == null) {
			if (getNorPrmList() != null) {
				if (currentNorPrm != null && !currentNorPrm.isEmpty()) {
					norPrm = currentNorPrm;
				} else {
					norPrm = getNorPrmList().get(0).getId();
				}
			}
		}
		return norPrm;
	}

	/**
	 * Setter de norPrm.
	 * 
	 * @param norPrm
	 */
	public void setNorPrm(String norPrm) {
		this.norPrm = norPrm;
	}

	/**
	 * Getter de choixDirectionMinistere.
	 * 
	 * @return choixDirectionMinistere
	 * @throws ClientException
	 */
	public String getChoixDirectionMinistere() {
		if (choixDirectionMinistere == null) {
			choixDirectionMinistere = "true";
		}
		return choixDirectionMinistere;
	}

	/**
	 * Setter de choixDirectionMinistere.
	 * 
	 * @param choixDirectionMinistere
	 */
	public void setChoixDirectionMinistere(String choixDirectionMinistere) {
		this.choixDirectionMinistere = choixDirectionMinistere;
		if ("false".equals(choixDirectionMinistere)) {
			currentNor = null;
		}
	}

	public String getChoixPoste() {
		if (choixPoste == null) {
			choixPoste = "true";
		}
		return choixPoste;
	}

	public void setChoixPoste(String choix) {
		this.choixPoste = choix;
		if ("false".equals(choixPoste)) {
			posteId = null;
		}
	}

	/**
	 * Listener appelé lors d'un changement de valeur dans le menu prm
	 * 
	 * @param event
	 */
	public void norPrmListener(ValueChangeEvent event) throws Exception {
		if (event != null && event.getNewValue() == null) {
			return;
		}
		if (event != null && event.getNewValue() != null && event.getNewValue() instanceof String) {
			norPrm = (String) event.getNewValue();
			// on enregistre le norPrm courant
			currentNorPrm = norPrm;
		} else {
			throw new ClientException("le changement de valeur du nor prm n'a as été correctement effectué !");
		}
	}

	/**
	 * Listener appelé lors d'un changement de valeur dans le radioButton de choix de la direction Ministère.
	 * 
	 * @param event
	 */
	public void choixDirectionMinistereListener(ValueChangeEvent event) throws Exception {
		if (event != null && event.getNewValue() != null && event.getNewValue() instanceof String) {
			choixDirectionMinistere = (String) event.getNewValue();
		}
	}

	/**
	 * Listener appelé lors d'un changement de valeur dans le radioButton de choix du poste.
	 * 
	 * @param event
	 */
	public void choixPosteListener(ValueChangeEvent event) throws Exception {
		if (event != null && event.getNewValue() != null && event.getNewValue() instanceof String) {
			choixPoste = (String) event.getNewValue();
		}
	}

	public void setCurrentNor(String currentNor) {
		this.currentNor = currentNor;
	}

	public String getCurrentNor() {
		return currentNor;
	}

	public void refreshPage() {
		resetCount();
		refreshActions.refreshPage();
	}

	public void resetCount() {
		resultCount = null;
	}

	public String getNorDossierCopieFdr() {
		return norDossierCopieFdr;
	}

	public void setNorDossierCopieFdr(String norDossierCopieFdr) {
		this.norDossierCopieFdr = norDossierCopieFdr;
	}

}
