package fr.dila.solonepg.api.constant;

/**
 * Nom des paramètres de configuration (nuxeo.conf) de l'application SOLON EPG.
 *
 * @author jtremeaux
 */
public class SolonEpgConfigConstant {
    /**
     * URL de la DILA où l'on peut envoyer par webservice les informations des dossiers pour publication / epreuvage.
     */
    public static final String DILA_WEBSERVICE_PUBLICATION_URL = "dila.webservice.publication.url";

    /**
     * Nom de l'utilisateur de la DILA qui envoie par webservice les informations des dossiers pour publication /
     * epreuvage.
     */
    public static final String DILA_WEBSERVICE_PUBLICATION_USER_NAME = "dila.webservice.publication.user.name";

    /**
     * Mot de passe de l'utilisateur de la DILA qui envoie par webservice les informations des dossiers pour publication
     * / epreuvage.
     */
    public static final String DILA_WEBSERVICE_PUBLICATION_USER_PASSWORD = "dila.webservice.publication.user.password";

    /**
     * Alias de la clé SSL pour envoyer par webservice les informations des dossiers pour publication / epreuvage. Non
     * utilisé par defaut.
     */
    public static final String DILA_WEBSERVICE_PUBLICATION_ALIAS_KEY_SSL = "dila.webservice.publication.alias.key.ssl";

    /**
     * Nombre d'essai défini avant que l'on arrête de renvoyer par webservice les informations des dossiers pour
     * publication / epreuvage. Si on atteint ce nombre d'essai, on supprime le document utilisé pour rejouer l'envoi de
     * webservice de publication.
     */
    public static final String DILA_WEBSERVICE_PUBLICATION_NB_RETRY_BEFORE_DROP =
        "dila.webservice.publication.nb.retry.before.drop";

    /**
     * Répertoire qui va contenir le stat publie de l'activite normative
     */
    public static final String SOLONEPG_AN_STATS_PUBLIE_DIRECTORY = "solonEpg.activiteNormative.stats.publie.dir";
    public static final String SOLONEPG_AP_STATS_PUBLIE_DIRECTORY = "solonEpg.activiteParlementaire.stats.publie.dir";
    /**
     * Répertoire qui va contenir d'archivage definitive
     */
    public static final String SOLONEPG_ARCHIVAGE_DEFINITIVE_DIRECTORY = "solonEpg.archivage.definitive.dir";
    /**
     * Répertoire qui va contenir le fichier Excel d'injection de gouvernement
     */
    public static final String SOLON_INJECTION_GOUVERNEMENT_DIRECTORY = "solonEpg.injection.gouvernement.excel";

    /**
     * Mode injection en cours : désactive l'envoi au service de publication pour les dossiers en cours d'injection
     */
    public static final String MODE_INJECTION_IN_PROGRESS = "mode.injection.inProgress";

    /**
     * Nom de l'utilisateur du web service eurlex pour transposition des directives
     */
    public static final String EURLEX_WEBSERVICE_USER_NAME = "solon.web.service.eurlex.user.name";

    /**
     * Mot de passe de l'utilisateur du web service eurlex pour transposition des directives
     */
    public static final String EURLEX_WEBSERVICE_MDP = "solon.web.service.eurlex.mdp";

    /**
     * URL du web service eurlex pour transposition des directives
     */
    public static final String EURLEX_WEBSERVICE_URL = "solon.web.service.eurlex.url";

    /**
     * Préfixe de l'URL du web service eurlex pour transposition des directives
     */
    public static final String EURLEX_WEBSERVICE_HOSTNAMES = "solon.web.service.eurlex.hostnames";

    /**
     * Host du proxy utilisé par le web service eurlex pour transposition des directives
     */
    public static final String EURLEX_WEBSERVICE_PROXY_HOST = "solon.web.service.eurlex.proxyHost";

    /**
     * Port du proxy utilisé par le web service eurlex pour transposition des directives
     */
    public static final String EURLEX_WEBSERVICE_PROXY_PORT = "solon.web.service.eurlex.proxyPort";

    /**
     * Protocole du proxy utilisé par le web service eurlex pour transposition des directives
     */
    public static final String EURLEX_WEBSERVICE_PROTOCOL = "solon.web.service.eurlex.protocol";

    /**
     * Batch d'indexation des dossiers : limite du nombre de dossiers à exporter
     */
    public static final String SOLONEPG_BATCH_INDEXATION_DOSSIERS_LIMITE = "solonepg.batch.indexation.dossiers.limite";

    /**
     * Batch d'indexation des dossiers : offset
     */
    public static final String SOLONEPG_BATCH_INDEXATION_DOSSIERS_OFFSET = "solonepg.batch.indexation.dossiers.offset";

    /**
     * Batch d'indexation des dossiers : limite des requêtes unitaires
     */
    public static final String SOLONEPG_BATCH_INDEXATION_DOSSIERS_LIMITE_REQUETE =
        "solonepg.batch.indexation.dossiers.limite.requete";

