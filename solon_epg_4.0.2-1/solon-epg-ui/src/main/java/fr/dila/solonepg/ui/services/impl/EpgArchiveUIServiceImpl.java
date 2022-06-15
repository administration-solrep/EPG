package fr.dila.solonepg.ui.services.impl;

import static fr.dila.st.ui.services.impl.STUserManagerUIServiceImpl.LOGGER;

import fr.dila.solonepg.adamant.SolonEpgAdamantServiceLocator;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.service.PdfGeneratorService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.services.EpgArchiveUIService;
import fr.dila.ss.api.feuilleroute.SSFeuilleRoute;
import fr.dila.ss.api.service.DocumentRoutingService;
import fr.dila.ss.api.service.DossierDistributionService;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.ss.ui.th.bean.DossierMailForm;
import fr.dila.st.api.constant.STEventConstant;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.service.JournalService;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.model.RouteTableElement;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.audit.api.LogEntry;

public class EpgArchiveUIServiceImpl implements EpgArchiveUIService {

    @Override
    public void exportFdd(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel dossierDoc = context.getCurrentDocument();
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        OutputStream outputStream = context.getFromContextData(SSContextDataKey.OUTPUTSTREAM);

        final List<DocumentModel> fondDossierDocs = SolonEpgServiceLocator
            .getFondDeDossierService()
            .getAllFoldersWithDocuments(session, dossier);

        SolonEpgServiceLocator.getArchiveService().writeZipStream(fondDossierDocs, outputStream, dossierDoc, session);

        // log de l'export
        final JournalService journalService = STServiceLocator.getJournalService();
        journalService.journaliserActionParapheur(
            session,
            dossierDoc,
            STEventConstant.EVENT_EXPORT_ZIP_DOSSIER,
            STEventConstant.COMMENT_EXPORT_ZIP_DOSSIER
        );
    }

    @Override
    public void exportParapheur(SpecificContext context) {
        CoreSession session = context.getSession();
        DocumentModel dossierDoc = context.getCurrentDocument();
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        OutputStream outputStream = context.getFromContextData(SSContextDataKey.OUTPUTSTREAM);

        final List<DocumentModel> fondDossierDocs = SolonEpgServiceLocator
            .getParapheurService()
            .getParapheurDocumentsWithoutEmptyFolders(session, dossier);

        SolonEpgServiceLocator.getArchiveService().writeZipStream(fondDossierDocs, outputStream, dossierDoc, session);

        // log de l'export
        final JournalService journalService = STServiceLocator.getJournalService();
        journalService.journaliserActionParapheur(
            session,
            dossierDoc,
            STEventConstant.EVENT_EXPORT_ZIP_DOSSIER,
            STEventConstant.COMMENT_EXPORT_ZIP_DOSSIER
        );
    }

    @Override
    public void exportDossier(SpecificContext context) {
        CoreSession session = context.getSession();
        List<DocumentModel> documentToExport = new ArrayList<>();
        DocumentModel dossierDoc = context.getCurrentDocument();
        final Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        OutputStream outputStream = context.getFromContextData(SSContextDataKey.OUTPUTSTREAM);
        final PdfGeneratorService pdfGeneratorService = SolonEpgServiceLocator.getPdfGeneratorService();

        documentToExport.addAll(
            SolonEpgServiceLocator.getParapheurService().getParapheurDocumentsWithoutEmptyFolders(session, dossier)
        );

        documentToExport.addAll(
            SolonEpgServiceLocator.getFondDeDossierService().getAllFoldersWithDocuments(session, dossier)
        );

        List<LogEntry> listLogEntryJournal = SolonEpgAdamantServiceLocator
            .getDossierExtractionService()
            .getLogEntryList(dossier.getDocument().getId());

        DossierDistributionService dossierDistributionService = SolonEpgServiceLocator.getDossierDistributionService();
        DocumentModel dossierRoute = dossierDistributionService.getLastDocumentRouteForDossier(session, dossierDoc);
        SSFeuilleRoute currentRoute = dossierRoute.getAdapter(SSFeuilleRoute.class);

        DocumentRoutingService documentRoutingService = SolonEpgServiceLocator.getDocumentRoutingService();
        List<RouteTableElement> routeTableElementList = documentRoutingService.getFeuilleRouteElements(
            currentRoute,
            session
        );

        try (
            ZipOutputStream zip = SolonEpgServiceLocator
                .getArchiveService()
                .initWriteZipStreamAndAddFile(documentToExport, outputStream, session)
        ) {
            pdfGeneratorService.addFeuilleRouteToZip(zip, dossier, routeTableElementList, session, null);
            pdfGeneratorService.addJournalToZip(session, zip, dossier, listLogEntryJournal, null);
            pdfGeneratorService.addBordereauToZip(session, zip, dossier, null);
        } catch (Exception e) {
            LOGGER.error(STLogEnumImpl.DEFAULT, e);
        }
        // log de l'export
        final JournalService journalService = STServiceLocator.getJournalService();
        journalService.journaliserActionParapheur(
            session,
            dossierDoc,
            STEventConstant.EVENT_EXPORT_ZIP_DOSSIER,
            STEventConstant.COMMENT_EXPORT_ZIP_DOSSIER
        );
    }

    @Override
    public DossierMailForm getDossierMailForm(SpecificContext context) {
        DocumentModel dossierDoc = context.getCurrentDocument();
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);

        DossierMailForm form = new DossierMailForm();
        form.setObjet(getObjetMailExportDossier(dossier));
        form.setMessage(getTexteMailExportDossier(dossier));

        return form;
    }

    private String getObjetMailExportDossier(Dossier dossier) {
        return ResourceHelper.getString("objet.mail.export.dossier", dossier.getNumeroNor());
    }

    private String getTexteMailExportDossier(Dossier dossier) {
        // Par défaut le ministère responsable du dossier comme destinataire du mail
        OrganigrammeNode ministereNode = STServiceLocator
            .getSTMinisteresService()
            .getEntiteNode(dossier.getMinistereResp());

        String ministereLabel = Optional
            .ofNullable(ministereNode)
            .map(OrganigrammeNode::getLabel)
            .orElse(StringUtils.EMPTY);
        String titreActe = Optional
            .ofNullable(StringUtils.wrap(dossier.getTitreActe(), "\""))
            .orElse(StringUtils.EMPTY);

        return ResourceHelper.getString("texte.mail.export.dossier", dossier.getNumeroNor(), titreActe, ministereLabel);
    }
}
