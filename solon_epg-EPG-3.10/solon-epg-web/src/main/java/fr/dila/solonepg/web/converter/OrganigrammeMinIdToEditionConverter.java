package fr.dila.solonepg.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;

import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.core.service.STServiceLocator;

/**
 * Converter JSF qui fournit le label d'un ministère.
 * 
 * @author Fabio Esposito 
 */
public class OrganigrammeMinIdToEditionConverter implements Converter {
    
    private static final Log log = LogFactory.getLog(OrganigrammeMinIdToEditionConverter.class);

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String string) {
        return string;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object object) {
        if (object instanceof String && !StringUtils.isEmpty((String) object)) {
            String identifier = (String)object;
            try {
                OrganigrammeNode node = STServiceLocator.getSTMinisteresService().getEntiteNode(identifier);
                
                if (node == null) {
                    log.error("Impossible de retrouver le ministere " + identifier + "...");
                    return "**ministère inconnu**";
                } else {
                    return ((EntiteNode) node).getEdition();
                }
            } catch (ClientException e) {
                log.error("Impossible de retrouver le ministere " + identifier + "...", e);
                return null;
            }
        }
        return null;
    }

}
