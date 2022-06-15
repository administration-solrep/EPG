package fr.dila.solon.birt.report;

import com.ibm.icu.util.ULocale;
import fr.dila.solon.birt.SolonBirtGenException;
import fr.dila.solon.birt.engine.BirtEngine;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;

/**
 * Helper class to make Birt API easier to use
 *
 * @author Tiry (tdelprat@nuxeo.com)
 *
 */
public class ReportHelper {

    private static final Logger LOGGER = LogManager.getLogger(ReportHelper.class);

    private ReportHelper() {}

    public static IReportRunnable getReport(InputStream stream, String birtHome) throws SolonBirtGenException {
        IDesignEngine dEngine = BirtEngine.getBirtDesignEngine(birtHome);
        SessionHandle sh = dEngine.newSessionHandle(ULocale.FRENCH);

        IReportRunnable modifiedReport;
        try {
            ReportDesignHandle designHandle = sh.openDesign((String) null, stream);
            modifiedReport = BirtEngine.getBirtEngine(birtHome).openReportDesign(designHandle);
        } catch (DesignFileException | EngineException e) {
            throw new SolonBirtGenException(e);
        }

        try {
            sh.closeAll(false);
        } catch (IOException e) {
            LOGGER.warn(e);
        }

        return modifiedReport;
    }

    public static DateTimeFormatter getSimpleDateFormat() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public static String getOrCreateTempDirectory(String baseDir, LocalDate date) {
        String tmpDir = date.format(getSimpleDateFormat());

        File directory = new File(baseDir + File.separator + tmpDir);
        if (!directory.exists()) {
            directory.mkdir();
        }

        return directory.getAbsolutePath();
    }
}
