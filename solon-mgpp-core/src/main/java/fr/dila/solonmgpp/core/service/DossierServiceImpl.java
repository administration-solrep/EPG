package fr.dila.solonmgpp.core.service;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.DonneesSignataire;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.service.EPGFeuilleRouteService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepp.rest.api.WSEpp;
import fr.dila.solonepp.rest.api.WSEvenement;
import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.constant.TypeEvenementConstants;
import fr.dila.solonmgpp.api.constant.VocabularyConstants;
import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.FichePresentationSD;
import fr.dila.solonmgpp.api.domain.FichePresentationSupportOrganisme;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.domain.RepresentantAUD;
import fr.dila.solonmgpp.api.domain.RepresentantAVI;
import fr.dila.solonmgpp.api.domain.RepresentantOEP;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.FicheDossierDTO;
import fr.dila.solonmgpp.api.dto.HistoriqueDossierDTO;
import fr.dila.solonmgpp.api.dto.IdentiteDTO;
import fr.dila.solonmgpp.api.dto.MgppPieceJointeDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeFichierDTO;
import fr.dila.solonmgpp.api.dto.TableReferenceDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.node.IdentiteNode;
import fr.dila.solonmgpp.api.node.MandatNode;
import fr.dila.solonmgpp.api.service.DossierService;
import fr.dila.solonmgpp.api.service.TableReferenceService;
import fr.dila.solonmgpp.core.builder.ContainerBuilder;
import fr.dila.solonmgpp.core.builder.DossierBuilder;
import fr.dila.solonmgpp.core.builder.EvenementBuilder;
import fr.dila.solonmgpp.core.domain.FichePresentationDRImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationDecretImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.solonmgpp.core.domain.RepresentantAUDImpl;
import fr.dila.solonmgpp.core.domain.RepresentantOEPImpl;
import fr.dila.solonmgpp.core.dto.EvenementDTOImpl;
import fr.dila.solonmgpp.core.dto.FicheDossierDTOImpl;
import fr.dila.solonmgpp.core.dto.HistoriqueDossierDTOImpl;
import fr.dila.solonmgpp.core.util.WSErrorHelper;
import fr.dila.ss.api.logging.enumerationCodes.SSLogEnumImpl;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.api.service.STLockService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.rest.client.HttpTransactionException;
import fr.dila.st.rest.client.WSProxyFactoryException;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import fr.sword.xsd.commons.TraitementStatut;
import fr.sword.xsd.solon.epp.ActionObjetReference;
import fr.sword.xsd.solon.epp.ChercherDossierRequest;
import fr.sword.xsd.solon.epp.ChercherDossierResponse;
import fr.sword.xsd.solon.epp.ChercherEvenementRequest;
import fr.sword.xsd.solon.epp.ChercherEvenementResponse;
import fr.sword.xsd.solon.epp.Depot;
import fr.sword.xsd.solon.epp.EppBaseEvenement;
import fr.sword.xsd.solon.epp.EppDOC01;
import fr.sword.xsd.solon.epp.EppEvt01;
import fr.sword.xsd.solon.epp.EppEvt02;
import fr.sword.xsd.solon.epp.EppEvt03;
import fr.sword.xsd.solon.epp.EppEvt10;
import fr.sword.xsd.solon.epp.EppEvt11;
import fr.sword.xsd.solon.epp.EppEvt24;
import fr.sword.xsd.solon.epp.EppEvt28;
import fr.sword.xsd.solon.epp.EppEvt39;
import fr.sword.xsd.solon.epp.EppEvt43Bis;
import fr.sword.xsd.solon.epp.EppEvt44;
import fr.sword.xsd.solon.epp.EppEvt44Ter;
import fr.sword.xsd.solon.epp.EppEvt49;
import fr.sword.xsd.solon.epp.EppEvt490;
import fr.sword.xsd.solon.epp.EppEvt51;
import fr.sword.xsd.solon.epp.EppEvtContainer;
import fr.sword.xsd.solon.epp.EppJSS01;
import fr.sword.xsd.solon.epp.EppLex40;
import fr.sword.xsd.solon.epp.EppSD02;
import fr.sword.xsd.solon.epp.EvenementType;
import fr.sword.xsd.solon.epp.EvtId;
import fr.sword.xsd.solon.epp.FicheDossier;
import fr.sword.xsd.solon.epp.Institution;
import fr.sword.xsd.solon.epp.MajTableRequest;
import fr.sword.xsd.solon.epp.MajTableResponse;
import fr.sword.xsd.solon.epp.Mandat;
import fr.sword.xsd.solon.epp.NatureLoi;
import fr.sword.xsd.solon.epp.ObjetContainer;
import fr.sword.xsd.solon.epp.ObjetType;
import fr.sword.xsd.solon.epp.Organisme;
import fr.sword.xsd.solon.epp.TypeOrganisme;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.uidgen.UIDGeneratorService;
import org.nuxeo.ecm.core.uidgen.UIDSequencer;

public class DossierServiceImpl extends fr.dila.solonepg.core.service.DossierServiceImpl implements DossierService {
    private static final String OEP_ID_END = "P";
    private static final String OEP_ID_START = "OEP_";
    private static final String SOLON_MGPP_INTERV_EXT_SEQUENCER = "MGPP_INTERV_EXT_SEQUENCER_";
    private static final String SOLON_MGPP_ARTICLE_34_1_SEQUENCER = "MGPP_ARTICLE_34_1_SEQUENCER_";
    private static final String SOLON_MGPP_AUD_SEQUENCER = "MGPP_AUD_SEQUENCER_";
    private static final String SOLONEPG_AP_STATS_PUBLIE_FILES_DIRECTORY = "listeOEP";

    private static final long serialVersionUID = -4066119739997405289L;

    private static final List<EvenementType> ficheLoiEvenements;

    static {
        ficheLoiEvenements = new ArrayList<>();
        ficheLoiEvenements.add(EvenementType.EVT_01);
        ficheLoiEvenements.add(EvenementType.EVT_02);
        ficheLoiEvenements.add(EvenementType.EVT_10);
        ficheLoiEvenements.add(EvenementType.EVT_11);
        ficheLoiEvenements.add(EvenementType.EVT_24);
        ficheLoiEvenements.add(EvenementType.LEX_40);
    }

    /**
     * Logger formalisé en surcouche du logger apache/log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(DossierServiceImpl.class);

    /************************************************************
     * Fonctions globales *
     ************************************************************/

    @Override
    public HistoriqueDossierDTO findDossier(final String idsDossiersEPPLies, final CoreSession session) {
        WSEpp wsEpp = null;

        try {
            wsEpp = SolonMgppWsLocator.getWSEpp(session);
        } catch (final WSProxyFactoryException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
            throw new NuxeoException(e);
        }

        final ChercherDossierRequest chercherDossierRequest = new ChercherDossierRequest();
        if (idsDossiersEPPLies == null) {
            throw new NuxeoException("Aucun identifiant fourni pour la recherche de l'historique");
        }
        List<String> idsDossierEppsList = Arrays.asList(idsDossiersEPPLies.split(";"));
        for (String idDossier : idsDossierEppsList) {
            chercherDossierRequest.getIdDossier().add(idDossier);
        }

        ChercherDossierResponse chercherDossierResponse = null;

        try {
            chercherDossierResponse = wsEpp.chercherDossier(chercherDossierRequest);
        } catch (final HttpTransactionException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
            throw new NuxeoException(SolonMgppWsLocator.getConnexionFailureMessage(session));
        } catch (final Exception e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
            throw new NuxeoException(e);
        }

        if (chercherDossierResponse == null) {
            throw new NuxeoException("Erreur de communication avec SOLON EPP, la récupération du dossier a échoué.");
        } else if (
            chercherDossierResponse.getStatut() == null ||
            !TraitementStatut.OK.equals(chercherDossierResponse.getStatut())
        ) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, chercherDossierResponse.getMessageErreur());

