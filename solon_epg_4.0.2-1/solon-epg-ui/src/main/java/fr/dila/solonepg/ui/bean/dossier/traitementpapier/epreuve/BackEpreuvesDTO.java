package fr.dila.solonepg.ui.bean.dossier.traitementpapier.epreuve;

import fr.dila.solonepg.api.cases.typescomplexe.InfoEpreuve;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.TraitementPapierConstants;
import fr.dila.solonepg.core.cases.typescomplexe.InfoEpreuveImpl;
import fr.dila.st.ui.annot.NxProp;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

public class BackEpreuvesDTO {
    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_EPREUVE_XPATH
    )
    private Map<String, Serializable> epreuve;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_NOUVELLE_DEMANDE_EPREUVES_XPATH
    )
    private Map<String, Serializable> nouvelleEpreuve;

    @NxProp(
        docType = DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE,
        xpath = TraitementPapierConstants.TRAITEMENT_PAPIER_RETOUR_DU_BON_A_TITRE_LE_XPATH
    )
    private Calendar dateRetourBon;

    public BackEpreuvesDTO() {
        // Default constructor
    }

    public BackEpreuvesDTO(EpreuveDTO epreuveDTO) {
        InfoEpreuve epreuve = new InfoEpreuveImpl();
        epreuve.setEnvoiRelectureLe(epreuveDTO.getDateEnvoiRelecture());
        epreuve.setEpreuveDemandeLe(epreuveDTO.getDateEpreuve());
        epreuve.setEpreuvePourLe(epreuveDTO.getDateEpreuvePourLe());
        epreuve.setDestinataireEntete(epreuveDTO.getDestinataire());
        epreuve.setNomsSignataires(epreuveDTO.getNomSignataire());
        epreuve.setNumeroListe(epreuveDTO.getNumeroListe());
        this.epreuve = epreuve.getSerializableMap();

        InfoEpreuve nouvelleEpreuve = new InfoEpreuveImpl();
        nouvelleEpreuve.setEnvoiRelectureLe(epreuveDTO.getNouvelleDateEnvoiRelecture());
        nouvelleEpreuve.setEpreuveDemandeLe(epreuveDTO.getNouvelleDateEpreuve());
        nouvelleEpreuve.setEpreuvePourLe(epreuveDTO.getNouvelleDateEpreuvePourLe());
        nouvelleEpreuve.setDestinataireEntete(epreuveDTO.getNouveauDestinataire());
        nouvelleEpreuve.setNomsSignataires(epreuveDTO.getNouveauNomSignataire());
        nouvelleEpreuve.setNumeroListe(epreuveDTO.getNouveauNumeroListe());
        this.nouvelleEpreuve = nouvelleEpreuve.getSerializableMap();

        this.dateRetourBon = epreuveDTO.getDateRetourBon();
    }

    public EpreuveDTO toEpreuveDTO() {
        EpreuveDTO epreuveDTO = new EpreuveDTO();

        InfoEpreuve epreuve = new InfoEpreuveImpl(this.epreuve);
        epreuveDTO.setDateEnvoiRelecture(epreuve.getEnvoiRelectureLe());
        epreuveDTO.setDateEpreuve(epreuve.getEpreuveDemandeLe());
        epreuveDTO.setDateEpreuvePourLe(epreuve.getEpreuvePourLe());
        epreuveDTO.setDestinataire(epreuve.getDestinataireEntete());
        epreuveDTO.setNomSignataire(epreuve.getNomsSignataires());
        epreuveDTO.setNumeroListe(epreuve.getNumeroListe());

        InfoEpreuve nouvelleEpreuve = new InfoEpreuveImpl(this.nouvelleEpreuve);
        epreuveDTO.setNouvelleDateEnvoiRelecture(nouvelleEpreuve.getEnvoiRelectureLe());
        epreuveDTO.setNouvelleDateEpreuve(nouvelleEpreuve.getEpreuveDemandeLe());
        epreuveDTO.setNouvelleDateEpreuvePourLe(nouvelleEpreuve.getEpreuvePourLe());
        epreuveDTO.setNouveauDestinataire(nouvelleEpreuve.getDestinataireEntete());
        epreuveDTO.setNouveauNomSignataire(nouvelleEpreuve.getNomsSignataires());
        epreuveDTO.setNouveauNumeroListe(nouvelleEpreuve.getNumeroListe());

        epreuveDTO.setDateRetourBon(dateRetourBon);
        return epreuveDTO;
    }
}
