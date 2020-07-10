package fr.dila.solonepg.api.service;

import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.migration.MigrationDetailModel;
import fr.dila.solonepg.api.migration.MigrationLoggerModel;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;

/**
 * Interface du service de gestion du changement de gouvernement.
 * 
 * @author arolin
 */
public interface ChangementGouvernementService {

    /**
     * Migre les fils d'un noeud organigramme vers un autre noeud organigramme et migre le numéro NOR des directions dans le cas d'une migration de ministère.
     * 
     * @param session
     * @param oldNode
     * @param newNode
     * @param migrationLoggerModel
     * @throws ClientException
     */
    void migrerElementsFils(CoreSession session, OrganigrammeNode oldNode, OrganigrammeNode newNode, MigrationLoggerModel migrationLoggerModel) throws ClientException;

    /**
     * Migre les postes des étapes de feuille de route des instances et des modèles vers un autre poste.
     * 
     * @param session
     * @param oldNode
     * @param newNode
     * @throws ClientException
     */
    void migrerModeleStepFdr(CoreSession session, OrganigrammeNode oldNode, OrganigrammeNode newNode, MigrationLoggerModel migrationLoggerModel) throws ClientException;

    /**
     * Met à jour le champ technique posteCreator utilisé pour la visibilité du dossier dans l'infoCentre de l'espace de suivi.
     * 
     * @param session
     * @param oldNode
     * @param newNode
     * @throws ClientException
     */
    void updateDossierCreatorPoste(CoreSession session, OrganigrammeNode oldNode, OrganigrammeNode newNode, MigrationLoggerModel migrationLoggerModel) throws ClientException;

    /**
     * Met à jour les corbeilles du poste : change les droits et le nom. (on déplace les dossiers links de l'ancienne mailbox dans la nouvelle)
     * 
     * @param session
     * @param oldNode
     * @param newNode
     * @throws ClientException
     */
    void updateMailBox(CoreSession session, OrganigrammeNode oldNode, OrganigrammeNode newNode, MigrationLoggerModel migrationLoggerModel) throws ClientException;

    /**
     * Migre les modèles de feuille de route d'un ministère ou une direction vers un autre ministère / direction.
     * 
     * @param session
     * @param oldNode
     * @param newNode
     * @param migrationLoggerModel
     * @throws ClientException
     */
    void migrerModeleFdrMinistere(CoreSession session, EntiteNode oldNode, EntiteNode newNode, MigrationLoggerModel migrationLoggerModel, Boolean desactivateModelFdr) throws ClientException;

    /**
     * Migre les modèles de feuille de route d'une direction vers une autre direction.
     * 
     * @param session
     * @param oldMinistereNode
     * @param oldDirectionNode
     * @param newMinistereNode
     * @param newDirectionNode
     * @throws ClientException
     */
    void migrerModeleFdrDirection(CoreSession session, EntiteNode oldMinistereNode, UniteStructurelleNode oldDirectionNode, EntiteNode newMinistereNode, UniteStructurelleNode newDirectionNode, MigrationLoggerModel migrationLoggerModel, Boolean desactivateModelFdr) throws ClientException;

    /**
	 * Réattribue les NORs des Dossiers d'un nouveau ministère.
	 * 
	 * @param session
	 * @param oldNode
	 * @param newNode
	 * @param norReattributionsPubConj
	 *            la liste des réattributions de nor sur les dossiers à
	 *            publications conjointes
	 * @throws ClientException
	 */
	void reattribuerNorDossierMinistere(CoreSession session, OrganigrammeNode oldNode, OrganigrammeNode newNode,
			MigrationLoggerModel migrationLoggerModel, Map<String, String> norReattributionsPubConj)
			throws ClientException;

    /**
     * Réattribue les NORs des Dossiers d'une nouvelle direction.
     * 
     * @param session
     * @param oldMinistereNode
     * @param oldDirectionNode
     * @param newMinistereNode
     * @param newDirectionNode
     * @throws ClientException
     */
	void reattribuerNorDossierDirection(CoreSession session, OrganigrammeNode oldMinistereNode,
			OrganigrammeNode oldDirectionNode, OrganigrammeNode newMinistereNode, OrganigrammeNode newDirectionNode,
			MigrationLoggerModel migrationLoggerModel, Map<String, String> norReattributions) throws ClientException;

    /**
     * Modifie le ministère de rattachement pour les dossiers à l'état clos, NOR attribué ou abandonné.
     * 
     * @param session
     * @param oldNode
     * @param newNode
     * @throws ClientException
     */
    void updateDossierMinistereRattachement(CoreSession session, OrganigrammeNode oldNode, OrganigrammeNode newNode, MigrationLoggerModel migrationLoggerModel) throws ClientException;

