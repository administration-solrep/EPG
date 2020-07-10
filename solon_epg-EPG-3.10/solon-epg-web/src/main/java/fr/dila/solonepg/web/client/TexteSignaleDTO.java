package fr.dila.solonepg.web.client;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public interface TexteSignaleDTO extends Map<String,Serializable> {
    
    public static final String DOSSIER_ID = "dossierId";   
    public static final String TITRE = "titre";
    public static final String NOR = "nor";
    public static final String DATE_DEMANDE_PUBLICATION_TS = "dateDemandePublicationTS";
    public static final String VECTEUR_PUBLICATION = "vecteurPublicationTS";
    public static final String ARRIVEE_SOLON = "arriveeSolon";
    public static final String ACCORD_PM = "accordPM";
    public static final String ACCORD_SGG = "accordSGG";
    public static final String ARRIVEE_ORIGINALE = "arriveeOriginale";
    public static final String MISE_EN_SIGNATURE = "miseEnSignature";
    public static final String RETOUR_SIGNATURE = "retourSignature";
    public static final String DECRET_PR = "decretPr";
    public static final String ENVOI_PR = "envoiPr";
    public static final String RETOUR_PR = "retourPr";
    public static final String DATE_JO = "dateJo";
    public static final String DELAI = "delai";
    public static final String DATE_PUBLICATION = "datePublication";
    public static final String OBSERVATION = "observation";
    public static final String PUBLICATION = "publication";
    
    /**
     * correspond a la valeur a manipuler en tant qu'id de document
     * lors de la selection sur la contentview
     */
    public static final String DOC_ID_FOR_SELECTION = "docIdForSelection";
    
    String getDossierId();
    void setDossierid(String dossierId);
    
    String getDocIdForSelection();
    void setDocIdForSelection(String docIdForSelection);
    
    String getTitre();
    void setTitre(String titre);
    
    String getNor();
    void setNor(String nor);
    
    Date getDateDemandePublicationTS();
    void setDateDemandePublicationTS(Date dateDemandePublicationTS);
    
    String getVecteurPublicationTS();
    void setVecteurPublicationTS(String vecteurPublicationTS);
    
    Boolean getArriveeOriginale();
    void setArriveeOriginale(Boolean arriveeOriginale);

    Boolean getArriveeSolon();
    void setArriveeSolon(Boolean arriveeSolon);
    
    Boolean getAccordPM();
    void setAccordPM(Boolean accordPM);
    
    Boolean getAccordSGG();
    void setAccordSGG(Boolean accordSGG);
    
    Boolean getMiseEnSignature();
    void setMiseEnSignature(Boolean miseEnSignature);
    
    Boolean getRetourSignature();
    void setRetourSignature(Boolean retourSignature);
    
    Boolean getDecretPr();
    void setDecretPr(Boolean decretPr);
    
    Boolean getEnvoiPr();
    void setEnvoiPr(Boolean envoiPr);
    
    Boolean getRetourPr();
    void setRetourPr(Boolean retourPr);
    
    Date getDateJo();
    void setDateJo(Date dateJo);
    
    String getDelai();
    void setDelai(String delai);

    Date getDatePublication();
    void setDatePublication(Date datePublication);
    
    String getObservation();
    void setObservation(String observation);
    
    Boolean getPublication();
    void setPublication(Boolean publication);
    
}
