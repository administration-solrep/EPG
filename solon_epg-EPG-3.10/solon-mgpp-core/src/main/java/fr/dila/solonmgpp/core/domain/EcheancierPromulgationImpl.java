package fr.dila.solonmgpp.core.domain;

import java.io.Serializable;
import java.util.Calendar;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.solonmgpp.api.domain.EcheancierPromulgation;

public class EcheancierPromulgationImpl implements EcheancierPromulgation, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4354567689926464467L;

    private final DocumentModel document;

    public EcheancierPromulgationImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public String getIdDossier() {
        return PropertyUtil.getStringProperty(document, ECHANCIER_PROMULGATION_SCHEMA, ID_DOSSIER);
    }

    @Override
    public void setIdDossier(String idDossier) {
        PropertyUtil.setProperty(document, ECHANCIER_PROMULGATION_SCHEMA, ID_DOSSIER, idDossier);
    }

    @Override
    public Calendar getDateSaisieCC() {
        return PropertyUtil.getCalendarProperty(document, ECHANCIER_PROMULGATION_SCHEMA, DATE_SAISINE_CC);
    }

    @Override
    public void setDateSaisieCC(Calendar dateSaisieCC) {
        PropertyUtil.setProperty(document, ECHANCIER_PROMULGATION_SCHEMA, DATE_SAISINE_CC, dateSaisieCC);

    }

    @Override
    public Calendar getDateDecision() {
        return PropertyUtil.getCalendarProperty(document, ECHANCIER_PROMULGATION_SCHEMA, DATE_DECISION);
    }

    @Override
    public void setDateDecision(Calendar dateDecision) {
        PropertyUtil.setProperty(document, ECHANCIER_PROMULGATION_SCHEMA, DATE_DECISION, dateDecision);

    }

    @Override
    public Calendar getRetourJO() {
        return PropertyUtil.getCalendarProperty(document, ECHANCIER_PROMULGATION_SCHEMA, RETOUR_JO);
    }

    @Override
    public void setRetourJO(Calendar retourJO) {
        PropertyUtil.setProperty(document, ECHANCIER_PROMULGATION_SCHEMA, RETOUR_JO, retourJO);

    }

    @Override
    public Calendar getEnvoiRelecture() {
        return PropertyUtil.getCalendarProperty(document, ECHANCIER_PROMULGATION_SCHEMA, ENVOI_RELECTURE);
    }

    @Override
    public void setEnvoiRelecture(Calendar envoiRelecture) {
        PropertyUtil.setProperty(document, ECHANCIER_PROMULGATION_SCHEMA, ENVOI_RELECTURE, envoiRelecture);

    }

    @Override
    public Calendar getRetourRelecture() {
        return PropertyUtil.getCalendarProperty(document, ECHANCIER_PROMULGATION_SCHEMA, RETOUR_RELECTURE);
    }

    @Override
    public void setRetourRelecture(Calendar retourRelecture) {
        PropertyUtil.setProperty(document, ECHANCIER_PROMULGATION_SCHEMA, RETOUR_RELECTURE, retourRelecture);

    }

    @Override
    public Calendar getMisAuContreseing() {
        return PropertyUtil.getCalendarProperty(document, ECHANCIER_PROMULGATION_SCHEMA, MIS_AU_CONTRESEING);
    }

    @Override
    public void setMisAuContreseing(Calendar misAuContreseing) {
        PropertyUtil.setProperty(document, ECHANCIER_PROMULGATION_SCHEMA, MIS_AU_CONTRESEING, misAuContreseing);

    }

    @Override
    public String getContreseingPM() {
        return PropertyUtil.getStringProperty(document, ECHANCIER_PROMULGATION_SCHEMA, CONTRESEING_PM);
    }

    @Override
    public void setContreseingPM(String contreseingPM) {
        PropertyUtil.setProperty(document, ECHANCIER_PROMULGATION_SCHEMA, CONTRESEING_PM, contreseingPM);

    }

    @Override
    public Calendar getDepartElysee() {
        return PropertyUtil.getCalendarProperty(document, ECHANCIER_PROMULGATION_SCHEMA, DEPART_ELYSEE);
    }

    @Override
    public void setDepartElysee(Calendar departElysee) {
        PropertyUtil.setProperty(document, ECHANCIER_PROMULGATION_SCHEMA, DEPART_ELYSEE, departElysee);

    }

    @Override
    public Calendar getRetourElysee() {
        return PropertyUtil.getCalendarProperty(document, ECHANCIER_PROMULGATION_SCHEMA, RETOUR_ELYSEE);
    }

    @Override
    public void setRetourElysee(Calendar retourElysee) {
        PropertyUtil.setProperty(document, ECHANCIER_PROMULGATION_SCHEMA, RETOUR_ELYSEE, retourElysee);

    }

    @Override
    public Calendar getPublicationJO() {
        return PropertyUtil.getCalendarProperty(document, ECHANCIER_PROMULGATION_SCHEMA, PUBLICATION_JO);
    }

    @Override
    public void setPublicationJO(Calendar publicationJO) {
        PropertyUtil.setProperty(document, ECHANCIER_PROMULGATION_SCHEMA, PUBLICATION_JO, publicationJO);

    }

    @Override
    public Calendar getDateLimitePromulgation() {
        return PropertyUtil.getCalendarProperty(document, ECHANCIER_PROMULGATION_SCHEMA, DATE_LIMITE_PROMULGATION);
    }

    @Override
    public void setDateLimitePromulgation(Calendar dateLimitePromulgation) {
        PropertyUtil.setProperty(document, ECHANCIER_PROMULGATION_SCHEMA, DATE_LIMITE_PROMULGATION, dateLimitePromulgation);

    }

    @Override
    public Calendar getDateReception() {
        return PropertyUtil.getCalendarProperty(document, ECHANCIER_PROMULGATION_SCHEMA, DATE_RECEPTION);
    }

    @Override
    public void setDateReception(Calendar dateReception) {
        PropertyUtil.setProperty(document, ECHANCIER_PROMULGATION_SCHEMA, DATE_RECEPTION, dateReception);
    }

    @Override
    public DocumentModel getDocument() {
        return document;
    }

}