    /**
     * Répertoire de dépôts de fichiers json créés
     */
    public static final String SOLONEPG_BATCH_INDEXATION_DOSSIERS_DESTINATION =
        "solonepg.batch.indexation.dossiers.destination";

    /**
     * Répertoire de création des fichiers json (emplacement avant déplacement)
     */
    public static final String SOLONEPG_BATCH_INDEXATION_DOSSIERS_DESTINATION_TEMPORAIRE =
        "solonepg.batch.indexation.dossiers.destinationTemporaire";

    /**
     * Répertoire de dépôts de fichiers json créés
     */
    public static final String SOLONEPG_BATCH_INDEXATION_DOSSIERS_THREAD_POOL_SIZE =
        "solonepg.batch.indexation.dossiers.threadPoolSize";

    /**
     * Taille maximum des fichiers indexés en ko.
     */
    public static final String SOLONEPG_BATCH_INDEXATION_DOSSIERS_DOCUMENT_TAILLE_MAXIMUM_KO =
        "solonepg.batch.indexation.dossiers.document.tailleMaximumKo";

    /**
     * Activation de l'indexation en continu.
     */
    public static final String SOLONEPG_BATCH_INDEXATION_DOSSIERS_CONTINUE_ENABLED =
        "solonepg.batch.indexation.dossiers.indexationContinue.enabled";

    /**
     * Mime types des documents qui seront indéxés par l'indexation.
     */
    public static final String SOLONEPG_BATCH_INDEXATION_DOSSIERS_DOCUMENT_AUTORISES =
        "solonepg.batch.indexation.dossiers.document.autorises";

    /**
     * Nombre de caractères minimum que doit contenir une pdf pour etre indéxé
     */
    public static final String SOLONEPG_BATCH_INDEXATION_DOSSIERS_DOCUMENT_PDF_CARACTERES =
        "solonepg.batch.indexation.dossiers.document.pdf.caracteres";

    /**
     * Interfaçage ElasticSearch : chaine avec les URL d'accès des serveurs du cluster ElasticSearch. (Ex http://mon.serveur:9200/)
     */
    public static final String SOLONEPG_ELASTICSEARCH_HOSTS = "solonepg.elasticsearch.hosts";

    /**
     * Interfaçage ElasticSearch : nombre de thread pour le client
     * ElasticSearch.
     */
    public static final String SOLONEPG_ELASTICSEARCH_IO_THREAD_COUNT = "solonepg.elasticsearch.ioThreadCount";

    /**
     * Interfaçage ElasticSearch : nombre de connexion par serveur
     * ElasticSearch.
     */
    public static final String SOLONEPG_ELASTICSEARCH_CONN_PER_ROUTE = "solonepg.elasticsearch.connPerRoute";

    /**
     * Interfaçage ElasticSearch : nombre de connexion total
     */
    public static final String SOLONEPG_ELASTICSEARCH_CON_TOTAL = "solonepg.elasticsearch.conTotal";

    /**
     * Interfaçage ElasticSearch : contexte des métadonnées et du contenu
     * textuel des documents.
     */
    public static final String SOLONEPG_ELASTICSEARCH_CONTEXT_DATA = "solonepg.elasticsearch.context.data";

    /**
     * Interfaçage ElasticSearch : contexte du contenu textuel des documents.
     */
    public static final String SOLONEPG_ELASTICSEARCH_CONTEXT_DOCUMENTS = "solonepg.elasticsearch.context.documents";

    public static final String SOLONEPG_ELASTICSEARCH_INDEX_DOSSIERS = "solonepg.elasticsearch.index.dossiers";

    public static final String SOLONEPG_ELASTICSEARCH_INDEX_DOCUMENTS = "solonepg.elasticsearch.index.documents";

    /**
     * Interfaçage Elasticsearch : activation de la page de recherche libre ->
     * on/off/liste de logins autorisés (séparés par une virgule).
     */
    public static final String SOLONEPG_ELASTICSEARCH_ACTIVATION = "solonepg.elasticsearch.activation";

    public static final String SOLONEPG_GENERATED_REPORT_DIRECTORY = "solonepg.birt.generated.report.dir";
    //SOLON2NG : Répertoire de l'archive SOLON-EDIT
    public static final String SOLON_EDIT_FOLDER = "solonepg.edit.chemin.repertoire";
    //SOLON2NG : Nom de l'archive SOLON-EDIT
    public static final String SOLON_EDIT_FILE = "solonepg.edit.fichier";
    //SOLON2NG : Control key file directory
    public static final String SOLON_EDIT_CONTROL_KEY_FOLDER = "solonepg.edit.controlkey.chemin.repertoire";
    //SOLON2NG : Control key file name
    public static final String SOLON_EDIT_CONTROL_KEY_FILE = "solonepg.edit.controlkey.fichier";

    public static final String SOLONEPG_STATS_REMOVE_ORPHAN_BINARIES = "solonepg.stats.remove.orphan.binaries";

    private SolonEpgConfigConstant() {
        // Private default constructor
    }
}