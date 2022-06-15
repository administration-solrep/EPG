package fr.dila.solonepg.ui.services.pan.impl;

import static fr.dila.solonepg.ui.enums.pan.PanContextDataKey.FIRST_TABLE_ELT_DTO;
import static fr.dila.solonepg.ui.enums.pan.PanContextDataKey.FIRST_TABLE_ID;
import static fr.dila.solonepg.ui.enums.pan.PanContextDataKey.FIRST_TABLE_LIST;
import static fr.dila.solonepg.ui.enums.pan.PanContextDataKey.MESURE_APPLICATIVE_NUMBER;
import static fr.dila.solonepg.ui.enums.pan.PanContextDataKey.RELOAD_FROM_DOSSIER;
import static fr.dila.solonepg.ui.enums.pan.PanContextDataKey.SECOND_TABLE_ELT_DTO;
import static fr.dila.solonepg.ui.enums.pan.PanContextDataKey.SECOND_TABLE_MAP;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.dto.DecretDTO;
import fr.dila.solonepg.api.dto.MesureApplicativeDTO;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.core.dto.activitenormative.DecretDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.MesureApplicativeDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreLoiDTOImpl;
import fr.dila.solonepg.core.exception.ActiviteNormativeException;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.pan.DecretsPanUnsortedList;
import fr.dila.solonepg.ui.bean.pan.MesuresApplicativesPanUnsortedList;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.services.pan.TexteMaitreUIService;
import fr.dila.solonepg.ui.th.model.bean.pan.DecretForm;
import fr.dila.solonepg.ui.th.model.bean.pan.MesureForm;
import fr.dila.solonepg.ui.th.model.bean.pan.TexteMaitreLoiForm;
import fr.dila.st.api.constant.STVocabularyConstants;
import fr.dila.st.api.domain.STDomainObject;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;

/**
 * Bean Seam de gestion du texte maitre (loi) de l'activite normative
 *
 * @author asatre
 */
