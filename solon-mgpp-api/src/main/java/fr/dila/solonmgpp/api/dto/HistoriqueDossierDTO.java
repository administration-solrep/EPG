package fr.dila.solonmgpp.api.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author admin
 *
 */
public interface HistoriqueDossierDTO {
    FicheDossierDTO getFicheDossierDTO();

    void setRootEvents(Map<String, EvenementDTO> rootEvents);

    public Map<String, EvenementDTO> getRootEvents();

    void setMapSuivant(Map<String, List<EvenementDTO>> mapSuivant);

    Map<String, List<EvenementDTO>> getMapSuivant();

    Set<String> getListHistoriqueType();
}
