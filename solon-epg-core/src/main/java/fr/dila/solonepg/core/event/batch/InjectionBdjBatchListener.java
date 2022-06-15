package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.WsBdjService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.SuiviBatchService;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.naiad.nuxeo.ufnxql.core.query.FlexibleQueryMaker;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.IterableQueryResult;
import org.nuxeo.ecm.core.event.Event;

/**
 * Batch de publication des textes modifiés vers BDJ.
 *
 * @author tlombard
 */
public class InjectionBdjBatchListener extends AbstractBatchEventListener {
    private static final STLogger LOGGER = STLogFactory.getLog(InjectionBdjBatchListener.class);

    private static final String QUERY_TEXTES_MAITRES_MODIFIES =
        "SELECT id FROM texte_maitre tm " +
        " WHERE (tm.DATEMODIFICATION > tm.DATEINJECTION) OR (tm.DATEMODIFICATION IS NOT NULL AND tm.DATEINJECTION IS NULL)";

    public InjectionBdjBatchListener() {
        super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_INJECTION_TM_BDJ);
    }

    @Override
    protected void processEvent(CoreSession session, Event event) {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_INJECTION_TM_BDJ_TEC);
        final long startTime = Calendar.getInstance().getTimeInMillis();

        final WsBdjService wsBdjService = SolonEpgServiceLocator.getWsBdjService();

        if (wsBdjService.isPublicationEcheancierBdjActivated(session)) {
            List<TexteMaitre> textesMaitres = new ArrayList<>();

            try (
                IterableQueryResult res = QueryUtils.doSqlQuery(
                    session,
                    new String[] { FlexibleQueryMaker.COL_ID },
                    QUERY_TEXTES_MAITRES_MODIFIES,
                    new Object[] {}
                );
            ) {
                final Iterator<Map<String, Serializable>> iterator = res.iterator();

                while (iterator.hasNext()) {
                    final Map<String, Serializable> row = iterator.next();
                    String texteMaitreId = (String) row.get(FlexibleQueryMaker.COL_ID);

                    DocumentModel doc = session.getDocument(new IdRef(texteMaitreId));
                    textesMaitres.add(doc.getAdapter(TexteMaitre.class));
                }
            }

            // Pour chacun de ces textes maitres, renvoyer vers le BDJ
            try {
                wsBdjService.publierEcheancierBDJ(textesMaitres, session);
            } catch (Exception e) {
                wsBdjService.sendTransferErrorMailToAdmins(e, session);
            }
        }

        final long endTime = Calendar.getInstance().getTimeInMillis();
        final SuiviBatchService suiviBatchService = STServiceLocator.getSuiviBatchService();
        try {
            suiviBatchService.createBatchResultFor(
                batchLoggerModel,
                "Injection des textes maitres au BDJ",
                endTime - startTime
            );
        } catch (Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
        }
        LOGGER.info(session, EpgLogEnumImpl.END_B_INJECTION_TM_BDJ_TEC);
    }
}
