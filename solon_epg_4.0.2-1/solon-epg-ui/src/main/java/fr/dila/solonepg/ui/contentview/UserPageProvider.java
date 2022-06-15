package fr.dila.solonepg.ui.contentview;

import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.solonepg.core.recherche.EpgUserListingDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.user.UserOrderComparator;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.contentview.AbstractDTOPageProvider;
import fr.sword.naiad.nuxeo.ufnxql.core.query.FlexibleQueryMaker;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.api.PageProviderDefinition;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;
import org.nuxeo.ecm.platform.usermanager.UserManager;

public class UserPageProvider extends AbstractDTOPageProvider {
    private static final long serialVersionUID = -3516175029492765213L;

    private static final STLogger LOGGER = STLogFactory.getLog(UserPageProvider.class);

    public UserPageProvider() {}

    @Override
    protected void fillCurrentPageMapList(CoreSession session) {
        checkQueryCache();
        if (currentItems == null) {
            error = null;
            errorMessage = null;

            if (query == null) {
                buildQuery();
            }
            if (query == null) {
                throw new NuxeoException(String.format("Cannot perform null query: check provider '%s'", getName()));
            }
            currentItems = new ArrayList<>();

            try (IterableQueryResult res = QueryUtils.doUFNXQLQuery(session, query, null)) {
                final UserManager userManager = STServiceLocator.getUserManager();
                List<STUser> users = new ArrayList<>();
                Set<String> userToRemove = new HashSet<>();

                StreamSupport
                    .stream(res.spliterator(), false)
                    .map(row -> (String) row.get("id"))
                    .forEach(id -> addOrRemoveUser(userManager, id, users, userToRemove));

                if (CollectionUtils.isNotEmpty(sortInfos) && isSortable()) {
                    for (SortInfo sortinfo : sortInfos) {
                        users.sort(new UserOrderComparator(sortinfo));
                    }
                }

                if (CollectionUtils.isNotEmpty(userToRemove)) {
                    //Suppression des users des derniers resultats consultés et des favoris de consultation du user courant
                    EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
                    String userworkspacePath = STServiceLocator
                        .getUserWorkspaceService()
                        .getCurrentUserPersonalWorkspace(session)
                        .getPathAsString();
                    espaceRechercheService.removeUserFromDerniersResultatsConsultes(
                        session,
                        userworkspacePath,
                        userToRemove
                    );
                    espaceRechercheService.removeUserFromFavorisConsultations(session, userworkspacePath, userToRemove);
                    session.getPrincipal().getName();
                }

                resultsCount = QueryUtils.doCountQuery(session, FlexibleQueryMaker.KeyCode.UFXNQL.getKey() + query);

                long pagesize = getPageSize();
                if (pagesize < 1) {
                    pagesize = maxPageSize;
                }
                int index = 0;
                while (offset + index < users.size() && index < pagesize) {
                    EpgUserListingDTO epgUserListingDTO = new EpgUserListingDTO();
                    STUser u = users.get((int) (offset + index));
                    epgUserListingDTO.setNom(u.getLastName());
                    epgUserListingDTO.setPrenom(u.getFirstName());
                    epgUserListingDTO.setUtilisateur(u.getUsername());
                    epgUserListingDTO.setMel(u.getEmail());
                    epgUserListingDTO.setDateDebut(SolonDateConverter.DATE_SLASH.format(u.getDateDebut()));
                    currentItems.add(epgUserListingDTO);
                    index++;
                }
            } catch (Exception e) {
                error = e;
                errorMessage = e.getMessage();
                LOGGER.warn(STLogEnumImpl.DEFAULT, e.getMessage());
            }
        }
    }

    private static void addOrRemoveUser(
        UserManager userManager,
        String id,
        List<STUser> users,
        Set<String> userToRemove
    ) {
        DocumentModel userModel = userManager.getUserModel(id);
        if (userModel != null) {
            STUser user = userModel.getAdapter(STUser.class);
            if (CollectionUtils.isEmpty(user.getPostes()) || user.isDeleted()) {
                userToRemove.add(userModel.getId());
            } else {
                users.add(user);
            }
        } else {
            userToRemove.add(id);
        }
    }

    @Override
    protected void buildQuery() {
        try {
            String newQuery;
            PageProviderDefinition def = getDefinition();
            if (def.getWhereClause() == null) {
                newQuery =
                    NXQLQueryBuilder.getQuery(
                        def.getPattern(),
                        getParameters(),
                        def.getQuotePatternParameters(),
                        def.getEscapePatternParameters(),
                        this.searchDocumentModel
                    );
            } else {
                DocumentModel searchDocumentModel = getSearchDocumentModel();
                if (searchDocumentModel == null) {
                    throw new NuxeoException(
                        String.format(
                            "Cannot build query of provider '%s': " + "no search document model is set",
                            getName()
                        )
                    );
                }
                newQuery = NXQLQueryBuilder.getQuery(searchDocumentModel, def.getWhereClause(), getParameters());
            }

            if (!newQuery.equals(query)) {
                // query has changed => refresh
                refresh();
                query = newQuery;
            }
        } catch (NuxeoException e) {
            throw new NuxeoException(e);
        }
    }

    @Override
    public void setSearchDocumentModel(DocumentModel searchDocumentModel) {
        if (this.searchDocumentModel != searchDocumentModel) {
            this.searchDocumentModel = searchDocumentModel;
            refresh();
        }
    }
}
