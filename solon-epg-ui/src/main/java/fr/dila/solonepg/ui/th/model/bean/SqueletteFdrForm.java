package fr.dila.solonepg.ui.th.model.bean;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.ss.ui.bean.FdrDTO;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwLength;
import fr.dila.st.ui.validators.annot.SwRequired;
import javax.ws.rs.FormParam;

@SwBean
public class SqueletteFdrForm {
    @FormParam("id")
    @SwId
    private String id;

    @NxProp(
        docType = SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE,
        xpath = STSchemaConstant.DUBLINCORE_TITLE_XPATH
    )
    @SwRequired
    @FormParam("intitule")
    @SwLength(max = 500)
    private String intitule;

    @NxProp(
        docType = SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE,
        xpath = SolonEpgSchemaConstant.SOLON_EPG_TYPE_ACTE_FEUILLE_ROUTE_XPATH
    )
    @SwRequired
    @FormParam("typeActe")
    @SwLength(max = 50)
    private String typeActe;

    @NxProp(
        docType = SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE,
        xpath = STSchemaConstant.DUBLINCORE_DESCRIPTION_PROPERTY
    )
    @FormParam("description")
    @SwLength(max = 500)
    private String description;

    private String typeActeLibelle;

    private String etat;

    private Boolean isLock;

    private Boolean isLockByCurrentUser;

    private FdrDTO squeletteDto;

    public SqueletteFdrForm() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(String typeActe) {
        this.typeActe = typeActe;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeActeLibelle() {
        return typeActeLibelle;
    }

    public void setTypeActeLibelle(String typeActeLibelle) {
        this.typeActeLibelle = typeActeLibelle;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Boolean getIsLock() {
        return isLock;
    }

    public void setIsLock(Boolean isLock) {
        this.isLock = isLock;
    }

    public Boolean getIsLockByCurrentUser() {
        return isLockByCurrentUser;
    }

    public void setIsLockByCurrentUser(Boolean isLockByCurrentUser) {
        this.isLockByCurrentUser = isLockByCurrentUser;
    }

    public FdrDTO getSqueletteDto() {
        return squeletteDto;
    }

    public void setSqueletteDto(FdrDTO squeletteDto) {
        this.squeletteDto = squeletteDto;
    }
}
