package fr.dila.solonepg.api.service;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.Habilitation;
import fr.dila.solonepg.api.cases.LigneProgrammation;
import fr.dila.solonepg.api.cases.LoiDeRatification;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.Ordonnance;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.cases.typescomplexe.LigneProgrammationHabilitation;
import fr.dila.solonepg.api.dto.DecretApplicationDTO;
import fr.dila.solonepg.api.dto.DecretDTO;
import fr.dila.solonepg.api.dto.HabilitationDTO;
import fr.dila.solonepg.api.dto.LoiDeRatificationDTO;
import fr.dila.solonepg.api.dto.MesureApplicativeDTO;
import fr.dila.solonepg.api.dto.OrdonnanceDTO;
import fr.dila.solonepg.api.dto.OrdonnanceHabilitationDTO;
import fr.dila.solonepg.api.dto.TexteMaitreLoiDTO;
import fr.dila.solonepg.api.exception.ActiviteNormativeException;

/**
 * Interface pour la gestion de l'activité nominatives
 * 
 * @author asatre
 * 
 */
public interface ActiviteNormativeService extends Serializable {

	/**
	 * Ajout d'un dossier dans l'activite normative
	 * 
	 * @param numeroNorDossier
	 * @param type
	 * @param session
	 * @throws ClientException
	 */
	void addDossierToTableauMaitre(String numeroNorDossier, String type, CoreSession session) throws ClientException;

	/**
	 * Sauvegarde du texte maître
	 * 
	 * @param dossier
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	DocumentModel saveTexteMaitre(TexteMaitre texteMaitre, CoreSession session) throws ClientException;

	/**
	 * Suppression du dossier de la liste en cours
	 * 
	 * @param doc
	 * @param type
	 * @param session
	 * @throws ClientException
	 */
	void removeActiviteNormativeFrom(DocumentModel doc, String type, CoreSession session) throws ClientException;

	/**
	 * Extraction de la date de promulgation de la loi a partir du titre de la loi<br/>
	 * Loi n°2010-43 du 23 janvier 2010 relative... retourne 23/01/2010
	 * 
	 * @param doc
	 * @return
	 * @throws ClientException
	 */
	Calendar extractDateFromTitre(String titre);

	/**
	 * verifie si un dossier est un decret, sinon {@link ActiviteNormativeException}
	 * 
	 * @param numeroNorDossier
	 * @param session
	 * @return le dossier
	 * @throws ClientException
	 */
	Dossier checkIsDecretFromNumeroNOR(String numeroNorDossier, CoreSession session) throws ClientException;

	/**
	 * retourne l'url parametre vers legifrance a partir d'un numeroNor
	 * 
	 * @param jorfLegifrance
	 * @return
	 */
	String createLienJORFLegifrance(String numeroNor);

	/**
	 * Sauvegarde des decrets
	 * 
	 * @param idCurrentMesure
	 * @param activiteNormative
	 * @param listDecret
	 * @param idCurrentTexteMaitre
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	void saveDecrets(String idCurrentMesure, List<DecretDTO> listDecret,
			String idCurrentTexteMaitre, CoreSession session) throws ClientException;

	/**
	 * Sauvegarde d'une mesure
	 * 
	 * @param activiteNormative
	 * @param listMesure
	 * @param session
	 * @throws ClientException
	 */
	void saveMesure(ActiviteNormative activiteNormative, List<MesureApplicativeDTO> listMesure, CoreSession session)
			throws ClientException;

	/**
	 * Sauvegarde d'une ordonnance
	 * 
	 * @param ordonnanceDTO
	 * @param texteMaitre
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	TexteMaitre saveOrdonnance(OrdonnanceDTO ordonnanceDTO, TexteMaitre texteMaitre, CoreSession session)
			throws ClientException;

	/**
	 * Fetch mesure
	 * 
	 * @param listIds
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	List<MesureApplicative> fetchMesure(List<String> listIds, CoreSession session) throws ClientException;

	/**
	 * fetch decret
	 * 
	 * @param listIds
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	List<Decret> fetchDecrets(List<String> listIds, CoreSession session) throws ClientException;

	/**
	 * Rattache un decret et sa mesure a sa loi
	 * 
	 * @param dossierDecret
	 * @param session
	 * @throws ClientException
	 */
	void attachDecretToLoi(Dossier dossierDecret, CoreSession session) throws ClientException;

