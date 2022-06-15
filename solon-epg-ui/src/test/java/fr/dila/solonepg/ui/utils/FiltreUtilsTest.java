package fr.dila.solonepg.ui.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.Test;
import org.nuxeo.ecm.core.api.SortInfo;

public class FiltreUtilsTest {

    @Test
    public void testParseSortInfoShouldBeOkWithSingleSortInfo() {
        String given = "[{\"sortColumn\":\"d.dos:numeroNor\",\"sortAscending\":true}]";

        List<SortInfo> actual = FiltreUtils.parseSortInfo(given);

        assertThat(actual).containsExactly(new SortInfo("d.dos:numeroNor", true));
    }
}
