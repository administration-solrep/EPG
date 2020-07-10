package fr.dila.solonepg.core.feuilleroute;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.ecm.platform.routing.core.impl.ElementRunner;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.core.feuilleroute.STFeuilleRouteImpl;
import fr.dila.st.core.util.PropertyUtil;

/**
 * Implémentation des feuilles de route de SOLON EPG.
 * 
 * @author jtremeaux
 */
public class SolonEpgFeuilleRouteImpl extends STFeuilleRouteImpl implements SolonEpgFeuilleRoute {

    private static final long serialVersionUID = -1110382493524514160L;

    /**
     * Constructeur de FeuilleRouteImpl.
     * 
     * @param doc doc
     * @param runner runner
     */
    public SolonEpgFeuilleRouteImpl(DocumentModel doc, ElementRunner runner) {
        super(doc, runner);
    }

    @Override
    public String getDirection() {
        return PropertyUtil.getStringProperty(document,
                STSchemaConstant.FEUILLE_ROUTE_SCHEMA,
                SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_DIRECTION_PROPERTY);
    }
    
    @Override
    public void setDirection(String direction) {
        PropertyUtil.setProperty(document,
                STSchemaConstant.FEUILLE_ROUTE_SCHEMA,
                SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_DIRECTION_PROPERTY,
                direction);
    }

    @Override
    public String getTypeActe() {
        return PropertyUtil.getStringProperty(document,
                STSchemaConstant.FEUILLE_ROUTE_SCHEMA,
                SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_TYPE_ACTE_PROPERTY);
    }
    
    @Override
    public void setTypeActe(String typeActe) {
        PropertyUtil.setProperty(document,
                STSchemaConstant.FEUILLE_ROUTE_SCHEMA,
                SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_TYPE_ACTE_PROPERTY,
                typeActe);
    }
    
    @Override
    public String getNumero() {
        return PropertyUtil.getStringProperty(document,
                STSchemaConstant.FEUILLE_ROUTE_SCHEMA,
                SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_NUMERO_PROPERTY);
    }
    
    @Override
    public void setNumero(String numero) {
        PropertyUtil.setProperty(document,
                STSchemaConstant.FEUILLE_ROUTE_SCHEMA,
                SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_NUMERO_PROPERTY,
                numero);
    }
    
    @Override
    public boolean isFeuilleRouteDefautGlobal() {
        return StringUtils.isBlank(getMinistere()) && StringUtils.isBlank(getDirection()) &&
            StringUtils.isBlank(getTypeActe()) && isFeuilleRouteDefaut();
    }
}
