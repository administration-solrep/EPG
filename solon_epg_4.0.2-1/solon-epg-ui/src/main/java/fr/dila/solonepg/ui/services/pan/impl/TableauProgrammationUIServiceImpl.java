package fr.dila.solonepg.ui.services.pan.impl;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.api.cases.LigneProgrammation;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.exception.WsBdjException;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.solonepg.api.service.SolonEpgParametreService;
import fr.dila.solonepg.api.service.WsBdjService;
import fr.dila.solonepg.core.dto.activitenormative.LigneProgrammationDTO;
import fr.dila.solonepg.core.dto.activitenormative.TableauDeProgrammationLoiDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.pan.TableauProgrammationOuSuiviDTO;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.TableauProgrammationUIService;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.IOException;
import java.util.List;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Bean Seam de gestion du tableau de programmation et de suivi de l'activite
 * normative. Migré de TableauProgrammationActionBean.
 *
 * @author asatre
 */
public class TableauProgrammationUIServiceImpl implements TableauProgrammationUIService {

    @Override
    public List<LigneProgrammationDTO> getCurrentListProgrammationLoi(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel activiteNormativeProgrammationDoc = context.getCurrentDocument();

        ActiviteNormativeProgrammation activiteNormativeProgrammation = activiteNormativeProgrammationDoc.getAdapter(
            ActiviteNormativeProgrammation.class
        );
        TableauProgrammationOuSuiviDTO tableauDeProgrammationLoiDTO = new TableauProgrammationOuSuiviDTO(
            activiteNormativeProgrammation,
            session,
            true,
            false,
            "Loi",
            context.getFromContextData(PanContextDataKey.CURRENT_SECTION)
        );
        return tableauDeProgrammationLoiDTO.getListe();
    }

    @Override
    public List<LigneProgrammationDTO> getCurrentListSuiviLoi(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel activiteNormativeProgrammationDoc = context.getCurrentDocument();

        boolean masquerApplique = context.getFromContextData(PanContextDataKey.MASQUER_APPLIQUE);

        ActiviteNormativeProgrammation activiteNormativeProgrammation = activiteNormativeProgrammationDoc.getAdapter(
            ActiviteNormativeProgrammation.class
        );
        TableauDeProgrammationLoiDTO tableauDeProgrammationLoiDTO = new TableauDeProgrammationLoiDTO(
            activiteNormativeProgrammation,
            session,
            Boolean.FALSE,
            masquerApplique
        );
        return tableauDeProgrammationLoiDTO.getListProgrammation();
    }

    @Override
    public String lockCurrentProgrammationLoi(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel activiteNormativeDoc = context.getCurrentDocument();
        TableauProgrammationOuSuiviDTO tableauDeProgrammationLoiDTO = context.getFromContextData(
            PanContextDataKey.TABLEAU_PROGRAMMATION_DTO
        );

        if (tableauDeProgrammationLoiDTO != null) {
            List<LigneProgrammation> lignesProgrammations = tableauDeProgrammationLoiDTO.remapField(
                session,
                activiteNormativeDoc
            );
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .saveCurrentProgrammationLoi(
                    lignesProgrammations,
                    activiteNormativeDoc.getAdapter(ActiviteNormative.class),
                    session
                );
        }

        return null;
    }

    @Override
    public String unlockCurrentProgrammationLoi(SpecificContext context) {
        CoreSession session = context.getSession();

        DocumentModel activiteNormativeDoc = context.getCurrentDocument();

        TableauProgrammationOuSuiviDTO tableauDeProgrammationLoiDTO = context.getFromContextData(
            PanContextDataKey.TABLEAU_PROGRAMMATION_DTO
        );

        if (tableauDeProgrammationLoiDTO != null) {
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .removeCurrentProgrammationLoi(activiteNormativeDoc.getAdapter(ActiviteNormative.class), session);
        }

        return null;
    }

    @Override
    public String getTableauProgrammationLockInfo(SpecificContext context) {
        DocumentModel activiteNormativeProgrammationDoc = context.getCurrentDocument();

        ActiviteNormativeProgrammation activiteNormativeProgrammation = activiteNormativeProgrammationDoc.getAdapter(
            ActiviteNormativeProgrammation.class
        );
        if (StringUtils.isNotEmpty(activiteNormativeProgrammation.getLockUser())) {
            return ResourceHelper.getString(
                "pan.tableau.programmation.lock.info",
                SolonDateConverter.getClientConverter().format(activiteNormativeProgrammation.getLockDate()),
                activiteNormativeProgrammation.getLockUser()
            );
        }
        return "";
    }

