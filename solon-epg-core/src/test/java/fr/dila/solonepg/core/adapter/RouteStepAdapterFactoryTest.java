package fr.dila.solonepg.core.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.core.feuilleroute.SolonEpgRouteStepImpl;
import fr.dila.ss.api.constant.SSConstant;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;

@RunWith(MockitoJUnitRunner.class)
public class RouteStepAdapterFactoryTest {
    private RouteStepAdapterFactory adapter;

    @Mock
    private DocumentModel doc;

    @Before
    public void setUp() {
        adapter = new RouteStepAdapterFactory();
    }

    @Test
    public void getAdapter() {
        when(doc.getType())
            .thenReturn(SSConstant.ROUTE_STEP_DOCUMENT_TYPE, SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE);

        assertThat(adapter.getAdapter(doc, SolonEpgRouteStep.class))
            .isNotNull()
            .isExactlyInstanceOf(SolonEpgRouteStepImpl.class);
        assertThat(adapter.getAdapter(doc, SolonEpgRouteStep.class))
            .isNotNull()
            .isExactlyInstanceOf(SolonEpgRouteStepImpl.class);
    }

    @Test
    public void getAdapterWithWrongType() {
        String docId = "doc-id";
        when(doc.getId()).thenReturn(docId);

        String docType = SolonEpgParapheurConstants.PARAPHEUR_MODEL_ROOT_DOCUMENT_TYPE;
        when(doc.getType()).thenReturn(docType);

        Throwable throwable = catchThrowable(() -> adapter.getAdapter(doc, SolonEpgRouteStep.class));
        assertThat(throwable)
            .isInstanceOf(NuxeoException.class)
            .hasMessageStartingWith(
                "Le document avec l'id %s doit avoir un de ces types : %s. Type du document : %s",
                docId,
                Arrays.asList(SSConstant.ROUTE_STEP_DOCUMENT_TYPE, SolonEpgConstant.ROUTE_STEP_SQUELETTE_DOCUMENT_TYPE),
                docType
            );
    }
}
