package fr.dila.solonepg.api.constant;

/**
 * Constantes liés a la table de reference et à ses éléments (schéma,type,métadonnées).
 * 
 * @author asatre
 * 
 */
public final class SolonEpgTableReferenceConstants {

	public static final String	TABLE_REFERENCE_SCHEMA							= "table_reference";
	public static final String	TABLE_REFERENCE_PREFIX							= "tbref";
	public static final String	TABLE_REFERENCE_ID								= "tablereference";
	public static final String	TABLE_REFERENCE_PATH							= "/case-management/table-reference-root";

	public static final String	TABLE_REFERENCE_DOCUMENT_TYPE					= "TableReference";

	public static final String	TABLE_REFERENCE_CABINET_PM						= "cabinetPM";

	public static final String	TABLE_REFERENCE_SIGNATAIRES						= "signataires";

	public static final String	TABLE_REFERENCE_CHARGES_MISSION					= "chargesMission";

	public static final String	TABLE_REFERENCE_MINISTERE_PM					= "ministerePm";

	public static final String	TABLE_REFERENCE_MINISTERE_CE					= "ministereCe";

	public static final String	TABLE_REFERENCE_PST_PUBLI_ID					= "postePublicationId";

	public static final String	TABLE_REFERENCE_PST_DAN_ID						= "posteDanId";
	
	public static final String	TABLE_REFERENCE_PST_DAN_ID_AVIS_RECTIFICATIF_CE	= "posteDanIdAvisRectificatifCE";

	// *************************************************************
	// Paramètrage vecteur de publication multiple property
	// *************************************************************

	public static final String	TABLE_REFERENCE_VECTEURS_PUBLICATIONS_PROPERTY	= "vecteursPublications";

	// *************************************************************
	// Paramètrage choix des directions du premier ministre dans la création du dossier property
	// *************************************************************

	public static final String	TABLE_REFERENCE_DIRECTION_PM_PROPERTY			= "directionPm";

	public static final String	TABLE_REFERENCE_SIGNATURE_CPM_PROPERTY			= "signatureCPM";

	public static final String	TABLE_REFERENCE_SIGNATURE_SGG_PROPERTY			= "signatureSGG";
	public static final String	TABLE_REFERENCE_TYPE_ACTE_PROPERTY				= "typeActe";
	
	public static final String	TABLE_REFERENCE_RETOUR_DAN_PROPERTY			= "retourDan";

	// *************************************************************
	// Properties mode de parution
	// *************************************************************
	public static final String	TABLE_REFERENCE_MODE_PARUTION_PROPERTY			= "mode";

	public static final String	TABLE_REFERENCE_DEBUT_MODE_PARUTION_PROPERTY	= "dateDebut";

	public static final String	TABLE_REFERENCE_FIN_MODE_PARUTION_PROPERTY		= "dateFin";

	public static final String	MODE_PARUTION_DOCUMENT_TYPE						= "ModeParution";

	// Constant utility class
	private SolonEpgTableReferenceConstants() {
	}
}
