package fr.dila.solon.birt.rest.config;

import static fr.dila.solon.birt.SolonBirtGenerator.BIRT_OUTPUT_DIR_KEY;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BirtTaskScheduler {

    private static final Logger LOGGER = LogManager.getLogger(BirtTaskScheduler.class);

    private final Properties properties;

    public BirtTaskScheduler(Properties properties) {
        this.properties = properties;
    }

    @Scheduled(cron = "${solon.birt.task.cron.purge-report}")
    public void taskPurgeTempReportDirectories() {
        final String tempDir = properties.getProperty(BIRT_OUTPUT_DIR_KEY);

        Instant yesterday = Instant.now().minus(1, ChronoUnit.MINUTES);

        deleteTempDir(tempDir, yesterday);
    }

    /**
     * deletes all the files in a directory modified before a given timestamp
     *
     * @param directoryPath path to delete from
     * @param stamp         everything with a mod date before this timestamp will be deleted
     */
    private void deleteTempDir(String directoryPath, Instant stamp) {
        Predicate<String> directoryFilter = Pattern
            .compile("^\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])$")
            .asPredicate();

        try (Stream<Path> stream = Files.list(Paths.get(directoryPath))) {
            stream
                .filter(name -> directoryFilter.test(name.getFileName().toString()))
                .forEach(path -> deleteDirRecursively(path, stamp));
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    /**
     * Recursively deletion according a timestamp date
     *
     * @param rootPath the root Path
     * @param stamp    the timestamp
     */
    private void deleteDirRecursively(Path rootPath, Instant stamp) {
        try (Stream<Path> stream = Files.walk(rootPath)) {
            stream
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .filter(f -> f.lastModified() <= (stamp.toEpochMilli()))
                .forEach(File::delete);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}
