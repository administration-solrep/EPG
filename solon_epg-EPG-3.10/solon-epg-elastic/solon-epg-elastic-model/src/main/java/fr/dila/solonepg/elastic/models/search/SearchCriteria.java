package fr.dila.solonepg.elastic.models.search;

import java.util.ArrayList;
import java.util.Collection;

import fr.dila.solonepg.elastic.models.ElasticDossier;

public class SearchCriteria {

	public static final String		FULLTEXT					= "fulltext";
	public static final String		SEARCH_BY_TEXT				= "searchByText";
	public static final String		RESPONSABLE					= "ministereResp";
	public static final String		VECTEUR_PUBLICATION			= "vecteurPublication";
	public static final String		DATE_PUBLICATION_MIN		= "dateParutionJorfMin";
	public static final String		DATE_PUBLICATION_MAX		= "dateParutionJorfMax";
	public static final String		STATUT						= "statut";
	public static final String		CATEGORIE_ACTE				= "categorieActe";
	public static final String		TYPE_ACTE					= "typeActe";
	public static final String		STATUT_ARCHIVAGE			= "statutArchivage";
	public static final String		PAGE						= "page";
	public static final String		PAGESIZE					= "page_size";
	public static final String		SORT_TYPE					= "sort_type";
	public static final String		ERASE						= "erase";

	// Pagination
	public static final Integer		DEFAULT_PAGE_SIZE			= 25;
	public static final Integer		MAX_PAGE_SIZE				= 100;

	// Limite de résultats renvoyés par ES pour une requête (défaut : 10 000)
	public static final Integer		ES_MAX_RESULTS				= 10000;

	// Tri
	public static final SortType DEFAULT_SORT_TYPE = SortType.RELEVANCE_DESC;

	private String fulltext;
	private Collection<String> responsable;
	private Collection<String> vecteurPublication;
	private String datePublicationMin;
	private String datePublicationMax;

	private String dateCreationMin;
	private String dateCreationMax;

	private Collection<String> statut;
	private Collection<String> categorieActe;
	private Collection<String> statutArchivage;
	private SortType sortType;
	private Integer page;
	private Integer pageSize;
	private Collection<String>		permsUtilisateur;
	private boolean hasDroitsNomination;

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
	private boolean					prod;

	private Collection<String> typeActe;

	public SearchCriteria() {
		this.fulltext = "";
		this.responsable = new ArrayList<String>();
		this.vecteurPublication = new ArrayList<String>();
		this.datePublicationMin = "";
		this.datePublicationMax = "";
		this.dateCreationMin = "";
		this.dateCreationMax = "";
		this.statut = new ArrayList<String>();
		this.categorieActe = new ArrayList<String>();
		this.statutArchivage = new ArrayList<String>();
		this.sortType = DEFAULT_SORT_TYPE;
		this.page = 1;
		this.pageSize = DEFAULT_PAGE_SIZE;
		this.expressionExacte = false;
		this.archive = false;
		this.prod = true;
		this.typeActe = new ArrayList<String>();
	}

	public String getFulltext() {
		return fulltext;
	}

	public void setFulltext(String fulltext) {
		this.fulltext = fulltext;
	}

	public Collection<String> getResponsable() {
		return responsable;
	}

	public void setResponsable(Collection<String> responsable) {
		this.responsable = responsable;
	}

	public Collection<String> getVecteurPublication() {
		return vecteurPublication;
	}

	public void setVecteurPublication(Collection<String> vecteurPublication) {
		this.vecteurPublication = vecteurPublication;
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
		this.statut = statut;
	}

	public Collection<String> getCategorieActe() {
		return categorieActe;
	}

	public void setCategorieActe(Collection<String> categorieActe) {
		this.categorieActe = categorieActe;
	}

	public Collection<String> getStatutArchivage() {
		return statutArchivage;
	}

	public void setStatutArchivage(Collection<String> statutArchivage) {
		this.statutArchivage = statutArchivage;
	}

	public SortType getSortType() {
		return sortType;
	}

	public void setSortType(SortType sortType) {
		this.sortType = sortType;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
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

	public static enum SortType {
		CREATION_ASC(ElasticDossier.DC_CREATED, "asc"), CREATION_DESC(ElasticDossier.DC_CREATED,
				"desc"), PUBLICATION_ASC(ElasticDossier.RETDILA_DATE_PARUTION_JORF, "asc"), PUBLICATION_DESC(
						ElasticDossier.RETDILA_DATE_PARUTION_JORF, "desc"), RELEVANCE_DESC("_score", "desc");

		private String field;
		private String order;

		SortType(String pField, String pOrder) {
			this.field = pField;
			this.order = pOrder;
		}

		public String getField() {
			return field;
		}

		public String getOrder() {
			return order;
		}
	}

	public Collection<String> getPermsUtilisateur() {
		return permsUtilisateur;
	}

	public void setPermsUtilisateur(Collection<String> permsUtilisateur) {
		this.permsUtilisateur = permsUtilisateur;
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

	public boolean isHasDroitsNomination() {
		return hasDroitsNomination;
	}

	public void setHasDroitsNomination(boolean hasDroitsNomination) {
		this.hasDroitsNomination = hasDroitsNomination;
	}
}
