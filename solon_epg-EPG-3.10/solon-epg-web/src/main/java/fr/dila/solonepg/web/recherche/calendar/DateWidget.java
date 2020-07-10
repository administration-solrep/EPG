package fr.dila.solonepg.web.recherche.calendar;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import fr.dila.solonepg.core.recherche.date.EpgDateHelper;
import fr.dila.solonepg.core.recherche.date.EpgDateHelper.Operator;
import fr.dila.solonepg.core.recherche.date.EpgDateHelper.Period;
import fr.dila.st.core.util.DateUtil;



public class DateWidget implements Serializable{

	private static final long serialVersionUID = 125647L;

	public static final int DEFAULT_ID = 0;

    public enum Mode {STATIC, DYNAMIC};
    
    private Mode selectedMode;

    // Static part 
    
    private Integer selectDay;
    
    private Integer selectMonth;
    
    private Integer selectYear;

    private Date calendarDate;
    
    // Dynamic part
    
    private Integer inputCount;
    
    private String selectPeriod;
    
    private String selectOperator;
    
    public DateWidget(){
    	reset();
    }
    
    public void reset(){
        this.inputCount = 0;
        this.selectedMode = Mode.STATIC;
        this.setCalendarDate(null);
        setUpSelects(DEFAULT_ID,DEFAULT_ID,DEFAULT_ID);
        this.selectPeriod = Period.DAY.toString();
        this.selectOperator = Operator.MINUS.toString();
    }
    
    public String getSelectedMode() {
        return this.selectedMode.toString();
    }

    public void setSelectedMode(String selectedMode) {
        if (selectedMode == null){
            return;
        }
        this.selectedMode = Mode.valueOf(selectedMode);
    }

    public Integer getSelectDay() {
        return selectDay;
    }

    public void setSelectDay(Integer selectDay) {
        this.selectDay = selectDay;
    }

    public Integer getSelectMonth() {
        return selectMonth;
    }

    public void setSelectMonth(Integer selectMonth) {
        this.selectMonth = selectMonth;
    }
    
    public Integer getSelectYear() {
        return selectYear;
    }
    
    public void setSelectYear(Integer selectYear) {
        this.selectYear = selectYear;
    }

    public Date getCalendarDate() {
        return DateUtil.copyDate(calendarDate);
    }

    public void setCalendarDate(Date calendarDate) {
        this.calendarDate = DateUtil.copyDate(calendarDate);
    }	    

    public void setUpSelects(int day, int month, int year) {
    	this.setSelectYear(year);
    	this.setSelectMonth(month);
        this.setSelectDay(day);
    }

    public void setSelectPeriod(String selectPeriod) {
        this.selectPeriod = selectPeriod;
    }
    
    public String getSelectPeriod() {
        return selectPeriod;
    }
    
    public void setSelectOperator(String selectOperator) {
        this.selectOperator = selectOperator;
    }
    
    public String getSelectOperator() {
        return selectOperator;
    }

    public void setInputCount(Integer inputCount) {
        this.inputCount = inputCount;
    }

    public Integer getInputCount() {
        return inputCount;
    }
    
    public List<String> getDays(){
        DateTime start = new DateTime(this.getCalendarDate());
        return EpgDateHelper.getDaysOfTheMonth(start);
    }
    
    public List<String> getMonths(){
        return EpgDateHelper.getAllMonths();
    }
    
    public List<String> getYears(){
        int currentYear = new DateTime(this.getCalendarDate()).getYearOfEra();
        return EpgDateHelper.getYears(currentYear);
    }
    
    public Operator[] getOperators(){
        return Operator.values();
    }
    
    public Period[] getPeriods(){
        return Period.values();
    }
}
