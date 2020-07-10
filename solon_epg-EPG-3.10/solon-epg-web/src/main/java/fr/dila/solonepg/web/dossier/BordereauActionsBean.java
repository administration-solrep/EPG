package fr.dila.solonepg.web.dossier;

import static org.jboss.seam.ScopeType.EVENT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.platform.ui.web.api.WebActions;
import org.nuxeo.ecm.webapp.edit.lock.LockActions;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.administration.BulletinOfficiel;
import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.ActeVisibilityConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgANEventConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.dto.VecteurPublicationDTO;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.DossierBordereauService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.HistoriqueMajMinisterielleService;
import fr.dila.solonepg.api.service.NORService;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.solonepg.core.dto.activitenormative.VecteurPublicationDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.solonepg.web.espace.CorbeilleActionsBean;
import fr.dila.solonepg.web.espace.EspaceTraitementActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.core.util.UnrestrictedCreateOrSaveDocumentRunner;
import fr.dila.st.web.dossier.DossierLockActionsBean;
import fr.dila.st.web.lock.STLockActionsBean;

@Name("bordereauActions")
@Scope(ScopeType.EVENT)
public class BordereauActionsBean implements Serializable {

	// to get the labels in message.properties
	public static final String							RESOURCES_ACCESSOR_PROPERTY			= "resourcesAccessor";

	private static final STLogger						LOGGER								= STLogFactory
																									.getLog(BordereauActionsBean.class);
	/**
	 * Serial UID.
	 */
	private static final long							serialVersionUID					= -8283345214350348613L;

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(create = true, required = false)
	protected transient NavigationContext				navigationContext;

	@In(create = true, required = false)
	protected transient FacesMessages					facesMessages;

	@In(create = true)
	protected transient ResourcesAccessor				resourcesAccessor;

	@In(create = true)
	protected transient LockActions						lockActions;

	@In(create = true, required = false)
	protected transient DossierDistributionActionsBean	dossierDistributionActions;

	@In(create = true, required = false)
	protected transient DossierLockActionsBean			dossierLockActions;

	@In(create = true, required = false)
	protected transient CorbeilleActionsBean			corbeilleActions;

	@In(create = true, required = false)
	protected transient EspaceTraitementActionsBean		espaceTraitementActions;

	@In(required = true, create = true)
	protected NuxeoPrincipal							currentUser;

	@In(required = true, create = true)
	protected STLockActionsBean							stLockActions;

	@In(create = true, required = false)
	protected transient WebActions						webActions;

	private String										currentVecteur;

	private String										currentModeParution;

	private String										currentPublicationConjointe;

	private Date										currentDateEffetEntreprise;

	/**
	 * Message affiché si l'on pas pas ajouté la publication conjointe
	 */
	private String										publicationConjointeError;

	/**
	 * Message affiché si l'on pas pas ajouté la nouvelle date d'effet entreprise
	 */
	private String										dateEffetEntrepriseError;

	/**
	 * Vrai si il manque des manque des métadonnées dans le bordereau et que l'on doit afficher en surbrillance les
	 * champs obligatoires du bordereau
	 */
	protected Boolean									displayRequiredMetadonneBordereau	= null;

	/**
	 * Retourne vrai si l'indexation est modifiable.
	 * 
	 * @return Condition
	 */
	@Factory(value = "isIndexationUpdatable", scope = EVENT)
	public boolean isIndexationUpdatable() {
		// Vérifie si le dossier est verrouillé
		if (!dossierLockActions.getCanUnlockCurrentDossier()) {
			return false;
		}

		// Vérifie que l'utilisateur a les droits d'indexation
		if (!currentUser.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_INDEXATION_UPDATER)) {
			return false;
		}

