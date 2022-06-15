package fr.dila.solonepg.ui.bean;

import fr.dila.ss.ui.bean.FdrDTO;
import fr.dila.ss.ui.enums.LabelColonne;
import fr.dila.st.ui.bean.ColonneInfo;

public class EpgFdrDTO extends FdrDTO {

    public EpgFdrDTO() {}

    public void buildColonnesFdrSquelette() {
        listeColonnes.clear();

        listeColonnes.add(new ColonneInfo(LabelColonne.TYPE_DESTINATAIRE.getLabel(), false, true, false, true));
        listeColonnes.add(new ColonneInfo(LabelColonne.POSTE.getLabel(), false, true, false, true));
        listeColonnes.add(new ColonneInfo(LabelColonne.ACTION.getLabel(), false, true, false, true));
        listeColonnes.add(new ColonneInfo(LabelColonne.OBLIGATOIRE.getLabel(), false, true, false, true));
        listeColonnes.add(new ColonneInfo(LabelColonne.ACTIONS.getLabel(), false, true, false, false));
    }
}
