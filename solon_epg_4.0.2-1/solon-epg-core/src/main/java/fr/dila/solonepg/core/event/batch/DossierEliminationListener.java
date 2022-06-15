package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.ProfileService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.query.sql.model.DateLiteral;
import org.nuxeo.runtime.transaction.TransactionHelper;

public class DossierEliminationListener extends AbstractBatchEventListener {
    private static final STLogger LOGGER = STLogFactory.getLog(DossierEliminationListener.class);

    public DossierEliminationListener() {
        super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_DOSSIER_ELIMINATION);
    }

    @Override
    protected void processEvent(CoreSession session, Event event) {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_ELIMINATION_TEC);
        final long startTime = Calendar.getInstance().getTimeInMillis();
        long nbEliminationCandidat = 0;
        try {
            final Calendar dua = Calendar.getInstance();
            final STParametreService paramService = STServiceLocator.getSTParametreService();
            final String duaDelai = paramService.getParametreValue(
                session,
                STParametreConstant.DELAI_ELIMINATION_DOSSIERS
            );
            dua.add(Calendar.MONTH, -Integer.parseInt(duaDelai) + 1);
            final String literalDate = DateLiteral.dateFormatter.print(dua.getTimeInMillis());

            final String query =
                "SELECT * FROM Dossier WHERE ecm:currentLifeCycleState = 'init' and dos:" +
                DossierSolonEpgConstants.DOSSIER_CANDIDAT_PROPERTY +
                " = '" +
                VocabularyConstants.CANDIDAT_AUCUN +
                "' and dos:" +
                DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY +
                " = '" +
                VocabularyConstants.STATUT_INITIE +
                "' and  dc:modified < TIMESTAMP '" +
                literalDate +
                " ' and ecm:isProxy = 0 ";

            DocumentModelList dossierList = null;
            dossierList = session.query(query);
            if (dossierList.size() > 0) {
                nbEliminationCandidat = dossierList.size();
                final ProfileService profileService = STServiceLocator.getProfileService();
                final List<STUser> users = profileService.getUsersFromBaseFunction(
                    STBaseFunctionConstant.ADMIN_FONCTIONNEL_EMAIL_RECEIVER
                );
                final StringBuilder builder = new StringBuilder();

                Integer compteurDossier = 0;
                for (final DocumentModel dossierDoc : dossierList) {
                    final DossierService dossierService = SolonEpgServiceLocator.getDossierService();
                    dossierService.candidateDossierToSuppression(session, dossierDoc);
                    final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
                    builder.append(dossier.getNumeroNor());
                    builder.append(System.getProperty("line.separator"));
                    compteurDossier = compteurDossier + 1;
                    if (compteurDossier.equals(50)) {
                        // Remise à zéro du compteur et commit pour éviter des problèmes de rollback
                        compteurDossier = 0;
                        TransactionHelper.commitOrRollbackTransaction();
                        TransactionHelper.startTransaction();
                    }
                }
                session.save();

                // mailService.sendMail(list.size() + " dossiers sont en attente d'archivage (fin de DUA atteinte)",
                // "[Réponses] Dossiers en attente d'archivage", "mailSession",
                // new Address[]{new InternetAddress(Framework.getProperty("reponses.mail.admin.fonctionnel"))});

                final List<String> emailsList = new ArrayList<String>();

                for (final STUser user : users) {
                    final String email = user.getEmail();
                    if (email != null) {
                        emailsList.add(email);
                    }
                }

                if (emailsList.isEmpty()) {
                    LOGGER.warn(session, STLogEnumImpl.FAIL_GET_PARAM_TEC);
                } else {
                    final Address[] adressList = new InternetAddress[emailsList.size()];
                    int index = 0;
                    for (final String email : emailsList) {
                        adressList[index] = new InternetAddress(email);
                        index = index + 1;
                    }

                    final String objet = paramService.getParametreValue(
                        session,
                        STParametreConstant.OBJET_MAIL_DOSSIER_ELIMINATION
                    );

                    String text = paramService.getParametreValue(
                        session,
                        STParametreConstant.TEXT_MAIL_DOSSIER_ELIMINATION
                    );
                    final String delai = paramService.getParametreValue(
                        session,
                        STParametreConstant.DELAI_ELIMINATION_DOSSIERS
                    );
                    final String delaiUnit = paramService
                        .getParametre(session, STParametreConstant.DELAI_ELIMINATION_DOSSIERS)
                        .getUnit();
                    text = text.replace("${DELAI}", delai + " " + delaiUnit);
                    final String emailMessage = text + System.getProperty("line.separator") + builder.toString();
                    STServiceLocator.getSTMailService().sendMail(emailMessage, objet, "mailSession", adressList);
                }
            }
        } catch (final Exception exc) {
            LOGGER.error(session, STLogEnumImpl.FAIL_SEND_MAIL_TEC, exc);
            errorCount++;
        }
        final long endTime = Calendar.getInstance().getTimeInMillis();
        try {
            STServiceLocator
                .getSuiviBatchService()
                .createBatchResultFor(
                    batchLoggerModel,
                    "Dossiers candidats élimination",
                    nbEliminationCandidat,
                    endTime - startTime
                );
        } catch (Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
        }
        LOGGER.info(session, EpgLogEnumImpl.END_B_ELIMINATION_TEC);
    }
}
