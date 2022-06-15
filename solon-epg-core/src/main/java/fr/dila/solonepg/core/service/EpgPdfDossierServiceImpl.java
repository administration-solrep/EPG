package fr.dila.solonepg.core.service;

import static fr.dila.solonepg.api.constant.VocabularyConstants.VOCABULARY_STATUS_ARCHIVAGE;
import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getFondDeDossierService;
import static fr.dila.ss.core.util.SSPdfUtil.addBreak;
import static fr.dila.ss.core.util.SSPdfUtil.addBreakPage;
import static fr.dila.ss.core.util.SSPdfUtil.addLeftIndentation;
import static fr.dila.ss.core.util.SSPdfUtil.addTableRow;
import static fr.dila.ss.core.util.SSPdfUtil.addTitle;
import static fr.dila.ss.core.util.SSPdfUtil.createDateCell;
import static fr.dila.ss.core.util.SSPdfUtil.createHeaderCell;
import static fr.dila.ss.core.util.SSPdfUtil.createImageCell;
import static fr.dila.ss.core.util.SSPdfUtil.createInvisibleTable;
import static fr.dila.ss.core.util.SSPdfUtil.createTextCell;
import static fr.dila.ss.core.util.SSPdfUtil.mergeCellVertically;
import static fr.dila.ss.core.util.SSPdfUtil.mergePdfs;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.StringUtils.SPACE;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.platform.audit.api.LogEntry;

import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication;
import fr.dila.solonepg.api.cases.typescomplexe.DonneesSignataire;
import fr.dila.solonepg.api.cases.typescomplexe.InfoEpreuve;
import fr.dila.solonepg.api.cases.typescomplexe.InfoHistoriqueAmpliation;
import fr.dila.solonepg.api.cases.typescomplexe.ParutionBo;
import fr.dila.solonepg.api.constant.ActeVisibilityConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgListeTraitementPapierConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.feuilleroute.SolonEpgRouteStep;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFolder;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.ArchiveService;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.EpgPdfDossierService;
import fr.dila.solonepg.api.service.vocabulary.TypeAvisTraitementPapierService;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.api.pdf.Cell;
import fr.dila.ss.api.service.DocumentRoutingService;
import fr.dila.ss.api.service.DossierDistributionService;
import fr.dila.ss.core.pdf.AbstractPdfDossierService;
import fr.dila.ss.core.pdf.SpanPosition;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.api.io.STByteArrayOutputStream;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.FileUtils;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.model.RouteTableElement;

public class EpgPdfDossierServiceImpl extends AbstractPdfDossierService<Dossier> implements EpgPdfDossierService {
    private static final STLogger LOGGER = STLogFactory.getLog(EpgPdfDossierServiceImpl.class);

    @Override
    public STByteArrayOutputStream genererPdfBordereau(CoreSession session, Dossier dossier, boolean archivageDefinitif) {
        return genererPdf(doc -> this.genererDonneesBordereau(doc, dossier, session, archivageDefinitif));
    }

    @Override
    public STByteArrayOutputStream genererPdfFeuilleRoute(
        CoreSession session,
        Dossier dossier,
        boolean archivageDefinitif
    ) {
        return genererPdf(doc -> this.genererTableauFeuilleDeRoute(session, dossier, doc, archivageDefinitif));
    }

    @Override
    public STByteArrayOutputStream genererPdfJournal(Dossier dossier) {
        return genererPdf(doc -> this.genererTableauJournal(doc, dossier));
    }

    @Override
    public STByteArrayOutputStream genererPdfTraitementPapier(CoreSession session, Dossier dossier) {
        return genererPdf(doc -> this.genererDonneesTraitementPapier(session, doc, dossier));
    }

    private STByteArrayOutputStream genererPdf(Consumer<XWPFDocument> generateur) {
        try (XWPFDocument document = new XWPFDocument()) {
            setOrientationPaysage(document);
            generateur.accept(document);
            return SSServiceLocator.getSSPdfService().generatePdf(document);
        } catch (IOException e) {
            throw new NuxeoException(e);
        }
    }

    private void genererDonneesBordereau(
        XWPFDocument document,
        Dossier dossier,
        CoreSession session,
        boolean archivageDefinitif
    ) {
        final ActeService acteService = SolonEpgServiceLocator.getActeService();
        final VocabularyService vocabularyService = STServiceLocator.getVocabularyService();
        final STUserService sTUserService = STServiceLocator.getSTUserService();

        String typeActeId = dossier.getTypeActe();
        Map<String, Boolean> mapVisibility = acteService.getActeVisibility(typeActeId);
        addTitle(document, ResourceHelper.getString("bordereau.principal.bloc"), SIZE_TITRE1);
        genererBordereauPartieInformationActe(
            session,
            document,
            dossier,
            mapVisibility,
            false,
            archivageDefinitif,
            vocabularyService
        );
        addBreak(document);
        genererBordereauPartieResponsable(
            document,
            dossier,
            false,
            STServiceLocator.getSTMinisteresService(),
            STServiceLocator.getSTUsAndDirectionService(),
            sTUserService
        );
        addBreak(document);

        if (TRUE.equals(mapVisibility.get(ActeVisibilityConstants.CE))) {
            genererBordereauPartieCe(document, dossier, vocabularyService);
        }

        // Signature
        addBreak(document);
        addTitle(document, ResourceHelper.getString("pdf.signature"), SIZE_TITRE2);
        XWPFTable table = addLeftIndentation(createInvisibleTable(document));

        // Chargé de mission
        addTableRow(
            table,
            true,
            createHeaderCell(ResourceHelper.getString("pdf.dossier.charge.mission")),
            createTextCell(ajoutLigneTexteList(dossier.getNomCompletChargesMissions()))
        );

        // Conseiller PM
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.dossier.conseiller.pm")),
            createTextCell(ajoutLigneTexteList(dossier.getNomCompletConseillersPms()))
        );

