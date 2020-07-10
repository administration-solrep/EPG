package fr.dila.solonmgpp.core.dto;

import java.util.Date;

import fr.dila.solonmgpp.api.dto.OrganismeDTO;
import fr.sword.xsd.solon.epp.TypeOrganisme;

/**
 * Implementation de {@link OrganismeDTO}
 * 
 * @author admin
 * 
 */
public class OrganismeDTOImpl extends TableReferenceDTOImpl implements OrganismeDTO {

	private TypeOrganisme	type;
	private String			titleModifie;
	private Date			dateDebutMandat;
	private Date			dateFinMandat;

	@Override
	public String getTitle() {
		return getNom();
	}

	@Override
	public TypeOrganisme getType() {
		return type;
	}

	@Override
	public void setType(TypeOrganisme type) {
		this.type = type;
	}

	@Override
	public Date getDateDebutMandat() {
		return dateDebutMandat;
	}

	@Override
	public void setDateDebutMandat(Date dateDebutMandat) {
		this.dateDebutMandat = dateDebutMandat;

	}

	@Override
	public Date getDateFinMandat() {
		return dateFinMandat;
	}

	@Override
	public void setDateFinMandat(Date dateFinMandat) {
		this.dateFinMandat = dateFinMandat;

	}

	@Override
	public String getTitleModifie() {
		return titleModifie;
	}

	@Override
	public void setTitleModifie(String title) {
		this.titleModifie = title;
	}

}
