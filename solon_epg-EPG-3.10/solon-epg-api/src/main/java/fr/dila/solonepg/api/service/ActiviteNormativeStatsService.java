package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.activitenormative.ANReport;
import fr.dila.solonepg.api.birt.ExportPANStat;

public interface ActiviteNormativeStatsService extends Serializable {

    /**
     * Actualise le rapport passé en paramètre pour l'utilisateur ciblé
     * 
     * @param session
     * @param userWorkspacePath
     * @param report
     * @param user
     * @param inputValues
     * @throws ClientException
     */
    void refreshStats(CoreSession session, String userWorkspacePath, ANReport report, String user, Map<String, String> inputValues) throws ClientException;

    /**
     * Retourne pour l'utilisateur de la session le document du rapport birt rafraichi s'il existe S'il n'existe pas retourne null
     * 
     * @param session
     * @param report
     * @return le document model du rapport birt rafraichi ou null si inexistant
     * @throws ClientException
     */
    DocumentModel getBirtRefreshDocForCurrentUser(CoreSession session, ANReport report) throws ClientException;

    /**
     * Retourne pour l'utilisateur dont le path du workspace est donné en paramètre le document du rapport birt rafraichi s'il existe S'il n'existe pas retourne null
     * 
     * @param session
     * @param report
     * @param userWorkspacePath
     * @return le document model du rapport birt rafraichi ou null si inexistant
     * @throws ClientException
     */
    DocumentModel getBirtRefreshDoc(CoreSession session, ANReport report, String userWorkspacePath) throws ClientException;

    /**
     * Passe pour l'utilisateur le document du rapport birt en life cycle rafraichissement en cours
     * 
     * @param session
     * @param report
     * @param user
     * @return vrai si la transition a pu être faite, faux sinon
     * @throws ClientException
     */
    boolean flagRefreshFor(CoreSession session, ANReport report, String user, String userWorkspacePath) throws ClientException;

    /**
     * Passe pour l'utilisateur le document du rapport birt en life cycle rafraichissement terminé
     * 
     * @param session
     * @param report
     * @param user
     * @throws ClientException
     */
    void flagEndRefreshFor(CoreSession session, ANReport report, String user, String userWorkspacePath) throws ClientException;

    /**
     * Vérifie si un rapport birt rafraichi existe pour l'utilisateur de la session
     * 
     * @param session
     * @param report
     * @return vrai si un rapport existe, faux sinon
     * @throws ClientException
     */
    boolean existBirtRefreshForCurrentUser(CoreSession session, ANReport report) throws ClientException;

    /**
     * Vérifie si un rapport birt rafraichi existe pour l'utilisateur passé en paramètre
     * 
     * @param session
     * @param report
     * @param user
     * @return vrai si un rapport existe, faux sinon
     * @throws ClientException
     */
    boolean existBirtRefresh(CoreSession session, ANReport report, String user) throws ClientException;

    /**
     * Récupère l'horodatage du rapport birt rafraichi pour l'utilisateur passé en paramètre
     * 
     * @param session
     * @param report
     * @param user
     * @return string format JJ/MM/AAAA HH:MM de la demande de rafraichissement
     * @throws ClientException
     */
    String getHorodatageRequest(CoreSession session, ANReport report, String user) throws ClientException;

    /**
     * Vérifie si le rapport est en cours de rafraichissement pour l'utilisateur passé en paramètre
     * 
     * @param session
     * @param report
     * @param user
     * @return vrai si un rafraichissement est en cours, faux sinon
     * @throws ClientException
     */
    boolean isCurrentlyRefreshing(CoreSession session, ANReport report, String user) throws ClientException;

    /**
     * Retourne le chemin de la racine du répertoire des rapports rafraichis pour un user
     * 
     * @param session
     * @param userWorkspacePath le chemin du userWorkspace de l'utilisateur
     * @return le chemin de la racine
     * @throws ClientException
     */
    String getBirtRefreshRootPath(CoreSession session, String userWorkspacePath) throws ClientException;

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
     * @throws ClientException
     */
    void exportPANStats(CoreSession session, final String userWorkspacePath, final String user, final Map<String, String> inputValues, List<String> exportedLegislature) throws ClientException;

    /**
     * @param session
     * @param user
     * @param curLegis
     * @return
     */
    public boolean isCurrentlyExporting(CoreSession session, String user,List<String> exportedLegislature) throws ClientException;

    /**
     * 
     * @param session
     * @param user
     * @param userWorkspacePath
     * @param curLegis
     * @return
     * @throws ClientException
     */
    boolean flagExportFor(CoreSession session, String user, String userWorkspacePath,List<String> exportedLegislature) throws ClientException;

    /**
     * 
     * @param session
     * @param user
     * @param curLegis
     * @return
     * @throws ClientException
     */
    String getExportHorodatageRequest(CoreSession session, String user, List<String> exportedLegislature) throws ClientException;

    /**
     * 
     * @param session
     * @param user
     * @param enCours 
     * @return
     * @throws ClientException
     */
    DocumentModel getExportPanStatDoc(CoreSession session, String user, List<String> exportedLegislature) throws ClientException;

    /**
     * 
     * @param session
     * @param userWorkspacePath
     * @return
     * @throws ClientException
     */
    String getExportPanStatRootPath(CoreSession session, String userWorkspacePath) throws ClientException;

    /**
     * 
     * @param session
     * @param user
     * @param userWorkspacePath
     * @param curLegis
     * @throws ClientException
     */
    void flagEndEXportingFor(CoreSession session, String user, String userWorkspacePath,List<String> exportedLegislature) throws ClientException;

	List<ExportPANStat> getAllExportPanStatDocForUser(CoreSession documentManager, String name) throws ClientException;
}
