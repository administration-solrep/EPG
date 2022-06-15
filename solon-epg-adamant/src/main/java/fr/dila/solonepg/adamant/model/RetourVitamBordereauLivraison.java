package fr.dila.solonepg.adamant.model;

import javax.persistence.Entity;

@Entity(name = RetourVitamBordereauLivraison.ENTITY)
public class RetourVitamBordereauLivraison extends AbstractVitamCommonData {
    public static final String ENTITY = "RETOUR_VITAM_BORDEREAU_LIVRAISON";

    public RetourVitamBordereauLivraison() {
        super();
    }
}
