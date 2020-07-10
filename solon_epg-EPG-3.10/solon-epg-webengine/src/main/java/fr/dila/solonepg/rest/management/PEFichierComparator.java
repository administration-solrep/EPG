package fr.dila.solonepg.rest.management;

import java.io.Serializable;
import java.util.Comparator;

import fr.sword.xsd.solon.spe.PEFichier;

/**
 * Comparateur de PEFichier
 * 
 * @author arolin
 */
public class PEFichierComparator  implements Serializable, Comparator<PEFichier> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public int compare(PEFichier fichier1, PEFichier fichier2) {
        if(fichier1 == null){
            return 1;
        }
        if(fichier2 == null){
            return -1;
        }
        return (fichier1.getOrdre()).compareTo(fichier2.getOrdre());
    }
}
