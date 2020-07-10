package fr.dila.solonepg.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Converter JSF qui fournit le nor d'un ministere
 * 
 * @author asatre
 */
public class OrganigrammeNorIdToLabelConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String string) {
		return string;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
		if (object instanceof String && !StringUtils.isEmpty((String) object)) {
			String id = (String) object;
			try {
				EntiteNode node = STServiceLocator.getSTMinisteresService().getEntiteNode(id);

				return node.getNorMinistere();

			} catch (ClientException e) {
				return null;
			}
		}
		return null;
	}

}
