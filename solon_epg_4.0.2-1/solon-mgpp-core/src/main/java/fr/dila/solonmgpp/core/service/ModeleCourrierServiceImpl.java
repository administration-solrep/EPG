package fr.dila.solonmgpp.core.service;

import static fr.dila.solonmgpp.api.constant.MgppDocTypeConstants.MODELE_COURRIER_TYPE;
import static fr.dila.solonmgpp.api.constant.ModeleCourrierConstants.MODELE_COURRIER_ROOT_PATH;
import static fr.dila.st.api.constant.MediaType.APPLICATION_OPENXML_WORD;
import static fr.dila.st.core.util.MimeTypeUtils.tikaDetect;
import static fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils.doUFNXQLQueryAndFetchForDocuments;
import static fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils.doUFNXQLQueryForIdsList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.nuxeo.template.adapters.source.TemplateSourceDocumentAdapterImpl.TEMPLATE_TYPE_PROP;

import fr.dila.solonmgpp.api.constant.SolonMgppActionConstant;
import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.domain.ModeleCourrier;
import fr.dila.solonmgpp.api.service.ModeleCourrierService;
import fr.dila.st.core.exception.STValidationException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.template.api.adapters.TemplateSourceDocument;

public class ModeleCourrierServiceImpl implements ModeleCourrierService {
    private static final String QUERY_ALL_MODELES =
        "Select d.ecm:uuid AS id From ModeleCourrier AS d WHERE testAcl(d.ecm:uuid) = 1 AND d.ecm:currentLifeCycleState != 'deleted' AND d.ecm:isVersion = 0";
    private static final String QUERY_MODELE_NAME = QUERY_ALL_MODELES + " AND d.dc:title = ?";
    private static final String QUERY_TEMPLATE_NAME = QUERY_ALL_MODELES + " AND d.tmpl:templateName = ?";

    private static final String QUERY_MODELE_FOR_COM = QUERY_ALL_MODELES + " AND d.mdc:types_communication = ?";

    @Override
    public ModeleCourrier createModeleCourrier(
        CoreSession session,
        String modeleName,
        InputStream in,
        String fileName,
        List<String> typesCommunication
    ) {
        Objects.requireNonNull(session);

        DocumentModel modelDoc = session.createDocumentModel(
            MODELE_COURRIER_ROOT_PATH,
            modeleName,
            MODELE_COURRIER_TYPE
        );

        ModeleCourrier modele = adapt(modelDoc);
        modele.setTitle(modeleName);
        modele.setFile(in, fileName);
        modele.setTypesCommuncation(typesCommunication);

        modelDoc.getAdapter(TemplateSourceDocument.class);
        modelDoc.setPropertyValue(TEMPLATE_TYPE_PROP, "XDocReportProcessor");

        return adapt(session.createDocument(modelDoc));
    }

    @Override
    public void validateModeleCourrier(CoreSession session, ModeleCourrier newModele) {
        validateModeleCourrier(session, newModele, null);
    }

    @Override
    public void validateModeleCourrier(CoreSession session, ModeleCourrier newModele, ModeleCourrier oldModele) {
        Objects.requireNonNull(session);
        Objects.requireNonNull(newModele);

        String oldModeleName = ofNullable(oldModele).map(ModeleCourrier::getTitle).orElse(null);
        checkCourrierModeleName(session, newModele.getTitle(), oldModeleName);
        checkFile(newModele.getContent());
        checkTypesCommunication(newModele.getTypesCommuncation());
    }

    private void checkCourrierModeleName(CoreSession session, String newModeleName, String oldModeleName) {
        if (StringUtils.isBlank(newModeleName)) {
            throw new STValidationException("modele.courrier.name.mandotary");
        }
        if (!Objects.equals(oldModeleName, newModeleName)) {
            List<String> modeleIds = doUFNXQLQueryForIdsList(
                session,
                QUERY_MODELE_NAME,
                new Object[] { newModeleName },
                1,
                0
            );
            if (!modeleIds.isEmpty()) {
                throw new STValidationException("modele.courrier.name.duplicate");
            }
        }
    }

    private void checkFile(Blob blob) {
        if (blob == null || blob.getFile() == null) {
            throw new STValidationException("modele.courrier.file.required");
        }

        // check docx format
        if (!APPLICATION_OPENXML_WORD.mime().equals(tikaDetect(blob.getFile()))) {
            throw new STValidationException("modele.courrier.file.format.docx.required");
        }
    }

    private void checkTypesCommunication(List<String> typesCommunication) {
        if (CollectionUtils.isEmpty(typesCommunication)) {
            throw new STValidationException("modele.courrier.types.communication.required");
        }
        List<EvenementTypeDescriptor> lstContributedEvent = SolonMgppServiceLocator
            .getEvenementTypeService()
            .findEvenementTypeByProcedure(SolonMgppActionConstant.PROCEDURE_LEGISLATIVE);

        // Vérification qu'il s'agit d'un type de courrier pour le traitement papier ou bien un type de communications
        if (
            typesCommunication
                .stream()
                .noneMatch(type -> "TP".equals(type) || isEventTypeInEventDefinition(type, lstContributedEvent))
        ) {
            throw new STValidationException("modele.courrier.types.communication.notValid");
        }
    }

    private boolean isEventTypeInEventDefinition(String type, List<EvenementTypeDescriptor> lstEventDef) {
        return IterableUtils.matchesAny(lstEventDef, event -> event.getName().equals(type));
    }

    @Override
    public Optional<ModeleCourrier> getModeleCourrierFromTemplateName(CoreSession session, String templateName) {
        List<DocumentModel> docs = doUFNXQLQueryAndFetchForDocuments(
            session,
            QUERY_TEMPLATE_NAME,
            new Object[] { templateName },
            1,
            0
        );
        return docs.isEmpty() ? empty() : of(adapt(docs.get(0)));
    }

    @Override
    public Optional<ModeleCourrier> getModeleCourrierFromModeleName(CoreSession session, String modeleName) {
        List<DocumentModel> docs = doUFNXQLQueryAndFetchForDocuments(
            session,
            QUERY_MODELE_NAME,
            new Object[] { modeleName },
            1,
            0
        );
        return docs.isEmpty() ? empty() : of(adapt(docs.get(0)));
    }

    @Override
    public ModeleCourrier getModeleCourrierFromModeleNameOrThrow(CoreSession session, String modeleName) {
        return getModeleCourrierFromModeleName(session, modeleName)
            .orElseThrow(() -> new STValidationException("modele.courrier.exist.fail", modeleName));
    }

    @Override
    public ModeleCourrier getModeleCourrierFromTemplateNameOrThrow(CoreSession session, String templateName) {
        return getModeleCourrierFromTemplateName(session, templateName)
            .orElseThrow(() -> new STValidationException("modele.courrier.template.exist.fail", templateName));
    }

    @Override
    public List<ModeleCourrier> getModelesCourrier(CoreSession session) {
        return doUFNXQLQueryAndFetchForDocuments(session, QUERY_ALL_MODELES, null)
            .stream()
            .map(this::adapt)
            .collect(toList());
    }

    private final ModeleCourrier adapt(DocumentModel doc) {
        return doc.getAdapter(ModeleCourrier.class);
    }

    @Override
    public List<DocumentModel> getModelesCourrierForCom(CoreSession session, String typeCommunication) {
        return doUFNXQLQueryAndFetchForDocuments(session, QUERY_MODELE_FOR_COM, new Object[] { typeCommunication });
    }
}
