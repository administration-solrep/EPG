package fr.dila.solonepg.web.recherche;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.core.Events;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.userworkspace.web.ejb.UserWorkspaceManagerActionsBean;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.richfaces.component.UITree;
import org.richfaces.event.NodeExpandedEvent;

import fr.dila.solonepg.api.service.EspaceRechercheService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.web.contentview.ProviderBean;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.web.feuilleroute.DocumentRoutingWebActionsBean;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Classe de gestion de l'arbre de recherche.
 * 
 * @author asatre
 */
@Name("espaceRechercheTree")
@Scope(ScopeType.CONVERSATION)
public class EspaceRechercheTreeBean implements Serializable {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(required = true, create = true)
    protected SSPrincipal ssPrincipal;

    @In(create = true, required = false)
    protected transient DocumentRoutingWebActionsBean routingWebActions;

    @In(create = true)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true, required = false)
    protected transient UserWorkspaceManagerActionsBean userWorkspaceManagerActions;

    @In(create = true, required = false)
    protected transient UserManagerActionsBean userManagerActions;

    private List<EspaceRechercheTreeNode> rootNodes;

    private EspaceRechercheTreeNode currentNode;

    private String query;

    public List<EspaceRechercheTreeNode> getEspaceRecherche() {
        if (rootNodes == null) {
            load();
        }
        return rootNodes;
    }

    private void load() {
        rootNodes = new ArrayList<EspaceRechercheTreeNode>();

        for (EspaceRechercheEnum espaceRechEnum : EspaceRechercheEnum.getRoots()) {
            EspaceRechercheTreeNode treeNode = new EspaceRechercheTreeNode(espaceRechEnum, null);
            rootNodes.add(treeNode);
            for (EspaceRechercheEnum espaceRechercheEnum : espaceRechEnum.getChildren()) {
                treeNode.addChild(new EspaceRechercheTreeNode(espaceRechercheEnum, treeNode));
            }
        }
    }

    public Boolean adviseNodeOpened(UITree treeComponent) {
        Object value = treeComponent.getRowData();
        if (value instanceof EspaceRechercheTreeNode) {
            EspaceRechercheTreeNode minNode = (EspaceRechercheTreeNode) value;
            return minNode.isOpened();
        }
        return null;
    }

    public void changeExpandListener(NodeExpandedEvent event) {
        UIComponent component = event.getComponent();
        if (component instanceof UITree) {
            UITree treeComponent = (UITree) component;
            Object value = treeComponent.getRowData();
            if (value instanceof EspaceRechercheTreeNode) {
                EspaceRechercheTreeNode node = (EspaceRechercheTreeNode) value;
                if (!node.isOpened()) {
                    node.setOpened(Boolean.TRUE);
                } else {
                    node.setOpened(Boolean.FALSE);
                }
            }
        }
    }

    public String setElement(EspaceRechercheTreeNode node) throws ClientException {
        currentNode = node;
        if (currentNode.getEnumType().name().contains(EspaceRechercheEnum.USER.name())
                || currentNode.getEnumType().name().contains(EspaceRechercheEnum.UTILISATEURS.name())) {
            // reset du user courant
            userManagerActions.resetAll();
        }

        StringBuilder queryBuilder = new StringBuilder();
        switch (currentNode.getEnumType()) {
        case DOSSIERS:
            switch (currentNode.getParent().getEnumType()) {
            case DERNIERS_RESULTATS_CONSULTES:
                queryBuilder.append(" SELECT rc.rescon:dossiersIds AS id FROM ResultatConsulte as rc ");
                queryBuilder.append(" WHERE rc.rescon:dossiersIds is not null and rc.dc:creator = '");
                queryBuilder.append(ssPrincipal.getName());
                queryBuilder.append("' ");

                // check des ACL
                checkACLDossier(EspaceRechercheEnum.DERNIERS_RESULTATS_CONSULTES);
                checkACLDossier(EspaceRechercheEnum.FAVORIS_CONSULTATION);

                query = queryBuilder.toString();
                break;
            case FAVORIS_CONSULTATION:
                queryBuilder.append(" SELECT d.ecm:uuid AS id FROM FavorisConsultation as fc, Dossier as d ");
                queryBuilder.append(" WHERE fc.dc:creator = '");
                queryBuilder.append(ssPrincipal.getName());
                queryBuilder.append("' ");
                queryBuilder.append(" AND fc.favcon:dossiersIds = d.ecm:uuid ");

                // check des ACL
                checkACLDossier(EspaceRechercheEnum.FAVORIS_CONSULTATION);

                queryBuilder.append(" AND (testAcl(d.ecm:uuid)=1) ");

                query = queryBuilder.toString();
                break;
            default:
                query = null;
                break;
            }
            break;
        case FDR:
            switch (currentNode.getParent().getEnumType()) {
            case DERNIERS_RESULTATS_CONSULTES:
                queryBuilder.append(" SELECT rc.rescon:fdrsIds AS id FROM ResultatConsulte as rc, FeuilleRoute as r ");
                queryBuilder.append(" WHERE rc.rescon:fdrsIds is not null");
                queryBuilder.append(" AND rc.rescon:fdrsIds = r.ecm:uuid");
                queryBuilder.append(" AND r.ecm:currentLifeCycleState != 'deleted' ");
                queryBuilder.append(" AND rc.dc:creator = '");
                queryBuilder.append(ssPrincipal.getName());
                queryBuilder.append("' ");
                query = queryBuilder.toString();
                break;
            case FAVORIS_CONSULTATION:
                queryBuilder.append(" SELECT r.ecm:uuid AS id FROM FavorisConsultation as fc, FeuilleRoute as r ");
                queryBuilder.append(" WHERE fc.dc:creator = '");
                queryBuilder.append(ssPrincipal.getName());
                queryBuilder.append("' ");
                queryBuilder.append(" AND fc.favcon:fdrsIds = r.ecm:uuid ");
                query = queryBuilder.toString();
                break;
            default:
                query = null;
                break;
            }
            break;
        case USER:
            switch (currentNode.getParent().getEnumType()) {
            case DERNIERS_RESULTATS_CONSULTES:
                queryBuilder.append(" SELECT rc.rescon:usersNames AS id FROM ResultatConsulte as rc ");
                queryBuilder.append(" WHERE rc.rescon:usersNames is not null and rc.dc:creator = '");
                queryBuilder.append(ssPrincipal.getName());
                queryBuilder.append("' ");
                query = queryBuilder.toString();
                break;
            case FAVORIS_CONSULTATION:
                queryBuilder.append(" SELECT fc.favcon:usersNames AS id FROM FavorisConsultation as fc ");
                queryBuilder.append(" WHERE fc.dc:creator = '");
                queryBuilder.append(ssPrincipal.getName());
                queryBuilder.append("' ");
                query = queryBuilder.toString();
                break;
            default:
                query = null;
                break;
            }
            break;
        case FAVORIS_RECHERCHE:
            queryBuilder.append(" SELECT d.ecm:uuid AS id FROM FavorisRecherche as d ");
            queryBuilder.append(" WHERE isChildOf(d.ecm:uuid, ");
            queryBuilder.append(" select w.ecm:uuid from Workspace AS w where w.dc:title ='");
            queryBuilder.append(ssPrincipal.getName());
            queryBuilder.append("' ) = 1 ");
            queryBuilder.append("AND d.ecm:currentLifeCycleState != 'deleted'");
            query = queryBuilder.toString();
            break;
        default:
            query = null;
            break;
        }

        routingWebActions.setFeuilleRouteView(node.getLink());
        Events.instance().raiseEvent(ProviderBean.REFRESH_CONTENT_VIEW_EVENT);
        navigationContext.setCurrentDocument(null);

        return node.getLink();
    }

    public String getQuery() {
        return query;
    }

    /**
     * supprime les dossiers n'existant plus ou les dossiers dont l'utilisateur n'as plus le droit de consultation ie testAcl=0
     * 
     * @param derniersResultatsConsultes
     * @param sb
     * @throws ClientException
     */
    private void checkACLDossier(EspaceRechercheEnum type) throws ClientException {
        EspaceRechercheService espaceRechercheService = SolonEpgServiceLocator.getEspaceRechercheService();
        switch (type) {
        case DERNIERS_RESULTATS_CONSULTES:
            espaceRechercheService.removeDossierFromDerniersResultatsConsultes(documentManager, userWorkspaceManagerActions
                    .getCurrentUserPersonalWorkspace().getPathAsString());
            break;
        case FAVORIS_CONSULTATION:
            espaceRechercheService.removeDossierFromFavorisConsultation(documentManager, userWorkspaceManagerActions
                    .getCurrentUserPersonalWorkspace().getPathAsString());
            break;
        default:
            break;
        }
    }

    public String getTitle() {
        if (currentNode != null) {
            if (currentNode.getParent() != null) {
                return resourcesAccessor.getMessages().get(currentNode.getParent().getLabel()) + " - "
                        + resourcesAccessor.getMessages().get(currentNode.getLabel());
            }
            return resourcesAccessor.getMessages().get(currentNode.getLabel());
        } else {
            return "";
        }
    }

}
