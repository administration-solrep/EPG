package fr.dila.solonmgpp.web.provider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.ClientRuntimeException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.query.api.PageSelection;
import org.nuxeo.ecm.platform.query.api.PageSelections;

import fr.dila.solonepg.web.contentview.AbstractDTOFiltrableProvider;
import fr.dila.solonmgpp.api.dto.MessageDTO;
import fr.dila.solonmgpp.api.tree.CorbeilleNode;
import fr.dila.solonmgpp.core.builder.QueryBuilder;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.corbeille.CorbeilleTreeBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;

/**
 * Provider de la liste des messages d'une corbeille
 * 
 * @author asatre
 * 
 */
public class CorbeillePageProvider extends AbstractDTOFiltrableProvider {

    private static final String CORBEILLE_TREE_BEAN = "corbeilleTreeBean";

    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(CorbeillePageProvider.class);    

    private static final long serialVersionUID = 1L;

    private List<Object> filtrableParameters;
    private int filtrableCount;

    private Map<String, Serializable> mapFiltrable;

    @Override
    protected void fillCurrentPageMapList(CoreSession coreSession) throws ClientException {
        CorbeilleTreeBean corbeilleTreeBean = getCorbeilleTreeBean();

        currentItems = new ArrayList<Map<String, Serializable>>();
        resultsCount = currentItems.size();

        CorbeilleNode corbeilleNode = corbeilleTreeBean.getCurrentItem();
        SSPrincipal ssPrincipal = corbeilleTreeBean.getSSPrincipal();
        String currentMenu = corbeilleTreeBean.getCurrentMenu();

        if (corbeilleNode != null) {
            List<MessageDTO> list = new ArrayList<MessageDTO>();
            if ((filtrableParameters == null || filtrableParameters.isEmpty())
                    && (sortInfos == null || sortInfos.size() == 1 && !sortInfos.get(0).getSortAscending()
                            && sortInfos.get(0).getSortColumn().equals(QueryBuilder.getDefaultOrderClause()))) {
                // pas de filtre et pas de tri => list par défaut
                list = corbeilleNode.getMessageDTO();
            } else {
                try {
                    list = SolonMgppServiceLocator.getMessageService().filtrerCorbeille(corbeilleNode, coreSession, mapFiltrable, sortInfos,
                            ssPrincipal, currentMenu);
                } catch (Exception e) {
                    error = e;
                    errorMessage = e.getMessage();
                    LOGGER.warn(coreSession, STLogEnumImpl.FAIL_FILTER_CORBEILLE_TEC,e.getMessage(),e) ;
                }
            }

            resultsCount = list.size();

            for (long i = offset; i < offset + getPageSize() && i < list.size(); i++) {
                currentItems.add(list.get((int) i));
            }

        }
    }

    @Override
    protected void buildQuery() {
        try {

            filtrableParameters = new ArrayList<Object>();
            mapFiltrable = new HashMap<String, Serializable>();

            filtrableCount = 0;

            PageSelections<Map<String, Serializable>> pageSelections = getCurrentFiltrableMap();
            for (PageSelection<Map<String, Serializable>> pageSelection : pageSelections.getEntries()) {
                Set<String> filtrableProps = new HashSet<String>();
                if (getFiltrableProperty() != null) {
                    filtrableProps.addAll(getFiltrableProperty());
                }
                if (!filtrableProps.isEmpty()) {
                    for (String key : pageSelection.getData().keySet()) {
                        Serializable value = pageSelection.getData().get(key);
                        if (!(value instanceof Map)) {
                            if (value instanceof String) {
                                String valeur = (String) value;
                                if (!StringUtils.isEmpty(valeur)) {
                                    valeur = valeur.trim().replace('*', '%');
                                    if (Boolean.TRUE.toString().equals(valeur)) {
                                        addParameter(key, Boolean.TRUE);
                                    } else if (Boolean.FALSE.toString().equals(valeur)) {
                                        addParameter(key, Boolean.FALSE);
                                    } else {
                                        addParameter(key, valeur);
                                    }
                                }
                            } else if (value instanceof Date) {
                                addParameterDate(key, (Date) value);
                            } else if (value instanceof Boolean) {
                                addParameter(key, value);
                            }
                        }
                    }
                }
                // il y a un seul document pour le filtre
                break;
            }

            query = "noquery";

        } catch (ClientException e) {
            throw new ClientRuntimeException(e);
        }
    }

    private void addParameter(String key, Serializable value) {

        mapFiltrable.put(key, value);
        filtrableParameters.add(value);
        filtrableCount++;

    }

    private void addParameterDate(String key, Date value) {

        mapFiltrable.put(key, value);
        filtrableParameters.add(value);
        filtrableCount++;

    }

    private CorbeilleTreeBean getCorbeilleTreeBean() {
        Map<String, Serializable> props = getProperties();
        return (CorbeilleTreeBean) props.get(CORBEILLE_TREE_BEAN);
    }

    @Override
    public Boolean isFiltreActif() {
        return filtrableParameters != null && !filtrableParameters.isEmpty();
    }

    @Override
    public Integer getFiltreActif() {
        if (filtrableParameters != null) {
            return filtrableCount;
        }
        return 0;
    }
}
