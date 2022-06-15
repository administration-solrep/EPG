package fr.dila.solonepg.ui.services.actions;

import fr.dila.solonepg.ui.bean.EpgIndexationMotCleListDTO;
import fr.dila.solonepg.ui.bean.EpgIndexationRubriqueDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface EpgGestionIndexationActionService {
    List<EpgIndexationMotCleListDTO> getListIndexationMotCle(SpecificContext context);

    List<EpgIndexationRubriqueDTO> getListIndexationRubrique(SpecificContext context);

    EpgIndexationMotCleListDTO getMotCleList(SpecificContext context);

    void changeMotCleList(SpecificContext context);

    void addRubrique(SpecificContext context);

    void addMotCleList(SpecificContext context);

    void deleteIndexation(SpecificContext context);

    void addMotCle(SpecificContext context);

    void removeMotCle(SpecificContext context);
}
