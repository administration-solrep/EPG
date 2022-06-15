package fr.dila.solonepg.core.groupcomputer;

import fr.dila.solonepg.api.constant.SolonEpgAclConstant;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STAclConstant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.platform.computedgroups.AbstractGroupComputer;
import org.nuxeo.ecm.platform.usermanager.NuxeoPrincipalImpl;

/**
 * Ce calculateur de groupe injecte dans le principal les groupes calculés pour SOLON EPG.
 *
 * @author jtremeaux
 */
public class SolonEpgGroupComputer extends AbstractGroupComputer {
    private static final Log log = LogFactory.getLog(SolonEpgGroupComputer.class);

    @Override
    public List<String> getGroupsForUser(NuxeoPrincipalImpl nuxeoPrincipal) {
        if (nuxeoPrincipal == null) {
            return Collections.emptyList();
        }

        // Récupère les groupes actuels du principal
        final List<String> groupList = nuxeoPrincipal.getGroups();
        try {
            List<String> newGroupList = new ArrayList<String>(groupList);
            if (!(nuxeoPrincipal instanceof SSPrincipal)) {
                throw new Exception("Le principal doit être du type SSPrincipal");
            }
            SSPrincipal ssPrincipal = (SSPrincipal) nuxeoPrincipal;

            // Injecte les groupes donnant accès aux dossiers
            newGroupList.addAll(getDossierMesureNominativeGroupSet(ssPrincipal));
            newGroupList.addAll(getDossierAdminMinUpdaterGroupSet(ssPrincipal));
            newGroupList.addAll(getDossierDistributionMinistereReaderGroupSet(ssPrincipal));
            newGroupList.addAll(getDossierDistributionDirectionReaderGroupSet(ssPrincipal));
            newGroupList.addAll(getDossierRattachementMinistereReaderGroupSet(ssPrincipal));
            newGroupList.addAll(getDossierRattachementDirectionReaderGroupSet(ssPrincipal));
            newGroupList.addAll(getIndexationMinUpdaterGroupSet(ssPrincipal));
            newGroupList.addAll(getIndexationMinPubliUpdaterGroupSet(ssPrincipal));
            newGroupList.addAll(getIndexationDirUpdaterGroupSet(ssPrincipal));
            newGroupList.addAll(getIndexationDirPubliUpdaterGroupSet(ssPrincipal));

            return newGroupList;
        } catch (Exception e) {
            log.error("Impossible d'associer les groupes de SOLON EPG à l'utilisateur connecté.", e);
            return groupList;
        }
    }

    /**
     * Ajout les groupes concernant les mesures nominatives.
     *
     * @param principal Principal
     * @return Groupes correspondant à cette fonction
     */
    private Set<String> getDossierMesureNominativeGroupSet(SSPrincipal ssPrincipal) {
        // Si l'utilisateur n'a pas accès aux mesures nominatives, ajout d'un groupe qui restreint l'accès
        final Set<String> baseFunctionSet = ssPrincipal.getBaseFunctionSet();
        final Set<String> newGroupSet = new HashSet<String>();
        if (!baseFunctionSet.contains(SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_READER)) {
            newGroupSet.add(SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_DENY_READER);
        }

        return newGroupSet;
    }

    /**
     * Ajout les groupes donnant accès aux DossierLink via la distribution dans les ministères.
     *
     * @param principal Principal
     * @return Groupes correspondant à cette fonction
     */
    private Set<String> getDossierAdminMinUpdaterGroupSet(SSPrincipal ssPrincipal) {
        // Ajoute les groupes uniquement si l'utilisateur possède la fonction unitaire
        final Set<String> baseFunctionSet = ssPrincipal.getBaseFunctionSet();
        final Set<String> newGroupSet = new HashSet<String>();
        if (!baseFunctionSet.contains(SolonEpgBaseFunctionConstant.DOSSIER_ADMIN_MIN_UPDATER)) {
            return newGroupSet;
        }

        // Injecte un groupe pour chacun des ministères
        final Set<String> ministereIdSet = ssPrincipal.getMinistereIdSet();
        for (String ministereId : ministereIdSet) {
            String group = STAclConstant.DOSSIER_LINK_UPDATER_MIN_ACE_PREFIX + ministereId;
            newGroupSet.add(group);
        }

        return newGroupSet;
    }

