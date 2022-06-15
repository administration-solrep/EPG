package fr.dila.solonepg.ui.th.bean;

import static fr.dila.st.api.constant.STProfilUtilisateurConstants.PROFIL_UTILISATEUR_DOCUMENT_TYPE;

import fr.dila.solonepg.api.constant.SolonEpgProfilUtilisateurConstants;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import java.util.ArrayList;
import javax.ws.rs.FormParam;

@SwBean
public class ProfilParametersForm {
    @SwNotEmpty
    @NxProp(
        xpath = SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_NB_DOSSIER_XPATH,
        docType = PROFIL_UTILISATEUR_DOCUMENT_TYPE
    )
    @FormParam("nbDossierPage")
    private Long nbDossierPage;

    @NxProp(
        xpath = SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_POSTE_DEFAUT_XPATH,
        docType = PROFIL_UTILISATEUR_DOCUMENT_TYPE
    )
    @SwNotEmpty
    @FormParam("defaultPoste")
    private String defaultPoste;

    @NxProp(
        xpath = SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_COLUMNS_XPATH,
        docType = PROFIL_UTILISATEUR_DOCUMENT_TYPE
    )
    @FormParam("metadatasDossiers")
    private ArrayList<String> dossiersMetadatas = new ArrayList<>();

    @NxProp(
        xpath = SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_INSTALL_SOLON_EDIT_XPATH,
        docType = PROFIL_UTILISATEUR_DOCUMENT_TYPE
    )
    @FormParam("solonedit")
    private boolean installSolonedit;

    @NxProp(
        xpath = SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_VAL_AUTO_ETAP_XPATH,
        docType = PROFIL_UTILISATEUR_DOCUMENT_TYPE
    )
    @FormParam("valAutoEtape")
    private boolean valAutoEtape;

    @NxProp(
        xpath = SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_MAJ_CONSEIL_ETAT_XPATH,
        docType = PROFIL_UTILISATEUR_DOCUMENT_TYPE
    )
    @FormParam("majConseilEtat")
    private boolean majConseilEtat;

    @NxProp(
        xpath = SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SUR_SIGNATURE_XPATH,
        docType = PROFIL_UTILISATEUR_DOCUMENT_TYPE
    )
    @FormParam("surSignature")
    private boolean surSignature;

    @NxProp(
        xpath = SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_RETOUR_SGG_XPATH,
        docType = PROFIL_UTILISATEUR_DOCUMENT_TYPE
    )
    @FormParam("retourSGG")
    private boolean retourSGG;

    @NxProp(
        xpath = SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_MESURE_NOMINATIVE_XPATH,
        docType = PROFIL_UTILISATEUR_DOCUMENT_TYPE
    )
    @FormParam("mesuresNominatives")
    private boolean mesuresNominatives;

    @NxProp(
        xpath = SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_TYPE_ACTE_ID_XPATH,
        docType = PROFIL_UTILISATEUR_DOCUMENT_TYPE
    )
    @FormParam("typeActe")
    private ArrayList<String> typeActe = new ArrayList<>();

    public ProfilParametersForm() {
        super();
    }

    public Long getNbDossierPage() {
        return nbDossierPage;
    }

    public void setNbDossierPage(Long nbDossierPage) {
        this.nbDossierPage = nbDossierPage;
    }

    public String getDefaultPoste() {
        return defaultPoste;
    }

    public void setDefaultPoste(String defaultPoste) {
        this.defaultPoste = defaultPoste;
    }

    public ArrayList<String> getDossiersMetadatas() {
        return dossiersMetadatas;
    }

    public void setDossiersMetadatas(ArrayList<String> dossiersMetadatas) {
        this.dossiersMetadatas = dossiersMetadatas;
    }

    public boolean isInstallSolonedit() {
        return installSolonedit;
    }

    public void setInstallSolonedit(boolean installSolonedit) {
        this.installSolonedit = installSolonedit;
    }

    public boolean isValAutoEtape() {
        return valAutoEtape;
    }

    public void setValAutoEtape(boolean valAutoEtape) {
        this.valAutoEtape = valAutoEtape;
    }

    public boolean isMajConseilEtat() {
        return majConseilEtat;
    }

    public void setMajConseilEtat(boolean majConseilEtat) {
        this.majConseilEtat = majConseilEtat;
    }

    public boolean isSurSignature() {
        return surSignature;
    }

    public void setSurSignature(boolean surSignature) {
        this.surSignature = surSignature;
    }

    public boolean isRetourSGG() {
        return retourSGG;
    }

    public void setRetourSGG(boolean retourSGG) {
        this.retourSGG = retourSGG;
    }

    public boolean isMesuresNominatives() {
        return mesuresNominatives;
    }

    public void setMesuresNominatives(boolean mesuresNominatives) {
        this.mesuresNominatives = mesuresNominatives;
    }

    public ArrayList<String> getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(ArrayList<String> typeActe) {
        this.typeActe = typeActe;
    }
}
