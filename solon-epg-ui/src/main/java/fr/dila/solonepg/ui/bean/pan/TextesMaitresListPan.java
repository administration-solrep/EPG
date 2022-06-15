package fr.dila.solonepg.ui.bean.pan;

import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.CHAMP_LIBRE;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.COMMISSION_AN;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.COMMISSION_SENAT;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.DATE_PUBLICATION;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.FONDEMENT_CONSTITUTIONNEL;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.LEGISLATURE_PUBLICATION;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.MINISTERE_PILOTE;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.MOT_CLE;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.NATURE_TEXTE;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.NUMERO;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.NUMERO_INTERNE;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.NUMERO_NOR;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.PROCEDURE_VOTE;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.PROMULGATION;
import static fr.dila.solonepg.ui.enums.pan.PanColumnEnum.TITRE;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreLoiDTOImpl;
import fr.dila.solonepg.ui.enums.pan.PanColumnEnum;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public class TextesMaitresListPan extends AbstractPanSortedList<TexteMaitreLoiDTOImpl> {

    public TextesMaitresListPan(SpecificContext context, List<TexteMaitreLoiDTOImpl> dtoList) {
        super(context, dtoList);
    }

    public TextesMaitresListPan() {
        super();
    }

    @Override
    public void buildColonnes(LoisSuiviesForm form) {
        if (
            StringUtils.equals(currentTab, ActiviteNormativeConstants.TAB_AN_TABLEAU_ORDONNANCES) ||
            StringUtils.equals(currentTab, ActiviteNormativeConstants.TAB_AN_TABLEAU_LOIS)
        ) {
            Stream
                .of(
                    NUMERO_NOR,
                    TITRE,
                    NUMERO,
                    MINISTERE_PILOTE,
                    PROMULGATION,
                    DATE_PUBLICATION,
                    PROCEDURE_VOTE,
                    NATURE_TEXTE,
                    LEGISLATURE_PUBLICATION,
                    COMMISSION_AN,
                    COMMISSION_SENAT,
                    NUMERO_INTERNE,
                    MOT_CLE,
                    FONDEMENT_CONSTITUTIONNEL,
                    CHAMP_LIBRE
                )
                .forEach(panColumneEnum -> addTabOrdoLoisColonne(panColumneEnum, form));
        } else {
            Stream
                .of(
                    MINISTERE_PILOTE,
                    NUMERO_NOR,
                    NUMERO,
                    TITRE,
                    MOT_CLE,
                    PROMULGATION,
                    DATE_PUBLICATION,
                    NATURE_TEXTE,
                    LEGISLATURE_PUBLICATION,
                    FONDEMENT_CONSTITUTIONNEL
                )
                .forEach(panColumneEnum -> addElseWhere(panColumneEnum, form));
        }
    }

    private void addElseWhere(PanColumnEnum panColumnEnum, LoisSuiviesForm form) {
        if (panColumnEnum.isVisibleElseWhere(currentSection, currentTab)) {
            addColonne(panColumnEnum, form);
        }
    }

    private void addTabOrdoLoisColonne(PanColumnEnum panColumnEnum, LoisSuiviesForm form) {
        if (panColumnEnum.isVisibleInTabOrdoLois(currentSection, currentTab)) {
            addColonne(panColumnEnum, form);
        }
    }
}