    /**
     * Ajout les groupes donnant accès au dossier.
     *
     * @param principal Principal
     * @return Groupes correspondant à cette fonction
     */
    private Set<String> getDossierDistributionMinistereReaderGroupSet(SSPrincipal ssPrincipal) {
        // Ajoute les groupes uniquement si l'utilisateur possède la fonction unitaire
        final Set<String> baseFunctionSet = ssPrincipal.getBaseFunctionSet();
        final Set<String> newGroupSet = new HashSet<String>();
        if (!baseFunctionSet.contains(SolonEpgBaseFunctionConstant.DOSSIER_DISTRIBUTION_MINISTERE_READER)) {
            return newGroupSet;
        }

        // Injecte un groupe pour chacun des ministères
        final Set<String> ministereIdSet = ssPrincipal.getMinistereIdSet();
        for (String ministereId : ministereIdSet) {
            String group = SolonEpgAclConstant.DOSSIER_DIST_MIN_ACE_PREFIX + ministereId;
            newGroupSet.add(group);
        }

        return newGroupSet;
    }

    /**
     * Ajout les groupes donnant accès au dossier.
     *
     * @param principal Principal
     * @return Groupes correspondant à cette fonction
     */
    private Set<String> getDossierDistributionDirectionReaderGroupSet(SSPrincipal ssPrincipal) {
        // Ajoute les groupes uniquement si l'utilisateur possède la fonction unitaire
        final Set<String> baseFunctionSet = ssPrincipal.getBaseFunctionSet();
        final Set<String> newGroupSet = new HashSet<String>();
        if (!baseFunctionSet.contains(SolonEpgBaseFunctionConstant.DOSSIER_DISTRIBUTION_DIRECTION_READER)) {
            return newGroupSet;
        }

        // Injecte un groupe pour chacune des directions
        final Set<String> directionIdSet = ssPrincipal.getDirectionIdSet();
        for (String directionId : directionIdSet) {
            String group = SolonEpgAclConstant.DOSSIER_DIST_DIR_ACE_PREFIX + directionId;
            newGroupSet.add(group);
        }

        return newGroupSet;
    }

    /**
     * Ajout les groupes donnant accès au dossier.
     *
     * @param principal Principal
     * @return Groupes correspondant à cette fonction
     */
    private Set<String> getDossierRattachementMinistereReaderGroupSet(SSPrincipal ssPrincipal) {
        // Ajoute les groupes uniquement si l'utilisateur possède la fonction unitaire
        final Set<String> baseFunctionSet = ssPrincipal.getBaseFunctionSet();
        final Set<String> newGroupSet = new HashSet<String>();
        if (!baseFunctionSet.contains(SolonEpgBaseFunctionConstant.DOSSIER_RATTACHEMENT_MINISTERE_READER)) {
            return newGroupSet;
        }

        // Injecte un groupe pour chacun des ministères
        final Set<String> ministereIdSet = ssPrincipal.getMinistereIdSet();
        for (String ministereId : ministereIdSet) {
            String group = SolonEpgAclConstant.DOSSIER_RATTACH_MIN_ACE_PREFIX + ministereId;
            newGroupSet.add(group);
        }

        return newGroupSet;
    }

    /**
     * Ajout les groupes donnant accès au dossier.
     *
     * @param principal Principal
     * @return Groupes correspondant à cette fonction
     */
    private Set<String> getDossierRattachementDirectionReaderGroupSet(SSPrincipal ssPrincipal) {
        // Ajoute les groupes uniquement si l'utilisateur possède la fonction unitaire
        final Set<String> baseFunctionSet = ssPrincipal.getBaseFunctionSet();
        final Set<String> newGroupSet = new HashSet<String>();
        if (!baseFunctionSet.contains(SolonEpgBaseFunctionConstant.DOSSIER_RATTACHEMENT_DIRECTION_READER)) {
            return newGroupSet;
        }

        // Injecte un groupe pour chacune des directions
        final Set<String> directionIdSet = ssPrincipal.getDirectionIdSet();
        for (String directionId : directionIdSet) {
            String group = SolonEpgAclConstant.DOSSIER_RATTACH_DIR_ACE_PREFIX + directionId;
            newGroupSet.add(group);
        }

        return newGroupSet;
    }

