package fr.dila.solonepg.ui.bean.dossier.tablesreference;

import fr.dila.solonepg.api.constant.SolonEpgTableReferenceConstants;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.SelectValueDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.FormParam;

@SwBean
public class AdminTableReferenceDTO {

    public AdminTableReferenceDTO() {}

    @NxProp(
        docType = SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE,
        xpath = SolonEpgTableReferenceConstants.TABLE_REFERENCE_CABINET_PM_XPATH
    )
    @FormParam("cabinetPmAutorite")
    private ArrayList<String> autoritesValidationCabinetPM;

    private Map<String, String> mapAutoritesValidationCabinetPM = new HashMap<>();

    @NxProp(
        docType = SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE,
        xpath = SolonEpgTableReferenceConstants.TABLE_REFERENCE_CHARGES_MISSION_XPATH
    )
    @FormParam("chargeMissionAutorite")
    private ArrayList<String> autoritesValidationChargesMission;

    private Map<String, String> mapAutoritesValidationChargesMission = new HashMap<>();

    @FormParam("signataires")
    private ArrayList<String> signataires;

    private Map<String, String> mapSignataires = new HashMap<>();

    @FormParam("signatairesLibres")
    private ArrayList<String> signatairesLibres;

    @NxProp(
        docType = SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE,
        xpath = SolonEpgTableReferenceConstants.TABLE_REFERENCE_SIGNATAIRES_XPATH
    )
    private List<String> persistedSignataires;

    @NxProp(
        docType = SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE,
        xpath = SolonEpgTableReferenceConstants.TABLE_REFERENCE_MINISTERE_PM_XPATH
    )
    @FormParam("ministerePm")
    private String ministerePm;

    private Map<String, String> mapMinisterePm = new HashMap<>();

    @NxProp(
        docType = SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE,
        xpath = SolonEpgTableReferenceConstants.TABLE_REFERENCE_DIRECTION_PM_XPATH
    )
    @FormParam("directionPm")
    private ArrayList<String> directionsPm;

    private Map<String, String> mapDirectionsPm = new HashMap<>();

    @NxProp(
        docType = SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE,
        xpath = SolonEpgTableReferenceConstants.TABLE_REFERENCE_MINISTERE_CE_XPATH
    )
    @FormParam("conseilEtat")
    private String conseilEtat;

    private Map<String, String> mapConseilEtat = new HashMap<>();

    @NxProp(
        docType = SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE,
        xpath = SolonEpgTableReferenceConstants.TABLE_REFERENCE_VECTEURS_PUBLICATIONS_XPATH
    )
    @FormParam("vecteurPublication")
    private ArrayList<String> vecteurPublicationMultiples;

    private Map<String, String> mapVecteurPublicationMultiples = new HashMap<>();

    @NxProp(
        docType = SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE,
        xpath = SolonEpgTableReferenceConstants.TABLE_REFERENCE_SIGNATURE_CPM_XPATH
    )
    @FormParam("cabinetPmSignature")
    private ArrayList<String> cheminCroixSignatureCPM;

    private Map<String, String> mapCheminCroixSignatureCPM = new HashMap<>();

    @NxProp(
        docType = SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE,
        xpath = SolonEpgTableReferenceConstants.TABLE_REFERENCE_SIGNATURE_SGG_XPATH
    )
    @FormParam("sgg")
    private ArrayList<String> cheminCroixSignatureSgg;

    private Map<String, String> mapCheminCroixSignatureSgg = new HashMap<>();

    @NxProp(
        docType = SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE,
        xpath = SolonEpgTableReferenceConstants.TABLE_REFERENCE_TYPE_ACTE_XPATH
    )
    @FormParam("typeActe")
    private ArrayList<String> typesActe;

    private List<SelectValueDTO> typesActeDTO;

    public List<SelectValueDTO> getTypesActeDTO() {
        return typesActeDTO;
    }

    public void setTypesActeDTO(List<SelectValueDTO> typesActeDTO) {
        this.typesActeDTO = typesActeDTO;
    }

    private Map<String, String> mapTypesActe = new HashMap<>();

    @NxProp(
        docType = SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE,
        xpath = SolonEpgTableReferenceConstants.TABLE_REFERENCE_PST_PUBLI_ID_XPATH
    )
    @FormParam("postePublication")
    private String postePublication;

    private Map<String, String> mapPostePublication = new HashMap<>();

