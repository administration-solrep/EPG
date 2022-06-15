package fr.dila.solonepg.elastic.models.search;

import static fr.dila.st.core.util.ObjectHelper.requireNonNullElse;

import fr.dila.solonepg.elastic.models.ElasticDossier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.elasticsearch.search.sort.SortOrder;

public class RechercheLibre extends AbstractCriteria {
    public static final String FULLTEXT = "fulltext";
    public static final String SEARCH_BY_TEXT = "searchByText";
    public static final String RESPONSABLE = "ministereResp";
    public static final String VECTEUR_PUBLICATION = "vecteurPublication";
    public static final String DATE_PUBLICATION_MIN = "dateParutionJorfMin";
    public static final String DATE_PUBLICATION_MAX = "dateParutionJorfMax";
    public static final String STATUT = "statut";
    public static final String CATEGORIE_ACTE = "categorieActe";
    public static final String TYPE_ACTE = "typeActe";
    public static final String STATUT_ARCHIVAGE = "statutArchivage";
    public static final String PAGE = "page";
    public static final String PAGESIZE = "page_size";
    public static final String SORT_TYPE = "sort_type";
    public static final String ERASE = "erase";

    // Pagination
    public static final Integer MAX_PAGE_SIZE = 100;

    // Limite de résultats renvoyés par ES pour une requête (défaut : 10 000)
    public static final Integer ES_MAX_RESULTS = 10000;

    // Tri
    public static final SortType DEFAULT_SORT_TYPE = SortType.RELEVANCE_DESC;

    private String fulltext;
    private Collection<String> directionAttache;
    private Collection<String> ministereAttache;
    private Collection<String> vecteurPublication;
    private String datePublicationMin;
    private String datePublicationMax;

    private String datePublicationJoMin;
    private String datePublicationJoMax;

    private String dateSignatureMin;
    private String dateSignatureMax;

    private String dateCreationMin;
    private String dateCreationMax;

    private Collection<String> statut;
    private Collection<String> categorieActe;
    private Collection<String> statutArchivage;
    private SortType sortType;
    private String poste;
    private Boolean posteEnCours;

    /**
     * Recherche d'expression exacte pour fulltext
     */
    private boolean expressionExacte;
    /**
     * Recherche parmi les versions courantes des documents seulement.
     */
    private boolean docCurrentVersion = true;
    /** Recherche parmi les dossiers archivés seulement. */
    private boolean archive;
    private boolean prod;

    private Collection<String> typeActe;

    public RechercheLibre() {
        super();
        this.fulltext = "";
        this.directionAttache = new ArrayList<>();
        this.ministereAttache = new ArrayList<>();
        this.vecteurPublication = new ArrayList<>();
        this.datePublicationMin = "";
        this.datePublicationMax = "";
        this.datePublicationJoMin = "";
        this.datePublicationJoMax = "";
        this.dateCreationMin = "";
        this.dateCreationMax = "";
        this.dateSignatureMin = "";
        this.dateSignatureMax = "";
        this.poste = "";
        this.statut = new ArrayList<>();
        this.categorieActe = new ArrayList<>();
        this.statutArchivage = new ArrayList<>();
        this.sortType = DEFAULT_SORT_TYPE;
        this.expressionExacte = false;
        this.archive = false;
        this.prod = true;
        this.typeActe = new ArrayList<>();
    }

    public String getFulltext() {
        return fulltext;
    }

    public void setFulltext(String fulltext) {
        this.fulltext = fulltext;
    }

    public Collection<String> getDirectionAttache() {
        return directionAttache;
    }

    public void setDirectionAttache(Collection<String> directionAttache) {
        this.directionAttache = new ArrayList<>(requireNonNullElse(directionAttache, Collections.emptyList()));
    }

    public Collection<String> getMinistereAttache() {
        return ministereAttache;
    }

    public void setMinistereAttache(Collection<String> ministereAttache) {
        this.ministereAttache = new ArrayList<>(requireNonNullElse(ministereAttache, Collections.emptyList()));
    }

    public Collection<String> getVecteurPublication() {
        return vecteurPublication;
    }

