package fr.dila.solonmgpp.core.service;

import java.util.ArrayList;
import java.util.List;

import fr.dila.solonmgpp.api.organigramme.InstitutionNode;
import fr.dila.solonmgpp.api.service.InstitutionService;
import fr.dila.solonmgpp.core.builder.InstitutionNodeImpl;
import fr.sword.xsd.solon.epp.Institution;

public class InstitutionServiceImpl implements InstitutionService {

    @Override
    public List<InstitutionNode> findAllInstitution() {
        
        List<InstitutionNode> listNode = new ArrayList<InstitutionNode>();
        
        for (Institution institution : Institution.values()) {
            InstitutionNode node = new InstitutionNodeImpl();
            node.setId(institution.value());
            listNode.add(node);
        }
        
        return listNode;
    }

    @Override
    public List<InstitutionNode> findFilteredInstitution() {
      
      List<InstitutionNode> listNode = new ArrayList<InstitutionNode>();
      
      InstitutionNode node = new InstitutionNodeImpl();
      node.setId(Institution.ASSEMBLEE_NATIONALE.value());
      listNode.add(node);
      node = new InstitutionNodeImpl();
      node.setId(Institution.SENAT.value());
      listNode.add(node);
      
      return listNode;
    }

}
