package fr.dila.solonepg.elastic.models.search.enums;

public enum FacetEnum {
    MINISTERE_ATTACHE,
    VECTEUR_PUBLICATION,
    STATUT,
    CATEGORIE_ACTE,
    MONTH,
    DIRECTION,
    TYPE_ACTE,
    STATUT_ARCHIVAGE,
    MAILBOX_DISTRIBUTION,
    DATE_SIGNATURE_DOSSIER,
    DATE_PARUTION_JORF,
    DIRECTION_ATTACHE;

    public static final String GLOBAL_FACET_ID = "global";
    public static final String FILTER_FACET_ID_PREFIX = "filter_";

    public static final String NAME_PREFIX = "facet_";

    public String getFacetName() {
        return NAME_PREFIX + this.toString().toLowerCase();
    }

    /**
     * Retourne la clé de la valeur de facette en fonction du type de la facette.
     *
     * @param bucketKey
     *            la clé de la valeur (bucket) de facette
     * @return une chaine de caractères.
     */
    public static FacetEnum valueFromFacetName(String name) {
        return valueOf(name.toUpperCase().substring(NAME_PREFIX.length()));
    }
}
