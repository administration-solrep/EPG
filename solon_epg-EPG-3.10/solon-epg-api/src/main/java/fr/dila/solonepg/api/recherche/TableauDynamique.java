package fr.dila.solonepg.api.recherche;

import java.io.Serializable;
import java.util.List;

import fr.dila.st.api.domain.STDomainObject;

public interface TableauDynamique extends STDomainObject,Serializable{

	public List<String> getUsers();
	public void setUsers(List<String> usersName);
	
	public String getQuery();
	public void setQuery(String query);
	
	public List<String> getDestinatairesId();
	public void setDestinatairesId(List<String> destinatairesId);

}
