package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.activitenormative.ANReport;
import fr.dila.solonepg.api.birt.ExportPANStat;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface ActiviteNormativeStatsService extends Serializable {
    /**
     * Actualise le rapport passé en paramètre pour l'utilisateur ciblé
     *
     * @param session
     * @param userWorkspacePath
     * @param report
     * @param user
     * @param inputValues
     */
    void refreshStats(
        CoreSession session,
        String userWorkspacePath,
        ANReport report,
        String user,
        Map<String, Serializable> inputValues
    );

    /**
     * Retourne pour l'utilisateur de la session le document du rapport birt
     * rafraichi s'il existe S'il n'existe pas retourne null
     *
     * @param session
     * @param report
     * @return le document model du rapport birt rafraichi ou null si inexistant
     *
     */
    DocumentModel getBirtRefreshDocForCurrentUser(CoreSession session, ANReport report);

    /**
     * Retourne pour l'utilisateur dont le path du workspace est donné en paramètre
     * le document du rapport birt rafraichi s'il existe S'il n'existe pas retourne
     * null
     *
     * @param session
     * @param report
     * @param userWorkspacePath
     * @return le document model du rapport birt rafraichi ou null si inexistant
     *
     */
    DocumentModel getBirtRefreshDoc(CoreSession session, ANReport report, String userWorkspacePath);

    /**
     * Passe pour l'utilisateur le document du rapport birt en life cycle
     * rafraichissement en cours
     *
     * @param session
     * @param report
     * @param user
     * @return vrai si la transition a pu être faite, faux sinon
     *
     */
    boolean flagRefreshFor(CoreSession session, ANReport report, String user, String userWorkspacePath);

    /**
     * Passe pour l'utilisateur le document du rapport birt en life cycle
     * rafraichissement terminé
     *
     * @param session
     * @param report
     * @param user
     *
     */
    void flagEndRefreshFor(CoreSession session, ANReport report, String user, String userWorkspacePath);

    /**
     * Vérifie si un rapport birt rafraichi existe pour l'utilisateur de la session
     *
     * @param session
     * @param report
     * @return vrai si un rapport existe, faux sinon
     *
     */
    boolean existBirtRefreshForCurrentUser(CoreSession session, ANReport report);

    /**
     * Vérifie si un rapport birt rafraichi existe pour l'utilisateur passé en
     * paramètre
     *
     * @param session
     * @param report
     * @param user
     * @return vrai si un rapport existe, faux sinon
     *
     */
    boolean existBirtRefresh(CoreSession session, ANReport report, String user);

    /**
     * Récupère l'horodatage du rapport birt rafraichi pour l'utilisateur passé en
     * paramètre
     *
     * @param session
     * @param report
     * @param user
     * @return string format JJ/MM/AAAA HH:MM de la demande de rafraichissement
     *
     */
    String getHorodatageRequest(CoreSession session, ANReport report, String user);

    /**
     * Vérifie si le rapport est en cours de rafraichissement pour l'utilisateur
     * passé en paramètre
     *
     * @param session
     * @param report
     * @param user
     * @return vrai si un rafraichissement est en cours, faux sinon
     *
     */
    boolean isCurrentlyRefreshing(CoreSession session, ANReport report, String user);

    /**
     * Retourne le chemin de la racine du répertoire des rapports rafraichis pour un
     * user
     *
     * @param session
     * @param userWorkspacePath le chemin du userWorkspace de l'utilisateur
     * @return le chemin de la racine
     *
     */
    String getBirtRefreshRootPath(CoreSession session, String userWorkspacePath);

    /**
     * export les stats PAN
     *
     * @param session
     * @param userWorkspacePath
     * @param report
     * @param user
     * @param inputValues
     * @param configParams
     * @param curLegis
     *
     */
    void exportPANStats(
        CoreSession session,
        final String userWorkspacePath,
        final String user,
        final Map<String, Serializable> inputValues,
        List<String> exportedLegislature
    );

    /**
     * @param session
     * @param user
     * @param curLegis
     * @return
     */
    boolean isCurrentlyExporting(CoreSession session, String user, List<String> exportedLegislature);

    /**
     *
     * @param session
     * @param user
     * @param userWorkspacePath
     * @param curLegis
     * @return
     *
     */
    boolean flagExportFor(CoreSession session, String user, String userWorkspacePath, List<String> exportedLegislature);

    /**
     *
     * @param session
     * @param user
     * @param curLegis
     * @return
     *
     */
    String getExportHorodatageRequest(CoreSession session, String user, List<String> exportedLegislature);

    /**
     *
     * @param session
     * @param user
     * @param enCours
     * @return
     *
     */
    DocumentModel getExportPanStatDoc(CoreSession session, String user, List<String> exportedLegislature);

    /**
     *
     * @param session
     * @param userWorkspacePath
     * @return
     *
     */
    String getExportPanStatRootPath(CoreSession session, String userWorkspacePath);

    /**
     *
     * @param session
     * @param user
     * @param userWorkspacePath
     * @param curLegis
     *
     */
    void flagEndEXportingFor(
        CoreSession session,
        String user,
        String userWorkspacePath,
        List<String> exportedLegislature
    );

    List<ExportPANStat> getAllExportPanStatDocForUser(CoreSession documentManager, String name);
}
