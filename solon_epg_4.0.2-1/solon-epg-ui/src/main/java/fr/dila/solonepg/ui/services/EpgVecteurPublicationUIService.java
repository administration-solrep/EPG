package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.dossier.vecteurpublication.AdminPublicationMinisterielleDTO;
import fr.dila.solonepg.ui.bean.dossier.vecteurpublication.AdminVecteurPublicationDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface EpgVecteurPublicationUIService {
    void createPublicationMinisterielle(SpecificContext context);

    void deleteBulletinOfficiel(SpecificContext context);

    List<AdminPublicationMinisterielleDTO> getBulletinsOfficiels(SpecificContext context);

    void addVecteur(SpecificContext context);

    void editVecteur(SpecificContext context);

    List<AdminVecteurPublicationDTO> getVecteurPublications(SpecificContext context);
}