    /**
     * Modifie la direction de rattachement pour les dossiers à l'état clos, NOR attribué ou abandonné.
     * 
     * @param session
     * @param oldMinistereNode
     * @param oldDirectionNode
     * @param newMinistereNode
     * @param newDirectionNode
     * @throws ClientException
     */
    void updateDossierDirectionRattachement(CoreSession session, OrganigrammeNode oldMinistereNode, OrganigrammeNode oldDirectionNode, OrganigrammeNode newMinistereNode, OrganigrammeNode newDirectionNode, MigrationLoggerModel migrationLoggerModel) throws ClientException;

    /**
     * Migre les bulletins officiels de l'écran "Liste des bulletins officiels".
     * 
     * @param session
     * @param oldNode
     * @param newNode
     * @throws ClientException
     */
    void migrerBulletinOfficiel(CoreSession session, OrganigrammeNode oldNode, OrganigrammeNode newNode, MigrationLoggerModel migrationLoggerModel) throws ClientException;

    /**
     * Migre les mots clés de l'écran "Gestion de l'indexation".
     * 
     * @param session
     * @param oldNode
     * @param newNode
     */
    void migrerGestionIndexation(CoreSession session, OrganigrammeNode oldNode, OrganigrammeNode newNode, MigrationLoggerModel migrationLoggerModel) throws ClientException;

    /**
     * Migre les ministères de la table de référence.
     * 
     * @param session
     * @param oldNode
     * @param newNode
     * @throws ClientException
     */
    void migrerTableReferenceMinistere(CoreSession session, OrganigrammeNode oldNode, OrganigrammeNode newNode, MigrationLoggerModel migrationLoggerModel) throws ClientException;

    /**
     * Migre les directions de la table de référence ( directions PRM ).
     * 
     * @param session
     * @param oldNode
     * @param newNode
     * @throws ClientException
     */
    void migrerTableReferenceDirection(CoreSession session, OrganigrammeNode oldMinistereNode, OrganigrammeNode oldDirectionNode, OrganigrammeNode newMinistereNode, OrganigrammeNode newDirectionNode, MigrationLoggerModel migrationLoggerModel) throws ClientException;

	/**
	 * Migre les postes de la table de référence ( corbeilles de retour au DAN ).
	 * 
	 * @param session
	 * @param oldPosteNode
	 * @param newPosteNode
	 * @param migrationLoggerModel
	 * @throws ClientException
	 */
	void migrerTableReferencePoste(CoreSession session, OrganigrammeNode oldPosteNode, OrganigrammeNode newPosteNode, MigrationLoggerModel migrationLoggerModel) throws ClientException;

    /**
     * Creation d'un logger pour une migration
     * 
     * @param ssPrincipal
     * 
     * @return
     * @throws ClientException
     */
    MigrationLoggerModel createMigrationLogger(final String ssPrincipal) throws ClientException;

    /**
     * Rechercche de logger de migration par id
     * 
     * @param idLogger
     * @return
     * @throws ClientException
     */
    MigrationLoggerModel findMigrationById(final Long idLogger) throws ClientException;

    /**
     * Sauvegarde du {@link MigrationLoggerModel}
     * 
     * @param migrationLoggerModel
     * @throws ClientException
     */
    void flushMigrationLogger(final MigrationLoggerModel migrationLoggerModel) throws ClientException;

    /**
     * Creation d'un detail de logger de migration au statut OK
     * 
     * @param migrationLoggerModel
     * @param type
     * @param detail
     * @throws ClientException
     */
    MigrationDetailModel createMigrationDetailFor(MigrationLoggerModel migrationLoggerModel, String type, String detail) throws ClientException;

    /**
     * Creation d'un detail de logger de migration
     * 
     * @param migrationLoggerModel
     * @param type
     * @param detail
     * @param statut
     * @throws ClientException
     */
    MigrationDetailModel createMigrationDetailFor(MigrationLoggerModel migrationLoggerModel, String type, String detail, String statut) throws ClientException;

    void reattribuerMinistereActiviteNormative(CoreSession session, OrganigrammeNode oldMinistereNode, OrganigrammeNode newMinistereNode) throws ClientException;

    /**
     * @return list of MigrationLoggerModel
     * @throws ClientException
     */
    List<MigrationLoggerModel> getMigrationWithoutEndDate() throws ClientException;

    /**
     * return list of MigrationLoggerMode
     * 
     * @return
     * @throws ClientException
     */
    List<MigrationLoggerModel> getMigrationLoggerModelList() throws ClientException;

    /**
     * @param loggerId
     * @return
     * @throws ClientException
     */
    List<MigrationDetailModel> getMigrationDetailModelList(final Long loggerId) throws ClientException;

    /**
     * updateMigrationDetail
     * 
     * @param migrationDetailModel
     * @throws ClientException
     */
    void flushMigrationDetail(final MigrationDetailModel migrationDetailModel) throws ClientException;

	/**
	 * Retourne le nom de la migration
	 * 
	 * @param migrationLoggerModel
	 * @return
	 * @throws ClientException
	 */
	String getLogMessage(MigrationLoggerModel migrationLoggerModel) throws ClientException;

}
