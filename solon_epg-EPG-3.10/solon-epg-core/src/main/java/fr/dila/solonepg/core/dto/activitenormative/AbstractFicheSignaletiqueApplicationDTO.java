package fr.dila.solonepg.core.dto.activitenormative;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

public abstract class AbstractFicheSignaletiqueApplicationDTO {

	private String				intitule;
	private Date				datePromulgation;
	private Date				datePublication;
	private String				numeroNor;
	private Date				dateLimite;

	private Boolean				applicationDirecte;
	private String				observation;

	private Map<String, Long>	mapMesures;
	private Map<String, String>	delaiPublication;
	private Map<String, String>	repartititionMinisteres;

	private Integer				tauxApplication;

	private Date				dateEntreeVigueur;

	private Date				dateReunionProgrammation;
	private Date				dateCirculationCompteRendu;

	public AbstractFicheSignaletiqueApplicationDTO(ActiviteNormative activiteNormative, CoreSession session)
			throws ClientException {
		TexteMaitre texteMaitre = activiteNormative.getDocument().getAdapter(TexteMaitre.class);

		// infos lois
		intitule = texteMaitre.getTitreActe();
		if (!StringUtils.isEmpty(texteMaitre.getTitreOfficiel())) {
			intitule = texteMaitre.getTitreOfficiel();
		}
		numeroNor = texteMaitre.getNumeroNor();
		datePromulgation = texteMaitre.getDatePromulgation() == null ? null : texteMaitre.getDatePromulgation()
				.getTime();
		datePublication = texteMaitre.getDatePublication() == null ? null : texteMaitre.getDatePublication().getTime();
		applicationDirecte = texteMaitre.isApplicationDirecte();
		dateReunionProgrammation = texteMaitre.getDateReunionProgrammation() == null ? null : texteMaitre
				.getDateReunionProgrammation().getTime();
		dateCirculationCompteRendu = texteMaitre.getDateCirculationCompteRendu() == null ? null : texteMaitre
				.getDateCirculationCompteRendu().getTime();

		dateEntreeVigueur = texteMaitre.getDateEntreeEnVigueur() == null ? null : texteMaitre.getDateEntreeEnVigueur()
				.getTime();

		computeDateLimite(texteMaitre);

		// Stats mesure
		buildMapMesures(session, activiteNormative);

		// Observation
		observation = texteMaitre.getObservation();

		// Répartition ministeres
		computeRepartitionMinistere(activiteNormative, session);

		// Délai publication
		computeDelaiPublication(texteMaitre, activiteNormative, session);

		// Taux Application
		computeTauxApplication(activiteNormative, session);

	}

	private void computeDelaiPublication(TexteMaitre texteMaitre, ActiviteNormative activiteNormative,
			CoreSession session) throws ClientException {
		delaiPublication = SolonEpgServiceLocator.getActiviteNormativeService()
				.buildDelaiPublicationForFicheSignaletique(session, activiteNormative, texteMaitre);
	}

	private void computeRepartitionMinistere(ActiviteNormative activiteNormative, CoreSession session)
			throws ClientException {
		repartititionMinisteres = SolonEpgServiceLocator.getActiviteNormativeService()
				.buildMinistereForFicheSignaletique(session, activiteNormative);
	}

	private void computeTauxApplication(ActiviteNormative activiteNormative, CoreSession session)
			throws ClientException {
		setTauxApplication(SolonEpgServiceLocator.getActiviteNormativeService()
				.buildTauxApplicationForFicheSignaletique(session, activiteNormative));
	}

	private void buildMapMesures(CoreSession session, ActiviteNormative activiteNormative) throws ClientException {
		setMapMesures(SolonEpgServiceLocator.getActiviteNormativeService().buildMesuresForFicheSignaletique(session,
				activiteNormative));
	}

	private void computeDateLimite(TexteMaitre texteMaitre) {
		setDateLimite(texteMaitre.getDatePublication() == null ? null : texteMaitre.getDatePublication().getTime());
		setDateLimite(texteMaitre.getDateEntreeEnVigueur() == null ? getDateLimite() : texteMaitre
				.getDateEntreeEnVigueur().getTime());
		if (getDateLimite() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(getDateLimite());
			cal.add(Calendar.MONTH, 6);
			setDateLimite(cal.getTime());
		}
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public Date getDatePromulgation() {
		return datePromulgation;
	}

	public void setDatePromulgation(Date datePromulgation) {
		this.datePromulgation = datePromulgation;
	}

	public Date getDatePublication() {
		return datePublication;
	}

	public void setDatePublication(Date datePublication) {
		this.datePublication = datePublication;
	}

	public String getNumeroNor() {
		return numeroNor;
	}

	public void setNumeroNor(String numeroNor) {
		this.numeroNor = numeroNor;
	}

	public Date getDateLimite() {
		return dateLimite;
	}

	public void setDateLimite(Date dateLimite) {
		this.dateLimite = dateLimite;
	}

	public Boolean getApplicationDirecte() {
		return applicationDirecte;
	}

	public void setApplicationDirecte(Boolean applicationDirecte) {
		this.applicationDirecte = applicationDirecte;
	}

	public Map<String, String> getRepartititionMinisteres() {
		return repartititionMinisteres;
	}

	public void setRepartititionMinisteres(Map<String, String> repartititionMinisteres) {
		this.repartititionMinisteres = repartititionMinisteres;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public Map<String, String> getDelaiPublication() {
		return delaiPublication;
	}

	public void setDelaiPublication(Map<String, String> delaiPublication) {
		this.delaiPublication = delaiPublication;
	}

	public void setMapMesures(Map<String, Long> mapMesures) {
		this.mapMesures = mapMesures;
	}

	public Map<String, Long> getMapMesures() {
		return mapMesures;
	}

	public void setTauxApplication(Integer tauxApplication) {
		this.tauxApplication = tauxApplication;
	}

	public Integer getTauxApplication() {
		return tauxApplication;
	}

	public void setDateEntreeVigueur(Date dateEntreeVigueur) {
		this.dateEntreeVigueur = dateEntreeVigueur;
	}

	public Date getDateEntreeVigueur() {
		return dateEntreeVigueur;
	}

	public void setDateCirculationCompteRendu(Date dateCirculationCompteRendu) {
		this.dateCirculationCompteRendu = dateCirculationCompteRendu;
	}

	public Date getDateCirculationCompteRendu() {
		return dateCirculationCompteRendu;
	}

	public void setDateReunionProgrammation(Date dateReunionProgrammation) {
		this.dateReunionProgrammation = dateReunionProgrammation;
	}

	public Date getDateReunionProgrammation() {
		return dateReunionProgrammation;
	}

}
