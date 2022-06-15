package fr.dila.solonepg.api.recherche;

import fr.dila.solonepg.api.enums.FavorisRechercheType;
import fr.dila.st.api.domain.STDomainObject;
import java.io.Serializable;
import java.util.List;

public interface TableauDynamique extends STDomainObject, Serializable {
    List<String> getUsers();

    void setUsers(List<String> usersName);

    FavorisRechercheType getType();

    void setType(FavorisRechercheType type);

    String getQuery();

    void setQuery(String query);

    List<String> getDestinatairesId();

    void setDestinatairesId(List<String> destinatairesId);
}
