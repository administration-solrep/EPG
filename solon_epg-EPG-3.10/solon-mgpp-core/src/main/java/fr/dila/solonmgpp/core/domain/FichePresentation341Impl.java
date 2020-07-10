package fr.dila.solonmgpp.core.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.solonmgpp.api.domain.FichePresentation341;

public class FichePresentation341Impl implements FichePresentation341, Serializable {

    private static final long serialVersionUID = -8873597187229419685L;

    private final DocumentModel document;

    public FichePresentation341Impl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public DocumentModel getDocument() {
        return document;
    }

    @Override
    public String getIdentifiantProposition() {
        return PropertyUtil.getStringProperty(document, SCHEMA, IDENTIFIANT_PROPOSITION);
    }

    @Override
    public void setIdentifiantProposition(String idProposition) {
        PropertyUtil.setProperty(document, SCHEMA, IDENTIFIANT_PROPOSITION, idProposition);
    }

    @Override
    public String getAuteur() {
        return PropertyUtil.getStringProperty(document, SCHEMA, AUTEUR);
    }

    @Override
    public void setAuteur(String auteur) {
        PropertyUtil.setProperty(document, SCHEMA, AUTEUR, auteur);
    }

    @Override
    public List<String> getCoAuteur() {
        return PropertyUtil.getStringListProperty(document, SCHEMA, CO_AUTEUR);
    }

    @Override
    public void setCoAuteur(List<String> coAuteur) {
        PropertyUtil.setProperty(document, SCHEMA, CO_AUTEUR, coAuteur);
    }

    @Override
    public String getIntitule() {
        return PropertyUtil.getStringProperty(document, SCHEMA, INTITULE);
    }

    @Override
    public void setIntitule(String intitule) {
        PropertyUtil.setProperty(document, SCHEMA, INTITULE, intitule);
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
    public String getNumeroDepot() {
        return PropertyUtil.getStringProperty(document, SCHEMA, NUMERO_DEPOT);
    }

    @Override
    public void setNumeroDepot(String numeroDepot) {
        PropertyUtil.setProperty(document, SCHEMA, NUMERO_DEPOT, numeroDepot);
    }

    @Override
    public Calendar getDateDepot() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_DEPOT);
    }

    @Override
    public void setDateDepot(Calendar dateDepot) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_DEPOT, dateDepot);
    }

    @Override
    public Calendar getDate() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE);
    }

    @Override
    public void setDate(Calendar date) {
        PropertyUtil.setProperty(document, SCHEMA, DATE_DEPOT, date);
    }

}
