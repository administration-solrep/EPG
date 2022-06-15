package fr.dila.solonepg.elastic.models.search;

import fr.dila.solonepg.elastic.models.search.enums.ElasticOperatorEnum;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;

public class ClauseEt {
    private String field;
    private String valeurMin;
    private String valeurMax;
    private String valeur;
    private Collection<String> listeValeur;
    private ElasticOperatorEnum operator;
    private String nestedPath;

    public ClauseEt() {}

    public String getField() {
        return field;
    }

    public String getFieldPath() {
        if (StringUtils.isNotEmpty(nestedPath)) {
            return nestedPath + "." + field;
        }
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public ElasticOperatorEnum getOperator() {
        return operator;
    }

    public void setOperator(ElasticOperatorEnum operator) {
        this.operator = operator;
    }

    public String getValeurMin() {
        return valeurMin;
    }

    public void setValeurMin(String valeurMin) {
        this.valeurMin = valeurMin;
    }

    public String getValeurMax() {
        return valeurMax;
    }

    public void setValeurMax(String valeurMax) {
        this.valeurMax = valeurMax;
    }

    public Collection<String> getListeValeur() {
        return listeValeur;
    }

    public void setListeValeur(Collection<String> listeValeur) {
        this.listeValeur = listeValeur;
    }

    public String getNestedPath() {
        return StringUtils.defaultString(nestedPath);
    }

    public void setNestedPath(String nestedPath) {
        this.nestedPath = nestedPath;
    }
}
