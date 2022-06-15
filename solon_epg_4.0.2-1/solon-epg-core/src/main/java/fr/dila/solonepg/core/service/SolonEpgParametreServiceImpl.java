package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.parametre.STParametre;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STParametreServiceImpl;
import fr.sword.naiad.nuxeo.commons.core.util.SessionUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.nuxeo.ecm.core.api.CloseableCoreSession;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.runtime.api.Framework;

/**
 * Surcharge du service de paramètres
 *
 */
public class SolonEpgParametreServiceImpl extends STParametreServiceImpl implements SolonEpgParametreService {
    private static final STLogger LOG = STLogFactory.getLog(SolonEpgParametreServiceImpl.class);

    private static final String GET_PARAM_AN_DOC_QUERY = "SELECT * FROM ParametrageAnModel WHERE ecm:isProxy = 0";

    /**
     * Default constructor
     */
    public SolonEpgParametreServiceImpl() {
        super();
    }

    @Override
    public String getUrlSuiviApplicationLois() throws LoginException {
        final StringBuilder url = new StringBuilder();

        LoginContext loginContext = null;
        try {
            loginContext = Framework.login();
            try (CloseableCoreSession coreSession = SessionUtil.openSession()) {
                STParametre param = getParametre(coreSession, SolonEpgParametreConstant.PAGE_SUIVI_APPLICATION_LOIS);
                url.append(param.getValue());
            }
        } catch (LoginException e) {
            LOG.error(STLogEnumImpl.DEFAULT, e);
        } finally {
            if (loginContext != null) {
                loginContext.logout();
            }
        }

        return url.toString();
    }

    /**
     * Récupère le document qui contient tout le paramétrage des legislatures pour le PAN
     */
    @Override
    public ParametrageAN getDocAnParametre(final CoreSession session) {
        ParametrageAN parametrageAN = null;
        DocumentModelList lstResults = QueryUtils.doQuery(session, GET_PARAM_AN_DOC_QUERY, 1, 0);

        if (!lstResults.isEmpty()) {
            DocumentModel parametrageANDoc = lstResults.get(0);
            if (parametrageANDoc != null) {
                parametrageAN = parametrageANDoc.getAdapter(ParametrageAN.class);
            }
        }

        return parametrageAN;
    }

    /**
     * Permet de mettre à jour les textes-maitres impactés par un changement d'une législature de la liste
     *
     * @param oldAndNewLegis
     *            map de remplacement des valeurs
     * @return
     */
    @Override
    public void updateLegislaturesFromTextesMaitres(CoreSession session, final Map<String, String> oldAndNewLegis) {
        for (final Entry<String, String> cursor : oldAndNewLegis.entrySet()) {
            List<DocumentModel> texteMaitreDocList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
                session,
                ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE,
                "select a.ecm:uuid as id from ActiviteNormative as a where a." +
                TexteMaitreConstants.TEXTE_MAITRE_PREFIX +
                ":" +
                TexteMaitreConstants.LEGISLATURE_PUBLICATION +
                " = ?",
                new Object[] { cursor.getKey() }
            );

            for (DocumentModel texteMaitreDoc : texteMaitreDocList) {
                TexteMaitre texteMaitre = texteMaitreDoc.getAdapter(TexteMaitre.class);
                texteMaitre.setLegislaturePublication(cursor.getValue());
                texteMaitre.save(session);
            }
        }
        session.save();
    }
}
