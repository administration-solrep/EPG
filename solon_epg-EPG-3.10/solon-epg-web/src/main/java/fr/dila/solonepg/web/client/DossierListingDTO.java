package fr.dila.solonepg.web.client;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import fr.dila.solonepg.api.cases.typescomplexe.ParutionBo;
import fr.dila.solonepg.api.dto.IdLabel;
import fr.dila.st.api.domain.ComplexeType;

public interface DossierListingDTO extends Map<String, Serializable> {

	public static final String	IS_LOCKED				= "isLocked";
	public static final String	LOCK_OWNER				= "lockOwner";
	public static final String	IS_DECRET_ARRIVE		= "isDecretArrive";

	public static final String	CASE_LINK_IDS_LABELS	= "caseLinkIdsLabels";
	public static final String	DOSSIER_ID				= "dossierId";

	public static final String	CASE_LINK_ID			= "caseLinkId";
	public static final String	IS_READ					= "isRead";
	public static final String	HAS_ERRATA				= "hasErrata";

	/**
	 * correspond a la valeur a manipuler en tant qu'id de document lors de la selection sur la contentview
	 */
	public static final String	DOC_ID_FOR_SELECTION	= "docIdForSelection";

	IdLabel[] getCaseLinkIdsLabels();

	String getDossierId();

	String getDocIdForSelection();

	Boolean isLocked();

	String getLockOwner();

	Boolean isDecretArrive();

	Boolean getRead();

	String getCaseLinkId();

	String getNumeroNor();

	String getTitreActe();

	Date getDateCreation();

	String getLastContributor();

	String getAuthor();

	Boolean getUrgent();

	String getStatut();

	String getTypeActe();

	String getMinistereResp();

	String getDirectionResp();

	String getMinistereAttache();

	String getDirectionAttache();

	String getNomRespDossier();

	String getPrenomRespDossier();

	String getQualiteRespDossier();

	String getTelephoneRespDossier();

	String getMailRespDossier();

	String getCategorieActe();

	String getBaseLegaleActe();

	Date getDatePublication();

	Boolean getPublicationRapportPresentation();

	String getSectionCe();

	String getPriorite();

	String getRapporteurCe();

	Date getDateTransmissionSectionCe();

	Date getDateSectionCe();

	Date getDateAgCe();

	String getNumeroISA();

	List<String> getNomCompletChargesMissions();

	List<String> getNomCompletConseillersPms();

	Date getDateSignature();

	Date getDatePourFournitureEpreuve();

	List<String> getVecteurPublication();

	List<String> getPublicationsConjointes();

	String getPublicationIntegraleOuExtrait();

	Boolean getDecretNumerote();

	String getModeParution();

	Date getDateParutionJorf();

	String getNumeroTexteParutionJorf();

	Long getPageParutionJorf();

	List<ParutionBo> getParutionBo();

	String getDelaiPublication();

	Date getDatePreciseePublication();

	String getZoneComSggDila();

	List<String> getIndexationRubrique();

	List<String> getIndexationMotsCles();

	List<String> getIndexationChampLibre();

	List<ComplexeType> getApplicationLoi();

	List<ComplexeType> getTranspositionOrdonnance();

	List<ComplexeType> getTranspositionDirective();

	List<String> getApplicationLoiRef();

	List<String> getTranspositionOrdonnanceRef();

	List<String> getTranspositionDirectiveRef();

	String getHabilitationRefLoi();

	String getHabilitationNumeroArticles();

	String getHabilitationCommentaire();

	Boolean isComplet();

	Boolean isRetourPourModification();
	
	Boolean isTexteEntreprise();

	List<Calendar> getDateEffet();

	void setRetourPourModification(Boolean retourPourModification);

	void setComplet(Boolean complet);

	void setCaseLinkIdsLabels(IdLabel[] caseLinkLabels);

	void setDossierId(String dossierId);

	void setDocIdForSelection(String docIdForSelection);

	void setLocked(Boolean isLocked);

	void setLockOwner(String lockOwner);

	void setDecretArrive(Boolean isDecretArrive);

	void setCaseLinkId(String caseLinkId);

	void setRead(Boolean read);

	void setNumeroNor(String numeroNor);

	void setTitreActe(String titreActe);

	void setDateCreation(Date date);

	void setLastContributor(String lastContributor);

	void setAuthor(String author);

	void setUrgent(Boolean isUrgent);

	void setStatut(String statut);

	void setTypeActe(String typeActe);

	void setMinistereResp(String ministereResp);

	void setDirectionResp(String directionResp);

	void setMinistereAttache(String ministereAttache);

	void setDirectionAttache(String directionAttache);

	void setNomRespDossier(String nomRespDossier);

	void setPrenomRespDossier(String prenomRespDossier);

	void setQualiteRespDossier(String qualiteRespDossier);

	void setTelephoneRespDossier(String telephoneRespDossier);

	void setMailRespDossier(String mailRespDossier);

	void setCategorieActe(String categorieActe);

	void setBaseLegaleActe(String baseLegaleActe);

	void setDatePublication(Date calendar);

	void setPublicationRapportPresentation(Boolean publicationRapportPresentation);

	void setSectionCe(String sectionCe);

	void setRapporteurCe(String rapporteurCe);

	void setDateTransmissionSectionCe(Date date);

	void setDateSectionCe(Date date);

	void setDateAgCe(Date date);

	void setNumeroISA(String numeroISA);

	void setNomCompletChargesMissions(List<String> nomCompletChargesMissions);

	void setNomCompletConseillersPms(List<String> nomCompletConseillersPms);

	void setDateSignature(Date date);

	void setDatePourFournitureEpreuve(Date date);

	void setVecteurPublication(List<String> vecteurPublication);

	void setPublicationsConjointes(List<String> publicationsConjointes);

	void setPublicationIntegraleOuExtrait(String publicationIntegraleOuExtrait);

	void setDecretNumerote(Boolean decretNumerote);

	void setModeParution(String modeParution);

	void setDateParutionJorf(Date date);

	void setNumeroTexteParutionJorf(String numeroTexteParutionJorf);

	void setPageParutionJorf(Long pageParutionJorf);

	void setParutionBo(List<ParutionBo> parutionBo);

	void setDelaiPublication(String delaiPublication);

	void setDatePreciseePublication(Date date);

	void setZoneComSggDila(String zoneComSggDila);

	void setIndexationRubrique(List<String> indexationRubrique);

	void setIndexationMotsCles(List<String> indexationMotsCles);

	void setIndexationChampLibre(List<String> indexationChampLibre);

	void setApplicationLoi(List<ComplexeType> applicationLoi);

	void setTranspositionOrdonnance(List<ComplexeType> transpositionOrdonnance);

	void setTranspositionDirective(List<ComplexeType> transpositionDirective);

	void setApplicationLoiRef(List<String> applicationLoi);

	void setTranspositionOrdonnanceRef(List<String> transpositionOrdonnance);

	void setTranspositionDirectiveRef(List<String> transpositionDirective);

	void setHabilitationRefLoi(String habilitationRefLoi);

	void setHabilitationNumeroArticles(String habilitationNumeroArticles);

	void setHabilitationCommentaire(String habilitationCommentaire);

	void setFromCaseLink(Boolean fromCaseLink);

	void setPriorite(String priorite);

	Boolean isFromCaseLink();

	void setDateEffet(List<Calendar> dateEffet);
	
	void setTexteEntreprise(Boolean te);
}
