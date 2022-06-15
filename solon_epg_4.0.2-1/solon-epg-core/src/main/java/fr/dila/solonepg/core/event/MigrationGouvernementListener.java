package fr.dila.solonepg.core.event;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.service.ChangementGouvernementService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.EpgDossierDistributionService;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.constant.SSEventConstant;
import fr.dila.ss.api.migration.MigrationLoggerModel;
import fr.dila.ss.core.event.SSAbstractMigrationGouvernementListener;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import java.util.HashMap;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;

/**
 * Gestion de la migration d'un gouvernement.
 *
 * @author asatre
 */
public class MigrationGouvernementListener extends SSAbstractMigrationGouvernementListener {

    @Override
    public void handleEvent(final EventBundle events) {
        if (events.containsEventName(SSEventConstant.MIGRATION_GVT_EVENT)) {
            for (final Event event : events) {
                handleEvent(event);
            }
        }
    }

    @Override
    protected void handleEvent(final Event event) {
        if (!event.getName().equals(SSEventConstant.MIGRATION_GVT_EVENT)) {
            return;
        }

        coreSession = event.getContext().getCoreSession();

        initServices();

        execMigrationOrganigramme(event, coreSession);
    }

    /**
     * Migration d'un ministère.
     *
     * @param migrationLoggerModel
     *
     * @param changementGouvernementService
     * @param epgOrganigrammeService
     * @param migrationWithDossierClos      Si vrai, on migre les dossiers clos
     */
    @Override
    protected void doMigrationMinistere(
        CoreSession session,
        MigrationLoggerModel migrationLoggerModel,
        EntiteNode oldMinistereNode,
        EntiteNode newMinistereNode
    ) {
        super.doMigrationMinistere(session, migrationLoggerModel, oldMinistereNode, newMinistereNode);

        final ChangementGouvernementService changementGouvernementService = SolonEpgServiceLocator.getChangementGouvernementService();

        // Réattribution du Nor.
        Map<String, String> norReattributionsPubConj = new HashMap<>();
        changementGouvernementService.reattribuerNorDossierMinistere(
            session,
            oldMinistereNode,
            newMinistereNode,
            migrationLoggerModel,
            norReattributionsPubConj
        );

        // Mise à jour de l'activite normative
        changementGouvernementService.reattribuerMinistereActiviteNormative(
            session,
            oldMinistereNode,
            newMinistereNode
        );
        session.save();

        // Migration des ministères spécifique de la table de référence (Ministère CE,
        // ministère du SGG)
        changementGouvernementService.migrerTableReferenceMinistere(
            session,
            oldMinistereNode,
            newMinistereNode,
            migrationLoggerModel
        );

        session.save();
        // Migration des bulletins officiels.
        changementGouvernementService.migrerBulletinOfficiel(
            session,
            oldMinistereNode,
            newMinistereNode,
            migrationLoggerModel
        );

        session.save();
        // Migration des mots cles de l'indexation
        changementGouvernementService.migrerGestionIndexation(
            session,
            oldMinistereNode,
            newMinistereNode,
            migrationLoggerModel
        );

        session.save();

        // Migration des dossiers link des ministères
        EpgDossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        dossierDistributionService.updateDossierLinksACLs(session, newElementOrganigramme);
        session.save();

        if (!norReattributionsPubConj.isEmpty()) {
            migrerPublicationsConjointes(session, norReattributionsPubConj);
            session.save();
        }
    }

    /**
     * Pour chaque NOR de dossier effectivement migré (NOR modifié), on migre les
     * publications conjointes en partant du NOR original.
     *
     * @param norReattributions une Map liant l'ancien NOR au nouveau NOR
     */
    private void migrerPublicationsConjointes(final CoreSession session, Map<String, String> norReattributions) {
        final DossierService dossierService = SolonEpgServiceLocator.getDossierService();

        for (String oldNor : norReattributions.keySet()) {
            String newNor = norReattributions.get(oldNor);
            Dossier dossier = SolonEpgServiceLocator
                .getNORService()
                .getDossierFromNOR(session, newNor)
                .getAdapter(Dossier.class);
            dossierService.updatePublicationsConjointes(dossier, session, oldNor, newNor, norReattributions);
        }
    }

    @Override
    protected void doMigrationDirection(
        CoreSession session,
        MigrationLoggerModel migrationLoggerModel,
        EntiteNode oldMinistereNode,
        EntiteNode newMinistereNode,
        UniteStructurelleNode oldDirectionNode,
        UniteStructurelleNode newDirectionNode
    ) {
        super.doMigrationDirection(
            session,
            migrationLoggerModel,
            oldMinistereNode,
            newMinistereNode,
            oldDirectionNode,
            newDirectionNode
        );

        final ChangementGouvernementService changementGouvernementService = SolonEpgServiceLocator.getChangementGouvernementService();

        // Réattribution du Nor.
        Map<String, String> norReattributions = new HashMap<>();
        changementGouvernementService.reattribuerNorDossierDirection(
            session,
            oldMinistereNode,
            oldDirectionNode,
            newMinistereNode,
            newDirectionNode,
            migrationLoggerModel,
            norReattributions
        );
        session.save();

        // Migrer les directions de la table de référence ( direction PRM)
        changementGouvernementService.migrerTableReferenceDirection(
            session,
            oldMinistereNode,
            oldDirectionNode,
            newMinistereNode,
            newDirectionNode,
            migrationLoggerModel
        );
        session.save();

        if (!norReattributions.isEmpty()) {
            migrerPublicationsConjointes(session, norReattributions);
            session.save();
        }
    }

    @Override
    protected void doMigrationPoste(
        CoreSession session,
        MigrationLoggerModel migrationLoggerModel,
        OrganigrammeNode oldPosteNode,
        OrganigrammeNode newPosteNode
    ) {
        super.doMigrationPoste(session, migrationLoggerModel, oldPosteNode, newPosteNode);

        final ChangementGouvernementService changementGouvernementService = SolonEpgServiceLocator.getChangementGouvernementService();

        // Mise à jour du champ technique posteCreator utilisé pour la visibilité du
        // dossier dans l'infoCentre de
        // l'espace de suivi.
        changementGouvernementService.updateDossierCreatorPoste(
            session,
            oldPosteNode,
            newPosteNode,
            migrationLoggerModel
        );
        session.save();

        // Migrer les postes de la table de référence ( corbeilles de retour au DAN )
        changementGouvernementService.migrerTableReferencePoste(
            session,
            oldPosteNode,
            newPosteNode,
            migrationLoggerModel
        );
        session.save();
    }

    @Override
    protected ChangementGouvernementService initChangementGouvernementService() {
        return SolonEpgServiceLocator.getChangementGouvernementService();
    }

    @Override
    protected EpgOrganigrammeService initOrganigrammeService() {
        return SolonEpgServiceLocator.getEpgOrganigrammeService();
    }
}
