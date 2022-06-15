package fr.dila.solon.birt;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solon.birt.common.RunResult;
import fr.dila.solon.birt.common.SerializationUtils;
import fr.dila.solon.birt.common.SolonBirtParameters;
import fr.dila.solon.birt.report.ReportHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.DocxRenderOption;
import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;

/**
 * Exécutable réalisant la génération de rapport BIRT.
 */
public class SolonBirtGenerator {

    protected static final String BIRT_HOME_DIR_KEY = "solon.birt.home.dir";
    protected static final String BIRT_LOG_DIR_KEY = "solon.birt.log.dir";
    protected static final String BIRT_REPORTS_DIR_KEY = "solon.birt.samples.dir";
    public static final String BIRT_OUTPUT_DIR_KEY = "solon.birt.output.dir";

    private static final String BIRT_MONITOR_KEY = "solon.birt.monitor";

    private static final String MODEL_EXT = "rptdesign";

    private static final Logger LOGGER = LogManager.getLogger(SolonBirtGenerator.class);

    /**
     * Paramètre de commande permettant de vérifier la bonne installation de
     * l'application.
     */
    static final String MODE_VERSION = "version";

    public static final int CODE_OK = 0;
    private static final int CODE_ERROR_APP_CONFIG_FILE = 1;
    protected static final int CODE_ERROR_WRONG_NUMBER_OF_PARAMS = 2;
    public static final int CODE_ERROR_CONFIG_FILE = 3;
    protected static final int CODE_ERROR_JSON_PARAM = 4;
    protected static final int CODE_ERROR_UNKNOWN_REPORT = 5;

    protected Properties properties;

    private MonitoringThread monitoringThread;

    public SolonBirtGenerator() {
        super();
    }

    public static void main(String[] args) {
        SolonBirtGenerator generator = new SolonBirtGenerator();
        System.exit(generator.run(args));
    }

    int run(String[] args) {
        // Paramètre pour test d'installation
        if ((args.length == 1 || args.length == 2) && MODE_VERSION.equals(args[0])) {
            version();
            return CODE_OK;
        }

        // check nb arguments
        if (args.length != 2 || "".equals(args[0]) || "".equals(args[1])) {
            logError(
                "Erreur : erreur dans le nombre d'arguments. Deux arguments sont nécessaires : les informations sur le rapport et le chemin vers le fichier de configuration.",
                null
            );
            return CODE_ERROR_WRONG_NUMBER_OF_PARAMS;
        }

        Properties prop = new Properties();
        try (InputStream inputStream = new FileInputStream(args[1])) {
            prop.load(inputStream);
        } catch (IOException e) {
            logError("Erreur : le fichier de configuration est absent ou corrompu", e);
            return CODE_ERROR_CONFIG_FILE;
        }

        properties = prop;
        boolean monitor = Boolean.parseBoolean(properties.getProperty(BIRT_MONITOR_KEY));
        LOGGER.debug("Monitoring: {}", monitor);

        if (monitor) {
            monitoringThread = new MonitoringThread(500);
            monitoringThread.start();
        }

        SolonBirtParameters solonBirtParameters = null;
        try {
            solonBirtParameters = SerializationUtils.deserialize(args[0]);
        } catch (JsonProcessingException e) {
            logError("Erreur dans la lecture du paramètre : " + e.getMessage(), e);
            if (monitor) {
                monitoringThread.stop();
            }
            return CODE_ERROR_JSON_PARAM;
        }

        String reportName = solonBirtParameters.getReportModelName();
        if (!reportName.endsWith("." + MODEL_EXT)) {
            reportName = reportName + "." + MODEL_EXT;
        }

        // Check existence du fichier rptdesign associé
        String reportsDir = properties.getProperty(BIRT_REPORTS_DIR_KEY);
        File reportFile = new File(reportsDir + File.separator + reportName);
        if (!reportFile.exists()) {
            logError("Erreur : Le rapport " + reportName + " n'existe pas.", null);
            if (monitor) {
                monitoringThread.stop();
            }
            return CODE_ERROR_UNKNOWN_REPORT;
        }

        System.out.println(generateReport(reportFile, solonBirtParameters, null));

        if (monitor) {
            // On continue à monitorer 10 secondes pour s'assurer que l'usage mémoire
            // diminue
            monitoringThread.waitThenStop(10000);
        }

        return CODE_OK;
    }

