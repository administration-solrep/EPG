package fr.dila.solonepg.core.recherche;

import fr.dila.solonepg.api.constant.EpgUserListConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.st.core.client.AbstractMapDTO;

public class EpgUserListingDTO extends AbstractMapDTO {
    private static final long serialVersionUID = -6369089723223447444L;

    public EpgUserListingDTO() {}

    public String getNom() {
        return getString(EpgUserListConstants.NOM);
    }

    public void setNom(String nom) {
        put(EpgUserListConstants.NOM, nom);
    }

    public String getPrenom() {
        return getString(EpgUserListConstants.PRENOM);
    }

    public void setPrenom(String prenom) {
        put(EpgUserListConstants.PRENOM, prenom);
    }

    public String getUtilisateur() {
        return getString(EpgUserListConstants.UTILISATEUR);
    }

    public void setUtilisateur(String utilisateur) {
        put(EpgUserListConstants.UTILISATEUR, utilisateur);
    }

    public String getMel() {
        return getString(EpgUserListConstants.MEL);
    }

    public void setMel(String mel) {
        put(EpgUserListConstants.MEL, mel);
    }

    public String getDateDebut() {
        return getString(EpgUserListConstants.DATE_DEBUT);
    }

    public void setDateDebut(String dateDebut) {
        put(EpgUserListConstants.DATE_DEBUT, dateDebut);
    }

    @Override
    public String getType() {
        return VocabularyConstants.USER_TYPE;
    }

    @Override
    public String getDocIdForSelection() {
        return getString(EpgUserListConstants.DOC_ID_FOR_SELECTION);
    }
}
