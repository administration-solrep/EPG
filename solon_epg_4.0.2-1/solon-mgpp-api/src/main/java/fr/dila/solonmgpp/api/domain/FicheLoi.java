package fr.dila.solonmgpp.api.domain;

import fr.sword.xsd.solon.epp.NatureLoi;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface de representation d'une fiche loi
 *
 * @author admin
 *
 */
public interface FicheLoi {
    public static final String DOC_TYPE = "FicheLoi";
    public static final String SCHEMA = "fiche_loi";
    public static final String PREFIX = "floi";

    public static final String NUMERO_NOR = "numeroNor";
    public static final String ID_DOSSIER = "idDossier";
    public static final String INTITULE = "intitule";
    public static final String DATE_CM = "dateCM";
    public static final String OBSERVATION = "observation";
    public static final String DATE_PROCEDURE_ACCELERE = "dateProcedureAcceleree";
    public static final String ARTICLE_49_3 = "article493";
    public static final String ASSEMBLEE_DEPOT = "assembleeDepot";
    public static final String DATE_DEPOT = "dateDepot";
    public static final String NUMERO_DEPOT = "numeroDepot";
    public static final String DATE_RECEPTION = "dateReception";
    public static final String DATE_LIMITE_PROMULGATION = "dateLimitePromulgation";
    public static final String DATE_SAISINE_CC = "dateSaisieCC";
    public static final String DATE_DECISION = "dateDecision";
    public static final String DATE_ADOPTION = "dateAdoption";
    public static final String DATE_JO = "dateJO";
    public static final String DATE_CREATION = "dateCreation";
    public static final String DEPART_ELYSEE = "departElysee";
    public static final String RETOUR_ELYSEE = "retourElysee";
    public static final String ECHEANCIER_PROMULGATION = "echeancierPromulgation";
    public static final String MINISTERE_RESP = "ministereResp";
    public static final String NOM_COMPLET_CHARGE_MISSION = "nomCompletChargesMissions";
    public static final String DATE_PROJET = "dateProjet";
    public static final String DATE_SECTION_CE = "dateSectionCe";
    public static final String NUMERO_ISA = "numeroISA";
    public static final String DIFFUSION = "diffusion";
    public static final String DIFFUSION_GENERALE = "diffusionGenerale";
    public static final String TITRE_OFFICIEL = "titreOfficiel";
    public static final String NATURE_LOI = "natureLoi";
    public static final String REFUS_PROC_ACC_ASS1 = "refusEngagementProcAss1";
    public static final String REFUS_PROC_ACC_ASS2 = "refusEngagementProcAss2";
    public static final String DATE_REFUS_PROC_ASS1 = "dateRefusEngProcAss1";
    public static final String DECISION_PROC_ACC_ASS2 = "decisionEngagementAssemblee2";

    String getNumeroNor();

    void setNumeroNor(String numeroNor);

    String getIdDossier();

    void setIdDossier(String idDossier);

    String getIntitule();

    void setIntitule(String intitule);

    Calendar getDateCM();

    void setDateCM(Calendar dateCM);

    String getObservation();

    void setObservation(String observation);

    Calendar getProcedureAcceleree();

    void setProcedureAcceleree(Calendar procedureAcceleree);

    Boolean isArticle493();

    void setArticle493(Boolean article493);

    String getAssembleeDepot();

    void setAssembleeDepot(String assembleeDepot);

    Calendar getDateDepot();

    void setDateDepot(Calendar dateDepot);

    String getNumeroDepot();

    void setNumeroDepot(String numeroDepot);

    Calendar getDateReception();

    void setDateReception(Calendar dateReception);

    Calendar getDateLimitePromulgation();

    void setDateLimitePromulgation(Calendar dateLimitePromulgation);

    Calendar getDateSaisieCC();

    void setDateSaisieCC(Calendar dateSaisieCC);

    Calendar getDateDecision();

    void setDateDecision(Calendar dateDecision);

    Calendar getDateAdoption();

    void setDateAdoption(Calendar dateAdoption);

    Calendar getDateJO();

    void setDateJO(Calendar dateJO);

    Calendar getDateCreation();

    void setDateCreation(Calendar dateCreation);

    Calendar getDepartElysee();

    void setDepartElysee(Calendar departElysee);

    Calendar getRetourElysee();

    void setRetourElysee(Calendar dateCreation);

    Boolean isEcheancierPromulgation();

    void setEcheancierPromulgation(Boolean echeancierPromulgation);

    String getMinistereResp();

    void setMinistereResp(String ministereResp);

    String getNomCompletChargeMission();

    void setNomCompletChargeMission(String nomCompletChargeMission);

    Calendar getDateProjet();

    void setDateProjet(Calendar dateProjet);

    Calendar getDateSectionCe();

    void setDateSectionCe(Calendar dateSectionCe);

    String getNumeroISA();

    void setNumeroISA(String numeroISA);

    String getDiffusion();

    void setDiffusion(String diffusion);

    String getDiffusionGenerale();

    void setDiffusionGenerale(String diffusionGenerale);

    String getTitreOfficiel();

    void setTitreOfficiel(String titreOfficiel);

    NatureLoi getNatureLoi();

    void setNatureLoi(NatureLoi natureLoi);

    String getRefusEngagementProcAss1();

    void setRefusEngagementProcAss1(String assemblee);

    Calendar getDateRefusEngProcAss1();

    void setDateRefusEngProcAss1(Calendar date);

    String getRefusEngagementProcAss2();

    void setRefusEngagementProcAss2(String assemblee);

    String getDecisionEngagementAssemblee2();

    void setDecisionEngagementAssemblee2(String decision);

    DocumentModel getDocument();
}
