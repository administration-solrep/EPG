package fr.dila.solonmgpp.core.domain;

import java.io.Serializable;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;

public class FichePresentationAVIImpl implements FichePresentationAVI, Serializable {

    private static final long serialVersionUID = 327466598355219338L;

    private final DocumentModel document;

    public FichePresentationAVIImpl(DocumentModel doc) {
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
    public String getIdOrganismeEPP() {
        return PropertyUtil.getStringProperty(document, SCHEMA, ID_ORGANISME_EPP);
    }

    @Override
    public void setIdOrganismeEPP(String idOrganismeEPP) {
        PropertyUtil.setProperty(document, SCHEMA, ID_ORGANISME_EPP, idOrganismeEPP);

    }

    @Override
    public String getNomOrganisme() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NOM_ORGANISME);
    }

    @Override
    public void setNomOrganisme(String nomOrganisme) {
        PropertyUtil.setProperty(document, SCHEMA, NOM_ORGANISME, nomOrganisme);
    }

    @Override
    public Calendar getDate() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE);
    }

    @Override
    public void setDate(Calendar date) {
        PropertyUtil.setProperty(document, SCHEMA, DATE, date);
    }

    @Override
    public String getBaseLegale() {
        return PropertyUtil.getStringProperty(document, SCHEMA, BASE_LEGALE);
    }

    @Override
    public void setBaseLegale(String baseLegale) {
        PropertyUtil.setProperty(document, BASE_LEGALE, DATE, baseLegale);
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
