package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.InfoHistoriqueAmpliation;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.service.AmpliationService;
import fr.dila.solonepg.core.cases.typescomplexe.InfoHistoriqueAmpliationImpl;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.core.exception.STValidationException;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.schema.FileSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.PathRef;

public class AmpliationServiceImpl implements AmpliationService {
    private static final String AMPILIATION_REPERTOIRE_NAME = "Ampliation";

    /**
     * ajouter un fichier ampliation
     *
     * @param repertoireParent le repertoire de parent
     * @param blob le contenu du fichier
     * @param session la session
     * @param currentUser
     * @return
     */
    @Override
    public DocumentModel ajouterAmpliationFichier(
        final DocumentModel dossierDoc,
        final Blob blob,
        final CoreSession session
    ) {
        DocumentModel repertoireAmpliation = getAmpliationRepertoire(session, dossierDoc);

        // création du DocumentModel
        final String docName = blob.getFilename();
        DocumentModel docModel = session.createDocumentModel(
            repertoireAmpliation.getPath().toString(),
            docName,
            DossierSolonEpgConstants.AMPLIATION_FILE_DOCUMENT_TYPE
        );
        // set document name
        DublincoreSchemaUtils.setTitle(docModel, docName);
        // set document file properties
        FileSchemaUtils.setContent(docModel, blob);

        // set entite
        // creation du document en session
        docModel = session.createDocument(docModel);

        return docModel;
    }

    @Override
    public void renommerAmpliationFichier(CoreSession session, DocumentModel dossierDoc, String nouveauNom) {
        DocumentModel ampliationFichier = this.getAmpliationFichier(dossierDoc, session);
        DublincoreSchemaUtils.setTitle(ampliationFichier, nouveauNom);
        session.saveDocument(ampliationFichier);
    }

    @Override
    public void supprimerAmpliationFichiers(CoreSession session, DocumentModel dossierDoc) {
        if (session.hasChild(dossierDoc.getRef(), AMPILIATION_REPERTOIRE_NAME)) {
            session.removeChildren(new PathRef(dossierDoc.getPathAsString(), AMPILIATION_REPERTOIRE_NAME));
        }
    }

    /**
     * recuperer le contenu du mail d'ampliation
     *
     * @param session la session
     * @param dossierDoc le dossier
     * @return
     */
    @Override
    public String getAmpliationMailText(final CoreSession session, final DocumentModel dossierDoc) {
        final STParametreService paramService = STServiceLocator.getSTParametreService();
        final String textMail = paramService.getParametreValue(session, STParametreConstant.AMPLIATION_MAIL_TEXT);

        return replaceAmpliationContenu(dossierDoc, textMail);
    }

    /**
     * recuperer le sujet du mail d'ampliation
     *
     * @param session la session
     * @param dossierDoc le dossier
     * @return
     */
    @Override
    public String getAmpliationMailObject(final CoreSession session, final DocumentModel dossierDoc) {
        final STParametreService paramService = STServiceLocator.getSTParametreService();
        final String objectMail = paramService.getParametreValue(session, STParametreConstant.AMPLIATION_MAIL_OBJET);

        return replaceAmpliationContenu(dossierDoc, objectMail);
    }

    /**
     * sendAmpliation Mail
     *
     * @param session
     * @param dossierDoc
     * @param objet
     * @param text
     */
    @Override
    public void sendAmpliationMail(
        final CoreSession session,
        final DocumentModel dossierDoc,
        final String expediteur,
        List<String> destinatairesEmails,
        final String objet,
        final String text
    ) {
        final TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);

        if (CollectionUtils.isEmpty(destinatairesEmails)) {
            throw new STValidationException("traitement.papier.ampliation.pas.de.destinataire.error");
        }

        final STMailService stMailService = STServiceLocator.getSTMailService();
        final DocumentModel ampliationFichierDoc = getAmpliationFichier(dossierDoc, session);
        if (ampliationFichierDoc == null) {
            throw new STValidationException("traitement.papier.ampliation.pas.de.fichier.error");
        }

        stMailService.sendMailWithAttachment(destinatairesEmails, expediteur, objet, text, ampliationFichierDoc);

