package fr.dila.solonepg.ui.jaxrs.webobject.page.dossier.traitementpapier;

import fr.dila.solonepg.ui.bean.dossier.traitementpapier.publication.PublicationDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgSelectValueUIService;
import fr.dila.solonepg.ui.services.EpgTraitementPapierUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliDossierTraitementPapierPublication")
public class EpgDossierTraitementPapierPublication extends SolonWebObject {

    public EpgDossierTraitementPapierPublication() {
        super();
    }

    @GET
    public ThTemplate getPublication() {
        Map<String, Object> map = template.getData();

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        PublicationDTO publicationDTO = epgTraitementPapierUIService.getPublicationDTO(context);
        map.put(EpgTemplateConstants.DTO, publicationDTO);

        EpgSelectValueUIService epgSelectValueUIService = EpgUIServiceLocator.getEpgSelectValueUIService();
        map.put(EpgTemplateConstants.LST_VECTEUR_PUBLICATION, epgSelectValueUIService.getActifVecteurPublication());
        map.put(EpgTemplateConstants.LST_MODE_PARUTION, epgSelectValueUIService.getActifModeParution());
        map.put(EpgTemplateConstants.LST_DELAI_PUBLICATION, epgSelectValueUIService.getDelaiPublicationFiltered());
        map.put(EpgTemplateConstants.IS_EDITABLE, epgTraitementPapierUIService.canEditTabs(context));

        return template;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response savePublication(
        @SwId @SwRequired @FormParam("dossierId") String dossierId,
        @SwBeanParam PublicationDTO publicationDTO
    ) {
        context.setCurrentDocument(dossierId);
        context.putInContextData(EpgContextDataKey.TRAITEMENT_PAPIER_PUBLICATION, publicationDTO);

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        epgTraitementPapierUIService.savePublicationDTO(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/dossier/onglets/traitementpapier/onglet-publication", context);
    }
}
