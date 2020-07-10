package fr.dila.solonepg.elastic.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ElasticHighlight {

	public static final String	TITREACTE	= "dos:titreActe";

	public static final String	TITLE		= "dc:title";

	public static final String	DATA		= "file:data";

	@JsonProperty(TITREACTE)
	private List<String>		titreacte;

	@JsonProperty(TITLE)
	private List<String>		title;

	@JsonProperty(DATA)
	private List<String>		data;

	public List<String> getTitreacte() {
		return titreacte;
	}

	public void setTitreacte(List<String> titreacte) {
		this.titreacte = titreacte;
	}

	public List<String> getTitle() {
		return title;
	}

	public void setTitle(List<String> titile) {
		this.title = titile;
	}

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}

}
