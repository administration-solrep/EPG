package fr.dila.solonepg.core.dto.activitenormative;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.util.DateUtil;
import java.util.Date;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.nuxeo.ecm.core.api.CoreSession;

public abstract class AbstractFicheSignaletiqueApplicationDTO {
    private String intitule;
    private Date datePromulgation;
    private Date datePublication;
    private String numeroNor;
    private Date dateLimite;

    private Boolean applicationDirecte;
    private String observation;

    private Map<String, Long> mapMesures;
    private Map<String, String> delaiPublication;
    private Map<String, String> repartititionMinisteres;

    private Integer tauxApplication;

    private Date dateEntreeVigueur;

    private Date dateReunionProgrammation;
    private Date dateCirculationCompteRendu;

    public AbstractFicheSignaletiqueApplicationDTO(ActiviteNormative activiteNormative, CoreSession session) {
        TexteMaitre texteMaitre = activiteNormative.getDocument().getAdapter(TexteMaitre.class);

        // infos lois
        intitule = texteMaitre.getTitreActe();
        if (!StringUtils.isEmpty(texteMaitre.getTitreOfficiel())) {
            intitule = texteMaitre.getTitreOfficiel();
        }
        numeroNor = texteMaitre.getNumeroNor();
        datePromulgation = DateUtil.toDate(texteMaitre.getDatePromulgation());
        datePublication = DateUtil.toDate(texteMaitre.getDatePublication());
        applicationDirecte = texteMaitre.isApplicationDirecte();
        dateReunionProgrammation = DateUtil.toDate(texteMaitre.getDateReunionProgrammation());
        dateCirculationCompteRendu = DateUtil.toDate(texteMaitre.getDateCirculationCompteRendu());

        dateEntreeVigueur = DateUtil.toDate(texteMaitre.getDateEntreeEnVigueur());

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

    private void computeDelaiPublication(
        TexteMaitre texteMaitre,
        ActiviteNormative activiteNormative,
        CoreSession session
    ) {
        delaiPublication =
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .buildDelaiPublicationForFicheSignaletique(session, activiteNormative, texteMaitre);
    }

    private void computeRepartitionMinistere(ActiviteNormative activiteNormative, CoreSession session) {
        repartititionMinisteres =
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .buildMinistereForFicheSignaletique(session, activiteNormative);
    }

    private void computeTauxApplication(ActiviteNormative activiteNormative, CoreSession session) {
        setTauxApplication(
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .buildTauxApplicationForFicheSignaletique(session, activiteNormative)
        );
    }

    private void buildMapMesures(CoreSession session, ActiviteNormative activiteNormative) {
        setMapMesures(
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .buildMesuresForFicheSignaletique(session, activiteNormative)
        );
    }

    private void computeDateLimite(TexteMaitre texteMaitre) {
        setDateLimite(DateUtil.toDate(texteMaitre.getDatePublication()));
        setDateLimite(
            texteMaitre.getDateEntreeEnVigueur() == null
                ? getDateLimite()
                : texteMaitre.getDateEntreeEnVigueur().getTime()
        );
        if (getDateLimite() != null) {
            setDateLimite(DateUtils.addMonths(getDateLimite(), 6));
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
