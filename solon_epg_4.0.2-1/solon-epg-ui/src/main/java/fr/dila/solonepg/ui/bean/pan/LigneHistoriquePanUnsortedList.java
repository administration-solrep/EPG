package fr.dila.solonepg.ui.bean.pan;

import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;

public class LigneHistoriquePanUnsortedList extends AbstractPanUnsortedList<LigneHistoriqueMAJMinisterielleDTO> {

    public LigneHistoriquePanUnsortedList(SpecificContext context, List<LigneHistoriqueMAJMinisterielleDTO> dtoList) {
        super(context, dtoList);
    }

    public LigneHistoriquePanUnsortedList() {
        super();
    }

    @Override
    public void buildColonnes() {
        getListeColonnes().clear();

        getListeColonnes().add(buildColonne("pan.application.historiqueMaj.header.label.dateMaj", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.historiqueMaj.header.label.modification", null, null, true));
        getListeColonnes().add(buildColonne("pan.application.historiqueMaj.header.label.numeroNor", null, null, true));
        getListeColonnes().add(buildColonne("pan.application.historiqueMaj.header.label.reference", null, null, true));
        getListeColonnes().add(buildColonne("pan.application.historiqueMaj.header.label.titre", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.historiqueMaj.header.label.numeroArticles", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.historiqueMaj.header.label.numeroOrdre", null, null, true));
        getListeColonnes()
            .add(buildColonne("pan.application.historiqueMaj.header.label.commentaire", null, null, true));
    }
}