            throw new NuxeoException(
                "Erreur de communication avec SOLON EPP, la récupération du dossier a échoué." +
                WSErrorHelper.buildCleanMessage(chercherDossierResponse.getMessageErreur())
            );
        }

        final FicheDossierDTO ficheDossierDTO = new FicheDossierDTOImpl();

        final List<fr.sword.xsd.solon.epp.Dossier> dossiers = chercherDossierResponse.getDossier();
        final List<FicheDossier> fichesDossiers = new ArrayList<>();
        FicheDossier ificheDossier;
        final List<EppEvtContainer> eppEvts = new ArrayList<>();

        for (fr.sword.xsd.solon.epp.Dossier idoss : dossiers) {
            ificheDossier = idoss.getFicheDossier();
            fichesDossiers.add(ificheDossier);
            eppEvts.addAll(idoss.getEvenement());
            if (ificheDossier != null) {
                DossierBuilder.getInstance().buildDossierDTOFromFicheDossier(ficheDossierDTO, ificheDossier, session);
            }
        }

        final Set<String> listHistoriqueType = new HashSet<>();

        final Map<String, List<EvenementDTO>> mapSuivant = new HashMap<>();

        final Map<String, EvenementDTO> rootEvents = new HashMap<>();

        final List<String> idsEvenementsDisplayed = new ArrayList<>();

        for (final EppEvtContainer eppEvtContainer : eppEvts) {
            final EvenementDTO eventDTO = new EvenementDTOImpl();
            EvenementBuilder.getInstance().buildEvenementDTOFromContainer(eventDTO, eppEvtContainer, session);

            if (StringUtils.isNotBlank(eventDTO.getIdEvenementPrecedent())) {
                if (mapSuivant.get(eventDTO.getIdEvenementPrecedent()) == null) {
                    mapSuivant.put(eventDTO.getIdEvenementPrecedent(), new ArrayList<>());
                }

                mapSuivant.get(eventDTO.getIdEvenementPrecedent()).add(eventDTO);
            } else {
                rootEvents.put(eventDTO.getIdEvenement(), eventDTO);
            }
            idsEvenementsDisplayed.add(eventDTO.getIdEvenement());

            listHistoriqueType.add(eventDTO.getTypeEvenementName());
        }

        final List<String> idEvenementsPrecedent = new ArrayList<>(mapSuivant.keySet());
        for (String id : idEvenementsPrecedent) {
            if (!idsEvenementsDisplayed.contains(id)) {
                List<EvenementDTO> eventsDTO = mapSuivant.get(id);
                for (EvenementDTO event : eventsDTO) {
                    rootEvents.put(event.getIdEvenement(), event);
                }
            }
        }

        return new HistoriqueDossierDTOImpl(ficheDossierDTO, rootEvents, mapSuivant, listHistoriqueType);
    }

    @Override
    public void attachIdDossierToDosier(final CoreSession session, final Dossier dossier, final String idDossier) {
        if (dossier == null) {
            throw new NuxeoException("Impossible de retrouver le dossier venant d'être créé");
        } else {
            if (StringUtils.isBlank(dossier.getIdDossier())) {
                dossier.setIdDossier(idDossier);
                session.saveDocument(dossier.getDocument());
                session.save();
            }

            final FicheLoi ficheDossier = findOrCreateFicheLoi(session, idDossier);

            STServiceLocator.getSTLockService().unlockDocUnrestricted(session, ficheDossier.getDocument());

            remapDossierFieldToFicheLoi(session, dossier, ficheDossier);

            computeDatePromulgationFicheLoi(ficheDossier);

            session.saveDocument(ficheDossier.getDocument());
            session.save();
        }
    }

    @Override
    public void publierDossier(final Dossier dossier, final CoreSession session, final boolean validateCurrentSteps) {
        super.publierDossier(dossier, session, validateCurrentSteps);
        // creation de l'EVT28 à la publication du dossier
        traiterPublication(session, dossier);
    }

    private void traiterPublication(final CoreSession session, final Dossier dossier) {
        if (StringUtils.isNotBlank(dossier.getIdDossier())) {
            final FicheLoi ficheLoi = findOrCreateFicheLoi(session, dossier.getIdDossier());
            STServiceLocator.getSTLockService().unlockDocUnrestricted(session, ficheLoi.getDocument());

            final RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
            final TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
            ficheLoi.setDateJO(retourDila.getDateParutionJorf());
            if (ficheLoi.getTitreOfficiel() == null) {
                DonneesSignataire signaturePr = traitementPapier.getSignaturePr();
                Calendar dateRetour = signaturePr.getDateRetourSignature();
                String dateRetourFrmt =
                    (
                        dateRetour == null
                            ? ""
                            : SolonDateConverter.getClientConverter().format(signaturePr.getDateRetourSignature())
                    );

                ficheLoi.setTitreOfficiel(
                    "Loi n° " +
                    StringUtils.defaultString(retourDila.getNumeroTexteParutionJorf()) +
                    " du " +
                    dateRetourFrmt
                );
            }

            computeDatePromulgationFicheLoi(ficheLoi);

            session.saveDocument(ficheLoi.getDocument());
            session.save();

            // creation de l'EVT 28 brouillon si non existant
            SolonMgppServiceLocator.getEvenementService().createEvenementEppEvt28Brouillon(dossier, session);
        }
    }

    private void remapDepotInformation(
        final String emetteur,
        final XMLGregorianCalendar date,
        final String numero,
        final FicheLoi ficheLoi
    ) {
        ficheLoi.setDateDepot(DateUtil.xmlGregorianCalendarToCalendar(date));
        ficheLoi.setNumeroDepot(numero);
        ficheLoi.setAssembleeDepot(emetteur);
    }

    @Override
    public void updateBordereau(final CoreSession session, final EvenementDTO evenementDTO, final Dossier dossier) {
        if (TypeEvenementConstants.TYPE_EVENEMENT_EVT02.equals(evenementDTO.getTypeEvenementName())) {
            // mise a jour du titre d'acte
            final EppBaseEvenement eppBase = ContainerBuilder
                .getInstance()
                .buildEppEvtFromEvenementDTO(evenementDTO, session);

            if (eppBase instanceof EppEvt02) {
                final EppEvt02 eppEvt02 = (EppEvt02) eppBase;
                if (eppEvt02.getIntitule() != null) {
                    dossier.setTitreActe(eppEvt02.getIntitule());
                    session.saveDocument(dossier.getDocument());
                    session.save();
                }
            }
        }
    }

    @Override
    public void updateFondDossier(final CoreSession session, final EvenementDTO evenementDTO, final Dossier dossier) {
        if (dossier == null) {
            return;
        }

        // recuperation des pieces jointes
        final EvenementTypeDescriptor descriptor = SolonMgppServiceLocator
            .getEvenementTypeService()
            .getEvenementType(evenementDTO.getTypeEvenementName());
        final List<PieceJointeFichierDTO> listPJF = new ArrayList<>();
        if (descriptor != null) {
            for (final String typePieceJointe : descriptor.getPieceJointe().keySet()) {
                if (evenementDTO.getListPieceJointe(typePieceJointe) != null) {
                    for (final MgppPieceJointeDTO pieceJointeDTO : evenementDTO.getListPieceJointe(typePieceJointe)) {
                        if (pieceJointeDTO.getFichier() != null) {
                            for (final PieceJointeFichierDTO pieceJointeFichierDTO : pieceJointeDTO.getFichier()) {
                                SolonMgppServiceLocator
                                    .getPieceJointeService()
                                    .setContentFromEpp(pieceJointeFichierDTO, evenementDTO, pieceJointeDTO, session);
                                listPJF.add(pieceJointeFichierDTO);
                            }
                        }
                    }
                }
            }
        }

        // ajout dans Répertoire réservé au SGG
        for (final PieceJointeFichierDTO pieceJointeFichierDTO : listPJF) {
            SolonEpgServiceLocator
                .getFondDeDossierService()
                .createFondDeDossierFile(
                    session,
                    pieceJointeFichierDTO.getNomFichier(),
                    pieceJointeFichierDTO.getContenu(),
                    SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_RESERVE_SGG,
                    dossier.getDocument()
                );
        }
    }

    public void generateBirtReports(final CoreSession session, final String idDossier, final String ficheId) {
        final String reportName = "fiche_oep_diffusion";
        final String generatedReportName = SolonMgppViewConstant.OEP_FICHES_PUBLIEES_PREFIX + idDossier;
        final String path = getPathAPPublieRepertoire();

        final File generatedFilesDir = new File(path);
        if (!generatedFilesDir.exists()) {
            generatedFilesDir.mkdirs();
        }

        final Map<String, Serializable> inputValues = new HashMap<>();

        final StringBuilder representantsAN = new StringBuilder();
        final StringBuilder representantsSE = new StringBuilder();
        final List<DocumentModel> docsAN = fetchRepresentantOEP(session, "AN", ficheId);
        final List<DocumentModel> docsSE = fetchRepresentantOEP(session, "SE", ficheId);
        putRepresentants(session, docsAN, representantsAN);
        putRepresentants(session, docsSE, representantsSE);

        inputValues.put("FICHEID_PARAM", ficheId);
        inputValues.put("REP_AN_PARAM", representantsAN.toString());
        inputValues.put("REP_SE_PARAM", representantsSE.toString());

        final List<BirtOutputFormat> outPutFormats = new ArrayList<>();
        outPutFormats.add(BirtOutputFormat.HTML);
        outPutFormats.add(BirtOutputFormat.PDF);
        final Map<BirtOutputFormat, Blob> exportsContent = SSServiceLocator
            .getSSBirtService()
            .generateReportResults(reportName, null, inputValues, outPutFormats);
        final Blob blobHtml = exportsContent.get(BirtOutputFormat.HTML);
        final Blob blobPdf = exportsContent.get(BirtOutputFormat.PDF);
        SolonEpgServiceLocator
            .getStatsGenerationResultatService()
            .publierReportResulat(generatedReportName, blobHtml, path, BirtOutputFormat.HTML.getExtension());
        SolonEpgServiceLocator
            .getStatsGenerationResultatService()
            .publierReportResulat(generatedReportName, blobPdf, path, BirtOutputFormat.PDF.getExtension());
        session.save();
    }

    public void deleteBirtReports(final CoreSession session, final String idDossier) {
        final String reportName = SolonMgppViewConstant.OEP_FICHES_PUBLIEES_PREFIX + idDossier;
        final String path = getPathAPPublieRepertoire();

        final String fileFullPathName = path + "/" + reportName;
        final File fileHtml = new File(fileFullPathName + ".html");
        final File filePdf = new File(fileFullPathName + ".pdf");

        if (fileHtml.delete()) {
            LOGGER.debug(session, SSLogEnumImpl.DEL_BIRT_TEC, fileHtml.getPath());
        } else {
            LOGGER.warn(session, SSLogEnumImpl.FAIL_DEL_BIRT_TEC, fileHtml.getPath());
        }

        if (filePdf.delete()) {
            LOGGER.debug(session, SSLogEnumImpl.DEL_BIRT_TEC, filePdf.getPath());
        } else {
            LOGGER.warn(session, SSLogEnumImpl.FAIL_DEL_BIRT_TEC, filePdf.getPath());
        }
    }

    public String constructTableMinisteres(final Map<String, List<FichePresentationOEP>> oepTm) {
        if (oepTm == null || oepTm.size() == 0) {
            return "";
        }

        final StringBuilder str = new StringBuilder("<table class='minTable'>");
        for (final Map.Entry<String, List<FichePresentationOEP>> entry : oepTm.entrySet()) {
            final String ministere = entry.getKey();
            final List<FichePresentationOEP> listOep = entry.getValue();

            str.append("<tr><td><span class='minTitle'>" + ministere + "</span></td></tr>");

            final Iterator<FichePresentationOEP> oepIt = listOep.iterator();
            while (oepIt.hasNext()) {
                final FichePresentationOEP tempOep = oepIt.next();
                final String idDossier = tempOep.getIdDossier();
                final String oepTitle = tempOep.getNomOrganisme();
                String dateMAJ = "";
                if (tempOep.getDateDiffusion() != null) {
                    dateMAJ = SolonDateConverter.DATE_SLASH.format(tempOep.getDateDiffusion());
                }

                str.append(
                    "<tr><td class='minTd'><span class='titreOepMin' onclick=\"openHtmlFile('" +
                    SolonMgppViewConstant.OEP_FICHES_PUBLIEES_PREFIX +
                    idDossier +
                    "')\" >" +
                    oepTitle +
                    "</span><span class='dateOepMin'>dernière mise à jour le " +
                    dateMAJ +
                    "</span>" +
                    "<a href='openSaveDialog.jsp?fileName=" +
                    SolonMgppViewConstant.OEP_FICHES_PUBLIEES_PREFIX +
                    idDossier +
                    "' ><img src='pdf.png' style='border:none;cursor:pointer' alt='more' /></a></td></tr>"
                );
            }
        }
        str.append("</table>");

        return str.toString();
    }

    /**
     * get le path du répertoire qui va contenir le jsp du tableau des fiches
     * OEP publiees + le repertoire contenant les rapports publies de l'activite
     * parlementaire
     *
     * @return
     */
    @Override
    public String getPathDirAPPublie() {
        final ConfigService configService = STServiceLocator.getConfigService();
        final String generatedReportPath = configService.getValue(
            SolonEpgConfigConstant.SOLONEPG_AP_STATS_PUBLIE_DIRECTORY
        );

        final File generatedReportDir = new File(generatedReportPath);
        if (generatedReportDir.exists() == false) {
            generatedReportDir.mkdirs();
        }
        return generatedReportPath;
    }

    /**
     * get le path du répertoire qui va contenir les fichiers publies de
     * l'activite parlementaire
     *
     * @return
     */
    @Override
    public String getPathAPPublieRepertoire() {
        final String originalPathOfActiviteParlementaire = getPathDirAPPublie();
        final String pathDuRepertoire =
            originalPathOfActiviteParlementaire + "/" + SOLONEPG_AP_STATS_PUBLIE_FILES_DIRECTORY;

        final File generatedReportDir = new File(pathDuRepertoire);
        if (!generatedReportDir.exists()) {
            generatedReportDir.mkdirs();
        }
        return pathDuRepertoire;
    }

    /**
     * recherche l'identité des représentants contenus dans une liste de
     * DocumentModel de représentants et insere dans une String le résultat sous
     * la forme : numero mandat/[Monsieur-Madame] Nom Prenom;
     *
     * @param representantsDoc
     *            liste de documentModel de représentant
     * @param representants
     *            StringBuilder à laquelle on ajoute les résultats des identités
     *            trouvées
     */
    private void putRepresentants(
        final CoreSession session,
        final List<DocumentModel> representantsDoc,
        final StringBuilder representants
    ) {
        final TableReferenceService trService = SolonMgppServiceLocator.getTableReferenceService();
        for (final DocumentModel repDoc : representantsDoc) {
            final RepresentantOEP rep = repDoc.getAdapter(RepresentantOEP.class);
            final String mandat = rep.getRepresentant();
            final MandatNode mandatNode = trService.getMandat(mandat, session);
            if (mandatNode == null) {
                representants.append(mandat).append("/").append("** Mandat inconnu **").append(";");
            } else {
                final IdentiteNode identite = mandatNode.getIdentite();
                representants
                    .append(mandat)
                    .append("/")
                    .append(identite.getCivilite())
                    .append(" ")
                    .append(identite.getNom())
                    .append(" ")
                    .append(identite.getPrenom())
                    .append(";");
            }
        }
    }

    public String appendTexteLibre(final CoreSession session) {
        final ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator
            .getParametreMgppService()
            .findParametrageMgpp(session);
        final String texteLibreListeOep = parametrageMgpp.getTexteLibreListeOep();
        final StringBuilder str = new StringBuilder("<span class='texteLibre'>");
        str.append(texteLibreListeOep);
        str.append("</span>");

        return str.toString();
    }

    @Override
    public void gererFichePresentation(
        final EppEvtContainer eppEvtContainer,
        final boolean publie,
        final CoreSession session
    ) {
        // recherche de l'evenement vu que EPP ne le donne pas...
        final EvenementDTO evenementDTO = new EvenementDTOImpl();
        EvenementBuilder.getInstance().buildEvenementDTOFromContainer(evenementDTO, eppEvtContainer, session);

        if (evenementDTO != null) {
            // creation de la fiche dossier suite a la creation d'une
            // communication 01
            if (TypeEvenementConstants.TYPE_EVENEMENT_EVT01.equals(evenementDTO.getTypeEvenementName())) {
                final FicheLoi ficheLoi = findOrCreateFicheLoi(session, evenementDTO.getIdDossier());
                if (ficheLoi.getNatureLoi() == null) {
                    ficheLoi.setNatureLoi(NatureLoi.PROJET);
                    session.saveDocument(ficheLoi.getDocument());
                    session.save();
                }
            } else if (
                TypeEvenementConstants.TYPE_EVENEMENT_EVT28.equals(evenementDTO.getTypeEvenementName()) && publie
            ) {
                // maj FicheLoi
                // Maj infos de publication
                final EppEvt28 eppEvt28 = eppEvtContainer.getEvt28();
                if (eppEvt28 != null) {
                    final FicheLoi fiche = findOrCreateFicheLoi(session, eppEvt28.getIdDossier());

                    STServiceLocator.getSTLockService().unlockDocUnrestricted(session, fiche.getDocument());

                    fiche.setDateJO(DateUtil.xmlGregorianCalendarToCalendar(eppEvt28.getDatePublication()));

                    computeDatePromulgationFicheLoi(fiche);

                    session.saveDocument(fiche.getDocument());
                    session.save();
                }
                // creation de la fiche Depot Rapport a la publication de
                // l'evenement 28
                SolonMgppServiceLocator.getDossierService().findOrCreateFicheDR(session, evenementDTO.getIdDossier());
            } else if (TypeEvenementConstants.TYPE_EVENEMENT_EVT44.equals(evenementDTO.getTypeEvenementName())) {
                // maj de la fiche DR
                final FichePresentationDR fichePresentationDR = SolonMgppServiceLocator
                    .getDossierService()
                    .findOrCreateFicheDR(session, evenementDTO.getIdDossier());

                if (publie) {
                    final EppEvt44 eppEvt44 = eppEvtContainer.getEvt44();

                    fichePresentationDR.setActif(Boolean.FALSE);
                    if (eppEvt44.getRapportParlement() != null) {
                        fichePresentationDR.setRapportParlement(eppEvt44.getRapportParlement().value());
                    }
                    fichePresentationDR.setObjet(eppEvt44.getObjet());
                    if (eppEvt44.getDate() != null) {
                        fichePresentationDR.setDateDepotEffectif(eppEvt44.getDate().toGregorianCalendar());
                    }
                }

                session.saveDocument(fichePresentationDR.getDocument());
                session.save();
            } else if (TypeEvenementConstants.TYPE_EVENEMENT_EVT44TER.equals(evenementDTO.getTypeEvenementName())) {
                // maj de la fiche DR
                final FichePresentationDR fichePresentationDR = SolonMgppServiceLocator
                    .getDossierService()
                    .findOrCreateFicheDR(session, evenementDTO.getIdDossier());

                if (publie) {
                    final EppEvt44Ter eppEvt44Ter = eppEvtContainer.getEvt44Ter();

                    fichePresentationDR.setActif(Boolean.FALSE);
                    fichePresentationDR.setObjet(eppEvt44Ter.getObjet());
                    if (eppEvt44Ter.getDate() != null) {
                        fichePresentationDR.setDateDepotEffectif(eppEvt44Ter.getDate().toGregorianCalendar());
                    }
                }

                session.saveDocument(fichePresentationDR.getDocument());
                session.save();
            } else if (
                TypeEvenementConstants.TYPE_EVENEMENT_EVT49.equals(evenementDTO.getTypeEvenementName()) ||
                TypeEvenementConstants.TYPE_EVENEMENT_EVT49_0.equals(evenementDTO.getTypeEvenementName())
            ) {
                // creation de la fiche OEP
                final FichePresentationOEP fichePresentationOEP = findOrCreateFicheRepresentationOEP(
                    session,
                    evenementDTO
                );
                session.saveDocument(fichePresentationOEP.getDocument());
                session.save();
            } else if (TypeEvenementConstants.isEvenementPG(evenementDTO.getTypeEvenementName())) {
                // creation de la fiche DPG
                final FichePresentationDPG fichePresentationDPG =
                    this.findOrCreateFicheDPG(session, evenementDTO.getIdDossier());

                // Règle RG-PG-CRE-03
                String objet = null;
                Calendar datePresentation = null;
                Calendar dateLettrePm = null;
                if (eppEvtContainer.getPG01() != null) {
                    objet = eppEvtContainer.getPG01().getObjet();
                    datePresentation =
                        eppEvtContainer.getPG01().getDatePresentation() != null
                            ? eppEvtContainer.getPG01().getDatePresentation().toGregorianCalendar()
                            : null;
                    dateLettrePm =
                        eppEvtContainer.getPG01().getDateLettrePm() != null
                            ? eppEvtContainer.getPG01().getDateLettrePm().toGregorianCalendar()
                            : null;
                } else if (eppEvtContainer.getPG02() != null) {
                    objet = eppEvtContainer.getPG02().getObjet();
                    datePresentation =
                        eppEvtContainer.getPG02().getDatePresentation() != null
                            ? eppEvtContainer.getPG02().getDatePresentation().toGregorianCalendar()
                            : null;
                    dateLettrePm =
                        eppEvtContainer.getPG02().getDateLettrePm() != null
                            ? eppEvtContainer.getPG02().getDateLettrePm().toGregorianCalendar()
                            : null;
                } else if (eppEvtContainer.getPG03() != null) {
                    objet = eppEvtContainer.getPG03().getObjet();
                    datePresentation =
                        eppEvtContainer.getPG03().getDatePresentation() != null
                            ? eppEvtContainer.getPG03().getDatePresentation().toGregorianCalendar()
                            : null;
                    dateLettrePm =
                        eppEvtContainer.getPG03().getDateLettrePm() != null
                            ? eppEvtContainer.getPG03().getDateLettrePm().toGregorianCalendar()
                            : null;
                } else if (eppEvtContainer.getGENERIQUE11() != null) {
                    objet = eppEvtContainer.getGENERIQUE11().getObjet();
                } else if (eppEvtContainer.getFUSION11() != null) {
                    objet = eppEvtContainer.getFUSION11().getObjet();
                } else if (eppEvtContainer.getALERTE11() != null) {
                    objet = eppEvtContainer.getALERTE11().getObjet();
                }

                if (fichePresentationDPG.getObjet() == null) {
                    fichePresentationDPG.setObjet(objet);
                }

                if (fichePresentationDPG.getDateLettrePm() == null) {
                    fichePresentationDPG.setDateLettrePm(dateLettrePm);
                }

                if (fichePresentationDPG.getDatePresentation() == null) {
                    fichePresentationDPG.setDatePresentation(datePresentation);
                }

                session.saveDocument(fichePresentationDPG.getDocument());
                session.save();
            } else if (TypeEvenementConstants.isEvenementSD(evenementDTO.getTypeEvenementName())) {
                // creation de la fiche SD

                boolean assignData = publie;
                FichePresentationSD fichePresentationSDationSD =
                    this.findFichePresentationSD(session, evenementDTO.getIdDossier());
                if (fichePresentationSDationSD == null) {
                    fichePresentationSDationSD = this.createFicheSD(session, evenementDTO.getIdDossier());
                    assignData = true;
                }
                // RG-SD-CRE-03
                if (assignData) {
                    if (eppEvtContainer.getSD02() != null) {
                        final EppSD02 eppSd02 = eppEvtContainer.getSD02();
                        if (
                            fichePresentationSDationSD.getDateDeclaration() == null &&
                            eppSd02.getDateDeclaration() != null
                        ) {
                            fichePresentationSDationSD.setDateDeclaration(
                                eppSd02.getDateDeclaration().toGregorianCalendar()
                            );
                        }
                        if (fichePresentationSDationSD.getObjet() == null) {
                            fichePresentationSDationSD.setObjet(evenementDTO.getObjet());
                        }

                        if (fichePresentationSDationSD.getDateLettrePm() == null && eppSd02.getDateLettrePm() != null) {
                            fichePresentationSDationSD.setDateLettrePm(eppSd02.getDateLettrePm().toGregorianCalendar());
                        }
                        fichePresentationSDationSD.setDemandeVote(eppSd02.isDemandeVote());
                        final List<String> groupeParlementaire = new ArrayList<>();
                        for (final Organisme organisme : eppSd02.getGroupeParlementaire()) {
                            groupeParlementaire.add(organisme.getId());
                        }
                        fichePresentationSDationSD.setGroupeParlementaire(groupeParlementaire);

                        // Save the doc
                        session.saveDocument(fichePresentationSDationSD.getDocument());
                    }
                }
                // Save the doc
                session.saveDocument(fichePresentationSDationSD.getDocument());
            } else if (TypeEvenementConstants.isEvenementJSS(evenementDTO.getTypeEvenementName())) {
                // creation de la fiche JSS
                final FichePresentationJSS fichePresentationSDationJSS =
                    this.findOrCreateFicheJSS(session, evenementDTO.getIdDossier());

                if (eppEvtContainer.getJSS01() != null) {
                    // RG-JSS-CRE-03
                    final EppJSS01 eppJSS01 = eppEvtContainer.getJSS01();

                    if (fichePresentationSDationJSS.getDateLettrePm() == null && eppJSS01.getDateLettrePm() != null) {
                        fichePresentationSDationJSS.setDateLettrePm(eppJSS01.getDateLettrePm().toGregorianCalendar());
                    }

                    if (fichePresentationSDationJSS.getObjet() == null) {
                        fichePresentationSDationJSS.setObjet(eppJSS01.getObjet());
                    }

                    // Save the doc
                    session.saveDocument(fichePresentationSDationJSS.getDocument());
                }
            } else if (TypeEvenementConstants.isEvenementDOC(evenementDTO.getTypeEvenementName())) {
                FichePresentationDOC fichePresentationDoc =
                    this.findOrCreateFicheDOC(session, evenementDTO.getIdDossier());

                if (eppEvtContainer.getDOC01() != null) {
                    EppDOC01 doc01 = eppEvtContainer.getDOC01();
                    if (fichePresentationDoc.getObjet() == null) {
                        fichePresentationDoc.setObjet(doc01.getObjet());
                    }

                    if (fichePresentationDoc.getBaseLegale() == null && doc01.getBaseLegale() != null) {
                        fichePresentationDoc.setBaseLegale(doc01.getBaseLegale());
                    }

                    if (fichePresentationDoc.getDateLettrePm() == null) {
                        fichePresentationDoc.setDateLettrePm(doc01.getDateLettrePm().toGregorianCalendar());
                    }

                    if (
                        fichePresentationDoc.getCommissions() == null || fichePresentationDoc.getCommissions().isEmpty()
                    ) {
                        List<String> commissions = new ArrayList<>();
                        for (Organisme orgnanisme : doc01.getCommission()) {
                            commissions.add(orgnanisme.getId());
                        }
                        fichePresentationDoc.setCommissions(commissions);
                    }
                }
                // Save the doc
                session.saveDocument(fichePresentationDoc.getDocument());
            } else if (TypeEvenementConstants.isEvenementAUD(evenementDTO.getTypeEvenementName())) {
                FichePresentationAUD fichePresentationAUD =
                    this.findOrCreateFicheAUD(session, evenementDTO.getIdDossier());
                // Save the doc
                session.saveDocument(fichePresentationAUD.getDocument());
            } else if (TypeEvenementConstants.TYPE_EVENEMENT_EVT10.equals(evenementDTO.getTypeEvenementName())) {
                // Engagement de la procédure accélérée
                final EppEvt10 eppEvt10 = eppEvtContainer.getEvt10();
                // Récupération de la fiche loi
                final FicheLoi fiche = getUnlockedFicheLoi(session, eppEvt10.getIdDossier());
                // Récupération de la date de la procédure accélérée
                Calendar dateProcAcc = DateUtil.xmlGregorianCalendarToCalendar(eppEvt10.getDateEngagementProcedure());
                // Renseignement de la date sur la fiche loi
                fiche.setProcedureAcceleree(dateProcAcc);
                computeDatePromulgationFicheLoi(fiche);
                // Enregistrement du document
                session.saveDocument(fiche.getDocument());
                session.save();
            }
        }
    }

    /************************************************************
     * Gestion des Fiches Loi *
     ************************************************************/

    private void remapDossierFieldToFicheLoi(
        final CoreSession session,
        final Dossier dossier,
        final FicheLoi ficheDossier
    ) {
        STServiceLocator.getSTLockService().unlockDocUnrestricted(session, ficheDossier.getDocument());

        ficheDossier.setNumeroNor(dossier.getNumeroNor());
        ficheDossier.setIntitule(dossier.getTitreActe());
    }

    @Override
    public FicheLoi findOrCreateFicheLoi(final CoreSession session, final String idDossier) {
        synchronized (this) {
            final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
            queryBuilder.append(FicheLoi.DOC_TYPE);
            queryBuilder.append(" as d WHERE d.");
            queryBuilder.append(FicheLoi.PREFIX);
            queryBuilder.append(":");
            queryBuilder.append(FicheLoi.ID_DOSSIER);
            queryBuilder.append(" = ? ");
            FicheLoi fiche = null;
            final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
                session,
                FicheLoi.DOC_TYPE,
                queryBuilder.toString(),
                new Object[] { idDossier }
            );
            if (docs == null || docs.isEmpty()) {
                fiche = createFicheLoi(session, idDossier);
            } else if (docs.size() == 2) {
                LOGGER.warn(session, MgppLogEnumImpl.GET_FICHE_LOI_TEC);
                fiche = mergeFiches(session, docs);
            } else if (docs.size() > 2) {
                throw new NuxeoException("Plusieurs fiches lois trouvées pour " + idDossier + ".");
            } else {
                fiche = docs.get(0).getAdapter(FicheLoi.class);
            }
            return fiche;
        }
    }

    @Override
    public List<FicheLoi> findFicheLoiWithoutNature(final CoreSession session) {
        synchronized (this) {
            // Requête pour récupérer l'ensemble des fiches lois qui n'ont pas
            // de nature
            final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
            queryBuilder.append(FicheLoi.DOC_TYPE);
            queryBuilder.append(" as d WHERE d.");
            queryBuilder.append(FicheLoi.PREFIX);
            queryBuilder.append(":");
            queryBuilder.append(FicheLoi.NATURE_LOI);
            queryBuilder.append(" is null");
            List<FicheLoi> listFiche = new ArrayList<>();
            final List<DocumentModel> lstDocs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
                session,
                FicheLoi.DOC_TYPE,
                queryBuilder.toString(),
                null
            );

            if (lstDocs != null) {
                for (DocumentModel doc : lstDocs) {
                    listFiche.add(doc.getAdapter(FicheLoi.class));
                }
            }

            return listFiche;
        }
    }

    /**
     * Fusion des 2 fiches créées par erreur de synchronisation lors de la
     * création de la fiche de MGPP.
     *
     * @param session
     * @param docs
     * @return
     */
    private FicheLoi mergeFiches(final CoreSession session, final List<DocumentModel> docs) {
        FicheLoi ficheTo = null;
        FicheLoi ficheFrom = null;

        if (SolonMgppServiceLocator.getNavetteService().hasNavette(session, docs.get(0).getId())) {
            ficheTo = docs.get(0).getAdapter(FicheLoi.class);
            ficheFrom = docs.get(1).getAdapter(FicheLoi.class);
        } else {
            ficheTo = docs.get(1).getAdapter(FicheLoi.class);
            ficheFrom = docs.get(0).getAdapter(FicheLoi.class);
        }

        ficheTo.setNumeroNor(ficheFrom.getNumeroNor());
        ficheTo.setProcedureAcceleree(ficheFrom.getProcedureAcceleree());
        ficheTo.setArticle493(ficheFrom.isArticle493());

        if (ficheTo.getAssembleeDepot() == null) {
            ficheTo.setAssembleeDepot(ficheFrom.getAssembleeDepot());
        }
        if (ficheTo.getIntitule() == null) {
            ficheTo.setIntitule(ficheFrom.getIntitule());
        }
        if (ficheTo.getDateCM() == null) {
            ficheTo.setDateCM(ficheFrom.getDateCM());
        }
        if (ficheTo.getDateAdoption() == null) {
            ficheTo.setDateAdoption(ficheFrom.getDateAdoption());
        }
        if (ficheTo.getDateDecision() == null) {
            ficheTo.setDateDecision(ficheFrom.getDateDecision());
        }
        if (ficheTo.getDateDepot() == null) {
            ficheTo.setDateDepot(ficheFrom.getDateDepot());
        }
        if (ficheTo.getDateSaisieCC() == null) {
            ficheTo.setDateSaisieCC(ficheFrom.getDateSaisieCC());
        }
        if (ficheTo.getDateLimitePromulgation() == null) {
            ficheTo.setDateLimitePromulgation(ficheFrom.getDateLimitePromulgation());
        }
        if (ficheTo.getDateJO() == null) {
            ficheTo.setDateJO(ficheFrom.getDateJO());
        }
        if (ficheTo.getDateReception() == null) {
            ficheTo.setDateReception(ficheFrom.getDateReception());
        }
        if (ficheTo.getNumeroDepot() == null) {
            ficheTo.setNumeroDepot(ficheFrom.getNumeroDepot());
        }
        if (ficheTo.getObservation() == null) {
            ficheTo.setObservation(ficheFrom.getObservation());
        }
        if (ficheTo.getMinistereResp() == null) {
            ficheTo.setMinistereResp(ficheFrom.getMinistereResp());
        }
        if (ficheTo.getDateProjet() == null) {
            ficheTo.setDateProjet(ficheFrom.getDateProjet());
        }
        if (ficheTo.getDateSectionCe() == null) {
            ficheTo.setDateSectionCe(ficheFrom.getDateSectionCe());
        }
        if (ficheTo.getNumeroISA() == null) {
            ficheTo.setNumeroISA(ficheFrom.getNumeroISA());
        }
        if (ficheTo.getDiffusion() == null) {
            ficheTo.setDiffusion(ficheFrom.getDiffusion());
        }
        if (ficheTo.getTitreOfficiel() == null) {
            ficheTo.setTitreOfficiel(ficheFrom.getTitreOfficiel());
        }
        if (ficheTo.getDateProjet() == null) {
            ficheTo.setDateProjet(ficheFrom.getDateProjet());
        }
        if (ficheTo.getNatureLoi() == null) {
            ficheTo.setNatureLoi(ficheFrom.getNatureLoi());
        }
        LOGGER.info(session, MgppLogEnumImpl.DEL_FICHE_LOI_TEC, ficheFrom.getDocument());
        session.removeDocument(ficheFrom.getDocument().getRef());
        session.saveDocument(ficheTo.getDocument());
        session.save();
        return ficheTo;
    }

    /**
     * Creation d'une {@link FicheLoi}
     *
     * @param session
     * @param idDossier
     * @return
     */
    private FicheLoi createFicheLoi(final CoreSession session, final String idDossier) {
        DocumentModel ficheLoiDoc = session.createDocumentModel(
            "/case-management/fiche-dossier",
            idDossier + "_FL",
            FicheLoi.DOC_TYPE
        );

        final FicheLoi ficheLoi = ficheLoiDoc.getAdapter(FicheLoi.class);
        ficheLoi.setIdDossier(idDossier);
        ficheLoi.setDateCreation(Calendar.getInstance());

        ficheLoi.setProcedureAcceleree(null);

        final Dossier dossier = SolonEpgServiceLocator.getDossierService().findDossierFromIdDossier(session, idDossier);

        if (dossier != null) {
            ficheLoi.setIntitule(dossier.getTitreActe());
            ficheLoi.setMinistereResp(dossier.getMinistereResp());
            try {
                final EPGFeuilleRouteService fdrs = SolonEpgServiceLocator.getEPGFeuilleRouteService();
                ficheLoi.setNomCompletChargeMission(fdrs.getLastChargeMission(session, dossier.getDocument()));
            } catch (Exception e) {
                LOGGER.error(session, MgppLogEnumImpl.FAIL_CREATE_FICHE_LOI_TEC, e);
            }

            final ConseilEtat conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);
            ficheLoi.setDateSectionCe(conseilEtat.getDateSectionCe());
            ficheLoi.setNumeroISA(conseilEtat.getNumeroISA());

            // au cas où les données n'aient jamais été initialisaée et qu'on
            // ait déjà publié la loi, on reporte les
            // infos
            TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
            RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
            if (traitementPapier != null && traitementPapier.getSignaturePr() != null) {
                DonneesSignataire signaturePr = traitementPapier.getSignaturePr();

                ficheLoi.setDepartElysee(signaturePr.getDateEnvoiSignature());
                ficheLoi.setRetourElysee(signaturePr.getDateRetourSignature());

                if (retourDila != null) {
                    if (StringUtils.isNotBlank(retourDila.getNumeroTexteParutionJorf())) {
                        ficheLoi.setTitreOfficiel(
                            "Loi n° " +
                            StringUtils.defaultString(retourDila.getNumeroTexteParutionJorf()) +
                            " du " +
                            SolonDateConverter.getClientConverter().format(signaturePr.getDateRetourSignature())
                        );
                    }
                }
            }

            if (retourDila != null) {
                ficheLoi.setDateJO(retourDila.getDateParutionJorf());
            }
        }

        ficheLoiDoc = session.createDocument(ficheLoi.getDocument());
        session.save();

        return ficheLoiDoc.getAdapter(FicheLoi.class);
    }

    protected void mapFicheLoiWithData(
        final CoreSession session,
        final String idDossier,
        final Institution emetteur,
        final Depot depot,
        final String intitule,
        final NatureLoi nature
    ) {
        final FicheLoi ficheLoi = getUnlockedFicheLoi(session, idDossier);
        if (depot != null) {
            remapDepotInformation(emetteur.value(), depot.getDate(), depot.getNumero(), ficheLoi);
            computeDatePromulgationFicheLoi(ficheLoi);
        }
        if (intitule != null) {
            ficheLoi.setIntitule(intitule);
        }
        if (nature != null) {
            ficheLoi.setNatureLoi(nature);
        }

        session.saveDocument(ficheLoi.getDocument());
        session.save();
    }

    @Override
    public void updateFicheLoi(final CoreSession session, final EvenementDTO evenementDTO) {
        if (TypeEvenementConstants.TYPE_EVENEMENT_EVT02.equals(evenementDTO.getTypeEvenementName())) {
            // mise a jour des infos de dépot
            final EppBaseEvenement eppBase = ContainerBuilder
                .getInstance()
                .buildEppEvtFromEvenementDTO(evenementDTO, session);

            if (eppBase instanceof EppEvt02) {
                final EppEvt02 eppEvt02 = (EppEvt02) eppBase;
                mapFicheLoiWithData(
                    session,
                    evenementDTO.getIdDossier(),
                    eppEvt02.getEmetteur(),
                    eppEvt02.getDepot(),
                    eppEvt02.getIntitule(),
                    eppEvt02.getNatureLoi()
                );
            }
        } else if (TypeEvenementConstants.TYPE_EVENEMENT_EVT03.equals(evenementDTO.getTypeEvenementName())) {
            // mise a jour des infos de dépot
            final EppBaseEvenement eppBase = ContainerBuilder
                .getInstance()
                .buildEppEvtFromEvenementDTO(evenementDTO, session);

            if (eppBase instanceof EppEvt03) {
                final EppEvt03 eppEvt03 = (EppEvt03) eppBase;
                mapFicheLoiWithData(
                    session,
                    evenementDTO.getIdDossier(),
                    eppEvt03.getEmetteur(),
                    eppEvt03.getDepot(),
                    null,
                    null
                );
            }
        } else if (TypeEvenementConstants.TYPE_EVENEMENT_EVT24.equals(evenementDTO.getTypeEvenementName())) {
            // mise a jour des infos de dépot concernat la partie Loi votée
            final EppBaseEvenement eppBase = ContainerBuilder
                .getInstance()
                .buildEppEvtFromEvenementDTO(evenementDTO, session);

            if (eppBase instanceof EppEvt24) {
                final EppEvt24 eppEvt24 = (EppEvt24) eppBase;
                FicheLoi ficheLoi = getUnlockedFicheLoi(session, eppEvt24.getIdDossier());

                if (eppEvt24.getDateAdoption() != null) {
                    ficheLoi.setDateAdoption(DateUtil.xmlGregorianCalendarToCalendar(eppEvt24.getDateAdoption()));
                }

                if (
                    eppEvt24.getVersionCourante() != null &&
                    eppEvt24.getVersionCourante().getMajeur() == 1 &&
                    eppEvt24.getVersionCourante().getMineur() == 0 &&
                    eppEvt24.getVersionCourante().getHorodatage() != null
                ) {
                    Calendar dateReception = DateUtil.xmlGregorianCalendarToCalendar(
                        eppEvt24.getVersionCourante().getHorodatage()
                    );

                    ficheLoi.setDateReception((Calendar) dateReception.clone());
                    dateReception.add(Calendar.DATE, 15);
                    ficheLoi.setDateLimitePromulgation((Calendar) dateReception.clone());
                }

                session.saveDocument(ficheLoi.getDocument());
                session.save();
            }
        } else if (TypeEvenementConstants.TYPE_EVENEMENT_EVT28.equals(evenementDTO.getTypeEvenementName())) {
            final EppBaseEvenement eppBase = ContainerBuilder
                .getInstance()
                .buildEppEvtFromEvenementDTO(evenementDTO, session);

            // Lorsqu'une loi est publiée, on crée une LEX 35, on en profite
            // alors pour mettre à jour les infos de
            // publications
            if (eppBase instanceof EppEvt28) {
                final EppEvt28 eppEvt28 = (EppEvt28) eppBase;
                FicheLoi ficheLoi = getUnlockedFicheLoi(session, eppEvt28.getIdDossier());

                final Dossier dossier = SolonEpgServiceLocator
                    .getDossierService()
                    .findDossierFromIdDossier(session, eppEvt28.getIdDossier());
                TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
                RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);

                if (traitementPapier != null && traitementPapier.getSignaturePr() != null) {
                    DonneesSignataire signaturePr = traitementPapier.getSignaturePr();
                    if (retourDila != null) {
                        if (StringUtils.isNotBlank(retourDila.getNumeroTexteParutionJorf())) {
                            Calendar dateRetour = signaturePr.getDateRetourSignature();
                            String dateRetourFrmt =
                                (
                                    dateRetour == null
                                        ? ""
                                        : SolonDateConverter
                                            .getClientConverter()
                                            .format(signaturePr.getDateRetourSignature())
                                );

                            ficheLoi.setTitreOfficiel(
                                "Loi n° " +
                                StringUtils.defaultString(retourDila.getNumeroTexteParutionJorf()) +
                                " du " +
                                dateRetourFrmt
                            );
                        }

                        ficheLoi.setDateJO(retourDila.getDateParutionJorf());

                        session.saveDocument(ficheLoi.getDocument());
                        session.save();
                    }
                }
            }
        }
    }

    @Override
    public FicheLoi saveFicheLoi(final CoreSession session, DocumentModel ficheLoi) {
        computeDatePromulgationFicheLoi(ficheLoi.getAdapter(FicheLoi.class));

        ficheLoi = session.saveDocument(ficheLoi);
        final FicheLoi fiche = ficheLoi.getAdapter(FicheLoi.class);
        LOGGER.debug(session, MgppLogEnumImpl.UPDATE_FICHE_LOI_TEC);

        return fiche;
    }

    @Override
    public FicheLoi publierFicheLoi(final CoreSession session, DocumentModel ficheLoi) {
        STServiceLocator.getSTLockService().unlockDocUnrestricted(session, ficheLoi);

        FicheLoi fiche = ficheLoi.getAdapter(FicheLoi.class);

        ficheLoi = session.saveDocument(fiche.getDocument());

        session.save();

        fiche = ficheLoi.getAdapter(FicheLoi.class);

        LOGGER.debug(session, MgppLogEnumImpl.UPDATE_FICHE_LOI_TEC);

        return fiche;
    }

    /**
     * Creation d'une {@link FicheLoi}
     */
    @Override
    public void createFicheLoi(final CoreSession session, final EvenementDTO evenementDTO) {
        findOrCreateFicheLoi(session, evenementDTO.getIdDossier());

        if (StringUtils.isNotBlank(evenementDTO.getNor())) {
            final Dossier dossier = SolonEpgServiceLocator
                .getNORService()
                .findDossierFromNOR(session, evenementDTO.getNor());
            if (dossier != null) {
                attachIdDossierToDosier(session, dossier, evenementDTO.getIdDossier());
            }
        }
    }

    /************************************************************
     * Gestion des Communication DR *
     ************************************************************/

    @Override
    public FichePresentationDR findFicheDR(final CoreSession session, final String idDossier) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(FichePresentationDRImpl.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(FichePresentationDRImpl.PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(FichePresentationDRImpl.ID_DOSSIER);
        queryBuilder.append(" = ? ");

        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            FichePresentationDRImpl.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { idDossier }
        );
        if (docs == null || docs.isEmpty()) {
            return null;
        } else if (docs.size() > 1) {
            throw new NuxeoException("Plusieurs fiches DR trouvées pour " + idDossier + ".");
        } else {
            return docs.get(0).getAdapter(FichePresentationDR.class);
        }
    }

    @Override
    public FichePresentationDR saveFicheDR(final CoreSession session, DocumentModel ficheDR) {
        ficheDR = session.saveDocument(ficheDR);
        final FichePresentationDR fiche = ficheDR.getAdapter(FichePresentationDR.class);
        LOGGER.debug(session, MgppLogEnumImpl.UPDATE_FICHE_PRESENTATION_DR_TEC);
        return fiche;
    }

    @Override
    public FichePresentationDR publierFicheDR(final CoreSession session, DocumentModel ficheDR) {
        FichePresentationDR fiche = ficheDR.getAdapter(FichePresentationDR.class);

        ficheDR = session.saveDocument(fiche.getDocument());

        session.save();

        fiche = ficheDR.getAdapter(FichePresentationDR.class);

        LOGGER.debug(session, MgppLogEnumImpl.UPDATE_FICHE_PRESENTATION_DR_TEC);

        return fiche;
    }

    @Override
    public FichePresentationDR findOrCreateFicheDR(final CoreSession session, final String idDossier) {
        synchronized (this) {
            final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
            queryBuilder.append(FichePresentationDRImpl.DOC_TYPE);
            queryBuilder.append(" as d WHERE d.");
            queryBuilder.append(FichePresentationDRImpl.PREFIX);
            queryBuilder.append(":");
            queryBuilder.append(FichePresentationDRImpl.ID_DOSSIER);
            queryBuilder.append(" = ? ");

            final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
                session,
                FichePresentationDRImpl.DOC_TYPE,
                queryBuilder.toString(),
                new Object[] { idDossier }
            );
            if (docs == null || docs.isEmpty()) {
                return createFicheDR(session, idDossier);
            } else if (docs.size() > 1) {
                throw new NuxeoException("Plusieurs fiches DR trouvées pour " + idDossier + ".");
            } else {
                return docs.get(0).getAdapter(FichePresentationDR.class);
            }
        }
    }

    /**
     * Creation d'une {@link FichePresentationDR}
     *
     * @param session
     * @param idDossier
     * @return
     */
    private FichePresentationDR createFicheDR(final CoreSession session, final String idDossier) {
        DocumentModel modelDesired = session.createDocumentModel(
            "/case-management/fiche-dossier",
            idDossier + "_PDR",
            FichePresentationDRImpl.DOC_TYPE
        );

        final FichePresentationDR ficheDossier = modelDesired.getAdapter(FichePresentationDR.class);
        ficheDossier.setIdDossier(idDossier);

        modelDesired = session.createDocument(ficheDossier.getDocument());
        session.save();

        return modelDesired.getAdapter(FichePresentationDR.class);
    }

    /************************************************************
     * Gestion des Communication IE *
     ************************************************************/

    /**
     * Generation d'un identifiant de proposition pour une intervention
     * extérieur
     *
     * @return
     */
    private String generateIdDossierArticle35() {
        final Calendar date = Calendar.getInstance();
        final String annee = Integer.toString(date.get(Calendar.YEAR)).substring(2, 4);

        final UIDGeneratorService uidGeneratorService = ServiceUtil.getRequiredService(UIDGeneratorService.class);
        final UIDSequencer sequencer = uidGeneratorService.getSequencer();
        final String compteur = String.format(
            "%05d",
            sequencer.getNextLong(SOLON_MGPP_INTERV_EXT_SEQUENCER + annee) - 1
        );

        return "35" + "C" + annee + compteur;
    }

    @Override
    public FichePresentationIE createEmptyFicheIE(final EvenementDTO evenementDTO, final CoreSession session) {
        final DocumentModel modelDesired = session.createDocumentModel(
            "/case-management/fiche-dossier",
            UUID.randomUUID() + "_FIE",
            FichePresentationIE.DOC_TYPE
        );

        final FichePresentationIE fichePresentationIE = modelDesired.getAdapter(FichePresentationIE.class);

        remapFicheIEFromEvenementDTO(evenementDTO, fichePresentationIE);

        return fichePresentationIE;
    }

    @Override
    public void createFicheIE(final CoreSession session, final FichePresentationIE fichePresentationIE) {
        if (StringUtils.isBlank(fichePresentationIE.getIdentifiantProposition())) {
            fichePresentationIE.setIdentifiantProposition(generateIdDossierArticle35());
        }

        final DocumentModel doc = session.createDocument(fichePresentationIE.getDocument());
        session.save();

        // creation EppEvt36 cf 4.13.7.1
        SolonMgppServiceLocator
            .getEvenementService()
            .createEvenementEppEvt36Brouillon(session, doc.getAdapter(FichePresentationIE.class));
    }

    @Override
    public FichePresentationIE saveFicheIE(final CoreSession session, final FichePresentationIE fichePresentationIE) {
        final DocumentModel doc = session.saveDocument(fichePresentationIE.getDocument());
        session.save();
        return doc.getAdapter(FichePresentationIE.class);
    }

    @Override
    public FichePresentationIE findFicheIE(final CoreSession session, final String identifiantProposition) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(FichePresentationIE.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(FichePresentationIE.PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(FichePresentationIE.IDENTIFIANT_PROPOSITION);
        queryBuilder.append(" = ? ");

        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            FichePresentationIE.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { identifiantProposition }
        );
        if (docs == null || docs.isEmpty()) {
            return null;
        } else if (docs.size() > 1) {
            throw new NuxeoException(
                "Plusieurs fiches Intervention Exterieure trouvées pour " + identifiantProposition + "."
            );
        } else {
            return docs.get(0).getAdapter(FichePresentationIE.class);
        }
    }

    private FichePresentationIE remapFicheIEFromEvenementDTO(
        final EvenementDTO evenementDTO,
        final FichePresentationIE fichePresentationIE
    ) {
        if (evenementDTO != null) {
            fichePresentationIE.setIdentifiantProposition(evenementDTO.getIdDossier());
            fichePresentationIE.setAuteur(evenementDTO.getAuteur());
            fichePresentationIE.setObservation(evenementDTO.getCommentaire());
            fichePresentationIE.setIntitule(evenementDTO.getObjet());
        }

        fichePresentationIE.setDate(Calendar.getInstance());

        return fichePresentationIE;
    }

    /************************************************************
     * Gestion des Communication 341 *
     ************************************************************/

    /**
     * Generation d'un identifiant des resolutions d'article 34-1
     *
     * @return
     */
    private String generateIddDossierArticle34_1() {
        final Calendar date = Calendar.getInstance();
        final String annee = Integer.toString(date.get(Calendar.YEAR)).substring(2, 4);

        final UIDGeneratorService uidGeneratorService = ServiceUtil.getRequiredService(UIDGeneratorService.class);
        final UIDSequencer sequencer = uidGeneratorService.getSequencer();
        final String compteur = String.format(
            "%05d",
            sequencer.getNextLong(SOLON_MGPP_ARTICLE_34_1_SEQUENCER + annee) - 1
        );

        return "34" + "C" + annee + compteur;
    }

    @Override
    public FichePresentation341 find341(final CoreSession session, final String idDossier) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(FichePresentation341.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(FichePresentation341.PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(FichePresentation341.IDENTIFIANT_PROPOSITION);
        queryBuilder.append(" = ? ");

        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            FichePresentation341.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { idDossier }
        );
        if (docs == null || docs.isEmpty()) {
            return null;
        } else if (docs.size() > 1) {
            throw new NuxeoException("Plusieurs fiches 34-1 trouvées pour " + idDossier + ".");
        } else {
            return docs.get(0).getAdapter(FichePresentation341.class);
        }
    }

    @Override
    public FichePresentation341 saveFiche341(final CoreSession session, final FichePresentation341 fiche341) {
        final DocumentModel doc = session.saveDocument(fiche341.getDocument());
        session.save();
        return doc.getAdapter(FichePresentation341.class);
    }

    @Override
    public FichePresentation341 createFiche341(
        final CoreSession session,
        final String idEvenement,
        EvenementDTO evenementDTO
    ) {
        synchronized (this) {
            if (evenementDTO == null) {
                // recherche de l'evenement
                evenementDTO =
                    SolonMgppServiceLocator.getEvenementService().findEvenement(idEvenement, null, session, false);
            }

            final EppBaseEvenement eppBaseEvenement = ContainerBuilder
                .getInstance()
                .buildEppEvtFromEvenementDTO(evenementDTO, session);

            if (eppBaseEvenement instanceof EppEvt39) {
                final EppEvt39 eppEvt39 = (EppEvt39) eppBaseEvenement;

                String idProposition = eppEvt39.getIdDossier();
                if (StringUtils.isBlank(idProposition)) {
                    idProposition = generateIddDossierArticle34_1();
                }

                FichePresentation341 fiche341 = find341(session, idProposition);

                if (fiche341 == null) {
                    fiche341 = createFiche341(session, idProposition);
                    fiche341.setIdentifiantProposition(eppEvt39.getIdDossier());

                    if (eppEvt39.getAuteur() != null) {
                        fiche341.setAuteur(eppEvt39.getAuteur().getId());
                    }

                    if (eppEvt39.getCoAuteur() != null) {
                        final List<String> listCoAuteur = new ArrayList<>();
                        for (final Mandat mandat : eppEvt39.getCoAuteur()) {
                            listCoAuteur.add(mandat.getId());
                        }

                        fiche341.setCoAuteur(listCoAuteur);
                    }

                    if (eppEvt39.getDepot() != null) {
                        if (eppEvt39.getDepot().getDate() != null) {
                            fiche341.setDateDepot(
                                DateUtil.xmlGregorianCalendarToCalendar(eppEvt39.getDepot().getDate())
                            );
                        }

                        if (eppEvt39.getDepot().getNumero() != null) {
                            fiche341.setNumeroDepot(eppEvt39.getDepot().getNumero());
                        }
                    }

                    fiche341.setObjet(eppEvt39.getObjet());
                    fiche341.setIntitule(eppEvt39.getIntitule());

                    final DocumentModel modelDesired = session.createDocument(fiche341.getDocument());
                    session.save();

                    fiche341 = modelDesired.getAdapter(FichePresentation341.class);
                }

                return fiche341;
            } else if (eppBaseEvenement instanceof EppEvt43Bis) {
                final EppEvt43Bis eppEvt43Bis = (EppEvt43Bis) eppBaseEvenement;

                String idProposition = eppEvt43Bis.getIdDossier();
                if (StringUtils.isBlank(idProposition)) {
                    idProposition = generateIddDossierArticle34_1();
                }

                FichePresentation341 fiche341 = find341(session, idProposition);

                if (fiche341 == null) {
                    fiche341 = createFiche341(session, idProposition);
                    fiche341.setIdentifiantProposition(eppEvt43Bis.getIdDossier());

                    fiche341.setObjet(eppEvt43Bis.getObjet());
                    fiche341.setIntitule(eppEvt43Bis.getIntitule());

                    final DocumentModel modelDesired = session.createDocument(fiche341.getDocument());
                    session.save();

                    fiche341 = modelDesired.getAdapter(FichePresentation341.class);
                }

                return fiche341;
            }
        }
        return null;
    }

    private FichePresentation341 createFiche341(final CoreSession session, final String idProposition) {
        FichePresentation341 fiche341;
        final DocumentModel modelDesired = session.createDocumentModel(
            "/case-management/fiche-dossier",
            idProposition + "_F341",
            FichePresentation341.DOC_TYPE
        );

        fiche341 = modelDesired.getAdapter(FichePresentation341.class);
        return fiche341;
    }

    @Override
    public void createFiche341(final CoreSession session, final FichePresentation341 fichePresentation341) {
        session.createDocument(fichePresentation341.getDocument());
        session.save();
    }

    @SuppressWarnings("unchecked")
    @Override
    public FichePresentation341 createEmptyFiche341(final EvenementDTO evenementDTO, final CoreSession session) {
        final DocumentModel modelDesired = session.createDocumentModel(
            "/case-management/fiche-dossier",
            evenementDTO.getIdDossier() + "_F341",
            FichePresentation341.DOC_TYPE
        );

        final FichePresentation341 fiche341 = modelDesired.getAdapter(FichePresentation341.class);
        fiche341.setIdentifiantProposition(evenementDTO.getIdDossier());
        fiche341.setDate(Calendar.getInstance());

        final String auteur = evenementDTO.getAuteur();
        final Serializable coAuteur = evenementDTO.get(EvenementDTOImpl.CO_AUTEUR);

        if (coAuteur instanceof List) {
            fiche341.setCoAuteur((List<String>) coAuteur);
        }

        final String intitule = evenementDTO.getIntitule();

        final Serializable depot = evenementDTO.get(EvenementDTOImpl.DEPOT);

        Calendar dateDepot = null;
        String numeroDepot = null;

        if (depot instanceof Map) {
            final Map<String, Serializable> depotMap = (Map<String, Serializable>) depot;
            final Date date = (Date) depotMap.get(EvenementDTOImpl.DATE);
            if (date != null) {
                dateDepot = Calendar.getInstance();
                dateDepot.setTime(date);
            }

            numeroDepot = (String) depotMap.get(EvenementDTOImpl.NUMERO);
        }

        final String objet = evenementDTO.getObjet();

        fiche341.setAuteur(auteur);
        fiche341.setDateDepot(dateDepot);
        fiche341.setIntitule(intitule);
        fiche341.setNumeroDepot(numeroDepot);
        fiche341.setObjet(objet);

        return fiche341;
    }

    /************************************************************
     * Gestion des Communication OEP *
     ************************************************************/

    @Override
    public void updateListeOEPPubliee(final CoreSession session) {
        String generatedReportPath = "";
        FileOutputStream outputStream1 = null;
        try {
            final List<DocumentModel> documentModelList = getFichePresentationOEPDiffuseList(session);

            generatedReportPath = getPathDirAPPublie();

            final File htmlReport = new File(generatedReportPath + File.separator + "listeDesFichesOEP.html");
            if (!htmlReport.exists()) {
                htmlReport.createNewFile();
            }
            outputStream1 = new FileOutputStream(htmlReport);
            outputStream1.write("<table cellpadding='0' cellspacing='0'>".getBytes());

            String min1, min2, min3, idDossier, dateMAJ = "", oepTitle;
            final Map<String, List<FichePresentationOEP>> oepTm = new TreeMap<>();

            final List<FichePresentationOEP> listDesFichesOEP = new ArrayList<>();

            for (final DocumentModel documentModel : documentModelList) {
                final FichePresentationOEP adapter = documentModel.getAdapter(FichePresentationOEP.class);
                if (adapter != null) {
                    listDesFichesOEP.add(adapter);
                }
            }
            // Sort the Collection
            Collections.sort(listDesFichesOEP, new FichePresentationOEPComp());

            for (final FichePresentationOEP fpOep : listDesFichesOEP) {
                min1 = fpOep.getMinistereRattachement();
                min2 = fpOep.getMinistereRattachement2();
                min3 = fpOep.getMinistereRattachement3();

                // fill table of group by ministeres
                if (StringUtils.isNotBlank(min1)) {
                    if (!oepTm.containsKey(min1)) {
                        oepTm.put(min1, new ArrayList<>());
                    }
                    oepTm.get(min1).add(fpOep);
                }
                if (StringUtils.isNotBlank(min2)) {
                    if (!oepTm.containsKey(min2)) {
                        oepTm.put(min2, new ArrayList<>());
                    }
                    oepTm.get(min2).add(fpOep);
                }
                if (StringUtils.isNotBlank(min3)) {
                    if (!oepTm.containsKey(min3)) {
                        oepTm.put(min3, new ArrayList<>());
                    }
                    oepTm.get(min3).add(fpOep);
                }

                idDossier = fpOep.getIdDossier();
                oepTitle = fpOep.getNomOrganisme();
                if (fpOep.getDateDiffusion() != null) {
                    dateMAJ = SolonDateConverter.DATE_SLASH.format(fpOep.getDateDiffusion());
                }

                outputStream1.write(
                    (
                        "<tr><td><span onclick=\"openHtmlFile('" +
                        SolonMgppViewConstant.OEP_FICHES_PUBLIEES_PREFIX +
                        idDossier +
                        "')\" class='titreOep' >" +
                        oepTitle +
                        "</span><span class='dateOep'>dernière mise à jour le " +
                        dateMAJ +
                        "</span>" +
                        "<a href='openSaveDialog.jsp?fileName=" +
                        SolonMgppViewConstant.OEP_FICHES_PUBLIEES_PREFIX +
                        idDossier +
                        "' ><img src='pdf.png' style='border:none;cursor:pointer' alt='more' /></a></td></tr>"
                    ).getBytes()
                );
            }
            outputStream1.write("</table>".getBytes());

            outputStream1.write(constructTableMinisteres(oepTm).getBytes());

            outputStream1.write(appendTexteLibre(session).getBytes());
        } catch (final Exception e) {
            throw new NuxeoException(e);
        } finally {
            try {
                outputStream1.close();
            } catch (IOException e) {
                throw new NuxeoException("Erroe while closing outputSteeam", e);
            }
        }
    }

    public class FichePresentationOEPComp implements Comparator<FichePresentationOEP> {

        @Override
        public int compare(final FichePresentationOEP arg0, final FichePresentationOEP arg1) {
            return arg0.getNomOrganisme().toUpperCase().compareTo(arg1.getNomOrganisme().toUpperCase());
        }
    }

    private FichePresentationOEP updateDiffusionFlagFicheOEP(
        final CoreSession session,
        DocumentModel ficheOEP,
        final boolean flag
    ) {
        final FichePresentationOEP fiche = ficheOEP.getAdapter(FichePresentationOEP.class);
        fiche.setDiffuse(flag);
        // corrction bug redmin 6853
        if (flag) {
            fiche.setDateDiffusion(Calendar.getInstance());
        } else {
            fiche.setDateDiffusion(null);
        }
        // fiche.setDate(Calendar.getInstance());
        ficheOEP = session.saveDocument(fiche.getDocument());
        return ficheOEP.getAdapter(FichePresentationOEP.class);
    }

    @Override
    public List<DocumentModel> getFichePresentationOEPDiffuseList(final CoreSession session) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(FichePresentationOEPImpl.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(FichePresentationOEPImpl.PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(FichePresentationOEPImpl.DIFFUSE);
        queryBuilder.append(" = ? ");

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            FichePresentationOEPImpl.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { true }
        );
    }

    @Override
    public FichePresentationOEP findOrCreateFicheRepresentationOEP(
        final CoreSession session,
        final EvenementDTO evenementDTO
    ) {
        final FichePresentationOEP fichePresentationOEP = findFicheOEP(session, evenementDTO.getIdDossier());
        if (fichePresentationOEP == null) {
            return createFicheRepresentationOEP(session, evenementDTO, Boolean.TRUE);
        } else {
            // deja existante
            return fichePresentationOEP;
        }
    }

    @Override
    public FichePresentationOEP createFicheRepresentationOEP(
        final CoreSession session,
        final EvenementDTO evenementDTO,
        final Boolean save
    ) {
        final DocumentModel modelDesired = session.createDocumentModel(
            "/case-management/fiche-dossier",
            "FOEP-" + UUID.randomUUID(),
            FichePresentationOEPImpl.DOC_TYPE
        );

        final FichePresentationOEP fichePresentationOEP = modelDesired.getAdapter(FichePresentationOEP.class);
        fichePresentationOEP.setDate(Calendar.getInstance());
        fichePresentationOEP.setIdDossier(
            generateIdDossierOEP(evenementDTO == null ? null : evenementDTO.getIdDossier())
        );
        fichePresentationOEP.setIdCommun(evenementDTO == null ? null : evenementDTO.getIdDossier());
        fichePresentationOEP.setNomOrganisme(evenementDTO == null ? null : evenementDTO.getObjet());
        // fichePresentationOEP.addToIdsANATLies(fichePresentationOEP.getIdDossier());
        if (evenementDTO != null) {
            EppBaseEvenement eppBaseEvenement = null;
            try {
                eppBaseEvenement = ContainerBuilder.getInstance().buildEppEvtFromEvenementDTO(evenementDTO, session);
            } catch (final NuxeoException e) {
                LOGGER.error(session, MgppLogEnumImpl.FAIL_TRANS_DTO_TEC);
            }

            if (eppBaseEvenement instanceof EppEvt490) {
                final List<DocumentModel> listRepresentantAN = fetchRepresentantOEP("AN", evenementDTO, session);
                final List<DocumentModel> listRepresentantSE = fetchRepresentantOEP("SE", evenementDTO, session);

                if (save) {
                    createFicheRepresentationOEP(session, modelDesired, listRepresentantAN, listRepresentantSE);
                }
            } else if (eppBaseEvenement instanceof EppEvt49) {
                final List<DocumentModel> listRepresentantAN = fetchRepresentantOEP("AN", evenementDTO, session);
                final List<DocumentModel> listRepresentantSE = fetchRepresentantOEP("SE", evenementDTO, session);

                if (save) {
                    createFicheRepresentationOEP(session, modelDesired, listRepresentantAN, listRepresentantSE);
                }
            } else if (eppBaseEvenement instanceof EppEvt51) {
                final List<DocumentModel> listRepresentantAN = fetchRepresentantOEP("AN", evenementDTO, session);
                final List<DocumentModel> listRepresentantSE = fetchRepresentantOEP("SE", evenementDTO, session);

                if (save) {
                    createFicheRepresentationOEP(session, modelDesired, listRepresentantAN, listRepresentantSE);
                }
            }
        }
        return fichePresentationOEP;
    }

    /**
     * OEP_XXXXXXXP
     *
     * @param idCommun
     * @return
     */
    public static String generateIdDossierOEP(final String idCommun) {
        if (StringUtils.isNotBlank(idCommun)) {
            if (idCommun.startsWith(OEP_ID_START) && idCommun.endsWith(OEP_ID_END)) {
                return idCommun;
            }
            final int length = idCommun.length();
            final int nbZero = 7 - length;
            if (nbZero <= 0) {
                return OEP_ID_START + idCommun + OEP_ID_END;
            } else {
                final String idDossier = String.format("%0" + nbZero + "d", 0);
                return OEP_ID_START + idDossier + idCommun + OEP_ID_END;
            }
        }
        return "OEP_XXXXXXXP";
    }

    @Override
    public List<DocumentModel> fetchRepresentantOEP(
        final CoreSession session,
        final String typeRepresentant,
        final String idFicheOEP
    ) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(RepresentantOEPImpl.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(RepresentantOEPImpl.REPRESENTANT_PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(RepresentantOEPImpl.ID_FICHE_ROEP);
        queryBuilder.append(" = ? AND d.");
        queryBuilder.append(RepresentantOEPImpl.REPRESENTANT_PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(RepresentantOEPImpl.TYPE);
        queryBuilder.append(" = ? ");

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            RepresentantOEPImpl.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { idFicheOEP, typeRepresentant }
        );
    }

    @Override
    public FichePresentationOEP diffuserFicheOEP(final CoreSession session, final DocumentModel ficheOEP) {
        final FichePresentationOEP fiche = updateDiffusionFlagFicheOEP(session, ficheOEP, true);
        session.save();
        generateBirtReports(session, fiche.getIdDossier(), ficheOEP.getId());
        return fiche;
    }

    @Override
    public void diffuserFicheOEP(final CoreSession session, final List<DocumentModel> ficheOEPList) {
        for (final DocumentModel documentModel : ficheOEPList) {
            diffuserFicheOEP(session, documentModel);
        }
        session.save();
    }

    @Override
    public FichePresentationOEP annulerDiffusionFicheOEP(final CoreSession session, final DocumentModel ficheOEP) {
        final FichePresentationOEP fiche = updateDiffusionFlagFicheOEP(session, ficheOEP, false);
        session.save();
        deleteBirtReports(session, fiche.getIdDossier());
        LOGGER.info(session, MgppLogEnumImpl.CANCEL_REPRESENTANT_OEP_TEC, fiche);
        return fiche;
    }

    @Override
    public void annulerDiffusionFicheOEP(final CoreSession session, final List<DocumentModel> ficheOEPList) {
        for (final DocumentModel documentModel : ficheOEPList) {
            annulerDiffusionFicheOEP(session, documentModel);
        }
        session.save();
    }

    @Override
    public FichePresentationOEP findFicheOEP(final CoreSession session, final String idDossier) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(FichePresentationOEPImpl.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(FichePresentationOEPImpl.PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(FichePresentationOEPImpl.ID_DOSSIER);
        queryBuilder.append(" = ? ");

        String formattedIdDossier = generateIdDossierOEP(idDossier);
        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            FichePresentationOEPImpl.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { formattedIdDossier }
        );
        if (docs == null || docs.isEmpty()) {
            return findFicheOEPinANATLies(session, idDossier);
        } else if (docs.size() > 1) {
            FichePresentationOEP fichePresentationOEP = findFicheOEPinANATLies(session, idDossier);
            if (fichePresentationOEP == null) {
                throw new NuxeoException("Plusieurs fiches OEP trouvées pour " + idDossier + ".");
            } else {
                return fichePresentationOEP;
            }
        } else {
            return docs.get(0).getAdapter(FichePresentationOEP.class);
        }
    }

    @Override
    public FichePresentationOEP findFicheOEPinANATLies(final CoreSession session, final String idDossier) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(FichePresentationOEPImpl.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(FichePresentationOEPImpl.PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(FichePresentationOEPImpl.IDS_ANAT_LIES);
        queryBuilder.append(" LIKE ? ");

        String formattedIdDossier = "%" + idDossier + "%";
        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            FichePresentationOEPImpl.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { formattedIdDossier }
        );
        if (docs == null || docs.isEmpty()) {
            return null;
        } else if (docs.size() > 1) {
            throw new NuxeoException("Plusieurs fiches OEP trouvées pour " + idDossier + ".");
        } else {
            return docs.get(0).getAdapter(FichePresentationOEP.class);
        }
    }

    @Override
    public FichePresentationOEP findFicheOEPbyIdDossierEPP(final CoreSession session, final String idDossierEPP) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(FichePresentationOEPImpl.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(FichePresentationOEPImpl.PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(FichePresentationOEPImpl.ID_ORGANISME_EPP);
        queryBuilder.append(" = ? ");

        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            FichePresentationOEPImpl.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { idDossierEPP }
        );
        if (docs == null || docs.isEmpty()) {
            return null;
        } else if (docs.size() > 1) {
            throw new NuxeoException("Plusieurs fiches OEP trouvées pour " + idDossierEPP + ".");
        } else {
            return docs.get(0).getAdapter(FichePresentationOEP.class);
        }
    }

    @Override
    public FichePresentationOEP saveFicheOEP(
        final CoreSession session,
        DocumentModel ficheOEP,
        final List<DocumentModel> listRepresentantAN,
        final List<DocumentModel> listRepresentantSE
    ) {
        FichePresentationOEP fiche = ficheOEP.getAdapter(FichePresentationOEP.class);

        // generation idDossier a partir de idCommun
        fiche.setIdDossier(generateIdDossierOEP(fiche.getIdCommun()));
        ficheOEP = session.saveDocument(fiche.getDocument());

        session.save();

        final List<DocumentModel> docsAN = fetchRepresentantOEP(session, "AN", ficheOEP.getId());
        final List<DocumentModel> docsSE = fetchRepresentantOEP(session, "SE", ficheOEP.getId());

        final Set<String> setRestant = new HashSet<>();

        for (final DocumentModel documentModel : listRepresentantAN) {
            final RepresentantOEP representantOEP = documentModel.getAdapter(RepresentantOEP.class);
            if (StringUtils.isEmpty(documentModel.getId())) {
                documentModel.setPathInfo(ficheOEP.getPathAsString(), "REP-" + UUID.randomUUID().toString() + "-AN");
                representantOEP.setIdFicheRepresentationOEP(ficheOEP.getId());
                if (StringUtils.isBlank(representantOEP.getRepresentant())) {
                    throw new NuxeoException("Le représentant est obligatoire");
                }
                session.createDocument(representantOEP.getDocument());
            } else {
                session.saveDocument(representantOEP.getDocument());
                setRestant.add(representantOEP.getDocument().getId());
            }
        }

        for (final DocumentModel documentModel : listRepresentantSE) {
            final RepresentantOEP representantOEP = documentModel.getAdapter(RepresentantOEP.class);
            if (StringUtils.isEmpty(documentModel.getId())) {
                documentModel.setPathInfo(ficheOEP.getPathAsString(), "REP-" + UUID.randomUUID().toString() + "-SE");
                representantOEP.setIdFicheRepresentationOEP(ficheOEP.getId());
                if (StringUtils.isBlank(representantOEP.getRepresentant())) {
                    throw new NuxeoException("Le représentant est obligatoire");
                }
                session.createDocument(representantOEP.getDocument());
            } else {
                session.saveDocument(representantOEP.getDocument());
                setRestant.add(representantOEP.getDocument().getId());
            }
        }

        // delinkage
        for (final DocumentModel documentModel : docsAN) {
            if (!setRestant.contains(documentModel.getId())) {
                LOGGER.info(session, MgppLogEnumImpl.DEL_REPRESENTANT_OEP_TEC, documentModel);
                session.removeDocument(documentModel.getRef());
            }
        }

        for (final DocumentModel documentModel : docsSE) {
            if (!setRestant.contains(documentModel.getId())) {
                LOGGER.info(session, MgppLogEnumImpl.DEL_REPRESENTANT_OEP_TEC, documentModel);
                session.removeDocument(documentModel.getRef());
            }
        }

        fiche = ficheOEP.getAdapter(FichePresentationOEP.class);

        majTabRefOep(fiche, session);

        return fiche;
    }

    @Override
    public List<DocumentModel> fetchRepresentantOEP(
        final String typeRepresentant,
        final EvenementDTO evenementDTO,
        final CoreSession session
    ) {
        final List<DocumentModel> listRepresentant = new ArrayList<>();

        if (evenementDTO != null) {
            EppBaseEvenement eppBaseEvenement = null;
            try {
                eppBaseEvenement = ContainerBuilder.getInstance().buildEppEvtFromEvenementDTO(evenementDTO, session);
            } catch (final NuxeoException e) {
                LOGGER.error(session, MgppLogEnumImpl.FAIL_TRANS_DTO_TEC);
            }

            if (eppBaseEvenement instanceof EppEvt490) {
                final EppEvt490 eppEvt49 = (EppEvt490) eppBaseEvenement;
                final Date date = DateUtil.xmlGregorianCalendarToDate(eppEvt49.getHorodatage());
                final Institution emetteur = eppEvt49.getEmetteur();
                final List<Mandat> listTitulaire = eppEvt49.getTitulaires();
                final List<Mandat> listSuppleant = eppEvt49.getSuppleant();

                buildRepresentants(
                    typeRepresentant,
                    session,
                    listRepresentant,
                    date,
                    emetteur,
                    listTitulaire,
                    listSuppleant,
                    null
                );
            } else if (eppBaseEvenement instanceof EppEvt49) {
                final EppEvt49 eppEvt49 = (EppEvt49) eppBaseEvenement;
                final Institution emetteur = eppEvt49.getEmetteur();
                final Date date = DateUtil.xmlGregorianCalendarToDate(eppEvt49.getHorodatage());
                final List<Mandat> listTitulaire = eppEvt49.getTitulaires();
                final List<Mandat> listSuppleant = eppEvt49.getSuppleant();
                buildRepresentants(
                    typeRepresentant,
                    session,
                    listRepresentant,
                    date,
                    emetteur,
                    listTitulaire,
                    listSuppleant,
                    null
                );
            } else if (eppBaseEvenement instanceof EppEvt51) {
                final EppEvt51 eppEvt51 = (EppEvt51) eppBaseEvenement;
                final Date date = DateUtil.xmlGregorianCalendarToDate(eppEvt51.getHorodatage());
                Calendar dateJOCalendar = null;
                if (eppEvt51.getDateJo() != null) {
                    dateJOCalendar = DateUtil.xmlGregorianCalendarToCalendar(eppEvt51.getDateJo());
                }
                final Institution emetteur = eppEvt51.getEmetteur();
                final List<Mandat> listTitulaire = eppEvt51.getTitulaires();
                final List<Mandat> listSuppleant = eppEvt51.getSuppleant();

                buildRepresentants(
                    typeRepresentant,
                    session,
                    listRepresentant,
                    date,
                    emetteur,
                    listTitulaire,
                    listSuppleant,
                    dateJOCalendar
                );
            }
        }

        return listRepresentant;
    }

    private void buildRepresentants(
        final String typeRepresentant,
        final CoreSession session,
        final List<DocumentModel> listRepresentant,
        final Date date,
        final Institution emetteur,
        final List<Mandat> listTitulaire,
        final List<Mandat> listSuppleant,
        final Calendar dateDebut
    ) {
        if (emetteur.equals(Institution.ASSEMBLEE_NATIONALE)) {
            if (VocabularyConstants.REPRESENTANT_TYPE_AN.equals(typeRepresentant)) {
                buildListRepresentantFromMandat(
                    session,
                    listRepresentant,
                    date,
                    listTitulaire,
                    listSuppleant,
                    dateDebut
                );
            }
        } else if (emetteur.equals(Institution.SENAT)) {
            if (VocabularyConstants.REPRESENTANT_TYPE_SE.equals(typeRepresentant)) {
                buildListRepresentantFromMandat(
                    session,
                    listRepresentant,
                    date,
                    listTitulaire,
                    listSuppleant,
                    dateDebut
                );
            }
        }
    }

    private void buildListRepresentantFromMandat(
        final CoreSession session,
        final List<DocumentModel> listRepresentant,
        final Date date,
        final List<Mandat> listTitulaire,
        final List<Mandat> listSuppleant,
        final Calendar dateDebut
    ) {
        for (final Mandat mandat : listTitulaire) {
            final TableReferenceDTO tableReferenceDTO = SolonMgppServiceLocator
                .getTableReferenceService()
                .findTableReferenceByIdAndTypeWS(mandat.getId(), "Identite", date, session);
            if (tableReferenceDTO instanceof IdentiteDTO) {
                final RepresentantOEP representantOEP = buildIdentiteDTO(
                    session,
                    tableReferenceDTO,
                    mandat.getId(),
                    dateDebut
                );

                representantOEP.setFonction(VocabularyConstants.FONCTION_TITULAIRE_ID);
                representantOEP.setType(VocabularyConstants.REPRESENTANT_TYPE_SE);
                listRepresentant.add(representantOEP.getDocument());
            }
        }

        for (final Mandat mandat : listSuppleant) {
            final TableReferenceDTO tableReferenceDTO = SolonMgppServiceLocator
                .getTableReferenceService()
                .findTableReferenceByIdAndTypeWS(mandat.getId(), "Identite", date, session);
            if (tableReferenceDTO instanceof IdentiteDTO) {
                final RepresentantOEP representantOEP = buildIdentiteDTO(
                    session,
                    tableReferenceDTO,
                    mandat.getId(),
                    dateDebut
                );

                representantOEP.setFonction(VocabularyConstants.FONCTION_SUPPLEANT_ID);
                representantOEP.setType(VocabularyConstants.REPRESENTANT_TYPE_SE);
                listRepresentant.add(representantOEP.getDocument());
            }
        }
    }

    private RepresentantOEP buildIdentiteDTO(
        final CoreSession session,
        final TableReferenceDTO tableReferenceDTO,
        final String idMandat,
        final Calendar dateDebut
    ) {
        final IdentiteDTO identiteDTO = (IdentiteDTO) tableReferenceDTO;
        final DocumentModel modelDesired = session.createDocumentModel(
            "/case-management/fiche-dossier",
            "" + Calendar.getInstance().getTimeInMillis(),
            RepresentantOEPImpl.DOC_TYPE
        );

        final RepresentantOEP representantOEP = modelDesired.getAdapter(RepresentantOEP.class);

        if (dateDebut == null) {
            if (identiteDTO.getDateDebut() != null) {
                final Calendar cal = Calendar.getInstance();
                cal.setTime(identiteDTO.getDateDebut());

                representantOEP.setDateDebut(cal);
            }
        } else {
            representantOEP.setDateDebut(dateDebut);
        }

        if (identiteDTO.getDateFin() != null) {
            final Calendar cal = Calendar.getInstance();
            cal.setTime(identiteDTO.getDateFin());

            representantOEP.setDateFin(cal);
        }

        representantOEP.setRepresentant(idMandat);
        return representantOEP;
    }

    @Override
    public void createFicheRepresentationOEP(
        final CoreSession session,
        DocumentModel ficheOEP,
        final List<DocumentModel> listAN,
        final List<DocumentModel> listSE
    ) {
        final FichePresentationOEP fiche = ficheOEP.getAdapter(FichePresentationOEP.class);

        if (fiche.getDate() == null) {
            fiche.setDate(Calendar.getInstance());
        }

        // generation idDossier a partir de idCommun
        fiche.setIdDossier(generateIdDossierOEP(fiche.getIdCommun()));

        ficheOEP = session.createDocument(fiche.getDocument());

        for (final DocumentModel documentModel : listAN) {
            final RepresentantOEP representantOEP = documentModel.getAdapter(RepresentantOEP.class);
            documentModel.setPathInfo(ficheOEP.getPathAsString(), "REP-" + UUID.randomUUID().toString() + "-AN");
            representantOEP.setIdFicheRepresentationOEP(ficheOEP.getId());
            if (StringUtils.isBlank(representantOEP.getRepresentant())) {
                throw new NuxeoException("Le représentant est obligatoire");
            }
            session.createDocument(representantOEP.getDocument());
        }

        for (final DocumentModel documentModel : listSE) {
            final RepresentantOEP representantOEP = documentModel.getAdapter(RepresentantOEP.class);
            documentModel.setPathInfo(ficheOEP.getPathAsString(), "REP-" + UUID.randomUUID().toString() + "-SE");
            representantOEP.setIdFicheRepresentationOEP(ficheOEP.getId());
            if (StringUtils.isBlank(representantOEP.getRepresentant())) {
                throw new NuxeoException("Le représentant est obligatoire");
            }
            session.createDocument(representantOEP.getDocument());
        }

        majTabRefOep(fiche, session);
    }

    private void majTabRefOep(final FichePresentationOEP ficheOep, final CoreSession session) {
        WSEpp wsEpp = null;

        try {
            wsEpp = SolonMgppWsLocator.getWSEpp(session);
        } catch (final WSProxyFactoryException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
            throw new NuxeoException(e);
        }

        final MajTableRequest majTableRequest = new MajTableRequest();

        if (StringUtils.isBlank(ficheOep.getIdOrganismeEPP())) {
            majTableRequest.setAction(ActionObjetReference.AJOUTER);
        } else {
            majTableRequest.setAction(ActionObjetReference.MODIFIER);
        }

        final ObjetContainer objetContainer = new ObjetContainer();
        objetContainer.setType(ObjetType.ORGANISME);

        final Organisme organisme = new Organisme();
        organisme.setId(ficheOep.getIdOrganismeEPP());
        organisme.setNom(ficheOep.getNomOrganisme());
        organisme.setDateDebut(DateUtil.calendarToXMLGregorianCalendar(ficheOep.getDate()));
        organisme.setProprietaire(Institution.GOUVERNEMENT);
        organisme.setIdCommun(ficheOep.getIdCommun());
        organisme.setType(TypeOrganisme.OEP);
        organisme.setDateFin(DateUtil.calendarToXMLGregorianCalendar(ficheOep.getDateFin()));
        organisme.setBaseLegale(ficheOep.getTexteRef());

        objetContainer.getOrganisme().add(organisme);

        majTableRequest.setObjetContainer(objetContainer);

        MajTableResponse majTableResponse = null;
        try {
            majTableResponse = wsEpp.majTable(majTableRequest);
        } catch (final HttpTransactionException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
            throw new NuxeoException(SolonMgppWsLocator.getConnexionFailureMessage(session));
        } catch (final Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_TABLE_REFERENCE_TEC, e);
            throw new NuxeoException(e);
        }

        if (majTableResponse == null) {
            throw new NuxeoException(
                "Erreur de communication avec SOLON EPP, la mise à jour d'une table de référence a échoué."
            );
        } else if (majTableResponse.getStatut() == null || !TraitementStatut.OK.equals(majTableResponse.getStatut())) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, majTableResponse.getMessageErreur());
            throw new NuxeoException(
                "Erreur de communication avec SOLON EPP, la mise à jour d'une table de référence a échoué." +
                WSErrorHelper.buildCleanMessage(majTableResponse.getMessageErreur())
            );
        } else {
            if (
                majTableResponse.getObjetContainer() != null &&
                !majTableResponse.getObjetContainer().getOrganisme().isEmpty()
            ) {
                ficheOep.setIdOrganismeEPP(majTableResponse.getObjetContainer().getOrganisme().get(0).getId());
                session.saveDocument(ficheOep.getDocument());
                session.save();
            }
        }
    }

    /************************************************************
     * Gestion des Communication AVI *
     ************************************************************/

    @Override
    public FichePresentationAVI findFicheAVI(final CoreSession session, final String idDossier) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(FichePresentationAVI.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(FichePresentationAVI.PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(FichePresentationAVI.ID_DOSSIER);
        queryBuilder.append(" = ? ");

        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            FichePresentationAVI.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { idDossier }
        );
        if (docs == null || docs.isEmpty()) {
            return null;
        } else if (docs.size() > 1) {
            throw new NuxeoException("Plusieurs fiches AVI trouvées pour " + idDossier + ".");
        } else {
            return docs.get(0).getAdapter(FichePresentationAVI.class);
        }
    }

    @Override
    public FichePresentationAVI findOrCreateFicheAVI(final CoreSession session, final String idDossier) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(FichePresentationAVI.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(FichePresentationAVI.PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(FichePresentationAVI.ID_DOSSIER);
        queryBuilder.append(" = ? ");

        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            FichePresentationAVI.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { idDossier }
        );
        if (docs == null || docs.isEmpty()) {
            return createFicheRepresentationAVI(session, idDossier);
        } else if (docs.size() > 1) {
            throw new NuxeoException("Plusieurs fiches AVI trouvées pour " + idDossier + ".");
        } else {
            return docs.get(0).getAdapter(FichePresentationAVI.class);
        }
    }

    @Override
    public FichePresentationAVI createFicheRepresentationAVI(
        final CoreSession session,
        final EvenementDTO evenementDTO
    ) {
        final DocumentModel modelDesired = session.createDocumentModel(
            "/case-management/fiche-dossier",
            "FAVI-" + UUID.randomUUID(),
            FichePresentationAVI.DOC_TYPE
        );

        final FichePresentationAVI fichePresentationAVI = modelDesired.getAdapter(FichePresentationAVI.class);
        fichePresentationAVI.setDate(Calendar.getInstance());
        if (evenementDTO != null) {
            fichePresentationAVI.setIdDossier(evenementDTO.getIdDossier());
        }

        return fichePresentationAVI;
    }

    public FichePresentationAVI createFicheRepresentationAVI(final CoreSession session, final String idDossier) {
        final DocumentModel modelDesired = session.createDocumentModel(
            "/case-management/fiche-dossier",
            "FAVI-" + UUID.randomUUID(),
            FichePresentationAVI.DOC_TYPE
        );

        final FichePresentationAVI fichePresentationAVI = modelDesired.getAdapter(FichePresentationAVI.class);
        fichePresentationAVI.setDate(Calendar.getInstance());
        fichePresentationAVI.setIdDossier(idDossier);

        return fichePresentationAVI;
    }

    @Override
    public void createFicheRepresentationAVI(
        final CoreSession session,
        DocumentModel ficheAVI,
        final List<DocumentModel> listRepresentant
    ) {
        final FichePresentationAVI fiche = ficheAVI.getAdapter(FichePresentationAVI.class);
        fiche.setDate(Calendar.getInstance());

        ficheAVI = session.createDocument(fiche.getDocument());

        for (final DocumentModel documentModel : listRepresentant) {
            final RepresentantAVI representantAVI = documentModel.getAdapter(RepresentantAVI.class);
            documentModel.setPathInfo(ficheAVI.getPathAsString(), "NOMINE-" + UUID.randomUUID().toString() + "");
            representantAVI.setIdFicheRepresentationAVI(ficheAVI.getId());
            if (StringUtils.isBlank(representantAVI.getNomine())) {
                throw new NuxeoException("Le nominé est obligatoire");
            }
            session.createDocument(representantAVI.getDocument());
        }

        majTabRefAvi(fiche, session);
    }

    @Override
    public List<DocumentModel> fetchNomineAVI(final CoreSession session, final String idFicheAVI) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(RepresentantAVI.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(RepresentantAVI.REPRESENTANT_AVI_PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(RepresentantAVI.ID_FICHE_RAVI);
        queryBuilder.append(" = ? ");

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            RepresentantAVI.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { idFicheAVI }
        );
    }

    @Override
    public FichePresentationAVI saveFicheAVI(
        final CoreSession session,
        DocumentModel ficheAVI,
        final List<DocumentModel> listRepresentant
    ) {
        FichePresentationAVI fiche = ficheAVI.getAdapter(FichePresentationAVI.class);

        ficheAVI = session.saveDocument(fiche.getDocument());

        session.save();

        final Set<String> setRestant = new HashSet<>();

        for (final DocumentModel documentModel : listRepresentant) {
            final RepresentantAVI representantAVI = documentModel.getAdapter(RepresentantAVI.class);
            if (StringUtils.isEmpty(documentModel.getId())) {
                documentModel.setPathInfo(ficheAVI.getPathAsString(), "REP-" + UUID.randomUUID().toString() + "-AN");
                representantAVI.setIdFicheRepresentationAVI(ficheAVI.getId());
                if (StringUtils.isBlank(representantAVI.getNomine())) {
                    throw new NuxeoException("Le nominé est obligatoire");
                }
                session.createDocument(representantAVI.getDocument());
            } else {
                session.saveDocument(representantAVI.getDocument());
            }

            setRestant.add(representantAVI.getDocument().getId());
        }

        final List<DocumentModel> oldRepresentant = SolonMgppServiceLocator
            .getDossierService()
            .fetchNomineAVI(session, ficheAVI.getId());

        // delinkage
        for (final DocumentModel documentModel : oldRepresentant) {
            if (!setRestant.contains(documentModel.getId())) {
                LOGGER.info(session, MgppLogEnumImpl.DEL_REPRESENTANT_AVI_TEC, documentModel);
                session.removeDocument(documentModel.getRef());
            }
        }

        fiche = ficheAVI.getAdapter(FichePresentationAVI.class);

        majTabRefAvi(fiche, session);

        return fiche;
    }

    private void majTabRefOrg(
        final FichePresentationSupportOrganisme fiche,
        final TypeOrganisme typeOrg,
        final CoreSession session
    ) {
        WSEpp wsEpp = null;

        try {
            wsEpp = SolonMgppWsLocator.getWSEpp(session);
        } catch (final WSProxyFactoryException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
            throw new NuxeoException(e);
        }

        final MajTableRequest majTableRequest = new MajTableRequest();

        if (StringUtils.isBlank(fiche.getIdOrganismeEPP())) {
            majTableRequest.setAction(ActionObjetReference.AJOUTER);
        } else {
            majTableRequest.setAction(ActionObjetReference.MODIFIER);
        }

        final ObjetContainer objetContainer = new ObjetContainer();
        objetContainer.setType(ObjetType.ORGANISME);

        final Organisme organisme = new Organisme();
        organisme.setId(fiche.getIdOrganismeEPP());
        organisme.setNom(fiche.getNomOrganisme());
        Calendar dateDebut = fiche.getDate() == null ? Calendar.getInstance() : fiche.getDate();
        organisme.setDateDebut(DateUtil.calendarToXMLGregorianCalendar(dateDebut));
        organisme.setProprietaire(Institution.GOUVERNEMENT);
        organisme.setType(typeOrg);
        organisme.setDateFin(DateUtil.calendarToXMLGregorianCalendar(fiche.getDateFin()));
        organisme.setBaseLegale(fiche.getBaseLegale());

        objetContainer.getOrganisme().add(organisme);

        majTableRequest.setObjetContainer(objetContainer);

        MajTableResponse majTableResponse = null;
        try {
            majTableResponse = wsEpp.majTable(majTableRequest);
        } catch (final HttpTransactionException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
            throw new NuxeoException(SolonMgppWsLocator.getConnexionFailureMessage(session));
        } catch (final Exception e) {
            LOGGER.error(session, STLogEnumImpl.FAIL_UPDATE_TABLE_REFERENCE_TEC, e);
            throw new NuxeoException(e);
        }

        if (majTableResponse == null) {
            throw new NuxeoException(
                "Erreur de communication avec SOLON EPP, la mise à jour d'une table de référence a échoué."
            );
        } else if (majTableResponse.getStatut() == null || !TraitementStatut.OK.equals(majTableResponse.getStatut())) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, majTableResponse.getMessageErreur());
            throw new NuxeoException(
                "Erreur de communication avec SOLON EPP, la mise à jour d'une table de référence a échoué." +
                WSErrorHelper.buildCleanMessage(majTableResponse.getMessageErreur())
            );
        } else {
            if (
                majTableResponse.getObjetContainer() != null &&
                !majTableResponse.getObjetContainer().getOrganisme().isEmpty()
            ) {
                fiche.setIdOrganismeEPP(majTableResponse.getObjetContainer().getOrganisme().get(0).getId());
                session.saveDocument(fiche.getDocument());
                session.save();
            }
        }
    }

    private void majTabRefAvi(final FichePresentationAVI ficheAvi, final CoreSession session) {
        this.majTabRefOrg(ficheAvi, TypeOrganisme.NOMINATION, session);
    }

    private void majTabRefAud(final FichePresentationAUD ficheAud, final CoreSession session) {
        this.majTabRefOrg(ficheAud, TypeOrganisme.AUDITION, session);
    }

    /************************************************************
     * Gestion des Communications Decret *
     ************************************************************/

    @Override
    public void notifierDecret(final String numeroNor, final CoreSession session, final Boolean actif) {
        final FichePresentationDecret fichePresentationDecret = findOrCreateFicheDecret(session, numeroNor);
        fichePresentationDecret.setActif(actif);
        session.saveDocument(fichePresentationDecret.getDocument());
    }

    @Override
    public FichePresentationDecret findOrCreateFicheDecret(final CoreSession session, final String nor) {
        synchronized (this) {
            final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
            queryBuilder.append(FichePresentationDecretImpl.DOC_TYPE);
            queryBuilder.append(" as d WHERE d.");
            queryBuilder.append(FichePresentationDecretImpl.PREFIX);
            queryBuilder.append(":");
            queryBuilder.append(FichePresentationDecretImpl.NOR);
            queryBuilder.append(" = ? ");

            final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
                session,
                FichePresentationDecretImpl.DOC_TYPE,
                queryBuilder.toString(),
                new Object[] { nor }
            );
            if (docs == null || docs.isEmpty()) {
                return createFicheDecret(session, nor);
            } else if (docs.size() > 1) {
                throw new NuxeoException("Plusieurs fiches décrets trouvées pour " + nor + ".");
            } else {
                return docs.get(0).getAdapter(FichePresentationDecret.class);
            }
        }
    }

    private FichePresentationDecret createFicheDecret(final CoreSession session, final String nor) {
        DocumentModel modelDesired = session.createDocumentModel(
            "/case-management/fiche-dossier",
            nor + "_FD",
            FichePresentationDecretImpl.DOC_TYPE
        );

        final FichePresentationDecret fichePresentationDecret = modelDesired.getAdapter(FichePresentationDecret.class);
        fichePresentationDecret.setNor(nor);

        modelDesired = session.createDocument(fichePresentationDecret.getDocument());
        session.save();

        return modelDesired.getAdapter(FichePresentationDecret.class);
    }

    @Override
    public FichePresentationDecret saveFicheDecret(final CoreSession session, DocumentModel ficheDecret) {
        ficheDecret = session.saveDocument(ficheDecret);
        session.save();
        return ficheDecret.getAdapter(FichePresentationDecret.class);
    }

    protected WSEvenement getWsEvenement(final CoreSession session) {
        WSEvenement wsEvenement = null;
        try {
            wsEvenement = SolonMgppWsLocator.getWSEvenement(session);
        } catch (final WSProxyFactoryException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_CON_WS_TEC, e);
            throw new NuxeoException(e);
        }
        if (wsEvenement == null) {
            throw new NuxeoException("Can't get evenement - WsEvenement is null");
        }
        return wsEvenement;
    }

    protected ChercherEvenementResponse getEvenementResponse(
        final CoreSession session,
        final ChercherEvenementRequest request
    ) {
        final WSEvenement wsEvenement = getWsEvenement(session);
        ChercherEvenementResponse chercherEvenementResponse = null;
        try {
            chercherEvenementResponse = wsEvenement.chercherEvenement(request);
        } catch (final HttpTransactionException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
            throw new NuxeoException(SolonMgppWsLocator.getConnexionFailureMessage(session));
        } catch (final Exception e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, e);
            throw new NuxeoException(e);
        }

        if (chercherEvenementResponse == null) {
            throw new NuxeoException(
                "Erreur de communication avec SOLON EPP, la récupération de la communication a échoué."
            );
        } else if (
            chercherEvenementResponse.getStatut() == null ||
            !TraitementStatut.OK.equals(chercherEvenementResponse.getStatut())
        ) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_WS_TEC, chercherEvenementResponse.getMessageErreur());

            throw new NuxeoException(
                "Erreur de communication avec SOLON EPP, la récupération de la communication a échoué." +
                WSErrorHelper.buildCleanMessage(chercherEvenementResponse.getMessageErreur())
            );
        }

        if (chercherEvenementResponse.getEvenement().size() > 1) {
            throw new NuxeoException(
                "La récupération de la version courante de la communication a retourné plusieurs résultats."
            );
        }

        return chercherEvenementResponse;
    }

    protected FicheLoi getUnlockedFicheLoi(final CoreSession session, final String idDossier) {
        final STLockService lockService = STServiceLocator.getSTLockService();
        final FicheLoi fiche = findOrCreateFicheLoi(session, idDossier);
        lockService.unlockDocUnrestricted(session, fiche.getDocument());
        return fiche;
    }

    @Override
    public void updateFicheLoi(final CoreSession session, final String idEvenement, final EvenementType typeEvenement) {
        // On ne met à jour la FL que pour les EVT correspondants
        if (ficheLoiEvenements.contains(typeEvenement)) {
            final ChercherEvenementRequest chercherEvenementRequest = new ChercherEvenementRequest();
            final EvtId evtId = new EvtId();
            evtId.setId(idEvenement);

            chercherEvenementRequest.getIdEvenement().add(evtId);
            final ChercherEvenementResponse chercherEvenementResponse = getEvenementResponse(
                session,
                chercherEvenementRequest
            );

            for (final EppEvtContainer eppEvtContainer : chercherEvenementResponse.getEvenement()) {
                if (EvenementType.EVT_01.equals(typeEvenement)) {
                    final EppEvt01 eppEvt01 = eppEvtContainer.getEvt01();
                    if (eppEvt01 != null) {
                        mapFicheLoiWithData(
                            session,
                            eppEvt01.getIdDossier(),
                            null,
                            null,
                            eppEvt01.getIntitule(),
                            eppEvt01.getNatureLoi()
                        );
                        break;
                    }
                } else if (EvenementType.EVT_02.equals(typeEvenement)) {
                    final EppEvt02 eppEvt02 = eppEvtContainer.getEvt02();
                    if (eppEvt02 != null) {
                        mapFicheLoiWithData(
                            session,
                            eppEvt02.getIdDossier(),
                            eppEvt02.getEmetteur(),
                            eppEvt02.getDepot(),
                            eppEvt02.getIntitule(),
                            eppEvt02.getNatureLoi()
                        );
                        break;
                    }
                } else if (EvenementType.EVT_10.equals(typeEvenement)) {
                    // Maj procédure accélérée
                    final EppEvt10 eppEvt10 = eppEvtContainer.getEvt10();
                    if (eppEvt10 != null) {
                        final FicheLoi fiche = getUnlockedFicheLoi(session, eppEvt10.getIdDossier());
                        Calendar dateProcAcc = DateUtil.xmlGregorianCalendarToCalendar(
                            eppEvt10.getDateEngagementProcedure()
                        );
                        fiche.setProcedureAcceleree(dateProcAcc);
                        computeDatePromulgationFicheLoi(fiche);

                        session.saveDocument(fiche.getDocument());
                        session.save();
                        break;
                    }
                } else if (EvenementType.EVT_11.equals(typeEvenement)) {
                    final EppEvt11 eppEvt11 = eppEvtContainer.getEvt11();
                    if (eppEvt11 != null) {
                        final FicheLoi fiche = getUnlockedFicheLoi(session, eppEvt11.getIdDossier());
                        fiche.setRefusEngagementProcAss1(eppEvt11.getEmetteur().value());
                        fiche.setDateRefusEngProcAss1(
                            DateUtil.xmlGregorianCalendarToCalendar(eppEvt11.getDateRefusEngagementProcedure())
                        );
                        session.saveDocument(fiche.getDocument());
                        session.save();
                        break;
                    }
                } else if (EvenementType.EVT_24.equals(typeEvenement)) {
                    // Maj dateAdoption
                    final EppEvt24 eppEvt24 = eppEvtContainer.getEvt24();
                    if (eppEvt24 != null) {
                        final FicheLoi fiche = getUnlockedFicheLoi(session, eppEvt24.getIdDossier());
                        fiche.setDateAdoption(DateUtil.xmlGregorianCalendarToCalendar(eppEvt24.getDateAdoption()));

                        session.saveDocument(fiche.getDocument());
                        session.save();
                        break;
                    }
                } else if (EvenementType.LEX_40.equals(typeEvenement)) {
                    final EppLex40 eppLex40 = eppEvtContainer.getLex40();
                    if (eppLex40 != null) {
                        final FicheLoi fiche = getUnlockedFicheLoi(session, eppLex40.getIdDossier());
                        fiche.setRefusEngagementProcAss2(eppLex40.getEmetteur().value());
                        fiche.setDecisionEngagementAssemblee2(eppLex40.getDecisionProcAcc().value());
                        session.saveDocument(fiche.getDocument());
                        session.save();
                        break;
                    }
                }
            }
        }
    }

    private void computeDatePromulgationFicheLoi(final FicheLoi ficheDossier) {
        final Calendar calSaisine = ficheDossier.getDateSaisieCC();
        final Calendar calDecision = ficheDossier.getDateDecision();
        final Calendar calReception = ficheDossier.getDateReception();

        Calendar calPromulgation = null;

        if (calSaisine == null || calDecision == null || calReception == null) {
            if (calReception != null) {
                // calPromulgation = calReception + 15j
                calPromulgation = Calendar.getInstance();
                calPromulgation.setTime(calReception.getTime());
                calPromulgation.add(Calendar.DAY_OF_MONTH, 15);

                ficheDossier.setDateLimitePromulgation(calPromulgation);
            }
        } else {
            // calPromulgation = calReception + 15j + (calDecision - calSaisine)
            calPromulgation = Calendar.getInstance();
            calPromulgation.setTime(calReception.getTime());
            calPromulgation.add(Calendar.DAY_OF_MONTH, 15);

            final long diff = calDecision.getTimeInMillis() - calSaisine.getTimeInMillis();
            final Double result = diff / 1000.0 / 60.0 / 60.0 / 24.0;
            calPromulgation.add(Calendar.DAY_OF_MONTH, (int) Math.round(result));

            ficheDossier.setDateLimitePromulgation(calPromulgation);
        }
    }

    @Override
    public FichePresentationDecret findFicheDecret(final CoreSession session, final String idDossier) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(FichePresentationDecretImpl.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(FichePresentationDecretImpl.PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(FichePresentationDecretImpl.NOR);
        queryBuilder.append(" = ? ");

        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            FichePresentationDecretImpl.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { idDossier }
        );
        if (docs == null || docs.isEmpty()) {
            return null;
        } else if (docs.size() > 1) {
            throw new NuxeoException("Plusieurs fiches DR trouvées pour " + idDossier + ".");
        } else {
            return docs.get(0).getAdapter(FichePresentationDecret.class);
        }
    }

    /************************************************************
     * Gestion des Communications AUD *
     ************************************************************/

    private String generateIddDossierAUD() {
        final UIDGeneratorService uidGeneratorService = ServiceUtil.getRequiredService(UIDGeneratorService.class);
        final UIDSequencer sequencer = uidGeneratorService.getSequencer();
        final String compteur = String.format("%04d", sequencer.getNextLong(SOLON_MGPP_AUD_SEQUENCER) - 1);

        return "AUD" + compteur;
    }

    @Override
    public FichePresentationAUD findFicheAUD(final CoreSession session, final String idDossier) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(FichePresentationAUD.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(FichePresentationAUD.PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(FichePresentationAUD.ID_DOSSIER);
        queryBuilder.append(" = ? ");

        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            FichePresentationAUD.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { idDossier }
        );
        if (docs == null || docs.isEmpty()) {
            return null;
        } else if (docs.size() > 1) {
            throw new NuxeoException("Plusieurs fiches AUD trouvées pour " + idDossier + ".");
        } else {
            return docs.get(0).getAdapter(FichePresentationAUD.class);
        }
    }

    @Override
    public FichePresentationAUD createFicheRepresentationAUD(
        final CoreSession session,
        final EvenementDTO evenementDTO
    ) {
        final String id = evenementDTO != null ? evenementDTO.getIdDossier() : this.generateIddDossierAUD();
        return createFicheAUD(session, id);
    }

    private FichePresentationAUD createFicheAUD(CoreSession session, final String idDossier) {
        DocumentModel modelDesired = session.createDocumentModel(
            "/case-management/fiche-dossier",
            idDossier,
            FichePresentationAUD.DOC_TYPE
        );
        final FichePresentationAUD ficheDossier = modelDesired.getAdapter(FichePresentationAUD.class);
        ficheDossier.setIdDossier(idDossier);

        modelDesired = session.createDocument(ficheDossier.getDocument());
        session.save();

        return modelDesired.getAdapter(FichePresentationAUD.class);
    }

    @Override
    public void createFicheRepresentationAUD(
        final CoreSession session,
        DocumentModel ficheAUD,
        final List<DocumentModel> listRepresentant
    ) {
        final FichePresentationAUD fiche = ficheAUD.getAdapter(FichePresentationAUD.class);
        fiche.setDate(Calendar.getInstance());

        // ficheAUD = session.createDocument(fiche.getDocument());

        for (final DocumentModel documentModel : listRepresentant) {
            final RepresentantAUD representantAUD = documentModel.getAdapter(RepresentantAUD.class);
            documentModel.setPathInfo(ficheAUD.getPathAsString(), "AUD-" + UUID.randomUUID().toString() + "");
            representantAUD.setIdFicheRepresentationAUD(ficheAUD.getId());
            if (StringUtils.isBlank(representantAUD.getPersonne())) {
                throw new NuxeoException("La personne est obligatoire");
            }
            session.createDocument(representantAUD.getDocument());
        }

        majTabRefAud(fiche, session);
    }

    @Override
    public List<DocumentModel> fetchPersonneAUD(final CoreSession session, final String id) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(RepresentantAUDImpl.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(RepresentantAUDImpl.REPRESENTANT_AUD_PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(RepresentantAUDImpl.ID_FICHE_RAUD);
        queryBuilder.append(" = ? ");

        return QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            RepresentantAUDImpl.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { id }
        );
    }

    @Override
    public FichePresentationAUD saveFicheAUD(
        final CoreSession session,
        DocumentModel ficheAUD,
        final List<DocumentModel> listRepresentant
    ) {
        FichePresentationAUD fiche = ficheAUD.getAdapter(FichePresentationAUD.class);

        ficheAUD = session.saveDocument(fiche.getDocument());

        final Set<String> setRestant = new HashSet<>();

        for (final DocumentModel documentModel : listRepresentant) {
            final RepresentantAUD representantAUD = documentModel.getAdapter(RepresentantAUD.class);
            if (StringUtils.isEmpty(documentModel.getId())) {
                documentModel.setPathInfo(ficheAUD.getPathAsString(), "REP-" + UUID.randomUUID().toString() + "-AUD");
                representantAUD.setIdFicheRepresentationAUD(ficheAUD.getId());
                if (StringUtils.isBlank(representantAUD.getPersonne())) {
                    throw new NuxeoException("La Personne est obligatoire");
                }
                session.createDocument(representantAUD.getDocument());
            } else {
                session.saveDocument(representantAUD.getDocument());
            }

            setRestant.add(representantAUD.getDocument().getId());
        }

        final List<DocumentModel> oldRepresentant = SolonMgppServiceLocator
            .getDossierService()
            .fetchPersonneAUD(session, ficheAUD.getId());

        for (final DocumentModel documentModel : oldRepresentant) {
            if (!setRestant.contains(documentModel.getId())) {
                LOGGER.info(session, MgppLogEnumImpl.DEL_REPRESENTANT_AUD_TEC, documentModel);
                session.removeDocument(documentModel.getRef());
            }
        }
        session.save();

        fiche = ficheAUD.getAdapter(FichePresentationAUD.class);

        majTabRefAud(fiche, session);

        return fiche;
    }

    @Override
    public FichePresentationAUD findOrCreateFicheAUD(CoreSession session, String idDossier) {
        FichePresentationAUD fichePresentationAUD = this.findFicheAUD(session, idDossier);
        if (fichePresentationAUD == null) {
            fichePresentationAUD = this.createFicheAUD(session, idDossier);
        }
        return fichePresentationAUD;
    }

    /************************************************************
     * Gestion des Communications PG *
     ************************************************************/

    /**
     * Creation d'une {@link FichePresentationDPG}
     *
     * @param session
     * @param idDossier
     * @return
     */
    private FichePresentationDPG createFicheDPG(final CoreSession session, final String idDossier) {
        DocumentModel modelDesired = session.createDocumentModel(
            "/case-management/fiche-dossier",
            idDossier,
            FichePresentationDPG.DOC_TYPE
        );
        final FichePresentationDPG ficheDossier = modelDesired.getAdapter(FichePresentationDPG.class);
        ficheDossier.setIdDossier(idDossier);

        modelDesired = session.createDocument(ficheDossier.getDocument());
        session.save();

        return modelDesired.getAdapter(FichePresentationDPG.class);
    }

    @Override
    public FichePresentationDPG findFichePresentationDPG(final CoreSession session, final String idDossier) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(FichePresentationDPG.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(FichePresentationDPG.PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(FichePresentationDRImpl.ID_DOSSIER);
        queryBuilder.append(" = ? ");

        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            FichePresentationDPG.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { idDossier }
        );
        if (docs == null || docs.isEmpty()) {
            return null;
        } else if (docs.size() > 1) {
            throw new NuxeoException("Plusieurs fiches DPG trouvées pour " + idDossier + ".");
        } else {
            return docs.get(0).getAdapter(FichePresentationDPG.class);
        }
    }

    @Override
    public FichePresentationDPG findOrCreateFicheDPG(final CoreSession session, final String idDossier) {
        FichePresentationDPG fichePresentationDPG = this.findFichePresentationDPG(session, idDossier);
        if (fichePresentationDPG == null) {
            fichePresentationDPG = this.createFicheDPG(session, idDossier);
        }
        return fichePresentationDPG;
    }

    @Override
    public FichePresentationDPG saveFicheDPG(final CoreSession session, DocumentModel ficheDPG) {
        ficheDPG = session.saveDocument(ficheDPG);
        final FichePresentationDPG fiche = ficheDPG.getAdapter(FichePresentationDPG.class);
        LOGGER.info(session, MgppLogEnumImpl.UPDATE_FICHE_PRESENTATION_DPG_TEC);
        return fiche;
    }

    /************************************************************
     * Gestion des Communications JSS *
     ************************************************************/

    /**
     * Creation d'une {@link FichePresentationJSS}
     *
     * @param session
     * @param idDossier
     * @return
     */
    private FichePresentationJSS createFicheJSS(final CoreSession session, final String idDossier) {
        DocumentModel modelDesired = session.createDocumentModel(
            "/case-management/fiche-dossier",
            idDossier,
            FichePresentationJSS.DOC_TYPE
        );
        final FichePresentationJSS ficheDossier = modelDesired.getAdapter(FichePresentationJSS.class);
        ficheDossier.setIdDossier(idDossier);

        modelDesired = session.createDocument(ficheDossier.getDocument());
        session.save();

        return modelDesired.getAdapter(FichePresentationJSS.class);
    }

    @Override
    public FichePresentationJSS findOrCreateFicheJSS(final CoreSession session, final String idDossier) {
        FichePresentationJSS fichePresentationJSS = this.findFichePresentationJSS(session, idDossier);
        if (fichePresentationJSS == null) {
            fichePresentationJSS = this.createFicheJSS(session, idDossier);
        }
        return fichePresentationJSS;
    }

    @Override
    public FichePresentationJSS saveFicheJSS(final CoreSession session, DocumentModel ficheJSS) {
        ficheJSS = session.saveDocument(ficheJSS);
        final FichePresentationJSS fiche = ficheJSS.getAdapter(FichePresentationJSS.class);
        LOGGER.info(session, MgppLogEnumImpl.UPDATE_FICHE_PRESENTATION_JSS_TEC);
        return fiche;
    }

    @Override
    public FichePresentationJSS findFichePresentationJSS(final CoreSession session, final String idDossier) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(FichePresentationJSS.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(FichePresentationJSS.PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(FichePresentationJSS.ID_DOSSIER);
        queryBuilder.append(" = ? ");

        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            FichePresentationJSS.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { idDossier }
        );
        if (docs == null || docs.isEmpty()) {
            return null;
        } else if (docs.size() > 1) {
            throw new NuxeoException("Plusieurs fiches JSS trouvées pour " + idDossier + ".");
        } else {
            return docs.get(0).getAdapter(FichePresentationJSS.class);
        }
    }

    /************************************************************
     * Gestion des Communications SD *
     ************************************************************/

    @Override
    public FichePresentationSD findFichePresentationSD(final CoreSession session, final String idDossier) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(FichePresentationSD.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(FichePresentationSD.PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(FichePresentationSD.ID_DOSSIER);
        queryBuilder.append(" = ? ");

        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            FichePresentationSD.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { idDossier }
        );
        if (docs == null || docs.isEmpty()) {
            return null;
        } else if (docs.size() > 1) {
            throw new NuxeoException("Plusieurs fiches SD trouvées pour " + idDossier + ".");
        } else {
            return docs.get(0).getAdapter(FichePresentationSD.class);
        }
    }

    @Override
    public FichePresentationSD findOrCreateFicheSD(final CoreSession session, final String idDossier) {
        FichePresentationSD fichePresentationSD = this.findFichePresentationSD(session, idDossier);
        if (fichePresentationSD == null) {
            fichePresentationSD = this.createFicheSD(session, idDossier);
        }
        return fichePresentationSD;
    }

    /**
     * Creation d'une {@link FichePresentationSD}
     *
     * @param session
     * @param idDossier
     * @return
     */
    private FichePresentationSD createFicheSD(final CoreSession session, final String idDossier) {
        DocumentModel modelDesired = session.createDocumentModel(
            "/case-management/fiche-dossier",
            idDossier,
            FichePresentationSD.DOC_TYPE
        );
        EvenementDTO evenementDTO = SolonMgppServiceLocator
            .getEvenementService()
            .findEvenement(idDossier, "_0000", session, false, false);
        final FichePresentationSD ficheDossier = modelDesired.getAdapter(FichePresentationSD.class);
        ficheDossier.setIdDossier(idDossier);

        modelDesired = session.createDocument(ficheDossier.getDocument());
        if (evenementDTO != null) { // si on retrouve l'événement alors on set
            // les données qu'il faut.
            modelDesired.setProperty(FichePresentationSD.SCHEMA, FichePresentationSD.OBJET, evenementDTO.getObjet());
        }
        session.save();

        return modelDesired.getAdapter(FichePresentationSD.class);
    }

    @Override
    public FichePresentationSD saveFicheSD(final CoreSession session, DocumentModel ficheSD) {
        ficheSD = session.saveDocument(ficheSD);
        final FichePresentationSD fiche = ficheSD.getAdapter(FichePresentationSD.class);
        LOGGER.info(session, MgppLogEnumImpl.UPDATE_FICHE_PRESENTATION_SD_TEC);
        return fiche;
    }

    /************************************************************
     * Gestion des Communications DOC *
     ************************************************************/

    @Override
    public FichePresentationDOC findOrCreateFicheDOC(CoreSession session, String idDossier) {
        FichePresentationDOC fichePresentationDOC = this.findFicheDOC(session, idDossier);
        if (fichePresentationDOC == null) {
            fichePresentationDOC = this.createFicheDoc(session, idDossier);
        }
        return fichePresentationDOC;
    }

    @Override
    public FichePresentationDOC findFicheDOC(final CoreSession session, final String idDossier) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(FichePresentationDOC.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(FichePresentationDOC.PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(FichePresentationDOC.ID_DOSSIER);
        queryBuilder.append(" = ? ");

        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            FichePresentationDOC.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { idDossier }
        );
        if (docs == null || docs.isEmpty()) {
            return null;
        } else if (docs.size() > 1) {
            throw new NuxeoException("Plusieurs fiches DOC trouvées pour " + idDossier + ".");
        } else {
            return docs.get(0).getAdapter(FichePresentationDOC.class);
        }
    }

    @Override
    public FichePresentationDOC createFicheRepresentationDOC(
        final CoreSession session,
        final EvenementDTO evenementDTO
    ) {
        final String id = evenementDTO != null
            ? evenementDTO.getIdDossier()
            : SolonMgppServiceLocator.getEvenementService().generateIdDossierDOC();
        return createFicheDocModel(session, id).getAdapter(FichePresentationDOC.class);
    }

    @Override
    public FichePresentationDOC createFicheDoc(final CoreSession session, final String idDossier) {
        DocumentModel modelDesired = session.createDocument(createFicheDocModel(session, idDossier));
        session.save();

        return modelDesired.getAdapter(FichePresentationDOC.class);
    }

    private DocumentModel createFicheDocModel(final CoreSession session, final String idDossier) {
        DocumentModel modelDesired = session.createDocumentModel(
            "/case-management/fiche-dossier",
            idDossier,
            FichePresentationDOC.DOC_TYPE
        );
        final FichePresentationDOC ficheDossier = modelDesired.getAdapter(FichePresentationDOC.class);
        ficheDossier.setIdDossier(idDossier);

        return modelDesired;
    }

    @Override
    public void createFicheRepresentationDOC(final CoreSession session, DocumentModel ficheDOC) {
        final FichePresentationDOC fiche = ficheDOC.getAdapter(FichePresentationDOC.class);
        ficheDOC = session.createDocument(fiche.getDocument());
    }

    @Override
    public FichePresentationDOC saveFicheDOC(final CoreSession session, DocumentModel ficheDOC) {
        final FichePresentationDOC fiche = ficheDOC.getAdapter(FichePresentationDOC.class);
        ficheDOC = session.saveDocument(fiche.getDocument());
        LOGGER.info(session, MgppLogEnumImpl.UPDATE_FICHE_PRESENTATION_DOC_TEC);
        return fiche;
    }

    @Override
    public DocumentModel findFicheLoiDocumentFromNor(CoreSession session, String numeroNor) {
        final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
        queryBuilder.append(FicheLoi.DOC_TYPE);
        queryBuilder.append(" as d WHERE d.");
        queryBuilder.append(FicheLoi.PREFIX);
        queryBuilder.append(":");
        queryBuilder.append(FicheLoi.NUMERO_NOR);
        queryBuilder.append(" = ? ");
        final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(
            session,
            FicheLoi.DOC_TYPE,
            queryBuilder.toString(),
            new Object[] { numeroNor }
        );
        DocumentModel ficheDoc = null;
        if (docs != null) {
            if (docs.size() == 1) {
                ficheDoc = docs.get(0);
            } else if (docs.size() == 2) {
                LOGGER.warn(session, MgppLogEnumImpl.GET_FICHE_LOI_TEC);
                FicheLoi fiche = mergeFiches(session, docs);
                ficheDoc = fiche.getDocument();
            } else if (docs.size() > 2) {
                throw new NuxeoException("Plusieurs fiches lois trouvées pour " + numeroNor + ".");
            }
        }
        return ficheDoc;
    }
}
