package fr.dila.solonepg.api.dto;

import java.util.Date;

public interface TexteMaitreLoiDTO {
    String getNumeroInterne();

    void setNumeroInterne(String numeroInterne);

    String getMotCle();

    void setMotCle(String motCle);

    Date getDateEntreeEnVigueur();

    void setDateEntreeEnVigueur(Date dateEntreeEnVigueur);

    String getObservation();

    void setObservation(String observation);

    String getChampLibre();

    void setChampLibre(String champLibre);

    Boolean getApplicationDirecte();

    void setApplicationDirecte(Boolean applicationDirecte);

    Date getDatePublication();

    void setDatePublication(Date datePublication);

    Date getDatePromulgation();

    void setDatePromulgation(Date datePromulgation);

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

    void setId(String id);

    String getId();

    String getNumeroNor();

    void setNumeroNor(String numeroNor);

    String getTitreActe();

    void setTitreActe(String titreActe);

    String getMinisterePilote();

    void setMinisterePilote(String ministerePilote);

    String getMinisterePiloteLabel();

    void setMinisterePiloteLabel(String ministerePilote);

    void setLienJORFLegifrance(String lienJORFLegifrance);

    String getLienJORFLegifrance();

    Boolean getTitreActeLocked();

    void setTitreActeLocked(Boolean titreActeLocked);

    Boolean getMinisterePiloteLocked();

    void setMinisterePiloteLocked(Boolean ministerePiloteLocked);

    Boolean getNumeroInterneLocked();

    void setNumeroInterneLocked(Boolean numeroInterneLocked);

    Boolean getDateEntreeEnVigueurLocked();

    void setDateEntreeEnVigueurLocked(Boolean dateEntreeEnVigueurLocked);

    Boolean getDatePublicationLocked();

    void setDatePublicationLocked(Boolean datePublicationLocked);

    Boolean getTitreOfficielLocked();

    void setTitreOfficielLocked(Boolean titreOfficielLocked);

    Boolean getLegislaturePublicationLocked();

    void setLegislaturePublicationLocked(Boolean legislaturePublicationLocked);

    Boolean getNatureTexteLocked();

    void setNatureTexteLocked(Boolean natureTexteLocked);

    Boolean getProcedureVoteLocked();

    void setProcedureVoteLocked(Boolean procedureVoteLocked);

    Boolean getCommissionAssNationaleLocked();

    void setCommissionAssNationaleLocked(Boolean commissionAssNationaleLocked);

    Boolean getCommissionSenatLocked();

    void setCommissionSenatLocked(Boolean commissionSenatLocked);

    void setNumeroLocked(Boolean numeroLocked);

    Boolean getNumeroLocked();

    void setNumero(String numero);

    String getNumero();

    void setDispositionHabilitation(Boolean dispositionHabilitation);

    Boolean getDispositionHabilitation();

    Date getDateReunionProgrammation();
    void setDateReunionProgrammation(Date dateReunionProgrammation);

    Date getDateCirculationCompteRendu();
    void setDateCirculationCompteRendu(Date dateCirculationCompteRendu);

    Date getDateModification();

    void setDateModification(Date dateModification);

    Date getDateInjection();

    void setDateInjection(Date dateInjection);
}
