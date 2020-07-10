package fr.dila.solonepg.core.cases;

import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.st.core.domain.STDomainObjectImpl;

/**
 * Implementation de {@link TexteMaitre} .
 * 
 * @author asatre
 * 
 */
public class TexteMaitreImpl extends STDomainObjectImpl implements TexteMaitre {

	private static final long	serialVersionUID	= 4376293619619241293L;

	public TexteMaitreImpl(DocumentModel doc) {
		super(doc);
	}

	@Override
	public String getNumeroInterne() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.NUMERO_INTERNE);
	}

	@Override
	public void setNumeroInterne(String numeroInterne) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_INTERNE, numeroInterne);
	}

	@Override
	public String getMotCle() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.MOT_CLE);
	}

	@Override
	public void setMotCle(String motCle) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.MOT_CLE, motCle);
	}

	@Override
	public Calendar getDateEntreeEnVigueur() {
		return PropertyUtil.getCalendarProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.DATE_ENTREE_EN_VIGUEUR);
	}

	@Override
	public void setDateEntreeEnVigueur(Calendar dateEntreeEnVigueur) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_ENTREE_EN_VIGUEUR,
				dateEntreeEnVigueur);
	}

	@Override
	public String getObservation() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.OBSERVATION);
	}

	@Override
	public void setObservation(String observation) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.OBSERVATION, observation);
	}

	@Override
	public String getChampLibre() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.CHAMP_LIBRE);
	}

	@Override
	public void setChampLibre(String champLibre) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CHAMP_LIBRE, champLibre);
	}

	@Override
	public Boolean isApplicationDirecte() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.APPLICATION_DIRECTE);
	}

	@Override
	public void setApplicationDirecte(Boolean applicationDirecte) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.APPLICATION_DIRECTE,
				applicationDirecte);
	}

	@Override
	public Calendar getDatePublication() {
		return PropertyUtil.getCalendarProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.DATE_PUBLICATION);
	}

	@Override
	public void setDatePublication(Calendar datePublication) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_PUBLICATION, datePublication);
	}

	@Override
	public Calendar getDatePromulgation() {
		return PropertyUtil.getCalendarProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.DATE_PROMULGATION);
	}

	@Override
	public void setDatePromulgation(Calendar datePromulgation) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_PROMULGATION, datePromulgation);
	}

	@Override
	public String getTitreOfficiel() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.TITRE_OFFICIEL);
	}

	@Override
	public void setTitreOfficiel(String titreOfficiel) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TITRE_OFFICIEL, titreOfficiel);
	}

	@Override
	public String getLegislaturePublication() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.LEGISLATURE_PUBLICATION);
	}

	@Override
	public void setLegislaturePublication(String legislaturePublication) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.LEGISLATURE_PUBLICATION,
				legislaturePublication);
	}

	@Override
	public String getNatureTexte() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.NATURE_TEXTE);
	}

	@Override
	public void setNatureTexte(String natureTexte) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NATURE_TEXTE, natureTexte);
	}

	@Override
	public String getProcedureVote() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.PROCEDURE_VOTE);
	}

	@Override
	public void setProcedureVote(String procedureVote) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.PROCEDURE_VOTE, procedureVote);
	}

	@Override
	public String getCommissionAssNationale() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.COMMISSION_ASS_NATIONALE);
	}

	@Override
	public void setCommissionAssNationale(String commissionAssNationale) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.COMMISSION_ASS_NATIONALE,
				commissionAssNationale);
	}

	@Override
	public String getCommissionSenat() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.COMMISSION_SENAT);
	}

	@Override
	public void setCommissionSenat(String commissionSenat) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.COMMISSION_SENAT, commissionSenat);
	}

	@Override
	public String getTitreActe() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.TITRE_ACTE);
	}

	@Override
	public void setTitreActe(String titreActe) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TITRE_ACTE, titreActe);
	}

	@Override
	public String getMinisterePilote() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.MINISTERE_PILOTE);
	}

	@Override
	public void setMinisterePilote(String ministerePilote) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.MINISTERE_PILOTE, ministerePilote);
	}

	@Override
	public String getObjet() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.OBJET);
	}

	@Override
	public void setObjet(String objet) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.OBJET, objet);
	}

	@Override
	public Boolean getCodification() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.CODIFICATION);
	}

	@Override
	public void setCodification(Boolean codification) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.CODIFICATION, codification);
	}

	@Override
	public Boolean isNumeroInterneLocked() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.NUMERO_INTERNE_LOCKED);
	}

	@Override
	public void setNumeroInterneLocked(Boolean isNumeroInterneLocked) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_INTERNE_LOCKED,
				isNumeroInterneLocked);
	}

	@Override
	public Boolean isDateEntreeEnVigueurLocked() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.DATE_ENTREE_EN_VIGUEUR_LOCKED);
	}

	@Override
	public void setDateEntreeEnVigueurLocked(Boolean isDateEntreeEnVigueurLocked) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_ENTREE_EN_VIGUEUR_LOCKED,
				isDateEntreeEnVigueurLocked);
	}

	@Override
	public Boolean isDatePublicationLocked() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.DATE_PUBLICATION_LOCKED);
	}

	@Override
	public void setDatePublicationLocked(Boolean isDatePublicationLocked) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_PUBLICATION_LOCKED,
				isDatePublicationLocked);
	}

	@Override
	public Boolean isDatePromulgationLocked() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.DATE_PROMULGATION_LOCKED);
	}

	@Override
	public void setDatePromulgationLocked(Boolean isDatePromulgationLocked) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_PROMULGATION_LOCKED,
				isDatePromulgationLocked);
	}

	@Override
	public Boolean isTitreOfficielLocked() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.TITRE_OFFICIEL_LOCKED);
	}

	@Override
	public void setTitreOfficielLocked(Boolean isTitreOfficielLocked) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TITRE_OFFICIEL_LOCKED,
				isTitreOfficielLocked);
	}

	@Override
	public Boolean isLegislaturePublicationLocked() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.LEGISLATURE_PUBLICATION_LOCKED);
	}

	@Override
	public void setLegislaturePublicationLocked(Boolean isLegislaturePublicationLocked) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.LEGISLATURE_PUBLICATION_LOCKED,
				isLegislaturePublicationLocked);
	}

	@Override
	public Boolean isNatureTexteLocked() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.NATURE_TEXTE_LOCKED);
	}

	@Override
	public void setNatureTexteLocked(Boolean isNatureTexteLocked) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NATURE_TEXTE_LOCKED,
				isNatureTexteLocked);
	}

	@Override
	public Boolean isProcedureVoteLocked() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.PROCEDURE_VOTE_LOCKED);
	}

	@Override
	public void setProcedureVoteLocked(Boolean isProcedureVoteLocked) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.PROCEDURE_VOTE_LOCKED,
				isProcedureVoteLocked);
	}

	@Override
	public Boolean isCommissionAssNationaleLocked() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.COMMISSION_ASS_NATIONALE_LOCKED);
	}

	@Override
	public void setCommissionAssNationaleLocked(Boolean isCommissionAssNationaleLocked) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.COMMISSION_ASS_NATIONALE_LOCKED,
				isCommissionAssNationaleLocked);
	}

	@Override
	public Boolean isCommissionSenatLocked() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.COMMISSION_SENAT_LOCKED);
	}

	@Override
	public void setCommissionSenatLocked(Boolean isCommissionSenatLocked) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.COMMISSION_SENAT_LOCKED,
				isCommissionSenatLocked);
	}

	@Override
	public Boolean isTitreActeLocked() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.TITRE_ACTE_LOCKED);
	}

	@Override
	public void setTitreActeLocked(Boolean isTitreActeLocked) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.TITRE_ACTE_LOCKED, isTitreActeLocked);
	}

	@Override
	public Boolean isMinistereRespLocked() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.MINISTERE_PILOTE_LOCKED);
	}

	@Override
	public void setMinistereRespLocked(Boolean isMinistereRespLocked) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.MINISTERE_PILOTE_LOCKED,
				isMinistereRespLocked);
	}

	@Override
	public Boolean isDispositionHabilitation() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.DISPOSITION_HABILITATION);
	}

	@Override
	public void setDispositionHabilitation(Boolean dispositionHabilitation) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DISPOSITION_HABILITATION,
				dispositionHabilitation);
	}

	@Override
	public String getNumero() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.NUMERO);
	}

	@Override
	public void setNumero(String numeroLoi) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO, numeroLoi);
	}

	@Override
	public Boolean isNumeroLocked() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.NUMERO_LOCKED);
	}

	@Override
	public void setNumeroLocked(Boolean numeroLoiLocked) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_LOCKED, numeroLoiLocked);
	}

	@Override
	public Boolean isRenvoiDecret() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.RENVOI_DECRET);
	}

	@Override
	public void setRenvoiDecret(Boolean renvoiDecret) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.RENVOI_DECRET, renvoiDecret);
	}

	@Override
	public List<String> getMesuresIds() {
		return PropertyUtil.getStringListProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.MESURE_IDS);
	}

	@Override
	public void setMesuresIds(List<String> mesuresIds) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.MESURE_IDS, mesuresIds);
	}

	@Override
	public List<String> getLoiRatificationIds() {
		return PropertyUtil.getStringListProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.LOI_RATIFICATION_IDS);
	}

	@Override
	public void setLoiRatificationIds(List<String> loiRatificationIds) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.LOI_RATIFICATION_IDS,
				loiRatificationIds);
	}

	@Override
	public List<String> getDecretIds() {
		return PropertyUtil.getStringListProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.DECRET_IDS);
	}

	@Override
	public void setDecretIds(List<String> decretIds) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DECRET_IDS, decretIds);
	}

	@Override
	public String getNumeroNor() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.NUMERO_NOR);
	}

	@Override
	public void setNumeroNor(String numeroNor) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_NOR, numeroNor);
	}

	@Override
	public String getNumeroOrdre() {
		return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_ORDRE);
	}

	@Override
	public void setNumeroOrdre(String numeroOrdre) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.NUMERO_ORDRE, numeroOrdre);
	}

	@Override
	public String getIdDossier() {
		return getStringProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ID_DOSSIER);
	}

	@Override
	public void setIdDossier(String idDossier) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ID_DOSSIER, idDossier);
	}

	@Override
	public List<String> getHabilitationIds() {
		return PropertyUtil.getStringListProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.HABILITATION_IDS);
	}

	@Override
	public void setHabilitationIds(List<String> habilitationIds) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.HABILITATION_IDS, habilitationIds);
	}

	@Override
	public String getDocLockUser() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.DOC_LOCK_USER);
	}

	@Override
	public void setDocLockUser(String docLockUser) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DOC_LOCK_USER, docLockUser);
	}

	@Override
	public Calendar getDocLockDate() {
		return PropertyUtil.getCalendarProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.DOC_LOCK_DATE);
	}

	@Override
	public void setDocLockDate(Calendar docLockDate) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DOC_LOCK_DATE, docLockDate);
	}

	@Override
	public Boolean isRatifie() {
		return PropertyUtil.getBooleanProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.RATIFIE);
	}

	@Override
	public void setRatifie(Boolean ratifie) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.RATIFIE, ratifie);
	}

	@Override
	public <T> T getAdapter(Class<T> itf) {
		return getDocument().getAdapter(itf);
	}

	@Override
	public String getEtapeSolon() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.ETAPE_SOLON);
	}

	@Override
	public void setEtapeSolon(String etapeSolon) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.ETAPE_SOLON, etapeSolon);
	}

	@Override
	public Calendar getDateCreation() {
		return PropertyUtil.getCalendarProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.DATE_CREATION);
	}

	@Override
	public void setDateCreation(Calendar dateCreation) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_CREATION, dateCreation);
	}

	@Override
	public Calendar getDateReunionProgrammation() {
		return PropertyUtil.getCalendarProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.DATE_REUNION_PROGRAMMATION);
	}

	@Override
	public void setDateReunionProgrammation(Calendar dateReunionProgrammation) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_REUNION_PROGRAMMATION,
				dateReunionProgrammation);
	}

	@Override
	public Calendar getDateCirculationCompteRendu() {
		return PropertyUtil.getCalendarProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.DATE_CIRCULATION_COMPTE_RENDU);
	}

	@Override
	public void setDateCirculationCompteRendu(Calendar dateCirculationCompteRendu) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_CIRCULATION_COMPTE_RENDU,
				dateCirculationCompteRendu);
	}

	@Override
	public Boolean hasValidation() {
		return getBooleanProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.HAS_VALIDATION);
	}

	@Override
	public void setValidation(Boolean validation) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.HAS_VALIDATION, validation);
	}

	@Override
	public String getCommunicationMinisterielle() {
		return PropertyUtil.getStringProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.COMMUNICATION_MINISTERIELLE);
	}

	@Override
	public void setCommunicationMinisterielle(String communicationMinisterielle) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.COMMUNICATION_MINISTERIELLE,
				communicationMinisterielle);
	}

	@Override
	public Calendar getDateModification() {
		return PropertyUtil.getCalendarProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.DATE_MODIFICATION);
	}

	@Override
	public void setDateModification(Calendar dateModification) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_MODIFICATION, dateModification);
	}
	
	@Override
	public Calendar getDateInjection() {
		return PropertyUtil.getCalendarProperty(document, TexteMaitreConstants.TEXTE_MAITRE_SCHEMA,
				TexteMaitreConstants.DATE_INJECTION);
	}

	@Override
	public void setDateInjection(Calendar dateInjection) {
		setProperty(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA, TexteMaitreConstants.DATE_INJECTION, dateInjection);
	}
}
