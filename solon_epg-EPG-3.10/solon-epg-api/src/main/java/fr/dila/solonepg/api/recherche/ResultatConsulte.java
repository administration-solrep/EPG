package fr.dila.solonepg.api.recherche;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import fr.dila.st.api.domain.STDomainObject;

/**
 * Interface pour les resultats consultes 
 * 
 * @author asatre
 */
public interface ResultatConsulte extends STDomainObject,Serializable{

	List<String> getDossiersId();
	void setDossiersId(List<String> dossiersId);
	/**
	 * ajoute un nouveau dossier en tete de liste
	 * le supprime de la liste si il etait deja present
	 * la liste est tronqué si elle depase la taille max
	 */
	void addNewDossier(final String uuid, int maxResult);
	void removeDossiers(final Collection<String> idsToRemove);
	
	List<String> getFdrsId();
	void setFdrsId(List<String> fdrsId);
	void addNewFdr(final String uuid, int maxResult);
	void removeFdrs(final Collection<String> idsToRemove);
	
	List<String> getUsers();
	void setUsers(List<String> usersName);
	void addNewUser(final String uuid, int maxResult);
	void removeUsers(final Collection<String> idsToRemove);

}
