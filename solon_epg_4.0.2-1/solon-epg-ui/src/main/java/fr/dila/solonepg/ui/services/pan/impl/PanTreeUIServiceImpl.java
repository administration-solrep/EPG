package fr.dila.solonepg.ui.services.pan.impl;

import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.ui.bean.pan.PanTreeNode;
import fr.dila.solonepg.ui.services.pan.PanTreeUIService;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

public class PanTreeUIServiceImpl implements PanTreeUIService {

    @Override
    public List<PanTreeNode> getEspaceActiviteNormative(SpecificContext context) {
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();

        List<PanTreeNode> rootNodes = new ArrayList<>();
        Boolean hasDroitApplicationDeslois = false;
        Boolean hasDroitApplicationDesOrdonnances = false;
        for (ActiviteNormativeEnum espaceANEnum : ActiviteNormativeEnum.values()) {
            if (espaceANEnum.getRight() == null || ssPrincipal.isMemberOf(espaceANEnum.getRight())) {
                // Si utilisateur ministère et qu'on a déjà ajouté les liens pour profil du PAN
                // On n'ajoute pas à nouveau les liens pour profil ministère
                // Car sinon liens en double
                if (
                    (
                        espaceANEnum.equals(ActiviteNormativeEnum.APPLICATION_DES_LOIS_MINISTERE) &&
                        hasDroitApplicationDeslois
                    ) ||
                    (
                        espaceANEnum.equals(ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES_MINISTERE) &&
                        hasDroitApplicationDesOrdonnances
                    )
                ) {
                    continue;
                }
                if (espaceANEnum.equals(ActiviteNormativeEnum.APPLICATION_DES_LOIS)) {
                    hasDroitApplicationDeslois = true;
                } else if (espaceANEnum.equals(ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES)) {
                    hasDroitApplicationDesOrdonnances = true;
                }
                String label = ResourceHelper.getString(espaceANEnum.getLabel());
                PanTreeNode treeNode = new PanTreeNode(label, espaceANEnum.getId(), espaceANEnum);
                treeNode.setOpened(true);
                rootNodes.add(treeNode);
            }
        }

        return rootNodes;
    }
}
