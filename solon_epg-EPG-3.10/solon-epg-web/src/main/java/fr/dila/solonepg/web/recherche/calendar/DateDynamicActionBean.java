package fr.dila.solonepg.web.recherche.calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;
import org.nuxeo.ecm.platform.ui.web.util.ComponentUtils;
import org.richfaces.component.html.HtmlCalendar;

import fr.dila.solonepg.core.recherche.date.EpgDateHelper;
import fr.dila.solonepg.core.recherche.date.EpgDateHelper.Operator;
import fr.dila.solonepg.core.recherche.date.EpgDateHelper.Period;
import fr.dila.solonepg.web.recherche.calendar.DateWidget.Mode;

/**
 * Le bean pour gérer les actions du widget de date dynamic.
 * 
 * @author jgomez
 * 
 */

@Name("dynamicDate")
@Scope(ScopeType.CONVERSATION)
public class DateDynamicActionBean {

	private static final String		HIDDEN_CALENDAR		= "hiddenCalendar_";

	private static final String		HIDDEN_DYNAMIC_PART	= "hiddenDynamicPart_";

	private static final String		DATE_SELECTOR_PART	= "hiddenDateSelectorPart_";

	private Map<String, DateWidget>	widgetsMap;

	@RequestParameter
	protected String				dynamicDateComponentId;

	@PostConstruct
	public void setUp() {
		widgetsMap = new HashMap<String, DateWidget>();
	}

	public void clearAll() {
		widgetsMap = new HashMap<String, DateWidget>();
	}

	public DateWidget getCurrentWidget() {
		if (!widgetsMap.containsKey(dynamicDateComponentId)) {
			widgetsMap.put(dynamicDateComponentId, new DateWidget());
		}
		return widgetsMap.get(dynamicDateComponentId);
	}

	public DateWidget getWidget(String widgetId) {
		dynamicDateComponentId = widgetId;
		return getCurrentWidget();
	}

	private void updateValueHolder(ActionEvent event) {
		HtmlCalendar valueHolder = getCalendarValueHolder(event);
		DateWidget currentWidget = getCurrentWidget();
		setDateIntoValueHolder(valueHolder, currentWidget.getCalendarDate());
	}

	private void setDateIntoValueHolder(HtmlCalendar valueHolder, Date date) {
		if (valueHolder != null) {
			valueHolder.setCurrentDate(date);
			FacesContext context = FacesContext.getCurrentInstance();
			ValueExpression expression = valueHolder.getValueExpression("value");
			expression.setValue(context.getELContext(), date);
			valueHolder.setValueExpression("value", expression);
			valueHolder.setImmediate(true);
			valueHolder.setValue(date);
			valueHolder.setSubmittedValue(date);
			valueHolder.setLocalValueSet(false);
			valueHolder.setValid(true);
		}
	}

	public void updateDateFromSelects(ActionEvent event) {
		DateWidget currentWidget = getCurrentWidget();
		DateTime jodaCalendarDate = new DateTime(currentWidget.getCalendarDate());

		// Year update
		if (currentWidget.getSelectYear() != null && currentWidget.getSelectYear() != DateWidget.DEFAULT_ID
				&& currentWidget.getCalendarDate() != null) {
			jodaCalendarDate = jodaCalendarDate.withYear(currentWidget.getSelectYear());
		}

		// Month update
		if (currentWidget.getSelectMonth() != null && currentWidget.getSelectMonth() != DateWidget.DEFAULT_ID
				&& currentWidget.getCalendarDate() != null) {
			jodaCalendarDate = jodaCalendarDate.withMonthOfYear(currentWidget.getSelectMonth());
		}

		// Day update (attention, l'ordre est important)
		if (currentWidget.getSelectDay() != null && currentWidget.getSelectDay() != DateWidget.DEFAULT_ID
				&& currentWidget.getCalendarDate() != null) {
			try {
				jodaCalendarDate = jodaCalendarDate.withDayOfMonth(currentWidget.getSelectDay());
			} catch (IllegalFieldValueException e) {
				jodaCalendarDate = jodaCalendarDate.dayOfMonth().withMaximumValue();
			}
		}
		currentWidget.setCalendarDate(jodaCalendarDate.toDate());
		updateValueHolder(event);
	}

	public void updateDateFromSelectDay(ActionEvent event) {
		updateDateFromSelects(event);
	}

	public void updateDateFromSelectMonth(ActionEvent event) {
		updateDateFromSelects(event);
	}

	public void updateDateFromSelectYear(ActionEvent event) {
		updateDateFromSelects(event);
	}

	/**
	 * Fonctionnement avec les sélections jour/mois/années
	 * 
	 * @param valueHolder
	 * @param event
	 */
	public void updateStaticDate(EditableValueHolder valueHolder, HtmlInputHidden dynamicInputHolder, ActionEvent event) {
		DateWidget currentWidget = getCurrentWidget();
		if (valueHolder == null) {
			return;
		}
		updateValueHolder(event);
		if (currentWidget.getCalendarDate() == null) {
			currentWidget.setUpSelects(DateWidget.DEFAULT_ID, DateWidget.DEFAULT_ID, DateWidget.DEFAULT_ID);
		} else {
			DateTime dateTime = new DateTime(currentWidget.getCalendarDate());
			currentWidget.setUpSelects(dateTime.getDayOfMonth(), dateTime.getMonthOfYear(), dateTime.getYear());
		}
	}

