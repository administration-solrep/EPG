package fr.dila.solonepg.api.cases;

import java.util.Calendar;
import java.util.List;

/**
 * Mesure Applicative ({@link ActiviteNormative}).
 *
 * @author asatre
 *
 */
public interface MesureApplicative extends TexteMaitre {
    String getArticle();
    void setArticle(String article);

    String getCodeModifie();
    void setCodeModifie(String codeModifie);

    String getBaseLegale();
    void setBaseLegale(String baseLegale);

    String getObjetRIM();
    void setObjetRIM(String objetRIM);

    String getTypeMesure();
    void setTypeMesure(String typeMesure);

    String getTypeMesureLabel();

    Boolean isFromAmendement();
    void setFromAmendement(Boolean fromAmendement);

    Boolean isDiffere();
    void setDiffere(Boolean differe);

    String getNumeroQuestion();
    void setNumeroQuestion(String numeroQuestion);

    Boolean isAmendement();
    void setAmendement(Boolean amendement);

    String getResponsableAmendement();
    void setResponsableAmendement(String responsableAmendement);
    String getResponsableAmendementLabel();

    @Override
    List<String> getDecretIds();

    @Override
    void setDecretIds(List<String> decretIds);

    String getConsultationsHCE();
    void setConsultationsHCE(String consultationsHCE);

    String getCalendrierConsultationsHCE();
    void setCalendrierConsultationsHCE(String calendrierConsultationsHCE);

    Calendar getDateDisponnibiliteAvantProjet();
    void setDateDisponnibiliteAvantProjet(Calendar dateDisponnibiliteAvantProjet);

    Calendar getDatePrevisionnelleSaisineCE();
    void setDatePrevisionnelleSaisineCE(Calendar datePrevisionnelleSaisineCE);

    void setPoleChargeMission(String poleChargeMission);
    String getPoleChargeMission();

    Calendar getDatePassageCM();
    void setDatePassageCM(Calendar datePassageCM);

    Calendar getDateObjectifPublication();
    void setDateObjectifPublication(Calendar dateObjectifPublication);

    String getDirectionResponsable();
    void setDirectionResponsable(String directionResponsable);

    Boolean getApplicationRecu();
    void setApplicationRecu(Boolean applicationRecu);

    Boolean getApplicationLoi();
    void setApplicationLoi(Boolean applicationLoi);

    @Override
    String toString();

    Boolean getMesureApplication();
    void setMesureApplication(Boolean mesureApplication);

    @Override
    Calendar getDateReunionProgrammation();

    @Override
    void setDateReunionProgrammation(Calendar dateReunionProgrammation);

    @Override
    Calendar getDateCirculationCompteRendu();

    @Override
    void setDateCirculationCompteRendu(Calendar dateCirculationCompteRendu);

    Calendar getDateMiseApplication();
    void setDateMiseApplication(Calendar dateDateMiseApplication);

    /**
     * récupère la liste des décrets de la mesure qui n'ont pas été validés pour cette mesure
     * @return
     */
    List<String> getDecretsIdsInvalidated();
    void setDecretsIdsInvalidated(List<String> decretsIdsNotValidated);
}
