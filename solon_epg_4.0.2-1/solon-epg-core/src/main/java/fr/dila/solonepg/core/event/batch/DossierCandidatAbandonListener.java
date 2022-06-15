package fr.dila.solonepg.core.event.batch;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.ProfileService;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.event.AbstractBatchEventListener;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.STMailHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.query.sql.model.DateLiteral;
import org.nuxeo.ecm.platform.usermanager.UserManager;
import org.nuxeo.runtime.transaction.TransactionHelper;

public class DossierCandidatAbandonListener extends AbstractBatchEventListener {
    private static final STLogger LOGGER = STLogFactory.getLog(DossierCandidatAbandonListener.class);
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public DossierCandidatAbandonListener() {
        super(LOGGER, SolonEpgEventConstant.BATCH_EVENT_DOSSIER_CANDIDAT_ABANDON);
    }

    @Override
    protected void processEvent(CoreSession session, Event event) {
        LOGGER.info(session, EpgLogEnumImpl.INIT_B_CANDIDAT_ABANDON_TEC);
        long startTime = Calendar.getInstance().getTimeInMillis();
        try {
            Calendar dua = Calendar.getInstance();
            STParametreService paramService = STServiceLocator.getSTParametreService();
            String objet = paramService.getParametreValue(
                session,
                STParametreConstant.OBJET_MAIL_DOSSIER_CANDIDAT_ABANDONNE
            );
            String text = paramService.getParametreValue(
                session,
                STParametreConstant.TEXT_MAIL_DOSSIER_CANDIDAT_ABANDONNE
            );
            String delai = paramService.getParametreValue(
                session,
                STParametreConstant.DELAI_CANDIDATURE_ABANDON_DOSSIERS
            );
            String delaiUnit = paramService
                .getParametre(session, STParametreConstant.DELAI_CANDIDATURE_ABANDON_DOSSIERS)
                .getUnit();
            String content = text.replace("${DELAI}", delai + " " + delaiUnit);
            String duaDelai = paramService.getParametreValue(
                session,
                STParametreConstant.DELAI_CANDIDATURE_ABANDON_DOSSIERS
            );
            dua.add(Calendar.MONTH, -Integer.parseInt(duaDelai) + 1);
            String literalDate = DateLiteral.dateFormatter.print(dua.getTimeInMillis());

            String query =
                "SELECT * FROM Dossier WHERE ecm:currentLifeCycleState = 'running' and dos:" +
                DossierSolonEpgConstants.DOSSIER_CANDIDAT_PROPERTY +
                " = '" +
                VocabularyConstants.CANDIDAT_AUCUN +
                "' and dos:" +
                DossierSolonEpgConstants.DOSSIER_STATUT_PROPERTY +
                " = '" +
                VocabularyConstants.STATUT_LANCE +
                "' and  dc:modified < TIMESTAMP '" +
                literalDate +
                " ' and dos:" +
                DossierSolonEpgConstants.DOSSIER_ID_DOSSIER_PROPERTY +
                " IS NULL and ecm:isProxy = 0 ";

            DocumentModelList dossierDocList = session.query(query);
            if (CollectionUtils.isNotEmpty(dossierDocList)) {
                Integer compteurDossier = 0;
                List<Dossier> dossierList = new ArrayList<>();
                StringBuilder numerosNor = new StringBuilder();

                for (DocumentModel dossierDoc : dossierDocList) {
                    Dossier dossier = dossierDoc.getAdapter(Dossier.class);
                    dossier.setCandidat(VocabularyConstants.CANDIDAT_ABANDON);
                    dossier.setDateCandidature(Calendar.getInstance());
                    session.saveDocument(dossierDoc);
                    compteurDossier += 1;
                    if (compteurDossier.equals(50)) {
                        compteurDossier = 0;
                        TransactionHelper.commitOrRollbackTransaction();
                        TransactionHelper.startTransaction();
                    }
                    dossierList.add(dossier);
                    numerosNor.append(dossier.getNumeroNor());
                    numerosNor.append(LINE_SEPARATOR);
                }

                session.save();

                STMailService mailService = STServiceLocator.getSTMailService();
                ProfileService profileService = STServiceLocator.getProfileService();

                sendmMailForAdminFonctionnel(
                    session,
                    mailService,
                    profileService,
                    numerosNor.toString(),
                    objet,
                    content
                );

                sendMailForAdminMin(mailService, profileService, dossierList, objet, content);
            }
        } catch (Exception exc) {
            LOGGER.error(session, STLogEnumImpl.FAIL_SEND_MAIL_TEC, exc);
            errorCount++;
        }
        long endTime = Calendar.getInstance().getTimeInMillis();
        try {
            STServiceLocator
                .getSuiviBatchService()
                .createBatchResultFor(
                    batchLoggerModel,
                    "Exécution du batch de candidature d'abandon des dossiers",
                    endTime - startTime
                );
        } catch (Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_CREATE_BATCH_RESULT_TEC, e);
        }
        LOGGER.info(session, EpgLogEnumImpl.END_B_CANDIDAT_ABANDON_TEC);
    }

    private static void sendmMailForAdminFonctionnel(
        CoreSession session,
        STMailService mailService,
        ProfileService profileService,
        String numerosNor,
        String objet,
        String content
    ) {
        List<STUser> users = profileService.getUsersFromBaseFunction(
            STBaseFunctionConstant.ADMIN_FONCTIONNEL_EMAIL_RECEIVER
        );

        List<Address> addresses = STMailHelper.toListAddress(users);
        if (CollectionUtils.isNotEmpty(addresses)) {
            String emailMessage = content + LINE_SEPARATOR + numerosNor;
            mailService.sendMail(emailMessage, objet, "mailSession", addresses.toArray(new Address[0]));
        } else {
            LOGGER.warn(session, STLogEnumImpl.FAIL_GET_PARAM_TEC);
        }
    }

    private static void sendMailForAdminMin(
        STMailService mailService,
        ProfileService profileService,
        List<Dossier> dossierList,
        String objet,
        String content
    )
        throws AddressException {
        List<STUser> adminMins = profileService.getUsersFromBaseFunction(
            SolonEpgBaseFunctionConstant.ADMIN_MINISTERIEL_EMAIL_RECEIVER
        );
        UserManager userManager = STServiceLocator.getUserManager();

        for (STUser adminMin : adminMins) {
            String email = adminMin.getEmail();
            if (StringUtils.isNotBlank(email)) {
                SSPrincipal principal = (SSPrincipal) userManager.getPrincipal(adminMin.getUsername());

                String numerosNor = dossierList
                    .stream()
                    .filter(dossier -> principal.getMinistereIdSet().contains(dossier.getMinistereAttache()))
                    .map(Dossier::getNumeroNor)
                    .collect(Collectors.joining(LINE_SEPARATOR));

                if (StringUtils.isNotBlank(numerosNor)) {
                    String emailMessage = content + LINE_SEPARATOR + numerosNor;
                    InternetAddress[] adminMinAddresses = { new InternetAddress(email) };
                    mailService.sendMail(emailMessage, objet, "mailSession", adminMinAddresses);
                }
            }
        }
    }
}
