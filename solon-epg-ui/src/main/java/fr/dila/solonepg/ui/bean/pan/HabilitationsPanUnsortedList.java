package fr.dila.solonepg.ui.bean.pan;

import fr.dila.solonepg.core.dto.activitenormative.HabilitationDTOImpl;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public class HabilitationsPanUnsortedList extends AbstractPanUnsortedList<HabilitationDTOImpl> {

    public HabilitationsPanUnsortedList(SpecificContext context, List<HabilitationDTOImpl> dtoList) {
        super(context, dtoList);
    }

    @Override
    public void buildColonnes() {
        getListeColonnes().add(buildColonne("pan.application.table.mesures.header.label.ordre", null, null, true));

        getListeColonnes().add(buildColonne("pan.application.table.mesures.header.label.articleLoi", null, null, true));

        getListeColonnes().add(buildColonne("pan.application.table.mesures.header.label.objet", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.typeHabilitation", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.ministerePilote", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.termeHabilitation", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.dateTerme", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.depotRatification", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.datePreviSaisineCE", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.datePreviCM", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.observations", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.legislature", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.codification", null, null, true));
    }
}
