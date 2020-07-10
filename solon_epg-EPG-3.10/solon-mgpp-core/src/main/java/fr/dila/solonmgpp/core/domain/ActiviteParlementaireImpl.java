package fr.dila.solonmgpp.core.domain;

import java.io.Serializable;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.solonmgpp.api.domain.ActiviteParlementaire;

public class ActiviteParlementaireImpl implements ActiviteParlementaire, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8026661518577949667L;

    private final DocumentModel document;

    public ActiviteParlementaireImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public String getIdDossier() {
        return PropertyUtil.getStringProperty(document, ACTIVITE_PARLEMENTAIRE_SCHEMA, ID_DOSSIER);
    }

    @Override
    public void setIdDossier(String idDossier) {
        PropertyUtil.setProperty(document, ACTIVITE_PARLEMENTAIRE_SCHEMA, ID_DOSSIER, idDossier);

    }

    @Override
    public String getIntitule() {
        return PropertyUtil.getStringProperty(document, ACTIVITE_PARLEMENTAIRE_SCHEMA, INTITULE);
    }

    @Override
    public void setIntitule(String intitule) {
        PropertyUtil.setProperty(document, ACTIVITE_PARLEMENTAIRE_SCHEMA, INTITULE, intitule);
    }

    @Override
    public String getActivite() {
        return PropertyUtil.getStringProperty(document, ACTIVITE_PARLEMENTAIRE_SCHEMA, ACTIVITE);
    }

    @Override
    public void setActivite(String activite) {
        PropertyUtil.setProperty(document, ACTIVITE_PARLEMENTAIRE_SCHEMA, ACTIVITE, activite);

    }

    @Override
    public String getAssemblee() {
        return PropertyUtil.getStringProperty(document, ACTIVITE_PARLEMENTAIRE_SCHEMA, ASSEMBLEE);
    }

    @Override
    public void setAssemblee(String assemblee) {
        PropertyUtil.setProperty(document, ACTIVITE_PARLEMENTAIRE_SCHEMA, ASSEMBLEE, assemblee);

    }

    @Override
    public Calendar getDate() {
        return PropertyUtil.getCalendarProperty(document, ACTIVITE_PARLEMENTAIRE_SCHEMA, DATE);
    }

    @Override
    public void setDate(Calendar date) {
        PropertyUtil.setProperty(document, ACTIVITE_PARLEMENTAIRE_SCHEMA, DATE, date);
    }

    @Override
    public DocumentModel getDocument() {
        return document;
    }

}
