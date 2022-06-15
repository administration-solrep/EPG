package fr.dila.solonmgpp.core.dto;

import fr.dila.solonmgpp.api.dto.FicheDossierDTO;
import fr.dila.st.core.client.AbstractMapDTO;

/**
 * Implementation de {@link FicheDossierDTO}
 * @author asatre
 *
 */
public class FicheDossierDTOImpl extends AbstractMapDTO implements FicheDossierDTO {
    private static final long serialVersionUID = 7353384746089295706L;

    @Override
    public String getType() {
        return null;
    }

    @Override
    public String getDocIdForSelection() {
        return null;
    }

    @Override
    public String getIdDossier() {
        return getString(ID_DOSSIER);
    }
}