    public RunResult run(Properties prop, SolonBirtParameters solonBirtParameters, Connection connection) {
        properties = prop;
        boolean monitor = Boolean.parseBoolean(properties.getProperty(BIRT_MONITOR_KEY));
        LOGGER.debug("Monitoring: {}", monitor);

        if (monitor) {
            monitoringThread = new MonitoringThread(500);
            monitoringThread.start();
        }

        String reportName = solonBirtParameters.getReportModelName();
        if (!reportName.endsWith("." + MODEL_EXT)) {
            reportName = reportName + "." + MODEL_EXT;
        }

        // Check existence du fichier rptdesign associé
        String reportsDir = properties.getProperty(BIRT_REPORTS_DIR_KEY);
        File reportFile = new File(reportsDir + File.separator + reportName);
        if (!reportFile.exists()) {
            String msg = "Erreur : Le rapport " + reportName + " n'existe pas.";
            logError(msg, null);
            if (monitor) {
                monitoringThread.stop();
            }
            return new RunResult(CODE_ERROR_UNKNOWN_REPORT, null);
        }

        String generatedFile = generateReport(reportFile, solonBirtParameters, connection);

        if (monitor) {
            // On continue à monitorer 10 secondes pour s'assurer que l'usage mémoire
            // diminue
            monitoringThread.waitThenStop(10000);
        }

        return new RunResult(CODE_OK, generatedFile);
    }

    private void logError(String msg, Exception e) {
        if (e != null) {
            LOGGER.error(msg, e);
        } else {
            LOGGER.error(msg);
        }
        System.err.println(msg);
    }

    private void version() {
        Properties projectProps = new Properties();
        try (InputStream inputStream = this.getClass().getResourceAsStream("/app.properties")) {
            projectProps.load(inputStream);
        } catch (IOException e) {
            logError("Erreur : le fichier de configuration app du projet est absent ou corrompu", e);
            System.exit(CODE_ERROR_APP_CONFIG_FILE);
        }

        System.out.println(projectProps.getProperty("solon.birt.versionmode"));
    }

    public String generateReport(File reportFile, SolonBirtParameters parameters, Connection connection) {
        // Compute name of the report file if it was not given
        DateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String reportFileName = reportFile.getName();
        String reportName =
            reportFileName.substring(0, reportFileName.length() - (MODEL_EXT.length() + 1)) +
            "_" +
            format.format(new Date());

        return generateReport(reportFile, parameters, reportName, connection);
    }

