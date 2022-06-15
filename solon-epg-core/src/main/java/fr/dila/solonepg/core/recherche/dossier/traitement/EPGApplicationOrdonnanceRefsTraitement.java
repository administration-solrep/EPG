package fr.dila.solonepg.core.recherche.dossier.traitement;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.solonepg.core.util.FullTextUtil;
import fr.dila.st.api.recherche.RequeteTraitement;
import org.apache.commons.lang3.StringUtils;

public class EPGApplicationOrdonnanceRefsTraitement implements RequeteTraitement<RequeteDossierSimple> {

    @Override
    public void doBeforeQuery(RequeteDossierSimple requete) {
        if (requete.getTranspositionOrdonnance() == null || StringUtils.isEmpty(requete.getTranspositionOrdonnance())) {
            requete.setTranspositionOrdonnance(null);
            return;
        } else {
            requete.setTranspositionOrdonnance(
                FullTextUtil.replaceStarByPercent(requete.getTranspositionOrdonnance().toUpperCase())
            );
        }
    }

    @Override
    public void init(RequeteDossierSimple requete) {
        return;
    }

    @Override
    public void doAfterQuery(RequeteDossierSimple requete) {
        return;
    }
}
