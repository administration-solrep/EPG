package fr.dila.solonepg.core.dto.activitenormative.injectionbdj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Texte de bilan semestriel à envoyer au BDJ (élément textes).
 * 
 * @author nvezian
 */
public class TexteBilanSemestrielDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String type;
	private String id;
	private String titre;
	private String mesuresAttendues;
	private String mesuresAppliquees;
	private String mesuresEnAttente;
	
	public TexteBilanSemestrielDTO(String type, String id, String titre, String mesuresAttendues,
			String mesuresAppliquees, String mesuresEnAttente) {
		super();
		this.type = type;
		this.id = id;
		this.titre = titre;
		this.mesuresAttendues = mesuresAttendues;
		this.mesuresAppliquees = mesuresAppliquees;
		this.mesuresEnAttente = mesuresEnAttente;
	}
	
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getMesuresAttendues() {
		return mesuresAttendues;
	}
	public void setMesuresAttendues(String mesuresAttendues) {
		this.mesuresAttendues = mesuresAttendues;
	}
	public String getMesuresAppliquees() {
		return mesuresAppliquees;
	}
	public void setMesuresAppliquees(String mesuresAppliquees) {
		this.mesuresAppliquees = mesuresAppliquees;
	}
	public String getMesuresEnAttente() {
		return mesuresEnAttente;
	}
	public void setMesuresEnAttente(String mesuresEnAttente) {
		this.mesuresEnAttente = mesuresEnAttente;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Retourne le taux d'application des mesures pour le texte équivalent à (mesuresAppliquees/mesuresAttendues)*100.
	 */
	public Double getTaux() {
		if (mesuresAttendues == null || mesuresAttendues.isEmpty() || Integer.parseInt(mesuresAttendues) == 0 || 
				mesuresAppliquees == null || mesuresAppliquees.isEmpty() || Integer.parseInt(mesuresAppliquees) == 0) {
			return 0d;
		}
		else {
			Double taux = (Double.parseDouble(this.mesuresAppliquees)/Double.parseDouble(this.mesuresAttendues))*100;
			return taux;
		}
	}
	
}
