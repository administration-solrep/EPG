package fr.dila.solonepg.ui.bean;

import fr.dila.st.core.client.AbstractMapDTO;
import java.util.Calendar;

public class MgppEcheancierPromulgationDTO extends AbstractMapDTO {
    private static final long serialVersionUID = 1L;

    private static final String ID = "id";
    private static final String INTITULE = "intitule";
    private static final String SAISIE_CC = "saisieCC";
    private static final String DECISION_CC = "decisionCC";
    private static final String DATE_RECEPTION = "dateReception";
    private static final String DEMANDE_EPREUVE = "demandeEpreuve";
    private static final String RETOUR_JO = "retourJo";
    private static final String ENVOI_RELECTURE = "envoiRelecture";
    private static final String RETOUR_RELECTURE = "retourRelecture";
    private static final String MIS_AU_CONTRESEING = "misAuContreseing";
    private static final String CONTRESEING_PM = "contreseingPm";
    private static final String DEPART_ELYSEE = "departElysee";
    private static final String RETOUR_ELYSEE = "retourElysee";
    private static final String DATE_LIMITE_PROMULGATION = "dateLimitePromulgation";
    private static final String PUBLICATION_JO = "publicationJo";

    public MgppEcheancierPromulgationDTO() {
        super();
    }

    public String getId() {
        return getString(ID);
    }

    public void setId(String id) {
        put(ID, id);
    }

    public String getIntitule() {
        return getString(INTITULE);
    }

    public void setIntitule(String intitule) {
        put(INTITULE, intitule);
    }

    public Calendar getConseilConstitutionnelSaisine() {
        return getCalendar(SAISIE_CC);
    }

    public void setConseilConstitutionnelSaisine(Calendar conseilConstitutionnelSaisine) {
        put(SAISIE_CC, conseilConstitutionnelSaisine);
    }

    public Calendar getConseilConstitutionnelDecision() {
        return getCalendar(DECISION_CC);
    }

    public void setConseilConstitutionnelDecision(Calendar conseilConstitutionnelDecision) {
        put(DECISION_CC, conseilConstitutionnelDecision);
    }

    public Calendar getDateReception() {
        return getCalendar(DATE_RECEPTION);
    }

    public void setDateReception(Calendar dateReception) {
        put(DATE_RECEPTION, dateReception);
    }

    public Calendar getDemandeEpreuvesJO() {
        return getCalendar(DEMANDE_EPREUVE);
    }

    public void setDemandeEpreuvesJO(Calendar demandeEpreuvesJO) {
        put(DEMANDE_EPREUVE, demandeEpreuvesJO);
    }

    public Calendar getRetourJO() {
        return getCalendar(RETOUR_JO);
    }

    public void setRetourJO(Calendar retourJO) {
        put(RETOUR_JO, retourJO);
    }

    public Calendar getEnvoiRelecture() {
        return getCalendar(ENVOI_RELECTURE);
    }

    public void setEnvoiRelecture(Calendar envoiRelecture) {
        put(ENVOI_RELECTURE, envoiRelecture);
    }

    public Calendar getRetourRelecture() {
        return getCalendar(RETOUR_RELECTURE);
    }

    public void setRetourRelecture(Calendar retourRelecture) {
        put(RETOUR_RELECTURE, retourRelecture);
    }

    public Calendar getMisAuContreseing() {
        return getCalendar(MIS_AU_CONTRESEING);
    }

    public void setMisAuContreseing(Calendar misAuContreseing) {
        put(MIS_AU_CONTRESEING, misAuContreseing);
    }

    public Calendar getContreseingPM() {
        return getCalendar(CONTRESEING_PM);
    }

    public void setContreseingPM(Calendar contreseingPM) {
        put(CONTRESEING_PM, contreseingPM);
    }

    public Calendar getDepartElysee() {
        return getCalendar(DEPART_ELYSEE);
    }

    public void setDepartElysee(Calendar departElysee) {
        put(DEPART_ELYSEE, departElysee);
    }

    public Calendar getRetourElysee() {
        return getCalendar(RETOUR_ELYSEE);
    }

    public void setRetourElysee(Calendar retourElysee) {
        put(RETOUR_ELYSEE, retourElysee);
    }

    public Calendar getDateLimitePromulgation() {
        return getCalendar(DATE_LIMITE_PROMULGATION);
    }

    public void setDateLimitePromulgation(Calendar dateLimitePromulgation) {
        put(DATE_LIMITE_PROMULGATION, dateLimitePromulgation);
    }

    public Calendar getPublicationJO() {
        return getCalendar(PUBLICATION_JO);
    }

    public void setPublicationJO(Calendar publicationJO) {
        put(PUBLICATION_JO, publicationJO);
    }

    @Override
    public String getType() {
        return "EcheancierPromulgation";
    }

    @Override
    public String getDocIdForSelection() {
        return null;
    }
}
