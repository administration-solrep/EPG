package fr.dila.solonepg.api.cases;

import java.util.Calendar;
import java.util.List;

/**
 * Decret.
 * 
 * @author asatre
 * 
 */
public interface Decret extends TexteMaitre {

	String getId();

	String getReferenceAvisCE();

	void setReferenceAvisCE(String referenceAvisCE);

	String getNumerosTextes();

	void setNumerosTextes(String numerosTextes);

	Calendar getDateSignature();

	void setDateSignature(Calendar dateSignature);

	String getDelaiPublication();

	void setDelaiPublication(String delaiPublication);

	Calendar getDateSectionCe();

	void setDateSectionCe(Calendar dateSectionCe);

	Calendar getDateSaisineCE();

	void setDateSaisineCE(Calendar dateSaisineCE);

	Calendar getDateSortieCE();

	void setDateSortieCE(Calendar dateSortieCE);

	Long getNumeroPage();

	void setNumeroPage(Long pageParutionJorf);

	String getRapporteurCe();

	void setRapporteurCe(String rapporteurCe);

	String getSectionCe();

	void setSectionCe(String sectionCe);

	String getTypeActe();

	void setTypeActe(String typeActe);

	List<String> getMesureIds();

	void setMesureIds(List<String> idMesures);

	String getNumeroJOPublication();

	void setNumeroJOPublication(String numeroJOPublication);

	Boolean isDateSectionCeLocked();

	void setDateSectionCeLocked(Boolean dateSectionCeLocked);

	Boolean isDateSaisineCELocked();

	void setDateSaisineCELocked(Boolean dateSaisineCELocked);

	Boolean isDateSortieCELocked();

	void setDateSortieCELocked(Boolean dateSortieCELocked);

	Boolean isNumeroJOPublicationLocked();

	void setNumeroJOPublicationLocked(Boolean numeroJOPublication);

	Boolean isNumeroPageLocked();

	void setNumeroPageLocked(Boolean numeroPageLocked);

	Boolean isRapporteurCeLocked();

	void setRapporteurCeLocked(Boolean rapporteurCeLocked);

	Boolean isSectionCeLocked();

	void setSectionCeLocked(Boolean sectionCeLocked);

	Boolean isTypeActeLocked();

	void setTypeActeLocked(Boolean typeActeLocked);

	List<String> getOrdonnanceIds();

	void setOrdonnanceIds(List<String> idsOrdonnance);

	String getReferenceDispositionRatification();

	void setReferenceDispositionRatification(String referenceDispositionRatification);

}
