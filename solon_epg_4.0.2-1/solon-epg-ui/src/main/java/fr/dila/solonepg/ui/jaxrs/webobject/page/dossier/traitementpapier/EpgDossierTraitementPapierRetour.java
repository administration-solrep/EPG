package fr.dila.solonepg.ui.jaxrs.webobject.page.dossier.traitementpapier;

import fr.dila.solonepg.ui.bean.dossier.traitementpapier.retour.RetourDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgTraitementPapierUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliDossierTraitementPapierRetour")
public class EpgDossierTraitementPapierRetour extends SolonWebObject {

    @GET
    public ThTemplate getRetour() {
        Map<String, Object> map = template.getData();

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        map.put(EpgTemplateConstants.IS_EDITABLE, epgTraitementPapierUIService.canEditTabs(context));
        RetourDTO retourDTO = epgTraitementPapierUIService.getRetourDTO(context);

        String signataire = retourDTO.getNomsSignataires();
        if (StringUtils.isNotEmpty(signataire)) {
            context.putInContextData(STContextDataKey.USER_ID, signataire);
            SelectValueDTO signataireValueDTO = epgTraitementPapierUIService.usrIdToSelectValueDTO(context);
            retourDTO.setSignataire(signataireValueDTO);
            map.put(
                EpgTemplateConstants.IS_SIGNATAIRE_PARAMETRE,
                EpgDossierTraitementPapierCommunication.isSignataireParametre(context, retourDTO.getSignataire())
            );
        } else {
            retourDTO.setSignataire(new SelectValueDTO());
        }

        map.put(EpgTemplateConstants.RETOUR_DTO, retourDTO);
        map.put(EpgTemplateConstants.SIGNATAIRES, epgTraitementPapierUIService.getReferencesSignataires(context));

        return template;
    }

    @POST
    @Path("editer")
    @Produces("application/vnd.ms-word")
    public Response editerBordereauRetour(@SwBeanParam RetourDTO retourDTO) {
        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        return epgTraitementPapierUIService.editerBordereauRetour(context);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveRetour(@FormParam("dossierId") String dossierId, @SwBeanParam RetourDTO retourDTO) {
        context.setCurrentDocument(dossierId);
        context.putInContextData(EpgContextDataKey.TRAITEMENT_PAPIER_RETOUR, retourDTO);

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        epgTraitementPapierUIService.saveRetourDTO(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/dossier/onglets/traitementpapier/onglet-retour", context);
    }
}
