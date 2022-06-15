package fr.dila.solonepg.ui.bean.pan;

import fr.dila.solonepg.core.dto.activitenormative.DecretDTOImpl;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public class DecretsPanUnsortedList extends AbstractPanUnsortedList<DecretDTOImpl> {

    public DecretsPanUnsortedList(SpecificContext context, List<DecretDTOImpl> dtoList) {
        super(context, dtoList);
    }

    public DecretsPanUnsortedList() {
        super();
    }

    @Override
    public void buildColonnes() {
        getListeColonnes().clear();

        getListeColonnes().add(buildColonne("pan.application.lois.table.decrets.header.label.nor", null, null, true));
        getListeColonnes().add(buildColonne("pan.application.lois.table.decrets.header.label.titre", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.lois.table.decrets.header.label.categorie", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.lois.table.decrets.header.label.sectionCE", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.lois.table.decrets.header.label.dateSaisineCE", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.lois.table.decrets.header.label.dateExamenCE", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.lois.table.decrets.header.label.rapporteurCE", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.lois.table.decrets.header.label.refAvisCE", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.lois.table.decrets.header.label.numeroTexte", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.lois.table.decrets.header.label.dateSignature", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.lois.table.decrets.header.label.datePublication", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.lois.table.decrets.header.label.delaiPublication", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.lois.table.decrets.header.label.numeroJO", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.lois.table.decrets.header.label.numeroPage", null, null, true));
    }
}
