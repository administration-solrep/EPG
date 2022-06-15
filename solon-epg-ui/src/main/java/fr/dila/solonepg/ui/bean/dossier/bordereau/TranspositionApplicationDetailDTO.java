package fr.dila.solonepg.ui.bean.dossier.bordereau;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwLength;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRegex;
import fr.dila.st.ui.validators.annot.SwYearFormat;
import javax.ws.rs.FormParam;

@SwBean
public class TranspositionApplicationDetailDTO {

    public TranspositionApplicationDetailDTO() {
        super();
    }

    public TranspositionApplicationDetailDTO(String type) {
        this.type = type;
    }

    @SwYearFormat
    @FormParam("annee")
    private Integer annee;

    @SwNotEmpty
    @SwRegex("^([0-9]{4}[\\/-])?[0-9]{1,4}$")
    @FormParam("reference")
    private String reference;

    @FormParam("titre")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String titre;

    @FormParam("numeroOrdre")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String numeroOrdre;

    @FormParam("numeroArticle")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String numeroArticle;

    @FormParam("commentaire")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String commentaire;

    @FormParam("type")
    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    private String type;

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getNumeroOrdre() {
        return numeroOrdre;
    }

    public void setNumeroOrdre(String numeroOrdre) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
