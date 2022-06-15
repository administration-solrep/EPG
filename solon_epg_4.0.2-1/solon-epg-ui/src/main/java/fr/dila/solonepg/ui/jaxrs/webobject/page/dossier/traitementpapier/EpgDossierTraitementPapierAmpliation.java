package fr.dila.solonepg.ui.jaxrs.webobject.page.dossier.traitementpapier;

import static fr.dila.solonepg.ui.enums.EpgContextDataKey.TRAITEMENT_PAPIER_AMPLIATION;
import static fr.dila.st.api.constant.MediaType.APPLICATION_PDF;

import com.google.common.collect.ImmutableList;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;
import fr.dila.solonepg.api.documentmodel.FileSolonEpg;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.ampliation.AmpliationDTO;
import fr.dila.solonepg.ui.jaxrs.webobject.ajax.EpgDossierAjax;
import fr.dila.solonepg.ui.services.EpgTraitementPapierUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.core.util.FileUtils;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.MailDTO;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.services.STUIServiceLocator;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.utils.FileDownloadUtils;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliDossierTraitementPapierAmpliation")
public class EpgDossierTraitementPapierAmpliation extends SolonWebObject {
    public static final String ERROR_IMPORT_MESSAGE = "organigramme.error.import";

    public EpgDossierTraitementPapierAmpliation() {
        super();
    }

    @GET
    public ThTemplate getAmpliation() {
        Map<String, Object> map = template.getData();

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();

        AmpliationDTO ampliationDTO = epgTraitementPapierUIService.getAmpliationDTO(context);
        map.put(EpgTemplateConstants.DTO, ampliationDTO);

        map.put(EpgTemplateConstants.IS_EDITABLE, epgTraitementPapierUIService.canEditTabs(context));
        map.put(STTemplateConstants.ACCEPTED_TYPE, ImmutableList.of(APPLICATION_PDF.extension()));

        map.put(EpgTemplateConstants.LST_HISTORIQUE, epgTraitementPapierUIService.getInfoAmpilation(context));

        return template;
    }

    @POST
    @Path("contenu")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public ThTemplate loadContent(
        @FormDataParam(EpgDossierAjax.DOSSIER_ID) String dossierId,
        @FormDataParam("ampliationFile_nom") String nomFichier
    )
        throws UnsupportedEncodingException {
        context.setCurrentDocument(dossierId);

        // Récupération et traitement du flux

        ThTemplate template = new AjaxLayoutThTemplate("fragments/components/mailModalContent", context);

        Map<String, Object> map = new HashMap<>();
        map.put(
            STTemplateConstants.LST_SENDER,
            STUIServiceLocator.getMailUIService().retrieveAdresseEmissionValues(context)
        );
        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        map.put(
            EpgTemplateConstants.DTO,
            new MailDTO(
                epgTraitementPapierUIService.getDefaultAmpliationMailObjet(context),
                epgTraitementPapierUIService.getDefaultAmpliationMailText(context),
                ImmutableList.of(FileUtils.generateCompletePdfFilename(URLDecoder.decode(nomFichier, "UTF-8").trim()))
            )
        );

        template.setData(map);

        return template;
    }

    @POST
    @Path("mail")
    public ThTemplate sendMailAndGetHistorique(@SwBeanParam AmpliationDTO ampliationDTO) {
        context.setCurrentDocument(ampliationDTO.getDossierId());
        context.putInContextData(TRAITEMENT_PAPIER_AMPLIATION, ampliationDTO);

        ThTemplate template = new AjaxLayoutThTemplate(
            "fragments/dossier/onglets/traitementpapier/ampliation/historique-ampliation",
            context
        );

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        epgTraitementPapierUIService.sendAmpliation(context);

        Map<String, Object> map = new HashMap<>();
        map.put(EpgTemplateConstants.LST_HISTORIQUE, epgTraitementPapierUIService.getInfoAmpilation(context));

        template.setData(map);

        return template;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAmpliation(@SwBeanParam AmpliationDTO ampliationDTO) {
        context.setCurrentDocument(ampliationDTO.getDossierId());
        context.putInContextData(TRAITEMENT_PAPIER_AMPLIATION, ampliationDTO);

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        epgTraitementPapierUIService.saveAmpliationDTO(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAmpliationFile(FormDataMultiPart multipart) {
        AmpliationDTO ampliationDTO = new AmpliationDTO(multipart);
        context.setCurrentDocument(ampliationDTO.getDossierId());

        context.putInContextData(TRAITEMENT_PAPIER_AMPLIATION, ampliationDTO);

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        epgTraitementPapierUIService.saveAmpliationFile(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @GET
    @Path("telecharger")
    public Response downloadAmpliationFile() {
        FileSolonEpg file = EpgUIServiceLocator.getEpgTraitementPapierUIService().getAmpliationFile(context);

        return FileDownloadUtils.getResponse(file.getFile(), file.getTitle(), file.getFileMimeType());
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/dossier/onglets/traitementpapier/onglet-ampliation", context);
    }
}
