package fr.dila.solonepg.api.caselink;

import java.io.Serializable;
import java.util.Calendar;

import fr.dila.st.api.caselink.STDossierLink;

/**
 * DossierLink Interface (herit CaseLink Interface)
 * 
 * @author arolin
 */
public interface DossierLink extends STDossierLink, Serializable {

    // /////////////////
    // Dossier Link getter/setter on property
    // ////////////////

    /**
     * Getter/setter pour statut archivage
     */
    String getStatutArchivage();

    void setStatutArchivage(String statutArchivage);

    /**
     * Recopie du cycle de vie du dossier associé au dossierlink
     * 
     * @return
     */
    String getCaseLifeCycleState();

    void setCaseLifeCycleState(String caseLifeCycleState);

    /**
     * Getter/setter pour le titre de l'acte
     */
    String getTitreActe();

    void setTitreActe(String titreActe);

    Calendar getDateCreation();

    void setDateCreation(Calendar dateCreation);

    String getCaseDocumentId();

    void setCaseDocumentId(String caseDocumentId);

    void setArchive(Boolean archive);

    Boolean getDeleted();

    void setDeleted(Boolean deleted);
}
