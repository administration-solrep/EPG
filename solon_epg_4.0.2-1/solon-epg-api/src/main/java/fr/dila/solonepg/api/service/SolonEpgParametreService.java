package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.administration.ParametrageAN;
import fr.dila.st.api.service.STParametreService;
import java.util.Map;
import javax.security.auth.login.LoginException;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Surcharge du service de paramètre
 *
 */
public interface SolonEpgParametreService extends STParametreService {
    /**
     * Renvoie l'url de la page de suivi de l'application des lois
     *
     * @return
     * @throws LoginException
     */
    String getUrlSuiviApplicationLois() throws LoginException;

    /**
     * Permet de retourner le document qui contient tout le paramétrage des législatures pour l'AN
     *
     * @param documentManager
     * @return
     */
    ParametrageAN getDocAnParametre(CoreSession documentManager);

    /**
     * Permet de mettre à jour les textes-maitres impactés par un changement d'une législature de la liste
     *
     * @param oldAndNewLegis
     *            map de remplacement des valeurs
     * @return
     */
    void updateLegislaturesFromTextesMaitres(CoreSession session, Map<String, String> oldAndNewLegis);
}
