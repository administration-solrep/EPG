package fr.dila.solonmgpp.core.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO pour la recherche
 *
 * @author admin
 *
 */
public class RechercheDTO {
    private StringBuilder whereClause;
    private List<Object> paramList;

    public RechercheDTO() {
        whereClause = new StringBuilder();
        paramList = new ArrayList<Object>();
    }

    public void setWhereClause(StringBuilder whereClause) {
        this.whereClause = whereClause;
    }

    public StringBuilder getWhereClause() {
        return whereClause;
    }

    public void setParamList(List<Object> paramList) {
        this.paramList = paramList;
    }

    public List<Object> getParamList() {
        return paramList;
    }
}
