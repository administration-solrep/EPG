package fr.dila.solonepg.core.dto.activitenormative;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.TexteMaitre;

public class FicheSignaletiqueApplicationOrdonnanceDTO extends AbstractFicheSignaletiqueApplicationDTO implements
		Serializable {

	private static final long	serialVersionUID	= 1L;

	public FicheSignaletiqueApplicationOrdonnanceDTO(ActiviteNormative activiteNormative, CoreSession session)
			throws ClientException {
		super(activiteNormative, session);
	}
}
