package fr.dila.solonepg.core.recherche.date;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;


/**
 * Une collection de méthodes destinée à facilité l'écriture du widget date de SOLON.
 * @author jgomez
 *
 */
public class EpgDateHelper {
    
    public enum Operator {
        PLUS("label.operator.plus"), MINUS("label.operator.minus");
        
        private String label;
        
        private Operator(String label){
            this.label = label;
        }
        
        public String getLabel() {
            return this.label;
        }
    }
    
    public enum Period {DAY("label.period.day"), MONTH("label.period.month"), YEAR("label.period.year");
    
        private String label;
        
        private Period(String label){
            this.label = label;
        }
        
        public String getLabel() {
            return this.label;
        }
    }
    
    /**
     * Retourne la liste des jours du mois de la date start passée en paramètre
     * @param start la Date à partir de laquelle on calcule les jours retournés
     * @return Une liste de numéros de jours
     */
    public static List<String> getDaysOfTheMonth(final DateTime start){
    	// j'éprouve des difficultés à mettre en place de manière dynamique le nombre de jours
    	// pour un jour. En première approche, je mets 31 jours partout
        DateTime defaultMonth = new DateTime(2004, 12, 01, 0, 0, 0, 0);
    	DateTime end = defaultMonth.plus(Months.ONE);
        Days days = Days.daysBetween(defaultMonth, end);
        List<String> results = new ArrayList<String>();
        for (int d=1;d<=days.getDays();d++){
            results.add(String.format("%02d",d ));
        }
        return results;
    }
    
    /**
     * Retourne tous les mois 
     * @return Une liste des 12 mois, en français
     */
    @SuppressWarnings("unchecked")
    public static List<String> getAllMonths(){
        String[] monthsArray = new DateFormatSymbols(Locale.FRENCH).getMonths();
        List<String> months = Arrays.asList(monthsArray);
        //Un bug donne 13 mois pour cette méthode ... le 13 est la chaîne vide, on le filtre
        List<String> results = (List<String>) CollectionUtils.selectRejected(months, PredicateUtils.identityPredicate(StringUtils.EMPTY));
        List<String> truncatedResults = new ArrayList<String>();
        for (String result:results){
            truncatedResults.add(result.substring(0,3) + ".");
        }
        return truncatedResults;
    }
    
    /**
     * Retourne une liste des années autour de l'année de référence passé en paramètre
     * @param currentYear : l'année de référence
     * @return Une liste de 10 années prise autour de l'année de référence
     */
    public static List<String> getYears(int currentYear){
        int count = 10;
        List<String> years = new ArrayList<String>();
        for (int i = currentYear - count/2; i < currentYear + count/2;i++ ){
            years.add(String.format("%04d",i ));
        }
        return years;
    }

    /**
     * Calcule une date à partir de la date de départ (ajourd'hui), d'un opérateur, d'une durée en jour/mois ou année et d'une valeur qui représente le nombre
     * d'unités temporelles.
     * @param operator
     * @param period
     * @param count
     * @param today
     * @return
     */
    public static Date getDate(Operator operator, Period period, Integer count, DateTime today) {
        DateTime result = new DateTime();
        today = today.withMinuteOfHour(0);
        today = today.withHourOfDay(0);
        today = today.withSecondOfMinute(0);
        if (Operator.PLUS.equals(operator)){
            if (period.equals(Period.DAY)){
                result = today.plusDays(count);
            }
            if (period.equals(Period.MONTH)){
                result = today.plusMonths(count);
            }
            if (period.equals(Period.YEAR)){
                result = today.plusYears(count);
            }
        }
        if (Operator.MINUS.equals(operator)){
            if (period.equals(Period.DAY)){
                result = today.minusDays(count);
            }
            if (period.equals(Period.MONTH)){
                result = today.minusMonths(count);
            }
            if (period.equals(Period.YEAR)){
                result = today.minusYears(count);
            }
        }
        return result.toDate();
    }

    private EpgDateHelper() {
     //Private default constructor
    }
    
}
