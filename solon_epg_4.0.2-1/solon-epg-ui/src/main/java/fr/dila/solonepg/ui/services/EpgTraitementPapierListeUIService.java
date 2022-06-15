package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.EpgListTraitementPapierDTO;
import fr.dila.solonepg.ui.bean.TraitementPapierList;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;
import java.util.List;
import javax.ws.rs.core.Response;

public interface EpgTraitementPapierListeUIService {
    TraitementPapierList consultTraitementPapierListe(SpecificContext context);

    List<EpgListTraitementPapierDTO> getListeTraitementPapierSignature(SpecificContext context);

    List<EpgListTraitementPapierDTO> getListeTraitementPapierAutre(SpecificContext context);

    boolean removeElementFromListe(SpecificContext context);

    File exportListeModal(SpecificContext context);

    void traiterEnSerieListe(SpecificContext context);

    void initGestionListeActionContext(SpecificContext context);

    Response imprimerListe(SpecificContext context);
}
