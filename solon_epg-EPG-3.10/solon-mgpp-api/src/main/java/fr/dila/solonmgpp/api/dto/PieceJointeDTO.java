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
public interface PieceJointeDTO extends Serializable {

    public static final String LIBELLE = "libelle";
    public static final String ID_INTERNE_EPP = "idInterneEpp";
    public static final String TYPE = "type";
    public static final String URL = "url";
    public static final String FICHIER = "fichier";

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
