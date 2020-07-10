package fr.dila.solonmgpp.core.domain;

import java.io.Serializable;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.solonmgpp.api.domain.RepresentantAVI;

public class RepresentantAVIImpl implements RepresentantAVI, Serializable {

    private static final long serialVersionUID = 4902180669574615408L;

    private final DocumentModel document;

    public RepresentantAVIImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public DocumentModel getDocument() {
        return document;
    }

    @Override
    public String getIdFicheRepresentationAVI() {
        return PropertyUtil.getStringProperty(document, SCHEMA, ID_FICHE_RAVI);
    }

    @Override
    public void setIdFicheRepresentationAVI(String idFicheRepresentationAVI) {
        PropertyUtil.setProperty(document, SCHEMA, ID_FICHE_RAVI, idFicheRepresentationAVI);
    }

    @Override
    public String getNomine() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NOMINE);
    }

    @Override
    public void setNomine(String nomine) {
        PropertyUtil.setProperty(document, SCHEMA, NOMINE, nomine);
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
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_AUDITION_SE);
    }

    @Override
    public void setDateAuditionSE(Calendar dateAuditionSE) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_AUDITION_SE, dateAuditionSE);
    }

    @Override
    public Calendar getDateAuditionAN() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_AUDITION_AN);
    }

    @Override
    public void setDateAuditionAN(Calendar dateAuditionAN) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_AUDITION_AN, dateAuditionAN);

    }

}
