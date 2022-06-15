package fr.dila.solonepg.rest.api;

import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.core.service.STServiceLocator;
import fr.dla.st.rest.api.AbstractJetonWS;
import fr.sword.xsd.commons.SolonEpgVersion;
import fr.sword.xsd.commons.VersionResponse;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;

public abstract class AbstractWSVersion<T> extends AbstractJetonWS {
    protected T delegate;

    protected T getDelegate(CoreSession session) {
        if (delegate == null && session != null) {
            delegate = createDelegate(session);
        }
        return delegate;
    }

    protected abstract T createDelegate(CoreSession session);

    protected VersionResponse version() throws Exception {
        VersionResponse response = new VersionResponse();
        SolonEpgVersion version = new SolonEpgVersion();
        response.setVersionSolonEpg(version);

        String numVersion = STServiceLocator.getVersionService().getVersionApp();

        version.setActes(numVersion);
        version.setAr(numVersion);
        version.setSolonCommons(numVersion);
        version.setWsSolonEpg(numVersion);
        return response;
    }

    protected boolean isPasswordOutdated() {
        CoreSession session = ctx.getCoreSession();
        try {
            if (
                SolonEpgServiceLocator
                    .getProfilUtilisateurService()
                    .isUserPasswordOutdated(session, session.getPrincipal().getName())
            ) {
                STServiceLocator.getSTUserService().forceChangeOutdatedPassword(session.getPrincipal().getName());
                return true;
            }
            return false;
        } catch (NuxeoException e) {
            getLogger().warn("Impossible de vérifier la validité de la date de changement de mot de passe", e);
            return false;
        }
    }

    protected boolean isPasswordTemporary() {
        CoreSession session = ctx.getCoreSession();
        try {
            return STServiceLocator.getSTUserService().isUserPasswordResetNeeded(session.getPrincipal().getName());
        } catch (NuxeoException e) {
            getLogger().warn("Impossible de vérifier si le mot de passe est temporaire", e);
            return false;
        }
    }
}
