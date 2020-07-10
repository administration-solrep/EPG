package fr.dila.solonepg.web.converter;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.model.PropertyException;

import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.SessionUtil;
import fr.dila.st.web.converter.AbstractConverter;

/**
 * Convertisseur de label des modes de parution utilisé dans la recherche experte
 * 
 */
public class ModeParutionConverter extends AbstractConverter {
    
	private static final STLogger LOGGER = STLogFactory.getLog(ModeParutionConverter.class);
	
    public ModeParutionConverter(){
        // Default constructor
    }

    @Override
    public String getAsString(FacesContext context, UIComponent arg1, Object object) {
       if (object instanceof String && !StringUtils.isEmpty((String) object)) {
           String idsStr = (String) object;
           String[] ids = idsStr.split(",");
           CoreSession session = null;
           List<String> labels =  new ArrayList<String>();
			try {
				session = SessionUtil.getCoreSession();
	           for (String id : ids){
	               String idTrimmed = id.trim();
	               String label = id;
				try {
					label = (String) session.getDocument(new IdRef(idTrimmed)).getPropertyValue("mod:mode");
				} catch (PropertyException pe) {
					LOGGER.error(session, STLogEnumImpl.FAIL_GET_PROPERTY_TEC, id, pe);
				} catch (ClientException ce) {
					LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, id, ce);
				}
	               labels.add(label);
	           }
			} catch (ClientException ce1) {
				LOGGER.error(session, STLogEnumImpl.FAIL_GET_SESSION_TEC, ce1);
			} finally {
				if (session != null) {
					SessionUtil.close(session);
				}
			}
           return StringUtils.join(labels,", ");
        }
        return null;
    }

}
