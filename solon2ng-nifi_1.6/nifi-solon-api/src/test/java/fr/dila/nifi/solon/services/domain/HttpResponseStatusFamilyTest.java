package fr.dila.nifi.solon.services.domain;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;

public class HttpResponseStatusFamilyTest {

    @Test
    public void testInformationFamily() {
        testRangeOfInt(100, 200, HttpResponseStatusFamily.INFORMATION);
    }

    @Test
    public void testSuccessFamily() {
        testRangeOfInt(200, 300, HttpResponseStatusFamily.SUCCESS);
    }

    @Test
    public void testRedirectionFamily() {
        testRangeOfInt(300, 400, HttpResponseStatusFamily.REDIRECTION);
    }

    @Test
    public void testClientErrorFamily() {
        testRangeOfInt(400, 500, HttpResponseStatusFamily.CLIENT_ERROR);
    }

    @Test
    public void testServerErrorFamily() {
        testRangeOfInt(500, 600, HttpResponseStatusFamily.SERVER_ERROR);
    }

    @Test
    public void testUndefinedFamily() {
        testRangeOfInt(-10, 100, HttpResponseStatusFamily.UNDEFINED);
        testRangeOfInt(600, 700, HttpResponseStatusFamily.UNDEFINED);
    }

    private void testRangeOfInt(
        final int startInclusive,
        final int endExclusive,
        HttpResponseStatusFamily expectedFamily
    ) {
        List<HttpResponseStatusFamily> collect = IntStream
            .range(startInclusive, endExclusive)
            .mapToObj(HttpResponseStatusFamily::valueOf)
            .collect(Collectors.toList());
        int expectedCollectionSize = endExclusive - startInclusive;
        Assertions
            .assertThat(collect)
            .hasSize(expectedCollectionSize)
            .hasSameElementsAs(Lists.newArrayList(expectedFamily));
    }
}
