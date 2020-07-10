package fr.dila.solonmgpp.core.service;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepp.rest.api.WSEpp;
import fr.dila.solonepp.rest.api.WSEvenement;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.st.rest.client.WSProxyFactory;
import fr.dila.st.rest.client.WSProxyFactoryException;

public class SolonMgppWsLocator {

    private SolonMgppWsLocator() {
        // private constructor
    }

    private static WSProxyFactory getParameters(CoreSession session) throws ClientException {
        ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator.getParametreMgppService().findParametrageMgpp(session);
        return new WSProxyFactory(parametrageMgpp.getUrlEpp(), null, parametrageMgpp.getLoginEpp(), parametrageMgpp.getPassEpp(), null);
    }

    /**
     * 
     * @return le service web EPP
     * @throws WSProxyFactoryException
     * @throws ClientException
     */
    public static WSEpp getWSEpp(CoreSession session) throws WSProxyFactoryException, ClientException {
        WSProxyFactory proxyFactory = getParameters(session);
        return proxyFactory.getService(WSEpp.class);
    }

    /**
     * 
     * @return le service web Evenement de EPP
     * @throws WSProxyFactoryException
     * @throws ClientException
     */
    public static WSEvenement getWSEvenement(CoreSession session) throws WSProxyFactoryException, ClientException {
        WSProxyFactory proxyFactory = getParameters(session);
        return proxyFactory.getService(WSEvenement.class);
    }

    public static String getConnexionFailureMessage(CoreSession session) throws ClientException {
        return "Connexion a SOLON EPP impossible (url : " + SolonMgppWsLocator.getUrl(session) + "), vérifier le paramétrage MGPP.";
    }

    /**
     * 
     * @return l'url de connexion a SolonEPP
     * @throws ClientException
     */
    private static String getUrl(CoreSession session) throws ClientException {
        ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator.getParametreMgppService().findParametrageMgpp(session);
        return parametrageMgpp.getUrlEpp();
    }

}
