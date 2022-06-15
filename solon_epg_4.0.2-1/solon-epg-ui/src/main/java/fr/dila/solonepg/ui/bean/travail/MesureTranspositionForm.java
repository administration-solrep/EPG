package fr.dila.solonepg.ui.bean.travail;

import fr.dila.solonepg.api.cases.typescomplexe.DossierTranspositionImmutable;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwLength;
import fr.dila.st.ui.validators.annot.SwRequired;
import fr.dila.st.ui.validators.annot.SwYearFormat;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class MesureTranspositionForm implements DossierTranspositionImmutable {
    @SwYearFormat
    @SwRequired
    @FormParam("annee")
    private Integer annee;

    @SwRequired
    @FormParam("reference")
    private Integer reference;

    @FormParam("titre")
    private String titre;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("numeroArticle")
    private String numeroArticle;

    @FormParam("commentaire")
    private String commentaire;

    public MesureTranspositionForm() {
        // année en cours
        Calendar now = Calendar.getInstance();
        this.annee = now.get(Calendar.YEAR);
    }

    public MesureTranspositionForm(
        Integer annee,
        Integer reference,
        String titre,
        String numeroArticle,
        String commentaire
    ) {
        this.annee = annee;
        this.reference = reference;
        this.titre = titre;
        this.numeroArticle = numeroArticle;
        this.commentaire = commentaire;
    }

    public int getAnnee() {
        return annee;
    }

    public void setAnnee(int annee) {
        this.annee = annee;
    }

    public Integer getReference() {
        return reference;
    }

    public void setReference(Integer reference) {
        this.reference = reference;
    }

    @Override
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getNumeroArticle() {
        return numeroArticle;
    }

    public void setNumeroArticle(String numeroArticle) {
        this.numeroArticle = numeroArticle;
    }

    @Override
    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public String getNumeroArticles() {
        return getNumeroArticle();
    }

    @Override
    public String getRef() {
        return String.join("/", String.valueOf(getAnnee()), String.valueOf(getReference()));
    }

    @Override
    public String getRefMesure() {
        return null;
    }
}
