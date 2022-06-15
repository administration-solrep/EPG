package fr.dila.solonepg.elastic.utils;

import com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.api.service.ElasticParametrageService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.elastic.models.ElasticDocument;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.ElasticException;
import fr.dila.solonepg.elastic.models.ElasticStep;
import fr.dila.solonepg.elastic.models.ElasticTranspositionDirective;
import fr.dila.solonepg.elastic.models.config.QueryBuilderProperty;
import fr.dila.solonepg.elastic.models.config.QueryBuildersPropertiesConfiguration;
import fr.dila.solonepg.elastic.models.search.AbstractCriteria;
import fr.dila.solonepg.elastic.models.search.RechercheLibre;
import fr.dila.st.api.constant.STLifeCycleConstant;
import fr.dila.st.core.util.SolonDateConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.nuxeo.ecm.core.api.CoreSession;

public class SearchRequestUtils {
    private static final Float FLOAT_TEN = 10.0f;

    public static Map<String, Float> getDossierFullTextFieldsAndPonderation(CoreSession session) {
        if (session == null) {
            return getDefaultsPonderations();
        }

        return ParametrePonderation.toStringAllDossier(session);
    }

    public static Map<String, Float> getDocumentFullTextFieldsAndPonderation() {
        return ImmutableMap.of(
            ElasticDocument.FILE_DATA,
            NumberUtils.FLOAT_ONE,
            ElasticDocument.DC_TITLE,
            NumberUtils.FLOAT_ONE
        );
    }

    public static Map<String, Float> getDocumentFieldsAndPonderation(CoreSession session) {
        if (session == null) {
            return ImmutableMap.of(
                ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE,
                2.0f,
                ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_DATA,
                NumberUtils.FLOAT_ONE
            );
        }

        return Stream
            .concat(
                ParametrePonderation.DOCUMENT_TITRE.toMap(session).entrySet().stream(),
                ParametrePonderation.DOCUMENT_FILEDATA.toMap(session).entrySet().stream()
            )
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static void addPerms(AbstractCriteria criteria, SearchRequest request) {
        Collection<String> permsUser = criteria.getPermsUtilisateur();
        // si l'utilisateur n'a pas de poste on en ajoute un virtuel pour éviter
        // de renvoyer tous les résultats
        if (permsUser == null) {
            permsUser = new ArrayList<>();
        }

        if (permsUser.isEmpty()) {
            permsUser.add("-1");
        }

        addFacet(permsUser, request, ElasticDossier.PERMS_KEYWORD, true);
    }

    public static void addDroitsNomination(AbstractCriteria criteria, SearchRequest request) {
        if (!criteria.isHasDroitsNomination()) {
            Collection<String> hecant = new ArrayList<>();
            hecant.add("false");
            addFacet(hecant, request, ElasticDossier.DOS_MESURE_NOMINATIVE);
        }
    }

    public static void addFacet(Collection<String> checkedValues, SearchRequest request, String field) {
        addFacet(checkedValues, request, field, false);
    }

    public static void addFacet(Collection<String> checkedValues, SearchRequest request, String field, boolean should) {
        if (checkedValues != null && !checkedValues.isEmpty()) {
            BoolQueryBuilder boolQueryBuilder = (BoolQueryBuilder) request.source().query();
            List<QueryBuilder> mustClauses = boolQueryBuilder.must();

            BoolQueryBuilder query = new BoolQueryBuilder();
            for (String value : checkedValues) {
                if (should) {
                    query.should(QueryBuilders.termQuery(field, value));
                } else {
                    query.must(QueryBuilders.termQuery(field, value));
                }
            }
            mustClauses.add(query);
        }
    }

    /**
     * Initialisation de la partie query
     *
     * @param request
     */
    public static void initQuery(SearchRequest request) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        request.source().query(boolQuery);
    }

