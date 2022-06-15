package fr.dila.solonepg.ui.services.impl;

import static java.util.Arrays.asList;

import fr.dila.solonepg.api.administration.ParametrageApplication;
import fr.dila.solonepg.core.recherche.EpgUserListingDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgUserList;
import fr.dila.solonepg.ui.contentview.DerniersUsersConsultesPageProvider;
import fr.dila.solonepg.ui.contentview.UserPageProvider;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.helper.SolonEpgProviderHelper;
import fr.dila.solonepg.ui.services.EpgUserListUIService;
import fr.dila.solonepg.ui.th.bean.EpgUserListForm;
import fr.dila.st.core.helper.PaginationHelper;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.th.bean.PaginationForm;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.nuxeo.ecm.core.api.CoreSession;

public class EpgUserListUIServiceImpl implements EpgUserListUIService {

    @Override
    public EpgUserList getFavorisConsultationUsers(SpecificContext context) {
        EpgUserListForm form = context.computeFromContextDataIfAbsent(
            EpgContextDataKey.USER_LIST_FORM,
            k -> new EpgUserListForm()
        );
        CoreSession session = context.getSession();
        UserPageProvider provider = form.getPageProvider(
            session,
            "user_page",
            asList(session.getPrincipal().getName())
        );

        // récupération du paramètre du nombre d'élément a afficher
        final ParametrageApplication param = SolonEpgServiceLocator
            .getParametreApplicationService()
            .getParametreApplicationDocument(session);
        provider.setPageSize(
            param != null && param.getNbFavorisConsultation() != null ? param.getNbFavorisConsultation().intValue() : 30
        );

        List<Map<String, Serializable>> docList = provider.getCurrentPage();
        EpgUserList lstResults = new EpgUserList(true);

        // On fait le mapping des documents vers notre DTO
        lstResults
            .getListe()
            .addAll(
                docList
                    .stream()
                    .filter(EpgUserListingDTO.class::isInstance)
                    .map(EpgUserListingDTO.class::cast)
                    .collect(Collectors.toList())
            );
        return lstResults;
    }

    @Override
    public EpgUserList getDernierUserConsulte(SpecificContext context) {
        PaginationForm form = context.getFromContextData(STContextDataKey.PAGINATION_FORM);
        String username = context.getSession().getPrincipal().getName();
        DerniersUsersConsultesPageProvider provider = form.getPageProvider(
            context.getSession(),
            "user_page_consult",
            asList(username)
        );
        EpgUserList userList = SolonEpgProviderHelper.buildUserList(provider);
        form.setPage(PaginationHelper.getPageFromCurrentIndex(provider.getCurrentPageIndex()));

        return userList;
    }
}
