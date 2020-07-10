package fr.dila.solonmgpp.api.service;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.FichePresentationSD;
import fr.dila.solonmgpp.api.domain.RepresentantOEP;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.HistoriqueDossierDTO;
import fr.sword.xsd.solon.epp.EppEvtContainer;
import fr.sword.xsd.solon.epp.EvenementType;

public interface DossierService extends fr.dila.solonepg.api.service.DossierService {

	HistoriqueDossierDTO findDossier(String norDossier, CoreSession session) throws ClientException;

	/**
	 * recherche une {@link FicheLoi}, creation si non trouvée
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	FicheLoi findOrCreateFicheLoi(CoreSession session, String idDossier) throws ClientException;

	/**
	 * Sauvegarde de la {@link FicheLoi}
	 * 
	 * @param session
	 * @param ficheDossier
	 * @return
	 * @throws ClientException
	 */
	FicheLoi saveFicheLoi(CoreSession session, DocumentModel ficheDossier) throws ClientException;

	/**
	 * Publication de la {@link FicheLoi}
	 * 
	 * @param session
	 * @param ficheDossier
	 * @return
	 * @throws ClientException
	 */
	FicheLoi publierFicheLoi(final CoreSession session, DocumentModel ficheLoi) throws ClientException;

	/**
	 * Creation de la fiche dossier
	 * 
	 * @param session
	 * @param evenementDTO
	 * @throws ClientException
	 */
	void createFicheLoi(CoreSession session, EvenementDTO evenementDTO) throws ClientException;

	/**
	 * Rattache un dossier EPG au MGPP via idDossier
	 * 
	 * @param session
	 * @param dossier
	 * @param idDossier
	 * @throws ClientException
	 */
	void attachIdDossierToDosier(CoreSession session, Dossier dossier, String idDossier) throws ClientException;

	/**
	 * Mise a jour d'une {@link FicheLoi} à partir d'un evenement
	 * 
	 * @param session
	 * @param evenementDTO
	 */
	void updateFicheLoi(CoreSession session, EvenementDTO evenementDTO) throws ClientException;

	/**
	 * Mise a jour du bordereau à partir d'un evenement
	 * 
	 * @param session
	 * @param evenementDTO
	 * @param dossier
	 */
	void updateBordereau(CoreSession session, EvenementDTO evenementDTO, Dossier dossier) throws ClientException;

	/**
	 * Mise a jour du FDD (dossier SGG) à partir d'un evenement
	 * 
	 * @param session
	 * @param evenementDTO
	 * @param dossier
	 * @throws ClientException
	 */
	void updateFondDossier(CoreSession session, EvenementDTO evenementDTO, Dossier dossier) throws ClientException;

	/**
	 * recherche d'une {@link FichePresentationDR}, creation si non trouvée
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	FichePresentationDR findOrCreateFicheDR(CoreSession session, String idDossier) throws ClientException;

	/**
	 * recherche d'une {@link FichePresentationDPG}, creation si non trouvée
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	FichePresentationDPG findOrCreateFicheDPG(CoreSession session, String idDossier) throws ClientException;

	/**
	 * recherche d'une {@link FichePresentationSD}, creation si non trouvée
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	FichePresentationSD findOrCreateFicheSD(CoreSession session, String idDossier) throws ClientException;

	/**
	 * recherche d'une {@link FichePresentationJSS}, creation si non trouvée
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	FichePresentationJSS findOrCreateFicheJSS(CoreSession session, String idDossier) throws ClientException;

	/**
	 * recherche d'une {@link FichePresentationDOC}, creation si non trouvée
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	FichePresentationDOC findOrCreateFicheDOC(CoreSession session, String idDossier) throws ClientException;

	/**
	 * recherche d'une {@link FichePresentationAUD}, creation si non trouvée
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	FichePresentationAUD findOrCreateFicheAUD(CoreSession session, String idDossier) throws ClientException;

	/**
	 * publication d'un {@link FichePresentationDR}
	 * 
	 * @param session
	 * @param ficheDR
	 * @return
	 * @throws ClientException
	 */
	FichePresentationDR publierFicheDR(CoreSession session, DocumentModel ficheDR) throws ClientException;

	/**
	 * Sauvegarde d'un {@link FichePresentationDR}
	 * 
	 * @param session
	 * @param ficheDR
	 * @return
	 * @throws ClientException
	 */
	FichePresentationDR saveFicheDR(CoreSession session, DocumentModel ficheDR) throws ClientException;

	/**
	 * Sauvegarde d'un {@link FichePresentationDPG}
	 * 
	 * @param session
	 * @param ficheDPG
	 * @return
	 * @throws ClientException
	 */
	FichePresentationDPG saveFicheDPG(CoreSession session, DocumentModel ficheDPG) throws ClientException;

