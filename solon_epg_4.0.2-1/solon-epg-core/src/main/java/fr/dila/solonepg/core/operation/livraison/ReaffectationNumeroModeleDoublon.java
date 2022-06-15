package fr.dila.solonepg.core.operation.livraison;

import fr.dila.cm.cases.CaseConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.operation.STVersion;
import fr.dila.st.core.service.AbstractPersistenceDefaultComponent;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import java.util.List;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.uidgen.UIDGeneratorService;
import org.nuxeo.ecm.core.uidgen.UIDSequencer;

@STVersion(version = "4.0.0")
@Operation(
    id = ReaffectationNumeroModeleDoublon.ID,
    category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY,
    label = "Réaffectation des numéros de modèles en doublon",
    description = "Correction du ticket M135296 - réaffectation des numéros de modèles en doublon"
)
public class ReaffectationNumeroModeleDoublon extends AbstractPersistenceDefaultComponent {
    public static final String ID = "SolonEpg.Reafectation.Numero.Modele";

    private static final STLogger LOG = STLogFactory.getLog(ReaffectationNumeroModeleDoublon.class);

    private static final String QUERY =
        "SELECT MIN(fr.ID) " +
        "FROM FEUILLE_ROUTE fr " +
        "JOIN HIERARCHY h ON h.ID = fr.ID " +
        "WHERE h.PARENTID = '3db97cf1-25ec-4438-9e03-4184f0351b80' " +
        "AND NUMERO IN (" +
        "SELECT NUMERO " +
        "FROM (SELECT NUMERO, COUNT(*) AS doublon " +
        "FROM FEUILLE_ROUTE fr " +
        "JOIN HIERARCHY h ON h.ID = fr.ID " +
        "WHERE h.PARENTID = '3db97cf1-25ec-4438-9e03-4184f0351b80' " +
        "GROUP BY NUMERO) " +
        "WHERE doublon > 1) " +
        "GROUP BY fr.NUMERO " +
        "ORDER BY NUMERO";

    @Context
    private CoreSession session;

    @Context
    private NuxeoPrincipal principal;

    public ReaffectationNumeroModeleDoublon() {}

    @OperationMethod
    public void run() {
        LOG.info(STLogEnumImpl.DEFAULT, "Début de l'opération de réaffectation des numémros de modèles en doublon");

        List<String> idList = getModeleIdDoublon();
        DocumentModelList docList = session.getDocuments(idList.stream().map(IdRef::new).toArray(DocumentRef[]::new));
        docList
            .stream()
            .map(doc -> doc.getAdapter(SolonEpgFeuilleRoute.class))
            .forEach(route -> changeNumeroModel(route));
        session.save();
        LOG.info(STLogEnumImpl.DEFAULT, "Fin de l'opération de réaffectation des numémros de modèles en doublon");
    }

    private void changeNumeroModel(SolonEpgFeuilleRoute route) {
        final UIDGeneratorService uidGeneratorService = ServiceUtil.getRequiredService(UIDGeneratorService.class);
        UIDSequencer sequencer = uidGeneratorService.getSequencer();
        route.setNumero(sequencer.getNextLong("SOLON_EPG_FDR_MODEL") + 1);
        session.saveDocument(route.getDocument());
    }

    @SuppressWarnings("unchecked")
    private List<String> getModeleIdDoublon() {
        return apply(true, entityManager -> entityManager.createNativeQuery(QUERY).getResultList());
    }
}
