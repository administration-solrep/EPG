package fr.dila.solonepg.ui.services.mgpp;

import fr.dila.solonepg.ui.bean.MgppDossierCommunicationConsultationFiche;
import fr.dila.solonepg.ui.th.bean.MgppRepresentantTableForm;
import fr.dila.st.ui.th.model.SpecificContext;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface MgppFicheUIService {
    boolean isFicheLoiVisible(SpecificContext context);

    MgppDossierCommunicationConsultationFiche remplirFicheDossier(SpecificContext context);

    MgppDossierCommunicationConsultationFiche getFicheRemplie(SpecificContext context);

    DocumentModel getFicheForDossier(SpecificContext context);

    DocumentModel saveFiche(SpecificContext context);

    String getIdDossierFromFiche(SpecificContext context);

    MgppRepresentantTableForm getRepresentantTable(SpecificContext context);

    String saveRepresentant(SpecificContext context);

    boolean isFicheSupprimable(SpecificContext context);

    void diffuserFiche(SpecificContext context);

    void annulerDiffusionFiche(SpecificContext context);
}
