package fr.dila.solonepg.core.recherche.dossier.traitement;

import java.util.ArrayList;
import java.util.List;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.st.api.recherche.RequeteTraitement;

/**
 * Classe pour le traitement des index full text.
 * On prend le texte entré par l'utlisateur et on construit une requête avec les index fulltext que
 * celui-ci a demandé dans la sélection.
 * @author jgomez
 *
 */
public class EPGGeneralRequeteTraitement implements RequeteTraitement<RequeteDossierSimple>{


    private final List<RequeteTraitement<RequeteDossierSimple>> traitements;
    
    public EPGGeneralRequeteTraitement(){
        traitements = new ArrayList<RequeteTraitement<RequeteDossierSimple>>();
        traitements.add(new NumeroNorTraitement());
        traitements.add(new EPGTexteIntegralTraitement());
        traitements.add(new EPGStatutsArchivageTraitement());
        traitements.add(new EPGConversionPosteIdMailboxIdTraitement());
        traitements.add(new EPGEtapeValidationStatutTraitement());
        traitements.add(new EPGCommentTraitement());
        traitements.add(new EPGTitreActeTraitement());
        traitements.add(new EPGIndexationTraitement());
        traitements.add(new EPGApplicationLoiRefsTraitement());
        traitements.add(new EPGApplicationOrdonnanceRefsTraitement());
        traitements.add(new EPGTranspositionDirectiveTraitement());
        traitements.add(new EPGInclusionDateTraitement());
    }
    
    public void init(RequeteDossierSimple requete) {
        for (RequeteTraitement<RequeteDossierSimple> traitement: traitements){
            traitement.init(requete);
        }
    }

    @Override
    public void doBeforeQuery(RequeteDossierSimple requete) {
        for (RequeteTraitement<RequeteDossierSimple> traitement: traitements){
            traitement.doBeforeQuery(requete);
        }
    }

    @Override
    public void doAfterQuery(RequeteDossierSimple requete) {
    }
    
}
