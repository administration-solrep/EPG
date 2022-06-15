package fr.dila.solonepg.elastic.models.config;

import fr.dila.solonepg.elastic.models.search.enums.QueryTypeEnum;
import fr.dila.solonepg.elastic.models.search.enums.TypeEnum;

public class QueryBuilderProperty {
    private String field;
    private String label;
    private TypeEnum type;
    private String nestedPath;
    private boolean stored;
    private boolean hasKeyword;
    private boolean returnable;
    private QueryTypeEnum queryType;
    private boolean sortable;

    public QueryBuilderProperty() {}

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public TypeEnum getType() {
        return type;
    }

    public void setType(TypeEnum type) {
        this.type = type;
    }

    public String getNestedPath() {
        return nestedPath;
    }

    public void setNestedPath(String nestedPath) {
        this.nestedPath = nestedPath;
    }

    public boolean isStored() {
        return stored;
    }

    public void setStored(boolean stored) {
        this.stored = stored;
    }

    public boolean isHasKeyword() {
        return hasKeyword;
    }

    public void setHasKeyword(boolean hasKeyword) {
        this.hasKeyword = hasKeyword;
    }

    public boolean isReturnable() {
        return returnable;
    }

    public void setReturnable(boolean returnable) {
        this.returnable = returnable;
    }

    public QueryTypeEnum getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryTypeEnum queryType) {
        this.queryType = queryType;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }
}
