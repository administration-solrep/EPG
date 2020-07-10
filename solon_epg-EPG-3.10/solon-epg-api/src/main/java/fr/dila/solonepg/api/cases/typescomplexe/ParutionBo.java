/**
 * 
 */
package fr.dila.solonepg.api.cases.typescomplexe;

import java.util.Calendar;

import fr.dila.st.api.domain.ComplexeType;

/**
 * type complexe ParutionBo.
 * 
 * @author antoine Rolin
 *
 */
public interface ParutionBo extends ComplexeType {

    /**
     * Getter numero texte parution Bulletin Officiel (BO)
     * 
     * @return String
     */
    String getNumeroTexteParutionBo();

    /**
     * Setter numero texte parution Bulletin Officiel (BO)
     * 
     * @param numeroTexteParutionBo
     */
    void setNumeroTexteParutionBo(String numeroTexteParutionBo);
    
    /**
     * Getter date parution BO
     * 
     * @return Calendar
     */
    Calendar getDateParutionBo();

    /**
     * Setter date parution BO
     * 
     * @param Calendar
     */
    void setDateParutionBo(Calendar dateParutionBo);

    /**
     * Getter Long parution BO
     * 
     * @return Long
     */
    Long getPageParutionBo();

    /**
     * Setter Long parution BO
     * 
     * @param Long
     */
    void setPageParutionBo(Long pageParutionBo);
}
