package fr.dila.solonepg.ui.bean.pan;

import fr.dila.solonepg.core.dto.activitenormative.OrdonnanceHabilitationDTOImpl;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public class OrdonnancesHabilitationsPanUnsortedList extends AbstractPanUnsortedList<OrdonnanceHabilitationDTOImpl> {

    public OrdonnancesHabilitationsPanUnsortedList(
        SpecificContext context,
        List<OrdonnanceHabilitationDTOImpl> dtoList
    ) {
        super(context, dtoList);
    }

    @Override
    public void buildColonnes() {
        getListeColonnes().add(buildColonne("pan.application.lois.table.lois.header.label.nor", null, null, true));
        getListeColonnes().add(buildColonne("pan.application.table.mesures.header.label.objet", null, null, true));

        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.ministerePilote", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.legislature", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.dateSaisineCE", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.datePassageCM", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.datePublication", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.titreOfficiel", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.numero", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.depotRatification", null, null, true));
        getListeColonnes().add(buildColonne("pan.habilitations.dateLimiteDepot", null, null, true));
    }
}
