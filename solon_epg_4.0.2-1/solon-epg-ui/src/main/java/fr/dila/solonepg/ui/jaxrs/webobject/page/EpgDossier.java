package fr.dila.solonepg.ui.jaxrs.webobject.page;

import static fr.dila.ss.ui.jaxrs.webobject.page.AbstractSSDossier.DOSSIER_WEBOBJECT;
import static org.apache.commons.lang3.StringUtils.defaultString;

import fr.dila.solonepg.ui.bean.EpgConsultDossierDTO;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgURLConstants;
import fr.dila.solonepg.ui.th.constants.MgppTemplateConstants;
import fr.dila.solonepg.ui.th.model.EpgLayoutThTemplate;
import fr.dila.ss.ui.jaxrs.webobject.page.AbstractSSDossier;
import fr.dila.ss.ui.services.SSUIServiceLocator;
import fr.dila.ss.ui.th.constants.SSTemplateConstants;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.bean.OngletConteneur;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.FileDownloadUtils;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = DOSSIER_WEBOBJECT)
public class EpgDossier extends AbstractSSDossier {

    public EpgDossier() {
        super("document"); // onglet par défaut
    }

    @Override
    public void setSpecificDataForGetDossier(ThTemplate template, String id, String tab) {
        Map<String, Object> map = template.getData();

        EpgConsultDossierDTO dossier = (EpgConsultDossierDTO) template.getData().get(SSTemplateConstants.MON_DOSSIER);
        if (dossier != null) {
            context.putInContextData(EpgContextDataKey.DOSSIER_NOR, dossier.getNumeroNor());
            context.setNavigationContextTitle(
                new Breadcrumb(
                    String.format("%s (%s)", defaultString(dossier.getTitreActe()), dossier.getNumeroNor()),
                    "/dossier/" + id + "/" + tab,
                    Breadcrumb.SUBTITLE_ORDER,
                    context.getWebcontext().getRequest()
                )
            );
        } else {
            context.setNavigationContextTitle(
                new Breadcrumb(
                    "Consultation d'un dossier inconnu",
                    String.format("/dossier/%s/%s", id, tab),
                    Breadcrumb.SUBTITLE_ORDER,
                    context.getWebcontext().getRequest()
                )
            );
        }
        if (context.getNavigationContext().size() > 1) {
            map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        } else {
            map.put(STTemplateConstants.URL_PREVIOUS_PAGE, "");
        }

        EpgUIServiceLocator.getEpgDossierUIService().loadDossierActions(context, template);

        // On passe le dossier à l'état lu
        SSUIServiceLocator.getSSDossierDistributionUIService().changeReadStateDossierLink(context);
    }

    @GET
    @Path("editerPdf/{id}")
    @Produces("application/pdf")
    public Response afficherPdfNewTab(@PathParam("id") String id) throws IOException {
        context.setCurrentDocument(id);
        File file = EpgUIServiceLocator.getEpgDossierUIService().generateDossierPdf(context);
        return FileDownloadUtils.getAttachmentPdf(file, file.getName());
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new EpgLayoutThTemplate();
    }

    @Path("{id}/saisine/rectificative")
    public Object getSaisineRectificative(@PathParam("id") String id) {
        context.setCurrentDocument(id);
        EpgUIServiceLocator.getEpgDossierUIService().initDossierActionContext(context);
        verifyAction(
            EpgActionEnum.SAISINE_RECTIFICATIVE,
            String.format(EpgURLConstants.URL_DOSSIER_SAISINE_RECTIFICATIVE, id)
        );
        return newObject("DossierSaisineRectificative", context);
    }

    @Override
    protected <T extends ThTemplate> void buildTemplateData(T template, String tab, String idMessage) {
        super.buildTemplateData(template, tab);
        Map<String, Object> map = template.getData();
        if (idMessage != null) {
            map.put(
                STTemplateConstants.MY_TABS,
                OngletConteneur.actionsToTabs(context, MgppActionCategory.ONGLET_DOSSIER_MGPP, tab)
            );
            map.put(MgppTemplateConstants.ID_MESSAGE, idMessage);
        }
    }
}