	/**
	 * Fonctionnement avec les opérateurs +/-
	 * 
	 * @param valueHolder
	 * @param event
	 */
	public void updateDynamicDate(HtmlCalendar valueHolder, HtmlInputHidden dynamicInputHolder, ActionEvent event) {
		DateWidget currentWidget = getCurrentWidget();
		if (valueHolder == null) {
			return;
		}
		if (Mode.DYNAMIC.toString().equals(currentWidget.getSelectedMode())) {
			Operator operator = Operator.valueOf(currentWidget.getSelectOperator());
			Period period = Period.valueOf(currentWidget.getSelectPeriod());
			Integer input = currentWidget.getInputCount();
			Date computedDate = EpgDateHelper.getDate(operator, period, input, new DateTime());
			setDateIntoValueHolder(valueHolder, computedDate);
			setDynamicInputHolder(dynamicInputHolder, operator, period, input);
		}
	}

	private void setDynamicInputHolder(HtmlInputHidden dynamicInputHolder, Operator operator, Period period,
			Integer input) {
		String dynamicDateFunction = getDynamicDateFunction(operator, period, input);
		dynamicInputHolder.setValue(dynamicDateFunction);
		dynamicInputHolder.setSubmittedValue(dynamicDateFunction);
		FacesContext context = FacesContext.getCurrentInstance();
		ValueExpression expression = dynamicInputHolder.getValueExpression("value");
		expression.setValue(context.getELContext(), dynamicDateFunction);
		dynamicInputHolder.setValueExpression("value", expression);
	}

	/**
	 * Cette méthode devrait être la responsabilité de KeywordUfnxqlDateResolver
	 * 
	 * @param operator
	 * @param period
	 * @param input
	 * @return
	 */
	private String getDynamicDateFunction(Operator operator, Period period, Integer input) {
		String opSymbol = "";
		if (operator == Operator.PLUS) {
			opSymbol = "+";
		}
		if (operator == Operator.MINUS) {
			opSymbol = "-";
		}
		String periodSymbol = "";
		if (period == Period.DAY) {
			periodSymbol = "J";
		}
		if (period == Period.MONTH) {
			periodSymbol = "M";
		}
		if (period == Period.YEAR) {
			periodSymbol = "Y";
		}
		return "ufnxql_date:(NOW" + opSymbol + input + periodSymbol + ")";
	}

	public void updateDate(ActionEvent event) {
		DateWidget currentWidget = getCurrentWidget();
		HtmlCalendar valueHolder = getCalendarValueHolder(event);
		HtmlInputHidden valueDynamicHolder = getDynamicPartValueHolder(event);
		if (Mode.STATIC.toString().equals(currentWidget.getSelectedMode())) {
			updateStaticDate(valueHolder, valueDynamicHolder, event);
		}
		if (Mode.DYNAMIC.toString().equals(currentWidget.getSelectedMode())) {
			updateDynamicDate(valueHolder, valueDynamicHolder, event);
		}
		setDateSelector(event, currentWidget);
	}

	private void setDateSelector(ActionEvent event, DateWidget currentWidget) {
		HtmlInputHidden dateSelector = getDateSelectorValueHolder(event);
		String modeStr = currentWidget.getSelectedMode().toString();
		dateSelector.setValue(modeStr);
		dateSelector.setSubmittedValue(modeStr);
		FacesContext context = FacesContext.getCurrentInstance();
		ValueExpression expression = dateSelector.getValueExpression("value");
		expression.setValue(context.getELContext(), modeStr);
		dateSelector.setValueExpression("value", expression);
	}

	public void updateDateAndSetStaticMode(ActionEvent event) {
		DateWidget currentWidget = getCurrentWidget();
		currentWidget.setSelectedMode(Mode.STATIC.toString());
		updateDate(event);
	}

	/**
	 * Retourne le calendrier caché qui correspond à la valeur field_0 du widget nuxeo - valeur de la date retournée par
	 * le widget
	 * 
	 * @param event
	 * @return
	 */
	private HtmlCalendar getCalendarValueHolder(ActionEvent event) {
		UIComponent component = event.getComponent();
		if (component == null) {
			return null;
		}
		return ComponentUtils.getComponent(component, HIDDEN_CALENDAR + dynamicDateComponentId, HtmlCalendar.class);
	}

	/**
	 * Retourne l'input de texte qui correspond à la valeur field_1 du widget nuxeo - valeur de la string retourné par
	 * le widget (ufnxql_date:(NOW-15J) par exemple).
	 * 
	 * @param event
	 * @return
	 */
	private HtmlInputHidden getDynamicPartValueHolder(ActionEvent event) {
		UIComponent component = event.getComponent();
		if (component == null) {
			return null;
		}
		return ComponentUtils.getComponent(component, HIDDEN_DYNAMIC_PART + dynamicDateComponentId,
				HtmlInputHidden.class);
	}

	/**
	 * Retourne le booleen qui correspond à la valeur field_2 du widget nuxeo - vrai si la sélection est dynamique, faux
	 * si elle est statique.
	 * 
	 * @param event
	 * @return
	 */
	private HtmlInputHidden getDateSelectorValueHolder(ActionEvent event) {
		UIComponent component = event.getComponent();
		if (component == null) {
			return null;
		}
		return ComponentUtils.getComponent(component, DATE_SELECTOR_PART + dynamicDateComponentId,
				HtmlInputHidden.class);
	}
}
