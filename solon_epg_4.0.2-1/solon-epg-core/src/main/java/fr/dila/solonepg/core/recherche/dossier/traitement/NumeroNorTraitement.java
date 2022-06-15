package fr.dila.solonepg.core.recherche.dossier.traitement;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.st.api.recherche.RequeteTraitement;
import fr.dila.st.core.util.FullTextUtil;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * Met à jour le champ numéro NOR de manière
 * à transformer la valeur de l'utilisateur en valeur utilisable
 * par le LIKE du moteur de recherche
 * @author jgomez
 *
 */
public class NumeroNorTraitement implements RequeteTraitement<RequeteDossierSimple> {

    @Override
    public void doBeforeQuery(RequeteDossierSimple requete) {
        if (requete.getNumeroNor() == null || StringUtils.isEmpty(requete.getNumeroNor())) {
            requete.setNumeroNorModified(null);
            return;
        } else {
            requete.setNumeroNorModified(FullTextUtil.replaceStarByPercent(requete.getNumeroNor().toUpperCase()));
        }
    }

    @Override
    public void doAfterQuery(RequeteDossierSimple requete) {
        return;
    }

    @Override
    public void init(RequeteDossierSimple requete) {
        return;
    }
}
