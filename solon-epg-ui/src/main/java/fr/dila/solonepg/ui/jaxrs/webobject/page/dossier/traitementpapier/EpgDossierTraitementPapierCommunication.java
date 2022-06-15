package fr.dila.solonepg.ui.jaxrs.webobject.page.dossier.traitementpapier;

import fr.dila.solonepg.ui.bean.dossier.bordereau.EpgBordereauDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.communication.CommunicationDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.communication.DestinataireCommunicationDTO;
import fr.dila.solonepg.ui.bean.dossier.traitementpapier.communication.PutAndGetTemplateDTO;
import fr.dila.solonepg.ui.enums.EpgActionCategory;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgSelectValueUIService;
import fr.dila.solonepg.ui.services.EpgTraitementPapierUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.constants.EpgTemplateConstants;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBeanParam;
import fr.dila.st.ui.bean.JsonResponse;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.enums.SolonStatus;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.dila.st.ui.th.constants.STTemplateConstants;
import fr.dila.st.ui.th.model.AjaxLayoutThTemplate;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "AppliDossierTraitementPapierCommunication")
public class EpgDossierTraitementPapierCommunication extends SolonWebObject {
    private static final String CABINET_PM = "cabinetPM";
    private static final String CHARGE_MISSION = "chargeMission";
    private static final String AUTRES = "autres";
    private static final String CABINET_SG = "cabinetSG";
    private static final String CABINET_PM_ARRAY = "cabinetPM[]";
    private static final String CHARGE_MISSION_ARRAY = "chargeMission[]";
    private static final String AUTRES_ARRAY = "autres[]";
    private static final String CABINET_SG_ARRAY = "cabinetSG[]";

    public EpgDossierTraitementPapierCommunication() {
        super();
    }

