package fr.dila.solonepg.ui.utils;

import com.google.gson.Gson;
import com.google.gson.internal.StringMap;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.FilterableColonneInfo;
import fr.dila.solonepg.ui.bean.IFilterableList;
import fr.dila.solonepg.ui.bean.RapidSearchDTO;
import fr.dila.solonepg.ui.contentview.AbstractDTOFiltrableProvider;
import fr.dila.solonepg.ui.contentview.DocumentModelFiltrablePaginatedPageDocumentProvider;
import fr.dila.solonepg.ui.contentview.DossierPageProvider;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgFiltreEnum;
import fr.dila.solonepg.ui.enums.EpgPageProviders;
import fr.dila.solonepg.ui.enums.IColumnEnum;
import fr.dila.solonepg.ui.enums.IFiltreEnum;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanFiltreEnum;
import fr.dila.solonepg.ui.helper.EpgDossierListHelper;
import fr.dila.solonepg.ui.helper.SolonEpgProviderHelper;
import fr.dila.solonepg.ui.helper.mgpp.MgppEditWidgetFactory;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.st.core.requete.recherchechamp.Parametre;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.STPageProviderHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.core.util.StringEscapeHelper;
import fr.dila.st.core.util.StringHelper;
import fr.dila.st.ui.bean.ColonneInfo;
import fr.dila.st.ui.bean.IColonneInfo;
import fr.dila.st.ui.bean.WidgetDTO;
import fr.dila.st.ui.th.bean.AbstractSortablePaginationForm;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.api.PageProvider;

/**
 * Comment utiliser les méthodes de cette classe ?
 *
 * Dans l'ordre :
 * <ol>
 * <li>{@link #extractMapSearch(String)} : extrait la map search depuis la
 * chaine json</li>
 * <li>{@link #extractProviderInfos(Map, List, Map, CoreSession)} : récupère
 * depuis le front les informations liées au provider</li>
 * <li>{@link #putProviderInfosInContextData(SpecificContext, String, List, String, PageProvider)}
 * : Injecte dans le contextData les informations du provider</li>
 * <li>{@link #applyFiltersOnProvider(CoreSession, PageProvider, Map, DossierListForm, Long)}
 * : calcule la liste de résultats à partir des informations fournies</li>
 * <li>{@link #initOtherParamMapWithProviderInfos(SpecificContext)} : initialise
 * le param otherParams pour le front avec les infos du provider.</li>
 * <li>{@link #putRapidSearchDtoIntoTemplateData(SpecificContext, IFilterableList, Map)}
 * : Construit la liste des filtres.</li>
 * </ol>
 *
 */
public final class FiltreUtils {
    private static final String ORDER_SUFFIX = "Order";
    private static final String SORT_SUFFIX = "Sort";

    private FiltreUtils() {}

    public static Serializable convertValue(Serializable widgetValue) {
        return widgetValue instanceof String && ((String) widgetValue).matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")
            ? SolonDateConverter.DATE_SLASH.parseToDateOrNull((String) widgetValue)
            : widgetValue;
    }

    /**
     * Reconstitue la liste des paramètres à adresser à un provider à partir des
     * éléments serializés en json-friendly (pour stockage dans le DOM)
     */
    private static List<String> parseProviderParams(String providerParamsStr) {
        @SuppressWarnings("unchecked")
        List<Object> providerParams = ObjectHelper.requireNonNullElseGet(
            new Gson().fromJson(providerParamsStr, List.class),
            ArrayList::new
        );

        return providerParams
            .stream()
            .map(oneParam -> ((String) oneParam).split(","))
            .map(elts -> StringHelper.join(elts, ",", "'"))
            .map(str -> str.replaceAll("'\\(", "('").replaceAll("\\)'", "')"))
            .collect(Collectors.toList());
    }

    /**
     * Reconstitue la liste des SortInfo à adresser à un provider à partir des
     * éléments serializés en json-friendly (pour stockage dans le DOM)
     */
    public static List<SortInfo> parseSortInfo(String providerParamsStr) {
        @SuppressWarnings("unchecked")
        List<Object> providerParams = new Gson().fromJson(providerParamsStr, List.class);

        return providerParams
            .stream()
            .map(sortInfoMap -> (StringMap) sortInfoMap)
            .map(map -> new SortInfo((String) map.get("sortColumn"), (boolean) map.get("sortAscending")))
            .collect(Collectors.toList());
    }

