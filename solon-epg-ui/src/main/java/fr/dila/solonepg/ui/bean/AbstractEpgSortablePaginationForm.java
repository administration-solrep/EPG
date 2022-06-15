package fr.dila.solonepg.ui.bean;

import static fr.dila.st.core.util.ObjectHelper.requireNonNullElseGet;

import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.contentview.DossierPageProvider;
import fr.dila.st.ui.th.bean.AbstractSortablePaginationForm;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.query.api.PageProvider;

public abstract class AbstractEpgSortablePaginationForm extends AbstractSortablePaginationForm {

    public AbstractEpgSortablePaginationForm() {
        super();
    }

    @Override
    public <T extends PageProvider<?>> T getPageProvider(
        CoreSession session,
        String pageProviderName,
        String prefix,
        List<Object> lstParams
    ) {
        T provider = super.getPageProvider(session, pageProviderName, prefix, lstParams);
        if (provider instanceof DossierPageProvider) {
            ProfilUtilisateurService profilUtilisateurService = SolonEpgServiceLocator.getProfilUtilisateurService();
            Collection<String> columns = profilUtilisateurService.getUserEspaceTraitementColumn(session);
            columns = new HashSet<>(requireNonNullElseGet(columns, ArrayList::new));
            Map<String, Serializable> props = provider.getProperties();
            props.put(DossierPageProvider.USER_COLUMN_PROPERTY, (Serializable) columns);
            provider.setProperties(props);
        }
        return provider;
    }
}
