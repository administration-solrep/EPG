package fr.dila.solonepg.core.recherche;

import fr.dila.solonepg.api.constant.SolonEpgEspaceRechercheConstants;
import fr.dila.solonepg.api.recherche.FavorisConsultation;
import fr.dila.st.core.domain.STDomainObjectImpl;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public class FavorisConsultationImpl extends STDomainObjectImpl implements FavorisConsultation {
    private static final long serialVersionUID = -8174534597540786374L;

    public FavorisConsultationImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public List<String> getDossiersId() {
        return getListStringProperty(
            SolonEpgEspaceRechercheConstants.FAVORIS_CONSULTATION_SCHEMA,
            SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_DOSSIERSID
        );
    }

    @Override
    public void setDossiersId(List<String> dossiersId) {
        setProperty(
            SolonEpgEspaceRechercheConstants.FAVORIS_CONSULTATION_SCHEMA,
            SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_DOSSIERSID,
            dossiersId
        );
    }

    @Override
    public List<String> getFdrsId() {
        return getListStringProperty(
            SolonEpgEspaceRechercheConstants.FAVORIS_CONSULTATION_SCHEMA,
            SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_FDRSID
        );
    }

    @Override
    public void setFdrsId(List<String> fdrsId) {
        setProperty(
            SolonEpgEspaceRechercheConstants.FAVORIS_CONSULTATION_SCHEMA,
            SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_FDRSID,
            fdrsId
        );
    }

    @Override
    public List<String> getUsers() {
        return getListStringProperty(
            SolonEpgEspaceRechercheConstants.FAVORIS_CONSULTATION_SCHEMA,
            SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_USERSNAME
        );
    }

    @Override
    public void setUsers(List<String> usersName) {
        setProperty(
            SolonEpgEspaceRechercheConstants.FAVORIS_CONSULTATION_SCHEMA,
            SolonEpgEspaceRechercheConstants.ESPACE_RECHERCHE_USERSNAME,
            usersName
        );
    }
}
