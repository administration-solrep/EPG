package fr.dila.solonepg.core.bdj;

import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

public class TestWsBdj {
    private WsBdjImpl wsBdjImpl = new WsBdjImpl();

    @Test
    public void testBuildEcheancierJsonStr() throws IOException {
        String echeancierXml = getFileContent("injectionbdj/echeancier.xml");
        String expectedJson = getFileContent("injectionbdj/echeancier.json");

        String echeancierJson = wsBdjImpl.buildEcheancierJsonStr(echeancierXml);
        Assert.assertEquals(expectedJson, echeancierJson);
    }

    private String getFileContent(String filePath) {
        try {
            return IOUtils.toString(
                getClass().getClassLoader().getResourceAsStream(filePath),
                Charset.defaultCharset()
            );
        } catch (IOException e) {
            Assert.fail("Failed to load file " + filePath);
            return null;
        }
    }
}
