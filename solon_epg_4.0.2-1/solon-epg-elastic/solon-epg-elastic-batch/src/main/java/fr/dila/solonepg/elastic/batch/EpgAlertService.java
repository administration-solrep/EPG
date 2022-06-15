package fr.dila.solonepg.elastic.batch;

import fr.dila.st.api.service.STAlertService;

/**
 * Le service de gestion de confirmation alertes, chargé d'envoyer les mails de confirmations des alertes et de désactiver ces alertes si l'utilisateur ne le fais pas.
 *
 * @author arolin
 */
public interface EpgAlertService extends STAlertService {}
