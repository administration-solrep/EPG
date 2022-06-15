package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.logging.STLogEnum;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.ProfileService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.STMailHelper;
import fr.dila.st.core.util.SolonDateConverter;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;
import javax.mail.Address;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.runtime.transaction.TransactionHelper;

public abstract class AbstractCandidatToArchivageListener extends AbstractBatchEventListener {
    private static final STLogger LOGGER = STLogFactory.getLog(AbstractCandidatToArchivageListener.class);
    private static final int LIMITE_DOSSIER_COMMIT = 50;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private STLogEnum logStart;
    private STLogEnum logEnd;
    private STLogEnum logFail;
    private String statutArchivage;
    private String description;
    private String duaDelaiParameterName;
    private String objetMailParameterName;
    private String texteMailParameterName;

    protected AbstractCandidatToArchivageListener(
        String eventName,
        STLogEnum logStart,
        STLogEnum logEnd,
        STLogEnum logFail,
        String statutArchivage,
        String description,
        String duaDelaiParameterName,
        String objetMailParameterName,
        String texteMailParameterName
    ) {
        super(LOGGER, eventName);
        this.logStart = logStart;
        this.logEnd = logEnd;
        this.logFail = logFail;
        this.statutArchivage = statutArchivage;
        this.description = description;
        this.duaDelaiParameterName = duaDelaiParameterName;
        this.objetMailParameterName = objetMailParameterName;
        this.texteMailParameterName = texteMailParameterName;
    }

    @Override
    protected void processEvent(CoreSession session, Event event) {
        LOGGER.info(session, logStart);
        Instant startTime = Instant.now();

        long nbArchiv = 0;
        try {
            Calendar dua = Calendar.getInstance();
            STParametreService paramService = STServiceLocator.getSTParametreService();
            String duaDelai = paramService.getParametreValue(session, duaDelaiParameterName);
            dua.add(Calendar.MONTH, -Integer.parseInt(duaDelai));
            String literalDate = SolonDateConverter.DATE_DASH_REVERSE.format(dua);

            String queryStr = getQuery(session, paramService, literalDate);

            DocumentModelList list = session.query(queryStr);
            if (CollectionUtils.isNotEmpty(list)) {
                nbArchiv = list.size();
                ProfileService profileService = STServiceLocator.getProfileService();
                List<STUser> users = profileService.getUsersFromBaseFunction(
                    STBaseFunctionConstant.ADMIN_FONCTIONNEL_EMAIL_RECEIVER
                );
                StringBuilder dossierNorListe = new StringBuilder();
                List<List<DocumentModel>> partitionList = ListUtils.partition(list, LIMITE_DOSSIER_COMMIT);

                for (List<DocumentModel> listDoc : partitionList) {
                    for (DocumentModel dossierDoc : listDoc) {
                        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
                        dossier.setStatutArchivage(statutArchivage);
                        session.saveDocument(dossierDoc);

                        additionalProcessToDossier(session, dossierDoc);
                        dossierNorListe.append(dossier.getNumeroNor());
                        dossierNorListe.append(LINE_SEPARATOR);
                    }
                    // commit pour éviter les problèmes de rollback
                    TransactionHelper.commitOrRollbackTransaction();
                    TransactionHelper.startTransaction();
                }
                session.save();

                List<Address> addresses = STMailHelper.toListAddress(users);
                if (CollectionUtils.isNotEmpty(addresses)) {
                    String objet = paramService.getParametreValue(session, objetMailParameterName);
                    String text = paramService.getParametreValue(session, texteMailParameterName);
                    String emailMessage = text + LINE_SEPARATOR + dossierNorListe;

                    STServiceLocator
                        .getSTMailService()
                        .sendMail(emailMessage, objet, "mailSession", addresses.toArray(new Address[0]));
                }
            }
        } catch (Exception exc) {
            LOGGER.error(session, logFail, exc);
            errorCount++;
        }
        Instant endTime = Instant.now();
        try {
            STServiceLocator
                .getSuiviBatchService()
                .createBatchResultFor(
                    batchLoggerModel,
                    description,
                    nbArchiv,
                    ChronoUnit.MILLIS.between(startTime, endTime)
                );
        } catch (Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
        }
        LOGGER.info(session, logEnd);
    }

    protected abstract String getQuery(CoreSession session, STParametreService paramService, String literalDate);

    protected void additionalProcessToDossier(CoreSession session, DocumentModel dossierDoc) {
        // nothing to do by default
    }
}
