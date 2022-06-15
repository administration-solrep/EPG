package fr.dila.solonepg.ui.jaxrs.webobject.page.dossier.traitementpapier;

import fr.dila.solonepg.ui.bean.dossier.traitementpapier.bordereau.BordereauCourrierForm;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.EpgTraitementPapierUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.FileDownloadUtils;
import java.io.File;
import java.util.List;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliDossierTraitementPapierBordereau")
public class EpgDossierTraitementPapierBordereauxCourriers extends SolonWebObject {

    @GET
    public ThTemplate getBordereauxCourriers() {
        Map<String, Object> map = template.getData();

        context.putInContextData(MgppContextDataKey.EVENT_TYPE, "TP");
        List<SelectValueDTO> courrierSelect = MgppUIServiceLocator
            .getModeleCourrierUIService()
            .getAvailableModels(context);
        map.put(EpgTemplateConstants.COURRIER_SELECT, courrierSelect);
        map.put(STTemplateConstants.CURRENT_ID, context.getCurrentDocument().getId());

        map.put(EpgTemplateConstants.FORM, EpgUIServiceLocator.getBordereauUIService().getBordereauEtCourrier(context));

        map.put(
            EpgTemplateConstants.LST_MIN,
            MgppUIServiceLocator.getMgppTableReferenceUIService().getCurrentMinisteres(context)
        );

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        map.put(EpgTemplateConstants.IS_EDITABLE, epgTraitementPapierUIService.canEditTabs(context));

        return template;
    }

    @GET
    @Produces("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
    @Path("telecharger")
    public Response genererCourrier(@SwBeanParam BordereauCourrierForm form) {
        context.setCurrentDocument(form.getDossierId());
        context.putInContextData(EpgContextDataKey.DOSSIER_AUTEUR, form.getAuteur());
        context.putInContextData(EpgContextDataKey.DOSSIER_COAUTEUR, form.getCoAuteur());
        context.putInContextData(MgppContextDataKey.MODELE_TEMPLATE_NAME, form.getModeleCourrier());

        File fichierCourrier = MgppUIServiceLocator
            .getModeleCourrierUIService()
            .buildModeleCourrierForTraitementPapier(context);

        return FileDownloadUtils.getAttachmentDocx(fichierCourrier, "courrier.docx");
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate(
            "fragments/dossier/onglets/traitementpapier/onglet-bordereaux-courriers",
            context
        );
    }
}
