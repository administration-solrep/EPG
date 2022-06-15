package fr.dila.solonepg.ui.services.pan.impl;

import static fr.dila.solonepg.ui.enums.pan.PanContextDataKey.FIRST_TABLE_LIST;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.Habilitation;
import fr.dila.solonepg.api.cases.Ordonnance;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.dto.HabilitationDTO;
import fr.dila.solonepg.api.dto.OrdonnanceHabilitationDTO;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.core.cases.ActiviteNormativeProgrammationImpl;
import fr.dila.solonepg.core.dto.activitenormative.HabilitationDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.OrdonnanceHabilitationDTOImpl;
import fr.dila.solonepg.core.dto.activitenormative.TexteMaitreLoiDTOImpl;
import fr.dila.solonepg.core.exception.ActiviteNormativeException;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.pan.HabilitationsPanUnsortedList;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.TexteMaitreHabilitationUIService;
import fr.dila.solonepg.ui.th.model.bean.pan.HabilitationForm;
import fr.dila.solonepg.ui.th.model.bean.pan.OrdonnanceHabilitationForm;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

/**
 * Bean Seam de gestion du texte maitre (38C) de l'activite normative
 *
 * @see Habilitation
 * @see TexteMaitre
 * @see Ordonnance
 *
 * @author asatre
 */