    @GET
    public ThTemplate getCommunication() {
        Map<String, Object> map = template.getData();

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        map.put(EpgTemplateConstants.IS_EDITABLE, epgTraitementPapierUIService.canEditTabs(context));
        CommunicationDTO communicationDTO = epgTraitementPapierUIService.getCommunicationDTO(context);

        EpgSelectValueUIService epgSelectValueUIService = EpgUIServiceLocator.getEpgSelectValueUIService();
        List<SelectValueDTO> sensAvis = epgSelectValueUIService.getTraitementPapierTypeAvis();
        map.put(EpgTemplateConstants.SENS_AVIS, sensAvis);

        map.put(
            EpgTemplateConstants.DESTINATAIRES_CABINET_PM,
            epgTraitementPapierUIService.getReferencesCabinetPremierMinistre(context)
        );
        map.put(
            EpgTemplateConstants.DESTINATAIRES_CHARGE_MISSION,
            epgTraitementPapierUIService.getReferencesChargesMission(context)
        );
        map.put(EpgTemplateConstants.DESTINATAIRES_AUTRES, new ArrayList<>());
        map.put(
            EpgTemplateConstants.DESTINATAIRES_CABINET_SG,
            epgTraitementPapierUIService.getReferencesCabinetSg(context)
        );
        map.put(EpgTemplateConstants.SIGNATAIRES, epgTraitementPapierUIService.getReferencesSignataires(context));

        String signataire = communicationDTO.getNomsSignatairesCommunication();
        if (StringUtils.isNotEmpty(signataire)) {
            context.putInContextData(STContextDataKey.USER_ID, signataire);
            communicationDTO.setSignataire(epgTraitementPapierUIService.usrIdToSelectValueDTO(context));
            map.put(
                EpgTemplateConstants.IS_SIGNATAIRE_PARAMETRE,
                isSignataireParametre(context, communicationDTO.getSignataire())
            );
        } else {
            map.put(EpgTemplateConstants.IS_SIGNATAIRE_PARAMETRE, true);
        }

        map.put(EpgTemplateConstants.COMMUNICATION_DTO, communicationDTO);

        // Actions pour le tableau Cabinet PM
        map.put(
            EpgTemplateConstants.ADD_ACTION_CABINET_PM,
            context.getActions(EpgActionCategory.COMMUNICATION_CABINET_PM_AJOUTER_ACTIONS)
        );
        map.put(
            EpgTemplateConstants.TABLE_ACTION_CABINET_PM,
            context.getActions(EpgActionCategory.COMMUNICATION_CABINET_PM_TABLE_ACTIONS)
        );

        // Actions pour le tableau Chargé de mission
        map.put(
            EpgTemplateConstants.ADD_ACTION_CHARGE_MISSION,
            context.getActions(EpgActionCategory.COMMUNICATION_CHARGE_MISSION_AJOUTER_ACTIONS)
        );
        map.put(
            EpgTemplateConstants.TABLE_ACTION_CHARGE_MISSION,
            context.getActions(EpgActionCategory.COMMUNICATION_CHARGE_MISSION_TABLE_ACTIONS)
        );

        // Actions pour le tableau Autres
        map.put(
            EpgTemplateConstants.ADD_ACTION_AUTRES,
            context.getActions(EpgActionCategory.COMMUNICATION_AUTRES_AJOUTER_ACTIONS)
        );
        map.put(
            EpgTemplateConstants.TABLE_ACTION_AUTRES,
            context.getActions(EpgActionCategory.COMMUNICATION_AUTRES_TABLE_ACTIONS)
        );

        // Actions pour le tableau Cabinet du Secrétaire général
        map.put(
            EpgTemplateConstants.ADD_ACTION_CABINET_SG,
            context.getActions(EpgActionCategory.COMMUNICATION_CABINET_SG_AJOUTER_ACTIONS)
        );
        map.put(
            EpgTemplateConstants.TABLE_ACTION_CABINET_SG,
            context.getActions(EpgActionCategory.COMMUNICATION_CABINET_SG_TABLE_ACTIONS)
        );

        // Tableau cabinet sg invisible + bouton editer bordereau envoi cabinet sg
        map.put(
            EpgTemplateConstants.TABLE_ACTION_CABINET_SG_VISIBLE,
            epgTraitementPapierUIService.isCommunicationSecretaireGeneralVisible(context)
        );

        // Bloc complement invisble avec boutons ("Priorité & signataires & liste des personnes nommées dans le même texte")
        map.put(
            EpgTemplateConstants.COMMUNICATION_COMPLEMENT_VISIBLE,
            epgTraitementPapierUIService.isCommunicationComplementVisible(context)
        );

        // on ajoute la liste des destinataire en session (pour les réafficher
        // lors de l'ajout/suppression)
        UserSessionHelper.putUserSessionParameter(context, CABINET_PM, communicationDTO.getCabinetPm());
        UserSessionHelper.putUserSessionParameter(context, CHARGE_MISSION, communicationDTO.getChargeMission());
        UserSessionHelper.putUserSessionParameter(context, AUTRES, communicationDTO.getAutresDestinataires());
        UserSessionHelper.putUserSessionParameter(context, CABINET_SG, communicationDTO.getCabinetSg());

        return template;
    }

    /**
     * Permet de savoir si le signataire fait partie de la liste paramétrée
     *
     * @param signataire
     * @return true si le signataire fait partie de la liste paramétrée
     */
    public static boolean isSignataireParametre(SpecificContext context, SelectValueDTO signataire) {
        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        return (
            signataire != null &&
            epgTraitementPapierUIService
                .getReferencesSignataires(context)
                .stream()
                .anyMatch(item -> signataire.getKey().equals(item.getKey()))
        );
    }

