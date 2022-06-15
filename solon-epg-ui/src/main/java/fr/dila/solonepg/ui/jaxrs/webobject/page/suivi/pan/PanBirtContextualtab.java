package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan;

import fr.dila.solonepg.ui.bean.pan.PanOnglet;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanBirtContextualtab")
public class PanBirtContextualtab extends PanAbstractSousOngletContextuel {

    public PanBirtContextualtab() {
        super();
    }

    @Override
    public void loadSubtabContent() {
        String ministereId = context.getFromContextData(PanContextDataKey.MINISTERE_PILOTE_ID);
        if (ministereId != null) {
            template.getData().put(PanTemplateConstants.MINISTERE_PILOTE, ministereId);
            template
                .getData()
                .put(
                    PanTemplateConstants.MINISTERE_PILOTE_LABEL,
                    STServiceLocator.getSTMinisteresService().getEntiteNode(ministereId).getLabelWithNor(ministereId)
                );
        }
        PanOnglet actif = context.getFromContextData(PanContextDataKey.ONGLET_ACTIF);
        PanStatistiques.loadBirtContext(actif, context, template.getData());
    }

    @Override
    public ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/pan/panBirtSubtab", getMyContext());
    }

    @Override
    public String getMyTemplateName() {
        return "fragments/pan/panBirtSubtab";
    }
}
