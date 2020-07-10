package fr.dila.solonepg.api.constant;
/**
 * 
 *  // *************************************************************<br />
 *  // Événements de l'activité normative<br />
 *  // *************************************************************<br />
 *
 */
public class SolonEpgANEventConstants {
    /**
     * Evenement de rafraichissement des statistiques de l'AN
     */
    public static final String REFRESH_STATS_EVENT = "refreshStatsANEvent";
    /**
     * Property EventContext rafraichissement des statistiques de l'AN : currentReport
     */
    public static final String CURRENT_REPORT_PROPERTY = "currentReportProperty";
    /**
     * Property EventContext rafraichissement des statistiques de l'AN : inputValues
     */
    public static final String INPUT_VALUES_PROPERTY = "inputValuesProperty";
    /**
     * Property EventContext rafraichissement des statistiques de l'AN : userWorkspace
     */
    public static final String USER_WS_PATH_PROPERTY = "userWorkspacePathProperty";
    
    /**
     * Property EventContext , export reports parameters
     */
    public static final String AN_STATS_PARAMETERS_PROPERTY = "ActiviteNormativeStatsParameters" ;
    /**
     * Property EventContext rafraichissement des statistiques de l'AN : user
     */
    public static final String USER_PROPERTY = "userProperty";
    /**
     * Evenement de rattachement des elements du dossier pour l'activite normative
     */
    public static final String ACTIVITE_NORMATIVE_ATTACH_EVENT = "activiteNormativeAttachEvent";
    /**
     * Paramètre de l'événement "activiteNormativeAttachEvent" : dossierDocId
     */
    public static final String ACTIVITE_NORMATIVE_ATTACH_DOSSIER_DOCID_PARAM = "dossierDocId";
    
    /**
     * Evenement d'export des statistiques de l'AN
     */
    public static final String EXPORT_STATS_EVENT = "exportPANStatsEvent";
    
    public static final String EXPORT_PAN_CONFIG_PARAMS = "export-pan-config-params" ;
	public static final String EXPORT_PAN_CURLEGISLATURE = "export-curlegislature";
	public static final String PUBLISH_STATS_EVENT = "publishPANStatsEvent";
	
	public static final String EXPORTED_LEGIS = "exported-legis";

    private SolonEpgANEventConstants() {
     //Private default constructor
    }
}
