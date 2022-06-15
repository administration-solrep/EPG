package fr.dila.solonepg.ui.bean.dossier.traitementpapier.publication;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.st.ui.annot.NxProp;
import fr.dila.st.ui.annot.NxProp.Way;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.SelectValueDTO;
import java.util.Calendar;
import javax.ws.rs.FormParam;

@SwBean
public class PublicationDTO {
    @FormParam("dateEnvoiJO")
    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_DATE_ENVOI_JO_PROPERTY
    )
    private Calendar dateEnvoiJO;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_NOM_PUBLICATION_PROPERTY
    )
    @FormParam("vecteurPublication")
    private String nomVecteurPublication;

    private SelectValueDTO vecteurPublication;

    @FormParam("modeParution")
    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_MODE_PUBLICATION_PROPERTY
    )
    private String nomModeParution;

    private SelectValueDTO modeParution;

    @FormParam("epreuvesRetour")
    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_EPREUVE_EN_RETOUR_PROPERTY
    )
    private Boolean epreuvesRetour;

    @FormParam("delaiPublication")
    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_DELAI_PROPERTY
    )
    private String nomDelaiPublication;

    private SelectValueDTO delaiPublication;

    @FormParam("datePublication")
    private Calendar datePublication;

    @FormParam("numeroListe")
    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_PUBLICATION_NUMERO_LISTE_PROPERTY,
        way = Way.DOC_TO_BEAN
    )
    private String numeroListe;

    @FormParam("dateParutionJORF")
    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_JORF_PROPERTY,
        way = Way.DOC_TO_BEAN
    )
    private Calendar dateParutionJORF;

    public PublicationDTO() {}

    public PublicationDTO(
        Calendar dateEnvoiJO,
        String nomVecteurPublication,
        SelectValueDTO vecteurPublication,
        String nomModeParution,
        SelectValueDTO modeParution,
        Boolean epreuvesRetour,
        String nomDelaiPublication,
        SelectValueDTO delaiPublication,
        Calendar datePublication,
        String numeroListe,
        Calendar dateParutionJORF
    ) {
        super();
        this.dateEnvoiJO = dateEnvoiJO;
        this.nomVecteurPublication = nomVecteurPublication;
        this.vecteurPublication = vecteurPublication;
        this.nomModeParution = nomModeParution;
        this.modeParution = modeParution;
        this.epreuvesRetour = epreuvesRetour;
        this.nomDelaiPublication = nomDelaiPublication;
        this.delaiPublication = delaiPublication;
        this.datePublication = datePublication;
        this.numeroListe = numeroListe;
        this.dateParutionJORF = dateParutionJORF;
    }

    public Calendar getDateEnvoiJO() {
        return dateEnvoiJO;
    }

    public void setDateEnvoiJO(Calendar dateEnvoiJO) {
        this.dateEnvoiJO = dateEnvoiJO;
    }

    public String getNomVecteurPublication() {
        return nomVecteurPublication;
    }

    public void setNomVecteurPublication(String nomVecteurPublication) {
        this.nomVecteurPublication = nomVecteurPublication;
    }

    public SelectValueDTO getVecteurPublication() {
        return vecteurPublication;
    }

    public void setVecteurPublication(SelectValueDTO vecteurPublication) {
        this.vecteurPublication = vecteurPublication;
    }

    public String getNomModeParution() {
        return nomModeParution;
    }

    public void setNomModeParution(String nomModeParution) {
        this.nomModeParution = nomModeParution;
    }

    public SelectValueDTO getModeParution() {
        return modeParution;
    }

    public void setModeParution(SelectValueDTO modeParution) {
        this.modeParution = modeParution;
    }

    public Boolean getEpreuvesRetour() {
        return epreuvesRetour;
    }

    public void setEpreuvesRetour(Boolean epreuvesRetour) {
        this.epreuvesRetour = epreuvesRetour;
    }

    public String getNomDelaiPublication() {
        return nomDelaiPublication;
    }

    public void setNomDelaiPublication(String nomDelaiPublication) {
        this.nomDelaiPublication = nomDelaiPublication;
    }

    public SelectValueDTO getDelaiPublication() {
        return delaiPublication;
    }

    public void setDelaiPublication(SelectValueDTO delaiPublication) {
        this.delaiPublication = delaiPublication;
    }

    public Calendar getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Calendar datePublication) {
        this.datePublication = datePublication;
    }

    public String getNumeroListe() {
        return numeroListe;
    }

    public void setNumeroListe(String numeroListe) {
        this.numeroListe = numeroListe;
    }

    public Calendar getDateParutionJORF() {
        return dateParutionJORF;
    }

    public void setDateParutionJORF(Calendar dateParutionJORF) {
        this.dateParutionJORF = dateParutionJORF;
    }
}
