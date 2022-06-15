package fr.dila.solonepg.ui.services.mgpp.impl;

import com.google.common.collect.ImmutableMap;
import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.ui.services.mgpp.MgppGenerationFicheUIService;
import fr.dila.solonmgpp.api.constant.SolonMgppBaseFunctionConstant;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.FichePresentationSD;
import fr.dila.solonmgpp.api.domain.RepresentantOEP;
import fr.dila.solonmgpp.api.dto.TableReferenceDTO;
import fr.dila.solonmgpp.api.enumeration.FicheReportsEnum;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.node.IdentiteNode;
import fr.dila.solonmgpp.api.node.MandatNode;
import fr.dila.solonmgpp.api.service.DossierService;
import fr.dila.solonmgpp.api.service.TableReferenceService;
import fr.dila.solonmgpp.core.domain.FichePresentationAUDImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationAVIImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationDPGImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationDRImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationDecretImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationJSSImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;

public class MgppGenerationFicheUIServiceImpl implements MgppGenerationFicheUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(MgppGenerationFicheUIServiceImpl.class);

    private static final String MANDAT_INCONNU = "** Mandat inconnu **";
    private static final String MINISTERE_INCONNU = "** Ministère inconnu **";

    private static final Map<String, FicheReportsEnum> FICHE_DOC_TYPE_TO_REPORT = new ImmutableMap.Builder<String, FicheReportsEnum>()
        .put(FicheLoi.DOC_TYPE, FicheReportsEnum.FICHE_LOI)
        .put(FichePresentationDRImpl.DOC_TYPE, FicheReportsEnum.FICHE_RAPPORT)
        .put(FichePresentationDOC.DOC_TYPE, FicheReportsEnum.FICHE_DOC)
        .put(FichePresentationAVI.DOC_TYPE, FicheReportsEnum.FICHE_AVIS_NOMINATION)
        .put(FichePresentationOEPImpl.DOC_TYPE, FicheReportsEnum.FICHE_OEP)
        .put(FichePresentationAUD.DOC_TYPE, FicheReportsEnum.FICHE_AUD)
        .put(FichePresentationDecretImpl.DOC_TYPE, FicheReportsEnum.FICHE_DECRET_PR)
        .put(FichePresentationJSS.DOC_TYPE, FicheReportsEnum.FICHE_JSS)
        .put(FichePresentationIE.DOC_TYPE, FicheReportsEnum.FICHE_IE)
        .put(FichePresentationDPG.DOC_TYPE, FicheReportsEnum.FICHE_DECLARATION_GENERALE)
        .put(FichePresentationSD.DOC_TYPE, FicheReportsEnum.FICHE_SD)
        .put(FichePresentation341.DOC_TYPE, FicheReportsEnum.FICHE_RESOLUTION)
        .build();

    private static final Map<String, BiFunction<CoreSession, DocumentModel, Map<String, Serializable>>> FICHE_DOC_TYPE_BUILD_PARAMETERS_FUNCTION = new ImmutableMap.Builder<String, BiFunction<CoreSession, DocumentModel, Map<String, Serializable>>>()
        .put(FichePresentationIE.DOC_TYPE, MgppGenerationFicheUIServiceImpl::getScalarParametersForFichePresentationIE)
        .put(FicheLoi.DOC_TYPE, MgppGenerationFicheUIServiceImpl::getScalarParametersForFicheLoi)
        .put(
            FichePresentationOEPImpl.DOC_TYPE,
            MgppGenerationFicheUIServiceImpl::getScalarParametersForFichePresentationOEP
        )
        .put(
            FichePresentation341.DOC_TYPE,
            MgppGenerationFicheUIServiceImpl::getScalarParametersForFichePresentation341
        )
        .put(
            FichePresentationDRImpl.DOC_TYPE,
            MgppGenerationFicheUIServiceImpl::getScalarParametersForFichePresentationDR
        )
        .put(FichePresentationSD.DOC_TYPE, MgppGenerationFicheUIServiceImpl::getScalarParametersForFichePresentationSD)
        .put(
            FichePresentationDOC.DOC_TYPE,
            MgppGenerationFicheUIServiceImpl::getScalarParametersForFichePresentationDOC
        )
        .put(FichePresentationDecretImpl.DOC_TYPE, MgppGenerationFicheUIServiceImpl::getScalarParametersEmpty)
        .put(FichePresentationAVIImpl.DOC_TYPE, MgppGenerationFicheUIServiceImpl::getScalarParametersEmpty)
        .put(FichePresentationAUDImpl.DOC_TYPE, MgppGenerationFicheUIServiceImpl::getScalarParametersEmpty)
        .put(FichePresentationJSSImpl.DOC_TYPE, MgppGenerationFicheUIServiceImpl::getScalarParametersEmpty)
        .put(FichePresentationDPGImpl.DOC_TYPE, MgppGenerationFicheUIServiceImpl::getScalarParametersEmpty)
        .build();

    @Override
    public File genererFicheXls(SpecificContext context) {
        return genererFiche(context, BirtOutputFormat.XLS);
    }

    @Override
    public File genererFichePdf(SpecificContext context) {
        return genererFiche(context, BirtOutputFormat.PDF);
    }

    private static File genererFiche(SpecificContext context, BirtOutputFormat outputFormat) {
        DocumentModel ficheDoc = context.getCurrentDocument();
        FicheReportsEnum ficheReport = FICHE_DOC_TYPE_TO_REPORT.get(ficheDoc.getType());
        Map<String, Serializable> scalarParameters = getScalarParameters(context);
        return SSServiceLocator
            .getBirtGenerationService()
            .generate(ficheReport.getId(), ficheReport.getName(), outputFormat, scalarParameters, null, true);
    }

    /**
     * Renseigne les données pour le rapport (id du documentModel à utiliser, et
     * auteurs, représentants suivant les cas)
     *
     * @param context
     * @return
     */
    private static Map<String, Serializable> getScalarParameters(SpecificContext context) {
        DocumentModel ficheDoc = context.getCurrentDocument();

        Map<String, Serializable> scalarParameters = new HashMap<>();
        scalarParameters.put("FICHEID_PARAM", ficheDoc.getId());
        Map<String, Serializable> ficheDocParameters = FICHE_DOC_TYPE_BUILD_PARAMETERS_FUNCTION
            .get(ficheDoc.getType())
            .apply(context.getSession(), ficheDoc);

        if (MapUtils.isNotEmpty(ficheDocParameters)) {
            scalarParameters.putAll(ficheDocParameters);
        }
        return scalarParameters;
    }

    private static Map<String, Serializable> getScalarParametersForFichePresentationIE(
        CoreSession session,
        DocumentModel ficheDoc
    ) {
        Map<String, Serializable> scalarParameters = new HashMap<>();
        FichePresentationIE fiche = ficheDoc.getAdapter(FichePresentationIE.class);

        String mandatAuteur = fiche.getAuteur();
        if (StringUtils.isNotEmpty(mandatAuteur)) {
            scalarParameters.put("AUTEUR_PARAM", getAuteurFromMandat(session, mandatAuteur));
        }

        return scalarParameters;
    }

    private static Map<String, Serializable> getScalarParametersForFicheLoi(
        CoreSession session,
        DocumentModel ficheDoc
    ) {
        Map<String, Serializable> scalarParameters = new HashMap<>();
        FicheLoi fiche = ficheDoc.getAdapter(FicheLoi.class);
        String idMin = fiche.getMinistereResp();

        String nomMinistere;
        try {
            nomMinistere =
                Optional
                    .ofNullable(STServiceLocator.getSTMinisteresService().getEntiteNode(idMin))
                    .map(OrganigrammeNode::getLabel)
                    .orElse("");
        } catch (NuxeoException e) {
            nomMinistere = MINISTERE_INCONNU;
            LOGGER.error(session, STLogEnumImpl.FAIL_GET_ENTITE_NODE_TEC, "node id : " + idMin, e);
        }
        scalarParameters.put("MINISTERE_PARAM", nomMinistere);

        return scalarParameters;
    }

    private static Map<String, Serializable> getScalarParametersForFichePresentationOEP(
        CoreSession session,
        DocumentModel ficheDoc
    ) {
        Map<String, Serializable> scalarParameters = new HashMap<>();
        FichePresentationOEP oep = ficheDoc.getAdapter(FichePresentationOEP.class);

        String representantsAN = getRepresentants(session, ficheDoc, "AN");
        if (StringUtils.isNotEmpty(representantsAN)) {
            scalarParameters.put("REP_AN_PARAM", representantsAN);
        }

        String representantsSE = getRepresentants(session, ficheDoc, "SE");
        if (StringUtils.isNotEmpty(representantsSE)) {
            scalarParameters.put("REP_SE_PARAM", representantsSE);
        }

        scalarParameters.put(
            "VIEW_UNRESTRICTED_PARAM",
            Boolean.toString(session.getPrincipal().isMemberOf(SolonMgppBaseFunctionConstant.DROIT_VUE_MGPP))
        );

        List<String> chargesMission = oep.getChargesMission();
        if (CollectionUtils.isNotEmpty(chargesMission)) {
            scalarParameters.put("CHARGES_MISSION_PARAM", StringUtils.join(chargesMission, "/"));
        }

        return scalarParameters;
    }

    private static String getRepresentants(CoreSession session, DocumentModel ficheDoc, String type) {
        String representants = "";
        try {
            DossierService dossierService = SolonMgppServiceLocator.getDossierService();
            String ficheId = ficheDoc.getId();
            List<DocumentModel> docs = dossierService.fetchRepresentantOEP(session, type, ficheId);
            representants = putRepresentants(session, docs);
        } catch (NuxeoException e) {
            LOGGER.error(session, MgppLogEnumImpl.FAIL_GET_REPRESENTANT_OEP_TEC, e);
        }
        return representants;
    }

    private static Map<String, Serializable> getScalarParametersForFichePresentation341(
        CoreSession session,
        DocumentModel ficheDoc
    ) {
        Map<String, Serializable> scalarParameters = new HashMap<>();
        FichePresentation341 fiche = ficheDoc.getAdapter(FichePresentation341.class);

        String mandatAuteur = fiche.getAuteur();
        if (StringUtils.isNotEmpty(mandatAuteur)) {
            scalarParameters.put("AUTEUR_PARAM", getAuteurFromMandat(session, mandatAuteur));
        }

        List<String> mandatCoauteurList = fiche.getCoAuteur();
        if (CollectionUtils.isNotEmpty(mandatCoauteurList)) {
            scalarParameters.put("COAUTEUR_PARAM", getCoauteurFromMandat(session, mandatCoauteurList));
        }

        return scalarParameters;
    }

    private static Map<String, Serializable> getScalarParametersForFichePresentationDR(
        CoreSession session,
        DocumentModel ficheDoc
    ) {
        Map<String, Serializable> scalarParameters = new HashMap<>();
        FichePresentationDR fiche = ficheDoc.getAdapter(FichePresentationDR.class);

        String ministeres = fiche.getMinisteres();
        if (StringUtils.isNotEmpty(ministeres)) {
            String label = ministeres;
            try {
                OrganigrammeNode node = STServiceLocator.getSTMinisteresService().getEntiteNode(ministeres);
                if (node != null) {
                    label = node.getLabel();
                }
            } catch (NuxeoException e) {
                LOGGER.error(
                    session,
                    STLogEnumImpl.FAIL_GET_ENTITE_NODE_TEC,
                    "Impossible de retrouver le ministère demandé.",
                    e
                );
            }
            scalarParameters.put("MINISTERE_LABEL", label);
        }

        return scalarParameters;
    }

    private static Map<String, Serializable> getScalarParametersForFichePresentationSD(
        CoreSession session,
        DocumentModel ficheDoc
    ) {
        Map<String, Serializable> scalarParameters = new HashMap<>();
        FichePresentationSD fiche = ficheDoc.getAdapter(FichePresentationSD.class);
        scalarParameters.put("GR_PARLEMENTAIRE", getOrganismeNamesByIds(session, fiche.getGroupeParlementaire()));
        return scalarParameters;
    }

    private static Map<String, Serializable> getScalarParametersForFichePresentationDOC(
        CoreSession session,
        DocumentModel ficheDoc
    ) {
        Map<String, Serializable> scalarParameters = new HashMap<>();
        FichePresentationDOC fiche = ficheDoc.getAdapter(FichePresentationDOC.class);
        scalarParameters.put("COMMISSION", getOrganismeNamesByIds(session, fiche.getCommissions()));
        return scalarParameters;
    }

    private static Map<String, Serializable> getScalarParametersEmpty(CoreSession session, DocumentModel ficheDoc) {
        return new HashMap<>();
    }

    private static String getOrganismeNamesByIds(CoreSession session, List<String> ids) {
        String organismeNames = "";
        if (CollectionUtils.isNotEmpty(ids)) {
            TableReferenceService trService = SolonMgppServiceLocator.getTableReferenceService();
            organismeNames =
                ids
                    .stream()
                    .map(id -> trService.findTableReferenceByIdAndType(id, "Organisme", null, session))
                    .filter(Objects::nonNull)
                    .map(TableReferenceDTO::getNom)
                    .collect(Collectors.joining(";"));
        }
        return organismeNames;
    }

    /**
     * Retourne l'identité des représentants contenus dans une liste de
     * DocumentModel de représentants et insere dans une String le résultat sous
     * la forme : numero mandat/[Monsieur-Madame] Nom Prenom;
     *
     * @param session
     * @param representantsDoc liste de documentModel de représentant
     * @return
     */
    private static String putRepresentants(CoreSession session, List<DocumentModel> representantsDoc) {
        final TableReferenceService trService = SolonMgppServiceLocator.getTableReferenceService();
        StringBuilder representants = new StringBuilder();
        for (DocumentModel repDoc : representantsDoc) {
            RepresentantOEP rep = repDoc.getAdapter(RepresentantOEP.class);
            String mandat = rep.getRepresentant();
            MandatNode mandatNode = trService.getMandat(mandat, session);
            if (mandatNode == null) {
                representants.append(mandat).append("/").append(MANDAT_INCONNU).append(";");
            } else {
                IdentiteNode identite = mandatNode.getIdentite();
                representants.append(mandat).append("/").append(identite.getCiviliteEtNomComplet()).append(";");
            }
        }
        return representants.toString();
    }

    /**
     * Retourne une chaine de caractère de la forme [Monsieur-Madame] Nom Prénom
     * à partir d'un mandat donné
     *
     * @param session
     * @param mandat
     * @return
     */
    private static String getAuteurFromMandat(CoreSession session, String mandat) {
        return getCoauteurFromMandat(session, Collections.singletonList(mandat));
    }

    /**
     * Retourne une list de chaine de caractère de la forme [Monsieur-Madame]
     * Nom Prénom à partir d'une lost de mandat donné
     *
     * @param session
     * @param mandatList
     * @return
     */
    private static String getCoauteurFromMandat(CoreSession session, List<String> mandatList) {
        String coauteur = null;

        try {
            if (CollectionUtils.isNotEmpty(mandatList)) {
                TableReferenceService trService = SolonMgppServiceLocator.getTableReferenceService();

                coauteur =
                    StringUtils.defaultIfBlank(
                        mandatList
                            .stream()
                            .map(mandat -> trService.getMandat(mandat, session))
                            .filter(Objects::nonNull)
                            .map(mandatNode -> mandatNode.getIdentite().getCiviliteEtNomComplet())
                            .collect(Collectors.joining(", ")),
                        MANDAT_INCONNU
                    );
            }
        } catch (NuxeoException e) {
            LOGGER.error(
                session,
                STLogEnumImpl.FAIL_GET_MANDAT_TEC,
                "Identité de l'auteur de la fiche résolution non trouvée",
                e
            );
        }

        return coauteur;
    }
}
