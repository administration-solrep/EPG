package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.TexteTransposition;
import fr.dila.solonepg.api.cases.TranspositionDirective;
import fr.dila.solonepg.api.dto.TexteTranspositionDTO;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Interface pour la gestion des transpositions de directive européenne
 *
 * @author asatre
 *
 */
public interface TranspositionDirectiveService extends Serializable {
    /**
     * recherche une transposition de directive européenne, creation si inexistante
     *
     * @param session
     * @param numero
     * @return
     */
    TranspositionDirective findOrCreateTranspositionDirective(CoreSession session, String numero);

    /**
     * mise a jour d'une transposition de directive européenne
     *
     * @param session
     * @param numero
     * @return
     */
    TranspositionDirective updateTranspositionDirective(
        CoreSession session,
        TranspositionDirective transpositionDirective
    );

    /**
     * recherche des dossiers lié à la transposition de directive européenne
     *
     * @param transpositionDirective
     * @param session
     * @return
     */
    Set<String> findLinkedDossierNor(TranspositionDirective transpositionDirective, CoreSession session);

    /**
     * construction de la liste des numero de directive européennes
     *
     * @param dossier
     * @param session
     */
    void attachDirectiveEuropenneToDossier(Dossier dossier, CoreSession session);

    /**
     * Mise ajour d'une transposition de directive européenne avec verification que son numero est unique
     *
     * @param session
     * @param transpositionDirective
     * @return
     */
    TranspositionDirective updateTranspositionDirectiveWithCheckUnique(
        CoreSession session,
        TranspositionDirective transpositionDirective
    );

    /**
     * retourne les {@link TexteTransposition} de la {@link TranspositionDirective}, creation des elements si non
     * existant
     *
     * @param transpositionDirective
     * @param session
     * @return
     */
    List<TexteTransposition> fetchTexteTransposition(
        TranspositionDirective transpositionDirective,
        CoreSession session
    );

    /**
     * Sauvegarde de la liste de {@link TexteTransposition} d'une {@link TranspositionDirective}
     *
     * @param transpositionDirective
     * @param listTexteTranspositionDTO
     * @param session
     * @return
     */
    TranspositionDirective saveTexteTranspositionDTO(
        TranspositionDirective transpositionDirective,
        List<TexteTranspositionDTO> listTexteTranspositionDTO,
        CoreSession session
    );

    /**
     * rechargement des {@link TexteTranspositionDTO} à partir des nor qu'ils contiennent
     *
     * @param listTexteTranspositionDTO
     * @param session
     */
    void refreshTexteTransposition(List<TexteTranspositionDTO> listTexteTranspositionDTO, CoreSession session);

    /**
     * reacharge completement une transposition
     *
     * @param adapter
     * @param session
     * @return
     */
    TranspositionDirective reloadTransposition(TranspositionDirective adapter, CoreSession session);

    /**
     * Recherche par webService EURLEX si une directive existe
     *
     * @return
     */
    String findDirectiveEurlexWS(String reference, Integer annee, String titre);
}
