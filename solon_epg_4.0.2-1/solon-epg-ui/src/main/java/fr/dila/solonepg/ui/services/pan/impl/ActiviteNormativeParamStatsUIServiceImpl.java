package fr.dila.solonepg.ui.services.pan.impl;

import static fr.dila.solonepg.ui.enums.pan.PanContextDataKey.LEGISLATURE_INPUT;
import static fr.dila.solonepg.ui.enums.pan.PanContextDataKey.LEGISLATURE_LIST;
import static fr.dila.solonepg.ui.enums.pan.PanContextDataKey.LEGISLATURE_OLD_INPUT;
import static fr.dila.solonepg.ui.enums.pan.PanContextDataKey.PARAMETER_STAT_DOC_ID;
import static fr.dila.solonepg.ui.enums.pan.PanContextDataKey.TEMPORARY_LEGIS;
import static fr.dila.solonepg.ui.enums.pan.PanContextDataKey.TEMPORARY_LEGIS_REVERSED;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.constant.SolonEpgEventConstant;
import fr.dila.solonepg.api.constant.TexteMaitreConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.pan.PanTreeNode;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.ActiviteNormativeParamStatsUIService;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

public class ActiviteNormativeParamStatsUIServiceImpl implements ActiviteNormativeParamStatsUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(ActiviteNormativeParamStatsUIServiceImpl.class);

    @Override
    public List<PanTreeNode> getParamStatsTreeNodes() {
        List<PanTreeNode> nodes = new ArrayList<>();

        PanTreeNode mainNode = new PanTreeNode("Paramétrages des statistiques", "param-stats-key", null);
        String detailTitle = "detailTitle";
        String detailKey = "detailKey";
        PanTreeNode childNode = new PanTreeNode(detailTitle, detailKey, null);
        mainNode.getChildren().add(childNode);

        nodes.add(mainNode);
        return nodes;
    }

    @Override
    public void validerParamStats(SpecificContext context) {
        CoreSession documentManager = context.getSession();
        DocumentModel parameterStatsDoc = context.getFromContextData("parameterStatsDoc");
        Map<String, String> temporaryLegis = context.getFromContextData(PanContextDataKey.TEMPORARY_LEGIS);

        ParametrageAN parametrageAN = parameterStatsDoc.getAdapter(ParametrageAN.class);
        parametrageAN.save(documentManager);

        // on met à jour les textes maitres car une legislature a changé
        if (!temporaryLegis.isEmpty()) {
            // Mise à jour du champ publication de tous les textes maitres concernés
            // MAJ du champ legislature en cours
            String oldLegisEnCours = parametrageAN.getLegislatureEnCours();
            if (temporaryLegis.containsKey(oldLegisEnCours)) {
                parametrageAN.setLegislatureEnCours(temporaryLegis.get(oldLegisEnCours));
                documentManager.saveDocument(parametrageAN.getDocument());
                documentManager.save();
            }

            SolonEpgServiceLocator
                .getSolonEpgParametreService()
                .updateLegislaturesFromTextesMaitres(documentManager, temporaryLegis);

            LOGGER.info(STLogEnumImpl.END_EVENT_TEC, "Rafraichissement de la contentview terminé.");
        }

        STServiceLocator
            .getJournalService()
            .journaliserActionPAN(
                documentManager,
                null,
                SolonEpgEventConstant.PARAM_PAN_UPDATE_EVENT,
                SolonEpgEventConstant.PARAM_PAN_UPDATE_COMMENT,
                SolonEpgEventConstant.CATEGORY_LOG_PAN_GENERAL
            );
    }

    @Override
    public boolean isThereAnOpenedAndLockedDocument(SpecificContext context) {
        DocumentModel texteMaitreDoc = context.getCurrentDocument();

        if (texteMaitreDoc != null && texteMaitreDoc.hasSchema(TexteMaitreConstants.TEXTE_MAITRE_SCHEMA)) {
            TexteMaitre docEnCours = texteMaitreDoc.getAdapter(TexteMaitre.class);
            if (
                docEnCours.getDocLockUser() != null &&
                docEnCours.getDocLockUser().equals(context.getSession().getPrincipal().getName())
            ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEspaceActiviteNormativeParametrageLegislatureReader(SpecificContext context) {
        CoreSession session = context.getSession();
        SSPrincipal ssPrincipal = (SSPrincipal) session.getPrincipal();

        return ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ESPACE_ACTIVITE_NORMATIVE_PARAM_LEGIS_UPDATER);
    }

    @Override
    public String addLegislature(SpecificContext context) {
        CoreSession session = context.getSession();

        String legislatureInput = context.getFromContextData(LEGISLATURE_INPUT);
        List<String> legislatureList = context.getFromContextData(LEGISLATURE_LIST);
        DocumentModel parameterStatsDoc = session.getDocument(
            new IdRef(context.getFromContextData(PARAMETER_STAT_DOC_ID))
        );

        if (!legislatureInput.isEmpty() && !legislatureList.contains(legislatureInput)) {
            legislatureList.add(legislatureInput);
            ParametrageAN parametrageAN = parameterStatsDoc.getAdapter(ParametrageAN.class);
            parametrageAN.setLegislatures(legislatureList);
        }
        return null;
    }

    @Override
    public String modifLegislature(SpecificContext context) {
        CoreSession session = context.getSession();

        String legislatureInput = context.getFromContextData(LEGISLATURE_INPUT);
        List<String> legislatures = context.getFromContextData(LEGISLATURE_LIST);
        String legislatureOldInput = context.getFromContextData(LEGISLATURE_OLD_INPUT);
        DocumentModel parameterStatsDoc = session.getDocument(
            new IdRef(context.getFromContextData(PARAMETER_STAT_DOC_ID))
        );
        Map<String, String> temporaryLegis = context.getFromContextData(TEMPORARY_LEGIS);
        Map<String, String> reverseTemporaryLegis = context.getFromContextData(TEMPORARY_LEGIS_REVERSED);

        if (!legislatures.contains(legislatureInput) && StringUtils.isNotBlank(legislatureInput)) {
            int oldIndex = legislatures.indexOf(legislatureOldInput);
            legislatures.add(oldIndex, legislatureInput);
            legislatures.remove(legislatureOldInput);

            ParametrageAN parametrageAN = parameterStatsDoc.getAdapter(ParametrageAN.class);
            parametrageAN.setLegislatures(legislatures);

            if (temporaryLegis.containsValue(legislatureOldInput)) {
                temporaryLegis.put(reverseTemporaryLegis.get(legislatureOldInput), legislatureInput);
                reverseTemporaryLegis.put(legislatureInput, reverseTemporaryLegis.get(legislatureOldInput));
                reverseTemporaryLegis.remove(legislatureOldInput);
            } else {
                temporaryLegis.put(legislatureOldInput, legislatureInput);
                reverseTemporaryLegis.put(legislatureInput, legislatureOldInput);
            }
        }
        return null;
    }

    @Override
    public List<String> getLegislatures(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel parameterStatsDoc = session.getDocument(
            new IdRef(context.getFromContextData(PARAMETER_STAT_DOC_ID))
        );

        if (parameterStatsDoc != null) {
            ParametrageAN parametrageAN = parameterStatsDoc.getAdapter(ParametrageAN.class);
            return parametrageAN.getLegislatures();
        }
        return new ArrayList<>();
    }
}
