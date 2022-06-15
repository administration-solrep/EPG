package fr.dila.solonepg.ui.services.actions.impl;

import fr.dila.solonepg.api.service.EpgCorbeilleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.services.actions.EpgCorbeilleActionService;
import fr.dila.ss.ui.services.actions.impl.SSCorbeilleActionServiceImpl;
import fr.dila.st.api.caselink.STDossierLink;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.ui.th.model.SpecificContext;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;

public class EpgCorbeilleActionServiceImpl extends SSCorbeilleActionServiceImpl implements EpgCorbeilleActionService {
    private static final STLogger LOGGER = STLogFactory.getLog(EpgCorbeilleActionServiceImpl.class);

    /*
     * Set de l'état de lecture du dossierLink
     */
    public void setReadStateDossierLink(SpecificContext context, Boolean read) {
        CoreSession session = context.getSession();
        STDossierLink dossierLink = getCurrentDossierLink(context);
        if (dossierLink != null) {
            // remet le tag "non lu"
            dossierLink.setReadState(read);
            session.saveDocument(dossierLink.getDocument());
        }
    }

    @Override
    public boolean isEtapePourAvisCE(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        CoreSession session = context.getSession();
        try {
            final EpgCorbeilleService corbeilleService = SolonEpgServiceLocator.getCorbeilleService();
            return corbeilleService.existsPourAvisCEStep(session, dossierDoc.getId());
        } catch (final NuxeoException e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_LINK_TEC, dossierDoc, e);
            return false;
        }
    }
}
