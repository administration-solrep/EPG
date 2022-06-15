package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.dossier.tablesreference.AdminModeParutionDTO;
import fr.dila.solonepg.ui.bean.dossier.tablesreference.AdminTableReferenceDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgDossierTablesReferenceUIService;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

public class EpgDossierTablesReferenceUIServiceImpl implements EpgDossierTablesReferenceUIService {
    private static final String ACCESS_DENIED_KEY = "error.403.shortmessage";

    private boolean isAccessAuthorized(SpecificContext context) {
        SSPrincipal ssPrincipal = (SSPrincipal) context.getSession().getPrincipal();
        return (
            ssPrincipal.isAdministrator() ||
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMINISTRATION_TABLES_REFERENCE_READER)
        );
    }

    @Override
    public AdminTableReferenceDTO getReferences(SpecificContext context) {
        if (!isAccessAuthorized(context)) {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString(ACCESS_DENIED_KEY));
            return null;
        }

        CoreSession session = context.getSession();
        TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
        DocumentModel tableReferenceDoc = tableReferenceService.getTableReference(session);

        AdminTableReferenceDTO dto = MapDoc2Bean.docToBean(tableReferenceDoc, AdminTableReferenceDTO.class);
        dto.setMapAutoritesValidationCabinetPM(
            fetchMapDataMultiple(dto.getAutoritesValidationCabinetPM(), OrganigrammeType.USER)
        );
        dto.setMapAutoritesValidationChargesMission(
            fetchMapDataMultiple(dto.getAutoritesValidationChargesMission(), OrganigrammeType.USER)
        );
        dto.setMapCheminCroixSignatureCPM(
            fetchMapDataMultiple(dto.getCheminCroixSignatureCPM(), OrganigrammeType.USER)
        );
        dto.setMapCheminCroixSignatureSgg(
            fetchMapDataMultiple(dto.getCheminCroixSignatureSgg(), OrganigrammeType.USER)
        );
        dto.setMapVecteurPublicationMultiples(
            fetchMapDataMultiple(dto.getVecteurPublicationMultiples(), OrganigrammeType.USER)
        );
        dto.setMapPosteDan(fetchMapData(dto.getPosteDan(), OrganigrammeType.POSTE));
        dto.setMapPostePublication(fetchMapData(dto.getPostePublication(), OrganigrammeType.POSTE));
        dto.setMapAvisRectificatifPosteDanDTO(fetchMapData(dto.getAvisRectificatifPosteDan(), OrganigrammeType.POSTE));
        dto.setMapCorbeillesRetourDAN(fetchMapDataMultiple(dto.getCorbeillesRetourDAN(), OrganigrammeType.POSTE));
        dto.setMapMinisterePm(fetchMapData(dto.getMinisterePm(), OrganigrammeType.MINISTERE));
        dto.setMapConseilEtat(fetchMapData(dto.getConseilEtat(), OrganigrammeType.MINISTERE));
        dto.setMapDirectionsPm(fetchMapDataMultiple(dto.getDirectionsPm(), OrganigrammeType.UNITE_STRUCTURELLE));

        ArrayList<String> signataires = dto
            .getPersistedSignataires()
            .stream()
            .filter(id -> STServiceLocator.getSTUserService().getOptionalUser(id).isPresent())
            .collect(Collectors.toCollection(ArrayList::new));
        dto.setSignataires(signataires);

        ArrayList<String> signatairesLibres = new ArrayList<>(dto.getPersistedSignataires());
        signatairesLibres.removeAll(signataires);
        dto.setSignatairesLibres(signatairesLibres);

        dto.setMapSignataire(fetchMapDataMultiple(dto.getSignataires(), OrganigrammeType.USER));

        return dto;
    }

    @Override
    public void saveReferences(SpecificContext context) {
        if (!isAccessAuthorized(context)) {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString(ACCESS_DENIED_KEY));
            return;
        }

        CoreSession session = context.getSession();
        TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
        DocumentModel tableReferenceDoc = tableReferenceService.getTableReference(session);

        AdminTableReferenceDTO adminTableReferenceDTO = context.getFromContextData(
            EpgContextDataKey.ADMIN_TABLE_REFERENCE
        );

        Set<String> persistedSignataires = new LinkedHashSet<>(
            ObjectHelper.requireNonNullElse(adminTableReferenceDTO.getSignataires(), Collections.emptySet())
        );
        persistedSignataires.addAll(adminTableReferenceDTO.getSignatairesLibres());
        persistedSignataires =
            persistedSignataires
                .stream()
                .filter(StringUtils::isNotBlank)
                .flatMap(ids -> Arrays.stream(ids.split(",")))
                .collect(Collectors.toSet());
        adminTableReferenceDTO.setPersistedSignataires(new ArrayList<>(persistedSignataires));
        MapDoc2Bean.beanToDoc(adminTableReferenceDTO, tableReferenceDoc);
        session.saveDocument(tableReferenceDoc);
        context.getMessageQueue().addToastSuccess(ResourceHelper.getString("admin.tables.reference.maj"));
    }

    private Map<String, String> fetchMapDataMultiple(List<String> list, OrganigrammeType type) {
        if (OrganigrammeType.USER == type) {
            return list
                .stream()
                .map(
                    username ->
                        ImmutablePair.of(
                            username,
                            STServiceLocator.getSTUserService().getUserFullNameWithUsername(username)
                        )
                )
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        } else {
            return list == null
                ? null
                : list
                    .stream()
                    .map(
                        id ->
                            STServiceLocator
                                .getOrganigrammeService()
                                .<OrganigrammeNode>getOrganigrammeNodeById(id, type)
                    )
                    .collect(Collectors.toMap(OrganigrammeNode::getId, OrganigrammeNode::getLabel));
        }
    }

    private Map<String, String> fetchMapData(String id, OrganigrammeType type) {
        return Optional
            .ofNullable(id)
            .map(
                nodeId -> {
                    OrganigrammeNode node = STServiceLocator.getOrganigrammeService().getOrganigrammeNodeById(id, type);
                    return Collections.singletonMap(id, node == null ? null : node.getLabel());
                }
            )
            .orElse(null);
    }

    @Override
    public List<AdminModeParutionDTO> getListModeParution(SpecificContext context) {
        if (!isAccessAuthorized(context)) {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString(ACCESS_DENIED_KEY));
            return Collections.emptyList();
        }

        CoreSession session = context.getSession();
        TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
        List<DocumentModel> modesParution = tableReferenceService.getModesParutionList(session);

        return modesParution
            .stream()
            .map(d -> MapDoc2Bean.docToBean(d, AdminModeParutionDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public void createModeParution(SpecificContext context) {
        if (!isAccessAuthorized(context)) {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString(ACCESS_DENIED_KEY));
            return;
        }

        AdminModeParutionDTO mode = context.getFromContextData(EpgContextDataKey.ADMIN_MODE_PARUTION);
        TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();

        CoreSession session = context.getSession();
        DocumentModel modeDoc = tableReferenceService.initModeParution(session);
        MapDoc2Bean.beanToDoc(mode, modeDoc);

        tableReferenceService.createModeParution(session, modeDoc.getAdapter(ModeParution.class));
        session.save();
        context
            .getMessageQueue()
            .addSuccessToQueue(ResourceHelper.getString("admin.tables.reference.mode.parution.ajout"));
    }

    @Override
    public void deleteModeParution(SpecificContext context) {
        if (!isAccessAuthorized(context)) {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString(ACCESS_DENIED_KEY));
            return;
        }

        CoreSession session = context.getSession();
        String id = context.getFromContextData(STContextDataKey.ID);
        IdRef modeParutionRef = new IdRef(id);
        ModeParution modeParution = session.getDocument(modeParutionRef).getAdapter(ModeParution.class);

        TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
        tableReferenceService.deleteModeParution(session, modeParution);
        context
            .getMessageQueue()
            .addSuccessToQueue(ResourceHelper.getString("admin.tables.reference.mode.parution.suppression"));
    }

    @Override
    public void saveModeParution(SpecificContext context) {
        if (!isAccessAuthorized(context)) {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString(ACCESS_DENIED_KEY));
            return;
        }

        AdminModeParutionDTO mode = context.getFromContextData(EpgContextDataKey.ADMIN_MODE_PARUTION);
        TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();

        CoreSession session = context.getSession();
        DocumentModel modeDoc = session.getDocument(new IdRef(mode.getId()));
        MapDoc2Bean.beanToDoc(mode, modeDoc);

        tableReferenceService.updateModeParution(session, modeDoc.getAdapter(ModeParution.class));
        context.getSession().save();
        context
            .getMessageQueue()
            .addSuccessToQueue(ResourceHelper.getString("admin.tables.reference.mode.parution.maj"));
    }
}
