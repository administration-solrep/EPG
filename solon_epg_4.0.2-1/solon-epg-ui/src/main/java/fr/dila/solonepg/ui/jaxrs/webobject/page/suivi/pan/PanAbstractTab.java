package fr.dila.solonepg.ui.jaxrs.webobject.page.suivi.pan;

import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.model.exceptions.WebSecurityException;

public abstract class PanAbstractTab extends SolonWebObject {
    public static final String CONTEXTUAL_BLOCK_TEMPLATE = "fragments/pan/panContextualBlock";

    protected static void verifyAnyAccess(CoreSession session, ActiviteNormativeEnum currentSection) {
        verifyAccess(session, currentSection, true, true);
    }

    protected static void verifyMinisterOrUpdaterAccess(CoreSession session, ActiviteNormativeEnum currentSection) {
        verifyAccess(session, currentSection, false, true);
    }

    protected static void verifyUpdaterOnlyAccess(CoreSession session, ActiviteNormativeEnum currentSection) {
        verifyAccess(session, currentSection, false, false);
    }

    private static void verifyAccess(
        CoreSession session,
        ActiviteNormativeEnum currentSection,
        boolean readerProfileHasAccess,
        boolean ministerielProfileHasAccess
    ) {
        if (currentSection == null) {
            currentSection = ActiviteNormativeEnum.APPLICATION_DES_LOIS;
        }
        List<String> accessFunctionGroupNames = currentSection.getRightsForProfile(
            readerProfileHasAccess,
            true,
            ministerielProfileHasAccess
        );
        boolean granted = accessFunctionGroupNames.stream().anyMatch(s -> session.getPrincipal().isMemberOf(s));
        if (!granted) {
            throw new WebSecurityException(
                "Not allowed to access PAN tab " +
                currentSection.getLabel() +
                "." +
                " Missing one right of " +
                String.join(", ", accessFunctionGroupNames)
            );
        }
    }
}
