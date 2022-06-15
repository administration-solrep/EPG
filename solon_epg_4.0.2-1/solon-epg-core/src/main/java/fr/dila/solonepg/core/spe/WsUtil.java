package fr.dila.solonepg.core.spe;

import static fr.dila.st.api.constant.STSchemaConstant.FILE_SCHEMA;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.constant.SolonEpgVecteurPublicationConstants;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.api.service.IndexationEpgService;
import fr.dila.solonepg.api.service.ParapheurService;
import fr.dila.solonepg.api.service.TreeService;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.solonepg.core.cases.typescomplexe.ParutionBoImpl;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.feuilleroute.SSRouteStep;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.api.domain.file.File;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.service.organigramme.STPostesService;
import fr.dila.st.api.service.organigramme.STUsAndDirectionService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.FileUtils;
import fr.gouv.premier_ministre.dila.solon.epg.solon_bordereau.BORDEREAU;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import fr.sword.xsd.solon.epg.AccordCollectif;
import fr.sword.xsd.solon.epg.Acte;
import fr.sword.xsd.solon.epg.Acte.Indexation;
import fr.sword.xsd.solon.epg.Acte.ParutionBo;
import fr.sword.xsd.solon.epg.Acte.ParutionJORF;
import fr.sword.xsd.solon.epg.Acte.Responsable;
import fr.sword.xsd.solon.epg.ActeType;
import fr.sword.xsd.solon.epg.Amnistie;
import fr.sword.xsd.solon.epg.Arrete;
import fr.sword.xsd.solon.epg.ArreteCE;
import fr.sword.xsd.solon.epg.ArreteInterministeriel;
import fr.sword.xsd.solon.epg.ArreteInterministerielIndividuel;
import fr.sword.xsd.solon.epg.ArreteMinisteriel;
import fr.sword.xsd.solon.epg.ArreteMinisterielIndividuel;
import fr.sword.xsd.solon.epg.ArretePM;
import fr.sword.xsd.solon.epg.ArretePMIndividuel;
import fr.sword.xsd.solon.epg.ArretePR;
import fr.sword.xsd.solon.epg.ArretePRIndividuel;
import fr.sword.xsd.solon.epg.Art38DispositionHabilitation;
import fr.sword.xsd.solon.epg.Avenant;
import fr.sword.xsd.solon.epg.Avis;
import fr.sword.xsd.solon.epg.Circulaire;
import fr.sword.xsd.solon.epg.Citation;
import fr.sword.xsd.solon.epg.Communique;
import fr.sword.xsd.solon.epg.Convention;
import fr.sword.xsd.solon.epg.Decision;
import fr.sword.xsd.solon.epg.Decret;
import fr.sword.xsd.solon.epg.DecretCE;
import fr.sword.xsd.solon.epg.DecretCECM;
import fr.sword.xsd.solon.epg.DecretCECMIndividuel;
import fr.sword.xsd.solon.epg.DecretCEIndividuel;
import fr.sword.xsd.solon.epg.DecretCEart37;
import fr.sword.xsd.solon.epg.DecretCM;
import fr.sword.xsd.solon.epg.DecretCMIndividuel;
import fr.sword.xsd.solon.epg.DecretPR;
import fr.sword.xsd.solon.epg.DecretPRCE;
import fr.sword.xsd.solon.epg.DecretPRIndividuel;
import fr.sword.xsd.solon.epg.DecretPublicationDeTraiteOuAccord;
import fr.sword.xsd.solon.epg.DecretSimple;
import fr.sword.xsd.solon.epg.DecretSimpleIndividuel;
import fr.sword.xsd.solon.epg.Deliberation;
import fr.sword.xsd.solon.epg.DemandeAvisCE;
import fr.sword.xsd.solon.epg.Divers;
import fr.sword.xsd.solon.epg.DossierEpg;
import fr.sword.xsd.solon.epg.DossierEpgWithFile;
import fr.sword.xsd.solon.epg.DossierModification;
import fr.sword.xsd.solon.epg.Exequatur;
import fr.sword.xsd.solon.epg.Fichier;
import fr.sword.xsd.solon.epg.InformationsParlementaires;
import fr.sword.xsd.solon.epg.Instruction;
import fr.sword.xsd.solon.epg.Liste;
import fr.sword.xsd.solon.epg.ListeFichiers;
import fr.sword.xsd.solon.epg.Loi;
import fr.sword.xsd.solon.epg.LoiArt53Constitution;
import fr.sword.xsd.solon.epg.LoiConstitutionnelle;
import fr.sword.xsd.solon.epg.LoiOrganique;
import fr.sword.xsd.solon.epg.MesureApplicationLoi;
import fr.sword.xsd.solon.epg.Note;
import fr.sword.xsd.solon.epg.Ordonnance;
import fr.sword.xsd.solon.epg.PublicationIntOuExtType;
import fr.sword.xsd.solon.epg.Rapport;
import fr.sword.xsd.solon.epg.Rectificatif;
import fr.sword.xsd.solon.epg.SectionCe;
import fr.sword.xsd.solon.epg.StatutActe;
import fr.sword.xsd.solon.epg.Tableau;
import fr.sword.xsd.solon.epg.TranspositionDirectiveEuropeenne;
import fr.sword.xsd.solon.epg.TranspositionOrdonnance;
import fr.sword.xsd.solon.spe.PEActeCategorie;
import fr.sword.xsd.solon.spe.PEDelaiPublication;
import fr.sword.xsd.solon.spe.PEDemandeType;
import fr.sword.xsd.solon.spe.PEFichier;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.impl.blob.ByteArrayBlob;
import org.nuxeo.ecm.core.api.trash.TrashService;

/**
 * Classe utilitaire concernant les webservices de solonEpg (wsepg et wsspe) et utilisée par le WsSpeService
 *
 * @author arolin
 */
public class WsUtil {
    private static final String DEFAULT_DATA = "Donnée manquante";
    private static final STLogger LOGGER = STLogFactory.getLog(WsUtil.class);

    /**
     * Créé et remplit un objet DossierEpg à partir d'un objet Dossier
     *
     * @param dossier
     *            dossier
     * @return un objet DossierEpg
     * @throws afficherFichierPourPublication
     * @throws IOException
     */
    public static DossierEpgWithFile getDossierEpgWithFileFromDossier(
        CoreSession session,
        Dossier dossier,
        boolean affichagePublicationEtEpreuvage
    )
        throws IOException {
        DossierEpgWithFile dossierEpgWithFile = new DossierEpgWithFile();

        // récupération du contenu du dossierEpg (bordereau)
        DossierEpg dossierEpg = getDossierEpgFromDossier(dossier, session, affichagePublicationEtEpreuvage);
        dossierEpgWithFile.setDossierEpg(dossierEpg);

        // récupération des fichiers du parapheurs
        final ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
        List<DocumentModel> parapheurFilesDoc;
        if (affichagePublicationEtEpreuvage) {
            // récupération des pièces du parapheur (avec les pièces complémentaires)
            parapheurFilesDoc = parapheurService.getPieceParapheur(session, dossier, true, FILE_SCHEMA);
            parapheurFilesDoc.addAll(parapheurService.getEpreuvesFiles(session, dossier));
        } else {
            parapheurFilesDoc = parapheurService.getParapheurFiles(session, dossier, FILE_SCHEMA);
        }

        if (parapheurFilesDoc != null && parapheurFilesDoc.size() > 0) {
            ListeFichiers parapheurFichiers = new ListeFichiers();
            fillListeFichiersWsFromDocModelList(
                parapheurFichiers,
                parapheurFilesDoc,
                SolonEpgParapheurConstants.DEFAULT_PARAPHEUR_NAME
            );
            dossierEpgWithFile.setParapheur(parapheurFichiers);
        }

        final FondDeDossierService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();

        List<DocumentModel> fddFilesDoc = new ArrayList<>();
        if (affichagePublicationEtEpreuvage) {
            if (dossier.getPublicationRapportPresentation()) {
                // récupération du répertoire rapport de présentation
                fddFilesDoc = fondDeDossierService.getRapportPresentationFiles(session, dossier);
            }

            // récupération du répertoire Epreuve
            List<DocumentModel> mesFichiersPourEpreuves = fondDeDossierService.getEpreuvesFiles(session, dossier);
            if (mesFichiersPourEpreuves != null && mesFichiersPourEpreuves.size() > 0) {
                fddFilesDoc.addAll(mesFichiersPourEpreuves);
            }
        } else {
            // récupération des répertoires publics du fond de dossier
            fddFilesDoc = fondDeDossierService.getPublicFddFiles(session, dossier);
        }

        if (fddFilesDoc != null && fddFilesDoc.size() > 0) {
            // on affiche les fichiers contenu dans le
            ListeFichiers fondDossierFichiers = new ListeFichiers();
            fillListeFichiersWsFromDocModelList(
                fondDossierFichiers,
                fddFilesDoc,
                SolonEpgFondDeDossierConstants.DEFAULT_FDD_NAME
            );
            dossierEpgWithFile.setFondDossier(fondDossierFichiers);
        }
        return dossierEpgWithFile;
    }

    /**
     * Retourne les dernières modifications d'un dossier à partir d'un dossier
     */

    public static DossierModification getModificationDossierEpgFromDossier(CoreSession session, Dossier dossier) {
        DossierModification dossierModif = new DossierModification();
        return dossierModif;
    }

    /**
     * Créé et remplit un objet DossierEpg à partir d'un objet Dossier dans le cas de la publication : on envoie aussi
     * la pièce du parapheur et le rapport de présentation si besoin
     *
     * @param dossier
     *            dossier
     * @return un objet DossierEpg
     * @throws IOException
     */
    public static DossierEpgWithFile getDossierEpgWithFileFromDossierPublication(CoreSession session, Dossier dossier)
        throws IOException {
        return getDossierEpgWithFileFromDossier(session, dossier, true);
    }

    /**
     * remplit la liste de fichiers webservices à partir d'une liste de fichier nuxeo
     *
     * @param listeFichiersWs
     * @param docModelList
     * @throws IOException
     */
    private static void fillListeFichiersWsFromDocModelList(
        ListeFichiers listeFichiersWs,
        List<DocumentModel> docModelList,
        String folderRoot
    )
        throws IOException {
        TreeService treeService = SolonEpgServiceLocator.getTreeService();
        for (DocumentModel fileDoc : docModelList) {
            File file = fileDoc.getAdapter(File.class);
            Blob blobFile = file.getContent();
            Fichier fichier = new Fichier();
            fichier.setNom(file.getFilename());
            if (blobFile != null) {
                fichier.setMimeType(blobFile.getMimeType());
                fichier.setContenu(blobFile.getByteArray());
                fichier.setTailleFichier((int) blobFile.getLength());
            }
            String cheminFichier = treeService.getFileFolderPath(fileDoc, folderRoot);
            fichier.setCheminFichier(cheminFichier);
            listeFichiersWs.getFichier().add(fichier);
        }
    }

