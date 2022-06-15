package fr.dila.solonepg.ui.services.mgpp;

import fr.dila.ss.ui.services.SSSelectValueUIService;
import fr.dila.st.ui.bean.SelectValueDTO;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

public interface MgppSelectValueUIService extends SSSelectValueUIService {
    List<SelectValueDTO> getAllAttributionsCommission();

    List<SelectValueDTO> getAllDecisionsProcAcc();

    List<SelectValueDTO> getAllInstitutions();

    List<SelectValueDTO> getAllMotifsIrrecevabilite();

    List<SelectValueDTO> getAllNaturesLoi();

    List<SelectValueDTO> getAllNaturesRapport();

    List<SelectValueDTO> getAllNiveauxLecture();

    List<SelectValueDTO> getAllRapportsParlement();

    List<SelectValueDTO> getAllResultatsCMP();

    List<SelectValueDTO> getAllSensAvis();

    List<SelectValueDTO> getAllSortsAdoption();

    List<SelectValueDTO> getAllTypesActeEpp();

    List<SelectValueDTO> getAllTypesLoi();

    List<SelectValueDTO> getSelectableInstitutions();

    List<SelectValueDTO> getSelectableMinisteres();

    List<SelectValueDTO> getChargesMission(CoreSession session);

    List<SelectValueDTO> getAllTypesRapport();

    List<SelectValueDTO> getAllTypesCommunication();

    List<SelectValueDTO> getAllBasesLegales();

    List<SelectValueDTO> getAllStatutDossier();

    List<SelectValueDTO> getAllTypesMandats();

    List<SelectValueDTO> getAllNaturesFdr();

    List<SelectValueDTO> getAllLegislatures();

    List<SelectValueDTO> getAllSessions();

    List<SelectValueDTO> getAllFonctions();
}
