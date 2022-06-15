package fr.dila.solonepg.ui.bean.pan;

import fr.dila.solonepg.core.dto.activitenormative.TexteTranspositionDTOImpl;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public class TextesTranspositionsPanUnsortedList extends AbstractPanUnsortedList<TexteTranspositionDTOImpl> {

    public TextesTranspositionsPanUnsortedList(SpecificContext context, List<TexteTranspositionDTOImpl> dtoList) {
        super(context, dtoList);
    }

    @Override
    public void buildColonnes() {
        getListeColonnes().add(buildColonne("pan.directives.textes.table.header.label.nature", null, null, true));
        getListeColonnes().add(buildColonne("pan.application.lois.table.lois.header.label.nor", null, null, true));
        getListeColonnes().add(buildColonne("pan.application.lois.table.lois.header.label.intitule", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.ministerePilote", null, null, true));
        getListeColonnes().add(buildColonne("pan.directives.textes.table.header.label.etapeSolon", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.lois.table.lois.header.label.datePublication", null, null, true));

        getListeColonnes()
            .add(buildColonne("pan.directives.textes.table.header.label.numeroTextePublie", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.directives.textes.table.header.label.titreTextePublie", null, null, true));
    }
}
