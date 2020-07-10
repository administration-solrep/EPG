package fr.dila.solonepg.elastic.persistence.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity(name = DossierIndexationQuery.ENTITY)
public class DossierIndexationQuery {

	public static final String ENTITY = "DossierIndexationQuery";

	@Id
	private String dossierId;

	/**
	 * Date de l'événement déclenchant l'indexation
	 */
	private Date eventDate;

	/**
	 * Date demandée pour l'indexation (via event)
	 */
	private Date indexationDate;

	/**
	 * Date demandée pour l'indexation (pour une indexation massive)
	 */
	private Date indexationMassiveDate;

	/**
	 * Indique si le dossier lié est supprimé
	 */
	private Boolean deleted = false;

	@OneToOne(optional = true)
	@JoinColumn(name = "dossierId", referencedColumnName = "dossierId")
	private DossierIndexationStatus status;

	public String getDossierId() {
		return dossierId;
	}

	public void setDossierId(String dossierId) {
		this.dossierId = dossierId;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public Date getIndexationDate() {
		return indexationDate;
	}

	public void setIndexationDate(Date indexationDate) {
		this.indexationDate = indexationDate;
	}

	public DossierIndexationStatus getStatus() {
		return status;
	}

	public void setStatus(DossierIndexationStatus status) {
		this.status = status;
	}

	public Date getIndexationMassiveDate() {
		return indexationMassiveDate;
	}

	public void setIndexationMassiveDate(Date indexationMassiveDate) {
		this.indexationMassiveDate = indexationMassiveDate;
	}

	public Boolean isDeleted() {
		return deleted != null && deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

}
