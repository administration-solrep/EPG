package fr.dila.solonepg.elastic.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class FacetResult {
    public static final String RESPONSABLE = "facet_responsable";
    public static final String CATEGORIE_ACTE = "facet_categorie_acte";
    public static final String VECTEUR_PUBLICATION = "facet_vecteur_publication";
    public static final String STATUT = "facet_statut";
    public static final String TYPE_ACTE = "facet_type_acte";
    public static final String STATUT_ARCHIVAGE = "facet_statut_archivage";

    @JsonProperty(RESPONSABLE)
    private FacetResultWrap reponsable;

    @JsonProperty(CATEGORIE_ACTE)
    private FacetResultWrap categorie_acte;

    @JsonProperty(VECTEUR_PUBLICATION)
    private FacetResultWrap vecteur_publication;

    @JsonProperty(STATUT)
    private FacetResultWrap statut;

    @JsonProperty(TYPE_ACTE)
    private FacetResultWrap type_acte;

    @JsonProperty(STATUT_ARCHIVAGE)
    private FacetResultWrap statut_archivage;

    public FacetResultWrap getReponsable() {
        return reponsable;
    }

    public void setReponsable(FacetResultWrap reponsable) {
        this.reponsable = reponsable;
    }

    public FacetResultWrap getCategorie_acte() {
        return categorie_acte;
    }

    public void setCategorie_acte(FacetResultWrap categorie_acte) {
        this.categorie_acte = categorie_acte;
    }

    public FacetResultWrap getVecteur_publication() {
        return vecteur_publication;
    }

    public void setVecteur_publication(FacetResultWrap vecteur_publication) {
        this.vecteur_publication = vecteur_publication;
    }

    public FacetResultWrap getStatut() {
        return statut;
    }

    public void setStatut(FacetResultWrap statut) {
        this.statut = statut;
    }

    public FacetResultWrap getType_acte() {
        return type_acte;
    }

    public void setType_acte(FacetResultWrap type_acte) {
        this.type_acte = type_acte;
    }

    public FacetResultWrap getStatutArchivage() {
        return statut_archivage;
    }

    public void setStatutArchivage(FacetResultWrap statut_archivage) {
        this.statut_archivage = statut_archivage;
    }

    public static class FacetResultWrap {
        public static final String COUNT_ERROR = "doc_count_error_upper_bound";
        public static final String SUM_DOC_COUNT = "sum_other_doc_count";
        public static final String BUCKETS = "buckets";

        @JsonProperty(COUNT_ERROR)
        private int count_error;

        @JsonProperty(SUM_DOC_COUNT)
        private int sum_doc_count;

        @JsonProperty(BUCKETS)
        private List<FacetResultEntry> buckets;

        public int getCount_error() {
            return count_error;
        }

        public void setCount_error(int count_error) {
            this.count_error = count_error;
        }

        public int getSum_doc_count() {
            return sum_doc_count;
        }

        public void setSum_doc_count(int sum_doc_count) {
            this.sum_doc_count = sum_doc_count;
        }

        public List<FacetResultEntry> getBuckets() {
            return buckets;
        }

        public void setBuckets(List<FacetResultEntry> buckets) {
            this.buckets = buckets;
        }
    }
}
