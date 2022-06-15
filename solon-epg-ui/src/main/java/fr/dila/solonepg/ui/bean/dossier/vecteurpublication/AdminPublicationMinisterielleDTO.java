package fr.dila.solonepg.ui.bean.dossier.vecteurpublication;

import fr.dila.solonepg.api.constant.SolonEpgBulletinOfficielConstants;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import fr.dila.st.ui.validators.annot.SwRegex;
import javax.ws.rs.FormParam;

@SwBean
public class AdminPublicationMinisterielleDTO {
    @NxProp(
        docType = SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_DOCUMENT_TYPE,
        xpath = STSchemaConstant.ECM_UUID_XPATH
    )
    @FormParam("id")
    @SwId
    private String id;

    @NxProp(
        docType = SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_DOCUMENT_TYPE,
        xpath = SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_INTITULE_XPATH
    )
    @SwNotEmpty
    @SwRegex("^[^<>]*$")
    @FormParam("intitulePublication")
    private String intitule;

    @NxProp(
        docType = SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_DOCUMENT_TYPE,
        xpath = SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_NOR_XPATH
    )
    private String nor;

    @SwNotEmpty
    @FormParam("idMinistere")
    private int idMinistere;

    public AdminPublicationMinisterielleDTO() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getNor() {
        return nor;
    }

    public void setNor(String nor) {
        this.nor = nor;
    }

    public int getIdMinistere() {
        return idMinistere;
    }

    public void setIdMinistere(int idMinistere) {
        this.idMinistere = idMinistere;
    }
}
