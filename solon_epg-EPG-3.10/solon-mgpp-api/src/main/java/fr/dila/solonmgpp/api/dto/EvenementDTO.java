package fr.dila.solonmgpp.api.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * DTO de representation d'un evenement EPP
 * 
 * @author asatre
 * 
 */
public interface EvenementDTO extends Map<String, Serializable> {

	public static final String	PREFIX					= "evt";

	public static final String	NAME					= "name";
	public static final String	LABEL					= "label";
	public static final String	TYPE_EVENEMENT			= "typeEvenement";
	public static final String	ID_DOSSIER				= "idDossier";
	public static final String	ID_EVENEMENT			= "idEvenement";
	public static final String	VERSION_COURANTE		= "versionCourante";
	public static final String	MINEUR					= "mineur";
	public static final String	MAJEUR					= "majeur";
	public static final String	VERSION_DISPONIBLE		= "versionDisponible";
	public static final String	NOR						= "nor";
	public static final String	DESTINATAIRE			= "destinataire";
	public static final String	COPIE					= "copie";
	public static final String	EMETTEUR				= "emetteur";
	public static final String	ACTION_SUIVANTE			= "actionSuivante";
	public static final String	ID_EVENEMENT_PRECEDENT	= "idEvenementPrecedent";
	public static final String	OBJET					= "objet";
	public static final String	MODE_CREATION			= "modeCreation";
	public static final String	AUTEUR					= "auteur";
	public static final String	COMMENTAIRE				= "commentaire";
	public static final String	CO_AUTEUR				= "coAuteur";
	public static final String	INTITULE				= "intitule";
	public static final String	DEPOT					= "depot";
	public static final String	DATE					= "date";
	public static final String	NUMERO					= "numero";
	public static final String	METADONNEE_MODIFIEE		= "metadonneeModifiee";
	public static final String	EVENEMENT_SUIVANT		= "evenementSuivant";
	public static final String	NATURE					= "nature";

	public static final String	NIVEAU_LECTURE			= "niveauLecture";
	public static final String	NIVEAU_LECTURE_CODE		= "code";
	public static final String	NIVEAU_LECTURE_NIVEAU	= "niveau";

	/**
	 * champ du niveauLecture
	 */
	public static final String	CODE					= "code";
	public static final String	NIVEAU					= "niveau";

	/**
	 * Champ OEP pour la FEV 394
	 */
	public static final String	OEP						= "OEP";

	// correspondance de "copie" des webServices et "destinataireCopie" des metadonnées
	public static final String	DESTINATAIRE_COPIE		= "destinataireCopie";
	// correspondance de "idDossier" des webServices et "dossier" des metadonnées
	public static final String	DOSSIER					= "dossier";

	String getTypeEvenementName();

	String getTypeEvenementLabel();

	List<PieceJointeDTO> getListPieceJointe(String typePieceJointe);

	String getIdDossier();

	void setIdDossier(String idDossier);

	String getIdEvenement();

	Integer getVersionCouranteMajeur();

	Integer getVersionCouranteMineur();

	String getNor();

	void setNor(String nor);

	String getEmetteur();

	String getDestinataire();

	void setDestinataire(String destinataire);

	void setCopie(List<String> copie);

	List<String> getCopie();

	List<String> getActionSuivante();

	String getIdEvenementPrecedent();

	String getObjet();

	String getVersionCouranteModeCreation();

	String getAuteur();

	String getCommentaire();

	List<String> getMetasModifiees();

	List<String> getEvenementsSuivants();

	List<String> getNature();

	String getOEP();

	void setOEP(String newOEP);

}
