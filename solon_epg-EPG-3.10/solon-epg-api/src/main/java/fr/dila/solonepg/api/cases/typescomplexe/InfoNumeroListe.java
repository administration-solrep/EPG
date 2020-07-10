/**
 * 
 */
package fr.dila.solonepg.api.cases.typescomplexe;

import java.util.Calendar;

import fr.dila.st.api.domain.ComplexeType;

/**
 * type complexe Info Numero Liste.
 * 
 * @author antoine Rolin
 *
 */
public interface InfoNumeroListe extends ComplexeType {

    /**
     * get date de génération de la Liste.
     * 
     * @return String
     */
    Calendar getDateGenerationListe();
    void setDateGenerationListe(Calendar dateGenerationListe);
    
    /**
     * get numero Liste.
     * 
     * @return String
     */
    String getNumeroListe();
    void setNumeroListe(String numeroListe);
}
