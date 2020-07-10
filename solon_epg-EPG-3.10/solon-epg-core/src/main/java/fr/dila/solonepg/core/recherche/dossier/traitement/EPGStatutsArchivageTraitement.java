package fr.dila.solonepg.core.recherche.dossier.traitement;

import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.recherche.dossier.RequeteDossier;
import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.st.api.recherche.RequeteTraitement;

/**
 * 
 * Met à jour le champ qui contient la liste des statuts d'archivage de dossier sur laquelle porte la recherche.
 * (Base de production ou base d'archivage)
 * @author jgomez
 *
 */
public class EPGStatutsArchivageTraitement implements RequeteTraitement<RequeteDossierSimple>{

    @Override
    public void doBeforeQuery(RequeteDossierSimple requeteSimple) {
        RequeteDossier requeteDossier = requeteSimple.getDocument().getAdapter(RequeteDossier.class);
        if (requeteDossier.getDansBaseProduction()){
            requeteSimple.setIdStatutArchivageDossier(VocabularyConstants.ARCHIVE_AUCUN_ET_CANDIDAT_INTERMEDIAIRE);
        }
        if (requeteDossier.getDansBaseArchivageIntermediaire()){
            requeteSimple.setIdStatutArchivageDossier(VocabularyConstants.ARCHIVE_INTERMEDIAIRE_ET_CANDIDAT_DEFINITIF);
        }
        //Recherche dans toutes les bases, plus besoin de contrainte
        if (requeteDossier.getDansBaseArchivageIntermediaire() && requeteDossier.getDansBaseProduction()){
            requeteSimple.setIdStatutArchivageDossier(null);
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