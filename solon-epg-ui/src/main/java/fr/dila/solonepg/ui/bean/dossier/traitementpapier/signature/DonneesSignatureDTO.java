package fr.dila.solonepg.ui.bean.dossier.traitementpapier.signature;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.validators.annot.SwLength;
import java.io.Serializable;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class DonneesSignatureDTO implements Serializable {
    private static final long serialVersionUID = 978100399376635402L;

    @NxProp(docType = "", xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_ENVOI_SIGNATURE_PROPERTY)
    @FormParam("dateEnvoi")
    protected Calendar dateEnvoi;

    @NxProp(docType = "", xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_SIGNATURE_PROPERTY)
    @FormParam("dateRetour")
    protected Calendar dateRetour;

    @SwLength(max = SolonEpgConstant.MAX_LENGTH_FORM_2000)
    @NxProp(docType = "", xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_COMMENTAIRE_SIGNATURE_PROPERTY)
    @FormParam("commentaire")
    protected String commentaire;

    public DonneesSignatureDTO() {}

    public DonneesSignatureDTO(Calendar dateEnvoi, Calendar dateRetour, String commentaire) {
        this.dateEnvoi = dateEnvoi;
        this.dateRetour = dateRetour;
        this.commentaire = commentaire;
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

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
