package fr.dila.solonepg.elastic.services;

import fr.dila.solonepg.api.elastic.service.ElasticRequeteurService;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.ElasticException;
import fr.dila.solonepg.elastic.models.search.RechercheLibre;
import fr.dila.solonepg.elastic.models.search.SearchCriteriaExp;
import fr.dila.solonepg.elastic.models.search.SearchCriteriaRapide;
import fr.dila.solonepg.elastic.models.search.SearchResult;
import fr.dila.solonepg.elastic.models.search.SearchResultExp;
import java.io.IOException;
import java.net.URISyntaxException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.nuxeo.ecm.core.api.CoreSession;

public interface RequeteurService extends ElasticRequeteurService {
    String getIndexDocuments();

    String getIndexDossiers();

    /**
     * Execute la recherche libre en fonction des critères donnés et retourne le résultat (liste de {@link ElasticDossier})
     * paginé, avec facettes.
     *
     * @param criteria les critères de la recherche libre
     * @return une liste de {@link Dossier}, paginée, avec facettes
     */
    SearchResult getResults(RechercheLibre criteria, CoreSession session) throws IOException, URISyntaxException;

    /**
     * Retourne le format JSON de la requête Elasticsearch correspondant aux critères de recherche
     *
     * @param criteria les critères de la recherche libre
     * @param session {@link CoreSession} de l'utilisateur
     * @return une requête ES en format JSON
     */
    String getESJsonQuery(RechercheLibre criteria, CoreSession session);

    SearchResult getResultsWithoutHighlight(RechercheLibre criteria, CoreSession session)
        throws IOException, ElasticException;

    long getCountResults(RechercheLibre criteria, CoreSession session) throws IOException;

    /**
     * Execute la recherche simple en fonction des critères donnés et retourne le résultat (liste de {@link ElasticDossier})
     * paginé, avec facettes.
     *
     * @param query la chaîne de caractère recherchée
     * @return une liste de {@link Dossier}, paginée, avec facettes
     */
    SearchResult getResults(String query, CoreSession session) throws IOException;

    /**
     * Execute la recherche experte en fonction des critères donnés et retourne le résultat (liste de {@link ElasticDossier})
     * paginé, avec facettes.
     *
     * @param criteriaExp les citères de la recherche experte
     * @return une liste de {@link Dossier}, paginée, avec facettes
     */
    SearchResultExp getResults(SearchCriteriaExp criteriaExp, CoreSession session)
        throws IOException, URISyntaxException, ElasticException;

    long getCountResults(SearchCriteriaExp criteriaExp, CoreSession session) throws IOException, URISyntaxException;

    /**
     * Execute la recherche rapide en fonction des critères donnés et retourne le résultat (liste de {@link ElasticDossier})
     * paginé, avec facettes.
     *
     * @param criteriaRapide les citères de la recherche rapide
     * @return une liste de {@link Dossier}, paginée, avec facette (identique pour recherche experte et rapide)
     */
    SearchResultExp getResults(SearchCriteriaRapide criteriaRapide, CoreSession session)
        throws IOException, URISyntaxException, ElasticException;

    long getCountResults(SearchCriteriaRapide criteriaRapide, CoreSession session)
        throws IOException, URISyntaxException;

    /**
     * Méthode de requêtage bas-niveau.
     */
    SearchResponse getResults(SearchRequest searchRequest) throws IOException;

    SearchResultExp getResults(SearchCriteriaExp criteriaExp, CoreSession session, boolean isExport)
        throws IOException, URISyntaxException, ElasticException;

    SearchResultExp getResults(SearchCriteriaRapide criteriaRapide, CoreSession session, boolean isExport)
        throws IOException, ElasticException, URISyntaxException;
}
