package fr.dila.solonepg.core.service;

import static fr.dila.ss.core.util.SSPdfUtil.addTableRow;
import static fr.dila.ss.core.util.SSPdfUtil.addTitle;
import static fr.dila.ss.core.util.SSPdfUtil.createHeaderCell;
import static fr.dila.ss.core.util.SSPdfUtil.createTextCell;

import com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.DonneesSignataire;
import fr.dila.solonepg.api.cases.typescomplexe.InfoEpreuve;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants;
import fr.dila.solonepg.api.constant.SolonEpgTraitementPapierConstants;
import fr.dila.solonepg.api.documentmodel.ListeTraitementPapier;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.service.ListeTraitementPapierService;
import fr.dila.solonepg.core.cases.typescomplexe.DonneesSignataireImpl;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.core.util.SSPdfUtil;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.FileUtils;
import fr.dila.st.core.util.ResourceHelper;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.UnrestrictedSessionRunner;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.nuxeo.ecm.core.query.sql.model.DateLiteral;
import org.nuxeo.ecm.core.schema.PrefetchInfo;

/**
 * classe d'implemetation de la liste de traitement Papier
 *
 * @author admin
 */
public class ListeTraitementPapierServiceImpl implements ListeTraitementPapierService {
    private static final String SELECT_FROM_TRAITEMENT_PAPIER_LISTE = "SELECT * FROM ListeTraitementPapier WHERE ";

