package fr.dila.solonepg.webengine.wsutil;

import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class ResourceResolver implements LSResourceResolver {
    private static final Log LOGGER = LogFactory.getLog(TestWsUtil.class);

    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        // note: in this sample, the XSD's are expected to be in the root of the classpath
        LOGGER.warn("Résolution de la ressource xsd/solon/epg/" + systemId);

        InputStream resourceAsStream =
            this.getClass().getClassLoader().getResourceAsStream("xsd/solon/epg/" + systemId);
        return new Input(publicId, systemId, resourceAsStream);
    }
}
