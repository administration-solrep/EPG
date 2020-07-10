package fr.dila.solonepg.core.event.batch;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgParametreConstant;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Batch de suppression des textes signalés
 * 
 * @author asatre
 */
public class CloseTextesNonPubliesBatchListener extends AbstractBatchEventListener {
	/**
	 * Logger
	 */
	private static final STLogger	LOGGER					= STLogFactory
																	.getLog(CloseTextesNonPubliesBatchListener.class);
	private static final String		QUERY_TXT				= "select d.ecm:uuid as id from Dossier as d where d.dos:typeActe = '"
																	+ TypeActeConstants.TYPE_ACTE_TEXTE_NON_PUBLIE
																	+ "' and d.dos:statut='"
																	+ VocabularyConstants.STATUT_LANCE
																	+ "'"
																	+ " and d.dos:dateSignature < ?";
	private static final int		DELAI_CLOTURE_DEFAULT	= 15;

	public CloseTextesNonPubliesBatchListener() {
		super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_CLOSE_TXT_NON_PUB);
	}

	@Override
	protected void processEvent(CoreSession session, Event event) throws ClientException {
		LOGGER.info(session, EpgLogEnumImpl.INIT_B_CLOSE_TXT_NON_PUB_TEC);
		final GregorianCalendar today = new GregorianCalendar();
		final long startTime = today.getTimeInMillis();
		final STParametreService paramServ = STServiceLocator.getSTParametreService();
		final DossierService dossServ = SolonEpgServiceLocator.getDossierService();

		int delaiClotureInt = DELAI_CLOTURE_DEFAULT;
		int nbTextesClos = 0;
		try {
			String delaiCloture = paramServ.getParametreValue(session,
					SolonEpgParametreConstant.DELAI_CLOTURE_TXT_NON_PUB);
			if (delaiCloture == null) {
				delaiClotureInt = DELAI_CLOTURE_DEFAULT;
			} else {
				try {
					delaiClotureInt = Integer.parseInt(delaiCloture);
				} catch (NumberFormatException exc) {
					delaiClotureInt = DELAI_CLOTURE_DEFAULT;
				}
			}
			today.add(GregorianCalendar.DAY_OF_MONTH, -delaiClotureInt);

			Long count = QueryUtils.doCountQuery(session, QueryUtils.ufnxqlToFnxqlQuery(QUERY_TXT),
					new Object[] { today });
			LOGGER.info(session, EpgLogEnumImpl.PROCESS_B_CLOSE_TXT_NON_PUB_TEC, count + " Documents trouvés");
			long limit = 100;
			List<DocumentModel> dossiersDocsList;
			for (Long offset = (long) 0; offset <= count; offset += limit) {
				Long borne = count < offset + limit ? count : offset + limit;
				LOGGER.info(session, EpgLogEnumImpl.PROCESS_B_CLOSE_TXT_NON_PUB_TEC, "Récupération des dossiers de "
						+ offset + " à " + borne);

				dossiersDocsList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
						DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, QueryUtils.ufnxqlToFnxqlQuery(QUERY_TXT),
						new Object[] { today }, limit, 0);

				for (DocumentModel dossierDoc : dossiersDocsList) {
					dossServ.cloreDossierUnrestricted(session, dossierDoc.getAdapter(Dossier.class));
					++nbTextesClos;
				}
				session.save();
				TransactionHelper.commitOrRollbackTransaction();
				TransactionHelper.startTransaction();
				LOGGER.info(session, EpgLogEnumImpl.PROCESS_B_CLOSE_TXT_NON_PUB_TEC,
						"Fin de traitement des dossiers de " + offset + " à " + borne);
			}
		} catch (final Exception exc) {
			LOGGER.error(session, EpgLogEnumImpl.FAIL_PROCESS_B_CLOSE_TXT_NON_PUB_TEC, exc);
			++errorCount;
		}
		final long endTime = Calendar.getInstance().getTimeInMillis();
		try {
			STServiceLocator.getSuiviBatchService().createBatchResultFor(batchLoggerModel,
					"Clôture des textes non publiés au JO signés depuis " + delaiClotureInt + " jours", nbTextesClos,
					endTime - startTime);
		} catch (ClientException exc) {
			LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, exc);
		}
		LOGGER.info(session, EpgLogEnumImpl.END_B_CLOSE_TXT_NON_PUB_TEC);
	}
}
