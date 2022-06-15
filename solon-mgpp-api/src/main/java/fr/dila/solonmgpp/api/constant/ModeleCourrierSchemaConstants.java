package fr.dila.solonmgpp.api.constant;

import static fr.dila.st.api.util.XPathUtils.xPath;

public final class ModeleCourrierSchemaConstants {
    public static final String SCHEMA_NAME = "modele_courrier";
    public static final String SCHEMA_PREFIX = "mdc";
    public static final String DOCUMENT_NAME = "ModeleCourrier";

    public static final String TYPES_COMMUNICATION_PROP = "types_communication";
    public static final String TYPES_COMMUNICATION_XPATH = xPath(SCHEMA_PREFIX, TYPES_COMMUNICATION_PROP);

    public static final String MODELE_COURRIER_TYPES_COMMUNICATION_XPATH =
        ModeleCourrierSchemaConstants.SCHEMA_PREFIX + ":" + ModeleCourrierSchemaConstants.TYPES_COMMUNICATION_PROP;

    private ModeleCourrierSchemaConstants() {}
}