    public static Map<String, Float> getDefaultsPonderations() {
        return ImmutableMap.of(
            ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE,
            2.0f,
            ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_DATA,
            NumberUtils.FLOAT_ONE,
            ElasticDossier.DOS_NUMERO_NOR,
            FLOAT_TEN,
            ElasticDossier.DOS_TITRE_ACTE,
            5.0f
        );
    }

    public static void addColonnes(
        SearchSourceBuilder sourceBuilder,
        Collection<String> colonnes,
        QueryBuildersPropertiesConfiguration properties
    )
        throws ElasticException {
        List<String> storedFields = new ArrayList<>();
        for (String colonne : colonnes) {
            if (properties.getProperty(colonne).isStored()) {
                storedFields.add(colonne);
            } else if (properties.getProperty(colonne).isReturnable()) {
                if (properties.getProperty(colonne).isHasKeyword()) {
                    sourceBuilder.docValueField(colonne + ".keyword");
                } else {
                    sourceBuilder.docValueField(colonne);
                }
            } else {
                throw new ElasticException("La colonne suivante ne peut pas être remontée " + colonne);
            }
        }
        sourceBuilder.storedFields(storedFields);
    }

    private enum ParametrePonderation {
        DOSSIER_TITRE_ACTE(ElasticDossier.DOS_TITRE_ACTE, "parametre-titreacte"),

        DOSSIER_NUMERO_NOR(ElasticDossier.DOS_NUMERO_NOR, "parametre-numeronor"),

        DOSSIER_CHARGES_MISSION(ElasticDossier.DOS_NOM_COMPLET_CHARGES_MISSIONS, "parametre-chargemission"),

        DOSSIER_CONSEILLERS_PM(ElasticDossier.DOS_NOM_COMPLET_CONSEILLERS_PMS, "parametre-conseillerpm"),

        DOSSIER_INFO_JO(ElasticDossier.RETDILA_TITRE_OFFICIEL, "parametre-info-jo"),

        DOSSIER_COM_SGG_DILA(ElasticDossier.DOS_ZONE_COM_SGG_DILA, "parametre-comm-sgg-dila"),

        DOSSIER_BASE_LEGALE(ElasticDossier.DOS_BASE_LEGALE_ACTE, "parametre-base-legale"),

        DOSSIER_RUBRIQUES(ElasticDossier.DOS_RUBRIQUES, "parametre-rubrique"),

        DOSSIER_MOTS_CLEFS(ElasticDossier.DOS_MOTSCLES, "parametre-mots-clefs"),

        DOSSIER_CHAMPS_LIBRES(ElasticDossier.DOS_LIBRE, "parametre-champs-libres"),

        DOSSIER_CE_RAPPORTEUR(ElasticDossier.CONSETAT_RAPPORTEUR_CE, "parametre-champs-ce"),

        DOSSIER_CE_SECTION(ElasticDossier.CONSETAT_SECTION_CE, "parametre-champs-ce"),

        DOSSIER_DIRECTIVE_TITRE(ElasticTranspositionDirective.TITRE, "parametre-champs-directive"),

        DOSSIER_DIRECTIVE_COMMENTAIRE(ElasticTranspositionDirective.COMMENTAIRE, "parametre-champs-directive"),

        DOSSIER_NUMERO_TEXTE_PARUTION_JORF(
            ElasticDossier.RETDILA_NUMERO_TEXTE_PARUTION_JORF,
            "parametre-champs-numero-jo"
        ),

        DOCUMENT_TITRE(ElasticDossier.DOCUMENTS + "." + ElasticDocument.DC_TITLE, "parametre-titredocument"),

        DOCUMENT_FILEDATA(ElasticDossier.DOCUMENTS + "." + ElasticDocument.FILE_DATA, "parametre-textedoc");

        private final String nomChamp;
        private final String nomParam;

        ParametrePonderation(String pNomChamp, String pNomParam) {
            nomChamp = pNomChamp;
            nomParam = pNomParam;
        }