    /**
     * Ajoute les groupes donnant accès à l'indexation.
     *
     * @param ssPrincipal Principal
     * @return Groupes correspondant à cette fonction
     */
    private Set<String> getIndexationMinUpdaterGroupSet(SSPrincipal ssPrincipal) {
        // Ajoute les groupes uniquement si l'utilisateur possède la fonction unitaire
        final Set<String> baseFunctionSet = ssPrincipal.getBaseFunctionSet();
        final Set<String> newGroupSet = new HashSet<String>();
        if (!baseFunctionSet.contains(SolonEpgBaseFunctionConstant.INDEXATION_MIN_UPDATER)) {
            return newGroupSet;
        }

        // Injecte un groupe pour chacun des ministères
        final Set<String> ministereIdSet = ssPrincipal.getMinistereIdSet();
        for (String ministereId : ministereIdSet) {
            String group = SolonEpgAclConstant.INDEXATION_MIN_ACE_PREFIX + ministereId;
            newGroupSet.add(group);
        }

        return newGroupSet;
    }

    /**
     * Ajoute les groupes donnant accès à l'indexation.
     *
     * @param ssPrincipal Principal
     * @return Groupes correspondant à cette fonction
     */
    private Set<String> getIndexationMinPubliUpdaterGroupSet(SSPrincipal ssPrincipal) {
        // Ajoute les groupes uniquement si l'utilisateur possède la fonction unitaire
        final Set<String> baseFunctionSet = ssPrincipal.getBaseFunctionSet();
        final Set<String> newGroupSet = new HashSet<String>();
        if (!baseFunctionSet.contains(SolonEpgBaseFunctionConstant.INDEXATION_MIN_PUBLI_UPDATER)) {
            return newGroupSet;
        }

        // Injecte un groupe pour chacun des ministères
        final Set<String> ministereIdSet = ssPrincipal.getMinistereIdSet();
        for (String ministereId : ministereIdSet) {
            String group = SolonEpgAclConstant.INDEXATION_MIN_PUBLI_ACE_PREFIX + ministereId;
            newGroupSet.add(group);
        }

        return newGroupSet;
    }

    /**
     * Ajoute les groupes donnant accès à l'indexation.
     *
     * @param ssPrincipal Principal
     * @return Groupes correspondant à cette fonction
     */
    private Set<String> getIndexationDirUpdaterGroupSet(SSPrincipal ssPrincipal) {
        // Ajoute les groupes uniquement si l'utilisateur possède la fonction unitaire
        final Set<String> baseFunctionSet = ssPrincipal.getBaseFunctionSet();
        final Set<String> newGroupSet = new HashSet<String>();
        if (!baseFunctionSet.contains(SolonEpgBaseFunctionConstant.INDEXATION_DIR_UPDATER)) {
            return newGroupSet;
        }

        // Injecte un groupe pour chacune des directions
        final Set<String> directionIdSet = ssPrincipal.getDirectionIdSet();
        for (String directionId : directionIdSet) {
            String group = SolonEpgAclConstant.INDEXATION_DIR_ACE_PREFIX + directionId;
            newGroupSet.add(group);
        }

        return newGroupSet;
    }

    /**
     * Ajoute les groupes donnant accès à l'indexation.
     *
     * @param ssPrincipal Principal
     * @return Groupes correspondant à cette fonction
     */
    private Set<String> getIndexationDirPubliUpdaterGroupSet(SSPrincipal ssPrincipal) {
        // Ajoute les groupes uniquement si l'utilisateur possède la fonction unitaire
        final Set<String> baseFunctionSet = ssPrincipal.getBaseFunctionSet();
        final Set<String> newGroupSet = new HashSet<String>();
        if (!baseFunctionSet.contains(SolonEpgBaseFunctionConstant.INDEXATION_DIR_PUBLI_UPDATER)) {
            return newGroupSet;
        }

        // Injecte un groupe pour chacune des directions
        final Set<String> directionIdSet = ssPrincipal.getDirectionIdSet();
        for (String directionId : directionIdSet) {
            String group = SolonEpgAclConstant.INDEXATION_DIR_PUBLI_ACE_PREFIX + directionId;
            newGroupSet.add(group);
        }

        return newGroupSet;
    }

    @Override
    public List<String> getParentsGroupNames(String groupName) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getSubGroupsNames(String groupName) {
        return Collections.emptyList();
    }

    /**
     * Retourne faux: aucune fonction unitaire ne doit être vue comme un groupe.
     */
    @Override
    public boolean hasGroup(String name) {
        return false;
    }

    /**
     * Returns an empty list for efficiency
     */
    @Override
    public List<String> getAllGroupIds() {
        return Collections.emptyList();
    }

    /**
     * Returns an empty list as mailboxes are not searchable
     */
    @Override
    public List<String> searchGroups(Map<String, Serializable> filter, Set<String> fulltext) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getGroupMembers(String groupName) {
        return Collections.emptyList();
    }
}
