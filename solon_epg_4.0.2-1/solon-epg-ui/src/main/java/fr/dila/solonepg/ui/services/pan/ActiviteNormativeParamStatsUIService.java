package fr.dila.solonepg.ui.services.pan;

import fr.dila.solonepg.ui.bean.pan.PanTreeNode;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public interface ActiviteNormativeParamStatsUIService {
    List<PanTreeNode> getParamStatsTreeNodes();

    void validerParamStats(SpecificContext context);

    /**
     * Permet de ne pas afficher la pop up de paramétrage si un document et ouvert
     * et verrouillé par l'utilisateur courant
     *
     * @return true si un document est ouvert et verrouillé
     */
    boolean isThereAnOpenedAndLockedDocument(SpecificContext context);

    boolean isEspaceActiviteNormativeParametrageLegislatureReader(SpecificContext context);

    /*
     * Méthodes du wigdet template de la liste de legislatures
     */
    String addLegislature(SpecificContext context);

    String modifLegislature(SpecificContext context);

    List<String> getLegislatures(SpecificContext context);
}