        List<InfoHistoriqueAmpliation> infoHistoriqueAmpliationList = traitementPapier.getAmpliationHistorique();
        if (infoHistoriqueAmpliationList == null) {
            infoHistoriqueAmpliationList = new ArrayList<>();
        }

        final InfoHistoriqueAmpliation infoHistoriqueAmpliation = new InfoHistoriqueAmpliationImpl();
        infoHistoriqueAmpliation.setDateEnvoiAmpliation(Calendar.getInstance());
        infoHistoriqueAmpliation.setAmpliationDestinataireMails(destinatairesEmails);

        infoHistoriqueAmpliationList.add(infoHistoriqueAmpliation);
        traitementPapier.setAmpliationHistorique(infoHistoriqueAmpliationList);
        traitementPapier.save(session);
        session.save();

        STServiceLocator
            .getJournalService()
            .journaliserAction(
                session,
                dossierDoc,
                SolonEpgEventConstant.TRAITEMENT_PAPIER_UPDATE,
                ResourceHelper.getString(
                    "traitement.papier.ampliation.journal.comment",
                    ampliationFichierDoc.getTitle(),
                    StringUtils.join(destinatairesEmails, ", ")
                ),
                SolonEpgEventConstant.CATEGORY_TRAITEMENT_PAPIER
            );
    }

    /**
     * création du document Ampliation à partir du dossier
     *
     * @param dossier
     * @param session
     * @return
     */
    @Override
    public DocumentModel createAmpliationDossierDocument(final Dossier dossier, final CoreSession session) {
        try {
            final String titre = AMPILIATION_REPERTOIRE_NAME;
            final DocumentModel dossierDoc = dossier.getDocument();
            final DocumentModel ampliationModel = session.createDocumentModel(
                dossierDoc.getPath().toString(),
                titre,
                SolonEpgFondDeDossierConstants.AMPLIATION_DOCUMENT_TYPE
            );

            final String title = titre + " " + dossier.getNumeroNor();
            DublincoreSchemaUtils.setTitle(ampliationModel, title);

            return session.createDocument(ampliationModel);
        } catch (final NuxeoException e) {
            throw new NuxeoException("erreur lors de la creation du document ", e);
        }
    }

    /**
     *
     * @param doc
     * @param session
     * @return
     */
    @Override
    public String getNomAmpliationFichier(final DocumentModel doc, final CoreSession session) {
        DocumentModel ampliation = getAmpliationFichier(doc, session);
        if (ampliation == null) {
            return null;
        }

        return ampliation.getTitle();
    }

    /**
     *
     * @param doc
     * @param session
     * @return
     */
    @Override
    public DocumentModel getAmpliationFichier(final DocumentModel doc, final CoreSession session) {
        DocumentModel ampliationRepertoire = getAmpliationRepertoire(session, doc);

        DocumentModelList children = session.getChildren(ampliationRepertoire.getRef());

        if (!children.isEmpty()) {
            return children.get(0);
        }

        return null;
    }

    private DocumentModel getAmpliationRepertoire(CoreSession session, DocumentModel dossierDoc) {
        if (!session.hasChild(dossierDoc.getRef(), AMPILIATION_REPERTOIRE_NAME)) {
            DocumentModel ampliationFolder =
                this.createAmpliationDossierDocument(dossierDoc.getAdapter(Dossier.class), session);
            session.save();
            return ampliationFolder;
        }

        return session.getChild(dossierDoc.getRef(), AMPILIATION_REPERTOIRE_NAME);
    }

    /**
     * remplacer les variables du dossier par leurs valeurs
     *
     * @param dossierDoc
     * @param contenu
     * @return
     */
    private String replaceAmpliationContenu(final DocumentModel dossierDoc, final String contenu) {
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        String result = contenu;
        result = result.replace("${NOR}", dossier.getNumeroNor());
        result = result.replace("\\n", "\n");
        if (dossier.getTitreActe() != null) {
            result = result.replace("${titre}", dossier.getTitreActe());
        } else {
            result = result.replace("${titre}", "");
        }
        if (dossier.getDateSignature() != null) {
            result = result.replace("${date}", SolonDateConverter.DATE_DASH_REVERSE.format(dossier.getDateSignature()));
        } else {
            result = result.replace("${date}", "-");
        }

        return result;
    }
}
