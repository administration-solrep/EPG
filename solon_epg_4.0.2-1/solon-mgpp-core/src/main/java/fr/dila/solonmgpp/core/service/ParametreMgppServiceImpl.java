package fr.dila.solonmgpp.core.service;

import fr.dila.solonepp.rest.api.WSEvenement;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.service.ParametreMgppService;
import fr.dila.solonmgpp.core.domain.ParametrageMgppImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.rest.client.WSProxyFactory;
import fr.dila.st.rest.client.WSProxyFactoryException;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;

/**
 * Implementation de {@link ParametreMgppService}
 *
 * @author asatre
 *
 */
public class ParametreMgppServiceImpl implements ParametreMgppService {
    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(ParametreMgppServiceImpl.class);

    private String idParametreMgpp;

    @Override
    public ParametrageMgpp findParametrageMgpp(final CoreSession session) {
        synchronized (this) {
            ParametrageMgpp parametrageMgpp = null;
            if (idParametreMgpp == null) {
                parametrageMgpp = findOrCreateParametrageMGPP(session);
                idParametreMgpp = parametrageMgpp.getDocument().getId();
                return parametrageMgpp;
            }
            try {
                parametrageMgpp = session.getDocument(new IdRef(idParametreMgpp)).getAdapter(ParametrageMgpp.class);
            } catch (Exception e) {
                parametrageMgpp = findOrCreateParametrageMGPP(session);
                idParametreMgpp = parametrageMgpp.getDocument().getId();
                return parametrageMgpp;
            }
            return parametrageMgpp;
        }
    }

    private ParametrageMgpp findOrCreateParametrageMGPP(final CoreSession session) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(ParametrageMgppImpl.DOC_TYPE);
        queryBuilder.append(" as d ");

        final List<DocumentModel> list = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            ParametrageMgppImpl.DOC_TYPE,
            queryBuilder.toString(),
            null
        );

        if (list.isEmpty()) {
            DocumentModel modelDesired = session.createDocumentModel(ParametrageMgppImpl.DOC_TYPE);
            modelDesired.setPathInfo(ParametrageMgppImpl.PATH, "ParametrageMgpp");
            modelDesired = session.createDocument(modelDesired);
            session.save();

            return modelDesired.getAdapter(ParametrageMgpp.class);
        } else {
            return list.get(0).getAdapter(ParametrageMgpp.class);
        }
    }

    @Override
    public ParametrageMgpp saveParametrageMgpp(
        final CoreSession session,
        final ParametrageMgpp parametrageMgpp,
        final Boolean checkConnexion
    ) {
        if (checkConnexion) {
            checkConnexionEPP(parametrageMgpp, session);
        }

        final DocumentModel doc = session.saveDocument(parametrageMgpp.getDocument());
        session.save();
        return doc.getAdapter(ParametrageMgpp.class);
    }

    private void checkConnexionEPP(final ParametrageMgpp parametrageMgpp, final CoreSession session) {
        WSEvenement wsEvenement = null;
        try {
            final WSProxyFactory proxyFactory = new WSProxyFactory(
                parametrageMgpp.getUrlEpp(),
                null,
                parametrageMgpp.getLoginEpp(),
                null
            );
            wsEvenement = proxyFactory.getService(WSEvenement.class, parametrageMgpp.getPassEpp());
        } catch (final WSProxyFactoryException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
            throw new NuxeoException(e);
        }

        try {
            wsEvenement.test();
        } catch (final Exception e) {
            throw new NuxeoException("Connexion impossible avec ces paramètres.");
        }
    }
}
