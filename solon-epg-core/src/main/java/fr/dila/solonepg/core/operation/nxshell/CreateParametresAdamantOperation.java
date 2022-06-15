package fr.dila.solonepg.core.operation.nxshell;

import static fr.dila.solonepg.api.constant.SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_NAME;
import static fr.dila.solonepg.api.constant.SolonEpgParametrageAdamantConstants.PARAMETRAGE_ADAMANT_DOCUMENT_TYPE;
import static fr.dila.solonepg.api.constant.SolonEpgParametrageAdamantConstants.PARAMETRE_ADAMANT_NAME_DESCRIPTION;
import static fr.dila.solonepg.api.constant.SolonEpgParametrageAdamantConstants.PARAMETRE_ADAMANT_NAME_TITLE;
import static fr.dila.solonepg.api.constant.SolonEpgParametrageAdamantConstants.PARAMETRE_ROOT_PATH;
import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getTypeActeService;

import fr.dila.solonepg.api.administration.ParametrageAdamant;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.enumeration.EpgVecteurPublication;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.core.operation.STVersion;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.tuple.Pair;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.PathRef;

/**
 * Opération pour ajouter des nouveaux paramètres dans réponses.
 *
 */
@Operation(id = CreateParametresAdamantOperation.ID, category = STConstant.PARAMETRE_DOCUMENT_TYPE)
@STVersion(version = "4.0.0")
public class CreateParametresAdamantOperation {
    public static final String ID = "Adamant.Parametre.Creation";

    public static final Integer NUMERO_SOLON = 20200020;
    public static final Integer DELAI_ELIGIBILITE = 12;
    public static final Integer NB_DOS_EXTRACTION = 6000;

    @Context
    private OperationContext context;

    protected void createParameter(CoreSession session) {
        DocumentModel doc;
        if (!session.exists(new PathRef(PARAMETRE_ROOT_PATH + PARAMETRAGE_ADAMANT_DOCUMENT_NAME))) {
            doc =
                session.createDocumentModel(
                    PARAMETRE_ROOT_PATH,
                    PARAMETRAGE_ADAMANT_DOCUMENT_NAME,
                    PARAMETRAGE_ADAMANT_DOCUMENT_TYPE
                );
            doc = session.createDocument(doc);
        } else {
            doc = session.getDocument(new PathRef(PARAMETRE_ROOT_PATH + PARAMETRAGE_ADAMANT_DOCUMENT_NAME));
        }

        DublincoreSchemaUtils.setTitle(doc, PARAMETRE_ADAMANT_NAME_TITLE);
        DublincoreSchemaUtils.setDescription(doc, PARAMETRE_ADAMANT_NAME_DESCRIPTION);
        ParametrageAdamant param = doc.getAdapter(ParametrageAdamant.class);
        param.setNumeroSolon(NUMERO_SOLON);
        param.setOriginatingAgencyBlocIdentifier("FRAN_NP_000002");
        param.setSubmissionAgencyBlocIdentifier("FRAN_NP_000221");
        param.setArchivalProfile("FRAN_PR_0002");
        param.setArchivalProfileSpecific("FRAN_PR_0003");
        param.setOriginatingAgencyIdentifier("FRAN_NP_000002");
        param.setSubmissionAgencyIdentifier("FRAN_NP_000221");
        param.setDelaiEligibilite(DELAI_ELIGIBILITE);
        param.setNbDossierExtraction(NB_DOS_EXTRACTION);
        List<String> vecteurPublication = Arrays.asList(
            EpgVecteurPublication.BODMR.getIntitule(),
            EpgVecteurPublication.JOURNAL_OFFICIEL.getIntitule()
        );
        param.setVecteurPublication(vecteurPublication);
        List<String> typeActe = getTypeActeService()
            .getEntries()
            .stream()
            .filter(e -> !TypeActeConstants.TYPE_ACTE_INFORMATIONS_PARLEMENTAIRES.equals(e.getKey()))
            .map(Pair::getValue)
            .collect(Collectors.toList());
        param.setTypeActe(typeActe);
        session.saveDocument(doc);
    }

    @OperationMethod
    public void createParametres() {
        if (!getContext().getPrincipal().isAdministrator()) {
            throw new NuxeoException(
                "Seul un administrateur Nuxeo peux créer des paramètres",
                HttpServletResponse.SC_FORBIDDEN
            );
        }
        CoreSession session = getContext().getCoreSession();

        createParameter(session);
    }

    protected OperationContext getContext() {
        return context;
    }
}
