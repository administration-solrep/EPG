package fr.dila.solonepg.elastic.indexing.mapping;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.elastic.indexing.mapping.exceptions.MappingException;

public interface IReferentielMapper {

	String fromVocabulary(String directory, String vocabularyId);

	List<Map<String, Serializable>> fromComplexeTypeList(Serializable complexeTypeList);

	Map<String, Serializable> fromComplexeType(Serializable complexeType);

	String labelFromUniteStructurelleNode(String uniteStructurelleId) throws MappingException;

	String labelFromMinistereNode(String uniteStructurelleId) throws MappingException;

	String labelFromModeParution(CoreSession session, String modeParutionId) throws MappingException;

	List<String> labelsFromVecteurIdList(CoreSession session, Serializable vecteurIdList) throws MappingException;

}
