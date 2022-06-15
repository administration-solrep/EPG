package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.ui.enums.EpgUserSessionKey.CRITERE_RECHERCHE_KEY;
import static fr.dila.st.api.constant.STConstant.PREFIX_POSTE;
import static fr.dila.st.ui.helper.UserSessionHelper.getUserSessionParameter;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.cases.typescomplexe.ParutionBo;
import fr.dila.solonepg.api.enums.FavorisRechercheType;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.cases.typescomplexe.ParutionBoImpl;
import fr.dila.solonepg.core.dto.DossierLinkMinimalMapper;
import fr.dila.solonepg.core.recherche.EpgDossierListingDTOImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.elastic.models.ElasticApplicationLoi;
import fr.dila.solonepg.elastic.models.ElasticDocument;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.ElasticException;
import fr.dila.solonepg.elastic.models.ElasticParutionBo;
import fr.dila.solonepg.elastic.models.ElasticTranspositionDirective;
import fr.dila.solonepg.elastic.models.ElasticTranspositionOrdonnance;
import fr.dila.solonepg.elastic.models.search.RechercheLibre;
import fr.dila.solonepg.elastic.models.search.RechercheLibre.SortType;
import fr.dila.solonepg.elastic.models.search.SearchCriteriaExp;
import fr.dila.solonepg.elastic.models.search.SearchCriteriaRapide;
import fr.dila.solonepg.elastic.models.search.SearchResult;
import fr.dila.solonepg.elastic.models.search.SearchResultExp;
import fr.dila.solonepg.elastic.models.search.enums.FacetEnum;
import fr.dila.solonepg.elastic.services.RequeteurService;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.recherchelibre.CritereRechercheForm;
import fr.dila.solonepg.ui.bean.recherchelibre.DocumentRechercheDTO;
import fr.dila.solonepg.ui.bean.recherchelibre.FiltreItemDTO;
import fr.dila.solonepg.ui.bean.recherchelibre.RechercheDossierDTO;
import fr.dila.solonepg.ui.bean.recherchelibre.RechercheDossierList;
import fr.dila.solonepg.ui.constants.EpgUIConstants;
import fr.dila.solonepg.ui.enums.EpgColumnEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.helper.EpgDossierListHelper;
import fr.dila.solonepg.ui.helper.EpgRequeteHelper;
import fr.dila.solonepg.ui.jaxrs.webobject.page.recherche.EpgRechercheLibre;
import fr.dila.solonepg.ui.services.EpgRechercheUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.ss.api.recherche.IdLabel;
import fr.dila.ss.ui.bean.RequeteExperteDTO;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.enums.SSUserSessionKey;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.STCorbeilleService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.core.helper.PaginationHelper;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.core.util.StreamUtils;
import fr.dila.st.core.util.StringHelper;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;

public class EpgRechercheUIServiceImpl implements EpgRechercheUIService {
    private static final String REQUIRED_DTO = "Un dto est attendu";
    private static final String REQUIRED_FORM = "Un formulaire est attendu";
    private static final String REQUIRED_NOR = "Un nor est attendu";

    @Override
    public RechercheDossierList doRechercheLibre(SpecificContext context) {
        return doRechercheLibre(context, toRechercheLibre(context));
    }

    private RechercheDossierList doRechercheLibre(SpecificContext context, RechercheLibre rechercheLibre) {
        try {
            SearchResult results = EpgUIServiceLocator
                .getRequeteurService()
                .getResults(rechercheLibre, context.getSession());

            return toRechercheDossierList(results, context);
        } catch (IOException | URISyntaxException e) {
            throw new NuxeoException(e);
        }
    }

    private EpgDossierList getEpgDossierListForRechercheLibre(SpecificContext context, RechercheLibre rechercheLibre) {
        try {
            SearchResult results = EpgUIServiceLocator
                .getRequeteurService()
                .getResults(rechercheLibre, context.getSession());

            return toEpgDossierList(context, results);
        } catch (IOException | URISyntaxException e) {
            throw new NuxeoException(e);
        }
    }

