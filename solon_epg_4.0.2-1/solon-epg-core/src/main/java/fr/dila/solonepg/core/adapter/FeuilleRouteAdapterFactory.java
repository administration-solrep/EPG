package fr.dila.solonepg.core.adapter;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.core.feuilleroute.SolonEpgFeuilleRouteImpl;
import fr.dila.ss.api.constant.SSConstant;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.constant.FeuilleRouteExecutionType;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.eltrunner.ParallelRunner;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.eltrunner.SerialRunner;
import fr.sword.idl.naiad.nuxeo.feuilleroute.core.utils.FeuilleRouteStepFolderSchemaUtil;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.adapter.DocumentAdapterFactory;

/**
 * Adaptateur de Document vers FeuilleRoute.
 *
 * @author jtremeaux
 */
public class FeuilleRouteAdapterFactory implements DocumentAdapterFactory {

    @Override
    public Object getAdapter(DocumentModel doc, Class<?> arg1) {
        String type = doc.getType();
        if (SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE.equalsIgnoreCase(type)) {
            FeuilleRouteExecutionType executionType = FeuilleRouteStepFolderSchemaUtil.getExecutionType(doc);
            switch (executionType) {
                case serial:
                    return new SolonEpgFeuilleRouteImpl(doc, new SerialRunner());
                case parallel:
                    return new SolonEpgFeuilleRouteImpl(doc, new ParallelRunner());
                default:
                    return null;
            }
        } else if (SolonEpgConstant.FEUILLE_ROUTE_SQUELETTE_DOCUMENT_TYPE.equalsIgnoreCase(type)) {
            return new SolonEpgFeuilleRouteImpl(doc, new SerialRunner());
        }

        return null;
    }
}
