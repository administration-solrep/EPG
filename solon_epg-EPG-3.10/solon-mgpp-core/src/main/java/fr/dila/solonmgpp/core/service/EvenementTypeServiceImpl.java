package fr.dila.solonmgpp.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

import fr.dila.solonmgpp.api.constant.SolonMgppActionConstant;
import fr.dila.solonmgpp.api.constant.VocabularyConstants;
import fr.dila.solonmgpp.api.descriptor.DistributionDescriptor;
import fr.dila.solonmgpp.api.descriptor.DistributionElementDescriptor;
import fr.dila.solonmgpp.api.descriptor.EvenementTypeDescriptor;
import fr.dila.solonmgpp.api.descriptor.PieceJointeDescriptor;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.service.EvenementTypeService;
import fr.dila.st.core.query.QueryUtils;

/**
 * Implémentation de {@link EvenementTypeService}
 * 
 * @author asatre
 */
public class EvenementTypeServiceImpl extends DefaultComponent implements EvenementTypeService {
	private static final String						GOUVERNEMENT					= "GOUVERNEMENT";

	/**
	 * Serial UID.
	 */
	private static final long						serialVersionUID				= 1L;

	/**
	 * Point d'extention des types de communications.
	 */
	public static final String						EVENEMENT_TYPE_EXTENSION_POINT	= "evenementType";

	/**
	 * Tableau associatif des types de communications contribués.
	 */
	private Map<String, EvenementTypeDescriptor>	evenementTypeMap;

	private static Map<String, String>				mapConversionMenu;

	static {

		mapConversionMenu = new HashMap<String, String>();
		mapConversionMenu.put(SolonMgppActionConstant.PROCEDURE_LEGISLATIVE, "PROCEDURE_LEGISLATIVE");
		mapConversionMenu.put(SolonMgppActionConstant.PUBLICATION, "PROCEDURE_LEGISLATIVE");
		mapConversionMenu.put(SolonMgppActionConstant.DEPOT_DE_RAPPORT, "DEPOT_RAPPORT_PARLEMENT");
		mapConversionMenu.put(SolonMgppActionConstant.DESIGNATION_OEP, "ORGANISME_EXTRA_PARLEMENTAIRE");
		mapConversionMenu.put(SolonMgppActionConstant.AVIS_NOMINATION, "CONSULTATION_ASSEMBLEE_PROJET_NOMINATION");
		mapConversionMenu
				.put(SolonMgppActionConstant.DECRET, "ORGANISATION_SESSION_EXTRAORDINAIRE,CONVOCATION_CONGRES");
		mapConversionMenu.put(SolonMgppActionConstant.INTERVENTION_EXTERIEURE,
				"DEMANDE_PROLONGATION_INTERVENTION_EXTERIEURE");
		mapConversionMenu.put(SolonMgppActionConstant.RESOLUTION_ARTICLE_341, "RESOLUTION_ARTICLE_34_1");
		mapConversionMenu.put(SolonMgppActionConstant.SUIVI, null);
		mapConversionMenu.put(SolonMgppActionConstant.DECLARATION_DE_POLITIQUE_GENERALE,
				"DECLARATION_DE_POLITIQUE_GENERALE");
		mapConversionMenu.put(SolonMgppActionConstant.DECLARATION_SUR_UN_SUJET_DETERMINE,
				"DECLARATION_SUR_UN_SUJET_DETERMINE_50_1C");
		mapConversionMenu.put(SolonMgppActionConstant.DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_283C,
				"DEMANDE_DE_MISE_EN_OEUVRE_ARTICLE_28_3C");
		mapConversionMenu.put(SolonMgppActionConstant.AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES,
				"AUTRES_DOCUMENTS_TRANSMIS_AUX_ASSEMBLEES");
		mapConversionMenu.put(SolonMgppActionConstant.DEMANDE_AUDITION, "DEMANDE_AUDITION_PERSONNES_EMPLOIS_ENVISAGEE");

	}

	@Override
	public void activate(ComponentContext context) {
		evenementTypeMap = new HashMap<String, EvenementTypeDescriptor>();
	}

