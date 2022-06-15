package fr.dila.solonepg.core.recherche.dossier.traitement;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.st.api.recherche.RequeteTraitement;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * Recherche sur le titre de l'acte.
 * @author jgomez
 *
 */
public class EPGTitreActeTraitement implements RequeteTraitement<RequeteDossierSimple> {

    @Override
    public void doBeforeQuery(RequeteDossierSimple requete) {
        String titreActe = requete.getTitreActe();
        if (titreActe == null || StringUtils.isEmpty(titreActe)) {
            requete.setTitreActeModified(null);
            return;
        } else {
            requete.setTitreActeModified("%" + titreActe + "%");
        }
    }

    @Override
    public void doAfterQuery(RequeteDossierSimple requeteSimple) {
        return;
    }

    @Override
    public void init(RequeteDossierSimple requeteSimple) {
        return;
    }
}
