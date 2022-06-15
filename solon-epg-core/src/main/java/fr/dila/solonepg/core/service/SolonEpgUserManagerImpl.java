package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.solonepg.api.service.ProfilUtilisateurService;
import fr.dila.solonepg.api.service.SolonEpgUserManager;
import fr.dila.solonepg.api.service.TableReferenceService;
import fr.dila.ss.core.security.principal.SSPrincipalImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.exception.WorkspaceNotFoundException;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.service.STUserManagerImpl;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.nuxeo.ecm.core.api.CoreInstance;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.ecm.platform.usermanager.NuxeoPrincipalImpl;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.transaction.TransactionHelper;

/**
 * Gestionnaires d'utilisateur de l'application SOLON EPG.
 *
 * @author jtremeaux
 */
public class SolonEpgUserManagerImpl extends STUserManagerImpl implements SolonEpgUserManager, Serializable {
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 8780120682303351969L;

    /**
     * Logger.
     */
    private static final STLogger LOGGER = STLogFactory.getLog(SolonEpgUserManagerImpl.class);

    protected NuxeoPrincipal makeSSPrincipal(DocumentModel userEntry, boolean anonymous, List<String> groups) {
        boolean admin = false;
        String username = userEntry.getId();

        // XXX why not set groups to anonymous user?
        List<String> virtualGroups = new LinkedList<String>();
        if (!anonymous) {
            // Add preconfigured groups: useful for LDAP
            if (defaultGroup != null) {
                virtualGroups.add(defaultGroup);
            }
            // Add additional groups: useful for virtual users
            if (groups != null) {
                virtualGroups.addAll(groups);
            }
            // Create a default admin if needed
            if (administratorIds != null && administratorIds.contains(username)) {
                admin = true;
                if (administratorGroups != null) {
                    virtualGroups.addAll(administratorGroups);
                }
            }
        }

        SSPrincipalImpl principal = new SSPrincipalImpl(username, anonymous, admin, false);
        principal.setConfig(userConfig);

        principal.setModel(userEntry, false);
        principal.setVirtualGroups(virtualGroups, true);

        List<String> roles = Arrays.asList("regular");
        principal.setRoles(roles);

        STUser stUser = userEntry.getAdapter(STUser.class);
        if (stUser == null) {
            throw new NuxeoException("Cannot cast user to STUser");
        }

        // Renseigne les fonctions unitaires de l'utilisateur
        Set<String> baseFunctionSet = STServiceLocator
            .getProfileService()
            .getBaseFunctionFromProfil(principal.getAllGroups());
        principal.setBaseFunctionSet(baseFunctionSet);

        // Renseigne les postes de l'utilisateur
        List<String> posteListe = stUser.getPostes();
        Set<String> posteIdSet = new HashSet<String>(posteListe);
        principal.setPosteIdSet(posteIdSet);

        // Renseigne les ministères de l'utilisateur
        final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
        Set<String> ministereIdSet = new HashSet<String>();
        for (String posteId : posteIdSet) {
            List<EntiteNode> ministereNodeList = ministeresService.getMinistereParentFromPoste(posteId);
            for (OrganigrammeNode ministereNode : ministereNodeList) {
                final String ministereId = ministereNode.getId();
                ministereIdSet.add(ministereId);
            }
        }
        principal.setMinistereIdSet(ministereIdSet);

        // Renseigne les directions de l'utilisateur
        final STUsAndDirectionService usService = STServiceLocator.getSTUsAndDirectionService();
        Set<String> directionIdSet = new HashSet<String>();
        for (String posteId : posteIdSet) {
            List<OrganigrammeNode> directionNodeList = usService.getDirectionFromPoste(posteId);
            for (OrganigrammeNode directionNode : directionNodeList) {
                final String directionId = directionNode.getId();
                directionIdSet.add(directionId);
            }
        }
        principal.setDirectionIdSet(directionIdSet);

        return principal;
    }

