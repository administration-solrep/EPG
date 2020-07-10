package fr.dila.solonepg.api.cases;

import java.util.Calendar;
import java.util.List;

/**
 * Interface pour les Habilitation de l'activite normative.
 * 
 * @author asatre
 * 
 */
public interface Habilitation extends TexteMaitre {
	
    String getArticle();
    void setArticle(String article);

    String getObjetRIM();
    void setObjetRIM(String objetRIM);

    String getTypeHabilitation();
    void setTypeHabilitation(String typeHabilitation);
    
    String getConvention();
    void setConvention(String convention);
    
    Calendar getDateTerme();
    void setDateTerme(Calendar dateTerme);
    
    String getConventionDepot();
    void setConventionDepot(String conventionDepot);
    
    Calendar getDatePrevisionnelleSaisineCE();
    void setDatePrevisionnelleSaisineCE(Calendar datePrevisionnelleSaisineCE);
    
    Calendar getDatePrevisionnelleCM();
    void setDatePrevisionnelleCM(Calendar datePrevisionnelleCM);
    
    String getLegislature();
    void setLegislature(String legislature);
    
    List<String> getOrdonnanceIds();
    void setOrdonnanceIds(List<String> ordonnanceIds);
    
    List<String> getOrdonnanceIdsInvalidated();
    void setOrdonnanceIdsInvalidated(List<String> ordonnanceIds);
}