	/**
	 * Rattache un decret et sa loi à une ordonnance
	 * 
	 * @param dossierOdonnance
	 * @param session
	 * @throws ClientException
	 */
	void attachDecretToOrdonnance(Dossier dossierOdonnance, CoreSession session) throws ClientException;

	/**
	 * construit les stats des mesures pour la fiche signaletique
	 * 
	 * @param session
	 * @param activiteNormative
	 * @return
	 * @throws ClientException
	 * @throws
	 */
	Map<String, Long> buildMesuresForFicheSignaletique(CoreSession session, ActiviteNormative activiteNormative)
			throws ClientException;

	/**
	 * construit les stats des ministeres pour la fiche signaletique
	 * 
	 * @param session
	 * @param activiteNormative
	 * @return
	 * @throws ClientException
	 */
	Map<String, String> buildMinistereForFicheSignaletique(CoreSession session, ActiviteNormative activiteNormative)
			throws ClientException;

	/**
	 * construit les stats du taux d'application pour la fiche signaletique
	 * 
	 * @param session
	 * @param activiteNormative
	 * @return
	 * @throws ClientException
	 */
	Integer buildTauxApplicationForFicheSignaletique(CoreSession session, ActiviteNormative activiteNormative)
			throws ClientException;

	/**
	 * construit les stats des delai pour la fiche sgnaletique
	 * 
	 * @param session
	 * @param adapter
	 * @param texteMaitre
	 * @return
	 * @throws ClientException
	 */
	Map<String, String> buildDelaiPublicationForFicheSignaletique(CoreSession session, ActiviteNormative adapter,
			TexteMaitre texteMaitre) throws ClientException;

	/**
	 * Sauvegarde des lignes du tableau de programmation dans l'activite normative
	 * 
	 * @param lignesProgrammations
	 * @param session
	 * @param activiteNormative
	 * @throws ClientException
	 */
	void saveCurrentProgrammationLoi(List<LigneProgrammation> lignesProgrammations,
			ActiviteNormative activiteNormative, CoreSession session) throws ClientException;

	/**
	 * Suppression des lignes du tableau de programmation dans l'activite normative
	 * 
	 * @param session
	 * @param activiteNormative
	 * @throws ClientException
	 */
	void removeCurrentProgrammationLoi(ActiviteNormative activiteNormative, CoreSession session) throws ClientException;

	/**
	 * publication du tableau de suivi
	 * 
	 * @param lignesProgrammations
	 * @param adapter
	 * @param documentManager
	 * @throws ClientException
	 */
	void publierTableauSuivi(List<LigneProgrammation> lignesProgrammations, ActiviteNormative session,
			CoreSession documentManager) throws ClientException;

	/**
	 * recherche de {@link ActiviteNormative} par numeroNor contenu dans {@link TexteMaitre}
	 * 
	 * @param numeroNor
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	ActiviteNormative findActiviteNormativeByNor(String numeroNor, CoreSession session) throws ClientException;

	Map<String, ActiviteNormative> findActiviteNormativeByNors(final List<String> numeroNors, final CoreSession session)
			throws ClientException;

	/**
	 * fetch des projets de loi de ratification
	 * 
	 * @param loiRatificationIds
	 * @param session
	 * @return
	 */
	List<LoiDeRatification> fetchLoiDeRatification(List<String> loiRatificationIds, CoreSession session);

	/**
	 * Creation ou update des {@link LoiDeRatification}
	 * 
	 * @param listLoiDeRatification
	 * @param session
	 * @param activiteNormative
	 * @return
	 * @throws ClientException
	 */
	ActiviteNormative saveProjetsLoiDeRatification(List<LoiDeRatificationDTO> listLoiDeRatification,
			CoreSession session, ActiviteNormative activiteNormative) throws ClientException;

	/**
	 * Sauvegarde des decrets d'une loi de ratification
	 * 
	 * @param idCurrentLoiDeRatification
	 * @param listDecret
	 * @param session
	 * @throws ClientException
	 */
	void saveDecretsOrdonnance(String idCurrentLoiDeRatification, List<DecretApplicationDTO> listDecret,
			CoreSession session) throws ClientException;

	/**
	 * get all dossier application loi
	 * 
	 * @param session
	 * @param legislatureEncours
	 * @return
	 * @throws ClientException
	 */
	public List<DocumentModel> getAllAplicationLoiDossiers(CoreSession session, boolean legislatureEncours)
			throws ClientException;

