package fr.dila.solonepg.api.constant;

/**
 * Constantes pour les requêteurs experts des dossiers et l'activité normative
 * @author jgomez
 *
 */
public class SolonEpgRequeteurConstant {
    public static final String REQUETEUR_EXPERT_DOSSIER_ASSEMBLER = "fr.dila.solonepg.query.jointure.contrib";

    public static final String REQUETEUR_ACTIVITE_NORMATIVE_DOSSIER_ASSEMBLER =
        "fr.dila.solonepg.activitenormative.query.jointure.contrib";

    public static final String REQUETEUR_EXPERT_DOSSIER_CONTRIBUTION = "fr.dila.solonepg.core.tests.requeteurtemplate";

    public static final String REQUETEUR_EXPERT_ACTIVITE_NORMATIVE_CONTRIBUTION =
        "fr.dila.solonepg.core.activitenormative.recherche";

    public static final String REQUETEUR_TYPE_DISPOSITION_HABILITATION = "disposition_habilitation";

    private SolonEpgRequeteurConstant() {
        //Private default constructor
    }
}
