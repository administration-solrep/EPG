package fr.dila.solonepg.api.logging.enumerationCodes;

import fr.dila.st.api.logging.enumerationCodes.STCodes;

/**
 * Énumération de l'objet des actions <br />
 * Décompte sur 3 chiffres, le premier (2) indique qu'il s'agit d'un objet de l'EPG <br />
 * <br />
 * 201 : Notification <br />
 * 202 : Mesure applicative <br />
 * 203 : SolonEpgAlert <br />
 * 204 : Ampliation <br />
 * 205 : Fond de dossier <br />
 * 206 : Indexation <br />
 * 207 : ListeTraitementPapier <br />
 * 208 : Parapheur <br />
 * 209 : Tableau dynamique <br />
 * 210 : Modèle de fond de dossier <br />
 * 211 : Modèle de parapheur <br />
 * 212 : Statistiques Activité Normative <br />
 * 213 : BirtRefreshFichier <br />
 * 214 : Notification WsSpe <br />
 * 215 : ExportPANStat <br />
 * 216 : Batch de suppression des export Statsiques PAN <br />
 * 217 : SUIVI <br />
 * 218 : Tableau de programmation <br />
 * 219 : Statut dossier <br />
 * 220 : Nor <br />
 * 221 : Mode de parution <br />
 * 222 : Section CE <br />
 * 223 : Ws Attribuer Nor <br />
 * 224 : Ws Modifier Dossier <br />
 * 225 : vecteur de publication <br />
 * 226 : Bulletin officiel <br />
 * 227 : Creer Dossier <br />
 * 228 : Chercher Dossier <br />
 * 229 : Donner Avis CE <br />
 * 230 : Modifier Dossier CE <br />
 * 231 : Chercher modification dossier <br />
 * 232 : maj ministerielle <br />
 * 233 : archivage définitif (ADAMANT) <br />
 * 234 : WS EPP <br />
 * 
 * 281 : Batch d'export des dossiers <br />
 * 282 : Batch de suppression des dossiers au flag deleted <br />
 * 283 : Batch de mise à jour du plan de classement <br />
 * 284 : Batch de mise à jour de la table MGPP_INFO_CORBEILLE <br />
 * 285 : Batch de suppression des rapports birt rafraichit <br />
 * 286 : Batch de suppression des textes signalés <br />
 * 287 : Batch de suppression des notifications reçues pour la mise à jour de corbeille <br />
 * 288 : Batch de génération des statistiques <br />
 * 289 : Batch d'envoi notification (demande publication) aux WS <br />
 * 290 : Batch d'élimination <br />
 * 291 : Batch de candidature d'abandon des dossiers <br />
 * 292 : Batch d'abandon des dossiers <br />
 * 293 : Batch de détection de nor incohérent <br />
 * 294 : Batch de correction de nor après injection <br />
 * 295 : Batch d'envoi des mails de confirmation des alertes <br />
 * 296 : Batch de clôture des textes non publiés <br />
 * 297 : Batch de suppression des dossiers link au flag deleted <br />
 * 298 : Batch des dossiers candidats à l'archivage intermédiaire <br />
 * 299 : Batch des dossiers candidats à l'archivage définitif <br />
 * 300 : Batch d'injection des textes-maitres au BDJ <br />
 */