    @Override
    public EpgDossierList getResultsForJsonQuery(SpecificContext context) {
        String jsonQuery = context.getFromContextData(EpgContextDataKey.JSON_QUERY);
        FavorisRechercheType type = context.getFromContextData(EpgContextDataKey.TYPE_RECHERCHE);
        CoreSession session = context.getSession();
        ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
        Long pageSizeUtilisateur = profilUtilisateurService.getUtilisateurPageSize(session);

        EpgDossierList list;
        DossierListForm dossierListForm = context.getFromContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM);

        Gson gson = new Gson();
        if (type == FavorisRechercheType.ES_LIBRE) {
            RechercheLibre rechercheLibre = gson.fromJson(jsonQuery, RechercheLibre.class);
            rechercheLibre.setPage(dossierListForm.getPage());
            rechercheLibre.setPageSize(dossierListForm.getSize());
            list = getEpgDossierListForRechercheLibre(context, rechercheLibre);
        } else if (type == FavorisRechercheType.ES_EXPERTE) {
            SearchCriteriaExp searchCriteriaExp = gson.fromJson(jsonQuery, SearchCriteriaExp.class);
            searchCriteriaExp.setPage(dossierListForm.getPage());

            if (pageSizeUtilisateur != null) {
                searchCriteriaExp.setPageSize(pageSizeUtilisateur.intValue());
            }
            searchCriteriaExp.setTris(getTris(dossierListForm));

            list = doRechercheExperte(context, searchCriteriaExp);
        } else {
            throw new NuxeoException("Type non géré : " + type);
        }

