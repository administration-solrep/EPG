package fr.dila.solonmgpp.core.domain;

import java.io.Serializable;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;

public class FichePresentationIEImpl implements FichePresentationIE, Serializable {

    private static final long serialVersionUID = 7270044925044450432L;

    private final DocumentModel document;

    public FichePresentationIEImpl(DocumentModel doc) {
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
    public void setIdentifiantProposition(String identifiantProposition) {
        PropertyUtil.setProperty(document, SCHEMA, IDENTIFIANT_PROPOSITION, identifiantProposition);
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
    public String getIntitule() {
        return PropertyUtil.getStringProperty(document, SCHEMA, INTITULE);
    }

    @Override
    public void setIntitule(String intitule) {
        PropertyUtil.setProperty(document, SCHEMA, INTITULE, intitule);
    }

    @Override
    public String getObservation() {
        return PropertyUtil.getStringProperty(document, SCHEMA, OBSERVATION);
    }

    @Override
    public void setObservation(String observation) {
        PropertyUtil.setProperty(document, SCHEMA, OBSERVATION, observation);
    }

    @Override
    public Calendar getDate() {
        return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE);
    }

    @Override
    public void setDate(Calendar date) {
        PropertyUtil.setProperty(document, SCHEMA, DATE, date);
    }

}
