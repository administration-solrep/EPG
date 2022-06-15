package fr.dila.solonepg.ui.bean.pan;

import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreTraiteDTOImpl;
import fr.dila.solonepg.ui.enums.pan.PanColumnEnum;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import java.util.stream.Stream;

public class TraitesListPan extends AbstractPanSortedList<TexteMaitreTraiteDTOImpl> {

    public TraitesListPan(SpecificContext context, List<TexteMaitreTraiteDTOImpl> dtoList) {
        super(context, dtoList);
    }

    @Override
    public void buildColonnes(LoisSuiviesForm form) {
        Stream
            .of(
                PanColumnEnum.CATEGORIE,
                PanColumnEnum.PAYS,
                PanColumnEnum.THEMATIQUE,
                PanColumnEnum.INTITULE,
                PanColumnEnum.DATE_SIGNATURE,
                PanColumnEnum.DATE_ENTREE_EN_VIGUEUR,
                PanColumnEnum.OBSERVATION
            )
            .forEach(panColumneEnum -> addColonne(panColumneEnum, form));
    }
}
