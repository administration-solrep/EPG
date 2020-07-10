package fr.dila.solonepg.api.cases;

import java.io.Serializable;
import java.util.Calendar;

import fr.dila.st.api.domain.STDomainObject;

/**
 * Conseil Etat.
 * 
 * @author asatre
 * 
 */
public interface ConseilEtat extends STDomainObject, Serializable {

	// /////////////////
	// info Conseil Etat (CE)
	// ////////////////

	/**
	 * Getter section Conseil d'Etat
	 * 
	 * @return String
	 */
	String getSectionCe();

	void setSectionCe(String sectionCe);

	/**
	 * Getter rapporteur Conseil d'Etat
	 * 
	 * @return String
	 */
	String getRapporteurCe();

	void setRapporteurCe(String rapporteurCe);

	/**
	 * Getter date de transmission au service Conseil d'Etat
	 * 
	 * @return Calendar
	 */
	Calendar getDateTransmissionSectionCe();

	void setDateTransmissionSectionCe(Calendar dateTransmissionSectionCe);

	/**
	 * Getter date section Conseil d'Etat
	 * 
	 * @return Calendar
	 */
	Calendar getDateSectionCe();

	void setDateSectionCe(Calendar dateSectionCe);

	/**
	 * Getter date Assemblée Générale Conseil d'Etat
	 * 
	 * @return Calendar
	 */
	Calendar getDateAgCe();

	void setDateAgCe(Calendar dateAgCe);

	/**
	 * Getter numero ISA
	 * 
	 * @return String
	 */
	String getNumeroISA();

	void setNumeroISA(String numeroISA);

	Calendar getDateSaisineCE();

	void setDateSaisineCE(Calendar dateSaisineCE);

	Calendar getDateSortieCE();

	void setDateSortieCE(Calendar dateSortieCE);

	/**
	 * Getter priorite Conseil d'Etat
	 * 
	 * @return String
	 */
	String getPriorite();

	void setPriorite(String priorite);
}
