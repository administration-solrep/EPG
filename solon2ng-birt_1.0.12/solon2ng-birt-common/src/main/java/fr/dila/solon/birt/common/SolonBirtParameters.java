package fr.dila.solon.birt.common;

import java.io.Serializable;
import java.util.Map;

/**
 * Contient l'ensemble des paramètres à transférer depuis les applications
 * SOLON-Réponses vers l'exécutable Birt. Le transfert se fait en serializant
 * depuis l'application SOLON-Réponses et en déserializant dans l'application
 * Birt (au format Json).
 * 
 * @author tlombard
 */
public class SolonBirtParameters implements Serializable {
	private static final long serialVersionUID = -4907880709033298676L;

	/** Chemin du rapport rptdesign dans models/. */
	private String reportModelName;

	/** Format de rapport souhaité. */
	private BirtOutputFormat outputFormat;

	/** Scalar parameters */
	private Map<String, ?> scalarParameters;
	
	/** (facultatif) : chemin et nom complet du fichier à générer. */
	private String resultPathName;

	private String jdbcUrl;
	private String jdbcUser;
	private String jdbcPassword;
	private String jdbcDriver;
	private boolean track;

	public SolonBirtParameters() {
	    super();
	}
	
	public String getReportModelName() {
		return reportModelName;
	}

	public void setReportModelName(String reportModelName) {
		this.reportModelName = reportModelName;
	}

	public BirtOutputFormat getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(BirtOutputFormat outputFormat) {
		this.outputFormat = outputFormat;
	}

	public Map<String, ?> getScalarParameters() {
		return scalarParameters;
	}

	public void setScalarParameters(Map<String, ?> scalarParameters) {
		this.scalarParameters = scalarParameters;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getJdbcUser() {
		return jdbcUser;
	}

	public void setJdbcUser(String jdbcUser) {
		this.jdbcUser = jdbcUser;
	}

	public String getJdbcPassword() {
		return jdbcPassword;
	}

	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public String getResultPathName() {
		return resultPathName;
	}

	public void setResultPathName(String resultPathName) {
		this.resultPathName = resultPathName;
	}

	public boolean isTrack() {
		return track;
	}

	public void setTrack(boolean track) {
		this.track = track;
	}
}
