package fr.dila.solonepg.core.util;

import com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.api.recherche.EpgDossierListingDTO;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.io.Serializable;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.query.api.PageProviderService;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryAndFetchPageProvider;

public final class DossierPageProviderHelper {

    private DossierPageProviderHelper() {
        // Private default constructor
    }

    public static PageProvider<EpgDossierListingDTO> getPageProvider(
        String providerName,
        List<SortInfo> sortInfos,
        long pageSize,
        long currentPage,
        CoreSession session
    ) {
        return getPageProvider(providerName, sortInfos, pageSize, currentPage, session, null);
    }

    public static PageProvider<EpgDossierListingDTO> getPageProvider(
        String providerName,
        List<SortInfo> sortInfos,
        long pageSize,
        long currentPage,
        CoreSession session,
        Object[] parameters
    ) {
        PageProviderService pageProviderService = ServiceUtil.getService(PageProviderService.class);
        return (PageProvider<EpgDossierListingDTO>) pageProviderService.getPageProvider(
            providerName,
            sortInfos,
            pageSize,
            currentPage,
            ImmutableMap.of(CoreQueryAndFetchPageProvider.CORE_SESSION_PROPERTY, (Serializable) session),
            parameters
        );
    }
}