        /**
         * Renvoie les pondérations sur l'ensemble des champs du dossier.
         *
         * @param session
         * @return
         */
        static Map<String, Float> toStringAllDossier(CoreSession session) {
            return Stream
                .of(values())
                .filter(p -> p.name().startsWith("DOSSIER_"))
                .flatMap(p -> p.toMap(session).entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        Map<String, Float> toMap(CoreSession session) {
            ElasticParametrageService elasticService = SolonEpgServiceLocator.getElasticParametrageService();

            String paramValue = elasticService.getParametreValue(session, nomParam);
            float poids = 1.0f;
            if (paramValue != null) {
                poids = Float.parseFloat(paramValue);
            }

            return ImmutableMap.of(nomChamp, poids);
        }
    }

    public static void addVecteurPublication(RechercheLibre criteria, SearchRequest request) {
        SearchRequestUtils.addFacet(criteria.getVecteurPublication(), request, ElasticDossier.DOS_VECTEUR_PUBLICATION);
    }

    public static void addArchive(RechercheLibre criteria, SearchRequest request) {
        Boolean boolArchive = criteria.isArchive();
        Boolean boolProd = criteria.isProd();
        if (boolArchive ^ boolProd) {
            ArrayList<String> values = new ArrayList<>();
            values.add(boolArchive.toString());
            SearchRequestUtils.addFacet(values, request, ElasticDossier.DOS_ARCHIVE);
        }
    }

    public static void addPoste(RechercheLibre criteria, SearchRequest request) {
        String poste = criteria.getPoste();
        List<QueryBuilder> mustClauses = ((BoolQueryBuilder) request.source().query()).must();

        if (StringUtils.isNotEmpty(poste)) {
            BoolQueryBuilder matchQuery = new BoolQueryBuilder();
            matchQuery.must(
                QueryBuilders.matchQuery(ElasticDossier.STEPS + "." + ElasticStep.RTSK_DISTRIBUTION_MAILBOX_ID, poste)
            );

            if (Boolean.TRUE.equals(criteria.getPosteEnCours())) {
                matchQuery.must(
                    QueryBuilders.matchQuery(
                        ElasticDossier.STEPS + "." + ElasticStep.ECM_CURRENT_LIFE_CYCLE_STATE,
                        STLifeCycleConstant.RUNNING_STATE
                    )
                );
            }

            BoolQueryBuilder mustQuery = new BoolQueryBuilder();
            mustQuery.must(QueryBuilders.nestedQuery(ElasticDossier.STEPS, matchQuery, ScoreMode.Avg));

            mustClauses.add(mustQuery);
        }
    }

    public static void addCategorieActe(RechercheLibre criteria, SearchRequest request) {
        SearchRequestUtils.addFacet(criteria.getCategorieActe(), request, ElasticDossier.DOS_CATEGORIE_ACTE);
    }

    public static void addTypeActe(RechercheLibre criteria, SearchRequest request) {
        SearchRequestUtils.addFacet(criteria.getTypeActe(), request, ElasticDossier.DOS_TYPE_ACTE);
    }

    public static void addStatut(RechercheLibre criteria, SearchRequest request) {
        SearchRequestUtils.addFacet(criteria.getStatut(), request, ElasticDossier.DOS_STATUT);
    }

    public static void addDirectionAttache(RechercheLibre criteria, SearchRequest request) {
        SearchRequestUtils.addFacet(criteria.getDirectionAttache(), request, ElasticDossier.DOS_DIRECTION_ATTACHE);
    }

    public static void addMinistereAttache(RechercheLibre criteria, SearchRequest request) {
        SearchRequestUtils.addFacet(criteria.getMinistereAttache(), request, ElasticDossier.DOS_MINISTERE_ATTACHE);
    }

    public static void addStatutArchivage(RechercheLibre criteria, SearchRequest request) {
        SearchRequestUtils.addFacet(criteria.getStatutArchivage(), request, ElasticDossier.DOS_STATUT_ARCHIVAGE, true);
    }

    public static void addDatesPublication(RechercheLibre criteria, SearchRequest request) {
        addDatesRange(
            ElasticDossier.DOS_DATE_PUBLICATION,
            criteria.getDatePublicationMin(),
            criteria.getDatePublicationMax(),
            request
        );
    }

    public static void addDatesPublicationJo(RechercheLibre criteria, SearchRequest request) {
        addDatesRange(
            ElasticDossier.RETDILA_DATE_PARUTION_JORF,
            criteria.getDatePublicationJoMin(),
            criteria.getDatePublicationJoMax(),
            request
        );
    }

    public static void addDatesCreation(RechercheLibre criteria, SearchRequest request) {
        addDatesRange(
            ElasticDossier.DOS_CREATED,
            criteria.getDateCreationMin(),
            criteria.getDateCreationMax(),
            request
        );
    }

    public static void addDatesSignature(RechercheLibre criteria, SearchRequest request) {
        addDatesRange(
            ElasticDossier.DOS_DATE_SIGNATURE,
            criteria.getDateSignatureMin(),
            criteria.getDateSignatureMax(),
            request
        );
    }

    public static void addDatesRange(String field, String dateMin, String dateMax, SearchRequest request) {
        BoolQueryBuilder boolQueryBuilder = (BoolQueryBuilder) request.source().query();
        List<QueryBuilder> mustClauses = boolQueryBuilder.must();

        BoolQueryBuilder query = new BoolQueryBuilder();
        if (StringUtils.isNotEmpty(dateMin) && StringUtils.isNotEmpty(dateMax)) {
            Date dateMinTime = SolonDateConverter.DATE_SLASH.parseToCalendar(dateMin).getTime();
            Date dateMaxTime = SolonDateConverter.DATE_SLASH.parseToCalendar(dateMax).getTime();
            query.must(QueryBuilders.rangeQuery(field).from(dateMinTime, true).to(dateMaxTime, true));
        } else if (StringUtils.isNotEmpty(dateMin)) {
            Date dateMinTime = SolonDateConverter.DATE_SLASH.parseToCalendar(dateMin).getTime();
            query.must(QueryBuilders.rangeQuery(field).gte(dateMinTime));
        } else if (StringUtils.isNotEmpty(dateMax)) {
            Date dateMaxTime = SolonDateConverter.DATE_SLASH.parseToCalendar(dateMax).getTime();
            query.must(QueryBuilders.rangeQuery(field).lte(dateMaxTime));
        }

        if (StringUtils.isNotEmpty(dateMin) || StringUtils.isNotEmpty(dateMax)) {
            mustClauses.add(query);
        }
    }

    public static void addTri(
        Map<String, String> triSet,
        QueryBuildersPropertiesConfiguration properties,
        SearchRequest request,
        SearchSourceBuilder sourceBuilder
    )
        throws ElasticException {
        List<Entry<String, String>> definedProperties = triSet
            .entrySet()
            .stream()
            .filter(e -> properties.getProperty(e.getKey()) != null)
            .collect(Collectors.toList());

        definedProperties
            .stream()
            .filter(
                e ->
                    !properties.getProperty(e.getKey()).isReturnable() ||
                    !properties.getProperty(e.getKey()).isSortable()
            )
            .findAny()
            .ifPresent(
                k -> {
                    throw new ElasticException("La colonne suivante ne peut être ni remontée ni triée " + k.getKey());
                }
            );

        definedProperties.forEach(
            p -> {
                QueryBuilderProperty property = properties.getProperty(p.getKey());
                String sortKey = p.getKey() + (property.isHasKeyword() ? ".keyword" : "");
                request.source().sort(sortKey, SortOrder.fromString(p.getValue()));
                sourceBuilder.docValueField(sortKey);
            }
        );
    }

    public static List<String> generateWildcardList(String wildcardAsString) {
        List<String> wildcards = Arrays.asList(wildcardAsString.split("\\s*;\\s*"));
        return wildcards.stream().map(String::trim).collect(Collectors.toList());
    }
}
