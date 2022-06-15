package fr.dila.solonepg.elastic.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.dila.solonepg.api.cases.typescomplexe.DossierTransposition;
import java.io.Serializable;

public class ElasticTranspositionDirective implements ElasticTransposition, Serializable {
    private static final long serialVersionUID = 1L;

    public static final String COMMENTAIRE = "commentaire";
    public static final String NUMERO_ARTICLES = "numeroArticles";
    public static final String REF = "ref";
    public static final String REF_MESURE = "refMesure";
    public static final String TITRE = "titre";

    @JsonProperty(COMMENTAIRE)
    private String commentaire;

    @JsonProperty(NUMERO_ARTICLES)
    private String numeroArticle;

    @JsonProperty(REF)
    private String ref;

    @JsonProperty(REF_MESURE)
    private String refMesure;

    @JsonProperty(TITRE)
    private String titre;

    @Override
    public void mapSpecific(DossierTransposition transposition) {
        this.setCommentaire(transposition.getCommentaire());
    }

    /**
     * @return the commentaire
     */
    public String getCommentaire() {
        return commentaire;
    }

    /**
     * @param commentaire
     *            the commentaire to set
     */
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    /**
     * @return the numeroArticle
     */
    @Override
    public String getNumeroArticle() {
        return numeroArticle;
    }

    /**
     * @param numeroArticle
     *            the numeroArticle to set
     */
    @Override
    public void setNumeroArticle(String numeroArticle) {
        this.numeroArticle = numeroArticle;
    }

    /**
     * @return the ref
     */
    @Override
    public String getRef() {
        return ref;
    }

    /**
     * @param ref
     *            the ref to set
     */
    @Override
    public void setRef(String ref) {
        this.ref = ref;
    }

    /**
     * @return the refMesure
     */
    @Override
    public String getRefMesure() {
        return refMesure;
    }

    /**
     * @param refMesure
     *            the refMesure to set
     */
    @Override
    public void setRefMesure(String refMesure) {
        this.refMesure = refMesure;
    }

    /**
     * @return the titre
     */
    @Override
    public String getTitre() {
        return titre;
    }

    /**
     * @param titre
     *            the titre to set
     */
    @Override
    public void setTitre(String titre) {
        this.titre = titre;
    }
}