	/**
	 * Sauvegarde d'un {@link FichePresentationSD}
	 * 
	 * @param session
	 * @param ficheSD
	 * @return
	 * @throws ClientException
	 */
	FichePresentationSD saveFicheSD(CoreSession session, DocumentModel ficheSD) throws ClientException;

	/**
	 * Sauvegarde d'un {@link FichePresentationJSS}
	 * 
	 * @param session
	 * @param ficheJSS
	 * @return
	 * @throws ClientException
	 */
	FichePresentationJSS saveFicheJSS(CoreSession session, DocumentModel ficheJSS) throws ClientException;

	/**
	 * Creation d'une {@link FichePresentationOEP} completer avec les info de l'evenement si non null
	 * 
	 * @param session
	 * @param evenementDTO
	 * @return
	 * @throws ClientException
	 */
	FichePresentationOEP createFicheRepresentationOEP(CoreSession session, EvenementDTO evenementDTO, Boolean save)
			throws ClientException;

	FichePresentationOEP findOrCreateFicheRepresentationOEP(CoreSession session, EvenementDTO evenementDTO)
			throws ClientException;

	/**
	 * Creation d'une {@link FichePresentationIE} vide
	 * 
	 * @param evenementDTO
	 * 
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	FichePresentationIE createEmptyFicheIE(EvenementDTO evenementDTO, CoreSession session) throws ClientException;

	/**
	 * Creation d'une {@link FichePresentationIE}
	 * 
	 * @param session
	 * @param fichePresentationIE
	 * @throws ClientException
	 */
	void createFicheIE(CoreSession session, FichePresentationIE fichePresentationIE) throws ClientException;

	/**
	 * Sauvegarde d'une {@link FichePresentationIE}
	 * 
	 * @param session
	 * @param fichePresentationIE
	 * @return
	 * @throws ClientException
	 */
	FichePresentationIE saveFicheIE(CoreSession session, FichePresentationIE fichePresentationIE)
			throws ClientException;

	/**
	 * Recherche d'une {@link FichePresentationIE}
	 * 
	 * @param session
	 * @param identifiantProposition
	 * @return
	 * @throws ClientException
	 */
	FichePresentationIE findFicheIE(CoreSession session, String identifiantProposition) throws ClientException;

	/**
	 * Recherche d'une {@link FichePresentationDPG}
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	FichePresentationDPG findFichePresentationDPG(CoreSession session, String idDossier) throws ClientException;

	/**
	 * Recherche d'une {@link FichePresentationSD}
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	FichePresentationSD findFichePresentationSD(CoreSession session, String idDossier) throws ClientException;

	/**
	 * Recherche d'une {@link FichePresentationJSS}
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	FichePresentationJSS findFichePresentationJSS(CoreSession session, String idDossier) throws ClientException;

	/**
	 * Recherche d'une {@link FichePresentation341}
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 */
	FichePresentation341 find341(CoreSession session, String idDossier) throws ClientException;

	/**
	 * Sauvegarde d'une {@link FichePresentation341}
	 * 
	 * @param session
	 * @param fiche341
	 * @return
	 */
	FichePresentation341 saveFiche341(CoreSession session, FichePresentation341 fiche341) throws ClientException;

	/**
	 * Creation d'une {@link FichePresentation341}
	 * 
	 * @param session
	 * @param idEvenement
	 * @param evenementDTO
	 * @return
	 * @throws ClientException
	 */
	FichePresentation341 createFiche341(CoreSession session, String idEvenement, EvenementDTO evenementDTO)
			throws ClientException;

	/**
	 * Creation d'une {@link FichePresentation341}
	 * 
	 * @param session
	 * @param fichePresentation341
	 */
	void createFiche341(CoreSession session, FichePresentation341 fichePresentation341) throws ClientException;

	/**
	 * Creation d'une {@link FichePresentation341} vide
	 * 
	 * @param evenementDTO
	 * @param session
	 * @return
	 */
	FichePresentation341 createEmptyFiche341(EvenementDTO evenementDTO, CoreSession session) throws ClientException;

	/**
	 * Fetch {@link RepresentantOEP} from {@link FichePresentationOEP}
	 * 
	 * @param session
	 * @param typeRepresentant
	 * @param idFicheOEP
	 * @return
	 * @throws ClientException
	 */
	List<DocumentModel> fetchRepresentantOEP(CoreSession session, String typeRepresentant, String idFicheOEP)
			throws ClientException;

	/**
	 * 
	 * @param session
	 * @param ficheOEPList
	 * @throws ClientException
	 */
	void diffuserFicheOEP(CoreSession session, List<DocumentModel> ficheOEPList) throws ClientException;

	/**
	 * 
	 * @param session
	 * @param ficheOEPList
	 * @throws ClientException
	 */
	FichePresentationOEP diffuserFicheOEP(CoreSession session, DocumentModel ficheOEP) throws ClientException;

