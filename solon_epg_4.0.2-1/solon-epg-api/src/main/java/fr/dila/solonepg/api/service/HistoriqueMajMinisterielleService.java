package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.MajMin;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants.MAJ_TARGET;
import fr.dila.solonepg.api.dto.MesureApplicativeDTO;
import fr.dila.st.api.domain.ComplexeType;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;

/**
 * Interface pour le service d'historique des mises à jour des ministères  dans l'activité normative
 * @author jgomez
 *
 */
public interface HistoriqueMajMinisterielleService {
    /**
     * Enregistre les mises à jour ministérielles du dossier : si des applications de loi, des directives ou des ordonnances ont été mises à jour,
     * des documents MajMin correspondants sont créées.
     *
     * @param session la session de l'utilisateur
     * @param dossierDoc le document model qui représente le dossier modifié
     */
    void registerMajDossier(CoreSession session, DocumentModel dossierDoc);

    /**
     * Enregistre les mises à jour ministérielles d'une mesure : si des applications de loi, ou des ordonnances ont été mises à jour,
     * des documents MajMin correspondants sont créés.
     *
     * @param session la session de l'utilisateur
     * @param texteMaitre le texte maitre de la mesure
     * @param MesureApplicative la mesure existante
     * @param MesureApplicativeDTO la mesure modifiée
     */
    void registerMajMesure(
        CoreSession session,
        TexteMaitre texteMaitre,
        MesureApplicative existingMesure,
        MesureApplicativeDTO updatedMesure
    );

    /**
     * Retourne les différences entre deux listes de complexeType
     * @param initialList la liste de départ
     * @param finalList la liste finale
     * @return une liste des mises à jour qui ont été effectuées
     */
    List<MajMin> createDifference(
        CoreSession session,
        Dossier dossier,
        MAJ_TARGET target,
        List<ComplexeType> initialList,
        List<ComplexeType> finalList
    );

    /**
     * Retourne la liste des mises à jour par rapport à une cible donnée (Ordonnance, Transposition, Application loi)
     * @param session la session de l'utilisateur
     * @param target la cible de l'historique
     * @return
     */
    List<DocumentModel> getHistoriqueMaj(CoreSession session, MAJ_TARGET target);

    /**
     * Retourne la référence de la racine des transpositions, ordonnances ou applicationLoi
     * @param target
     * @return
     */
    DocumentRef getRef(MAJ_TARGET target);

    /**
     * Retourne la référence de la racine des ordonnances
     * @param target
     * @return
     */
    DocumentRef getOrdonnanceRef(CoreSession session);

    /**
     * Retourne la référence de la racine des  applicationLoi
     * @param target
     * @return
     */
    DocumentRef getApplicationLoiRef(CoreSession session);

    /**
     * Retourne la référence de la racine des transpositions
     * @param target
     * @return
     */
    DocumentRef getTranspositionRef(CoreSession session);

    /**
     * Retourne l'historique complet
     * @param target
     * @return
     */
    List<Map<String, Serializable>> getHistoriqueMajMap(CoreSession session, MAJ_TARGET target);

    /**
     * Crée un document Mise à jour dans le répertoire des mises à jour habilitation
     *
     * @param session la session utilisateur
     * @return la maj créée
     */
    MajMin createMajMinHabilitation(CoreSession session, Dossier oldDossier, Dossier newDossier);
}
