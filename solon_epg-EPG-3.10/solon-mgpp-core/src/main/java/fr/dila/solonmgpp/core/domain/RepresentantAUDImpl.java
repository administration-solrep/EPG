package fr.dila.solonmgpp.core.domain;

import java.io.Serializable;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.solonmgpp.api.domain.RepresentantAUD;

public class RepresentantAUDImpl implements RepresentantAUD, Serializable {

    private static final long serialVersionUID = 4902180669574615408L;

    private final DocumentModel document;

    public RepresentantAUDImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public DocumentModel getDocument() {
        return document;
    }

    @Override
    public String getIdFicheRepresentationAUD() {
        return PropertyUtil.getStringProperty(document, SCHEMA, ID_FICHE_RAUD);
    }

    @Override
    public void setIdFicheRepresentationAUD(String idFicheRepresentationAUD) {
        PropertyUtil.setProperty(document, SCHEMA, ID_FICHE_RAUD, idFicheRepresentationAUD);
    }

    @Override
    public String getPersonne() {
        return PropertyUtil.getStringProperty(document, SCHEMA, PERSONNE);
    }

    @Override
    public void setPersonne(String personne) {
        PropertyUtil.setProperty(document, SCHEMA, PERSONNE, personne);
    }

    @Override
    public Calendar getDateDebut() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_DEBUT);
    }

    @Override
    public void setDateDebut(Calendar dateDebut) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_DEBUT, dateDebut);
    }

    @Override
    public Calendar getDateFin() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_FIN);
    }

    @Override
    public void setDateFin(Calendar dateFin) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_FIN, dateFin);
    }

    @Override
    public Calendar getDateAuditionSE() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_AUDITION_SENAT);
    }

    @Override
    public void setDateAuditionSE(Calendar dateAuditionSE) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_AUDITION_SENAT, dateAuditionSE);
    }

    @Override
    public Calendar getDateAuditionAN() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_AUDITION_AN);
    }

    @Override
    public void setDateAuditionAN(Calendar dateAuditionAN) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_AUDITION_AN, dateAuditionAN);

    }

    @Override
    public String getFonction() {
        return PropertyUtil.getStringProperty(document, SCHEMA, FONCTION);
    }

    @Override
    public void setFonction(String fonction) {
        PropertyUtil.setProperty(document, SCHEMA, FONCTION, fonction);
    }

}
