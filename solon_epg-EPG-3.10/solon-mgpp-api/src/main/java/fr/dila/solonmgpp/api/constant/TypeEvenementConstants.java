package fr.dila.solonmgpp.api.constant;

import fr.sword.xsd.solon.epp.EvenementType;

/**
 * Constantes des type d'evenements
 * 
 * @see EvenementType
 * @author asatre
 * 
 */
public final class TypeEvenementConstants {

	public static final String	TYPE_EVENEMENT_EVT01	= "EVT01";
	public static final String	TYPE_EVENEMENT_EVT02	= "EVT02";
	public static final String	TYPE_EVENEMENT_EVT03	= "EVT03";
	public static final String	TYPE_EVENEMENT_EVT04BIS	= "EVT04BIS";
	public static final String	TYPE_EVENEMENT_EVT10	= "EVT10";
	public static final String	TYPE_EVENEMENT_EVT19	= "EVT19";
	public static final String	TYPE_EVENEMENT_EVT24	= "EVT24";
	public static final String	TYPE_EVENEMENT_EVT28	= "EVT28";
	public static final String	TYPE_EVENEMENT_EVT32	= "EVT32";
	public static final String	TYPE_EVENEMENT_EVT34	= "EVT34";
	public static final String	TYPE_EVENEMENT_EVT39	= "EVT39";
	public static final String	TYPE_EVENEMENT_EVT40	= "EVT40";
	public static final String	TYPE_EVENEMENT_EVT43	= "EVT43";
	public static final String	TYPE_EVENEMENT_EVT43BIS	= "EVT43BIS";
	public static final String	TYPE_EVENEMENT_EVT44	= "EVT44";
	public static final String	TYPE_EVENEMENT_EVT44TER	= "EVT44TER";
	public static final String	TYPE_EVENEMENT_EVT49_0	= "EVT49-0";
	public static final String	TYPE_EVENEMENT_EVT49	= "EVT49";
	public static final String	TYPE_EVENEMENT_EVT51	= "EVT51";
	public static final String	ALERTE01				= "ALERTE01";
	public static final String	ALERTE02				= "ALERTE02";
	public static final String	ALERTE03				= "ALERTE03";
	public static final String	ALERTE04				= "ALERTE04";
	public static final String	ALERTE05				= "ALERTE05";
	public static final String	ALERTE06				= "ALERTE06";
	public static final String	ALERTE07				= "ALERTE07";
	public static final String	ALERTE08				= "ALERTE08";
	public static final String	ALERTE09				= "ALERTE09";
	public static final String	ALERTE10				= "ALERTE10";

	// Evenements: Déclaration de politique générale
	public static final String	FUSION11				= "FUSION11";
	public static final String	GENERIQUE11				= "GENERIQUE11";
	public static final String	ALERTE11				= "ALERTE11";
	public static final String	PG01					= "PG01";
	public static final String	PG02					= "PG02";
	public static final String	PG03					= "PG03";
	public static final String	PG04					= "PG04";

	// Evenements: Déclaration sur un sujet determine
	public static final String	FUSION12				= "FUSION12";
	public static final String	GENERIQUE12				= "GENERIQUE12";
	public static final String	ALERTE12				= "ALERTE12";
	public static final String	SD01					= "SD01";
	public static final String	SD02					= "SD02";
	public static final String	SD03					= "SD03";

	// Evenements: Article 28-3C
	public static final String	FUSION13				= "FUSION13";
	public static final String	GENERIQUE13				= "GENERIQUE13";
	public static final String	ALERTE13				= "ALERTE13";
	public static final String	JSS01					= "JSS01";
	public static final String	JSS02					= "JSS02";

	// Evenements: documents
	public static final String	DOC01					= "DOC01";
	public static final String	FUSION15				= "FUSION15";
	public static final String	GENERIQUE15				= "GENERIQUE15";
	public static final String	ALERTE15				= "ALERTE15";

	// Evenements: documents
	public static final String	AUD01					= "AUD01";
	public static final String	AUD02					= "AUD01";
	public static final String	FUSION14				= "FUSION14";
	public static final String	GENERIQUE14				= "GENERIQUE14";
	public static final String	ALERTE14				= "ALERTE14";

	public static boolean isEvenementPG(String typeEvenement) {
		return typeEvenement != null && (typeEvenement.equals(PG01) || typeEvenement.equals(PG02)
				|| typeEvenement.equals(PG03) || typeEvenement.equals(PG04) || typeEvenement.equals(GENERIQUE11)
				|| typeEvenement.equals(FUSION11) || typeEvenement.equals(ALERTE11));
	}

	public static boolean isEvenementSD(String typeEvenement) {
		return typeEvenement != null && (typeEvenement.equals(SD01) || typeEvenement.equals(SD02)
				|| typeEvenement.equals(SD03) || typeEvenement.equals(GENERIQUE11) || typeEvenement.equals(FUSION12)
				|| typeEvenement.equals(GENERIQUE12) || typeEvenement.equals(ALERTE12));
	}

	public static boolean isEvenementJSS(String typeEvenement) {
		return typeEvenement != null
				&& (typeEvenement.equals(JSS01) || typeEvenement.equals(JSS02) || typeEvenement.equals(FUSION13)
						|| typeEvenement.equals(GENERIQUE13) || typeEvenement.equals(ALERTE13));
	}

	public static boolean isEvenementDOC(String typeEvenement) {
		return typeEvenement != null && (typeEvenement.equals(DOC01) || typeEvenement.equals(GENERIQUE15)
				|| typeEvenement.equals(FUSION15) || typeEvenement.equals(ALERTE15));
	}

	public static boolean isEvenementAUD(String typeEvenement) {
		return typeEvenement != null && (typeEvenement.equals(AUD01) || typeEvenement.equals(AUD02)
				|| typeEvenement.equals(GENERIQUE14) || typeEvenement.equals(FUSION14)
				|| typeEvenement.equals(ALERTE14));
	}

	public static boolean isEvenementOEP(String typeEvenement) {
		return typeEvenement != null && (typeEvenement.equals(TYPE_EVENEMENT_EVT49_0)
				|| typeEvenement.equals(TYPE_EVENEMENT_EVT49) || typeEvenement.equals(TYPE_EVENEMENT_EVT51));
	}

	private TypeEvenementConstants() {
		// private default constructor
	}
}
