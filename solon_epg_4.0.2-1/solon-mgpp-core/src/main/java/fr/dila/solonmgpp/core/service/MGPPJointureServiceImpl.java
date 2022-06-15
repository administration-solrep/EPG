package fr.dila.solonmgpp.core.service;

import fr.dila.solonmgpp.api.service.MGPPJointureService;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.recherche.QueryAssembler;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.jointure.CorrespondenceDescriptor;
import fr.dila.st.core.jointure.DependencyDescriptor;
import fr.dila.st.core.jointure.QueryAssemblerDescriptor;
import fr.dila.st.core.query.ufnxql.UFNXQLQueryAssembler;
import fr.dila.st.core.service.STJointureServiceImpl;
import java.util.ArrayList;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

public class MGPPJointureServiceImpl extends DefaultComponent implements MGPPJointureService {
    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(STJointureServiceImpl.class);

    private static final String CORRESPONDENCES_EP = "correspondences";

    private static final String QUERYASSEMBLER_EP = "queryassembler";

    private static final String DEPENDENCIES_EP = "dependencies";

    protected UFNXQLQueryAssembler queryAssembler;

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        if (QUERYASSEMBLER_EP.equals(extensionPoint)) {
            QueryAssemblerDescriptor desc = (QueryAssemblerDescriptor) contribution;
            UFNXQLQueryAssembler assembler = null;
            try {
                assembler = desc.getKlass().newInstance();
                setQueryAssembler(assembler);
                LOGGER.debug(null, STLogEnumImpl.USE_QUERYASSEMBLER_TEC, "classe = " + assembler.getClass());
            } catch (InstantiationException e) {
                IllegalArgumentException exception = new IllegalArgumentException(
                    "Assembler de classe " + desc.getKlass() + "non trouvé"
                );
                LOGGER.error(null, STLogEnumImpl.FAIL_GET_QUERYASSEMBLER_TEC, exception);
                throw exception;
            } catch (IllegalAccessException e) {
                IllegalArgumentException exception1 = new IllegalArgumentException(
                    "Problème d'accès à l'assembler de classe " + desc.getKlass()
                );
                LOGGER.error(null, STLogEnumImpl.FAIL_GET_QUERYASSEMBLER_TEC, exception1);
                throw exception1;
            }
        }
        if (CORRESPONDENCES_EP.equals(extensionPoint) && queryAssembler != null) {
            CorrespondenceDescriptor correspondence = (CorrespondenceDescriptor) contribution;
            correspondence.setDependencies(new ArrayList<CorrespondenceDescriptor>());
            queryAssembler.add((CorrespondenceDescriptor) contribution);
        }
        if (DEPENDENCIES_EP.equals(extensionPoint) && queryAssembler != null) {
            queryAssembler.build((DependencyDescriptor) contribution);
        }
    }

    public QueryAssembler getQueryAssembler() {
        return queryAssembler;
    }

    public void setQueryAssembler(UFNXQLQueryAssembler assembler) {
        this.queryAssembler = assembler;
    }

    @Override
    public void unregisterContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {}
}
