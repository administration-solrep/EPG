package fr.dila.solonepg.ui.bean.squelette;

import fr.dila.solonepg.api.feuilleroute.SqueletteStepTypeDestinataire;
import fr.dila.ss.ui.bean.fdr.EtapeDTO;
import org.apache.commons.lang3.StringUtils;

public class EtapeSqueletteDTO extends EtapeDTO {
    private SqueletteStepTypeDestinataire typeDestinataire;

    public EtapeSqueletteDTO() {}

    public SqueletteStepTypeDestinataire getTypeDestinataire() {
        return typeDestinataire;
    }

    public void setTypeDestinataire(SqueletteStepTypeDestinataire typeDestinataire) {
        this.typeDestinataire = typeDestinataire;
    }

    @Override
    public String getPosteInMinistere() {
        if (SqueletteStepTypeDestinataire.ORGANIGRAMME.equals(typeDestinataire)) {
            return (
                (StringUtils.isNotEmpty(ministere) ? ministere : "Sans ministère défini") +
                " <br /> " +
                (StringUtils.isNotEmpty(poste) ? poste : "sans poste")
            );
        } else {
            return (
                (StringUtils.isNotEmpty(ministere) ? ministere : "") +
                " <br /> " +
                (StringUtils.isNotEmpty(poste) ? poste : "<br />")
            );
        }
    }
}
