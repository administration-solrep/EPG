package fr.dila.solonepg.api.requeteur;

import fr.dila.solonepg.api.enumeration.QueryType;
import fr.dila.st.api.requeteur.RequeteExperte;

public interface EpgRequeteExperte extends RequeteExperte {
    QueryType getQueryType();

    void setQueryType(QueryType queryType);
}
