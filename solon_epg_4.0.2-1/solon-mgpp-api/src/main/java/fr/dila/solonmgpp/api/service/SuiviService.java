package fr.dila.solonmgpp.api.service;

import fr.dila.solonmgpp.api.domain.ActiviteParlementaire;
import fr.dila.solonmgpp.api.domain.EcheancierPromulgation;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface SuiviService {
    /**
     * rtourne Les textes de loi en cours de discussion et non encore définitivement adopté
     *
     * @param session
     * @return Les textes de loi en cours de discussion et non encore définitivement adopté
     */
    List<DocumentModel> getTexteLoiEnCoursDiscussionNonAdoptee(CoreSession session);

    /**
     * recuperer toutes les echeanciers de promulgation
     *
     * @param session
     * @return la liste de l'echeancier de promulgation
     */
    List<DocumentModel> getAllEcheancierPromulgation(CoreSession session);

    /**
     * recuperer le nombres des echeanciers de promulgation
     *
     * @param session
     * @return
     */
    Long getEcheancierPromulgationCount(CoreSession session);

    /**
     *
     * recuperer les echeanciers de promulgation par page
     *
     * @param session
     * @param limit
     * @param offset
     * @return
     */
    List<DocumentModel> getEcheancierPromulgationPage(CoreSession session, int limit, int offset);

    /**
     * creer un echeancier de promulgation
     *
     * @param session
     * @param idDossier
     *            l'id du dossier de la fiche de loi
     */
    EcheancierPromulgation creerEcheancierPromulgation(CoreSession session, String idDossier);

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
     */
    void creerActiviteParlementaire(CoreSession session, String idDossier, String activite, String assemble, Date date);

    /**
     * supprimer une activite parlementaire
     *
     * @param session
     * @param activiteId
     *            activite parlementaire Id
     */
    void removeActiviteParlementaire(CoreSession session, String activiteId);

    /**
     * supprimer une semaine parlementaire
     *
     * @param session
     * @param activiteId
     *            activite parlementaire Id
     */
    void removeSemaineParlementaire(CoreSession session, String activiteId);

    /**
     * recuperer toutes les textes de loi du calendrier parlememntaire
     *
     * @param session
     * @return toutes les textes de loi du calendrier parlememntaire
     */
    List<DocumentModel> getAllTexteLoiPourCalendrierParlementaire(CoreSession session, Date startDate, Date endDate);

    /**
     * recuperer toutes les dates des activites parlementaires
     *
     * @param session
     * @return la liste de toutes les dates des activites parlementaires
     */
    List<Calendar> getAllDateActiviteParlementaire(CoreSession session);

    /**
     * recuperer toutes les dates des activites parlementaires
     *
     * @param session
     * @param startDate
     * @param endDate
     * @return la liste de toutes les dates des activites parlementaires
     */
    List<Calendar> getAllDateActiviteParlementaire(CoreSession session, Date startDate, Date endDate);

    /**
     * recuperer toutes les activites parlemetaires concernant une fiche de loi
     *
     * @param session
     * @param DossierId
     *            l'id du dossier de la fiche de loi
     * @return la liste de toutes les activites parlemetaires concernant une fiche de loi
     */
    List<DocumentModel> getAllActiviteParlementaireByDossierId(CoreSession session, String DossierId);

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
     */
    void creerSemaineParlementaire(
        CoreSession session,
        String orientation,
        String assemblee,
        Calendar dateDebut,
        Calendar dateFin
    );

    List<DocumentModel> getAllSemaineParlementaire(CoreSession session, String assemble);

    /**
     * Diffuser la calendrier Parlementaire
     *
     * @param listedeSemainesParTypes
     * @param listeDeToutesLesDatesDesActivites
     * @param listeDeToutLesLois
     * @param mapDeToutesLesActivitesParlementaireDeCeLoi
     */
    void diffuserCalendrierParlementaire(
        Map<String, List<Map<String, String>>> listedeSemainesParTypes,
        List<Date> listeDeToutesLesDatesDesActivites,
        List<FicheLoi> listeDeToutLesLois,
        Map<String, Collection<List<ActiviteParlementaire>>> mapDeToutesLesActivitesParlementaireDeCeLoi
    );

    /**
     * Diffuser l'echeance de promulgation
     *
     * @param listEcheancier
     * @param entetesDesColonnes
     */
    void diffuserEcheancierPromulgation(List<String[]> listEcheancier, String[] entetesDesColonnes);

    /**
     *
     * @param session
     * @return
     */
    List<String> getListIdsDossierPublie(CoreSession session);

    /**
     *
     * @param session
     * @param idDossier
     * @return
     */
    EcheancierPromulgation findOrCreateEcheancierPromulgation(CoreSession session, String idDossier);

    HashMap<String, String> getTexteLoiEnCoursDiscussionNonAdoptee2(CoreSession session);
}
