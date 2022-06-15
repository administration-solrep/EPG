package fr.dila.solonepg.adamant;

import com.google.common.collect.ImmutableList;
import fr.dila.solonepg.adamant.batch.ExtractionAdamantBatchListener;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.st.api.event.batch.BatchResultModel;
import java.util.List;

/**
 * Liste de constantes diverses associées au module Adamant.
 *
 * @author tlombard
 */
public class SolonEpgAdamantConstant {
    /**
     * Chemin du répertoire d'archivage
     */
    public static final String SOLON_ARCHIVAGE_REPERTOIRE = "solon.archivage.definitif.repertoire";
    /**
     * Model de validation
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_BALISE_HREF = "solon.archivage.definitif.balise.href";
    /**
     * Archivage définitif - Archival Agreement
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_ARCHIVAL_AGREEMENT =
        "solon.archivage.definitif.archival.agreement";
    /**
     * Archivage définitif - message digest algorithm code list version
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_MESSAGE_DIGEST_ALGORITHM =
        "solon.archivage.definitif.message.digest.algorithme";
    /**
     * Archivage définitif - Mime type code list version
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_MIME_TYPE = "solon.archivage.definitif.mime.type";
    /**
     * Archivage définitif - File format code list version
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_FILE_FORMAT = "solon.archivage.definitif.file.format";
    /**
     * Archivage définitif - Appraisal rule code list version
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_APPRAISAL_RULE_CODE_LIST_VERSION =
        "solon.archivage.definitif.appraisal.rule.code.list.version";
    /**
     * Archivage définitif - access rule code list version
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_ACCESS_RULE_CODE_LIST_VERSION =
        "solon.archivage.definitif.access.rule.code.list.version";
    /**
     * Archivage définitif - Dissemination rule code list version
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_DISSEMINATION_RULE_CODE_LIST_VERSION =
        "solon.archivage.definitif.dissemination.rule.code.list.version";
    /**
     * Archivage définitif - Reuse rule code list version
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_REUSE_RULE_CODE_LIST_VERSION =
        "solon.archivage.definitif.reuse.rule.code.list.version";
    /**
     * Archivage définitif - Reply code list version
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_REPLY_CODE_LIST_VERSION =
        "solon.archivage.definitif.reply.code.list.version";
    /**
     * Archivage définitif - Encoding code list version
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_ENCODING_CODE_LIST_VERSION =
        "solon.archivage.definitif.encoding.code.list.version";
    /**
     * Archivage définitif - Compression Algorithm code list version
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_COMPRESSION_ALGORITHM_CODE_LIST_VERSION =
        "solon.archivage.definitif.compression.algorithm.code.list.version";
    /**
     * Archivage définitif - Data object version code list version
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_DATA_OBJECT_VERSION_CODE_LIST_VERSION =
        "solon.archivage.definitif.data.object.version.code.list.version";
    /**
     * Archivage définitif - Storage rule code list version
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_STORAGE_RULE_CODE_LIST_VERSION =
        "solon.archivage.definitif.storage.rule.code.list.version";
    /**
     * Archivage définitif - Classification rule code list version
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_CLASSIFICATION_RULE_CODE_LIST_VERSION =
        "solon.archivage.definitif.classification.rule.code.list.version";
    /**
     * Archivage définitif - Relationship code list version
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_RELATIONSHIP_CODE_LIST_VERSION =
        "solon.archivage.definitif.relationship.code.list.version";

    /**
     * Archivage définitif - Authorization code list version
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_AUTHORIZATION_CODE_LIST_VERSION =
        "solon.archivage.definitif.authorization.code.list.version";

    /**
     * Archivage définitif - Acquisition information code list version
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_ACQUISITION_INFORMATION =
        "solon.archivage.definitif.acquisition.information";
    /**
     * Archivage définitif - Niveau de service
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_SERVICE_LEVEL = "solon.archivage.definitif.service.level";
    /**
     * Archivage définitif - Archival agency identifier
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_ARCHIVAL_AGENCY_IDENTIFIER =
        "solon.archivage.definitif.archival.acency.identifier";
    /**
     * Archivage définitif - Transfering agency identifier
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_TRANSFERING_AGENCY_IDENTIFIER =
        "solon.archivage.definitif.transfering.agency.identifier";

    /**
     * Archivage définitif : appraising rule
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_APPRAISAL_RULE =
        "solon.archivage.definitif.appraisal.rule.rule";

    /**
     * Archivage définitif : appraising rule
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_APPRAISAL_RULE_FINAL_ACTION =
        "solon.archivage.definitif.appraisal.rule.final.action";

    /**
     * Archivage définitif access rule
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_ACCESS_RULE = "solon.archivage.definitif.access.rule";

    /**
     * Archivage définitif access rule list ACC-00002
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_ACCESS_RULE_LIST_ACC_00002 =
        "solon.archivage.definitif.access.rule.list.ACC_00002";

    /**
     * Archivage définitif access rule list ACC-00003
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_ACCESS_RULE_LIST_ACC_00003 =
        "solon.archivage.definitif.access.rule.list.ACC_00003";

    /**
     * Archivage définitif access rule list ACC-00020
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_ACCESS_RULE_LIST_ACC_00020 =
        "solon.archivage.definitif.access.rule.list.ACC_00020";

    /**
     * Archivage définitif access rule list ACC-00025
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_ACCESS_RULE_LIST_ACC_00025 =
        "solon.archivage.definitif.access.rule.list.ACC_00025";

    /**
     * Archivage définitif dissemination rule
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_DISSEMINATION_RULE =
        "solon.archivage.definitif.dissemination.rule.rule";

    /**
     * Archivage définitif reuse rule
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_REUSE_RULE = "solon.archivage.definitif.reuse.rule.rule";

    /**
     * Chemin du répertoire VITAM todo
     */
    public static final String SOLON_RETOUR_VITAM_TODO_REP = "solon.retour.vitam.todo.repertoire";

