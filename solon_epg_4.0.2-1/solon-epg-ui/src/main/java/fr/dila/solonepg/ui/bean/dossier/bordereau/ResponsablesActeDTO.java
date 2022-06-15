package fr.dila.solonepg.ui.bean.dossier.bordereau;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.ui.annot.NxProp;
import java.util.List;

public class ResponsablesActeDTO {

    public ResponsablesActeDTO() {
        super();
    }

    private String intituleMinistereResp;

    private String intituleDirectionResp;

    private String intituleMinistereRattach;

    private String intituleDirectionRattach;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_NOM_RESPONSABLE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String nomResponsable;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_PRENOM_RESPONSABLE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String prenomResponsable;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_QUALITE_RESPONSABLE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String qualiteResponsable;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_TEL_RESPONSABLE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String telResponsable;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_MAIL_RESPONSABLE_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String melResponsable;

    @NxProp(
        xpath = STSchemaConstant.DOSSIER_SCHEMA_PREFIX + ":" + DossierSolonEpgConstants.DOSSIER_ID_CREATEUR_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private String creePar;

    private String creeParLibelle;

    private String creeParEmail;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_NOM_COMPLET_CHARGES_MISSIONS_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private List<String> chargeMission;

    @NxProp(
        xpath = DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX +
        ":" +
        DossierSolonEpgConstants.DOSSIER_NOM_COMPLET_CONSEILLERS_PMS_PROPERTY,
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE
    )
    private List<String> conseillerPM;

    public String getIntituleMinistereResp() {
        return intituleMinistereResp;
    }

    public void setIntituleMinistereResp(String intituleMinistereResp) {
        this.intituleMinistereResp = intituleMinistereResp;
    }

    public String getIntituleDirectionResp() {
        return intituleDirectionResp;
    }

    public void setIntituleDirectionResp(String intituleDirectionResp) {
        this.intituleDirectionResp = intituleDirectionResp;
    }

    public String getIntituleMinistereRattach() {
        return intituleMinistereRattach;
    }

    public void setIntituleMinistereRattach(String intituleMinistereRattach) {
        this.intituleMinistereRattach = intituleMinistereRattach;
    }

    public String getIntituleDirectionRattach() {
        return intituleDirectionRattach;
    }

    public void setIntituleDirectionRattach(String intituleDirectionRattach) {
        this.intituleDirectionRattach = intituleDirectionRattach;
    }

    public String getNomResponsable() {
        return nomResponsable;
    }

    public void setNomResponsable(String nomResponsable) {
        this.nomResponsable = nomResponsable;
    }

    public String getPrenomResponsable() {
        return prenomResponsable;
    }

    public void setPrenomResponsable(String prenomResponsable) {
        this.prenomResponsable = prenomResponsable;
    }

    public String getQualiteResponsable() {
        return qualiteResponsable;
    }

    public void setQualiteResponsable(String qualiteResponsable) {
        this.qualiteResponsable = qualiteResponsable;
    }

    public String getTelResponsable() {
        return telResponsable;
    }

    public void setTelResponsable(String telResponsable) {
        this.telResponsable = telResponsable;
    }

    public String getMelResponsable() {
        return melResponsable;
    }

    public void setMelResponsable(String melResponsable) {
        this.melResponsable = melResponsable;
    }

    public String getCreePar() {
        return creePar;
    }

    public void setCreePar(String creePar) {
        this.creePar = creePar;
    }

    public String getCreeParLibelle() {
        return creeParLibelle;
    }

    public void setCreeParLibelle(String creeParLibelle) {
        this.creeParLibelle = creeParLibelle;
    }

    public String getCreeParEmail() {
        return creeParEmail;
    }

    public void setCreeParEmail(String creeParEmail) {
        this.creeParEmail = creeParEmail;
    }

    public List<String> getChargeMission() {
        return chargeMission;
    }

    public void setChargeMission(List<String> chargeMission) {
        this.chargeMission = chargeMission;
    }

    public List<String> getConseillerPM() {
        return conseillerPM;
    }

    public void setConseillerPM(List<String> conseillerPM) {
        this.conseillerPM = conseillerPM;
    }
}
