package fr.dila.solonepg.api.cases;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import fr.dila.solonepg.api.cases.typescomplexe.ParutionBo;
import fr.dila.st.api.domain.STDomainObject;

/**
 * Interface pour les retour DILA.
 * 
 * @author asatre
 * 
 */
public interface RetourDila extends STDomainObject, Serializable {

    /**
     * note :Champ technique pour savoir si la première demande "Pour Epreuvage / Pour Publication" a déjà été envoyée
     * 
     * @return vrai si la première demande "Pour Epreuvage / Pour Publication" a déjà été envoyée
     */
    Boolean getIsPublicationEpreuvageDemandeSuivante();
    void setIsPublicationEpreuvageDemandeSuivante(Boolean isPublicationEpreuvageDemandeSuivante);
    
    Calendar getDateParutionJorf();
    void setDateParutionJorf(Calendar dateParutionJorf);

    String getNumeroTexteParutionJorf();
    void setNumeroTexteParutionJorf(String numeroTexteParutionJorf);

    Long getPageParutionJorf();
    void setPageParutionJorf(Long pageParutionJorf);

    String getModeParution();
    void setModeParution(String modeParution);
    
    Calendar getDatePromulgation();
    void setDatePromulgation(Calendar datePromulgation);
    
    String getTitreOfficiel();
    void setTitreOfficiel(String titreOfficiel);
    
    String getLegislaturePublication();
    void setLegislaturePublication(String legislaturePublication);

    /**
     * Getter Parutions BO (il peut y avoir plusieurs publications BO par Dossier)  
     * 
     * @return List<ParutionBo>
     */
    List<ParutionBo> getParutionBo();

    /**
     * Setter Parutions BO
     * 
     * @param List<ParutionBo>
     */
    void setParutionBo(List<ParutionBo> parutionBoList);
}
