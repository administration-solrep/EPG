package fr.dila.solonepg.core.dto.activitenormative.injectionbdj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bilan semestriel à envoyer au BDJ (élément textes) pour plusieurs lois ou ordonnances.
 * 
 * @author nvezian
 */
public class BilanSemestrielDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum BilanSemestrielType {
		LOI("loi"), ORDONNANCE("ordonnance");
		
		private final String label;
		
		BilanSemestrielType(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
		
	}
	
	private BilanSemestrielType type;
	private Date dateDebutIntervalleTextesPublies;
	private Date dateDebutIntervalleMesures;
	private Date dateFinIntervalleTextesPublies;
	private Date dateFinIntervalleMesures;
	private Date dateBilan;
	
	private List<TexteBilanSemestrielDTO> listeTextes = new ArrayList<TexteBilanSemestrielDTO>();
	
	
	/**
	 * Crée un nouveau bilanSemestriel. 
	 * Initialise sa date de création (dateBilan) à la date courante.
	 */
	public BilanSemestrielDTO() {
		super();
		this.setDateBilan(new Date());
	}
	
	public void addTexte(String Id, String Titre, String mesuresAttendues, String mesuresAppliquees, String mesuresEnAttente) {
		TexteBilanSemestrielDTO nouveauTexteBilan = new TexteBilanSemestrielDTO(this.type.getLabel(), Id, Titre, mesuresAttendues, mesuresAppliquees, mesuresEnAttente);
		this.listeTextes.add(nouveauTexteBilan);
	}
	
	public List<TexteBilanSemestrielDTO> getListeTextes() {
		return listeTextes;
	}
	
	public BilanSemestrielType getType() {
		return type;
	}

	public void setType(BilanSemestrielType type) {
		this.type = type;
	}

	public Date getDateFinIntervalleTextesPublies() {
		return dateFinIntervalleTextesPublies;
	}

	public void setDateFinIntervalleTextesPublies(Date dateFinIntervalleTextesPublies) {
		this.dateFinIntervalleTextesPublies = dateFinIntervalleTextesPublies;
	}

	public Date getDateFinIntervalleMesures() {
		return dateFinIntervalleMesures;
	}

	public void setDateFinIntervalleMesures(Date dateFinIntervalleMesures) {
		this.dateFinIntervalleMesures = dateFinIntervalleMesures;
	}
	
	public Date getDateDebutIntervalleTextesPublies() {
		return dateDebutIntervalleTextesPublies;
	}

	public void setDateDebutIntervalleTextesPublies(Date dateDebutIntervalleTextesPublies) {
		this.dateDebutIntervalleTextesPublies = dateDebutIntervalleTextesPublies;
	}

	public Date getDateDebutIntervalleMesures() {
		return dateDebutIntervalleMesures;
	}

	public void setDateDebutIntervalleMesures(Date dateDebutIntervalleMesures) {
		this.dateDebutIntervalleMesures = dateDebutIntervalleMesures;
	}

	public Date getDateBilan() {
		return dateBilan;
	}

	public void setDateBilan(Date dateBilan) {
		this.dateBilan = dateBilan;
	}
		
}
