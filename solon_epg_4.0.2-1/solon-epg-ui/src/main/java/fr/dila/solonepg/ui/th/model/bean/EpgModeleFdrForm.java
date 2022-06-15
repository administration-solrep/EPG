package fr.dila.solonepg.ui.th.model.bean;

import fr.dila.ss.ui.th.bean.ModeleFdrForm;
import fr.dila.st.ui.annot.SwBean;
import javax.ws.rs.FormParam;

@SwBean
public class EpgModeleFdrForm extends ModeleFdrForm {
    @FormParam("numero")
    private Integer numero;

    @FormParam("typeActe")
    private String typeActe;

    private String typeActeLibelle;

    @FormParam("intituleLibre")
    private String intituleLibre;

    private String intituleComplet;

    private boolean isFeuilleRouteDefautGlobal;

    public String getIntituleComplet() {
        return intituleComplet;
    }

    public void setIntituleComplet(String intituleComplet) {
        this.intituleComplet = intituleComplet;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(String typeActe) {
        this.typeActe = typeActe;
    }

    public String getIntituleLibre() {
        return intituleLibre;
    }

    public void setIntituleLibre(String intituleLibre) {
        this.intituleLibre = intituleLibre;
    }

    public String getTypeActeLibelle() {
        return typeActeLibelle;
    }

    public void setTypeActeLibelle(String typeActeLibelle) {
        this.typeActeLibelle = typeActeLibelle;
    }

    public boolean getIsFeuilleRouteDefautGlobal() {
        return isFeuilleRouteDefautGlobal;
    }

    public void setIsFeuilleRouteDefautGlobal(boolean isFeuilleRouteDefautGlobal) {
        this.isFeuilleRouteDefautGlobal = isFeuilleRouteDefautGlobal;
    }
}
