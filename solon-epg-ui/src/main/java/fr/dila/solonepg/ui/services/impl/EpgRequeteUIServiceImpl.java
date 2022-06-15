package fr.dila.solonepg.ui.services.impl;

import com.google.gson.Gson;
import fr.dila.solonepg.api.enumeration.QueryType;
import fr.dila.solonepg.api.requeteur.EpgRequeteExperte;
import fr.dila.solonepg.elastic.models.search.AbstractCriteria;
import fr.dila.solonepg.ui.services.EpgRechercheUIService;
import fr.dila.solonepg.ui.services.EpgRequeteUIService;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.ss.ui.bean.AlerteForm;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.st.api.constant.STRequeteConstants;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.Optional;
import java.util.function.Function;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.core.api.security.ACL;
import org.nuxeo.ecm.core.api.security.SecurityConstants;

public class EpgRequeteUIServiceImpl implements EpgRequeteUIService {

    @Override
    public String saveRequeteLibreES(SpecificContext context) {
        EpgRechercheUIService rechercheUIService = EpgUIServiceLocator.getEpgRechercheUIService();
        return saveRequeteES(context, QueryType.ES_LIBRE, rechercheUIService::toRechercheLibre);
    }

    @Override
    public String saveRequeteExperteES(SpecificContext context) {
        EpgRechercheUIService rechercheUIService = EpgUIServiceLocator.getEpgRechercheUIService();
        return saveRequeteES(context, QueryType.ES_EXPERTE, rechercheUIService::toSearchCriteriaExp);
    }

    private static String convertRequeteToJson(
        SpecificContext context,
        Function<SpecificContext, ? extends AbstractCriteria> createSearchCriteria
    ) {
        Gson gson = new Gson();
        AbstractCriteria criteria = createSearchCriteria.apply(context);
        // la requête de recherche sera utilisée pour faire un export donc il ne faut pas conserver les infos de pagination sélectionnées par l'utilisateur dans l'IHM
        criteria.setPage(1);
        criteria.setPageSize(0);
        return gson.toJson(criteria);
    }

    private static String saveRequeteES(
        SpecificContext context,
        QueryType queryType,
        Function<SpecificContext, ? extends AbstractCriteria> createSearchCriteria
    ) {
        CoreSession session = context.getSession();

        String parentPath = STServiceLocator
            .getUserWorkspaceService()
            .getCurrentUserPersonalWorkspace(session)
            .getPathAsString();
        AlerteForm alerteForm = context.getFromContextData(SSContextDataKey.ALERTE_FORM);
        String titre = alerteForm.getTitre();

        DocumentModel doc = context.getCurrentDocument();
        boolean isCreation = doc == null;
        if (isCreation) {
            doc = session.createDocumentModel(parentPath, titre, STRequeteConstants.SMART_FOLDER_DOCUMENT_TYPE);
        }

        EpgRequeteExperte requeteExperte = doc.getAdapter(EpgRequeteExperte.class);
        requeteExperte.setWhereClause(convertRequeteToJson(context, createSearchCriteria));
        requeteExperte.setQueryType(queryType);
        DublincoreSchemaUtils.setTitle(doc, titre);

        DocumentModel savedDoc;
        if (isCreation) {
            savedDoc = session.createDocument(doc);
        } else {
            savedDoc = session.saveDocument(doc);
        }

        STServiceLocator
            .getSecurityService()
            .addAceToAcl(savedDoc, ACL.LOCAL_ACL, session.getPrincipal().getName(), SecurityConstants.EVERYTHING);

        return Optional
            .ofNullable(savedDoc.getId())
            .orElseThrow(
                () -> new NuxeoException("Une erreur s'est produite lors de l'enregistrement de la requête : " + titre)
            );
    }
}
