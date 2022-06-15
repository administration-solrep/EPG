package fr.dila.solonepg.ui.bean.dossier.traitementpapier.epreuve;

import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.SelectValueDTO;
import java.util.Calendar;
import javax.ws.rs.FormParam;
import org.apache.commons.lang3.ObjectUtils;

@SwBean
public class EpreuveDTO {
    @FormParam("dateEpreuve")
    private Calendar dateEpreuve;

    @FormParam("dateEpreuvePourLe")
    private Calendar dateEpreuvePourLe;

    @FormParam("numeroListe")
    private String numeroListe;

    @FormParam("dateEnvoiRelecture")
    private Calendar dateEnvoiRelecture;

    @FormParam("destinataire")
    private String destinataire;

    @FormParam("signataire")
    private String nomSignataire;

    private SelectValueDTO signataire;

    @FormParam("nouvelleDateEpreuve")
    private Calendar nouvelleDateEpreuve;

    @FormParam("nouvelleDateEpreuvePourLe")
    private Calendar nouvelleDateEpreuvePourLe;

    @FormParam("nouveauNumeroListe")
    private String nouveauNumeroListe;

    @FormParam("nouvelleDateEnvoiRelecture")
    private Calendar nouvelleDateEnvoiRelecture;

    @FormParam("nouveauDestinataire")
    private String nouveauDestinataire;

    @FormParam("nouveauSignataire")
    private String nouveauNomSignataire;

    private SelectValueDTO nouveauSignataire;

    @FormParam("dateRetourBon")
    private Calendar dateRetourBon;

    public EpreuveDTO() {}

    public EpreuveDTO(
        Calendar dateEpreuve,
        Calendar dateEpreuvePourLe,
        String numeroListe,
        Calendar dateEnvoiRelecture,
        String destinataire,
        String nomSignataire,
        SelectValueDTO signataire,
        Calendar nouvelleDateEpreuve,
        Calendar nouvelleDateEpreuvePourLe,
        String nouveauNumeroListe,
        Calendar nouvelleDateEnvoiRelecture,
        String nouveauDestinataire,
        String nouveauNomSignataire,
        SelectValueDTO nouveauSignataire,
        Calendar dateRetourBon
    ) {
        super();
        this.dateEpreuve = dateEpreuve;
        this.dateEpreuvePourLe = dateEpreuvePourLe;
        this.numeroListe = numeroListe;
        this.dateEnvoiRelecture = dateEnvoiRelecture;
        this.destinataire = destinataire;
        this.nomSignataire = nomSignataire;
        this.signataire = signataire;
        this.nouvelleDateEpreuve = nouvelleDateEpreuve;
        this.nouvelleDateEpreuvePourLe = nouvelleDateEpreuvePourLe;
        this.nouveauNumeroListe = nouveauNumeroListe;
        this.nouvelleDateEnvoiRelecture = nouvelleDateEnvoiRelecture;
        this.nouveauDestinataire = nouveauDestinataire;
        this.nouveauNomSignataire = nouveauNomSignataire;
        this.nouveauSignataire = nouveauSignataire;
        this.dateRetourBon = dateRetourBon;
    }

    public Calendar getDateEpreuve() {
        return dateEpreuve;
    }

    public void setDateEpreuve(Calendar dateEpreuve) {
        this.dateEpreuve = dateEpreuve;
    }

    public Calendar getDateEpreuvePourLe() {
        return dateEpreuvePourLe;
    }

    public void setDateEpreuvePourLe(Calendar dateEpreuvePourLe) {
        this.dateEpreuvePourLe = dateEpreuvePourLe;
    }

    public String getNumeroListe() {
        return numeroListe;
    }

    public void setNumeroListe(String numeroListe) {
        this.numeroListe = numeroListe;
    }

    public Calendar getDateEnvoiRelecture() {
        return dateEnvoiRelecture;
    }

    public void setDateEnvoiRelecture(Calendar dateEnvoiRelecture) {
        this.dateEnvoiRelecture = dateEnvoiRelecture;
    }

    public String getDestinataire() {
        return destinataire;
    }

    public void setDestinataire(String destinataire) {
        this.destinataire = destinataire;
    }

    public String getNomSignataire() {
        return nomSignataire;
    }

    public void setNomSignataire(String nomSignataire) {
        this.nomSignataire = nomSignataire;
    }

    public SelectValueDTO getSignataire() {
        return signataire;
    }

    public void setSignataire(SelectValueDTO signataire) {
        this.signataire = signataire;
    }

    public Calendar getNouvelleDateEpreuve() {
        return nouvelleDateEpreuve;
    }

    public void setNouvelleDateEpreuve(Calendar nouvelleDateEpreuve) {
        this.nouvelleDateEpreuve = nouvelleDateEpreuve;
    }

    public Calendar getNouvelleDateEpreuvePourLe() {
        return nouvelleDateEpreuvePourLe;
    }

    public void setNouvelleDateEpreuvePourLe(Calendar nouvelleDateEpreuvePourLe) {
        this.nouvelleDateEpreuvePourLe = nouvelleDateEpreuvePourLe;
    }

    public String getNouveauNumeroListe() {
        return nouveauNumeroListe;
    }

    public void setNouveauNumeroListe(String nouveauNumeroListe) {
        this.nouveauNumeroListe = nouveauNumeroListe;
    }

    public Calendar getNouvelleDateEnvoiRelecture() {
        return nouvelleDateEnvoiRelecture;
    }

    public void setNouvelleDateEnvoiRelecture(Calendar nouvelleDateEnvoiRelecture) {
        this.nouvelleDateEnvoiRelecture = nouvelleDateEnvoiRelecture;
    }

    public String getNouveauDestinataire() {
        return nouveauDestinataire;
    }

    public void setNouveauDestinataire(String nouveauDestinataire) {
        this.nouveauDestinataire = nouveauDestinataire;
    }

    public String getNouveauNomSignataire() {
        return nouveauNomSignataire;
    }

    public void setNouveauNomSignataire(String nouveauNomSignataire) {
        this.nouveauNomSignataire = nouveauNomSignataire;
    }

    public SelectValueDTO getNouveauSignataire() {
        return nouveauSignataire;
    }

    public void setNouveauSignataire(SelectValueDTO nouveauSignataire) {
        this.nouveauSignataire = nouveauSignataire;
    }

    public Calendar getDateRetourBon() {
        return dateRetourBon;
    }

    public void setDateRetourBon(Calendar dateRetourBon) {
        this.dateRetourBon = dateRetourBon;
    }

    public boolean isNouvelleEpreuve() {
        return ObjectUtils.anyNotNull(
            nouveauDestinataire,
            nouveauNomSignataire,
            nouveauSignataire,
            nouveauNumeroListe,
            nouvelleDateEnvoiRelecture,
            nouvelleDateEpreuve,
            nouvelleDateEpreuvePourLe
        );
    }
}
