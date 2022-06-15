package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.api.constant.SolonEpgStatistiquesConstant.STATISTIQUE_CATEGORY_GLOBAL;
import static fr.dila.solonepg.api.constant.SolonEpgStatistiquesConstant.STATISTIQUE_CATEGORY_SGG;
import static fr.dila.solonepg.api.constant.VocabularyConstants.VOCABULARY_STATUS_ARCHIVAGE;
import static fr.dila.solonepg.ui.enums.EpgContextDataKey.STATUT_ARCHIVAGE_DOSSIER_FORM;
import static fr.dila.solonepg.ui.enums.EpgUserSessionKey.DOSSIER_ARCHIVAGE_STAT_PP;
import static fr.dila.ss.api.birt.ReportProperty.SCALAR_TYPE;
import static fr.dila.ss.core.service.SSServiceLocator.getBirtGenerationService;
import static fr.dila.ss.ui.enums.SSContextDataKey.BIRT_OUTPUT_FORMAT;
import static fr.dila.st.core.service.STServiceLocator.getVocabularyService;
import static fr.dila.st.core.util.DateUtil.toDate;
import static fr.dila.st.ui.helper.STUIPageProviderHelper.getProviderFromContext;
import static fr.dila.st.ui.helper.UserSessionHelper.getUserSessionParameter;
import static java.io.File.separator;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.birt.constant.EpgBirtReportParams;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.enumeration.EpgRapportStatistique;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.StatistiquesService;
import fr.dila.solonepg.core.dto.DossierArchivageStatDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.EpgBirtReportList;
import fr.dila.solonepg.ui.bean.StatutArchivageDossierDTO;
import fr.dila.solonepg.ui.bean.StatutArchivageDossierList;
import fr.dila.solonepg.ui.contentview.DossierArchivageStatPageProvider;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.services.EpgStatistiquesUIService;
import fr.dila.solonepg.ui.th.bean.StatutArchivageDossierForm;
import fr.dila.solonepg.ui.th.model.bean.EpgStatForm;
import fr.dila.ss.api.birt.BirtReport;
import fr.dila.ss.api.birt.ReportProperty;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.ss.ui.services.impl.AbstractSSStatistiquesUIServiceImpl;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.VocabularyService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.NXCore;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.InlineEventContext;
import org.nuxeo.ecm.core.security.SecurityService;

