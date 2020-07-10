package fr.dila.solonepg.core.mock;

import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.core.cases.DecretImpl;

public class MockDecret extends DecretImpl implements Decret {

	private static final long serialVersionUID = 1L;

	private String numeroNor;
	private String titreOfficiel;

	public MockDecret(String pNumeroNor, String pTitreOfficiel) {
		super(null);
		this.numeroNor = pNumeroNor;
		this.titreOfficiel = pTitreOfficiel;
	}

	@Override
	public String getNumeroNor() {
		return numeroNor;
	}

	@Override
	public void setNumeroNor(String numeroNor) {
		this.numeroNor = numeroNor;
	}

	@Override
	public String getTitreOfficiel() {
		return titreOfficiel;
	}

	@Override
	public void setTitreOfficiel(String titreOfficiel) {
		this.titreOfficiel = titreOfficiel;
	}
}