    @NxProp(
        docType = SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE,
        xpath = SolonEpgTableReferenceConstants.TABLE_REFERENCE_PST_DAN_ID_XPATH
    )
    @FormParam("posteDan")
    private String posteDan;

    private Map<String, String> mapPosteDan = new HashMap<>();

    @NxProp(
        docType = SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE,
        xpath = SolonEpgTableReferenceConstants.TABLE_REFERENCE_PST_DAN_ID_AVIS_RECTIFICATIF_CE_XPATH
    )
    @FormParam("avisRectificatifPosteDan")
    private String avisRectificatifPosteDan;

    private Map<String, String> mapAvisRectificatifPosteDanDTO;

    @NxProp(
        docType = SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE,
        xpath = SolonEpgTableReferenceConstants.TABLE_REFERENCE_RETOUR_DAN_XPATH
    )
    @FormParam("posteAutorise")
    private ArrayList<String> corbeillesRetourDAN;

    private Map<String, String> mapCorbeillesRetourDAN = new HashMap<>();

    public ArrayList<String> getAutoritesValidationCabinetPM() {
        return autoritesValidationCabinetPM;
    }

    public void setAutoritesValidationCabinetPM(ArrayList<String> autoritesValidationCabinetPM) {
        this.autoritesValidationCabinetPM = autoritesValidationCabinetPM;
    }

    public Map<String, String> getMapAutoritesValidationCabinetPM() {
        return mapAutoritesValidationCabinetPM;
    }

    public void setMapAutoritesValidationCabinetPM(Map<String, String> mapAutoritesValidationCabinetPM) {
        this.mapAutoritesValidationCabinetPM = mapAutoritesValidationCabinetPM;
    }

    public ArrayList<String> getAutoritesValidationChargesMission() {
        return autoritesValidationChargesMission;
    }

    public void setAutoritesValidationChargesMission(ArrayList<String> autoritesValidationChargesMission) {
        this.autoritesValidationChargesMission = autoritesValidationChargesMission;
    }

    public Map<String, String> getMapAutoritesValidationChargesMission() {
        return mapAutoritesValidationChargesMission;
    }

    public void setMapAutoritesValidationChargesMission(Map<String, String> mapAutoritesValidationChargesMission) {
        this.mapAutoritesValidationChargesMission = mapAutoritesValidationChargesMission;
    }

    public ArrayList<String> getSignataires() {
        return signataires;
    }

    public void setSignataires(ArrayList<String> signataires) {
        this.signataires = signataires;
    }

    public Map<String, String> getMapSignataires() {
        return mapSignataires;
    }

    public void setMapSignataire(Map<String, String> mapSignataires) {
        this.mapSignataires = mapSignataires;
    }

    public ArrayList<String> getSignatairesLibres() {
        return signatairesLibres;
    }

    public void setSignatairesLibres(ArrayList<String> signatairesLibres) {
        this.signatairesLibres = signatairesLibres;
    }

    public List<String> getPersistedSignataires() {
        return persistedSignataires;
    }

    public void setPersistedSignataires(List<String> persistedSignataires) {
        this.persistedSignataires = persistedSignataires;
    }

    public String getMinisterePm() {
        return ministerePm;
    }

    public void setMinisterePm(String ministerePm) {
        this.ministerePm = ministerePm;
    }

    public Map<String, String> getMapMinisterePm() {
        return mapMinisterePm;
    }

    public void setMapMinisterePm(Map<String, String> mapMinisterePm) {
        this.mapMinisterePm = mapMinisterePm;
    }

    public ArrayList<String> getDirectionsPm() {
        return directionsPm;
    }

    public void setDirectionsPm(ArrayList<String> directionsPm) {
        this.directionsPm = directionsPm;
    }

    public Map<String, String> getMapDirectionsPm() {
        return mapDirectionsPm;
    }

    public void setMapDirectionsPm(Map<String, String> mapDirectionsPm) {
        this.mapDirectionsPm = mapDirectionsPm;
    }

    public String getConseilEtat() {
        return conseilEtat;
    }

    public void setConseilEtat(String conseilEtat) {
        this.conseilEtat = conseilEtat;
    }

    public Map<String, String> getMapConseilEtat() {
        return mapConseilEtat;
    }

    public void setMapConseilEtat(Map<String, String> mapConseilEtat) {
        this.mapConseilEtat = mapConseilEtat;
    }

