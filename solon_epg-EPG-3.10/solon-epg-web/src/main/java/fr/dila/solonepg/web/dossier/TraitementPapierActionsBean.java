package fr.dila.solonepg.web.dossier;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.common.utils.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.nuxeo.ecm.platform.ui.web.util.files.FileUtils;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManagerBean;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.UploadItem;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication;
import fr.dila.solonepg.api.cases.typescomplexe.InfoEpreuve;
import fr.dila.solonepg.api.cases.typescomplexe.InfoHistoriqueAmpliation;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants;
import fr.dila.solonepg.api.constant.SolonEpgTraitementPapierConstants;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.service.AmpliationService;
import fr.dila.solonepg.api.service.ListeTraitementPapierService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.api.service.TraitementPapierService;
import fr.dila.solonepg.core.cases.typescomplexe.DestinataireCommunicationImpl;
import fr.dila.solonepg.core.cases.typescomplexe.InfoEpreuveImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.context.NavigationContextBean;
import fr.dila.ss.web.birt.BirtReportActionsBean;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.web.dossier.DossierLockActionsBean;

@Name("papierActions")
@Scope(ScopeType.CONVERSATION)
public class TraitementPapierActionsBean implements Serializable {

	private static final long							serialVersionUID	= 1L;

	private static final Logger							logger				= Logger.getLogger(TraitementPapierActionsBean.class);

	@In(create = true, required = true)
	protected transient CoreSession						documentManager;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean	routingWebActions;

	@In(required = true, create = true)
	protected NuxeoPrincipal							currentUser;

	@In(create = true, required = false)
	protected transient NavigationContextBean			navigationContext;

	@In(create = true, required = true)
	protected transient BirtReportActionsBean			birtReportingActions;

	@In(create = true, required = false)
	protected transient DocumentsListsManagerBean		documentsListsManager;

	public String										mailObject;

	public String										mailText;

	public List<String>									destinatairePm		= new ArrayList<String>();

	public List<String>									destinataireCm		= new ArrayList<String>();

	public List<String>									destinataireAutres	= new ArrayList<String>();

	public String										destinataireIndex;

	public String										typeDestinataire;

	/**
	 * Liste des fichiers du répertoire courant.
	 */
	protected UploadItem								uploadData;

	/**
	 * Propriété liées à l'affichage des erreurs.
	 */
	protected String									errorName;

	public List<String>									destinataires;

	@In(create = true, required = false)
	protected FacesMessages								facesMessages;

	@In(create = true)
	protected ResourcesAccessor							resourcesAccessor;

	@In(create = true, required = false)
	protected transient DossierLockActionsBean			dossierLockActions;

	public TraitementPapierActionsBean() {

	}

	/**
	 * Teste si l'utliisateur a la fonction unitaire TraitementPapierWriter
	 * 
	 * @return vrai si l'utilisateur peut écrire au nivau du traitement papier
	 */
	public boolean canCurrentUserWrite() {
		return currentUser.isMemberOf(SolonEpgBaseFunctionConstant.TRAITEMENT_PAPIER_WRITER);

	}

	public String navigateToEspaceSuiviInfoCentreSGG() {

		documentsListsManager.resetWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);

