package fr.dila.solonepg.core.dto.activitenormative.injectionbdj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Texte maître unitaire à envoyer au BDJ (élément Texte)
 * 
 * @author tlombard
 */
public class TexteMaitreDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;

	private String nor;

	private List<MesureApplicationDTO> mesures = new ArrayList<MesureApplicationDTO>();

	public TexteMaitreDTO(String pId, String pNor) {
		super();
		this.id = pId;
		this.nor = pNor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNor() {
		return nor;
	}

	public void setNor(String nor) {
		this.nor = nor;
	}

	public List<MesureApplicationDTO> getMesures() {
		return mesures;
	}

	public void setMesures(List<MesureApplicationDTO> mesures) {
		this.mesures = mesures;
	}
}
