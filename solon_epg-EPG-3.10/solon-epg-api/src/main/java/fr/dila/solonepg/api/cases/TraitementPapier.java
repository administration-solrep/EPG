package fr.dila.solonepg.api.cases;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication;
import fr.dila.solonepg.api.cases.typescomplexe.DonneesSignataire;
import fr.dila.solonepg.api.cases.typescomplexe.InfoEpreuve;
import fr.dila.solonepg.api.cases.typescomplexe.InfoHistoriqueAmpliation;
import fr.dila.solonepg.api.cases.typescomplexe.InfoNumeroListe;
import fr.dila.st.api.domain.STDomainObject;

public interface TraitementPapier extends STDomainObject, Serializable {

    // /////////////////
    // traitement papier
    // ////////////////

    // /////////////////
    // onglet Référence
    // ////////////////

    Calendar getDateArrivePapier();

    void setDateArrivePapier(Calendar dateArrivePapier);

    String getCommentaireReference();

    void setCommentaireReference(String commentaireReference);

    Boolean getTexteAPublier();

    void setTexteAPublier(Boolean texteAPublier);

    Boolean getTexteSoumisAValidation();

    void setTexteSoumisAValidation(Boolean texteSoumisAValidation);

    String getSignataire();

    void setSignataire(String signataire);

    // /////////////////
    // onglet Communication
    // ////////////////

    String getPriorite();

    void setPriorite(String priorite);

    String getPersonnesNommees();

    void setPersonnesNommees(String personnesNommees);

    List<DestinataireCommunication> getCabinetPmCommunication();

    void setCabinetPmCommunication(List<DestinataireCommunication> cabinetPmCommunication);

    List<DestinataireCommunication> getChargeMissionCommunication();

    void setChargeMissionCommunication(List<DestinataireCommunication> chargeMissionCommunication);

    List<DestinataireCommunication> getAutresDestinatairesCommunication();

    void setAutresDestinatairesCommunication(List<DestinataireCommunication> autresDestinatairesCommunication);
        
    void setCabinetSgCommunication(List<DestinataireCommunication> cabinetSgCommunication);
    
    List<DestinataireCommunication> getCabinetSgCommunication();

    String getNomsSignatairesCommunication();

    void setNomsSignatairesCommunication(String nomsSignatairesCommunication);

    // /////////////////
    // onglet Signature
    // ////////////////

    DonneesSignataire getSignaturePm();

    void setSignaturePm(DonneesSignataire signaturePm);

    DonneesSignataire getSignatureSgg();

    void setSignatureSgg(DonneesSignataire signatureSgg);

    DonneesSignataire getSignaturePr();

    void setSignaturePr(DonneesSignataire signaturePr);

    Boolean getCheminCroix();

    void setCheminCroix(Boolean cheminCroix);

    List<InfoNumeroListe> getNumerosListeSignature();

    void setNumerosListeSignature(List<InfoNumeroListe> numerosListeSignature);

    public String getSignatureDestinataireSGG();

    public void setSignatureDestinataireSGG(String signatureDestinataireSGG);

    public String getSignatureDestinataireCPM();

    public void setSignatureDestinataireCPM(String signatureDestinataireCPM);

    // /////////////////
    // onglet Retour
    // ////////////////

    String getRetourA();

    void setRetourA(String retourA);

    Calendar getDateRetour();

    void setDateRetour(Calendar dateRetour);

    String getMotifRetour();

    void setMotifRetour(String motifRetour);

    String getNomsSignatairesRetour();

    void setNomsSignatairesRetour(String nomsSignatairesRetour);

    // /////////////////
    // onglet Epreuves
    // ////////////////

    InfoEpreuve getEpreuve();

    void setEpreuve(InfoEpreuve epreuve);

    InfoEpreuve getNouvelleDemandeEpreuves();

    void setNouvelleDemandeEpreuves(InfoEpreuve nouvelleDemandeEpreuves);

    Calendar getRetourDuBonaTitrerLe();

    void setRetourDuBonaTitrerLe(Calendar retourDuBonaTitrerLe);

    // /////////////////
    // onglet Publication
    // ////////////////

    Calendar getPublicationDateEnvoiJo();

    void setPublicationDateEnvoiJo(Calendar publicationDateEnvoiJo);

    String getPublicationNomPublication();

    void setPublicationNomPublication(String publicationNomPublication);

    String getPublicationModePublication();

    void setPublicationModePublication(String publicationModePublication);

    Boolean getPublicationEpreuveEnRetour();

    void setPublicationEpreuveEnRetour(Boolean publicationEpreuveEnRetour);

    String getPublicationDelai();

    void setPublicationDelai(String publicationDelai);

    Calendar getPublicationDateDemande();

    void setPublicationDateDemande(Calendar publicationDateDemande);

    String getPublicationNumeroListe();

    void setPublicationNumeroListe(String publicationNumeroListe);

    Calendar getPublicationDate();

    void setPublicationDate(Calendar publicationDate);

    // /////////////////
    // onglet Ampliation
    // ////////////////

    List<String> getAmpliationDestinataireMails();

    void setAmpliationDestinataireMails(List<String> ampliationDestinataireMails);

    List<InfoHistoriqueAmpliation> getAmpliationHistorique();

    void setAmpliationHistorique(List<InfoHistoriqueAmpliation> ampliationHistorique);
    
    public Boolean getPapierArchive() ;

    public void setPapierArchive(Boolean papierArchive) ;

}
