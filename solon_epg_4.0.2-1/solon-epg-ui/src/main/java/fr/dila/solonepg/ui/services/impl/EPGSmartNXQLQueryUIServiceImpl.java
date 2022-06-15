/**
 *
 * Surcharge le beam seam SmartNXQLQueryActions du package smartSearch de Nuxeo,
 * afin d'apporter d'avantages de fonctionnalité.
 * Correction de problème d'échappement dans la requête et traduction d'une requête en language
 * utilisateur.
 *
 */
package fr.dila.solonepg.ui.services.impl;

import static fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl.LOG_DEBUG_TEC;
import static fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl.USE_QUERYASSEMBLER_TEC;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.core.util.FullTextUtil;
import fr.dila.solonepg.core.util.RequeteurUtils;
import fr.dila.solonepg.ui.services.EPGSmartNXQLQueryUIService;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.recherche.QueryAssembler;
import fr.dila.st.api.service.RequeteurService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.Requeteur;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.smartquery.HistoryList;
import fr.dila.st.ui.services.actions.impl.STSmartNXQLQueryActionServiceImpl;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.nuxeo.ecm.core.api.CoreSession;

public class EPGSmartNXQLQueryUIServiceImpl
    extends STSmartNXQLQueryActionServiceImpl
    implements EPGSmartNXQLQueryUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(EPGSmartNXQLQueryUIServiceImpl.class);

    @Override
    public Requeteur initCurrentSmartQuery(
        String existingQueryPart,
        boolean resetHistory,
        HistoryList<String> queryPartHistory
    ) {
        LOGGER.debug(LOG_DEBUG_TEC, "Initialisation requêteur d'EPG ");
        Requeteur requeteur = super.initCurrentSmartQuery(existingQueryPart, resetHistory, queryPartHistory);
        //        currentSmartQuery = new STIncrementalSmartNXQLQuery(existingQueryPart);
        requeteur.updateTranslation();
        return requeteur;
    }

    public String getFullQuery(CoreSession session, String queryPart) {
        QueryAssembler assembler = RequeteurUtils.getDossierAssembler();
        RequeteurService requeteurService = STServiceLocator.getRequeteurService();
        String fullQuery = requeteurService.getPattern(session, assembler, queryPart);
        //la recherche NOR ne doit pas commencer par '*' : on ne veut pas que l'utilisateur puisse rechercher tous les dossiers.
        if (FullTextUtil.beginWithStarQuery(fullQuery)) {
            LOGGER.debug(LOG_DEBUG_TEC, "Requête commençant par '*' :" + fullQuery);
            fullQuery = DossierSolonEpgConstants.QUERY_BEGIN_STAR_ERROR;
        }
        // pour toutes les sous-requêtes sur le NOR on convertit les ';' en 'OR'
        Pattern norPattern = Pattern.compile("d.dos:numeroNor [^'\"]* ['\"][^'\"]*['\"]");
        Matcher norMatcher = norPattern.matcher(fullQuery);
        while (norMatcher.find()) {
            String norQueryOld = norMatcher.group();
            String[] norQueryParts = norQueryOld.split("'|\"", 2);
            if (norQueryParts[1].contains(";")) {
                String[] nors = norQueryParts[1].toUpperCase().substring(0, norQueryParts[1].length() - 1).split(";");
                StringBuilder norQueryNew = new StringBuilder();
                for (String nor : nors) {
                    if (!nor.trim().isEmpty()) {
                        norQueryNew.append(norQueryParts[0] + "\"" + nor.trim() + "\"");
                        if (norQueryParts[0].contains("NOT") || norQueryParts[0].contains("!=")) {
                            norQueryNew.append(" AND ");
                        } else {
                            norQueryNew.append(" OR ");
                        }
                    }
                }
                if (!norQueryNew.toString().isEmpty()) {
                    norQueryNew.append("(" + norQueryNew.substring(0, norQueryNew.length() - 4) + ")");
                    fullQuery = fullQuery.replace(norQueryOld, norQueryNew);
                }
            }
        }
        LOGGER.debug(LOG_DEBUG_TEC, "Requête du requeteur de dossier expert");
        LOGGER.debug(USE_QUERYASSEMBLER_TEC, fullQuery);
        return fullQuery;
    }
}
