package fr.dila.solonepg.web.client;

import java.util.Date;

import fr.dila.st.core.client.AbstractMapDTO;

public class TexteSignaleDTOImpl extends AbstractMapDTO implements TexteSignaleDTO {

    private static final long serialVersionUID = 1L;
    
    public TexteSignaleDTOImpl(){
    }
    
    @Override
    public String getDelai() {
        return getString(DELAI);
    }

    @Override
    public void setDelai(String delai) {
        put(DELAI, delai);
    }

    @Override
    public String getDossierId() {
        return getString(DOSSIER_ID);
    }

    @Override
    public void setDossierid(String dossierId) {
       put(DOSSIER_ID, dossierId);
    }

    @Override 
    public String getDocIdForSelection(){
        return getString(DOC_ID_FOR_SELECTION);
    }
    
    public void setDocIdForSelection(String docIdForSelection){
        put(DOC_ID_FOR_SELECTION, docIdForSelection);
    }

    @Override
    public String getTitre() {
        return getString(TITRE);
    }

    @Override
    public void setTitre(String titre) {
        put(TITRE, titre);
    }

    @Override
    public String getNor() {
        return getString(NOR);
    }

    @Override
    public void setNor(String nor) {
        put(NOR, nor);
        
    }

    @Override
    public Date getDateDemandePublicationTS() {
        return getDate(DATE_DEMANDE_PUBLICATION_TS);
    }

    @Override
    public void setDateDemandePublicationTS(Date dateDemandePublicationTS) {
        put(DATE_DEMANDE_PUBLICATION_TS, dateDemandePublicationTS);
    }

    @Override
    public String getVecteurPublicationTS() {
        return getString(VECTEUR_PUBLICATION);
    }

    @Override
    public void setVecteurPublicationTS(String vecteurPublicationTS) {
        put(VECTEUR_PUBLICATION, vecteurPublicationTS);
    }

    @Override
    public Boolean getArriveeSolon() {
        return getBoolean(ARRIVEE_SOLON);
    }

    @Override
    public void setArriveeSolon(Boolean arriveeSolon) {
       put(ARRIVEE_SOLON, arriveeSolon);
    }

    @Override
    public Boolean getAccordPM() {
        return getBoolean(ACCORD_PM);
    }

    @Override
    public void setAccordPM(Boolean accordPM) {
        put(ACCORD_PM, accordPM);
    }

    @Override
    public Boolean getAccordSGG() {
        return getBoolean(ACCORD_SGG);
    }

    @Override
    public void setAccordSGG(Boolean accordSGG) {
        put(ACCORD_SGG, accordSGG);
    }

    @Override
    public Boolean getMiseEnSignature() {
        return getBoolean(MISE_EN_SIGNATURE);
    }

    @Override
    public void setMiseEnSignature(Boolean miseEnSignature) {
        put(MISE_EN_SIGNATURE, miseEnSignature);
    }

    @Override
    public Boolean getRetourSignature() {
        return getBoolean(RETOUR_SIGNATURE);
    }

    @Override
    public void setRetourSignature(Boolean retourSignature) {
        put(RETOUR_SIGNATURE, retourSignature);
    }

    @Override
    public Boolean getDecretPr() {
        return getBoolean(DECRET_PR);
    }

    @Override
    public void setDecretPr(Boolean decretPr) {
        put(DECRET_PR, decretPr);
    }

    @Override
    public Boolean getEnvoiPr() {
        return getBoolean(ENVOI_PR);
    }

    @Override
    public void setEnvoiPr(Boolean envoiPr) {
        put(ENVOI_PR, envoiPr);
    }

    @Override
    public Boolean getRetourPr() {
        return getBoolean(RETOUR_PR);
    }

    @Override
    public void setRetourPr(Boolean retourPr) {
        put(RETOUR_PR, retourPr);
    }

    @Override
    public Date getDateJo() {
        return getDate(DATE_JO);
    }

    @Override
    public void setDateJo(Date dateJo) {
        put(DATE_JO, dateJo);
    }

    @Override
    public Date getDatePublication() {
        return getDate(DATE_PUBLICATION);
    }

    @Override
    public void setDatePublication(Date datePublication) {
        put(DATE_PUBLICATION, datePublication);
    }

    @Override
    public String getObservation() {
        return getString(OBSERVATION);
    }

    @Override
    public void setObservation(String observation) {
        put(OBSERVATION, observation);
    }

    @Override
    public Boolean getPublication() {
        return getBoolean(PUBLICATION);
    }

    @Override
    public void setPublication(Boolean publication) {
        put(PUBLICATION, publication);
    }

    @Override
    public Boolean getArriveeOriginale() {
        return getBoolean(ARRIVEE_ORIGINALE);
    }

    @Override
    public void setArriveeOriginale(Boolean arriveeOriginale) {
        put(ARRIVEE_ORIGINALE, arriveeOriginale);
    }

    @Override
    public String getType() {
        return "TexteSignale";
    }

}