    public String generateReport(
        File reportFile,
        SolonBirtParameters parameters,
        String outputName,
        Connection connection
    ) {
        long start = System.currentTimeMillis();

        IReportEngine engine = null;

        Map<String, ?> scalarParameters = parameters.getScalarParameters();
        BirtOutputFormat outputFormat = parameters.getOutputFormat();

        LOGGER.debug(
            "Report model = {} ; isTracked = {} ; format = {} ; scalar parameters = {}",
            reportFile.getName(),
            parameters.isTrack(),
            outputFormat,
            scalarParameters
        );

        try {
            // Config and platform startup
            EngineConfig config = new EngineConfig();
            String birtHome = properties.getProperty(BIRT_HOME_DIR_KEY);
            config.setBIRTHome(birtHome);
            config.setLogConfig(properties.getProperty(BIRT_LOG_DIR_KEY), Level.INFO);
            Platform.startup(config);

            // Engine creation
            final IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(
                IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY
            );
            engine = factory.createReportEngine(config);

            // Report retrieval
            InputStream is = new FileInputStream(reportFile);
            IReportRunnable design = ReportHelper.getReport(is, birtHome);

            // Task creation
            IRunAndRenderTask task = engine.createRunAndRenderTask(design);

            // Injection des scalar parameters
            task.setParameterValues(scalarParameters);

            // Ajout de la connexion JDBC
            Connection conn = connection;
            if (conn == null) {
                conn = initConnection(parameters);
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> appContext = task.getAppContext();
            appContext.put("OdaJDBCDriverPassInConnection", conn);
            appContext.put("OdaJDBCDriverPassInConnectionCloseAfterUse", false);
            task.setAppContext(appContext);

            RenderOption renderOption = initRenderOption(
                outputFormat,
                outputName,
                parameters.getResultPathName(),
                parameters.isTrack()
            );
            task.setRenderOption(renderOption);

            task.run();
            task.close();

            engine.destroy();

            String outputFileName = renderOption.getOutputFileName();

            long finish = System.currentTimeMillis();

            LOGGER.info("Report generated : {} in {}ms", outputFileName, (finish - start));

            return outputFileName;
        } catch (final Exception e) {
            logError(null, e);
        } finally {
            Platform.shutdown();
        }
        return null;
    }

    protected RenderOption initRenderOption(
        BirtOutputFormat outputFormat,
        String outputName,
        String resultPathName,
        boolean track
    )
        throws IOException {
        RenderOption renderOption = null;

        String outputFileName = null;
        String outputDirPath = null;
        if (resultPathName != null && !resultPathName.isEmpty()) {
            outputFileName = resultPathName;
            outputDirPath = resultPathName.substring(0, resultPathName.lastIndexOf('/'));
        } else if (track) {
            outputDirPath =
                ReportHelper.getOrCreateTempDirectory(
                    properties.getProperty(BIRT_OUTPUT_DIR_KEY),
                    Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()
                );
            outputFileName = outputDirPath + File.separator + outputName + "." + outputFormat.getExtension();
        } else {
            outputDirPath = properties.getProperty(BIRT_OUTPUT_DIR_KEY);
            outputFileName = outputDirPath + File.separator + outputName + "." + outputFormat.getExtension();
        }

        switch (outputFormat) {
            case PDF:
                renderOption = initRenderOptionPdf(outputFormat);
                break;
            case XLSX:
                renderOption = initRenderOptionXls();
                break;
            case DOCX:
                renderOption = initRenderOptionDocx(outputFormat);
                break;
            default:/* HTML format */
                renderOption = initRenderOptionHtml(outputFormat, outputName, outputFileName, outputDirPath);
        }

        renderOption.setOutputFileName(outputFileName);

        return renderOption;
    }

    private RenderOption initRenderOptionPdf(BirtOutputFormat outputFormat) {
        RenderOption renderOption;
        renderOption = new PDFRenderOption();
        renderOption.setOutputFormat(outputFormat.getExtension());
        return renderOption;
    }

    private RenderOption initRenderOptionXls() {
        RenderOption renderOption;
        renderOption = new EXCELRenderOption();
        renderOption.setImageHandler(new HTMLEmbeddedImageHandler());
        renderOption.setOutputFormat("xls");
        return renderOption;
    }

    private RenderOption initRenderOptionDocx(BirtOutputFormat outputFormat) {
        RenderOption renderOption;
        renderOption = new DocxRenderOption();
        renderOption.setImageHandler(new HTMLServerImageHandler());
        renderOption.setOutputFormat(outputFormat.getExtension());
        return renderOption;
    }

    private RenderOption initRenderOptionHtml(
        BirtOutputFormat outputFormat,
        String outputName,
        String outputFileName,
        String outputDirPath
    )
        throws IOException {
        HTMLRenderOption renderOption = new HTMLRenderOption();
        renderOption.setOutputFormat(outputFormat.getExtension());

        String baseImageUrl = "img_" + outputName;

        File imagesDir = new File(outputDirPath, baseImageUrl);
        if (imagesDir.exists()) {
            FileUtils.deleteDirectory(imagesDir);
        }
        imagesDir.mkdir();

        File result = new File(outputFileName);
        OutputStream out = new FileOutputStream(result);
        result.getParentFile().mkdirs();

        renderOption.setImageHandler(new HTMLEmbeddedImageHandler());
        renderOption.setOutputStream(out);
        renderOption.setOutputFileName(outputFileName);
        renderOption.setBaseImageURL(baseImageUrl);
        renderOption.setImageDirectory(imagesDir.getAbsolutePath());
        renderOption.setSupportedImageFormats("GIF");
        renderOption.setEmbeddable(true);

        return renderOption;
    }

    private Connection initConnection(SolonBirtParameters parameters) throws ClassNotFoundException, SQLException {
        Class.forName(parameters.getJdbcDriver());
        return DriverManager.getConnection(
            parameters.getJdbcUrl(),
            parameters.getJdbcUser(),
            parameters.getJdbcPassword()
        );
    }
}
