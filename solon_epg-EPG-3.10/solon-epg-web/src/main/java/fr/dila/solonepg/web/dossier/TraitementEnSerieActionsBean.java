package fr.dila.solonepg.web.dossier;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;

import fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants;
import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.documentmodel.ListeTraitementPapier;
import fr.dila.solonepg.api.service.ListeTraitementPapierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.st.core.util.DateUtil;

@Name("traitementEnSerieActions")
@Scope(ScopeType.PAGE)
public class TraitementEnSerieActionsBean {

    @In(create = true, required = true)
    protected transient CoreSession documentManager;
    @In(create = true)
    protected transient DocumentsListsManager documentsListsManager;
    final ListeTraitementPapierService listeTraitementPapierService = SolonEpgServiceLocator.getListeTraitementPapierService();
    /**
     * Date de retour de signature PM
     */
    private Date dateRetourPM;
    /**
     * Date de retour de signature SGG
     */
    private Date dateRetourSGG;
    /**
     * Date de retour de signature PR
     */
    private Date dateRetourPR;
    /**
     * Date d'envoi de signature PM
     */
    private Date dateEnvoiPR;
    /**
     * Date de retour de demande d'epreuve
     */
    private Date datePourLe;

    /**
     * methode pour tester si la liste de Traitement Papier est de type mise en signature
     * 
     * @param listeTraitementPapierDoc la liste de Traitement Papier document
     * @return
     */
    public Boolean isMiseEnSignatureList(DocumentModel listeTraitementPapierDoc) {
        ListeTraitementPapier listeTraitementPapier = listeTraitementPapierDoc.getAdapter(ListeTraitementPapier.class);
        return SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_MISE_EN_SIGNATURE.equals(listeTraitementPapier.getTypeListe());
    }

    /**
     * methode pour tester si la liste de Traitement Papier est de type demande d'epreuves
     * 
     * @param listeTraitementPapierDoc la liste de Traitement Papier document
     * @return
     */
    public Boolean isDemandeEpreuvesList(DocumentModel listeTraitementPapierDoc) {
        ListeTraitementPapier listeTraitementPapier = listeTraitementPapierDoc.getAdapter(ListeTraitementPapier.class);
        return SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE.equals(listeTraitementPapier.getTypeListe());
    }

    /**
     * methode pour tester si la liste de Traitement Papier est de type demande de publication
     * 
     * @param listeTraitementPapierDoc la liste de Traitement Papier document
     * @return
     */
    public Boolean isDemandeDePublicationList(DocumentModel listeTraitementPapierDoc) {
        ListeTraitementPapier listeTraitementPapier = listeTraitementPapierDoc.getAdapter(ListeTraitementPapier.class);
        return SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION.equals(listeTraitementPapier.getTypeListe());
    }

    /**
     * methode pour tester si la liste de Traitement Papier est de type signataire SGG
     * 
     * @param listeTraitementPapierDoc la liste de Traitement Papier document
     * @return
     */
    public Boolean isSignataireSGGList(DocumentModel listeTraitementPapierDoc) {
        ListeTraitementPapier listeTraitementPapier = listeTraitementPapierDoc.getAdapter(ListeTraitementPapier.class);
        return SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_SGG.equals(listeTraitementPapier
                .getTypeSignature());
    }

    /**
     * methode pour tester si la liste de Traitement Papier est de type signataire PR
     * 
     * @param listeTraitementPapierDoc la liste de Traitement Papier document
     * @return
     */
    public Boolean isSignatairePRList(DocumentModel listeTraitementPapierDoc) {
        ListeTraitementPapier listeTraitementPapier = listeTraitementPapierDoc.getAdapter(ListeTraitementPapier.class);
        return SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_PR
                .equals(listeTraitementPapier.getTypeSignature());
    }

    /**
     * methode pour tester si la liste de Traitement Papier est de type signataire PM
     * 
     * @param listeTraitementPapierDoc la liste de Traitement Papier document
     * @return
     */
    public Boolean isSignatairePMList(DocumentModel listeTraitementPapierDoc) {
        ListeTraitementPapier listeTraitementPapier = listeTraitementPapierDoc.getAdapter(ListeTraitementPapier.class);
        return SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_PM
                .equals(listeTraitementPapier.getTypeSignature());
    }

