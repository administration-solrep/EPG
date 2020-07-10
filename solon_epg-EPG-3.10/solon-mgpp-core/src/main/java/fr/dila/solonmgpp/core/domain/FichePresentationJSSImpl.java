package fr.dila.solonmgpp.core.domain;

import java.io.Serializable;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;

public class FichePresentationJSSImpl implements FichePresentationJSS, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final DocumentModel document;

    public FichePresentationJSSImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public void setIdDossier(String idDossier) {
        PropertyUtil.setProperty(document, SCHEMA, ID_DOSSIER, idDossier);
    }

    @Override
    public String getIdDossier() {
        return PropertyUtil.getStringProperty(document, SCHEMA, ID_DOSSIER);
    }

    @Override
    public String getObjet() {
        return PropertyUtil.getStringProperty(document, SCHEMA, OBJET);
    }

    @Override
    public void setObjet(String objet) {
        PropertyUtil.setProperty(document, SCHEMA, OBJET, objet);

    }

    @Override
    public Calendar getDateLettrePm() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_LETTRE_PM);
    }

    @Override
    public void setDateLettrePm(Calendar dateLettrePm) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_LETTRE_PM, dateLettrePm);

    }

    @Override
    public DocumentModel getDocument() {
        return this.document;
    }

}
