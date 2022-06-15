package fr.dila.solonepg.ui.services.pan.impl;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.LoiDeRatification;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.api.dto.DecretApplicationDTO;
import fr.dila.solonepg.api.dto.LoiDeRatificationDTO;
import fr.dila.solonepg.api.dto.OrdonnanceDTO;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.core.dto.activitenormative.DecretApplicationDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.LoiDeRatificationDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.OrdonnanceDTOImpl;
import fr.dila.solonepg.core.exception.ActiviteNormativeException;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.pan.LoisDeRatificationPanUnsortedList;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.TexteMaitreOrdonnanceUIService;
import fr.dila.solonepg.ui.th.model.bean.pan.LoiRatificationForm;
import fr.dila.solonepg.ui.th.model.bean.pan.OrdonnanceForm;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

/**
 * Bean Seam de gestion du texte maitre (74-1) de l'activite normative
 *
 * @author asatre
 */
public class TexteMaitreOrdonnanceUIServiceImpl implements TexteMaitreOrdonnanceUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(TexteMaitreOrdonnanceUIServiceImpl.class);

    private static final String QUERY_ORDONNANCES =
        "SELECT d.ecm:uuid as id FROM " +
        ActiviteNormativeConstants.ACTIVITE_NORMATIVE_DOCUMENT_TYPE +
        " as d WHERE d." +
        ActiviteNormativeConstants.ACTIVITE_NORMATIVE_PREFIX +
        ":" +
        ActiviteNormativeConstants.P_ORDONNANCE +
        " = '" +
        ActiviteNormativeConstants.ACTIVITE_NORMATIVE_ENABLE +
        "' ";

    private static final String QUERY_ORDONNANCES_NOT_RATIFIEE =
        QUERY_ORDONNANCES +
        " AND d." +
        TexteMaitreConstants.TEXTE_MAITRE_PREFIX +
        ":" +
        TexteMaitreConstants.RATIFIE +
        " = 0 ";

    @Override
    public String reloadOrdonnance(SpecificContext context) {
        TexteMaitre texteMaitre = context.getCurrentDocument().getAdapter(TexteMaitre.class);
        Dossier dossier = SolonEpgServiceLocator
            .getNORService()
            .findDossierFromNOR(context.getSession(), texteMaitre.getNumeroNor());
        if (dossier == null) {
            LOGGER.error(
                EpgLogEnumImpl.LOG_EXCEPTION_PAN_TEC,
                "le dossier avec nor " + texteMaitre.getNumeroNor() + " n'existe pas"
            );
        }
        return null;
    }

    public OrdonnanceDTO updateTexteMaitreDTO(SpecificContext context) {
        OrdonnanceDTOImpl texteMaitreDTO = context.getFromContextData(PanContextDataKey.TEXTE_MAITRE_DTO);
        OrdonnanceForm texteMaitreForm = context.getFromContextData(PanContextDataKey.TEXTE_MAITRE_FORM);
        texteMaitreDTO.setNumero(texteMaitreForm.getNumero());
        texteMaitreDTO.setNumeroInterne(texteMaitreForm.getNumeroInterne());
        texteMaitreDTO.setTitreOfficiel(texteMaitreForm.getTitre());
        texteMaitreDTO.setDatePublication(
            texteMaitreForm.getDatePublication() == null ? null : texteMaitreForm.getDatePublication().getTime()
        );
        texteMaitreDTO.setDispositionHabilitation(texteMaitreForm.isFondementConstitutionnel());
        texteMaitreDTO.setRatifie(texteMaitreForm.isRatifie());
        texteMaitreDTO.setRenvoiDecret(texteMaitreForm.isRenvoiDecret());
        texteMaitreDTO.setObservation(texteMaitreForm.getObservation());
        texteMaitreDTO.setMinisterePilote(texteMaitreForm.getMinisterePilote());
        texteMaitreDTO.setMinisterePiloteLabel(texteMaitreForm.getMinisterePiloteLabel());
        return texteMaitreDTO;
    }

    @Override
    public String saveOrdonnance(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        OrdonnanceDTO currentOrdonnance = updateTexteMaitreDTO(context);
        DocumentModel currentDocument = context.getCurrentDocument();

        SolonEpgServiceLocator
            .getActiviteNormativeService()
            .saveOrdonnance(currentOrdonnance, currentDocument.getAdapter(TexteMaitre.class), documentManager);
        SolonEpgServiceLocator.getActiviteNormativeService().updateOrdonnancesListePubliee(documentManager, true);
        journaliserActionPAN(
            SolonEpgEventConstant.MODIF_TM_EVENT,
            SolonEpgEventConstant.MODIF_TM_COMMENT,
            currentDocument,
            documentManager
        );
        return null;
    }

    @Override
    public String saveLoiRatification(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        DocumentModel currentDocument = context.getCurrentDocument();
        List<LoiDeRatificationDTO> listLoiDeRatification = context.getFromContextData(
            PanContextDataKey.FIRST_TABLE_LIST
        );

        DocumentModel doc = documentManager.getDocument(currentDocument.getRef());
        ActiviteNormative activiteNormative = doc.getAdapter(ActiviteNormative.class);

        try {
            activiteNormative =
                SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .saveProjetsLoiDeRatification(listLoiDeRatification, documentManager, activiteNormative);
        } catch (ActiviteNormativeException e) {
            LOGGER.error(EpgLogEnumImpl.LOG_EXCEPTION_PAN_TEC, e);
            if (e.getErrors() == null || e.getErrors().isEmpty()) {
                return ResourceHelper.getString("activite.normative.error.save.after.loiRatification");
            } else {
                return StringUtils.join(e.getErrors(), "\n");
            }
        }
        mapLoiDeRatification(activiteNormative.getDocument().getAdapter(TexteMaitre.class), false, documentManager);
        return null;
    }

    @Override
    public LoisDeRatificationPanUnsortedList getLoiDeRatificationList(SpecificContext context) {
        TexteMaitre texteMaitre = context.getCurrentDocument().getAdapter(TexteMaitre.class);

        List<LoiDeRatification> listLoiRatification = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .fetchLoiDeRatification(texteMaitre.getLoiRatificationIds(), context.getSession());

        List<LoiDeRatificationDTOImpl> dtoList = listLoiRatification
            .stream()
            .map(loi -> new LoiDeRatificationDTOImpl(loi))
            .collect(Collectors.toList());

        return new LoisDeRatificationPanUnsortedList(context, dtoList);
    }

    private List<LoiDeRatificationDTO> mapLoiDeRatification(
        TexteMaitre texteMaitre,
        boolean fromReload,
        CoreSession documentManager
    ) {
        List<LoiDeRatificationDTO> list = new ArrayList<>();
        List<LoiDeRatification> listLoiRatification = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .fetchLoiDeRatification(texteMaitre.getLoiRatificationIds(), documentManager);

        for (LoiDeRatification loiDeRatification : listLoiRatification) {
            if (fromReload) {
                Dossier dossier = SolonEpgServiceLocator
                    .getNORService()
                    .findDossierFromNOR(documentManager, loiDeRatification.getNumeroNor());
                DocumentModel ficheLoiDoc = null;
                if (dossier != null) {
                    // M154020: fallback sur le champ iddossier pour faire la jointure
                    ficheLoiDoc =
                        SolonEpgServiceLocator
                            .getDossierService()
                            .findFicheLoiDocumentFromMGPP(documentManager, dossier.getNumeroNor());
                    if (ficheLoiDoc == null && dossier.getIdDossier() != null) {
                        ficheLoiDoc =
                            SolonEpgServiceLocator
                                .getDossierService()
                                .findFicheLoiDocumentFromMGPP(documentManager, dossier.getIdDossier());
                    }
                }
                list.add(new LoiDeRatificationDTOImpl(loiDeRatification, dossier, ficheLoiDoc));
            } else {
                list.add(new LoiDeRatificationDTOImpl(loiDeRatification));
            }
        }
        if (list.isEmpty()) {
            list.add(new LoiDeRatificationDTOImpl());
        }

        return list;
    }

    @Override
    public String addLoiRatification(SpecificContext context) {
        String newLoiRatification = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_NOR);
        CoreSession session = context.getSession();
        DocumentModel currentDocument = context.getCurrentDocument();

        OrdonnanceDTO currentOrdonnance = new OrdonnanceDTOImpl(
            currentDocument.getAdapter(ActiviteNormative.class),
            session
        );
        List<LoiDeRatificationDTO> listLoiDeRatification = context.getFromContextData(
            PanContextDataKey.FIRST_TABLE_LIST
        );

        if (StringUtils.isNotEmpty(newLoiRatification)) {
            final ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();

            LoiDeRatificationDTO loiDeRatificationDTO = null;

            ActiviteNormative activiteNormative = panService.findActiviteNormativeByNor(newLoiRatification, session);

            if (activiteNormative != null) {
                LoiDeRatification loiDeRatification = activiteNormative
                    .getDocument()
                    .getAdapter(LoiDeRatification.class);
                loiDeRatificationDTO = new LoiDeRatificationDTOImpl(loiDeRatification);
            } else {
                try {
                    Dossier dossier = panService.checkIsLoi(newLoiRatification, session);
                    loiDeRatificationDTO = new LoiDeRatificationDTOImpl(dossier);
                } catch (ActiviteNormativeException e) {
                    String message = ResourceHelper.getString(e.getMessage());
                    LOGGER.error(EpgLogEnumImpl.LOG_EXCEPTION_PAN_TEC, e, message);
                    return message;
                }
            }
            ConseilEtat ce = SolonEpgServiceLocator
                .getDossierService()
                .findDossierFromIdDossier(session, newLoiRatification)
                .getDocument()
                .getAdapter(ConseilEtat.class);
            loiDeRatificationDTO.setDateSaisineCE(DateUtil.toDate(ce.getDateSaisineCE()));
            loiDeRatificationDTO.setDateExamenCE(DateUtil.toDate(ce.getDateSectionCe()));
            loiDeRatificationDTO.setSectionCE(ce.getSectionCe());

            loiDeRatificationDTO.setNumeroNor(newLoiRatification);
            if (!currentOrdonnance.isDispositionHabilitation() && currentOrdonnance.getDatePublication() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(currentOrdonnance.getDatePublication());
                cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH) + 18));
                loiDeRatificationDTO.setDateLimitePublication(cal.getTime());
            }
            listLoiDeRatification.add(loiDeRatificationDTO);
            // Ajout dans le journal du PAN
            journaliserActionPAN(
                SolonEpgEventConstant.AJOUT_RATIF_EVENT,
                SolonEpgEventConstant.AJOUT_RATIF_COMMENT + " [" + loiDeRatificationDTO.getNumeroNor() + "]",
                currentDocument,
                session
            );
        }

        return null;
    }

    /**
     * Decoration de la ligne de la mesure selectionnée dans la table des mesure
     */
    public String decorate(AbstractMapDTO dto, String defaultClass, LoiDeRatificationDTO currentLoiDeRatification) {
        if (dto instanceof LoiDeRatificationDTO) {
            LoiDeRatificationDTO loiDeRatificationDTO = (LoiDeRatificationDTO) dto;
            if (loiDeRatificationDTO.getId() != null && !loiDeRatificationDTO.hasValidation()) {
                return "dataRowRetourPourModification";
            }
        } else if (dto instanceof DecretApplicationDTO) {
            DecretApplicationDTO decretAppDTO = (DecretApplicationDTO) dto;
            if (decretAppDTO.getId() != null && (!decretAppDTO.hasValidation() || !decretAppDTO.hasValidationLink())) {
                return "dataRowRetourPourModification";
            }
        }

        if (currentLoiDeRatification != null) {
            if (currentLoiDeRatification.getId() == null) {
                return null;
            } else if (currentLoiDeRatification.getId().equals(dto.getDocIdForSelection())) {
                return "dataRowSelected";
            }
        }
        return defaultClass;
    }

    private Map<String, DecretApplicationDTO> loadDecret(OrdonnanceDTO currentOrdonnance, CoreSession session) {
        Map<String, DecretApplicationDTO> mapDecret;
        if (currentOrdonnance == null) {
            // reset decret
            mapDecret = new TreeMap<>();
        } else {
            // load list decret
            mapDecret = mapDecret(currentOrdonnance, Boolean.FALSE, session);
        }
        return mapDecret;
    }

    private Map<String, DecretApplicationDTO> mapDecret(
        OrdonnanceDTO ordonnanceDTO,
        Boolean refreshFromDossier,
        CoreSession documentManager
    ) {
        Map<String, DecretApplicationDTO> list = new TreeMap<>();
        ActiviteNormativeService activiteNormativeService = SolonEpgServiceLocator.getActiviteNormativeService();

        List<Decret> listDecret = activiteNormativeService.fetchDecrets(ordonnanceDTO.getDecretIds(), documentManager);

        for (Decret decret : listDecret) {
            if (refreshFromDossier && decret.getIdDossier() != null) {
                DocumentModel dossierDoc = documentManager.getDocument(new IdRef(decret.getIdDossier()));
                list.put(
                    decret.getNumeroNor(),
                    new DecretApplicationDTOImpl(decret, dossierDoc.getAdapter(Dossier.class), ordonnanceDTO)
                );
            } else {
                list.put(decret.getNumeroNor(), new DecretApplicationDTOImpl(decret, null, ordonnanceDTO));
            }
        }

        if (list.isEmpty()) {
            list.put("", new DecretApplicationDTOImpl());
        }

        return list;
    }

    @Override
    public String removeLoiDeRatification(SpecificContext context) {
        LoiDeRatification loiDeRatification = context
            .getSession()
            .getDocument(new IdRef(context.getFromContextData(PanContextDataKey.FIRST_TABLE_ID)))
            .getAdapter(LoiDeRatification.class);
        DocumentModel currentDocument = context.getCurrentDocument();
        TexteMaitre texteMaitre = currentDocument.getAdapter(TexteMaitre.class);
        List<String> loisIds = texteMaitre.getLoiRatificationIds();
        loisIds.remove(loiDeRatification.getId());
        texteMaitre.setLoiRatificationIds(loisIds);
        context.putInContextData(PanContextDataKey.RELOAD_FROM_DOSSIER, false);

        final ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();
        panService.saveTexteMaitre(texteMaitre, context.getSession());

        journaliserActionPAN(
            SolonEpgEventConstant.SUPPR_RATIF_EVENT,
            SolonEpgEventConstant.SUPPR_RATIF_COMMENT + " [" + loiDeRatification.getNumeroNor() + "]",
            currentDocument,
            context.getSession()
        );

        return null;
    }

    @Override
    public void setListDecret(SpecificContext context) {
        List<DecretApplicationDTO> listDecret = context.getFromContextData(PanContextDataKey.SECOND_TABLE_LIST);
        Map<String, DecretApplicationDTO> mapDecret = context.getFromContextData(PanContextDataKey.SECOND_TABLE_MAP);

        for (DecretApplicationDTO decret : listDecret) {
            mapDecret.put(decret.getNumeroNor(), decret);
        }
    }

    @Override
    public List<DecretApplicationDTO> getListDecret(SpecificContext context) {
        Map<String, DecretApplicationDTO> mapDecret = context.getFromContextData(PanContextDataKey.SECOND_TABLE_MAP);
        OrdonnanceDTO currentOrdonnance = context.getFromContextData(PanContextDataKey.ORDONNANCE_DTO);
        CoreSession documentManager = context.getSession();
        if (mapDecret == null) {
            mapDecret = loadDecret(currentOrdonnance, documentManager);
        }
        return new ArrayList<>(mapDecret.values());
    }

    @Override
    public String addNewDecret(SpecificContext context) {
        String newDecret = context.getFromContextData(PanContextDataKey.SECOND_TABLE_ELT_NOR);
        CoreSession documentManager = context.getSession();
        Map<String, DecretApplicationDTO> mapDecret = context.getFromContextData(PanContextDataKey.SECOND_TABLE_MAP);

        DecretApplicationDTO decretDTO = null;
        if (StringUtils.isNotEmpty(newDecret)) {
            if (!mapDecret.containsKey(newDecret)) {
                try {
                    Dossier dossier = SolonEpgServiceLocator
                        .getActiviteNormativeService()
                        .checkIsDecretFromNumeroNOR(newDecret, documentManager);
                    if (dossier != null) {
                        decretDTO = new DecretApplicationDTOImpl(dossier);

                        decretDTO.setValidation(Boolean.TRUE);
                        decretDTO.setValidate(Boolean.TRUE);
                        mapDecret.put(decretDTO.getNumeroNor(), decretDTO);
                    }
                } catch (ActiviteNormativeException e) {
                    LOGGER.error(EpgLogEnumImpl.LOG_EXCEPTION_PAN_TEC, e);
                    return ResourceHelper.getString(e.getMessage());
                }
            } else {
                return ResourceHelper.getString("activite.normative.decret.existant");
            }
        }

        return null;
    }

    @Override
    public Map<String, DecretApplicationDTO> removeDecret(SpecificContext context) {
        DecretApplicationDTO decretDTO = context.getFromContextData(PanContextDataKey.SECOND_TABLE_ELT_DTO);
        Map<String, DecretApplicationDTO> mapDecret = context.getFromContextData(PanContextDataKey.SECOND_TABLE_MAP);
        mapDecret.remove(decretDTO.getNumeroNor());
        return mapDecret;
    }

    @Override
    public Map<String, DecretApplicationDTO> reloadDecrets(SpecificContext context) {
        boolean refreshFromDossier = context.getFromContextData(PanContextDataKey.RELOAD_FROM_DOSSIER);
        OrdonnanceDTO currentOrdonnance = context.getFromContextData(PanContextDataKey.ORDONNANCE_DTO);
        Map<String, DecretApplicationDTO> mapDecret;
        CoreSession documentManager = context.getSession();
        if (currentOrdonnance != null) {
            mapDecret = mapDecret(currentOrdonnance, refreshFromDossier, documentManager);
        } else {
            mapDecret = new TreeMap<>();
        }
        return mapDecret;
    }

    @Override
    public String saveDecret(SpecificContext context) {
        OrdonnanceDTO currentOrdonnance = context.getFromContextData(PanContextDataKey.ORDONNANCE_DTO);
        CoreSession documentManager = context.getSession();
        if (currentOrdonnance != null) {
            String idCurrentOrdonnance = currentOrdonnance.getId();
            if (StringUtils.isEmpty(idCurrentOrdonnance)) {
                return ResourceHelper.getString("activite.normative.error.save.before.decret");
            }

            try {
                List<DecretApplicationDTO> lstDecret = getListDecret(context);
                SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .saveDecretsOrdonnance(idCurrentOrdonnance, lstDecret, documentManager);
                currentOrdonnance.setDecretIds(
                    documentManager
                        .getDocument(new IdRef(idCurrentOrdonnance))
                        .getAdapter(TexteMaitre.class)
                        .getDecretIds()
                );
                currentOrdonnance.setDecretIdsInvalidated(null);
                reloadDecrets(context);
            } catch (ActiviteNormativeException e) {
                LOGGER.error(EpgLogEnumImpl.LOG_EXCEPTION_PAN_TEC, e);
                String message = ResourceHelper.getString("activite.normative.error.save.after.decret");
                if (e.getErrors() == null || e.getErrors().isEmpty()) {
                    return message;
                } else {
                    message =
                        ResourceHelper.getString(
                            "activite.normative.error.save.mauvais.decret",
                            StringUtils.join(e.getErrors(), ", ", "")
                        );
                    return message;
                }
            }
        }
        return null;
    }

    @Override
    public String getQuery(SpecificContext context) {
        boolean isMasquerRatifie = context.getFromContextData(PanContextDataKey.MASQUER_RATIFIE);
        if (isMasquerRatifie) {
            return QueryUtils.ufnxqlToFnxqlQuery(QUERY_ORDONNANCES_NOT_RATIFIEE);
        } else {
            return QueryUtils.ufnxqlToFnxqlQuery(QUERY_ORDONNANCES);
        }
    }

    @Override
    public String updateLoiDeRatification(SpecificContext context) {
        LoiRatificationForm form = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_FORM);
        LoiDeRatificationDTO dto = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_DTO);

        dto.setNumeroNor(form.getNumeroNor());
        dto.setSectionCE(form.getSectionCE());
        dto.setChambreDepot(form.getChambreDepot());
        dto.setNumeroDepot(form.getNumeroDepot());
        dto.setTitreActe(form.getTitreActe());
        dto.setTitreOfficiel(form.getTitreOfficiel());

        if (form.getDateLimiteDepot() != null) {
            dto.setDateLimiteDepot(form.getDateLimiteDepot().getTime());
        }

        if (form.getDateSaisineCE() != null) {
            dto.setDateSaisineCE(form.getDateSaisineCE().getTime());
        }

        if (form.getDateExamenCE() != null) {
            dto.setDateExamenCE(form.getDateExamenCE().getTime());
        }

        if (form.getDateExamenCM() != null) {
            dto.setDateExamenCM(form.getDateExamenCM().getTime());
        }

        if (form.getDateDepot() != null) {
            dto.setDateDepot(form.getDateDepot().getTime());
        }

        if (form.getDatePublication() != null) {
            dto.setDatePublication(form.getDatePublication().getTime());
        }

        return saveLoiRatification(context);
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
        STServiceLocator
            .getJournalService()
            .journaliserActionPAN(
                documentManager,
                dossierDoc.getRef().toString(),
                event,
                comment,
                SolonEpgEventConstant.CATEGORY_LOG_PAN_RATIFICATION_ORDO
            );
    }
}
