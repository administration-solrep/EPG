package fr.dila.solonmgpp.core.operation;

import static fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil.getRequiredService;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.service.ModeleCourrierService;
import fr.dila.solonmgpp.core.domain.ParametrageMgppImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.runtime.api.Framework;

/**
 * Une opération pour injecter des données métiers dans la BDD et le repository,
 * on produit ainsi des dossiers et autres données sans passer par les
 * Webservices.
 *
 * @author fskaff
 */
@Operation(
    id = MgppDataInjectionOperation.ID,
    category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY,
    label = "CreationJeuDonnees",
    description = "Injecte des données dans la base de données"
)
public class MgppDataInjectionOperation {
    private static final Log LOGGER = LogFactory.getLog(MgppDataInjectionOperation.class);

    private static final String PASSWORD = "Solon2NG";

    private static final String URL_EPP = Framework.getProperty(
        "mgpp.param.epp.url",
        "http://localhost:8080/solon-epp/site/solonepp"
    );

    private static final String USER_WS_GOUVERNEMENT = "ws-gouvernement";

    public static final String MODELE_COURRIER_FILE_RESOURCE_NAME = "docs/MonModeleCourrier.docx";

    /**
     * Identifiant technique de l'opération.
     */
    public static final String ID = "MGPP.Injection.Donnees";

    @Context
    protected CoreSession session;

    @OperationMethod
    public void run() {
        if (checkNotRun() && (Framework.isDevModeSet() || Framework.isTestModeSet())) {
            LOGGER.info("Début de l'injection de données.");

            injectParametrageMgpp();
            createModeleCourrierRoot();
            injectModelesCourrier();

            LOGGER.info("Fin de l'injection de données.");
        }
    }

    public void run(CoreSession session) {
        this.session = session;
        run();
    }

    /**
     * Vérification : l'opération ne doit jamais avoir été exécutée. En particulier
     * le paramétrage MGPP ne doit pas avoir été injecté.
     *
     * @return true si on ne trouve pas de paramétrage MGPP.
     */
    private boolean checkNotRun() {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(ParametrageMgppImpl.DOC_TYPE);
        queryBuilder.append(" as d ");

        return QueryUtils
            .doUFNXQLQueryAndFetchForDocuments(session, ParametrageMgppImpl.DOC_TYPE, queryBuilder.toString(), null)
            .isEmpty();
    }

    private void injectParametrageMgpp() {
        DocumentModel paramMgppDoc = session.createDocumentModel(ParametrageMgppImpl.DOC_TYPE);
        paramMgppDoc.setPathInfo(ParametrageMgppImpl.PATH, "ParametrageMgpp");
        ParametrageMgpp paramMgpp = paramMgppDoc.getAdapter(ParametrageMgpp.class);
        paramMgpp.setUrlEpp(URL_EPP);
        paramMgpp.setLoginEpp(USER_WS_GOUVERNEMENT);
        paramMgpp.setPassEpp(PASSWORD);
        session.createDocument(paramMgppDoc);
    }

    private void createModeleCourrierRoot() {
        AutomationService automationService = getRequiredService(AutomationService.class);
        OperationContext opCtx = new OperationContext(session);
        try {
            automationService.run(opCtx, CreateModeleCourrierRootOperation.ID);
        } catch (OperationException e) {
            throw new NuxeoException(e);
        }
    }

    private void injectModelesCourrier() {
        ModeleCourrierService mcService = SolonMgppServiceLocator.getModeleCourrierService();
        URL modeleURL = Framework.getResourceLoader().getResource(MODELE_COURRIER_FILE_RESOURCE_NAME);
        createModele(mcService, session, "Modèle LEX-01", modeleURL, Collections.singletonList("EVT01"));
        createModele(mcService, session, "Modèle CENS-01", modeleURL, Collections.singletonList("EVT17"));
    }

    private void createModele(
        ModeleCourrierService mcService,
        CoreSession session,
        String modeleName,
        URL modeleURL,
        List<String> typesComms
    ) {
        try (InputStream in = modeleURL.openStream()) {
            mcService.createModeleCourrier(session, modeleName, in, modeleName, typesComms);
        } catch (IOException e) {
            throw new NuxeoException(e);
        }
    }
}
