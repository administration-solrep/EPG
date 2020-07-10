package fr.dila.solonepg.core.recherche.dossier.traitement;

import org.apache.commons.lang.StringUtils;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.st.api.recherche.RequeteTraitement;

/**
 * 
 * Crée le moyen de requêter sur un commentaire d'étape.
 * @author jgomez
 *
 */
public class EPGCommentTraitement implements RequeteTraitement<RequeteDossierSimple>{

    @Override
    public void doBeforeQuery(RequeteDossierSimple requete) {
        if (requete.getEtapeNote() == null || StringUtils.isEmpty(requete.getEtapeNote())){
            requete.setEtapeNoteModified(null);
            return;
        }
        else{
            requete.setEtapeNoteModified("%" + requete.getEtapeNote() + "%");
        }
    }

    @Override
    public void doAfterQuery(RequeteDossierSimple requeteSimple) {
        return ;
    }

    @Override
    public void init(RequeteDossierSimple requeteSimple) {
        return ;
    }

}