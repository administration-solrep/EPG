package fr.dila.solonepg.core.query;

import java.util.HashMap;
import java.util.Map;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgIndextionConstants;
import fr.dila.solonepg.api.constant.SolonEpgParametrageAdamantConstants;
import fr.dila.solonepg.api.constant.SolonEpgParametrageApplicationConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.SolonEpgProfilUtilisateurConstants;
import fr.dila.solonepg.api.constant.SolonEpgRequeteDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.constant.SolonEpgTableReferenceConstants;
import fr.dila.solonepg.api.constant.TexteSignaleConstants;
import fr.dila.ss.api.constant.SSInfoUtilisateurConnectionConstants;
import fr.dila.st.api.constant.STAlertConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.core.query.FlexibleQueryMaker;

public class SolonEpgFNXQLQueryMaker extends FlexibleQueryMaker {

	protected static final Map<String, String>	mapTypeSchema	= new HashMap<String, String>();

	static {
		// Dossier
		mapTypeSchema.put(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, DossierSolonEpgConstants.DOSSIER_SCHEMA);
		// DossierLink
		mapTypeSchema.put(DossierSolonEpgConstants.DOSSIER_LINK_DOCUMENT_TYPE,
				DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA);
		// Delegation
		mapTypeSchema.put(STConstant.DELEGATION_DOCUMENT_TYPE, STSchemaConstant.DELEGATION_SCHEMA);
		// Fond de dossier
		mapTypeSchema.put(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_DOCUMENT_TYPE,
				SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_DOCUMENT_SCHEMA);
		// mapTypeSchema.put(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_DOCUMENT_TYPE,
		// SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_DOCUMENT_SCHEMA);
		mapTypeSchema.put(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_ROOT_DOCUMENT_TYPE,
				SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_MODEL_ROOT_DOCUMENT_SCHEMA);
		// mapTypeSchema.put(SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_TYPE,
		// SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_DOCUMENT_SCHEMA);
		// Parapheur
		mapTypeSchema.put(SolonEpgParapheurConstants.PARAPHEUR_MODEL_DOCUMENT_TYPE,
				SolonEpgParapheurConstants.PARAPHEUR_MODEL_DOCUMENT_SCHEMA);
		// mapTypeSchema.put(SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE,
		// SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_SCHEMA);
		// Profil utilisateur
		mapTypeSchema.put(SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_DOCUMENT_TYPE,
				SolonEpgProfilUtilisateurConstants.PROFIL_UTILISATEUR_SCHEMA);
		// Parametrage de l'application
		mapTypeSchema.put(SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_DOCUMENT_TYPE,
				SolonEpgParametrageApplicationConstants.PARAMETRAGE_APPLICATION_SCHEMA);
		// Parametrage Adamant
		mapTypeSchema.put(SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_TYPE,
				SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_SCHEMA);
		// Liste traitement papier (mce)
		// mapTypeSchema.put(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DOCUMENT_TYPE,
		// SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA);
		// Table reference (ase)
		mapTypeSchema.put(SolonEpgTableReferenceConstants.TABLE_REFERENCE_DOCUMENT_TYPE,
				SolonEpgTableReferenceConstants.TABLE_REFERENCE_SCHEMA);
		// Bulletin officiel
		// mapTypeSchema.put(SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_DOCUMENT_TYPE,
		// SolonEpgBulletinOfficielConstants.BULLETIN_OFFICIEL_SCHEMA);
		// Indexation
		mapTypeSchema.put(SolonEpgIndextionConstants.INDEXATION_RUBRIQUE_DOCUMENT_TYPE,
				SolonEpgIndextionConstants.INDEXATION_RUBRIQUE_SCHEMA);
		mapTypeSchema.put(SolonEpgIndextionConstants.INDEXATION_MOT_CLE_DOCUMENT_TYPE,
				SolonEpgIndextionConstants.INDEXATION_MOT_CLE_SCHEMA);
		// Espace recherche (jgz & ase )
		// ARN : pas d'optim pour le ResultatConsulte et FavorisConsultation car le schema n'existe pas forcement : (err
		// de type : No table for schema named [favoris_consultation])
		// mapTypeSchema.put(SolonEpgEspaceRechercheConstants.RESULTAT_CONSULTE_DOCUMENT_TYPE,
		// SolonEpgEspaceRechercheConstants.RESULTAT_CONSULTE_SCHEMA);
		// mapTypeSchema.put(SolonEpgEspaceRechercheConstants.FAVORIS_CONSULTATION_DOCUMENT_TYPE,
		// SolonEpgEspaceRechercheConstants.FAVORIS_CONSULTATION_SCHEMA);
		mapTypeSchema.put(SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_DOCUMENT_TYPE,
				SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_SCHEMA);
		// Favoris recherche
		// mapTypeSchema.put(SolonEpgConstant.FAVORIS_RECHERCHE_DOCUMENT_TYPE,
		// SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA);
		// Spe
		mapTypeSchema.put(SolonEpgSchemaConstant.SPE_TYPE, SolonEpgSchemaConstant.SPE_SCHEMA);
		// RequeteDossier
		mapTypeSchema.put(SolonEpgRequeteDossierConstants.REQUETE_DOSSIER_DOCUMENT_TYPE,
				SolonEpgRequeteDossierConstants.REQUETE_DOSSIER_SCHEMA);
		// Activite Normative
		// mapTypeSchema.put(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE,
		// ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA);
		// Activite Normative texte-maitre
		// mapTypeSchema.put(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE,
		// TexteMaitreConstants.TEXTE_MAITRE_SCHEMA);
		// Activite Normative programmation
		// mapTypeSchema.put(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE,
		// ActiviteNormativeProgrammationConstants.ACTIVITE_NORMATIVE_PROGRAMMATION_SCHEMA);
		// InfoUtilisateurConnection
		mapTypeSchema.put(SSInfoUtilisateurConnectionConstants.INFO_UTILISATEUR_CONNECTION_DOCUMENT_TYPE,
				SSInfoUtilisateurConnectionConstants.INFO_UTILISATEUR_CONNECTION_SCHEMA);
		// TODO voir si bijection effective sur fdr et solonegmailbox
		// Feuille route
		mapTypeSchema.put(STConstant.FEUILLE_ROUTE_DOCUMENT_TYPE, STSchemaConstant.FEUILLE_ROUTE_SCHEMA);
		// schema solonepgmailbox
		mapTypeSchema.put(SolonEpgConstant.SOLON_EPG_MAILBOX_TYPE, SolonEpgConstant.SOLON_EPG_MAILBOX_SCHEMA);
		// schema mailbox
		mapTypeSchema.put(SolonEpgConstant.SOLON_EPG_MAILBOX_TYPE, STSchemaConstant.MAILBOX_SCHEMA);
		// type alerte (juan)
		mapTypeSchema.put(STAlertConstant.ALERT_DOCUMENT_TYPE, STAlertConstant.ALERT_SCHEMA);
		// Texte signale
		mapTypeSchema
				.put(TexteSignaleConstants.TEXTE_SIGNALE_DOCUMENT_TYPE, TexteSignaleConstants.TEXTE_SIGNALE_SCHEMA);
		// Commentaire d'étape
		mapTypeSchema.put(STConstant.COMMENT_DOCUMENT_TYPE, STConstant.COMMENT_SCHEMA);
		// Date effet
		mapTypeSchema.put(DossierSolonEpgConstants.DOSSIER_DATE_EFFET_DOCUMENT_TYPE,
				DossierSolonEpgConstants.DOSSIER_DATE_EFFET_SCHEMA);
	};

	public SolonEpgFNXQLQueryMaker() {
		super(mapTypeSchema);
	}
}
