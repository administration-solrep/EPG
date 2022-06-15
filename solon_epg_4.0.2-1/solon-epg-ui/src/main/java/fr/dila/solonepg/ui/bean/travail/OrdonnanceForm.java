package fr.dila.solonepg.ui.bean.travail;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwLength;
import javax.ws.rs.FormParam;

@SwBean
public class OrdonnanceForm {
    @FormParam("reference")
    private String reference;

    @FormParam("numeroOrdre")
    private int numeroOrdre;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("numeroArticle")
    private String numeroArticle;

    @FormParam("commentaire")
    private String commentaire;

    public OrdonnanceForm() {}

    public OrdonnanceForm(String reference, int numeroOrdre, String numeroArticle, String commentaire) {
        this.reference = reference;
        this.numeroOrdre = numeroOrdre;
        this.numeroArticle = numeroArticle;
        this.commentaire = commentaire;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getNumeroOrdre() {
        return numeroOrdre;
    }

    public void setNumeroOrdre(int numeroOrdre) {
        this.numeroOrdre = numeroOrdre;
    }

    public String getNumeroArticle() {
        return numeroArticle;
    }

    public void setNumeroArticle(String numeroArticle) {
        this.numeroArticle = numeroArticle;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