public class TexteMaitreUIServiceImpl implements TexteMaitreUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(TexteMaitreUIServiceImpl.class);

    @Override
    public String getLienLegifranceFromJORF(String jorfLegifrance) {
        return SolonEpgServiceLocator.getActiviteNormativeService().createLienJORFLegifrance(jorfLegifrance);
    }

    private TexteMaitre getTexteMaitreFromContext(SpecificContext context) {
        return context.getCurrentDocument().getAdapter(TexteMaitre.class);
    }

    private MesureApplicative getMesureApplicativeFromContext(SpecificContext context) {
        DocumentRef texteMaitreRef = new IdRef(context.getFromContextData(FIRST_TABLE_ID));
        DocumentModel currentDocument = context.getSession().getDocument(texteMaitreRef);

        return currentDocument.getAdapter(MesureApplicative.class);
    }

    @Override
    public void saveTexteMaitre(SpecificContext context) {
        TexteMaitre texteMaitre = getTexteMaitreFromContext(context);
        CoreSession session = context.getSession();
        TexteMaitreLoiDTOImpl currentTexteMaitre = updateTexteMaitreLoiDTO(context);

        currentTexteMaitre.remapField(texteMaitre, session);

        final ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();
        panService.saveTexteMaitre(texteMaitre, session);

        panService.updateLoiListePubliee(session, true);

        // Ajout dans le journal du PAN
        ActiviteNormative activiteNormative = texteMaitre.getAdapter(ActiviteNormative.class);
        DocumentModel dossierDoc = SolonEpgServiceLocator
            .getNORService()
            .getDossierFromNOR(session, texteMaitre.getNumeroNor());
        String logCategory = "";
        if ("1".equals(activiteNormative.getApplicationLoi())) {
            logCategory = SolonEpgEventConstant.CATEGORY_LOG_PAN_APPLICATION_LOIS;
        } else if ("1".equals(activiteNormative.getApplicationOrdonnance())) {
            logCategory = SolonEpgEventConstant.CATEGORY_LOG_PAN_APPLICATION_ORDO;
        } else if ("1".equals(activiteNormative.getOrdonnance38C())) {
            logCategory = SolonEpgEventConstant.CATEGORY_LOG_PAN_SUIVI_HABILITATIONS;
        }
        STServiceLocator
            .getJournalService()
            .journaliserActionPAN(
                session,
                dossierDoc.getRef().toString(),
                SolonEpgEventConstant.MODIF_TM_EVENT,
                SolonEpgEventConstant.MODIF_TM_COMMENT,
                logCategory
            );
    }

    private TexteMaitreLoiDTOImpl updateTexteMaitreLoiDTO(SpecificContext context) {
        TexteMaitreLoiDTOImpl texteMaitreLoiDTO = context.getFromContextData(PanContextDataKey.TEXTE_MAITRE_DTO);
        TexteMaitreLoiForm texteMaitreForm = context.getFromContextData(PanContextDataKey.TEXTE_MAITRE_FORM);
        String currentSection = context.getFromContextData(PanContextDataKey.CURRENT_SECTION);
        texteMaitreLoiDTO.setMotCle(texteMaitreForm.getMotCle());
        texteMaitreLoiDTO.setNumero(texteMaitreForm.getNumero());
        texteMaitreLoiDTO.setTitreOfficiel(texteMaitreForm.getTitreOfficiel());
        texteMaitreLoiDTO.setDatePublication(
            texteMaitreForm.getDatePublication() == null ? null : texteMaitreForm.getDatePublication().getTime()
        );
        texteMaitreLoiDTO.setObservation(texteMaitreForm.getObservation());
        texteMaitreLoiDTO.setLegislaturePublication(texteMaitreForm.getLegislaturePublication());

        if (
            StringUtils.equals(currentSection, ActiviteNormativeEnum.APPLICATION_DES_LOIS.getId()) ||
            StringUtils.equals(currentSection, ActiviteNormativeEnum.APPLICATION_DES_ORDONNANCES.getId())
        ) {
            texteMaitreLoiDTO.setNumeroInterne(texteMaitreForm.getNumeroInterne());
            texteMaitreLoiDTO.setTitreActe(texteMaitreForm.getTitreActe());
            texteMaitreLoiDTO.setMinisterePilote(texteMaitreForm.getMinisterePilote());
            texteMaitreLoiDTO.setMinisterePiloteLabel(texteMaitreForm.getMinisterePiloteLabel());
            texteMaitreLoiDTO.setNatureTexte(texteMaitreForm.getNatureTexte());
            texteMaitreLoiDTO.setNatureTexteLabel(texteMaitreForm.getNatureTexteLabel());
            texteMaitreLoiDTO.setProcedureVote(texteMaitreForm.getProcedureVote());
            texteMaitreLoiDTO.setProcedureVoteLabel(texteMaitreForm.getProcedureVoteLabel());
            texteMaitreLoiDTO.setDateEntreeEnVigueur(
                texteMaitreForm.getDateEntreeEnVigueur() == null
                    ? null
                    : texteMaitreForm.getDateEntreeEnVigueur().getTime()
            );
            texteMaitreLoiDTO.setCommissionAssNationale(texteMaitreForm.getCommissionAssNationale());
            texteMaitreLoiDTO.setCommissionSenat(texteMaitreForm.getCommissionSenat());
            texteMaitreLoiDTO.setApplicationDirecte(texteMaitreForm.isApplicationDirecte());
            texteMaitreLoiDTO.setChampLibre(texteMaitreForm.getChampLibre());
            //texteMaitreLoiDTO.setDispositionHabilitation(texteMaitreForm.isDispositionHabilitation());
            texteMaitreLoiDTO.setDispositionHabilitation(texteMaitreForm.isFondementConstitutionnel());
            texteMaitreLoiDTO.setDateCirculationCompteRendu(
                texteMaitreForm.getDateCirculationCompteRendu() == null
                    ? null
                    : texteMaitreForm.getDateCirculationCompteRendu().getTime()
            );
            texteMaitreLoiDTO.setDateReunionProgrammation(
                texteMaitreForm.getDateReunionProgrammation() == null
                    ? null
                    : texteMaitreForm.getDateReunionProgrammation().getTime()
            );
        }
        return texteMaitreLoiDTO;
    }

    @Override
    public String reloadTexteMaitre(SpecificContext context) {
        TexteMaitre texteMaitre = getTexteMaitreFromContext(context);
        CoreSession session = context.getSession();

        ActiviteNormative activiteNormative = context.getCurrentDocument().getAdapter(ActiviteNormative.class);
        TexteMaitreLoiDTOImpl currentTexteMaitre = new TexteMaitreLoiDTOImpl(activiteNormative);

        Dossier dossier = SolonEpgServiceLocator
            .getNORService()
            .findDossierFromNOR(
                session,
                texteMaitre.getNumeroNor(),
                ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
                RetourDilaConstants.RETOUR_DILA_SCHEMA
            );
        DocumentModel ficheLoiDoc = null;
        if (dossier != null) {
            // M154020: fallback sur le champ iddossier pour faire la jointure
            ficheLoiDoc =
                SolonEpgServiceLocator
                    .getDossierService()
                    .findFicheLoiDocumentFromMGPP(session, dossier.getNumeroNor());
            if (ficheLoiDoc == null && dossier.getIdDossier() != null) {
                ficheLoiDoc =
                    SolonEpgServiceLocator
                        .getDossierService()
                        .findFicheLoiDocumentFromMGPP(session, dossier.getIdDossier());
            }
        }
        currentTexteMaitre.refresh(dossier, ficheLoiDoc);

        reloadMesures(session, texteMaitre.getDocument());
        return null;
    }

    private Map<String, DecretDTO> mapDecret(
        MesureApplicative currentMesure,
        Boolean refreshFromDossier,
        CoreSession documentManager,
        DocumentModel currentDoc
    ) {
        Map<String, DecretDTO> list = new TreeMap<>();
        final ActiviteNormativeService activiteNormativeService = SolonEpgServiceLocator.getActiviteNormativeService();

        List<Decret> listDecret = activiteNormativeService.fetchDecrets(currentMesure.getDecretIds(), documentManager);

        for (Decret decret : listDecret) {
            if (refreshFromDossier && StringUtils.isNotBlank(decret.getNumeroNor())) {
                Dossier dossier = SolonEpgServiceLocator
                    .getNORService()
                    .findDossierFromNOR(
                        documentManager,
                        decret.getNumeroNor(),
                        ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
                        RetourDilaConstants.RETOUR_DILA_SCHEMA
                    );
                list.put(
                    decret.getNumeroNor(),
                    new DecretDTOImpl(decret, dossier, currentMesure, currentDoc.getAdapter(TexteMaitre.class))
                );
            } else {
                list.put(
                    decret.getNumeroNor(),
                    new DecretDTOImpl(decret, null, currentMesure, currentDoc.getAdapter(TexteMaitre.class))
                );
            }
        }
        return list;
    }

    @Override
    public MesuresApplicativesPanUnsortedList getMesuresList(SpecificContext context) {
        DocumentModel loiDoc = context.getCurrentDocument();
        TexteMaitre texteMaitre = loiDoc.getAdapter(TexteMaitre.class);
        List<MesureApplicativeDTOImpl> mesuresDTO = mapMesure(texteMaitre, context.getSession());

        return new MesuresApplicativesPanUnsortedList(context, mesuresDTO);
    }

    @Override
    public DecretsPanUnsortedList getDecretsList(SpecificContext context) {
        MesureApplicative mesure = context.getCurrentDocument().getAdapter(MesureApplicative.class);
        mesure.getIdDossier();
        TexteMaitre texteMaitre = context
            .getSession()
            .getDocument(new IdRef(mesure.getIdDossier()))
            .getAdapter(TexteMaitre.class);
        List<DecretDTOImpl> decretsDTO = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .fetchDecrets(mesure.getDecretIds(), context.getSession())
            .stream()
            .map(decret -> new DecretDTOImpl(decret, null, mesure, texteMaitre))
            .collect(Collectors.toList());
        return new DecretsPanUnsortedList(context, decretsDTO);
    }

    private List<MesureApplicativeDTOImpl> mapMesure(TexteMaitre texteMaitre, CoreSession documentManager) {
        List<MesureApplicativeDTOImpl> list = new ArrayList<>();

        List<MesureApplicative> listMesureApplicative = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .fetchMesure(texteMaitre.getMesuresIds(), documentManager);

        for (MesureApplicative mesureApplicative : listMesureApplicative) {
            list.add(new MesureApplicativeDTOImpl(mesureApplicative, texteMaitre));
        }

        // tri par defaut sur le numero d'ordre
        list.sort(
            (mesure1, mesure2) -> {
                if (mesure1.getNumeroOrdre() != null && mesure2.getNumeroOrdre() != null) {
                    String str1 = String.format("%9s", mesure1.getNumeroOrdre());
                    String str2 = String.format("%9s", mesure2.getNumeroOrdre());
                    return str1.compareTo(str2);
                }
                return -1;
            }
        );
        return list;
    }

    private List<MesureApplicativeDTOImpl> reloadMesures(CoreSession documentManager, DocumentModel currentDoc) {
        TexteMaitre texteMaitre = documentManager.getDocument(currentDoc.getRef()).getAdapter(TexteMaitre.class);
        return mapMesure(texteMaitre, documentManager);
    }

    @Override
    public Map<String, DecretDTO> reloadDecrets(SpecificContext context) {
        MesureApplicative currentMesure = getMesureApplicativeFromContext(context);
        TexteMaitre texteMaitre = getTexteMaitreFromContext(context);
        boolean refreshFromDossier = context.getFromContextData(RELOAD_FROM_DOSSIER);
        if (currentMesure != null) {
            return mapDecret(currentMesure, refreshFromDossier, context.getSession(), texteMaitre.getDocument());
        }
        return new TreeMap<>();
    }

    @Override
    public MesureApplicativeDTO setMesure(SpecificContext context) {
        MesureApplicativeDTO mesureApplicativeDTO = context.getFromContextData(FIRST_TABLE_ELT_DTO);
        CoreSession session = context.getSession();
        /*
         * Il faut completement recharger la mesure et recréer le dto car les
         * informations en base concernant ses decrets ont pu être modifiées via le
         * bordereau des dossiers
         */
        TexteMaitre texteMaitre = getTexteMaitreFromContext(context);

        DocumentModel mesureDoc = session.getDocument(new IdRef(mesureApplicativeDTO.getId()));
        MesureApplicative mesureApplicative = mesureDoc.getAdapter(MesureApplicative.class);
        MesureApplicativeDTO currentMesure = new MesureApplicativeDTOImpl(mesureApplicative, texteMaitre);

        final ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();
        panService.saveTexteMaitre(texteMaitre, session);

        reloadDecrets(context);
        return currentMesure;
    }

    @Override
    public Map<String, DecretDTO> removeMesure(SpecificContext context) {
        MesureApplicative currentMesure = getMesureApplicativeFromContext(context);
        TexteMaitre currentDoc = getTexteMaitreFromContext(context);

        List<String> mesuresIds = currentDoc.getMesuresIds();
        mesuresIds.remove(currentMesure.getId());
        currentDoc.setMesuresIds(mesuresIds);

        context.putInContextData(PanContextDataKey.RELOAD_FROM_DOSSIER, false);

        final ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();
        panService.saveTexteMaitre(currentDoc, context.getSession());

        journaliserActionPAN(
            SolonEpgEventConstant.SUPPR_MESURE_EVENT,
            SolonEpgEventConstant.SUPPR_MESURE_COMMENT,
            currentDoc.getDocument(),
            context.getSession()
        );
        return reloadDecrets(context);
    }

    @Override
    public String addNewMesure(SpecificContext context) {
        DocumentModel activiteNormativeDoc = context.getCurrentDocument();
        List<MesureApplicativeDTO> listMesure = context.getFromContextData(FIRST_TABLE_LIST);
        int nombreMesuresToAdd = context.getFromContextData(MESURE_APPLICATIVE_NUMBER);
        TexteMaitre currentDoc = getTexteMaitreFromContext(context);

        for (int i = 0; i < nombreMesuresToAdd; i++) {
            MesureApplicativeDTO temp = new MesureApplicativeDTOImpl();
            temp.setValidate(true);
            listMesure.add(temp);
        }

        final ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();
        panService.saveTexteMaitre(currentDoc, context.getSession());

        // Ajout dans le journal du PAN
        String comment = nombreMesuresToAdd == 1
            ? SolonEpgEventConstant.AJOUT_MESURE_COMMENT
            : nombreMesuresToAdd > 1 ? "Ajout de " + nombreMesuresToAdd + " mesures" : null;
        if (comment != null) {
            journaliserActionPAN(
                SolonEpgEventConstant.AJOUT_MESURE_EVENT,
                comment,
                activiteNormativeDoc,
                context.getSession()
            );
        }
        return null;
    }

    @Override
    public String addNewDecret(SpecificContext context) {
        CoreSession session = context.getSession();

        Map<String, DecretDTO> mapDecret = context.getFromContextData(SECOND_TABLE_MAP);
        String newDecret = context.getFromContextData(PanContextDataKey.SECOND_TABLE_ELT_NOR);
        MesureApplicative currentMesure = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT);
        TexteMaitre texteMaitre = getTexteMaitreFromContext(context);

        final ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();
        String message = "";
        if (StringUtils.isNotEmpty(newDecret)) {
            if (!mapDecret.containsKey(newDecret)) {
                try {
                    Dossier dossier = panService.checkIsDecretFromNumeroNOR(
                        newDecret,
                        session,
                        ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
                        RetourDilaConstants.RETOUR_DILA_SCHEMA
                    );
                    if (dossier != null) {
                        // recherche si le decret existe deja pour pas le recréer
                        ActiviteNormative activiteNormative = panService.findActiviteNormativeByNor(newDecret, session);
                        DecretDTO decretToSave;
                        if (activiteNormative != null) {
                            Decret decret = activiteNormative.getDocument().getAdapter(Decret.class);
                            decretToSave = new DecretDTOImpl(decret, dossier, currentMesure, texteMaitre);
                            // m154544 : la case doit être cochées par défaut
                            decretToSave.setValidate(true);
                            mapDecret.put(decretToSave.getNumeroNor(), decretToSave);
                        } else {
                            decretToSave = new DecretDTOImpl(newDecret, dossier, currentMesure, texteMaitre);
                            // m154544 : la case doit être cochées par défaut
                            decretToSave.setValidate(true);
                            mapDecret.put(decretToSave.getNumeroNor(), decretToSave);
                        }
                        // Ajout dans le journal du PAN
                        journaliserActionPAN(
                            SolonEpgEventConstant.AJOUT_DECRET_APP_EVENT,
                            SolonEpgEventConstant.AJOUT_DECRET_APP_COMMENT + " [" + newDecret + "]",
                            texteMaitre.getDocument(),
                            session
                        );
                    }
                } catch (ActiviteNormativeException e) {
                    LOGGER.error(EpgLogEnumImpl.LOG_EXCEPTION_PAN_TEC, e);
                    message = ResourceHelper.getString(e.getMessage());
                }
            } else {
                message = ResourceHelper.getString("activite.normative.decret.existant");
            }
        } else {
            message = ResourceHelper.getString("activite.normative.dossier.not.found");
        }
        return message;
    }

    @Override
    public void updateDecret(SpecificContext context) throws ParseException {
        DecretDTO updateDecret = context.getFromContextData(PanContextDataKey.SECOND_TABLE_ELT_DTO);
        DecretForm decretForm = context.getFromContextData(PanContextDataKey.SECOND_TABLE_ELT_FORM);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        if (StringUtils.isNotBlank(decretForm.getDateSaisineCE())) {
            updateDecret.setDateSaisineCE(sdf.parse(decretForm.getDateSaisineCE()));
        }

        if (StringUtils.isNotBlank(decretForm.getDatePublication())) {
            updateDecret.setDatePublication(sdf.parse(decretForm.getDatePublication()));
        }

        if (StringUtils.isNotBlank(decretForm.getDateSignature())) {
            updateDecret.setDateSignature(sdf.parse(decretForm.getDateSignature()));
        }

        if (StringUtils.isNotBlank(decretForm.getDateExamenCE())) {
            updateDecret.setDateSectionCE(sdf.parse(decretForm.getDateExamenCE()));
        }

        updateDecret.setNumeroJOPublication(decretForm.getNumeroJO());

        updateDecret.setNumeroNor(decretForm.getNumeroNor());

        if (StringUtils.isNumeric(decretForm.getNumeroPage())) {
            updateDecret.setNumeroPage(Long.valueOf(decretForm.getNumeroPage()));
        }
        updateDecret.setTypeActe(decretForm.getTypeActe());
        updateDecret.setTypeActeLabel(
            SolonEpgServiceLocator
                .getSolonEpgVocabularyService()
                .getLabelFromId(
                    VocabularyConstants.TYPE_ACTE_VOCABULARY,
                    decretForm.getTypeActe(),
                    STVocabularyConstants.COLUMN_LABEL
                )
        );
        updateDecret.setNumerosTextes(decretForm.getNumeroTexte());
        updateDecret.setRapporteurCE(decretForm.getRapporteurCE());
        updateDecret.setSectionCE(decretForm.getSectionCE());
        updateDecret.setReferenceAvisCE(decretForm.getReferenceAvisCE());
        updateDecret.setTitreOfficiel(decretForm.getTitreOfficiel());

        updateDecret.setValidate(true);

        saveDecret(context);
    }

    @Override
    public DecretsPanUnsortedList getDecrets(SpecificContext context) {
        MesureApplicative mesure = context.getCurrentDocument().getAdapter(MesureApplicative.class);

        context.putInContextData(PanContextDataKey.TEXTE_MAITRE_ID, mesure.getIdDossier());
        context.putInContextData(PanContextDataKey.FIRST_TABLE_ID, mesure.getId());
        Map<String, DecretDTO> decretsDTO = PanUIServiceLocator.getTexteMaitreUIService().reloadDecrets(context);

        return new DecretsPanUnsortedList(
            context,
            decretsDTO.values().stream().map(it -> ((DecretDTOImpl) it)).collect(Collectors.toList())
        );
    }

    @Override
    public String saveMesure(SpecificContext context) {
        CoreSession session = context.getSession();

        DocumentModel activiteNormativeDoc = context.getCurrentDocument();
        List<MesureApplicativeDTO> listMesure = context.getFromContextData(FIRST_TABLE_LIST);

        final ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();
        DocumentModel doc = session.getDocument(activiteNormativeDoc.getRef());
        ActiviteNormative activiteNormative = doc.getAdapter(ActiviteNormative.class);

        try {
            panService.saveMesure(activiteNormative, listMesure, session);
        } catch (ActiviteNormativeException e) {
            LOGGER.error(EpgLogEnumImpl.LOG_EXCEPTION_PAN_TEC, e);
            if (e.getErrors() == null || e.getErrors().isEmpty()) {
                return ResourceHelper.getString("activite.normative.error.save.after.mesure");
            } else {
                StringBuilder messageBuilder = new StringBuilder();
                for (String error : e.getErrors()) {
                    messageBuilder.append(error + "\n");
                }
                return messageBuilder.toString();
            }
        }

        // Save to html file
        panService.generateANRepartitionMinistereHtml(session, activiteNormative, true);

        TexteMaitre texteMaitre = getTexteMaitreFromContext(context);
        reloadMesures(session, texteMaitre.getDocument());
        MesureApplicativeDTO mesure = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_DTO);
        String idMesure = context.getFromContextData(FIRST_TABLE_ID);
        if (StringUtils.isNotBlank(idMesure)) {
            String comment = getJournalComment(mesure, SolonEpgEventConstant.MODIF_MESURE_COMMENT);
            journaliserActionPAN(SolonEpgEventConstant.MODIF_MESURE_EVENT, comment, activiteNormativeDoc, session);
        } else {
            String comment = getJournalComment(mesure, SolonEpgEventConstant.AJOUT_MESURE_COMMENT);
            journaliserActionPAN(SolonEpgEventConstant.AJOUT_MESURE_EVENT, comment, activiteNormativeDoc, session);
        }
        return null;
    }

    private static String getJournalComment(MesureApplicativeDTO mesure, String action) {
        String numeroOrdre = mesure.getNumeroOrdre();
        return action + (StringUtils.isNotBlank(numeroOrdre) ? " [" + numeroOrdre + "]" : " []");
    }

    @Override
    public String saveDecret(SpecificContext context) {
        CoreSession session = context.getSession();

        Map<String, DecretDTO> mapDecret = context.getFromContextData(SECOND_TABLE_MAP);
        MesureApplicative currentMesure = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT);
        List<MesureApplicativeDTO> listMesure = context.getFromContextData(FIRST_TABLE_LIST);
        TexteMaitre texteMaitre = getTexteMaitreFromContext(context);

        if (currentMesure != null) {
            String idCurrentMesure = currentMesure.getId();
            if (StringUtils.isEmpty(idCurrentMesure)) {
                return ResourceHelper.getString("activite.normative.error.save.before.decret");
            }

            try {
                List<DecretDTO> lstDecret = new ArrayList<>(mapDecret.values());
                SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .saveDecrets(idCurrentMesure, lstDecret, texteMaitre.getId(), session);

                for (MesureApplicativeDTO mesureApplicativeDTO : listMesure) {
                    if (idCurrentMesure.equals(mesureApplicativeDTO.getId())) {
                        mesureApplicativeDTO.setDecretIds(
                            session
                                .getDocument(new IdRef(idCurrentMesure))
                                .getAdapter(MesureApplicative.class)
                                .getDecretIds()
                        );
                    }
                }
                reloadDecrets(context);
            } catch (ActiviteNormativeException e) {
                LOGGER.error(EpgLogEnumImpl.LOG_EXCEPTION_PAN_TEC, e);
                String message = ResourceHelper.getString("activite.normative.error.save.after.decret");
                if (e.getErrors() != null && !e.getErrors().isEmpty()) {
                    message = ResourceHelper.getString("activite.normative.error.save.mauvais.decret");
                    message += StringUtils.join(e.getErrors(), ", ", "");
                }
                return message;
            }
        }
        return null;
    }

    @Override
    public String removeDecret(SpecificContext context) {
        CoreSession session = context.getSession();

        DecretDTO decretDTO = context.getFromContextData(SECOND_TABLE_ELT_DTO);
        Map<String, DecretDTO> mapDecret = context.getFromContextData(SECOND_TABLE_MAP);
        TexteMaitre texteMaitre = getTexteMaitreFromContext(context);

        mapDecret.remove(decretDTO.getNumeroNor());
        // Ajout dans le journal du PAN
        journaliserActionPAN(
            SolonEpgEventConstant.SUPPR_DECRET_APP_EVENT,
            SolonEpgEventConstant.SUPPR_DECRET_APP_COMMENT + " [" + decretDTO.getNumeroNor() + "]",
            texteMaitre.getDocument(),
            session
        );
        return null;
    }

    /**
     * Decoration de la ligne de la mesure selectionnée dans la table des mesure
     */
    @Override
    public String decorate(SpecificContext context) {
        CoreSession session = context.getSession();

        MesureApplicativeDTO mesureDTO = context.getFromContextData(FIRST_TABLE_ELT_DTO);

        if (mesureDTO != null && mesureDTO.getId() != null) {
            final ActiviteNormativeService actNormS = SolonEpgServiceLocator.getActiviteNormativeService();
            DocumentModel mesureDoc = session.getDocument(new IdRef(mesureDTO.getId()));
            MesureApplicative mesure = mesureDoc.getAdapter(MesureApplicative.class);

            if (!mesureDTO.hasValidation() || !actNormS.checkDecretsValidationForMesure(session, mesure)) {
                return "dataRowRetourPourModification";
            }
        }

        DecretDTO decretDTO = context.getFromContextData(SECOND_TABLE_ELT_DTO);
        if (
            decretDTO != null &&
            decretDTO.getId() != null &&
            (!decretDTO.hasValidation() || !decretDTO.hasValidationLink())
        ) {
            return "dataRowRetourPourModification";
        }

        MesureApplicative currentMesure = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT);
        DecretDTOImpl decretDTOImpl = ((DecretDTOImpl) decretDTO);
        if (currentMesure != null) {
            if (currentMesure.getId() == null) {
                return null;
            } else if (decretDTOImpl != null && currentMesure.getId().equals(decretDTOImpl.getDocIdForSelection())) {
                return "dataRowSelected";
            }
        }
        return "";
    }

    @Override
    public String computeLegifranceLink(SpecificContext context) {
        String decretNor = context.getFromContextData(PanContextDataKey.SECOND_TABLE_ELT_NOR);

        return SolonEpgServiceLocator.getActiviteNormativeService().createLienJORFLegifrance(decretNor);
    }

    @Override
    public String getTitreDivMesure(SpecificContext context) {
        MesureApplicative currentMesure = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT);

        return getTitreDivMesure("Loi", currentMesure.getDocument());
    }

    @Override
    public String getTitreDivMesureAppOrdo(SpecificContext context) {
        MesureApplicative currentMesure = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT);

        return getTitreDivMesure("Ordonnance", currentMesure.getDocument());
    }

    private String getTitreDivMesure(String type, DocumentModel doc) {
        StringBuilder queryBuilder = new StringBuilder(ResourceHelper.getString("activite.normative.mesures"));
        if (doc != null) {
            TexteMaitre texteMaitre = doc.getAdapter(TexteMaitre.class);
            queryBuilder.append(" (").append(type).append(" N°");
            queryBuilder.append(texteMaitre.getNumero());
            queryBuilder.append(")");
        }
        return queryBuilder.toString();
    }

    @Override
    public String getTitreDivDecret(SpecificContext context) {
        MesureApplicative currentMesure = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT);
        TexteMaitre texteMaitre = getTexteMaitreFromContext(context);

        return getTitreDivDecret("Loi", texteMaitre.getDocument(), currentMesure);
    }

    @Override
    public String getTitreDivDecretAppOrdo(SpecificContext context) {
        MesureApplicative currentMesure = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT);
        TexteMaitre texteMaitre = getTexteMaitreFromContext(context);

        return getTitreDivDecret("Ordonnance", texteMaitre.getDocument(), currentMesure);
    }

    private String getTitreDivDecret(String type, DocumentModel doc, MesureApplicative currentMesure) {
        StringBuilder queryBuilder = new StringBuilder(ResourceHelper.getString("activite.normative.decrets"));
        if (doc != null) {
            TexteMaitre texteMaitre = doc.getAdapter(TexteMaitre.class);
            queryBuilder.append(" (").append(type).append(" N°");
            queryBuilder.append(texteMaitre.getNumero());
            if (currentMesure != null) {
                queryBuilder.append(" - Mesure " + currentMesure.getNumeroOrdre());
            }
            queryBuilder.append(")");
        }
        return queryBuilder.toString();
    }

    @Override
    public void validerMesure(SpecificContext context) {
        CoreSession session = context.getSession();

        Integer numeroOrdre = context.getFromContextData(PanContextDataKey.MESURE_APPLICATIVE_ORDER);
        TexteMaitre texteMaitre = getTexteMaitreFromContext(context);

        String comment = SolonEpgEventConstant.MODIF_MESURE_COMMENT.concat(
            numeroOrdre != null ? " [" + numeroOrdre + "]" : " []"
        );
        journaliserActionPAN(SolonEpgEventConstant.MODIF_MESURE_EVENT, comment, texteMaitre.getDocument(), session);
    }

    @Override
    public void updateMesure(SpecificContext context) throws ParseException {
        MesureApplicativeDTO mesure = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_DTO);

        MesureForm mesureForm = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_FORM);
        SimpleDateFormat sdf = SolonDateConverter.DATE_SLASH.simpleDateFormat();
        if (mesureForm.getAmendement() != null) {
            mesure.setAmendement(mesureForm.getAmendement());
        }
        if (mesureForm.getArticle() != null) {
            mesure.setArticle(mesureForm.getArticle());
        }
        if (mesureForm.getBaseLegale() != null) {
            mesure.setBaseLegale(mesureForm.getBaseLegale());
        }
        if (mesureForm.getTypeMesure() != null) {
            mesure.setTypeMesure(mesureForm.getTypeMesure());
        }
        if (mesureForm.getChampLibre() != null) {
            mesure.setChampLibre(mesureForm.getChampLibre());
        }
        if (mesureForm.getObjet() != null) {
            mesure.setObjetRIM(mesureForm.getObjet());
        }
        if (mesureForm.getObservation() != null) {
            mesure.setObservation(mesureForm.getObservation());
        }
        if (mesureForm.getCodeModifie() != null) {
            mesure.setCodeModifie(mesureForm.getCodeModifie());
        }
        if (mesureForm.getApplicationRecu() != null) {
            mesure.setApplicationRecu(mesureForm.getApplicationRecu());
        }
        mesure.setCalendrierConsultationsHCE(mesureForm.getCalendrierHorsCE());
        mesure.setCommunicationMinisterielle(mesureForm.getCommunicationMinisterielle());
        mesure.setConsultationsHCE(mesureForm.getConsultationHorsCE());

        if (mesureForm.getNumeroOrdre() != null) {
            mesure.setNumeroOrdre(mesureForm.getNumeroOrdre());
        }
        if (mesureForm.getNumeroQuestion() != null) {
            mesure.setNumeroQuestion(mesureForm.getNumeroQuestion());
        }
        if (mesureForm.getResponsableAmendement() != null) {
            mesure.setResponsableAmendement(mesureForm.getResponsableAmendement());
        }

        Optional<SelectValueDTO> poleChargeMission = EpgUIServiceLocator
            .getEpgSelectValueUIService()
            .getPoleChargeMission()
            .stream()
            .filter(it -> it.getKey().equals(mesureForm.getPoleChargeMission()))
            .findFirst();

        poleChargeMission.ifPresent(selectValueDTO -> mesure.setPoleChargeMission(selectValueDTO.getLabel()));

        mesure.setValidate(true);

        if (mesure.getApplicationRecu()) {
            List<String> decretsIds = mesureForm.getDecretsNors();
            if (decretsIds != null) {
                decretsIds =
                    mesureForm
                        .getDecretsNors()
                        .stream()
                        .map(decretNor -> getOrCreateDecret(context, decretNor))
                        .map(STDomainObject::getId)
                        .collect(Collectors.toList());
            }
            mesure.setDecretIds(decretsIds);
        }

        if (StringUtils.isNotBlank(mesureForm.getDatePrevisionnelleSaisineCE())) {
            mesure.setDatePrevisionnelleSaisineCE(sdf.parse(mesureForm.getDatePrevisionnelleSaisineCE()));
        }
        if (StringUtils.isNotBlank(mesureForm.getDateEntreeVigueur())) {
            mesure.setDateEntreeEnVigueur(sdf.parse(mesureForm.getDateEntreeVigueur()));
        }
        if (StringUtils.isNotBlank(mesureForm.getDateMiseApplication())) {
            mesure.setDateMiseApplication(sdf.parse(mesureForm.getDateMiseApplication()));
        }
        if (StringUtils.isNotBlank(mesureForm.getDateObjectifPublication())) {
            mesure.setDateObjectifPublication(sdf.parse(mesureForm.getDateObjectifPublication()));
        }
        if (StringUtils.isNotBlank(mesureForm.getDatePassageCM())) {
            mesure.setDatePassageCM(sdf.parse(mesureForm.getDatePassageCM()));
        }

        mesure.setDiffere(mesureForm.getDiffereApplication());
        mesure.setDirectionResponsable(mesureForm.getDirectionResponsable());
        mesure.setMinisterePilote(mesureForm.getMinisterePilote());

        saveMesure(context);
    }

    private ActiviteNormative getOrCreateDecret(SpecificContext context, String decretNor) {
        ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();
        ActiviteNormative an = panService.findActiviteNormativeByNor(decretNor, context.getSession());
        if (an == null) {
            context.putInContextData(PanContextDataKey.SECOND_TABLE_ELT_NOR, decretNor);
            addNewDecret(context);
            saveDecret(context);
            an = panService.findActiviteNormativeByNor(decretNor, context.getSession());
        }
        return an;
    }

    private void journaliserActionPAN(
        String event,
        String comment,
        DocumentModel currentDoc,
        CoreSession documentManager
    ) {
        DocumentModel dossierDoc;
        dossierDoc =
            SolonEpgServiceLocator
                .getNORService()
                .getDossierFromNOR(documentManager, currentDoc.getAdapter(TexteMaitre.class).getNumeroNor());
        String category = null;
        if ("1".equals(currentDoc.getAdapter(ActiviteNormative.class).getApplicationLoi())) {
            category = SolonEpgEventConstant.CATEGORY_LOG_PAN_APPLICATION_LOIS;
        } else if ("1".equals(currentDoc.getAdapter(ActiviteNormative.class).getApplicationOrdonnance())) {
            category = SolonEpgEventConstant.CATEGORY_LOG_PAN_APPLICATION_ORDO;
        }
        if (category != null) {
            STServiceLocator
                .getJournalService()
                .journaliserActionPAN(documentManager, dossierDoc.getRef().toString(), event, comment, category);
        }
    }
}
