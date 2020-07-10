package fr.dila.solonepg.core.mock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.core.cases.TexteMaitreImpl;

public class MockTexteMaitre extends TexteMaitreImpl implements TexteMaitre {
	private static final long serialVersionUID = 1L;

	private String numero;
	private String numeroNor;
	private List<String> mesureIds = new ArrayList<String>();
	private String legislaturePublication;
	private Calendar dateInjection;

	public MockTexteMaitre() {
		super(null);
	}

	@Override
	public String getNumero() {
		return numero;
	}

	@Override
	public void setNumero(String pNumero) {
		numero = pNumero;
	}

	@Override
	public String getNumeroNor() {
		return numeroNor;
	}

	@Override
	public void setNumeroNor(String pNumeroNor) {
		numeroNor = pNumeroNor;
	}

	@Override
	public List<String> getMesuresIds() {
		return mesureIds;
	}

	@Override
	public void setMesuresIds(List<String> pMesuresIds) {
		mesureIds = pMesuresIds;
	}

	@Override
	public String getLegislaturePublication() {
		return legislaturePublication;
	}

	@Override
	public void setLegislaturePublication(String legislaturePublication) {
		this.legislaturePublication = legislaturePublication;
	}

	@Override
	public Calendar getDateInjection() {
		return dateInjection;
	}

	@Override
	public void setDateInjection(Calendar dateInjection) {
		this.dateInjection = dateInjection;
	}
	
	@Override
	public DocumentModel save(CoreSession session) throws ClientException {
		return document;
	}
}
