package fr.dila.solonepg.web.recherche;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.SolonEpgViewConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.elastic.models.FacetResultEntry;
import fr.dila.solonepg.elastic.models.search.FacetEnum;
import fr.dila.solonepg.elastic.models.search.SearchCriteria;
import fr.dila.solonepg.elastic.models.search.SearchCriteria.SortType;
import fr.dila.solonepg.elastic.models.search.SearchResult;
import fr.dila.solonepg.elastic.service.SolonEpgElasticLocator;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.web.context.NavigationContextBean;

@Name("requeteurDossierLibreActions")
@Scope(ScopeType.CONVERSATION)
public class RequeteurDossierLibreActionsBean implements Serializable {

	private static final long					serialVersionUID	= 1L;

	@In(create = true, required = false)
	protected transient NavigationContextBean	navigationContext;

	@In(create = true, required = true)
	protected transient CoreSession				documentManager;

	@In(create = true, required = false)
	protected transient DocumentRoutingWebActionsBean	routingWebActions;

	private static final Log					LOGGER				= LogFactory
																			.getLog(RequeteurDossierLibreActionsBean.class);

	private SearchCriteria						searchCriteria;

	private SearchResult						searchResult;

	/**
	 * Le nombre de pages calculé après la réception des résultats
	 */
	private int									nbPages;

	/**
	 * Utilisées pour échanger le nom et type d'un checkbox facet qui a été cliqué
	 */
	private String								checkboxFacetName;
	private String								checkboxFacetType;

	@PostConstruct
	public void initialize() {
		searchCriteria = new SearchCriteria();
		searchResult = new SearchResult();
	}

	/**
	 * Lance une recherche et récupère les résultats pendant que la page se recharge
	 * 
	 * @throws Exception
	 */
	public void sendSearchRequest() throws Exception {

		searchResult = SolonEpgElasticLocator.getElasticRequeteurService().getResults(searchCriteria, documentManager);

		setNbPages((int) (searchResult.getCount() + searchCriteria.getPageSize() - 1) / searchCriteria.getPageSize());

		LOGGER.debug("SearchCriteria sent\nFulltext : " + searchCriteria.getFulltext() + "\nExpression exacte: "
				+ searchCriteria.isExpressionExacte() + "\nDatePublicationMin : "
				+ searchCriteria.getDatePublicationMin() + "\nDatePublicationMax : "
				+ searchCriteria.getDatePublicationMax() + "\nDateCrecationMin : "
				+ searchCriteria.getDateCreationMin() + "\nDateCreationMax : " + searchCriteria.getDateCreationMax()
				+ "\nTri: " + searchCriteria.getSortType() + "\nRésultats par page: " + searchCriteria.getPageSize()
				+ "\nPage: " + searchCriteria.getPage());
	}


	public List<String> getFacetNames() {
		List<String> facetNames = new ArrayList<String>();
		facetNames.add(FacetEnum.VECTEUR_PUBLICATION.toString().toLowerCase());
		facetNames.add(FacetEnum.TYPE_ACTE.toString().toLowerCase());
		facetNames.add(FacetEnum.CATEGORIE_ACTE.toString().toLowerCase());
		facetNames.add(FacetEnum.STATUT.toString().toLowerCase());
		facetNames.add(FacetEnum.RESPONSABLE.toString().toLowerCase());
		facetNames.add(FacetEnum.STATUT_ARCHIVAGE.toString().toLowerCase());
		return facetNames;
	}

	/**
	 * Retourne la liste des facets d'une catégorie
	 * 
	 * @param facetName
	 *            Nom de la catégorie de facets
	 * @return
	 */
	public List<FacetResultEntry> getFacetEntries(String facetName) {
		return searchResult != null ? searchResult.getFacetEntries(FacetEnum.valueFromFacetName(facetName))
				: new ArrayList<FacetResultEntry>();
	}

	/**
	 * Retourne la liste des facets sélectionnés d'une catégorie
	 * 
	 * @param facetName
	 *            Nom de la catégorie de facets
	 * @return
	 */
	public Collection<String> getSelectedFacets(String facetName) {
		FacetEnum facetEnum = FacetEnum.valueFromFacetName(facetName);
		Collection<String> selectedFacets = new ArrayList<String>();
		switch (facetEnum) {
			case VECTEUR_PUBLICATION:
				selectedFacets = searchCriteria.getVecteurPublication();
				break;
			case TYPE_ACTE:
				selectedFacets = searchCriteria.getTypeActe();
				break;
			case CATEGORIE_ACTE:
				selectedFacets = searchCriteria.getCategorieActe();
				break;
			case STATUT:
				selectedFacets = searchCriteria.getStatut();
				break;
			case RESPONSABLE:
				selectedFacets = searchCriteria.getResponsable();
				break;
			case STATUT_ARCHIVAGE:
				selectedFacets = searchCriteria.getStatutArchivage();
				// si des statuts équivalents à initial sont présents,
				// ils sont remplacés par le premier statut équivalent à initial
				boolean isInitialSelected = false;
				Iterator<String> i = selectedFacets.iterator();
				while (i.hasNext()) {
					String statutArchivage = i.next();
					if (VocabularyConstants.STATUT_ARCHIVAGE_INITIAL_FACET.contains(statutArchivage)) {
						isInitialSelected = true;
						i.remove();
					}
				}
				if (isInitialSelected) {
					selectedFacets.add(VocabularyConstants.STATUT_ARCHIVAGE_INITIAL_FACET.get(1));
				}
				break;
		}
		return selectedFacets;
	}