    public static String convertWidgetNameToNuxeoField(String widgetName, Class<? extends IFiltreEnum> filtreClass) {
        String appender = "";
        String correctWidgetName = StringUtils.removeEnd(widgetName, "Debut");

        if (widgetName.endsWith("Fin")) {
            correctWidgetName = StringUtils.removeEnd(widgetName, "Fin");
            appender = DocumentModelFiltrablePaginatedPageDocumentProvider.MAX;
        }

        IFiltreEnum filtre = null;
        if (EpgFiltreEnum.class.equals(filtreClass)) {
            filtre = EpgFiltreEnum.fromWidgetName(correctWidgetName);
        } else if (PanFiltreEnum.class.equals(filtreClass)) {
            filtre = PanFiltreEnum.fromWidgetName(correctWidgetName);
        }

        return filtre == null ? null : filtre.getXpath() + appender;
    }

    /**
     * Extrait la map search depuis les données transmises directement du front.
     * Cette map contient les données pour les filtres.
     */
    public static Map<String, Serializable> extractMapSearch(String jsonSearch) {
        Gson gson = new Gson();

        return jsonSearch != null ? gson.fromJson(jsonSearch, Map.class) : new HashMap<>();
    }

    /**
     * Extraction des informations du provider à partir de la map envoyée par le
     * front : params + filtres
     *
     * @param mapSearch      map envoyée en entrée, correspond aux éléments envoyés
     *                       par le front
     * @param providerParams map à remplir avec les paramètres du provider (sans les
     *                       filtres)
     * @param filters        map à remplir avec les filtres
     * @return une paire contenant le nom du provider et le titre de la liste
     *         récupérés tous deux depuis la map.
     */
    public static Pair<PageProvider<?>, String> extractProviderInfos(
        Map<String, Serializable> mapSearch,
        List<String> providerParams,
        Map<String, Serializable> filters,
        CoreSession session,
        Class<? extends IFiltreEnum> filtreClass
    ) {
        extractFilters(mapSearch, filters, EpgFiltreEnum.class);

        // Nom du provider
        String providerName = (String) mapSearch.get(EpgContextDataKey.PROVIDER_NAME.getName());

        // Récupération des params
        String providerParamsStr = (String) mapSearch.get(EpgContextDataKey.PROVIDER_PARAM_LIST.getName());
        providerParams.addAll(parseProviderParams(providerParamsStr));

        // Titre de la liste
        String providerTitle = (String) mapSearch.get(EpgContextDataKey.PROVIDER_TITLE.getName());

        // Provider
        PageProvider<?> provider = STPageProviderHelper.getPageProvider(
            providerName,
            session,
            providerParams.toArray()
        );

        // Informations de tri
        if (provider instanceof DossierPageProvider) {
            List<SortInfo> sortInfos = extractSortInfos(mapSearch, (DossierPageProvider) provider, filtreClass);
            if (CollectionUtils.isNotEmpty(sortInfos)) {
                provider.setSortInfos(sortInfos);
            } else {
                // Tri par défaut : par NOR
                provider.setSortInfo(new SortInfo("d.dos:numeroNor", true));
            }
        }

        return Pair.of(provider, providerTitle);
    }

    /**
     * @param filtreClass Quelle classe de filtre ?
     */
    public static void extractFilters(
        Map<String, Serializable> mapSearch,
        Map<String, Serializable> filters,
        Class<? extends IFiltreEnum> filtreClass
    ) {
        // Récupération des filtres
        for (Entry<String, Serializable> entry : mapSearch.entrySet()) {
            String key = convertWidgetNameToNuxeoField(entry.getKey(), filtreClass);
            Serializable value = convertValue(entry.getValue());

            if (!"asc".equals(value) && !"desc".equals(value)) {
                IFiltreEnum filtre = null;

                if (EpgFiltreEnum.class.equals(filtreClass)) {
                    filtre = EpgFiltreEnum.fromXpath(key);
                } else if (PanFiltreEnum.class.equals(filtreClass)) {
                    filtre = PanFiltreEnum.fromXpath(key);
                }

                if (filtre != null) {
                    value = filtre.computeTreatedValue(value);
                }

                if (key != null) {
                    filters.put(key, value);
                }
            }
        }
    }

    /**
     * Extraction des informations de tri depuis la mapSearch récupérée du front et
     * le provider.
     */
    private static List<SortInfo> extractSortInfos(
        Map<String, Serializable> mapSearch,
        DossierPageProvider provider,
        Class<? extends IFiltreEnum> filtreClass
    ) {
        return mapSearch
            .entrySet()
            .stream()
            .filter(e -> e.getKey().endsWith(SORT_SUFFIX))
            .map(e -> toPair(mapSearch, provider, filtreClass, e))
            .filter(p -> p.getKey() != null)
            .sorted(Comparator.comparingInt(ImmutablePair::getValue))
            .map(ImmutablePair::getKey)
            .collect(Collectors.toList());
    }

