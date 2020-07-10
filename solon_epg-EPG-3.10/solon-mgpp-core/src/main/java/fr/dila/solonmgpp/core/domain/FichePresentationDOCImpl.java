package fr.dila.solonmgpp.core.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;

public class FichePresentationDOCImpl implements FichePresentationDOC, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private final DocumentModel document;

    public FichePresentationDOCImpl(DocumentModel doc) {
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
    public List<String> getCommissions() {
        return PropertyUtil.getStringListProperty(document, SCHEMA, COMMISSIONS);
    }

    @Override
    public void setCommissions(List<String> listCommissions) {
        PropertyUtil.setProperty(document, SCHEMA, COMMISSIONS, listCommissions);
    }

    @Override
    public String getBaseLegale() {
        return PropertyUtil.getStringProperty(document, SCHEMA, BASE_LEGALE);
    }

    @Override
    public void setBaseLegale(String BaseLegale) {
        PropertyUtil.setProperty(document, SCHEMA, BASE_LEGALE, BaseLegale);
    }

    @Override
    public DocumentModel getDocument() {
        return this.document;
    }

}
