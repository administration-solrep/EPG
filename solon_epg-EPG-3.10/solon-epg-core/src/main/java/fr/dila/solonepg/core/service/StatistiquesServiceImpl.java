package fr.dila.solonepg.core.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

import org.jboss.seam.annotations.In;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelComparator;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IterableQueryResult;

import fr.dila.solonepg.api.service.StatistiquesService;
import fr.dila.ss.api.documentmodel.SSInfoUtilisateurConnection;
import fr.dila.ss.api.service.SSUtilisateurConnectionMonitorService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.recherche.UserRequeteur;
import fr.dila.st.core.util.DateUtil;

public class StatistiquesServiceImpl implements StatistiquesService {

	// private static final Log log = LogFactory.getLog(StatistiquesServiceImpl.class);

	@In(create = true, required = true)
	protected transient CoreSession	documentManager;

	/**
	 * get Users By status(active or not active)
	 * 
	 * @param active
	 *            the user status
	 * @return list of users
	 * @throws ClientException
	 */
	@Override
	public DocumentModelList getUsers(boolean active) throws ClientException {

		String query = "";
		if (active) {
			query = "(&(&(objectClass=personne)(uid=*))(deleted={1})(|(dateFin>={0})(!(dateFin=*))))";
		} else {
			query = "(&(&(objectClass=personne)(uid=*))(deleted={1})(dateFin<={0}))";
		}
		List<String> args = new ArrayList<String>();
		Date today = Calendar.getInstance().getTime();
		SimpleDateFormat dateFormat = DateUtil.simpleDateFormat("yyyyMMddHHmmss'Z'");
		dateFormat.setTimeZone(new SimpleTimeZone(0, "Z"));
		String date = dateFormat.format(today);
		args.add(date);
		args.add("FALSE");
		UserRequeteur userRequeteur = new UserRequeteur();
		Map<String, String> orderBy = new HashMap<String, String>();
		orderBy.put("lastName", DocumentModelComparator.ORDER_ASC);
		return userRequeteur.searchUsers(query, args, orderBy);
	}

	/**
	 * get Liste des utilisateurs ne s’étant pas connectés depuis une certaine durée
	 * 
	 * @param dateDeConnexion
	 *            la date depuis laquelle l'utilisateur ne s'est pas connecte
	 * @return liste des users
	 * @throws ClientException
	 */
	@Override
	public DocumentModelList getUsers(CoreSession session, Date dateDeConnexion) throws ClientException {

		// on récupère d'abord en base grâce à la table info_utilisateur_connection
		ArrayList<String> usersToFetch = new ArrayList<String>();
		String query = "SELECT username as id from info_utilisateur_connection where DATEDERNIERECONNEXION < ?";
		IterableQueryResult res = null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateDeConnexion);
		Map<String, Calendar> mapUsersIDdDate = new HashMap<String, Calendar>();
		try {
			res = QueryUtils.doSqlQuery(session, new String[] { FlexibleQueryMaker.COL_ID }, query,
					new Object[] { cal });
			final Iterator<Map<String, Serializable>> iterator = res.iterator();
			while (iterator.hasNext()) {
				final Map<String, Serializable> row = iterator.next();
				String userName = (String) row.get(FlexibleQueryMaker.COL_ID);
				usersToFetch.add(userName);
				SSUtilisateurConnectionMonitorService ssUtilisateurConnectionMonitorService = SSServiceLocator
						.getUtilisateurConnectionMonitorService();
				SSInfoUtilisateurConnection infoUserConnection = ssUtilisateurConnectionMonitorService
						.getInfoUtilisateurConnection(session, userName);
				mapUsersIDdDate.put(userName, infoUserConnection.getDateDerniereConnexion());
			}
		} catch (ClientException ce) {
			throw ce;
		} finally {
			if(res!=null) {
				res.close();
			}
		}

		// puis fetch dans le LDAP
		StringBuilder ldapquery = new StringBuilder("(&(objectClass=personne)(|");
		for (String iusername : usersToFetch) {
			ldapquery.append("(uid=").append(iusername).append(")");
		}
		ldapquery.append("))");
		UserRequeteur userRequeteur = new UserRequeteur();
		Map<String, String> orderBy = new HashMap<String, String>();
		orderBy.put("lastName", DocumentModelComparator.ORDER_ASC);
		DocumentModelList listUsersLdap = userRequeteur.searchUsers(ldapquery.toString(), new ArrayList<String>(),
				orderBy);
		for (DocumentModel userLdap : listUsersLdap) {
			STUser stuserLdap = userLdap.getAdapter(STUser.class);
			stuserLdap.setDateLastConnection(mapUsersIDdDate.get(stuserLdap.getUsername()));
		}
		return listUsersLdap;
	}
}
