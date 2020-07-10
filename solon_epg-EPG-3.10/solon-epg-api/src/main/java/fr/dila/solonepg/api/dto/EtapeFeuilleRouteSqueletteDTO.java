package fr.dila.solonepg.api.dto;

import fr.dila.solonepg.api.feuilleroute.SqueletteStepTypeDestinataire;
import fr.dila.ss.api.dto.EtapeFeuilleDeRouteDTO;

public interface EtapeFeuilleRouteSqueletteDTO extends EtapeFeuilleDeRouteDTO {
	SqueletteStepTypeDestinataire getTypeDestinataire();

	void setTypeDestinataire(SqueletteStepTypeDestinataire typeDestinataire);
}