		return true;
	}

	/**
	 * Retourne vrai si le bordereau est modifiable.
	 * 
	 * @return Condition
	 * @throws ClientException
	 */
	@Factory(value = "isBordereauUpdatable", scope = EVENT)
	public boolean isBordereauUpdatable() throws ClientException {
		// Vérifie si le dossier est verrouillé
		if (!dossierLockActions.getCanUnlockCurrentDossier()) {
			return false;
		}

		// Vérifie si le DossierLink est chargé
		if (!corbeilleActions.isDossierLoadedInCorbeille()) {
			return false;
		}

		// À l'étape "pour impression", le dossier est en lecture seule sauf pour les administrateurs
		if (!dossierDistributionActions.isEtapePourImpressionUpdater()) {
			return false;
		}

		return true;
	}
	
	/**
	 * Retourne vrai si le bordereau est modifiable.
	 * 
	 * @return Condition
	 * @throws ClientException
	 */
	@Factory(value = "hasRightModifCE", scope = EVENT)
	public boolean hasRightModifCE() throws ClientException {
		// Vérifie si le dossier est en étape pour avis CE et si l'utilisateur à le droit de modifier les infos
		if (!currentUser.isMemberOf(SolonEpgBaseFunctionConstant.CONSEIL_ETAT_UPDATER) && corbeilleActions.isEtapePourAvisCE()) {
			return false;
		}
		return true;
	}

	/**
	 * Retourne vrai si la partie activité normative du bordereau est modifiable.
	 * 
	 * @return Condition
	 */
	@Factory(value = "isBordereauActiviteNormativeUpdatable", scope = EVENT)
	public boolean isBordereauActiviteNormativeUpdatable() {
		// Vérifie si le dossier est verrouillé
		if (!dossierLockActions.getCanUnlockCurrentDossier()) {
			return false;
		}

		// Vérifie si le DossierLink est chargé ou que l'utilisateur est un contributeur de l'activité normative
		if (!corbeilleActions.isDossierLoadedInCorbeille()
				&& !currentUser.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_ACTIVITE_NORMATIVE_UPDATER)) {
			return false;
		}

		return true;
	}

	public boolean isEditableTypeActe(final DocumentModel dossierDoc) {
		final ActeService acteService = SolonEpgServiceLocator.getActeService();
		final String typeActeId = getTypeActeId(dossierDoc);
		if (!acteService.isDecret(typeActeId)
				|| !currentUser.isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_TYPE_ACTE_DECRET_UPDATER)) {
			return false;
		}
		// on ne peut pas changer le type d'acte des dossiers publiés
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		return !VocabularyConstants.STATUT_PUBLIE.equals(dossier.getStatut());
	}

	@Factory(value = "acteVisibility", scope = EVENT)
	public Map<String, Boolean> getActeVisibility() {
		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		final String typeActeId = getTypeActeId(dossierDoc);
		final ActeService acteService = SolonEpgServiceLocator.getActeService();
		return acteService.getActeVisibility(typeActeId);
	}

	public boolean isVisibleCategorieActe() {
		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		final String typeActeId = getTypeActeId(dossierDoc);
		final ActeService acteService = SolonEpgServiceLocator.getActeService();
		return !acteService.isRapportAuParlement(typeActeId);
	}
	
	public boolean isVisibleStatutArchivage() {
		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		return !dossier.getStatutArchivage().equals(VocabularyConstants.STATUT_ARCHIVAGE_AUCUN) ? true : false;
	}
	
	public String getLibelleStatutArchivage() {
		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		final Map<String, String> libelleStatutArchivage = VocabularyConstants.LIST_LIBELLE_STATUT_ARCHIVAGE_PAR_ID;
		return libelleStatutArchivage.get(dossier.getStatutArchivage());
	}

	public boolean isVisibleCategorieActeConventionCollective() {
		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		final String typeActeId = getTypeActeId(dossierDoc);
		// On détermine si on affice ou pas 'Convention collective' dans la liste déroulante du type d'acte
		// FEV 501
		if (typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_PM)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_PR)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_CE)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_AVIS)) {
			// On affiche
			return true;
		} else {
			// On affiche pas
			return false;
		}
	}

	public boolean isVisibleBaseLegale(final DocumentModel dossierDoc) {
		final ActeService acteService = SolonEpgServiceLocator.getActeService();
		final String typeActeId = getTypeActeId(dossierDoc);
		return acteService.isVisibleBaseLegale(typeActeId);
	}

	public boolean isVisiblePublicationRapport(final DocumentModel dossierDoc) {
		final ActeService acteService = SolonEpgServiceLocator.getActeService();
		final String typeActeId = getTypeActeId(dossierDoc);
		return acteService.isVisiblePublicationRapport(typeActeId);
	}

	public boolean isVisibleNumeroTexteJO(final DocumentModel dossierDoc) {
		final ActeService acteService = SolonEpgServiceLocator.getActeService();
		final String typeActeId = getTypeActeId(dossierDoc);
		return acteService.isVisibleNumeroTexteJO(typeActeId);
	}

	public boolean isVisiblePublicationIntegrale(final DocumentModel dossierDoc) {
		final ActeService acteService = SolonEpgServiceLocator.getActeService();
		final String typeActeId = getTypeActeId(dossierDoc);
		return acteService.isVisiblePublicationIntegrale(typeActeId);
	}

	public boolean isVisibleDecretNumerote(final DocumentModel dossierDoc) {
		final ActeService acteService = SolonEpgServiceLocator.getActeService();
		final String typeActeId = getTypeActeId(dossierDoc);
		return acteService.isVisibleDecretNumerote(typeActeId);
	}

	public boolean isEditableCE(final DocumentModel dossierDoc) {
		Boolean locked = Boolean.TRUE;
		try {
			locked = stLockActions.isCurrentDocumentLockedByCurrentUser();
		} catch (final ClientException exc) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_PROPERTY_TEC,
					"Le verrou sur le dossier n'a pas pu être récupéré", exc);
			facesMessages.add(StatusMessage.Severity.ERROR,
					resourcesAccessor.getMessages().get("epg.bordereau.error.lock"));
		}
		return currentUser.isMemberOf(SolonEpgBaseFunctionConstant.CONSEIL_ETAT_UPDATER) && locked;
	}

	/**
	 * Retourne vrai si le champ "commentaire sgg dila" est modifiable
	 * 
	 * @return boolean
	 */
	public boolean isModifiableZoneComSggDila() {
		return currentUser.isMemberOf(SolonEpgBaseFunctionConstant.ZONE_COMMENTAIRE_SGG_DILA_UPDATER);
	}

	/**
	 * Retourne vrai si le champ "date signature" est modifiable
	 * 
	 * @return boolean
	 */
	public boolean isModifiableDateSignature() {
		return currentUser.isMemberOf(SolonEpgBaseFunctionConstant.BORDEREAU_DATE_SIGNATURE_UPDATER);
	}

	/**
	 * Retourne vrai si l'on doit afficher une erreur de validation pour le champ
	 * 
	 * @param property
	 * @return
	 */
	public boolean displayFieldValidationMessage(final String property, final boolean isRequiredField,
			final boolean displayRequiredMetadonneBordereau) {

		if (this.displayRequiredMetadonneBordereau == null) {
			// on récupère l'information pour savoir si l'on doit mettre en surbrillance le bordereau
			this.displayRequiredMetadonneBordereau = displayRequiredMetadonneBordereau;
			// On réinitialise cette information dans le DossierDistributionBean
			dossierDistributionActions.resetDisplayRequiredMetadonneBordereau();
		}

		// on verifie que l'on doit afficher les messages de validations et que le champ est un champ obligatoire
		if (this.displayRequiredMetadonneBordereau == null || !this.displayRequiredMetadonneBordereau
				|| !isRequiredField) {
			return false;
		}

		// on regarde si le champ est vide
		final DocumentModel dossier = navigationContext.getCurrentDocument();
		try {
			final Object value = dossier.getPropertyValue(property);
			if (value == null || value instanceof String && ((String) value).isEmpty()) {
				return true;
			} else if (value.toString().equals("[]")) {
				// HACK : cas du tableau vide
				return true;
			}
		} catch (final Exception e) {
			LOGGER.warn(documentManager, STLogEnumImpl.FAIL_VALIDATE_PROPERTY_TEC,
					"erreur lors du test d'affichage du message de validation d'un champ", e);
		}

		return false;
	}

	/**
	 * Retourne vrai si le champ est editable
	 * 
	 * @param property
	 * @return
	 */
	public boolean isFieldEditable(final String property) {
		return DossierSolonEpgConstants.BORDEREAU_EDITABLE_FIELDS.contains(property);
	}

	public String getModeParutionDossier(final String modeParutionId) {
		final IdRef modeParutionRef = new IdRef(modeParutionId);

		try {
			if (documentManager.exists(modeParutionRef)) {
				DocumentModel modeParutionDoc = documentManager.getDocument(modeParutionRef);
				ModeParution mode = modeParutionDoc.getAdapter(ModeParution.class);
				return mode.getMode();
			}
		} catch (ClientException exc) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOCUMENT_FONC, modeParutionId, exc);
		}

		return "";
	}

	public List<VecteurPublicationDTO> getVecteurPublicationList(final DocumentModel dossierDoc) throws ClientException {
		final BulletinOfficielService bulletinService = SolonEpgServiceLocator.getBulletinOfficielService();
		final List<VecteurPublicationDTO> vecteurs = new ArrayList<VecteurPublicationDTO>();
		final List<DocumentModel> allBulletinOrdered = bulletinService.getAllBulletinOfficielOrdered(
				navigationContext.getCurrentDocument(), documentManager);
		List<DocumentModel> lstVecteurs = bulletinService.getAllActifVecteurPublication(documentManager);
		VecteurPublicationDTO vpJournalOff = null;
		String joIdLibelle = "";
		try {
			joIdLibelle = bulletinService.getLibelleForJO(documentManager);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOCUMENT_FONC, bulletinService, e);
		}

		// récupère la liste des vecteurs de publication
		for (DocumentModel doc : lstVecteurs) {
			VecteurPublication vect = doc.getAdapter(VecteurPublication.class);

			if (!joIdLibelle.equals(doc.getId())) {
				vecteurs.add(new VecteurPublicationDTOImpl(vect));
			} else {
				vpJournalOff = new VecteurPublicationDTOImpl(vect);
			}
		}

		for (final DocumentModel docModel : allBulletinOrdered) {
			BulletinOfficiel bulletin = docModel.getAdapter(BulletinOfficiel.class);
			VecteurPublicationDTOImpl convertedBulletin = new VecteurPublicationDTOImpl();
			convertedBulletin.setId(bulletin.getIntitule());
			convertedBulletin.setIntitule(bulletin.getIntitule());

			vecteurs.add(convertedBulletin);
		}

		// On trie dans l'ordre alphabétique
		Collections.sort(vecteurs, VecteurPublicationDTO.COMPARE);
		// on place la ligne du journal officiel en première position
		vecteurs.add(0, vpJournalOff);

		return vecteurs;
	}

	public List<VecteurPublicationDTO> getVecteurPublicationDossier(final DocumentModel dossierDoc) {
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		List<VecteurPublicationDTO> lstIntituleVecteurs = new ArrayList<VecteurPublicationDTO>();
		for (String idVect : dossier.getVecteurPublication()) {
			final IdRef docRef = new IdRef(idVect);
			try {

				// Si ça n'est pas un vecteur, c'est le libellé d'un bulletin
				if (documentManager.exists(docRef)) {
					DocumentModel doc = documentManager.getDocument(docRef);
					if (doc.hasSchema(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA)) {
						VecteurPublication vecteur = doc.getAdapter(VecteurPublication.class);
						lstIntituleVecteurs.add(new VecteurPublicationDTOImpl(vecteur));
					}
				} else {
					lstIntituleVecteurs.add(new VecteurPublicationDTOImpl(idVect));
				}
			} catch (ClientException ce) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_DOCUMENT_FONC, idVect, ce);
			}
		}

		return lstIntituleVecteurs;
	}

	public void addVecteurPublication(final DocumentModel dossierDoc) {
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		final List<String> vecteurs = dossier.getVecteurPublication();
		if (!vecteurs.contains(currentVecteur)) {
			if (currentVecteur != null) {
				vecteurs.add(currentVecteur);
				dossier.setVecteurPublication(vecteurs);
			}
		}
	}

	public void removeVecteurPublication(final DocumentModel dossierDoc, final String vecteur) {
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		final List<String> vecteurs = dossier.getVecteurPublication();
		// Attention, on a 2 cas d'utilisation pour la même colonne DOS_VECTEURPUBLICATION.ITEM:
		// -dans le cas des bulletins officiels, elle contient le libellé du bulletin
		// -dans le cas des vecteurs de publication (évol de la FEV 391), elle contient l'ID de la table
		// VECTEUR_PUBLICATION
		vecteurs.remove(vecteur);
		dossier.setVecteurPublication(vecteurs);
	}

	/**
	 * M158580 - ORA-00060 workaround
	 * La mise à jour d'un dossier avec publication conjointe met à jour le dossier
	 * et les champs delaiPublication, DatePreciseePublication et modeParution des dossiers en publication
	 * conjointe.
	 * 
	 * Cela peut entrainer un deadlock en cas de modification simultanée car on aura pour une modification de A
	 * et B en modification conjointe.
	 * 
	 * saveBordereau(A): modif. A (lock) -> ... B (deadlock) -> ...
	 * saveBordereau(B): modif. B (lock) -> ... A (deadlock) -> ...
	 * 
	 * Le contournement mis en oeuvre consiste à provoquer la modification de dc:modified sur le dossier et ses
	 * publications conjointes dans un ordre déterministe.
	 * 
	 * saveBordereau(A): dc:modified A (lock) -> ... dc:modified B -> ... ; modif. A -> ... B -> ... (release all)
	 * saveBordereau(B): dc:modified A (wait) -> ... dc:modified B -> ... ; modif. B -> ... A -> ... (release all)
	 * 
	 * Pour que le contournement fonctionne, il ne faut pas que l'appel à saveBordereau(...) soit précédé d'un
	 * session.save(...) qui déclenche le lock.
	 * 
	 * @param dossierDoc
	 * @param dossier
	 * @throws ClientException
	 */
	private void workaroundOra00060Deadlock(DocumentModel dossierDoc, Dossier dossier) throws ClientException {
		final NORService norService = SolonEpgServiceLocator.getNORService();
		
		List<String> publicationsConjointesWa = getPublicationsConjointes(dossierDoc);
		if (dossier.getNumeroNor() != null) {
			publicationsConjointesWa.add(dossier.getNumeroNor());
		}
		Collections.sort(publicationsConjointesWa);
		for (String pubNor : publicationsConjointesWa) {
			DocumentModel pubDossierDoc = norService.getDossierFromNOR(documentManager, pubNor);
			if (pubDossierDoc == null){
				continue;
			}
			Dossier pubDossierConjoint = pubDossierDoc.getAdapter(Dossier.class);
			DublincoreSchemaUtils.setModifiedDate(pubDossierDoc, Calendar.getInstance());
			if (pubDossierDoc != null) {
				if (pubNor.equals(dossier.getNumeroNor())) {
					// unrestricted n'est pas nécessaire sur le dossier cible
					documentManager.saveDocument(pubDossierDoc);
				} else {
					new UnrestrictedCreateOrSaveDocumentRunner(documentManager).saveDocument(pubDossierConjoint
							.getDocument());
				}
			}
		}
	}

	public String saveBordereau(DocumentModel dossierDoc) throws ClientException {
		final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
		final DossierBordereauService dbs = SolonEpgServiceLocator.getDossierBordereauService();
		final HistoriqueMajMinisterielleService historiqueMajService = SolonEpgServiceLocator
				.getHistoriqueMajMinisterielleService();
		final NORService norService = SolonEpgServiceLocator.getNORService();
		if (dossierDoc == null) {
			facesMessages.add(StatusMessage.Severity.ERROR, "Aucun dossier");
			return null;
		}

		LOGGER.info(documentManager, STLogEnumImpl.UPDATE_DOSSIER_FONC, dossierDoc);
		// vérification des champs si le dossier est lancé
		if (Dossier.DossierState.running.toString().equals(dossierDoc.getCurrentLifeCycleState())
				&& !isBordereauComplet(dossierDoc)) {
			String message = resourcesAccessor.getMessages().get("feedback.solonepg.document.bordereau.noncomplet");
			// on récupère la liste des métadonnées obligatoires non remplies
			message = message + getBordereauMetadonnesObligatoiresManquantes(dossierDoc);
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return null;
		}

		Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		workaroundOra00060Deadlock(dossierDoc, dossier);

		// vérifier que la date de signature est valide: > 01/01/1900
		if(dossier.getDateSignature()!= null && dossier.getDateSignature().before(DossierSolonEpgConstants.DATE_01_01_1900)) {
			String message = resourcesAccessor.getMessages().get(
					"feedback.solonepg.document.bordereau.dateSignatureIncorrecte");
			facesMessages.add(StatusMessage.Severity.WARN, resourcesAccessor.getMessages().get(message));
			return null;
		}

		// Message erreur si texte entreprise et aucune date d'effet renseignée
		if (dossier.getTexteEntreprise() && dossier.getDateEffet().isEmpty()) {
			String message = resourcesAccessor.getMessages().get(
					"feedback.solonepg.bordereau.date.effet.obligatoire.si.texte.entreprise");
			facesMessages.add(StatusMessage.Severity.WARN, message);
			return null;
		}
		// suppression du champ "publication date précisée" si le délai n'est pas "a date précisée"
		if (!"1".equals(dossier.getDelaiPublication())) {
			LOGGER.info(documentManager, STLogEnumImpl.UPDATE_DOSSIER_FONC,
					"Suppression du champ \"publication date précisée\"");
			dossier.setDatePreciseePublication(null);
		}

		// periodicite seulement dans le cas où PeriodiciteRapport = periodique
		if (!DossierSolonEpgConstants.RAPPORT_PERIODIQUE_PERIODICITE_ID.equals(dossier.getPeriodiciteRapport())) {
			dossier.setPeriodicite(null);
		}

		// on logge les modifications effectuées sur le bordereau.
		dbs.logAllDocumentUpdate(documentManager, dossierDoc);

		// on regarde si les modifications ont eu lieu sur les applications des lois, les transpositions ou les
		// ordonnances
		// afin de mettre à jour l'historique des maj ministérielles
		historiqueMajService.registerMajDossier(documentManager, dossierDoc);
		navigationContext.setCurrentDocument(dossierDoc);

		// mise à jour du dossier si le type d'acte change
		if (dossierService.updateDossierWhenTypeActeUpdated(dossierDoc, documentManager)) {
			// si le type d'acte a changé, on recharge le fond de dossier
			Events.instance().raiseEvent(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_TREE_CHANGED_EVENT);
			Events.instance().raiseEvent(SolonEpgParapheurConstants.PARAPHEUR_CHANGED_EVENT);
		}

		LOGGER.info(documentManager, STLogEnumImpl.SAVE_DOSSIER_FONC, dossierDoc);
		dossierDoc = documentManager.saveDocument(dossierDoc);
		dossier = dossierDoc.getAdapter(Dossier.class);

		// Event de rattachement de l'activite normative (post commit)
		final EventProducer eventProducer = STServiceLocator.getEventProducer();
		final Map<String, Serializable> eventProperties = new HashMap<String, Serializable>();
		eventProperties.put(SolonEpgANEventConstants.ACTIVITE_NORMATIVE_ATTACH_DOSSIER_DOCID_PARAM, dossier
				.getDocument().getId());
		final InlineEventContext inlineEventContext = new InlineEventContext(documentManager,
				documentManager.getPrincipal(), eventProperties);
		eventProducer.fireEvent(inlineEventContext.newEvent(SolonEpgANEventConstants.ACTIVITE_NORMATIVE_ATTACH_EVENT));

		// mise à jour du dossier de la fiche dr dans le cas de rapport au parlement et màj du de la fiche loi dans le
		// cas des types d'actes de type loi
		ActeService acteService = SolonEpgServiceLocator.getActeService();
		String typeActe = dossier.getTypeActe();
		if (acteService.isRapportAuParlement(typeActe) || acteService.isLoi(typeActe)) {
			final Map<String, Serializable> eventPropertiesFicheDR = new HashMap<String, Serializable>();
			eventPropertiesFicheDR.put(SolonEpgEventConstant.DOSSIER_EVENT_PARAM, dossier.getDocument());

			if (acteService.isRapportAuParlement(typeActe)) {
				eventPropertiesFicheDR.put(SolonEpgEventConstant.TYPE_ACTE_EVENT_PARAM,
						SolonEpgEventConstant.TYPE_ACTE_DR_EVENT_VALUE);
			} else if (acteService.isLoi(typeActe)) {
				eventPropertiesFicheDR.put(SolonEpgEventConstant.TYPE_ACTE_EVENT_PARAM,
						SolonEpgEventConstant.TYPE_ACTE_FL_EVENT_VALUE);
			}

			final InlineEventContext inlineEventContextFP = new InlineEventContext(documentManager,
					documentManager.getPrincipal(), eventPropertiesFicheDR);
			eventProducer.fireEvent(inlineEventContextFP
					.newEvent(SolonEpgEventConstant.BATCH_EVENT_UPDATE_FICHE_AFTER_UPDATE_BORDEREAU));
		}

		// Propagation des modifications aux publications conjointes du dossier
		List<String> publications = getPublicationsConjointes(dossierDoc);

		// Récupération des données à propager
		// Délai de publication
		final String delaiPublication = dossier.getDelaiPublication();
		// Publication à date précisée
		final Calendar datePreciseePublication = dossier.getDatePreciseePublication();
		// Mode de parution
		final RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
		final String modeParution = retourDila.getModeParution();

		// Parcours des publications conjointes du dossier courrant
		for (String pubNor : publications) {
			DocumentModel pubDossierDoc = norService.getDossierFromNOR(documentManager, pubNor);
			if(pubDossierDoc == null){		
				String message = resourcesAccessor.getMessages().get("feedback.solonepg.document.bordereau.publicationconjointe");
				message = message + pubNor;
				facesMessages.add(StatusMessage.Severity.WARN, message);
				continue;
			}
			Dossier pubDossierConjoint = pubDossierDoc.getAdapter(Dossier.class);
			if (pubDossierDoc != null) {
				dbs.propagatePublicationData(pubDossierConjoint, delaiPublication, datePreciseePublication,
						modeParution);
				new UnrestrictedCreateOrSaveDocumentRunner(documentManager).saveDocument(pubDossierConjoint
						.getDocument());
			}
		}

		// FEV509 - On ne déverouille plus le dossier lorsqu'on enregistre le bordereau

		// on signale que le dossier a changé : met à jour les informations du dossier dans la liste de sélection
		Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

		navigationContext.setCurrentDocument(dossierDoc);

		// force creation of new actions if needed
		webActions.getTabsList();

		// set current tab
		webActions.setCurrentTabId("TAB_DOSSIER_BORDEREAU");

		return null;
	}

	public boolean isBordereauComplet(final DocumentModel dossierDoc) {

		final ActeService acteService = SolonEpgServiceLocator.getActeService();
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

		final String typeActeId = dossier.getTypeActe();
		final Map<String, Boolean> mapVisibility = acteService.getActeVisibility(typeActeId);

		boolean isComplet = true;

		// Mantis #38727
		if (isPropertyEmpty(dossier.getTitreActe())) {
			isComplet = false;
		}

		// Nom resp dossier
		if (isPropertyEmpty(dossier.getNomRespDossier())) {
			isComplet = false;
		}

		// Qualité resp dossier
		if (isPropertyEmpty(dossier.getQualiteRespDossier())) {
			isComplet = false;
		}

		// Tél resp dossier
		if (isPropertyEmpty(dossier.getTelephoneRespDossier())) {
			isComplet = false;
		}

		if (mapVisibility.get(ActeVisibilityConstants.PUBLICATION)) {
			// Vecteur publication
			if (dossier.getVecteurPublication().size() < 1) {
				isComplet = false;
			}

			// Publication à date précisée
			if ("1".equals(dossier.getDelaiPublication()) && dossier.getDatePreciseePublication() == null) {
				isComplet = false;
			}
		}

		if (mapVisibility.get(ActeVisibilityConstants.INDEXATION)) {
			// Indexation : Rubrique
			if (dossier.getIndexationRubrique() == null || dossier.getIndexationRubrique().isEmpty()) {
				isComplet = false;
			}
		}

		// Raport Au Parlement
		String baseLegaleNatureTexte = dossier.getBaseLegaleNatureTexte();
		if (this.isRapportAuParlement() && (baseLegaleNatureTexte == null || baseLegaleNatureTexte.isEmpty())) {
			isComplet = false;
		}

		// Date effet pour dossier entreprise - FEV547
		if (dossier.getTexteEntreprise() && dossier.getDateEffet().isEmpty()) {
			isComplet = false;
		}
		return isComplet;
	}

	public String getBordereauMetadonnesObligatoiresManquantes(final DocumentModel dossierDoc) {

		DossierBordereauService bordereauService = SolonEpgServiceLocator.getDossierBordereauService();
		return bordereauService.getBordereauMetadonnesObligatoiresManquantes(dossierDoc);
	}

	private boolean isPropertyEmpty(final String property) {
		return StringUtils.isBlank(property);
	}

	private String getTypeActeId(final DocumentModel dossierDoc) {
		if (dossierDoc == null) {
			return "-1";
		} else {
			final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			return dossier.getTypeActe();
		}
	}

	public String getCurrentVecteur() {
		return currentVecteur;
	}

	public void setCurrentVecteur(final String currentVecteur) {
		this.currentVecteur = currentVecteur;
	}

	/**
	 * @param currentModeParution
	 *            the currentModeParution to set
	 */
	public void setCurrentModeParution(String currentModeParution) {
		this.currentModeParution = currentModeParution;
	}

	/**
	 * @return the currentModeParution
	 */
	public String getCurrentModeParution() {
		return currentModeParution;
	}

	public String getCurrentPublicationConjointe() {
		return currentPublicationConjointe;
	}

	public void setCurrentPublicationConjointe(final String currentPublicationConjointe) {
		this.currentPublicationConjointe = currentPublicationConjointe;
	}

	public List<String> getPublicationsConjointes(final DocumentModel dossierDoc) {
		final List<String> list = new ArrayList<String>();
		if (dossierDoc != null) {
			final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			list.addAll(dossier.getPublicationsConjointes());
		}
		return list;
	}

	public List<String> getPublicationsConjointesUnrestricted(final DocumentModel dossierDoc) {
		final List<String> list = new ArrayList<String>();
		if (dossierDoc != null) {
			final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			list.addAll(dossier.getPublicationsConjointesUnrestricted(documentManager));
		}
		return list;
	}

	public String addPublicationConjointe(final DocumentModel dossierDoc) throws ClientException {
		publicationConjointeError = "";

		if (currentPublicationConjointe.isEmpty()) {
			return null;
		}
		// on ne prend pas en compte les espaces qui peuvent être rajouté par copier-coller
		currentPublicationConjointe = currentPublicationConjointe.replaceAll(" ", "");
		// On regarde si on a des ; et des - (FEV509)
		final List<String> newPublicationsConjointes = StringUtil.split(currentPublicationConjointe, new String[] {
				";", "-" });

		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		final DossierBordereauService dbs = SolonEpgServiceLocator.getDossierBordereauService();

		final List<String> listNorNonTrouves = new ArrayList<String>(0);
		final List<String> listNorDejaPublies = new ArrayList<String>(0);
		if (!dbs.addPublicationsConjointes(documentManager, dossier, newPublicationsConjointes, listNorNonTrouves,
				listNorDejaPublies)) {
			if (!listNorNonTrouves.isEmpty()) {
				final String message = resourcesAccessor.getMessages().get("feedback.solonepg.aucuns.dossiers.nor");
				publicationConjointeError = message + " " + StringUtil.join(listNorNonTrouves, ", ", "") + ". ";
			}
			if (!listNorDejaPublies.isEmpty()) {
				final String message = resourcesAccessor.getMessages().get(
						"feedback.solonepg.dossiers.conjoints.deja.publies.error");
				publicationConjointeError = publicationConjointeError + message + " "
						+ StringUtil.join(listNorDejaPublies, ", ", "") + ". ";
				facesMessages.add(StatusMessage.Severity.WARN, message);
			}
		}

		currentPublicationConjointe = null;

		return null;
	}

	public void removePublicationConjointe(final DocumentModel dossierDoc, final String publication)
			throws ClientException {
		publicationConjointeError = null;
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		final DossierBordereauService dbs = SolonEpgServiceLocator.getDossierBordereauService();
		dbs.removePublicationConjointe(documentManager, dossier, publication);
	}

	/**
	 * Validation utilisée pour le numéro d'ordre de l'habilitation
	 * 
	 * @param facesContext
	 * @param uIComponent
	 * @param object
	 * @throws ValidatorException
	 */
	public void validateNumeroOrdre(final FacesContext facesContext, final UIComponent uIComponent, final Object object)
			throws ValidatorException {
		final String numeroOrdre = (String) object;
		if (StringUtils.isEmpty(numeroOrdre)) {
			return;
		}
		final DossierBordereauService dossierBordereauService = SolonEpgServiceLocator.getDossierBordereauService();
		try {
			if (!dossierBordereauService.isFieldNumeroOrdreValid(documentManager, numeroOrdre)) {
				final FacesMessage message = new FacesMessage();
				message.setSummary("Le numéro d'ordre n'est pas au bon format.");
				throw new ValidatorException(message);
			}
		} catch (final ClientException e) {
			final FacesMessage message = new FacesMessage();
			message.setSummary("Erreur lors de la vérification du format du numéro d'ordre.");
			throw new ValidatorException(message);
		}
	}

	// Getter & Setter

	public String getPublicationConjointeError() {
		return publicationConjointeError;
	}

	public void setPublicationConjointeError(final String publicationConjointeError) {
		this.publicationConjointeError = publicationConjointeError;
	}

	public boolean isPeriodiciteDuMoiVisisble(String periodicite) {
		if (periodicite != null && !periodicite.isEmpty()) {
			return DossierSolonEpgConstants.RAPPORT_PERIODIQUE_PERIODICITE_ID.equals(periodicite);
		}
		return false;
	}

	private Boolean isRapportAuParlement() {
		DocumentModel currentDoc = navigationContext.getCurrentDocument();
		if (currentDoc != null && currentDoc.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
			Dossier dossier = currentDoc.getAdapter(Dossier.class);
			if (dossier != null) {
				ActeService acteService = SolonEpgServiceLocator.getActeService();
				return acteService.isRapportAuParlement(dossier.getTypeActe());
			}
		}
		return Boolean.FALSE;
	}

	private Boolean isTexteNonPublie() {
		DocumentModel currentDoc = navigationContext.getCurrentDocument();
		if (currentDoc != null && currentDoc.hasSchema(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
			Dossier dossier = currentDoc.getAdapter(Dossier.class);
			if (dossier != null) {
				ActeService acteService = SolonEpgServiceLocator.getActeService();
				return acteService.isActeTexteNonPublie(dossier.getTypeActe());
			}
		}
		return Boolean.FALSE;
	}

	public boolean showWidget(String widgetName) {
		// Liste des éléments visibles uniquement pour les rapports au parlement
		List<String> raportAuParlementVisible = Arrays.asList("base_legale_date", "base_legale_nature_texte",
				"base_legale_numero_texte");
		// Liste des éléments invisibles pour les rapport au parlement
		List<String> raportAuParlementHidden = Arrays.asList("ref_texte_a_publie", "ref_texte_soumis_a_validation",
				"ref_signataire");

		if (raportAuParlementVisible.contains(widgetName)) {
			return this.isRapportAuParlement();
		}

		if (raportAuParlementHidden.contains(widgetName)) {
			// ce widget ne doit pas apparaitre pour les textes non publies
			if ("ref_texte_a_publie".equals(widgetName) && isTexteNonPublie()) {
				return false;
			}
			return !this.isRapportAuParlement();
		}

		return true;
	}

	public boolean isSectionCEEmpty(final DocumentModel dossierDoc) {
		ConseilEtat ce = dossierDoc.getAdapter(ConseilEtat.class);
		if (ce != null) {
			if ((ce.getPriorite() == null || ce.getPriorite().isEmpty())
					&& (ce.getSectionCe() == null || ce.getSectionCe().isEmpty())
					&& (ce.getRapporteurCe() == null || ce.getRapporteurCe().isEmpty())
					&& ce.getDateTransmissionSectionCe() == null && ce.getDateSectionCe() == null
					&& ce.getDateAgCe() == null && (ce.getNumeroISA() == null || ce.getNumeroISA().isEmpty())) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}

	public boolean isParutionJorfEmpty(final DocumentModel dossierDoc) {
		RetourDila doc = dossierDoc.getAdapter(RetourDila.class);
		if (doc != null && doc.getDateParutionJorf() == null
				&& (doc.getNumeroTexteParutionJorf() == null || doc.getNumeroTexteParutionJorf().isEmpty())
				&& doc.getPageParutionJorf() == null
				&& (doc.getTitreOfficiel() == null || doc.getTitreOfficiel().isEmpty())) {
			return true;
		} else if (doc == null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isSGGDILAEmpty(final DocumentModel dossierDoc) {
		Dossier doc = dossierDoc.getAdapter(Dossier.class);
		if (doc != null) {
			if (doc.getZoneComSggDila() == null || doc.getZoneComSggDila().isEmpty()) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public boolean isBaseLegaleEmpty(final DocumentModel dossierDoc) {
		Dossier doc = dossierDoc.getAdapter(Dossier.class);
		if (doc != null) {
			if ((doc.getBaseLegaleActe() == null || doc.getBaseLegaleActe().isEmpty()
					&& doc.getBaseLegaleDate() == null
					&& (doc.getBaseLegaleNatureTexte() == null || doc.getBaseLegaleNatureTexte().isEmpty()))
					&& (doc.getBaseLegaleNumeroTexte() == null || doc.getBaseLegaleNumeroTexte().isEmpty())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean isVisibleTexteEntreprise() {
		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		final String typeActeId = getTypeActeId(dossierDoc);
		// On détermine si on affice ou pas 'Texte entreprise' dans le bordereau - FEV547
		if (typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_PM)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_PR)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_CE)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_DECRET_CE)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_DECRET_CM)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_DECRET_CM_IND)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_DECRET_PR)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_DECRET_PR_CE)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_DECRET_DE_PUBLICATION_DE_TRAITE_OU_ACCORD)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND)
				|| typeActeId.equals(TypeActeConstants.TYPE_ACTE_ORDONNANCE)) {
			// On affiche
			return true;
		} else {
			// On affiche pas
			return false;
		}
	}

	public Date getCurrentDateEffetEntreprise() {
		return currentDateEffetEntreprise;
	}

	public void setCurrentDateEffetEntreprise(final Date currentDateEffetEntreprise) {
		this.currentDateEffetEntreprise = currentDateEffetEntreprise;
	}

	public void addCurrentDateEffetEntreprise() throws ClientException {
		dateEffetEntrepriseError = null;
		final Dossier dossier = navigationContext.getCurrentDocument().getAdapter(Dossier.class);
		final List<Calendar> datesEffet = dossier.getDateEffet();
		final Calendar today = Calendar.getInstance();
		if (currentDateEffetEntreprise != null && today.getTime().after(currentDateEffetEntreprise)) {
			// Erreur car il faut forcément que la date soit postérieure à la date du jour
			dateEffetEntrepriseError = resourcesAccessor.getMessages().get(
					"feedback.solonepg.bordereau.date.effet.inferieure.date.jour");
			;
		} else if (currentDateEffetEntreprise != null) {
			boolean alreadyPresent = false;
			for (Calendar dateSaved : datesEffet) {
				if (dateSaved.getTime().equals(currentDateEffetEntreprise)) {
					dateEffetEntrepriseError = resourcesAccessor.getMessages().get(
							"feedback.solonepg.bordereau.date.effet.deja.presente");
					alreadyPresent = true;
					break;
				}
			}

			if (!alreadyPresent) {
				Calendar calDateEffet = Calendar.getInstance();
				calDateEffet.setTime(currentDateEffetEntreprise);
				datesEffet.add(calDateEffet);
				dossier.setDateEffet(datesEffet);
				// Remise à zéro du champ currentDateEffetEntreprise
				currentDateEffetEntreprise = null;
			}
		}
	}

	public List<Date> getDatesEffetEntreprise() {
		List<Date> list = new ArrayList<Date>();
		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		if (dossierDoc != null) {
			final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			for (Calendar dateEffet : dossier.getDateEffet()) {
				list.add(dateEffet.getTime());
			}
		}
		Collections.sort(list);
		return list;
	}

	public void removeDateEffetEntreprise(final DocumentModel dossierDoc, final Date dateEffet) throws ClientException {
		dateEffetEntrepriseError = null;
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		Calendar dateEffetCalendar = Calendar.getInstance();
		dateEffetCalendar.setTime(dateEffet);
		List<Calendar> datesEffetDossier = dossier.getDateEffet();
		datesEffetDossier = new ArrayList<Calendar>(datesEffetDossier);
		datesEffetDossier.remove(dateEffetCalendar);
		dossier.setDateEffet(datesEffetDossier);
	}

	public String getDateEffetEntrepriseError() {
		return dateEffetEntrepriseError;
	}

	public void setDateEffetEntrepriseError(final String dateEffetEntrepriseError) {
		this.dateEffetEntrepriseError = dateEffetEntrepriseError;
	}

	public boolean isTexteEntrepriseCoche() throws ClientException {
		if (!isVisibleTexteEntreprise())
			return false;
		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		return dossier.getTexteEntreprise();
	}

	public void removeAllDatesEffetEntreprise() {
		final DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		if (dossierDoc != null) {
			final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			dossier.setDateEffet(new ArrayList<Calendar>());
		}
	}
}