    /**
     * Chemin du répertoire VITAM done
     */
    public static final String SOLON_RETOUR_VITAM_DONE_REP = "solon.retour.vitam.done.repertoire";

    /**
     * Chemin du répertoire VITAM error
     */
    public static final String SOLON_RETOUR_VITAM_ERROR_REP = "solon.retour.vitam.error.repertoire";

    // Document qui n'ont pas de writeBloc
    public static final List<String> DOC_WITHOUT_WRITE_BLOC_LIST = ImmutableList.of(
        SolonEpgAdamantConstant.BORDEREAU,
        SolonEpgAdamantConstant.FEUILLE_ROUTE,
        SolonEpgAdamantConstant.JOURNAL,
        SolonEpgAdamantConstant.TRAITEMENT_PAPIER,
        SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER,
        SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_JOINTE,
        SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_DOC_NON_CLASIFIED
    );

    /**
     * Archivage définitif - Nombre max de NORs de dossiers avec erreurs
     * d'extraction à afficher par {@link BatchResultModel} durant le suivi
     * d'une occurrence du batch {@link ExtractionAdamantBatchListener}.
     */
    public static final String SOLON_ARCHIVAGE_DEFINITIF_BATCH_SUIVI_MAX_ERREURS =
        "solon.archivage.definitif.batch.suivi.max.erreurs";

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static final String ARCHIVAGE_LOG = "ARCHIVAGE-LOG";

    /**
     * Libellé des conteneur du dossier
     */

    public static final String BORDEREAU_PDF = "bordereau.pdf";

    public static final String JOURNAL_PDF = "journal.pdf";

    public static final String FEUILLE_ROUTE_PDF = "feuilleRoute.pdf";

    public static final String TRAITEMENT_PAPIER_PDF = "traitementPapier.pdf";

    /**
     * Libellé onglet du dossier
     */

    public static final String BORDEREAU = "Bordereau";

    public static final String FEUILLE_ROUTE = "Feuille de route";

    public static final String JOURNAL = "Journal";

    public static final String TRAITEMENT_PAPIER = "Traitement papier";

    /*
     * Retour VITAM
     */
    public static final String RETOUR_VITAM_LIVRAISON_OK = "solon.retour.vitam.libelle.livraison.ok";

    public static final String RETOUR_VITAM_LIVRAISON_KO = "solon.retour.vitam.libelle.livraison.ko";

    public static final String RETOUR_VITAM_VERSEMENT_OK = "solon.retour.vitam.libelle.versement.ok";

    public static final String RETOUR_VITAM_VERSEMENT_KO = "solon.retour.vitam.libelle.versement.ko";
}
