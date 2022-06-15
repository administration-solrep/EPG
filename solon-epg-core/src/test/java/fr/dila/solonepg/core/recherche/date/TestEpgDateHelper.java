package fr.dila.solonepg.core.recherche.date;

import fr.dila.solonepg.core.recherche.date.EpgDateHelper.Operator;
import fr.dila.solonepg.core.recherche.date.EpgDateHelper.Period;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

public class TestEpgDateHelper {

    @Test
    public void testGetDaysOfTheMonth() {
        DateTime start = new DateTime(2004, 12, 01, 0, 0, 0, 0);
        List<String> allDays = EpgDateHelper.getDaysOfTheMonth(start);
        Assert.assertEquals(31, allDays.size());
        Assert.assertEquals("01", allDays.get(0));
    }

    @Test
    public void testGetAllMonths() {
        List<String> allMonths = EpgDateHelper.getAllMonths();
        Assert.assertEquals(12, allMonths.size());
        Assert.assertEquals("jan.", allMonths.get(0));
    }

    @Test
    public void testGetYears() {
        int currentYear = 2000;
        List<String> years = EpgDateHelper.getYears(currentYear);
        Assert.assertEquals(10, years.size());
        Assert.assertEquals("1995", years.get(0));

        currentYear = 1995;
        years = EpgDateHelper.getYears(currentYear);
        Assert.assertEquals(10, years.size());
        Assert.assertEquals("1990", years.get(0));
    }

    @Test
    public void testGetDynamicDate() {
        DateTime today = new DateTime(2004, 12, 01, 0, 0, 0, 0);
        Date todayPlus15Days = EpgDateHelper.getDate(Operator.PLUS, Period.DAY, 15, today);
        DateTime reference = today.plusDays(15);
        Assert.assertEquals(reference, new DateTime(todayPlus15Days));

        Date todayMinus3Months = EpgDateHelper.getDate(Operator.MINUS, Period.MONTH, 3, today);
        reference = today.minusMonths(3);
        Assert.assertEquals(reference, new DateTime(todayMinus3Months));
    }

    @Test
    public void testGetDynamicDateRealLife() {
        DateTime today = new DateTime();
        Date result = EpgDateHelper.getDate(Operator.PLUS, Period.DAY, 5, today);
        Assert.assertNotSame(new DateTime(today), result);
    }
}
