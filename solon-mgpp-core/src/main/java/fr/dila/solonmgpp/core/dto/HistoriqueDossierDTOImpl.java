package fr.dila.solonmgpp.core.dto;

import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.FicheDossierDTO;
import fr.dila.solonmgpp.api.dto.HistoriqueDossierDTO;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.MapUtils;

/**
 * Implementation of {@link HistoriqueDossierDTO}
 *
 * @author asatre
 *
 */
public class HistoriqueDossierDTOImpl implements HistoriqueDossierDTO, Serializable {
    private static final long serialVersionUID = -2815664010799258763L;

    private final FicheDossierDTO ficheDossierDTO;
    // L'ordre est important pour les évènement racines et les éléments suivants
    private Map<String, EvenementDTO> rootEvents = new LinkedHashMap<>();
    private Map<String, List<EvenementDTO>> mapSuivant = new LinkedHashMap<>();
    private final Set<String> listHistoriqueType;

    public HistoriqueDossierDTOImpl(
        FicheDossierDTO ficheDossierDTO,
        Map<String, EvenementDTO> rootEvents,
        Map<String, List<EvenementDTO>> mapSuivant,
        Set<String> listHistoriqueType
    ) {
        this.ficheDossierDTO = ficheDossierDTO;
        this.setRootEvents(rootEvents);
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
        if (MapUtils.isNotEmpty(rootEvents)) {
            this.rootEvents = rootEvents;
        } else {
            this.rootEvents.clear();
        }
    }

    @Override
    public Map<String, EvenementDTO> getRootEvents() {
        return rootEvents;
    }

    @Override
    public void setMapSuivant(Map<String, List<EvenementDTO>> mapSuivant) {
        if (MapUtils.isNotEmpty(mapSuivant)) {
            this.mapSuivant = mapSuivant;
        } else {
            this.mapSuivant.clear();
        }
    }

    @Override
    public Map<String, List<EvenementDTO>> getMapSuivant() {
        return mapSuivant;
    }
}
