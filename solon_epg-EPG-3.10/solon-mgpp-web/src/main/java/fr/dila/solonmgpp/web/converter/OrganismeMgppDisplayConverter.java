package fr.dila.solonmgpp.web.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.sun.faces.util.MessageFactory;

public class OrganismeMgppDisplayConverter implements Converter {

    /**
     * Returns given value (does not do any reverse conversion)
     */
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        return value;
    }

    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
      String ident = (String)value;
      FacesMessage facesMessage = MessageFactory.getMessage(context, ident);
      return facesMessage.getDetail();
    }

}