		return SolonEpgViewConstant.ESPACE_SUIVI_INFOCENTRE_SGG;

	}

	public void selectDestinataire() {

		// List<String> destinatairesdx = this.getDestinatires();

		if ("nxw_communication_dest_pm".equals(typeDestinataire)) {
			if (destinatairePm.contains(destinataireIndex)) {
				destinatairePm.remove(destinataireIndex);
			} else {
				destinatairePm.add(destinataireIndex);
			}
		} else if ("nxw_communication_dest_charge_mission".equals(typeDestinataire)) {
			if (destinataireCm.contains(destinataireIndex)) {
				destinataireCm.remove(destinataireIndex);
			} else {
				destinataireCm.add(destinataireIndex);
			}
		} else if ("nxw_communication_dest_autres".equals(typeDestinataire)) {
			if (destinataireAutres.contains(destinataireIndex)) {
				destinataireAutres.remove(destinataireIndex);
			} else {
				destinataireAutres.add(destinataireIndex);
			}
		}
	}

	public void removeDestinataire() throws Exception {

		if ("nxw_communication_dest_pm".equals(typeDestinataire)) {
			if (destinatairePm.contains(destinataireIndex)) {
				destinatairePm.remove(destinataireIndex);
			}
		} else if ("nxw_communication_dest_charge_mission".equals(typeDestinataire)) {
			if (destinataireCm.contains(destinataireIndex)) {
				destinataireCm.remove(destinataireIndex);
			}
		} else if ("nxw_communication_dest_autres".equals(typeDestinataire)) {
			if (destinataireAutres.contains(destinataireIndex)) {
				destinataireAutres.remove(destinataireIndex);
			}
		}

	}

	/**
	 * 
	 * Retourne le contenu d'une table de reference identifié par la valeur de refType
	 * 
	 * 
	 * 
	 * @param refType
	 *            SolonEpgTraitementPapierConstants.REFERENCE_TYPE_CABINET_PM pour les valeur 'Cabinet PM',
	 *            SolonEpgTraitementPapierConstants .REFERENCE_TYPE_CHARGES_MISSION pour les valeur 'Charges mission'
	 *            SolonEpgTraitementPapierConstants.REFERENCE_TYPE_SIGNATAIRE pour les valeurs 'signataires'
	 * 
	 * @return
	 * 
	 * @throws ClientException
	 */

	public List<Map<String, String>> getReferences(String refType) throws ClientException {

		final STUserService stUserService = STServiceLocator.getSTUserService();

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
		DocumentModel tableReferenceDoc = tableReferenceService.getTableReference(documentManager);
		TableReference tableReference = tableReferenceDoc.getAdapter(TableReference.class);
		if (SolonEpgTraitementPapierConstants.REFERENCE_TYPE_CABINET_PM.equals(refType)) {
			addElement("", "Sélectionner une valeur", list);
			if (tableReference.getCabinetPM() != null) {
				for (String idUser : tableReference.getCabinetPM()) {
					addElement(idUser, stUserService.getUserInfo(idUser, STUserService.CIVILITE_ABREGEE_PRENOM_NOM),
							list);
				}
			}
		} else if (SolonEpgTraitementPapierConstants.REFERENCE_TYPE_CHARGES_MISSION.equals(refType)) {
			addElement("", "Sélectionner une valeur", list);
			if (tableReference.getChargesMission() != null) {
				for (String idUser : tableReference.getChargesMission()) {
					addElement(idUser, stUserService.getUserInfo(idUser, STUserService.CIVILITE_ABREGEE_PRENOM_NOM),
							list);
				}
			}
		} else if (SolonEpgTraitementPapierConstants.REFERENCE_TYPE_SIGNATAIRE.equals(refType)) {
			addElement("", "Sélectionner une valeur", list);
			if (tableReference.getSignataires() != null) {
				for (String idUser : tableReference.getSignataires()) {
					addElement(idUser, stUserService.getUserInfo(idUser, STUserService.CIVILITE_ABREGEE_PRENOM_NOM),
							list);
				}
			}
		} else if (SolonEpgTraitementPapierConstants.REFERENCE_TYPE_SIGNATURE_SGG.equals(refType)) {
			addElement("", "Sélectionner une valeur", list);
			if (tableReference.getSignatureSGG() != null) {
				for (String idUser : tableReference.getSignatureSGG()) {
					addElement(idUser, stUserService.getUserInfo(idUser, STUserService.CIVILITE_ABREGEE_PRENOM_NOM),
							list);
				}
			}
		} else if (SolonEpgTraitementPapierConstants.REFERENCE_TYPE_SIGNATURE_CPM.equals(refType)) {
			addElement("", "Sélectionner une valeur", list);
			if (tableReference.getSignatureCPM() != null) {
				for (String idUser : tableReference.getSignatureCPM()) {
					addElement(idUser, stUserService.getUserInfo(idUser, STUserService.CIVILITE_ABREGEE_PRENOM_NOM),
							list);
				}
			}
		} else if (SolonEpgTraitementPapierConstants.REFERENCE_TYPE_COMMUNICATION_DIRECTEUR.equals(refType)) {
			addElement("", "Sélectionner une valeur", list);
			addElement("Directeur", "Directeur", list);
		}

		return list;
	}

	private void addElement(String ident, String value, List<Map<String, String>> list) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", ident);
		map.put("value", value);
		list.add(map);
	}

	/**
	 * 
	 * Test si l'option PM a été coché sur l'onget REFERENCE
	 * 
	 * @param dossierDoc
	 * @return
	 */

	public Boolean isSignatairePMSelected(DocumentModel dossierDoc) {
		TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
		return SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_PM.equals(traitementPapier.getSignataire());
	}

	/**
	 * 
	 * Test si l'option PR a été coché sur l'onget REFERENCE
	 * 
	 * @param dossierDoc
	 * 
	 * @return
	 */

	public Boolean isSignatairePRSelected(DocumentModel dossierDoc) {
		TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
		return SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_PR.equals(traitementPapier.getSignataire());
	}

	/**
	 * 
	 * Test si l'option SGG a été coché sur l'onget REFERENCE
	 * 
	 * @param dossierDoc
	 * 
	 * @return
	 */

	public Boolean isSignataireSGGSelected(DocumentModel dossierDoc) {
		TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
		return SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_SGG.equals(traitementPapier.getSignataire());
	}

	/**
	 * public
	 * 
	 * Test si une valeur est presente dans les signataires
	 * 
	 * publicde la table de reference
	 * 
	 * @param dossierDoc
	 * 
	 * 
	 *            public
	 * @return
	 */

	public Boolean isSignataireInRef(String signataire) throws ClientException {
		TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
		DocumentModel tableReferenceDoc = tableReferenceService.getTableReference(documentManager);
		TableReference tableReference = tableReferenceDoc.getAdapter(TableReference.class);
		return tableReference.getSignataires().contains(signataire);
	}

	/**
	 * get la valeur de l'objet
	 * 
	 * @param dossier
	 * @return
	 */
	public String objectValue(Dossier dossier) {
		TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
		Boolean texteAPublier = traitementPapier.getTexteAPublier();
		Boolean hasSignataire = !SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_AUCUN.equals(traitementPapier
				.getSignataire());
		if (hasSignataire) {
			if (texteAPublier) {
				return SolonEpgTraitementPapierConstants.COMMUNICATION_OBJET_AVIS_AVANT_PUBLIC_SIGN;

			} else {
				return SolonEpgTraitementPapierConstants.COMMUNICATION_OBJET_AVIS_AVANT_SIGN;
			}
		} else {
			if (texteAPublier) {
				return SolonEpgTraitementPapierConstants.COMMUNICATION_OBJET_AVIS_AVANT_PUBLIC;
			} else {
				return SolonEpgTraitementPapierConstants.COMMUNICATION_OBJET_AVIS;
			}
		}

	}

	/**
	 * 
	 * Initialise les valeurs par défaut pour les informations sur les signataires de l'onglet Communication du
	 * traitement papier.
	 * 
	 * @param dossierDoc
	 * @param widgetValues
	 */
	public void checkDefaultValue(DocumentModel dossierDoc, Map<String, Serializable> widgetValues) {

		logger.debug("checkDefaultValue ");

		Object val = widgetValues.get(SolonEpgTraitementPapierConstants.SCHEMA_COMMUNICATION_DESTINATAIRE_OBJET);
		if (val == null) {
			Dossier dossier = dossierDoc.getAdapter(Dossier.class);
			widgetValues.put(SolonEpgTraitementPapierConstants.SCHEMA_COMMUNICATION_DESTINATAIRE_OBJET,
					objectValue(dossier));
		}
	}

	/**
	 * enregisterer les infos du traitement papaier
	 * 
	 * @param dossierDoc
	 * @throws ClientException
	 */
	public void saveTraitementPapier(DocumentModel dossierDoc) throws ClientException {
		final TraitementPapierService traitementPapierService = SolonEpgServiceLocator.getTraitementPapierService();
		traitementPapierService.saveTraitementPapier(documentManager, dossierDoc);
		// Affiche un message de confirmation
		facesMessages.add(StatusMessage.Severity.INFO,
				resourcesAccessor.getMessages().get("label.epg.papier.message.save.ok"));

		final EventProducer eventProducer = STServiceLocator.getEventProducer();
		final Map<String, Serializable> eventPropertiesFicheDR = new HashMap<String, Serializable>();
		eventPropertiesFicheDR.put(SolonEpgEventConstant.DOSSIER_EVENT_PARAM, dossierDoc);
		eventPropertiesFicheDR.put(SolonEpgEventConstant.TYPE_ACTE_EVENT_PARAM,
				SolonEpgEventConstant.TYPE_ACTE_FL_EVENT_VALUE);

		final InlineEventContext inlineEventContextFP = new InlineEventContext(documentManager,
				documentManager.getPrincipal(), eventPropertiesFicheDR);
		eventProducer.fireEvent(inlineEventContextFP
				.newEvent(SolonEpgEventConstant.BATCH_EVENT_UPDATE_FICHE_AFTER_UPDATE_BORDEREAU));

	}

	/**
	 * 
	 * @param dossierDoc
	 * @return
	 * @throws ClientException
	 */
	public List<DocumentModel> getListeTraitementPapierMiseEnSinature(DocumentModel dossierDoc) throws ClientException {

		// DocumentModel listeTraitementPapier = null;
		final ListeTraitementPapierService listeTraitementPapierService = SolonEpgServiceLocator
				.getListeTraitementPapierService();
		List<DocumentModel> list = listeTraitementPapierService.getListeTraitementPapierOfDossierAndType(
				documentManager, dossierDoc.getId(),
				SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_MISE_EN_SIGNATURE);
		return list;
	}

	/**
	 * 
	 * C'est une fonction qui assure la jointure des mail dans la liste
	 * 
	 * @param mail
	 * 
	 * @return
	 */

	public String mailListToString(List<String> mail) {
		if (mail == null) {
			return "";
		} else {
			return StringUtils.join(mail, ";");
		}
	}

	/**
	 * 
	 * @param dossierDoc
	 * @return
	 */
	public List<InfoHistoriqueAmpliation> getInfoAmpilation(DocumentModel dossierDoc) {
		TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
		List<InfoHistoriqueAmpliation> result = traitementPapier.getAmpliationHistorique();
		if (result == null) {
			result = new ArrayList<InfoHistoriqueAmpliation>();
		}
		return result;

	}

	/**
	 * 
	 * @param dossierDoc
	 * @return
	 */
	public String getMail(DocumentModel dossierDoc) {
		TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
		String result = "";
		List<String> mails = traitementPapier.getAmpliationDestinataireMails();
		if (mails == null) {
			result = "Null List";
		} else if (mails.isEmpty()) {
			result = "Empty List";
		} else {
			result = StringUtils.join(mails, ";");
		}
		return result;

	}

	/**
	 * valider l'ampliation
	 * 
	 * @param dossierDoc
	 * @return
	 * @throws ClientException
	 * @throws FileNotFoundException
	 */
	public String validerAmpliation(DocumentModel dossierDoc) throws ClientException, FileNotFoundException {

		try {
			AmpliationService ampliationService = SolonEpgServiceLocator.getAmpliationService();
			if (!validateEmailAdress(dossierDoc)) {
				facesMessages.add(StatusMessage.Severity.WARN,
						resourcesAccessor.getMessages().get("selection.user.ampliation.pas.de.destinataire.error"));
			}

			else if (!validateAmpliation(dossierDoc)) {
				facesMessages.add(StatusMessage.Severity.WARN,
						resourcesAccessor.getMessages().get("selection.user.ampliation.pas.de.fichier.error"));
			}

			else {
				documentManager.saveDocument(dossierDoc);
				ampliationService.sendAmpliationMail(documentManager, dossierDoc, mailObject, mailText);
			}
		}

		catch (Exception e) {
			facesMessages.add(StatusMessage.Severity.ERROR,
					resourcesAccessor.getMessages().get("selection.user.ampliation.error.envoie"));
		}

		return routingWebActions.getFeuilleRouteView();

	}

	public String getMailText() {
		return mailText;

	}

	public void mailTextChanged(ValueChangeEvent event) {
		mailText = (String) event.getNewValue();
	}

	public void setDefaultMailTextAndObject() throws ClientException {
		AmpliationService ampliationService = SolonEpgServiceLocator.getAmpliationService();
		DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		mailObject = ampliationService.getAmpliationMailObject(documentManager, dossierDoc);
		mailText = ampliationService.getAmpliationMailText(documentManager, dossierDoc);
	}

	public void setMailText(String mailText) {
		this.mailText = mailText;
	}

	public String getMailObject() {
		return mailObject;

	}

	public void setMailObject(String mailObject) {

		this.mailObject = mailObject;
	}

	/**
	 * 
	 * Listener sur l'upload d'un fichier : on ajoute le fichier dans la liste
	 */

	public void fileUploadListener(UploadEvent event) throws Exception {

		if (event == null || event.getUploadItem() == null || event.getUploadItem().getFileName() == null) {
			errorName = "Le fichier est vide";
		} else {
			// recupèration du fichier
			uploadData = event.getUploadItem();
			errorName = "";
		}

	}

	/**
	 * clear les donnes uploade
	 * 
	 * @param event
	 */
	public void clearUploadData(ActionEvent event) {

		uploadData = null;

		// nettoyage des erreurs
		errorName = "";
	}

	/**
	 * refraichier l'entete destinataire dans la demande d'epreuve
	 * 
	 * @return
	 * @throws ClientException
	 */
	public String refreshDestinataire() throws ClientException {
		return refreshEntete(false);
	}

	/**
	 * refraichier l'entete destinataire dans la demande de la nouvelle epreuve
	 * 
	 * @return
	 * @throws ClientException
	 */
	public String refreshNouveauDestinataire() throws ClientException {

		return refreshEntete(true);

	}

	/**
	 * recuperer le nim du fichire d'ampliation
	 * 
	 * @param dossierDoc
	 *            le dossier Document
	 * @return
	 * @throws ClientException
	 */
	public String getNomAmpliationFichier(DocumentModel dossierDoc) throws ClientException {

		AmpliationService ampliationService = SolonEpgServiceLocator.getAmpliationService();
		return ampliationService.getNomAmpliationFichier(dossierDoc, documentManager);
	}

	/**
	 * supprimer le Fichier d'ampliation
	 * 
	 * @param dossierDoc
	 * @return
	 * @throws ClientException
	 */
	public String removeAmpliationFichier(DocumentModel dossierDoc) throws ClientException {
		AmpliationService ampliationService = SolonEpgServiceLocator.getAmpliationService();
		ampliationService.supprimerAmpliationFichierUnrestricted(documentManager, dossierDoc);
		return routingWebActions.getFeuilleRouteView();

	}

	/**
	 * ajouter un Fichier d'ampliation
	 * 
	 * @param dossierDoc
	 *            le dossier Document
	 * @return
	 * @throws ClientException
	 * @throws FileNotFoundException
	 */
	public String saveUploadedFile(DocumentModel dossierDoc) throws ClientException, FileNotFoundException {

		AmpliationService ampliationService = SolonEpgServiceLocator.getAmpliationService();
		if (uploadData == null) {
			errorName = "pas de fichier selectionn";
		} else {
			// for (UploadItem uploadItem : uploadData) {
			Blob blob = FileUtils.createSerializableBlob(new FileInputStream(uploadData.getFile()),
					uploadData.getFileName(), null);
			ampliationService.ajouterAmpliationFichier(dossierDoc, uploadData.getFileName(), blob, documentManager);
			// }
		}

		return routingWebActions.getFeuilleRouteView();

	}

	/**
	 * annuler l'ampliation
	 * 
	 * @param dossierDoc
	 *            le dossier Document
	 * @throws Exception
	 */
	public String annulerAmpliationForm(DocumentModel dossierDoc) throws ClientException {
		TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
		traitementPapier.setAmpliationDestinataireMails(new ArrayList<String>());
		traitementPapier.save(documentManager);
		this.removeAmpliationFichier(dossierDoc);
		return routingWebActions.getFeuilleRouteView();

	}

	/**
	 * imprimer le bordereau d'envoi de chemin de croix
	 * 
	 * @param dossierDoc
	 *            le dossier Document
	 * @throws Exception
	 */
	public void imprimerCheminDeCroix(DocumentModel dossierDoc) throws Exception {

		final TraitementPapierService traitementPapierService = SolonEpgServiceLocator.getTraitementPapierService();
		traitementPapierService.saveTraitementPapier(documentManager, navigationContext.getCurrentDocument());
		TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
		String destSGG = traitementPapier.getSignatureDestinataireSGG();
		String destCPM = traitementPapier.getSignatureDestinataireCPM();

		if (destSGG == null || destSGG.isEmpty()) {
			destSGG = ".";
		}

		if (destCPM == null || destCPM.isEmpty()) {
			destCPM = ".";
		}

		String resolveddestSGG = resolveName(destSGG, true, true);
		String resolveddestdestCPM = resolveName(destCPM, true, true);

		if (resolveddestSGG != null) {
			destSGG = resolveddestSGG;
		}

		if (resolveddestdestCPM != null) {
			destCPM = resolveddestdestCPM;
		}

		HashMap<String, String> inputValues = new HashMap<String, String>();

		inputValues.put("DOSSIERID_PARAM", traitementPapier.getDocument().getId());
		inputValues.put("SIGNATAIRE_PARAM", destSGG);
		inputValues.put("A_SIGNATAIRE_PARAM", destCPM);
		birtReportingActions.generateWord(SolonEpgConstant.BIRT_REPORT_BORDEREAU_CHEMIN_DE_CROIX, inputValues,
				"Bordereau_retour_a_envoyeur");
	}

	/**
	 * imprimer le bordereau d'envoi en relecture pour la demande d'epreuve
	 * 
	 * @param dossierDoc
	 *            le dossier Document
	 * @throws Exception
	 */
	public void imprimerDemandeEpreuve(DocumentModel dossierDoc) throws Exception {

		TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
		String dest = traitementPapier.getEpreuve().getDestinataireEntete();

		String signataire = traitementPapier.getEpreuve().getNomsSignataires();

		if (signataire == null || signataire.isEmpty()) {
			signataire = ".";
		}

		if (dest == null || dest.isEmpty()) {
			dest = ".";
		}

		String resolvedDest = resolveName(dest, true, false);
		String signataireFonction = getUserFonction(signataire);
		String resolvedSignataire = resolveName(signataire, false, false);
		if (resolvedDest != null) {
			dest = resolvedDest;
		}

		if (resolvedSignataire != null) {
			signataire = resolvedSignataire;
		}

		// RG-SUI-PAP-31 : les champs date « Envoi en relecture » et « Envoi en
		// relecture de la nouvelle demande » sont alimentés automatiquement
		// avec la date du jour d’édition des bordereaux
		Calendar dua = Calendar.getInstance();
		InfoEpreuve infoEpreuve = traitementPapier.getEpreuve();
		if (infoEpreuve == null) {
			infoEpreuve = new InfoEpreuveImpl();
		}
		infoEpreuve.setEnvoiRelectureLe(dua);
		traitementPapier.setEpreuve(infoEpreuve);

		documentManager.saveDocument(dossierDoc);

		HashMap<String, String> inputValues = new HashMap<String, String>();
		inputValues.put("DOSSIERID_PARAM", traitementPapier.getDocument().getId());
		inputValues.put("DESTINATAIRE_PARAM", dest);
		inputValues.put("SIGNATAIRE_PARAM", signataire);
		inputValues.put("SIGNATAIRE_FONCTION_PARAM", signataireFonction);
		birtReportingActions.generateWord(SolonEpgConstant.BIRT_REPORT_BORDEREAU_ENVOI_EPREUVE_EN_RELECTURE,
				inputValues, "Bordereau_retour_a_envoyeur");

	}

	/**
	 * imprimer le bordereau d'envoi en relecture pour la demande de la nouvelle epreuve
	 * 
	 * @param dossierDoc
	 *            le dossier Document
	 * @throws Exception
	 */
	public void imprimerNouvelleEpreuve(DocumentModel dossierDoc) throws Exception {
		TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
		String dest = traitementPapier.getNouvelleDemandeEpreuves().getDestinataireEntete();

		String signataire = traitementPapier.getNouvelleDemandeEpreuves().getNomsSignataires();
		if (signataire == null || signataire.isEmpty()) {
			signataire = ".";
		}

		if (dest == null || dest.isEmpty()) {
			dest = ".";
		}

		String resolvedDest = resolveName(dest, true, false);
		String signataireFonction = getUserFonction(signataire);
		String resolvedSignataire = resolveName(signataire, false, false);
		if (resolvedDest != null) {
			dest = resolvedDest;
		}

		if (resolvedSignataire != null) {
			signataire = resolvedSignataire;
		}

		// RG-SUI-PAP-31 : les champs date « Envoi en relecture » et « Envoi en
		// relecture de la nouvelle demande » sont alimentés automatiquement
		// avec la date du jour d’édition des bordereaux
		Calendar dua = Calendar.getInstance();
		InfoEpreuve infoEpreuve = traitementPapier.getNouvelleDemandeEpreuves();
		if (infoEpreuve == null) {
			infoEpreuve = new InfoEpreuveImpl();
		}
		infoEpreuve.setEnvoiRelectureLe(dua);
		traitementPapier.setNouvelleDemandeEpreuves(infoEpreuve);

		documentManager.saveDocument(dossierDoc);

		HashMap<String, String> inputValues = new HashMap<String, String>();
		inputValues.put("DOSSIERID_PARAM", traitementPapier.getDocument().getId());
		inputValues.put("DESTINATAIRE_PARAM", dest);
		inputValues.put("SIGNATAIRE_PARAM", signataire);
		inputValues.put("SIGNATAIRE_FONCTION_PARAM", signataireFonction);
		birtReportingActions.generateWord(SolonEpgConstant.BIRT_REPORT_BORDEREAU_ENVOI_EPREUVE_EN_RELECTURE,
				inputValues, "Bordereau_retour_a_envoyeur_pour_relecture");

	}

	/**
	 * imprimer le bordereau de retour
	 * 
	 * @param dossierDoc
	 *            le dossier
	 * 
	 * @throws Exception
	 */

	public void imprimerRetour(DocumentModel dossierDoc) throws Exception {

		TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
		traitementPapier.setDateRetour(Calendar.getInstance());
		String dest = traitementPapier.getRetourA();
		if (dest == null || dest.isEmpty()) {
			dest = ".";
		}

		String sig = traitementPapier.getNomsSignatairesRetour();
		if (sig == null || sig.isEmpty()) {
			sig = ".";
		}

		String resolvedDest = resolveName(dest, true, false);
		String signataireFonction = getUserFonction(sig);
		String resolvedSignataire = resolveName(sig, false, false);

		if (resolvedDest != null) {
			dest = resolvedDest;
		}

		if (resolvedSignataire != null) {
			sig = resolvedSignataire;
		}

		HashMap<String, String> inputValues = new HashMap<String, String>();

		documentManager.saveDocument(dossierDoc);
		inputValues.put("DOSSIERID_PARAM", traitementPapier.getDocument().getId());
		inputValues.put("DESTINATAIRE_PARAM", dest);
		inputValues.put("SIGNATAIRE_PARAM", sig);
		inputValues.put("SIGNATAIRE_FONCTION_PARAM", signataireFonction);

		birtReportingActions.generateWord(SolonEpgConstant.BIRT_REPORT_BORDEREAU_ENVOI_DE_RETOUR, inputValues,
				"Bordereau_retour_a_envoyeur");

	}

	/**
	 * imprimer le bordereau de la communication
	 * 
	 * @param dossierDoc
	 *            le dossier
	 * 
	 * @throws Exception
	 */
	public void imprimerCommunication(DocumentModel dossierDoc) throws Exception {

		StringBuilder dest = new StringBuilder();

		TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);

		List<DestinataireCommunication> cabinetPmCommunicationList = traitementPapier.getCabinetPmCommunication();
		List<DestinataireCommunication> chargeMissionCommunicationList = traitementPapier
				.getChargeMissionCommunication();
		List<DestinataireCommunication> autresDestinatairesCommunicationList = traitementPapier
				.getAutresDestinatairesCommunication();

		List<DestinataireCommunication> updatedCabinetPmCommunicationList = new ArrayList<DestinataireCommunication>();
		List<DestinataireCommunication> updatedChargeMissionCommunicationList = new ArrayList<DestinataireCommunication>();
		List<DestinataireCommunication> updatedAutresDestinatairesCommunicationList = new ArrayList<DestinataireCommunication>();

		Integer index = 0;
		for (DestinataireCommunication cabinetPmCommunication : cabinetPmCommunicationList) {
			String cabinetPmCommunicationDest = cabinetPmCommunication.getNomDestinataireCommunication();

			DestinataireCommunication destinataireCommunication = new DestinataireCommunicationImpl();
			destinataireCommunication.setDateRetour(cabinetPmCommunication.getDateRetour());
			destinataireCommunication.setNomDestinataireCommunication(cabinetPmCommunicationDest);
			destinataireCommunication.setDateRelance(cabinetPmCommunication.getDateRelance());
			destinataireCommunication.setSensAvis(cabinetPmCommunication.getSensAvis());
			destinataireCommunication.setObjet(cabinetPmCommunication.getObjet());

			if (destinatairePm.contains(index.toString()) && cabinetPmCommunicationDest != null
					&& !cabinetPmCommunicationDest.trim().equals("")) {

				dest.append("$name$=").append(resolveName(cabinetPmCommunicationDest, false, false))
						.append(";;$object$=").append(cabinetPmCommunication.getObjet()).append(";;$title$=")
						.append(getCivilite(cabinetPmCommunicationDest)).append(";;&");

				destinataireCommunication.setDateEnvoi(Calendar.getInstance());
			} else {
				destinataireCommunication.setDateEnvoi(cabinetPmCommunication.getDateEnvoi());
			}

			updatedCabinetPmCommunicationList.add(destinataireCommunication);
			
			documentManager.save();

			index++;
		}

		index = 0;
		for (DestinataireCommunication chargeMissionCommunication : chargeMissionCommunicationList) {

			String chargeMissionCommunicationDest = chargeMissionCommunication.getNomDestinataireCommunication();

			DestinataireCommunication destinataireCommunication = new DestinataireCommunicationImpl();
			destinataireCommunication.setDateRetour(chargeMissionCommunication.getDateRetour());
			destinataireCommunication.setNomDestinataireCommunication(chargeMissionCommunicationDest);
			destinataireCommunication.setDateRelance(chargeMissionCommunication.getDateRelance());
			destinataireCommunication.setSensAvis(chargeMissionCommunication.getSensAvis());
			destinataireCommunication.setObjet(chargeMissionCommunication.getObjet());

			if (destinataireCm.contains(index.toString()) && chargeMissionCommunicationDest != null
					&& !chargeMissionCommunicationDest.trim().equals("")) {

				dest.append("$name$=").append(resolveName(chargeMissionCommunicationDest, false, false))
						.append(";;$object$=").append(chargeMissionCommunication.getObjet()).append(";;$title$=")
						.append(getCivilite(chargeMissionCommunicationDest)).append(";;&");

				destinataireCommunication.setDateEnvoi(Calendar.getInstance());
			} else {
				destinataireCommunication.setDateEnvoi(chargeMissionCommunication.getDateEnvoi());
			}

			updatedChargeMissionCommunicationList.add(destinataireCommunication);
			
			documentManager.save();

			index++;
		}

		index = 0;
		for (DestinataireCommunication autresDestinatairesCommunication : autresDestinatairesCommunicationList) {

			String autresDestinatairesCommunicationDest = autresDestinatairesCommunication
					.getNomDestinataireCommunication();

			DestinataireCommunication destinataireCommunication = new DestinataireCommunicationImpl();
			destinataireCommunication.setDateRetour(autresDestinatairesCommunication.getDateRetour());
			destinataireCommunication.setNomDestinataireCommunication(autresDestinatairesCommunicationDest);
			destinataireCommunication.setDateRelance(autresDestinatairesCommunication.getDateRelance());
			destinataireCommunication.setSensAvis(autresDestinatairesCommunication.getSensAvis());
			destinataireCommunication.setObjet(autresDestinatairesCommunication.getObjet());

			if (destinataireAutres.contains(index.toString()) && autresDestinatairesCommunicationDest != null
					&& !autresDestinatairesCommunicationDest.trim().equals("")) {

				dest.append("$name$=").append(autresDestinatairesCommunicationDest).append(";;$object$=")
						.append(autresDestinatairesCommunication.getObjet()).append(";;$title$=;;&");

				destinataireCommunication.setDateEnvoi(Calendar.getInstance());
			} else {
				destinataireCommunication.setDateEnvoi(autresDestinatairesCommunication.getDateEnvoi());
			}

			updatedAutresDestinatairesCommunicationList.add(destinataireCommunication);
			
			documentManager.save();

			index++;
		}

		String signataire = traitementPapier.getNomsSignatairesCommunication();

		if (signataire == null || signataire.isEmpty()) {
			signataire = ".";
		}

		String signataireFonction = getUserFonction(signataire);
		String resolvedSignataire = resolveName(signataire, false, false);

		if (resolvedSignataire != null) {
			signataire = resolvedSignataire;
		}

		traitementPapier.setCabinetPmCommunication(updatedCabinetPmCommunicationList);
		traitementPapier.setChargeMissionCommunication(updatedChargeMissionCommunicationList);
		traitementPapier.setAutresDestinatairesCommunication(updatedAutresDestinatairesCommunicationList);

		traitementPapier.save(documentManager);

		documentManager.save();

		destinatairePm = new ArrayList<String>();
		destinataireCm = new ArrayList<String>();
		destinataireAutres = new ArrayList<String>();

		HashMap<String, String> inputValues = new HashMap<String, String>();
		inputValues.put("DOSSIERID_PARAM", traitementPapier.getDocument().getId());
		inputValues.put("DESTINATAIRES_PARAM", dest.toString());
		inputValues.put("SIGNATAIRE_PARAM", signataire);
		inputValues.put("SIGNATAIRE_FONCTION_PARAM", signataireFonction);
		inputValues.put("RELANCE_PARAM", "false");
		if (traitementPapier.getTexteSoumisAValidation() == true) {
			birtReportingActions.generateWord(
					SolonEpgConstant.BIRT_REPORT_BORDEREAU_DE_COMMUNICATION_SOUMIS_A_VALIDATION, inputValues,
					"Bordereau_communication");
		} else {
			birtReportingActions.generateWord(SolonEpgConstant.BIRT_REPORT_BORDEREAU_DE_COMMUNICATION, inputValues,
					"Bordereau_communication");

		}

	}

	/**
	 * imprimer le bordereau de la relance
	 * 
	 * @param liste
	 * 
	 * @throws Exception
	 */
	public void imprimerCommunicationRelance(DocumentModel dossierDoc) throws Exception {

		StringBuilder dest = new StringBuilder();
		String oldDate = " ";
		final SimpleDateFormat formatter = DateUtil.simpleDateFormat("dd/MM/yyyy");
		TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);

		List<DestinataireCommunication> cabinetPmCommunicationList = traitementPapier.getCabinetPmCommunication();
		List<DestinataireCommunication> chargeMissionCommunicationList = traitementPapier
				.getChargeMissionCommunication();
		List<DestinataireCommunication> autresDestinatairesCommunicationList = traitementPapier
				.getAutresDestinatairesCommunication();

		List<DestinataireCommunication> updatedCabinetPmCommunicationList = new ArrayList<DestinataireCommunication>();
		List<DestinataireCommunication> updatedChargeMissionCommunicationList = new ArrayList<DestinataireCommunication>();
		List<DestinataireCommunication> updatedAutresDestinatairesCommunicationList = new ArrayList<DestinataireCommunication>();

		Integer index = 0;
		for (DestinataireCommunication cabinetPmCommunication : cabinetPmCommunicationList) {
			String cabinetPmCommunicationDest = cabinetPmCommunication.getNomDestinataireCommunication();

			DestinataireCommunication destinataireCommunication = new DestinataireCommunicationImpl();
			destinataireCommunication.setDateRetour(cabinetPmCommunication.getDateRetour());
			destinataireCommunication.setNomDestinataireCommunication(cabinetPmCommunicationDest);
			destinataireCommunication.setDateEnvoi(cabinetPmCommunication.getDateEnvoi());
			destinataireCommunication.setSensAvis(cabinetPmCommunication.getSensAvis());
			destinataireCommunication.setObjet(cabinetPmCommunication.getObjet());

			if (destinatairePm.contains(index.toString()) && cabinetPmCommunicationDest != null
					&& !cabinetPmCommunicationDest.trim().equals("")) {
				oldDate = " ";

				if (cabinetPmCommunication.getDateEnvoi() != null) {
					oldDate = formatter.format(cabinetPmCommunication.getDateEnvoi().getTime());
				}

				dest.append("$name$=").append(resolveName(cabinetPmCommunicationDest, false, false))
						.append(";;$object$=").append(cabinetPmCommunication.getObjet()).append(";;$title$=")
						.append(getCivilite(cabinetPmCommunicationDest)).append(";;").append("$oldDate$=")
						.append(oldDate).append("&").append(";;");

				destinataireCommunication.setDateRelance(Calendar.getInstance());
			} else {
				destinataireCommunication.setDateRelance(cabinetPmCommunication.getDateRelance());
			}

			updatedCabinetPmCommunicationList.add(destinataireCommunication);

			index++;
		}

		index = 0;
		for (DestinataireCommunication chargeMissionCommunication : chargeMissionCommunicationList) {

			String chargeMissionCommunicationDest = chargeMissionCommunication.getNomDestinataireCommunication();

			DestinataireCommunication destinataireCommunication = new DestinataireCommunicationImpl();
			destinataireCommunication.setDateRetour(chargeMissionCommunication.getDateRetour());
			destinataireCommunication.setNomDestinataireCommunication(chargeMissionCommunicationDest);
			destinataireCommunication.setDateEnvoi(chargeMissionCommunication.getDateEnvoi());
			destinataireCommunication.setSensAvis(chargeMissionCommunication.getSensAvis());
			destinataireCommunication.setObjet(chargeMissionCommunication.getObjet());

			if (destinataireCm.contains(index.toString()) && chargeMissionCommunicationDest != null
					&& !chargeMissionCommunicationDest.trim().equals("")) {
				oldDate = " ";
				if (chargeMissionCommunication.getDateEnvoi() != null) {
					oldDate = formatter.format(chargeMissionCommunication.getDateEnvoi().getTime());
				}

				dest.append("$name$=").append(resolveName(chargeMissionCommunicationDest, false, false))
						.append(";;$object$=").append(chargeMissionCommunication.getObjet()).append(";;$title$=")
						.append(getCivilite(chargeMissionCommunicationDest)).append(";;").append("$oldDate$=")
						.append(oldDate).append(";;").append("&");

				destinataireCommunication.setDateRelance(Calendar.getInstance());
			} else {
				destinataireCommunication.setDateRelance(chargeMissionCommunication.getDateRelance());
			}

			updatedChargeMissionCommunicationList.add(destinataireCommunication);

			index++;
		}

		index = 0;
		for (DestinataireCommunication autresDestinatairesCommunication : autresDestinatairesCommunicationList) {

			String autresDestinatairesCommunicationDest = autresDestinatairesCommunication
					.getNomDestinataireCommunication();

			DestinataireCommunication destinataireCommunication = new DestinataireCommunicationImpl();
			destinataireCommunication.setDateRetour(autresDestinatairesCommunication.getDateRetour());
			destinataireCommunication.setNomDestinataireCommunication(autresDestinatairesCommunicationDest);
			destinataireCommunication.setDateEnvoi(autresDestinatairesCommunication.getDateEnvoi());
			destinataireCommunication.setSensAvis(autresDestinatairesCommunication.getSensAvis());
			destinataireCommunication.setObjet(autresDestinatairesCommunication.getObjet());

			if (destinataireAutres.contains(index.toString()) && autresDestinatairesCommunicationDest != null
					&& !autresDestinatairesCommunicationDest.trim().equals("")) {
				oldDate = " ";
				if (autresDestinatairesCommunication.getDateEnvoi() != null) {
					oldDate = formatter.format(autresDestinatairesCommunication.getDateEnvoi().getTime());
				}

				dest.append("$name$=").append(resolveName(autresDestinatairesCommunicationDest, false, false))
						.append(";;$object$=").append(autresDestinatairesCommunication.getObjet()).append(";;$title$=")
						.append(getCivilite(autresDestinatairesCommunicationDest)).append(";;").append("$oldDate$=")
						.append(oldDate).append(";;").append("&");

				destinataireCommunication.setDateRelance(Calendar.getInstance());
			} else {
				destinataireCommunication.setDateRelance(autresDestinatairesCommunication.getDateRelance());
			}

			updatedAutresDestinatairesCommunicationList.add(destinataireCommunication);

			index++;
		}

		String signataire = traitementPapier.getNomsSignatairesCommunication();
		if (signataire == null || signataire.isEmpty()) {
			signataire = ".";
		}

		String signataireFonction = getUserFonction(signataire);
		String resolvedSignataire = resolveName(signataire, false, false);
		if (resolvedSignataire != null) {
			signataire = resolvedSignataire;
		}

		traitementPapier.setCabinetPmCommunication(updatedCabinetPmCommunicationList);
		traitementPapier.setChargeMissionCommunication(updatedChargeMissionCommunicationList);
		traitementPapier.setAutresDestinatairesCommunication(updatedAutresDestinatairesCommunicationList);

		traitementPapier.save(documentManager);

		documentManager.save();

		destinatairePm = new ArrayList<String>();
		destinataireCm = new ArrayList<String>();
		destinataireAutres = new ArrayList<String>();

		HashMap<String, String> inputValues = new HashMap<String, String>();
		inputValues.put("DOSSIERID_PARAM", traitementPapier.getDocument().getId());
		inputValues.put("DESTINATAIRES_PARAM", dest.toString());
		inputValues.put("SIGNATAIRE_PARAM", signataire);
		inputValues.put("SIGNATAIRE_FONCTION_PARAM", signataireFonction);
		inputValues.put("RELANCE_PARAM", "true");
		if (traitementPapier.getTexteSoumisAValidation() == true) {
			birtReportingActions.generateWord(
					SolonEpgConstant.BIRT_REPORT_BORDEREAU_DE_COMMUNICATION_SOUMIS_A_VALIDATION, inputValues,
					"Bordereau_communication");
		} else {
			birtReportingActions.generateWord(SolonEpgConstant.BIRT_REPORT_BORDEREAU_DE_COMMUNICATION, inputValues,
					"Bordereau_communication");

		}
	}

	/**
	 * imprimer le bordereau de communication au cabinet du Secrétaire général
	 * 
	 * @param liste
	 * 
	 * @throws Exception
	 */
	public void imprimerCommunicationCabinetSg(DocumentModel dossierDoc) throws Exception {

		TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);

		List<DestinataireCommunication> cabinetSgDestinatairesCommunicationList = traitementPapier
				.getCabinetSgCommunication();
		List<DestinataireCommunication> updatedcabinetSgCommunicationList = new ArrayList<DestinataireCommunication>();

		for (DestinataireCommunication cabinetSgCommunication : cabinetSgDestinatairesCommunicationList) {
			String cabinetPmCommunicationDest = cabinetSgCommunication.getNomDestinataireCommunication();
			DestinataireCommunication destinataireCommunication = new DestinataireCommunicationImpl();
			destinataireCommunication.setDateRetour(cabinetSgCommunication.getDateRetour());
			destinataireCommunication.setNomDestinataireCommunication(cabinetPmCommunicationDest);
			destinataireCommunication.setDateEnvoi(Calendar.getInstance());
			destinataireCommunication.setSensAvis(cabinetSgCommunication.getSensAvis());
			destinataireCommunication.setObjet(cabinetSgCommunication.getObjet());
			updatedcabinetSgCommunicationList.add(destinataireCommunication);
			documentManager.save();
		}

		traitementPapier.setCabinetSgCommunication(updatedcabinetSgCommunicationList);
		traitementPapier.save(documentManager);
		documentManager.save();

		String signataire = traitementPapier.getNomsSignatairesCommunication();
		if (signataire == null || signataire.isEmpty()) {
			signataire = ".";
		}

		String signataireFonction = getUserFonction(signataire);
		String resolvedSignataire = resolveName(signataire, false, false);
		if (resolvedSignataire != null) {
			signataire = resolvedSignataire;
		}

		HashMap<String, String> inputValues = new HashMap<String, String>();
		inputValues.put("DOSSIERID_PARAM", traitementPapier.getDocument().getId());
		inputValues.put("SIGNATAIRE_PARAM", signataire);
		inputValues.put("SIGNATAIRE_FONCTION_PARAM", signataireFonction);
		birtReportingActions.generateWord(SolonEpgConstant.BIRT_REPORT_BORDEREAU_DE_COMMUNICATION_CABINET_SG,
				inputValues, "Bordereau_communication");
	}

	public UploadItem getUploadData() {
		return uploadData;
	}

	public void setUploadData(UploadItem uploadData) {
		this.uploadData = uploadData;
	}

	public String getErrorName() {
		return errorName;
	}

	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}

	public List<String> getDestinatires() {
		return destinataires;
	}

	public void setDestinatires(List<String> destinataires) {
		this.destinataires = destinataires;
	}

	public List<String> getDestinatairePm() {
		return destinatairePm;
	}

	public void setDestinatairePm(List<String> destinatairePm) {
		this.destinatairePm = destinatairePm;
	}

	public List<String> getDestinataireCm() {
		return destinataireCm;
	}

	public void setDestinataireCm(List<String> destinataireCm) {
		this.destinataireCm = destinataireCm;
	}

	public List<String> getDestinataireAutres() {
		return destinataireAutres;
	}

	public void setDestinataireAutres(List<String> destinataireAutres) {
		this.destinataireAutres = destinataireAutres;
	}

	public String getDestinataireIndex() {
		return destinataireIndex;
	}

	public void setDestinataireIndex(String destinataireIndex) {
		this.destinataireIndex = destinataireIndex;
	}

	public String getTypeDestinataire() {
		return typeDestinataire;
	}

	public void setTypeDestinataire(String typeDestinataire) {
		this.typeDestinataire = typeDestinataire;
	}

	public List<String> getDestinataires() {
		return destinataires;
	}

	public void setDestinataires(List<String> destinataires) {
		this.destinataires = destinataires;
	}

	/**
	 * 
	 * @param retreouver
	 *            le nom Complet de l'utilistaur (avec ou sans civilite)
	 * @param isWithCivilite
	 *            flag : true => ajouter la civilite dans le nom otherwise false => retourner le nom sans civilite
	 * @return
	 * @throws ClientException
	 */
	private String resolveName(String userName, boolean isWithCivilite, boolean abregee) throws ClientException {

		final UserManager userManager = STServiceLocator.getUserManager();
		DocumentModel docUser = userManager.getUserModel(userName);
		String result = "";
		if (docUser == null) {
			result = userName;

		} else {

			STUser stUser = docUser.getAdapter(STUser.class);
			String firstName = stUser.getFirstName();
			String lastName = stUser.getLastName();
			String civilite = stUser.getTitle();
			StringBuilder nameBuilder = new StringBuilder();

			if (isWithCivilite == true && civilite != null && !civilite.trim().equals("")) {
				nameBuilder.append(civilite).append(" ");
			}

			if (firstName != null && !firstName.trim().equals("")) {
				nameBuilder.append(firstName).append(" ");
			}

			if (lastName != null && !lastName.trim().equals("")) {
				nameBuilder.append(lastName);
			}
			result = nameBuilder.toString();
		}

		if (abregee) {
			result = result.replace("Monsieur", "M.");
			result = result.replace("Madame", "Mme.");
		}

		return result;

	}

	/**
	 * get user emplyee type (fonction)
	 * 
	 * @param userName
	 * @return
	 * @throws ClientException
	 */
	private String getUserFonction(String userName) throws ClientException {

		final UserManager userManager = STServiceLocator.getUserManager();
		DocumentModel docUser = userManager.getUserModel(userName);
		String result = "";
		if (docUser != null) {
			STUser stUser = docUser.getAdapter(STUser.class);
			result = stUser.getEmployeeType();
		}
		return result;

	}

	/**
	 * retourner la civilite d'un utilisateur
	 * 
	 * @param userName
	 * @return
	 * @throws ClientException
	 */
	private String getCivilite(String userName) throws ClientException {

		final UserManager userManager = STServiceLocator.getUserManager();
		DocumentModel docUser = userManager.getUserModel(userName);
		String result = "";

		if (docUser != null) {
			STUser stUser = docUser.getAdapter(STUser.class);
			String civilite = stUser.getTitle();
			result = "";

			if (civilite != null && !civilite.trim().equals("")) {
				result = civilite;
			}
		}
		return result;
	}

	/**
	 * teste si les adresses emails des destinataires d'ampliation sont non vide
	 * 
	 * @param dossierDoc
	 *            le dossier
	 * 
	 * @return true dans le cas ou les adresses emails des destinataires d'ampliation sont non vide
	 */
	private boolean validateEmailAdress(DocumentModel dossierDoc) {

		TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
		if (traitementPapier.getAmpliationDestinataireMails() == null
				|| traitementPapier.getAmpliationDestinataireMails().isEmpty()) {
			return false;
		}
		return true;

	}

	/**
	 * test si le fichier d'ampliation n'est pas vide
	 * 
	 * @param dossierDoc
	 * @return
	 * @throws ClientException
	 */
	private boolean validateAmpliation(DocumentModel dossierDoc) throws ClientException {

		AmpliationService ampliationService = SolonEpgServiceLocator.getAmpliationService();
		if (ampliationService.getAmpliationFichier(dossierDoc, documentManager) != null) {
			return true;
		}
		return false;
	}

	/**
	 * refraichier l'entete destinataire dans la rubrique epreuves du traitement papaier
	 * 
	 * @return
	 * @throws ClientException
	 */
	private String refreshEntete(Boolean nouvelledemande) throws ClientException {

		String entete = "";

		// EpgOrganigrammeService epgOrganigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
		DocumentModel currentDocument = navigationContext.getCurrentDocument();
		TraitementPapier traitementPapier = currentDocument.getAdapter(TraitementPapier.class);
		Dossier dossier = currentDocument.getAdapter(Dossier.class);
		EntiteNode ministere = STServiceLocator.getSTMinisteresService().getEntiteNode(dossier.getMinistereResp());

		if (ministere != null) {
			entete = ministere.getFormule();
		}

		if (nouvelledemande) {
			InfoEpreuve infoEpreuve = traitementPapier.getNouvelleDemandeEpreuves();
			if (infoEpreuve != null) {
				infoEpreuve.setDestinataireEntete(entete);
			}
			traitementPapier.setNouvelleDemandeEpreuves(infoEpreuve);
		} else {
			InfoEpreuve infoEpreuve = traitementPapier.getEpreuve();
			if (infoEpreuve != null) {
				infoEpreuve.setDestinataireEntete(entete);
			}
			traitementPapier.setEpreuve(infoEpreuve);
		}

		return null;

	}

	public boolean isDipslayEditerCheminCroix() {
		DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		return "4".equals(dossier.getTypeActe()) || "38".equals(dossier.getTypeActe())
				|| "13".equals(dossier.getTypeActe()) || "14".equals(dossier.getTypeActe())
				|| "40".equals(dossier.getTypeActe()) || "15".equals(dossier.getTypeActe())
				|| "41".equals(dossier.getTypeActe()) || "16".equals(dossier.getTypeActe())
				|| "42".equals(dossier.getTypeActe()) || "17".equals(dossier.getTypeActe())
				|| "43".equals(dossier.getTypeActe()) || "18".equals(dossier.getTypeActe())
				|| "44".equals(dossier.getTypeActe()) || "19".equals(dossier.getTypeActe())
				|| "26".equals(dossier.getTypeActe()) || "27".equals(dossier.getTypeActe())
				|| "28".equals(dossier.getTypeActe()) || "29".equals(dossier.getTypeActe())
				|| "3".equals(dossier.getTypeActe()) || "34".equals(dossier.getTypeActe());

	}

	public boolean isDossierDeTypeArrete() {
		DocumentModel dossierDoc = navigationContext.getCurrentDocument();
		Dossier dossier = dossierDoc.getAdapter(Dossier.class);
		return "2".equals(dossier.getTypeActe()) || "3".equals(dossier.getTypeActe())
				|| "4".equals(dossier.getTypeActe()) || "5".equals(dossier.getTypeActe())
				|| "36".equals(dossier.getTypeActe()) || "37".equals(dossier.getTypeActe())
				|| "38".equals(dossier.getTypeActe()) || "39".equals(dossier.getTypeActe());
	}

}
