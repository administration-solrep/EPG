package fr.dila.solonmgpp.api.domain;

import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface de representation d'une fiche presentation de depot de rapport
 * 
 * @author asatre
 * 
 */
public interface FichePresentationDR {

    public static final String DOC_TYPE = "FichePresentationDR";
    public static final String SCHEMA = "fiche_presentation_dr";
    public static final String PREFIX = "fpdr";

    public static final String ID_DOSSIER = "idDossier";
    public static final String ACTIF = "actif";
    public static final String RAPPORT_PARLEMENT = "rapportParlement";
    public static final String OBJET = "objet";
    public static final String DATE_DEPOT_EFFECTIF = "dateDepotEffectif";
    
    public static final String NATURE = "nature";
    public static final String POLE = "pole";
    public static final String PERIODICITE = "periodicite";
    public static final String RESPONSABILITE_ELABORATION = "responsabiliteElaboration";
    public static final String MINISTERES = "ministeres";
    public static final String TEXTE_REF = "texteRef";
    
    

    String getIdDossier();

    void setIdDossier(String idDossier);

    DocumentModel getDocument();

    Boolean getActif();

    void setActif(Boolean actif);

    void setRapportParlement(String rapportParlement);
    
    String getRapportParlement();
    
    void setObjet(String objet);
    
    String getObjet();
    
    void setDateDepotEffectif(Calendar dateDepotEffectif);
    
    Calendar getDateDepotEffectif();
    
    String getNature();
    
    void setNature(String nature);
    
    List<String> getPole();
    
    void setPole(List<String> pole);

    String getResponsabiliteElaboration();
    
    void setResponsabiliteElaboration(String responsabiliteElaboration);
    
    String getMinisteres();
    
    void setMinisteres(String ministeres);
    
    String getPeriodicite();
    
    void setPeriodicite(String periodicite);
    
    String getTexteRef();
    
    void setTexteRef(String texteRef);
    
}