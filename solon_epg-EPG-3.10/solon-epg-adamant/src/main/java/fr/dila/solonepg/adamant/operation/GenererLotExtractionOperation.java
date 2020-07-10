package fr.dila.solonepg.adamant.operation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.annotations.Param;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.api.Framework;

import fr.dila.solonepg.adamant.SolonEpgAdamantConstant;
import fr.dila.solonepg.adamant.service.DossierExtractionService;
import fr.dila.solonepg.adamant.service.ExtractionLotService;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.query.QueryUtils;

/**
 * Opération permettant d'initialiser les données pour effectuer les tests des fonctionnalitées d'archivage. 
 * Les données des NOR concernés sont initialisé. L'éligibilité des dossier est vérifié et la procédure de 
 * création de lot est initié.
 * 
 * Exemple d'appel dans le nxshell : SolonEpg.Extraction.GenererLot -nor ('ACTI1236057D','AFSP1615740L','TREL1732848A')
 * 
 */
@Operation(id = GenererLotExtractionOperation.ID, label = "Vérification éligibilité et création de lot pour archivage", description = GenererLotExtractionOperation.DESCRIPTION)
public class GenererLotExtractionOperation {
	/**
	 * Identifiant technique de l'opération.
	 */
	public static final String ID = "SolonEpg.Extraction.GenererLot";

	public static final String		DESCRIPTION	= "Cette opération vérifie l'éligibilité des dossiers à l'archivage et lance la procédure de création de lot";

	private static final STLogger		LOGGER		= STLogFactory.getLog(GenererLotExtractionOperation.class);
	
	private static final Log ARCHIVAGE_LOGGER = LogFactory.getLog(SolonEpgAdamantConstant.ARCHIVAGE_LOG);

	@Context
	protected OperationContext		context;

	@Context
	protected CoreSession			session;

	@Param(name = "nor", required = true)
	protected String listNor;

	/*
	 * Paramètre permettant de forcer la génération des dossiers a un statut déjà archivé 
	 */
	@Param(name = "f", required = false)
	protected boolean forceGenDosArchive;
	
	/*
	 * Paramètre permettant de forcer la génération de tous les dossier y compris les dossiers non éligibles
	 */
	@Param(name = "F", required = false)
	protected boolean forceGenNonEligible;

	@OperationMethod
	public void run() throws Exception {
		// récupération des dossiers

		LOGGER.info(STLogEnumImpl.LOG_INFO_TEC,
				"Lancement de la procédure de création de lot sur les dossiers " + listNor);
		if (forceGenDosArchive) {
			LOGGER.info(STLogEnumImpl.LOG_INFO_TEC,"Mode force activé, tous les dossiers doivent être traités, incluant les dossiers au statut archivé");
		}

		String param = listNor.substring(1, listNor.length()-1);

		final String query = "select d.ecm:uuid as id from Dossier as d where d.dos:numeroNor in (" + param + ")";
		final List<DocumentModel> docList = QueryUtils.doUFNXQLQueryAndFetchForDocuments(session,
				DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, query, new Object[] {});
		
		if (!docList.isEmpty()) {
			DossierExtractionService dossierExtractionService = Framework.getService(DossierExtractionService.class);
			List<DocumentModel> docListPourLot = new ArrayList<DocumentModel>();
			
			StringBuilder listNorPourLot = new StringBuilder();
			
			for (DocumentModel doc : docList) {
				Dossier dossier = doc.getAdapter(Dossier.class);
				// Si la procdure est forcé -F OU
				// si le dossier est éligible et que la procédure est forcé -f OU 
				// si le dossier est éligible et que le dossier n'est pas a un statut archivé ou en cours
				if (forceGenNonEligible 
						|| (dossierExtractionService.isDocumentEligible(dossier, session)
							&& (forceGenDosArchive || !dossier.getStatutArchivage().equals(VocabularyConstants.STATUT_ARCHIVAGE_EXTRACTION_EN_COURS)
								&& !dossier.getStatutArchivage().equals(VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_GENERE)
								&& !dossier.getStatutArchivage().equals(VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_LIVRE)
								&& !dossier.getStatutArchivage().equals(VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_ARCHIVE)))) {
					// initialisation des document de la list
					initialiserDoc(doc);
					
					String nor = dossier.getNumeroNor();
					ARCHIVAGE_LOGGER.info("Dossier retenu pour le lot : " + nor);
					listNorPourLot.append(nor + ", ");
					// ajout du document initialisé à la list
					docListPourLot.add(doc);
					
				} else {
					ARCHIVAGE_LOGGER.info("Dossier non retenu pour le lot : " + dossier.getNumeroNor());
				}
			}
			// création du lot a partir de la list des ids des documents
			// éligibles
			ARCHIVAGE_LOGGER.info("Création d'un lot contenant les " + docListPourLot.size()
					+ " dossier(s) suivant(s) : " + listNorPourLot.toString());
			createLotFromDocumentList(docListPourLot);
		}
	}

	private void initialiserDoc(DocumentModel doc) {
		Dossier dossier = doc.getAdapter(Dossier.class);
		dossier.setIdExtractionLot(null);
		dossier.save(session);
	}

	private void createLotFromDocumentList (List<DocumentModel> docs) throws Exception {
		List<String> idList = new ArrayList<String>();
		for (DocumentModel doc : docs) {
			idList.add(doc.getId());
		}
		ExtractionLotService extractionLotService = Framework.getService(ExtractionLotService.class);
		extractionLotService.createAndAssignLotToDocumentsByIds(idList, session);
	}

}
