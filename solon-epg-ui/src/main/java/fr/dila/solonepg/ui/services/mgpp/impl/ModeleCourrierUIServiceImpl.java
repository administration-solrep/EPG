package fr.dila.solonepg.ui.services.mgpp.impl;

import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getTableReferenceService;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.MODELE_CONSULTATION;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.MODELE_NAME;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.MODELE_TEMPLATE_NAME;
import static fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey.TYPES_COMMUNICATION;
import static fr.dila.solonmgpp.api.constant.MgppDocTypeConstants.COMMUNICATION_COURRIER_TYPE;
import static fr.dila.solonmgpp.core.service.SolonMgppServiceLocator.getModeleCourrierService;
import static fr.dila.solonmgpp.core.service.SolonMgppServiceLocator.getParametreMgppService;
import static fr.dila.st.api.util.XPathUtils.xPath;
import static fr.dila.st.core.service.STServiceLocator.getDownloadService;
import static fr.dila.st.core.service.STServiceLocator.getSTUserService;
import static fr.dila.st.core.util.ResourceHelper.getString;
import static fr.dila.st.ui.enums.STContextDataKey.DOSSIER_ID;
import static fr.dila.st.ui.enums.STContextDataKey.FILE_CONTENT;
import static fr.dila.st.ui.enums.STContextDataKey.FILE_NAME;
import static java.util.stream.Collectors.toList;

import fr.dila.solonepg.api.administration.TableReference;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.ModeleCourrierDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.enums.mgpp.MgppContextDataKey;
import fr.dila.solonepg.ui.services.mgpp.ModeleCourrierUIService;
import fr.dila.solonepg.ui.th.bean.ModeleCourrierConsultationForm;
import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.domain.CommunicationCourrier;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.ModeleCourrier;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.service.STUserService;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.FileUtils;
import fr.dila.st.core.util.STMailHelper;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.bean.DocumentDTO;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.mail.Address;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.model.BlobNotFoundException;
import org.nuxeo.template.api.adapters.TemplateSourceDocument;

public class ModeleCourrierUIServiceImpl implements ModeleCourrierUIService {
    private static final STLogger LOGGER = STLogFactory.getLog(ModeleCourrierUIServiceImpl.class);

    @Override
    public ModeleCourrierDTO getModeleCourrierDTO(SpecificContext context) {
        List<ModeleCourrier> modelesCourrier = getModeleCourrierService().getModelesCourrier(context.getSession());
        List<String> modeleNames = modelesCourrier.stream().map(ModeleCourrier::getTitle).sorted().collect(toList());
        ModeleCourrierDTO dto = new ModeleCourrierDTO();
        dto.setModeleCourriers(modeleNames);
        return dto;
    }

    @Override
    public void createModeleCourrier(SpecificContext context) {
        String modeleName = context.getFromContextData(MODELE_NAME);
        InputStream inputStream = context.getFromContextData(FILE_CONTENT);
        String fileName = FileUtils.generateCompleteDocxFilename(context.getFromContextData(FILE_NAME));
        List<String> typesCommunication = context.getFromContextData(TYPES_COMMUNICATION);

        getModeleCourrierService()
            .createModeleCourrier(context.getSession(), modeleName, inputStream, fileName, typesCommunication);

        context.getMessageQueue().addToastSuccess(getString("modele.courrier.create.success", modeleName));
    }

    @Override
    public void updateModeleCourrier(SpecificContext context) {
        String modeleName = context.getFromContextData(MODELE_TEMPLATE_NAME);
        ModeleCourrier modele = context.getCurrentDocument().getAdapter(ModeleCourrier.class);
        modele.setTitle(modeleName);
        modele.setTypesCommuncation(context.getFromContextData(TYPES_COMMUNICATION));
        if (StringUtils.isNotBlank(context.getFromContextData(FILE_NAME))) {
            String fileName = FileUtils.generateCompleteDocxFilename(context.getFromContextData(FILE_NAME));
            modele.setFile(context.getFromContextData(FILE_CONTENT), fileName);
        }
        context.getSession().saveDocument(modele.getDocument());

        context.getMessageQueue().addToastSuccess(getString("modele.courrier.update.success", modeleName));
    }

