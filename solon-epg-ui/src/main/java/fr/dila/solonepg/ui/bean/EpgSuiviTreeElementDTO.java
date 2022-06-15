package fr.dila.solonepg.ui.bean;

import fr.dila.st.api.dossier.STDossier;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.ui.bean.TreeElementDTO;

public class EpgSuiviTreeElementDTO extends TreeElementDTO {
    private OrganigrammeType type;

    private OrganigrammeNode node;

    private Long count;

    private STDossier.DossierState dossierState;

    public EpgSuiviTreeElementDTO(OrganigrammeType type) {
        this.type = type;
    }

    public EpgSuiviTreeElementDTO(
        OrganigrammeType type,
        OrganigrammeNode node,
        Long count,
        STDossier.DossierState dossierState
    ) {
        this.type = type;
        this.node = node;
        this.count = count;
        this.dossierState = dossierState;
    }

    public EpgSuiviTreeElementDTO() {}

    public OrganigrammeType getType() {
        return type;
    }

    public void setType(OrganigrammeType type) {
        this.type = type;
    }

    public OrganigrammeNode getNode() {
        return node;
    }

    public void setNode(OrganigrammeNode node) {
        this.node = node;
    }

    public STDossier.DossierState getDossierState() {
        return dossierState;
    }

    public void setDossierState(STDossier.DossierState dossierState) {
        this.dossierState = dossierState;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