    private static ImmutablePair<SortInfo, Integer> toPair(
        Map<String, Serializable> mapSearch,
        DossierPageProvider provider,
        Class<? extends IFiltreEnum> filtreClass,
        Entry<String, Serializable> entry
    ) {
        String field = convertWidgetNameToNuxeoField(StringUtils.remove(entry.getKey(), SORT_SUFFIX), filtreClass);
        Integer priority = Optional
            .ofNullable((String) mapSearch.get(entry.getKey() + ORDER_SUFFIX))
            .map(order -> StringUtils.unwrap(order, "'"))
            .map(Integer::valueOf)
            .orElse(null);
        if (field != null) {
            int dotPos = field.indexOf(':');
            if (dotPos >= 0) {
                String schema = field.substring(0, dotPos);
                String prefix = provider.getPrefixForSchema().get(schema);

                return ImmutablePair.of(new SortInfo(prefix + "." + field, "asc".equals(entry.getValue())), priority);
            }
        }
        return ImmutablePair.nullPair();
    }

    /**
     * Applique les filtres sur le provider, renvoie la liste de résultats de la
     * page courante.
     *
     * @param session         session courante
     * @param provider        le provider à utiliser
     * @param filters         les filtres standards du provider
     * @param dossierListForm le formulaire du front (permet notamment de récupérer
     *                        les colonnes)
     * @param page            page courante
     */
    public static EpgDossierList applyFiltersOnProvider(
        CoreSession session,
        PageProvider<?> provider,
        Map<String, Serializable> filters,
        DossierListForm dossierlistForm,
        Long page
    ) {
        EpgDossierList result;
        if (provider instanceof AbstractDTOFiltrableProvider) {
            AbstractDTOFiltrableProvider filtrableProvider = (AbstractDTOFiltrableProvider) provider;
            filters.forEach(filtrableProvider::addFiltrableProperty);

            List<Map<String, Serializable>> docList = filtrableProvider.setCurrentPage(page);
            dossierlistForm.setPage(1 + (int) filtrableProvider.getCurrentPageIndex());

            String providerName = provider.getName();
            List<String> additionalColumns = SolonEpgProviderHelper.getAdditionalColumns(providerName);

            result =
                EpgDossierListHelper.buildDossierList(
                    session,
                    docList,
                    "",
                    dossierlistForm,
                    additionalColumns,
                    (int) provider.getResultsCount(),
                    providerName
                );
        } else {
            // Pas de filtrage possible ou pas de provider en cours !
            result = new EpgDossierList();
        }
        return result;
    }

    /**
     * Transforme la liste d'objects (paramètres de provider par exemple) de manière
     * à supprimer les quotes (json friendly) => la liste obtenue peut être intégrée
     * en paramètre dans le html sans déclencher une alerte ESAPI
     */
    private static List<String> serializeToHtmlParam(List<?> params) {
        return ObjectHelper
            .requireNonNullElseGet(params, ArrayList::new)
            .stream()
            .map(String.class::cast)
            .map(str -> RegExUtils.removeAll(str, "'"))
            .collect(Collectors.toList());
    }

    /**
     * A utiliser lors du calcul d'une liste, met dans le contextData les
     * informations relatives au provider :
     * <ul>
     * <li>le nom du provider</li>
     * <li>les paramètres du provider</li>
     * <li>le titre de la liste à afficher</li>
     * </ul>
     */
    public static void putProviderInfosInContextData(
        SpecificContext context,
        String providerName,
        List<?> providerParams,
        String listTitle,
        PageProvider<?> provider
    ) {
        context.putInContextData(EpgContextDataKey.PROVIDER_NAME, providerName);
        context.putInContextData(EpgContextDataKey.PROVIDER_PARAM_LIST, providerParams);
        context.putInContextData(EpgContextDataKey.PROVIDER_TITLE, listTitle);

        List<Pair<String, String>> sortInfos = provider
            .getSortInfos()
            .stream()
            .map(
                sortInfo -> {
                    String column = sortInfo.getSortColumn();
                    String sortColumn = StringUtils.substringAfter(column, ":") + SORT_SUFFIX;

                    String asc = sortInfo.getSortAscending() ? "asc" : "desc";
                    return Pair.of(sortColumn, asc);
                }
            )
            .collect(Collectors.toList());

        context.putInContextData(EpgContextDataKey.PROVIDER_SORT, sortInfos);
    }