	/**
	 * Retourne la liste des types de tri
	 * 
	 * @return
	 */
	public List<SortType> getSortTypes() {
		List<SortType> sortTypes = new ArrayList<SortType>();
		sortTypes.add(SortType.RELEVANCE_DESC);
		sortTypes.add(SortType.CREATION_ASC);
		sortTypes.add(SortType.CREATION_DESC);
		sortTypes.add(SortType.PUBLICATION_ASC);
		sortTypes.add(SortType.PUBLICATION_DESC);
		return sortTypes;
	}

	/**
	 * Retourne la liste des options pour "Résultats par page"
	 * 
	 * @return
	 */
	public List<Integer> getPageSizes() {
		List<Integer> pageSizes = new ArrayList<Integer>();
		pageSizes.add(10);
		pageSizes.add(25);
		pageSizes.add(50);
		pageSizes.add(100);
		return pageSizes;
	}

	/**
	 * Ajoute un facet qui a été sélectionné dans l'interface
	 */
	public void checkboxCheck() {
		searchCriteria.setPage(1);
		Collection<String> selectedFacets = getSelectedFacets("facet_" + checkboxFacetType);
		if (!selectedFacets.contains(checkboxFacetName))
			selectedFacets.add(checkboxFacetName);
	}

	/**
	 * Retire un facet qui a été déselectionné dans l'interface
	 */
	public void checkboxUncheck() {
		searchCriteria.setPage(1);
		Collection<String> selectedFacets = getSelectedFacets("facet_" + checkboxFacetType);
		if (selectedFacets.contains(checkboxFacetName))
			selectedFacets.remove(checkboxFacetName);
	}

	/**
	 * Réinitialise le searchCriteria
	 */
	public void resetCriteria() {
		searchCriteria = new SearchCriteria();
	}

	/**
	 * Assure le bon formattage des nombres en séparant les milliers par des espaces
	 * 
	 * @param number
	 * @return
	 */
	public String formatNumber(int number) {
		DecimalFormat decimalFormat = new DecimalFormat("###,##0");
		String result = decimalFormat.format(number);
		result = result.replace(',', ' ');
		return result;
	}

	public String getLabelStatutArchivage(String statutArchivageId) {
		return statutArchivageId == null || statutArchivageId.isEmpty() ? ""
				: STServiceLocator.getVocabularyService()
						.getEntryLabel(VocabularyConstants.STATUT_ARCHIVAGE_FACET_VOCABULARY, statutArchivageId);
	}

	public SearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public void setSearchResult(SearchResult searchResult) {
		this.searchResult = searchResult;
	}

	public SearchResult getSearchResult() {
		return searchResult;
	}

	public void setNbPages(int nbPages) {
		this.nbPages = nbPages;
	}

	public int getNbPages() {
		return nbPages;
	}

	public String getCheckboxFacetName() {
		return checkboxFacetName;
	}

	public void setCheckboxFacetName(String checkboxFacetName) {
		this.checkboxFacetName = checkboxFacetName;
	}

	public String getCheckboxFacetType() {
		return checkboxFacetType;
	}

	public void setCheckboxFacetType(String checkboxFacetType) {
		this.checkboxFacetType = checkboxFacetType;
	}

	public String showNor(String numeroNor) {

		DocumentModel currentNor;
		try {
			currentNor = SolonEpgServiceLocator.getNORService().getDossierFromNOR(documentManager, numeroNor);
			navigationContext.setCurrentDocument(currentNor);
			routingWebActions.setFeuilleRouteView(SolonEpgViewConstant.REQUETEUR_DOSSIER_LIBRE_RESULTATS);
		} catch (ClientException e) {
			// TODO si on n'arrive pas à récupérer le dossier, afficher un message
			LOGGER.info(STLogEnumImpl.FAIL_GET_DOCUMENT_TEC);
		}
		return SolonEpgViewConstant.REQUETEUR_DOSSIER_LIBRE_RESULTATS;
	}

}
