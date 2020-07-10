package fr.dila.solonmgpp.core.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;

public class FichePresentationDRImpl implements FichePresentationDR, Serializable {

    private static final long serialVersionUID = -8873597187229419685L;

    private final DocumentModel document;

    public FichePresentationDRImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public String getIdDossier() {
        return PropertyUtil.getStringProperty(document, SCHEMA, ID_DOSSIER);
    }

    @Override
    public void setIdDossier(String idDossier) {
        PropertyUtil.setProperty(document, SCHEMA, ID_DOSSIER, idDossier);
    }

    @Override
    public DocumentModel getDocument() {
        return document;
    }

    @Override
    public Boolean getActif() {
        return PropertyUtil.getBooleanProperty(document, SCHEMA, ACTIF);
    }

    @Override
    public void setActif(Boolean actif) {
        PropertyUtil.setProperty(document, SCHEMA, ACTIF, actif);
    }

    @Override
    public void setRapportParlement(String rapportParlement) {
        PropertyUtil.setProperty(document, SCHEMA, RAPPORT_PARLEMENT, rapportParlement);
    }

    @Override
    public String getRapportParlement() {
        return PropertyUtil.getStringProperty(document, SCHEMA, RAPPORT_PARLEMENT);
    }

    @Override
    public void setObjet(String objet) {
        PropertyUtil.setProperty(document, SCHEMA, OBJET, objet);
    }

    @Override
    public String getObjet() {
        return PropertyUtil.getStringProperty(document, SCHEMA, OBJET);
    }

    @Override
    public void setDateDepotEffectif(Calendar dateDepotEffectif) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_DEPOT_EFFECTIF, dateDepotEffectif);
    }

    @Override
    public Calendar getDateDepotEffectif() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_DEPOT_EFFECTIF);
    }

    @Override
    public String getNature() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NATURE);
    }

    @Override
    public void setNature(String nature) {
        PropertyUtil.setProperty(document, SCHEMA, NATURE, nature);
    }

    @Override
    public List<String> getPole() {
        return PropertyUtil.getStringListProperty(document, SCHEMA,POLE);
    }

    @Override
    public void setPole(List<String> pole) {
        PropertyUtil.setProperty(document, SCHEMA, POLE, pole);
        
    }

    @Override
    public String getResponsabiliteElaboration() {
        return PropertyUtil.getStringProperty(document, SCHEMA, RESPONSABILITE_ELABORATION);
    }

    @Override
    public void setResponsabiliteElaboration(String responsabiliteElaboration) {
        PropertyUtil.setProperty(document, SCHEMA, RESPONSABILITE_ELABORATION, responsabiliteElaboration);
        
    }

    @Override
    public String getMinisteres() {
        return PropertyUtil.getStringProperty(document, SCHEMA, MINISTERES);
    }

    @Override
    public void setMinisteres(String ministeres) {
        PropertyUtil.setProperty(document, SCHEMA, MINISTERES, ministeres);
        
    }

    @Override
    public String getPeriodicite() {
        return PropertyUtil.getStringProperty(document, SCHEMA, PERIODICITE);
    }

    @Override
    public void setPeriodicite(String periodicite) {
        if(!DossierSolonEpgConstants.RAPPORT_PERIODIQUE_PERIODICITE_ID.equals(this.getRapportParlement())){
          periodicite = null ;
        }      
        PropertyUtil.setProperty(document, SCHEMA, PERIODICITE, periodicite);
        
    }

    @Override
    public String getTexteRef() {
        return PropertyUtil.getStringProperty(document, SCHEMA, TEXTE_REF);
    }

    @Override
    public void setTexteRef(String texteRef) {
        PropertyUtil.setProperty(document, SCHEMA, TEXTE_REF, texteRef);
        
    }
}
