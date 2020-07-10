package fr.dila.solonepg.elastic.models.search;

public enum FacetEnum {

	RESPONSABLE, VECTEUR_PUBLICATION, STATUT, CATEGORIE_ACTE, MONTH, DIRECTION, TYPE_ACTE, STATUT_ARCHIVAGE;

	public static final String	GLOBAL_FACET_ID			= "global";
	public static final String	FILTER_FACET_ID_PREFIX	= "filter_";

	public static final String	NAME_PREFIX				= "facet_";

	private KeyTransformer		keyTransformer;

	private FacetEnum(KeyTransformer keyTransformer) {
		this.keyTransformer = keyTransformer;
	}

	private FacetEnum() {
		//
	}

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
	// TODO elastic-search
	// public String bucketKeyAsString(Bucket bucket) {
	// if (keyTransformer != null) {
	// return keyTransformer.keyAsString(bucket.getKey());
	// }
	//
	// return bucket.getKeyAsString();
	// }

	public static FacetEnum valueFromFacetName(String name) {
		return valueOf(name.toUpperCase().substring(NAME_PREFIX.length()));
	}

	/**
	 * Interface spécifiant une transformation de clé de valeur de facette.
	 */
	private static interface KeyTransformer {

		/**
		 * Retournbe la clé de la valeur de facette.
		 * 
		 * @param key
		 * @return
		 */
		String keyAsString(Object key);

	}
}