    @Override
    protected NuxeoPrincipal makePrincipal(DocumentModel userEntry, boolean anonymous, List<String> groups) {
        // Crée le principal
        NuxeoPrincipal principal = makeSSPrincipal(userEntry, anonymous, groups);

        if (activateComputedGroup() && principal instanceof NuxeoPrincipalImpl) {
            NuxeoPrincipalImpl nuxPrincipal = (NuxeoPrincipalImpl) principal;

            List<String> vGroups = getService().computeGroupsForUser(nuxPrincipal);

            if (vGroups == null) {
                vGroups = new ArrayList<String>();
            }

            List<String> origVGroups = nuxPrincipal.getVirtualGroups();
            if (origVGroups == null) {
                origVGroups = new ArrayList<String>();
            }

            // MERGE!
            origVGroups.addAll(vGroups);

            nuxPrincipal.setVirtualGroups(origVGroups);

            // This a hack to work around the problem of running tests
            if (!Framework.isTestModeSet()) {
                nuxPrincipal.updateAllGroups();
            } else {
                List<String> allGroups = nuxPrincipal.getGroups();
                for (String vGroup : vGroups) {
                    if (!allGroups.contains(vGroup)) {
                        allGroups.add(vGroup);
                    }
                }
                nuxPrincipal.setGroups(allGroups);
            }
        }
        return principal;
    }

    @Override
    public boolean checkUsernamePassword(String username, String password) {
        // Test de la validité du mot de passe
        TransactionHelper.runInTransaction(
            () -> {
                RepositoryManager repoManager = ServiceUtil.getRequiredService(RepositoryManager.class);
                CoreInstance.doPrivileged(
                    repoManager.getDefaultRepositoryName(),
                    session -> {
                        try {
                            String userId = username;
                            NuxeoPrincipal principal = STServiceLocator.getUserManager().getPrincipal(username);
                            if (principal != null) {
                                userId = principal.getName();
                                final ProfilUtilisateurService profilService = SolonEpgServiceLocator.getProfilUtilisateurService();
                                profilService.getOrCreateUserProfilFromId(session, userId);
                                if (profilService.isUserPasswordOutdated(session, userId)) {
                                    LOGGER.info(
                                        session,
                                        STLogEnumImpl.NOTIFICATION_PASSWORD_FONC,
                                        "Mot de passe expiré pour l'utilisateur " + username
                                    );
                                }
                            }
                        } catch (WorkspaceNotFoundException we) {
                            LOGGER.debug(session, STLogEnumImpl.FAIL_GET_WORKSPACE_FONC, we.getMessage());
                            LOGGER.debug(session, STLogEnumImpl.FAIL_GET_WORKSPACE_FONC, we);
                        }
                    }
                );
            }
        );
        if (super.checkUsernamePassword(username, password)) {
            if (virtualUsers.containsKey(username)) {
                return true;
            } else {
                try {
                    DocumentModel userdoc = getPrivilegedUserModel(username);
                    STUser user = userdoc.getAdapter(STUser.class);
                    return user.isActive();
                } catch (NuxeoException e) {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isExistAndNotActive(String username) {
        try {
            DocumentModel userDoc = getUserModel(username);
            if (userDoc == null) {
                return false;
            } else {
                STUser user = userDoc.getAdapter(STUser.class);
                return !user.isActive();
            }
        } catch (NuxeoException e) {
            return false;
        }
    }

    @Override
    public List<String> getAdministratorIds() {
        return this.administratorIds;
    }

    @Override
    public void deleteUserConsultation(CoreSession session, DocumentModel userDoc) {
        // retirer le user des favoris de consultation et derniers users consultés
        Set<String> userToRemove = Collections.singleton(userDoc.getId());
        EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
        String userworkspacePath = STServiceLocator
            .getUserWorkspaceService()
            .getCurrentUserPersonalWorkspace(session)
            .getPathAsString();
        espaceRechercheService.removeUserFromDerniersResultatsConsultes(session, userworkspacePath, userToRemove);
        espaceRechercheService.removeUserFromFavorisConsultations(session, userworkspacePath, userToRemove);
    }

    @Override
    public boolean canDeleteUser(CoreSession session, String userId) {
        TableReferenceService tableReferenceService = SolonEpgServiceLocator.getTableReferenceService();
        TableReference tableReference = tableReferenceService
            .getTableReference(session)
            .getAdapter(TableReference.class);

        return Stream
            .of(
                tableReference.getVecteurPublicationMultiples(),
                tableReference.getCabinetPM(),
                tableReference.getChargesMission(),
                tableReference.getSignataires()
            )
            .filter(CollectionUtils::isNotEmpty)
            .noneMatch(users -> users.contains(userId));
    }
}
