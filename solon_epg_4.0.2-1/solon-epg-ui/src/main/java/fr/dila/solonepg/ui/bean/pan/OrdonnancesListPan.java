package fr.dila.solonepg.ui.bean.pan;

import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.DATE_PUBLICATION;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.FONDEMENT_CONSTITUTIONNEL;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.NUMERO;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.TITRE;

import fr.dila.solonepg.core.dto.activitenormative.OrdonnanceDTOImpl;
import fr.dila.solonepg.ui.enums.pan.PanColumnEnum;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import java.util.stream.Stream;

public class OrdonnancesListPan extends AbstractPanSortedList<OrdonnanceDTOImpl> {

    public OrdonnancesListPan(SpecificContext context, List<OrdonnanceDTOImpl> dtoList) {
        super(context, dtoList);
    }

    @Override
    public void buildColonnes(LoisSuiviesForm form) {
        Stream
            .of(
                PanColumnEnum.MINISTERE_PILOTE,
                PanColumnEnum.NUMERO_NOR,
                NUMERO,
                TITRE,
                DATE_PUBLICATION,
                PanColumnEnum.INTITULE,
                FONDEMENT_CONSTITUTIONNEL
            )
            .forEach(panColumneEnum -> addColonne(panColumneEnum, form));
    }
}
