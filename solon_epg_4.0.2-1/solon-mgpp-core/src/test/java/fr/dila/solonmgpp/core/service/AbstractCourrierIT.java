package fr.dila.solonmgpp.core.service;

import static fr.dila.solonmgpp.core.operation.MgppDataInjectionOperation.MODELE_COURRIER_FILE_RESOURCE_NAME;
import static fr.dila.st.api.constant.STConstant.CASE_MANAGEMENT_ID;
import static fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil.getRequiredService;
import static java.util.Collections.singletonList;

import fr.dila.solonmgpp.api.domain.ModeleCourrier;
import fr.dila.solonmgpp.api.service.ModeleCourrierService;
import fr.dila.solonmgpp.core.operation.CreateModeleCourrierRootOperation;
import fr.dila.st.core.test.STCommonFeature;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.inject.Inject;
import org.junit.Before;
import org.nuxeo.ecm.automation.AutomationService;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.OperationException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.runtime.test.ResourceHelper;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;

@Features(STCommonFeature.class)
@Deploy("org.nuxeo.ecm.platform.types.core")
@Deploy("org.nuxeo.ecm.platform.convert")
@Deploy("org.nuxeo.ecm.platform.rendition.core")
@Deploy("org.nuxeo.template.manager")
@Deploy("org.nuxeo.template.manager.xdocreport")
public class AbstractCourrierIT {
    protected static final URL DOCX_URL = ResourceHelper.getResource(MODELE_COURRIER_FILE_RESOURCE_NAME);

    @Inject
    protected CoreSession session;

    @Inject
    protected ModeleCourrierService mcService;

    @Before
    public void setUp() {
        DocumentModel cm = session.createDocumentModel("/", CASE_MANAGEMENT_ID, "Folder");
        session.createDocument(cm);

        AutomationService automationService = getRequiredService(AutomationService.class);
        OperationContext opCtx = new OperationContext(session);
        try {
            automationService.run(opCtx, CreateModeleCourrierRootOperation.ID);
        } catch (OperationException e) {
            throw new NuxeoException(e);
        }
    }

    protected ModeleCourrier createModele(String title) {
        try (InputStream in = DOCX_URL.openStream()) {
            return mcService.createModeleCourrier(session, title, in, title, singletonList("typeComm"));
        } catch (IOException e) {
            throw new NuxeoException(e);
        }
    }
}
