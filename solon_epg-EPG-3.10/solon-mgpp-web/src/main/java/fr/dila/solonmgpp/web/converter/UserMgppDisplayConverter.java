package fr.dila.solonmgpp.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonmgpp.api.dto.TableReferenceDTO;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.SessionUtil;

public class UserMgppDisplayConverter implements Converter {

    /**
     * Logger formalisé en surcouche du logger apache/log4j 
     */
    private static final STLogger LOGGER = STLogFactory.getLog(UserMgppDisplayConverter.class);
    
    /**
     * Returns given value (does not do any reverse conversion)
     */
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) {
        return value;
    }

    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
      
        TableReferenceDTO tableReferenceDTO = null;
        String ident = (String) value;
        String title = null;
        CoreSession session = null;
        try {
            try {
                session = SessionUtil.getCoreSession();
            } catch (ClientException exc) {
                LOGGER.error(null, STLogEnumImpl.FAIL_GET_SESSION_TEC, exc);
            }
            if (session != null) {
                try {
                    tableReferenceDTO = SolonMgppServiceLocator.getTableReferenceService().findTableReferenceByIdAndType(ident, "Identite", null, 
                            session);
                } catch (ClientException exc) {
                    LOGGER.error(session, STLogEnumImpl.FAIL_GET_TABLE_REFERENCE_TEC, exc);
                }
                if (tableReferenceDTO != null) {
                    title = tableReferenceDTO.getTitle();
                }
            }
        } finally {
            if (session != null) {
                SessionUtil.close(session);
            }
        }
        return title;
        
    }
}