    @Override
    public ModeleCourrierConsultationForm convertToModeleCourrierConsultationForm(SpecificContext context) {
        String modeleName = context.getFromContextData(MODELE_NAME);
        String templateName = context.getFromContextData(MODELE_TEMPLATE_NAME);

        ModeleCourrier modeleCourrier;
        if (templateName != null) {
            modeleCourrier =
                getModeleCourrierService().getModeleCourrierFromTemplateNameOrThrow(context.getSession(), modeleName);
        } else {
            modeleCourrier =
                getModeleCourrierService().getModeleCourrierFromModeleNameOrThrow(context.getSession(), modeleName);
        }
        ModeleCourrierConsultationForm modeleCourrierForm = MapDoc2Bean.docToBean(
            modeleCourrier.getDocument(),
            ModeleCourrierConsultationForm.class
        );

        if (BooleanUtils.isTrue(context.getFromContextData(MODELE_CONSULTATION))) {
            modeleCourrierForm.setTypesCommunication(getLabels(modeleCourrierForm.getTypesCommunication()));
        }

        DocumentDTO doc = convertToDocumentDTO(modeleCourrier.getDocument());
        modeleCourrierForm.setPiecesJointes(doc);
        return modeleCourrierForm;
    }

    private DocumentDTO convertToDocumentDTO(DocumentModel pjFichierDoc) {
        DocumentDTO dto = new DocumentDTO();
        ModeleCourrier pjFichier = pjFichierDoc.getAdapter(ModeleCourrier.class);
        dto.setId(pjFichierDoc.getId());
        dto.setNom(pjFichier.getSafeFilename());
        try {
            dto.setLink(
                getDownloadService()
                    .getDownloadUrl(
                        pjFichierDoc,
                        xPath(STSchemaConstant.FILE_SCHEMA, STSchemaConstant.FILE_CONTENT_PROPERTY),
                        FileUtils.generateCompleteDocxFilename(pjFichier.getFilename())
                    )
            );
        } catch (BlobNotFoundException e) {
            LOGGER.warn(STLogEnumImpl.FAIL_GET_DOCUMENT_TEC, e);
        }
        return dto;
    }

    private List<String> getLabels(List<String> typeCommunicationIds) {
        return typeCommunicationIds
            .stream()
            .map(this::getTypeCommunicationLabel)
            .filter(Objects::nonNull)
            .collect(toList());
    }

    private String getTypeCommunicationLabel(String typeId) {
        if ("TP".equals(typeId)) {
            return "Traitement papier";
        }

        return Optional
            .ofNullable(SolonMgppServiceLocator.getEvenementTypeService().findEvenementTypeById(typeId))
            .map(EvenementTypeDescriptor::getLabel)
            .orElse(null);
    }

    @Override
    public File buildModeleCourrierForCommunication(SpecificContext context) {
        String norDossier = context.getFromContextData(EpgContextDataKey.DOSSIER_NOR);

        String idTec;

        CoreSession session = context.getSession();
        if (StringUtils.isEmpty(context.getFromContextData(DOSSIER_ID))) {
            idTec = SolonEpgServiceLocator.getNORService().getDossierIdFromNOR(session, norDossier);
        } else {
            idTec = context.getFromContextData(DOSSIER_ID);
        }

        DocumentModel fiche = SolonMgppServiceLocator
            .getDossierService()
            .findFicheLoiDocumentFromMGPP(session, norDossier);

        try {
            return SolonMgppServiceLocator
                .getCourrierProcessorService()
                .generateCourrierUnrestrictedFromDossierAndFiche(
                    session,
                    context.getFromContextData(MODELE_TEMPLATE_NAME),
                    idTec,
                    fiche
                );
        } catch (Exception e) {
            String ficheLoi = Optional
                .ofNullable(fiche)
                .map(doc -> doc.getAdapter(FicheLoi.class))
                .map(FicheLoi::getIdDossier)
                .orElse("");
            String message = getString("modele.courrier.erreur.texte.mail", ficheLoi);
            String objet = getString("modele.courrier.erreur.objet.mail");
            String email = session.getPrincipal().getEmail();
            STServiceLocator
                .getSTMailService()
                .sendMail(
                    message,
                    objet,
                    "mailSession",
                    STMailHelper.toMailAdresses(Collections.singletonList(email)).toArray(new Address[0])
                );
            throw e;
        }
    }

