package fr.dila.solonmgpp.api.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DTO de representation d'un message EPP
 * 
 * @author asatre
 * 
 */
public interface MessageDTO extends Map<String, Serializable> {

    public static final String DATE_DEPOT = "dateDepot";
    public static final String NUMERO_DEPOT = "numeroDepot";
    public static final String ETAT_DOSSIER = "etatDossier";
    public static final String ID_SENAT = "idSenat";
    public static final String ID_DOSSIER = "idDossier";
    public static final String CODE_LECTURE = "codeLecture";
    public static final String NIVEAU_LECTURE = "niveauLecture";
    public static final String OBJET = "objet";
    public static final String PRESENCE_PIECE_JOINTE = "presencePieceJointe";
    public static final String COPIE_EVENEMENT = "copieEvenement";
    public static final String DESTINATAIRE_EVENEMENT = "destinataireEvenement";
    public static final String EMETTEUR_EVENEMENT = "emetteurEvenement";
    public static final String DATE_EVENEMENT = "dateEvenement";
    public static final String TYPE_EVENEMENT = "typeEvenement";
    public static final String ETAT_EVENEMENT = "etatEvenement";
    public static final String ID_EVENEMENT = "idEvenement";
    public static final String ETAT_MESSAGE = "etatMessage";
    public static final String EN_ATTENTE = "en_attente";

    String getEtatMessage();

    void setEtatMessage(String etatMessage);

    String getIdEvenement();

    void setIdEvenement(String value);

    String getEtatEvenement();

    void setEtatEvenement(String etatEvenement);

    String getTypeEvenement();

    void setTypeEvenement(String typeEvenement);

    Date getDateEvenement();

    void setDateEvenement(Date dateEvenement);

    String getEmetteurEvenement();

    void setEmetteurEvenement(String emetteurEvenement);

    String getDestinataireEvenement();

    void setDestinataireEvenement(String destinataireEvenement);

    List<String> getCopieEvenement();

    void setCopieEvenement(List<String> copieEvenement);

    Boolean isPresencePieceJointe();

    void setPresencePieceJointe(Boolean presencePieceJointe);

    String getObjet();

    void setObjet(String objet);

    Integer getNiveauLecture();

    void setNiveauLecture(Integer niveauLecture);

    String getCodeLecture();

    void setCodeLecture(String codeLecture);

    String getIdDossier();

    void setIdDossier(String idDossier);

    String getIdSenat();

    void setIdSenat(String idSenat);

    String getEtatDossier();

    void setEtatDossier(String etatDossier);

    String getNumeroDepot();

    void setNumeroDepot(String numeroDepot);

    Date getDateDepot();

    void setDateDepot(Date dateDepot);

    String toComparableString();
    
    Boolean isEnAttente();

    void setEnAttente(Boolean enAttente);
}
