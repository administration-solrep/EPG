package fr.dila.solonepg.ui.services;

import fr.dila.st.ui.th.model.SpecificContext;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Injection de gouvernement.
 *
 * @author jbrunet
 */
public interface OrganigrammeInjectionUIService {
    void saveExcelInjection(SpecificContext context);

    boolean isFichierInjectionDisponible();

    void injecterGouvernementEPG(SpecificContext context);

    void exportGouvernement(CoreSession session);

    void executeInjectionEPG(SpecificContext context);

    void executeInjectionEPP(SpecificContext context);
}
