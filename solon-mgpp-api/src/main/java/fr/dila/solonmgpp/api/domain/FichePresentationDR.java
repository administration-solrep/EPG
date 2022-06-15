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

    Long getNumeroOrdre();

    String getLegislature();

    String getSession();

    Calendar getDatePublicationTexteReference();

    String getArticleTexteReference();

    Calendar getDateRelanceSGG();

    Long getNumeroDepotJOSenat();

    String getObservation();

    String getRubrique();

    String getCreateurDepot();

    List<String> getDestinataire1Rapport();

    String getDestinataire2Rapport();

    String getConventionCalcul();

    Boolean getRapportSenat();

    String getDirecteurCM();

    String getDAJMinistere();

    Long getAnneeDupliquee();
}
