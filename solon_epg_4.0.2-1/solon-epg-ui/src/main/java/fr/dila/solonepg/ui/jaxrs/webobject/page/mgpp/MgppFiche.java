package fr.dila.solonepg.ui.jaxrs.webobject.page.mgpp;

import fr.dila.solonepg.ui.bean.MgppDossierCommunicationConsultationFiche;
import fr.dila.solonepg.ui.enums.mgpp.MgppActionCategory;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppCreationFicheEnum;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.MgppTemplateConstants;
import fr.dila.solonepg.ui.th.model.mgpp.MgppDossierParlementaireTemplate;
import fr.dila.solonmgpp.api.domain.FileSolonMgpp;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.bean.Breadcrumb;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.FileDownloadUtils;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "MgppFiche")
public class MgppFiche extends SolonWebObject {

    public MgppFiche() {
        super();
    }

    @GET
    @Path("creation/{typeFiche}")
    public ThTemplate getCreerFiche(
        @PathParam("typeFiche") String typeFiche,
        @QueryParam(MgppTemplateConstants.ID_COMMUNICATION) String idCommunication
    ) {
        template.setName("pages/mgpp/creerFiche");
        String title = ResourceHelper.getString("mgpp.fiche.creation.title." + typeFiche);
        context.setNavigationContextTitle(
            new Breadcrumb(
                title,
                StringUtils.join(
                    "/mgpp/fiche/creation/",
                    typeFiche,
                    StringUtils.isNotEmpty(idCommunication) ? "?idCommunication=" + idCommunication : "",
                    "#main_content"
                ),
                Breadcrumb.SUBTITLE_ORDER + 2,
                context.getWebcontext().getRequest()
            )
        );
        template.setContext(context);

        context.putInContextData(MgppContextDataKey.COMMUNICATION_ID, idCommunication);
        MgppCreationFicheEnum creationFicheEnum = MgppCreationFicheEnum.getByFicheType(typeFiche);
        creationFicheEnum.initFiche(context);

        context.putInContextData(MgppContextDataKey.IS_FICHE_VERROUILLEE, true);
        MgppDossierCommunicationConsultationFiche fiche = MgppUIServiceLocator
            .getMgppFicheUIService()
            .getFicheRemplie(context);
        creationFicheEnum.changeWidgetType(fiche);

        Map<String, Object> map = new HashMap<>();
        if (context.getNavigationContext().size() > 1) {
            map.put(STTemplateConstants.URL_PREVIOUS_PAGE, context.getUrlPreviousPage());
        } else {
            context.removeNavigationContextTitle();
            map.put(STTemplateConstants.URL_PREVIOUS_PAGE, "");
        }
        map.put(STTemplateConstants.INTITULE, title);
        map.put(MgppTemplateConstants.TYPE_FICHE, typeFiche);
        map.put(MgppTemplateConstants.ID_COMMUNICATION_A_TRAITER, idCommunication);
        map.put(MgppTemplateConstants.LST_WIDGETS_PRESENTATION, fiche.getLstWidgetsPresentation());
        map.put(MgppTemplateConstants.LST_TABLES_REPRESENTANTS, fiche.getLstTablesRepresentants());
        map.put(STTemplateConstants.MAIN_ACTIONS, context.getActions(MgppActionCategory.MAIN_CREATE_DOSSIER_MGPP));
        map.put(STTemplateConstants.BASE_ACTIONS, context.getActions(MgppActionCategory.BASE_CREATE_DOSSIER_MGPP));
        template.setData(map);

        return template;
    }

    @GET
    @Path("telecharger")
    public Response downloadFile(@QueryParam("fileId") @SwNotEmpty @SwId String fileId) {
        DocumentModel docModel = context.getSession().getDocument(new IdRef(fileId));
        FileSolonMgpp fileSolonMgpp = docModel.getAdapter(FileSolonMgpp.class);
        File file = fileSolonMgpp.getContent().getFile();
        return FileDownloadUtils.getResponse(file, fileSolonMgpp.getFilename(), fileSolonMgpp.getMimeType());
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new MgppDossierParlementaireTemplate();
    }
}
