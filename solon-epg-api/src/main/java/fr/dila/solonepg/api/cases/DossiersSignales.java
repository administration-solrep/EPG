package fr.dila.solonepg.api.cases;

import fr.dila.st.api.domain.STDomainObject;
import java.io.Serializable;
import java.util.List;

public interface DossiersSignales extends STDomainObject, Serializable {
    public static final String DOSSIERS_SIGNALES_DOSSIERS_IDS_PROPERTY = "dossiersIds";

    public List<String> getDossiersIds();

    public void setDossiersIds(List<String> dossiersIds);
}
