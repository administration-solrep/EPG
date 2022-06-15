package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.core.feuilleroute.SolonEpgRouteStepImpl;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.st.core.adapter.STDocumentAdapterFactory;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.eltrunner.StepElementRunner;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public class RouteStepAdapterFactory implements STDocumentAdapterFactory {
    public static final List<String> authorizedDocumentsType = Collections.unmodifiableList(
        Arrays.asList(SSConstant.ROUTE_STEP_DOCUMENT_TYPE, SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE)
    );

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        checkDocumentTypes(doc, authorizedDocumentsType);
        return new SolonEpgRouteStepImpl(doc, new StepElementRunner());
    }
}
