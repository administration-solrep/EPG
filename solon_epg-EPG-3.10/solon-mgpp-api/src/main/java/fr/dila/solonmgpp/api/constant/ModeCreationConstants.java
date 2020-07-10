package fr.dila.solonmgpp.api.constant;

import fr.sword.xsd.solon.epp.ModeCreationVersion;

/**
 * Constantes des modes de creation d'une version
 * @see ModeCreationVersion
 * @author asatre
 *
 */
public final class ModeCreationConstants {

    public static final String BROUILLON_INIT = ModeCreationVersion.BROUILLON_INIT.value();
    public static final String BROUILLON_COMPLETION = ModeCreationVersion.BROUILLON_COMPLETION.value();
    public static final String BROUILLON_RECTIFICATION = ModeCreationVersion.BROUILLON_RECTIFICATION.value();
    public static final String PUBLIE_ANNULATION = ModeCreationVersion.PUBLIE_ANNULATION.value();
    public static final String PUBLIE_COMPLETION = ModeCreationVersion.PUBLIE_COMPLETION.value();
    public static final String PUBLIE_DELTA_DEMANDE_RECTIFICATION = ModeCreationVersion.PUBLIE_DELTA_DEMANDE_RECTIFICATION.value();
    public static final String PUBLIE_DEMANDE_ANNULATION = ModeCreationVersion.PUBLIE_DEMANDE_ANNULATION.value();
    public static final String PUBLIE_DEMANDE_RECTIFICATION = ModeCreationVersion.PUBLIE_DEMANDE_RECTIFICATION.value();
    public static final String PUBLIE_INIT = ModeCreationVersion.PUBLIE_INIT.value();
    public static final String PUBLIE_VALIDATION_ANNULATION = ModeCreationVersion.PUBLIE_VALIDATION_ANNULATION.value();
    public static final String PUBLIE_VALIDATION_RECTIFICATION = ModeCreationVersion.PUBLIE_VALIDATION_RECTIFICATION.value();

    private ModeCreationConstants() {
        // private default constructor
    }

}
