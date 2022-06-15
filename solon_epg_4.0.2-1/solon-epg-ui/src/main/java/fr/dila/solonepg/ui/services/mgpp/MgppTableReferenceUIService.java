package fr.dila.solonepg.ui.services.mgpp;

import fr.dila.solonepg.ui.bean.MgppTableReferenceEPP;
import fr.dila.solonepg.ui.th.bean.MgppIdentiteForm;
import fr.dila.solonepg.ui.th.bean.MgppMandatForm;
import fr.dila.solonepg.ui.th.bean.MgppMinistereForm;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.th.bean.GouvernementForm;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface MgppTableReferenceUIService {
    MgppTableReferenceEPP getTableReferenceEPP(SpecificContext context);

    GouvernementForm getGouvernementFormFromId(SpecificContext context);

    void creerGouvernement(SpecificContext context);

    void modifierGouvernement(SpecificContext context);

    MgppMinistereForm getMinistereFormFromId(SpecificContext context);

    void creerMinistere(SpecificContext context);

    void modifierMinistere(SpecificContext context);

    MgppMandatForm getMandat(SpecificContext context);

    MgppIdentiteForm getIdentite(SpecificContext context);

    void saveMandat(SpecificContext context);

    void saveIdentite(SpecificContext context);

    String newActeur(SpecificContext context);

    void creerMandatComplet(SpecificContext context);

    MgppTableReferenceEPP getCurrentGouvernement(SpecificContext context);

    List<SelectValueDTO> getCurrentMinisteres(SpecificContext context);
}
