package fr.dila.solonepg.ui.enums;

import static org.junit.Assert.assertEquals;

import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class EpgPageProvidersTest {

    @Test
    public void someColumnsShouldBeAdditional() {
        EpgPageProviders[] providersWithDatePublicationSouhaitee = new EpgPageProviders[] {
            EpgPageProviders.PROVIDER_CORBEILLE_DOSSIERS_EN_CREATION, // SFD§4.2.2.2
            EpgPageProviders.PROVIDER_CORBEILLE_POSTE, // SFD§4.3.3.3
            EpgPageProviders.PROVIDER_CORBEILLE_TYPE_ACTE, // SFD§4.3.3.3
            EpgPageProviders.PROVIDER_CORBEILLE_TYPE_ETAPE // SFD§4.3.3.3
        };

        Stream
            .of(EpgPageProviders.values())
            .forEach(
                provider -> {
                    boolean expectedToHaveCol = ArrayUtils.contains(providersWithDatePublicationSouhaitee, provider);
                    boolean actuallyHasCol = provider
                        .getAdditionalColumns()
                        .contains(EpgColumnEnum.DATE_PUBLICATION_SOUHAITEE.getNxPropName());

                    assertEquals(expectedToHaveCol, actuallyHasCol);
                }
            );
    }
}