	/**
	 * 
	 * @param session
	 * @param ficheOEPList
	 * @throws ClientException
	 */
	void annulerDiffusionFicheOEP(CoreSession session, List<DocumentModel> ficheOEPList) throws ClientException;

	/**
	 * 
	 * @param session
	 * @param ficheOEPList
	 * @throws ClientException
	 */
	FichePresentationOEP annulerDiffusionFicheOEP(CoreSession session, DocumentModel ficheOEP) throws ClientException;

	/**
	 * recherche d'une {@link FichePresentationOEP} par son idDossier
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 */
	FichePresentationOEP findFicheOEP(CoreSession session, String idDossier) throws ClientException;

	/**
	 * recherche d'une {@link FichePresentationOEP} par son idDossier
	 * 
	 * @param session
	 * @param idDossier
	 *            l'id du dossier EPP
	 * @return
	 */
	FichePresentationOEP findFicheOEPbyIdDossierEPP(CoreSession session, String idDossierEPP) throws ClientException;

	/**
	 * Sauvegarde d'une {@link FichePresentationOEP}
	 * 
	 * @param session
	 * @param ficheOEP
	 * @param listRepresentantSE
	 * @param listRepresentantAN
	 * @return
	 */
	FichePresentationOEP saveFicheOEP(CoreSession session, DocumentModel ficheOEP,
			List<DocumentModel> listRepresentantAN, List<DocumentModel> listRepresentantSE) throws ClientException;

	List<DocumentModel> fetchRepresentantOEP(String typeRepresentant, EvenementDTO currentEvenement, CoreSession session)
			throws ClientException;

	/**
	 * Sauvegarde de la creation d'une {@link FichePresentationOEP}
	 * 
	 * @param session
	 * @param ficheOEP
	 * @param listAN
	 * @param list2
	 * @throws ClientException
	 */
	void createFicheRepresentationOEP(CoreSession session, DocumentModel ficheOEP, List<DocumentModel> listAN,
			List<DocumentModel> listSE) throws ClientException;

	void gererFichePresentation(EppEvtContainer eppEvtContainer, boolean publie, CoreSession session)
			throws ClientException;

	/**
	 * recherche d'une {@link FichePresentationAVI}
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	FichePresentationAVI findFicheAVI(CoreSession session, String idDossier) throws ClientException;

	/**
	 * creation d'une {@link FichePresentationAVI}
	 * 
	 * @param session
	 * @param evenementDTO
	 * @return
	 * @throws ClientException
	 */
	FichePresentationAVI createFicheRepresentationAVI(CoreSession session, EvenementDTO evenementDTO)
			throws ClientException;

	/**
	 * creation d'une {@link FichePresentationAVI}
	 * 
	 * @param session
	 * @param ficheAVI
	 * @param listRepresentant
	 * @throws ClientException
	 */
	void createFicheRepresentationAVI(CoreSession session, DocumentModel ficheAVI, List<DocumentModel> listRepresentant)
			throws ClientException;

	/**
	 * fetch des nominés d'une {@link FichePresentationAVI}
	 * 
	 * @param session
	 * @param id
	 * @return
	 * @throws ClientException
	 */
	List<DocumentModel> fetchNomineAVI(CoreSession session, String id) throws ClientException;

	/**
	 * sauvegarde d'une {@link FichePresentationAVI}
	 * 
	 * @param session
	 * @param ficheAVI
	 * @param listRepresentant
	 * @return
	 * @throws ClientException
	 */
	FichePresentationAVI saveFicheAVI(CoreSession session, DocumentModel ficheAVI, List<DocumentModel> listRepresentant)
			throws ClientException;

	/**
	 * Positionne la fiche de decret par rapport a l'etape courante de la feuille de route du décret
	 * 
	 * @param numeroNor
	 * @param session
	 * @param actif
	 */
	void notifierDecret(String numeroNor, CoreSession session, Boolean actif) throws ClientException;

	/**
	 * Recherche d'une {@link FichePresentationDecret}, creation si non trouvée
	 * 
	 * @param session
	 * @param nor
	 * @return
	 */
	FichePresentationDecret findOrCreateFicheDecret(CoreSession session, String nor) throws ClientException;

	/**
	 * Sauvegarde d'une {@link FichePresentationDecret}
	 * 
	 * @param session
	 * @param ficheDecret
	 * @return
	 */
	FichePresentationDecret saveFicheDecret(CoreSession session, DocumentModel ficheDecret) throws ClientException;

	/**
	 * Mise a jour de la fiche loi suivant une communication
	 * 
	 * @param session
	 * @param idEvenement
	 * @param typeEvenement
	 * @throws ClientException
	 */
	void updateFicheLoi(CoreSession session, String idEvenement, EvenementType typeEvenement) throws ClientException;

