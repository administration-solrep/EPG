package fr.dila.solonepg.core.recherche.dossier.traitement;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.st.api.recherche.RequeteTraitement;
import fr.dila.st.core.util.FullTextUtil;
import org.apache.commons.lang3.StringUtils;

public class EPGApplicationLoiRefsTraitement implements RequeteTraitement<RequeteDossierSimple> {

    @Override
    public void doBeforeQuery(RequeteDossierSimple requete) {
        if (requete.getApplicationLoi() == null || StringUtils.isEmpty(requete.getApplicationLoi())) {
            requete.setApplicationLoi(null);
            return;
        } else {
            requete.setApplicationLoi(FullTextUtil.replaceStarByPercent(requete.getApplicationLoi().toUpperCase()));
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
