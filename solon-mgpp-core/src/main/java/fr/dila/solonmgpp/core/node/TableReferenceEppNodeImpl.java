package fr.dila.solonmgpp.core.node;

import fr.dila.solonmgpp.api.node.TableReferenceEppNode;
import fr.dila.st.core.util.DateUtil;
import java.util.Date;

public class TableReferenceEppNodeImpl implements TableReferenceEppNode {
    private String identifiant;
    private String typeValue;
    private Date dateDebut;
    private Date dateFin;
    private String label;
    private Boolean hasChildren;

    public TableReferenceEppNodeImpl() {}

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public Date getDateDebut() {
        return DateUtil.copyDate(dateDebut);
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = DateUtil.copyDate(dateDebut);
    }

    public Date getDateFin() {
        return DateUtil.copyDate(dateFin);
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = DateUtil.copyDate(dateFin);
    }

    public String getLabel() {
        return label;
    }

    public String getStyle() {
        return (this.dateFin != null && this.dateFin.getTime() < (new Date()).getTime()) ? "font-style:italic" : "";
    }

    public Boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean value) {
        this.hasChildren = value;
    }
}