        if (TRUE.equals(mapVisibility.get(ActeVisibilityConstants.DATE_SIGNATURE))) {
            // Date signature
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.date.signature")),
                createTextCell(ajoutLigneDate(dossier.getDateSignature()))
            );
        }

        if (TRUE.equals(mapVisibility.get(ActeVisibilityConstants.PUBLICATION))) {
            addBreak(document);
            addTitle(document, ResourceHelper.getString("pdf.dossier.publication"), SIZE_TITRE2);
            table = addLeftIndentation(createInvisibleTable(document));

            // Publication
            // Date de publication souhaitée
            addTableRow(
                table,
                true,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.date.publication.souhaitee")),
                createTextCell(ajoutLigneDate(dossier.getDatePublication()))
            );
        }

        RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
        if (TRUE.equals(mapVisibility.get(ActeVisibilityConstants.PARUTION)) || archivageDefinitif) {
            genererBordereauPartieParution(session, document, dossier, retourDila, vocabularyService);
        }

        if (TRUE.equals(mapVisibility.get(ActeVisibilityConstants.JORF))) {
            // Parution JORF
            addBreak(document);
            addTitle(document, ResourceHelper.getString("pdf.parution.jorf"), SIZE_TITRE2);
            table = addLeftIndentation(createInvisibleTable(document));

            // Date JO
            addTableRow(
                table,
                true,
                createHeaderCell(ResourceHelper.getString("pdf.retour.dila.date.jo")),
                createTextCell(ajoutLigneDate(retourDila.getDateParutionJorf()))
            );

            // N° du texte JO
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.retour.dila.numero.texte.jo")),
                createTextCell(retourDila.getNumeroTexteParutionJorf())
            );

            // Page JO
            if (retourDila.getPageParutionJorf() != null) {
                addTableRow(
                    table,
                    false,
                    createHeaderCell(ResourceHelper.getString("pdf.retour.dila.page.jo")),
                    createTextCell(retourDila.getPageParutionJorf().toString())
                );
            }

            // Titre officiel
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.retour.dila.titre.officiel")),
                createTextCell(retourDila.getTitreOfficiel())
            );
        }

        if (TRUE.equals(mapVisibility.get(ActeVisibilityConstants.PARUTION_BO))) {
            // Parution BO
            List<ParutionBo> parutionBoList = retourDila.getParutionBo();
            if (CollectionUtils.isNotEmpty(parutionBoList)) {
                addBreak(document);
                addTitle(document, ResourceHelper.getString("pdf.parution.bo"), SIZE_TITRE2);

                for (ParutionBo parutionBo : parutionBoList) {
                    table = addLeftIndentation(createInvisibleTable(document));

                    // Date JO
                    addTableRow(
                        table,
                        true,
                        createHeaderCell(ResourceHelper.getString("pdf.parution.bo.date")),
                        createTextCell(ajoutLigneDate(parutionBo.getDateParutionBo()))
                    );

                    // N° du texte BO
                    addTableRow(
                        table,
                        false,
                        createHeaderCell(ResourceHelper.getString("")),
                        createTextCell(parutionBo.getNumeroTexteParutionBo())
                    );

                    // Page BO
                    if (parutionBo.getPageParutionBo() != null) {
                        addTableRow(
                            table,
                            false,
                            createHeaderCell(ResourceHelper.getString("")),
                            createTextCell(parutionBo.getPageParutionBo().toString())
                        );
                    }
                }
            }
        }

        if (TRUE.equals(mapVisibility.get(ActeVisibilityConstants.SGG_DILA))) {
            // SGG-DILA
            addBreak(document);
            addTitle(document, ResourceHelper.getString("pdf.sgg.dila"), SIZE_TITRE2);
            table = addLeftIndentation(createInvisibleTable(document));

            // Zone commentaire SGG-DILA
            // note : visible pour tous les utilisateurs
            addTableRow(
                table,
                true,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.zone.commentaire")),
                createTextCell(dossier.getZoneComSggDila())
            );
        }

        if (TRUE.equals(mapVisibility.get(ActeVisibilityConstants.BASE_LEGALE))) {
            // Base légale
            addBreak(document);
            addTitle(document, ResourceHelper.getString("pdf.dossier.base.legale"), SIZE_TITRE2);
            table = addLeftIndentation(createInvisibleTable(document));

            addTableRow(
                table,
                true,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.base.legale")),
                createTextCell(dossier.getBaseLegaleActe())
            );
        }

        if (TRUE.equals(mapVisibility.get(ActeVisibilityConstants.INDEXATION))) {
            addBreak(document);
            addTitle(document, ResourceHelper.getString("pdf.donnes.indexation"), SIZE_TITRE2);
            table = addLeftIndentation(createInvisibleTable(document));

            // Rubriques
            addTableRow(
                table,
                true,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.rubriques")),
                createTextCell(ajoutLigneTexteList(dossier.getIndexationRubrique()))
            );

            // Mots-clés
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.mots.cles")),
                createTextCell(ajoutLigneTexteList(dossier.getIndexationMotsCles()))
            );

            // Champs libres
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.champs.libres")),
                createTextCell(ajoutLigneTexteList(dossier.getIndexationChampLibre()))
            );
        }

        if (TRUE.equals(mapVisibility.get(ActeVisibilityConstants.TRANSPOSITION_APPLICATION))) {
            genererBordereauPartieTransposition(document, dossier, mapVisibility);
        }

        addBreak(document);
        addBreakPage(document);
    }

    private void genererBordereauPartieCe(XWPFDocument document, Dossier dossier, VocabularyService vocabularyService) {
        // partie CE
        addTitle(document, ResourceHelper.getString("pdf.partie.ce"), SIZE_TITRE2);
        XWPFTable table = addLeftIndentation(createInvisibleTable(document));
        ConseilEtat conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);

        // Priorité CE
        addTableRow(
            table,
            true,
            createHeaderCell(ResourceHelper.getString("pdf.conseil.etat.priorite")),
            createTextCell(
                conseilEtat.getPriorite() != null
                    ? vocabularyService.getEntryLabel(VocabularyConstants.VOC_PRIORITE, conseilEtat.getPriorite())
                    : ""
            )
        );

        // Section du CE
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.conseil.etat.section.ce")),
            createTextCell(conseilEtat.getSectionCe())
        );

        // Rapporteur
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.conseil.etat.rapporteur")),
            createTextCell(conseilEtat.getRapporteurCe())
        );

        // Date transm. section CE
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.conseil.etat.date.transmission.section.ce")),
            createTextCell(ajoutLigneDate(conseilEtat.getDateTransmissionSectionCe()))
        );

        // Date section CE
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.conseil.etat.date.previsionnelle.section.ce")),
            createTextCell(ajoutLigneDate(conseilEtat.getDateSectionCe()))
        );

        // Date AG CE
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.conseil.etat.date.previsionnelle.ag.ce")),
            createTextCell(ajoutLigneDate(conseilEtat.getDateAgCe()))
        );

        // Numéro ISA
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.conseil.etat.numero.isa")),
            createTextCell(conseilEtat.getNumeroISA())
        );
    }

    private void genererBordereauPartieParution(
        CoreSession session,
        XWPFDocument document,
        Dossier dossier,
        RetourDila retourDila,
        VocabularyService vocabularyService
    ) {
        addBreak(document);
        addTitle(document, ResourceHelper.getString("pdf.dossier.parution"), SIZE_TITRE2);
        XWPFTable table = addLeftIndentation(createInvisibleTable(document));

        // Pour fourniture d'épreuve
        addTableRow(
            table,
            true,
            createHeaderCell(ResourceHelper.getString("pdf.dossier.fourniture.epreuve")),
            createTextCell(ajoutLigneDate(dossier.getDatePourFournitureEpreuve()))
        );

        BulletinOfficielService bulletinService = SolonEpgServiceLocator.getBulletinOfficielService();
        if (dossier.getVecteurPublication() != null && !dossier.getVecteurPublication().isEmpty()) {
            // Vecteur de publication
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.vecteur.publication")),
                createTextCell(
                    ajoutLigneTexteList(
                        bulletinService.convertVecteurIdListToLabels(session, dossier.getVecteurPublication())
                    )
                )
            );
        } else {
            // Vecteur de publication
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.vecteur.publication")),
                createTextCell(null)
            );
        }

        // Publication intégrale ou par extrait
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.dossier.publication.integrale.extrait")),
            createTextCell(
                vocabularyService.getEntryLabel(
                    VocabularyConstants.TYPE_PUBLICATION,
                    dossier.getPublicationIntegraleOuExtrait()
                )
            )
        );

        // Decret numéro
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.dossier.decret.numero")),
            createTextCell(ajoutLigneBooleen(dossier.getDecretNumerote()))
        );

        // Publication conjointe
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.dossier.publication.conjoite")),
            createTextCell(ajoutLigneTexteList(dossier.getPublicationsConjointes()))
        );

        // Mode parution
        final String docId = retourDila.getModeParution();
        final IdRef docRef = new IdRef(docId);

        try {
            if (docId != null && session.exists(docRef)) {
                DocumentModel modeParutionDoc = session.getDocument(docRef);
                if (modeParutionDoc != null) {
                    ModeParution modeParution = modeParutionDoc.getAdapter(ModeParution.class);
                    addTableRow(
                        table,
                        false,
                        createHeaderCell(ResourceHelper.getString("pdf.mode.parution")),
                        createTextCell(modeParution.getMode())
                    );
                }
            }
        } catch (NuxeoException ce) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_GET_MODE_PARUTION_TEC, docId, ce);
        }

        // Délai publication
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.dossier.delai.publication")),
            createTextCell(dossier.getDelaiPublicationLabel())
        );

        if (VocabularyConstants.STATUT_PUBLIE.equals(dossier.getStatut())) {
            // Publication à date précisée
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.publication.date.precisee")),
                createTextCell(ajoutLigneDate(dossier.getDatePreciseePublication()))
            );
        }
    }

    private void genererBordereauPartieTransposition(
        XWPFDocument document,
        Dossier dossier,
        Map<String, Boolean> mapVisibility
    ) {
        boolean isVisibleApplicationLoi = mapVisibility.get(ActeVisibilityConstants.APPLICATION_LOI);
        boolean isVisibleTranspositionOrdonnance = mapVisibility.get(ActeVisibilityConstants.TRANSPOSITION_ORDONNANCE);
        boolean isVisibleHabilitation = mapVisibility.get(ActeVisibilityConstants.HABILITATION);
        boolean isVisibleTranspositionDirective = mapVisibility.get(ActeVisibilityConstants.TRANSPOSITION_DIRECTIVE);

        if (
            isVisibleApplicationLoi ||
            isVisibleTranspositionOrdonnance ||
            isVisibleHabilitation ||
            isVisibleTranspositionDirective
        ) {
            // Transposition et application
            addBreak(document);
            addTitle(document, ResourceHelper.getString("pdf.transposition.application"), SIZE_TITRE2);

            if (isVisibleApplicationLoi) {
                // Application des lois
                addBreak(document);
                addTitle(document, ResourceHelper.getString("pdf.application.lois"), SIZE_TITRE2);

                List<ComplexeType> applicationLoi = new ArrayList<>(dossier.getApplicationLoi().getTranspositions());
                ajoutTranspositionEtHabilitation(document, applicationLoi, true);
            }

            if (isVisibleTranspositionOrdonnance) {
                // Application des ordonnances
                addBreak(document);
                addTitle(document, ResourceHelper.getString("pdf.application.ordonnances"), SIZE_TITRE2);

                List<ComplexeType> transpositionOrdonnance = new ArrayList<>(
                    dossier.getTranspositionOrdonnance().getTranspositions()
                );
                ajoutTranspositionEtHabilitation(document, transpositionOrdonnance, false);
            }

            if (isVisibleTranspositionDirective) {
                // Transposition des directives
                addBreak(document);
                addTitle(document, ResourceHelper.getString("pdf.transposition.directives"), SIZE_TITRE2);

                List<ComplexeType> transpositionDirective = new ArrayList<>(
                    dossier.getTranspositionDirective().getTranspositions()
                );
                ajoutTranspositionEtHabilitation(document, transpositionDirective, false);
            }

            if (isVisibleHabilitation) {
                // Habilitations
                addBreak(document);
                addTitle(document, ResourceHelper.getString("pdf.habilitations"), SIZE_TITRE2);

                // ajoute la table
                XWPFTable table = addLeftIndentation(createInvisibleTable(document));

                // Référence de la loi
                addTableRow(
                    table,
                    true,
                    createHeaderCell(ResourceHelper.getString("pdf.dossier.reference.loi")),
                    createTextCell(dossier.getHabilitationRefLoi())
                );

                // Numéro article
                addTableRow(
                    table,
                    false,
                    createHeaderCell(ResourceHelper.getString("pdf.dossier.numero.article")),
                    createTextCell(dossier.getHabilitationNumeroArticles())
                );

                // Commentaire
                addTableRow(
                    table,
                    false,
                    createHeaderCell(ResourceHelper.getString("pdf.dossier.commentaire")),
                    createTextCell(dossier.getHabilitationCommentaire())
                );
            }
        }
    }

    private void genererBordereauPartieInformationActe(
        CoreSession session,
        XWPFDocument document,
        Dossier dossier,
        Map<String, Boolean> mapVisibility,
        boolean traitementPapier,
        boolean archivageDefinitif,
        VocabularyService vocabularyService
    ) {
        addTitle(document, ResourceHelper.getString("pdf.dossier.information.acte"), SIZE_TITRE2);
        XWPFTable table = addLeftIndentation(createInvisibleTable(document));

        if (!traitementPapier) {
            // statut
            addTableRow(
                table,
                true,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.Statut")),
                createTextCell(
                    vocabularyService.getEntryLabel(
                        VocabularyConstants.STATUT_DOSSIER_DIRECTORY_NAME,
                        dossier.getStatut()
                    )
                )
            );

            // type acte
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.type.acte")),
                createTextCell(
                    vocabularyService.getEntryLabel(VocabularyConstants.TYPE_ACTE_VOCABULARY, dossier.getTypeActe())
                )
            );
        }

        // NOR
        addTableRow(
            table,
            traitementPapier,
            createHeaderCell(ResourceHelper.getString("pdf.dossier.nor")),
            createTextCell(dossier.getNumeroNor())
        );

        // titre acte
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.dossier.titre.acte")),
            createTextCell(dossier.getTitreActe())
        );

        genererBordereauPartieInformationActePartie2(
            session,
            table,
            dossier,
            mapVisibility,
            traitementPapier,
            archivageDefinitif,
            vocabularyService
        );
    }

    private void genererBordereauPartieInformationActePartie2(
        CoreSession session,
        XWPFTable table,
        Dossier dossier,
        Map<String, Boolean> mapVisibility,
        boolean traitementPapier,
        boolean archivageDefinitif,
        VocabularyService vocabularyService
    ) {
        if (!traitementPapier && !archivageDefinitif) {
            if (TRUE.equals(mapVisibility.get(ActeVisibilityConstants.CATEGORY_ACTE))) {
                // categorie acte
                addTableRow(
                    table,
                    false,
                    createHeaderCell(ResourceHelper.getString("pdf.dossier.categorie.acte")),
                    createTextCell(
                        Optional
                            .ofNullable(dossier.getCategorieActe())
                            .map(
                                c ->
                                    vocabularyService.getEntryLabel(
                                        VocabularyConstants.CATEGORY_ACTE_CONVENTION_COLLECTIVE,
                                        c
                                    )
                            )
                            .orElse("")
                    )
                );
            }

            if (TRUE.equals(mapVisibility.get(ActeVisibilityConstants.PUBLICATION_RAPPORT_PRESENTATION))) {
                // publication rapport de présentation
                addTableRow(
                    table,
                    false,
                    createHeaderCell(ResourceHelper.getString("pdf.dossier.publication.rapport.presentation")),
                    createTextCell(ajoutLigneBooleen(dossier.getPublicationRapportPresentation()))
                );
            }

            if (
                Integer.parseInt(dossier.getStatutArchivage()) >
                Integer.parseInt(VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_DEFINITIVE)
            ) {
                genererBordereauPartieArchivage(session, table, dossier, vocabularyService);
            }
        } else if (archivageDefinitif) {
            // categorie acte
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.categorie.acte")),
                createTextCell(
                    Optional
                        .ofNullable(dossier.getCategorieActe())
                        .map(
                            c ->
                                vocabularyService.getEntryLabel(
                                    VocabularyConstants.CATEGORY_ACTE_CONVENTION_COLLECTIVE,
                                    c
                                )
                        )
                        .orElse("")
                )
            );

            if (TRUE.equals(mapVisibility.get(ActeVisibilityConstants.PUBLICATION_RAPPORT_PRESENTATION))) {
                // publication rapport de présentation
                addTableRow(
                    table,
                    false,
                    createHeaderCell(ResourceHelper.getString("pdf.dossier.publication.rapport.presentation")),
                    createTextCell(ajoutLigneBooleen(dossier.getPublicationRapportPresentation()))
                );
            }
        }

        if (TRUE.equals(dossier.getTexteEntreprise())) {
            // Texte relevant de la rubrique entreprise
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.texte.rubrique.entreprise")),
                createTextCell(ajoutLigneBooleen(dossier.getTexteEntreprise()))
            );

            // dates d'effet
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.date.effet")),
                createTextCell(ajoutLigneDateList(dossier.getDateEffet()))
            );
        } else {
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.texte.rubrique.entreprise")),
                createTextCell(ajoutLigneBooleen(dossier.getTexteEntreprise()))
            );

            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.date.effet")),
                createTextCell("")
            );
        }
    }

    private void genererBordereauPartieArchivage(
        CoreSession session,
        XWPFTable table,
        Dossier dossier,
        VocabularyService vocabularyService
    ) {
        // statut d'archivage
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.dossier.statut.archivage")),
            createTextCell(
                Optional
                    .ofNullable(dossier.getStatutArchivage())
                    .map(c -> vocabularyService.getEntryLabel(VOCABULARY_STATUS_ARCHIVAGE, c))
                    .orElse("")
            )
        );

        // Bordereau de versement
        List<FondDeDossierFolder> repertoiresRacine = getFondDeDossierService().getAllRootFolder(session, dossier);

        repertoiresRacine
            .stream()
            .filter(
                folder ->
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_BORDEREAU_VERSEMENT.equals(
                        folder.getName()
                    )
            )
            .findFirst()
            .flatMap(
                repBordereauVersement ->
                    getFondDeDossierService()
                        .getChildrenFile(session, repBordereauVersement.getDocument())
                        .stream()
                        .findFirst()
            )
            .ifPresent(
                bordereauVersementFile ->
                    addTableRow(
                        table,
                        false,
                        createHeaderCell(ResourceHelper.getString("pdf.dossier.bordereau.versement")),
                        createTextCell(Optional.ofNullable(bordereauVersementFile.getName()).orElse(""))
                    )
            );
    }

    private void genererBordereauPartieResponsable(
        XWPFDocument document,
        Dossier dossier,
        boolean traitementPapier,
        final STMinisteresService ministeresService,
        final STUsAndDirectionService usService,
        final STUserService sTUserService
    ) {
        addTitle(document, ResourceHelper.getString("pdf.dossier.responsable.acte"), SIZE_TITRE2);
        XWPFTable table = addLeftIndentation(createInvisibleTable(document));

        // Ministère resp.
        addTableRow(
            table,
            true,
            createHeaderCell(ResourceHelper.getString("pdf.dossier.ministere.responsable")),
            createTextCell(
                ajoutLigneOrganigramme(
                    ministeresService,
                    usService,
                    dossier.getMinistereResp(),
                    STConstant.ORGANIGRAMME_ENTITE_DIR
                )
            )
        );

        // Direction resp.
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.dossier.direction.responsable")),
            createTextCell(
                ajoutLigneOrganigramme(
                    ministeresService,
                    usService,
                    dossier.getDirectionResp(),
                    STConstant.ORGANIGRAMME_UNITE_STRUCTURELLE_DIR
                )
            )
        );

        if (!traitementPapier) {
            // Ministère rattach.
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.ministere.rattachement")),
                createTextCell(
                    ajoutLigneOrganigramme(
                        ministeresService,
                        usService,
                        dossier.getMinistereAttache(),
                        STConstant.ORGANIGRAMME_ENTITE_DIR
                    )
                )
            );

            // Direction rattach..
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.direction.rattachement")),
                createTextCell(
                    ajoutLigneOrganigramme(
                        ministeresService,
                        usService,
                        dossier.getDirectionAttache(),
                        STConstant.ORGANIGRAMME_UNITE_STRUCTURELLE_DIR
                    )
                )
            );
        }

        // Nom du responsable du dossier
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.dossier.responsable")),
            createTextCell(dossier.getNomRespDossier())
        );

        // Prénom resp. dossier
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.dossier.prenom.responsable")),
            createTextCell(dossier.getPrenomRespDossier())
        );

        // Qualité resp. dossier
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.dossier.qualite.responsable")),
            createTextCell(dossier.getQualiteRespDossier())
        );

        // Tél. resp. dossier
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.dossier.tel.responsable")),
            createTextCell(dossier.getTelephoneRespDossier())
        );

        if (!traitementPapier) {
            // Mél resp. dossier
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.mel.responsable")),
                createTextCell(dossier.getMailRespDossier())
            );

            // Créé par
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.dossier.cree.par")),
                createTextCell(ajoutLigneUser(sTUserService, dossier.getIdCreateurDossier()))
            );
        }
    }

    private void genererTableauFeuilleDeRoute(
        CoreSession session,
        Dossier dossier,
        XWPFDocument document,
        boolean archivageDefinitif
    ) {
        DossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        DocumentModel dossierRoute = dossierDistributionService.getLastDocumentRouteForDossier(
            session,
            dossier.getDocument()
        );
        SSFeuilleRoute currentRoute = dossierRoute.getAdapter(SSFeuilleRoute.class);

        DocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
        List<RouteTableElement> routeTableElementList = documentRoutingService.getFeuilleRouteElements(
            currentRoute,
            session
        );

        addTitle(document, ResourceHelper.getString("dossier.onglet.fdr"), SIZE_TITRE1);
        if (CollectionUtils.isNotEmpty(routeTableElementList)) {
            boolean hasStepFolders = SolonEpgServiceLocator
                .getEPGFeuilleRouteService()
                .hasStepFolders(routeTableElementList);
            List<SpanPosition> rowSpanPositionList = new ArrayList<>();
            XWPFTable table = document.createTable();
            addTableRow(
                table,
                true,
                FOND_HEADER,
                getTableauFeuilleRouteHeaderCells(hasStepFolders, archivageDefinitif)
            );

            IntStream
                .range(0, routeTableElementList.size())
                .forEach(
                    i -> {
                        RouteTableElement docRouteTableElement = routeTableElementList.get(i);
                        SolonEpgRouteStep routeStep = docRouteTableElement
                            .getDocument()
                            .getAdapter(SolonEpgRouteStep.class);

                        addTableRow(
                            table,
                            false,
                            setCouleurLigneEtape(routeStep),
                            getTableauFeuilleRouteBodyCells(
                                session,
                                hasStepFolders,
                                archivageDefinitif,
                                docRouteTableElement,
                                rowSpanPositionList,
                                i + 1
                            )
                        );
                    }
                );

            rowSpanPositionList.forEach(rowSpan -> mergeCellVertically(table, 0, rowSpan.getBegin(), rowSpan.getEnd()));
            rowSpanPositionList.clear();
        }
        addBreakPage(document);
    }

    private Cell[] getTableauFeuilleRouteHeaderCells(boolean hasStepFolders, boolean archivageDefinitif) {
        return Stream
            .of(
                hasStepFolders ? createHeaderCell("") : null,
                hasStepFolders ? createHeaderCell("") : null,
                createHeaderCell(ResourceHelper.getString("pdf.fdr.column.header.etat")),
                createHeaderCell(ResourceHelper.getString("pdf.fdr.column.header.action")),
                createHeaderCell(ResourceHelper.getString("pdf.fdr.column.header.ministere")),
                createHeaderCell(ResourceHelper.getString("pdf.fdr.column.header.poste")),
                createHeaderCell(ResourceHelper.getString("pdf.fdr.column.header.utilisateur")),
                createHeaderCell(ResourceHelper.getString("pdf.fdr.column.header.echeance")),
                createHeaderCell(ResourceHelper.getString("pdf.fdr.column.header.date.traitement")),
                createHeaderCell(ResourceHelper.getString("pdf.fdr.column.header.obligatoire")),
                archivageDefinitif ? createHeaderCell("") : null
            )
            .filter(Objects::nonNull)
            .toArray(Cell[]::new);
    }

    private Cell[] getTableauFeuilleRouteBodyCells(
        CoreSession session,
        boolean hasStepFolders,
        boolean archivageDefinitif,
        RouteTableElement docRouteTableElement,
        List<SpanPosition> rowSpanPositionList,
        int position
    ) {
        SolonEpgRouteStep routeStep = docRouteTableElement.getDocument().getAdapter(SolonEpgRouteStep.class);

        String validationStatusLabel = "";
        if (archivageDefinitif) {
            String validationStatusId = routeStep.getValidationStatus();
            if (StringUtils.isNotEmpty(validationStatusId)) {
                ArchiveService archiveService = SolonEpgServiceLocator.getArchiveService();
                validationStatusLabel = archiveService.idIconeToLabelFDR(routeStep.getType(), validationStatusId);
            }
        }

        Map<String, String> feuilleRouteValMap = setFeuilleDeRouteVal(routeStep, session);
        return Stream
            .of(
                hasStepFolders
                    ? createImageCell(getEtapeParallelIcon(docRouteTableElement, position, rowSpanPositionList))
                    : null,
                hasStepFolders ? createImageCell(getEtapeSerialIcon(docRouteTableElement)) : null,
                createImageCell(feuilleRouteValMap.get("etat")),
                createTextCell(ResourceHelper.getString(feuilleRouteValMap.get("action"))),
                createTextCell(feuilleRouteValMap.get("ministere")),
                createTextCell(feuilleRouteValMap.get("poste")),
                createTextCell(feuilleRouteValMap.get("utilisateur")),
                createTextCell(feuilleRouteValMap.get("echeance")),
                createTextCell(feuilleRouteValMap.get("traitement")),
                createImageCell(feuilleRouteValMap.get("obligatoire")),
                archivageDefinitif ? createTextCell(validationStatusLabel) : null
            )
            .filter(Objects::nonNull)
            .toArray(Cell[]::new);
    }

    private void genererTableauJournal(XWPFDocument document, Dossier dossier) {
        JournalService journalService = STServiceLocator.getJournalService();
        List<LogEntry> logEntryList = journalService.queryDocumentAllLogs(
            Collections.singletonList(dossier.getDocument().getId()),
            null,
            0,
            0,
            null
        );
        if (logEntryList != null && CollectionUtils.isNotEmpty(logEntryList)) {
            addTitle(document, ResourceHelper.getString("dossier.onglet.journal"), SIZE_TITRE1);
            XWPFTable table = document.createTable();

            addTableRow(
                table,
                true,
                FOND_HEADER,
                createHeaderCell(ResourceHelper.getString("pdf.journal.column.header.date")),
                createHeaderCell(ResourceHelper.getString("pdf.journal.column.header.utilisateur")),
                createHeaderCell(ResourceHelper.getString("pdf.journal.column.header.poste")),
                createHeaderCell(ResourceHelper.getString("pdf.journal.column.header.categorie")),
                createHeaderCell(ResourceHelper.getString("pdf.journal.column.header.commentaire"))
            );

            for (LogEntry logEntry : logEntryList) {
                addTableRow(
                    table,
                    false,
                    createDateCell(logEntry.getEventDate()),
                    createTextCell(logEntry.getPrincipalName()),
                    createTextCell(logEntry.getDocPath()),
                    createTextCell(logEntry.getCategory()),
                    createTextCell(ResourceHelper.translateKeysInString(logEntry.getComment(), SPACE))
                );
            }
        }
    }

    @Override
    public File generateDossierPdf(CoreSession session, Dossier dossier) throws IOException {
        if (dossier == null) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, "Dossier null !");
            return null;
        }
        try (XWPFDocument document = new XWPFDocument()) {
            setOrientationPaysage(document);
            genererDonneesBordereau(document, dossier, session, false);
            genererTableauJournal(document, dossier);
            addBreakPage(document);
            genererTableauFeuilleDeRoute(session, dossier, document, false);

            // Ajout du fond de dossier
            File pdfDossierFile = generatePdf(session, dossier, document);
            List<DocumentModel> fddFiles = SolonEpgServiceLocator
                .getFondDeDossierService()
                .getFddDocuments(session, dossier);
            List<File> pdfFondFiles = convertDocumentModelsBlobsToPdf(fddFiles);
            if (CollectionUtils.isNotEmpty(pdfFondFiles)) {
                pdfFondFiles.add(0, pdfDossierFile);

                String mergedPdfFilename = FileUtils.generateCompletePdfFilename(
                    "DossierFinal_" + generateDossierFileName(session, dossier)
                );
                pdfDossierFile = mergePdfs(FileUtils.getAppTmpFilePath(mergedPdfFilename), pdfFondFiles);
            }

            return pdfDossierFile;
        }
    }

    @Override
    protected String generateDossierFileName(CoreSession session, Dossier dossier) {
        return "dossier_" + SolonDateConverter.DATE_DASH.formatNow() + ".pdf";
    }

    private String ajoutLigneBooleen(Boolean value) {
        return (value == null || !value) ? "non" : "oui";
    }

    private String ajoutLigneDateList(List<Calendar> values) {
        StringBuilder listeDateFormatee = new StringBuilder();
        for (Calendar date : values) {
            if (StringUtils.isNotEmpty(listeDateFormatee)) {
                listeDateFormatee.append(", ");
            }
            listeDateFormatee.append(SolonDateConverter.DATE_SLASH.format(date));
        }
        return listeDateFormatee.toString();
    }

    private String ajoutLigneDate(Calendar datePublication) {
        return SolonDateConverter.DATE_SLASH.format(datePublication);
    }

    private String ajoutLigneOrganigramme(
        STMinisteresService ministeresService,
        STUsAndDirectionService usService,
        String value,
        String nodeType
    ) {
        if (!StringUtils.isEmpty(value) && !StringUtils.isEmpty(nodeType)) {
            try {
                OrganigrammeNode node = null;
                if (STConstant.ORGANIGRAMME_ENTITE_DIR.equals(nodeType)) {
                    node = ministeresService.getEntiteNode(value);
                } else if (STConstant.ORGANIGRAMME_UNITE_STRUCTURELLE_DIR.equals(nodeType)) {
                    node = usService.getUniteStructurelleNode(value);
                } else {
                    LOGGER.error(null, STLogEnumImpl.FAIL_GET_ENTITE_NODE_TEC, nodeType + " - " + value);
                    value = "**type du noeud (poste ou ministère) inconnu**";
                }
                if (node == null) {
                    LOGGER.error(null, STLogEnumImpl.FAIL_GET_ENTITE_NODE_TEC, nodeType + " - " + value);
                    value = "**poste ou ministère inconnu**";
                } else {
                    value = node.getLabel();
                }
            } catch (NuxeoException e) {
                LOGGER.error(null, STLogEnumImpl.FAIL_GET_ENTITE_NODE_TEC, nodeType + " - " + value, e);
                value = null;
            }
        }
        return value;
    }

    private String ajoutLigneTexteList(List<String> values) {
        return CollectionUtils.isNotEmpty(values) ? StringUtils.join(values, ",") : "";
    }

    private String ajoutLigneUser(STUserService stUserservice, String value) {
        return StringUtils.isNotEmpty(value) ? stUserservice.getUserFullName(value) : "";
    }

    private void ajoutTranspositionEtHabilitation(
        XWPFDocument document,
        List<ComplexeType> applicationLoi,
        boolean isApplicationLoi
    ) {
        XWPFTable table;
        if (CollectionUtils.isNotEmpty(applicationLoi)) {
            for (ComplexeType complexeType : applicationLoi) {
                // ajoute la table
                table = addLeftIndentation(createInvisibleTable(document));

                Map<String, Serializable> serializableMap = complexeType.getSerializableMap();

                // Référence
                addTableRow(
                    table,
                    true,
                    createHeaderCell(ResourceHelper.getString("pdf.dossier.reference")),
                    createTextCell(
                        convertToString(
                            serializableMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY)
                        )
                    )
                );

                // Titre
                addTableRow(
                    table,
                    false,
                    createHeaderCell(ResourceHelper.getString("pdf.dossier.titre")),
                    createTextCell(
                        convertToString(
                            serializableMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY)
                        )
                    )
                );

                // Numéro d'article
                addTableRow(
                    table,
                    false,
                    createHeaderCell(ResourceHelper.getString("pdf.dossier.numero.article")),
                    createTextCell(
                        convertToString(
                            serializableMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_NUMERO_PROPERTY)
                        )
                    )
                );

                if (isApplicationLoi) {
                    // Référence de la mesure
                    addTableRow(
                        table,
                        false,
                        createHeaderCell(ResourceHelper.getString("pdf.dossier.reference.mesure")),
                        createTextCell(
                            convertToString(
                                serializableMap.get(DossierSolonEpgConstants.DOSSIER_LOI_APPLIQUEE_REF_MESURE_PROPERTY)
                            )
                        )
                    );
                }

                // Commentaire
                addTableRow(
                    table,
                    false,
                    createHeaderCell(ResourceHelper.getString("pdf.dossier.commentaire")),
                    createTextCell(
                        convertToString(
                            serializableMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_COMMENTAIRE_PROPERTY)
                        )
                    )
                );
            }
        }
    }

    private void genererDonneesTraitementPapier(CoreSession session, XWPFDocument document, Dossier dossier) {
        if (dossier == null) {
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOSSIER_TEC, "Dossier non trouvé !");
            return;
        }

        DocumentModel dossierDoc = dossier.getDocument();
        TraitementPapier traitementPapierDoc = dossierDoc.getAdapter(TraitementPapier.class);

        final ActeService acteService = SolonEpgServiceLocator.getActeService();
        Map<String, Boolean> mapVisibility = acteService.getActeVisibility(dossier.getTypeActe());

        // Signataire
        String signataire = "";
        final VocabularyService vocabularyService = STServiceLocator.getVocabularyService();
        if (traitementPapierDoc.getSignataire() != null && !traitementPapierDoc.getSignataire().isEmpty()) {
            signataire =
                vocabularyService.getEntryLabel(
                    VocabularyConstants.TYPE_SIGNATAIRE_TP_DIRECTORY_NAME,
                    traitementPapierDoc.getSignataire()
                );
        }

        genererDonneesTraitementPapierReferences(session, document, dossier, mapVisibility, signataire);
        genererDonneesTraitementPapierCommunication(document, dossier);
        genererDonneesTraitementPapierSignature(document, traitementPapierDoc, signataire);
        genererDonneesTraitementPapierEpreuves(document, traitementPapierDoc);
        genererDonneesTraitementPapierPublication(session, document, traitementPapierDoc);
        genererDonneesTraitementPapierAmpliation(document, traitementPapierDoc);
    }

    private void genererDonneesTraitementPapierReferences(
        CoreSession session,
        XWPFDocument document,
        Dossier dossier,
        Map<String, Boolean> mapVisibility,
        String signataire
    ) {
        final VocabularyService vocabularyService = STServiceLocator.getVocabularyService();
        final STUserService sTUserService = STServiceLocator.getSTUserService();

        addTitle(document, ResourceHelper.getString("pdf.traitement.papier.references"), SIZE_TITRE1);

        addTitle(document, ResourceHelper.getString("pdf.traitement.papier.donnees.bordereau"), SIZE_TITRE2);
        genererBordereauPartieInformationActe(
            session,
            document,
            dossier,
            mapVisibility,
            true,
            false,
            vocabularyService
        );
        addBreak(document);

        genererBordereauPartieResponsable(
            document,
            dossier,
            true,
            STServiceLocator.getSTMinisteresService(),
            STServiceLocator.getSTUsAndDirectionService(),
            sTUserService
        );
        addBreak(document);

        // Publication
        if (TRUE.equals(mapVisibility.get(ActeVisibilityConstants.PARUTION))) {
            addTitle(document, ResourceHelper.getString("pdf.traitement.papier.publication"), SIZE_TITRE2);
            XWPFTable table = addLeftIndentation(createInvisibleTable(document));
            addTableRow(
                table,
                true,
                createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.publication.conjointe")),
                createTextCell(ajoutLigneTexteList(dossier.getPublicationsConjointes()))
            );
            addBreak(document);
        }

        // Complément
        addTitle(document, ResourceHelper.getString("pdf.traitement.papier.complement"), SIZE_TITRE1);
        // Références
        addTitle(document, ResourceHelper.getString("pdf.traitement.papier.references"), SIZE_TITRE2);

        XWPFTable table = addLeftIndentation(createInvisibleTable(document));
        DocumentModel dossierDoc = dossier.getDocument();
        TraitementPapier traitementPapierDoc = dossierDoc.getAdapter(TraitementPapier.class);
        addTableRow(
            table,
            true,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.references.date.arrivee")),
            createDateCell(traitementPapierDoc.getDateArrivePapier())
        );
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.references.commentaire")),
            createTextCell(traitementPapierDoc.getCommentaireReference())
        );
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.references.texte.publier")),
            createTextCell(ajoutLigneBooleen(traitementPapierDoc.getTexteAPublier()))
        );
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.references.texte.publication")),
            createTextCell(ajoutLigneBooleen(traitementPapierDoc.getTexteSoumisAValidation()))
        );
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.references.signataire")),
            createTextCell(signataire)
        );
        addBreakPage(document);
    }

    private void genererDonneesTraitementPapierCommunication(XWPFDocument document, Dossier dossier) {
        DocumentModel dossierDoc = dossier.getDocument();
        TraitementPapier traitementPapierDoc = dossierDoc.getAdapter(TraitementPapier.class);
        final ActeService acteService = SolonEpgServiceLocator.getActeService();

        addTitle(document, ResourceHelper.getString("pdf.traitement.papier.communication"), SIZE_TITRE1);
        // --Cabinet du PM
        genererTableauCommunicationDestinataires(
            document,
            ResourceHelper.getString("pdf.traitement.papier.communication.cabinet.pm"),
            traitementPapierDoc.getCabinetPmCommunication()
        );
        addBreak(document);

        // --Chargé de mission
        genererTableauCommunicationDestinataires(
            document,
            ResourceHelper.getString("pdf.traitement.papier.communication.charge.mission"),
            traitementPapierDoc.getChargeMissionCommunication()
        );
        addBreak(document);

        // --Autres
        genererTableauCommunicationDestinataires(
            document,
            ResourceHelper.getString("pdf.traitement.papier.communication.autres"),
            traitementPapierDoc.getAutresDestinatairesCommunication()
        );
        addBreak(document);

        // --Secretaire general
        if (acteService.isArrete(dossier.getTypeActe())) {
            genererTableauCommunicationDestinataires(
                document,
                ResourceHelper.getString("pdf.traitement.papier.communication.cabinet.secretaire.general"),
                traitementPapierDoc.getCabinetSgCommunication()
            );
            addBreak(document);
        }

        // --Priorité & signataires & liste de personnes nommées dans le même texte
        addTitle(document, ResourceHelper.getString("pdf.traitement.papier.communication.complement"), SIZE_TITRE2);

        XWPFTable table = addLeftIndentation(createInvisibleTable(document));
        final VocabularyService vocabularyService = STServiceLocator.getVocabularyService();
        // priorité
        String priorite = "";
        if (traitementPapierDoc.getPriorite() != null && !traitementPapierDoc.getPriorite().isEmpty()) {
            priorite =
                vocabularyService.getEntryLabel(
                    VocabularyConstants.NIVEAU_PRIORITE_TP_DIRECTORY_NAME,
                    traitementPapierDoc.getPriorite()
                );
        }
        addTableRow(
            table,
            true,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.communication.priorite")),
            createTextCell(priorite)
        );

        // Liste des personnes nommées
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.communication.personnes.nommees")),
            createTextCell(traitementPapierDoc.getPersonnesNommees())
        );

        // Signataire
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.communication.signataire")),
            createTextCell(traitementPapierDoc.getNomsSignatairesCommunication())
        );
        addBreakPage(document);
    }

    private void genererDonneesTraitementPapierSignature(
        XWPFDocument document,
        TraitementPapier traitementPapierDoc,
        String signataire
    ) {
        addTitle(document, ResourceHelper.getString("pdf.traitement.papier.signature"), SIZE_TITRE1);
        XWPFTable table = addLeftIndentation(createInvisibleTable(document));
        // Chemin de croix
        addTableRow(
            table,
            true,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.signature.chemin.croix")),
            createTextCell(ajoutLigneBooleen(traitementPapierDoc.getCheminCroix()))
        );

        if (
            traitementPapierDoc.getSignaturePm() != null &&
            signataire.equals(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_PM)
        ) {
            this.genererSignatureDonneesSignataire(
                    document,
                    ResourceHelper.getString("pdf.traitement.papier.signature.signature.pm"),
                    traitementPapierDoc.getSignaturePm()
                );
        }
        if (
            traitementPapierDoc.getSignaturePr() != null &&
            signataire.equals(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_PR)
        ) {
            this.genererSignatureDonneesSignataire(
                    document,
                    ResourceHelper.getString("pdf.traitement.papier.signature.signature.pr"),
                    traitementPapierDoc.getSignaturePr()
                );
        }
        if (
            traitementPapierDoc.getSignatureSgg() != null &&
            signataire.equals(SolonEpgListeTraitementPapierConstants.LISTE_TRAITEMENT_PAPIER_REFERENCE_SIGNATAIRE_SGG)
        ) {
            this.genererSignatureDonneesSignataire(
                    document,
                    ResourceHelper.getString("pdf.traitement.papier.signature.signature.sgg"),
                    traitementPapierDoc.getSignatureSgg()
                );
        }
        addBreakPage(document);

        // **Retour
        addTitle(document, ResourceHelper.getString("pdf.traitement.papier.retour"), SIZE_TITRE1);
        table = addLeftIndentation(createInvisibleTable(document));
        // Retour a
        addTableRow(
            table,
            true,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.retour.retour.a")),
            createTextCell(traitementPapierDoc.getRetourA())
        );
        // Date
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.retour.date")),
            createDateCell((traitementPapierDoc.getDateRetour()))
        );
        // Motif
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.retour.motif")),
            createTextCell(traitementPapierDoc.getMotifRetour())
        );
        // Signataire
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.retour.signataire")),
            createTextCell(traitementPapierDoc.getNomsSignatairesRetour())
        );
        addBreakPage(document);
    }

    private void genererDonneesTraitementPapierEpreuves(XWPFDocument document, TraitementPapier traitementPapierDoc) {
        addTitle(document, ResourceHelper.getString("pdf.traitement.papier.epreuves"), SIZE_TITRE1);
        XWPFTable table = addLeftIndentation(createInvisibleTable(document));

        // Epreuve demandée le
        InfoEpreuve infoEpreuve = traitementPapierDoc.getEpreuve();
        addTableRow(
            table,
            true,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.epreuves.epreuve.demande.le")),
            createDateCell(infoEpreuve.getEpreuveDemandeLe())
        );
        // Pour le
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.epreuves.epreuve.pour.le")),
            createDateCell(infoEpreuve.getEpreuvePourLe())
        );
        // Numéro de liste
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.epreuves.epreuve.numero.liste")),
            createTextCell(infoEpreuve.getNumeroListe())
        );
        // Envoi en relecture le
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.epreuves.epreuve.envoi.relecture")),
            createDateCell(infoEpreuve.getEnvoiRelectureLe())
        );
        // Destinataire
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.epreuves.epreuve.destinataire")),
            createTextCell(infoEpreuve.getDestinataireEntete())
        );
        // Signataire
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.epreuves.epreuve.signataire")),
            createTextCell(infoEpreuve.getNomsSignataires())
        );

        addBreak(document);
        table = addLeftIndentation(createInvisibleTable(document));
        InfoEpreuve nouvelleDemandeEpreuve = traitementPapierDoc.getNouvelleDemandeEpreuves();
        //      Epreuve demandée le
        addTableRow(
            table,
            true,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.epreuves.nouvelle.epreuve.demande.le")),
            createDateCell(nouvelleDemandeEpreuve.getEpreuveDemandeLe())
        );
        // Pour le
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.epreuves.nouvelle.epreuve.pour.le")),
            createDateCell(nouvelleDemandeEpreuve.getEpreuvePourLe())
        );
        // Numéro de liste
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.epreuves.nouvelle.epreuve.numero.liste")),
            createTextCell(nouvelleDemandeEpreuve.getNumeroListe())
        );
        // Envoi en relecture le
        addTableRow(
            table,
            false,
            createHeaderCell(
                ResourceHelper.getString("pdf.traitement.papier.epreuves.nouvelle.epreuve.envoi.relecture")
            ),
            createDateCell(nouvelleDemandeEpreuve.getEnvoiRelectureLe())
        );
        // Destinataire
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.epreuves.nouvelle.epreuve.destinataire")),
            createTextCell(nouvelleDemandeEpreuve.getDestinataireEntete())
        );
        // Signataire
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.epreuves.nouvelle.epreuve.signataire")),
            createTextCell(nouvelleDemandeEpreuve.getNomsSignataires())
        );
        addBreak(document);

        // Retour du bon à tirer le
        table = addLeftIndentation(createInvisibleTable(document));
        addTableRow(
            table,
            true,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.epreuves.bon.tirer")),
            createDateCell(traitementPapierDoc.getRetourDuBonaTitrerLe())
        );
        addBreakPage(document);
    }

    private void genererDonneesTraitementPapierPublication(
        CoreSession session,
        XWPFDocument document,
        TraitementPapier traitementPapierDoc
    ) {
        addTitle(document, ResourceHelper.getString("pdf.traitement.papier.publication"), SIZE_TITRE1);
        XWPFTable table = addLeftIndentation(createInvisibleTable(document));

        // date d'envoi JO
        addTableRow(
            table,
            true,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.publication.date.envoi")),
            createDateCell(traitementPapierDoc.getPublicationDateEnvoiJo())
        );
        final BulletinOfficielService bulletinService = SolonEpgServiceLocator.getBulletinOfficielService();
        // Vecteur de publication
        if (
            traitementPapierDoc.getPublicationNomPublication() != null &&
            !traitementPapierDoc.getPublicationNomPublication().isEmpty()
        ) {
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.publication.vecteur.publication")),
                createTextCell(
                    bulletinService.convertVecteurIdToLabel(session, traitementPapierDoc.getPublicationNomPublication())
                )
            );
        } else {
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.publication.vecteur.publication")),
                createTextCell("")
            );
        }
        // Mode de parution
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.publication.mode.parution")),
            createTextCell(traitementPapierDoc.getPublicationModePublication())
        );
        // Epreuves en retour
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.publication.epreuves.retour")),
            createTextCell(ajoutLigneBooleen(traitementPapierDoc.getPublicationEpreuveEnRetour()))
        );
        final VocabularyService vocabularyService = STServiceLocator.getVocabularyService();
        // Délai publication
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.publication.delai.publication")),
            createTextCell(
                Optional
                    .ofNullable(traitementPapierDoc.getPublicationDelai())
                    .map(v -> vocabularyService.getEntryLabel(VocabularyConstants.DELAI_PUBLICATION, v))
                    .orElse("")
            )
        );

        //
        if (VocabularyConstants.DELAI_PUBLICATION_A_DATE_PRECISEE.equals(traitementPapierDoc.getPublicationDelai())) {
            // Publication à date précisée
            addTableRow(
                table,
                false,
                createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.publication.date.precisee")),
                createDateCell(traitementPapierDoc.getPublicationDateDemande())
            );
        }
        // Numéro de liste
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.publication.numero.liste")),
            createTextCell(traitementPapierDoc.getPublicationNumeroListe())
        );
        // Parution JORF
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.publication.date.jorf")),
            createDateCell(traitementPapierDoc.getPublicationDate())
        );
        addBreakPage(document);
    }

    private void genererDonneesTraitementPapierAmpliation(XWPFDocument document, TraitementPapier traitementPapierDoc) {
        // **Ampliation
        addTitle(document, ResourceHelper.getString("pdf.traitement.papier.ampliation"), SIZE_TITRE1);
        XWPFTable table = addLeftIndentation(createInvisibleTable(document));
        // Dossier papier archivé
        addTableRow(
            table,
            true,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.ampliation.dossier.archive")),
            createTextCell(ajoutLigneBooleen(traitementPapierDoc.getPapierArchive()))
        );
        // Destinataire
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.ampliation.destinataire")),
            createTextCell(ajoutLigneTexteList(traitementPapierDoc.getAmpliationDestinataireMails()))
        );

        // historique des ampliations
        genererTableauAmpliationDestinataires(document, traitementPapierDoc.getAmpliationHistorique());
    }

    private void genererTableauAmpliationDestinataires(
        XWPFDocument document,
        List<InfoHistoriqueAmpliation> listHistoriqueAmpliation
    ) {
        XWPFTable table = createInvisibleTable(document);
        addTableRow(
            table,
            true,
            FOND_HEADER,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.ampliation.destinataires.date.envoi")),
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.ampliation.destinataires.liste"))
        );

        listHistoriqueAmpliation.forEach(
            ampliation ->
                addTableRow(
                    table,
                    false,
                    FOND_HEADER,
                    createDateCell(ampliation.getDateEnvoiAmpliation()),
                    createTextCell(ajoutLigneTexteList(ampliation.getAmpliationDestinataireMails()))
                )
        );
    }

    private void genererTableauCommunicationDestinataires(
        XWPFDocument document,
        String titre,
        List<DestinataireCommunication> listDestinataire
    ) {
        TypeAvisTraitementPapierService typeAvisTraitementPapierService = SolonEpgServiceLocator.getTypeAvisTraitementPapierService();

        addTitle(document, titre, SIZE_TITRE2);
        XWPFTable table = document.createTable();

        addTableRow(
            table,
            true,
            FOND_HEADER,
            createHeaderCell(
                ResourceHelper.getString("pdf.traitement.papier.communication.destinataires.destinataire")
            ),
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.communication.destinataires.date.envoi")),
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.communication.destinataires.date.retour")),
            createHeaderCell(
                ResourceHelper.getString("pdf.traitement.papier.communication.destinataires.date.relance")
            ),
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.communication.destinataires.sens.avis")),
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.communication.destinataires.objet"))
        );

        for (DestinataireCommunication destinataire : listDestinataire) {
            addTableRow(
                table,
                false,
                createTextCell(destinataire.getNomDestinataireCommunication()),
                createDateCell(destinataire.getDateEnvoi()),
                createDateCell(destinataire.getDateRetour()),
                createDateCell(destinataire.getDateRelance()),
                createTextCell(
                    typeAvisTraitementPapierService
                        .getEntry(destinataire.getSensAvis())
                        .map(ImmutablePair::getValue)
                        .orElse("")
                ),
                createTextCell(destinataire.getObjet())
            );
        }
    }

    private void genererSignatureDonneesSignataire(
        XWPFDocument document,
        String titre,
        DonneesSignataire donneesSignataire
    ) {
        addTitle(document, titre, SIZE_TITRE2);
        XWPFTable table = createInvisibleTable(document);
        addTableRow(
            table,
            true,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.signature.date.envoi")),
            createDateCell(donneesSignataire.getDateEnvoiSignature())
        );
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.signature.date.retour")),
            createDateCell(donneesSignataire.getDateRetourSignature())
        );
        addTableRow(
            table,
            false,
            createHeaderCell(ResourceHelper.getString("pdf.traitement.papier.signature.commentaire")),
            createTextCell(donneesSignataire.getCommentaireSignature())
        );
    }

    public static String convertToString(Serializable serializable) {
        if (serializable == null) {
            return StringUtils.EMPTY;
        } else {
            return serializable.toString();
        }
    }
}