    @Override
    public boolean isCurrentProgrammationLoiLocked(SpecificContext context) {
        DocumentModel activiteNormativeProgrammationDoc = context.getCurrentDocument();

        ActiviteNormativeProgrammation activiteNormativeProgrammation = activiteNormativeProgrammationDoc.getAdapter(
            ActiviteNormativeProgrammation.class
        );
        return StringUtils.isEmpty(activiteNormativeProgrammation.getLockUser());
    }

    @Override
    public String sauvegarderTableauSuivi(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel activiteNormativeDoc = context.getCurrentDocument();
        TableauProgrammationOuSuiviDTO tableauDeProgrammationLoiDTO = context.getFromContextData(
            PanContextDataKey.TABLEAU_PROGRAMMATION_DTO
        );

        if (tableauDeProgrammationLoiDTO != null) {
            List<LigneProgrammation> lignesProgrammations = tableauDeProgrammationLoiDTO.remapField(
                session,
                activiteNormativeDoc
            );
            SolonEpgServiceLocator
                .getActiviteNormativeService()
                .publierTableauSuivi(
                    lignesProgrammations,
                    activiteNormativeDoc.getAdapter(ActiviteNormative.class),
                    session
                );
        }

        return null;
    }

    @Override
    public void publierTableauSuivi(SpecificContext context)
        throws WsBdjException, IOException, FactoryConfigurationError, XMLStreamException {
        CoreSession session = context.getSession();
        DocumentModel activiteNormativeProgrammationDoc = context.getCurrentDocument();
        boolean masquerApplique = context.getFromContextData(PanContextDataKey.MASQUER_APPLIQUE);

        // Récupération de la législature en cours et de la précédente
        SolonEpgParametreService paramService = SolonEpgServiceLocator.getSolonEpgParametreService();
        ParametrageAN param = paramService.getDocAnParametre(session);
        String legislatureEnCours = param.getLegislatureEnCours();
        String legislaturePrecPublication = param.getLegislaturePrecPublication();

        // Si la législature du document courant est celle en court ou la précédente
        String legislature = activiteNormativeProgrammationDoc
            .getAdapter(TexteMaitre.class)
            .getLegislaturePublication();
        if (
            StringUtils.isNotEmpty(legislature) &&
            !legislature.equals(legislatureEnCours) &&
            !legislature.equals(legislaturePrecPublication)
        ) {
            context
                .getMessageQueue()
                .addInfoToQueue(ResourceHelper.getString("pan.tableau.suivi.publier.alerte.invalidLegislature"));
            return;
        }

        sauvegarderTableauSuivi(context);

        ActiviteNormativeService actNormativeService = SolonEpgServiceLocator.getActiviteNormativeService();
        WsBdjService wsBdjService = SolonEpgServiceLocator.getWsBdjService();

        actNormativeService.publierTableauSuiviHTML(
            activiteNormativeProgrammationDoc,
            session,
            masquerApplique,
            false,
            true
        );
        if (wsBdjService.isPublicationEcheancierBdjActivated(session)) {
            // S'il n'y a pas de législature de publication, on affiche un message
            if (
                StringUtils.isEmpty(
                    activiteNormativeProgrammationDoc.getAdapter(TexteMaitre.class).getLegislaturePublication()
                )
            ) {
                context
                    .getMessageQueue()
                    .addInfoToQueue(ResourceHelper.getString("pan.tableau.suivi.publier.alerte.legislature"));
            }
            String status = wsBdjService.publierEcheancierBDJ(activiteNormativeProgrammationDoc, session);

            if ("SUCCESS".equals(status)) {
                context
                    .getMessageQueue()
                    .addToastSuccess(ResourceHelper.getString("wsbdj.publication.echeancier.succes"));
            } else if (StringUtils.isNotEmpty(status)) {
                context
                    .getMessageQueue()
                    .addErrorToQueue(ResourceHelper.getString("wsbdj.publication.echeancier.error"));
            }
        }
    }

    @Override
    public String getTableauSuiviPublicationInfo(SpecificContext context) {
        DocumentModel activiteNormativeProgrammationDoc = context.getCurrentDocument();

        ActiviteNormativeProgrammation activiteNormativeProgrammation = activiteNormativeProgrammationDoc.getAdapter(
            ActiviteNormativeProgrammation.class
        );
        if (StringUtils.isNotEmpty(activiteNormativeProgrammation.getTableauSuiviPublicationUser())) {
            return ResourceHelper.getString(
                "pan.tableau.programmation.publication.info",
                SolonDateConverter
                    .getClientConverter()
                    .format(activiteNormativeProgrammation.getTableauSuiviPublicationDate()),
                activiteNormativeProgrammation.getTableauSuiviPublicationUser()
            );
        }
        return "";
    }
}
