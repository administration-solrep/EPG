package fr.dila.solonepg.core.dto.activitenormative;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import java.io.Serializable;
import org.nuxeo.ecm.core.api.CoreSession;

public class FicheSignaletiqueApplicationOrdonnanceDTO
    extends AbstractFicheSignaletiqueApplicationDTO
    implements Serializable {
    private static final long serialVersionUID = 1L;

    public FicheSignaletiqueApplicationOrdonnanceDTO(ActiviteNormative activiteNormative, CoreSession session) {
        super(activiteNormative, session);
    }
}
