package fr.dila.solonepg.core.client;

import fr.dila.solonepg.api.client.InjectionEpgGvtDTO;
import fr.dila.ss.api.client.InjectionGvtDTO;
import java.io.Serializable;

/**
 * Pour l'affichage des différence entre les ministères
 *
 * @author jbrunet
 *
 */
public class InjectionEpgGvtDTOImpl implements InjectionEpgGvtDTO, Serializable {
    private static final long serialVersionUID = 1L;

    private String modification;
    private InjectionGvtDTO baseGvt;
    private InjectionGvtDTO importedGvt;
    private boolean nouveauMinistere;
    private boolean nouveauMandat;
    private boolean nouvelleIdentite;

    /**
     * Constructeur par défaut
     *
     * @param modification
     * @param baseGvt
     * @param importedGvt
     */
    public InjectionEpgGvtDTOImpl(String modification, InjectionGvtDTO baseGvt, InjectionGvtDTO importedGvt) {
        this.modification = modification;
        this.baseGvt = baseGvt;
        this.importedGvt = importedGvt;
    }

    @Override
    public String getModification() {
        return modification;
    }

    @Override
    public void setModification(String modification) {
        this.modification = modification;
    }

    @Override
    public InjectionGvtDTO getBaseGvt() {
        return baseGvt;
    }

    @Override
    public void setBaseGvt(InjectionGvtDTO baseGvt) {
        this.baseGvt = baseGvt;
    }

    @Override
    public InjectionGvtDTO getImportedGvt() {
        return importedGvt;
    }

    @Override
    public void setImportedGvt(InjectionGvtDTO importedGvt) {
        this.importedGvt = importedGvt;
    }

    @Override
    public boolean hasNewMinistere() {
        return nouveauMinistere;
    }

    @Override
    public boolean hasNewMandat() {
        return nouveauMandat;
    }

    @Override
    public boolean hasNewIdentite() {
        return nouvelleIdentite;
    }

    @Override
    public void setHasNewMinistere(boolean newMinistere) {
        this.nouveauMinistere = newMinistere;
    }

    @Override
    public void setHasNewMandat(boolean newMandat) {
        this.nouveauMandat = newMandat;
    }

    @Override
    public void setHasNewIdentite(boolean newIdentite) {
        this.nouvelleIdentite = newIdentite;
    }
}
