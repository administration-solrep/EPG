package fr.dila.solonepg.core.recherche.dossier.traitement;

import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.st.api.recherche.RequeteTraitement;

public class EPGInclusionDateTraitement implements RequeteTraitement<RequeteDossierSimple> {

    public final static Log LOGGER = LogFactory.getLog(EPGInclusionDateTraitement.class); 
    
    @Override
    public void init(RequeteDossierSimple requete) {
     // Pour les dates de fin d'intervalle, on ajoute un jour afin d'inclure
        // le dernier jour choisi par l'utilisateur dans la recherche
        Calendar dateAct2 = requete.getEtapeDateActivation_2();
        Calendar dateValidation2 = requete.getEtapeDateValidation_2();
        Calendar datePublication2 = requete.getDatePublication_2();
        Calendar dateSignature2 = requete.getDateSignature_2();
        
        if (dateAct2 != null) {
            dateAct2.add(Calendar.HOUR_OF_DAY, 23);
            dateAct2.add(Calendar.MINUTE, 59);
            dateAct2.add(Calendar.SECOND, 59);
            requete.setEtapeDateActivation_2(dateAct2);
        }
        
        if (dateValidation2 != null) {
            dateValidation2.add(Calendar.HOUR_OF_DAY, 23);
            dateValidation2.add(Calendar.MINUTE, 59);
            dateValidation2.add(Calendar.SECOND, 59);       
            requete.setEtapeDateValidation_2(dateValidation2);
        }
        
        if (datePublication2 != null) {
            datePublication2.add(Calendar.HOUR_OF_DAY, 23);
            datePublication2.add(Calendar.MINUTE, 59);
            datePublication2.add(Calendar.SECOND, 59);         
            requete.setDatePublication_2(datePublication2);
        }
        
        if (dateSignature2 != null) {
            dateSignature2.add(Calendar.HOUR_OF_DAY, 23);
            dateSignature2.add(Calendar.MINUTE, 59);
            dateSignature2.add(Calendar.SECOND, 59);       
            requete.setDateSignature_2(dateSignature2);
        }
    }

    @Override
    public void doBeforeQuery(RequeteDossierSimple requete) {
    }

    @Override
    public void doAfterQuery(RequeteDossierSimple requete) {
    }

}
