package fr.dila.solonepg.elastic.models.config;

import fr.dila.solonepg.elastic.models.ElasticException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryBuildersPropertiesConfiguration {
    private static final String FILE_NAME = "Query_builder_properties.csv";
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryBuildersPropertiesConfiguration.class);
    private static final List<QueryBuilderProperty> properties = new ArrayList<>();
    private static final BeanUtilsBean beanUtilsBean = new BeanUtilsBean(
        new ConvertUtilsBean() {

            @Override
            public Object convert(String value, Class clazz) {
                if (clazz.isEnum()) {
                    return Enum.valueOf(clazz, value);
                } else {
                    return super.convert(value, clazz);
                }
            }
        }
    );
    private static QueryBuildersPropertiesConfiguration instance;

    private QueryBuildersPropertiesConfiguration() {
        super();
    }

    public static QueryBuildersPropertiesConfiguration getInstance() {
        synchronized (QueryBuildersPropertiesConfiguration.class) {
            if (instance == null) {
                instance = new QueryBuildersPropertiesConfiguration();
                try {
                    InputStream contentStream = Thread
                        .currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream(FILE_NAME);
                    processData(contentStream);
                } catch (ElasticException e) {
                    LOGGER.error("Erreur de récupération du contenu de " + FILE_NAME, e);
                }
            }
        }
        return instance;
    }

    private static void processData(InputStream inputStream) throws ElasticException {
        // Chargement du CSV
        String csvStr;
        try {
            csvStr = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            LOGGER.error("Erreur de chargement du fichier CSV " + FILE_NAME, e);
            return;
        }

        CSVFormat csvFormat = CSVFormat
            .EXCEL.withHeader()
            .withDelimiter(';')
            .withIgnoreEmptyLines(true)
            .withIgnoreSurroundingSpaces(true);

        try (CSVParser csvRecords = CSVParser.parse(csvStr, csvFormat)) {
            for (CSVRecord csvRecord : csvRecords) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Ajout d'un record : {}", csvRecord);
                }
                processRecord(csvRecord);
            }
        } catch (IOException e) {
            LOGGER.error("Erreur de parsing du fichier CSV : " + FILE_NAME, e);
        }
    }

    private static void processRecord(CSVRecord record) throws ElasticException {
        QueryBuilderProperty property = new QueryBuilderProperty();
        Map<String, String> recordMmap = record.toMap();
        try {
            beanUtilsBean.populate(property, recordMmap);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ElasticException("Erreur lors de la création de la propriété", e);
        }
        properties.add(property);
    }

    /**
     * Récupère la propriété associée au champ ES sélectionné.
     *
     * @param field : le field recherché
     * @return la propriété associée au champs
     */
    public QueryBuilderProperty getProperty(String field) {
        return getProperty(field, null);
    }

    /**
     * Récupère la propriété associée au champ ES sélectionné.
     *
     * @param field : le field recherché
     * @param nestedPath : le chemin nested recherché
     * @return la propriété associée au champs
     */
    public QueryBuilderProperty getProperty(String field, String nestedPath) {
        Predicate<QueryBuilderProperty> isEqualField = prop -> StringUtils.equals(prop.getField(), field);
        Predicate<QueryBuilderProperty> isEmptyNestedPath = prop ->
            StringUtils.isAllEmpty(nestedPath, prop.getNestedPath());
        Predicate<QueryBuilderProperty> isEqualNestedPath = prop ->
            StringUtils.equals(prop.getNestedPath(), nestedPath);

        return properties
            .stream()
            .filter(isEqualField.and(isEmptyNestedPath.or(isEqualNestedPath)))
            .findFirst()
            .orElse(null);
    }
}
