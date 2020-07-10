package fr.dila.solonepg.core.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.InfoHistoriqueAmpliation;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.AmpliationService;
import fr.dila.solonepg.core.cases.typescomplexe.InfoHistoriqueAmpliationImpl;
import fr.dila.st.api.constant.STParametreConstant;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.service.STMailService;
import fr.dila.st.api.service.STParametreService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.UnrestrictedQueryRunner;

public class AmpliationServiceImpl implements AmpliationService {

    private static final String GET_ALL_AMPLIATION_FICHIER = "select * from AmpliationFichier where ecm:path STARTSWITH '%s' and ecm:isProxy = 0 ";

    private static final String AMPILIATION_REPERTOIRE_NAME = "Ampliation";
    private static final Log LOGGER = LogFactory.getLog(AmpliationServiceImpl.class);
    /**
     * Logger formalisé en surcouche du logger apache/log4j 
     */
    private static final STLogger LOG = STLogFactory.getLog(AmpliationServiceImpl.class);

    /**
     * ajouter un fichier ampliation
     * 
     * @param repertoireParent le repertoire de parent
     * @param fileName le nom d file
     * @param blob le contenu du fichier
     * @param session la session
     * @param currentUser
     * @return
     * @throws ClientException
     */
    @Override
    public DocumentModel ajouterAmpliationFichier(final DocumentModel dossierDoc, final String fileName, final Blob blob, final CoreSession session)
            throws ClientException {
        // on récupère le nom du fichier
        final String docName = fileName;
        DocumentModel docModel = null;

        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        DocumentModel repertoireAmpliation = getAmpliationRepertoire(dossierDoc, session);

        if (repertoireAmpliation == null) {
            repertoireAmpliation = createAmpliationDossierDocument(dossier, session);
            session.save();
        }
        // création du DocumentModel
        docModel = session.createDocumentModel(repertoireAmpliation.getPath().toString(), docName,
                DossierSolonEpgConstants.AMPLIATION_FILE_DOCUMENT_TYPE);
        // set document name
        DublincoreSchemaUtils.setTitle(docModel, docName);
        // set document file properties
        docModel.setProperty(STSchemaConstant.FILE_SCHEMA, STSchemaConstant.FILE_FILENAME_PROPERTY, fileName);
        docModel.setProperty(STSchemaConstant.FILE_SCHEMA, STSchemaConstant.FILE_CONTENT_PROPERTY, blob);

        // set entite
        // creation du document en session
        docModel = session.createDocument(docModel);
        // commit creation
        session.save();

        return docModel;
    }

    /**
     * supprimer un fichier d'ampliation
     * 
     * @param session la session
     * @param ampliationFileDoc le fichir d'ampliation
     * @throws ClientException
     */
    @Override
    public void supprimerAmpliationFichierUnrestricted(final CoreSession session, final DocumentModel dossierDoc) throws ClientException {
        final String query = String.format(GET_ALL_AMPLIATION_FICHIER, dossierDoc.getPath().toString() + "/" + AMPILIATION_REPERTOIRE_NAME);
               
        new UnrestrictedSessionRunner(session) {
            @Override
            public void run() throws ClientException {
                final DocumentModelList list = new UnrestrictedQueryRunner(session, query).findAll();
                if (list != null && list.size() > 0) {
                    LOG.info(session, EpgLogEnumImpl.DEL_AMPLIATION_TEC, list);
                    for (final DocumentModel ampliation : list) {
                        session.removeDocument(ampliation.getRef());
                    }
                }
            }
        }.runUnrestricted();

    }