	/**
	 * recherche d'une {@link FichePresentationDR} par son idDossier
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 */
	FichePresentationDR findFicheDR(CoreSession session, String idDossier) throws ClientException;

	/**
	 * recherche d'une {@link FichePresentationDR} par son idDossier
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 */
	FichePresentationDecret findFicheDecret(CoreSession session, String idDossier) throws ClientException;

	/**
	 * Gets the list OEP Diffuse List
	 * 
	 * @param session
	 *            the session
	 * @return the list OEP Diffuse List
	 * @throws ClientException
	 */
	List<DocumentModel> getFichePresentationOEPDiffuseList(final CoreSession session) throws ClientException;

	/**
	 * Updates la liste de toutes les fiches OEP publiees dans EPG
	 * 
	 * @param session
	 *            the session
	 * @throws ClientException
	 */
	void updateListeOEPPubliee(final CoreSession session) throws ClientException;

	/**
	 * 
	 * @return le path des fichiers des activites parlementaires
	 * @throws ClientException
	 */
	String getPathDirAPPublie() throws ClientException;

	/**
	 * 
	 * @return le path du répertoire qui va contenir les fichiers publies de l'activite parlementaire
	 * @throws ClientException
	 */
	String getPathAPPublieRepertoire() throws ClientException;

	/**
	 * recherche d'une {@link FichePresentationAUD}
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	FichePresentationAUD findFicheAUD(CoreSession session, String idDossier) throws ClientException;

	/**
	 * creation d'une {@link FichePresentationAUD}
	 * 
	 * @param session
	 * @param evenementDTO
	 * @return
	 * @throws ClientException
	 */
	FichePresentationAUD createFicheRepresentationAUD(CoreSession session, EvenementDTO evenementDTO)
			throws ClientException;

	/**
	 * creation d'une {@link FichePresentationAUD}
	 * 
	 * @param session
	 * @param ficheAUD
	 * @param listRepresentant
	 * @throws ClientException
	 */
	void createFicheRepresentationAUD(CoreSession session, DocumentModel ficheAUD, List<DocumentModel> listRepresentant)
			throws ClientException;

	/**
	 * fetch des nominés d'une {@link FichePresentationAUD}
	 * 
	 * @param session
	 * @param id
	 * @return
	 * @throws ClientException
	 */
	List<DocumentModel> fetchPersonneAUD(CoreSession session, String id) throws ClientException;

	/**
	 * sauvegarde d'une {@link FichePresentationAUD}
	 * 
	 * @param session
	 * @param ficheAUD
	 * @param listRepresentant
	 * @return
	 * @throws ClientException
	 */
	FichePresentationAUD saveFicheAUD(CoreSession session, DocumentModel ficheAUD, List<DocumentModel> listRepresentant)
			throws ClientException;

	/**
	 * recherche d'une {@link FichePresentationDOC}
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	FichePresentationDOC findFicheDOC(CoreSession session, String idDossier) throws ClientException;

	/**
	 * creation d'une {@link FichePresentationDOC}
	 * 
	 * @param session
	 * @param evenementDTO
	 * @return
	 * @throws ClientException
	 */
	FichePresentationDOC createFicheRepresentationDOC(CoreSession session, EvenementDTO evenementDTO)
			throws ClientException;

	/**
	 * creation d'une {@link FichePresentationDOC}
	 * 
	 * @param session
	 * @param ficheDOC
	 * @param listRepresentant
	 * @throws ClientException
	 */
	void createFicheRepresentationDOC(CoreSession session, DocumentModel ficheDOC) throws ClientException;

	/**
	 * sauvegarde d'une {@link FichePresentationDOC}
	 * 
	 * @param session
	 * @param ficheDOC
	 * @return
	 * @throws ClientException
	 */
	FichePresentationDOC saveFicheDOC(CoreSession session, DocumentModel ficheDOC) throws ClientException;

	FichePresentationAVI findOrCreateFicheAVI(final CoreSession session, final String idDossier) throws ClientException;

	/**
	 * Permet de récupérer l'ensemble des fiches loi qui n'ont pas de nature
	 * 
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	List<FicheLoi> findFicheLoiWithoutNature(CoreSession session) throws ClientException;

	FichePresentationOEP findFicheOEPinANATLies(CoreSession session, String idDossier) throws ClientException;

	/**
	 * Retourne la fiche loi qui possède la propriété floi:numeroNor = numeroNor en paramètre
	 * 
	 * @param session
	 * @param numeroNor
	 * @return la première fiche loi trouvée
	 * @throws ClientException
	 */
	DocumentModel findFicheLoiDocumentFromNor(CoreSession session, String numeroNor) throws ClientException;

}
