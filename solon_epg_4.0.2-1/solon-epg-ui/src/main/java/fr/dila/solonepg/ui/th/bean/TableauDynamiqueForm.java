package fr.dila.solonepg.ui.th.bean;

import static fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_DESTINATAIRES_XPATH;
import static fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_DOCUMENT_TYPE;

import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwRequired;
import java.util.ArrayList;
import java.util.HashMap;
import javax.ws.rs.FormParam;

@SwBean(keepdefaultValue = true)
public class TableauDynamiqueForm {

    public TableauDynamiqueForm() {
        super();
    }

    @SwId
    @FormParam("id")
    @NxProp(docType = TABLEAU_DYNAMIQUE_DOCUMENT_TYPE, xpath = STSchemaConstant.ECM_UUID_XPATH)
    private String id;

    @SwRequired
    @FormParam("libelle")
    @NxProp(docType = TABLEAU_DYNAMIQUE_DOCUMENT_TYPE, xpath = STSchemaConstant.DUBLINCORE_TITLE_XPATH)
    private String libelle;

    @FormParam("destinataires")
    @NxProp(docType = TABLEAU_DYNAMIQUE_DOCUMENT_TYPE, xpath = TABLEAU_DYNAMIQUE_DESTINATAIRES_XPATH)
    private ArrayList<String> destinataires;

    private HashMap<String, String> mapDestinataires = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public ArrayList<String> getDestinataires() {
        return destinataires;
    }

    public void setDestinataires(ArrayList<String> destinataires) {
        this.destinataires = destinataires;
    }

    public HashMap<String, String> getMapDestinataires() {
        return mapDestinataires;
    }

    public void setMapDestinataires(HashMap<String, String> mapDestinataires) {
        this.mapDestinataires = mapDestinataires;
    }
}