	/**
	 * get all dossier application loi
	 * 
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	public List<DocumentModel> getAllLoiHabilitationDossiers(CoreSession session, boolean curLegis)
			throws ClientException;

	public List<String> getAllAplicationMinisteresList(CoreSession session, boolean curLegis) throws ClientException;

	public List<String> getAllHabilitationMinisteresList(CoreSession session, boolean curLegis) throws ClientException;

	/**
	 * met a jour les informations de publication correspondant au {@link RetourDila}
	 * 
	 * @param retourDila
	 * @param session
	 * @throws ClientException
	 */
	void setPublicationInfo(RetourDila retourDila, CoreSession session) throws ClientException;

	/**
	 * verifie que le numero interne est unique pour une loi ou une ordonnance (RG-LOI-MAI-12)
	 * 
	 * @param texteMaitre
	 * @param numeroInterne
	 * @param session
	 */
	void checkNumeroInterne(TexteMaitre texteMaitre, String numeroInterne, CoreSession session)
			throws ClientException;

	/**
	 * recherche de toutes le habilitation d'une loi
	 * 
	 * @param idsHabilitation
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	List<Habilitation> fetchListHabilitation(List<String> idsHabilitation, CoreSession session) throws ClientException;

	/**
	 * fetch ordonnance
	 * 
	 * @param ordonnanceIds
	 * @param session
	 * @return
	 */
	List<Ordonnance> fetchListOrdonnance(List<String> ordonnanceIds, CoreSession session);

	/**
	 * Verifie si le dossier est une ordonnance 38C, si non {@link ActiviteNormativeException}, si oui retourne
	 * l'ordonnance correspondante si elle existe
	 * 
	 * @param numeroNorDossier
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	Ordonnance checkIsOrdonnance38CFromNumeroNOR(String numeroNorDossier, CoreSession session) throws ClientException;

	/**
	 * attache une ordonnnance avec sa loi et son habilitation
	 * 
	 * @param dossier
	 * @param session
	 * @throws ClientException
	 */
	void attachOrdonnanceToLoiHabilitation(Dossier dossier, CoreSession session) throws ClientException;

	/**
	 * Sauvegarde des Habilitations
	 * 
	 * @param listHabilitation
	 * @param idDossier
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	TexteMaitre saveHabilitation(List<HabilitationDTO> listHabilitation, String idDossier, CoreSession session)
			throws ClientException;

	/**
	 * sauvegarde des ordonnances d'une habilitation
	 * 
	 * @param listOrdonnanceHabilitation
	 * @param idHabilitation
	 * @param idTexteMaitre
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	List<String> saveOrdonnanceHabilitation(List<OrdonnanceHabilitationDTO> listOrdonnanceHabilitation,
			String idHabilitation, String idTexteMaitre, CoreSession session) throws ClientException;

	/**
	 * sauvegarde du tableau de programmation des ordonnances 38C
	 * 
	 * @param lignesProgrammations
	 * @param activiteNormative
	 * @param session
	 * @throws ClientException
	 */
	void saveCurrentProgrammationHabilitation(List<LigneProgrammationHabilitation> lignesProgrammations,
			ActiviteNormative activiteNormative, CoreSession session) throws ClientException;

	/**
	 * suppression du tableau de programmation des ordonnances 38C
	 * 
	 * @param activiteNormative
	 * @param session
	 * @throws ClientException
	 */
	void removeCurrentProgrammationHabilitation(ActiviteNormative activiteNormative, CoreSession session)
			throws ClientException;

	/**
	 * Publication du tableau de suivi des ordonnances 38C
	 * 
	 * @param lignesProgrammations
	 * @param activiteNormative
	 * @param session
	 * @throws ClientException
	 */
	void publierTableauSuiviHab(List<LigneProgrammationHabilitation> lignesProgrammations,
			ActiviteNormative activiteNormative, CoreSession session) throws ClientException;

	/**
	 * Verifi si le nor correspond bine a une loi
	 * 
	 * @param numeroNor
	 * @throws ClientException
	 */
	Dossier checkIsLoi(String numeroNor, CoreSession session) throws ClientException;

	/**
	 * true si le currentUser a locké le {@link TexteMaitre}
	 * 
	 * @param texteMaitre
	 * @param session
	 * @return
	 */
	Boolean isTexteMaitreLockByCurrentUser(TexteMaitre texteMaitre, CoreSession session);