public enum EpgObjetsEnum implements STCodes {
	/**
	 * 201 : Notification
	 */
	NOTIF(201, "Notification"),
	/**
	 * 202 : Mesure applicative
	 */
	MESURE(202, "Mesure applicative"),
	/**
	 * 203 : Alerte SolonEpg
	 */
	ALERTE_EPG(203, "Alerte Solon Epg"),
	/**
	 * 204 : Ampliation
	 */
	AMPLIATION(204, "Ampliation"),
	/**
	 * 205 : Répertoire de fond de dossier
	 */
	REP_FDD(205, "Répertoire de fond de dossier"),
	/**
	 * 206 : Indexation
	 */
	INDEXATION(206, "Indexation"),
	/**
	 * 207 : Liste traitement papier
	 */
	LST_TRAITEMENT_PAPIER(207, "Liste traitement papier"),
	/**
	 * 208 : Parapheur
	 */
	REP_PARAPHEUR(208, "Répertoire Parapheur"),
	/**
	 * 209 : Tableau dynamique
	 */
	TAB_DYNAMIQUE(209, "Tableau dynamique"),
	/**
	 * 210 : Modèle FDD
	 */
	MOD_FDD(210, "Modèle de fond de dossier"),
	/**
	 * 211 : Modèle Parapheur
	 */
	MOD_PARAPHEUR(211, "Modèle de parapheur"),
	/**
	 * 212 : Statistiques Activité Normative
	 */
	STATS_AN(212, "Statistiques activités normative"),
	/**
	 * 213 : BirtRefreshFichier
	 */
	BIRT_REFRESH_FICHIER(213, "BirtRefreshFichier"),
	/**
	 * 214 : Notification WsSpe
	 */
	WS_SPE(214, "Notification Ws Spe"),
	/**
	 * 215 : ExportPANStat
	 */
	EXPORT_PAN_STATS(215, "ExportPANStat"),
	/**
	 * 216 : Batch de suppression des export Statsiques PAN
	 */
	BATCH_DEL_EXPORT_PAN_STATS(216, "Batch de suppression des exports statsiques PAN"),
	/**
	 * 217 : SUIVI
	 */
	SUIVI(217, "Suivi"),
	/**
	 * 218 : Tableau de programmation
	 */
	TABLEAU_PROGRAMMATION(218, "Tableau de programmation"),
	/**
	 * 219 : Statut epg dossier
	 */
	STATUT_DOSSIER(219, "Statut dossier"),
	/**
	 * 220 : Nor
	 */
	NOR(220, "Nor"),
	/**
	 * 221 : Mode de parution
	 */
	MODE_PARUTION(221, "Mode de parution"),
	/**
	 * 222 : Section CE
	 */
	SECTION_CE(222, "Section CE"),
	/**
	 * 223 : WS Attribuer Nor
	 */
	WS_ATTRIBUER_NOR(223, "Attribuer Nor"),
	/**
	 * 224 : Modifier Dossier
	 */
	WS_MODIFIER_DOSSIER(224, "Modifier Dossier"),
	/**
	 * 225 : Vecteur de publication
	 */
	VECTEUR_PUB(225, "Vecteur de publication"),
	/**
	 * 226 : Bulletin officiel
	 */
	BULLETIN_OFFICIEL(226, "Bulletin officiel"),
	/**
	 * 227 : Créer Dossier
	 */
	WS_CREER_DOSSIER(227, "Créer Dossier"),
	/**
	 * 228 : Chercher Dossier
	 */
	WS_CHERCHER_DOSSIER(228, "Chercher Dossier"),
	/**
	 * 229 : Donner Avis CE
	 */
	WS_DONNER_AVIS_CE(229, "Donner Avis CE"),
	/**
	 * 230 : Modifier Dossier CE
	 */
	WS_MODIFIER_DOSSIER_CE(230, "Modifier Dossier CE"),
	/**
	 * 231 : Chercher modification dossier
	 */
	WS_CHERCHER_MODIFICATION_DOSSIER(231, "Chercher modification dossier"),
	/**
	 * 232 : maj ministerielle
	 */
	MAJ_MIN(232, "Mise à jour ministérielle"),
	/**
	 * 233 : archivage définitif (ADAMANT)
	 */
	ARCHIVAGE_DEFINITIF(233, "Archivage définitif"),
	/**
	 * 234 : WS EPP
	 */
	WS_EPP(234, "WS EPP"),

	// ******************************BATCHS
	
	/* **** Rajouter vos batchs ici, en ordre décroissant. **** */
	/**
	 * 280 : Batch d'extraction Adamant
	 */
	BATCH_EXTRACTION_ADAMANT(280, "Batch d'extraction Adamant"),
	/**
	 * 281 : Batch d'export des dossiers
	 */
	BATCH_EXPORT_DOSSIERS(281, "Batch d'export des dossiers"),

