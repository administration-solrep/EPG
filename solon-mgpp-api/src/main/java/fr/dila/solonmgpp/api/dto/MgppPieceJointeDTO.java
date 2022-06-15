package fr.dila.solonmgpp.api.dto;

import java.io.Serializable;
import java.util.List;

/**
 * DTO de representation d'une piece jointe EPP
 *
 * Les noms de champs doivent être cohérents avec le contrat de ContenuFichier (api xsdl EPP).
 *
 * @author asatre
 *
 */
public interface MgppPieceJointeDTO extends Serializable {
    String getIdInterneEpp();

    void setIdInterneEpp(String idInterneEpp);

    String getLibelle();

    void setLibelle(String libelle);

    String getType();

    void setType(String type);

    String getUrl();

    void setUrl(String url);

    List<PieceJointeFichierDTO> getFichier();

    void setFichier(List<PieceJointeFichierDTO> fichier);
}