    private CommunicationCourrier buildCommCourrierFromDossier(SpecificContext context) {
        CoreSession session = context.getSession();
        Dossier dossier = context.getCurrentDocument().getAdapter(Dossier.class);
        DocumentModel commCourrierDoc = session.createDocumentModel(
            STServiceLocator.getUserWorkspaceService().getCurrentUserPersonalWorkspace(session).getPathAsString(),
            "courrier-tp-" + dossier.getNumeroNor(),
            COMMUNICATION_COURRIER_TYPE
        );
        CommunicationCourrier commCourrier = commCourrierDoc.getAdapter(CommunicationCourrier.class);

        DocumentModel tabRefDoc = getTableReferenceService().getTableReference(session);

        List<String> signatures = Optional
            .ofNullable(tabRefDoc)
            .map(d -> d.getAdapter(TableReference.class))
            .map(TableReference::getSignatureSGG)
            .orElseGet(ArrayList::new);

        commCourrier.setSignatureSGG(
            signatures
                .stream()
                .map(signature -> getSTUserService().getUserInfo(signature, STUserService.CIVILITE_ABREGEE_PRENOM_NOM))
                .collect(toList())
        );
        commCourrier.setDateDuJour(SolonDateConverter.getClientConverter().formatNow());
        commCourrier.setNor(dossier.getNumeroNor());
        commCourrier.setTitreActe(dossier.getTitreActe());

        ParametrageMgpp param = getParametreMgppService().findParametrageMgpp(session);

        commCourrier.setNomAN(param.getNomAN());
        commCourrier.setNomSenat(param.getNomSenat());
        commCourrier.setSignataireSgg(param.getNomSGG());
        commCourrier.setSignataireAdjointSgg(param.getNomDirecteurAdjointSGG());

        commCourrier.setMinistereResponsableDossier(context.getFromContextData(EpgContextDataKey.DOSSIER_AUTEUR));
        commCourrier.setCoauteursLEX01(context.getFromContextData(EpgContextDataKey.DOSSIER_COAUTEUR));

        return commCourrier;
    }

    @Override
    public File buildModeleCourrierForTraitementPapier(SpecificContext context) {
        CommunicationCourrier commCourrier = buildCommCourrierFromDossier(context);
        CoreSession session = context.getSession();
        DocumentModel doc = session.createDocument(commCourrier.getDocument());

        return SolonMgppServiceLocator
            .getCourrierProcessorService()
            .generateCourrierUnrestricted(
                session,
                context.getFromContextData(MODELE_TEMPLATE_NAME),
                doc.getAdapter(CommunicationCourrier.class)
            );
    }

    @Override
    public List<SelectValueDTO> getAvailableModels(SpecificContext context) {
        String eventType = context.getFromContextData(MgppContextDataKey.EVENT_TYPE);
        List<DocumentModel> lstModeles = getModeleCourrierService()
            .getModelesCourrierForCom(context.getSession(), eventType);
        return lstModeles
            .stream()
            .map(doc -> doc.getAdapter(TemplateSourceDocument.class))
            .map(tmpl -> new SelectValueDTO(tmpl.getName(), tmpl.getTitle()))
            .collect(toList());
    }
}
