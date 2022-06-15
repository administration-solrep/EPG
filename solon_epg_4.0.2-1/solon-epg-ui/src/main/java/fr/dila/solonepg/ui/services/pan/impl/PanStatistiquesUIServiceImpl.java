package fr.dila.solonepg.ui.services.pan.impl;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.api.activitenormative.ANReport;
import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.birt.BirtRefreshFichier;
import fr.dila.solonepg.api.birt.BirtReportContent;
import fr.dila.solonepg.api.birt.BirtResultatFichier;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.SolonEpgANEventConstants;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.api.service.ActiviteNormativeStatsService;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.api.service.StatsGenerationResultatService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.SuiviLibreReportDTO;
import fr.dila.solonepg.ui.bean.pan.PANExportParametreDTO;
import fr.dila.solonepg.ui.bean.pan.PanStatistiquesParamDTO;
import fr.dila.solonepg.ui.bean.pan.birt.PanBirtDTO;
import fr.dila.solonepg.ui.bean.pan.birt.PanBirtParamForm;
import fr.dila.solonepg.ui.bean.pan.birt.PanBirtStats;
import fr.dila.solonepg.ui.constants.pan.PanConstants;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.PanStatistiquesUIService;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.model.bean.pan.PanStatistiquesParamForm;
import fr.dila.solonepg.ui.th.model.bean.pan.PanStatistiquesParamLegisEnCoursForm;
import fr.dila.solonepg.ui.th.model.bean.pan.PanStatistiquesParamLegisPrecForm;
import fr.dila.ss.api.birt.BirtReport;
import fr.dila.ss.api.birt.ReportProperty;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.services.impl.AbstractSSStatistiquesUIServiceImpl;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.common.TriConsumer;
import org.json.JSONException;
import org.json.JSONObject;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;

