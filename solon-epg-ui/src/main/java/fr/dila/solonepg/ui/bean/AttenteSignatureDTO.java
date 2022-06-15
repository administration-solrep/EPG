package fr.dila.solonepg.ui.bean;

import fr.dila.st.core.client.AbstractMapDTO;
import java.util.Date;

public class AttenteSignatureDTO extends AbstractMapDTO {
    private static final long serialVersionUID = 1L;
    public static final String DOSSIER_ID = "dossierId";
    public static final String TITRE = "titre";
    public static final String NOR = "nor";
    public static final String DATE_DEMANDE_PUBLICATION_TS = "dateDemandePublicationTS";
    public static final String VECTEUR_PUBLICATION = "vecteurPublicationTS";
    public static final String VECTEUR_PUBLICATION_LIBELLE = "vecteurPublicationTSLibelle";
    public static final String ARRIVEE_SOLON = "arriveeSolon";
    public static final String ACCORD_PM = "accordPM";
    public static final String ACCORD_SGG = "accordSGG";
    public static final String ARRIVEE_ORIGINALE = "arriveeOriginale";
    public static final String MISE_EN_SIGNATURE = "miseEnSignature";
    public static final String RETOUR_SIGNATURE = "retourSignature";
    public static final String DECRET_PR = "decretPr";
    public static final String ENVOI_PR = "envoiPr";
    public static final String RETOUR_PR = "retourPr";
    public static final String DATE_JO = "dateJo";
    public static final String DELAI = "delai";
    public static final String DELAI_LIBELLE = "delaiLibelle";
    public static final String DATE_PUBLICATION = "datePublication";
    public static final String OBSERVATION = "observation";
    public static final String PUBLICATION = "publication";
    public static final String DOC_ID_FOR_SELECTION = "docIdForSelection";

    public AttenteSignatureDTO() {}

    public String getDossierId() {
        return getString(DOSSIER_ID);
    }

    public void setDossierId(String dossierId) {
        put(DOSSIER_ID, dossierId);
    }

    public String getTitre() {
        return getString(TITRE);
    }

    public void setTitre(String titre) {
        put(TITRE, titre);
    }

    public String getNor() {
        return getString(NOR);
    }

    public void setNor(String nor) {
        put(NOR, nor);
    }

    public Date getDateDemandePublicationTS() {
        return getDate(DATE_DEMANDE_PUBLICATION_TS);
    }

    public void setDateDemandePublicationTS(Date dateDemandePublicationTS) {
        put(DATE_DEMANDE_PUBLICATION_TS, dateDemandePublicationTS);
    }

    public String getVecteurPublicationTS() {
        return getString(VECTEUR_PUBLICATION);
    }

    public void setVecteurPublicationTS(String vecteurPublicationTS) {
        put(VECTEUR_PUBLICATION, vecteurPublicationTS);
    }

    public String getVecteurPublicationTSLibelle() {
        return getString(VECTEUR_PUBLICATION_LIBELLE);
    }

    public void setVecteurPublicationTSLibelle(String vecteurPublicationTSLibelle) {
        put(VECTEUR_PUBLICATION_LIBELLE, vecteurPublicationTSLibelle);
    }

    public Boolean getArriveeOriginale() {
        return getBoolean(ARRIVEE_ORIGINALE);
    }

    public void setArriveeOriginale(Boolean arriveeOriginale) {
        put(ARRIVEE_ORIGINALE, arriveeOriginale);
    }

    public Boolean getArriveeSolon() {
        return getBoolean(ARRIVEE_SOLON);
    }

    public void setArriveeSolon(Boolean arriveeSolon) {
        put(ARRIVEE_SOLON, arriveeSolon);
    }

    public Boolean getAccordPM() {
        return getBoolean(ACCORD_PM);
    }

    public void setAccordPM(Boolean accordPM) {
        put(ACCORD_PM, accordPM);
    }

    public Boolean getAccordSGG() {
        return getBoolean(ACCORD_SGG);
    }

    public void setAccordSGG(Boolean accordSGG) {
        put(ACCORD_SGG, accordSGG);
    }

    public Boolean getMiseEnSignature() {
        return getBoolean(MISE_EN_SIGNATURE);
    }

    public void setMiseEnSignature(Boolean miseEnSignature) {
        put(MISE_EN_SIGNATURE, miseEnSignature);
    }

    public Boolean getRetourSignature() {
        return getBoolean(RETOUR_SIGNATURE);
    }

    public void setRetourSignature(Boolean retourSignature) {
        put(RETOUR_SIGNATURE, retourSignature);
    }

    public Boolean getDecretPr() {
        return getBoolean(DECRET_PR);
    }

    public void setDecretPr(Boolean decretPr) {
        put(DECRET_PR, decretPr);
    }

    public Boolean getEnvoiPr() {
        return getBoolean(ENVOI_PR);
    }

    public void setEnvoiPr(Boolean envoiPr) {
        put(ENVOI_PR, envoiPr);
    }

    public Boolean getRetourPr() {
        return getBoolean(RETOUR_PR);
    }

    public void setRetourPr(Boolean retourPr) {
        put(RETOUR_PR, retourPr);
    }

    public Date getDateJo() {
        return getDate(DATE_JO);
    }

    public void setDateJo(Date dateJo) {
        put(DATE_JO, dateJo);
    }

    public String getDelai() {
        return getString(DELAI);
    }

    public void setDelai(String delai) {
        put(DELAI, delai);
    }

    public String getDelaiLibelle() {
        return getString(DELAI_LIBELLE);
    }

    public void setDelaiLibelle(String delaiLibelle) {
        put(DELAI_LIBELLE, delaiLibelle);
    }

    public Date getDatePublication() {
        return getDate(DATE_PUBLICATION);
    }

    public void setDatePublication(Date datePublication) {
        put(DATE_PUBLICATION, datePublication);
    }

    public String getObservation() {
        return getString(OBSERVATION);
    }

    public void setObservation(String observation) {
        put(OBSERVATION, observation);
    }

    public Boolean getPublication() {
        return getBoolean(PUBLICATION);
    }

    public void setPublication(Boolean publication) {
        put(PUBLICATION, publication);
    }

    public String getType() {
        return "Attente signature";
    }

    @Override
    public String getDocIdForSelection() {
        return getString(DOC_ID_FOR_SELECTION);
    }

    public void setDocIdForSelection(String docIdForSelection) {
        put(DOC_ID_FOR_SELECTION, docIdForSelection);
    }
}
