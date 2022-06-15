package fr.dila.solonmgpp.api.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * DTO de representation d'un evenement EPP
 *
 * @author asatre
 *
 */
public interface EvenementDTO extends Map<String, Serializable> {
    String getTypeEvenementName();

    String getTypeEvenementLabel();

    List<MgppPieceJointeDTO> getListPieceJointe(String typePieceJointe);

    String getIdDossier();

    void setIdDossier(String idDossier);

    String getIdEvenement();

    Integer getVersionCouranteMajeur();

    Integer getVersionCouranteMineur();

    String getVersionCourante();

    String getNor();

    void setNor(String nor);

    String getEmetteur();

    String getDestinataire();

    void setDestinataire(String destinataire);

    void setCopie(List<String> copie);

    List<String> getCopie();

    List<String> getActionSuivante();

    String getIdEvenementPrecedent();

    String getObjet();

    String getVersionCouranteModeCreation();

    String getVersionCouranteEtat();

    String getAuteur();

    String getCommentaire();

    List<String> getMetasModifiees();

    List<String> getEvenementsSuivants();

    List<String> getNature();

    String getOEP();

    void setOEP(String newOEP);

    String getEtat();

    boolean hasData();

    String getIntitule();
}
