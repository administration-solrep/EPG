package fr.dila.solonepg.core.cases;

import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import fr.dila.solonepg.core.test.PropertiesPrinter;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 *
 * Permet de récupérer facilement la liste des propriétés sur lesquelles est basé un adapter.
 * Pré-requis : L'adapter doit utiliser PropertyUtils pour ses getters
 *
 * @author jgomez
 *
 */
public class TestAdapterContentPrinter extends AbstractEPGTest {
    private static final Log LOGGER = LogFactory.getLog(TestAdapterContentPrinter.class);

    @Inject
    private PropertiesPrinter printer;

    @Test
    public void testPrintPropertiesMesureApplicative() throws Exception {
        DocumentModel doc = createDocument(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE, "mesure");
        Assert.assertNotNull(doc);
        LOGGER.info("Affichage des propriétés des MesuresApplicative");
        MesureApplicativePropsPrinter impl = new MesureApplicativePropsPrinter(doc);
        printer.print(impl, fr.dila.solonepg.api.cases.MesureApplicative.class);
        LOGGER.info("Fin d'affichage des propriétés des MesuresApplicative");
    }

    @Test
    public void testPrintPropertiesDecretImpl() throws Exception {
        DocumentModel doc = createDocument(ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE, "decret");
        Assert.assertNotNull(doc);
        LOGGER.info("Affichage des propriétés des Decrets");
        DecretPropsPrinter impl = new DecretPropsPrinter(doc);
        printer.print(impl, fr.dila.solonepg.api.cases.Decret.class);
        LOGGER.info("Fin d'affichage des propriétés des Decrets");
    }

    /**
     * Implémentation qui permet d'afficher les propriétés de MesureApplicative
     * @author jgomez
     *
     */
    class MesureApplicativePropsPrinter extends MesureApplicativeImpl {

        public MesureApplicativePropsPrinter(DocumentModel doc) {
            super(doc);
        }

        private static final long serialVersionUID = 1213L;

        private void print(String type, String schema, String value) {
            LOGGER.info("Prop : " + schema + ":" + value);
        }

        protected Object getProperty(String schema, String value) {
            print("default", schema, value);
            return document.getProperty(schema, value);
        }

        protected Long getLongProperty(String schema, String value) {
            print("long", schema, value);
            return super.getLongProperty(schema, value);
        }

        protected Calendar getDateProperty(String schema, String value) {
            print("date", schema, value);
            return super.getDateProperty(schema, value);
        }

        protected String getStringProperty(String schema, String value) {
            print("string", schema, value);
            return super.getStringProperty(schema, value);
        }

        protected List<String> getListStringProperty(String schema, String value) {
            print("stringlist", schema, value);
            return super.getListStringProperty(schema, value);
        }

        protected Boolean getBooleanProperty(String schema, String value) {
            print("boolean", schema, value);
            return super.getBooleanProperty(schema, value);
        }
    }

    /**
     * Implémentation qui permet d'afficher les propriétés de Decret
     * @author jgomez
     *
     */
    class DecretPropsPrinter extends DecretImpl {

        public DecretPropsPrinter(DocumentModel doc) {
            super(doc);
        }

        private static final long serialVersionUID = 12143L;

        private void print(String type, String schema, String value) {
            LOGGER.info("Prop : " + schema + ":" + value);
        }

        protected Object getProperty(String schema, String value) {
            print("default", schema, value);
            return document.getProperty(schema, value);
        }

        protected Long getLongProperty(String schema, String value) {
            print("long", schema, value);
            return super.getLongProperty(schema, value);
        }

        protected Calendar getDateProperty(String schema, String value) {
            print("date", schema, value);
            return super.getDateProperty(schema, value);
        }

        protected String getStringProperty(String schema, String value) {
            print("string", schema, value);
            return super.getStringProperty(schema, value);
        }

        protected List<String> getListStringProperty(String schema, String value) {
            print("stringlist", schema, value);
            return super.getListStringProperty(schema, value);
        }

        protected Boolean getBooleanProperty(String schema, String value) {
            print("boolean", schema, value);
            return super.getBooleanProperty(schema, value);
        }
    }
}
