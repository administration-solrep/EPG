package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.traites;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreTraiteDTOImpl;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan.PanAbstractTextemaitre;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.PanTemplateConstants;
import fr.dila.solonepg.ui.th.model.bean.pan.TexteMaitreTraiteForm;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.th.model.SpecificContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "PanTraitesTextemaitre")
public class PanTraitesTextemaitre extends PanAbstractTextemaitre {

    @Override
    public void loadSpecificTexteMaitreContent() {
        ActiviteNormative activiteNormative = context.getCurrentDocument().getAdapter(ActiviteNormative.class);
        TexteMaitreTraiteDTOImpl texteMaitreDTO = new TexteMaitreTraiteDTOImpl(activiteNormative, context.getSession());
        template.getData().put(PanTemplateConstants.TEXTE_MAITRE, texteMaitreDTO);
    }

    @Override
    protected AbstractMapDTO getTexteMaitreDto(SpecificContext context) {
        ActiviteNormative activiteNormative = context.getCurrentDocument().getAdapter(ActiviteNormative.class);
        return new TexteMaitreTraiteDTOImpl(activiteNormative, context.getSession());
    }

    @Path("sauvegarder")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response sauvegarderTexteMaitre(@SwBeanParam TexteMaitreTraiteForm texteMaitreFormDTO) {
        context.putInContextData(PanContextDataKey.TEXTE_MAITRE_FORM, texteMaitreFormDTO);
        return sauvegarderTexteMaitre();
    }

    @Override
    protected void saveTexteMaitre(SpecificContext context) {
        PanUIServiceLocator.getTexteMaitreTraitesUIService().saveTexteMaître(context);
    }

    @Override
    public String getSecondTableauAjaxWebObject() {
        return null;
    }

    public PanTraitesTextemaitre() {
        super();
    }
}
