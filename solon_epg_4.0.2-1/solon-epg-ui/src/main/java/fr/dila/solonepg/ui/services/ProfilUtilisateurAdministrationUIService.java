package fr.dila.solonepg.ui.services;

import fr.dila.st.ui.th.model.SpecificContext;

public interface ProfilUtilisateurAdministrationUIService {
    void addTypeActe(SpecificContext context, String typeActe);

    void deleteTypeActe(SpecificContext context, String idVocabulaire);

    boolean isVisibleMesureNominative(SpecificContext context);
}
