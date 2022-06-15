package fr.dila.solonmgpp.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.SortInfo;

/**
 * DTO de critere de recherche pour la recherche simple
 *
 * @author asatre
 *
 */
public interface CritereRechercheDTO extends Map<String, Serializable> {
    List<String> getTypeEvenement();

    void setTypeEvenement(List<String> typeEvenement);

    String getProcedure();

    void setProcedure(String procedure);

    String getNor();

    void setNor(String nor);

    String getNorLoi();

    void setNorLoi(String norLoi);

    String getObjet();

    void setObjet(String objet);

    String getNomOrganisme();

    void setNomOrganisme(String nomOrganisme);

    String getMenu();

    void setMenu(String menu);

    Long getBulletinBlanc();

    void setBulletin(Long bulletinBlanc);

    String getNumeroNor();

    void setNumeroNor(String numeroNor);

    String getTitreActe();

    void setTitreActe(String titreActe);

    List<String> getTypeActes();

    void setTypeActes(List<String> typeActe);

    void setCodeLecture(String codeLecture);

    void setNiveauLecture(Long niveauLecture);

    void setEmetteur(String emetteur);

    void setDestinataire(String destinataire);

    void setCopie(String copie);

    void setDateEvenementMax(Date dateEvenementMax);

    void setDateEvenementMin(Date dateEvenementMin);

    void setIdEvenement(String idEvenement);

    String getCodeLecture();

    Long getNiveauLecture();

    String getEmetteur();

    String getDestinataire();

    String getCopie();

    Date getDateEvenementMax();

    Date getDateEvenementMin();

    String getIdEvenement();

    List<String> getIdsCorbeille();

    void setIdsCorbeille(List<String> idsCorbeille);

    List<String> getVisaInterne();

    void setVisaInterne(List<String> interne);

    Boolean isEnAttente();

    void setEnAttente(Boolean enAttente);

    void setSortInfos(List<SortInfo> sortInfos);

    List<SortInfo> getSortInfos();

    void setEtatMessages(List<String> etatMessages);

    List<String> getEtatMessages();

    void setEtatMessage(String etatMessage);

    String getEtatMessage();

    void setEtatEvenement(String etatEvenement);

    String getEtatEvenement();

    void setPresencePieceJointe(Boolean presencePieceJointe);

    Boolean getPresencePieceJointe();

    void setAncienneteMessage(Long ancienneteMessage);

    Long getAncienneteMessage();

    String getIdEvenementPrecedent();

    String getIdDossier();

    void setIdDossier(String idDossier);

    List<String> getEmetteurs();

    List<String> getDestinataires();

    List<String> getCopies();

    Date getHorodatage();

    Date getHorodatageMax();

    Date getDateAr();

    Date getDateArMax();

    Date getDateDemande();

    Date getDateDemandeMax();

    String getTypeLoi();

    String getNatureLoi();

    List<String> getAuteurs();

    List<String> getCoAuteurs();

    String getIntitule();

    String getDescription();

    String getUrlDossierAn();

    String getUrlDossierSenat();

    String getCosignataire();

    String getResultatCMP();

    Date getDateCMP();

    Date getDateCMPMax();

    String getDepotNumero();

    List<String> getCommissionSaisieAuFond();

    List<String> getCommissionSaisiePourAvis();

    Date getDateRetrait();

    Date getDateRetraitMax();

    Date getDateDistributionElectronique();

    Date getDateDistributionElectroniqueMax();

    String getNatureRapport();

    List<String> getRapporteur();

    String getTitre();

    Date getDateDepotRapport();

    Date getDateDepotRapportMax();

    String getNumeroDepotRapport();

    List<String> getCommissionSaisie();

    List<String> getCommissions();

    Date getDateRefus();

    Date getDateRefusMax();

    List<String> getLibelleAnnexe();

    Date getDateEngagementProcedure();

    Date getDateEngagementProcedureMax();

    Date getDateRefusProcedureEngagementAn();

    Date getDateRefusProcedureEngagementAnMax();

    Date getDateRefusProcedureEngagementSenat();

    Date getDateRefusProcedureEngagementSenatMax();

    Date getDateAdoption();

    Date getDateAdoptionMax();

    String getNumeroTexteAdopte();

    String getSortAdoption();

    Boolean getPositionAlerte();

    Boolean getRedepot();

    Boolean getRectificatif();

    String getMotifIrrecevabilite();

    Date getDate();

    Date getDateMax();

    Long getAnneeRapport();

    String getUrlBaseLegale();

    String getBaseLegale();

    List<String> getTitulaires();

    List<String> getSuppleant();

    Date getDatePromulgation();

    Date getDatePromulgationMax();

    Date getDatePublication();

    Date getDatePublicationMax();

    String getNumeroLoi();

    String getTypeActe();

    Date getDateActe();

    Date getDateActeMax();

    Date getDateConvocation();

    Date getDateConvocationMax();

    Date getDateDesignation();

    Date getDateDesignationMax();

    Long getNumeroJo();

    Long getPageJo();

    Long getAnneeJo();

    Date getDateJo();

    Date getDateJoMax();

    Long getNumeroRubrique();

    String getUrlPublication();

    String getEcheance();

    String getSensAvis();

    Long getSuffrageExprime();

    Long getVotePour();

    Long getVoteContre();

    Long getAbstention();

    Date getDateCaducite();

    Date getDateCaduciteMax();

    String getDossierCible();

    String getRapportParlement();

    Date getDateCongres();

    Date getDateCongresMax();

    List<String> getDossierLegislatif();

    Date getDateDepotTexte();

    Date getDateDepotTexteMax();

    String getIdentifiantMetier();

    void setIdentifiantMetier(String identifiantMetier);

    String getPieceJointeLabel();

    String getPieceJointeFullText();

    Date getDatePresentation();

    Date getDatePresentationMax();

    Date getDateLettrePm();

    Date getDateLettrePmMax();

    Date getDateVote();

    Date getDateVoteMax();

    Date getDateDeclaration();

    Date getDateDeclarationMax();

    Boolean getDemandeVote();

    List<String> getGroupeParlementaire();

    List<String> getOrganisme();

    Date getDateAudition();

    Date getDateAuditionMax();

    String getPersonne();

    String getFonction();

    Date getDateRefusEngagementProcAcc();

    Date getDateRefusEngagementProcAccAssMax();

    Date getDateRefusEngagementProcAccAss1();

    Date getDateRefusEngagementProcAccAss1Max();

    Date getDateConfPresidentsAss2();

    Date getDateConfPresidentsAss2Max();

    String getDecisionEngProcAcc();
}
