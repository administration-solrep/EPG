package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.st.api.recherche.STRechercheService;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModelList;

/**
 *
 *
 * @author jgomez
 * La classe de service pour les recherches
 *
 */

public interface RequeteurDossierSimpleService extends STRechercheService {
    public static final String REQUETE_TEXTE_INTEGRAL = "requeteTexteIntegral";

    public static final String REQUETE_CRITERES_ETAPES = "requeteCriteresEtapes";

    public static final String REQUETE_CRITERES_SECONDAIRES = "requeteCriteresSecondaires";

    public static final String REQUETE_CRITERES_PRINCIPAUX = "requeteCriteresPrincipaux";

    /**
     * Retourne une nouvelle requête
     * @param session
     * @param name
     * @return
     */
    RequeteDossierSimple getRequete(CoreSession session);

    /**
     * Retourne la requête complête, construite à partir de différents models.
     * @param model
     * @param modelNames
     * @return
     */
    String getFullQuery(RequeteDossierSimple requete, String... modelNames);

    /**
     * Retourne la requête complête, construite à partir de différents models.
     * @param model
     * @param modelNames
     * @return
     */
    DocumentModelList query(CoreSession session, RequeteDossierSimple requete);
}
