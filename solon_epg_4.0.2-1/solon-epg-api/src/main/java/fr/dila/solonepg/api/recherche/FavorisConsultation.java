package fr.dila.solonepg.api.recherche;

import fr.dila.st.api.domain.STDomainObject;
import java.io.Serializable;
import java.util.List;

public interface FavorisConsultation extends STDomainObject, Serializable {
    public List<String> getDossiersId();

    public void setDossiersId(List<String> dossiersId);

    public List<String> getFdrsId();

    public void setFdrsId(List<String> fdrsId);

    public List<String> getUsers();

    public void setUsers(List<String> usersName);
}
