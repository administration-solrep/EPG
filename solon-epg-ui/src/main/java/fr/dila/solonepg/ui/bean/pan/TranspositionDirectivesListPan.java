package fr.dila.solonepg.ui.bean.pan;

import fr.dila.solonepg.core.dto.activitenormative.TranspositionDirectiveDTOImpl;
import fr.dila.solonepg.ui.enums.pan.PanColumnEnum;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import java.util.stream.Stream;

public class TranspositionDirectivesListPan extends AbstractPanSortedList<TranspositionDirectiveDTOImpl> {

    public TranspositionDirectivesListPan(SpecificContext context, List<TranspositionDirectiveDTOImpl> dtoList) {
        super(context, dtoList);
    }

    @Override
    public void buildColonnes(LoisSuiviesForm form) {
        Stream
            .of(
                PanColumnEnum.NUMERO,
                PanColumnEnum.DATE_ADOPTION,
                PanColumnEnum.MINISTERE_PILOTE,
                PanColumnEnum.TITRE_DIRECTIVE,
                PanColumnEnum.DATE_ECHEANCE,
                PanColumnEnum.DROIT_CONFORME,
                PanColumnEnum.DATE_PROCHAIN_TAB_AFFICHAGE,
                PanColumnEnum.ACHEVEE,
                PanColumnEnum.OBSERVATION
            )
            .forEach(panColumneEnum -> addColonne(panColumneEnum, form));
    }
}
