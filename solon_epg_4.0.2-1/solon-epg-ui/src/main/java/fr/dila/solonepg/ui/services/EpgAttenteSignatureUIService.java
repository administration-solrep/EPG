package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.AttenteSignatureDTO;
import fr.dila.solonepg.ui.bean.AttenteSignatureList;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;

public interface EpgAttenteSignatureUIService {
    void ajouterDossierDansAttenteSignature(SpecificContext context);

    AttenteSignatureList getTextesEnAttenteSignature(SpecificContext context);

    AttenteSignatureDTO getAttenteSignatureDTOFromDossier(SpecificContext context);

    void saveTexteAttenteSignature(SpecificContext context);

    void retirerTextesAttenteSignature(SpecificContext context);

    File exportTextesAttenteSignature(SpecificContext context);
}
