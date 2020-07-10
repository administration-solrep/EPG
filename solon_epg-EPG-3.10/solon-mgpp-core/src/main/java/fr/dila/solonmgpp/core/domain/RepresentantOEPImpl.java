package fr.dila.solonmgpp.core.domain;

import java.io.Serializable;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.solonmgpp.api.domain.RepresentantOEP;

public class RepresentantOEPImpl implements RepresentantOEP, Serializable {

    private static final long serialVersionUID = 6717935433174615506L;

    private final DocumentModel document;

    public RepresentantOEPImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public DocumentModel getDocument() {
        return document;
    }

    @Override
    public String getIdFicheRepresentationOEP() {
        return PropertyUtil.getStringProperty(document, SCHEMA, ID_FICHE_ROEP);
    }

    @Override
    public void setIdFicheRepresentationOEP(String idFicheRepresentationOEP) {
        PropertyUtil.setProperty(document, SCHEMA, ID_FICHE_ROEP, idFicheRepresentationOEP);
    }

    @Override
    public String getType() {
        return PropertyUtil.getStringProperty(document, SCHEMA, TYPE);
    }

    @Override
    public void setType(String type) {
        PropertyUtil.setProperty(document, SCHEMA, TYPE, type);

    }

    @Override
    public String getRepresentant() {
        return PropertyUtil.getStringProperty(document, SCHEMA, REPRESENTANT);
    }

    @Override
    public void setRepresentant(String representant) {
        PropertyUtil.setProperty(document, SCHEMA, REPRESENTANT, representant);

    }

    @Override
    public String getFonction() {
        return PropertyUtil.getStringProperty(document, SCHEMA, FONCTION);
    }

    @Override
    public void setFonction(String fonction) {
        PropertyUtil.setProperty(document, SCHEMA, FONCTION, fonction);

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

}
