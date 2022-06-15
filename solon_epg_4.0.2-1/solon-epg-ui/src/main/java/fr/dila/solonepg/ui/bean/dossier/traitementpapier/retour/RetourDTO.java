package fr.dila.solonepg.ui.bean.dossier.traitementpapier.retour;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.SelectValueDTO;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class RetourDTO {
    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_RETOUR_A_XPATH
    )
    @FormParam("retourA")
    private String retourA;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_DATE_RETOUR_XPATH
    )
    @FormParam("date")
    private Calendar date;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_MOTIF_RETOUR_XPATH
    )
    @FormParam("motif")
    private String motif;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_NOMS_SIGNATAIRES_RETOUR_XPATH
    )
    @FormParam("nomsSignataires")
    private String nomsSignataires;

    private SelectValueDTO signataire;

    public RetourDTO() {}

    public String getRetourA() {
        return retourA;
    }

    public void setRetourA(String retourA) {
        this.retourA = retourA;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getNomsSignataires() {
        return nomsSignataires;
    }

    public void setNomsSignataires(String nomsSignataires) {
        this.nomsSignataires = nomsSignataires;
    }

    public SelectValueDTO getSignataire() {
        return signataire;
    }

    public void setSignataire(SelectValueDTO signataire) {
        this.signataire = signataire;
    }
}
