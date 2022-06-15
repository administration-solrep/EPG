package fr.dila.solonepg.elastic.models.config;

import static org.assertj.core.api.Assertions.assertThat;

import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.solonepg.elastic.models.search.enums.QueryTypeEnum;
import fr.dila.solonepg.elastic.models.search.enums.TypeEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class QueryBuildersPropertiesConfigurationTest {
    private QueryBuildersPropertiesConfiguration configuration;

    @Before
    public void setUp() {
        configuration = QueryBuildersPropertiesConfiguration.getInstance();
    }

    @Test
    public void testGetProperty() {
        String field1 = ElasticDossier.CONSETAT_DATE_AG_CE;
        QueryBuilderProperty property1 = new QueryBuilderProperty();
        property1.setField(field1);
        property1.setLabel("date prévisionnelle AG CE");
        property1.setNestedPath("");
        property1.setType(TypeEnum.DATE);
        property1.setQueryType(QueryTypeEnum.DATE);
        property1.setReturnable(true);
        property1.setSortable(true);

        assertThat(configuration.getProperty(field1)).isEqualToComparingFieldByField(property1);

        String field2 = ElasticDossier.TP_TEXTE_APUBLIER;
        QueryBuilderProperty property2 = new QueryBuilderProperty();
        property2.setField(field2);
        property2.setLabel("Référence : texte à publier");
        property2.setNestedPath("");
        property2.setType(TypeEnum.KEYWORD);
        property2.setQueryType(QueryTypeEnum.BOOL);
        property2.setReturnable(false);
        property2.setSortable(true);

        assertThat(configuration.getProperty(field2)).isEqualToComparingFieldByField(property2);
    }

    @Test
    public void testGetPropertyIsNull() {
        assertThat(configuration.getProperty(ElasticDossier.UID)).isNull();
    }
}
