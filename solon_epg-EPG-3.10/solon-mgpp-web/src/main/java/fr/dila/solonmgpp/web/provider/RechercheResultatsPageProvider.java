package fr.dila.solonmgpp.web.provider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonmgpp.api.dto.CritereRechercheDTO;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.recherche.RechercheActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.web.contentview.AbstractDTOPageProvider;

/**
 * Provider de la liste des messages d'un resultat de recherche
 * 
 * @author asatre
 * 
 */
public class RechercheResultatsPageProvider extends AbstractDTOPageProvider {

    private static final String RECHERCHE_ACTION_BEAN = "rechercheActionsBean";

    private static final long serialVersionUID = 1L;

    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(RechercheResultatsPageProvider.class);    

    @Override
    protected void fillCurrentPageMapList(CoreSession coreSession) throws ClientException {
        RechercheActionsBean rechercheActionsBean = getRechercheActionsBean();

        currentItems = new ArrayList<Map<String, Serializable>>();

        List<MessageDTO> list = new ArrayList<MessageDTO>();

        try {
            CritereRechercheDTO critereRechercheDTO = rechercheActionsBean.getCritereRechercheAvancee();
            critereRechercheDTO.setSortInfos(sortInfos);

            list = SolonMgppServiceLocator.getRechercheService().findMessage(critereRechercheDTO, coreSession);

        } catch (Exception e) {
            error = e;
            errorMessage = e.getMessage();
            LOGGER.warn(coreSession, MgppLogEnumImpl.FAIL_GET_MESSAGE_TEC,e) ;            
        }

        resultsCount = list.size();

        for (long i = offset; i < offset + getPageSize() && i < list.size(); i++) {
            currentItems.add(list.get((int) i));
        }

    }

    private RechercheActionsBean getRechercheActionsBean() {
        Map<String, Serializable> props = getProperties();
        return (RechercheActionsBean) props.get(RECHERCHE_ACTION_BEAN);
    }

}
