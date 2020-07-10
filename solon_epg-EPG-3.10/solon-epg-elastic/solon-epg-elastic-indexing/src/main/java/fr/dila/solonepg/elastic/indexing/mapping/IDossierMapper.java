package fr.dila.solonepg.elastic.indexing.mapping;

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.elastic.indexing.mapping.exceptions.MappingException;
import fr.dila.solonepg.elastic.models.ElasticDossier;

public interface IDossierMapper {

	ElasticDossier from(DocumentModel documentModel, CoreSession session) throws MappingException;

}