	/**
	 * 282 : Batch de suppression des dossiers au flag deleted
	 */
	BATCH_DEL_DOS(282, "Batch de suppression des dossiers au flag deleted"),
	/**
	 * 283 : Batch de mise à jour du plan de classement
	 */
	BATCH_INIT_CASE_ROOT(283, "Batch de mise à jour du plan de classement"),
	/**
	 * 284 : Batch de mise à jour de la table MGPP_INFO_CORBEILLE
	 */
	BATCH_UPDATE_MGPP_INFO_CORBEILLE(284, "Batch de mise à jour de la table MGPP_INFO_CORBEILLE"),
	/**
	 * 285 : Batch de suppression des rapports birt rafraichit
	 */
	BATCH_DEL_BIRT_REFRESH(285, "Batch de suppression des rapports birt rafraichit"),
	/**
	 * 286 : Batch de suppression des textes signalés
	 */
	BATCH_DEL_TXT_SIGNALES(286, "Batch de suppression des textes signalés"),
	/**
	 * 287 : Batch de suppression des notifications reçues pour la mise à jour de corbeille
	 */
	BATCH_DEL_NOTIF(287, "Batch de suppression des notifications"),
	/**
	 * 288 : Batch de génération des rapports des statistiques
	 */
	BATCH_GENERATE_STATS(288, "Batch de génération des statistiques"),
	/**
	 * 289 : Batch d'envoi notification (demande de publication) aux WS
	 */
	BATCH_NOTIFICATION(289, "Batch d'envoi notifications (demande de publication) aux WS"),
	/**
	 * 290 : Batch d'élimination des dossiers
	 */
	BATCH_ELIMINATION(290, "Batch d'élimination des dossiers"),
	/**
	 * 291 : Batch de candidature d'abandon des dossiers
	 */
	BATCH_CANDIDAT_ABANDON(291, "Batch de candidature d'abandon des dossiers"),
	/**
	 * 292 : Batch d'abandon des dossiers
	 */
	BATCH_ABANDON(292, "Batch d'abandon des dossiers"),
	/**
	 * 293 : Batch de détection de nor incohérent
	 */
	BATCH_NOR_INCONSISTENT(293, "Batch de détection de nor incohérent"),
	/**
	 * 294 : Batch de correction de nor après injection
	 */
	BATCH_CORRECT_NOR(294, "Batch de correction de nor après injection"),
	/**
	 * 295 : Batch d'envoi des mails de confirmation des alertes
	 */
	BATCH_CONFIRM_ALERT(295, "Batch d'envoi des mails de confirmations des alertes"),
	/**
	 * 296 : Batch de clôture des textes non publiés
	 */
	BATCH_CLOSE_TXT_NON_PUB(296, "Batch de cloture des dossiers Textes non publiés au JO"),
	/**
	 * 297 : Batch de suppression des dossiers link au flag deleted
	 */
	BATCH_DEL_DL(297, "Batch de suppression des dossiers link au flag deleted"),
	/**
	 * 298 : Batch des dossiers candidats à l'archivage intermédiaire
	 */
	BATCH_ARCHIV_INTER(298, "Batch des dossiers candidats à l'archivage intermédiaire"),
	/**
	 * 299 : Batch des dossiers candidats à l'archivage définitif
	 */
	BATCH_ARCHIV_DEF(299, "Batch des dossiers candidats à l'archivage définitif"),
	/**
	 * 300 : Batch d'injection des textes maitres au BDJ
	 */
	BATCH_INJECTION_TM_BDJ(300, "Batch d'injection des textes-maitres au BDJ");
	/* **** Ne pas dépasser 299, si ajout d'un nouveau batch, aller en ordre décroissant depuis le premier batch. **** */

	/* **** (Ne pas oublier de tenir à jour la documentation en lien avec le code) **** */

	private int		codeNumber;
	private String	codeText;

	EpgObjetsEnum(int codeNumber, String codeText) {
		this.codeNumber = codeNumber;
		this.codeText = codeText;
	}

	@Override
	public int getCodeNumber() {
		return this.codeNumber;
	}

	@Override
	public String getCodeText() {
		return this.codeText;
	}

	@Override
	public String getCodeNumberStr() {
		return String.valueOf(this.codeNumber);
	}

}

