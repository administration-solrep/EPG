package fr.dila.solonepg.ui.jaxrs.webobject.page.dossier;

import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.ss.ui.jaxrs.webobject.page.dossier.SSDossierFdr;
import fr.dila.st.ui.bean.SelectValueDTO;
import java.util.List;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliDossierFdr")
public class EpgDossierFdr extends SSDossierFdr {

    public EpgDossierFdr() {
        super();
    }

    @Override
    public List<SelectValueDTO> getTypeEtapeAjout() {
        return EpgUIServiceLocator
            .getEpgSelectValueUIService()
            .getRoutingTaskTypesFilteredByIdFdr(context.getSession(), context.getCurrentDocument().getId());
    }
}
