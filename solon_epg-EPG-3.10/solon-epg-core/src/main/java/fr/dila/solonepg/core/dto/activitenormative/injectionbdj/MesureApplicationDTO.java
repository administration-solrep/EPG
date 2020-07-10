package fr.dila.solonepg.core.dto.activitenormative.injectionbdj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Mesure d'application unitaire envoyée au BDJ (élément Mesure)
 * 
 * @author tlombard
 */
public class MesureApplicationDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer numero;

	private String articleTexte;

	private String baseLegale;

	private String objet;

	private String objectif;

	private List<DecretApplicatifDTO> decrets = new ArrayList<DecretApplicatifDTO>();

	public MesureApplicationDTO() {
		super();
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public List<DecretApplicatifDTO> getDecrets() {
		return decrets;
	}

	public void setDecrets(List<DecretApplicatifDTO> decrets) {
		this.decrets = decrets;
	}

	public String getArticleTexte() {
		return articleTexte;
	}

	public void setArticleTexte(String articleTexte) {
		this.articleTexte = articleTexte;
	}

	public String getBaseLegale() {
		return baseLegale;
	}

	public void setBaseLegale(String baseLegale) {
		this.baseLegale = baseLegale;
	}

	public String getObjet() {
		return objet;
	}

	public void setObjet(String objet) {
		this.objet = objet;
	}

	public String getObjectif() {
		return objectif;
	}

	public void setObjectif(String objectif) {
		this.objectif = objectif;
	}
}
