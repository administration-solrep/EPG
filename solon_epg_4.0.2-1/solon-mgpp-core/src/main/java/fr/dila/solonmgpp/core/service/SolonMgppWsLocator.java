package fr.dila.solonmgpp.core.service;

import fr.dila.solonepp.rest.api.WSEpp;
import fr.dila.solonepp.rest.api.WSEvenement;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.st.rest.client.WSProxyFactory;
import fr.dila.st.rest.client.WSProxyFactoryException;
import org.nuxeo.ecm.core.api.CoreSession;

public class SolonMgppWsLocator {

    private SolonMgppWsLocator() {
        // private constructor
    }

    private static WSProxyFactory getParameters(CoreSession session) {
        ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator
            .getParametreMgppService()
            .findParametrageMgpp(session);
        return new WSProxyFactory(parametrageMgpp.getUrlEpp(), null, parametrageMgpp.getLoginEpp(), null);
    }

    /**
     *
     * @return le service web EPP
     * @throws WSProxyFactoryException
     */
    public static WSEpp getWSEpp(CoreSession session) throws WSProxyFactoryException {
        WSProxyFactory proxyFactory = getParameters(session);
        return proxyFactory.getService(
            WSEpp.class,
            SolonMgppServiceLocator.getParametreMgppService().findParametrageMgpp(session).getPassEpp()
        );
    }

    /**
     *
     * @return le service web Evenement de EPP
     * @throws WSProxyFactoryException
     */
    public static WSEvenement getWSEvenement(CoreSession session) throws WSProxyFactoryException {
        WSProxyFactory proxyFactory = getParameters(session);
        return proxyFactory.getService(
            WSEvenement.class,
            SolonMgppServiceLocator.getParametreMgppService().findParametrageMgpp(session).getPassEpp()
        );
    }

    public static String getConnexionFailureMessage(CoreSession session) {
        return (
            "Connexion a SOLON EPP impossible (url : " +
            SolonMgppWsLocator.getUrl(session) +
            "), vérifier le paramétrage MGPP."
        );
    }

    /**
     *
     * @return l'url de connexion a SolonEPP
     */
    private static String getUrl(CoreSession session) {
        ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator
            .getParametreMgppService()
            .findParametrageMgpp(session);
        return parametrageMgpp.getUrlEpp();
    }
}
