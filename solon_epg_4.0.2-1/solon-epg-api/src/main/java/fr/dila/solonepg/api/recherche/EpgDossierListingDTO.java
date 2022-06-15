package fr.dila.solonepg.api.recherche;

import fr.dila.solonepg.api.cases.typescomplexe.ParutionBo;
import fr.dila.ss.api.recherche.IdLabel;
import fr.dila.st.api.domain.ComplexeType;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface EpgDossierListingDTO {
    IdLabel[] getCaseLinkIdsLabels();

    String getDossierId();

    void setCaseLinkIdsLabels(IdLabel[] caseLinkIdsLabels);

    void setDossierId(String dossierId);

    String getDocIdForSelection();

    void setDocIdForSelection(String docIdForSelection);

    String getType();

    Boolean isLocked();

    void setLocked(Boolean isLocked);

    String getLockOwner();

    void setDecretArrive(Boolean isDecretArrive);

    Boolean isDecretArrive();

    void setLockOwner(String lockOwner);

    String getCaseLinkId();

    Boolean getRead();

    void setCaseLinkId(String caseLinkId);

    void setRead(Boolean read);

    String getStatut();

    void setStatut(String statut);

    String getNumeroNor();

    void setNumeroNor(String numeroNor);

    String getTypeActe();

    void setTypeActe(String typeActe);

    String getTitreActe();

    void setTitreActe(String titreActe);

    String getMinistereResp();

    void setMinistereResp(String ministereResp);

    String getDirectionResp();

    void setDirectionResp(String ministereResp);

    String getMinistereAttache();

    void setMinistereAttache(String ministereAttache);

    String getDirectionAttache();

    void setDirectionAttache(String ministereAttache);

    String getNomRespDossier();

    void setNomRespDossier(String nomRespDossier);

    String getPrenomRespDossier();

    void setPrenomRespDossier(String prenomRespDossier);

    String getQualiteRespDossier();

    void setQualiteRespDossier(String qualite);

    String getTelephoneRespDossier();

    void setTelephoneRespDossier(String telephoneRespDossier);

    String getMailRespDossier();

    void setMailRespDossier(String mailRespDossier);

    String getCategorieActe();

    void setCategorieActe(String categorieActe);

    String getBaseLegaleActe();

    void setBaseLegaleActe(String baseLegaleActe);

    Date getDateArrivee();

    void setDateArrivee(Date dateArrivee);

    Date getDatePublication();

    void setDatePublication(Date datePublication);

    Boolean getPublicationRapportPresentation();

    void setPublicationRapportPresentation(Boolean publicationRapportPresentation);

    List<String> getNomCompletChargesMissions();

    void setNomCompletChargesMissions(List<String> chargeMissionIds);

    List<String> getNomCompletConseillersPms();

    void setNomCompletConseillersPms(List<String> conseillerPmIds);

    Date getDateSignature();

    void setDateSignature(Date dateSignature);

    Date getDatePourFournitureEpreuve();

    void setDatePourFournitureEpreuve(Date pourFournitureEpreuve);

    List<String> getVecteurPublication();

    void setVecteurPublication(List<String> vecteurPublication);

    List<String> getPublicationsConjointes();

    void setPublicationsConjointes(List<String> publicationsConjointes);

    String getPublicationIntegraleOuExtrait();

    void setPublicationIntegraleOuExtrait(String publicationIntegraleOuExtrait);

    Boolean getDecretNumerote();

    void setDecretNumerote(Boolean decretNumerote);

    String getDelaiPublication();

    void setDelaiPublication(String delaiPublication);

    Date getDatePreciseePublication();

    void setDatePreciseePublication(Date datePreciseePublication);

    String getZoneComSggDila();

    void setZoneComSggDila(String zoneComSggDila);

    List<String> getIndexationRubrique();

    void setIndexationRubrique(List<String> indexationRubrique);

    List<String> getIndexationMotsCles();

    void setIndexationMotsCles(List<String> indexationMotsCles);

    List<String> getIndexationChampLibre();

    void setIndexationChampLibre(List<String> indexationChampLibre);

    List<ComplexeType> getApplicationLoi();

    void setApplicationLoi(List<ComplexeType> loisAppliquees);

    List<ComplexeType> getTranspositionOrdonnance();

    void setTranspositionOrdonnance(List<ComplexeType> applicationOrdonnances);

    List<ComplexeType> getTranspositionDirective();

    void setTranspositionDirective(List<ComplexeType> directivesEuropeennes);

    List<String> getApplicationLoiRef();

    void setApplicationLoiRef(List<String> loisAppliquees);

    List<String> getTranspositionOrdonnanceRef();

    void setTranspositionOrdonnanceRef(List<String> applicationOrdonnances);

    List<String> getTranspositionDirectiveRef();

    void setTranspositionDirectiveRef(List<String> directivesEuropeennes);

    String getHabilitationRefLoi();

    void setHabilitationRefLoi(String habilitationRefLoi);

    String getHabilitationNumeroArticles();

    void setHabilitationNumeroArticles(String numeroArticles);

    String getHabilitationCommentaire();

    void setHabilitationCommentaire(String habilitationCommentaire);

    Boolean getUrgent();

    void setUrgent(Boolean urgent);

    Date getDateCreation();

    String getLastContributor();

    String getAuthor();

    void setAuthor(String author);

    void setDateCreation(Date date);

    void setLastContributor(String lastContributor);

    Date getDateParutionJorf();

    void setDateParutionJorf(Date dateParutionJorf);

    String getNumeroTexteParutionJorf();

    void setNumeroTexteParutionJorf(String numeroTexteParutionJorf);

    Long getPageParutionJorf();

    void setPageParutionJorf(Long pageParutionJorf);

    String getModeParution();

    void setModeParution(String modeParution);

    List<ParutionBo> getParutionBo();

    void setParutionBo(List<ParutionBo> parutionBoList);

    String getPriorite();

    void setPriorite(String priorite);

    String getSectionCe();

    void setSectionCe(String sectionCe);

    String getRapporteurCe();

    void setRapporteurCe(String rapporteurCe);

    Date getDateTransmissionSectionCe();

    void setDateTransmissionSectionCe(Date dateTransmissionSectionCe);

    Date getDateSectionCe();

    void setDateSectionCe(Date dateSectionCe);

    Date getDateAgCe();

    void setDateAgCe(Date dateAgCe);

    String getNumeroISA();

    void setNumeroISA(String numeroISA);

    String getDossierCompletErreur();

    void setDossierCompletErreur(String dossierCompletErreur);

    Boolean isRetourPourModification();

    void setRetourPourModification(Boolean retourPourModification);

    void setFromCaseLink(Boolean fromCaseLink);

    Boolean isFromCaseLink();

    List<Calendar> getDateEffet();

    void setDateEffet(List<Calendar> dateEffet);

    Boolean isTexteEntreprise();

    void setTexteEntreprise(Boolean te);

    String getHabilitationNumeroOrdre();

    void setHabilitationNumeroOrdre(String habilitationNumeroOrdre);
}
