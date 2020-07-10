package fr.dila.solonepg.core.recherche;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants;
import fr.dila.solonepg.api.recherche.ResultatConsulte;
import fr.dila.st.core.domain.STDomainObjectImpl;

public class ResultatConsulteImpl extends STDomainObjectImpl implements ResultatConsulte {

	private static final long serialVersionUID = 8970921482127283294L;

	public ResultatConsulteImpl(DocumentModel doc) {
		super(doc);
	}

	@Override
	public List<String> getDossiersId() {
		return getListStringProperty(SolonEpgEspaceRechercheConstants.RESULTAT_CONSULTE_SCHEMA, SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_DOSSIERSID);
	}

	@Override
	public void setDossiersId(List<String> dossiersId) {
		setProperty(SolonEpgEspaceRechercheConstants.RESULTAT_CONSULTE_SCHEMA, SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_DOSSIERSID, dossiersId);
		
	}
	
	@Override 
	public void addNewDossier(final String uuid, int maxResult) {
		setDossiersId(addToList(getDossiersId(), uuid, maxResult));
	}
	
	@Override
	public void removeDossiers(final Collection<String> idsToRemove){
		final List<String> copy = new LinkedList<String>(getDossiersId());
		copy.removeAll(idsToRemove);
		setDossiersId(copy);
	}

	@Override
	public List<String> getFdrsId() {
		return getListStringProperty(SolonEpgEspaceRechercheConstants.RESULTAT_CONSULTE_SCHEMA, SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_FDRSID);
	}

	@Override
	public void setFdrsId(List<String> fdrsId) {
		setProperty(SolonEpgEspaceRechercheConstants.RESULTAT_CONSULTE_SCHEMA, SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_FDRSID, fdrsId);
	}
	
	@Override 
	public void addNewFdr(final String uuid, int maxResult) {
		setFdrsId(addToList(getFdrsId(), uuid, maxResult));
	}
	
	@Override
	public void removeFdrs(final Collection<String> idsToRemove){
		final List<String> copy = new LinkedList<String>(getFdrsId());
		copy.removeAll(idsToRemove);
		setFdrsId(copy);
	}

	@Override
	public List<String> getUsers() {
		return getListStringProperty(SolonEpgEspaceRechercheConstants.RESULTAT_CONSULTE_SCHEMA, SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_USERSNAME);
	}

	@Override
	public void setUsers(List<String> usersName) {
		setProperty(SolonEpgEspaceRechercheConstants.RESULTAT_CONSULTE_SCHEMA, SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_USERSNAME, usersName);
	}
	
	@Override 
	public void addNewUser(final String uuid, int maxResult) {
		setUsers(addToList(getUsers(), uuid, maxResult));
	}
	
	@Override
	public void removeUsers(final Collection<String> idsToRemove){
		final List<String> copy = new LinkedList<String>(getUsers());
		copy.removeAll(idsToRemove);
		setUsers(copy);
	}
	
	private List<String> addToList(final List<String> docList, final String uuid, int maxResult){
		final LinkedList<String> listidDossier = new LinkedList<String>(docList);
        listidDossier.remove(uuid);
        listidDossier.push(uuid);

        if (listidDossier.size() > maxResult) {
            // il faut supprimer le dernier resultat consulte
            listidDossier.remove(listidDossier.size() - 1);
        }
        return listidDossier;
	}

}
