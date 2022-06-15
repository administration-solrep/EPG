package fr.dila.solonepg.ui.jaxrs.webobject;

import fr.dila.st.ui.jaxrs.webobject.SolonWebObject;
import fr.sword.naiad.nuxeo.commons.core.util.SessionUtil;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.nuxeo.ecm.core.api.CloseableCoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.runtime.api.Framework;

public abstract class AbstractEpgSuiviLibrePage extends SolonWebObject {

    protected void logAndDo(Runnable r) {
        try {
            LoginContext loginContext = Framework.login();
            try (CloseableCoreSession session = SessionUtil.openSession()) {
                context.setSession(session);
                r.run();
                loginContext.logout();
            }
        } catch (LoginException e) {
            throw new NuxeoException(e);
        }
    }
}
