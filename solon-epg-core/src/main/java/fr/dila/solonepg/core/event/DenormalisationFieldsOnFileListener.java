package fr.dila.solonepg.core.event;

import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.documentmodel.FileSolonEpg;
import fr.dila.st.core.event.AbstractFilterEventListener;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.Path;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.event.DocumentEventTypes;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

/**
 * Gestion de l'évenement de dénormalisation de champs des fichiers pour pouvoir effectuer une recherche dessus.
 * - On met à jour la propriété dc:nature qui possède les valeurs de l'énumération CategorieFichierRecherche ( avec des valeurs comme Actes, Extrait, AutrePieceParapeur)
 * - On met à jour un champ filepg:relatedDocument qui renvoie à une référence du dossier
 *
 * @author jgomez
 */
public class DenormalisationFieldsOnFileListener extends AbstractFilterEventListener<DocumentEventContext> {
    /** L'emplacement du segment qui donne l'identifiant du dossier : c'est très fragile, mais efficace et ça marche pour le fond
     * de dossier et la parapheur **/
    private static final int DOSSIER_SEGMENT_INDEX = 6;

    /** L'emplacement du segment qui donne  la nature des documents : Extrait, Parapheur, ... **/

    private static final int NATURE_SEGMENT_INDEX = 7;

    private static final Log LOGGER = LogFactory.getLog(DenormalisationFieldsOnFileListener.class);

    public DenormalisationFieldsOnFileListener() {
        super(DocumentEventTypes.BEFORE_DOC_UPDATE, DocumentEventContext.class);
    }

    @Override
    protected void doHandleEvent(final Event event, final DocumentEventContext ctx) {
        // On traite les documents avant qu'ils ne soient sauvegardés

        // Traite uniquement les fichiers de type ParapheurFichier et FondDeDossierFichier
        DocumentModel doc = ctx.getSourceDocument();
        String docType = doc.getType();
        if (
            docType == null ||
            (
                !SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FICHIER_DOCUMENT_TYPE.equals(docType) &&
                !SolonEpgParapheurConstants.PARAPHEUR_FILE_DOCUMENT_TYPE.equals(docType)
            )
        ) {
            return;
        }

        CoreSession session = ctx.getCoreSession();
        DocumentRef refToDossier = getDossierReferenceFromFile(doc);
        long filetypeId = getFiletypeIdFromFile(doc);

        LOGGER.info("Report de l'identifiant du dossier sur le fichier [" + doc.getName() + "]");

        FileSolonEpg file = doc.getAdapter(FileSolonEpg.class);
        file.setRelatedDocument(session.getDocument(refToDossier).getId());
        file.setFiletypeId(filetypeId);
        LOGGER.info(
            "Mise en place de l'attribut filetypeId de valeur " + filetypeId + " sur le fichier [" + doc.getName() + "]"
        );
    }

    private DocumentRef getDossierReferenceFromFile(DocumentModel doc) {
        Path path = doc.getPath();
        Path pathToDossier = path.uptoSegment(DOSSIER_SEGMENT_INDEX);
        return new PathRef(pathToDossier.toString());
    }

    private Long getFiletypeIdFromFile(DocumentModel doc) {
        Path path = doc.getPath();
        String natureTitle = path.segment(NATURE_SEGMENT_INDEX);
        List<String> repFdd = Arrays.asList(
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_MIN_PORTEUR_ET_SGG,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_ALL_USER,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_BORDEREAU_VERSEMENT
        );
        Long filetypeId = null;
        if (SolonEpgParapheurConstants.PARAPHEUR_FOLDER_ACTE_NAME.equals(natureTitle)) {
            filetypeId = VocabularyConstants.FILETYPEID_ACTE;
        } else if (SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EXTRAIT_NAME.equals(natureTitle)) {
            filetypeId = VocabularyConstants.FILETYPEID_EXTRAIT;
        } else if (
            SolonEpgParapheurConstants.PARAPHEUR_FOLDER_PIECE_COMPLEMENTAIRE_NAME.equals(natureTitle) ||
            SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EPREUVES_NAME.equals(natureTitle) ||
            SolonEpgParapheurConstants.PARAPHEUR_FOLDER_RAPPORT_NAME.equals(natureTitle)
        ) {
            filetypeId = VocabularyConstants.FILETYPEID_AUTREPARAPHEUR;
        } else if (repFdd.contains(natureTitle)) {
            filetypeId = VocabularyConstants.FILETYPEID_FONDDOSSIER;
        }
        return filetypeId;
    }
}
