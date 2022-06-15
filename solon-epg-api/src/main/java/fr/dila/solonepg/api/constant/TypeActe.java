package fr.dila.solonepg.api.constant;

import com.google.common.base.Objects;

/**
 * Représentation d'un type d'Acte : identifiant, label et nor associé au type
 * Utilisé dans le service des types d'actes
 *
 * @author Antoine Rolin
 *
 */
public class TypeActe implements Comparable<TypeActe> {
    private String id;
    private String label;
    private String nor;

    public TypeActe(String id, String label, String nor) {
        this.id = id;
        this.label = label;
        this.nor = nor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    @Override
    public int compareTo(TypeActe arg0) {
        return label.compareTo(arg0.getLabel());
    }

    @Override
    public boolean equals(Object arg0) {
        if (arg0 instanceof TypeActe) {
            return this.compareTo((TypeActe) arg0) == 0;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(label.hashCode());
    }
}
