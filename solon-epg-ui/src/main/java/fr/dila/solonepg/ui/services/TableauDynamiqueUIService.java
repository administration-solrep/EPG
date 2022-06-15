package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.EpgDossierList;
import fr.dila.solonepg.ui.bean.TableauxDynamiquesDTO;
import fr.dila.solonepg.ui.th.bean.TableauDynamiqueForm;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

public interface TableauDynamiqueUIService {
    TableauDynamiqueForm getTableauDynamiqueForm(SpecificContext context);

    void createOrSaveTableauDynamique(SpecificContext context);

    void deleteTableauDynamique(SpecificContext context);

    TableauxDynamiquesDTO getTableauxDynamiqueDTO(SpecificContext context);

    EpgDossierList getDossierListForTableauxDynamique(SpecificContext context);

    List<TableauxDynamiquesDTO> getListTableauxDynamiqueDTO(SpecificContext context);

    boolean isDirectionViewer(NuxeoPrincipal ssPrincipal);

    /**
     * @return true si l'utilisateur courant a un nombre de tableaux dynamiques
     *         inférieur au nombre maximal autorisé.
     */
    boolean canAddNewTableauDynamique(SpecificContext context);
}
