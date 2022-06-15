package fr.dila.solonepg.ui.jaxrs.webobject.page.dossier.traitementpapier;

import fr.dila.solonepg.ui.bean.dossier.traitementpapier.epreuve.EpreuveDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgTraitementPapierUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.Map;
import java.util.function.Consumer;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliDossierTraitementPapierEpreuves")
public class EpgDossierTraitementPapierEpreuves extends SolonWebObject {

    public EpgDossierTraitementPapierEpreuves() {
        super();
    }

    @GET
    public ThTemplate getEpreuves() {
        Map<String, Object> map = template.getData();
        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        EpreuveDTO epreuveDTO = epgTraitementPapierUIService.getEpreuveDTO(context);

        injectSignataire(
            epgTraitementPapierUIService,
            map,
            epreuveDTO.getNomSignataire(),
            epreuveDTO::setSignataire,
            EpgTemplateConstants.IS_SIGNATAIRE_PARAMETRE
        );
        injectSignataire(
            epgTraitementPapierUIService,
            map,
            epreuveDTO.getNouveauNomSignataire(),
            epreuveDTO::setNouveauSignataire,
            EpgTemplateConstants.IS_NOUVEAU_SIGNATAIRE_PARAMETRE
        );

        map.put(EpgTemplateConstants.EPREUVE_DTO, epreuveDTO);
        map.put(EpgTemplateConstants.IS_EDITABLE, epgTraitementPapierUIService.canEditTabs(context));
        map.put(EpgTemplateConstants.SIGNATAIRES, epgTraitementPapierUIService.getReferencesSignataires(context));

        return template;
    }

    private void injectSignataire(
        EpgTraitementPapierUIService epgTraitementPapierUIService,
        Map<String, Object> map,
        String signataire,
        Consumer<SelectValueDTO> signataireConsumer,
        String isSignataireparametre
    ) {
        if (StringUtils.isNotEmpty(signataire)) {
            context.putInContextData(STContextDataKey.USER_ID, signataire);
            SelectValueDTO signataireValueDTO = epgTraitementPapierUIService.usrIdToSelectValueDTO(context);
            signataireConsumer.accept(signataireValueDTO);
            map.put(
                isSignataireparametre,
                EpgDossierTraitementPapierCommunication.isSignataireParametre(context, signataireValueDTO)
            );
        } else {
            signataireConsumer.accept(new SelectValueDTO());
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveEpreuves(
        @SwId @SwRequired @FormParam("dossierId") String dossierId,
        @SwBeanParam EpreuveDTO epreuveDTO
    ) {
        context.setCurrentDocument(dossierId);
        context.putInContextData(EpgContextDataKey.TRAITEMENT_PAPIER_EPREUVE, epreuveDTO);

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        epgTraitementPapierUIService.saveEpreuveDTO(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @POST
    @Path("actualiser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualiserDestinataire(
        @SwId @SwRequired @FormParam("dossierId") String dossierId,
        @SwBeanParam EpreuveDTO epreuveDTO
    ) {
        context.putInContextData(EpgContextDataKey.TRAITEMENT_PAPIER_EPREUVE, epreuveDTO);
        context.setCurrentDocument(dossierId);

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        String destinataire = epgTraitementPapierUIService.getEpreuveDestinataire(context);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue(), destinataire).build();
    }

    @POST
    @Path("editer")
    @Produces("application/vnd.ms-word")
    public Response editerBordereauEnvoiRelecture(
        @SwId @SwRequired @FormParam("dossierId") String dossierId,
        @SwBeanParam EpreuveDTO epreuveDTO
    ) {
        context.setCurrentDocument(dossierId);

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        if (epreuveDTO.isNouvelleEpreuve()) {
            return epgTraitementPapierUIService.editerNouvelleEpreuve(context);
        } else {
            return epgTraitementPapierUIService.editerDemandeEpreuve(context);
        }
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/dossier/onglets/traitementpapier/onglet-epreuves", context);
    }
}
