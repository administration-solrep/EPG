package fr.dila.solonepg.ui.services.pan;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.ui.bean.pan.ReportResultatData;
import fr.dila.st.ui.th.model.SpecificContext;

public interface ActiviteNormativeStatsUIService {
    /**
     * initialiser le contenu du resulat du rapport
     */
    ReportResultatData initReportResultatData(SpecificContext context);

    boolean isBilanSemestriel(SpecificContext context);

    /**
     * refresh le resultat du rapport birt
     */
    String refreshBirtResultat(SpecificContext context);

    boolean canBeRefresh(SpecificContext context);

    String getHorodatageRequest(SpecificContext context);

    /**
     * tester s'il faut afficher le lien (masquer ou afficher les decret publics)
     */
    boolean isDisplayLienPubliee(SpecificContext context);

    boolean isActiviteNormativeUpdater(SpecificContext context);

    boolean isActiviteNormativeReader(SpecificContext context);

    int getAnneeDeDepart(SpecificContext context);

    ParametrageAN getParameterStatsDoc(SpecificContext context);
}
