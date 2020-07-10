package fr.dila.solonmgpp.core.domain;

import java.io.Serializable;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;
import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.solonmgpp.api.domain.SemaineParlementaire;

public class SemaineParlementaireImpl implements SemaineParlementaire, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5777870229605286423L;
    private final DocumentModel document;

    public SemaineParlementaireImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public String getIntitule() {
        return PropertyUtil.getStringProperty(document, SEMAINE_PARLEMENTAIRE_SCHEMA, INTITULE);

    }

    @Override
    public void setIntitule(String intitule) {
        PropertyUtil.setProperty(document, SEMAINE_PARLEMENTAIRE_SCHEMA, INTITULE, intitule);

    }

    @Override
    public String getOrientation() {
        return PropertyUtil.getStringProperty(document, SEMAINE_PARLEMENTAIRE_SCHEMA, ORIENTATION);

    }

    @Override
    public void setOrientation(String orientation) {
        PropertyUtil.setProperty(document, SEMAINE_PARLEMENTAIRE_SCHEMA, ORIENTATION, orientation);
    }

    @Override
    public String getAssemblee() {
        return PropertyUtil.getStringProperty(document, SEMAINE_PARLEMENTAIRE_SCHEMA, ASSEMBLEE);
    }

    @Override
    public void setAssemblee(String assemblee) {
        PropertyUtil.setProperty(document, SEMAINE_PARLEMENTAIRE_SCHEMA, ASSEMBLEE, assemblee);

    }

    @Override
    public Calendar getDateDebut() {
        return PropertyUtil.getCalendarProperty(document, SEMAINE_PARLEMENTAIRE_SCHEMA, DATE_DEBUT);
    }

    @Override
    public void setDateDebut(Calendar dateDebut) {
        PropertyUtil.setProperty(document, SEMAINE_PARLEMENTAIRE_SCHEMA, DATE_DEBUT, dateDebut);
    }

    @Override
    public Calendar getDateFin() {
        return PropertyUtil.getCalendarProperty(document, SEMAINE_PARLEMENTAIRE_SCHEMA, DATE_FIN);
    }

    @Override
    public void setDateFin(Calendar dateFin) {
        PropertyUtil.setProperty(document, SEMAINE_PARLEMENTAIRE_SCHEMA, DATE_FIN, dateFin);
    }

    @Override
    public DocumentModel getDocument() {
        // TODO Auto-generated method stub
        return document;
    }

}
