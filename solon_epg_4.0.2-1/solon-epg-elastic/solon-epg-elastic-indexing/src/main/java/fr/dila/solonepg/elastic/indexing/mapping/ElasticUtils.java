package fr.dila.solonepg.elastic.indexing.mapping;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ElasticUtils {

    public static Date toDate(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        return calendar.getTime();
    }

    public static List<Date> toDates(List<Calendar> calendars) {
        if (calendars == null) {
            return null;
        }
        List<Date> dates = new ArrayList<>();
        for (Calendar calendar : calendars) {
            dates.add(calendar.getTime());
        }
        return dates;
    }
}
