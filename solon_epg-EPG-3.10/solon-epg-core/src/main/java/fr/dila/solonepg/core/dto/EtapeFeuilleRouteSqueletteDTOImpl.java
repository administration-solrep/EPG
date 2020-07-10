package fr.dila.solonepg.core.dto;

import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.dto.EtapeFeuilleRouteSqueletteDTO;
import fr.dila.solonepg.api.feuilleroute.SqueletteStepTypeDestinataire;
import fr.dila.ss.core.dto.activitenormative.EtapeFeuilleDeRouteDTOImpl;
import fr.dila.st.api.feuilleroute.STRouteStep;

public class EtapeFeuilleRouteSqueletteDTOImpl extends EtapeFeuilleDeRouteDTOImpl implements EtapeFeuilleRouteSqueletteDTO {

	private static final long serialVersionUID = -8790308791740995929L;

	public EtapeFeuilleRouteSqueletteDTOImpl() {
		super();
	}
	
	public EtapeFeuilleRouteSqueletteDTOImpl(STRouteStep routeStep) {
		super(routeStep);
	}
	
	@Override
	public SqueletteStepTypeDestinataire getTypeDestinataire() {
		if (get(SolonEpgSchemaConstant.ROUTING_TASK_TYPE_DESTINATAIRE_PROPERTY) != null) {
			return SqueletteStepTypeDestinataire.valueOf((String) get(SolonEpgSchemaConstant.ROUTING_TASK_TYPE_DESTINATAIRE_PROPERTY));
		}
		return null;
	}

	@Override
	public void setTypeDestinataire(SqueletteStepTypeDestinataire typeDestinataire) {
		put(SolonEpgSchemaConstant.ROUTING_TASK_TYPE_DESTINATAIRE_PROPERTY, typeDestinataire.name());
	}

}
