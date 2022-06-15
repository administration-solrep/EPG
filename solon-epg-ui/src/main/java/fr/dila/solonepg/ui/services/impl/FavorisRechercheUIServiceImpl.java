package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getEspaceRechercheService;
import static fr.dila.solonepg.ui.enums.EpgContextDataKey.FAVORIS_RECHERCHE_FORM;
import static fr.dila.solonepg.ui.enums.EpgContextDataKey.FAVORIS_RECHERCHE_LIST_FORM;
import static fr.dila.solonepg.ui.enums.EpgUserSessionKey.EPG_FAVORIS_RECHERCHE_MODELE_FDR_FORM;
import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getEpgRechercheModeleFeuilleRouteActionService;
import static fr.dila.solonepg.ui.services.EpgUIServiceLocator.getRechercheModeleFeuilleRouteUIService;
import static fr.dila.st.core.util.ResourceHelper.getString;
import static fr.dila.st.ui.enums.STContextDataKey.USERS_LIST_FORM;
import static fr.dila.st.ui.helper.UserSessionHelper.getUserSessionParameter;
import static fr.dila.st.ui.mapper.MapDoc2Bean.docToBean;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

import fr.dila.solonepg.api.enums.FavorisRechercheType;
import fr.dila.solonepg.api.recherche.FavorisRecherche;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.EpgFavorisRechercheModeleFdrForm;
import fr.dila.solonepg.ui.bean.favoris.recherche.EpgFavorisRechercheDTO;
import fr.dila.solonepg.ui.bean.favoris.recherche.EpgFavorisRechercheList;
import fr.dila.solonepg.ui.contentview.UserRecherchePageDocumentProvider;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.FavorisRechercheModeleFeuilleRouteUIService;
import fr.dila.solonepg.ui.services.FavorisRechercheUIService;
import fr.dila.solonepg.ui.services.actions.EpgRechercheModeleFeuilleRouteActionService;
import fr.dila.solonepg.ui.th.bean.EpgFavorisRechercheListForm;
import fr.dila.solonepg.ui.th.bean.FavorisRechercheForm;
import fr.dila.ss.ui.bean.SSUsersList;
import fr.dila.ss.ui.bean.fdr.FeuilleRouteDTO;
import fr.dila.ss.ui.bean.fdr.ModeleFDRList;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.helper.ModeleFDRListHelper;
import fr.dila.ss.ui.th.bean.ModeleFDRListForm;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.contentview.PaginatedPageDocumentProvider;
import fr.dila.st.ui.contentview.UfnxqlPageDocumentProvider;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.services.STUIServiceLocator;
import fr.dila.st.ui.services.STUtilisateursUIService;
import fr.dila.st.ui.th.bean.UserForm;
import fr.dila.st.ui.th.bean.UserSearchForm;
import fr.dila.st.ui.th.bean.UsersListForm;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.constant.FeuilleRouteConstant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class FavorisRechercheUIServiceImpl implements FavorisRechercheUIService {

    @Override
    public EpgFavorisRechercheList getFavorisRechercheList(SpecificContext context) {
        EpgFavorisRechercheListForm favorisListForm = context.getFromContextData(FAVORIS_RECHERCHE_LIST_FORM);
        CoreSession session = context.getSession();

        PaginatedPageDocumentProvider provider = favorisListForm.getPageProvider(
            session,
            "pp_favoris_recherche",
            "d.",
            asList(session.getPrincipal().getName())
        );
        List<EpgFavorisRechercheDTO> dtos = provider.getCurrentPage().stream().map(this::toDto).collect(toList());

        EpgFavorisRechercheList list = new EpgFavorisRechercheList((int) provider.getResultsCount());
        list.setListeFavoris(dtos);
        return list;
    }

    @Override
    public EpgDossierList getDossiers(SpecificContext context) {
        DocumentModel favRechDoc = context.getCurrentDocument();
        Objects.requireNonNull(favRechDoc);
        FavorisRecherche favRech = favRechDoc.getAdapter(FavorisRecherche.class);
        String query = favRech.getQueryPart();
        context.putInContextData(EpgContextDataKey.JSON_QUERY, query);
        context.putInContextData(EpgContextDataKey.TYPE_RECHERCHE, favRech.getTypeEnum());
        return EpgUIServiceLocator.getEpgRechercheUIService().getResultsForJsonQuery(context);
    }

    private EpgFavorisRechercheDTO toDto(DocumentModel doc) {
        EpgFavorisRechercheDTO dto = docToBean(doc, EpgFavorisRechercheDTO.class);
        ofNullable(dto.getType())
            .map("favoris.recherche."::concat)
            .map(ResourceHelper::getString)
            .ifPresent(dto::setType);
        return dto;
    }

    @Override
    public SSUsersList getUsersList(SpecificContext context) {
        UsersListForm userForm = context.getFromContextData(USERS_LIST_FORM);

        FavorisRecherche favRech = context.getCurrentDocument().getAdapter(FavorisRecherche.class);
        String query = favRech.getQueryPart();

        UserRecherchePageDocumentProvider provider = userForm.getPageProvider(
            context.getSession(),
            "recherche_user_resultat",
            asList(query)
        );
        List<DocumentModel> currentPage = provider.getCurrentPage();
        STUtilisateursUIService userService = STUIServiceLocator.getSTUtilisateursUIService();
        List<UserForm> dtos = currentPage
            .stream()
            .map(d -> userService.mapDocToUserForm(d, context, true))
            .collect(toList());

        SSUsersList list = new SSUsersList();
        list.setListe(dtos);
        list.setNbTotal(provider.getResultsCount());
        return list;
    }

    @Override
    public ModeleFDRList getModelesFeuilleRoute(SpecificContext context) {
        ModeleFDRListForm fdrForm = context.getFromContextData(SSContextDataKey.LIST_MODELE_FDR);
        FavorisRechercheModeleFeuilleRouteUIService service = EpgUIServiceLocator.getRechercheModeleFeuilleRouteUIService();
        CoreSession session = context.getSession();

        UfnxqlPageDocumentProvider provider = fdrForm.getPageProvider(
            session,
            "recherche_modele_feuille_route_resultat",
            "r."
        );

        String searchQueryString = service.getSearchQueryString(context);
        List<Object> param = service.getSearchQueryParameter(context);
        Map<String, Serializable> props = provider.getProperties();
        props.put(UfnxqlPageDocumentProvider.QUERY_STRING_PROPERTY, searchQueryString);
        props.put(UfnxqlPageDocumentProvider.QUERY_PARAMETER_PROPERTY, (Serializable) param);
        props.put(UfnxqlPageDocumentProvider.TYPE_DOCUMENT, FeuilleRouteConstant.TYPE_FEUILLE_ROUTE);
        provider.setProperties(props);

        List<DocumentModel> fdrDocs = provider.getCurrentPage();
        EpgRechercheModeleFeuilleRouteActionService actionService = getEpgRechercheModeleFeuilleRouteActionService();
        List<FeuilleRouteDTO> dtos = fdrDocs
            .stream()
            .map(d -> actionService.getFeuilleRouteDTOFromDoc(d, session))
            .collect(toList());

        return ModeleFDRListHelper.buildModeleFDRList(dtos, fdrForm, (int) provider.getResultsCount());
    }

    @Override
    public void saveFavori(SpecificContext context) {
        FavorisRechercheType favorisType = context.getFromContextData(EpgContextDataKey.TYPE_RECHERCHE);
        if (favorisType == FavorisRechercheType.ES_LIBRE || favorisType == FavorisRechercheType.ES_EXPERTE) {
            saveFavoriDossier(context);
        } else if (favorisType == FavorisRechercheType.USER) {
            UserSearchForm userSearchForm = getUserSessionParameter(context, EpgUserSessionKey.USER_SEARCH_FORM);
            context.putInContextData(STContextDataKey.USER_SEARCH_FORM, userSearchForm);
            String query = STUIServiceLocator.getRechercheUtilisateursUIService().getUserRequeteur(context).getQuery();
            saveFavori(context, favorisType, query);
        } else if (favorisType == FavorisRechercheType.MODELE_FEUILLE_ROUTE) {
            EpgFavorisRechercheModeleFdrForm rechercheModeleFdrForm = getUserSessionParameter(
                context,
                EPG_FAVORIS_RECHERCHE_MODELE_FDR_FORM
            );
            context.putInContextData(EpgContextDataKey.FAVORIS_RECHERCHE_MODELE_FDR_FORM, rechercheModeleFdrForm);
            DocumentModel favDoc = getRechercheModeleFeuilleRouteUIService().mapFormToFavorisDoc(context);
            FavorisRechercheForm favorisRechercheForm = context.getFromContextData(FAVORIS_RECHERCHE_FORM);
            getEspaceRechercheService()
                .addToFavorisRecherche(context.getSession(), favorisRechercheForm.getPostes(), favDoc);
        }
    }

    @Override
    public void saveFavoriDossier(SpecificContext context) {
        String esQuery = EpgUIServiceLocator.getEpgRechercheUIService().getJsonQuery(context);
        FavorisRechercheType type = context.getFromContextData(EpgContextDataKey.TYPE_RECHERCHE);
        saveFavori(context, type, esQuery);
    }

    private void saveFavori(SpecificContext context, FavorisRechercheType favorisType, String query) {
        FavorisRechercheForm favorisRechercheForm = context.getFromContextData(FAVORIS_RECHERCHE_FORM);
        Objects.requireNonNull(favorisRechercheForm);
        String intitule = favorisRechercheForm.getIntitule();
        ArrayList<String> postes = favorisRechercheForm.getPostes();

        List<DocumentModel> usersNotUpdated = SolonEpgServiceLocator
            .getEspaceRechercheService()
            .addToFavorisRecherche(context.getSession(), postes, intitule, query, favorisType);

        if (usersNotUpdated.isEmpty()) {
            context.getMessageQueue().addToastSuccess(getString("recherche.favoris.ajouter.success"));
        } else {
            STUserService stUserService = STServiceLocator.getSTUserService();
            String fullnameUsersNotUpdated = usersNotUpdated
                .stream()
                .map(stUserService::getUserFullName)
                .collect(Collectors.joining(", "));
            context
                .getMessageQueue()
                .addWarnToQueue(getString("recherche.favoris.ajouter.limite.atteinte", fullnameUsersNotUpdated));
        }
    }
}
