package fr.dila.solonepg.api.dto;

import fr.dila.solonepg.api.cases.MesureApplicative;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface MesureApplicativeDTO extends Map<String, Serializable> {
    MesureApplicative remapField(MesureApplicative mesureApplicative);

    String getId();

    void setId(String id);

    String getNumeroOrdre();

    void setNumeroOrdre(String numeroOrdre);

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

    void setTypeMesureLabel(String typeMesure);

    Boolean getFromAmendement();

    void setFromAmendement(Boolean fromAmendement);

    Boolean getDiffere();

    void setDiffere(Boolean differe);

    Date getDateEntreeEnVigueur();

    void setDateEntreeEnVigueur(Date dateEntreeEnVigueur);

    String getNumeroQuestion();

    void setNumeroQuestion(String numeroQuestion);

    Boolean getAmendement();

    void setAmendement(Boolean amendement);

    String getResponsableAmendement();

    void setResponsableAmendement(String responsableAmendement);

    List<String> getDecretIds();

    void setDecretIds(List<String> decretIds);

    List<String> getDecretIdsInvalidated();

    void setDecretIdsInvalidated(List<String> decretIds);

    void addDecret(String idDossier);

    Boolean hasValidation();

    void setValidation(Boolean validation);

    String getChampLibre();

    void setChampLibre(String champLibre);

    @Override
    String toString();

    String getMinisterePilote();

    void setMinisterePilote(String ministerePilote);

    String getMinisterePiloteLabel();

    void setMinisterePiloteLabel(String ministerePilote);

    String getDirectionResponsable();

    void setDirectionResponsable(String directionResponsable);

    Boolean getMesureApplication();

    void setMesureApplication(Boolean mesureApplication);

    Date getDateReunionProgrammation();

    void setDateReunionProgrammation(Date dateReunionProgrammation);

    Date getDateCirculationCompteRendu();

    void setDateCirculationCompteRendu(Date dateCirculationCompteRendu);

    String getConsultationsHCE();

    void setConsultationsHCE(String consultationsHCE);

    String getCalendrierConsultationsHCE();

    void setCalendrierConsultationsHCE(String calendrierConsultationsHCE);

    Date getDateDisponnibiliteAvantProjet();

    void setDateDisponnibiliteAvantProjet(Date dateDisponibiliteAvantProjet);

    String getPoleChargeMission();

    void setPoleChargeMission(String poleChargeMission);

    Date getDatePassageCM();

    void setDatePassageCM(Date datePassageCM);

    Boolean getApplicationRecu();

    void setApplicationRecu(Boolean applicationRecu);

    Boolean getValidate();

    void setValidate(Boolean validation);

    Date getDateObjectifPublication();

    void setDateObjectifPublication(Date dateObjectifPublication);

    String getObservation();

    void setObservation(String observation);

    Date getDatePrevisionnelleSaisineCE();

    void setDatePrevisionnelleSaisineCE(Date datePrevisionnelleSaisineCE);

    Date getDateMiseApplication();

    void setDateMiseApplication(Date dateMiseApplication);

    String getCommunicationMinisterielle();

    void setCommunicationMinisterielle(String communicationMinisterielle);

    Date getDateLimite();
}