	/**
	 * Lock du {@link TexteMaitre}
	 * 
	 * @param texteMaitre
	 * @param session
	 * @throws ClientException
	 */
	TexteMaitre lockTexteMaitre(TexteMaitre texteMaitre, CoreSession session) throws ClientException;

	/**
	 * Unlock du {@link TexteMaitre}
	 * 
	 * @param texteMaitre
	 * @param session
	 * @throws ClientException
	 */
	TexteMaitre unlockTexteMaitre(TexteMaitre texteMaitre, CoreSession session) throws ClientException;

	/**
	 * Ajout d'un traite dans le tableau maitre des traités et accords
	 * 
	 * @param titre
	 * @param dateSignature
	 * @param publication
	 * @param session
	 * @throws ClientException
	 */
	void addTraiteToTableauMaitre(String titre, Date dateSignature, Boolean publication, CoreSession session)
			throws ClientException;

	/**
	 * Recherche d'une activite normative par son numeroNor, creation de celle-ci si elle n'existe pas
	 * 
	 * @param session
	 * @param numeroNor
	 * @return
	 * @throws ClientException
	 */
	ActiviteNormative findOrCreateActiviteNormativeByNor(CoreSession session, String numeroNor) throws ClientException;

	TexteMaitre addLoiToTableauMaitreReprise(TexteMaitreLoiDTO texteMaitreLoiDTO, CoreSession session)
			throws ClientException;

	MesureApplicative saveMesureReprise(String idActiviteNormative, MesureApplicativeDTO mesureApplicativeDTO,
			CoreSession session) throws ClientException;

	void addDecretToMesureReprise(String texteMaitreLoiId, String mesureId, DecretDTO decretDTO, CoreSession session)
			throws ClientException;

	/**
	 * get le path du répertoire qui va contenir le stat publie de l'activite normative
	 * 
	 * @return
	 */
	String getPathDirANStatistiquesPublie();

	/**
	 * genrer AN html repartion ministeriel pour la page de suivi AN
	 * 
	 * @param session
	 * @param activiteNormative
	 * @param legislatureEnCours
	 */
	void generateANRepartitionMinistereHtml(CoreSession session, ActiviteNormative activiteNormative,
			boolean legislatureEnCours);

	/**
	 * 
	 * @param session
	 * @param curLegis
	 *            : si il s'agit d'une publication pour la législature courante ou non
	 */
	void updateLoiListePubliee(CoreSession session, boolean curLegis);

	/**
	 * @param curLegis
	 *            : si il s'agit d'une publication pour la législature courante ou non
	 * @param session
	 */
	void updateHabilitationListePubliee(CoreSession session, boolean curLegis);

	List<DocumentModel> findTexteMaitreByMinisterePilote(String id, CoreSession session) throws ClientException;

	/**
	 * Trouve l'activité normative par numéro, si elle existe, renvoie null dans le cas contraire
	 * 
	 * @param session
	 *            la session utilisateur
	 * @param numero
	 *            le numéro de l'activité normative à rechercher
	 * @return le document model de l'activité normative
	 * @throws ClientException
	 */
	DocumentModel findActiviteNormativeDocByNumero(CoreSession session, String numero) throws ClientException;

	/**
	 * create Ordonnance pour la Reprise
	 * 
	 * @param ordonnanceDTO
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	Ordonnance createOrdonnanceReprise(OrdonnanceDTO ordonnanceDTO, CoreSession session) throws ClientException;

	Ordonnance saveOrdonnanceHabilitationReprise(OrdonnanceHabilitationDTO ordonnanceHabilitationDTO, String article,
			String idDossier, CoreSession session, boolean ratifie) throws ClientException;

	ActiviteNormative saveProjetsLoiDeRatificationReprise(LoiDeRatificationDTO loiDeRatificationDTO,
			CoreSession session, ActiviteNormative activiteNormative) throws ClientException;

	/**
	 * Update decret from feuille de route
	 * 
	 * @param dossierDoc
	 * @param session
	 * @throws ClientException
	 */
	void updateDecretFromFeuilleDeRoute(DocumentModel dossierDoc, CoreSession session, boolean fluxRetour)
			throws ClientException;

