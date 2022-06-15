package fr.dila.solon.birt.rest.config;

import fr.dila.solon.birt.SolonBirtGenerator;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    private static final Logger LOGGER = LogManager.getLogger(AppConfig.class);

    @Value("${properties.location}")
    private String propertiesLocation;

    private final ApplicationContext appContext;

    public AppConfig(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    @Bean
    public SolonBirtGenerator solonBirtGenerator() {
        return new SolonBirtGenerator();
    }

    @Bean
    public Properties properties() {
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(propertiesLocation)) {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.error("Erreur : le fichier de configuration est absent ou corrompu", e);
            System.exit(SpringApplication.exit(appContext, () -> SolonBirtGenerator.CODE_ERROR_CONFIG_FILE));
        }
        return properties;
    }
}
