package fr.dila.solonmgpp.core.dto;

import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.MgppPieceJointeDTO;
import fr.dila.st.core.client.AbstractMapDTO;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class EvenementDTOImpl extends AbstractMapDTO implements EvenementDTO, Serializable {
    private static final long serialVersionUID = 1156726888970180429L;

    public static final String PREFIX = "evt";

    public static final String NAME = "name";
    public static final String LABEL = "label";
    public static final String TYPE_EVENEMENT = "typeEvenement";
    public static final String ID_DOSSIER = "idDossier";
    public static final String ID_EVENEMENT = "idEvenement";
    public static final String VERSION_COURANTE = "versionCourante";
    public static final String MINEUR = "mineur";
    public static final String MAJEUR = "majeur";
    public static final String VERSION_DISPONIBLE = "versionDisponible";
    public static final String NOR = "nor";
    public static final String DESTINATAIRE = "destinataire";
    public static final String COPIE = "copie";
    public static final String EMETTEUR = "emetteur";
    public static final String ACTION_SUIVANTE = "actionSuivante";
    public static final String ID_EVENEMENT_PRECEDENT = "idEvenementPrecedent";
    public static final String OBJET = "objet";
    public static final String MODE_CREATION = "modeCreation";
    public static final String AUTEUR = "auteur";
    public static final String COMMENTAIRE = "commentaire";
    public static final String CO_AUTEUR = "coAuteur";
    public static final String INTITULE = "intitule";
    public static final String DEPOT = "depot";
    public static final String DEPOT_RAPPORT = "depotRapport";
    public static final String DEPOT_TEXTE = "depotTexte";
    public static final String DATE = "date";
    public static final String NUMERO = "numero";
    public static final String METADONNEE_MODIFIEE = "metadonneeModifiee";
    public static final String EVENEMENT_SUIVANT = "evenementSuivant";
    public static final String NATURE = "nature";
    public static final String ETAT = "etat";
    public static final String COMMISSION = "commission";
    public static final String COMMISSION_SAISIE = "commissionSaisie";

    public static final String NIVEAU_LECTURE = "niveauLecture";
    public static final String NIVEAU_LECTURE_CODE = "code";
    public static final String NIVEAU_LECTURE_NIVEAU = "niveau";

    /**
     * champ du niveauLecture
     */
    public static final String CODE = "code";
    public static final String NIVEAU = "niveau";

    /**
     * Champ OEP pour la FEV 394
     */
    public static final String OEP = "OEP";

    // correspondance de "copie" des webServices et "destinataireCopie" des metadonnées
    public static final String DESTINATAIRE_COPIE = "destinataireCopie";
    // correspondance de "idDossier" des webServices et "dossier" des metadonnées
    public static final String DOSSIER = "dossier";
    // correspondance de "coauteur" des webServices et "coAuteur" des metadonnées
    public static final String COAUTEUR = "coauteur";

    public static final String DATE_AR = "dateAr";

    @Override
    public String getType() {
        return EvenementDTO.class.getSimpleName();
    }

    @Override
    public String getDocIdForSelection() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getTypeEvenementName() {
        if (get(TYPE_EVENEMENT) != null) {
            return (String) ((Map<String, Serializable>) get(TYPE_EVENEMENT)).get(NAME);
        } else {
            return "TYPE_VIDE";
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getTypeEvenementLabel() {
        if (get(TYPE_EVENEMENT) != null) {
            return (String) ((Map<String, Serializable>) get(TYPE_EVENEMENT)).get(LABEL);
        } else {
            return "Type de communication inconnue";
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<MgppPieceJointeDTO> getListPieceJointe(String typePieceJointe) {
        if ("AUTRE".equals(typePieceJointe)) {
            typePieceJointe = "AUTRES";
        }
        return (List<MgppPieceJointeDTO>) get(typePieceJointe);
    }

    @Override
    public String getIdDossier() {
        return getString(ID_DOSSIER);
    }

    @Override
    public String getIntitule() {
        return getString(INTITULE);
    }

    @Override
    public String getIdEvenement() {
        return getString(ID_EVENEMENT);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Integer getVersionCouranteMajeur() {
        return (Integer) ((Map<String, Serializable>) get(VERSION_COURANTE)).get(MAJEUR);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Integer getVersionCouranteMineur() {
        return (Integer) ((Map<String, Serializable>) get(VERSION_COURANTE)).get(MINEUR);
    }

    @Override
    public String getVersionCourante() {
        return String.valueOf(getVersionCouranteMajeur()) + '.' + getVersionCouranteMineur();
    }

    @Override
    public void setIdDossier(String idDossier) {
        put(ID_DOSSIER, idDossier);
    }

    @Override
    public String getNor() {
        return getString(NOR);
    }

    @Override
    public void setNor(String nor) {
        put(NOR, nor);
    }

    @Override
    public String getEmetteur() {
        return getString(EMETTEUR);
    }

    @Override
    public String getDestinataire() {
        return getString(DESTINATAIRE);
    }

    @Override
    public void setDestinataire(String destinataire) {
        put(DESTINATAIRE, destinataire);
    }

    @Override
    public void setCopie(List<String> copie) {
        putListString(COPIE, copie);
    }

    @Override
    public List<String> getCopie() {
        return getListString(COPIE);
    }

    @Override
    public List<String> getActionSuivante() {
        return getListString(ACTION_SUIVANTE);
    }

    @Override
    public String getIdEvenementPrecedent() {
        return getString(ID_EVENEMENT_PRECEDENT);
    }

    @Override
    public String getObjet() {
        return getString(OBJET);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getVersionCouranteModeCreation() {
        return (String) ((Map<String, Serializable>) get(VERSION_COURANTE)).get(MODE_CREATION);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getVersionCouranteEtat() {
        return (String) ((Map<String, Serializable>) get(VERSION_COURANTE)).get(ETAT);
    }

    @Override
    public String getAuteur() {
        return getString(AUTEUR);
    }

    @Override
    public String getCommentaire() {
        return getString(COMMENTAIRE);
    }

    @Override
    public List<String> getMetasModifiees() {
        return getListString(METADONNEE_MODIFIEE);
    }

    @Override
    public List<String> getEvenementsSuivants() {
        return getListString(EVENEMENT_SUIVANT);
    }

    @Override
    public List<String> getNature() {
        return getListString(NATURE);
    }

    @Override
    public String getOEP() {
        return getString(OEP);
    }

    @Override
    public void setOEP(String newOEP) {
        put(OEP, newOEP);
    }

    @Override
    public String getEtat() {
        return getString(ETAT);
    }

    @Override
    public boolean hasData() {
        return this.entrySet()
            .stream()
            .anyMatch(
                object ->
                    StringUtils.isNotEmpty(Objects.toString(object, "")) &&
                    !(object instanceof Collection && CollectionUtils.isEmpty((Collection<?>) object))
            );
    }
}
