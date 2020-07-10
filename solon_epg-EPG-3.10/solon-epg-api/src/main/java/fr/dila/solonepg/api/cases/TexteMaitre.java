package fr.dila.solonepg.api.cases;

import java.util.Calendar;
import java.util.List;

import fr.dila.st.api.domain.STDomainObject;

/**
 * Interface pour les Textes-Maîtres de l'activite normative.
 * 
 * @author asatre
 * 
 */
public interface TexteMaitre extends STDomainObject {

	String getNumeroNor();

	void setNumeroNor(String numeroNor);

	String getNumeroInterne();

	void setNumeroInterne(String numeroInterne);

	String getNumero();

	void setNumero(String numero);

	String getMotCle();

	void setMotCle(String motCle);

	Calendar getDateEntreeEnVigueur();

	void setDateEntreeEnVigueur(Calendar dateEntreeEnVigueur);

	String getObservation();

	void setObservation(String observation);

	String getChampLibre();

	void setChampLibre(String champLibre);

	Boolean isApplicationDirecte();

	void setApplicationDirecte(Boolean applicationDirecte);

	Calendar getDatePublication();

	void setDatePublication(Calendar datePublication);

	Calendar getDatePromulgation();

	void setDatePromulgation(Calendar datePromulgation);

	String getTitreOfficiel();

	void setTitreOfficiel(String titreOfficiel);

	String getLegislaturePublication();

	void setLegislaturePublication(String legislaturePublication);

	String getNatureTexte();

	void setNatureTexte(String natureTexte);

	String getProcedureVote();

	void setProcedureVote(String procedureVote);

	String getCommissionAssNationale();

	void setCommissionAssNationale(String commissionAssNationale);

	String getCommissionSenat();

	void setCommissionSenat(String commissionSenat);

	String getTitreActe();

	void setTitreActe(String titreActe);

	String getMinisterePilote();

	void setMinisterePilote(String ministereResp);

	String getObjet();

	void setObjet(String objet);

	Boolean getCodification();

	void setCodification(Boolean codification);

	String getDocLockUser();

	void setDocLockUser(String docLockUser);

	Calendar getDocLockDate();

	void setDocLockDate(Calendar docLockDate);

	String getCommunicationMinisterielle();

	void setCommunicationMinisterielle(String communicationMinisterielle);

	/**
	 * true if 38C
	 * 
	 * @return
	 */
	Boolean isDispositionHabilitation();

	void setDispositionHabilitation(Boolean dispositionHabilitation);

	Boolean isRenvoiDecret();

	void setRenvoiDecret(Boolean renvoiDecret);

	Boolean isRatifie();

	void setRatifie(Boolean ratifie);

	List<String> getMesuresIds();

	void setMesuresIds(List<String> mesuresIds);

	List<String> getLoiRatificationIds();

	void setLoiRatificationIds(List<String> loiRatificationIds);

	List<String> getHabilitationIds();

	void setHabilitationIds(List<String> habilitationIds);

	List<String> getDecretIds();

	void setDecretIds(List<String> decretIds);

	String getNumeroOrdre();

	void setNumeroOrdre(String numeroOrdre);

	String getIdDossier();

	void setIdDossier(String idDossier);

	Calendar getDateReunionProgrammation();

	void setDateReunionProgrammation(Calendar dateReunionProgrammation);

	Calendar getDateCirculationCompteRendu();

	void setDateCirculationCompteRendu(Calendar dateCirculationCompteRendu);

	// //////////////////
	// // Partie Lock ///
	// //////////////////

	Boolean isNumeroInterneLocked();

	void setNumeroInterneLocked(Boolean numeroInterne);

	Boolean isNumeroLocked();

	void setNumeroLocked(Boolean numeroLoiLocked);

	Boolean isDateEntreeEnVigueurLocked();

	void setDateEntreeEnVigueurLocked(Boolean isDateEntreeEnVigueurLocked);

	Boolean isDatePublicationLocked();

	void setDatePublicationLocked(Boolean isDatePublicationLocked);

	Boolean isDatePromulgationLocked();

	void setDatePromulgationLocked(Boolean isDatePromulgationLocked);

	Boolean isTitreOfficielLocked();

	void setTitreOfficielLocked(Boolean isTitreOfficielLocked);

	Boolean isLegislaturePublicationLocked();

	void setLegislaturePublicationLocked(Boolean isLegislaturePublicationLocked);

	Boolean isNatureTexteLocked();

	void setNatureTexteLocked(Boolean isNatureTexteLocked);

	Boolean isProcedureVoteLocked();

	void setProcedureVoteLocked(Boolean isProcedureVoteLocked);

	Boolean isCommissionAssNationaleLocked();

	void setCommissionAssNationaleLocked(Boolean isCommissionAssNationaleLocked);

	Boolean isCommissionSenatLocked();

	void setCommissionSenatLocked(Boolean isCommissionSenatLocked);

	Boolean isTitreActeLocked();

	void setTitreActeLocked(Boolean isTitreActeLocked);

	Boolean isMinistereRespLocked();

	void setMinistereRespLocked(Boolean isMinistereRespLocked);

	<T> T getAdapter(Class<T> itf);

	String getEtapeSolon();

	void setEtapeSolon(String etapeSolon);

	Calendar getDateCreation();

	void setDateCreation(Calendar dateCreation);

	/**
	 * Indique si le texte maitre est valide dans le PAN (i.e. : validé par l'utilisateur ou automatiquement)
	 * 
	 * @return
	 */
	Boolean hasValidation();

	/**
	 * Valide ou invalide le texte maitre
	 * 
	 * @param validation
	 */
	void setValidation(Boolean validation);

	Calendar getDateModification();

	void setDateModification(Calendar dateModification);
	
	Calendar getDateInjection();

	void setDateInjection(Calendar dateInjection);
}
