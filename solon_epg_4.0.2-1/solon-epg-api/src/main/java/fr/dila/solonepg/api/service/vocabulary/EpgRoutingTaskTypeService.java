package fr.dila.solonepg.api.service.vocabulary;

import fr.dila.ss.api.service.vocabulary.RoutingTaskTypeService;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;

public interface EpgRoutingTaskTypeService extends RoutingTaskTypeService {
    List<ImmutablePair<Integer, String>> getFilteredEntries(NuxeoPrincipal principal, String typeActe);
}
