package fr.dila.solonepg.ui.bean.dossier.traitementpapier.signature;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.mapper.MapDoc2BeanComplexeTypeProcess;
import fr.dila.st.ui.validators.annot.SwLength;
import java.util.List;
import javax.ws.rs.FormParam;

@SwBean
public class SignatureDTO {
    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_CHEMIN_CROIX_XPATH
    )
    @FormParam("cheminCroix")
    private Boolean cheminDeCroix = Boolean.FALSE;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATURE_DESTINATAIRE_SGG_XPATH
    )
    @FormParam("sgg")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String signatureDestinataireSGG;

    private SelectValueDTO signatureDestinataireSGGValue;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATURE_DESTINATAIRE_CPM_XPATH
    )
    @FormParam("cabinetPM")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String signatureDestinataireCPM;

    private SelectValueDTO signatureDestinataireCPMValue;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATURE_PM_XPATH,
        process = MapDoc2BeanComplexeTypeProcess.class
    )
    private DonneesSignatureDTO signaturePm;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATURE_SGG_XPATH,
        process = MapDoc2BeanComplexeTypeProcess.class
    )
    private DonneesSignatureDTO signatureSgg;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATURE_PR_XPATH,
        process = MapDoc2BeanComplexeTypeProcess.class
    )
    private DonneesSignatureDTO signaturePr;

    private List<ListeSignatureDTO> listes;

    public SignatureDTO() {}

    public SignatureDTO(
        Boolean cheminDeCroix,
        String signatureDestinataireSGG,
        String signatureDestinataireCPM,
        DonneesSignatureDTO signaturePm,
        DonneesSignatureDTO signatureSgg,
        DonneesSignatureDTO signaturePr,
        List<ListeSignatureDTO> listes
    ) {
        this.cheminDeCroix = cheminDeCroix;
        this.signatureDestinataireSGG = signatureDestinataireSGG;
        this.signatureDestinataireCPM = signatureDestinataireCPM;
        this.signaturePm = signaturePm;
        this.signatureSgg = signatureSgg;
        this.signaturePr = signaturePr;
        this.listes = listes;
    }

    public Boolean getCheminDeCroix() {
        return cheminDeCroix;
    }

    public void setCheminDeCroix(Boolean cheminDeCroix) {
        this.cheminDeCroix = cheminDeCroix;
    }

    public String getSignatureDestinataireSGG() {
        return signatureDestinataireSGG;
    }

    public void setSignatureDestinataireSGG(String signatureDestinataireSGG) {
        this.signatureDestinataireSGG = signatureDestinataireSGG;
    }

    public SelectValueDTO getSignatureDestinataireSGGValue() {
        return signatureDestinataireSGGValue;
    }

    public void setSignatureDestinataireSGGValue(SelectValueDTO signatureDestinataireSGGValue) {
        this.signatureDestinataireSGGValue = signatureDestinataireSGGValue;
    }

    public String getSignatureDestinataireCPM() {
        return signatureDestinataireCPM;
    }

    public void setSignatureDestinataireCPM(String signatureDestinataireCPM) {
        this.signatureDestinataireCPM = signatureDestinataireCPM;
    }

    public SelectValueDTO getSignatureDestinataireCPMValue() {
        return signatureDestinataireCPMValue;
    }

    public void setSignatureDestinataireCPMValue(SelectValueDTO signatureDestinataireCPMValue) {
        this.signatureDestinataireCPMValue = signatureDestinataireCPMValue;
    }

    public DonneesSignatureDTO getSignaturePm() {
        return signaturePm;
    }

    public void setSignaturePm(DonneesSignatureDTO signaturePm) {
        this.signaturePm = signaturePm;
    }

    public DonneesSignatureDTO getSignatureSgg() {
        return signatureSgg;
    }

    public void setSignatureSgg(DonneesSignatureDTO signatureSgg) {
        this.signatureSgg = signatureSgg;
    }

    public DonneesSignatureDTO getSignaturePr() {
        return signaturePr;
    }

    public void setSignaturePr(DonneesSignatureDTO signaturePr) {
        this.signaturePr = signaturePr;
    }

    public List<ListeSignatureDTO> getListes() {
        return listes;
    }

    public void setListes(List<ListeSignatureDTO> listes) {
        this.listes = listes;
    }
}