    public void setVecteurPublication(Collection<String> vecteurPublication) {
        this.vecteurPublication = new ArrayList<>(requireNonNullElse(vecteurPublication, Collections.emptyList()));
    }

    public String getDatePublicationMin() {
        return datePublicationMin;
    }

    public void setDatePublicationMin(String datePublicationMin) {
        this.datePublicationMin = datePublicationMin;
    }

    public String getDatePublicationMax() {
        return datePublicationMax;
    }

    public void setDatePublicationMax(String datePublicationMax) {
        this.datePublicationMax = datePublicationMax;
    }

    public Collection<String> getStatut() {
        return statut;
    }

    public void setStatut(Collection<String> statut) {
        this.statut = new ArrayList<>(requireNonNullElse(statut, Collections.emptyList()));
    }

    public Collection<String> getCategorieActe() {
        return categorieActe;
    }

    public void setCategorieActe(Collection<String> categorieActe) {
        this.categorieActe = new ArrayList<>(requireNonNullElse(categorieActe, Collections.emptyList()));
    }

    public Collection<String> getStatutArchivage() {
        return statutArchivage;
    }

    public void setStatutArchivage(Collection<String> statutArchivage) {
        this.statutArchivage = new ArrayList<>(requireNonNullElse(statutArchivage, Collections.emptyList()));
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public String getDateCreationMin() {
        return dateCreationMin;
    }

    public void setDateCreationMin(String dateCreationMin) {
        this.dateCreationMin = dateCreationMin;
    }

    public String getDateCreationMax() {
        return dateCreationMax;
    }

    public void setDateCreationMax(String dateCreationMax) {
        this.dateCreationMax = dateCreationMax;
    }

    public boolean isExpressionExacte() {
        return expressionExacte;
    }

    public void setExpressionExacte(boolean expressionExacte) {
        this.expressionExacte = expressionExacte;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public boolean isProd() {
        return prod;
    }

    public void setProd(boolean prod) {
        this.prod = prod;
    }

    public enum SortType {
        CREATION_ASC(ElasticDossier.DOS_CREATED, SortOrder.ASC),
        CREATION_DESC(ElasticDossier.DOS_CREATED, SortOrder.DESC),
        PUBLICATION_ASC(ElasticDossier.RETDILA_DATE_PARUTION_JORF, SortOrder.ASC),
        PUBLICATION_DESC(ElasticDossier.RETDILA_DATE_PARUTION_JORF, SortOrder.DESC),
        RELEVANCE_DESC("_score", SortOrder.DESC);

        private final String field;
        private final SortOrder sortOrder;

        SortType(String field, SortOrder sortOrder) {
            this.field = field;
            this.sortOrder = sortOrder;
        }

        public String getField() {
            return field;
        }

        public SortOrder getSortOrder() {
            return sortOrder;
        }
    }

    public Collection<String> getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(Collection<String> typeActe) {
        this.typeActe = typeActe;
    }

    public boolean isDocCurrentVersion() {
        return docCurrentVersion;
    }

    public void setDocCurrentVersion(boolean docCurrentVersion) {
        this.docCurrentVersion = docCurrentVersion;
    }

    public String getDatePublicationJoMin() {
        return datePublicationJoMin;
    }

    public void setDatePublicationJoMin(String datePublicationJoMin) {
        this.datePublicationJoMin = datePublicationJoMin;
    }

    public String getDatePublicationJoMax() {
        return datePublicationJoMax;
    }

    public void setDatePublicationJoMax(String datePublicationJoMax) {
        this.datePublicationJoMax = datePublicationJoMax;
    }

    public String getDateSignatureMin() {
        return dateSignatureMin;
    }

    public void setDateSignatureMin(String dateSignatureMin) {
        this.dateSignatureMin = dateSignatureMin;
    }

    public String getDateSignatureMax() {
        return dateSignatureMax;
    }

    public void setDateSignatureMax(String dateSignatureMax) {
        this.dateSignatureMax = dateSignatureMax;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public Boolean getPosteEnCours() {
        return posteEnCours;
    }

    public void setPosteEnCours(Boolean posteEnCours) {
        this.posteEnCours = posteEnCours;
    }
}
