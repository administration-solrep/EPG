package fr.dila.solonepg.adamant.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name = DossierExtractionBordereau.ENTITY)
public class DossierExtractionBordereau {
	public static final String ENTITY = "DOSSIER_EXTRACTION_BORDEREAU";
	
	@EmbeddedId
	private BordereauPK bordereauPK;

	@Column(name = "EXTRACTION_DATE")
	private Date extractionDate;
	
	/**
	 * Statut succès (true) / échec (false) de l'extraction
	 */
	@Column(name = "SUCCESS")
	private Boolean success;
	
	/**
	 * Nom complet du fichier SIP
	 */
	@Column(name = "NOM_SIP")
	private String nomSip;
	
	/**
	 * Empreinte du SIP calculée d'après l'algorithme SHA-512.
	 */
	@Column(name = "EMPREINTE")
	private String empreinte;
	
	/**
	 * Poids du SIP en octets.
	 */
	@Column(name = "POIDS")
	private Long poids;
	
	/**
	 * Type d'acte en toutes lettres.
	 */
	@Column(name = "TYPE_ACTE")
	private String typeActe;
	
	/**
	 * Statut du dossier.
	 */
	@Column(name = "STATUT")
	private String statut;
	
	/**
	 * Version de SOLON dans laquelle le dossier a été généré.
	 */
	@Column(name = "VERSION")
	private String version;	
	
	/**
	 * Message associé à la génération, potentiellement vide. Peut contenir la
	 * trace de l'erreur rencontrée lors de la génération.
	 */
	@Column(name = "MESSAGE")
	private String message;
	
	/**
	 * Statut d'archivage du dossier après l'exécution de l'action.
	 */
	@Column(name = "STATUT_ARCHIVAGE_AFTER")
	private String statutArchivageAfter;
	
	public DossierExtractionBordereau() {
		super();
	}

	public BordereauPK getBordereauPK() {
		return bordereauPK;
	}

	public void setBordereauPK(BordereauPK bordereauPK) {
		this.bordereauPK = bordereauPK;
	}

	public Date getExtractionDate() {
		return extractionDate;
	}

	public void setExtractionDate(Date extractionDate) {
		this.extractionDate = extractionDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEmpreinte() {
		return empreinte;
	}

	public void setEmpreinte(String empreinte) {
		this.empreinte = empreinte;
	}

	public Long getPoids() {
		return poids;
	}

	public void setPoids(Long poids) {
		this.poids = poids;
	}

	public String getTypeActe() {
		return typeActe;
	}

	public void setTypeActe(String typeActe) {
		this.typeActe = typeActe;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getNomSip() {
		return nomSip;
	}

	public void setNomSip(String nomSip) {
		this.nomSip = nomSip;
	}

	public String getStatutArchivageAfter() {
		return statutArchivageAfter;
	}

	public void setStatutArchivageAfter(String statutArchivageAfter) {
		this.statutArchivageAfter = statutArchivageAfter;
	}
	
}
