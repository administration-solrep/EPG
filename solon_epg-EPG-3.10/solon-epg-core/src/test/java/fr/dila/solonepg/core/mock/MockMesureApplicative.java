package fr.dila.solonepg.core.mock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.core.cases.MesureApplicativeImpl;

public class MockMesureApplicative extends MesureApplicativeImpl implements MesureApplicative {
	private static final long serialVersionUID = 1L;

	private String numeroOrdre;
	private String article;
	private String baseLegale;
	private String objetRIM;
	private Calendar objectif;
	private List<String> decretIds = new ArrayList<String>();

	public MockMesureApplicative() {
		super(null);
	}

	@Override
	public String getNumeroOrdre() {
		return numeroOrdre;
	}

	@Override
	public void setNumeroOrdre(String pNumeroOrdre) {
		numeroOrdre = pNumeroOrdre;
	}

	@Override
	public String getArticle() {
		return article;
	}

	@Override
	public void setArticle(String pArticle) {
		article = pArticle;
	}

	@Override
	public String getBaseLegale() {
		return baseLegale;
	}

	@Override
	public void setBaseLegale(String pBaseLegale) {
		baseLegale = pBaseLegale;
	}

	@Override
	public String getObjetRIM() {
		return objetRIM;
	}

	@Override
	public void setObjetRIM(String pObjet) {
		objetRIM = pObjet;
	}

	@Override
	public Calendar getDateObjectifPublication() {
		return objectif;
	}

	@Override
	public void setDateObjectifPublication(Calendar pDateObjectifPublication) {
		objectif = pDateObjectifPublication;
	}

	@Override
	public List<String> getDecretIds() {
		return decretIds;
	}

	@Override
	public void setDecretIds(List<String> decretIds) {
		this.decretIds = decretIds;
	}
}
