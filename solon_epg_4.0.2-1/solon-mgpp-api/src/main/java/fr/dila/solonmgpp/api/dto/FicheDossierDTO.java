package fr.dila.solonmgpp.api.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * DTO de representation d'une ficheDossier EPP
 * @author asatre
 *
 */
public interface FicheDossierDTO extends Map<String, Serializable> {
    public static final String ID_DOSSIER = "idDossier";

    String getIdDossier();
}
