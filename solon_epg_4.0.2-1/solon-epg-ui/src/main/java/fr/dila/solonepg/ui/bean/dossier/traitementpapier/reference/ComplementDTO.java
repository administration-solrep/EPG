package fr.dila.solonepg.ui.bean.dossier.traitementpapier.reference;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.SelectValueDTO;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class ComplementDTO {
    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ARRIVE_PAPIER_XPATH
    )
    @FormParam("dateArriveePapier")
    private Calendar dateArriveePapier;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_COMMENTAIRE_REFERENCE_XPATH
    )
    @FormParam("commentaire")
    private String commentaire;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_TEXTE_A_PUBLIER_XPATH
    )
    @FormParam("texteAPublier")
    private Boolean texteAPublier = Boolean.FALSE;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_TEXTE_SOUMIS_A_VALIDATION_XPATH
    )
    @FormParam("texteSoumisAValidation")
    private Boolean texteSoumisAValidation = Boolean.FALSE;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_SIGNATAIRE_XPATH
    )
    @FormParam("signataire")
    private String signataire;

    private SelectValueDTO signataireValue;

    public ComplementDTO() {}

    public ComplementDTO(
        Calendar dateArriveePapier,
        String commentaire,
        Boolean texteAPublier,
        Boolean texteSoumisAValidation,
        String signataire
    ) {
        this.dateArriveePapier = dateArriveePapier;
        this.commentaire = commentaire;
        this.texteAPublier = texteAPublier;
        this.texteSoumisAValidation = texteSoumisAValidation;
        this.signataire = signataire;
    }

    public Calendar getDateArriveePapier() {
        return dateArriveePapier;
    }

    public void setDateArriveePapier(Calendar dateArriveePapier) {
        this.dateArriveePapier = dateArriveePapier;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Boolean getTexteAPublier() {
        return texteAPublier;
    }

    public void setTexteAPublier(Boolean texteAPublier) {
        this.texteAPublier = texteAPublier;
    }

    public Boolean getTexteSoumisAValidation() {
        return texteSoumisAValidation;
    }

    public void setTexteSoumisAValidation(Boolean texteSoumisAValidation) {
        this.texteSoumisAValidation = texteSoumisAValidation;
    }

    public String getSignataire() {
        return signataire;
    }

    public void setSignataire(String signataire) {
        this.signataire = signataire;
    }

    public SelectValueDTO getSignataireValue() {
        return signataireValue;
    }

    public void setSignataireValue(SelectValueDTO signataireValue) {
        this.signataireValue = signataireValue;
    }
}
