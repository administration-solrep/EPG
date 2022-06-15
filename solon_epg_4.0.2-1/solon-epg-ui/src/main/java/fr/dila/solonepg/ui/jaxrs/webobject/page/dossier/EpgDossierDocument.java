package fr.dila.solonepg.ui.jaxrs.webobject.page.dossier;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFile;
import fr.dila.solonepg.ui.enums.EpgActionEnum;
import fr.dila.solonepg.ui.enums.SourceFichierEnum;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.ss.api.tree.SSTreeFile;
import fr.dila.ss.ui.bean.FondDTO;
import fr.dila.ss.ui.jaxrs.weboject.model.WebObjectExportModel;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.FileDownloadUtils;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.webengine.model.WebObject;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.ui.bean.CopyFileDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;


@WebObject(type = "AppliDossierDocument")
public class EpgDossierDocument extends SolonWebObject implements WebObjectExportModel {

    public EpgDossierDocument() {
        super();
    }

    @GET
    public ThTemplate getDocument() {
        if (template.getData() == null) {
            Map<String, Object> map = new HashMap<>();
            template.setData(map);
        }

        Map<String, Object> data = template.getData();
        String dossierId = context.getCurrentDocument().getId();
        CopyFileDTO dto = UserSessionHelper.getUserSessionParameter(
                context,
                SolonEpgConstant.CUT_FILE_SESSION_KEY,
                CopyFileDTO.class
        );
        context.putInContextData(EpgContextDataKey.IS_FILE_CUT, dto != null && dossierId.equals(dto.getDossierId()));

        FondDTO parapheurDto = EpgUIServiceLocator.getEpgParapheurUIService().getParapheurDTO(context);
        data.put(EpgTemplateConstants.PARAPHEUR_DTO, parapheurDto);
        FondDTO fondDto = EpgUIServiceLocator.getEpgFondDeDossierUIService().getFondDTO(context);
        data.put(EpgTemplateConstants.FOND_DTO, fondDto);
        data.put(STTemplateConstants.LST_COLONNES, fondDto.getLstColonnes());
        data.put(EpgTemplateConstants.REP_DOSSIER_ID, dossierId);
        data.put(EpgTemplateConstants.SOURCE_PARAPHEUR, SourceFichierEnum.PARAPHEUR);
        data.put(EpgTemplateConstants.SOURCE_FOND_DOSSIER, SourceFichierEnum.FOND_DOSSIER);
        data.put(EpgTemplateConstants.EDIT_ACTION_FDD, context.getAction(EpgActionEnum.EDITE_DOCUMENT_FDD));
        data.put(EpgTemplateConstants.EDIT_ACTION_PARAPHEUR, context.getAction(EpgActionEnum.EDITE_DOCUMENT_PARAPHEUR));

        return template;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveFondDeDossier() {
        return new JsonResponse(SolonStatus.OK, context.getMessageQueue(), null).build();
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/dossier/onglets/document", getMyContext());
    }

    @GET
    @Path("download")
    public Response downloadFile(@QueryParam("fileId") String fileId) {
        DocumentModel docModel = context.getSession().getDocument(new IdRef(fileId));
        SSTreeFile ssTreeFile = docModel.getAdapter(FondDeDossierFile.class);
        File file = ssTreeFile.getContent().getFile();
        return FileDownloadUtils.getResponse(file, ssTreeFile.getFilename(), ssTreeFile.getFileMimeType());
    }

    @Path("fond/export")
    @GET
    public Response exportFondDossier() {
        StreamingOutput outputStream = getOutputStream(
            context,
            EpgUIServiceLocator.getEpgArchiveUIService()::exportFdd
        );

        DocumentModel dossierDoc = context.getCurrentDocument();
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        final String zipFilename = dossier.getNumeroNor() + "-fond_dossier.zip";

        return FileDownloadUtils.getZipResponse(outputStream, zipFilename);
    }

    @Path("parapheur/export")
    @GET
    public Response exportParapheur() {
        StreamingOutput outputStream = getOutputStream(
            context,
            EpgUIServiceLocator.getEpgArchiveUIService()::exportParapheur
        );

        DocumentModel dossierDoc = context.getCurrentDocument();
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        final String zipFilename = dossier.getNumeroNor() + "-parapheur.zip";

        return FileDownloadUtils.getZipResponse(outputStream, zipFilename);
    }
}
