package fr.dila.solonepg.core.dto.activitenormative;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import fr.dila.solonepg.api.cases.TranspositionDirective;
import fr.dila.solonepg.api.dto.TranspositionDirectiveDTO;

/**
 * DTo implementation for {@link TranspositionDirective}
 * 
 * @author admin
 * 
 */
public class TranspositionDirectiveDTOImpl implements Serializable, TranspositionDirectiveDTO {

	private static final long	serialVersionUID	= 1610950990094969530L;

	private String				id;
	private String				numero;
	private Date				dateDirective;
	private String				titre;
	private Date				dateEcheance;
	private Boolean				tabAffichageMarcheInt;
	private Date				dateProchainTabAffichage;
	private String				ministerePilote;
	private Boolean				achevee;
	private String				observation;
	private String				directionResponsable;
	private Date				dateTranspositionEffective;
	private String				champLibre;
	private String				etat;
	private String				annee;

	private TranspositionDirectiveDTOImpl() {
		// private default constructor
	}

	public TranspositionDirectiveDTOImpl(TranspositionDirective transpositionDirective) {
		this();

		this.id = transpositionDirective.getId();
		this.numero = transpositionDirective.getNumero();
		this.dateDirective = transpositionDirective.getDateDirective() == null ? null : transpositionDirective
				.getDateDirective().getTime();
		this.dateProchainTabAffichage = transpositionDirective.getDateProchainTabAffichage() == null ? null
				: transpositionDirective.getDateProchainTabAffichage().getTime();
		this.dateEcheance = transpositionDirective.getDateEcheance() == null ? null : transpositionDirective
				.getDateEcheance().getTime();
		this.titre = transpositionDirective.getTitre();
		this.ministerePilote = transpositionDirective.getMinisterePilote();
		this.achevee = transpositionDirective.isAchevee();
		this.observation = transpositionDirective.getObservation();
		this.tabAffichageMarcheInt = transpositionDirective.isTabAffichageMarcheInt();
		this.directionResponsable = transpositionDirective.getDirectionResponsable();
		this.dateTranspositionEffective = transpositionDirective.getDateTranspositionEffective() == null ? null
				: transpositionDirective.getDateTranspositionEffective().getTime();
		this.champLibre = transpositionDirective.getChampLibre();
		this.etat = transpositionDirective.getEtat();

	}

	@Override
	public TranspositionDirective remapField(TranspositionDirective transpositionDirective) {
		transpositionDirective.setAchevee(achevee);

		if (dateDirective == null) {
			transpositionDirective.setDateDirective(null);
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateDirective);
			transpositionDirective.setDateDirective(cal);
		}

		if (dateEcheance == null) {
			transpositionDirective.setDateEcheance(null);
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateEcheance);
			transpositionDirective.setDateEcheance(cal);
		}

		if (dateProchainTabAffichage == null) {
			transpositionDirective.setDateProchainTabAffichage(null);

		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateProchainTabAffichage);
			transpositionDirective.setDateProchainTabAffichage(cal);
		}

		if (dateTranspositionEffective == null) {
			transpositionDirective.setDateTranspositionEffective(null);
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateTranspositionEffective);
			transpositionDirective.setDateTranspositionEffective(cal);
		}

		transpositionDirective.setMinisterePilote(ministerePilote);
		transpositionDirective.setDirectionResponsable(directionResponsable);
		transpositionDirective.setChampLibre(champLibre);

		transpositionDirective.setNumero(numero);
		transpositionDirective.setObservation(observation);
		transpositionDirective.setTitre(titre);
		transpositionDirective.setTabAffichageMarcheInt(tabAffichageMarcheInt);
		transpositionDirective.setEtat(etat);

		return transpositionDirective;
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getNumero() {
		return numero;
	}

	@Override
	public void setNumero(String numero) {
		this.numero = numero;
	}

	@Override
	public Date getDateDirective() {
		return dateDirective;
	}

	@Override
	public void setDateDirective(Date dateDirective) {
		this.dateDirective = dateDirective;
	}

	@Override
	public String getTitre() {
		return titre;
	}

	@Override
	public void setTitre(String titre) {
		this.titre = titre;
	}

	@Override
	public Date getDateEcheance() {
		return dateEcheance;
	}

	@Override
	public void setDateEcheance(Date dateEcheance) {
		this.dateEcheance = dateEcheance;
	}

	@Override
	public Boolean getTabAffichageMarcheInt() {
		return tabAffichageMarcheInt;
	}

	@Override
	public void setTabAffichageMarcheInt(Boolean tabAffichageMarcheInt) {
		this.tabAffichageMarcheInt = tabAffichageMarcheInt;
	}

	@Override
	public Date getDateProchainTabAffichage() {
		return dateProchainTabAffichage;
	}

	@Override
	public void setDateProchainTabAffichage(Date dateProchainTabAffichage) {
		this.dateProchainTabAffichage = dateProchainTabAffichage;
	}

	@Override
	public String getMinisterePilote() {
		return ministerePilote;
	}

	@Override
	public void setMinisterePilote(String ministerePilote) {
		this.ministerePilote = ministerePilote;
	}

	@Override
	public Boolean getAchevee() {
		return achevee;
	}

	@Override
	public void setAchevee(Boolean achevee) {
		this.achevee = achevee;
	}

	@Override
	public String getObservation() {
		return observation;
	}

	@Override
	public void setObservation(String observation) {
		this.observation = observation;
	}

	@Override
	public void setDirectionResponsable(String directionResponsable) {
		this.directionResponsable = directionResponsable;
	}

	@Override
	public String getDirectionResponsable() {
		return directionResponsable;
	}

	public void setDateTranspositionEffective(Date dateTranspositionEffective) {
		this.dateTranspositionEffective = dateTranspositionEffective;
	}

	public Date getDateTranspositionEffective() {
		return dateTranspositionEffective;
	}

	public void setChampLibre(String champLibre) {
		this.champLibre = champLibre;
	}

	public String getChampLibre() {
		return champLibre;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

}
