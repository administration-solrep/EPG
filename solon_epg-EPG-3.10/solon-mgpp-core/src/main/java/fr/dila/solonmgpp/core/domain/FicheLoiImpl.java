package fr.dila.solonmgpp.core.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.core.util.PropertyUtil;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.sword.xsd.solon.epp.NatureLoi;

public class FicheLoiImpl implements FicheLoi, Serializable {

	private static final long	serialVersionUID	= -3193942676429367582L;

	private final DocumentModel	document;

	public FicheLoiImpl(DocumentModel doc) {
		this.document = doc;
	}

	@Override
	public String getIdDossier() {
		return PropertyUtil.getStringProperty(document, SCHEMA, ID_DOSSIER);
	}

	@Override
	public void setIdDossier(String idDossier) {
		PropertyUtil.setProperty(document, SCHEMA, ID_DOSSIER, idDossier);

	}

	@Override
	public String getIntitule() {
		return PropertyUtil.getStringProperty(document, SCHEMA, INTITULE);
	}

	@Override
	public void setIntitule(String intitule) {
		PropertyUtil.setProperty(document, SCHEMA, INTITULE, intitule);
	}

	@Override
	public Calendar getDateCM() {
		return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_CM);
	}

	@Override
	public void setDateCM(Calendar dateCM) {
		PropertyUtil.setProperty(document, SCHEMA, DATE_CM, dateCM);
	}

	@Override
	public String getObservation() {
		return PropertyUtil.getStringProperty(document, SCHEMA, OBSERVATION);
	}

	@Override
	public void setObservation(String observation) {
		PropertyUtil.setProperty(document, SCHEMA, OBSERVATION, observation);
	}

	@Override
	public Calendar getProcedureAcceleree() {
		return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_PROCEDURE_ACCELERE);
	}

	@Override
	public void setProcedureAcceleree(Calendar procedureAcceleree) {
		PropertyUtil.setProperty(document, SCHEMA, DATE_PROCEDURE_ACCELERE, procedureAcceleree);
	}

	@Override
	public Boolean isArticle493() {
		return PropertyUtil.getBooleanProperty(document, SCHEMA, ARTICLE_49_3);
	}

	@Override
	public void setArticle493(Boolean article493) {
		PropertyUtil.setProperty(document, SCHEMA, ARTICLE_49_3, article493);
	}

	@Override
	public String getAssembleeDepot() {
		return PropertyUtil.getStringProperty(document, SCHEMA, ASSEMBLEE_DEPOT);
	}

	@Override
	public void setAssembleeDepot(String assembleeDepot) {
		PropertyUtil.setProperty(document, SCHEMA, ASSEMBLEE_DEPOT, assembleeDepot);
	}

	@Override
	public Calendar getDateDepot() {
		return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_DEPOT);
	}

	@Override
	public void setDateDepot(Calendar dateDepot) {
		PropertyUtil.setProperty(document, SCHEMA, DATE_DEPOT, dateDepot);
	}

	@Override
	public String getNumeroDepot() {
		return PropertyUtil.getStringProperty(document, SCHEMA, NUMERO_DEPOT);
	}

	@Override
	public void setNumeroDepot(String numeroDepot) {
		PropertyUtil.setProperty(document, SCHEMA, NUMERO_DEPOT, numeroDepot);
	}

	@Override
	public DocumentModel getDocument() {
		return document;
	}

	@Override
	public Calendar getDateReception() {
		return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_RECEPTION);
	}

	@Override
	public void setDateReception(Calendar dateReception) {
		PropertyUtil.setProperty(document, SCHEMA, DATE_RECEPTION, dateReception);
	}

	@Override
	public Calendar getDateLimitePromulgation() {
		return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_LIMITE_PROMULGATION);
	}

	@Override
	public void setDateLimitePromulgation(Calendar dateLimitePromulgation) {
		PropertyUtil.setProperty(document, SCHEMA, DATE_LIMITE_PROMULGATION, dateLimitePromulgation);
	}

	@Override
	public Calendar getDateSaisieCC() {
		return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_SAISINE_CC);
	}

	@Override
	public void setDateSaisieCC(Calendar dateSaisieCC) {
		PropertyUtil.setProperty(document, SCHEMA, DATE_SAISINE_CC, dateSaisieCC);
	}

	@Override
	public Calendar getDateDecision() {
		return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_DECISION);
	}

	@Override
	public void setDateDecision(Calendar dateDecision) {
		PropertyUtil.setProperty(document, SCHEMA, DATE_DECISION, dateDecision);
	}

	@Override
	public Calendar getDateAdoption() {
		return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_ADOPTION);
	}

	@Override
	public void setDateAdoption(Calendar dateAdoption) {
		PropertyUtil.setProperty(document, SCHEMA, DATE_ADOPTION, dateAdoption);
	}

	@Override
	public Calendar getDateJO() {
		return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_JO);
	}

	@Override
	public void setDateJO(Calendar dateJO) {
		PropertyUtil.setProperty(document, SCHEMA, DATE_JO, dateJO);
	}

	@Override
	public String getNumeroNor() {
		return PropertyUtil.getStringProperty(document, SCHEMA, NUMERO_NOR);
	}

	@Override
	public void setNumeroNor(String numeroNor) {
		PropertyUtil.setProperty(document, SCHEMA, NUMERO_NOR, numeroNor);
	}

	@Override
	public Calendar getDateCreation() {
		return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_CREATION);
	}

	@Override
	public void setDateCreation(Calendar dateCreation) {
		PropertyUtil.setProperty(document, SCHEMA, DATE_CREATION, dateCreation);
	}

	@Override
	public Calendar getDepartElysee() {
		return PropertyUtil.getCalendarProperty(document, SCHEMA, DEPART_ELYSEE);
	}

	@Override
	public void setDepartElysee(Calendar departElysee) {
		PropertyUtil.setProperty(document, SCHEMA, DEPART_ELYSEE, departElysee);
	}

	@Override
	public Calendar getRetourElysee() {
		return PropertyUtil.getCalendarProperty(document, SCHEMA, RETOUR_ELYSEE);
	}

	@Override
	public void setRetourElysee(Calendar retourElysee) {
		PropertyUtil.setProperty(document, SCHEMA, RETOUR_ELYSEE, retourElysee);
	}

	@Override
	public Boolean isEcheancierPromulgation() {
		return PropertyUtil.getBooleanProperty(document, SCHEMA, ECHEANCIER_PROMULGATION);
	}

	@Override
	public void setEcheancierPromulgation(Boolean echeancierPromulgation) {
		PropertyUtil.setProperty(document, SCHEMA, ECHEANCIER_PROMULGATION, echeancierPromulgation);
	}

	@Override
	public String getMinistereResp() {
		return PropertyUtil.getStringProperty(document, SCHEMA, MINISTERE_RESP);
	}

	@Override
	public void setMinistereResp(String ministereResp) {
		PropertyUtil.setProperty(document, SCHEMA, MINISTERE_RESP, ministereResp);
	}

	@Override
	public String getNomCompletChargeMission() {
		return PropertyUtil.getStringListProperty(document, SCHEMA, NOM_COMPLET_CHARGE_MISSION).get(0);
	}

	@Override
	public void setNomCompletChargeMission(String nomCompletChargeMission) {
		List<String> value = new ArrayList<String>();
		if (!StringUtils.isEmpty(nomCompletChargeMission)) {
			value.add(nomCompletChargeMission);
		}
		PropertyUtil.setProperty(document, SCHEMA, NOM_COMPLET_CHARGE_MISSION, value);
	}

	@Override
	public Calendar getDateProjet() {
		return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_PROJET);
	}

	@Override
	public void setDateProjet(Calendar dateProjet) {
		PropertyUtil.setProperty(document, SCHEMA, DATE_PROJET, dateProjet);
	}

	@Override
	public Calendar getDateSectionCe() {
		return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_SECTION_CE);
	}

	@Override
	public void setDateSectionCe(Calendar dateSectionCe) {
		PropertyUtil.setProperty(document, SCHEMA, DATE_SECTION_CE, dateSectionCe);
	}

	@Override
	public String getNumeroISA() {
		return PropertyUtil.getStringProperty(document, SCHEMA, NUMERO_ISA);
	}

	@Override
	public void setNumeroISA(String numeroISA) {
		PropertyUtil.setProperty(document, SCHEMA, NUMERO_ISA, numeroISA);
	}

	@Override
	public String getDiffusion() {
		return PropertyUtil.getStringProperty(document, SCHEMA, DIFFUSION);
	}

	@Override
	public void setDiffusion(String diffusion) {
		PropertyUtil.setProperty(document, SCHEMA, DIFFUSION, diffusion);
	}

	@Override
	public String getDiffusionGenerale() {
		return PropertyUtil.getStringProperty(document, SCHEMA, DIFFUSION_GENERALE);
	}

	@Override
	public void setDiffusionGenerale(String diffusionGenerale) {
		PropertyUtil.setProperty(document, SCHEMA, DIFFUSION_GENERALE, diffusionGenerale);
	}

	@Override
	public String getTitreOfficiel() {
		return PropertyUtil.getStringProperty(document, SCHEMA, TITRE_OFFICIEL);
	}

	@Override
	public void setTitreOfficiel(String titreOfficiel) {
		PropertyUtil.setProperty(document, SCHEMA, TITRE_OFFICIEL, titreOfficiel);
	}

	@Override
	public NatureLoi getNatureLoi() {
		String nature = PropertyUtil.getStringProperty(document, SCHEMA, NATURE_LOI);
		if (nature != null) {
			return NatureLoi.valueOf(nature);
		} else {
			return null;
		}
	}

	@Override
	public void setNatureLoi(NatureLoi natureLoi) {
		PropertyUtil.setProperty(document, SCHEMA, NATURE_LOI, natureLoi.toString());
	}

	@Override
	public String getRefusEngagementProcAss1() {
		return PropertyUtil.getStringProperty(document, SCHEMA, REFUS_PROC_ACC_ASS1);
	}

	@Override
	public void setRefusEngagementProcAss1(String assemblee) {
		PropertyUtil.setProperty(document, SCHEMA, REFUS_PROC_ACC_ASS1, assemblee);
	}

	@Override
	public String getRefusEngagementProcAss2() {
		return PropertyUtil.getStringProperty(document, SCHEMA, REFUS_PROC_ACC_ASS2);
	}

	@Override
	public void setRefusEngagementProcAss2(String assemblee) {
		PropertyUtil.setProperty(document, SCHEMA, REFUS_PROC_ACC_ASS2, assemblee);
	}

	@Override
	public Calendar getDateRefusEngProcAss1() {
		return PropertyUtil.getCalendarProperty(document, SCHEMA, DATE_REFUS_PROC_ASS1);
	}

	@Override
	public void setDateRefusEngProcAss1(Calendar date) {
		PropertyUtil.setProperty(document, SCHEMA, DATE_REFUS_PROC_ASS1, date);
	}

	@Override
	public String getDecisionEngagementAssemblee2() {
		return PropertyUtil.getStringProperty(document, SCHEMA, DECISION_PROC_ACC_ASS2);
	}

	@Override
	public void setDecisionEngagementAssemblee2(String decision) {
		PropertyUtil.setProperty(document, SCHEMA, DECISION_PROC_ACC_ASS2, decision);
	}

}