	@Override
	public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
		if (extensionPoint.equals(EVENEMENT_TYPE_EXTENSION_POINT)) {
			EvenementTypeDescriptor descriptor = (EvenementTypeDescriptor) contribution;
			evenementTypeMap.put(descriptor.getName(), descriptor);
		} else {
			throw new IllegalArgumentException("Unknown extension point: " + extensionPoint);
		}
	}

	@Override
	public EvenementTypeDescriptor getEvenementType(String evenementType) throws ClientException {
		EvenementTypeDescriptor evenementTypeDescriptor = evenementTypeMap.get(evenementType);
		if (evenementTypeDescriptor == null) {
			// retry avec EVT_ en plus en prefix
			evenementTypeDescriptor = evenementTypeMap.get("EVT_" + evenementType);
		}

		if (evenementTypeDescriptor == null && evenementType.endsWith("TER")) {
			// retry avec _TER en plus en prefix
			evenementTypeDescriptor = evenementTypeMap.get(evenementType.replace("TER", "_TER"));
		}

		if (evenementTypeDescriptor == null && evenementType.contains("-")) {
			// retry avec _ au lieu de -
			evenementTypeDescriptor = evenementTypeMap.get(evenementType.replace("-", "_"));
		}

		if (evenementTypeDescriptor == null) {
			throw new ClientException("Type de communication inconnu : " + evenementType);
		}
		return evenementTypeDescriptor;
	}

	@Override
	public boolean isTypeCreateur(String evenementType) throws ClientException {
		return getEvenementType(evenementType).isCreateur();
	}

	@Override
	public boolean isTypeSuccessif(String evenementType) throws ClientException {
		return getEvenementType(evenementType).isSuccessif();
	}

	@Override
	public boolean isDemandeAr(String evenementType) throws ClientException {
		return getEvenementType(evenementType).isDemandeAr();
	}

	@Override
	public boolean isEmetteurAutorise(String evenementType, String emetteur) throws ClientException {
		DistributionDescriptor distribution = getEvenementType(evenementType).getDistribution();
		if (distribution == null) {
			throw new ClientException("La distribution n'est pas renseigné pour le type de communication : "
					+ evenementType);
		}
		DistributionElementDescriptor element = distribution.getEmetteur();
		Map<String, String> emetteurMap = element.getInstitution();
		return emetteurMap != null && emetteurMap.containsKey(emetteur);
	}

	@Override
	public boolean isDestinataireAutorise(String evenementType, String destinataire) throws ClientException {
		DistributionDescriptor distribution = getEvenementType(evenementType).getDistribution();
		if (distribution == null) {
			throw new ClientException("La distribution n'est pas renseigné pour le type de communication : "
					+ evenementType);
		}
		DistributionElementDescriptor element = distribution.getDestinataire();
		Map<String, String> destinataireMap = element.getInstitution();
		return destinataireMap != null && destinataireMap.containsKey(destinataire);
	}

	@Override
	public boolean isDestinataireCopieAutorise(String evenementType, String destinataireCopie) throws ClientException {
		DistributionDescriptor distribution = getEvenementType(evenementType).getDistribution();
		if (distribution == null) {
			throw new ClientException("La distribution n'est pas renseigné pour le type de communication : "
					+ evenementType);
		}
		DistributionElementDescriptor element = distribution.getCopie();
		if (element == null) {
			return false;
		}
		Map<String, String> destinataireCopieMap = element.getInstitution();
		return destinataireCopieMap != null && destinataireCopieMap.containsKey(destinataireCopie);
	}

	@Override
	public boolean isDestinataireCopieObligatoire(String evenementType) throws ClientException {
		DistributionDescriptor distribution = getEvenementType(evenementType).getDistribution();
		if (distribution == null) {
			throw new ClientException("La distribution n'est pas renseigné pour le type de communication : "
					+ evenementType);
		}
		DistributionElementDescriptor element = distribution.getCopie();
		if (element == null) {
			return false;
		}
		return element.isObligatoire();
	}

	@Override
	public boolean isPieceJointeObligatoire(String evenementType, String pieceJointeType) throws ClientException {
		Map<String, PieceJointeDescriptor> pieceJointeDescriptorMap = getEvenementType(evenementType).getPieceJointe();
		PieceJointeDescriptor pieceJointeDescriptor = pieceJointeDescriptorMap.get(pieceJointeType);
		if (pieceJointeDescriptor == null) {
			return false;
		}
		return pieceJointeDescriptor.isObligatoire();
	}

	@Override
	public boolean isCreerBrouillon(String evenementType) throws ClientException {
		return getEvenementType(evenementType).isCreerBrouillon();
	}

	@Override
	public boolean isCompleter(String evenementType) throws ClientException {
		return getEvenementType(evenementType).isCompleter();
	}

	@Override
	public boolean isRectifier(String evenementType) throws ClientException {
		return getEvenementType(evenementType).isRectifier();
	}

	@Override
	public boolean isAnnuler(String evenementType) throws ClientException {
		return getEvenementType(evenementType).isAnnuler();
	}

	@Override
	public List<EvenementTypeDescriptor> findEvenementTypeCreateur(String currentMenu) {
		List<EvenementTypeDescriptor> evenementCreateurList = new ArrayList<EvenementTypeDescriptor>();

		for (EvenementTypeDescriptor evenementTypeDescriptor : evenementTypeMap.values()) {

			// recupère que les evenements dont le gouvernement est l'emetteur
			if (evenementTypeDescriptor.isCreateur()
					&& evenementTypeDescriptor.getDistribution().getEmetteur().getInstitution().keySet()
							.contains(GOUVERNEMENT)
					&& ("DIVERS".equals(evenementTypeDescriptor.getProcedure()) || mapConversionMenu.get(currentMenu) != null
							&& mapConversionMenu.get(currentMenu).contains(evenementTypeDescriptor.getProcedure()))) {
				evenementCreateurList.add(evenementTypeDescriptor);
			}
		}

		return evenementCreateurList;
	}

	@Override
	public List<EvenementTypeDescriptor> findEvenementTypeSuccessif(String evenementType,
			List<String> evenementsSuivants) {

		EvenementTypeDescriptor currentEvtTypeDescriptor = evenementTypeMap.get(evenementType);
		if (evenementsSuivants == null) {
			evenementsSuivants = new ArrayList<String>();
		}

		List<EvenementTypeDescriptor> evenementSuccessifList = new ArrayList<EvenementTypeDescriptor>();

		for (String evtType : evenementTypeMap.keySet()) {
			EvenementTypeDescriptor evenementTypeDescriptor = evenementTypeMap.get(evtType);
			// recupère que les evenements dont le gouvernement est l'emetteur
			if (evenementTypeDescriptor.isSuccessif()
					&& !evenementsSuivants.contains(evenementTypeDescriptor.getName())
					&& evenementTypeDescriptor.getDistribution().getEmetteur().getInstitution().keySet()
							.contains(GOUVERNEMENT)
					&& (currentEvtTypeDescriptor.getProcedure().equals(
							VocabularyConstants.CATEGORIE_EVENEMENT_DIVERS_VALUE) || (evenementTypeDescriptor
							.getProcedure().equals(currentEvtTypeDescriptor.getProcedure()) || evenementTypeDescriptor
							.getProcedure().equals(VocabularyConstants.CATEGORIE_EVENEMENT_DIVERS_VALUE)))) {
				evenementSuccessifList.add(evenementTypeDescriptor);
			}
		}

		return evenementSuccessifList;
	}

	@Override
	public List<EvenementTypeDescriptor> findEvenementType() {
		return new ArrayList<EvenementTypeDescriptor>(evenementTypeMap.values());
	}

	@Override
	public List<EvenementTypeDescriptor> findEvenementTypeByProcedure(String menu) {
		List<EvenementTypeDescriptor> evenementTypeList = new ArrayList<EvenementTypeDescriptor>();

		for (EvenementTypeDescriptor evenementTypeDescriptor : evenementTypeMap.values()) {
			if ("DIVERS".equals(evenementTypeDescriptor.getProcedure()) || mapConversionMenu.get(menu) != null
					&& mapConversionMenu.get(menu).contains(evenementTypeDescriptor.getProcedure())
					|| SolonMgppActionConstant.PROCEDURE_LEGISLATIVE.equals(menu)
					&& "PROCEDURE_CENSURE".equals(evenementTypeDescriptor.getProcedure())) {
				evenementTypeList.add(evenementTypeDescriptor);
			}
		}

		return evenementTypeList;
	}

	@Override
	public List<FichePresentationOEP> getAllFichesPresentationOEP(CoreSession session) throws ClientException {
		List<FichePresentationOEP> allFPOEP = new ArrayList<FichePresentationOEP>();

		final StringBuilder queryBuilder = new StringBuilder("SELECT d.ecm:uuid as id FROM ");
		queryBuilder.append(FichePresentationOEP.DOC_TYPE).append(" as d");

		final List<DocumentModel> docs = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				FichePresentationOEP.DOC_TYPE, queryBuilder.toString(), new Object[] {});
		if (docs == null || docs.isEmpty()) {
			return null;
		} else {
			for (DocumentModel myDoc : docs) {
				allFPOEP.add(myDoc.getAdapter(FichePresentationOEP.class));
			}
			return allFPOEP;
		}
	}
}