    @POST
    @Path("initialiser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response initModalAjouter(@SwId @SwRequired @FormParam("dossierId") String dossierId) {
        context.setCurrentDocument(dossierId);

        Map<String, Object> jsonData = new HashMap<>();
        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        jsonData.put("objet", epgTraitementPapierUIService.getCommunicationDestinataireObjet(context));
        jsonData.put("dateEnvoi", null);
        jsonData.put("dateRelance", null);
        jsonData.put("dateRetour", null);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue(), (Serializable) jsonData).build();
    }

    @GET
    @Path("ajouter")
    public ThTemplate addDestinataire(
        @QueryParam("id") String id,
        @QueryParam("uniqueId") String uniqueId,
        @SwBeanParam DestinataireCommunicationDTO destinataire
    ) {
        Function<PutAndGetTemplateDTO, Map<String, Object>> addFunction = this::putAndGetTemplateData;
        return addOrEditCommunication(id, uniqueId, destinataire, addFunction);
    }

    @GET
    @Path("editer")
    public ThTemplate editDestinataire(
        @QueryParam("id") String id,
        @QueryParam("uniqueId") String uniqueId,
        @SwBeanParam DestinataireCommunicationDTO destinataire
    ) {
        Function<PutAndGetTemplateDTO, Map<String, Object>> addFunction = this::updateAndGetTemplateData;
        return addOrEditCommunication(id, uniqueId, destinataire, addFunction);
    }

    private ThTemplate addOrEditCommunication(
        String id,
        String uniqueId,
        DestinataireCommunicationDTO destinataire,
        Function<PutAndGetTemplateDTO, Map<String, Object>> addOrEditFunction
    ) {
        ThTemplate template = new AjaxLayoutThTemplate(
            "fragments/dossier/onglets/traitementpapier/communication/table-destinataire",
            context
        );

        destinataire.setId(uniqueId); // set new id
        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();

        Map<String, Object> map = null;
        PutAndGetTemplateDTO dto;
        switch (id) {
            case CABINET_PM:
                dto =
                    new PutAndGetTemplateDTO(
                        CABINET_PM,
                        destinataire,
                        "dossier.traitement.papier.communication.cabinet.pm",
                        epgTraitementPapierUIService.getReferencesCabinetPremierMinistre(context),
                        context.getActions(EpgActionCategory.COMMUNICATION_CABINET_PM_AJOUTER_ACTIONS),
                        context.getActions(EpgActionCategory.COMMUNICATION_CABINET_PM_TABLE_ACTIONS)
                    );
                map = addOrEditFunction.apply(dto);
                break;
            case CHARGE_MISSION:
                dto =
                    new PutAndGetTemplateDTO(
                        CHARGE_MISSION,
                        destinataire,
                        "dossier.traitement.papier.communication.charge.mission",
                        epgTraitementPapierUIService.getReferencesChargesMission(context),
                        context.getActions(EpgActionCategory.COMMUNICATION_CHARGE_MISSION_AJOUTER_ACTIONS),
                        context.getActions(EpgActionCategory.COMMUNICATION_CHARGE_MISSION_TABLE_ACTIONS)
                    );
                map = addOrEditFunction.apply(dto);
                break;
            case AUTRES:
                dto =
                    new PutAndGetTemplateDTO(
                        AUTRES,
                        destinataire,
                        "dossier.traitement.papier.communication.autres",
                        new ArrayList<>(),
                        context.getActions(EpgActionCategory.COMMUNICATION_AUTRES_AJOUTER_ACTIONS),
                        context.getActions(EpgActionCategory.COMMUNICATION_AUTRES_TABLE_ACTIONS)
                    );
                map = addOrEditFunction.apply(dto);
                break;
            case CABINET_SG:
                dto =
                    new PutAndGetTemplateDTO(
                        CABINET_SG,
                        destinataire,
                        "dossier.traitement.papier.communication.cabinet.sg",
                        epgTraitementPapierUIService.getReferencesCabinetSg(context),
                        context.getActions(EpgActionCategory.COMMUNICATION_CABINET_SG_AJOUTER_ACTIONS),
                        context.getActions(EpgActionCategory.COMMUNICATION_CABINET_SG_TABLE_ACTIONS)
                    );
                map = addOrEditFunction.apply(dto);
                break;
            default:
                map = new HashMap<>();
                break;
        }
        // valeurs identiques pour chaque tableau
        EpgBordereauDTO bordereauDto = EpgUIServiceLocator.getBordereauUIService().getBordereau(context);
        map.put(EpgTemplateConstants.IS_EDITABLE, bordereauDto.getIsEdit());
        map.put(STTemplateConstants.ID, id);
        EpgSelectValueUIService epgSelectValueUIService = EpgUIServiceLocator.getEpgSelectValueUIService();
        map.put(EpgTemplateConstants.SENS_AVIS, epgSelectValueUIService.getTraitementPapierTypeAvis());

        template.setData(map);

        return template;
    }

    private Map<String, Object> putAndGetTemplateData(PutAndGetTemplateDTO dto) {
        // on récupère la liste des destinataires en session
        List<DestinataireCommunicationDTO> destinataires = UserSessionHelper.getUserSessionParameter(
                context,
                dto.getType()
            ) !=
            null
            ? UserSessionHelper.getUserSessionParameter(context, dto.getType())
            : new ArrayList<>();

        DestinataireCommunicationDTO newDestinataire = dto.getDestinataire();
        EpgUIServiceLocator.getEpgTraitementPapierUIService().setLabelValues(Stream.of(newDestinataire));
        destinataires.add(newDestinataire);
        return buildSessionAndMap(dto, destinataires);
    }

    private Map<String, Object> updateAndGetTemplateData(PutAndGetTemplateDTO dto) {
        // on récupère la liste des destinataires en session
        List<DestinataireCommunicationDTO> destinataires = UserSessionHelper.getUserSessionParameter(
                context,
                dto.getType()
            ) !=
            null
            ? UserSessionHelper.getUserSessionParameter(context, dto.getType())
            : new ArrayList<>();

        if (CollectionUtils.isNotEmpty(destinataires)) {
            // On récupère la communication editer en fonction du username et la delete de la liste
            destinataires
                .stream()
                .filter(dest -> dest.getId().equals(dto.getDestinataire().getId()))
                .findFirst()
                .ifPresent(oldDestinataire -> destinataires.remove(oldDestinataire));

            // On ajoute dans la liste la nouvelle communication avec les valeur édité
            DestinataireCommunicationDTO newDestinataire = dto.getDestinataire();
            EpgUIServiceLocator.getEpgTraitementPapierUIService().setLabelValues(Stream.of(newDestinataire));
            destinataires.add(newDestinataire);
        }
        return buildSessionAndMap(dto, destinataires);
    }

    private Map<String, Object> buildSessionAndMap(
        PutAndGetTemplateDTO dto,
        List<DestinataireCommunicationDTO> destinataires
    ) {
        Map<String, Object> map = new HashMap<>();
        // on met à jour la nouvelle liste de destinataire en session
        UserSessionHelper.putUserSessionParameter(context, dto.getType(), destinataires);
        // puis on ajoute les datas à la map
        map.put(EpgTemplateConstants.LIST_DESTINATAIRE_COMMUNICATION_DTO, destinataires);
        map.put(STTemplateConstants.TITLE, dto.getTitle());
        map.put(EpgTemplateConstants.LIST_DESTINATAIRE, dto.getListDestinataire());
        map.put(EpgTemplateConstants.ADD_ACTION, dto.getAddAction());
        map.put(STTemplateConstants.EDIT_ACTION, dto.getEditAction());

        return map;
    }

    @POST
    @Path("supprimer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeDestinataire(@FormParam("id") String id, @FormParam("idDestinataire") String idDestinataire) {
        // on récupère la liste des destinataires en session
        List<DestinataireCommunicationDTO> destinataires = UserSessionHelper.getUserSessionParameter(context, id);
        // on supprime le destinataire de la liste
        if (CollectionUtils.isNotEmpty(destinataires)) {
            for (int i = 0; i < destinataires.size(); i++) {
                if (idDestinataire.equals(destinataires.get(i).getId())) {
                    destinataires.remove(i);
                    break;
                }
            }
        }
        // on met à jour la nouvelle liste de destinataire en session
        UserSessionHelper.putUserSessionParameter(context, id, destinataires);

        return new JsonResponse(SolonStatus.OK, context.getMessageQueue()).build();
    }

    @POST
    @Path("editer/bordereau/cabinetsg")
    @Produces("application/vnd.ms-word")
    public Response editerBordereauCabinetSG(
        @SwBeanParam CommunicationDTO communicationDTO,
        @FormParam(CABINET_PM_ARRAY) List<String> cabinetPM,
        @FormParam(CHARGE_MISSION_ARRAY) List<String> chargeMission,
        @FormParam(AUTRES_ARRAY) List<String> autres,
        @FormParam(CABINET_SG_ARRAY) List<String> cabinetSG
    ) {
        putCommunicationDTOInContext(context, communicationDTO);
        putSelectedDestinatairesInCommunicationDTO(communicationDTO, cabinetPM, chargeMission, autres);

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        return epgTraitementPapierUIService.editerBordereauCabinetSg(context);
    }

    @POST
    @Path("editer/bordereau/relance")
    @Produces("application/vnd.ms-word")
    public Response editerBordereauRelance(
        @SwBeanParam CommunicationDTO communicationDTO,
        @FormParam(CABINET_PM_ARRAY) List<String> cabinetPM,
        @FormParam(CHARGE_MISSION_ARRAY) List<String> chargeMission,
        @FormParam(AUTRES_ARRAY) List<String> autres,
        @FormParam(CABINET_SG_ARRAY) List<String> cabinetSG
    ) {
        putCommunicationDTOInContext(context, communicationDTO);
        putSelectedDestinatairesInCommunicationDTO(communicationDTO, cabinetPM, chargeMission, autres);

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        return epgTraitementPapierUIService.editerBordereauRelance(context);
    }

    @POST
    @Path("editer/bordereau/envoi")
    @Produces("application/vnd.ms-word")
    public Response editerBordereauEnvoi(
        @SwBeanParam CommunicationDTO communicationDTO,
        @FormParam(CABINET_PM_ARRAY) List<String> cabinetPM,
        @FormParam(CHARGE_MISSION_ARRAY) List<String> chargeMission,
        @FormParam(AUTRES_ARRAY) List<String> autres,
        @FormParam(CABINET_SG_ARRAY) List<String> cabinetSG
    ) {
        putCommunicationDTOInContext(context, communicationDTO);
        putSelectedDestinatairesInCommunicationDTO(communicationDTO, cabinetPM, chargeMission, autres);

        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        return epgTraitementPapierUIService.editerBordereauEnvoi(context);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveCommunication(
        @FormParam("dossierId") String dossierId,
        @SwBeanParam CommunicationDTO communicationDTO
    ) {
        context.setCurrentDocument(dossierId);

        putCommunicationDTOInContext(context, communicationDTO);
        EpgTraitementPapierUIService epgTraitementPapierUIService = EpgUIServiceLocator.getEpgTraitementPapierUIService();
        epgTraitementPapierUIService.saveCommunicationDTO(context);

        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            UserSessionHelper.putUserSessionParameter(
                context,
                SpecificContext.MESSAGE_QUEUE,
                context.getMessageQueue()
            );
        }

        return getJsonResponseWithMessagesInSessionIfSuccess();
    }

    private void putCommunicationDTOInContext(SpecificContext context, CommunicationDTO communicationDTO) {
        List<DestinataireCommunicationDTO> destinatairesCabinetPm = ObjectHelper.requireNonNullElseGet(
            UserSessionHelper.getUserSessionParameter(context, CABINET_PM),
            ArrayList::new
        );
        communicationDTO.setCabinetPm(destinatairesCabinetPm);

        List<DestinataireCommunicationDTO> destinatairesChargeMission = ObjectHelper.requireNonNullElseGet(
            UserSessionHelper.getUserSessionParameter(context, CHARGE_MISSION),
            ArrayList::new
        );
        communicationDTO.setChargeMission(destinatairesChargeMission);

        List<DestinataireCommunicationDTO> autresDestinataires = ObjectHelper.requireNonNullElseGet(
            UserSessionHelper.getUserSessionParameter(context, AUTRES),
            ArrayList::new
        );
        communicationDTO.setAutresDestinataires(autresDestinataires);

        List<DestinataireCommunicationDTO> destinatairesCabinetSg = ObjectHelper.requireNonNullElseGet(
            UserSessionHelper.getUserSessionParameter(context, CABINET_SG),
            ArrayList::new
        );
        communicationDTO.setCabinetSg(destinatairesCabinetSg);

        context.putInContextData(EpgContextDataKey.TRAITEMENT_PAPIER_COMMUNICATION, communicationDTO);
    }

    private void putSelectedDestinatairesInCommunicationDTO(
        CommunicationDTO communicationDTO,
        List<String> cabinetPM,
        List<String> chargeMission,
        List<String> autres
    ) {
        communicationDTO.setSelectedAutresDestinataires(autres);
        communicationDTO.setSelectedCabinetPm(cabinetPM);
        communicationDTO.setSelectedChargeMission(chargeMission);
    }

    @Override
    protected ThTemplate getMyTemplate() {
        return new AjaxLayoutThTemplate("fragments/dossier/onglets/traitementpapier/onglet-communication", context);
    }
}