    /**
     * recuperer le nombre de liste de traitement papier
     *
     * @param session
     *            la session
     * @param typeListe
     *            type de la liste
     * @param typeSignature
     *            type de signature
     * @return le nombre de liste de traitement papier
     */
    private Long getListeTraitementPapierCount(
        final CoreSession session,
        final String typeListe,
        final String typeSignature
    ) {
        Calendar dua = Calendar.getInstance();
        final String currentDate = DateLiteral.dateFormatter.print(dua.getTimeInMillis());
        dua = Calendar.getInstance();
        dua.add(Calendar.DAY_OF_YEAR, +1);
        final String tomorrowLiteralDate = DateLiteral.dateFormatter.print(dua.getTimeInMillis());

        final StringBuilder query = new StringBuilder(SELECT_FROM_TRAITEMENT_PAPIER_LISTE);

        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
        query.append(":");
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_TYPE_LISTE_PROPERTY);
        query.append("='");
        query.append(typeListe);
        query.append("' and ");
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
        query.append(":");
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DATE_GENERATION_LISTE_PROPERTY);
        query.append("< TIMESTAMP '");
        query.append(tomorrowLiteralDate);
        query.append(" ' and ");
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
        query.append(":");
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DATE_GENERATION_LISTE_PROPERTY);
        query.append(">= TIMESTAMP '");
        query.append(currentDate);
        query.append(" ' ");
        query.append(" and ecm:isProxy = 0 ");

        if (StringUtils.isNotBlank(typeSignature)) {
            query.append(" and ");
            query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
            query.append(":");
            query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_TYPE_SIGNATURE);
            query.append("='");
            query.append(typeSignature);
            query.append("' ");
        }
        return QueryUtils.doCountQuery(session, query.toString());
    }

    /**
     * recuperer la liste de traitement papier an se basant sur le type et un intervale de date de création
     */
    @Override
    public DocumentModelList getListeTraitementPapierFromTypeAndDate(
        final CoreSession session,
        final String dateDebut,
        final String dateFin,
        final List<String> typeList
    ) {
        final StringBuilder query = new StringBuilder(SELECT_FROM_TRAITEMENT_PAPIER_LISTE);
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
        query.append(":");
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_TYPE_LISTE_PROPERTY);
        query.append(" IN ");
        query.append(typeList.stream().collect(Collectors.joining("','", "('", "')")));
        query.append(" AND ");
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
        query.append(":");
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DATE_GENERATION_LISTE_PROPERTY);
        query.append(" BETWEEN TIMESTAMP '" + dateDebut);
        query.append("' AND TIMESTAMP '" + dateFin);
        query.append("' AND ecm:isProxy = 0 ORDER BY ");
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
        query.append(":");
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DATE_GENERATION_LISTE_PROPERTY);

        return session.query(query.toString());
    }

    /**
     * recuperer la liste de traitement papier an basant sur le dossier et la type
     *
     * @param session
     *            la session
     * @param docId
     *            le dossier Id
     * @param typeListe
     *            type de la liste
     * @return
     */
    @Override
    public DocumentModelList getListeTraitementPapierOfDossierAndType(
        final CoreSession session,
        final String docId,
        final String typeListe
    ) {
        final StringBuilder query = new StringBuilder(SELECT_FROM_TRAITEMENT_PAPIER_LISTE);
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
        query.append(":");
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_TYPE_LISTE_PROPERTY);
        query.append("='");
        query.append(typeListe);
        query.append("' and ");
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
        query.append(":");
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_IDS_DOSSIER_PROPERTY);
        query.append("='" + docId);
        query.append("'");
        query.append(" and ecm:isProxy = 0 ");

        return session.query(query.toString());
    }

    /**
     * recuperer la liste de traitement papier an basant sur la liste dossiers et la type
     *
     * @param session
     *            la session
     * @param docsId
     *            la liste des dossier Id
     * @param typeListe
     *            type de la liste
     * @return
     */
    @Override
    public DocumentModelList getListeTraitementPapierOfDossierListAndType(
        final CoreSession session,
        final List<String> docsId,
        final String typeListe
    ) {
        final StringBuilder query = new StringBuilder(SELECT_FROM_TRAITEMENT_PAPIER_LISTE);
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
        query.append(":");
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_TYPE_LISTE_PROPERTY);
        query.append("='");
        query.append(typeListe);
        query.append("' and ");
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_SCHEMA_PREFIX);
        query.append(":");
        query.append(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_IDS_DOSSIER_PROPERTY);
        query.append("  IN ('");
        query.append(StringUtils.join(docsId, "','"));
        query.append("')");
        query.append(" and ecm:isProxy = 0 ");

        return session.query(query.toString());
    }

    /**
     * creer une Nouvelle Liste de Traitement Papier pour Mise en signature
     *
     * @param session
     *            la session
     * @param documentIdsList
     *            la liste des dossiers de la lite a creer
     * @param typeSignature
     *            type de signataire
     */
    @Override
    public ListeTraitementPapier creerNouvelleListeTraitementPapierMiseEnSignature(
        final CoreSession session,
        final String typeSignataire,
        final List<DocumentModel> documentsList
    ) {
        final List<String> docIdsList = new ArrayList<>();

        for (final DocumentModel dossierDoc : documentsList) {
            docIdsList.add(dossierDoc.getId());
        }

        final ListeTraitementPapier liste = creerNouvelleListeTraitementPapier(
            session,
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_MISE_EN_SIGNATURE,
            typeSignataire,
            docIdsList
        );
        initDonneesSignataireUnrestricted(session, documentsList, typeSignataire);
        return liste;
    }

    /**
     * creer une Nouvelle Liste de Traitement Papier pour Demandes D'epreuves
     *
     * @param session
     *            la session
     * @param documentIdsList
     *            la liste des dossiers de la lite a creer
     */
    @Override
    public void creerNouvelleListeTraitementPapierDemandesEpreuve(
        final CoreSession session,
        final List<DocumentModel> documentsList
    ) {
        final List<String> docIdsList = new ArrayList<>();
        for (final DocumentModel dossierDoc : documentsList) {
            docIdsList.add(dossierDoc.getId());
        }
        final ListeTraitementPapier liste = creerNouvelleListeTraitementPapier(
            session,
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE,
            "",
            docIdsList
        );
        initDonneesDemandesEpreuvesUnrestricted(
            session,
            documentsList,
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_EPREUVE_LABEL +
            liste.getNumeroListe()
        );
    }

    /**
     * creer une Nouvelle Liste de Traitement Papier pour Demandes De Publication
     *
     * @param session
     *            la session
     * @param documentIdsList
     *            la liste des dossiers de la lite a creer
     */
    @Override
    public void creerNouvelleListeTraitementPapierDemandesDePublication(
        final CoreSession session,
        final List<DocumentModel> documentsList
    ) {
        final List<String> docIdsList = new ArrayList<>();

        for (final DocumentModel dossierDoc : documentsList) {
            docIdsList.add(dossierDoc.getId());
        }
        final ListeTraitementPapier liste = creerNouvelleListeTraitementPapier(
            session,
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION,
            "",
            docIdsList
        );
        initDonneesDemandePublicationUnrestricted(
            session,
            documentsList,
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DEMANDE_PUBLICATION_LABEL +
            liste.getNumeroListe()
        );
    }

    /**
     * Traiter en serie les dates des dossiers pour la liste de traitement de papiers En signature SGG
     *
     * @param session
     *            la session
     * @param docList
     *            the list de dossirs a traiter
     * @param property
     *            le nom de la propreiete a traiter en serie
     * @param value
     *            la nouvelle date
     */
    @Override
    public void traiterEnSerieSignataireSGGUnrestricted(
        final CoreSession session,
        final List<DocumentModel> docList,
        final Calendar value
    ) {
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                for (final DocumentModel documentModel : docList) {
                    final TraitementPapier traitementPapier = documentModel.getAdapter(TraitementPapier.class);
                    final DonneesSignataire donneesSignataire = traitementPapier.getSignatureSgg();
                    donneesSignataire.setDateRetourSignature(value);
                    traitementPapier.setSignatureSgg(donneesSignataire);
                    traitementPapier.save(session);
                }
                session.save();
            }
        }
        .runUnrestricted();
    }

    /**
     * Traiter en serie les dates des dossiers pour la liste de traitement de papiers En signature PM
     *
     * @param session
     *            la session
     * @param docList
     *            the list de dossirs a traiter
     * @param property
     *            le nom de la propreiete a traiter en serie
     * @param value
     *            la nouvelle date
     */
    @Override
    public void traiterEnSerieSignatairePMUnrestricted(
        final CoreSession session,
        final List<DocumentModel> docList,
        final Calendar value
    ) {
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                for (final DocumentModel documentModel : docList) {
                    final TraitementPapier traitementPapier = documentModel.getAdapter(TraitementPapier.class);
                    final DonneesSignataire donneesSignataire = traitementPapier.getSignaturePm();
                    donneesSignataire.setDateRetourSignature(value);
                    traitementPapier.setSignaturePm(donneesSignataire);
                    traitementPapier.save(session);
                }
                session.save();
            }
        }
        .runUnrestricted();
    }

    /**
     * Traiter en serie les dates des dossiers pour la liste de traitement de papiers En signature PR
     *
     * @param session
     *            la session
     * @param docList
     *            the list de dossirs a traiter
     * @param property
     *            le nom de la propreiete a traiter en serie
     * @param value
     *            la nouvelle date
     */
    @Override
    public void traiterEnSerieSignatairePRUnrestricted(
        final CoreSession session,
        final List<DocumentModel> docList,
        final Calendar dateRetour,
        final Calendar dateEnvoi
    ) {
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                for (final DocumentModel documentModel : docList) {
                    final TraitementPapier traitementPapier = documentModel.getAdapter(TraitementPapier.class);
                    final DonneesSignataire donneesSignataire = traitementPapier.getSignaturePr();
                    if (dateRetour != null) {
                        donneesSignataire.setDateRetourSignature(dateRetour);
                    }
                    if (dateEnvoi != null) {
                        donneesSignataire.setDateEnvoiSignature(dateEnvoi);
                    }
                    traitementPapier.setSignaturePr(donneesSignataire);
                    traitementPapier.save(session);

                    updateFichePresentationAfterUpdateBordereau(session, documentModel);
                }
                session.save();
            }
        }
        .runUnrestricted();
    }

    @Override
    public void updateFichePresentationAfterUpdateBordereau(CoreSession session, DocumentModel doc) {
        final EventProducer eventProducer = STServiceLocator.getEventProducer();
        final Map<String, Serializable> eventPropertiesFicheDR = ImmutableMap.of(
            SolonEpgEventConstant.DOSSIER_EVENT_PARAM,
            doc,
            SolonEpgEventConstant.TYPE_ACTE_EVENT_PARAM,
            SolonEpgEventConstant.TYPE_ACTE_FL_EVENT_VALUE
        );

        final InlineEventContext inlineEventContextFP = new InlineEventContext(
            session,
            session.getPrincipal(),
            eventPropertiesFicheDR
        );
        eventProducer.fireEvent(
            inlineEventContextFP.newEvent(SolonEpgEventConstant.BATCH_EVENT_UPDATE_FICHE_AFTER_UPDATE_BORDEREAU)
        );
    }

    /**
     * Traiter en serie les dates des dossiers pour la liste de traitement de papiers demande d'epreuve
     *
     * @param session
     *            la session
     * @param docList
     *            the list de dossirs a traiter
     * @param property
     *            le nom de la propreiete a traiter en serie
     * @param value
     *            la nouvelle date pour le
     */
    @Override
    public void traiterEnSerieDemandeEpreuveUnrestricted(
        final CoreSession session,
        final List<DocumentModel> docList,
        final Calendar value
    ) {
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                docList
                    .stream()
                    .map(d -> d.getAdapter(TraitementPapier.class))
                    .forEach(
                        traitementPapier -> {
                            if (traitementPapier.getNouvelleDemandeEpreuves().getEpreuveDemandeLe() == null) {
                                InfoEpreuve epreuve = traitementPapier.getEpreuve();
                                epreuve.setEpreuvePourLe(value);
                                traitementPapier.setEpreuve(epreuve);
                            } else {
                                InfoEpreuve epreuve = traitementPapier.getNouvelleDemandeEpreuves();
                                epreuve.setEpreuvePourLe(value);
                                traitementPapier.setNouvelleDemandeEpreuves(epreuve);
                            }
                            traitementPapier.save(session);
                        }
                    );
                session.save();
            }
        }
        .runUnrestricted();
    }

    /**
     * creer une Nouvelle Liste de Traitement Papier
     *
     * @param session
     * @param typeListe
     * @param typeSignataire
     * @param documentIdsList
     */
    private ListeTraitementPapier creerNouvelleListeTraitementPapier(
        final CoreSession session,
        final String typeListe,
        String typeSignataire,
        final List<String> documentIdsList
    ) {
        if (StringUtils.isNotEmpty(typeSignataire.trim())) {
            if (typeSignataire.equals(SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_SGG)) {
                typeSignataire = "SGG";
            } else if (typeSignataire.equals(SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_PM)) {
                typeSignataire = "PM";
            } else if (typeSignataire.equals(SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_PR)) {
                typeSignataire = "PR";
            } else if (typeSignataire.equals(SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_AUCUN)) {
                typeSignataire = "";
            }
        }
        final Long listeTraitementPapierCount = getListeTraitementPapierCount(session, typeListe, typeSignataire);
        final Long numeroDeListe = listeTraitementPapierCount + 1;

        final DocumentModel listeTraitementPapierModel = session.createDocumentModel(
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_PATH,
            UUID.randomUUID().toString(),
            SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_DOCUMENT_TYPE
        );

        ListeTraitementPapier listeTraitementPapier = listeTraitementPapierModel.getAdapter(
            ListeTraitementPapier.class
        );

        final Calendar calendar = Calendar.getInstance();

        listeTraitementPapier.setDateGenerationListe(calendar);
        listeTraitementPapier.setNumeroListe(numeroDeListe);
        listeTraitementPapier.setTypeListe(typeListe);
        listeTraitementPapier.setTypeSignature(typeSignataire);
        listeTraitementPapier.setIdsDossier(documentIdsList);
        return session.createDocument(listeTraitementPapierModel).getAdapter(ListeTraitementPapier.class);
    }

    /**
     * initialises donnees signatures
     *
     * @param session
     * @param docList
     * @param typeSignataire
     */
    private void initDonneesSignataireUnrestricted(
        final CoreSession session,
        final List<DocumentModel> docList,
        final String typeSignataire
    ) {
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                final DonneesSignataire donneesSignataire = new DonneesSignataireImpl();
                donneesSignataire.setDateEnvoiSignature(Calendar.getInstance());

                if (typeSignataire.equals(SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_SGG)) {
                    for (final DocumentModel dossierDoc : docList) {
                        final TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
                        traitementPapier.setSignatureSgg(donneesSignataire);
                        traitementPapier.save(session);
                    }
                } else if (typeSignataire.equals(SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_PM)) {
                    for (final DocumentModel dossierDoc : docList) {
                        final TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
                        traitementPapier.setSignaturePm(donneesSignataire);
                        traitementPapier.save(session);
                    }
                }
                session.save();
            }
        }
        .runUnrestricted();
    }

    /**
     * initialiser les donnes pour demandes epreuves
     *
     * @param session
     * @param docList
     * @param numeroDeListe
     */
    private void initDonneesDemandesEpreuvesUnrestricted(
        final CoreSession session,
        final List<DocumentModel> docList,
        final String numeroDeListe
    ) {
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                docList
                    .stream()
                    .map(d -> d.getAdapter(TraitementPapier.class))
                    .forEach(
                        traitementPapier -> {
                            if (traitementPapier.getEpreuve().getEpreuveDemandeLe() == null) {
                                InfoEpreuve epreuve = traitementPapier.getEpreuve();
                                epreuve.setEpreuveDemandeLe(Calendar.getInstance());
                                epreuve.setNumeroListe(numeroDeListe);
                                traitementPapier.setEpreuve(epreuve);
                            } else {
                                InfoEpreuve epreuve = traitementPapier.getNouvelleDemandeEpreuves();
                                epreuve.setEpreuveDemandeLe(Calendar.getInstance());
                                epreuve.setNumeroListe(numeroDeListe);
                                traitementPapier.setNouvelleDemandeEpreuves(epreuve);
                            }
                            traitementPapier.save(session);
                        }
                    );

                session.save();
            }
        }
        .runUnrestricted();
    }

    /**
     * initialiser les donnes pour demandes de publication
     *
     * @param session
     * @param docList
     * @param numeroDeListe
     */
    private void initDonneesDemandePublicationUnrestricted(
        final CoreSession session,
        final List<DocumentModel> docList,
        final String numeroDeListe
    ) {
        new UnrestrictedSessionRunner(session) {

            @Override
            public void run() {
                for (final DocumentModel dossierDoc : docList) {
                    final TraitementPapier traitementPapier = dossierDoc.getAdapter(TraitementPapier.class);
                    traitementPapier.setPublicationDateEnvoiJo(Calendar.getInstance());
                    traitementPapier.setPublicationNumeroListe(numeroDeListe);
                    traitementPapier.save(session);
                }
                session.save();
            }
        }
        .runUnrestricted();
        // infoEpreuve.setDestinataireEntete(DestinataireEntete);
    }

    @Override
    public File exportPdfListe(CoreSession session, DocumentModel listeDoc) throws IOException {
        ListeTraitementPapier liste = listeDoc.getAdapter(ListeTraitementPapier.class);
        String nameListe =
            StringUtils.defaultString(StringUtils.upperCase(liste.getTypeSignature())) + liste.getNumeroListe();
        String filename = FileUtils.generateCompletePdfFilename("Liste_de_mise_en_signature_" + nameListe);
        try (XWPFDocument document = new XWPFDocument()) {
            if (CollectionUtils.isNotEmpty(liste.getIdsDossier())) {
                addTitle(
                    document,
                    ResourceHelper.getString("traitement.papier.liste.export.title", nameListe),
                    SSPdfUtil.SIZE_TITRE1
                );
                XWPFTable table = document.createTable();
                addTableRow(
                    table,
                    true,
                    SSPdfUtil.FOND_HEADER,
                    createHeaderCell(ResourceHelper.getString("traitement.papier.liste.colonne.intituleMin")),
                    createHeaderCell(ResourceHelper.getString("traitement.papier.liste.colonne.nor")),
                    createHeaderCell(ResourceHelper.getString("traitement.papier.liste.colonne.titreText"))
                );

                List<DocumentModel> docs = session.getDocuments(
                    liste.getIdsDossier(),
                    new PrefetchInfo(
                        StringUtils.join(
                            new String[] {
                                DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_XPATH,
                                DossierSolonEpgConstants.DOSSIER_TITRE_ACTE_XPATH,
                                DossierSolonEpgConstants.DOSSIER_MINISTERE_RESPONSABLE_XPATH
                            },
                            ","
                        )
                    )
                );

                for (DocumentModel doc : docs) {
                    Dossier dossier = doc.getAdapter(Dossier.class);
                    String libelleMin = SolonEpgServiceLocator
                        .getEpgOrganigrammeService()
                        .getOrganigrammeNodeById(dossier.getMinistereResp(), OrganigrammeType.MINISTERE)
                        .getLabel();
                    addTableRow(
                        table,
                        false,
                        createTextCell(libelleMin),
                        createTextCell(dossier.getNumeroNor()),
                        createTextCell(dossier.getTitreActe())
                    );
                }

                // Ajout du fond de dossier
                return SSServiceLocator.getSSPdfService().generatePdf(filename, document);
            } else {
                throw new EPGException(ResourceHelper.getString("traitement.papier.liste.export.error.liste.vide"));
            }
        }
    }
}