    /**
     * Créé et remplit un objet DossierEpg à partir d'un objet Dossier
     *
     * @param dossier
     *            dossier
     * @return un objet DossierEpg
     */
    public static DossierEpg getDossierEpgFromDossier(
        Dossier dossier,
        CoreSession session,
        Boolean affichagePublicationEtEpreuvage
    ) {
        DossierEpg dossierEpg = new DossierEpg();
        String typeActe = dossier.getTypeActe();
        ActeType acteType = WsUtil.getWsActeTypeFromDossierTypeActe(typeActe);
        if (acteType != null) {
            switch (acteType) {
                case AMNISTIE:
                    Amnistie amnistie = new Amnistie();
                    // propriétés spécifiques au type d'acte
                    amnistie.setBaseLegale(dossier.getBaseLegaleActe());
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, amnistie, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setAmnistie(amnistie);
                    break;
                case ARRETE_MINISTERIEL:
                    ArreteMinisteriel arreteMinisteriel = new ArreteMinisteriel();
                    arreteMinisteriel.setSectionCe(getSectionCeFromDossier(dossier));
                    fillArreteFromDossier(
                        session,
                        dossier,
                        acteType,
                        arreteMinisteriel,
                        affichagePublicationEtEpreuvage
                    );
                    dossierEpg.setArreteMinisteriel(arreteMinisteriel);
                    break;
                case ARRETE_INTERMINISTERIEL:
                    ArreteInterministeriel arreteInterministeriel = new ArreteInterministeriel();
                    fillArreteFromDossier(
                        session,
                        dossier,
                        acteType,
                        arreteInterministeriel,
                        affichagePublicationEtEpreuvage
                    );
                    dossierEpg.setArreteInterministeriel(arreteInterministeriel);
                    break;
                case ARRETE_PM:
                    ArretePM arretePM = new ArretePM();
                    fillArreteFromDossier(session, dossier, acteType, arretePM, affichagePublicationEtEpreuvage);
                    dossierEpg.setArretePm(arretePM);
                    break;
                case ARRETE_PR:
                    ArretePR arretePR = new ArretePR();
                    fillArreteFromDossier(session, dossier, acteType, arretePR, affichagePublicationEtEpreuvage);
                    dossierEpg.setArretePr(arretePR);
                    break;
                case ARRETE_CE:
                    ArreteCE arreteCE = new ArreteCE();
                    arreteCE.setSectionCe(getSectionCeFromDossier(dossier));
                    fillArreteFromDossier(session, dossier, acteType, arreteCE, affichagePublicationEtEpreuvage);
                    dossierEpg.setArreteCe(arreteCE);
                    break;
                case AVENANT:
                    Avenant avenant = new Avenant();
                    // propriétés spécifiques au type d'acte
                    avenant.setBaseLegale(dossier.getBaseLegaleActe());
                    avenant.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, avenant, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setAvenant(avenant);
                    break;
                case AVIS:
                    Avis avis = new Avis();
                    // propriétés spécifiques au type d'acte
                    avis.setBaseLegale(dossier.getBaseLegaleActe());
                    avis.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, avis, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setAvis(avis);
                    break;
                case CIRCULAIRE:
                    Circulaire circulaire = new Circulaire();
                    // propriétés spécifiques au type d'acte
                    circulaire.setBaseLegale(dossier.getBaseLegaleActe());
                    circulaire.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, circulaire, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setCirculaire(circulaire);
                    break;
                case CITATION:
                    Citation citation = new Citation();
                    // propriétés spécifiques au type d'acte
                    citation.setBaseLegale(dossier.getBaseLegaleActe());
                    citation.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, citation, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setCitation(citation);
                    break;
                case COMMUNIQUE:
                    Communique communique = new Communique();
                    // propriétés spécifiques au type d'acte
                    communique.setBaseLegale(dossier.getBaseLegaleActe());
                    communique.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, communique, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setCommunique(communique);
                    break;
                case CONVENTION:
                    Convention convention = new Convention();
                    // propriétés spécifiques au type d'acte
                    convention.setBaseLegale(dossier.getBaseLegaleActe());
                    convention.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, convention, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setConvention(convention);
                    break;
                case DECISION:
                    Decision decision = new Decision();
                    // propriétés spécifiques au type d'acte
                    decision.setBaseLegale(dossier.getBaseLegaleActe());
                    decision.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, decision, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setDecision(decision);
                    break;
                case DECRET_CE_ART_37:
                    DecretCEart37 decretCEArt37 = new DecretCEart37();
                    // propriété spécifique à ce type de décret
                    decretCEArt37.setSection(getSectionPosteFrom(dossier, session));
                    decretCEArt37.setSectionCe(getSectionCeFromDossier(dossier));
                    // propriétés communes à tous les autres types de décrets
                    fillDecretFromDossier(session, dossier, acteType, decretCEArt37, affichagePublicationEtEpreuvage);
                    dossierEpg.setDecretCeArt37(decretCEArt37);
                    break;
                case DECRET_CE:
                    DecretCE decretCE = new DecretCE();
                    // propriété spécifique à ce type de décret
                    decretCE.setSection(getSectionPosteFrom(dossier, session));
                    decretCE.setSectionCe(getSectionCeFromDossier(dossier));
                    // propriétés communes à tous les autres types de décrets
                    fillDecretFromDossier(session, dossier, acteType, decretCE, affichagePublicationEtEpreuvage);
                    dossierEpg.setDecretCe(decretCE);
                    break;
                case DECRET_CE_CM:
                    DecretCECM decretCECM = new DecretCECM();
                    // propriété spécifique à ce type de décret
                    decretCECM.setSection(getSectionPosteFrom(dossier, session));
                    decretCECM.setSectionCe(getSectionCeFromDossier(dossier));
                    // propriétés communes à tous les autres types de décrets
                    fillDecretFromDossier(session, dossier, acteType, decretCECM, affichagePublicationEtEpreuvage);
                    dossierEpg.setDecretCeCm(decretCECM);
                    break;
                case DECRET_CM:
                    DecretCM decretCM = new DecretCM();
                    fillDecretFromDossier(session, dossier, acteType, decretCM, affichagePublicationEtEpreuvage);
                    dossierEpg.setDecretCm(decretCM);
                    break;
                case DECRET_PR:
                    DecretPR decretPR = new DecretPR();
                    fillDecretFromDossier(session, dossier, acteType, decretPR, affichagePublicationEtEpreuvage);
                    dossierEpg.setDecretPr(decretPR);
                    break;
                case DECRET_SIMPLE:
                    DecretSimple decretSimple = new DecretSimple();
                    fillDecretFromDossier(session, dossier, acteType, decretSimple, affichagePublicationEtEpreuvage);
                    dossierEpg.setDecretSimple(decretSimple);
                    break;
                case DECRET_PUBLICATION_TRAITE_OU_ACCORD:
                    DecretPublicationDeTraiteOuAccord decretDePublicationDeTraiteOuAccord = new DecretPublicationDeTraiteOuAccord();
                    fillDecretFromDossier(
                        session,
                        dossier,
                        acteType,
                        decretDePublicationDeTraiteOuAccord,
                        affichagePublicationEtEpreuvage
                    );
                    dossierEpg.setDecretPublicationTraiteOuAccord(decretDePublicationDeTraiteOuAccord);
                    break;
                case DECRET_PR_CE:
                    DecretPRCE decretPRCE = new DecretPRCE();
                    // propriété spécifique à ce type de décret
                    // decretPRCE.setSection(getSectionPosteFrom(dossier,session));
                    decretPRCE.setSectionCe(getSectionCeFromDossier(dossier));
                    // propriétés communes à tous les autres types de décrets
                    fillDecretFromDossier(session, dossier, acteType, decretPRCE, affichagePublicationEtEpreuvage);
                    dossierEpg.setDecretPrCe(decretPRCE);
                    break;
                case DELIBERATION:
                    Deliberation deliberation = new Deliberation();
                    // propriétés spécifiques au type d'acte
                    deliberation.setBaseLegale(dossier.getBaseLegaleActe());
                    deliberation.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, deliberation, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setDeliberation(deliberation);
                    break;
                case DEMANDE_AVIS_CE:
                    DemandeAvisCE demandeAvisCE = new DemandeAvisCE();
                    // propriétés spécifiques au type d'acte
                    demandeAvisCE.setBaseLegale(dossier.getBaseLegaleActe());
                    demandeAvisCE.setSection(getSectionPosteFrom(dossier, session));
                    demandeAvisCE.setSectionCe(getSectionCeFromDossier(dossier));
                    demandeAvisCE.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, demandeAvisCE, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setDemandeAvisCe(demandeAvisCE);
                    break;
                case DIVERS:
                    Divers divers = new Divers();
                    // propriétés spécifiques au type d'acte
                    divers.setBaseLegale(dossier.getBaseLegaleActe());
                    divers.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, divers, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setDivers(divers);
                    break;
                case EXEQUATUR:
                    Exequatur exequatur = new Exequatur();
                    // propriétés spécifiques au type d'acte
                    exequatur.setBaseLegale(dossier.getBaseLegaleActe());
                    exequatur.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, exequatur, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setExequatur(exequatur);
                    break;
                case INFORMATIONS_PARLEMENTAIRES:
                    InformationsParlementaires informationsParlementaires = new InformationsParlementaires();
                    // propriétés spécifiques au type d'acte
                    informationsParlementaires.setEmetteur(dossier.getEmetteur());
                    informationsParlementaires.setRubrique(dossier.getRubrique());
                    informationsParlementaires.setCommentaire(dossier.getCommentaire());
                    informationsParlementaires.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    informationsParlementaires.setIdEvenement(dossier.getIdEvenement());
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(
                        session,
                        informationsParlementaires,
                        acteType,
                        dossier,
                        affichagePublicationEtEpreuvage
                    );
                    dossierEpg.setInformationsParlementaires(informationsParlementaires);
                    break;
                case INSTRUCTION:
                    Instruction instruction = new Instruction();
                    // propriétés spécifiques au type d'acte
                    instruction.setBaseLegale(dossier.getBaseLegaleActe());
                    instruction.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, instruction, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setInstruction(instruction);
                    break;
                case LISTE:
                    Liste liste = new Liste();
                    // propriétés spécifiques au type d'acte
                    liste.setBaseLegale(dossier.getBaseLegaleActe());
                    liste.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, liste, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setListe(liste);
                    break;
                case LOI:
                    Loi loi = new Loi();
                    // propriétés spécifiques au type d'acte
                    loi.setBaseLegale(dossier.getBaseLegaleActe());
                    loi.setSection(getSectionPosteFrom(dossier, session));
                    loi.setSectionCe(getSectionCeFromDossier(dossier));
                    loi.getTranspositionDirectiveEuropeenne().addAll(getTranspoDirEuropeenneList(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, loi, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setLoi(loi);
                    break;
                case LOI_ART_53_CONSTITUTION:
                    LoiArt53Constitution loiArt53Constitution = new LoiArt53Constitution();
                    // propriétés spécifiques au type d'acte
                    loiArt53Constitution.setBaseLegale(dossier.getBaseLegaleActe());
                    loiArt53Constitution.setSection(getSectionPosteFrom(dossier, session));
                    loiArt53Constitution.setSectionCe(getSectionCeFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(
                        session,
                        loiArt53Constitution,
                        acteType,
                        dossier,
                        affichagePublicationEtEpreuvage
                    );
                    dossierEpg.setLoiArt53Constitution(loiArt53Constitution);
                    break;
                case LOI_CONSTITUTIONNELLE:
                    LoiConstitutionnelle loiConstitutionnelle = new LoiConstitutionnelle();
                    // propriétés spécifiques au type d'acte
                    loiConstitutionnelle.setBaseLegale(dossier.getBaseLegaleActe());
                    loiConstitutionnelle.setSection(getSectionPosteFrom(dossier, session));
                    loiConstitutionnelle.setSectionCe(getSectionCeFromDossier(dossier));
                    loiConstitutionnelle
                        .getTranspositionDirectiveEuropeenne()
                        .addAll(getTranspoDirEuropeenneList(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(
                        session,
                        loiConstitutionnelle,
                        acteType,
                        dossier,
                        affichagePublicationEtEpreuvage
                    );
                    dossierEpg.setLoiConstitutionnelle(loiConstitutionnelle);
                    break;
                case LOI_ORGANIQUE:
                    LoiOrganique loiOrganique = new LoiOrganique();
                    // propriétés spécifiques au type d'acte
                    loiOrganique.setBaseLegale(dossier.getBaseLegaleActe());
                    loiOrganique.setSection(getSectionPosteFrom(dossier, session));
                    loiOrganique.setSectionCe(getSectionCeFromDossier(dossier));
                    loiOrganique.getTranspositionDirectiveEuropeenne().addAll(getTranspoDirEuropeenneList(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, loiOrganique, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setLoiOrganique(loiOrganique);
                    break;
                case NOTE:
                    Note note = new Note();
                    // propriétés spécifiques au type d'acte
                    note.setBaseLegale(dossier.getBaseLegaleActe());
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, note, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setNote(note);
                    break;
                case ORDONNANCE:
                    Ordonnance ordonnance = new Ordonnance();
                    // propriétés spécifiques au type d'acte
                    ordonnance.setBaseLegale(dossier.getBaseLegaleActe());
                    ordonnance.setPublicationRapportPresentation(dossier.getPublicationRapportPresentation());
                    ordonnance.setSection(getSectionPosteFrom(dossier, session));
                    ordonnance.setSectionCe(getSectionCeFromDossier(dossier));
                    ordonnance.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    ordonnance.getTranspositionDirectiveEuropeenne().addAll(getTranspoDirEuropeenneList(dossier));

                    // récupération des informations sur l'habilitation
                    Art38DispositionHabilitation art38DispositionHabilitation = new Art38DispositionHabilitation();
                    art38DispositionHabilitation.setCommentaire(dossier.getHabilitationCommentaire());
                    art38DispositionHabilitation.setReferenceLoi(dossier.getHabilitationRefLoi());
                    // note : affiche tous les numéros articles dans une seule
                    // balise <numero_article></numero_article>
                    art38DispositionHabilitation.getNumeroArticle().add(dossier.getHabilitationNumeroArticles());
                    ordonnance.setDispositionHabilitation(art38DispositionHabilitation);
                    // FEV547
                    ordonnance.setEntreprise(dossier.getTexteEntreprise());
                    ordonnance.getDateEffet().addAll(DateUtil.listCalendarToGregorianCalendar(dossier.getDateEffet()));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, ordonnance, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setOrdonnance(ordonnance);
                    break;
                case RAPPORT:
                    Rapport rapport = new Rapport();
                    // propriétés spécifiques au type d'acte
                    rapport.setBaseLegale(dossier.getBaseLegaleActe());
                    rapport.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, rapport, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setRapport(rapport);
                    break;
                case RECTIFICATIF:
                    Rectificatif rectificatif = new Rectificatif();
                    // propriétés spécifiques au type d'acte
                    rectificatif.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, rectificatif, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setRectificatif(rectificatif);
                    break;
                case TABLEAU:
                    Tableau tableau = new Tableau();
                    // propriétés spécifiques au type d'acte
                    tableau.setBaseLegale(dossier.getBaseLegaleActe());
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, tableau, acteType, dossier, affichagePublicationEtEpreuvage);
                    dossierEpg.setTableau(tableau);
                    break;
                case ARRETE_MINISTERIEL_INDIVIDUEL:
                    ArreteMinisterielIndividuel arreteMinisterielInd = new ArreteMinisterielIndividuel();
                    fillArreteFromDossier(
                        session,
                        dossier,
                        acteType,
                        arreteMinisterielInd,
                        affichagePublicationEtEpreuvage
                    );
                    dossierEpg.setArreteMinisterielInd(arreteMinisterielInd);
                    break;
                case ARRETE_INTERMINISTERIEL_INDIVIDUEL:
                    ArreteInterministerielIndividuel arreteInterministerielInd = new ArreteInterministerielIndividuel();
                    fillArreteFromDossier(
                        session,
                        dossier,
                        acteType,
                        arreteInterministerielInd,
                        affichagePublicationEtEpreuvage
                    );
                    dossierEpg.setArreteInterministerielInd(arreteInterministerielInd);
                    break;
                case ARRETE_PM_INDIVIDUEL:
                    ArretePMIndividuel arretePMInd = new ArretePMIndividuel();
                    fillArreteFromDossier(session, dossier, acteType, arretePMInd, affichagePublicationEtEpreuvage);
                    dossierEpg.setArretePmInd(arretePMInd);
                    break;
                case ARRETE_PR_INDIVIDUEL:
                    ArretePRIndividuel arretePRInd = new ArretePRIndividuel();
                    fillArreteFromDossier(session, dossier, acteType, arretePRInd, affichagePublicationEtEpreuvage);
                    dossierEpg.setArretePrInd(arretePRInd);
                    break;
                case DECRET_CE_INDIVIDUEL:
                    DecretCEIndividuel decretCEIndividuel = new DecretCEIndividuel();
                    // propriété spécifique à ce type de décret
                    decretCEIndividuel.setSection(getSectionPosteFrom(dossier, session));
                    decretCEIndividuel.setSectionCe(getSectionCeFromDossier(dossier));
                    // propriétés communes à tous les autres types de décrets
                    fillDecretFromDossier(
                        session,
                        dossier,
                        acteType,
                        decretCEIndividuel,
                        affichagePublicationEtEpreuvage
                    );
                    dossierEpg.setDecretCeInd(decretCEIndividuel);
                    break;
                case DECRET_CE_CM_INDIVIDUEL:
                    DecretCECMIndividuel decretCECMIndividuel = new DecretCECMIndividuel();
                    // propriété spécifique à ce type de décret
                    decretCECMIndividuel.setSection(getSectionPosteFrom(dossier, session));
                    decretCECMIndividuel.setSectionCe(getSectionCeFromDossier(dossier));
                    // propriétés communes à tous les autres types de décrets
                    fillDecretFromDossier(
                        session,
                        dossier,
                        acteType,
                        decretCECMIndividuel,
                        affichagePublicationEtEpreuvage
                    );
                    dossierEpg.setDecretCeCmInd(decretCECMIndividuel);
                    break;
                case DECRET_CM_INDIVIDUEL:
                    DecretCMIndividuel decretCMIndividuel = new DecretCMIndividuel();
                    fillDecretFromDossier(
                        session,
                        dossier,
                        acteType,
                        decretCMIndividuel,
                        affichagePublicationEtEpreuvage
                    );
                    dossierEpg.setDecretCmInd(decretCMIndividuel);
                    break;
                case DECRET_PR_INDIVIDUEL:
                    DecretPRIndividuel decretPRIndividuel = new DecretPRIndividuel();
                    fillDecretFromDossier(
                        session,
                        dossier,
                        acteType,
                        decretPRIndividuel,
                        affichagePublicationEtEpreuvage
                    );
                    dossierEpg.setDecretPrInd(decretPRIndividuel);
                    break;
                case DECRET_SIMPLE_INDIVIDUEL:
                    DecretSimpleIndividuel decretSimpleIndividuel = new DecretSimpleIndividuel();
                    fillDecretFromDossier(
                        session,
                        dossier,
                        acteType,
                        decretSimpleIndividuel,
                        affichagePublicationEtEpreuvage
                    );
                    dossierEpg.setDecretSimpleInd(decretSimpleIndividuel);
                    break;
                case ACCORD_COLLECTIF:
                    AccordCollectif accord = new AccordCollectif();

                    fillDecretFromDossier(session, dossier, acteType, accord, affichagePublicationEtEpreuvage);
                    dossierEpg.setAccordCollectif(accord);
                    break;
                // note: à utiliser si besoin d'implémenter un nouveau type d'acte
                // case TACHE_GENERIQUE:
                // TacheGenerique tacheGenerique = new TacheGenerique();
                // fillActeFromDossier(session,
                // (Acte)tacheGenerique,acteType,dossier);
                // dossierEpg.setTacheGenerique(tacheGenerique);
                // break;

                default:
                    break;
            }
        }

        return dossierEpg;
    }

    /**
     * Créé et remplit un objet DossierEpg à partir d'un objet Dossier
     *
     * @param dossier
     *            dossier
     * @return un objet DossierEpg
     */
    public static BORDEREAU getBordereauFromDossier(
        Dossier dossier,
        CoreSession session,
        Boolean affichagePublicationEtEpreuvage
    ) {
        BORDEREAU bordereau = new BORDEREAU();
        String typeActe = dossier.getTypeActe();
        ActeType acteType = WsUtil.getWsActeTypeFromDossierTypeActe(typeActe);
        if (acteType != null) {
            switch (acteType) {
                case AMNISTIE:
                    Amnistie amnistie = new Amnistie();
                    // propriétés spécifiques au type d'acte
                    amnistie.setBaseLegale(dossier.getBaseLegaleActe());
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, amnistie, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setAmnistie(amnistie);
                    break;
                case ARRETE_MINISTERIEL:
                    ArreteMinisteriel arreteMinisteriel = new ArreteMinisteriel();
                    fillArreteFromDossier(
                        session,
                        dossier,
                        acteType,
                        arreteMinisteriel,
                        affichagePublicationEtEpreuvage
                    );
                    bordereau.setArreteMinisteriel(arreteMinisteriel);
                    break;
                case ARRETE_INTERMINISTERIEL:
                    ArreteInterministeriel arreteInterministeriel = new ArreteInterministeriel();
                    fillArreteFromDossier(
                        session,
                        dossier,
                        acteType,
                        arreteInterministeriel,
                        affichagePublicationEtEpreuvage
                    );
                    bordereau.setArreteInterministeriel(arreteInterministeriel);
                    break;
                case ARRETE_PM:
                    ArretePM arretePM = new ArretePM();
                    fillArreteFromDossier(session, dossier, acteType, arretePM, affichagePublicationEtEpreuvage);
                    bordereau.setArretePm(arretePM);
                    break;
                case ARRETE_PR:
                    ArretePR arretePR = new ArretePR();
                    fillArreteFromDossier(session, dossier, acteType, arretePR, affichagePublicationEtEpreuvage);
                    bordereau.setArretePr(arretePR);
                    break;
                case ARRETE_CE:
                    ArreteCE arreteCE = new ArreteCE();
                    arreteCE.setSectionCe(getSectionCeFromDossier(dossier));
                    fillArreteFromDossier(session, dossier, acteType, arreteCE, affichagePublicationEtEpreuvage);
                    bordereau.setArreteCe(arreteCE);
                    break;
                case AVENANT:
                    Avenant avenant = new Avenant();
                    // propriétés spécifiques au type d'acte
                    avenant.setBaseLegale(dossier.getBaseLegaleActe());
                    avenant.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, avenant, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setAvenant(avenant);
                    break;
                case AVIS:
                    Avis avis = new Avis();
                    // propriétés spécifiques au type d'acte
                    avis.setBaseLegale(dossier.getBaseLegaleActe());
                    avis.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, avis, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setAvis(avis);
                    break;
                case CIRCULAIRE:
                    Circulaire circulaire = new Circulaire();
                    // propriétés spécifiques au type d'acte
                    circulaire.setBaseLegale(dossier.getBaseLegaleActe());
                    circulaire.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, circulaire, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setCirculaire(circulaire);
                    break;
                case CITATION:
                    Citation citation = new Citation();
                    // propriétés spécifiques au type d'acte
                    citation.setBaseLegale(dossier.getBaseLegaleActe());
                    citation.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, citation, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setCitation(citation);
                    break;
                case COMMUNIQUE:
                    Communique communique = new Communique();
                    // propriétés spécifiques au type d'acte
                    communique.setBaseLegale(dossier.getBaseLegaleActe());
                    communique.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, communique, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setCommunique(communique);
                    break;
                case CONVENTION:
                    Convention convention = new Convention();
                    // propriétés spécifiques au type d'acte
                    convention.setBaseLegale(dossier.getBaseLegaleActe());
                    convention.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, convention, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setConvention(convention);
                    break;
                case DECISION:
                    Decision decision = new Decision();
                    // propriétés spécifiques au type d'acte
                    decision.setBaseLegale(dossier.getBaseLegaleActe());
                    decision.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, decision, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setDecision(decision);
                    break;
                case DECRET_CE_ART_37:
                    DecretCEart37 decretCEArt37 = new DecretCEart37();
                    // propriété spécifique à ce type de décret
                    decretCEArt37.setSection(getSectionPosteFrom(dossier, session));
                    decretCEArt37.setSectionCe(getSectionCeFromDossier(dossier));
                    // propriétés communes à tous les autres types de décrets
                    fillDecretFromDossier(session, dossier, acteType, decretCEArt37, affichagePublicationEtEpreuvage);
                    bordereau.setDecretCeArt37(decretCEArt37);
                    break;
                case DECRET_CE:
                    DecretCE decretCE = new DecretCE();
                    // propriété spécifique à ce type de décret
                    decretCE.setSection(getSectionPosteFrom(dossier, session));
                    decretCE.setSectionCe(getSectionCeFromDossier(dossier));
                    // propriétés communes à tous les autres types de décrets
                    fillDecretFromDossier(session, dossier, acteType, decretCE, affichagePublicationEtEpreuvage);
                    bordereau.setDecretCe(decretCE);
                    break;
                case DECRET_CE_CM:
                    DecretCECM decretCECM = new DecretCECM();
                    // propriété spécifique à ce type de décret
                    decretCECM.setSection(getSectionPosteFrom(dossier, session));
                    decretCECM.setSectionCe(getSectionCeFromDossier(dossier));
                    // propriétés communes à tous les autres types de décrets
                    fillDecretFromDossier(session, dossier, acteType, decretCECM, affichagePublicationEtEpreuvage);
                    bordereau.setDecretCeCm(decretCECM);
                    break;
                case DECRET_CM:
                    DecretCM decretCM = new DecretCM();
                    fillDecretFromDossier(session, dossier, acteType, decretCM, affichagePublicationEtEpreuvage);
                    bordereau.setDecretCm(decretCM);
                    break;
                case DECRET_PR:
                    DecretPR decretPR = new DecretPR();
                    fillDecretFromDossier(session, dossier, acteType, decretPR, affichagePublicationEtEpreuvage);
                    bordereau.setDecretPr(decretPR);
                    break;
                case DECRET_SIMPLE:
                    DecretSimple decretSimple = new DecretSimple();
                    fillDecretFromDossier(session, dossier, acteType, decretSimple, affichagePublicationEtEpreuvage);
                    bordereau.setDecretSimple(decretSimple);
                    break;
                case DECRET_PUBLICATION_TRAITE_OU_ACCORD:
                    DecretPublicationDeTraiteOuAccord decretDePublicationDeTraiteOuAccord = new DecretPublicationDeTraiteOuAccord();
                    fillDecretFromDossier(
                        session,
                        dossier,
                        acteType,
                        decretDePublicationDeTraiteOuAccord,
                        affichagePublicationEtEpreuvage
                    );
                    bordereau.setDecretPublicationTraiteOuAccord(decretDePublicationDeTraiteOuAccord);
                    break;
                case DECRET_PR_CE:
                    DecretPRCE decretPRCE = new DecretPRCE();
                    // propriété spécifique à ce type de décret
                    // decretPRCE.setSection(getSectionPosteFrom(dossier,session));
                    decretPRCE.setSectionCe(getSectionCeFromDossier(dossier));
                    // propriétés communes à tous les autres types de décrets
                    fillDecretFromDossier(session, dossier, acteType, decretPRCE, affichagePublicationEtEpreuvage);
                    bordereau.setDecretPrCe(decretPRCE);
                    break;
                case DELIBERATION:
                    Deliberation deliberation = new Deliberation();
                    // propriétés spécifiques au type d'acte
                    deliberation.setBaseLegale(dossier.getBaseLegaleActe());
                    deliberation.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, deliberation, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setDeliberation(deliberation);
                    break;
                case DEMANDE_AVIS_CE:
                    DemandeAvisCE demandeAvisCE = new DemandeAvisCE();
                    // propriétés spécifiques au type d'acte
                    demandeAvisCE.setBaseLegale(dossier.getBaseLegaleActe());
                    demandeAvisCE.setSection(getSectionPosteFrom(dossier, session));
                    demandeAvisCE.setSectionCe(getSectionCeFromDossier(dossier));
                    demandeAvisCE.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, demandeAvisCE, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setDemandeAvisCe(demandeAvisCE);
                    break;
                case DIVERS:
                    Divers divers = new Divers();
                    // propriétés spécifiques au type d'acte
                    divers.setBaseLegale(dossier.getBaseLegaleActe());
                    divers.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, divers, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setDivers(divers);
                    break;
                case EXEQUATUR:
                    Exequatur exequatur = new Exequatur();
                    // propriétés spécifiques au type d'acte
                    exequatur.setBaseLegale(dossier.getBaseLegaleActe());
                    exequatur.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, exequatur, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setExequatur(exequatur);
                    break;
                case INFORMATIONS_PARLEMENTAIRES:
                    InformationsParlementaires informationsParlementaires = new InformationsParlementaires();
                    // propriétés spécifiques au type d'acte
                    informationsParlementaires.setEmetteur(dossier.getEmetteur());
                    informationsParlementaires.setRubrique(dossier.getRubrique());
                    informationsParlementaires.setCommentaire(dossier.getCommentaire());
                    informationsParlementaires.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    informationsParlementaires.setIdEvenement(dossier.getIdEvenement());
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(
                        session,
                        informationsParlementaires,
                        acteType,
                        dossier,
                        affichagePublicationEtEpreuvage
                    );
                    bordereau.setInformationsParlementaires(informationsParlementaires);
                    break;
                case INSTRUCTION:
                    Instruction instruction = new Instruction();
                    // propriétés spécifiques au type d'acte
                    instruction.setBaseLegale(dossier.getBaseLegaleActe());
                    instruction.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, instruction, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setInstruction(instruction);
                    break;
                case LISTE:
                    Liste liste = new Liste();
                    // propriétés spécifiques au type d'acte
                    liste.setBaseLegale(dossier.getBaseLegaleActe());
                    liste.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, liste, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setListe(liste);
                    break;
                case LOI:
                    Loi loi = new Loi();
                    // propriétés spécifiques au type d'acte
                    loi.setBaseLegale(dossier.getBaseLegaleActe());
                    loi.setSection(getSectionPosteFrom(dossier, session));
                    loi.setSectionCe(getSectionCeFromDossier(dossier));
                    loi.getTranspositionDirectiveEuropeenne().addAll(getTranspoDirEuropeenneList(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, loi, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setLoi(loi);
                    break;
                case LOI_ART_53_CONSTITUTION:
                    LoiArt53Constitution loiArt53Constitution = new LoiArt53Constitution();
                    // propriétés spécifiques au type d'acte
                    loiArt53Constitution.setBaseLegale(dossier.getBaseLegaleActe());
                    loiArt53Constitution.setSection(getSectionPosteFrom(dossier, session));
                    loiArt53Constitution.setSectionCe(getSectionCeFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(
                        session,
                        loiArt53Constitution,
                        acteType,
                        dossier,
                        affichagePublicationEtEpreuvage
                    );
                    bordereau.setLoiArt53Constitution(loiArt53Constitution);
                    break;
                case LOI_CONSTITUTIONNELLE:
                    LoiConstitutionnelle loiConstitutionnelle = new LoiConstitutionnelle();
                    // propriétés spécifiques au type d'acte
                    loiConstitutionnelle.setBaseLegale(dossier.getBaseLegaleActe());
                    loiConstitutionnelle.setSection(getSectionPosteFrom(dossier, session));
                    loiConstitutionnelle.setSectionCe(getSectionCeFromDossier(dossier));
                    loiConstitutionnelle
                        .getTranspositionDirectiveEuropeenne()
                        .addAll(getTranspoDirEuropeenneList(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(
                        session,
                        loiConstitutionnelle,
                        acteType,
                        dossier,
                        affichagePublicationEtEpreuvage
                    );
                    bordereau.setLoiConstitutionnelle(loiConstitutionnelle);
                    break;
                case LOI_ORGANIQUE:
                    LoiOrganique loiOrganique = new LoiOrganique();
                    // propriétés spécifiques au type d'acte
                    loiOrganique.setBaseLegale(dossier.getBaseLegaleActe());
                    loiOrganique.setSection(getSectionPosteFrom(dossier, session));
                    loiOrganique.setSectionCe(getSectionCeFromDossier(dossier));
                    loiOrganique.getTranspositionDirectiveEuropeenne().addAll(getTranspoDirEuropeenneList(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, loiOrganique, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setLoiOrganique(loiOrganique);
                    break;
                case NOTE:
                    Note note = new Note();
                    // propriétés spécifiques au type d'acte
                    note.setBaseLegale(dossier.getBaseLegaleActe());
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, note, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setNote(note);
                    break;
                case ORDONNANCE:
                    Ordonnance ordonnance = new Ordonnance();
                    // propriétés spécifiques au type d'acte
                    ordonnance.setBaseLegale(dossier.getBaseLegaleActe());
                    ordonnance.setPublicationRapportPresentation(dossier.getPublicationRapportPresentation());
                    ordonnance.setSection(getSectionPosteFrom(dossier, session));
                    ordonnance.setSectionCe(getSectionCeFromDossier(dossier));
                    ordonnance.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    ordonnance.getTranspositionDirectiveEuropeenne().addAll(getTranspoDirEuropeenneList(dossier));

                    // récupération des informations sur l'habilitation
                    Art38DispositionHabilitation art38DispositionHabilitation = new Art38DispositionHabilitation();
                    art38DispositionHabilitation.setCommentaire(dossier.getHabilitationCommentaire());
                    art38DispositionHabilitation.setReferenceLoi(dossier.getHabilitationRefLoi());
                    // note : affiche tous les numéros articles dans une seule balise <numero_article></numero_article>
                    art38DispositionHabilitation.getNumeroArticle().add(dossier.getHabilitationNumeroArticles());
                    ordonnance.setDispositionHabilitation(art38DispositionHabilitation);

                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, ordonnance, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setOrdonnance(ordonnance);
                    break;
                case RAPPORT:
                    Rapport rapport = new Rapport();
                    // propriétés spécifiques au type d'acte
                    rapport.setBaseLegale(dossier.getBaseLegaleActe());
                    rapport.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, rapport, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setRapport(rapport);
                    break;
                case RECTIFICATIF:
                    Rectificatif rectificatif = new Rectificatif();
                    // propriétés spécifiques au type d'acte
                    rectificatif.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, rectificatif, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setRectificatif(rectificatif);
                    break;
                case TABLEAU:
                    Tableau tableau = new Tableau();
                    // propriétés spécifiques au type d'acte
                    tableau.setBaseLegale(dossier.getBaseLegaleActe());
                    // propriétés communes à tous les types d'actes
                    fillActeFromDossier(session, tableau, acteType, dossier, affichagePublicationEtEpreuvage);
                    bordereau.setTableau(tableau);
                    break;
                case ARRETE_MINISTERIEL_INDIVIDUEL:
                    ArreteMinisterielIndividuel arreteMinisterielInd = new ArreteMinisterielIndividuel();
                    fillArreteFromDossier(
                        session,
                        dossier,
                        acteType,
                        arreteMinisterielInd,
                        affichagePublicationEtEpreuvage
                    );
                    bordereau.setArreteMinisterielInd(arreteMinisterielInd);
                    break;
                case ARRETE_INTERMINISTERIEL_INDIVIDUEL:
                    ArreteInterministerielIndividuel arreteInterministerielInd = new ArreteInterministerielIndividuel();
                    fillArreteFromDossier(
                        session,
                        dossier,
                        acteType,
                        arreteInterministerielInd,
                        affichagePublicationEtEpreuvage
                    );
                    bordereau.setArreteInterministerielInd(arreteInterministerielInd);
                    break;
                case ARRETE_PM_INDIVIDUEL:
                    ArretePMIndividuel arretePMInd = new ArretePMIndividuel();
                    fillArreteFromDossier(session, dossier, acteType, arretePMInd, affichagePublicationEtEpreuvage);
                    bordereau.setArretePmInd(arretePMInd);
                    break;
                case ARRETE_PR_INDIVIDUEL:
                    ArretePRIndividuel arretePRInd = new ArretePRIndividuel();
                    fillArreteFromDossier(session, dossier, acteType, arretePRInd, affichagePublicationEtEpreuvage);
                    bordereau.setArretePrInd(arretePRInd);
                    break;
                case DECRET_CE_INDIVIDUEL:
                    DecretCEIndividuel decretCEIndividuel = new DecretCEIndividuel();
                    // propriété spécifique à ce type de décret
                    decretCEIndividuel.setSection(getSectionPosteFrom(dossier, session));
                    decretCEIndividuel.setSectionCe(getSectionCeFromDossier(dossier));
                    // propriétés communes à tous les autres types de décrets
                    fillDecretFromDossier(
                        session,
                        dossier,
                        acteType,
                        decretCEIndividuel,
                        affichagePublicationEtEpreuvage
                    );
                    bordereau.setDecretCeInd(decretCEIndividuel);
                    break;
                case DECRET_CE_CM_INDIVIDUEL:
                    DecretCECMIndividuel decretCECMIndividuel = new DecretCECMIndividuel();
                    // propriété spécifique à ce type de décret
                    decretCECMIndividuel.setSection(getSectionPosteFrom(dossier, session));
                    decretCECMIndividuel.setSectionCe(getSectionCeFromDossier(dossier));
                    // propriétés communes à tous les autres types de décrets
                    fillDecretFromDossier(
                        session,
                        dossier,
                        acteType,
                        decretCECMIndividuel,
                        affichagePublicationEtEpreuvage
                    );
                    bordereau.setDecretCeCmInd(decretCECMIndividuel);
                    break;
                case DECRET_CM_INDIVIDUEL:
                    DecretCMIndividuel decretCMIndividuel = new DecretCMIndividuel();
                    fillDecretFromDossier(
                        session,
                        dossier,
                        acteType,
                        decretCMIndividuel,
                        affichagePublicationEtEpreuvage
                    );
                    bordereau.setDecretCmInd(decretCMIndividuel);
                    break;
                case DECRET_PR_INDIVIDUEL:
                    DecretPRIndividuel decretPRIndividuel = new DecretPRIndividuel();
                    fillDecretFromDossier(
                        session,
                        dossier,
                        acteType,
                        decretPRIndividuel,
                        affichagePublicationEtEpreuvage
                    );
                    bordereau.setDecretPrInd(decretPRIndividuel);
                    break;
                case DECRET_SIMPLE_INDIVIDUEL:
                    DecretSimpleIndividuel decretSimpleIndividuel = new DecretSimpleIndividuel();
                    fillDecretFromDossier(
                        session,
                        dossier,
                        acteType,
                        decretSimpleIndividuel,
                        affichagePublicationEtEpreuvage
                    );
                    bordereau.setDecretSimpleInd(decretSimpleIndividuel);
                    break;
                case ACCORD_COLLECTIF:
                    AccordCollectif accord = new AccordCollectif();
                    fillDecretFromDossier(session, dossier, acteType, accord, affichagePublicationEtEpreuvage);
                    bordereau.setAccordCollectif(accord);
                    break;
                // note: à utiliser si besoin d'implémenter un nouveau type d'acte
                // case TACHE_GENERIQUE:
                // TacheGenerique tacheGenerique = new TacheGenerique();
                // fillActeFromDossier(session, (Acte)tacheGenerique,acteType,dossier);
                // dossierEpg.setTacheGenerique(tacheGenerique);
                // break;

                default:
                    break;
            }
        }

        return bordereau;
    }

    /**
     * Récupère le poste de l'étape courante de la feuille de route : utilisé uniquement pour les dossiers destiné au
     * Conseil d'Etat.
     *
     * @param dossier
     * @return
     */
    private static String getSectionPosteFrom(Dossier dossier, CoreSession session) {
        String section = null;
        List<String> sectionLabelListe = new ArrayList<>();
        try {
            DocumentModel dossierDocModel = dossier.getDocument();
            // récupération de la feuille de route
            DocumentModel feuilleRouteDocModel = SolonEpgServiceLocator
                .getDossierDistributionService()
                .getLastDocumentRouteForDossier(session, dossierDocModel);
            // récupération des étapes de feuille de route courante
            List<DocumentModel> runningStep = SolonEpgServiceLocator
                .getEPGFeuilleRouteService()
                .getRunningSteps(session, feuilleRouteDocModel.getId());
            if (runningStep != null && runningStep.size() > 0) {
                // Chargement des services
                final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
                final STPostesService postesService = STServiceLocator.getSTPostesService();
                final EpgOrganigrammeService epgOrganigrammeService = SolonEpgServiceLocator.getEpgOrganigrammeService();
                final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
                // récupération du ministère ce
                DocumentModel tableReferenceDoc = SolonEpgServiceLocator
                    .getTableReferenceService()
                    .getTableReference(session);
                TableReference tableReference = tableReferenceDoc.getAdapter(TableReference.class);
                String ministereCeId = tableReference.getMinistereCE();
                // récupération des libellés des postes des étapes courantes si ils appartiennent au ministère Conseil
                // d'Etat
                for (DocumentModel documentModel : runningStep) {
                    // récupération du poste à partir de l'étape courante
                    SSRouteStep routeStep = documentModel.getAdapter(SSRouteStep.class);
                    String distributionMailboxId = routeStep.getDistributionMailboxId();
                    String posteId = mailboxPosteService.getPosteIdFromMailboxId(distributionMailboxId);
                    // vérification que le poste est un poste du Conseil d'Etat
                    boolean isPosteCe = false;
                    List<EntiteNode> entiteNodeList = ministeresService.getMinistereParentFromPoste(posteId);
                    for (EntiteNode entiteNode : entiteNodeList) {
                        if (ministereCeId != null && ministereCeId.equals(entiteNode.getId())) {
                            isPosteCe = true;
                            break;
                        }
                    }
                    if (isPosteCe) {
                        // récupération du libellé de la section (direction)
                        OrganigrammeNode posteNode = postesService.getPoste(posteId);
                        List<OrganigrammeNode> sectionNodeList = epgOrganigrammeService.getParentList(posteNode);
                        if (sectionNodeList != null && sectionNodeList.size() == 1) {
                            OrganigrammeNode sectionNode = sectionNodeList.get(0);
                            sectionLabelListe.add(sectionNode.getLabel());
                        } else if (sectionNodeList != null && sectionNodeList.size() > 1) {
                            // si le poste a plusieurs parents, on sélectionne la permière direction du conseil d'état
                            for (OrganigrammeNode uniteStructurelleNode : sectionNodeList) {
                                List<EntiteNode> ministereNodeListFromParentNode = ministeresService.getMinistereParentFromUniteStructurelle(
                                    uniteStructurelleNode.getId()
                                );
                                boolean isUniteStructurelleCe = false;
                                for (EntiteNode ministereNode : ministereNodeListFromParentNode) {
                                    isPosteCe = false;
                                    if (ministereCeId != null && ministereCeId.equals(ministereNode.getId())) {
                                        isUniteStructurelleCe = true;
                                        break;
                                    }
                                }
                                if (isUniteStructurelleCe) {
                                    // l'unité structurelle fait parti du ministère CE : on récupère son label
                                    sectionLabelListe.add(uniteStructurelleNode.getLabel());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (NuxeoException exc) {
            LOGGER.error(session, EpgLogEnumImpl.FAIL_GET_SECTION_CE_TEC, dossier.getDocument(), exc);
        }
        if (sectionLabelListe.size() > 0) {
            section = StringUtils.join(sectionLabelListe.toArray(), ", ");
        }
        return section;
    }

    /**
     * Récupère la section du conseil d'etat.
     *
     * @param dossier
     *
     * @return SectionCe
     */
    private static SectionCe getSectionCeFromDossier(Dossier dossier) {
        SectionCe sectionCe = new SectionCe();
        ConseilEtat conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);
        sectionCe.setSectionCe(conseilEtat.getSectionCe());
        if (conseilEtat.getRapporteurCe() != null && !conseilEtat.getRapporteurCe().isEmpty()) {
            for (String rapporteur : conseilEtat.getRapporteurCe().split(", ")) {
                sectionCe.getRapporteur().add(rapporteur);
            }
        }
        sectionCe.setNumeroIsa(conseilEtat.getNumeroISA());
        sectionCe.setDateSectionCe(DateUtil.calendarToXMLGregorianCalendar(conseilEtat.getDateSectionCe()));
        sectionCe.setDateAgCe(DateUtil.calendarToXMLGregorianCalendar(conseilEtat.getDateAgCe()));
        sectionCe.setDateTransmissionSectionCe(
            DateUtil.calendarToXMLGregorianCalendar(conseilEtat.getDateTransmissionSectionCe())
        );
        if (conseilEtat.getPriorite() != null && !conseilEtat.getPriorite().isEmpty()) {
            sectionCe.setPriorite(new BigInteger(conseilEtat.getPriorite()));
        }
        return sectionCe;
    }

    /**
     * Récupére les propriétés spécifiques aux décrets.
     *
     * @param dossier
     * @param acteType
     * @param decret
     */
    private static void fillDecretFromDossier(
        CoreSession session,
        Dossier dossier,
        ActeType acteType,
        Decret decret,
        Boolean affichagePublicationEtEpreuvage
    ) {
        // propriétés spécifiques aux décrets
        decret.setBaseLegale(dossier.getBaseLegaleActe());
        decret.setPublicationRapportPresentation(dossier.getPublicationRapportPresentation());
        decret.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));
        decret.setDecretNumerote(dossier.getDecretNumerote());

        // mesure application des lois
        decret.getMesureApplicationLoi().addAll(getMesureApplicationLoiList(dossier));

        // transposition ordonnance
        decret.getTranspositionOrdonnance().addAll(getTranspositionOrdonnanceList(dossier));

        // récupération des transpositions de directives européennes
        decret.getTranspositionDirectiveEuropeenne().addAll(getTranspoDirEuropeenneList(dossier));

        // Texte entreprise et date d'effet
        decret.setEntreprise(dossier.getTexteEntreprise());
        decret.getDateEffet().addAll(DateUtil.listCalendarToGregorianCalendar(dossier.getDateEffet()));

        // propriétés communes à tous les types d'actes
        fillActeFromDossier(session, decret, acteType, dossier, affichagePublicationEtEpreuvage);
    }

    /**
     * Remplit un DossierEpg de type Arrete à partir d'un dossier
     *
     * @param dossier
     * @param acteType
     * @param arrete
     */
    private static void fillArreteFromDossier(
        CoreSession session,
        Dossier dossier,
        ActeType acteType,
        Arrete arrete,
        Boolean affichagePublicationEtEpreuvage
    ) {
        // récupération base légale
        arrete.setBaseLegale(dossier.getBaseLegaleActe());

        // récupération publication intégrale ou extrait
        arrete.setPublicationIntegraleOuExtrait(getTypePublicationFromDossier(dossier));

        // récupération des transpositions de directives européennes
        arrete.getTranspositionDirectiveEuropeenne().addAll(getTranspoDirEuropeenneList(dossier));

        // Texte entreprise et date d'effet
        arrete.setEntreprise(dossier.getTexteEntreprise());
        arrete.getDateEffet().addAll(DateUtil.listCalendarToGregorianCalendar(dossier.getDateEffet()));

        // propriétés communes à tous les types d'actes
        fillActeFromDossier(session, arrete, acteType, dossier, affichagePublicationEtEpreuvage);
    }

    /**
     * Récupère la liste des transpositions de directives européennes.
     *
     * @param dossier
     *
     * @return List<TranspositionDirectiveEuropeenne>
     */
    private static List<TranspositionDirectiveEuropeenne> getTranspoDirEuropeenneList(Dossier dossier) {
        List<ComplexeType> transpositionDirectiveList = new ArrayList<>(
            dossier.getTranspositionDirective().getTranspositions()
        );
        List<TranspositionDirectiveEuropeenne> transpoDirEuropeenneList = new ArrayList<>();

        // récupération des transpositions de directives européennes
        for (ComplexeType transpositionDirective : transpositionDirectiveList) {
            TranspositionDirectiveEuropeenne transpositionDirectiveEuropeenne = new TranspositionDirectiveEuropeenne();
            Map<String, Serializable> transpositionDirectiveMap = transpositionDirective.getSerializableMap();

            // récupération des propriétés d'une transposition de directive européenne
            transpositionDirectiveEuropeenne.setCommentaire(
                (String) transpositionDirectiveMap.get(
                    DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_COMMENTAIRE_PROPERTY
                )
            );
            transpositionDirectiveEuropeenne.setReferenceDirective(
                (String) transpositionDirectiveMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY)
            );
            transpositionDirectiveEuropeenne.setTitreDirective(
                (String) transpositionDirectiveMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY)
            );
            // ajout à la liste
            transpoDirEuropeenneList.add(transpositionDirectiveEuropeenne);
        }
        return transpoDirEuropeenneList;
    }

    /**
     * Récupère la liste des mesures d'applications des lois.
     *
     * @param dossier
     *
     * @return List<MesureApplicationLoi>
     */
    private static List<MesureApplicationLoi> getMesureApplicationLoiList(Dossier dossier) {
        List<ComplexeType> applicationsLois = new ArrayList<>(dossier.getApplicationLoi().getTranspositions());
        List<MesureApplicationLoi> mesureApplicationLoiList = new ArrayList<>();

        // récupération des transpositions de directives européennes
        for (ComplexeType applicationLoi : applicationsLois) {
            MesureApplicationLoi mesureApplicationLoi = new MesureApplicationLoi();
            Map<String, Serializable> mesureApplicationLoiMap = applicationLoi.getSerializableMap();

            // récupération des propriétés d'une transposition de directive européenne
            mesureApplicationLoi.setCommentaire(
                (String) mesureApplicationLoiMap.get(
                    DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_COMMENTAIRE_PROPERTY
                )
            );
            mesureApplicationLoi.setReferenceLoi(
                (String) mesureApplicationLoiMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY)
            );
            mesureApplicationLoi.setReferenceMesure(
                (String) mesureApplicationLoiMap.get(DossierSolonEpgConstants.DOSSIER_LOI_APPLIQUEE_REF_MESURE_PROPERTY)
            );
            mesureApplicationLoi.setTitreLoi(
                (String) mesureApplicationLoiMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY)
            );
            // ajout à la liste
            mesureApplicationLoiList.add(mesureApplicationLoi);
        }
        return mesureApplicationLoiList;
    }

    /**
     * Récupère la liste des mesures d'applications des lois.
     *
     * @param dossier
     *
     * @return List<TranspositionOrdonnance>
     */
    private static List<TranspositionOrdonnance> getTranspositionOrdonnanceList(Dossier dossier) {
        List<ComplexeType> transpositionsOrdonnaces = new ArrayList<>(
            dossier.getTranspositionOrdonnance().getTranspositions()
        );
        List<TranspositionOrdonnance> transpositionOrdonnanceList = new ArrayList<>();

        // récupération des transpositions de directives européennes
        for (ComplexeType transOrdonnance : transpositionsOrdonnaces) {
            TranspositionOrdonnance transpositionOrdonnance = new TranspositionOrdonnance();
            Map<String, Serializable> transpositionOrdonnanceMap = transOrdonnance.getSerializableMap();

            // récupération des propriétés d'une transposition de directive européenne
            transpositionOrdonnance.setCommentaire(
                (String) transpositionOrdonnanceMap.get(
                    DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_COMMENTAIRE_PROPERTY
                )
            );
            transpositionOrdonnance.setReferenceOrdonnance(
                (String) transpositionOrdonnanceMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_REF_PROPERTY)
            );
            transpositionOrdonnance.setTitreOrdonnance(
                (String) transpositionOrdonnanceMap.get(DossierSolonEpgConstants.DOSSIER_COMPLEXE_TYPE_TITRE_PROPERTY)
            );
            // ajout à la liste
            transpositionOrdonnanceList.add(transpositionOrdonnance);
        }
        return transpositionOrdonnanceList;
    }

    /**
     * Récupération du type de publication : extrait ou version intégrale
     *
     * @param dossier
     *            dossier
     * @return PublicationIntOuExtType
     */
    private static PublicationIntOuExtType getTypePublicationFromDossier(Dossier dossier) {
        String publicationIntegraleOuExtrait = dossier.getPublicationIntegraleOuExtrait();
        if (publicationIntegraleOuExtrait.equals(VocabularyConstants.TYPE_PUBLICATION_INTEGRAL)) {
            return PublicationIntOuExtType.IN_EXTENSO;
        } else if (publicationIntegraleOuExtrait.equals(VocabularyConstants.TYPE_PUBLICATION_EXTRAIT)) {
            return PublicationIntOuExtType.EXTRAIT;
        }
        return null;
    }

    /**
     * Methode utilisé pour récupérer le type d'acte
     *
     * @param typeActe
     *
     * @return ActeType
     */
    public static ActeType getWsActeTypeFromDossierTypeActe(String typeActe) {
        ActeType result = null;

        if (typeActe != null && !typeActe.isEmpty()) {
            if (typeActe.equals(TypeActeConstants.TYPE_ACTE_AMNISTIE)) {
                result = ActeType.AMNISTIE;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL)) {
                result = ActeType.ARRETE_MINISTERIEL;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL)) {
                result = ActeType.ARRETE_INTERMINISTERIEL;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_ARRETE_PM)) {
                result = ActeType.ARRETE_PM;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_ARRETE_PR)) {
                result = ActeType.ARRETE_PR;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_ARRETE_CE)) {
                result = ActeType.ARRETE_CE;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_AVENANT)) {
                result = ActeType.AVENANT;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_AVIS)) {
                result = ActeType.AVIS;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_CIRCULAIRE)) {
                return ActeType.CIRCULAIRE;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_CITATION)) {
                result = ActeType.CITATION;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_COMMUNIQUE)) {
                result = ActeType.COMMUNIQUE;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_CONVENTION)) {
                result = ActeType.CONVENTION;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DECISION)) {
                result = ActeType.DECISION;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37)) {
                result = ActeType.DECRET_CE_ART_37;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DECRET_CE)) {
                result = ActeType.DECRET_CE;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DECRET_PR_CE)) {
                result = ActeType.DECRET_PR_CE;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM)) {
                result = ActeType.DECRET_CE_CM;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DECRET_CM)) {
                result = ActeType.DECRET_CM;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DECRET_PR)) {
                result = ActeType.DECRET_PR;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE)) {
                result = ActeType.DECRET_SIMPLE;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DECRET_DE_PUBLICATION_DE_TRAITE_OU_ACCORD)) {
                result = ActeType.DECRET_PUBLICATION_TRAITE_OU_ACCORD;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DELIBERATION)) {
                result = ActeType.DELIBERATION;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DEMANDE_AVIS_CE)) {
                result = ActeType.DEMANDE_AVIS_CE;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DIVERS)) {
                result = ActeType.DIVERS;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_EXEQUATUR)) {
                result = ActeType.EXEQUATUR;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_INSTRUCTION)) {
                result = ActeType.INSTRUCTION;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_LISTE)) {
                result = ActeType.LISTE;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_LOI)) {
                result = ActeType.LOI;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION)) {
                result = ActeType.LOI_ART_53_CONSTITUTION;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE)) {
                result = ActeType.LOI_CONSTITUTIONNELLE;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE)) {
                result = ActeType.LOI_ORGANIQUE;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_NOTE)) {
                result = ActeType.NOTE;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_ORDONNANCE)) {
                result = ActeType.ORDONNANCE;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_RAPPORT)) {
                result = ActeType.RAPPORT;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_RECTIFICATIF)) {
                result = ActeType.RECTIFICATIF;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_TABLEAU)) {
                result = ActeType.TABLEAU;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND)) {
                result = ActeType.ARRETE_MINISTERIEL_INDIVIDUEL;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND)) {
                result = ActeType.ARRETE_INTERMINISTERIEL_INDIVIDUEL;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND)) {
                result = ActeType.ARRETE_PM_INDIVIDUEL;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND)) {
                result = ActeType.ARRETE_PR_INDIVIDUEL;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND)) {
                result = ActeType.DECRET_CE_INDIVIDUEL;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND)) {
                result = ActeType.DECRET_CE_CM_INDIVIDUEL;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DECRET_CM_IND)) {
                result = ActeType.DECRET_CM_INDIVIDUEL;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND)) {
                result = ActeType.DECRET_PR_INDIVIDUEL;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND)) {
                result = ActeType.DECRET_SIMPLE_INDIVIDUEL;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_INFORMATIONS_PARLEMENTAIRES)) {
                result = ActeType.INFORMATIONS_PARLEMENTAIRES;
            } else if (typeActe.equals(TypeActeConstants.TYPE_ACTE_ACCORD_COLLECTIF_PUBLIC)) {
                result = ActeType.ACCORD_COLLECTIF;
            }
        }

        return result;
    }

    public static String getDossierActeTypeFromWsTypeActe(ActeType acteType) {
        String result = null;
        if (acteType != null) {
            if (acteType.equals(ActeType.AMNISTIE)) {
                result = TypeActeConstants.TYPE_ACTE_AMNISTIE;
            } else if (acteType.equals(ActeType.ARRETE_MINISTERIEL)) {
                result = TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL;
            } else if (acteType.equals(ActeType.ARRETE_INTERMINISTERIEL)) {
                result = TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL;
            } else if (acteType.equals(ActeType.ARRETE_PM)) {
                result = TypeActeConstants.TYPE_ACTE_ARRETE_PM;
            } else if (acteType.equals(ActeType.ARRETE_PR)) {
                result = TypeActeConstants.TYPE_ACTE_ARRETE_PR;
            } else if (acteType.equals(ActeType.ARRETE_CE)) {
                result = TypeActeConstants.TYPE_ACTE_ARRETE_CE;
            } else if (acteType.equals(ActeType.AVENANT)) {
                result = TypeActeConstants.TYPE_ACTE_AVENANT;
            } else if (acteType.equals(ActeType.AVIS)) {
                result = TypeActeConstants.TYPE_ACTE_AVIS;
            } else if (acteType.equals(ActeType.CIRCULAIRE)) {
                result = TypeActeConstants.TYPE_ACTE_CIRCULAIRE;
            } else if (acteType.equals(ActeType.CITATION)) {
                result = TypeActeConstants.TYPE_ACTE_CITATION;
            } else if (acteType.equals(ActeType.COMMUNIQUE)) {
                result = TypeActeConstants.TYPE_ACTE_COMMUNIQUE;
            } else if (acteType.equals(ActeType.CONVENTION)) {
                result = TypeActeConstants.TYPE_ACTE_CONVENTION;
            } else if (acteType.equals(ActeType.DECISION)) {
                result = TypeActeConstants.TYPE_ACTE_DECISION;
            } else if (acteType.equals(ActeType.DECRET_CE_ART_37)) {
                result = TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37;
            } else if (acteType.equals(ActeType.DECRET_CE)) {
                result = TypeActeConstants.TYPE_ACTE_DECRET_CE;
            } else if (acteType.equals(ActeType.DECRET_CE_CM)) {
                result = TypeActeConstants.TYPE_ACTE_DECRET_CE_CM;
            } else if (acteType.equals(ActeType.DECRET_CM)) {
                result = TypeActeConstants.TYPE_ACTE_DECRET_CM;
            } else if (acteType.equals(ActeType.DECRET_PR)) {
                result = TypeActeConstants.TYPE_ACTE_DECRET_PR;
            } else if (acteType.equals(ActeType.DECRET_PR_CE)) {
                result = TypeActeConstants.TYPE_ACTE_DECRET_PR_CE;
            } else if (acteType.equals(ActeType.DECRET_SIMPLE)) {
                result = TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE;
            } else if (acteType.equals(ActeType.DECRET_PUBLICATION_TRAITE_OU_ACCORD)) {
                result = TypeActeConstants.TYPE_ACTE_DECRET_DE_PUBLICATION_DE_TRAITE_OU_ACCORD;
            } else if (acteType.equals(ActeType.DELIBERATION)) {
                result = TypeActeConstants.TYPE_ACTE_DELIBERATION;
            } else if (acteType.equals(ActeType.DEMANDE_AVIS_CE)) {
                result = TypeActeConstants.TYPE_ACTE_DEMANDE_AVIS_CE;
            } else if (acteType.equals(ActeType.DIVERS)) {
                result = TypeActeConstants.TYPE_ACTE_DIVERS;
            } else if (acteType.equals(ActeType.EXEQUATUR)) {
                result = TypeActeConstants.TYPE_ACTE_EXEQUATUR;
            } else if (acteType.equals(ActeType.INSTRUCTION)) {
                result = TypeActeConstants.TYPE_ACTE_INSTRUCTION;
            } else if (acteType.equals(ActeType.LISTE)) {
                result = TypeActeConstants.TYPE_ACTE_LISTE;
            } else if (acteType.equals(ActeType.LOI)) {
                result = TypeActeConstants.TYPE_ACTE_LOI;
            } else if (acteType.equals(ActeType.LOI_ART_53_CONSTITUTION)) {
                result = TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION;
            } else if (acteType.equals(ActeType.LOI_CONSTITUTIONNELLE)) {
                result = TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE;
            } else if (acteType.equals(ActeType.LOI_ORGANIQUE)) {
                result = TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE;
            } else if (acteType.equals(ActeType.NOTE)) {
                result = TypeActeConstants.TYPE_ACTE_NOTE;
            } else if (acteType.equals(ActeType.ORDONNANCE)) {
                result = TypeActeConstants.TYPE_ACTE_ORDONNANCE;
            } else if (acteType.equals(ActeType.RAPPORT)) {
                result = TypeActeConstants.TYPE_ACTE_RAPPORT;
            } else if (acteType.equals(ActeType.RECTIFICATIF)) {
                result = TypeActeConstants.TYPE_ACTE_RECTIFICATIF;
            } else if (acteType.equals(ActeType.TABLEAU)) {
                result = TypeActeConstants.TYPE_ACTE_TABLEAU;
            } else if (acteType.equals(ActeType.ARRETE_MINISTERIEL_INDIVIDUEL)) {
                result = TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND;
            } else if (acteType.equals(ActeType.ARRETE_INTERMINISTERIEL_INDIVIDUEL)) {
                result = TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND;
            } else if (acteType.equals(ActeType.ARRETE_PM_INDIVIDUEL)) {
                result = TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND;
            } else if (acteType.equals(ActeType.ARRETE_PR_INDIVIDUEL)) {
                result = TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND;
            } else if (acteType.equals(ActeType.DECRET_CE_INDIVIDUEL)) {
                result = TypeActeConstants.TYPE_ACTE_DECRET_CE_IND;
            } else if (acteType.equals(ActeType.DECRET_CE_CM_INDIVIDUEL)) {
                result = TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND;
            } else if (acteType.equals(ActeType.DECRET_CM_INDIVIDUEL)) {
                result = TypeActeConstants.TYPE_ACTE_DECRET_CM_IND;
            } else if (acteType.equals(ActeType.DECRET_PR_INDIVIDUEL)) {
                result = TypeActeConstants.TYPE_ACTE_DECRET_PR_IND;
            } else if (acteType.equals(ActeType.DECRET_SIMPLE_INDIVIDUEL)) {
                result = TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND;
            } else if (acteType.equals(ActeType.INFORMATIONS_PARLEMENTAIRES)) {
                result = TypeActeConstants.TYPE_ACTE_INFORMATIONS_PARLEMENTAIRES;
            } else if (acteType.equals(ActeType.ACCORD_COLLECTIF)) {
                result = TypeActeConstants.TYPE_ACTE_ACCORD_COLLECTIF_PUBLIC;
            }
        }
        return result;
    }

    /**
     * Remplit les champs d'un acte webservice à partir d'un Dossier. note : si un champ non obligatoire est vide on ne
     * remplit pas ce champ pour former un xml valide, cad eviter les balises sans contenu ex :<a></a>
     *
     * @param acte
     * @param acteType
     * @param dossier
     */
    public static void fillActeFromDossier(
        CoreSession session,
        Acte acte,
        ActeType acteType,
        Dossier dossier,
        Boolean affichagePublicationEtEpreuvage
    ) {
        // affichage des ministères responsables
        final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
        final STUsAndDirectionService usService = STServiceLocator.getSTUsAndDirectionService();

        // on récupère le statut de l'acte
        fillStatutActeFromDossier(acte, dossier);
        // on récupère le type d'acte
        acte.setTypeActe(acteType);

        acte.setNor(dossier.getNumeroNor());

        if (!StringUtils.isEmpty(dossier.getTitreActe())) {
            acte.setTitreActe(dossier.getTitreActe());
        }

        // on renvoie l'intitulé court du ministère responsable
        OrganigrammeNode node = ministeresService.getEntiteNode(dossier.getMinistereResp());
        if (node != null) {
            String nodeLabel = node.getLabel();
            acte.setEntiteResponsable(nodeLabel);
            // on renvoie l'intitulé court de la direction responsable
            node = usService.getUniteStructurelleNode(dossier.getDirectionResp());
            nodeLabel = node.getLabel();
            acte.setDirectionResponsable(nodeLabel);
            // on renvoie l'intitulé court du ministère attache
            node = ministeresService.getEntiteNode(dossier.getMinistereAttache());
            nodeLabel = node.getLabel();
            acte.setMinistereRattachement(nodeLabel);
            // on renvoie l'intitulé court de la direction attache
            node = usService.getUniteStructurelleNode(dossier.getDirectionAttache());
            nodeLabel = node.getLabel();
            acte.setDirectionRattachement(nodeLabel);
        }

        // récupération créateur du document : on récupère son nom et son prénom
        String creator = DublincoreSchemaUtils.getCreator(dossier.getDocument());
        acte.setCreateur(STServiceLocator.getSTUserService().getUserFullName(creator));

        // récupération des informations du responsable
        Responsable responsable = new Responsable();

        // Le nom du responsable doit être présent, sinon le xml n'est pas valide
        if (dossier.getNomRespDossier() == null) {
            responsable.setNomReponsable(DEFAULT_DATA);
        } else {
            responsable.setNomReponsable(dossier.getNomRespDossier());
        }
        if (!StringUtils.isEmpty(dossier.getPrenomRespDossier())) {
            responsable.setPrenomResponsable(dossier.getPrenomRespDossier());
        }

        // La qualité du responsable doit être présent, sinon le xml n'est pas valide
        if (dossier.getQualiteRespDossier() == null) {
            responsable.setQualiteResponsable(DEFAULT_DATA);
        } else {
            responsable.setQualiteResponsable(dossier.getQualiteRespDossier());
        }

        // Le téléphone du responsable doit être présent, sinon le xml n'est pas valide
        if (dossier.getTelephoneRespDossier() == null) {
            responsable.setTelephoneResponsable(DEFAULT_DATA);
        } else {
            responsable.setTelephoneResponsable(dossier.getTelephoneRespDossier());
        }

        if (!StringUtils.isEmpty(dossier.getMailRespDossier())) {
            responsable.setMailResponsable(dossier.getMailRespDossier());
        }
        acte.setResponsable(responsable);
        // catégorie acte
        String categorieActe = dossier.getCategorieActe();
        if (!StringUtils.isEmpty(categorieActe)) {
            if (categorieActe.equals(VocabularyConstants.NATURE_REGLEMENTAIRE)) {
                acte.setCategorieActe(PEActeCategorie.REGLEMENTAIRE);
            } else if (categorieActe.equals(VocabularyConstants.NATURE_NON_REGLEMENTAIRE)) {
                acte.setCategorieActe(PEActeCategorie.NON_REGLEMENTAIRE);
            } else if (categorieActe.equals(VocabularyConstants.NATURE_CONVENTION_COLLECTIVE)) {
                acte.setCategorieActe(PEActeCategorie.CONVENTION_COLLECTIVE);
            }
        }

        acte.setDatePublicationSouhaitee(DateUtil.calendarToXMLGregorianCalendar(dossier.getDatePublication()));

        acte.getChargeDeMission().addAll(dossier.getNomCompletChargesMissions());
        acte.getConseillerPm().addAll(dossier.getNomCompletConseillersPms());

        // date signature
        acte.setDateSignature(DateUtil.calendarToXMLGregorianCalendar(dossier.getDateSignature()));
        // fournitureEpreuve
        acte.setPourFournitureEpreuve(DateUtil.calendarToXMLGregorianCalendar(dossier.getDatePourFournitureEpreuve()));
        // vecteur publication
        if (!dossier.getVecteurPublication().isEmpty()) {
            List<String> vecteurs = new ArrayList<>();
            for (String idVect : dossier.getVecteurPublication()) {
                final IdRef docRef = new IdRef(idVect);
                try {
                    // Si ça n'est pas un vecteur, c'est le libellé d'un bulletin
                    if (session.exists(docRef)) {
                        DocumentModel doc = session.getDocument(docRef);
                        if (doc.hasSchema(SolonEpgVecteurPublicationConstants.VECTEUR_PUBLICATION_SCHEMA)) {
                            VecteurPublication vecteur = doc.getAdapter(VecteurPublication.class);
                            vecteurs.add(vecteur.getIntitule());
                        }
                    } else {
                        vecteurs.add(idVect);
                    }
                } catch (NuxeoException ce) {
                    LOGGER.error(session, STLogEnumImpl.FAIL_GET_DOCUMENT_FONC, idVect, ce);
                }
            }
            acte.getVecteurDePublication().addAll(vecteurs);
        }
        // publication conjointe
        acte.getPublicationConjointe().addAll(dossier.getPublicationsConjointes());

        // mode_parution
        RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
        fillModeParutionfromDossier(session, acte, retourDila.getModeParution());

        // delai publication
        fillDelaiPublicationfromDossier(acte, dossier.getDelaiPublication());

        // delai publication
        acte.setPublicationDatePrecisee(DateUtil.calendarToXMLGregorianCalendar(dossier.getDatePreciseePublication()));

        // Parution JORF (pas de parution Jorf en cas de publication ou d'epreuvage
        if (!affichagePublicationEtEpreuvage) {
            ParutionJORF parutionJORF = new ParutionJORF();
            parutionJORF.setDateJorf(DateUtil.calendarToXMLGregorianCalendar(retourDila.getDateParutionJorf()));
            parutionJORF.setNumeroTexteJo(retourDila.getNumeroTexteParutionJorf());
            Long pageParutionJorf = retourDila.getPageParutionJorf();
            if (pageParutionJorf != null) {
                parutionJORF.setPageJo(pageParutionJorf.intValue());
            }
            if (dossier.getStatut() != null && dossier.getStatut().equals(VocabularyConstants.STATUT_PUBLIE)) {
                acte.setParutionJORF(parutionJORF);
            }
            List<fr.dila.solonepg.api.cases.typescomplexe.ParutionBo> parutionBoList = retourDila.getParutionBo();
            for (fr.dila.solonepg.api.cases.typescomplexe.ParutionBo parutionBo : parutionBoList) {
                // on parcourt les objets parutions BO
                ParutionBo parutionBoWs = new ParutionBo();
                parutionBoWs.setDateBo(DateUtil.calendarToXMLGregorianCalendar(parutionBo.getDateParutionBo()));
                parutionBoWs.setNumeroTexteBo(parutionBo.getNumeroTexteParutionBo());
                Long pageParutionBo = parutionBo.getPageParutionBo();
                if (pageParutionBo != null && pageParutionBo != 0) {
                    parutionBoWs.setPageBo(pageParutionBo.intValue());
                }
                acte.getParutionBo().add(parutionBoWs);
            }
        }
        // commentaire sgg : tous les utilisateurs peuvent voir le commentaire
        acte.setCommentaireSggDila(dossier.getZoneComSggDila());

        // indexation
        Indexation indexation = new Indexation();
        indexation.getRubriques().addAll(dossier.getIndexationRubrique());
        indexation.getMotsCles().addAll(indexation.getMotsCles());
        indexation.getChampLibre().addAll(dossier.getIndexationChampLibre());
        if (indexation.getRubriques().isEmpty()) {
            indexation.getRubriques().add(DEFAULT_DATA);
        }
        acte.setIndexation(indexation);
    }

    /**
     * @param acte
     * @param delaiPublication
     */
    private static void fillDelaiPublicationfromDossier(Acte acte, String delaiPublication) {
        if (delaiPublication != null && !delaiPublication.isEmpty()) {
            if (delaiPublication.equals(VocabularyConstants.DELAI_PUBLICATION_A_DATE_PRECISEE)) {
                acte.setDelaiPublication(PEDelaiPublication.A_DATE_PRECISEE);
            } else if (delaiPublication.equals(VocabularyConstants.DELAI_PUBLICATION_AUCUN)) {
                acte.setDelaiPublication(PEDelaiPublication.AUCUN);
            } else if (delaiPublication.equals(VocabularyConstants.DELAI_PUBLICATION_DE_RIGUEUR)) {
                acte.setDelaiPublication(PEDelaiPublication.DE_RIGUEUR);
            } else if (delaiPublication.equals(VocabularyConstants.DELAI_PUBLICATION_SANS_TARDER)) {
                acte.setDelaiPublication(PEDelaiPublication.SANS_TARDER);
            } else if (delaiPublication.equals(VocabularyConstants.DELAI_PUBLICATION_SOUS_EMBARGO)) {
                acte.setDelaiPublication(PEDelaiPublication.SOUS_EMBARGO);
            } else if (delaiPublication.equals(VocabularyConstants.DELAI_PUBLICATION_URGENT)) {
                acte.setDelaiPublication(PEDelaiPublication.URGENT);
            }
        }
    }

    private static void fillModeParutionfromDossier(CoreSession session, Acte acte, String modeParution) {
        if (modeParution != null && !modeParution.isEmpty()) {
            final IdRef docRef = new IdRef(modeParution);
            try {
                if (session.exists(docRef)) {
                    ModeParution mode = session.getDocument(docRef).getAdapter(ModeParution.class);
                    acte.setModeParution(mode.getMode().toUpperCase(Locale.FRENCH));
                }
            } catch (NuxeoException exc) {
                LOGGER.error(session, EpgLogEnumImpl.FAIL_GET_MODE_PARUTION_TEC, modeParution, exc);
            }
        }
    }

    public static void fillStatutActeFromDossier(Acte acte, Dossier dossier) {
        String statut = dossier.getStatut();
        if (statut != null && !statut.isEmpty()) {
            if (statut.equals(VocabularyConstants.STATUT_INITIE)) {
                acte.setStatutActe(StatutActe.INITIE);
            } else if (statut.equals(VocabularyConstants.STATUT_LANCE)) {
                // note : le statut lancé correspond fonctionnellement à l'état initié (cf 3.2.2 cycle de vie d'un
                // dossier) //Remplacé par LANCE suite au Mantis M156328
                acte.setStatutActe(StatutActe.LANCE);
            } else if (statut.equals(VocabularyConstants.STATUT_ABANDONNE)) {
                acte.setStatutActe(StatutActe.ABANDONNE);
            } else if (statut.equals(VocabularyConstants.STATUT_PUBLIE)) {
                acte.setStatutActe(StatutActe.PUBLIE);
            } else if (statut.equals(VocabularyConstants.STATUT_NOR_ATTRIBUE)) {
                acte.setStatutActe(StatutActe.NOR_ATTRIBUE);
            }
        }

        // gestion de l'archivage et de l'abandon
        String statutArchivage = dossier.getStatutArchivage();
        if (statutArchivage != null) {
            if (
                !VocabularyConstants.STATUT_ARCHIVAGE_AUCUN.equals(statutArchivage) &&
                !VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE.equals(statutArchivage)
            ) {
                // si le dossier est dans la base d'archiavge intermédiaire, on le marque comme archive
                acte.setStatutActe(StatutActe.ARCHIVE);
            }
        }
    }

    /**
     * Récupère une liste de fichier et enregistre ces fichiers dans le répertoire du fond de dossier ayant le nom
     * choisi.
     *
     * @param dossierDoc
     * @param fondDeDossierService
     * @param fichierList
     */
    public static void createFilesInFdd(
        CoreSession session,
        DocumentModel dossierDoc,
        FondDeDossierService fondDeDossierService,
        List<Fichier> fichierList,
        String folderName
    ) {
        Collections.sort(fichierList, new FichierComparator());
        for (Fichier fichier : fichierList) {
            fondDeDossierService.createFondDeDossierFile(
                session,
                fichier.getNom(),
                new ByteArrayBlob(fichier.getContenu()),
                folderName,
                dossierDoc
            );
        }
    }

    /**
     * Récupère les 2 fichier d'epreuve et les enregistrer dans le FDD
     *
     * @param dossierDoc
     * @param fondDeDossierService
     * @param fichierList
     */
    public static void createEpreuveFilesInFdd(
        CoreSession session,
        DocumentModel dossierDoc,
        List<PEFichier> fichiers
    ) {
        FondDeDossierService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
        List<DocumentModel> lstDocsEpreuves = fondDeDossierService.getEpreuvesFiles(
            session,
            dossierDoc.getAdapter(Dossier.class)
        );

        // Si des 2 documents sont déjà présent dans le répertoire épreuve, on les met a jour avec les nouveaux fichiers
        if (CollectionUtils.isNotEmpty(lstDocsEpreuves) && lstDocsEpreuves.size() == 2) {
            for (int i = 0; i < 2; i++) {
                String filename = fichiers.get(i).getNom().replaceAll("'", "_");
                final ByteArrayBlob blobArray = new ByteArrayBlob(fichiers.get(i).getContent());
                blobArray.setFilename(filename);
                fondDeDossierService.updateFile(
                    session,
                    lstDocsEpreuves.get(i),
                    blobArray,
                    session.getPrincipal(),
                    dossierDoc
                );
            }
        } else {
            // sinon on vide le répertoire et ont crée les nouveau fichiers
            ServiceUtil.getRequiredService(TrashService.class).trashDocuments(lstDocsEpreuves);
            fichiers.forEach(
                file -> {
                    String filename = file.getNom().replaceAll("'", "_");
                    fondDeDossierService.createFondDeDossierFile(
                        session,
                        filename,
                        file.getContent(),
                        SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_EPREUVES,
                        dossierDoc
                    );
                }
            );
        }
    }

    /**
     * Récupère un fichier et enregistre ce fichier dans le répertoire du fond de dossier ayant le nom choisi.
     *
     * @param dossierDoc
     * @param fondDeDossierService
     * @param fichierList
     */
    public static void createOrUpdateFileListInFdd(
        CoreSession session,
        DocumentModel dossierDoc,
        List<Fichier> fichierList,
        String folderName
    ) {
        FondDeDossierService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
        List<DocumentModel> fddDocList = fondDeDossierService.getAvisCEFiles(
            session,
            dossierDoc.getAdapter(Dossier.class)
        );
        Map<Fichier, DocumentModel> fichierUpdateMap = new HashMap<>();
        List<Fichier> fichierCreate = new ArrayList<>();

        // S'il n'y à pas de document dans le répertoire on crée une version pour chaque fichier
        if (fddDocList == null || fddDocList.isEmpty()) {
            createFilesInFdd(session, dossierDoc, fondDeDossierService, fichierList, folderName);
        } else { // Sinon on regarde si le document existe déjà dans le répertoire
            for (Fichier fichier : fichierList) {
                for (DocumentModel fddDoc : fddDocList) {
                    // Si le fichier existe, on ajoute le documentModel et le fichier dans la map pour update
                    File fichierFdd = fddDoc.getAdapter(File.class);
                    if (fichier.getNom().equals(fichierFdd.getFilename()) && !fichierUpdateMap.containsKey(fichier)) {
                        fichierUpdateMap.put(fichier, fddDoc);
                    }
                }
                // Si le fichier n'est pas présent dans la Map, on l'ajoute dans la List pour le créer
                if (!fichierUpdateMap.containsKey(fichier)) {
                    fichierCreate.add(fichier);
                }
            }
            // Si le document est nouveau il est ajouté à une liste puis créé
            createFilesInFdd(session, dossierDoc, fondDeDossierService, fichierCreate, folderName);
            // Si le document existe déjà sa version est incrémenté
            for (Map.Entry<Fichier, DocumentModel> entry : fichierUpdateMap.entrySet()) {
                Blob blob = new ByteArrayBlob(entry.getKey().getContenu());
                blob.setFilename(entry.getKey().getNom());
                fondDeDossierService.updateFile(session, entry.getValue(), blob, session.getPrincipal(), dossierDoc);
            }
        }
    }

    /**
     * Récupère le fichier d'epreuve et l'enregistre dans le répertoire du parapheur
     *
     * @param dossierDoc
     * @param parapheurService
     * @param fichierList
     */
    public static void createEpreuveFileInParapheur(
        CoreSession session,
        DocumentModel dossierDoc,
        PEFichier fichierEpreuve,
        Optional<PEFichier> fichierCompare
    ) {
        ParapheurService parapheurService = SolonEpgServiceLocator.getParapheurService();
        List<DocumentModel> lstDocsEpreuves = parapheurService.getEpreuvesFiles(
            session,
            dossierDoc.getAdapter(Dossier.class)
        );
        if (!lstDocsEpreuves.isEmpty()) {
            ServiceUtil.getRequiredService(TrashService.class).trashDocuments(lstDocsEpreuves);
        }

        createEpreuveFile(session, dossierDoc, fichierEpreuve, parapheurService);

        if (fichierCompare.isPresent()) {
            createEpreuveFile(session, dossierDoc, fichierCompare.get(), parapheurService);
        }
    }

    private static void createEpreuveFile(
        CoreSession session,
        DocumentModel dossierDoc,
        PEFichier fichier,
        ParapheurService parapheurService
    ) {
        String filename = FileUtils.sanitizePathTraversal(fichier.getNom());
        parapheurService.createParapheurFile(
            session,
            filename,
            fichier.getContent(),
            SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EPREUVES_NAME,
            dossierDoc
        );
    }

    public static PEDemandeType getTypeDemande(String typePublication) {
        if (!StringUtils.isEmpty(typePublication)) {
            if (VocabularyConstants.ROUTING_TASK_TYPE_FOURNITURE_EPREUVE.equals(typePublication)) {
                return PEDemandeType.EPREUVAGE;
            } else if (VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_BO.equals(typePublication)) {
                return PEDemandeType.PUBLICATION_BO;
            } else if (VocabularyConstants.ROUTING_TASK_TYPE_PUBLICATION_DILA_JO.equals(typePublication)) {
                return PEDemandeType.PUBLICATION_JO;
            }
        }
        return null;
    }

    public static void remapDossier(CoreSession session, Dossier dossier, DossierEpg dossierEpg, ActeType acteType) {
        Acte acte = null;
        String typeActeId = dossier.getTypeActe();
        if (ActeType.AVIS.equals(acteType)) {
            Avis avis = dossierEpg.getAvis();
            acte = avis;

            String avisBL = avis.getBaseLegale();
            if (avisBL != null) {
                dossier.setBaseLegaleActe(avisBL);
            }
            PublicationIntOuExtType avisPub = avis.getPublicationIntegraleOuExtrait();
            if (avisPub != null) {
                if (PublicationIntOuExtType.IN_EXTENSO.equals(avisPub)) {
                    dossier.setPublicationIntegraleOuExtrait(VocabularyConstants.TYPE_PUBLICATION_INTEGRAL);
                } else if (PublicationIntOuExtType.EXTRAIT.equals(avisPub)) {
                    dossier.setPublicationIntegraleOuExtrait(VocabularyConstants.TYPE_PUBLICATION_EXTRAIT);
                }
            }
        } else if (ActeType.DECRET_PR_INDIVIDUEL.equals(acteType)) {
            DecretPRIndividuel decret = dossierEpg.getDecretPrInd();
            acte = decret;
            String avisBL = decret.getBaseLegale();
            if (avisBL != null) {
                dossier.setBaseLegaleActe(avisBL);
            }
            PublicationIntOuExtType avisPub = decret.getPublicationIntegraleOuExtrait();
            if (avisPub != null) {
                if (PublicationIntOuExtType.IN_EXTENSO.equals(avisPub)) {
                    dossier.setPublicationIntegraleOuExtrait(VocabularyConstants.TYPE_PUBLICATION_INTEGRAL);
                } else if (PublicationIntOuExtType.EXTRAIT.equals(avisPub)) {
                    dossier.setPublicationIntegraleOuExtrait(VocabularyConstants.TYPE_PUBLICATION_EXTRAIT);
                }
            }
        } else if (ActeType.INFORMATIONS_PARLEMENTAIRES.equals(acteType)) {
            InformationsParlementaires informationsParlementaires = dossierEpg.getInformationsParlementaires();
            acte = informationsParlementaires;
            String ipEmetteur = informationsParlementaires.getEmetteur();
            if (ipEmetteur != null) {
                dossier.setEmetteur(ipEmetteur);
            }
            String ipRubrique = informationsParlementaires.getRubrique();
            if (ipRubrique != null) {
                dossier.setRubrique(ipRubrique);
            }
            String ipCommentaire = informationsParlementaires.getCommentaire();
            if (ipCommentaire != null) {
                dossier.setCommentaire(ipCommentaire);
            }
            String ipIdEvenement = informationsParlementaires.getIdEvenement();
            if (ipIdEvenement != null) {
                dossier.setIdEvenement(ipIdEvenement);
            }
            PublicationIntOuExtType infoParlPub = informationsParlementaires.getPublicationIntegraleOuExtrait();
            if (infoParlPub != null) {
                if (PublicationIntOuExtType.IN_EXTENSO.equals(infoParlPub)) {
                    dossier.setPublicationIntegraleOuExtrait(VocabularyConstants.TYPE_PUBLICATION_INTEGRAL);
                } else if (PublicationIntOuExtType.EXTRAIT.equals(infoParlPub)) {
                    dossier.setPublicationIntegraleOuExtrait(VocabularyConstants.TYPE_PUBLICATION_EXTRAIT);
                }
            }
            informationsParlementaires.setDelaiPublication(PEDelaiPublication.A_DATE_PRECISEE);
            XMLGregorianCalendar datePubSouhaitee = informationsParlementaires.getDatePublicationSouhaitee();
            if (datePubSouhaitee != null) {
                informationsParlementaires.setPublicationDatePrecisee(datePubSouhaitee);
            }
        } else {
            throw new EPGException("Type d'acte non géré.");
        }

        PEActeCategorie acteCat = acte.getCategorieActe();
        if (acteCat != null) {
            if (PEActeCategorie.REGLEMENTAIRE.equals(acteCat)) {
                dossier.setCategorieActe(VocabularyConstants.NATURE_REGLEMENTAIRE);
            } else if (PEActeCategorie.NON_REGLEMENTAIRE.equals(acteCat)) {
                dossier.setCategorieActe(VocabularyConstants.NATURE_NON_REGLEMENTAIRE);
            } else if (PEActeCategorie.CONVENTION_COLLECTIVE.equals(acteCat)) {
                // vérification pour savoir si pour ce type d'acte on peut avoir cette catégorie
                if (
                    typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND) ||
                    typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL) ||
                    typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL) ||
                    typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND) ||
                    typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_PM) ||
                    typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND) ||
                    typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_PR) ||
                    typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND) ||
                    typeActeId.equals(TypeActeConstants.TYPE_ACTE_ARRETE_CE) ||
                    typeActeId.equals(TypeActeConstants.TYPE_ACTE_AVIS)
                ) {
                    dossier.setCategorieActe(VocabularyConstants.NATURE_CONVENTION_COLLECTIVE);
                } else {
                    // Là on balance une exception car on n'a pas le droit
                    throw new EPGException(
                        "La catégorie d'acte Convention collective n'est pas valide pour ce type d'acte."
                    );
                }
            }
        }

        List<String> acteCM = acte.getChargeDeMission();
        if (acteCM != null && !acteCM.isEmpty()) {
            dossier.setNomCompletChargesMissions(acteCM);
        }

        String acteCommDila = acte.getCommentaireSggDila();
        if (acteCommDila != null) {
            dossier.setZoneComSggDila(acteCommDila);
        }

        List<String> acteCPM = acte.getConseillerPm();
        if (acteCPM != null && !acteCPM.isEmpty()) {
            dossier.setNomCompletConseillersPms(acteCPM);
        }

        XMLGregorianCalendar acteDatePub = acte.getDatePublicationSouhaitee();
        if (acteDatePub != null) {
            dossier.setDatePublication(DateUtil.xmlGregorianCalendarToCalendar(acteDatePub));
        }

        XMLGregorianCalendar acteDateSignature = acte.getDateSignature();
        if (acteDateSignature != null) {
            dossier.setDateSignature(DateUtil.xmlGregorianCalendarToCalendar(acteDateSignature));
        }

        PEDelaiPublication acteDelaiPub = acte.getDelaiPublication();
        if (acteDelaiPub != null) {
            if (PEDelaiPublication.A_DATE_PRECISEE.equals(acteDelaiPub)) {
                dossier.setDelaiPublication(VocabularyConstants.DELAI_PUBLICATION_A_DATE_PRECISEE);
            } else if (PEDelaiPublication.AUCUN.equals(acteDelaiPub)) {
                dossier.setDelaiPublication(VocabularyConstants.DELAI_PUBLICATION_AUCUN);
            } else if (PEDelaiPublication.DE_RIGUEUR.equals(acteDelaiPub)) {
                dossier.setDelaiPublication(VocabularyConstants.DELAI_PUBLICATION_DE_RIGUEUR);
            } else if (PEDelaiPublication.SANS_TARDER.equals(acteDelaiPub)) {
                dossier.setDelaiPublication(VocabularyConstants.DELAI_PUBLICATION_SANS_TARDER);
            } else if (PEDelaiPublication.SOUS_EMBARGO.equals(acteDelaiPub)) {
                dossier.setDelaiPublication(VocabularyConstants.DELAI_PUBLICATION_SOUS_EMBARGO);
            } else if (PEDelaiPublication.URGENT.equals(acteDelaiPub)) {
                dossier.setDelaiPublication(VocabularyConstants.DELAI_PUBLICATION_URGENT);
            }
        }

        Indexation acteIndex = acte.getIndexation();
        if (acteIndex != null) {
            final IndexationEpgService indexationService = SolonEpgServiceLocator.getIndexationEpgService();
            List<String> acteIndexChampLibre = acteIndex.getChampLibre();
            if (acteIndexChampLibre != null && !acteIndexChampLibre.isEmpty()) {
                dossier.setIndexationChampLibre(acteIndexChampLibre);
            }

            List<String> acteIndexMotsCles = acteIndex.getMotsCles();
            if (acteIndexMotsCles != null && !acteIndexMotsCles.isEmpty()) {
                List<String> motClesList = indexationService.findAllIndexationMotCleForDossier(
                    session,
                    dossier.getDocument(),
                    null
                );
                if (motClesList.containsAll(acteIndexMotsCles)) {
                    dossier.setIndexationMotsCles(acteIndexMotsCles);
                } else {
                    throw new EPGException(
                        "Les mots clés renseignés n'existent pas dans l'application pour ce ministère"
                    );
                }
            }

            List<String> acteIndexRub = acteIndex.getRubriques();
            if (acteIndexRub != null && !acteIndexRub.isEmpty()) {
                List<String> rubriquesList = indexationService.findAllIndexationRubrique(session, null);
                if (rubriquesList.containsAll(acteIndexRub)) {
                    dossier.setIndexationRubrique(acteIndexRub);
                } else {
                    throw new EPGException("Les rubriques renseignées n'existent pas dans l'application");
                }
            }
        }

        XMLGregorianCalendar actePourFourEpr = acte.getPourFournitureEpreuve();
        if (actePourFourEpr != null) {
            dossier.setDatePourFournitureEpreuve(DateUtil.xmlGregorianCalendarToCalendar(actePourFourEpr));
        }

        List<String> actePubConjointe = acte.getPublicationConjointe();
        if (actePubConjointe != null && !actePubConjointe.isEmpty()) {
            dossier.setPublicationsConjointes(actePubConjointe);
        }

        XMLGregorianCalendar actePubDatePrecisee = acte.getPublicationDatePrecisee();
        if (actePubDatePrecisee != null) {
            dossier.setDatePreciseePublication(DateUtil.xmlGregorianCalendarToCalendar(actePubDatePrecisee));
        }

        Responsable acteResp = acte.getResponsable();
        if (acteResp != null) {
            String acteMailResp = acteResp.getMailResponsable();
            if (acteMailResp != null) {
                dossier.setMailRespDossier(acteMailResp);
            }

            String acteNomResp = acteResp.getNomReponsable();
            if (acteNomResp != null) {
                dossier.setNomRespDossier(acteNomResp);
            }

            String actePrenomResp = acteResp.getPrenomResponsable();
            if (actePrenomResp != null) {
                dossier.setPrenomRespDossier(actePrenomResp);
            }

            String acteQualiteResp = acteResp.getQualiteResponsable();
            if (acteQualiteResp != null) {
                dossier.setQualiteRespDossier(acteQualiteResp);
            }

            String acteTelResp = acteResp.getTelephoneResponsable();
            if (acteTelResp != null) {
                dossier.setTelephoneRespDossier(acteTelResp);
            }
        }

        String acteTitre = acte.getTitreActe();
        if (acteTitre != null) {
            dossier.setTitreActe(acteTitre);
        }

        // On fait la conversion pour les vecteurs : si on parvient à retrouver un vecteur grâce au libellé on rajoute
        // l'ID
        // sinon on rajoute le libellé (et ça devient un bulletin officiel)
        List<String> acteVectPub = acte.getVecteurDePublication();
        if (acteVectPub != null && !acteVectPub.isEmpty()) {
            List<String> lstVectEtBulletinConvertis = new ArrayList<>();
            BulletinOfficielService bulletinService = SolonEpgServiceLocator.getBulletinOfficielService();

            for (String element : acteVectPub) {
                String idVecteur = bulletinService.getIdForLibelle(session, element);
                if (StringUtils.isNotBlank(idVecteur)) {
                    lstVectEtBulletinConvertis.add(idVecteur);
                } else {
                    lstVectEtBulletinConvertis.add(element);
                }
            }
            dossier.setVecteurPublication(lstVectEtBulletinConvertis);
        }
        dossier.save(session);

        // ******************************Traitement des données pour le retourDila
        RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
        String acteModeParution = acte.getModeParution();
        if (acteModeParution != null) {
            String query = "SELECT * FROM ModeParution WHERE mod:mode ILIKE '" + acteModeParution + "'";
            try {
                DocumentModelList results = session.query(query);
                if (results.isEmpty() || results.size() > 1) {} else {
                    DocumentModel modeParutionDoc = results.get(0);
                    retourDila.setModeParution(modeParutionDoc.getId());
                }
            } catch (NuxeoException exc) {}
        }

        List<ParutionBo> acteParuBO = acte.getParutionBo();
        if (acteParuBO != null && !acteParuBO.isEmpty()) {
            List<fr.dila.solonepg.api.cases.typescomplexe.ParutionBo> parutionsBoDossier = new ArrayList<>();
            for (ParutionBo parution : acteParuBO) {
                fr.dila.solonepg.api.cases.typescomplexe.ParutionBo parutionDossier = new ParutionBoImpl();
                XMLGregorianCalendar acteDateBo = parution.getDateBo();
                if (acteDateBo != null) {
                    parutionDossier.setDateParutionBo(DateUtil.xmlGregorianCalendarToCalendar(acteDateBo));
                }

                String acteNumeroTexteBo = parution.getNumeroTexteBo();
                if (acteNumeroTexteBo != null) {
                    parutionDossier.setNumeroTexteParutionBo(acteNumeroTexteBo);
                }

                Long actePageBo = Long.valueOf(parution.getPageBo());
                if (actePageBo != null) {
                    parutionDossier.setPageParutionBo(actePageBo);
                }

                parutionsBoDossier.add(parutionDossier);
            }
            retourDila.setParutionBo(parutionsBoDossier);
        }

        ParutionJORF acteParuJORF = acte.getParutionJORF();
        if (acteParuJORF != null) {
            XMLGregorianCalendar dateJORF = acteParuJORF.getDateJorf();
            if (dateJORF != null) {
                retourDila.setDateParutionJorf(DateUtil.xmlGregorianCalendarToCalendar(dateJORF));
            }

            String acteNumTexteJO = acteParuJORF.getNumeroTexteJo();
            if (acteNumTexteJO != null) {
                retourDila.setNumeroTexteParutionJorf(acteNumTexteJO);
            }

            Long pageJoJorf = Long.valueOf(acteParuJORF.getPageJo());
            if (pageJoJorf != null) {
                retourDila.setPageParutionJorf(pageJoJorf);
            }

            String titreOfficielJorf = acteParuJORF.getTitreOfficiel();
            if (titreOfficielJorf != null) {
                retourDila.setTitreOfficiel(titreOfficielJorf);
            }
        }
        retourDila.save(session);
    }

    private WsUtil() {
        // Private default constructor
    }
}
