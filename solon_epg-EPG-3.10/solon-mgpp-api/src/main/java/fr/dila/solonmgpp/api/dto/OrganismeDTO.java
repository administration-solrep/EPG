package fr.dila.solonmgpp.api.dto;

import fr.sword.xsd.solon.epp.TypeOrganisme;

/**
 * interface de representation d'un Organisme
 * 
 * @author asatre
 * 
 */
public interface OrganismeDTO extends TableReferenceDTO {

    TypeOrganisme getType();

    void setType(TypeOrganisme type);

}