        EpgDossierListHelper.setUserColumns(
            session,
            dossierListForm,
            list,
            Lists.newArrayList(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName())
        );
        return list;
    }

    @Override
    public String getJsonQuery(SpecificContext context) {
        FavorisRechercheType type = context.getFromContextData(EpgContextDataKey.TYPE_RECHERCHE);

        Object objectToSerialize;

        if (type == FavorisRechercheType.ES_LIBRE) {
            CritereRechercheForm rechercheForm = getUserSessionParameter(
                context,
                EpgUserSessionKey.CRITERE_RECHERCHE_KEY
            );
            context.putInContextData(EpgContextDataKey.CRITERE_RECHERCHE_FORM, rechercheForm);
            objectToSerialize = toRechercheLibre(context);
        } else if (type == FavorisRechercheType.ES_EXPERTE) {
            DossierListForm dossierListForm = getUserSessionParameter(context, EpgUserSessionKey.DOSSIER_LIST_FORM);
            dossierListForm = ObjectHelper.requireNonNullElseGet(dossierListForm, DossierListForm::newForm);
            context.putInContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM, dossierListForm);
            context.putInContextData(
                SSContextDataKey.REQUETE_EXPERTE_DTO,
                UserSessionHelper.getUserSessionParameter(
                    context,
                    SSUserSessionKey.REQUETE_EXPERTE_DTO + EpgUIConstants.EPG_SUFFIX
                )
            );
            objectToSerialize = toSearchCriteriaExp(context);
        } else {
            throw new NuxeoException("Type non géré : " + type);
        }

        return new Gson().toJson(objectToSerialize);
    }

    @Override
    public RechercheLibre toRechercheLibre(SpecificContext context) {
        CritereRechercheForm critereRechercheForm = Objects.requireNonNull(
            context.getFromContextData(EpgContextDataKey.CRITERE_RECHERCHE_FORM),
            REQUIRED_FORM
        );

        RechercheLibre rechercheLibre = new RechercheLibre();

        rechercheLibre.setPage(critereRechercheForm.getPage());
        rechercheLibre.setPageSize(critereRechercheForm.getSize());

        if (StringUtils.isNoneBlank(critereRechercheForm.getSort())) {
            switch (critereRechercheForm.getSort()) {
                case EpgRechercheLibre.OPTIONS_TRI_DATE_CREATION_ASC:
                    rechercheLibre.setSortType(SortType.CREATION_ASC);
                    break;
                case EpgRechercheLibre.OPTIONS_TRI_DATE_CREATION_DESC:
                    rechercheLibre.setSortType(SortType.CREATION_DESC);
                    break;
                case EpgRechercheLibre.OPTIONS_TRI_DATE_PUBLICATION_ASC:
                    rechercheLibre.setSortType(SortType.PUBLICATION_ASC);
                    break;
                case EpgRechercheLibre.OPTIONS_TRI_DATE_PUBLICATION_DESC:
                    rechercheLibre.setSortType(SortType.PUBLICATION_DESC);
                    break;
                default:
                    rechercheLibre.setSortType(SortType.RELEVANCE_DESC);
                    break;
            }
        }

        rechercheLibre.setFulltext(critereRechercheForm.getRecherche());
        rechercheLibre.setExpressionExacte(BooleanUtils.isTrue(critereRechercheForm.getExpressionExacte()));
        rechercheLibre.setDocCurrentVersion(BooleanUtils.isTrue(critereRechercheForm.getDerniereVersion()));
        rechercheLibre.setArchive(BooleanUtils.isTrue(critereRechercheForm.getBaseArchivage()));
        rechercheLibre.setProd(BooleanUtils.isNotFalse(critereRechercheForm.getBaseProduction()));

        rechercheLibre.setCategorieActe(getValuesFromSelectValueDTOs(critereRechercheForm.getCategorieActe()));
        rechercheLibre.setMinistereAttache(getValuesFromSelectValueDTOs(critereRechercheForm.getMinistereAttache()));
        rechercheLibre.setDirectionAttache(getValuesFromSelectValueDTOs(critereRechercheForm.getDirectionAttache()));
        rechercheLibre.setStatut(getValuesFromSelectValueDTOs(critereRechercheForm.getStatut()));
        rechercheLibre.setStatutArchivage(getValuesFromSelectValueDTOs(critereRechercheForm.getStatutArchivage()));
        rechercheLibre.setTypeActe(getValuesFromSelectValueDTOs(critereRechercheForm.getTypeActe()));
        rechercheLibre.setVecteurPublication(
            getValuesFromSelectValueDTOs(critereRechercheForm.getVecteurPublication())
        );

        rechercheLibre.setDateCreationMin(
            SolonDateConverter.DATE_SLASH.format(critereRechercheForm.getDateDebutCreation())
        );
        rechercheLibre.setDateCreationMax(
            SolonDateConverter.DATE_SLASH.format(critereRechercheForm.getDateFinCreation())
        );
        rechercheLibre.setDatePublicationMin(
            SolonDateConverter.DATE_SLASH.format(critereRechercheForm.getDateDebutPublication())
        );
        rechercheLibre.setDatePublicationMax(
            SolonDateConverter.DATE_SLASH.format(critereRechercheForm.getDateFinPublication())
        );
        rechercheLibre.setDatePublicationJoMin(
            SolonDateConverter.DATE_SLASH.format(critereRechercheForm.getDateDebutPublicationJO())
        );
        rechercheLibre.setDatePublicationJoMax(
            SolonDateConverter.DATE_SLASH.format(critereRechercheForm.getDateFinPublicationJO())
        );
        rechercheLibre.setDateSignatureMin(
            SolonDateConverter.DATE_SLASH.format(critereRechercheForm.getDateDebutSignature())
        );
        rechercheLibre.setDateSignatureMax(
            SolonDateConverter.DATE_SLASH.format(critereRechercheForm.getDateFinSignature())
        );
        if (StringUtils.isNotBlank(critereRechercheForm.getPosteId())) {
            rechercheLibre.setPoste(PREFIX_POSTE + critereRechercheForm.getPosteId());
            if (Boolean.TRUE.equals(critereRechercheForm.getPosteEnCours())) {
                rechercheLibre.setPosteEnCours(true);
            }
        }

        return rechercheLibre;
    }

    private static List<String> getValuesFromSelectValueDTOs(List<SelectValueDTO> selectValueDTOs) {
        return getWithGetterFromSelectValueDTOs(selectValueDTOs, SelectValueDTO::getValue);
    }

    private static List<String> getWithGetterFromSelectValueDTOs(
        List<SelectValueDTO> selectValueDTOs,
        Function<SelectValueDTO, String> getter
    ) {
        return selectValueDTOs.stream().map(getter).collect(Collectors.toList());
    }

    private RechercheDossierList toRechercheDossierList(SearchResult results, SpecificContext context) {
        RechercheDossierList rechercheDossierList = new RechercheDossierList();

        Map<FacetEnum, List<FiltreItemDTO>> facets = rechercheDossierList.getFacets();
        facets.put(
            FacetEnum.CATEGORIE_ACTE,
            toListFiltreItemDTO(FacetEnum.CATEGORIE_ACTE, results, this::newFiltreItemDTO)
        );
        STUsAndDirectionService stUsAndDirectionService = STServiceLocator.getSTUsAndDirectionService();
        facets.put(
            FacetEnum.DIRECTION_ATTACHE,
            toListFiltreItemDTO(
                FacetEnum.DIRECTION_ATTACHE,
                results,
                bucket ->
                    this.newKeyValueFiltreItemDTO(
                            bucket,
                            id ->
                                this.getLabel(
                                        stUsAndDirectionService::getUniteStructurelleNode,
                                        UniteStructurelleNode::getLabel,
                                        id
                                    )
                        )
            )
        );
        STMinisteresService stMinisteresService = STServiceLocator.getSTMinisteresService();
        facets.put(
            FacetEnum.MINISTERE_ATTACHE,
            toListFiltreItemDTO(
                FacetEnum.MINISTERE_ATTACHE,
                results,
                bucket ->
                    this.newKeyValueFiltreItemDTO(
                            bucket,
                            id -> this.getLabel(stMinisteresService::getEntiteNode, EntiteNode::getLabel, id)
                        )
            )
        );
        facets.put(FacetEnum.STATUT, toListFiltreItemDTO(FacetEnum.STATUT, results, this::newFiltreItemDTO));
        facets.put(
            FacetEnum.STATUT_ARCHIVAGE,
            toListFiltreItemDTO(FacetEnum.STATUT_ARCHIVAGE, results, this::newFiltreItemDTO)
        );
        facets.put(FacetEnum.TYPE_ACTE, toListFiltreItemDTO(FacetEnum.TYPE_ACTE, results, this::newFiltreItemDTO));
        facets.put(
            FacetEnum.VECTEUR_PUBLICATION,
            toListFiltreItemDTO(FacetEnum.VECTEUR_PUBLICATION, results, this::newFiltreItemDTO)
        );

        List<RechercheDossierDTO> listRechercheDossierDTO = results
            .getResults()
            .stream()
            .map(result -> toRechercheDossierDTO(result, context))
            .collect(Collectors.toList());
        rechercheDossierList.setListe(listRechercheDossierDTO);
        rechercheDossierList.setNbTotal(results.getCount().intValue());
        return rechercheDossierList;
    }

    private List<FiltreItemDTO> toListFiltreItemDTO(
        FacetEnum facet,
        SearchResult results,
        Function<Bucket, FiltreItemDTO> itemDtoMapper
    ) {
        return results
            .getFacetEntries(facet)
            .stream()
            .flatMap(e -> e.getBuckets().stream())
            .map(itemDtoMapper)
            .collect(Collectors.toList());
    }

    private FiltreItemDTO newFiltreItemDTO(Bucket bucket) {
        // on modifie tous les caractères spéciaux pour générer un Id
        String id = StringHelper.convertToId(bucket.getKeyAsString());
        return new FiltreItemDTO(id, bucket.getKeyAsString(), (int) bucket.getDocCount());
    }

    private FiltreItemDTO newKeyValueFiltreItemDTO(Bucket bucket, Function<String, String> labelGetter) {
        // on modifie tous les caractères spéciaux pour générer un Id
        String id = StringHelper.convertToId(bucket.getKeyAsString());
        return new FiltreItemDTO(id, labelGetter.apply(bucket.getKeyAsString()), (int) bucket.getDocCount());
    }

    private RechercheDossierDTO toRechercheDossierDTO(ElasticDossier dossier, SpecificContext context) {
        RechercheDossierDTO dto = new RechercheDossierDTO();

        dto.setDateCreation(DateUtil.dateToGregorianCalendar(dossier.getDosCreated()));
        dto.setDatePublication(DateUtil.dateToGregorianCalendar(dossier.getDosDatePublication()));
        dto.setDossierId(dossier.getUid());
        STMinisteresService stMinisteresService = STServiceLocator.getSTMinisteresService();
        dto.setMinistereResponsable(
            getLabel(stMinisteresService::getEntiteNode, EntiteNode::getLabel, dossier.getDosMinistereResp())
        );
        dto.setNor(dossier.getDosNumeroNor());
        dto.setStatut(dossier.getDosStatut());
        dto.setTitre(dossier.getDosTitreActe());
        dto.setIsInSelectionTool(
            EpgUIServiceLocator.getEpgSelectionToolUIService().isInSelectionTool(context, dossier.getUid())
        );

        dto.setLstDocuments(
            dossier.getDocuments().stream().map(this::toDocumentRechercheDTO).collect(Collectors.toList())
        );
        dto.setCaseLinkIdsLabels(getIdLabelForCurrentSteps(context, dossier));

        return dto;
    }

    private DocumentRechercheDTO toDocumentRechercheDTO(ElasticDocument document) {
        DocumentRechercheDTO documentRechercheDTO = new DocumentRechercheDTO();

        documentRechercheDTO.setNom(document.getDcTitle());
        document.getVersion().ifPresent(documentRechercheDTO::setVersion);
        documentRechercheDTO.setExtrait(document.getFileData());

        return documentRechercheDTO;
    }

    @Override
    public EpgDossierList doRechercheRapide(SpecificContext context) {
        RequeteurService requeteurService = EpgUIServiceLocator.getRequeteurService();
        String nor = Objects.requireNonNull(context.getFromContextData(EpgContextDataKey.DOSSIER_NOR), REQUIRED_NOR);

        DossierListForm form = Objects.requireNonNull(
            context.getFromContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM),
            REQUIRED_FORM
        );

        try {
            SearchCriteriaRapide rechercheRapide = new SearchCriteriaRapide();
            rechercheRapide.setWildcardList(Stream.of(nor.split(";")).collect(Collectors.toList()));
            rechercheRapide.setPage(form.getPage());
            rechercheRapide.setPageSize(form.getSize());
            rechercheRapide.setTris(getTris(form));

            SearchResultExp results = requeteurService.getResults(rechercheRapide, context.getSession());

            return toEpgDossierList(context, results);
        } catch (IOException | URISyntaxException | ElasticException e) {
            throw new NuxeoException(e);
        }
    }

    private EpgDossierList toEpgDossierList(SpecificContext context, SearchResultExp results) {
        List<String> defaultColumns = SolonEpgServiceLocator
            .getProfilUtilisateurService()
            .getUserEspaceTraitementColumn(context.getSession());
        defaultColumns.addAll(Lists.newArrayList(EpgColumnEnum.INFO_COMPLEMENTAIRE.getNxPropName()));
        DossierListForm form = context.getFromContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM);
        EpgDossierList list = new EpgDossierList();
        list.buildColonnes(form, defaultColumns, true, true);

        list.setNbTotal((int) results.getCount());
        list.setListe(
            results
                .getResults()
                .values()
                .stream()
                .map(res -> toEpgDossierListingDTO(context, res))
                .collect(Collectors.toList())
        );

        return list;
    }

    private EpgDossierList toEpgDossierList(SpecificContext context, SearchResult results) {
        EpgDossierList list = new EpgDossierList();
        list.buildColonnes(null, null, false, true);

        list.setNbTotal(results.getCount());
        list.setListe(
            results.getResults().stream().map(res -> toEpgDossierListingDTO(context, res)).collect(Collectors.toList())
        );

        return list;
    }

    private <E> String getLabel(Function<String, E> entiteSupplier, Function<E, String> labelGetter, String id) {
        return StringUtils.isNotBlank(id)
            ? Optional.ofNullable(entiteSupplier.apply(id)).map(labelGetter).orElse(id)
            : "";
    }

    private EpgDossierListingDTOImpl toEpgDossierListingDTO(SpecificContext context, ElasticDossier dossier) {
        EpgDossierListingDTOImpl dto = new EpgDossierListingDTOImpl();
        CoreSession session = context.getSession();

        dto.setApplicationLoiRef(
            dossier.getDosApplicationLoi().stream().map(ElasticApplicationLoi::getRef).collect(Collectors.toList())
        );
        dto.setAuthor(dossier.getDosIdCreateurDossier());
        dto.setBaseLegaleActe(dossier.getDosBaseLegaleActe());
        dto.setCaseLinkIdsLabels(getIdLabelForCurrentSteps(context, dossier).toArray(new IdLabel[0]));
        dto.setCategorieActe(dossier.getDosCategorieActe());
        dto.setDateAgCe(dossier.getConsetatDateAgCe());
        dto.setDateArrivee(dossier.getDcModified());
        dto.setDateCreation(dossier.getDosCreated());
        dto.setDateEffet(
            dossier.getDosDateEffet().stream().map(DateUtil::dateToGregorianCalendar).collect(Collectors.toList())
        );
        dto.setDateParutionJorf(dossier.getRetdilaDateParutionJorf());
        dto.setDatePourFournitureEpreuve(dossier.getDosPourFournitureEpreuve());
        dto.setDatePublication(dossier.getDosDatePublication());
        dto.setDatePreciseePublication(dossier.getDosDatePreciseePublication());
        dto.setDateSectionCe(dossier.getConsetatDateSectionCe());
        dto.setDateSignature(dossier.getDosDateSignature());
        dto.setDateTransmissionSectionCe(dossier.getConsetatDateTransmissionSectionCe());
        dto.setDelaiPublication(dossier.getDosDelaiPublication());
        STUsAndDirectionService stUsAndDirectionService = STServiceLocator.getSTUsAndDirectionService();
        dto.setDirectionAttache(
            getLabel(
                stUsAndDirectionService::getUniteStructurelleNode,
                UniteStructurelleNode::getLabel,
                dossier.getDosDirectionAttache()
            )
        );
        dto.setDirectionResp(
            getLabel(
                stUsAndDirectionService::getUniteStructurelleNode,
                UniteStructurelleNode::getLabel,
                dossier.getDosDirectionRespId()
            )
        );
        dto.setDossierCompletErreur(dossier.getDosDossierCompletErreur());
        dto.setDossierId(dossier.getUid());
        dto.setHabilitationCommentaire(dossier.getDosHabilitationCommentaire());
        dto.setHabilitationNumeroArticles(dossier.getDosHabilitationNumeroArticles());
        dto.setHabilitationRefLoi(dossier.getDosHabilitationRefLoi());
        dto.setIndexationChampLibre(dossier.getDosLibre());
        dto.setIndexationMotsCles(dossier.getDosMotscles());
        dto.setIndexationRubrique(dossier.getDosRubriques());
        dto.setLocked(dossier.getDcLocked());
        dto.setLockOwner(dossier.getDcLockOwner());
        dto.setMailRespDossier(dossier.getDosMailRespDossier());
        STMinisteresService stMinisteresService = STServiceLocator.getSTMinisteresService();
        dto.setMinistereAttache(
            getLabel(stMinisteresService::getEntiteNode, EntiteNode::getLabel, dossier.getDosMinistereAttache())
        );
        dto.setMinistereResp(
            getLabel(stMinisteresService::getEntiteNode, EntiteNode::getLabel, dossier.getDosMinistereResp())
        );
        TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
        dto.setModeParution(tableReferenceService.getModeParutionLabel(session, dossier.getRetdilaModeParution()));
        dto.setNomCompletChargesMissions(dossier.getDosNomCompletChargesMissions());
        dto.setNomCompletConseillersPms(dossier.getDosNomCompletConseillersPms());
        dto.setNomRespDossier(dossier.getDosNomRespDossier());
        dto.setNumeroISA(dossier.getConsetatNumeroISA());
        dto.setNumeroNor(dossier.getDosNumeroNor());
        dto.setNumeroTexteParutionJorf(dossier.getRetdilaNumeroTexteParutionJorf());
        dto.setPrenomRespDossier(dossier.getDosPrenomRespDossier());
        dto.setPriorite(dossier.getConsetatPriorite());
        dto.setQualiteRespDossier(dossier.getDosQualiteRespDossier());
        dto.setRapporteurCe(dossier.getConsetatRapporteurCe());
        dto.setSectionCe(dossier.getConsetatSectionCe());
        dto.setStatut(dossier.getDosStatut());
        dto.setTelephoneRespDossier(dossier.getDosTelRespDossier());
        dto.setTitreActe(dossier.getDosTitreActe());
        dto.setTypeActe(dossier.getDosTypeActe());
        dto.setUrgent(dossier.getDosIsUrgent());
        dto.setPublicationsConjointes(dossier.getDosPublicationsConjointes());
        dto.setPublicationRapportPresentation(dossier.getDosPublicationRapportPresentation());
        dto.setPublicationIntegraleOuExtrait(dossier.getDosPublicationIntegraleOuExtrait());
        dto.setPageParutionJorf(dossier.getRetdilaPageParutionJorf());
        dto.setParutionBo(
            dossier
                .getRetdilaParutionBo()
                .stream()
                .map(EpgRechercheUIServiceImpl::mapParutionBo)
                .collect(Collectors.toList())
        );
        dto.setTexteEntreprise(dossier.getDosTexteEntreprise());
        dto.setTranspositionOrdonnanceRef(
            dossier
                .getDosTranspositionOrdonnance()
                .stream()
                .map(ElasticTranspositionOrdonnance::getRef)
                .collect(Collectors.toList())
        );
        dto.setTranspositionDirectiveRef(
            dossier
                .getDosTranspositionDirective()
                .stream()
                .map(ElasticTranspositionDirective::getRef)
                .collect(Collectors.toList())
        );
        dto.setVecteurPublication(dossier.getDosVecteurPublication());
        dto.setZoneComSggDila(dossier.getDosZoneComSggDila());
        return dto;
    }

    private List<IdLabel> getIdLabelForCurrentSteps(SpecificContext context, ElasticDossier dossier) {
        STCorbeilleService corbeilleService = STServiceLocator.getCorbeilleService();
        List<DocumentModel> dossiersLinks = corbeilleService.findDossierLink(context.getSession(), dossier.getUid());
        List<IdLabel> caseLinkIdsLabels = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dossiersLinks)) {
            caseLinkIdsLabels =
                dossiersLinks
                    .stream()
                    .map(doc -> doc.getAdapter(DossierLink.class))
                    .map(DossierLinkMinimalMapper::getIdLabelFromDossierLink)
                    .collect(Collectors.toList());
        }
        return caseLinkIdsLabels;
    }

    @Override
    public EpgDossierList doRechercheExperte(SpecificContext context) {
        return doRechercheExperte(context, toSearchCriteriaExp(context));
    }

    private EpgDossierList doRechercheExperte(SpecificContext context, SearchCriteriaExp searchCriteriaExp) {
        RequeteurService requeteurService = SolonEpgUIServiceLocator.getRequeteurService();

        try {
            SearchResultExp results = requeteurService.getResults(searchCriteriaExp, context.getSession());
            DossierListForm form = context.getFromContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM);
            int pageSize = searchCriteriaExp.getPageSize();
            int from = searchCriteriaExp.getFrom();
            form.setPage(PaginationHelper.getPageFromOffsetAndSize(from, pageSize));

            return toEpgDossierList(context, results);
        } catch (IOException | URISyntaxException | ElasticException e) {
            throw new NuxeoException(e);
        }
    }

    @Override
    public SearchCriteriaExp toSearchCriteriaExp(SpecificContext context) {
        DossierListForm form = Objects.requireNonNull(
            context.getFromContextData(EpgContextDataKey.DOSSIER_RECHERCHE_FORM),
            REQUIRED_FORM
        );

        SearchCriteriaExp searchCriteriaExp = new SearchCriteriaExp();
        searchCriteriaExp.setPage(form.getPage());
        searchCriteriaExp.setPageSize(form.getSize());
        searchCriteriaExp.setTotal(form.getTotal());

        searchCriteriaExp.setTris(getTris(form));

        RequeteExperteDTO dto = Objects.requireNonNull(
            context.getFromContextData(SSContextDataKey.REQUETE_EXPERTE_DTO),
            REQUIRED_DTO
        );
        searchCriteriaExp.setClausesOu(EpgRequeteHelper.toListClausesOu(dto.getRequetes()));

        return searchCriteriaExp;
    }

    private LinkedHashMap<String, String> getTris(DossierListForm form) {
        return form
            .getSortInfos()
            .stream()
            .collect(
                Collectors.toMap(
                    s -> StringUtils.substringAfter(s.getSortColumn(), "."),
                    s -> s.getSortAscending() ? SortOrder.ASC.getValue() : SortOrder.DESC.getValue(),
                    StreamUtils.throwingMerger(),
                    LinkedHashMap::new
                )
            );
    }

    @Override
    public CritereRechercheForm getUpdatedCritereRechercheForm(SpecificContext context) {
        // le formulaire contenant les autres paramètres de recherche
        CritereRechercheForm form = context.getFromContextData(EpgContextDataKey.CRITERE_RECHERCHE_FORM);

        // je récupère les critères s'ils existent
        CritereRechercheForm critereRechercheForm = ObjectHelper.requireNonNullElseGet(
            UserSessionHelper.getUserSessionParameter(context, CRITERE_RECHERCHE_KEY),
            CritereRechercheForm::new
        );

        // MAJ du formulaire stocké avec tous les paramètres
        critereRechercheForm.setBaseProduction(form.getBaseProduction());
        critereRechercheForm.setBaseArchivage(form.getBaseArchivage());
        critereRechercheForm.setExpressionExacte(form.getExpressionExacte());
        critereRechercheForm.setDerniereVersion(form.getDerniereVersion());
        critereRechercheForm.setRecherche(form.getRecherche());
        critereRechercheForm.setPosteId(form.getPosteId());
        critereRechercheForm.setPosteEnCours(form.getPosteEnCours());
        critereRechercheForm.setDateDebutCreation(form.getDateDebutCreation());
        critereRechercheForm.setDateFinCreation(form.getDateFinCreation());
        critereRechercheForm.setDateDebutPublication(form.getDateDebutPublication());
        critereRechercheForm.setDateFinPublication(form.getDateFinPublication());
        critereRechercheForm.setDateDebutSignature(form.getDateDebutSignature());
        critereRechercheForm.setDateFinSignature(form.getDateFinSignature());
        critereRechercheForm.setDateDebutPublicationJO(form.getDateDebutPublicationJO());
        critereRechercheForm.setDateFinPublicationJO(form.getDateFinPublicationJO());

        // Comme je recharge la page si pas d'erreur je met en session les critères pour les récupérer au rechargement
        UserSessionHelper.putUserSessionParameter(context, CRITERE_RECHERCHE_KEY, critereRechercheForm);
        return critereRechercheForm;
    }

    private static ParutionBo mapParutionBo(ElasticParutionBo epb) {
        ParutionBo result = new ParutionBoImpl();
        result.setDateParutionBo(DateUtil.dateToGregorianCalendar(epb.getDateParutionBo()));
        return result;
    }
}
