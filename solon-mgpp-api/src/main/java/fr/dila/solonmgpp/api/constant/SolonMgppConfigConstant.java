package fr.dila.solonmgpp.api.constant;

/**
 * Nom des paramètres de configuration (nuxeo.conf) de l'application SOLON MGPP.
 *
 * @author asatre
 */
public final class SolonMgppConfigConstant {
    /**
     * endPoint de SOLON EPP
     */
    public static final String SOLON_EPP_ENDPOINT = "solon.epp.endpoint";

    /**
     * basePath des ws de SOLON EPP
     */
    public static final String SOLON_EPP_BASEPATH = "solon.epp.webservice.basepath";

    /**
     * username de connexion au ws gvt de SOLON EPP
     */
    public static final String SOLON_EPP_USER_NAME = "solon.epp.webservice.gouvernement.user.name";

    /**
     * password de connexion au ws gvt de SOLON EPP
     */
    public static final String SOLON_EPP_USER_PASSSWORD = "solon.epp.webservice.gouvernement.user.password";

    private SolonMgppConfigConstant() {
        // default private constructor
    }
}
