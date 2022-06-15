package fr.dila.solonepg.ui.bean.dossier.traitementpapier.communication;

import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import java.io.Serializable;
import java.util.Calendar;
import javax.ws.rs.QueryParam;

@SwBean
public class DestinataireCommunicationDTO implements Serializable {
    private static final long serialVersionUID = 8826269764376661370L;

    @NxProp(docType = "", xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_ID_COMMUNICATION_PROPERTY)
    private String id;

    @QueryParam("destinataire")
    @NxProp(docType = "", xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_NOM_DESTINATAIRE_COMMUNICATION_PROPERTY)
    protected String destinataireCommunication;

    protected String destinataireCommunicationLabel;

    @QueryParam("dateEnvoi")
    @NxProp(docType = "", xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_COMMUNICATION_PROPERTY)
    protected Calendar dateEnvoi;

    @QueryParam("dateRetour")
    @NxProp(docType = "", xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_COMMUNICATION_PROPERTY)
    protected Calendar dateRetour;

    @QueryParam("dateRelance")
    @NxProp(docType = "", xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RELANCE_COMMUNICATION_PROPERTY)
    protected Calendar dateRelance;

    @QueryParam("sensAvis")
    @NxProp(docType = "", xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_SENS_AVIS_COMMUNICATION_PROPERTY)
    protected String sensAvis;

    protected String sensAvisLabel;

    @QueryParam("objet")
    @NxProp(docType = "", xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_OBJET_COMMUNICATION_PROPERTY)
    protected String objet;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestinataireCommunication() {
        return destinataireCommunication;
    }

    public void setDestinataireCommunication(String nomDestinataireCommunication) {
        this.destinataireCommunication = nomDestinataireCommunication;
    }

    public String getDestinataireCommunicationLabel() {
        return destinataireCommunicationLabel;
    }

    public void setDestinataireCommunicationLabel(String destinataireCommunicationLabel) {
        this.destinataireCommunicationLabel = destinataireCommunicationLabel;
    }

    public Calendar getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Calendar dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public Calendar getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Calendar dateRetour) {
        this.dateRetour = dateRetour;
    }

    public Calendar getDateRelance() {
        return dateRelance;
    }

    public void setDateRelance(Calendar dateRelance) {
        this.dateRelance = dateRelance;
    }

    public String getSensAvis() {
        return sensAvis;
    }

    public void setSensAvis(String sensAvis) {
        this.sensAvis = sensAvis;
    }

    public String getSensAvisLabel() {
        return sensAvisLabel;
    }

    public void setSensAvisLabel(String sensAvisLabel) {
        this.sensAvisLabel = sensAvisLabel;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }
}
