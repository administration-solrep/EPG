package fr.dila.solonepg.elastic.indexing.mapping;

import fr.dila.solonepg.elastic.indexing.mapping.exceptions.MappingException;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface IDossierMapper {
    ElasticDossier from(DocumentModel documentModel, CoreSession session, Map<String, String> vecteurPublications)
        throws MappingException;
}
