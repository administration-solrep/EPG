package fr.dila.solonepg.ui.bean.travail;

import fr.dila.solonepg.api.cases.typescomplexe.DossierTranspositionImmutable;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwLength;
import javax.ws.rs.FormParam;

@SwBean
public class MesureApplicationForm implements DossierTranspositionImmutable {
    @FormParam("reference")
    private String reference;

    @FormParam("titre")
    private String titre;

    @FormParam("numeroOrdre")
    private int numeroOrdre;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @FormParam("numeroArticle")
    private String numeroArticle;

    @FormParam("commentaire")
    private String commentaire;

    @FormParam("loi")
    private boolean loi;

    @FormParam("ordonnance")
    private boolean ordonnance;

    public MesureApplicationForm() {}

    public MesureApplicationForm(
        String reference,
        String titre,
        int numeroOrdre,
        String numeroArticle,
        String commentaire
    ) {
        this.reference = reference;
        this.titre = titre;
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

    @Override
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
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

    public boolean isLoi() {
        return loi;
    }

    public void setLoi(boolean loi) {
        this.loi = loi;
    }

    public boolean isOrdonnance() {
        return ordonnance;
    }

    public void setOrdonnance(boolean ordonnance) {
        this.ordonnance = ordonnance;
    }

    @Override
    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public String getRef() {
        return getReference();
    }

    @Override
    public String getNumeroArticles() {
        return getNumeroArticle();
    }

    @Override
    public String getRefMesure() {
        return String.valueOf(getNumeroOrdre());
    }
}
