package fr.dila.solonmgpp.api.enumeration;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * Enumération des regroupements de corbeilles 
 * dans l'interface mgpp en lien avec l'id utilisé dans le solonmgpp-action-contrib
 *
 */
public enum MgppGroupementCorbeilleEnum {

	PROCEDURE_LEG("mgpp_procedureLegislative", "CORBEILLE_GOUVERNEMET_PROC_LEG_RECEPTION", 
			"CORBEILLE_GOUVERNEMET_PROC_LEG_EMIS", "CORBEILLE_GOUVERNEMENT_CENSURE"),
	RESOLUTIONS("mgpp_resolutionArticle341", "CORBEILLE_GOUVERNEMENT_RESOLUTION_34-1"),
	DECLARATION("mgpp_declaration_category_id", "CORBEILLE_GOUVERNEMENT_INTERVENTION_EXTERIEURE", 
			"CORBEILLE_GOUVERNEMENT_DECLARATION_DE_POLITIQUE_GENERALE",
			"CORBEILLE_GOUVERNEMENT_DECLARATION_SUR_UN_SUJET_DETERMINE_50_1C"),
	NOMINATION("mgpp_nomination_category_id", "CORBEILLE_GOUVERNEMENT_OEP",
			"CORBEILLE_GOUVERNEMENT_NOMINATION", "CORBEILLE_GOUVERNEMENT_DEMANDE_AUDITION"),
	ORGANISATION("mgpp_organisation_category_id", "CORBEILLE_GOUVERNEMENT_DECRET", 
			"CORBEILLE_GOUVERNEMENT_DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_28-3C"),
	RAPPORT("mgpp_raport_category_id", "CORBEILLE_GOUVERNEMENT_RAPPORT", 
			"CORBEILLE_GOUVERNEMENT_AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES"),
	DECLARATION_IE("mgpp_interventionExterieure", "CORBEILLE_GOUVERNEMENT_INTERVENTION_EXTERIEURE"),
	DECLARATION_POL_GEN("mgpp_declarationDePolitiqueGenerale", 
			"CORBEILLE_GOUVERNEMENT_DECLARATION_DE_POLITIQUE_GENERALE"),
	DECLARATION_SUJET50_1C("mgpp_declarationSurUnSujetDetermine", 
			"CORBEILLE_GOUVERNEMENT_DECLARATION_SUR_UN_SUJET_DETERMINE_50_1C"),
	NOMINATION_OEP("mgpp_designationOEP", "CORBEILLE_GOUVERNEMENT_OEP"),
	NOMINATION_NOMINATION("mgpp_avisNomination", "CORBEILLE_GOUVERNEMENT_NOMINATION"),
	NOMINATION_AUDITION("mgpp_demandeAudition", "CORBEILLE_GOUVERNEMENT_DEMANDE_AUDITION"),
	ORGANISATION_DECRET_PR("mgpp_decret", "CORBEILLE_GOUVERNEMENT_DECRET"),
	ORGANISATION_ARTICLE28_3C("mgpp_demandeMiseEnOeuvreArticle283C", 
			"CORBEILLE_GOUVERNEMENT_DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_28"),
	RAPPORT_DEPOT("mgpp_depotDeRapport", "CORBEILLE_GOUVERNEMENT_RAPPORT"),
	RAPPORT_AUTRES_DOCS("mgpp_autresDocumentsTransmisAuxAssemblees", 
			"CORBEILLE_GOUVERNEMENT_AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES");
	
	private String actionMgppId;
	private List<String> corbeillesIds;
	
	private MgppGroupementCorbeilleEnum(String actionMgppId, String...corbeillesIds) {
		this.actionMgppId = actionMgppId;
		this.corbeillesIds = Arrays.asList(corbeillesIds);
	}
	
	/**
	 * Récupère l'actionMgppId à partir d'un id corbeille en paramètre
	 * @param corbeilleId
	 * @return actionMgppId ou null si aucun ne correspond
	 */
	public static String getActionMgppIdFromCorbeilleId(String corbeilleId) {
		for (MgppGroupementCorbeilleEnum elem : MgppGroupementCorbeilleEnum.values()) {
			if (elem.isCorbeilleInGroupement(corbeilleId)) {
				return elem.getActionMgppId();
			}
		}
		return null;
	}
	
	/**
	 * Récupère les corbeilles liées à un actionMgppId
	 * @param actionMgppId
	 * @return la liste des corbeillesIds, ou une liste vide si l'actionMgppId n'existe pas
	 */
	public static List<String> getCorbeilleIdsFromActionMgppId(String actionMgppId) {
		for (MgppGroupementCorbeilleEnum elem : MgppGroupementCorbeilleEnum.values()) {
			if (elem.getActionMgppId().equals(actionMgppId)) {
				return elem.getCorbeillesIds();
			}
		}
		return new ArrayList<String>();
	}
	
	public String getActionMgppId() {
		return this.actionMgppId;
	}
	
	public List<String> getCorbeillesIds() {
		return this.corbeillesIds;
	}
	
	public boolean isCorbeilleInGroupement(String corbeilleId) {
		return this.corbeillesIds.contains(corbeilleId);
	}
}
