package fr.dila.solonepg.ui.bean.dossier.traitementpapier.communication;

import fr.dila.st.ui.bean.SelectValueDTO;
import java.io.Serializable;
import java.util.List;
import org.nuxeo.ecm.platform.actions.Action;

public class PutAndGetTemplateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private DestinataireCommunicationDTO destinataire;
    private String title;
    private List<SelectValueDTO> listDestinataire;
    private List<Action> addAction;
    private List<Action> editAction;

    public PutAndGetTemplateDTO() {}

    public PutAndGetTemplateDTO(
        String type,
        DestinataireCommunicationDTO newDestinataire,
        String title,
        List<SelectValueDTO> listDestinataire,
        List<Action> addAction,
        List<Action> editAction
    ) {
        this.type = type;
        this.destinataire = newDestinataire;
        this.title = title;
        this.listDestinataire = listDestinataire;
        this.addAction = addAction;
        this.editAction = editAction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DestinataireCommunicationDTO getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(DestinataireCommunicationDTO destinataire) {
        this.destinataire = destinataire;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SelectValueDTO> getListDestinataire() {
        return listDestinataire;
    }

    public void setListDestinataire(List<SelectValueDTO> listDestinataire) {
        this.listDestinataire = listDestinataire;
    }

    public List<Action> getAddAction() {
        return addAction;
    }

    public void setAddAction(List<Action> addAction) {
        this.addAction = addAction;
    }

    public List<Action> getEditAction() {
        return editAction;
    }

    public void setEditAction(List<Action> editAction) {
        this.editAction = editAction;
    }
}
