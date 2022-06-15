package fr.dila.solonepg.api.service;

import java.io.OutputStream;
import java.util.List;
import java.util.function.Consumer;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentRef;

public interface EpgExcelService {
    Consumer<OutputStream> creationExcelListModeleFdr(CoreSession session, List<DocumentRef> idsModeles);
}
