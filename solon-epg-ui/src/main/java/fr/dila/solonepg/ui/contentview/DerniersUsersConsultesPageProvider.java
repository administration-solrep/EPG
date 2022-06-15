package fr.dila.solonepg.ui.contentview;

import fr.dila.solonepg.api.recherche.ResultatConsulte;
import fr.dila.solonepg.core.recherche.EpgUserListingDTO;
import fr.dila.solonepg.ui.helper.SolonEpgProviderHelper;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.helper.PaginationHelper;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.contentview.AbstractDTOPageProvider;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.platform.usermanager.UserManager;

public class DerniersUsersConsultesPageProvider extends AbstractDTOPageProvider {
    private static final long serialVersionUID = 7130198498642813718L;

    @Override
    protected void fillCurrentPageMapList(CoreSession coreSession) {
        currentItems = new ArrayList<>();
        resultsCount = 0L;

        DocumentModelList resultatsConsultesDocs = QueryUtils.doQuery(coreSession, query, 1, 0);
        if (CollectionUtils.isNotEmpty(resultatsConsultesDocs)) {
            List<String> userIds = resultatsConsultesDocs.get(0).getAdapter(ResultatConsulte.class).getUsers();
            if (CollectionUtils.isNotEmpty(userIds)) {
                resultsCount = userIds.size();

                // récupération page courante
                offset = PaginationHelper.calculeOffSet(offset, getPageSize(), resultsCount);

                List<String> ids = SolonEpgProviderHelper.paginateList(userIds, offset, getPageSize(), resultsCount);
                final UserManager userManager = STServiceLocator.getUserManager();
                currentItems =
                    ids
                        .stream()
                        .map(userManager::getUserModel)
                        .filter(Objects::nonNull)
                        .map(doc -> doc.getAdapter(STUser.class))
                        .filter(user -> !user.isDeleted())
                        .map(DerniersUsersConsultesPageProvider::mapUserToUserDTO)
                        .collect(Collectors.toList());
            }
        }
    }

    private static EpgUserListingDTO mapUserToUserDTO(STUser user) {
        EpgUserListingDTO epgUserListingDTO = new EpgUserListingDTO();
        epgUserListingDTO.setNom(user.getLastName());
        epgUserListingDTO.setPrenom(user.getFirstName());
        epgUserListingDTO.setUtilisateur(user.getUsername());
        epgUserListingDTO.setMel(user.getEmail());
        epgUserListingDTO.setDateDebut(SolonDateConverter.DATE_SLASH.format(user.getDateDebut()));
        return epgUserListingDTO;
    }
}