	/**
	 * Update loi from feuille de route
	 * 
	 * @param dossierDoc
	 * @param session
	 * @throws ClientException
	 */
	void updateLoiFromFeuilleDeRoute(DocumentModel dossierDoc, CoreSession session, boolean fluxRetour)
			throws ClientException;

	/**
	 * Publier le tableau de suivi pour la page d'acceuil
	 * 
	 * @param currentDocument
	 * @param documentManager
	 * @param masquerApplique
	 * @param fromBatch
	 * @param legislatureEnCours
	 * @throws ClientException
	 */
	void publierTableauSuiviHTML(DocumentModel currentDocument, CoreSession documentManager, boolean masquerApplique,
			boolean fromBatch, boolean legislatureEnCours) throws ClientException;

	/**
	 * Publication de l'échéancier du texte maître
	 * 
	 * @param currentDocument
	 * @param documentManager
	 * @throws ClientException
	 */
	void publierEcheancierBDJ(DocumentModel currentDocument, CoreSession documentManager) throws ClientException;

	/**
	 * Publication de l'échéancier des textes maîtres
	 * 
	 * @param currentDocument
	 * @param documentManager
	 * @throws ClientException
	 */
	void publierEcheancierBDJ(List<TexteMaitre> textesMaitres, CoreSession session) throws ClientException;

	
	/**
	 * Indique si la fonctionnalité de publication de l'échéancier vers la BDJ
	 * est activée.
	 * 
	 * @return
	 */
	boolean isPublicationEcheancierBdjActivated(CoreSession session) throws ClientException;

	/**
	 * Indique si la fonctionnalité de publication des bilans semestriels vers la BDJ
	 * est activée.
	 * 
	 * @return
	 */
	boolean isPublicationBilanSemestrielsBdjActivated(CoreSession session) throws ClientException;
	
	/**
	 * Permet de récupérer le path pour les statistiques publiées pour la législature précédente
	 * 
	 * @param documentManager
	 * @return
	 */
	String getPathDirANStatistiquesLegisPrecPublie(CoreSession documentManager);

	/**
	 * Vérifie la validation des décrets pour une mesure d'application donnée. (application des lois)
	 * 
	 * @param session
	 * @param mesure
	 * @return faux si la mesure contient au moins 1 décret non validé, vrai sinon
	 * @throws ClientException
	 */
	boolean checkDecretsValidationForMesure(CoreSession session, MesureApplicative mesure) throws ClientException;

	/**
	 * Vérifie la validation des ordonnances pour une mesure d'habilitation. (suivi des habilitations)
	 * 
	 * @param session
	 * @param habilitation
	 * @return faux si la mesure contient au moins 1 ordonnance non validée, vrai sinon
	 * @throws ClientException
	 */
	boolean checkOrdonnancesValidationForHabilitation(CoreSession session, Habilitation habilitation)
			throws ClientException;

	/**
	 * Vérifie la validation des décrets pour une ordonnance (ratification des ordonnances)
	 * 
	 * @param session
	 * @param ordonnance
	 * @return
	 * @throws ClientException
	 */
	boolean checkDecretsValidationForOrdonnance(CoreSession session, Ordonnance ordonnance) throws ClientException;

	void lockCurrentProgrammationHabilitation(ActiviteNormative activiteNormative, CoreSession session)
			throws ClientException;

	void unlockCurrentProgrammationHabilitation(ActiviteNormative activiteNormative, CoreSession session)
			throws ClientException;

	/**
	 * get all dossier application ordonnances
	 * 
	 * @param session
	 * @param legislatureEncours
	 * @return
	 * @throws ClientException
	 */
	public List<DocumentModel> getAllAplicationOrdonnanceDossiers(final CoreSession session, boolean legislatureEnCours)
			throws ClientException;

	/**
	 * 
	 * @param session
	 * @param curLegis
	 *            : si il s'agit d'une publication pour la législature courante ou non
	 */
	void updateOrdonnancesListePubliee(final CoreSession session, boolean curLegis);

	
	void publierBilanSemestrielLoiBDJ(CoreSession session, String legislatureEnCours, Date DatePromulBorneInf,
			Date DatePromulBorneSup, Date DatePubliBorneInf, Date DatePubliBorneSup) throws ClientException;

	void publierBilanSemestrielOrdonnanceBDJ(CoreSession session, String legislatureEnCours, Date DatePromulBorneInf,
			Date DatePromulBorneSup, Date DatePubliBorneInf, Date DatePubliBorneSup) throws ClientException;

}