public class EpgStatistiquesUIServiceImpl
    extends AbstractSSStatistiquesUIServiceImpl
    implements EpgStatistiquesUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(EpgStatistiquesUIServiceImpl.class);

    public Map<Integer, StatistiquesItem> getStatistquesMap(SpecificContext context) {
        Map<Integer, StatistiquesItem> statsMap = UserSessionHelper.getUserSessionParameter(context, "statsMap");
        if (statsMap != null) {
            return statsMap;
        }

        StatMapBuilder builder = new StatMapBuilder();

        NuxeoPrincipal principal = context.getSession().getPrincipal();

        if (this.canViewSggStat(principal)) {
            builder.setCurrentCategory(STATISTIQUE_CATEGORY_SGG);
            builder.putStat(EpgRapportStatistique.LISTE_TEXTES_EN_COURS_COMMUNICATION_VALIDATION);
            builder.putStat(EpgRapportStatistique.LISTE_ORIGINAUX_TEXTES_ARRIVES_SGG);
            builder.putStat(EpgRapportStatistique.LISTE_TEXTES_SIGNATURE_PREMIER_MINISTRE_PRESIDENT);
            builder.putStat(EpgRapportStatistique.LISTE_TEXTES_AMPLIATIONS);
            builder.putStat(EpgRapportStatistique.LISTE_TEXTES_CHEMINANT_DEMATERIALISEE);
            builder.putStat(EpgRapportStatistique.LISTE_DOSSIERS_TRANSMIS_DILA_POUR_JO_BO);
            builder.putStat(EpgRapportStatistique.LISTE_DOSSIERS_TRANSMIS_DILA_POUR_EPR_VEC);
            builder.putStat(EpgRapportStatistique.LISTE_EPREUVES_PAR_VECT_PUBLI);
        }
        builder.setCurrentCategory(STATISTIQUE_CATEGORY_GLOBAL);
        builder.putStat(EpgRapportStatistique.TEXTES_CORBEILLES_TRAVAILLES);
        builder.putStat(EpgRapportStatistique.NOMBRE_ACTES_TYPE, true, true);
        builder.putStat(EpgRapportStatistique.NOMBRE_ACTES_CREES_MINISTERE, true, true);
        builder.putStat(EpgRapportStatistique.NOMBRE_ACTES_CREES_DIRECTION, true, true);
        builder.putStat(EpgRapportStatistique.NOMBRE_ACTES_CREES_POSTE, true, true);
        builder.putStat(EpgRapportStatistique.TAUX_INDEXATION_TEXTE);
        builder.putStat(EpgRapportStatistique.TAUX_TEXTES_RETOUR_SGG);
        builder.putStat(EpgRapportStatistique.TAUX_TEXTES_RETOUR_BUREAU);
        builder.putStat(EpgRapportStatistique.DOSSIER_ARCHIVAGE, false, false);

        statsMap = builder.getMap();
        return statsMap;
    }

    @Override
    public Map<Integer, StatistiquesItem> getStatistquesMapSgg(SpecificContext context) {
        Map<Integer, StatistiquesItem> statsSggMap = UserSessionHelper.getUserSessionParameter(context, "statsSggMap");
        if (statsSggMap != null) {
            return statsSggMap;
        }

        statsSggMap = getStatistquesMapByType(context, STATISTIQUE_CATEGORY_SGG);
        UserSessionHelper.putUserSessionParameter(context, "statsSggMap", statsSggMap);
        return statsSggMap;
    }

    @Override
    public Map<Integer, StatistiquesItem> getStatistquesMapGlobal(SpecificContext context) {
        Map<Integer, StatistiquesItem> statsGlobalMap = UserSessionHelper.getUserSessionParameter(
            context,
            "statsGlobalMap"
        );
        if (statsGlobalMap != null) {
            return statsGlobalMap;
        }

        statsGlobalMap = getStatistquesMapByType(context, STATISTIQUE_CATEGORY_GLOBAL);
        UserSessionHelper.putUserSessionParameter(context, "statsGlobalMap", statsGlobalMap);
        return statsGlobalMap;
    }

    private Map<Integer, StatistiquesItem> getStatistquesMapByType(SpecificContext context, String type) {
        Map<Integer, StatistiquesItem> statsMap = getStatistquesMap(context);
        return statsMap
            .entrySet()
            .stream()
            .filter(i -> type.equals(i.getValue().getCategory()))
            .collect((toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    @Override
    public EpgBirtReportList getStatistquesMapSggAsList(SpecificContext context) {
        if (!canViewSggStat(context.getSession().getPrincipal())) {
            return null;
        }
        return getMapAsList(getStatistquesMapSgg(context));
    }

    @Override
    public EpgBirtReportList getStatistquesMapGlobalAsList(SpecificContext context) {
        return getMapAsList(getStatistquesMapGlobal(context));
    }

    private EpgBirtReportList getMapAsList(Map<Integer, StatistiquesItem> map) {
        EpgBirtReportList list = new EpgBirtReportList();
        list.setNbTotal(map.size());
        list.setListe(new LinkedList<>(map.values()));
        return list;
    }

    /**
     * Builder pour la map des statistiques, afin de rendre le code plus lisible et maintenable
     **/
    private static class StatMapBuilder {
        private final Map<Integer, StatistiquesItem> map;
        private String currentCategory;

        public StatMapBuilder() {
            map = new TreeMap<>();
        }

        public void setCurrentCategory(String cat) {
            currentCategory = cat;
        }

        public void putStat(EpgRapportStatistique rapp) {
            putStat(rapp, true, false);
        }

        public void putStat(EpgRapportStatistique rapp, boolean statBirt, boolean statGraphBirt) {
            int idRapport = rapp.ordinal();
            map.put(
                idRapport,
                new StatistiquesItem(
                    idRapport,
                    rapp.getBundleKey(),
                    rapp.getId(),
                    currentCategory,
                    "",
                    statBirt,
                    statGraphBirt
                )
            );
        }

        public Map<Integer, StatistiquesItem> getMap() {
            return map;
        }
    }

    private void typeActeToJson(Map<String, String> parametersMap, Map<String, String> resultList) {
        StringBuilder jsonBuilder = new StringBuilder();
        ActeService acteService = SolonEpgServiceLocator.getActeService();
        StringBuilder itemsBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : resultList.entrySet()) {
            String strCount = entry.getValue();
            String strId = acteService.getActe(entry.getKey()).getId();

            if (itemsBuilder.length() != 0) {
                itemsBuilder.append(",");
            }

            itemsBuilder.append("{");

            // Add ID to the Json
            itemsBuilder.append("\"");
            itemsBuilder.append("id");
            itemsBuilder.append("\"");
            itemsBuilder.append(":");
            itemsBuilder.append("\"");
            itemsBuilder.append(strId);
            itemsBuilder.append("\"");
            itemsBuilder.append(",");

            // Add count to the json
            itemsBuilder.append("\"");
            itemsBuilder.append("count");
            itemsBuilder.append("\"");
            itemsBuilder.append(":");
            itemsBuilder.append("\"");
            itemsBuilder.append(strCount);
            itemsBuilder.append("\"");
            itemsBuilder.append("}");
        }

        jsonBuilder.append("[");
        jsonBuilder.append(itemsBuilder.toString());
        jsonBuilder.append("]");

        parametersMap.put("TYPE_ACTE_COUNT", jsonBuilder.toString());
    }

    private Map<String, String> initParametersMap(SpecificContext context) {
        Map<String, String> parametersMap = new HashMap<>();

        NuxeoPrincipal principal = context.getSession().getPrincipal();
        String[] principals = SecurityService.getPrincipalsToCheck(principal);
        String principalStr = String.join("&", principals);
        parametersMap.put("USERS_PARAM", principalStr);

        String[] permissions = NXCore.getSecurityService().getPermissionsToCheck(SecurityConstants.BROWSE);

        String permissionsStr = String.join("&", permissions);
        parametersMap.put("PERMISSIONS_PARAM", permissionsStr);

        return parametersMap;
    }

    @Override
    public String getHtmlReportURL(SpecificContext context) {
        StatistiquesItem statItem = getCurrentStatItem(context);
        Map<String, String> params = initParametersMap(context);
        setStatReportParams(params, context, statItem);
        return genererStatistiquesHtml(params, statItem);
    }

    @Override
    public BirtReport getBirtReport(SpecificContext context) {
        StatistiquesItem statItem = getCurrentStatItem(context);
        if (
            STATISTIQUE_CATEGORY_SGG.equals(statItem.getCategory()) &&
            !canViewSggStat(context.getSession().getPrincipal())
        ) {
            return null;
        }
        String idRapportBirt = statItem.getIdRapportBirt();
        return SSServiceLocator.getBirtGenerationService().getReport(idRapportBirt);
    }

    private StatistiquesItem getCurrentStatItem(SpecificContext context) {
        Map<Integer, StatistiquesItem> map = getStatistquesMap(context);
        String statsId = context.getFromContextData(EpgContextDataKey.STATS_ID);
        return map.get(Integer.valueOf(statsId));
    }

    private String genererStatistiquesHtml(Map<String, String> parametersMap, StatistiquesItem statistiqueCourante) {
        String idRapportBirt = statistiqueCourante.getIdRapportBirt();
        String reportResultDir = getUserBirtResultDir();
        File dir = new File(reportResultDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        BirtReport birtReport = getBirtGenerationService().getReport(idRapportBirt);
        return generateReportFile(birtReport, parametersMap);
    }

    @Override
    public File genererStatistiques(SpecificContext context) {
        StatistiquesItem statItem = getCurrentStatItem(context);
        BirtOutputFormat format = context.getFromContextData(BIRT_OUTPUT_FORMAT);
        Map<String, String> params = getUserSessionParameter(context, EpgUserSessionKey.BIRT_REPORT_PARAMS);
        String tmpDir = System.getProperty("java.io.tmp");
        return getBirtGenerationService().generate(statItem.getIdRapportBirt(), null, format, params, tmpDir, true);
    }

    private String getUserBirtResultDir() {
        String birtReportdir = getGeneratedReportDirectoryValue();
        return String.join(separator, birtReportdir);
    }

    private void setDateDeDebut(
        StatistiquesItem statistiqueCourante,
        Map<String, String> parametersMap,
        Date dateDeDebut
    ) {
        if (dateDeDebut == null) {
            return;
        }
        String dateDeDebutStr = SolonDateConverter.DATE_SLASH.format(dateDeDebut);

        EpgRapportStatistique rapport = EpgRapportStatistique.fromInt(statistiqueCourante.getId());

        switch (rapport) {
            case LISTE_ORIGINAUX_TEXTES_ARRIVES_SGG:
            case NOMBRE_ACTES_TYPE:
                parametersMap.put(EpgBirtReportParams.DATEARRIVEPAPIER_BI_PARAM, dateDeDebutStr);
                break;
            case TAUX_TEXTES_RETOUR_SGG:
            case TAUX_TEXTES_RETOUR_BUREAU:
                parametersMap.put(EpgBirtReportParams.DATERETOUR_BI_PARAM, dateDeDebutStr);
                break;
            case NOMBRE_ACTES_CREES_MINISTERE:
            case NOMBRE_ACTES_CREES_DIRECTION:
            case NOMBRE_ACTES_CREES_POSTE:
                parametersMap.put(EpgBirtReportParams.DATEDOSSIERCREE_BI_PARAM, dateDeDebutStr);
                break;
            case LISTE_DOSSIERS_TRANSMIS_DILA_POUR_EPR_VEC:
            case LISTE_DOSSIERS_TRANSMIS_DILA_POUR_JO_BO:
                parametersMap.put(EpgBirtReportParams.DATEDOSSIER_PARAM, dateDeDebutStr);
                break;
            default:
                break;
        }
    }

    private void setDateDeFin(Map<String, String> parametersMap, StatistiquesItem statistiqueCourante, Date dateDeFin) {
        if (dateDeFin != null) {
            String dateDeFinStr = SolonDateConverter.DATE_SLASH.format(dateDeFin);

            EpgRapportStatistique rapport = EpgRapportStatistique.fromInt(statistiqueCourante.getId());

            switch (rapport) {
                case LISTE_ORIGINAUX_TEXTES_ARRIVES_SGG:
                case NOMBRE_ACTES_TYPE:
                    parametersMap.put(EpgBirtReportParams.DATEARRIVEPAPIER_BS_PARAM, dateDeFinStr);
                    break;
                case TAUX_TEXTES_RETOUR_SGG:
                case TAUX_TEXTES_RETOUR_BUREAU:
                    parametersMap.put(EpgBirtReportParams.DATERETOUR_BS_PARAM, dateDeFinStr);
                    break;
                case NOMBRE_ACTES_CREES_MINISTERE:
                case NOMBRE_ACTES_CREES_DIRECTION:
                case NOMBRE_ACTES_CREES_POSTE:
                    parametersMap.put(EpgBirtReportParams.DATEDOSSIERCREE_BS_PARAM, dateDeFinStr);
                    break;
                default:
                    break;
            }
        }
    }

    private void setMinistereId(
        Map<String, String> parametersMap,
        StatistiquesItem statistiqueCourante,
        String ministereId
    ) {
        if (StringUtils.isNotBlank(ministereId)) {
            EntiteNode ministereNode = STServiceLocator.getSTMinisteresService().getEntiteNode(ministereId);
            if (statistiqueCourante.getId() == EpgRapportStatistique.TAUX_TEXTES_RETOUR_SGG.ordinal()) {
                parametersMap.put(EpgBirtReportParams.MINISTEREATTACHE_PARAM, ministereId);
                parametersMap.put(EpgBirtReportParams.MINISTEREATTACHELABEL_PARAM, ministereNode.getLabel());
            } else {
                parametersMap.put(EpgBirtReportParams.MINISTERERESP_PARAM, ministereId);
                parametersMap.put(EpgBirtReportParams.MINISTERERESPLABEL_PARAM, ministereNode.getLabel());
            }
        } else {
            if (parametersMap.containsKey(EpgBirtReportParams.MINISTERERESP_PARAM)) {
                parametersMap.remove(EpgBirtReportParams.MINISTERERESP_PARAM);
                parametersMap.remove(EpgBirtReportParams.MINISTERERESPLABEL_PARAM);
            } else if (parametersMap.containsKey(EpgBirtReportParams.MINISTEREATTACHE_PARAM)) {
                parametersMap.remove(EpgBirtReportParams.MINISTEREATTACHE_PARAM);
                parametersMap.remove(EpgBirtReportParams.MINISTEREATTACHELABEL_PARAM);
            }
        }
    }

    private void setDirectionId(Map<String, String> parametersMap, String directionId) {
        if (StringUtils.isNotBlank(directionId)) {
            parametersMap.put(EpgBirtReportParams.DIRECTIONRESP_PARAM, directionId);
            OrganigrammeNode orgaNode = STServiceLocator
                .getSTUsAndDirectionService()
                .getUniteStructurelleNode(directionId);
            parametersMap.put(EpgBirtReportParams.DIRECTIONRESPLABEL_PARAM, orgaNode.getLabel());
        } else {
            if (parametersMap.containsKey(EpgBirtReportParams.DIRECTIONRESP_PARAM)) {
                parametersMap.remove(EpgBirtReportParams.DIRECTIONRESP_PARAM);
                parametersMap.remove(EpgBirtReportParams.DIRECTIONRESPLABEL_PARAM);
            }
        }
    }

    private void setVecteurPublication(Map<String, String> parametersMap, String vecteurPublication) {
        parametersMap.put(EpgBirtReportParams.VECTEURPUBLICATION_PARAM, vecteurPublication);
    }

    private void setPeriodeType(Map<String, String> parametersMap, String periodeType) {
        parametersMap.put(EpgBirtReportParams.TYPEPERIODE_PARAM, periodeType);
    }

    private void setPeriodeValue(Map<String, String> parametersMap, String periodeValue) {
        int periodeValueInt = Integer.parseInt(periodeValue.trim());

        if (periodeValueInt > 0) {
            parametersMap.put(EpgBirtReportParams.VALUEPERIODE_PARAM, String.valueOf(periodeValueInt));
        }
    }

    private void setPosteId(Map<String, String> parametersMap, StatistiquesItem statistiqueCourante, String posteId) {
        EpgRapportStatistique rapport = EpgRapportStatistique.fromInt(statistiqueCourante.getId());
        if (StringUtils.isNotBlank(posteId)) {
            OrganigrammeNode orgaNode = STServiceLocator.getSTPostesService().getPoste(posteId);
            switch (rapport) {
                case TEXTES_CORBEILLES_TRAVAILLES:
                    parametersMap.put(
                        EpgBirtReportParams.DISTRIBUTIONMAILBOXID_PARAM,
                        STConstant.PREFIX_POSTE + posteId
                    );
                    parametersMap.put(EpgBirtReportParams.DISTRIBUTIONMAILBOXLABEL_PARAM, orgaNode.getLabel());
                    break;
                case NOMBRE_ACTES_CREES_POSTE:
                    parametersMap.put(EpgBirtReportParams.POSTECREATOR_PARAM, STConstant.PREFIX_POSTE + posteId);
                    parametersMap.put(EpgBirtReportParams.POSTECREATORLABEL_PARAM, orgaNode.getLabel());
                    break;
                default:
                    break;
            }
        } else {
            switch (rapport) {
                case TEXTES_CORBEILLES_TRAVAILLES:
                    if (parametersMap.containsKey(EpgBirtReportParams.DISTRIBUTIONMAILBOXID_PARAM)) {
                        parametersMap.remove(EpgBirtReportParams.DISTRIBUTIONMAILBOXID_PARAM);
                        parametersMap.remove(EpgBirtReportParams.DISTRIBUTIONMAILBOXLABEL_PARAM);
                    }
                    break;
                case NOMBRE_ACTES_CREES_POSTE:
                    if (parametersMap.containsKey(EpgBirtReportParams.POSTECREATOR_PARAM)) {
                        parametersMap.remove(EpgBirtReportParams.POSTECREATOR_PARAM);
                        parametersMap.remove(EpgBirtReportParams.POSTECREATORLABEL_PARAM);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private String getRubrique(Map<String, String> parametersMap, StatistiquesItem statistiqueCourante) {
        String result = null;
        if (statistiqueCourante.getId() == EpgRapportStatistique.TAUX_INDEXATION_TEXTE.ordinal()) {
            result = parametersMap.get(EpgBirtReportParams.RUBRIQUES_PARAM);
        }

        return result;
    }

    private void setRubrique(Map<String, String> parametersMap, StatistiquesItem statistiqueCourante, String rubrique) {
        if (
            statistiqueCourante.getId() == EpgRapportStatistique.TAUX_INDEXATION_TEXTE.ordinal() &&
            StringUtils.isNotBlank(rubrique)
        ) {
            parametersMap.put(EpgBirtReportParams.RUBRIQUES_PARAM, rubrique.trim());
        }
    }

    private String getMotsCles(Map<String, String> parametersMap, StatistiquesItem statistiqueCourante) {
        String result = null;
        if (statistiqueCourante.getId() == EpgRapportStatistique.TAUX_INDEXATION_TEXTE.ordinal()) {
            result = parametersMap.get(EpgBirtReportParams.MOTSCLES_PARAM);
        }

        return result;
    }

    private void setMotsCles(Map<String, String> parametersMap, StatistiquesItem statistiqueCourante, String motsCles) {
        if (
            statistiqueCourante.getId() == EpgRapportStatistique.TAUX_INDEXATION_TEXTE.ordinal() &&
            StringUtils.isNotBlank(motsCles)
        ) {
            parametersMap.put(EpgBirtReportParams.MOTSCLES_PARAM, motsCles.trim());
        }
    }

    private String getChampsLibre(Map<String, String> parametersMap, StatistiquesItem statistiqueCourante) {
        String result = null;
        if (statistiqueCourante.getId() == EpgRapportStatistique.TAUX_INDEXATION_TEXTE.ordinal()) {
            result = parametersMap.get(EpgBirtReportParams.LIBRE_PARAM);
        }

        return result;
    }

    private void setChampsLibre(
        Map<String, String> parametersMap,
        StatistiquesItem statistiqueCourante,
        String champsLibre
    ) {
        if (
            StringUtils.isNotBlank(champsLibre) &&
            statistiqueCourante.getId() == EpgRapportStatistique.TAUX_INDEXATION_TEXTE.ordinal()
        ) {
            parametersMap.put(EpgBirtReportParams.LIBRE_PARAM, champsLibre.trim());
        }
    }

    private void setStatReportParams(
        Map<String, String> parametersMap,
        SpecificContext context,
        StatistiquesItem statistiqueCourante
    ) {
        EpgRapportStatistique rapport = EpgRapportStatistique.fromInt(statistiqueCourante.getId());
        switch (rapport) {
            case TAUX_TEXTES_RETOUR_SGG:
            case TAUX_TEXTES_RETOUR_BUREAU:
                setDistributionMailboxId(parametersMap);
                break;
            case LISTE_TEXTES_EN_COURS_COMMUNICATION_VALIDATION:
            case LISTE_TEXTES_CHEMINANT_DEMATERIALISEE:
                String ministeresParam = SolonEpgServiceLocator
                    .getStatsGenerationResultatService()
                    .getMinisteresListBirtReportParam(context.getSession());
                if (StringUtils.isNotBlank(ministeresParam)) {
                    parametersMap.put("MINISTERES_PARAM", ministeresParam);
                }
                break;
            default:
                break;
        }
        EpgStatForm form = context.getFromContextData(EpgContextDataKey.EPG_STAT_FORM);
        String ministereId = form.getMinistereId();
        if (ministereId != null) {
            setMinistereId(parametersMap, statistiqueCourante, ministereId);
        }
        String posteId = form.getPosteId();
        if (posteId != null) {
            setPosteId(parametersMap, statistiqueCourante, posteId);
        }
        String uniteId = form.getUniteId();
        if (uniteId != null) {
            setDirectionId(parametersMap, uniteId);
        }
        Calendar periodeDebut = form.getPeriodeDebut();
        if (periodeDebut != null) {
            setDateDeDebut(statistiqueCourante, parametersMap, periodeDebut.getTime());
        }
        Calendar periodeFin = form.getPeriodeFin();
        if (periodeFin != null) {
            setDateDeFin(parametersMap, statistiqueCourante, periodeFin.getTime());
        }
        Calendar date = form.getDate();
        if (date != null) {
            setDateDeDebut(statistiqueCourante, parametersMap, date.getTime());
        }
        String vecteurPublication = form.getVecteurPublication();
        if (vecteurPublication != null) {
            setVecteurPublication(parametersMap, vecteurPublication);
        }
        String periodeType = form.getPeriodeType();
        if (periodeType != null) {
            setPeriodeType(parametersMap, periodeType);
        }
        String periodeValue = form.getPeriodeValue();
        if (periodeValue != null) {
            setPeriodeValue(parametersMap, periodeValue);
        }
        String rubrique = form.getRubrique();
        if (rubrique != null) {
            setRubrique(parametersMap, statistiqueCourante, rubrique);
        }
        String motsCles = form.getMotsCles();
        if (motsCles != null) {
            setMotsCles(parametersMap, statistiqueCourante, motsCles);
        }
        String champLibre = form.getChampLibre();
        if (champLibre != null) {
            setChampsLibre(parametersMap, statistiqueCourante, champLibre);
        }

        if (rapport == EpgRapportStatistique.NOMBRE_ACTES_TYPE) {
            CoreSession session = context.getSession();
            StatistiquesService statistiquesService = SolonEpgServiceLocator.getStatistiquesService();
            long startQ = System.currentTimeMillis();
            Map<String, String> resultList = statistiquesService.getNbTypeActeParType(
                session,
                SolonDateConverter.DATE_SLASH.parseToCalendarOrNull(
                    parametersMap.get(EpgBirtReportParams.DATEARRIVEPAPIER_BI_PARAM)
                ),
                SolonDateConverter.DATE_SLASH.parseToCalendarOrNull(
                    parametersMap.get(EpgBirtReportParams.DATEARRIVEPAPIER_BS_PARAM)
                )
            );
            long endQ = System.currentTimeMillis();
            long resultQ = endQ - startQ;
            LOGGER.info(session, STLogEnumImpl.LOG_INFO_TEC, "Temps d'execution de la requête : " + resultQ);

            if (resultList != null) {
                long startJ = System.currentTimeMillis();
                typeActeToJson(parametersMap, resultList);
                long endJ = System.currentTimeMillis();
                long resultJ = endJ - startJ;
                LOGGER.info(
                    session,
                    STLogEnumImpl.LOG_INFO_TEC,
                    "Temps de préparation du JSON ( " + resultList.size() + " rec) : " + resultJ
                );
            }
        }
        if (
            rapport == EpgRapportStatistique.TAUX_INDEXATION_TEXTE &&
            StringUtils.isAllEmpty(
                getRubrique(parametersMap, statistiqueCourante),
                getMotsCles(parametersMap, statistiqueCourante),
                getChampsLibre(parametersMap, statistiqueCourante)
            )
        ) {
            String message = ResourceHelper.getString("espace.suivi.statistique.taux_indexation_texte.erreur");
            context.getMessageQueue().addErrorToQueue(message);
        }
        UserSessionHelper.putUserSessionParameter(context, EpgUserSessionKey.BIRT_REPORT_PARAMS, parametersMap);
    }

    private void setDistributionMailboxId(Map<String, String> parametersMap) {
        String listPosteBdc = getListPosteBdc();
        if (StringUtils.isNotBlank(listPosteBdc)) {
            parametersMap.put(EpgBirtReportParams.DISTRIBUTIONMAILBOXID_PARAM, listPosteBdc);
        }
    }

    @Override
    public List<String> getScalarParamsForBirtReport(SpecificContext context) {
        Collection<ReportProperty> scalarProperties = getScalarProperties(getBirtReport(context));
        return scalarProperties
            .stream()
            .filter(p -> SCALAR_TYPE.equals(p.getType()))
            .map(ReportProperty::getName)
            .collect(toList());
    }

    /*
     * get la liste des users Bdc
     * @return
     */
    private String getListPosteBdc() {
        return STServiceLocator
            .getSTPostesService()
            .getPosteBdcNodeList()
            .stream()
            .map(OrganigrammeNode::getId)
            .map(SSConstant.MAILBOX_POSTE_ID_PREFIX::concat)
            .collect(Collectors.joining("&"));
    }

    @Override
    public boolean canViewSggStat(NuxeoPrincipal user) {
        return (
            user.isAdministrator() ||
            user.isMemberOf(SolonEpgBaseFunctionConstant.ADMIN_FONCTIONNEL_STATISTIQUES) ||
            user.isMemberOf(STBaseFunctionConstant.PROFIL_SGG)
        );
    }

    @Override
    public Map<Integer, StatistiquesItem> getStatistquesMap(SpecificContext context, String type) {
        Map<Integer, StatistiquesItem> map = new TreeMap<>();
        for (Map.Entry<Integer, StatistiquesItem> entry : getStatistquesMap(context).entrySet()) {
            StatistiquesItem statistiquesItem = entry.getValue();
            if (statistiquesItem.getCategory().equals(type)) {
                map.put(entry.getKey(), statistiquesItem);
            }
        }

        return map;
    }

    @Override
    public List<SelectValueDTO> getVecteurPublicationSelectValues(SpecificContext context) {
        return getVecteurPublicationList(context).stream().map(SelectValueDTO::new).collect(toList());
    }

    private List<String> getVecteurPublicationList(SpecificContext context) {
        final List<String> vecteurs = new ArrayList<>();

        final BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
        CoreSession session = context.getSession();
        List<DocumentModel> lstVecteurs = bulletinOfficielService.getAllActifVecteurPublication(session);

        // récupère la liste des vecteurs de publication
        for (DocumentModel doc : lstVecteurs) {
            VecteurPublication vect = doc.getAdapter(VecteurPublication.class);
            vecteurs.add(vect.getIntitule());
        }
        return vecteurs;
    }

    @Override
    public void exportListResultats(SpecificContext context) {
        DossierArchivageStatPageProvider provider = getProviderFromContext(context, DOSSIER_ARCHIVAGE_STAT_PP);

        List<DossierArchivageStatDTO> listDossiersArchives;
        List<String> idsDossier = context.getFromContextData(STContextDataKey.IDS);
        if (CollectionUtils.isNotEmpty(idsDossier)) {
            listDossiersArchives = provider.getListResultDossierDTO(idsDossier);
        } else {
            listDossiersArchives = provider.getListFullResultDossierDTO(context.getSession());
        }
        CoreSession session = context.getSession();
        NuxeoPrincipal principal = session.getPrincipal();
        String recipient = principal.getEmail();
        if (StringUtils.isBlank(recipient)) {
            context.getMessageQueue().addWarnToQueue(ResourceHelper.getString("feedback.solonepg.no.mail"));
            return;
        }

        // Post commit event
        EventProducer eventProducer = STServiceLocator.getEventProducer();
        Map<String, Serializable> eventProperties = new HashMap<>();
        eventProperties.put(
            SolonEpgEventConstant.SEND_STAT_DOSSIER_ARCHIVE_LIST_DOS_PROPERTY,
            (Serializable) listDossiersArchives
        );
        Object[] parameters = provider.getParameters();
        eventProperties.put(SolonEpgEventConstant.SEND_STAT_DOSSIER_ARCHIVE_RECIPIENT_PROPERTY, recipient);
        eventProperties.put(SolonEpgEventConstant.SEND_STAT_DOSSIER_ARCHIVE_STARTDATE_PROPERTY, (Date) parameters[0]);
        eventProperties.put(SolonEpgEventConstant.SEND_STAT_DOSSIER_ARCHIVE_ENDDATE_PROPERTY, (Date) parameters[1]);

        InlineEventContext eventContext = new InlineEventContext(session, principal, eventProperties);
        eventProducer.fireEvent(eventContext.newEvent(SolonEpgEventConstant.SEND_STAT_DOSSIER_ARCHIVE_EVENT));

        context.getMessageQueue().addToastSuccess(ResourceHelper.getString("feedback.solonepg.export"));
    }

    @Override
    public String generateReport(BirtReport birtReport, Map<String, String> params) {
        throw new UnsupportedOperationException("Not yet Implemented");
    }

    @Override
    public boolean hasRequiredParameters(BirtReport birtReport, Map<String, String> params) {
        return false;
    }

    @Override
    public Blob generateReport(SpecificContext context) {
        throw new UnsupportedOperationException("Not yet Implemented");
    }

    @Override
    protected String getGeneratedReportDirectory() {
        return SolonEpgConfigConstant.SOLONEPG_GENERATED_REPORT_DIRECTORY;
    }

    @Override
    public StatutArchivageDossierList getStatutArchivageDossierList(SpecificContext context) {
        StatutArchivageDossierForm dossierForm = context.getFromContextData(STATUT_ARCHIVAGE_DOSSIER_FORM);
        Date dateDebut = toDate(dossierForm.getPeriodeDebut());
        Date dateFin = toDate(dossierForm.getPeriodeFin());
        String statutArchivage = dossierForm.getStatutArchivage();

        if (!ObjectUtils.allNotNull(dateDebut, dateFin, statutArchivage)) {
            StatutArchivageDossierList emptyLst = new StatutArchivageDossierList(0);
            emptyLst.setListe(Collections.emptyList());
            return emptyLst;
        }

        DossierArchivageStatPageProvider provider = getProviderFromContext(context, DOSSIER_ARCHIVAGE_STAT_PP);
        Boolean recomputeList = context.getFromContextData(EpgContextDataKey.RECOMPUTE_LIST);
        if (provider == null || BooleanUtils.isTrue(recomputeList)) {
            provider =
                dossierForm.getPageProvider(
                    context.getSession(),
                    "espace_suivi_statistique_dossier_archivage_content",
                    asList(dateDebut, dateFin, statutArchivage)
                );
            UserSessionHelper.putUserSessionParameter(context, DOSSIER_ARCHIVAGE_STAT_PP, provider);
        }

        List<Map<String, Serializable>> currentPage = provider.getCurrentPage();
        StatutArchivageDossierList list = new StatutArchivageDossierList((int) provider.getResultsCount());
        List<StatutArchivageDossierDTO> dtos = currentPage
            .stream()
            .map(DossierArchivageStatDTO.class::cast)
            .map(this::toStatutArchivageDossierDTO)
            .collect(toList());
        list.setListe(dtos);
        return list;
    }

    private StatutArchivageDossierDTO toStatutArchivageDossierDTO(DossierArchivageStatDTO providerDto) {
        StatutArchivageDossierDTO dto = new StatutArchivageDossierDTO();
        dto.setDateChangementStatut(providerDto.getDateModif());
        dto.setDossierId(providerDto.getDocIdForSelection());
        dto.setMessageErreur(providerDto.getErreur());
        dto.setNor(providerDto.getNor());
        VocabularyService vocService = getVocabularyService();
        dto.setStatutArchivagePeriode(
            vocService.getEntryLabel(VOCABULARY_STATUS_ARCHIVAGE, providerDto.getStatutPeriode())
        );
        dto.setStatutEnCours(vocService.getEntryLabel(VOCABULARY_STATUS_ARCHIVAGE, providerDto.getStatut()));
        dto.setTitreActe(providerDto.getTitreActe());
        return dto;
    }
}
