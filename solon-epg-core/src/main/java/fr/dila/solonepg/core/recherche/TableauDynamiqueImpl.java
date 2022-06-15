package fr.dila.solonepg.core.recherche;

import fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants;
import fr.dila.solonepg.api.enums.FavorisRechercheType;
import fr.dila.solonepg.api.recherche.TableauDynamique;
import fr.dila.st.core.domain.STDomainObjectImpl;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public class TableauDynamiqueImpl extends STDomainObjectImpl implements TableauDynamique {
    private static final long serialVersionUID = -95774957199970885L;

    public TableauDynamiqueImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public FavorisRechercheType getType() {
        String type = getStringProperty(
            SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_SCHEMA,
            SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_TYPE
        );
        return FavorisRechercheType.fromType(type);
    }

    @Override
    public void setType(FavorisRechercheType type) {
        setProperty(
            SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_SCHEMA,
            SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_TYPE,
            type.getType()
        );
    }

    @Override
    public String getQuery() {
        return getStringProperty(
            SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_SCHEMA,
            SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_QUERY
        );
    }

    @Override
    public void setQuery(String query) {
        setProperty(
            SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_SCHEMA,
            SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_QUERY,
            query
        );
    }

    @Override
    public List<String> getUsers() {
        return getListStringProperty(
            SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_SCHEMA,
            SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_USERSNAME
        );
    }

    @Override
    public void setUsers(List<String> users) {
        setProperty(
            SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_SCHEMA,
            SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_USERSNAME,
            users
        );
    }

    @Override
    public List<String> getDestinatairesId() {
        return getListStringProperty(
            SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_SCHEMA,
            SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_DESTINATAIRES
        );
    }

    @Override
    public void setDestinatairesId(List<String> destinatairesId) {
        setProperty(
            SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_SCHEMA,
            SolonEpgEspaceRechercheConstants.TABLEAU_DYNAMIQUE_DESTINATAIRES,
            destinatairesId
        );
    }
}
