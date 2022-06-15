package fr.dila.solonepg.ui.jaxrs.webobject.page.dossier.traitementpapier;

import fr.dila.solonepg.ui.bean.dossier.bordereau.EpgBordereauDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.reference.ComplementDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgSelectValueUIService;
import fr.dila.solonepg.ui.services.EpgTraitementPapierUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliDossierTraitementPapierReferences")
public class EpgDossierTraitementPapierReferences extends SolonWebObject {

    public EpgDossierTraitementPapierReferences() {
        super();
    }

    @GET
    public ThTemplate getReferences() {
        Map<String, Object> map = template.getData();

        EpgBordereauDTO bordereauDto = EpgUIServiceLocator.getBordereauUIService().getBordereau(context);
        template.getData().put("bordereauDto", bordereauDto);

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        map.put(EpgTemplateConstants.COMPLEMENT_DTO, epgTraitementPapierUIService.getComplementDTO(context));
        map.put(EpgTemplateConstants.IS_EDITABLE, epgTraitementPapierUIService.canEditTabs(context));

        EpgSelectValueUIService epgSelectValueUIService = SolonEpgUIServiceLocator.getEpgSelectValueUIService();
        map.put(EpgTemplateConstants.SIGNATAIRE_OPTION, epgSelectValueUIService.getTypeSignataireTraitementPapier());

        // visibilite bloc publication + Element Texte à publier dans complément
        map.put(
            EpgTemplateConstants.REFERENCES_PUBLICATION_VISIBLE,
            epgTraitementPapierUIService.isReferencesPublicationVisible(context)
        );
        // Elements Texte soumis à validation et Signataire dans complément
        map.put(
            EpgTemplateConstants.REFERENCES_COMPLEMENT_VISIBLE,
            epgTraitementPapierUIService.isReferencesComplementElementVisible(context)
        );

        return template;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveReferences(@FormParam("dossierId") String dossierId, @SwBeanParam ComplementDTO form) {
        context.setCurrentDocument(dossierId);
        context.putInContextData(EpgContextDataKey.TRAITEMENT_PAPIER_REFERENCE, form);

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        epgTraitementPapierUIService.saveComplementDTO(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/dossier/onglets/traitementpapier/onglet-references", context);
    }
}
