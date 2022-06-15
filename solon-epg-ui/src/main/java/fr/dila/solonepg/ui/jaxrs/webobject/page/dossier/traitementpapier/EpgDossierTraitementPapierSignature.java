package fr.dila.solonepg.ui.jaxrs.webobject.page.dossier.traitementpapier;

import fr.dila.solonepg.ui.bean.dossier.traitementpapier.signature.DonneesSignatureDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.signature.SignatureDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgTraitementPapierUIService;
import fr.dila.solonepg.ui.services.SolonEpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.SelectValueDTO;
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

@WebObject(type = "AppliDossierTraitementPapierSignature")
public class EpgDossierTraitementPapierSignature extends SolonWebObject {
    private static final String SIGNATURE_PM = "PM";
    private static final String SIGNATURE_SGG = "SGG";
    private static final String SIGNATURE_PR = "PR";

    public EpgDossierTraitementPapierSignature() {
        super();
    }

    @GET
    public ThTemplate getSignature() {
        Map<String, Object> map = template.getData();

        EpgTraitementPapierUIService epgTraitementPapierUIService = SolonEpgUIServiceLocator.getEpgTraitementPapierUIService();
        map.put(EpgTemplateConstants.IS_EDITABLE, epgTraitementPapierUIService.canEditTabs(context));
        SignatureDTO signatureDTO = epgTraitementPapierUIService.getSignatureDTO(context);

        // Signataire
        String signataire = null;
        SelectValueDTO signataireValue = epgTraitementPapierUIService.getComplementDTO(context).getSignataireValue();
        if (signataireValue != null) {
            signataire = signataireValue.getLabel();
            switch (signataire) {
                case SIGNATURE_PM:
                    map.put(EpgTemplateConstants.DONNEES_SIGNATURE_DTO, signatureDTO.getSignaturePm());
                    break;
                case SIGNATURE_SGG:
                    map.put(EpgTemplateConstants.DONNEES_SIGNATURE_DTO, signatureDTO.getSignatureSgg());
                    break;
                case SIGNATURE_PR:
                    map.put(EpgTemplateConstants.DONNEES_SIGNATURE_DTO, signatureDTO.getSignaturePr());
                    break;
                default:
                    break;
            }
        }
        map.put(EpgTemplateConstants.SIGNATAIRE, signataire);

        map.put(EpgTemplateConstants.SIGNATURE_DTO, signatureDTO);
        map.put(
            EpgTemplateConstants.IS_VISIBLE,
            epgTraitementPapierUIService.isSignatureEditerCheminCroixVisible(context)
        );

        // Listes de valeurs pour les listes déroulantes
        map.put(
            EpgTemplateConstants.LST_SGG,
            epgTraitementPapierUIService.getReferencesSecretariatSecretaireGeneral(context)
        );
        map.put(
            EpgTemplateConstants.LST_CABINET_PM,
            epgTraitementPapierUIService.getReferencesCabinetPremierMinistre(context)
        );

        return template;
    }

    @POST
    @Path("editer")
    @Produces("application/vnd.ms-word")
    public Response editerCheminCroix(@SwBeanParam SignatureDTO signatureDTO) {
        EpgTraitementPapierUIService epgTraitementPapierUIService = SolonEpgUIServiceLocator.getEpgTraitementPapierUIService();
        return epgTraitementPapierUIService.editerCheminDeCroix(context);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveSignature(
        @FormParam("dossierId") String dossierId,
        @SwBeanParam SignatureDTO signatureDTO,
        @SwBeanParam DonneesSignatureDTO donneesSignatureDTO,
        @FormParam("signataire") String signataire
    ) {
        context.setCurrentDocument(dossierId);
        if (StringUtils.isNotBlank(signataire)) {
            switch (signataire) {
                case SIGNATURE_PM:
                    signatureDTO.setSignaturePm(donneesSignatureDTO);
                    break;
                case SIGNATURE_SGG:
                    signatureDTO.setSignatureSgg(donneesSignatureDTO);
                    break;
                case SIGNATURE_PR:
                    signatureDTO.setSignaturePr(donneesSignatureDTO);
                    break;
                default:
                    break;
            }
        }
        context.putInContextData(EpgContextDataKey.TRAITEMENT_PAPIER_SIGNATURE, signatureDTO);
        EpgTraitementPapierUIService epgTraitementPapierUIService = SolonEpgUIServiceLocator.getEpgTraitementPapierUIService();
        epgTraitementPapierUIService.saveSignatureDTO(context);

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/dossier/onglets/traitementpapier/onglet-signature", context);
    }
}
