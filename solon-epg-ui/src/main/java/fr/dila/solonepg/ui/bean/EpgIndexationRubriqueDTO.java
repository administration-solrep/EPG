package fr.dila.solonepg.ui.bean;

import fr.dila.solonepg.api.constant.SolonEpgIndextionConstants;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.ui.annot.NxProp;
import org.nuxeo.ecm.platform.actions.Action;

public class EpgIndexationRubriqueDTO {
    @NxProp(
        docType = SolonEpgIndextionConstants.INDEXATION_RUBRIQUE_DOCUMENT_TYPE,
        xpath = STSchemaConstant.ECM_UUID_XPATH
    )
    private String id;

    @NxProp(
        docType = SolonEpgIndextionConstants.INDEXATION_RUBRIQUE_DOCUMENT_TYPE,
        xpath = SolonEpgIndextionConstants.INDEXATION_RUBRIQUE_INTITULE_XPATH
    )
    private String label;

    private Action action;

    public EpgIndexationRubriqueDTO() {}

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

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
