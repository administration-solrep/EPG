package fr.dila.solonepg.ui.services.impl;

import static fr.dila.st.core.service.STServiceLocator.getSTLockService;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.exception.TextesSignalesException;
import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.api.service.TexteSignaleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.core.util.EpgExcelUtil;
import fr.dila.solonepg.ui.bean.AttenteSignatureDTO;
import fr.dila.solonepg.ui.bean.AttenteSignatureList;
import fr.dila.solonepg.ui.contentview.AttenteSignaturePageProvider;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.EpgOngletAttenteSignatureEnum;
import fr.dila.solonepg.ui.helper.EpgAttenteSignatureHelper;
import fr.dila.solonepg.ui.services.EpgAttenteSignatureUIService;
import fr.dila.solonepg.ui.th.bean.AttenteSignatureForm;
import fr.dila.ss.core.util.ExportUtils;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.STPageProviderHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.services.actions.impl.STLockActionServiceImpl;
import fr.dila.st.ui.th.bean.PaginationForm;
import fr.dila.st.ui.th.enums.AlertType;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.activation.DataSource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;

public class EpgAttenteSignatureUIServiceImpl implements EpgAttenteSignatureUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(EpgAttenteSignatureUIServiceImpl.class);

    @Override
    public void ajouterDossierDansAttenteSignature(SpecificContext context) {
        DocumentModel currentDoc = context.getCurrentDocument();
        // Vérifie si le document est verrouillé par l'utilisateur en cours
        if (!getSTLockService().isLockByCurrentUser(context.getSession(), currentDoc)) {
            context
                .getMessageQueue()
                .addMessageToQueue(
                    ResourceHelper.getString(STLockActionServiceImpl.LOCK_LOST_ERROR_MSG),
                    AlertType.TOAST_DANGER
                );
            return;
        }

        String type = context.getFromContextData(EpgContextDataKey.TYPE_ATTENTE_SIGNATURE);
        if (StringUtils.isBlank(type)) {
            SolonEpgVocabularyService vocabularyService = SolonEpgServiceLocator.getSolonEpgVocabularyService();

            DocumentModel voc = vocabularyService.getDocumentModelFromId(
                VocabularyConstants.VOCABULARY_TYPE_ACTE_DIRECTORY,
                currentDoc.getAdapter(Dossier.class).getTypeActe()
            );
            type =
                (String) voc.getProperty(
                    VocabularyConstants.VOCABULARY_TYPE_ACTE_SCHEMA,
                    VocabularyConstants.VOCABULARY_TYPE_ACTE_CLASSIFICATION
                );

            // Si le type d'acte fait partie des arrêtés et autres textes, la popup de choix de type est affichée
            if (EpgOngletAttenteSignatureEnum.AUTRES.getId().equals(type)) {
                return;
            }
        }
        TexteSignaleService texteSignaleService = SolonEpgServiceLocator.getTexteSignaleService();
        texteSignaleService.verser(context.getSession(), currentDoc.getAdapter(Dossier.class), type);
        context.getMessageQueue().addSuccessToQueue(ResourceHelper.getString("action.add.attente.signature.success"));
    }

    @Override
    public AttenteSignatureList getTextesEnAttenteSignature(SpecificContext context) {
        String type = context.getFromContextData(EpgContextDataKey.TYPE_ATTENTE_SIGNATURE);

        AttenteSignatureList attenteSignatureList = new AttenteSignatureList();

        PaginationForm form = context.getFromContextData(STContextDataKey.PAGINATION_FORM);

        AttenteSignaturePageProvider provider = form.getPageProvider(
            context.getSession(),
            "attenteSignaturePageProvider",
            Collections.singletonList(type)
        );
        attenteSignatureList.setListe(
            provider
                .getCurrentPage()
                .stream()
                .filter(AttenteSignatureDTO.class::isInstance)
                .map(AttenteSignatureDTO.class::cast)
                .collect(Collectors.toList())
        );
        attenteSignatureList.setNbTotal((int) provider.getResultsCount());
        return attenteSignatureList;
    }

    @Override
    public AttenteSignatureDTO getAttenteSignatureDTOFromDossier(SpecificContext context) {
        String type = context.getFromContextData(EpgContextDataKey.TYPE_ATTENTE_SIGNATURE);
        return EpgAttenteSignatureHelper.dossierDocToAttenteSignatureDTO(
            context.getSession(),
            context.getCurrentDocument(),
            type
        );
    }

    @Override
    public void saveTexteAttenteSignature(SpecificContext context) {
        AttenteSignatureForm attenteSignatureForm = context.getFromContextData(
            EpgContextDataKey.ATTENTE_SIGNATURE_FORM
        );
        String type = context.getFromContextData(EpgContextDataKey.TYPE_ATTENTE_SIGNATURE);
        TexteSignaleService texteSignaleService = SolonEpgServiceLocator.getTexteSignaleService();
        Date dateDemandePublicationTS = StringUtils.isNotEmpty(attenteSignatureForm.getDateDemandePublicationTS())
            ? SolonDateConverter.DATE_SLASH.parseToDate(attenteSignatureForm.getDateDemandePublicationTS())
            : null;
        try {
            texteSignaleService.save(
                context.getSession(),
                attenteSignatureForm.getDossierId(),
                dateDemandePublicationTS,
                attenteSignatureForm.getVecteurPublicationTS(),
                attenteSignatureForm.getObservation(),
                type
            );
        } catch (TextesSignalesException e) {
            context
                .getMessageQueue()
                .addMessageToQueue(
                    ResourceHelper.getString("attente.signature.toast.error.modification"),
                    AlertType.TOAST_DANGER
                );
            LOGGER.error(STLogEnumImpl.FAIL_SAVE_DOCUMENT_FONC, e);
        }
        if (CollectionUtils.isEmpty(context.getMessageQueue().getErrorQueue())) {
            context
                .getMessageQueue()
                .addToastSuccess(ResourceHelper.getString("attente.signature.toast.success.modification"));
        }
    }

    @Override
    public void retirerTextesAttenteSignature(SpecificContext context) {
        List<String> dossierIds = context.getFromContextData(STContextDataKey.IDS);
        CoreSession session = context.getSession();
        TexteSignaleService texteSignaleService = SolonEpgServiceLocator.getTexteSignaleService();

        dossierIds
            .stream()
            .map(id -> session.getDocument(new IdRef(id)))
            .map(dm -> dm.getAdapter(Dossier.class))
            .forEach(dossier -> texteSignaleService.retirer(session, dossier));
        session.save();

        context.getMessageQueue().addToastSuccess(ResourceHelper.getString("admin.delete.dossier.retirer.success"));
    }

    @Override
    public File exportTextesAttenteSignature(SpecificContext context) {
        String type = context.getFromContextData(EpgContextDataKey.TYPE_ATTENTE_SIGNATURE);
        boolean isPDF = BirtOutputFormat.PDF == context.getFromContextData(SSContextDataKey.BIRT_OUTPUT_FORMAT);
        AttenteSignaturePageProvider pageProvider = STPageProviderHelper.getPageProvider(
            "attenteSignaturePageProvider",
            context.getSession()
        );
        pageProvider.setPageSize(0);
        pageProvider.setParameters(new Object[] { type });
        List<List<String>> textesAttenteSignature = pageProvider
            .getCurrentPage()
            .stream()
            .filter(AttenteSignatureDTO.class::isInstance)
            .map(AttenteSignatureDTO.class::cast)
            .map(dto -> convertAttenteSignatureDtoToList(dto, type))
            .collect(Collectors.toList());
        DataSource data = EpgExcelUtil.exportAttenteSignature(
            textesAttenteSignature,
            EpgOngletAttenteSignatureEnum.AUTRES.getId().equals(type),
            isPDF
        );
        return ExportUtils.createXlsOrPdfFromDataSource(data, isPDF);
    }

    private static List<String> convertAttenteSignatureDtoToList(AttenteSignatureDTO dto, String type) {
        List<String> data = new ArrayList<>();
        data.add(dto.getTitre());
        data.add(dto.getNor());
        data.add(
            StringUtils.joinWith(
                " ",
                SolonDateConverter.DATE_SLASH.format(dto.getDateDemandePublicationTS()),
                dto.getVecteurPublicationTSLibelle()
            )
        );
        data.add(getLabelForBoolean(dto.getArriveeSolon()));
        data.add(getLabelForBoolean(dto.getAccordPM()));
        data.add(getLabelForBoolean(dto.getAccordSGG()));
        if (EpgOngletAttenteSignatureEnum.LOIS.getId().equals(type)) {
            data.add(getLabelForBoolean(dto.getArriveeOriginale()));
            data.add(getLabelForBoolean(dto.getMiseEnSignature()));
            data.add(getLabelForBoolean(dto.getRetourSignature()));
            data.add(getLabelForBoolean(dto.getDecretPr()));
            data.add(getLabelForBoolean(dto.getEnvoiPr()));
            data.add(getLabelForBoolean(dto.getRetourPr()));
        }
        data.add(SolonDateConverter.DATE_SLASH.format(dto.getDateJo()));
        data.add(
            StringUtils.joinWith(
                " ",
                dto.getDelaiLibelle(),
                SolonDateConverter.DATE_SLASH.format(dto.getDatePublication())
            )
        );
        data.add(dto.getObservation());
        return data;
    }

    private static String getLabelForBoolean(boolean bool) {
        return ResourceHelper.getString(bool ? "label.yes" : "label.no");
    }
}
