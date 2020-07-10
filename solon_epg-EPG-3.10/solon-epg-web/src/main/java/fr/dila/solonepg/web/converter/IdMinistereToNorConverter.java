package fr.dila.solonepg.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.core.service.STServiceLocator;

public class IdMinistereToNorConverter implements Converter {

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value instanceof String) {
			EntiteNode node;
			try {
				node = STServiceLocator.getSTMinisteresService().getEntiteNode((String) value);
			} catch (ClientException e) {
				// node non trouvé
				node = null;
			}
			return node != null ? node.getNorMinistere() : (String) value;
		}
		return " ";
	}

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		EpgOrganigrammeService organigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
		EntiteNode node;
		try {
			node = organigrammeService.getMinistereFromNor(arg2);
		} catch (ClientException e) {
			// node non trouvé
			node = null;
		}
		return node != null ? node.getId() : arg2;
	}

}