public class PanStatistiquesUIServiceImpl
    extends AbstractSSStatistiquesUIServiceImpl
    implements PanStatistiquesUIService {
    public static final String BIRT_PATTERN = "pan_stat\\d{2,3}";
    private static final String BIRT_PATTERN_SAMPLE = "pan_stat22";

    private static final String LIBELLE_PROMULGATION_LOIS = "promulgation.lois";
    private static final String LIBELLE_PROMULGATION_ORDO = "promulgation.ordonnances";
    private static final String LIBELLE_PUBLICATION_DECRETS = "publication.decrets";

    private static final STLogger LOGGER = STLogFactory.getLog(PanStatistiquesUIServiceImpl.class);

    @Override
    public String generateReport(BirtReport birtReport, Map<String, String> params) {
        return generateReportFile(birtReport, getScalarValues(birtReport, params));
    }

    @Override
    protected Pattern getBirtReportIdPattern() {
        return Pattern.compile(BIRT_PATTERN);
    }

    @Override
    protected String getBirtPatternSample() {
        return BIRT_PATTERN_SAMPLE;
    }

    @Override
    public Blob generateReport(SpecificContext context) {
        String activeTab = context.getFromContextData(PanContextDataKey.SEARCH_FORM_KEY);
        PanBirtParamForm form = context.getFromContextData(activeTab);
        String statId = context.getFromContextData(SSContextDataKey.STAT_ID);
        BirtReport birtReport = getBirtReport(statId);

        PanBirtDTO dto = getBirtDTOFromContext(context, form);

        Map<String, Serializable> scalarValues = getScalarValues(birtReport, dto.toBirt());

        BirtOutputFormat outputFormat = context.getFromContextData(SSContextDataKey.BIRT_OUTPUT_FORMAT);

        return generateReportFile(birtReport, scalarValues, outputFormat);
    }

    @Override
    public void refreshReport(String statId, Map<String, String> birtParams, SpecificContext context) {
        BirtReport birtReport = getBirtReport(statId);
        CoreSession session = context.getSession();
        String user = session.getPrincipal().getName();
        ANReport anReport = getAnReport(birtReport, context.getFromContextData(EpgContextDataKey.BIRT_REPORT_SUFFIX));
        final String userWorkspacePath = STServiceLocator
            .getUserWorkspaceService()
            .getCurrentUserPersonalWorkspace(session)
            .getPathAsString();
        Map<String, Serializable> scalarValues = getScalarValues(birtReport, birtParams);

        ActiviteNormativeStatsService anStatService = SolonEpgServiceLocator.getActiviteNormativeStatsService();

        if (
            !anStatService.isCurrentlyRefreshing(session, anReport, user) &&
            anStatService.flagRefreshFor(session, anReport, user, userWorkspacePath)
        ) {
            EventProducer eventProducer = STServiceLocator.getEventProducer();
            Map<String, Serializable> eventProperties = new HashMap<>();
            eventProperties.put(SolonEpgANEventConstants.CURRENT_REPORT_PROPERTY, anReport);
            eventProperties.put(SolonEpgANEventConstants.INPUT_VALUES_PROPERTY, (Serializable) scalarValues);
            eventProperties.put(SolonEpgANEventConstants.USER_PROPERTY, user);
            eventProperties.put(SolonEpgANEventConstants.USER_WS_PATH_PROPERTY, userWorkspacePath);
            InlineEventContext inlineEventContext = new InlineEventContext(
                session,
                session.getPrincipal(),
                eventProperties
            );
            eventProducer.fireEvent(inlineEventContext.newEvent(SolonEpgANEventConstants.REFRESH_STATS_EVENT));
        }
        notifyReportRefreshing(context, session, user, anReport, anStatService);
    }

    @Override
    public String getReportUrl(BirtReport birtReport, Map<String, String> params, String idContextuel) {
        String url = "report?statId=" + birtReport.getId();
        if (StringUtils.isNotBlank(idContextuel)) {
            url = url + "&idContextuel=" + idContextuel;
        }
        return url;
    }

    @Override
    protected String getGeneratedReportDirectory() {
        return SolonEpgConfigConstant.SOLONEPG_GENERATED_REPORT_DIRECTORY;
    }

    @Override
    public BirtResultatFichier saveBirtRefreshAsResultat(String statId, String suffix, SpecificContext context) {
        BirtReport birtReport = getBirtReport(statId);
        ANReport anReport = getAnReport(birtReport, suffix);

        return saveBirtRefreshAsResultat(context.getSession(), statId, getBirtRefresh(context.getSession(), anReport));
    }

    private BirtResultatFichier saveBirtRefreshAsResultat(
        CoreSession session,
        String statId,
        BirtRefreshFichier birtRefresh
    ) {
        return Optional
            .ofNullable(birtRefresh)
            .map(
                birtRefreshFichier ->
                    SolonEpgServiceLocator
                        .getStatsGenerationResultatService()
                        .saveReportResulat(
                            session,
                            statId,
                            birtRefreshFichier.getHtmlContent(),
                            birtRefreshFichier.getPdfContent(),
                            birtRefreshFichier.getXlsContent()
                        )
            )
            .orElse(null);
    }

    @Override
    public boolean publishReport(String statId, String suffix, SpecificContext context) {
        CoreSession session = context.getSession();
        ANReport anReport = getAnReport(getBirtReport(statId), suffix);

        BirtResultatFichier birtResultat;
        String paramList = null;
        BirtRefreshFichier birtRefreshFichier = getBirtRefresh(session, anReport);
        if (birtRefreshFichier != null) {
            birtResultat = saveBirtRefreshAsResultat(session, statId, birtRefreshFichier);
            paramList = birtRefreshFichier.getParamList();
        } else {
            birtResultat = getBirtResultat(session, anReport);
        }

        if (birtResultat != null) {
            String dossierId = context.getFromContextData(PanContextDataKey.DOSSIER_ID);
            String ministereId = context.getFromContextData(PanContextDataKey.MINISTERE_PILOTE_ID);
            String nor = null;
            // Le dossier id est défini à la valeur du ministere sur l'onglet ministere; on bypass ce if si on est pas explicitement sur un dossier
            if (ministereId == null && dossierId != null) {
                DocumentModel document = context.getSession().getDocument(new IdRef(dossierId));
                TexteMaitre texteMaitre = document.getAdapter(TexteMaitre.class);
                nor = texteMaitre.getNumeroNor();
            }

            ANReportEnum anReportEnum = ANReportEnum.getByReportId(statId);
            if (anReportEnum != null) {
                try {
                    publishReport(session, anReport, birtResultat, paramList, ministereId, nor, anReportEnum);

                    return true;
                } catch (JSONException e) {
                    LOGGER.error(EpgLogEnumImpl.LOG_EXCEPTION_PAN_TEC, e);
                }
            }
        }

        return false;
    }

    private void publishReport(
        CoreSession session,
        ANReport anReport,
        BirtResultatFichier birtResultat,
        String paramList,
        String ministereId,
        String nor,
        ANReportEnum anReportEnum
    )
        throws JSONException {
        SolonEpgServiceLocator
            .getStatsGenerationResultatService()
            .publierReportResulat(
                anReportEnum.getFilename(),
                ministereId,
                nor,
                birtResultat.getHtmlContent(),
                true,
                session
            );

        ActiviteNormativeService activiteNormativeService = SolonEpgServiceLocator.getActiviteNormativeService();

        if (activiteNormativeService.isPublicationBilanSemestrielsBdjActivated(session) && paramList != null) {
            JSONObject paramListStr = new JSONObject(paramList);
            Date debutIntervalle1 = parseDateFromJson(paramListStr, "DEBUT_INTERVALLE1_PARAM");
            Date finIntervalle1 = parseDateFromJson(paramListStr, "FIN_INTERVALLE1_PARAM");
            Date debutIntervalle2 = parseDateFromJson(paramListStr, "DEBUT_INTERVALLE2_PARAM");
            Date finIntervalle2 = parseDateFromJson(paramListStr, "FIN_INTERVALLE2_PARAM");

            SolonEpgParametreService paramService = SolonEpgServiceLocator.getSolonEpgParametreService();
            ParametrageAN param = paramService.getDocAnParametre(session);
            String legislatureEnCours = param.getLegislatureEnCours();

            if (anReport.getType() == ANReportEnum.TAB_AN_BILAN_SEM_LOI_TOUS) {
                activiteNormativeService.publierBilanSemestrielLoiBDJ(
                    session,
                    legislatureEnCours,
                    debutIntervalle1,
                    finIntervalle1,
                    debutIntervalle2,
                    finIntervalle2
                );
            } else if (anReport.getType() == ANReportEnum.TAB_AN_BILAN_SEM_ORDONNANCE_TOUS) {
                activiteNormativeService.publierBilanSemestrielOrdonnanceBDJ(
                    session,
                    legislatureEnCours,
                    debutIntervalle1,
                    finIntervalle1,
                    debutIntervalle2,
                    finIntervalle2
                );
            }
        }
    }

    private static Date parseDateFromJson(JSONObject paramListStr, String dateKey) throws JSONException {
        return SolonDateConverter.DATE_SLASH.parseToCalendar((String) paramListStr.get(dateKey)).getTime();
    }

    private BirtRefreshFichier getBirtRefresh(CoreSession session, ANReport anReport) {
        ActiviteNormativeStatsService anStatService = SolonEpgServiceLocator.getActiviteNormativeStatsService();
        return Optional
            .ofNullable(anStatService.getBirtRefreshDocForCurrentUser(session, anReport))
            .map(refreshDoc -> refreshDoc.getAdapter(BirtRefreshFichier.class))
            .orElse(null);
    }

    private BirtResultatFichier getBirtResultat(CoreSession session, ANReport anReport) {
        StatsGenerationResultatService statsGenService = SolonEpgServiceLocator.getStatsGenerationResultatService();
        return Optional
            .ofNullable(statsGenService.getBirtResultatFichier(session, anReport.getName()))
            .map(birtResultatDoc -> birtResultatDoc.getAdapter(BirtResultatFichier.class))
            .orElse(null);
    }

    private BirtReportContent getBirtRefreshOrResultat(CoreSession session, ANReport anReport) {
        BirtRefreshFichier result = getBirtRefresh(session, anReport);
        if (result != null) {
            return result;
        } else {
            return getBirtResultat(session, anReport);
        }
    }

    @Override
    public boolean isRefreshExisting(SpecificContext context) {
        String statId = context.getFromContextData(SSContextDataKey.STAT_ID);
        String suffix = context.getFromContextData(EpgContextDataKey.BIRT_REPORT_SUFFIX);
        return getBirtRefresh(context.getSession(), getAnReport(getBirtReport(statId), suffix)) != null;
    }

    @Override
    public boolean isResultatExisting(SpecificContext context) {
        String statId = context.getFromContextData(SSContextDataKey.STAT_ID);
        String suffix = context.getFromContextData(EpgContextDataKey.BIRT_REPORT_SUFFIX);
        return getBirtResultat(context.getSession(), getAnReport(getBirtReport(statId), suffix)) != null;
    }

    @Override
    public Blob getBlobCurrentReport(SpecificContext context) {
        String statId = context.getFromContextData(SSContextDataKey.STAT_ID);
        String suffix = context.getFromContextData(EpgContextDataKey.BIRT_REPORT_SUFFIX);
        BirtReportContent birtContentHolder = getBirtRefreshOrResultat(
            context.getSession(),
            getAnReport(getBirtReport(statId), suffix)
        );
        if (birtContentHolder == null) {
            return null;
        } else {
            return birtContentHolder.getContent(context.getFromContextData(SSContextDataKey.BIRT_OUTPUT_FORMAT));
        }
    }

    @Override
    public String isCurrentReportRefreshing(SpecificContext context) {
        String statId = context.getFromContextData(SSContextDataKey.STAT_ID);
        String suffix = context.getFromContextData(EpgContextDataKey.BIRT_REPORT_SUFFIX);
        CoreSession session = context.getSession();
        BirtReport birtReport = getBirtReport(statId);
        ANReport anReport = getAnReport(birtReport, suffix);
        String user = session.getPrincipal().getName();

        ActiviteNormativeStatsService anStatService = SolonEpgServiceLocator.getActiviteNormativeStatsService();

        if (anStatService.isCurrentlyRefreshing(session, anReport, user)) {
            notifyReportRefreshing(context, session, user, anReport, anStatService);
            String dateRequest = anStatService.getHorodatageRequest(session, anReport, user);
            return ResourceHelper.getString("pan.espace.an.stats.export.msg", dateRequest);
        }
        return null;
    }

    @Override
    public boolean hasRequiredParameters(BirtReport birtReport, Map<String, String> params) {
        for (ReportProperty prop : birtReport.getProperties().values()) {
            if (!params.containsKey(prop.getName()) || params.get(prop.getName()) == null) {
                return false;
            }
        }
        return true;
    }

    private void notifyReportRefreshing(
        SpecificContext context,
        CoreSession session,
        String user,
        ANReport anReport,
        ActiviteNormativeStatsService anStatService
    ) {
        String dateRequest = anStatService.getHorodatageRequest(session, anReport, user);
        context
            .getMessageQueue()
            .addToastSuccess(ResourceHelper.getString("pan.espace.an.stats.export.msg", dateRequest));
    }

    private ANReport getAnReport(BirtReport birtReport, String suffix) {
        ANReportEnum reportEnum = ANReportEnum.getByReportId(birtReport.getId());
        String filename = reportEnum.getFilename();
        if (StringUtils.isNotBlank(suffix)) {
            filename += "-" + suffix;
        }
        return new ANReport(filename, reportEnum);
    }

    private Map<String, Serializable> getScalarValues(BirtReport birtReport, Map<String, String> stringValues) {
        return getScalarProperties(birtReport)
            .stream()
            .collect(
                Collectors.toMap(
                    ReportProperty::getName,
                    property -> StringUtils.defaultString(stringValues.get(property.getName()), property.getValue())
                )
            );
    }

    @Override
    public PanBirtDTO getBirtDTOFromContext(SpecificContext context, PanBirtParamForm form) {
        String statId = context.getFromContextData(SSContextDataKey.STAT_ID);
        BirtReport birtReport = SSServiceLocator.getBirtGenerationService().getReport(statId);
        PanBirtDTO dto = new PanBirtDTO(birtReport);
        PanBirtStats stats = PanBirtStats.PAN_BIRT_TREE_RELATION.get(statId);

        if (form == null) {
            form = new PanBirtParamForm();
        }

        PanStatistiquesParamDTO paramAn = getStatParams(context);

        dto.setDossierId(form.getDossierId());

        dto.setMinisteres(
            SolonEpgServiceLocator
                .getStatsGenerationResultatService()
                .getMinisteresListBirtReportParam(context.getSession())
        );

        dto.setDirections(
            SolonEpgServiceLocator.getStatsGenerationResultatService().getDirectionsListBirtReportParam()
        );

        if (form.getMinisterePilote() != null) {
            dto.setMinisterePilote(form.getMinisterePilote());
            dto.setMinisterePiloteLabel(
                STServiceLocator
                    .getOrganigrammeService()
                    .getOrganigrammeNodeById(form.getMinisterePilote(), OrganigrammeType.MINISTERE)
                    .getLabel()
            );
        }

        dto.setLibelleDateIntervalle2(LIBELLE_PUBLICATION_DECRETS);
        PanConstants.Section section = stats.getSection();
        PanConstants.Tab tab = stats.getTab();

        if (section == PanConstants.Section.SECTION_LOIS) {
            dto.setLibelleDateIntervalle1(LIBELLE_PROMULGATION_LOIS);
            applySection(
                stats,
                form,
                dto,
                paramAn.getLegisEnCours(),
                PanStatistiquesUIServiceImpl::applyBSL,
                PanStatistiquesUIServiceImpl::applyTXL,
                PanStatistiquesUIServiceImpl::applyINDL
            );
        } else if (section == PanConstants.Section.SECTION_ORDONNANCES) {
            dto.setLibelleDateIntervalle1(LIBELLE_PROMULGATION_ORDO);
            applySection(
                stats,
                form,
                dto,
                paramAn.getLegisEnCours(),
                PanStatistiquesUIServiceImpl::applyBSO,
                PanStatistiquesUIServiceImpl::applyTXO,
                PanStatistiquesUIServiceImpl::applyINDO
            );
        }

        dto.setDebutLegislature(
            form.getDebutLegislature() != null
                ? form.getDebutLegislature().getTime()
                : paramAn.getLegisEnCours().getDebutLegislature()
        );
        dto.setOptionsLegislatures(paramAn.getLegislatures());
        dto.setLegislature(ObjectHelper.requireNonNullElse(form.getLegislatures(), paramAn.getLegislatureEnCours()));

        if (tab.equals(PanConstants.Tab.TAB_STATISTIQUES_PUBLICATION)) {
            dto.setAnnee(ObjectHelper.requireNonNullElse(form.getAnnee(), Calendar.getInstance().get(Calendar.YEAR)));
            dto.setAnneeDebut(PanUIServiceLocator.getActiviteNormativeStatsUIService().getAnneeDeDepart(context));
            dto.setPeriode(ObjectHelper.requireNonNullElse(form.getPeriode(), "M"));
            dto.setMois(
                ObjectHelper.requireNonNullElse(form.getMois(), Calendar.getInstance().get(Calendar.MONTH) + 1)
            );
        }

        return dto;
    }

    @Override
    public PanStatistiquesParamDTO getStatParams(SpecificContext context) {
        ParametrageAN paramAn = SolonEpgServiceLocator
            .getSolonEpgParametreService()
            .getDocAnParametre(context.getSession());

        PanStatistiquesParamDTO dto = new PanStatistiquesParamDTO();

        dto.setLegislatures(
            paramAn.getLegislatures().stream().map(it -> new SelectValueDTO(it, it)).collect(Collectors.toList())
        );
        dto.setLegislaturesRaw(paramAn.getLegislatures());
        dto.setLegislatureEnCours(paramAn.getLegislatureEnCours());

        PanStatistiquesParamDTO.PanLegislatureDTO legisEnCours = dto.getLegisEnCours();
        getStatParamsCurrentLegis(legisEnCours, paramAn);

        PanStatistiquesParamDTO.PanLegislatureDTO legisPrecedente = dto.getLegisPrec();
        getStatParamsPrevLegis(legisPrecedente, paramAn);

        return dto;
    }

    private void getStatParamsCurrentLegis(
        PanStatistiquesParamDTO.PanLegislatureDTO legisEnCours,
        ParametrageAN paramAn
    ) {
        legisEnCours.setDebutLegislature(paramAn.getLegislatureEnCoursDateDebut());
        legisEnCours.setPromulgationLoisBS(
            ImmutablePair.of(paramAn.getLECBSDatePromulBorneInf(), paramAn.getLECBSDatePromulBorneSup())
        );
        legisEnCours.setPublicationDecretsLoisBS(
            ImmutablePair.of(paramAn.getLECBSDatePubliBorneInf(), paramAn.getLECBSDatePubliBorneSup())
        );
        legisEnCours.setPromulgationOrdonnancesBS(
            ImmutablePair.of(paramAn.getLECBSDatePubliOrdoBorneInf(), paramAn.getLECBSDatePubliOrdoBorneSup())
        );
        legisEnCours.setPublicationDecretsOrdonnancesBS(
            ImmutablePair.of(paramAn.getLECBSDatePubliOrdoBorneInf(), paramAn.getLECBSDatePubliOrdoBorneSup())
        );
        legisEnCours.setPromulgationLoisTXD(
            ImmutablePair.of(paramAn.getLECTauxSPDatePromulBorneInf(), paramAn.getLECTauxSPDatePromulBorneSup())
        );
        legisEnCours.setPublicationDecretsLoisTXD(
            ImmutablePair.of(paramAn.getLECTauxSPDatePubliBorneInf(), paramAn.getLECTauxSPDatePubliBorneSup())
        );
        legisEnCours.setPromulgationLoisTX(
            ImmutablePair.of(
                paramAn.getLECTauxDebutLegisDatePromulBorneInf(),
                paramAn.getLECTauxDebutLegisDatePromulBorneSup()
            )
        );
        legisEnCours.setPublicationDecretsLoisTX(
            ImmutablePair.of(
                paramAn.getLECTauxDebutLegisDatePubliBorneInf(),
                paramAn.getLECTauxDebutLegisDatePubliBorneSup()
            )
        );
        legisEnCours.setPromulgationOrdonnancesTXD(
            ImmutablePair.of(paramAn.getLECTauxSPDatePubliOrdoBorneInf(), paramAn.getLECTauxSPDatePubliOrdoBorneSup())
        );
        legisEnCours.setPublicationDecretsOrdonnancesTXD(
            ImmutablePair.of(
                paramAn.getLECTauxSPDatePubliDecretOrdoBorneInf(),
                paramAn.getLECTauxSPDatePubliDecretOrdoBorneSup()
            )
        );
        legisEnCours.setPromulgationOrdonnancesTX(
            ImmutablePair.of(
                paramAn.getLECTauxDebutLegisDatePubliOrdoBorneInf(),
                paramAn.getLECTauxDebutLegisDatePubliOrdoBorneSup()
            )
        );
        legisEnCours.setPublicationDecretsOrdonnancesTX(
            ImmutablePair.of(
                paramAn.getLECTauxDebutLegisDatePubliDecretOrdoBorneInf(),
                paramAn.getLECTauxDebutLegisDatePubliDecretOrdoBorneSup()
            )
        );
    }

    private void getStatParamsPrevLegis(
        PanStatistiquesParamDTO.PanLegislatureDTO legisPrecedente,
        ParametrageAN paramAn
    ) {
        legisPrecedente.setDebutLegislature(paramAn.getLegislaturePrecedenteDateDebut());
        legisPrecedente.setPromulgationLoisBS(
            ImmutablePair.of(paramAn.getLPBSDatePromulBorneInf(), paramAn.getLPBSDatePromulBorneSup())
        );
        legisPrecedente.setPublicationDecretsLoisBS(
            ImmutablePair.of(paramAn.getLPBSDatePubliBorneInf(), paramAn.getLPBSDatePubliBorneSup())
        );
        legisPrecedente.setPromulgationOrdonnancesBS(
            ImmutablePair.of(paramAn.getLPBSDatePubliOrdoBorneInf(), paramAn.getLPBSDatePubliOrdoBorneSup())
        );
        legisPrecedente.setPublicationDecretsOrdonnancesBS(
            ImmutablePair.of(paramAn.getLPBSDatePubliOrdoBorneInf(), paramAn.getLPBSDatePubliOrdoBorneSup())
        );
        legisPrecedente.setPromulgationLoisTXD(
            ImmutablePair.of(paramAn.getLPTauxSPDatePromulBorneInf(), paramAn.getLPTauxSPDatePromulBorneSup())
        );
        legisPrecedente.setPublicationDecretsLoisTXD(
            ImmutablePair.of(paramAn.getLPTauxSPDatePubliBorneInf(), paramAn.getLPTauxSPDatePubliBorneSup())
        );
        legisPrecedente.setPromulgationLoisTX(
            ImmutablePair.of(
                paramAn.getLPTauxDebutLegisDatePromulBorneInf(),
                paramAn.getLPTauxDebutLegisDatePromulBorneSup()
            )
        );
        legisPrecedente.setPublicationDecretsLoisTX(
            ImmutablePair.of(
                paramAn.getLPTauxDebutLegisDatePubliBorneInf(),
                paramAn.getLPTauxDebutLegisDatePubliBorneSup()
            )
        );
        legisPrecedente.setPromulgationOrdonnancesTXD(
            ImmutablePair.of(paramAn.getLPTauxSPDatePubliOrdoBorneInf(), paramAn.getLPTauxSPDatePubliOrdoBorneSup())
        );
        legisPrecedente.setPublicationDecretsOrdonnancesTXD(
            ImmutablePair.of(
                paramAn.getLPTauxSPDatePubliDecretOrdoBorneInf(),
                paramAn.getLPTauxSPDatePubliDecretOrdoBorneSup()
            )
        );
        legisPrecedente.setPromulgationOrdonnancesTX(
            ImmutablePair.of(
                paramAn.getLPTauxDebutLegisDatePubliOrdoBorneInf(),
                paramAn.getLPTauxDebutLegisDatePubliOrdoBorneSup()
            )
        );
        legisPrecedente.setPublicationDecretsOrdonnancesTX(
            ImmutablePair.of(
                paramAn.getLPTauxDebutLegisDatePubliDecretOrdoBorneInf(),
                paramAn.getLPTauxDebutLegisDatePubliDecretOrdoBorneSup()
            )
        );
    }

    @Override
    public void updateStatParams(
        PanStatistiquesParamForm legisForm,
        PanStatistiquesParamLegisEnCoursForm legisEnCoursForm,
        PanStatistiquesParamLegisPrecForm legisPrecForm,
        SpecificContext context
    ) {
        ParametrageAN paramAn = SolonEpgServiceLocator
            .getSolonEpgParametreService()
            .getDocAnParametre(context.getSession());

        // sécurité back : on est sensé pouvoir ajouter des législatures mais pas en supprimer
        Set<String> legislatures = new HashSet<>(paramAn.getLegislatures());
        legislatures.addAll(legisForm.getLegislatures());

        paramAn.setLegislatures(new ArrayList<>(legislatures));
        paramAn.setLegislatureEnCours(legisForm.getLegislatureEnCours());

        updateStatParamsCurrentLegis(paramAn, legisEnCoursForm);
        updateStatParamsPrevLegis(paramAn, legisPrecForm);
        paramAn.save(context.getSession());

        context
            .getMessageQueue()
            .addToastSuccess(ResourceHelper.getString("pan.stats.parametrage.sauvegarde.ok.label"));
    }

    private void updateStatParamsCurrentLegis(
        ParametrageAN paramAn,
        PanStatistiquesParamLegisEnCoursForm legisEnCoursForm
    ) {
        paramAn.setLegislatureEnCoursDateDebut(legisEnCoursForm.getDebutLegislatureEnCours().getTime());
        paramAn.setLECBSDatePromulBorneInf(legisEnCoursForm.getDebutPromulgationLoisBSLegisEnCours().getTime());
        paramAn.setLECBSDatePromulBorneSup(legisEnCoursForm.getFinPromulgationLoisBSLegisEnCours().getTime());
        paramAn.setLECBSDatePubliBorneInf(legisEnCoursForm.getDebutPublicationDecretsLoisBSLegisEnCours().getTime());
        paramAn.setLECBSDatePubliBorneSup(legisEnCoursForm.getFinPublicationDecretsLoisBSLegisEnCours().getTime());
        paramAn.setLECBSDatePubliOrdoBorneInf(
            legisEnCoursForm.getDebutPromulgationOrdonnancesBSLegisEnCours().getTime()
        );
        paramAn.setLECBSDatePubliOrdoBorneSup(legisEnCoursForm.getFinPromulgationOrdonnancesBSLegisEnCours().getTime());
        paramAn.setLECBSDatePubliDecretOrdoBorneInf(
            legisEnCoursForm.getDebutPublicationDecretsOrdonnancesBSLegisEnCours().getTime()
        );
        paramAn.setLECBSDatePubliDecretOrdoBorneSup(
            legisEnCoursForm.getFinPublicationDecretsOrdonnancesBSLegisEnCours().getTime()
        );

        paramAn.setLECTauxSPDatePromulBorneInf(legisEnCoursForm.getDebutPromulgationLoisTXDLegisEnCours().getTime());
        paramAn.setLECTauxSPDatePromulBorneSup(legisEnCoursForm.getFinPromulgationLoisTXDLegisEnCours().getTime());
        paramAn.setLECTauxSPDatePubliBorneInf(
            legisEnCoursForm.getDebutPublicationDecretsLoisTXDLegisEnCours().getTime()
        );
        paramAn.setLECTauxDebutLegisDatePromulBorneInf(
            legisEnCoursForm.getDebutPromulgationLoisTXLegisEnCours().getTime()
        );
        paramAn.setLECTauxDebutLegisDatePromulBorneSup(
            legisEnCoursForm.getFinPromulgationLoisTXLegisEnCours().getTime()
        );
        paramAn.setLECTauxDebutLegisDatePubliBorneInf(
            legisEnCoursForm.getDebutPublicationDecretsLoisTXLegisEnCours().getTime()
        );
        paramAn.setLECTauxDebutLegisDatePubliBorneSup(
            legisEnCoursForm.getFinPublicationDecretsLoisTXLegisEnCours().getTime()
        );

        paramAn.setLECTauxSPDatePubliOrdoBorneInf(
            legisEnCoursForm.getDebutPromulgationOrdonnancesTXDLegisEnCours().getTime()
        );
        paramAn.setLECTauxSPDatePubliOrdoBorneSup(
            legisEnCoursForm.getFinPromulgationOrdonnancesTXDLegisEnCours().getTime()
        );
        paramAn.setLECTauxSPDatePubliDecretOrdoBorneInf(
            legisEnCoursForm.getDebutPublicationDecretsOrdonnancesTXDLegisEnCours().getTime()
        );
        paramAn.setLECTauxSPDatePubliDecretOrdoBorneSup(
            legisEnCoursForm.getFinPublicationDecretsOrdonnancesTXDLegisEnCours().getTime()
        );
        paramAn.setLECTauxDebutLegisDatePubliOrdoBorneInf(
            legisEnCoursForm.getDebutPromulgationOrdonnancesTXLegisEnCours().getTime()
        );
        paramAn.setLECTauxDebutLegisDatePubliOrdoBorneSup(
            legisEnCoursForm.getFinPromulgationOrdonnancesTXLegisEnCours().getTime()
        );
        paramAn.setLECTauxDebutLegisDatePubliDecretOrdoBorneInf(
            legisEnCoursForm.getDebutPublicationDecretsOrdonnancesTXLegisEnCours().getTime()
        );
        paramAn.setLECTauxDebutLegisDatePubliDecretOrdoBorneSup(
            legisEnCoursForm.getFinPublicationDecretsOrdonnancesTXLegisEnCours().getTime()
        );
    }

    private void updateStatParamsPrevLegis(ParametrageAN paramAn, PanStatistiquesParamLegisPrecForm legisPrecForm) {
        paramAn.setLegislaturePrecedenteDateDebut(legisPrecForm.getDebutLegislaturePrecedente().getTime());
        paramAn.setLPBSDatePromulBorneInf(legisPrecForm.getDebutPromulgationLoisBSLegisPrecedente().getTime());
        paramAn.setLPBSDatePromulBorneSup(legisPrecForm.getFinPromulgationLoisBSLegisPrecedente().getTime());
        paramAn.setLPBSDatePubliBorneInf(legisPrecForm.getDebutPublicationDecretsLoisBSLegisPrecedente().getTime());
        paramAn.setLPBSDatePubliBorneSup(legisPrecForm.getFinPublicationDecretsLoisBSLegisPrecedente().getTime());
        paramAn.setLPBSDatePubliOrdoBorneInf(
            legisPrecForm.getDebutPromulgationOrdonnancesBSLegisPrecedente().getTime()
        );
        paramAn.setLPBSDatePubliOrdoBorneSup(legisPrecForm.getFinPromulgationOrdonnancesBSLegisPrecedente().getTime());
        paramAn.setLPBSDatePubliDecretOrdoBorneInf(
            legisPrecForm.getDebutPublicationDecretsOrdonnancesBSLegisPrecedente().getTime()
        );
        paramAn.setLPBSDatePubliDecretOrdoBorneSup(
            legisPrecForm.getFinPublicationDecretsOrdonnancesBSLegisPrecedente().getTime()
        );

        paramAn.setLPTauxSPDatePromulBorneInf(legisPrecForm.getDebutPromulgationLoisTXDLegisPrecedente().getTime());
        paramAn.setLPTauxSPDatePromulBorneSup(legisPrecForm.getFinPromulgationLoisTXDLegisPrecedente().getTime());
        paramAn.setLPTauxSPDatePubliBorneInf(
            legisPrecForm.getDebutPublicationDecretsLoisTXDLegisPrecedente().getTime()
        );
        paramAn.setLPTauxSPDatePubliBorneSup(legisPrecForm.getFinPublicationDecretsLoisTXDLegisPrecedente().getTime());
        paramAn.setLPTauxDebutLegisDatePromulBorneInf(
            legisPrecForm.getDebutPromulgationLoisTXLegisPrecedente().getTime()
        );
        paramAn.setLPTauxDebutLegisDatePromulBorneSup(
            legisPrecForm.getFinPromulgationLoisTXLegisPrecedente().getTime()
        );
        paramAn.setLPTauxDebutLegisDatePubliBorneInf(
            legisPrecForm.getDebutPublicationDecretsLoisTXLegisPrecedente().getTime()
        );
        paramAn.setLPTauxDebutLegisDatePubliBorneSup(
            legisPrecForm.getFinPublicationDecretsLoisTXLegisPrecedente().getTime()
        );

        paramAn.setLPTauxSPDatePubliOrdoBorneInf(
            legisPrecForm.getDebutPromulgationOrdonnancesTXDLegisPrecedente().getTime()
        );
        paramAn.setLPTauxSPDatePubliOrdoBorneSup(
            legisPrecForm.getFinPromulgationOrdonnancesTXDLegisPrecedente().getTime()
        );
        paramAn.setLPTauxSPDatePubliDecretOrdoBorneInf(
            legisPrecForm.getDebutPublicationDecretsOrdonnancesTXDLegisPrecedente().getTime()
        );
        paramAn.setLPTauxSPDatePubliDecretOrdoBorneSup(
            legisPrecForm.getFinPublicationDecretsOrdonnancesTXDLegisPrecedente().getTime()
        );
        paramAn.setLPTauxDebutLegisDatePubliOrdoBorneInf(
            legisPrecForm.getDebutPromulgationOrdonnancesTXLegisPrecedente().getTime()
        );
        paramAn.setLPTauxDebutLegisDatePubliOrdonnanceBorneSup(
            legisPrecForm.getFinPromulgationOrdonnancesTXLegisPrecedente().getTime()
        );
        paramAn.setLPTauxDebutLegisDatePubliDecretOrdoBorneInf(
            legisPrecForm.getDebutPublicationDecretsOrdonnancesTXLegisPrecedente().getTime()
        );
        paramAn.setLPTauxDebutLegisDatePubliDecretOrdoBorneSup(
            legisPrecForm.getFinPublicationDecretsOrdonnancesTXLegisPrecedente().getTime()
        );
    }

    private void applySection(
        PanBirtStats stats,
        PanBirtParamForm form,
        PanBirtDTO dto,
        PanStatistiquesParamDTO.PanLegislatureDTO paramAn,
        TriConsumer<PanBirtParamForm, PanBirtDTO, PanStatistiquesParamDTO.PanLegislatureDTO> fctBS,
        TriConsumer<PanBirtParamForm, PanBirtDTO, PanStatistiquesParamDTO.PanLegislatureDTO> fctTX,
        TriConsumer<PanBirtParamForm, PanBirtDTO, PanStatistiquesParamDTO.PanLegislatureDTO> fctIND
    ) {
        PanConstants.Tab tab = stats.getTab();
        PanConstants.SubTab subtab = stats.getSubtab();

        switch (tab) {
            case TAB_TAB_MAITRE:
                if (subtab == PanConstants.SubTab.SUBTAB_TAB_M_BS) {
                    fctBS.apply(form, dto, paramAn);
                }
                break;
            case TAB_TAB_MAITRE_MIN:
                applyTabMaitreMin(subtab, fctTX, fctBS, form, dto, paramAn);
                break;
            case TAB_BS:
                fctBS.apply(form, dto, paramAn);
                break;
            case TAB_INDICATEURS:
            case TAB_INDICATEURS_LOLF:
                applyIndicateurs(subtab, fctTX, fctIND, form, dto, paramAn);
                break;
            default:
        }
    }

    private void applyTabMaitreMin(
        PanConstants.SubTab subtab,
        TriConsumer<PanBirtParamForm, PanBirtDTO, PanStatistiquesParamDTO.PanLegislatureDTO> fctTX,
        TriConsumer<PanBirtParamForm, PanBirtDTO, PanStatistiquesParamDTO.PanLegislatureDTO> fctBS,
        PanBirtParamForm form,
        PanBirtDTO dto,
        PanStatistiquesParamDTO.PanLegislatureDTO paramAn
    ) {
        if (
            subtab == PanConstants.SubTab.SUBTAB_TAB_MIN_TX_EXE_LOIS ||
            subtab == PanConstants.SubTab.SUBTAB_TAB_MIN_TX_EXE_ORDONNANCES
        ) {
            fctTX.apply(form, dto, paramAn);
        } else if (subtab == PanConstants.SubTab.SUBTAB_BS_PAR_MIN) {
            fctBS.apply(form, dto, paramAn);
        }
    }

    private void applyIndicateurs(
        PanConstants.SubTab subtab,
        TriConsumer<PanBirtParamForm, PanBirtDTO, PanStatistiquesParamDTO.PanLegislatureDTO> fctTX,
        TriConsumer<PanBirtParamForm, PanBirtDTO, PanStatistiquesParamDTO.PanLegislatureDTO> fctIND,
        PanBirtParamForm form,
        PanBirtDTO dto,
        PanStatistiquesParamDTO.PanLegislatureDTO paramAn
    ) {
        if (
            subtab == PanConstants.SubTab.SUBTAB_IND_TX_EXE_LOIS ||
            subtab == PanConstants.SubTab.SUBTAB_IND_TX_EXE_ORDONNANCES
        ) {
            fctTX.apply(form, dto, paramAn);
        } else if (
            subtab == PanConstants.SubTab.SUBTAB_IND_DELAI_MISE_APP_LOIS ||
            subtab == PanConstants.SubTab.SUBTAB_IND_TX_EXE_SESSION_LOIS ||
            subtab == PanConstants.SubTab.SUBTAB_IND_DELAI_MISE_APP_ORDONNANCES ||
            subtab == PanConstants.SubTab.SUBTAB_IND_TX_EXE_SESSION_ORDONNANCES
        ) {
            fctIND.apply(form, dto, paramAn);
        }
    }

    private static void applyBSL(
        PanBirtParamForm form,
        PanBirtDTO dto,
        PanStatistiquesParamDTO.PanLegislatureDTO paramAn
    ) {
        applyDate(paramAn.getPromulgationLoisBS(), paramAn.getPublicationDecretsLoisBS(), form, dto);
    }

    private static void applyBSO(
        PanBirtParamForm form,
        PanBirtDTO dto,
        PanStatistiquesParamDTO.PanLegislatureDTO paramAn
    ) {
        applyDate(paramAn.getPromulgationOrdonnancesBS(), paramAn.getPublicationDecretsOrdonnancesBS(), form, dto);
    }

    private static void applyTXL(
        PanBirtParamForm form,
        PanBirtDTO dto,
        PanStatistiquesParamDTO.PanLegislatureDTO paramAn
    ) {
        applyDate(paramAn.getPromulgationLoisTX(), paramAn.getPublicationDecretsLoisTX(), form, dto);
    }

    private static void applyTXO(
        PanBirtParamForm form,
        PanBirtDTO dto,
        PanStatistiquesParamDTO.PanLegislatureDTO paramAn
    ) {
        applyDate(paramAn.getPromulgationOrdonnancesTX(), paramAn.getPublicationDecretsOrdonnancesTX(), form, dto);
    }

    private static void applyINDL(
        PanBirtParamForm form,
        PanBirtDTO dto,
        PanStatistiquesParamDTO.PanLegislatureDTO paramAn
    ) {
        applyDate(paramAn.getPromulgationLoisTXD(), paramAn.getPublicationDecretsLoisTXD(), form, dto);
    }

    private static void applyINDO(
        PanBirtParamForm form,
        PanBirtDTO dto,
        PanStatistiquesParamDTO.PanLegislatureDTO paramAn
    ) {
        applyDate(paramAn.getPromulgationOrdonnancesTXD(), paramAn.getPublicationDecretsOrdonnancesTXD(), form, dto);
    }

    private static void applyDate(
        Pair<Date, Date> date1,
        Pair<Date, Date> date2,
        PanBirtParamForm form,
        PanBirtDTO dto
    ) {
        if (form.getDebutIntervalle1() != null) {
            dto.setDebutIntervalle1(form.getDebutIntervalle1().getTime());
        } else {
            dto.setDebutIntervalle1(date1.getLeft());
        }

        if (form.getFinIntervalle1() != null) {
            dto.setFinIntervalle1(form.getFinIntervalle1().getTime());
        } else {
            dto.setFinIntervalle1(date1.getRight());
        }

        if (form.getDebutIntervalle2() != null) {
            dto.setDebutIntervalle2(form.getDebutIntervalle2().getTime());
        } else {
            dto.setDebutIntervalle2(date2.getLeft());
        }

        if (form.getFinIntervalle2() != null) {
            dto.setFinIntervalle2(form.getFinIntervalle2().getTime());
        } else {
            dto.setFinIntervalle2(date2.getRight());
        }
    }

    @Override
    public PANExportParametreDTO prepareExport(PanStatistiquesParamLegisEnCoursForm legisForm) {
        PANExportParametreDTO dto = new PANExportParametreDTO();
        dto.setBilanPromulgationDebut(legisForm.getDebutPromulgationLoisBSLegisEnCours().getTime());
        dto.setBilanPromulgationFin(legisForm.getFinPromulgationLoisBSLegisEnCours().getTime());
        dto.setBilanPublicationDebut(legisForm.getDebutPublicationDecretsLoisBSLegisEnCours().getTime());
        dto.setBilanPublicationFin(legisForm.getFinPublicationDecretsLoisBSLegisEnCours().getTime());

        dto.setBilanPublicationDebutOrdo(legisForm.getDebutPromulgationOrdonnancesBSLegisEnCours().getTime());
        dto.setBilanPublicationFinOrdo(legisForm.getFinPromulgationOrdonnancesBSLegisEnCours().getTime());
        dto.setBilanPublicationDebutOrdoDecret(
            legisForm.getDebutPublicationDecretsOrdonnancesBSLegisEnCours().getTime()
        );
        dto.setBilanPublicationFinOrdoDecret(legisForm.getFinPublicationDecretsOrdonnancesBSLegisEnCours().getTime());

        dto.setTauxPromulgationDebut(legisForm.getDebutPromulgationLoisTXLegisEnCours().getTime());
        dto.setTauxPromulgationFin(legisForm.getFinPromulgationLoisTXLegisEnCours().getTime());
        dto.setTauxPublicationDebut(legisForm.getDebutPublicationDecretsLoisTXLegisEnCours().getTime());
        dto.setTauxPublicationFin(legisForm.getFinPublicationDecretsLoisTXLegisEnCours().getTime());

        dto.setTauxDLPublicationOrdosDebut(legisForm.getDebutPromulgationOrdonnancesTXLegisEnCours().getTime());
        dto.setTauxDLPublicationOrdosFin(legisForm.getFinPromulgationOrdonnancesTXLegisEnCours().getTime());
        dto.setTauxDLPublicationDecretsOrdosDebut(
            legisForm.getDebutPublicationDecretsOrdonnancesTXLegisEnCours().getTime()
        );
        dto.setTauxDLPublicationDecretsOrdosFin(
            legisForm.getFinPublicationDecretsOrdonnancesTXLegisEnCours().getTime()
        );

        dto.setLolfPromulgationDebut(legisForm.getDebutPromulgationLoisTXDLegisEnCours().getTime());
        dto.setLolfPromulgationFin(legisForm.getFinPromulgationLoisTXDLegisEnCours().getTime());
        dto.setLolfPublicationDebut(legisForm.getDebutPublicationDecretsLoisTXDLegisEnCours().getTime());
        dto.setLolfPublicationFin(legisForm.getFinPublicationDecretsLoisTXDLegisEnCours().getTime());

        dto.setTauxSPPublicationOrdosDebut(legisForm.getDebutPromulgationOrdonnancesTXDLegisEnCours().getTime());
        dto.setTauxSPPublicationOrdosFin(legisForm.getFinPromulgationOrdonnancesTXDLegisEnCours().getTime());
        dto.setTauxSPPublicationDecretsOrdosDebut(
            legisForm.getDebutPublicationDecretsOrdonnancesTXDLegisEnCours().getTime()
        );
        dto.setTauxSPPublicationDecretsOrdosFin(
            legisForm.getFinPublicationDecretsOrdonnancesTXDLegisEnCours().getTime()
        );

        dto.setDateDebutLegislature(legisForm.getDebutLegislatureEnCours().getTime());

        return dto;
    }

    @Override
    public List<SuiviLibreReportDTO> getReportsSuiviLibre(SpecificContext context) {
        String section = context.getFromContextData(PanContextDataKey.SUIVI_CURRENT_SECTION);
        String tab = context.getFromContextData(PanContextDataKey.SUIVI_CURRENT_TAB);
        List<ANReportEnum> list = ANReportEnum.getSuiviReport(section, tab);

        return list
            .stream()
            .map(
                it ->
                    toSuiviLibreReportDTO(
                        context,
                        it.getFilename(),
                        it.getSuiviLibelle(),
                        it.isForMinistere(),
                        it.isForLoi() || it.isForTexte()
                    )
            )
            .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<ANReportEnum>> getAdditionnalReportsSuiviLibre(SpecificContext context) {
        String section = context.getFromContextData(PanContextDataKey.SUIVI_CURRENT_SECTION);
        Map<String, List<ANReportEnum>> res = new HashMap<>();
        List<ANReportEnum> list = ANReportEnum.getOtherSuiviReportBySection(section);
        list.forEach(
            (ANReportEnum e) -> {
                String key = e.getSuiviExtradata();

                if (res.containsKey(key)) {
                    res.get(key).add(e);
                } else {
                    res.put(key, new ArrayList<>(Collections.singletonList(e)));
                }
            }
        );
        return res;
    }

    private SuiviLibreReportDTO toSuiviLibreReportDTO(
        SpecificContext context,
        String filename,
        String libelle,
        boolean forMinistere,
        boolean forElm
    ) {
        SuiviLibreReportDTO report = new SuiviLibreReportDTO();

        report.setTitre(libelle);

        String link = filename;

        if (forMinistere) {
            link = link + "-" + context.getFromContextData(PanContextDataKey.MINISTERE_PILOTE_ID);
        } else if (forElm) {
            link = link + "-" + context.getFromContextData(PanContextDataKey.NUMERO_NOR);
        }
        report.setLink(link + ".html");

        report.setAvailable(isReportPublished(context, report.getLink()));

        return report;
    }

    @Override
    public Date getSuiviDateActualisation(SpecificContext context) {
        boolean legislatureEnCours = context.getFromContextData(PanContextDataKey.LEGISLATURE_EN_COURS);
        String section = context.getFromContextData(PanContextDataKey.CURRENT_SECTION);
        ActiviteNormativeEnum anEnum = ActiviteNormativeEnum.getById(section);

        String generatedReportPath;
        if (legislatureEnCours) {
            generatedReportPath = SolonEpgServiceLocator.getActiviteNormativeService().getPathDirANStatistiquesPublie();
        } else {
            generatedReportPath =
                SolonEpgServiceLocator
                    .getActiviteNormativeService()
                    .getPathDirANStatistiquesLegisPrecPublie(context.getSession());
        }

        switch (anEnum) {
            case APPLICATION_DES_LOIS:
            case APPLICATION_DES_LOIS_MINISTERE:
                generatedReportPath += File.separator + "listeDesLois.html";
                break;
            case ORDONNANCES_38C:
                generatedReportPath += File.separator + "listeDesLoisHabilitation.html";
                break;
            case APPLICATION_DES_ORDONNANCES:
            case APPLICATION_DES_ORDONNANCES_MINISTERE:
                generatedReportPath += File.separator + "listeDesOrdonnances.html";
                break;
            default:
                return null;
        }

        File file = new File(generatedReportPath);
        if (file.exists()) {
            return new Date(file.lastModified());
        }
        return null;
    }

    @Override
    public List<SuiviLibreReportDTO> getMainReportsSuiviLibre(SpecificContext context) {
        context.putInContextData(PanContextDataKey.SUIVI_CURRENT_TAB, ANReportEnum.SuiviConstants.SUIVI_DETAILS);
        List<SuiviLibreReportDTO> list;

        if (
            ANReportEnum.SuiviConstants.SUIVI_CATEGORIE_SUIVI_ORDONNANCES.equals(
                context.getFromContextData(PanContextDataKey.SUIVI_CURRENT_SECTION).toString()
            )
        ) {
            list =
                Collections.singletonList(
                    toSuiviLibreReportDTO(
                        context,
                        "tableauDeSuiviHab",
                        ANReportEnum.SuiviConstants.SUIVI_LIBELLE_MESURES_APPLICATION_ORDONNANCE,
                        false,
                        true
                    )
                );
        } else {
            list = getReportsSuiviLibre(context);
            list.add(
                toSuiviLibreReportDTO(
                    context,
                    "repartitionParMinistere",
                    ANReportEnum.SuiviConstants.SUIVI_LIBELLE_REPARTITION_MESURES_MINISTERE,
                    false,
                    true
                )
            );
        }
        return list;
    }

    @Override
    public List<SuiviLibreReportDTO> getSecondaryReportsSuiviLibre(SpecificContext context) {
        List<SuiviLibreReportDTO> list = new ArrayList<>();
        if (
            !ANReportEnum.SuiviConstants.SUIVI_CATEGORIE_SUIVI_ORDONNANCES.equals(
                context.getFromContextData(PanContextDataKey.SUIVI_CURRENT_SECTION).toString()
            )
        ) {
            // Tableau de suivi n'a pas à être afficé apparement. Si jamais il suffit de décommenter ça.
            // list.add(
            //     toSuiviLibreReportDTO(
            //         context,
            //         "tableauDeSuivi",
            //         ANReportEnum.SuiviConstants.SUIVI_LIBELLE_MESURES_APPLICATION_LOI,
            //         false,
            //         true
            //     )
            // );
            context.putInContextData(
                PanContextDataKey.SUIVI_CURRENT_TAB,
                ANReportEnum.SuiviConstants.SUIVI_ACC_DETAILS
            );
            list.addAll(getReportsSuiviLibre(context));
        }
        return list;
    }

    @Override
    public boolean isReportPublished(SpecificContext context, String filename) {
        boolean legislatureEnCours = context.getFromContextData(PanContextDataKey.LEGISLATURE_EN_COURS);
        String dir = legislatureEnCours
            ? SolonEpgServiceLocator.getActiviteNormativeService().getPathDirANStatistiquesPublie()
            : SolonEpgServiceLocator
                .getActiviteNormativeService()
                .getPathDirANStatistiquesLegisPrecPublie(context.getSession());

        File file = new File(dir, fr.dila.st.core.util.FileUtils.sanitizePathTraversal(filename));
        return file.exists();
    }
}
