package fr.dila.solonepg.core.recherche.dossier.traitement;

import org.apache.commons.lang.StringUtils;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.solonepg.core.util.FullTextUtil;
import fr.dila.st.api.recherche.RequeteTraitement;

public class EPGTranspositionDirectiveTraitement implements RequeteTraitement<RequeteDossierSimple> {

    @Override
    public void doBeforeQuery(RequeteDossierSimple requete) {
        if (requete.getTranspositionDirective()== null || StringUtils.isEmpty(requete.getTranspositionDirective())){
            requete.setTranspositionDirective(null);
            return;
        }
        else{
            requete.setTranspositionDirective(FullTextUtil.replaceStarByPercent(requete.getTranspositionDirective().toUpperCase()));
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
