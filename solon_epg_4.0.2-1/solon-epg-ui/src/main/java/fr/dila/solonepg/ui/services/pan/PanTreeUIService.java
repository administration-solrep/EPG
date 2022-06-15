package fr.dila.solonepg.ui.services.pan;

import fr.dila.solonepg.ui.bean.pan.PanTreeNode;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

/**
 * Classe de gestion du menu de l'acitivite normative.
 *
 * @author asatre
 */
public interface PanTreeUIService {
    List<PanTreeNode> getEspaceActiviteNormative(SpecificContext context);
}
