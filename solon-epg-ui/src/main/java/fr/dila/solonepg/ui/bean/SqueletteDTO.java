package fr.dila.solonepg.ui.bean;

import fr.dila.ss.ui.bean.fdr.FeuilleRouteDTO;

public class SqueletteDTO extends FeuilleRouteDTO {
    private static final long serialVersionUID = 3951662876012579952L;

    private static final String TYPE_ACTE = "typeActe";

    public SqueletteDTO() {
        super();
    }

    public SqueletteDTO(
        String typeActe,
        String id,
        String etat,
        String intitule,
        String ministere,
        String auteur,
        String derniereModif,
        Boolean lock,
        String lockOwner
    ) {
        super(id, etat, intitule, ministere, auteur, derniereModif, lock, lockOwner);
        setTypeActe(typeActe);
    }

    public String getTypeActe() {
        return getString(TYPE_ACTE);
    }

    public void setTypeActe(String typeActe) {
        put(TYPE_ACTE, typeActe);
    }

    @Override
    public String getType() {
        return "Squelette";
    }
}
