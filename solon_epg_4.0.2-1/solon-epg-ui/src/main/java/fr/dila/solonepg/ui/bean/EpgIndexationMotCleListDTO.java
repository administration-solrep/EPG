package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.api.constant.SolonEpgIndextionConstants;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.ui.annot.NxProp;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.platform.actions.Action;

public class EpgIndexationMotCleListDTO {
    @NxProp(
        docType = SolonEpgIndextionConstants.INDEXATION_MOT_CLE_DOCUMENT_TYPE,
        xpath = STSchemaConstant.ECM_UUID_XPATH
    )
    private String id;

    @NxProp(
        docType = SolonEpgIndextionConstants.INDEXATION_MOT_CLE_DOCUMENT_TYPE,
        xpath = SolonEpgIndextionConstants.INDEXATION_MOT_CLE_INTITULE_XPATH
    )
    private String label;

    @NxProp(
        docType = SolonEpgIndextionConstants.INDEXATION_MOT_CLE_DOCUMENT_TYPE,
        xpath = SolonEpgIndextionConstants.INDEXATION_MOT_CLE_MINISTERE_IDS_XPATH
    )
    private List<String> ministereIds;

    @NxProp(
        docType = SolonEpgIndextionConstants.INDEXATION_MOT_CLE_DOCUMENT_TYPE,
        xpath = SolonEpgIndextionConstants.INDEXATION_MOT_CLE_MOTS_CLES_XPATH
    )
    private List<String> motCle;

    private List<Action> generalesActions;

    private Action action;

    private Map<String, String> mapMinistere;

    public EpgIndexationMotCleListDTO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getMinistereIds() {
        return ministereIds;
    }

    public void setMinistereIds(List<String> ministereIds) {
        this.ministereIds = ministereIds;
    }

    public List<String> getMotCle() {
        return motCle;
    }

    public void setMotCle(List<String> motCle) {
        this.motCle = motCle;
    }

    public List<Action> getGeneralesActions() {
        return generalesActions;
    }

    public void setGeneralesActions(List<Action> generalesActions) {
        this.generalesActions = generalesActions;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Map<String, String> getMapMinistere() {
        return mapMinistere;
    }

    public void setMapMinistere(Map<String, String> mapMinistere) {
        this.mapMinistere = mapMinistere;
    }
}
