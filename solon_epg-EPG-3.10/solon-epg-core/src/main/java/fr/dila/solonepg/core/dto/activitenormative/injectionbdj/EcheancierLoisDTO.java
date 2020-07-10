package fr.dila.solonepg.core.dto.activitenormative.injectionbdj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Echéancier de loi à envoyer au BDJ (élément textes).
 * 
 * @author tlombard
 */
public class EcheancierLoisDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<TexteMaitreDTO> textes = new ArrayList<TexteMaitreDTO>();

	public EcheancierLoisDTO() {
		super();
	}

	public List<TexteMaitreDTO> getTextes() {
		return textes;
	}

	public void setTextes(List<TexteMaitreDTO> textes) {
		this.textes = textes;
	}
}
