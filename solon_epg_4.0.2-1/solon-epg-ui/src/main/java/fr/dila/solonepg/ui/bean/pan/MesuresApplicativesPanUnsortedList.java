package fr.dila.solonepg.ui.bean.pan;

import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.core.dto.activitenormative.MesureApplicativeDTOImpl;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class MesuresApplicativesPanUnsortedList extends AbstractPanUnsortedList<MesureApplicativeDTOImpl> {

    public MesuresApplicativesPanUnsortedList(SpecificContext context, List<MesureApplicativeDTOImpl> dtoList) {
        super(context, dtoList);
    }

    public MesuresApplicativesPanUnsortedList() {
        super();
    }

    public MesureColonneInfo buildColonne(
        String label,
        String userColumn,
        List<String> lstUserVisibleColumns,
        boolean labelVisible,
        boolean hideable
    ) {
        return new MesureColonneInfo(buildColonne(label, userColumn, lstUserVisibleColumns, labelVisible), hideable);
    }

    @Override
    public void buildColonnes() {
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.ordre", null, null, true, false));

        if (StringUtils.equals(currentSection, ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId())) {
            getListeColonnes()
                .add(buildColonne("pan.application.table.mesures.header.label.articleLoi", null, null, true, false));
        } else {
            getListeColonnes()
                .add(
                    buildColonne(
                        "pan.application.ordonnances.table.mesures.header.label.articleOrdonnance",
                        null,
                        null,
                        true,
                        false
                    )
                );
        }

        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.baseLegale", null, null, true, false));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.objet", null, null, true, false));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.ministerePilote", null, null, true, false));
        getListeColonnes()
            .add(
                buildColonne("pan.application.table.mesures.header.label.directionResponsable", null, null, true, false)
            );
        getListeColonnes()
            .add(
                buildColonne("pan.application.table.mesures.header.label.consultationHorsCE", null, null, true, false)
            );
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.calendrierHorsCE", null, null, true, false));
        getListeColonnes()
            .add(
                buildColonne("pan.application.table.mesures.header.label.datePreviSaisineCE", null, null, true, false)
            );
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.objectifPubli", null, null, true, false));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.observations", null, null, true, false));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.typeMesure", null, null, true, false));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.dateApplication", null, null, true, false));
        getListeColonnes()
            .add(
                buildColonne(
                    "pan.application.table.mesures.header.label.dateCertaineEntreeVigueur",
                    null,
                    null,
                    true,
                    false
                )
            );
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.dateLimiteSixMois", null, null, true, false));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.codeModifie", null, null, true, true));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.poleChargeMission", null, null, true, true));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.amendement", null, null, true, true));
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.datePassageCM", null, null, true, true));
        getListeColonnes()
            .add(
                buildColonne("pan.application.table.mesures.header.label.responsableAmendement", null, null, true, true)
            );
        getListeColonnes()
            .add(
                buildColonne(
                    "pan.application.table.mesures.header.label.numeroQuestionParlementaire",
                    null,
                    null,
                    true,
                    true
                )
            );
        getListeColonnes()
            .add(buildColonne("pan.application.table.mesures.header.label.champLibre", null, null, true, true));
        getListeColonnes()
            .add(
                buildColonne(
                    "pan.application.table.mesures.header.label.communicationMinisterielle",
                    null,
                    null,
                    true,
                    true
                )
            );
    }
}
