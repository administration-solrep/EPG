package fr.dila.solonepg.core.spe;

import java.io.Serializable;
import java.util.Comparator;

import fr.sword.xsd.solon.epg.Fichier;

/**
 * Comparateur de Fichier
 * 
 * @author arolin
 */
public class FichierComparator implements Serializable, Comparator<Fichier> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public int compare(Fichier obj1, Fichier obj2) {
        if(obj1 == null){
            return 1;
        }
        if(obj2 == null){
            return -1;
        }
        if(obj1.getOrdre() == null){
            return 1;
        }
        if(obj2.getOrdre() == null){
            return -1;
        }
        return (obj1.getOrdre()).compareTo(obj2.getOrdre());
    }
}