    /**
     * Action pour traiter en serie les liste de demande d'epreuve et de signature
     * 
     * @return l'ecran de Gestion de liste
     * @throws ClientException
     */
    public String traiterEnSerie(DocumentModel listeTraitementPapierDoc) throws ClientException {

        List<DocumentModel> docs = documentsListsManager.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);

        if (isSignataireSGGList(listeTraitementPapierDoc) && dateRetourSGG!=null) {
            traiterSignataireSGGList(docs);
        }
        if (isSignatairePMList(listeTraitementPapierDoc) && dateRetourPM!=null) {
            traiterSignatairePMList(docs);
        }
        if (isSignatairePRList(listeTraitementPapierDoc) && (dateEnvoiPR!=null || dateRetourPR!=null)) {
            traiterSignatairePRList(docs);
        }
        if (isDemandeEpreuvesList(listeTraitementPapierDoc) && datePourLe!=null) {
            traiterDemandeEpreuve(docs);
        }

        Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);

        return SolonEpgViewConstant.ESPACE_SUIVI_INFOCENTRE_SGG;
    }

    /**
     * Traiter les documents de signature sgg
     * 
     * @param docs
     * @throws ClientException
     */
    private void traiterSignataireSGGList(List<DocumentModel> docs) throws ClientException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateRetourSGG);
        listeTraitementPapierService.traiterEnSerieSignataireSGGUnrestricted(documentManager, docs, calendar);
    }

    /**
     * Traiter les documents de signature PM
     * 
     * @param docs
     * @throws ClientException
     */
    private void traiterSignatairePMList(List<DocumentModel> docs) throws ClientException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateRetourPM);
        listeTraitementPapierService.traiterEnSerieSignatairePMUnrestricted(documentManager, docs, calendar);
    }

    /**
     * Traiter les documents de signature PR
     * 
     * @param docs
     * @throws ClientException
     */
    private void traiterSignatairePRList(List<DocumentModel> docs) throws ClientException {
        Calendar cRetour = null, cEnvoi = null;
        if(dateRetourPR!=null) {
          cRetour = Calendar.getInstance();
          cRetour.setTime(dateRetourPR);
        }
        if(dateEnvoiPR != null) {
          cEnvoi = Calendar.getInstance();
          cEnvoi.setTime(dateEnvoiPR);
        }
        listeTraitementPapierService.traiterEnSerieSignatairePRUnrestricted(documentManager, docs, cRetour, cEnvoi);
    }

    /**
     * Traiter les documents de demande d'epreuve
     * 
     * @param docs
     * @throws ClientException
     */
    private void traiterDemandeEpreuve(List<DocumentModel> docs) throws ClientException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datePourLe);
        listeTraitementPapierService.traiterEnSerieDemandeEpreuveUnrestricted(documentManager, docs, calendar);
    }

    public Date getDateRetourPM() {
        return DateUtil.copyDate(dateRetourPM);
    }

    public void setDateRetourPM(Date dateRetourPM) {
        this.dateRetourPM = DateUtil.copyDate(dateRetourPM);
    }

    public Date getDateRetourSGG() {
        return DateUtil.copyDate(dateRetourSGG);
    }

    public void setDateRetourSGG(Date dateRetourSGG) {
        this.dateRetourSGG = DateUtil.copyDate(dateRetourSGG);
    }

    public Date getDateRetourPR() {
        return DateUtil.copyDate(dateRetourPR);
    }

    public void setDateRetourPR(Date dateRetourPR) {
        this.dateRetourPR = DateUtil.copyDate(dateRetourPR);
    }

    public Date getDateEnvoiPR() {
        return DateUtil.copyDate(dateEnvoiPR);
    }

    public void setDateEnvoiPR(Date dateEnvoiPR) {
        this.dateEnvoiPR = DateUtil.copyDate(dateEnvoiPR);
    }

    public Date getDatePourLe() {
        return DateUtil.copyDate(datePourLe);
    }

    public void setDatePourLe(Date datePourLe) {
        this.datePourLe = DateUtil.copyDate(datePourLe);
    }

}
