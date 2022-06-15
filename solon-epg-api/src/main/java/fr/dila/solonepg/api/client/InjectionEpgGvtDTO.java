package fr.dila.solonepg.api.client;

import fr.dila.ss.api.client.InjectionGvtDTO;
import java.io.Serializable;

public interface InjectionEpgGvtDTO extends Serializable {
    String getModification();

    void setModification(String modification);

    InjectionGvtDTO getBaseGvt();

    void setBaseGvt(InjectionGvtDTO baseGvt);

    InjectionGvtDTO getImportedGvt();

    void setImportedGvt(InjectionGvtDTO importedGvt);

    boolean hasNewMinistere();

    boolean hasNewMandat();

    boolean hasNewIdentite();

    void setHasNewMinistere(boolean newMinistere);

    void setHasNewMandat(boolean newMandat);

    void setHasNewIdentite(boolean newIdentite);
}
