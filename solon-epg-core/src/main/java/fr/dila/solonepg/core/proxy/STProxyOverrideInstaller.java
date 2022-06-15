package fr.dila.solonepg.core.proxy;

import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.rest.client.proxy.STProxyOverride;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.Arrays;
import java.util.List;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;

public class STProxyOverrideInstaller extends DefaultComponent {
    private STProxyOverride stProxyOverride;

    private static final STLogger LOGGER = STLogFactory.getLog(STProxyOverrideInstaller.class);

    @Override
    public void activate(ComponentContext context) {
        // solon.web.service.eurlex.proxy.hostnames
        // solon.web.service.eurlex.proxy.proxyHost
        // solon.web.service.eurlex.proxy.proxyPort
        // solon.web.service.eurlex.proxy.protocol (default HTTP ; SOCKS ou DIRECT possible)
        ConfigService configService = STServiceLocator.getConfigService();
        String hostnames = configService.getValue(SolonEpgConfigConstant.EURLEX_WEBSERVICE_HOSTNAMES);
        String proxyHost = configService.getValue(SolonEpgConfigConstant.EURLEX_WEBSERVICE_PROXY_HOST);
        String proxyPort = configService.getValue(SolonEpgConfigConstant.EURLEX_WEBSERVICE_PROXY_PORT);
        String protocol = configService.getValue(SolonEpgConfigConstant.EURLEX_WEBSERVICE_PROTOCOL);
        if (!hostnames.isEmpty()) {
            List<String> hostnameList = Arrays.asList(hostnames.split(","));
            int proxyPortInt = Integer.parseInt(proxyPort);
            Proxy proxy = new Proxy(Type.valueOf(protocol), new InetSocketAddress(proxyHost, proxyPortInt));
            stProxyOverride = new STProxyOverride(hostnameList, proxy);
            stProxyOverride.replaceProxySelector();
            LOGGER.info(
                STLogEnumImpl.LOG_INFO_TEC,
                "Configuration du proxy custom pour EUR-Lex: Hostnames='" +
                hostnames +
                "', ProxyHost='" +
                proxyHost +
                "', ProxyPort='" +
                proxyPort +
                "', Protocol='" +
                protocol +
                "'"
            );
        } else {
            LOGGER.info(STLogEnumImpl.LOG_INFO_TEC, "Proxy custom pour EUR-Lex non activé");
        }
    }

    @Override
    public void deactivate(ComponentContext context) {
        if (stProxyOverride != null) {
            stProxyOverride.resetProxySelector();
        }
    }
}
