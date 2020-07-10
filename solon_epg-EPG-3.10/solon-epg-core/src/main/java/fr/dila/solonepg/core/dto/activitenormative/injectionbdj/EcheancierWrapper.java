package fr.dila.solonepg.core.dto.activitenormative.injectionbdj;

import java.io.Serializable;

/**
 * Wrapper destiné à l'envoi du xml au sein d'une string json.
 * 
 * @author tlombard
 */
public class EcheancierWrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	private String flux;

	public EcheancierWrapper(String pFlux) {
		super();
		this.flux = pFlux;
	}

	public String getFlux() {
		return flux;
	}

	public void setFlux(String flux) {
		this.flux = flux;
	}
}
