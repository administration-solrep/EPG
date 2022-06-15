package fr.dila.solonmgpp.api.service;

import fr.dila.solonmgpp.api.organigramme.InstitutionNode;
import java.util.List;

public interface InstitutionService {
    List<InstitutionNode> findAllInstitution();
    List<InstitutionNode> findFilteredInstitution();
}
