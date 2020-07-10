package fr.dila.solonepg.adamant.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = DossierExtractionLot.ENTITY)
public class DossierExtractionLot {

	public static final String ENTITY = "DOSSIER_EXTRACTION_LOT";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", nullable = false, columnDefinition = "integer")
	private Integer id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name="CREATION_DATE")
	private Date creationDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name="STATUS")
	private ExtractionStatus status;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public ExtractionStatus getStatus() {
		return status;
	}

	public void setStatus(ExtractionStatus status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
