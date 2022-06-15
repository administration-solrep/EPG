package fr.dila.solonepg.ui.bean.fdr;

import fr.dila.ss.ui.bean.fdr.FeuilleRouteDTO;

public class EpgFeuilleRouteDTO extends FeuilleRouteDTO {
    private static final long serialVersionUID = 1744085173620182864L;

    private static final String NUMERO = "numero";

    public EpgFeuilleRouteDTO() {
        super();
    }

    public EpgFeuilleRouteDTO(FeuilleRouteDTO dto) {
        super();
        setId(dto.getId());
        setDocIdForSelection(dto.getDocIdForSelection());
        setEtat(dto.getEtat());
        setIntitule(dto.getIntitule());
        setMinistere(dto.getMinistere());
        setAuteur(dto.getAuteur());
        setDerniereModif(dto.getDerniereModif());
        setLock(dto.getlock());
        setLockOwner(dto.getlockOwner());
    }

    public static String getNumero() {
        return NUMERO;
    }

    public void setNumero(Long numero) {
        put(NUMERO, numero);
    }
}
