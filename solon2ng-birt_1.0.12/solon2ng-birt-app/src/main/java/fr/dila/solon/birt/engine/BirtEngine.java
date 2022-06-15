package fr.dila.solon.birt.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;

/**
 * This is a Singleton used to trigger BIRT deployment and get access to the
 * Reporting and Design engine
 *
 * @author Tiry (tdelprat@nuxeo.com)
 *
 */
public class BirtEngine {

	private static IReportEngine reportEngine = null;

    private static IDesignEngine birtDesignEngine = null;

    
    private static final Logger LOGGER = LogManager.getLogger(BirtEngine.class);

	private BirtEngine() {
		// Default constructor
	}

    private static void initLogConfig(EngineConfig config) {
    	org.apache.logging.log4j.core.Logger coreLogger = (org.apache.logging.log4j.core.Logger) LOGGER;
    	
    	String folder = null;
        List<Appender> appenders = new ArrayList<>(coreLogger.getAppenders().values());
        if(!appenders.isEmpty()) {
			Appender appender = appenders.get(0);
        	if(appender instanceof FileAppender) {
        		String filename = ((FileAppender) appender).getFileName();
        		int pos = filename.lastIndexOf('/');
				if (pos > -1) {
					folder = filename.substring(0, pos+1) + "birt";
				}
        	}
        }
        
		java.util.logging.Level level = java.util.logging.Level.WARNING;
		String loggerLevel = LOGGER.getLevel().name();
		try {
			java.util.logging.Level.parse(loggerLevel);
		} catch(IllegalArgumentException e) {
			LOGGER.debug(String.format("Unknown logging level : %s", loggerLevel), e);
		}

        config.setLogConfig(folder, level);
    }
    
    public static synchronized IReportEngine getBirtEngine(final String birtHome) {
		if (reportEngine == null) {
            EngineConfig config = new EngineConfig();

            try {
            	initLogConfig(config);
            } catch (Exception e) {
            	// An exception in the log4j2 configuration should not block birt execution
            	LOGGER.warn(e);
            }
            
            config.setEngineHome("");
            config.setBIRTHome(birtHome);

            IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			reportEngine = factory.createReportEngine(config);

            DesignConfig dconfig = new DesignConfig();
            IDesignEngineFactory df = (IDesignEngineFactory) Platform.createFactoryObject(IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY);
            birtDesignEngine = df.createDesignEngine(dconfig);
        }
		return reportEngine;
    }

    public static synchronized IDesignEngine getBirtDesignEngine(final String birtHome) {
        if (birtDesignEngine == null) {
            getBirtEngine(birtHome);
        }
        return birtDesignEngine;
    }
}
