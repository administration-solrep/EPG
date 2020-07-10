package fr.dila.solonmgpp.api.service;

import java.util.List;

import fr.dila.solonmgpp.api.organigramme.InstitutionNode;

public interface InstitutionService {

    List<InstitutionNode> findAllInstitution();
    List<InstitutionNode> findFilteredInstitution();
}
