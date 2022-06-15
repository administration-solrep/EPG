package fr.dila.solonepg.ui.services;

import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface EpgFavorisConsultationUIService {
    List<String> getFavorisConsultationDossiersId(SpecificContext context);
    List<String> getFavorisConsultationFdrsId(SpecificContext context);
    List<String> getFavorisConsultationUsers(SpecificContext context);
    void removeFavorisConsultation(SpecificContext context);
    void removeFavorisConsultationUser(SpecificContext context);
    boolean addDossiersToFavorisConsultation(SpecificContext context);
    boolean addUtilisateursToFavorisConsultation(SpecificContext context);
    boolean addFDRToFavorisConsultation(SpecificContext context);
}
