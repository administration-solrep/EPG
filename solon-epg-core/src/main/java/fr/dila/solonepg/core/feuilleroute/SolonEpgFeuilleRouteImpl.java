package fr.dila.solonepg.core.feuilleroute;

import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.constant.SSFeuilleRouteConstant;
import fr.dila.ss.core.feuilleroute.SSFeuilleRouteImpl;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.eltrunner.ElementRunner;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implémentation des feuilles de route de SOLON EPG.
 *
 * @author jtremeaux
 */
public class SolonEpgFeuilleRouteImpl extends SSFeuilleRouteImpl implements SolonEpgFeuilleRoute {
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
        return PropertyUtil.getStringProperty(
            document,
            SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA,
            SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_DIRECTION_PROPERTY
        );
    }

    @Override
    public void setDirection(String direction) {
        PropertyUtil.setProperty(
            document,
            SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA,
            SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_DIRECTION_PROPERTY,
            direction
        );
    }

    @Override
    public String getTypeActe() {
        return PropertyUtil.getStringProperty(
            document,
            SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA,
            SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_TYPE_ACTE_PROPERTY
        );
    }

    @Override
    public void setTypeActe(String typeActe) {
        PropertyUtil.setProperty(
            document,
            SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA,
            SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_TYPE_ACTE_PROPERTY,
            typeActe
        );
    }

    @Override
    public Long getNumero() {
        return PropertyUtil.getLongProperty(
            document,
            SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA,
            SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_NUMERO_PROPERTY
        );
    }

    @Override
    public void setNumero(Long numero) {
        PropertyUtil.setProperty(
            document,
            SSFeuilleRouteConstant.FEUILLE_ROUTE_SCHEMA,
            SolonEpgSchemaConstant.SOLON_EPG_FEUILLE_ROUTE_NUMERO_PROPERTY,
            numero
        );
    }

    @Override
    public boolean isFeuilleRouteDefautGlobal() {
        return (
            StringUtils.isBlank(getMinistere()) &&
            StringUtils.isBlank(getDirection()) &&
            StringUtils.isBlank(getTypeActe()) &&
            isFeuilleRouteDefaut()
        );
    }

    /**
     * Indique si on manipule un squelette plutôt qu'un modèle "classique" de feuille de route
     */
    @Override
    public boolean isSqueletteFeuilleRoute() {
        return SolonEpgServiceLocator.getDocumentRoutingService().isSqueletteFeuilleRoute(document);
    }
}