    /**
     * recuperer le contenu du mail d'ampliation
     * 
     * @param session la session
     * @param dossierDoc le dossier
     * @return
     * @throws ClientException
     */
    @Override
    public String getAmpliationMailText(final CoreSession session, final DocumentModel dossierDoc) throws ClientException {

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
     * @throws ClientException
     */
    @Override
    public String getAmpliationMailObject(final CoreSession session, final DocumentModel dossierDoc) throws ClientException {

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
     * @throws ClientException
     */
    @Override
    public void sendAmpliationMail(final CoreSession session, final DocumentModel dossierDoc, final String objet, final String text)
            throws ClientException {

        // Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        final TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
        final List<String> adresseMailsList = new ArrayList<String>();

        for (final String mail : traitementPapier.getAmpliationDestinataireMails()) {
            adresseMailsList.add(mail);
        }

        final STMailService stMailService = STServiceLocator.getSTMailService();
        final DocumentModel ampliationFichierDoc = getAmpliationFichier(dossierDoc, session);
        if (ampliationFichierDoc != null) {
            try {
                stMailService.sendMailWithAttachment(adresseMailsList, objet, text, ampliationFichierDoc);
            } catch (final Exception e) {
                LOGGER.error("erreur envoie mail Ampliation", e);
                throw new ClientException("erreur envoie mail Ampliation");
            }

            List<InfoHistoriqueAmpliation> infoHistoriqueAmpliationList = traitementPapier.getAmpliationHistorique();
            if (infoHistoriqueAmpliationList == null) {
                infoHistoriqueAmpliationList = new ArrayList<InfoHistoriqueAmpliation>();
            }

            final InfoHistoriqueAmpliation infoHistoriqueAmpliation = new InfoHistoriqueAmpliationImpl();
            infoHistoriqueAmpliation.setDateEnvoiAmpliation(Calendar.getInstance());
            infoHistoriqueAmpliation.setAmpliationDestinataireMails(adresseMailsList);

            infoHistoriqueAmpliationList.add(infoHistoriqueAmpliation);
            traitementPapier.setAmpliationHistorique(infoHistoriqueAmpliationList);
            traitementPapier.save(session);
            session.save();
        }
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

            final String titre = "Ampliation";
            final DocumentModel dossierDoc = dossier.getDocument();
            final DocumentModel ampliationModel = session.createDocumentModel(dossierDoc.getPath().toString(), titre,
                    SolonEpgFondDeDossierConstants.AMPLIATION_DOCUMENT_TYPE);

            final String title = titre + " " + dossier.getNumeroNor();
            DublincoreSchemaUtils.setTitle(ampliationModel, title);

            final DocumentModel ampliation = session.createDocument(ampliationModel);

            return ampliation;
        } catch (final ClientException e) {
            throw new RuntimeException("erreur lors de la creation du document ", e);
        }
    }

    /**
     * 
     * @param doc
     * @param session
     * @return
     * @throws ClientException
     */
    @Override
    public String getNomAmpliationFichier(final DocumentModel doc, final CoreSession session) throws ClientException {

        String fileName = null;
        final String query = String.format(GET_ALL_AMPLIATION_FICHIER, doc.getPath().toString() + "/" + AMPILIATION_REPERTOIRE_NAME);
        final DocumentModelList docs = session.query(query);
        if (docs.size() > 0) {
            fileName = (String) docs.get(0).getProperty(STSchemaConstant.FILE_SCHEMA, STSchemaConstant.FILE_FILENAME_PROPERTY);
        }
        return fileName;
    }

    /**
     * 
     * @param doc
     * @param session
     * @return
     * @throws ClientException
     */
    @Override
    public DocumentModel getAmpliationFichier(final DocumentModel doc, final CoreSession session) throws ClientException {
        DocumentModel file = null;
        final String query = String.format(GET_ALL_AMPLIATION_FICHIER, doc.getPath().toString() + "/" + AMPILIATION_REPERTOIRE_NAME);
        final DocumentModelList docs = session.query(query);
        if (docs.size() > 0) {
            file = docs.get(0);
        }
        return file;
    }

    /**
     * 
     * @param doc
     * @param session
     * @return
     * @throws ClientException
     */
    public DocumentModel getAmpliationRepertoire(final DocumentModel doc, final CoreSession session) throws ClientException {
        DocumentModel repertoire = null;
        final String query = "select * from " + SolonEpgFondDeDossierConstants.AMPLIATION_DOCUMENT_TYPE + " where ecm:path = '"
                + doc.getPath().toString() + "/" + AMPILIATION_REPERTOIRE_NAME + "' and ecm:isProxy = 0 ";
        final DocumentModelList docs = session.query(query);
        if (docs.size() > 0) {
            repertoire = docs.get(0);
        }
        return repertoire;
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
            final SimpleDateFormat sdf = DateUtil.simpleDateFormat("yyyy-MM-dd");
            result = result.replace("${date}", sdf.format(dossier.getDateSignature().getTime()));
        } else {
            result = result.replace("${date}", "-");
        }

        return result;
    }
}
