package fr.dila.solonepg.ui.bean.pan;

import fr.dila.solonepg.core.dto.activitenormative.LoiDeRatificationDTOImpl;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public class LoisDeRatificationPanUnsortedList extends AbstractPanUnsortedList<LoiDeRatificationDTOImpl> {

    public LoisDeRatificationPanUnsortedList(SpecificContext context, List<LoiDeRatificationDTOImpl> dtoList) {
        super(context, dtoList);
    }

    @Override
    public void buildColonnes() {
        getListeColonnes().add(buildColonne("pan.ratification.loiRatification.dateLimiteDepot", null, null, true));
        getListeColonnes().add(buildColonne("pan.application.lois.table.lois.header.label.nor", null, null, true));
        getListeColonnes().add(buildColonne("pan.application.lois.table.lois.header.label.titre", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.lois.table.decrets.header.label.dateSaisineCE", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.lois.table.decrets.header.label.dateExamenCE", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.lois.table.decrets.header.label.sectionCE", null, null, true));
        getListeColonnes().add(buildColonne("pan.ratification.loiRatification.dateExamenCM", null, null, true));
        getListeColonnes().add(buildColonne("pan.ratification.loiRatification.chambreDepot", null, null, true));
        getListeColonnes().add(buildColonne("pan.ratification.loiRatification.dateDepot", null, null, true));
        getListeColonnes().add(buildColonne("pan.ratification.loiRatification.numeroDepot", null, null, true));
        getListeColonnes().add(buildColonne("pan.application.lois.table.lois.header.label.titre", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.datePublication", null, null, true));
    }
}
