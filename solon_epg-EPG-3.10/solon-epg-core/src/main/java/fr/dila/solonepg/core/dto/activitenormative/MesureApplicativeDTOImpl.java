package fr.dila.solonepg.core.dto.activitenormative;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.dto.MesureApplicativeDTO;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.core.client.AbstractMapDTO;

public class MesureApplicativeDTOImpl extends AbstractMapDTO implements MesureApplicativeDTO {

	private static final String	VALIDATE			= "validate";
	private static final String	DATE_LIMITE			= "dateLimite";
	private static final long	serialVersionUID	= 78416213510924898L;

	public MesureApplicativeDTOImpl(MesureApplicative mesureApplicative, TexteMaitre texteMaitre) {
		setNumeroOrdre(mesureApplicative.getNumeroOrdre());
		setArticle(mesureApplicative.getArticle());
		setCodeModifie(mesureApplicative.getCodeModifie());
		setBaseLegale(mesureApplicative.getBaseLegale());
		setObjetRIM(mesureApplicative.getObjetRIM());
		setTypeMesure(mesureApplicative.getTypeMesure());
		setFromAmendement(mesureApplicative.isFromAmendement() != null ? mesureApplicative.isFromAmendement()
				: Boolean.FALSE);
		setDiffere(mesureApplicative.isDiffere() != null ? mesureApplicative.isDiffere() : Boolean.FALSE);
		setDateEntreeEnVigueur(mesureApplicative.getDateEntreeEnVigueur() != null ? mesureApplicative
				.getDateEntreeEnVigueur().getTime() : null);
		setNumeroQuestion(mesureApplicative.getNumeroQuestion());
		setAmendement(mesureApplicative.isAmendement() != null ? mesureApplicative.isAmendement() : Boolean.FALSE);
		setResponsableAmendement(mesureApplicative.getResponsableAmendement());
		setDecretIds(mesureApplicative.getDecretIds());
		setId(mesureApplicative.getId());
		setValidation(mesureApplicative.hasValidation());
		setValidate(mesureApplicative.hasValidation());

		setMesureApplication(mesureApplicative.getMesureApplication());
		setConsultationsHCE(mesureApplicative.getConsultationsHCE());
		setCalendrierConsultationsHCE(mesureApplicative.getCalendrierConsultationsHCE());
		setDateDisponnibiliteAvantProjet(mesureApplicative.getDateDisponnibiliteAvantProjet() != null ? mesureApplicative
				.getDateDisponnibiliteAvantProjet().getTime() : null);
		setPoleChargeMission(mesureApplicative.getPoleChargeMission());
		setDatePassageCM(mesureApplicative.getDatePassageCM() != null ? mesureApplicative.getDatePassageCM().getTime()
				: null);
		setDateReunionProgrammation(mesureApplicative.getDateReunionProgrammation() != null ? mesureApplicative
				.getDateReunionProgrammation().getTime() : null);
		setDateCirculationCompteRendu(mesureApplicative.getDateCirculationCompteRendu() != null ? mesureApplicative
				.getDateCirculationCompteRendu().getTime() : null);
		setDateObjectifPublication(mesureApplicative.getDateObjectifPublication() != null ? mesureApplicative
				.getDateObjectifPublication().getTime() : null);
		setDateMiseApplication(mesureApplicative.getDateMiseApplication() != null ? mesureApplicative
				.getDateMiseApplication().getTime() : null);
		setMinisterePilote(mesureApplicative.getMinisterePilote());
		setDirectionResponsable(mesureApplicative.getDirectionResponsable());
		setApplicationRecu(mesureApplicative.getApplicationRecu());
		setObservation(mesureApplicative.getObservation());
		setDatePrevisionnelleSaisineCE(mesureApplicative.getDatePrevisionnelleSaisineCE() != null ? mesureApplicative
				.getDatePrevisionnelleSaisineCE().getTime() : null);

		setChampLibre(mesureApplicative.getChampLibre());
		setCommunicationMinisterielle(mesureApplicative.getCommunicationMinisterielle());

		// RG-LOI-MAI-28
		computeDateLimite(texteMaitre);

		setDecretIdsInvalidated(mesureApplicative.getDecretsIdsInvalidated());
	}

