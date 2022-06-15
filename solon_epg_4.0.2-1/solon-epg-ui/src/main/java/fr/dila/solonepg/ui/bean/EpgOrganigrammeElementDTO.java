package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.service.organigramme.OrganigrammeService;
import fr.dila.st.ui.bean.OrganigrammeElementDTO;
import java.util.List;
import java.util.Objects;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public class EpgOrganigrammeElementDTO extends OrganigrammeElementDTO {

    public EpgOrganigrammeElementDTO(CoreSession session, OrganigrammeNode organigrammeNode) {
        super(session, organigrammeNode);
    }

    public EpgOrganigrammeElementDTO(
        CoreSession session,
        OrganigrammeNode organigrammeNode,
        String ministereId,
        OrganigrammeElementDTO parent
    ) {
        super(session, organigrammeNode, ministereId);
        setParent(parent);
    }

    @Override
    public void setOrganigrammeNode(CoreSession session, OrganigrammeNode organigrammeNode) {
        this.organigrammeNode = organigrammeNode;
        if (organigrammeNode != null) {
            setKey(organigrammeNode.getId());
            setCompleteKey(getKey());
            setLabel(
                OrganigrammeType.UNITE_STRUCTURELLE == organigrammeNode.getType()
                    ? organigrammeNode.getLabel()
                    : organigrammeNode.getLabelWithNor(ministereId)
            );
            setType(organigrammeNode.getType().getValue());
            setIsActive(organigrammeNode.isActive());
            setLockDate(organigrammeNode.getLockDate());
            setLockUserName(organigrammeNode.getLockUserName());

            if (OrganigrammeType.POSTE == organigrammeNode.getType()) {
                // Récupérer le ministère CE depuis al table de référence pour identifier si l'organigramme node appartient à ce MIN.
                TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
                DocumentModel tableReferenceDoc = tableReferenceService.getTableReference(session);
                String idMinCE = tableReferenceDoc.getAdapter(TableReference.class).getMinistereCE();

                OrganigrammeService organigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
                OrganigrammeNode minCE = organigrammeService.getOrganigrammeNodeById(
                    idMinCE,
                    OrganigrammeType.MINISTERE
                );
                List<PosteNode> posteCEListe = organigrammeService.getAllSubPostes(minCE);
                setIsPosteCE(
                    posteCEListe
                        .stream()
                        .map(OrganigrammeNode::getId)
                        .anyMatch(posteCEId -> Objects.equals(posteCEId, organigrammeNode.getId()))
                );
            }
        } else {
            setKey("");
            setCompleteKey("");
            setLabel("");
            setType("");
            setIsActive(true);
            setLockDate(null);
            setLockUserName("");
        }
    }
}
