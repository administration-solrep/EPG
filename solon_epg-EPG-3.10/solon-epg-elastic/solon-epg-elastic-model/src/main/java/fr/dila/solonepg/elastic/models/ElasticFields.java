package fr.dila.solonepg.elastic.models;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ElasticFields {
	public static final String MINISTERERESP = ElasticDossier.DOS_MINISTERE_RESP;
	public static final String NUMERONOR = ElasticDossier.DOS_NUMERO_NOR;
	public static final String DATEPARUTIONJORF = ElasticDossier.RETDILA_DATE_PARUTION_JORF;
	public static final String TITREACTE = ElasticDossier.DOS_TITRE_ACTE;
	public static final String STATUT = ElasticDossier.DOS_STATUT;
	public static final String DOC_TITLE = ElasticDocument.DC_TITLE;
	public static final String DOS_CREATED = ElasticDossier.DOS_CREATED;
	public static final String	UID_MAJOR_VERSION	= ElasticDocument.UID_MAJOR_VERSION;
	public static final String	UID_MINOR_VERSION	= ElasticDocument.UID_MINOR_VERSION;


	@JsonProperty(MINISTERERESP)
	private List<String>		ministereresp;

	@JsonProperty(NUMERONOR)
	private List<String>		numeronor;

	@JsonProperty(DATEPARUTIONJORF)
	private List<Date>			dateparutionjorf;

	@JsonProperty(TITREACTE)
	private List<String>		titreacte;

	@JsonProperty(STATUT)
	private List<String>		statut;

	@JsonProperty(DOC_TITLE)
	private List<String> docTitle;
	
	@JsonProperty(DOS_CREATED)
	private List<Timestamp> dosCreated;

	@JsonProperty(UID_MAJOR_VERSION)
	private List<Integer>		uidMajorVersion;

	@JsonProperty(UID_MINOR_VERSION)
	private List<Integer>		uidMinorVersion;

	public List<String> getMinistereresp() {
		return ministereresp;
	}

	public void setMinistereresp(List<String> ministereresp) {
		this.ministereresp = ministereresp;
	}

	public List<String> getNumeronor() {
		return numeronor;
	}

	public void setNumeronor(List<String> numeronor) {
		this.numeronor = numeronor;
	}

	public List<Date> getDateparutionjorf() {
		return dateparutionjorf;
	}

	public void setDateparutionjorf(List<Date> dateparutionjorf) {
		this.dateparutionjorf = dateparutionjorf;
	}

	public List<String> getTitreacte() {
		return titreacte;
	}

	public void setTitreacte(List<String> titreacte) {
		this.titreacte = titreacte;
	}

	public List<String> getStatut() {
		return statut;
	}

	public void setStatus(List<String> statut) {
		this.statut = statut;
	}

	public List<String> getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(List<String> docTitle) {
		this.docTitle = docTitle;
	}

	public List<Timestamp> getDosCreated() {
		return dosCreated;
	}

	public void setDosCreated(List<Timestamp> dosCreated) {
		this.dosCreated = dosCreated;
	}

	public void setUidMajorVersion(List<Integer> uidMajorVersion) {
		this.uidMajorVersion = uidMajorVersion;
	}

	public List<Integer> getUidMajorVersion() {
		return uidMajorVersion;
	}

	public void setUidMinorVersion(List<Integer> uidMinorVersion) {
		this.uidMinorVersion = uidMinorVersion;
	}

	public List<Integer> getUidMinorVersion() {
		return uidMinorVersion;
	}

}
