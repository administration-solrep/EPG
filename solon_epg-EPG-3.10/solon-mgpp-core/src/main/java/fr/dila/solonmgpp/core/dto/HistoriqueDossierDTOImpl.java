package fr.dila.solonmgpp.core.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.FicheDossierDTO;
import fr.dila.solonmgpp.api.dto.HistoriqueDossierDTO;

/**
 * Implementation of {@link HistoriqueDossierDTO}
 * 
 * @author asatre
 * 
 */
public class HistoriqueDossierDTOImpl implements HistoriqueDossierDTO, Serializable {

    private static final long serialVersionUID = -2815664010799258763L;

    private final FicheDossierDTO ficheDossierDTO;
    private Map<String, EvenementDTO> rootEvents;
    private Map<String, List<EvenementDTO>> mapSuivant;
    private final Set<String> listHistoriqueType;

    public HistoriqueDossierDTOImpl(FicheDossierDTO ficheDossierDTO, Map<String, EvenementDTO> rootEvents,
            Map<String, List<EvenementDTO>> mapSuivant, Set<String> listHistoriqueType) {
        this.ficheDossierDTO = ficheDossierDTO;
        this.rootEvents = rootEvents;
        this.listHistoriqueType = listHistoriqueType;
        this.setMapSuivant(mapSuivant);
    }

    @Override
    public FicheDossierDTO getFicheDossierDTO() {
        return ficheDossierDTO;
    }

    @Override
    public Set<String> getListHistoriqueType() {
        return listHistoriqueType;
    }

    @Override
    public void setRootEvents(Map<String, EvenementDTO> rootEvents) {
        this.rootEvents = rootEvents;
    }

    @Override
    public Map<String, EvenementDTO> getRootEvents() {
        return rootEvents;
    }

    @Override
    public void setMapSuivant(Map<String, List<EvenementDTO>> mapSuivant) {
        this.mapSuivant = mapSuivant;
    }

    @Override
    public Map<String, List<EvenementDTO>> getMapSuivant() {
        return mapSuivant;
    }

}
