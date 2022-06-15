package fr.dila.solonmgpp.core.event;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.service.DossierService;
import fr.dila.solonmgpp.core.SolonMgppFeature;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import javax.inject.Inject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventBundle;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.impl.EventBundleImpl;
import org.nuxeo.ecm.core.event.impl.EventContextImpl;
import org.nuxeo.ecm.core.event.impl.EventImpl;
import org.nuxeo.ecm.core.test.CoreFeature;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features({ SolonMgppFeature.class })
public class TestUpdateFichePresentationAfterUpdateBordereauListener {
    @Inject
    protected CoreSession session;

    @Inject
    private CoreFeature coreFeature;

    @Test
    public void testUpdateFicheLoi() {
        DocumentModel docModel = session.createDocumentModel(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE);
        docModel.setPathInfo("/", "newDossierTest");
        docModel = session.createDocument(docModel);
        Dossier dossier = docModel.getAdapter(Dossier.class);
        String oldNor = "AAAB00000001C";
        dossier.setNumeroNor(oldNor);
        dossier.setIdDossier(oldNor);
        dossier.setTypeActe(TypeActeConstants.TYPE_ACTE_LOI);

        final DossierService dossierService = SolonMgppServiceLocator.getDossierService();
        FicheLoi fiche = dossierService.findOrCreateFicheLoi(session, dossier.getIdDossier());
        fiche.setNumeroNor(oldNor);
        DocumentModel ficheLoiDoc = session.createDocument(fiche.getDocument());
        session.saveDocument(ficheLoiDoc);
        session.save();

        String newNor = "XXXX0123456Z";
        dossier.setNumeroNor(newNor);
        session.saveDocument(docModel);
        session.save();

        DocumentModel docOldNor = dossierService.findFicheLoiDocumentFromNor(session, oldNor);
        Assert.assertNotNull(docOldNor);
        FicheLoi oldFiche = docOldNor.getAdapter(FicheLoi.class);
        Assert.assertEquals(oldNor, oldFiche.getNumeroNor());

        EventContext eventContext = new EventContextImpl();
        eventContext.setCoreSession(session);
        eventContext.setProperty(SolonEpgEventConstant.DOSSIER_EVENT_PARAM, docModel);
        eventContext.setProperty(
            SolonEpgEventConstant.TYPE_ACTE_EVENT_PARAM,
            SolonEpgEventConstant.TYPE_ACTE_FL_EVENT_VALUE
        );
        eventContext.setProperty(SolonEpgEventConstant.NOR_PRE_REATTRIBUTION_VALUE, oldNor);

        UpdateFichePresentationAfterUpdateBordereauListener listener = new UpdateFichePresentationAfterUpdateBordereauListener();

        Event event = new EventImpl(
            SolonEpgEventConstant.BATCH_EVENT_UPDATE_FICHE_AFTER_UPDATE_BORDEREAU,
            eventContext
        );
        EventBundle events = new EventBundleImpl();
        events.push(event);

        listener.handleEvent(events);
        coreFeature.waitForAsyncCompletion();

        docOldNor = dossierService.findFicheLoiDocumentFromNor(session, oldNor);
        Assert.assertNull(docOldNor);

        DocumentModel docNewNor = dossierService.findFicheLoiDocumentFromNor(session, newNor);
        Assert.assertNotNull(docNewNor);
        FicheLoi newFiche = docNewNor.getAdapter(FicheLoi.class);
        Assert.assertEquals(newNor, newFiche.getNumeroNor());
    }
}