	public MesureApplicativeDTOImpl() {
		// constructeur vide pour créer une ligne vide
		setFromAmendement(Boolean.FALSE);
		setDiffere(Boolean.FALSE);
		setAmendement(Boolean.FALSE);
		setValidate(Boolean.FALSE);
		setMesureApplication(Boolean.FALSE);
		setApplicationRecu(Boolean.FALSE);
	}

	/**
	 * RG-LOI-MAI-28
	 * 
	 * @param activiteNormative
	 */
	private void computeDateLimite(TexteMaitre texteMaitre) {

		setDateLimite(texteMaitre.getDatePublication() != null ? texteMaitre.getDatePublication().getTime() : null);
		setDateLimite(texteMaitre.getDateEntreeEnVigueur() != null ? texteMaitre.getDateEntreeEnVigueur().getTime()
				: getDateLimite());
		if (getDateLimite() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getDateLimite());
			cal.add(Calendar.MONTH, 6);
			setDateLimite(cal.getTime());
		}
	}

	@Override
	public MesureApplicative remapField(MesureApplicative mesureApplicative) throws ClientException {
		mesureApplicative.setNumeroOrdre(getNumeroOrdre());
		mesureApplicative.setArticle(getArticle());
		mesureApplicative.setCodeModifie(getCodeModifie());
		mesureApplicative.setBaseLegale(getBaseLegale());
		mesureApplicative.setObjetRIM(getObjetRIM());
		mesureApplicative.setTypeMesure(getTypeMesure());
		mesureApplicative.setFromAmendement(getFromAmendement());
		mesureApplicative.setDiffere(getDiffere());

		if (getDateEntreeEnVigueur() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getDateEntreeEnVigueur());
			mesureApplicative.setDateEntreeEnVigueur(cal);
		} else {
			mesureApplicative.setDateEntreeEnVigueur(null);
		}

		if (getDatePrevisionnelleSaisineCE() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getDatePrevisionnelleSaisineCE());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			mesureApplicative.setDatePrevisionnelleSaisineCE(cal);
		} else {
			mesureApplicative.setDatePrevisionnelleSaisineCE(null);
		}

		mesureApplicative.setAmendement(getAmendement());
		mesureApplicative.setResponsableAmendement(getResponsableAmendement());
		mesureApplicative.setDecretIds(getDecretIds());

		if (getValidate() != null && getValidate() || getId() == null) {
			mesureApplicative.setValidation(Boolean.TRUE);
		} else if (getValidate() != null && !getValidate()) {
			mesureApplicative.setValidation(Boolean.FALSE);
		}

		mesureApplicative.setMesureApplication(getMesureApplication());
		mesureApplicative.setConsultationsHCE(getConsultationsHCE());

		mesureApplicative.setCalendrierConsultationsHCE(getCalendrierConsultationsHCE());

		if (getDateDisponnibiliteAvantProjet() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getDateDisponnibiliteAvantProjet());
			mesureApplicative.setDateDisponnibiliteAvantProjet(cal);
		} else {
			mesureApplicative.setDateDisponnibiliteAvantProjet(null);
		}

		mesureApplicative.setPoleChargeMission(getPoleChargeMission());

		if (getDatePassageCM() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getDatePassageCM());
			mesureApplicative.setDatePassageCM(cal);
		} else {
			mesureApplicative.setDatePassageCM(null);
		}

		if (getDateReunionProgrammation() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getDateReunionProgrammation());
			mesureApplicative.setDateReunionProgrammation(cal);
		} else {
			mesureApplicative.setDateReunionProgrammation(null);
		}

		if (getDateCirculationCompteRendu() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getDateCirculationCompteRendu());
			mesureApplicative.setDateCirculationCompteRendu(cal);
		} else {
			mesureApplicative.setDateCirculationCompteRendu(null);
		}

		if (getDateObjectifPublication() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getDateObjectifPublication());
			cal.set(Calendar.DAY_OF_MONTH, 1);
			mesureApplicative.setDateObjectifPublication(cal);
		} else {
			mesureApplicative.setDateObjectifPublication(null);
		}

		mesureApplicative.setMinisterePilote(getMinisterePilote());
		mesureApplicative.setDirectionResponsable(getDirectionResponsable());
		mesureApplicative.setApplicationRecu(getApplicationRecu());

		mesureApplicative.setObservation(getObservation());

		mesureApplicative.setChampLibre(getChampLibre());
		mesureApplicative.setCommunicationMinisterielle(getCommunicationMinisterielle());
		// TCH: mantis0051671: DILA 0149561: application des lois - mesures d'application - champ Numéro question
		// parlementaire
		mesureApplicative.setNumeroQuestion(getNumeroQuestion());

		if (getDateMiseApplication() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getDateMiseApplication());
			mesureApplicative.setDateMiseApplication(cal);
		} else {
			mesureApplicative.setDateMiseApplication(null);
		}
		return mesureApplicative;
	}

	@Override
	public String getNumeroOrdre() {
		return getString(TexteMaitreConstants.NUMERO_ORDRE);
	}

	@Override
	public void setNumeroOrdre(String numeroOrdre) {
		put(TexteMaitreConstants.NUMERO_ORDRE, numeroOrdre);
	}

	@Override
	public String getArticle() {
		return getString(TexteMaitreConstants.ARTICLE);
	}

	@Override
	public void setArticle(String article) {
		put(TexteMaitreConstants.ARTICLE, article);
	}

	@Override
	public String getCodeModifie() {
		return getString(TexteMaitreConstants.CODE_MODIFIE);
	}

	@Override
	public void setCodeModifie(String codeModifie) {
		put(TexteMaitreConstants.CODE_MODIFIE, codeModifie);
	}

	@Override
	public String getBaseLegale() {
		return getString(TexteMaitreConstants.BASE_LEGALE);
	}

	@Override
	public void setBaseLegale(String baseLegale) {
		put(TexteMaitreConstants.BASE_LEGALE, baseLegale);
	}

	@Override
	public String getObjetRIM() {
		return getString(TexteMaitreConstants.OBJET_RIM);
	}

	@Override
	public void setObjetRIM(String objetRIM) {
		put(TexteMaitreConstants.OBJET_RIM, objetRIM);
	}

	@Override
	public String getTypeMesure() {
		return getString(TexteMaitreConstants.TYPE_MESURE);
	}

	@Override
	public void setTypeMesure(String typeMesure) {
		put(TexteMaitreConstants.TYPE_MESURE, typeMesure);
	}

	@Override
	public Boolean getFromAmendement() {
		return getBoolean(TexteMaitreConstants.FROM_AMENDEMENT);
	}

	@Override
	public void setFromAmendement(Boolean fromAmendement) {
		put(TexteMaitreConstants.FROM_AMENDEMENT, fromAmendement);
	}

	@Override
	public Boolean getDiffere() {
		return getBoolean(TexteMaitreConstants.DIFFERE);
	}

	@Override
	public void setDiffere(Boolean differe) {
		put(TexteMaitreConstants.DIFFERE, differe);
	}

	@Override
	public Date getDateEntreeEnVigueur() {
		return getDate(TexteMaitreConstants.DATE_ENTREE_EN_VIGUEUR);
	}

	@Override
	public void setDateEntreeEnVigueur(Date dateEntreeEnVigueur) {
		put(TexteMaitreConstants.DATE_ENTREE_EN_VIGUEUR, dateEntreeEnVigueur);
	}

	@Override
	public String getNumeroQuestion() {
		return getString(TexteMaitreConstants.NUMERO_QUESTION);
	}

	@Override
	public void setNumeroQuestion(String numeroQuestion) {
		put(TexteMaitreConstants.NUMERO_QUESTION, numeroQuestion);
	}

	@Override
	public Boolean getAmendement() {
		return getBoolean(TexteMaitreConstants.AMENDEMENT);
	}

	@Override
	public void setAmendement(Boolean amendement) {
		put(TexteMaitreConstants.AMENDEMENT, amendement);
	}

	@Override
	public String getResponsableAmendement() {
		return getString(TexteMaitreConstants.RESPONSABLE_AMENDEMENT);
	}

	@Override
	public void setResponsableAmendement(String responsableAmendement) {
		put(TexteMaitreConstants.RESPONSABLE_AMENDEMENT, responsableAmendement);
	}

	public Date getDateLimite() {
		return getDate(DATE_LIMITE);
	}

	public void setDateLimite(Date dateLimite) {
		put(DATE_LIMITE, dateLimite);
	}

	@Override
	public String getType() {
		return "Mesure";
	}

	@Override
	public String getDocIdForSelection() {
		return getId();
	}

	@Override
	public String getId() {
		return getString(STSchemaConstant.ECM_UUID_PROPERTY);
	}

	@Override
	public void setId(String id) {
		put(STSchemaConstant.ECM_UUID_PROPERTY, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getDecretIds() {
		return (List<String>) get(TexteMaitreConstants.DECRET_IDS);
	}

	@Override
	public void setDecretIds(List<String> decretIds) {
		put(TexteMaitreConstants.DECRET_IDS, (Serializable) decretIds);
	}

	@Override
	public void addDecret(String idDossier) {
		List<String> decertIds = getDecretIds();
		if (decertIds == null) {
			decertIds = new ArrayList<String>();
		}
		decertIds.add(idDossier);
		setDecretIds(decertIds);
	}

	@Override
	public Boolean hasValidation() {
		return getBoolean(TexteMaitreConstants.HAS_VALIDATION);
	}

	@Override
	public void setValidation(Boolean validation) {
		put(TexteMaitreConstants.HAS_VALIDATION, validation);
	}

	@Override
	public String toString() {
		return "[MesureApplicativeDTO : Id :" + getId() + ", NumeroOrdre : " + getNumeroOrdre() + "]";
	}

	@Override
	public String getMinisterePilote() {
		return getString(TexteMaitreConstants.MINISTERE_PILOTE);
	}

	@Override
	public void setMinisterePilote(String ministerePilote) {
		put(TexteMaitreConstants.MINISTERE_PILOTE, ministerePilote);
	}

	@Override
	public String getDirectionResponsable() {
		return getString(TexteMaitreConstants.DIRECTION_RESPONSABLE);
	}

	@Override
	public void setDirectionResponsable(String directionResponsable) {
		put(TexteMaitreConstants.DIRECTION_RESPONSABLE, directionResponsable);
	}

	@Override
	public Boolean getMesureApplication() {
		return getBoolean(TexteMaitreConstants.MESURE_APPLICATION);
	}

	@Override
	public void setMesureApplication(Boolean mesureApplication) {
		put(TexteMaitreConstants.MESURE_APPLICATION, mesureApplication);
	}

	@Override
	public Date getDateReunionProgrammation() {
		return getDate(TexteMaitreConstants.DATE_REUNION_PROGRAMMATION);
	}

	@Override
	public void setDateReunionProgrammation(Date dateReunionProgrammation) {
		put(TexteMaitreConstants.DATE_REUNION_PROGRAMMATION, dateReunionProgrammation);
	}

	@Override
	public Date getDateCirculationCompteRendu() {
		return getDate(TexteMaitreConstants.DATE_CIRCULATION_COMPTE_RENDU);
	}

	@Override
	public void setDateCirculationCompteRendu(Date dateCirculationCompteRendu) {
		put(TexteMaitreConstants.DATE_CIRCULATION_COMPTE_RENDU, dateCirculationCompteRendu);
	}

	@Override
	public String getConsultationsHCE() {
		return getString(TexteMaitreConstants.CONSULTATIONS_HCE);
	}

	@Override
	public void setConsultationsHCE(String consultationsHCE) {
		put(TexteMaitreConstants.CONSULTATIONS_HCE, consultationsHCE);
	}

	@Override
	public String getCalendrierConsultationsHCE() {
		return getString(TexteMaitreConstants.CALENDRIER_CONSULTATIONS_HCE);
	}

	@Override
	public void setCalendrierConsultationsHCE(String calendrierConsultationsHCE) {
		put(TexteMaitreConstants.CALENDRIER_CONSULTATIONS_HCE, calendrierConsultationsHCE);
	}

	@Override
	public Date getDateDisponnibiliteAvantProjet() {
		return getDate(TexteMaitreConstants.DATE_DISPONIBILITE_AVANT_PROJET);
	}

	@Override
	public void setDateDisponnibiliteAvantProjet(Date dateDisponibiliteAvantProjet) {
		put(TexteMaitreConstants.DATE_DISPONIBILITE_AVANT_PROJET, dateDisponibiliteAvantProjet);
	}

	@Override
	public String getPoleChargeMission() {
		return getString(TexteMaitreConstants.POLE_CHARGE_MISSION);
	}

	@Override
	public void setPoleChargeMission(String poleChargeMission) {
		put(TexteMaitreConstants.POLE_CHARGE_MISSION, poleChargeMission);
	}

	@Override
	public Date getDatePassageCM() {
		return getDate(TexteMaitreConstants.DATE_PASSAGE_CM);
	}

	@Override
	public void setDatePassageCM(Date datePassageCM) {
		put(TexteMaitreConstants.DATE_PASSAGE_CM, datePassageCM);
	}

	@Override
	public Boolean getApplicationRecu() {
		return getBoolean(TexteMaitreConstants.APPLICATION_RECU);
	}

	@Override
	public void setApplicationRecu(Boolean applicationRecu) {
		put(TexteMaitreConstants.APPLICATION_RECU, applicationRecu);
	}

	@Override
	public Boolean getValidate() {
		return getBoolean(VALIDATE);
	}

	@Override
	public void setValidate(Boolean validation) {
		put(VALIDATE, validation);
	}

	@Override
	public Date getDateObjectifPublication() {
		return getDate(TexteMaitreConstants.DATE_OBJECTIF_PUBLICATION);
	}

	@Override
	public void setDateObjectifPublication(Date dateObjectifPublication) {
		put(TexteMaitreConstants.DATE_OBJECTIF_PUBLICATION, dateObjectifPublication);
	}

	@Override
	public String getObservation() {
		return getString(TexteMaitreConstants.OBSERVATION);
	}

	@Override
	public void setObservation(String observation) {
		put(TexteMaitreConstants.OBSERVATION, observation);
	}

	@Override
	public Date getDatePrevisionnelleSaisineCE() {
		return getDate(TexteMaitreConstants.DATE_PREVISIONNELLE_SAISINE_CE);
	}

	@Override
	public void setDatePrevisionnelleSaisineCE(Date datePrevisionnelleSaisineCE) {
		put(TexteMaitreConstants.DATE_PREVISIONNELLE_SAISINE_CE, datePrevisionnelleSaisineCE);
	}

	@Override
	public Date getDateMiseApplication() {
		return getDate(TexteMaitreConstants.DATE_MISE_APPLICATION);
	}

	@Override
	public void setDateMiseApplication(Date dateMiseApplication) {
		put(TexteMaitreConstants.DATE_MISE_APPLICATION, dateMiseApplication);
	}

	@Override
	public String getChampLibre() {
		return getString(TexteMaitreConstants.CHAMP_LIBRE);
	}

	@Override
	public void setChampLibre(String champLibre) {
		put(TexteMaitreConstants.CHAMP_LIBRE, champLibre);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getDecretIdsInvalidated() {
		return (List<String>) get(TexteMaitreConstants.IDS_INVALIDATED);
	}

	@Override
	public void setDecretIdsInvalidated(List<String> decretIds) {
		put(TexteMaitreConstants.IDS_INVALIDATED, (Serializable) decretIds);
	}

	@Override
	public String getCommunicationMinisterielle() {
		return getString(TexteMaitreConstants.COMMUNICATION_MINISTERIELLE);
	}

	@Override
	public void setCommunicationMinisterielle(String communicationMinisterielle) {
		put(TexteMaitreConstants.COMMUNICATION_MINISTERIELLE, communicationMinisterielle);
	}

}