    public ArrayList<String> getVecteurPublicationMultiples() {
        return vecteurPublicationMultiples;
    }

    public void setVecteurPublicationMultiples(ArrayList<String> vecteurPublicationMultiples) {
        this.vecteurPublicationMultiples = vecteurPublicationMultiples;
    }

    public Map<String, String> getMapVecteurPublicationMultiples() {
        return mapVecteurPublicationMultiples;
    }

    public void setMapVecteurPublicationMultiples(Map<String, String> mapVecteurPublicationMultiples) {
        this.mapVecteurPublicationMultiples = mapVecteurPublicationMultiples;
    }

    public ArrayList<String> getCheminCroixSignatureCPM() {
        return cheminCroixSignatureCPM;
    }

    public void setCheminCroixSignatureCPM(ArrayList<String> cheminCroixSignatureCPM) {
        this.cheminCroixSignatureCPM = cheminCroixSignatureCPM;
    }

    public Map<String, String> getMapCheminCroixSignatureCPM() {
        return mapCheminCroixSignatureCPM;
    }

    public void setMapCheminCroixSignatureCPM(Map<String, String> mapCheminCroixSignatureCPM) {
        this.mapCheminCroixSignatureCPM = mapCheminCroixSignatureCPM;
    }

    public ArrayList<String> getCheminCroixSignatureSgg() {
        return cheminCroixSignatureSgg;
    }

    public void setCheminCroixSignatureSgg(ArrayList<String> cheminCroixSignatureSgg) {
        this.cheminCroixSignatureSgg = cheminCroixSignatureSgg;
    }

    public Map<String, String> getMapCheminCroixSignatureSgg() {
        return mapCheminCroixSignatureSgg;
    }

    public void setMapCheminCroixSignatureSgg(Map<String, String> mapCheminCroixSignatureSgg) {
        this.mapCheminCroixSignatureSgg = mapCheminCroixSignatureSgg;
    }

    public ArrayList<String> getTypesActe() {
        return typesActe;
    }

    public void setTypesActe(ArrayList<String> typesActe) {
        this.typesActe = typesActe;
    }

    public Map<String, String> getMapTypesActe() {
        return mapTypesActe;
    }

    public void setMapTypesActe(Map<String, String> mapTypesActe) {
        this.mapTypesActe = mapTypesActe;
    }

    public String getPostePublication() {
        return postePublication;
    }

    public void setPostePublication(String postePublication) {
        this.postePublication = postePublication;
    }

    public Map<String, String> getMapPostePublication() {
        return mapPostePublication;
    }

    public void setMapPostePublication(Map<String, String> mapPostePublication) {
        this.mapPostePublication = mapPostePublication;
    }

    public String getPosteDan() {
        return posteDan;
    }

    public void setPosteDan(String posteDan) {
        this.posteDan = posteDan;
    }

    public Map<String, String> getMapPosteDan() {
        return mapPosteDan;
    }

    public void setMapPosteDan(Map<String, String> mapPosteDan) {
        this.mapPosteDan = mapPosteDan;
    }

    public String getAvisRectificatifPosteDan() {
        return avisRectificatifPosteDan;
    }

    public void setAvisRectificatifPosteDan(String avisRectificatifPosteDan) {
        this.avisRectificatifPosteDan = avisRectificatifPosteDan;
    }

    public Map<String, String> getMapAvisRectificatifPosteDanDTO() {
        return mapAvisRectificatifPosteDanDTO;
    }

    public void setMapAvisRectificatifPosteDanDTO(Map<String, String> mapAvisRectificatifPosteDanDTO) {
        this.mapAvisRectificatifPosteDanDTO = mapAvisRectificatifPosteDanDTO;
    }

    public ArrayList<String> getCorbeillesRetourDAN() {
        return corbeillesRetourDAN;
    }

    public void setCorbeillesRetourDAN(ArrayList<String> corbeillesRetourDAN) {
        this.corbeillesRetourDAN = corbeillesRetourDAN;
    }

    public Map<String, String> getMapCorbeillesRetourDAN() {
        return mapCorbeillesRetourDAN;
    }

    public void setMapCorbeillesRetourDAN(Map<String, String> mapCorbeillesRetourDAN) {
        this.mapCorbeillesRetourDAN = mapCorbeillesRetourDAN;
    }
}
