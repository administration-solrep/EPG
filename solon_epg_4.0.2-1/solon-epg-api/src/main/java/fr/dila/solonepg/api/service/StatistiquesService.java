package fr.dila.solonepg.api.service;

import java.util.Calendar;
import java.util.Map;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModelList;

/**
 * Service qui permet de gérer les statistiques
 *
 * @author admin
 */
public interface StatistiquesService {
    /**
     * get Users By status(active or not active)
     *
     * @param active
     *            the user status
     * @return list of users
     */
    DocumentModelList getUsers(boolean active);

    /**
     * get Count des typed d'acte par type dans l'interval de date
     *
     * @param dateDebut
     *            Date de début de l'interval
     * @param dateFin
     *            Date de fin de l'interval
     * @return liste des users
     */
    Map<String, String> getNbTypeActeParType(CoreSession session, Calendar dateDebut, Calendar dateFin);
}
