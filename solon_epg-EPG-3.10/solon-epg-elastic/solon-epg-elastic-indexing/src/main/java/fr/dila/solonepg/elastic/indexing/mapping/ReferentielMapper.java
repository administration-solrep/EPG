package fr.dila.solonepg.elastic.indexing.mapping;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.dila.solonepg.api.service.SolonEpgVocabularyService;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.elastic.indexing.mapping.exceptions.MappingException;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.core.domain.ComplexeTypeImpl;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;

public class ReferentielMapper extends DefaultComponent implements IReferentielMapper {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReferentielMapper.class);

	private SolonEpgVocabularyService vocabularyService;

	@Override
	public void activate(ComponentContext context) throws Exception {
		vocabularyService = SolonEpgServiceLocator.getSolonEpgVocabularyService();
	}

	@Override
	public void deactivate(ComponentContext context) throws Exception {
		vocabularyService = null;
	}

	@Override
	public String fromVocabulary(String directory, String vocabularyId) {
		if (vocabularyId == null) {
			return null;
		}
		if (StringUtil.isBlank(vocabularyId)) {
			return null;
		}
		if (StringUtil.isBlank(directory)) {
			throw new RuntimeException(String.format("directory '%s' cannot be null or blank", directory));
		}
		// On a 'Réglementaire ' comme libellé dans VOC_ACTE_CATEGORY
		// Il y a peu de chance de tomber sur des valeurs signifiantes les les vocabulaires
		// On fait donc un trim systématique sur les résultats
		try {
			return vocabularyService.getEntryLabel(directory, vocabularyId).trim();
		} catch (NullPointerException e) {
			LOGGER.error(String.format("Erreur de récupération du vocabulaire %s/%s", directory, vocabularyId));
			return null;
		}
	}

	@Override
	public List<Map<String, Serializable>> fromComplexeTypeList(Serializable /* List<? extends Serializable> */ complexeTypeList) {
		if (complexeTypeList == null) {
			return Collections.emptyList();
		}
		if (!(complexeTypeList instanceof List)) {
			throw new RuntimeException(String.format("Type non attendu pour complexeTypeList: %s", complexeTypeList.getClass().getName()));
		}
		@SuppressWarnings("unchecked")
		List<? extends Serializable> typed = (List<? extends Serializable>) complexeTypeList;
		ArrayList<Map<String, Serializable>> result = new ArrayList<Map<String, Serializable>>();
		for (Serializable item : typed) {
			result.add(fromComplexeType(item));
		}
		return result;
	}

	@Override
	public Map<String, Serializable> fromComplexeType(Serializable complexeType) {
		if (complexeType == null) {
			return null;
		}
		if (!(complexeType instanceof Map)) {
			throw new RuntimeException(String.format("Type non attendu pour item: %s", complexeType.getClass().getName()));
		}
		@SuppressWarnings("unchecked")
		Map<String, Serializable> typedItem = (Map<String, Serializable>) complexeType;
		ComplexeType typed = new ComplexeTypeImpl(typedItem);
		HashMap<String, Serializable> result = new HashMap<String, Serializable>();
		result.putAll(typed.getSerializableMap());
		return result;
	}

	@Override
	public String labelFromUniteStructurelleNode(String uniteStructurelleId) throws MappingException {
		if (uniteStructurelleId == null) {
			return null;
		}
		if (StringUtil.isBlank(uniteStructurelleId)) {
			throw new RuntimeException("uniteStructurelleId cannot be empty");
		}
		try {
			UniteStructurelleNode node = STServiceLocator.getSTUsAndDirectionService().getUniteStructurelleNode(uniteStructurelleId);
			return node != null ? node.getLabel() : null;
		} catch (ClientException e) {
			String message = String.format("Erreur de récupération %s pour l'identifiant %s",
					UniteStructurelleNode.class.getSimpleName(), uniteStructurelleId);
			throw new MappingException(message, e);
		}
	}

	@Override
	public String labelFromMinistereNode(String ministereId) throws MappingException {
		if (ministereId == null) {
			return null;
		}
		if (StringUtil.isBlank(ministereId)) {
			throw new RuntimeException("uniteStructurelleId cannot be empty");
		}
		try {
			EntiteNode node = STServiceLocator.getSTMinisteresService().getEntiteNode(ministereId);
			return node != null ? node.getLabel() : null;
		} catch (ClientException e) {
			String message = String.format("Erreur de récupération %s pour l'identifiant %s",
					EntiteNode.class.getSimpleName(), ministereId);
			throw new MappingException(message, e);
		}
	}

	@Override
	public String labelFromModeParution(CoreSession session, String modeParutionId) throws MappingException {
		if (StringUtil.isBlank(modeParutionId)) {
			return null;
		}
		IdRef modeParutionRef = new IdRef(modeParutionId.toString());
		try {
			return session.getDocument(modeParutionRef).getAdapter(ModeParution.class).getMode();
		} catch (ClientException e) {
			String message = String.format("Erreur de récupération %s pour l'identifiant %s",
					ModeParution.class.getSimpleName(), modeParutionId);
			throw new MappingException(message, e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> labelsFromVecteurIdList(CoreSession session, Serializable vecteurIdList) throws MappingException {
		if (vecteurIdList == null) {
			return null;
		}
		if (!(vecteurIdList instanceof List)) {
			throw new MappingException(String.format("Type non attendu pour vecteurIdList: %s", vecteurIdList.getClass().getName()));
		}
		
		// On fait un trim des vecteurs uniquement à l'indexation pour éviter les pb historique des BO qui contiennent des espaces et tabulations
		List<String> trimedVecteurs = new ArrayList<String>();
		for(String s : SolonEpgServiceLocator.getBulletinOfficielService().convertVecteurIdListToLabels(session, (List<String>) vecteurIdList))
		{
			trimedVecteurs.add(s.trim());
		}
		return trimedVecteurs;
	}
	

}