    /**
     * Réincorpore dans les other parameters les infos du provider à destination du
     * front.
     */
    public static Map<String, Object> initOtherParamMapWithProviderInfos(SpecificContext context) {
        Map<String, Object> paramMap = new HashMap<>();

        // Nom du provider
        paramMap.put(
            EpgContextDataKey.PROVIDER_NAME.getName(),
            context.getFromContextData(EpgContextDataKey.PROVIDER_NAME)
        );

        // Paramètres du provider
        List<String> providerParams = serializeToHtmlParam(
            context.getFromContextData(EpgContextDataKey.PROVIDER_PARAM_LIST)
        );
        paramMap.put(EpgContextDataKey.PROVIDER_PARAM_LIST.getName(), new Gson().toJson(providerParams));

        // Titre de la liste
        paramMap.put(
            EpgContextDataKey.PROVIDER_TITLE.getName(),
            StringEscapeHelper.decodeSpecialCharactersHtmlParams(
                context.getFromContextData(EpgContextDataKey.PROVIDER_TITLE)
            )
        );

        // Tris de la liste
        paramMap.put(
            EpgContextDataKey.PROVIDER_SORT.getName(),
            context.getFromContextData(EpgContextDataKey.PROVIDER_SORT)
        );

        return paramMap;
    }

    /**
     * Initialise le rapidSearchDTO et l'injecte dans les template data, à partir de
     * la liste d'objets à afficher <code>lstResults</code>
     */
    public static void putRapidSearchDtoIntoTemplateData(
        SpecificContext context,
        IFilterableList lstResults,
        Map<String, Object> templateData
    ) {
        context.putInContextData(EpgContextDataKey.LIST_FILTERABLE_COLUMNS, lstResults.getFilterableColonnes());
        RapidSearchDTO rapidSearchDTO = buildFilters(context);
        templateData.put(STTemplateConstants.RAPID_SEARCH, rapidSearchDTO.getLstWidgets());
    }

    /**
     * Construction des widgets de filtres à partir des colonnes filtrables.
     */
    private static RapidSearchDTO buildFilters(SpecificContext context) {
        RapidSearchDTO staticFilters = new RapidSearchDTO();
        List<IColonneInfo> columns = ObjectHelper.requireNonNullElse(
            context.getFromContextData(EpgContextDataKey.LIST_FILTERABLE_COLUMNS),
            new ArrayList<>()
        );

        staticFilters
            .getLstWidgets()
            .addAll(
                columns
                    .stream()
                    .filter(FilterableColonneInfo.class::isInstance)
                    .map(FilterableColonneInfo.class::cast)
                    .map(fci -> buildWidgetDTO(fci.getFiltre(), context))
                    .collect(Collectors.toList())
            );

        return staticFilters;
    }

    private static WidgetDTO buildWidgetDTO(IFiltreEnum filtre, SpecificContext context) {
        WidgetDTO widgetFiltre = filtre.initWidget();
        Map<String, Serializable> oldValues = context.getFromContextData(MgppContextDataKey.MAP_SEARCH);
        if (MapUtils.isNotEmpty(oldValues) && oldValues.containsKey(filtre.getWidgetName())) {
            widgetFiltre
                .getParametres()
                .add(new Parametre(MgppEditWidgetFactory.VALEUR, oldValues.get(filtre.getWidgetName())));
        }

        return widgetFiltre;
    }

    /**
     * Construction des actions de la liste.
     *
     * @param map template map
     */
    public static void buildActions(Map<String, Object> map, SpecificContext context) {
        EpgPageProviders provider = EpgPageProviders.fromContentViewName(
            context.getFromContextData(EpgContextDataKey.PROVIDER_NAME)
        );
        if (provider != null) {
            provider.putActionsInMap(map, context);
        }
    }

    /**
     * Rajoute le caractère filtrable à une colonne si celle-ci a un filtre associé.
     */
    public static IColonneInfo decorateFilterable(
        ColonneInfo colonneInfo,
        IColumnEnum<? extends AbstractSortablePaginationForm, ? extends IFiltreEnum> columnEnum
    ) {
        IFiltreEnum filtre = columnEnum.getFiltre();
        if (filtre != null) {
            // Colonne filtrable
            return new FilterableColonneInfo(colonneInfo, filtre);
        }
        return colonneInfo;
    }
}
