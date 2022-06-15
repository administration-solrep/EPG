package fr.dila.solonepg.core.dto.activitenormative;

import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.st.core.client.AbstractMapDTO;

public abstract class AbstractMapTexteMaitreTableDTO extends AbstractMapDTO {
    private static final long serialVersionUID = 1922121777609238106L;

    private static final String VALIDATE = "validate";

    public AbstractMapTexteMaitreTableDTO() {
        super();
    }

    public String getId() {
        return getString(TexteMaitreConstants.ID);
    }

    public void setId(String id) {
        put(TexteMaitreConstants.ID, id);
    }

    public Boolean isValidate() {
        return getBoolean(VALIDATE);
    }

    public void setValidate(Boolean validation) {
        put(VALIDATE, validation);
    }

    public String getMinisterePiloteLabel() {
        return getString(TexteMaitreConstants.MINISTERE_PILOTE_LABEL);
    }

    public void setMinisterePiloteLabel(String ministerePilote) {
        put(TexteMaitreConstants.MINISTERE_PILOTE_LABEL, ministerePilote);
    }
}
