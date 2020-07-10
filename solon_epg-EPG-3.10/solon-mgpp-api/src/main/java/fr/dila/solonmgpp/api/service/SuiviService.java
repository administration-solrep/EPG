package fr.dila.solonmgpp.api.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.solonmgpp.api.domain.ActiviteParlementaire;
import fr.dila.solonmgpp.api.domain.EcheancierPromulgation;
import fr.dila.solonmgpp.api.domain.FicheLoi;

public interface SuiviService {

	/**
	 * rtourne Les textes de loi en cours de discussion et non encore définitivement adopté
	 * 
	 * @param session
	 * @return Les textes de loi en cours de discussion et non encore définitivement adopté
	 * @throws ClientException
	 */
	List<DocumentModel> getTexteLoiEnCoursDiscussionNonAdoptee(CoreSession session) throws ClientException;

	/**
	 * recuperer toutes les echeanciers de promulgation
	 * 
	 * @param session
	 * @return la liste de l'echeancier de promulgation
	 * @throws ClientException
	 */
	List<DocumentModel> getAllEcheancierPromulgation(CoreSession session) throws ClientException;

	/**
	 * recuperer le nombres des echeanciers de promulgation
	 * 
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	Long getEcheancierPromulgationCount(CoreSession session) throws ClientException;

	/**
	 * 
	 * recuperer les echeanciers de promulgation par page
	 * 
	 * @param session
	 * @param limit
	 * @param offset
	 * @return
	 * @throws ClientException
	 */
	List<DocumentModel> getEcheancierPromulgationPage(CoreSession session, int limit, int offset)
			throws ClientException;

	/**
	 * creer un echeancier de promulgation
	 * 
	 * @param session
	 * @param idDossier
	 *            l'id du dossier de la fiche de loi
	 * @throws ClientException
	 */
	EcheancierPromulgation creerEcheancierPromulgation(CoreSession session, String idDossier) throws ClientException;

	/**
	 * creer une activite parlemenataire
	 * 
	 * @param session
	 * @param idDossier
	 *            l'id du dossier de la fiche de loi
	 * @param activite
	 *            l'activite
	 * @param assemble
	 *            l'assemble
	 * @param date
	 *            la date de l'activite
	 * @throws ClientException
	 */
	void creerActiviteParlementaire(CoreSession session, String idDossier, String activite, String assemble, Date date)
			throws ClientException;

	/**
	 * supprimer une activite parlementaire
	 * 
	 * @param session
	 * @param activiteId
	 *            activite parlementaire Id
	 * @throws ClientException
	 */
	void removeActiviteParlementaire(CoreSession session, String activiteId) throws ClientException;

	/**
	 * supprimer une semaine parlementaire
	 * 
	 * @param session
	 * @param activiteId
	 *            activite parlementaire Id
	 * @throws ClientException
	 */
	void removeSemaineParlementaire(CoreSession session, String activiteId) throws ClientException;

	/**
	 * recuperer toutes les textes de loi du calendrier parlememntaire
	 * 
	 * @param session
	 * @return toutes les textes de loi du calendrier parlememntaire
	 * @throws ClientException
	 */
	List<DocumentModel> getAllTexteLoiPourCalendrierParlementaire(CoreSession session, Date startDate, Date endDate)
			throws ClientException;

	/**
	 * recuperer toutes les dates des activites parlementaires
	 * 
	 * @param session
	 * @return la liste de toutes les dates des activites parlementaires
	 * @throws ClientException
	 */
	List<Calendar> getAllDateActiviteParlementaire(CoreSession session) throws ClientException;

	/**
	 * recuperer toutes les dates des activites parlementaires
	 * 
	 * @param session
	 * @param startDate
	 * @param endDate
	 * @return la liste de toutes les dates des activites parlementaires
	 * @throws ClientException
	 */
	List<Calendar> getAllDateActiviteParlementaire(CoreSession session, Date startDate, Date endDate)
			throws ClientException;

	/**
	 * recuperer toutes les activites parlemetaires concernant une fiche de loi
	 * 
	 * @param session
	 * @param DossierId
	 *            l'id du dossier de la fiche de loi
	 * @return la liste de toutes les activites parlemetaires concernant une fiche de loi
	 * @throws ClientException
	 */
	List<DocumentModel> getAllActiviteParlementaireByDossierId(CoreSession session, String DossierId)
			throws ClientException;

	/**
	 * creer une semaine parlementaire
	 * 
	 * @param session
	 * @param orientation
	 *            l'orienattaion de la semaine
	 * @param assemblee
	 *            assemblee
	 * @param dateDebut
	 *            date de debut
	 * @param dateFin
	 *            date de fin
	 * @throws ClientException
	 */
	void creerSemaineParlementaire(CoreSession session, String orientation, String assemblee, Calendar dateDebut,
			Calendar dateFin) throws ClientException;

	List<DocumentModel> getAllSemaineParlementaire(CoreSession session, String assemble) throws ClientException;

	/**
	 * Diffuser la calendrier Parlementaire
	 * 
	 * @param listedeSemainesParTypes
	 * @param listeDeToutesLesDatesDesActivites
	 * @param listeDeToutLesLois
	 * @param mapDeToutesLesActivitesParlementaireDeCeLoi
	 * @throws ClientException
	 */
	void diffuserCalendrierParlementaire(Map<String, List<Map<String, String>>> listedeSemainesParTypes,
			List<Date> listeDeToutesLesDatesDesActivites, List<FicheLoi> listeDeToutLesLois,
			Map<String, Collection<List<ActiviteParlementaire>>> mapDeToutesLesActivitesParlementaireDeCeLoi)
			throws ClientException;

	/**
	 * Diffuser l'echeance de promulgation
	 * 
	 * @param listEcheancier
	 * @param entetesDesColonnes
	 * @throws ClientException
	 */
	void diffuserEcheancierPromulgation(List<String[]> listEcheancier, String[] entetesDesColonnes)
			throws ClientException;

	/**
	 * 
	 * @param session
	 * @return
	 * @throws ClientException
	 */
	List<String> getListIdsDossierPublie(CoreSession session) throws ClientException;

	/**
	 * 
	 * @param session
	 * @param idDossier
	 * @return
	 * @throws ClientException
	 */
	EcheancierPromulgation findOrCreateEcheancierPromulgation(CoreSession session, String idDossier)
			throws ClientException;

	HashMap<String, String> getTexteLoiEnCoursDiscussionNonAdoptee2(CoreSession session) throws ClientException;

}
