package fr.dila.solonepg.core.recherche.dossier.traitement;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.st.api.recherche.RequeteTraitement;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * Recherche sur l'indexation : par défaut on recherche tous les dossiers dont le terme d'indexation renseignée dans l'écran contient l'expression saisie.
 *
 * @author jgomez
 *
 */
public class EPGIndexationTraitement implements RequeteTraitement<RequeteDossierSimple> {

    @Override
    public void doBeforeQuery(RequeteDossierSimple requete) {
        //terme d'indexation Rubriques
        if (StringUtils.isEmpty(requete.getIndexationRubrique())) {
            requete.setIndexationRubriqueModified(null);
        } else {
            requete.setIndexationRubriqueModified(("%" + requete.getIndexationRubrique() + "%"));
        }
        //terme d'indexation Mots-clés
        if (StringUtils.isEmpty(requete.getIndexationMotsClefs())) {
            requete.setIndexationMotsClefsModified(null);
        } else {
            requete.setIndexationMotsClefsModified("%" + requete.getIndexationMotsClefs() + "%");
        }
        //terme d'indexation Champ Libre
        if (StringUtils.isEmpty(requete.getIndexationChampLibre())) {
            requete.setIndexationChampLibreModified(null);
        } else {
            requete.setIndexationChampLibreModified("%" + requete.getIndexationChampLibre() + "%");
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
