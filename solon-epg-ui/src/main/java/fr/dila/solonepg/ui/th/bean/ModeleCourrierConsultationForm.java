package fr.dila.solonepg.ui.th.bean;

import fr.dila.solonmgpp.api.constant.ModeleCourrierSchemaConstants;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.bean.DocumentDTO;
import fr.dila.st.ui.validators.annot.SwId;
import fr.dila.st.ui.validators.annot.SwNotEmpty;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.FormParam;

public class ModeleCourrierConsultationForm implements ModeleCourrierInterfaceForm {
    @SwId
    @NxProp(docType = ModeleCourrierSchemaConstants.DOCUMENT_NAME, xpath = STSchemaConstant.ECM_UUID_XPATH)
    private String id;

    @SwNotEmpty
    @FormParam("modeleName")
    @NxProp(docType = ModeleCourrierSchemaConstants.DOCUMENT_NAME, xpath = STSchemaConstant.DUBLINCORE_TITLE_XPATH)
    private String modeleName;

    @SwNotEmpty
    @FormParam("typesCommunication")
    @NxProp(
        docType = ModeleCourrierSchemaConstants.DOCUMENT_NAME,
        xpath = ModeleCourrierSchemaConstants.MODELE_COURRIER_TYPES_COMMUNICATION_XPATH
    )
    private List<String> typesCommunication = new ArrayList<>();

    private DocumentDTO piecesJointes;

    public ModeleCourrierConsultationForm() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getModeleName() {
        return modeleName;
    }

    @Override
    public void setModeleName(String modeleName) {
        this.modeleName = modeleName;
    }

    @Override
    public List<String> getTypesCommunication() {
        return typesCommunication;
    }

    @Override
    public void setTypesCommunication(List<String> typesCommunication) {
        this.typesCommunication = typesCommunication;
    }

    public DocumentDTO getPiecesJointes() {
        return piecesJointes;
    }

    public void setPiecesJointes(DocumentDTO piecesJointes) {
        this.piecesJointes = piecesJointes;
    }
}
