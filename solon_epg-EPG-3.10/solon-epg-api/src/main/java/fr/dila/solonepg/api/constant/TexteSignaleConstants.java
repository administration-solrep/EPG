package fr.dila.solonepg.api.constant;

import fr.dila.solonepg.api.cases.TexteSignale;

/**
 * Constantes des {@link TexteSignale}
 * 
 * @author asatre
 * 
 */
public final class TexteSignaleConstants {

    public static final String TEXTE_SIGNALE_SCHEMA = "texte_signale";

    public static final String TEXTE_SIGNALE_PREFIX = "ts";

    public static final String TEXTE_SIGNALE_DOCUMENT_TYPE = "TexteSignale";

    public static final String DATE_DEMANDE_PUBLICATION_TS = "dateDemandePublicationTS";

    public static final String VECTEUR_PUBLICATION_TS = "vecteurPublicationTS";

    public static final String OBSERVATION_TS = "observationTS";

    public static final String ID_DOSSIER_TS = "idDossier";

    public static final String TYPE = "type_ts";

    public static final String TEXTE_SIGNALE_PATH = "/case-management/texte-signale-root";

    private TexteSignaleConstants() {
        // private empty constructor
    }

}
