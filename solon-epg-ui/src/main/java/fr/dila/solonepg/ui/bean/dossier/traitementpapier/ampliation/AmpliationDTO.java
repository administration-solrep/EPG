package fr.dila.solonepg.ui.bean.dossier.traitementpapier.ampliation;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import fr.dila.solonepg.ui.jaxrs.webobject.ajax.EpgDossierAjax;
import fr.dila.st.ui.annot.SwBean;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ws.rs.FormParam;
import org.apache.commons.collections4.CollectionUtils;

@SwBean
public class AmpliationDTO {
    @FormParam("dossierPapierArchive")
    private Boolean dossierPapierArchive = false;

    @FormParam("destinataire")
    private List<String> destinataires;

    @FormParam("destinataireLibre")
    private List<String> destinatairesLibres;

    private String fileName;

    private InputStream file;

    private FormDataContentDisposition fileDisposition;

    @FormParam("expediteur")
    private String expediteur;

    @FormParam("objet")
    private String objet;

    @FormParam("message")
    private String message;

    @FormParam(EpgDossierAjax.DOSSIER_ID)
    private String dossierId;

    private Map<String, String> mapDestinataire = new HashMap<>();

    public AmpliationDTO(FormDataMultiPart multipart) {
        super();
        List<FormDataBodyPart> listFDBP = multipart.getFields("ampliationFile");
        if (CollectionUtils.isNotEmpty(listFDBP)) {
            FormDataBodyPart formDataBodyPart = listFDBP.get(0);
            this.file = ((BodyPartEntity) formDataBodyPart.getEntity()).getInputStream();
            this.fileName = multipart.getField("ampliationFile_nom").getValue();
            this.fileDisposition = formDataBodyPart.getFormDataContentDisposition();
        }

        this.dossierId = multipart.getField(EpgDossierAjax.DOSSIER_ID).getValue();
    }

    public AmpliationDTO() {}

    public Boolean getDossierPapierArchive() {
        return dossierPapierArchive;
    }

    public void setDossierPapierArchive(Boolean dossierPapierArchive) {
        this.dossierPapierArchive = dossierPapierArchive;
    }

    public List<String> getAllDestinataires() {
        Set<String> allDestinataires = new HashSet<>();

        if (CollectionUtils.isNotEmpty(destinataires)) {
            allDestinataires.addAll(destinataires);
        }

        if (CollectionUtils.isNotEmpty(destinatairesLibres)) {
            allDestinataires.addAll(destinatairesLibres);
        }

        return new ArrayList<>(allDestinataires);
    }

    public List<String> getDestinataires() {
        return destinataires;
    }

    public void setDestinataires(List<String> destinataires) {
        this.destinataires = destinataires;
    }

    public List<String> getDestinatairesLibres() {
        return destinatairesLibres;
    }

    public void setDestinatairesLibres(List<String> destinatairesLibres) {
        this.destinatairesLibres = destinatairesLibres;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public FormDataContentDisposition getFileDisposition() {
        return fileDisposition;
    }

    public void setFileDisposition(FormDataContentDisposition fileDisposition) {
        this.fileDisposition = fileDisposition;
    }

    public String getExpediteur() {
        return expediteur;
    }

    public void setExpediteur(String expediteur) {
        this.expediteur = expediteur;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDossierId() {
        return dossierId;
    }

    public void setDossierId(String dossierId) {
        this.dossierId = dossierId;
    }

    public Map<String, String> getMapDestinataire() {
        return mapDestinataire;
    }

    public void setMapDestinataire(Map<String, String> mapDestinataire) {
        this.mapDestinataire = mapDestinataire;
    }
}