public class TexteMaitreHabilitationUIServiceImpl implements TexteMaitreHabilitationUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(ActiviteNormativeProgrammationImpl.class);

    @Override
    public TexteMaitreLoiDTOImpl getCurrentTexteMaitre(SpecificContext context) {
        ActiviteNormative activiteNormative = context.getCurrentDocument().getAdapter(ActiviteNormative.class);
        return new TexteMaitreLoiDTOImpl(activiteNormative);
    }

    @Override
    public HabilitationsPanUnsortedList getListHabilitation(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        TexteMaitre texteMaitre = context.getCurrentDocument().getAdapter(TexteMaitre.class);
        List<HabilitationDTOImpl> listHabilitation = mapHabilitation(texteMaitre.getHabilitationIds(), documentManager);
        Collections.sort(
            listHabilitation,
            (hab1, hab2) -> {
                if (hab1.getNumeroOrdre() != null && hab2.getNumeroOrdre() != null) {
                    try {
                        return Integer.valueOf(hab1.getNumeroOrdre()).compareTo(Integer.valueOf(hab2.getNumeroOrdre()));
                    } catch (NumberFormatException nfe) {
                        return hab1.getNumeroOrdre().compareTo(hab2.getNumeroOrdre());
                    }
                }
                return -1;
            }
        );
        return new HabilitationsPanUnsortedList(context, listHabilitation);
    }

    private List<HabilitationDTOImpl> mapHabilitation(List<String> habilitationIds, CoreSession documentManager) {
        List<HabilitationDTOImpl> list = new ArrayList<>();
        List<Habilitation> listHab = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .fetchListHabilitation(habilitationIds, documentManager);

        for (Habilitation habilitation : listHab) {
            list.add(new HabilitationDTOImpl(habilitation));
        }

        return list;
    }

    @Override
    public void setListOrdonnance(SpecificContext context) {
        List<OrdonnanceHabilitationDTO> listOrdonnance = context.getFromContextData(
            PanContextDataKey.SECOND_TABLE_LIST
        );
        Map<String, OrdonnanceHabilitationDTO> mapOrdonnance = context.getFromContextData(
            PanContextDataKey.SECOND_TABLE_MAP
        );

        for (OrdonnanceHabilitationDTO ordo : listOrdonnance) {
            mapOrdonnance.put(ordo.getNumeroNor(), ordo);
        }
    }

    @Override
    public List<OrdonnanceHabilitationDTO> getListOrdonnance(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        Map<String, OrdonnanceHabilitationDTO> mapOrdonnance = context.getFromContextData(
            PanContextDataKey.SECOND_TABLE_MAP
        );
        TexteMaitreLoiDTOImpl currentTexteMaitre = getCurrentTexteMaitre(context);
        DocumentModel currentDocument = context.getCurrentDocument();
        HabilitationDTO currentHabilitationDTO = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_DTO);

        if (
            mapOrdonnance == null ||
            currentTexteMaitre == null ||
            !currentTexteMaitre.getId().equals(currentDocument.getId())
        ) {
            mapOrdonnance = mapOrdonnance(currentHabilitationDTO, Boolean.FALSE, documentManager);
        }
        return new ArrayList<>(mapOrdonnance.values());
    }

    private Map<String, OrdonnanceHabilitationDTO> mapOrdonnance(
        HabilitationDTO habilitationDTO,
        Boolean refreshFromDossier,
        CoreSession documentManager
    ) {
        Map<String, OrdonnanceHabilitationDTO> list = new TreeMap<>();
        List<Ordonnance> listOrdo = SolonEpgServiceLocator
            .getActiviteNormativeService()
            .fetchListOrdonnance(habilitationDTO.getOrdonnanceIds(), documentManager);

        for (Ordonnance ordonnance : listOrdo) {
            Dossier dossier = null;
            if (refreshFromDossier) {
                dossier =
                    SolonEpgServiceLocator
                        .getNORService()
                        .findDossierFromNOR(documentManager, ordonnance.getNumeroNor());
            }
            list.put(
                ordonnance.getNumeroNor(),
                new OrdonnanceHabilitationDTOImpl(ordonnance, habilitationDTO, dossier, documentManager)
            );
        }

        return list;
    }

    @Override
    public String addNewHabilitation(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        List<HabilitationDTO> listHabilitation = context.getFromContextData(PanContextDataKey.FIRST_TABLE_LIST);

        DocumentModel currentDoc = context.getCurrentDocument();

        HabilitationDTO tmp = new HabilitationDTOImpl();
        tmp.setValidate(true);
        listHabilitation.add(tmp);
        // Ajout dans le journal du PAN
        try {
            journaliserActionPAN(
                SolonEpgEventConstant.AJOUT_HABIL_EVENT,
                SolonEpgEventConstant.AJOUT_HABIL_COMMENT,
                currentDoc,
                documentManager
            );
        } catch (Exception e) {
            LOGGER.error(documentManager, STLogEnumImpl.FAIL_AJOUT_JOURNAL_PAN, e);
        }
        return null;
    }

    @Override
    public String removeHabilitation(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        HabilitationDTO currentHabilitation = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_DTO);
        TexteMaitre currentDoc = context.getCurrentDocument().getAdapter(TexteMaitre.class);
        List<String> habilitationsIds = currentDoc.getHabilitationIds();
        habilitationsIds.remove(currentHabilitation.getId());
        currentDoc.setHabilitationIds(habilitationsIds);
        context.putInContextData(PanContextDataKey.RELOAD_FROM_DOSSIER, false);

        final ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();
        panService.saveTexteMaitre(currentDoc, context.getSession());

        // Ajout dans le journal du PAN
        try {
            journaliserActionPAN(
                SolonEpgEventConstant.SUPPR_HABIL_EVENT,
                SolonEpgEventConstant.SUPPR_HABIL_COMMENT,
                context.getCurrentDocument(),
                documentManager
            );
        } catch (Exception e) {
            LOGGER.error(documentManager, STLogEnumImpl.FAIL_AJOUT_JOURNAL_PAN, e);
        }
        reloadOrdonnanceHabilitation(context);
        return null;
    }

    @Override
    public String addNewOrdonnance(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        String numeroNor = context.getFromContextData(PanContextDataKey.SECOND_TABLE_ELT_NOR);
        Map<String, OrdonnanceHabilitationDTO> mapOrdonnance = context.getFromContextData(
            PanContextDataKey.SECOND_TABLE_MAP
        );

        HabilitationDTO currentHabilitationDTO = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_DTO);
        DocumentModel currentDoc = context.getCurrentDocument();

        String message = "";
        Ordonnance ordonnance = null;
        if (StringUtils.isNotEmpty(numeroNor)) {
            if (!mapOrdonnance.containsKey(numeroNor)) {
                try {
                    ordonnance =
                        SolonEpgServiceLocator
                            .getActiviteNormativeService()
                            .checkIsOrdonnance38CFromNumeroNOR(numeroNor, documentManager);
                } catch (ActiviteNormativeException e) {
                    LOGGER.error(EpgLogEnumImpl.LOG_EXCEPTION_PAN_TEC, e);
                    message = ResourceHelper.getString(e.getMessage());
                    return message;
                }

                OrdonnanceHabilitationDTO ordonnanceDTO = null;

                if (ordonnance == null) {
                    ordonnanceDTO = new OrdonnanceHabilitationDTOImpl(numeroNor, currentHabilitationDTO);
                } else {
                    Dossier dossier = SolonEpgServiceLocator
                        .getNORService()
                        .findDossierFromNOR(
                            documentManager,
                            ordonnance.getNumeroNor(),
                            ConseilEtatConstants.CONSEIL_ETAT_SCHEMA,
                            RetourDilaConstants.RETOUR_DILA_SCHEMA
                        );

                    ordonnanceDTO =
                        new OrdonnanceHabilitationDTOImpl(
                            ordonnance,
                            currentHabilitationDTO,
                            dossier,
                            context.getSession()
                        );
                }
                ordonnanceDTO.setValidation(Boolean.TRUE);
                ordonnanceDTO.setValidate(Boolean.TRUE);
                mapOrdonnance.put(ordonnanceDTO.getNumeroNor(), ordonnanceDTO);
                // Ajout dans le journal du PAN
                journaliserActionPAN(
                    SolonEpgEventConstant.AJOUT_ORDO_EVENT,
                    SolonEpgEventConstant.AJOUT_ORDO_COMMENT +
                    " [" +
                    ordonnanceDTO.getNumeroNor() +
                    "] à l'habilitation [" +
                    currentHabilitationDTO.getNumeroOrdre() +
                    "]",
                    currentDoc,
                    documentManager
                );
            } else {
                message = ResourceHelper.getString("activite.normative.ordonnance.existant");
            }
        }
        return message;
    }

    @Override
    public String removeOrdonnanceHabilitation(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        OrdonnanceHabilitationDTO ordonnanceHabilitationDTO = context.getFromContextData(
            PanContextDataKey.SECOND_TABLE_ELT_DTO
        );
        Map<String, OrdonnanceHabilitationDTO> mapOrdonnance = context.getFromContextData(
            PanContextDataKey.SECOND_TABLE_MAP
        );
        HabilitationDTO currentHabilitationDTO = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_DTO);

        DocumentModel currentDoc = context.getCurrentDocument();

        mapOrdonnance.remove(ordonnanceHabilitationDTO.getNumeroNor());
        // Ajout dans le journal du PAN
        try {
            journaliserActionPAN(
                SolonEpgEventConstant.SUPPR_ORDO_EVENT,
                SolonEpgEventConstant.SUPPR_ORDO_COMMENT +
                " [" +
                ordonnanceHabilitationDTO.getNumeroNor() +
                "] de l'habilitation [" +
                currentHabilitationDTO.getNumeroOrdre() +
                "]",
                currentDoc,
                documentManager
            );
        } catch (Exception e) {
            LOGGER.error(documentManager, STLogEnumImpl.FAIL_AJOUT_JOURNAL_PAN, e);
        }
        return null;
    }

    /**
     * Decoration de la ligne de la mesure selectionnée dans la table des mesure
     */
    public String decorate(
        AbstractMapDTO dto,
        String defaultClass,
        CoreSession documentManager,
        HabilitationDTO currentHabilitationDTO
    ) {
        if (dto instanceof HabilitationDTO) {
            HabilitationDTO habilitationDTO = (HabilitationDTO) dto;
            if (habilitationDTO.getId() != null) {
                final ActiviteNormativeService actNormS = SolonEpgServiceLocator.getActiviteNormativeService();
                DocumentModel habilitationDoc = documentManager.getDocument(new IdRef(habilitationDTO.getId()));
                Habilitation habilitation = habilitationDoc.getAdapter(Habilitation.class);

                if (
                    !habilitationDTO.hasValidation() ||
                    !actNormS.checkOrdonnancesValidationForHabilitation(documentManager, habilitation)
                ) {
                    return "dataRowRetourPourModification";
                }
            }
        }

        if (dto instanceof OrdonnanceHabilitationDTO) {
            OrdonnanceHabilitationDTO ordonnanceHabilitationDTO = (OrdonnanceHabilitationDTO) dto;
            if (
                ordonnanceHabilitationDTO.getId() != null &&
                (!ordonnanceHabilitationDTO.hasValidation() || !ordonnanceHabilitationDTO.hasValidationLink())
            ) {
                return "dataRowRetourPourModification";
            }
        }

        if (currentHabilitationDTO != null) {
            if (currentHabilitationDTO.getId() == null) {
                return null;
            } else if (currentHabilitationDTO.getId().equals(dto.getDocIdForSelection())) {
                return "dataRowSelected";
            }
        }
        return defaultClass;
    }

    /**
     * reload des habilitations
     */
    @Override
    public List<HabilitationDTOImpl> reloadHabilitation(SpecificContext context) {
        TexteMaitre texteMaitre = context.getCurrentDocument().getAdapter(TexteMaitre.class);
        return mapHabilitation(texteMaitre.getHabilitationIds(), context.getSession());
    }

    /**
     * Sauvegarde des ordonnances habilitations
     */
    @Override
    public String saveOrdonnanceHabilitation(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        TexteMaitre texteMaitre = context.getCurrentDocument().getAdapter(TexteMaitre.class);
        HabilitationDTO currentHabilitationDTO = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_DTO);

        List<String> listOrdonnancesId = null;
        try {
            List<OrdonnanceHabilitationDTO> lstOrdos = getListOrdonnance(context);
            listOrdonnancesId =
                SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .saveOrdonnanceHabilitation(
                        lstOrdos,
                        currentHabilitationDTO.getId(),
                        texteMaitre.getId(),
                        documentManager
                    );
        } catch (ActiviteNormativeException e) {
            LOGGER.error(EpgLogEnumImpl.LOG_EXCEPTION_PAN_TEC, e);
            if (e.getErrors() == null || e.getErrors().isEmpty()) {
                return ResourceHelper.getString("activite.normative.error.save.after.decret");
            } else {
                StringBuilder nors = new StringBuilder(StringUtils.join(e.getErrors(), ", ", ""));
                return ResourceHelper.getString("activite.normative.error.save.mauvais.decret") + nors.toString();
            }
        }

        currentHabilitationDTO.setOrdonnanceIds(listOrdonnancesId);
        currentHabilitationDTO.setOrdonnanceIdsInvalidated(null);

        // rechargement des ordonnances
        reloadOrdonnanceHabilitation(currentHabilitationDTO, Boolean.FALSE, documentManager);
        return null;
    }

    /**
     * reload des ordonnances habilitations
     */
    @Override
    public Map<String, OrdonnanceHabilitationDTO> reloadOrdonnanceHabilitation(SpecificContext context) {
        HabilitationDTO currentHabilitationDTO = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_DTO);
        CoreSession documentManager = context.getSession();
        return reloadOrdonnanceHabilitation(
            currentHabilitationDTO,
            context.getFromContextData(PanContextDataKey.RELOAD_FROM_DOSSIER),
            documentManager
        );
    }

    @Override
    public void updateHabilitation(SpecificContext context) {
        HabilitationDTO habilitation = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_DTO);
        HabilitationForm habilitationForm = context.getFromContextData(PanContextDataKey.FIRST_TABLE_ELT_FORM);

        habilitation.setNumeroOrdre(habilitationForm.getNumeroOrdre());
        habilitation.setArticle(habilitationForm.getArticle());
        habilitation.setObjetRIM(habilitationForm.getObjet());
        habilitation.setTypeHabilitation(habilitationForm.getTypeHabilitation());
        habilitation.setCodification(habilitationForm.getCodification());
        habilitation.setLegislature(habilitationForm.getLegislature());
        habilitation.setObservation(habilitationForm.getObservations());
        habilitation.setConventionDepot(habilitationForm.getDepotRatification());
        habilitation.setMinisterePilote(habilitationForm.getMinisterePilote());
        habilitation.setConvention(habilitationForm.getTermeHabilitation());
        habilitation.setValidate(true);

        if (habilitationForm.getDatePreviSaisineCE() != null) {
            habilitation.setDatePrevisionnelleSaisineCE(habilitationForm.getDatePreviSaisineCE().getTime());
        }

        if (habilitationForm.getDateTerme() != null) {
            habilitation.setDateTerme(habilitationForm.getDateTerme().getTime());
        }

        if (habilitationForm.getDatePreviCM() != null) {
            habilitation.setDatePrevisionnelleCM(habilitationForm.getDatePreviCM().getTime());
        }

        saveHabilitation(context);
    }

    @Override
    public void updateOrdonnanceHabilitation(SpecificContext context) {
        OrdonnanceHabilitationDTO ordonnanceHabilitationDto = context.getFromContextData(
            PanContextDataKey.SECOND_TABLE_ELT_DTO
        );
        OrdonnanceHabilitationForm ordonnanceHabilitationForm = context.getFromContextData(
            PanContextDataKey.SECOND_TABLE_ELT_FORM
        );

        ordonnanceHabilitationDto.setNumeroNor(ordonnanceHabilitationForm.getNumeroNor());
        ordonnanceHabilitationDto.setNumero(ordonnanceHabilitationForm.getNumero());
        ordonnanceHabilitationDto.setMinisterePilote(ordonnanceHabilitationForm.getMinisterePilote());
        ordonnanceHabilitationDto.setLegislature(ordonnanceHabilitationForm.getLegislature());
        ordonnanceHabilitationDto.setObjet(ordonnanceHabilitationForm.getObjet());
        ordonnanceHabilitationDto.setTitreOfficiel(ordonnanceHabilitationForm.getTitreOfficiel());
        ordonnanceHabilitationDto.setConventionDepot(ordonnanceHabilitationForm.getDepotRatification());
        ordonnanceHabilitationDto.setValidate(true);

        if (ordonnanceHabilitationForm.getDateLimiteDepot() != null) {
            ordonnanceHabilitationDto.setDateLimiteDepot(ordonnanceHabilitationForm.getDateLimiteDepot().getTime());
        }

        if (ordonnanceHabilitationForm.getDatePassageCM() != null) {
            ordonnanceHabilitationDto.setDatePassageCM(ordonnanceHabilitationForm.getDatePassageCM().getTime());
        }

        if (ordonnanceHabilitationForm.getDatePublication() != null) {
            ordonnanceHabilitationDto.setDatePublication(ordonnanceHabilitationForm.getDatePublication().getTime());
        }

        if (ordonnanceHabilitationForm.getDateSaisineCE() != null) {
            ordonnanceHabilitationDto.setDateSaisineCE(ordonnanceHabilitationForm.getDateSaisineCE().getTime());
        }
        saveOrdonnanceHabilitation(context);
    }

    private void saveHabilitation(SpecificContext context) {
        CoreSession session = context.getSession();

        DocumentModel activiteNormativeDoc = context.getCurrentDocument();
        List<HabilitationDTO> listDto = context.getFromContextData(FIRST_TABLE_LIST);

        final ActiviteNormativeService panService = SolonEpgServiceLocator.getActiviteNormativeService();
        DocumentModel doc = session.getDocument(activiteNormativeDoc.getRef());
        ActiviteNormative activiteNormative = doc.getAdapter(ActiviteNormative.class);

        try {
            panService.saveHabilitation(listDto, activiteNormative.getId(), session);
        } catch (ActiviteNormativeException e) {
            LOGGER.error(EpgLogEnumImpl.LOG_EXCEPTION_PAN_TEC, e);
        }
    }

    private Map<String, OrdonnanceHabilitationDTO> reloadOrdonnanceHabilitation(
        HabilitationDTO currentHabilitationDTO,
        Boolean refreshDossier,
        CoreSession documentManager
    ) {
        return mapOrdonnance(currentHabilitationDTO, refreshDossier, documentManager);
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

        if (dossierDoc == null) {
            LOGGER.warn(
                documentManager,
                STLogEnumImpl.FAIL_GET_DOCUMENT_TEC,
                "Impossible de récuperer le dossier associé au nor " +
                currentDoc.getAdapter(TexteMaitre.class).getNumeroNor() +
                " : journalisation impossible"
            );
        } else {
            STServiceLocator
                .getJournalService()
                .journaliserActionPAN(
                    documentManager,
                    dossierDoc.getRef().toString(),
                    event,
                    comment,
                    SolonEpgEventConstant.CATEGORY_LOG_PAN_SUIVI_HABILITATIONS
                );
        }
    }
}
