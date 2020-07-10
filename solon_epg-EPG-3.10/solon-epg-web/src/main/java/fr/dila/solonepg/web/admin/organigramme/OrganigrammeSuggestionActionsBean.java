package fr.dila.solonepg.web.admin.organigramme;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.jboss.annotation.ejb.SerializedConcurrentAccess;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.persistence.PersistenceProvider;
import org.nuxeo.ecm.core.persistence.PersistenceProvider.RunCallback;
import org.nuxeo.ecm.core.persistence.PersistenceProviderFactory;
import org.nuxeo.runtime.api.Framework;

import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.service.organigramme.OrganigrammeServiceImpl;

/**
 * Auto-complétion de l'organigramme spécifique à EPG
 * 
 * @author acleuet
 */
@Name("organigrammeSuggestionActions")
@Scope(ScopeType.SESSION)
@SerializedConcurrentAccess
@Install(precedence = Install.APPLICATION + 1)
public class OrganigrammeSuggestionActionsBean extends
		fr.dila.ss.web.admin.organigramme.OrganigrammeSuggestionActionsBean {

	/**
	 * Serial UID.
	 */
	private static final long					serialVersionUID		= 1L;

	@In(create = true, required = false)
	protected CoreSession						documentManager;

	/**
	 * Type de l'élément sélectionné
	 */
	@RequestParameter
	protected String idType;
	
	private static volatile PersistenceProvider	persistenceProvider;

	private static final String					ENTITE_LIKE_LABEL_QUERY	= "SELECT e FROM EntiteNode e WHERE UPPER(e.label) LIKE UPPER(:label) AND (e.deleted=false OR e.deleted is NULL) AND (e.dateFin is null OR e.dateFin > :curDate)";
	private static final String					GVT_LIKE_LABEL_QUERY	= "SELECT g FROM GouvernementNode g WHERE UPPER(g.label) LIKE UPPER(:label) AND (g.deleted=false OR g.deleted is NULL) AND (g.dateFin is null OR g.dateFin > :curDate)";
	private static final String					UNITE_LIKE_LABEL_QUERY	= "SELECT u FROM UniteStructurelleNode u WHERE UPPER(u.label) LIKE UPPER(:label) AND (u.deleted=false OR u.deleted is NULL) AND (u.dateFin is null OR u.dateFin > :curDate)";
	private static final String					POSTE_LIKE_LABEL_QUERY	= "SELECT p FROM PosteNode p WHERE UPPER(p.label) LIKE UPPER(:label) AND (p.deleted=false OR p.deleted is NULL) AND (p.dateFin is null OR p.dateFin > :curDate)";
	private static final String					INSTIT_LIKE_LABEL_QUERY	= "SELECT i FROM InstitutionNode i WHERE UPPER(i.label) LIKE UPPER(:label) AND (i.deleted=false OR i.deleted is NULL) AND (i.dateFin is null OR i.dateFin > :curDate)";

	/**
	 * Constructeur de OrganigrammeSuggestionActionsBean.
	 */
	public OrganigrammeSuggestionActionsBean() {
		// do nothing
	}

	/**
	 * Retourne une suggestion d'élément du LDAP selon le type en paramètre.
	 * 
	 * @param input
	 * @return
	 * @throws ClientException
	 */
	public List<OrganigrammeSuggestionDto> getSuggestions(Object input) throws ClientException {
		// Traitement sur l'input pour retirer les majuscules et les accents
		List<OrganigrammeSuggestionDto> out = new ArrayList<OrganigrammeSuggestionDto>();
		if (input == null || selectionType == null) {
			return out;
		}
		List<OrganigrammeType> directoryName = new ArrayList<OrganigrammeType>();

		if (selectionType.equals("USER_TYPE")) {
			// Cas spécial pour les utilisateurs
			List<DocumentModel> usersDoc = userSuggestionActions.getUserSuggestions(input);
			for (DocumentModel userDoc : usersDoc) {
				STUser user = userDoc.getAdapter(STUser.class);
				if (!user.isActive()) {
					continue;
				}

				OrganigrammeSuggestionDto suggestion = new OrganigrammeSuggestionDto();
				suggestion.setId(userDoc.getId());
				suggestion.setLabel(StringUtils.stripToEmpty(user.getFirstName()) + " "
						+ StringUtils.stripToEmpty(user.getLastName()));
				out.add(suggestion);
			}
			return out;
		}
		if (selectionType.contains("MIN_TYPE") || selectionType.equals("NOR_TYPE")) {
			directoryName.add(OrganigrammeType.MINISTERE);
		}
		if (selectionType.contains("DIR_TYPE") || selectionType.contains("UST_TYPE")
				|| selectionType.contains("DIR_AND_UST_TYPE")) {
			directoryName.add(OrganigrammeType.UNITE_STRUCTURELLE);
		}
		if (selectionType.contains("POSTE_TYPE") || selectionType.contains("MAILBOX_TYPE")) {
			directoryName.add(OrganigrammeType.POSTE);
		}
		if (selectionType.contains("GVT_TYPE")) {
			directoryName.add(OrganigrammeType.GOUVERNEMENT);
		}

		if (directoryName != null || !directoryName.isEmpty()) {
			String pattern = (String) input;
			String patternSansAccent = pattern.toLowerCase().replace("é", "e").replace("è", "e").replace("à", "a")
					.replace("ê", "e").replace("ë", "e").replace("ä", "a").replace("â", "a").replace("ç", "c")
					.replace("î", "i").replace("ï", "i").replace("ô", "o").replace("ö", "o").replace("ù", "u")
					.replace("ü", "u").replace("û", "u");
			// Permet de corriger le problème d'apostrophe qui se produit quand on a des apostrophe bizarres en BDD. Pas
			// utile de le faire dans le sens inverse car pas d'apostrophe comme ça sur le clavier
			String patternRemplacementQuotes = pattern.toLowerCase().replace("'", "’");
			List<OrganigrammeNode> results = getOrganigrameLikeLabel(pattern, directoryName);
			List<OrganigrammeNode> resultsSansAccent = getOrganigrameLikeLabel(patternSansAccent, directoryName);
			List<OrganigrammeNode> resultsChangementQuotes = getOrganigrameLikeLabel(patternRemplacementQuotes,
					directoryName);
			for (OrganigrammeNode nodeSansAccent : resultsSansAccent) {
				if (!results.contains(nodeSansAccent)) {
					results.add(nodeSansAccent);
				}
			}
			for (OrganigrammeNode nodeQuotes : resultsChangementQuotes) {
				if (!results.contains(nodeQuotes)) {
					results.add(nodeQuotes);
				}
			}
			final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
			for (OrganigrammeNode node : results) {

				if (node.getDeleted() || !node.isActive()) {
					continue;
				}

				// Cas des postes, une vérification est faite
				if (selectionType.contains("POSTE_TYPE") || selectionType.contains("MAILBOX_TYPE")) {
					if (activatePosteFilter == null || activatePosteFilter.equals("true")) {
						List<EntiteNode> entiteNodes = ministeresService.getMinistereParentFromPoste(node.getId());
						boolean allowed = false;
						for (EntiteNode entiteNode : entiteNodes) {
							if (entiteNode != null && organigrammeManagerActions.allowAddPoste(entiteNode.getId())) {
								allowed = true;
							}
						}
						if (!allowed) {
							continue;
						}
					}
				}

				// Ajout du node aux suggestions
				OrganigrammeSuggestionDto suggestion = new OrganigrammeSuggestionDto();
				if (selectionType.contains("MAILBOX_TYPE")) {
					suggestion.setId(organigrammeManagerActions.getMailboxIdFromPosteId(node.getId()));
				} else {
					suggestion.setId(node.getId().toString());
				}
				suggestion.setLabel(node.getLabel());
				
				if(OrganigrammeManagerActionsBean.PREFIXED_ID_TYPE.equals(idType)) {
					if (node instanceof EntiteNode) {
						suggestion.setId(STConstant.PREFIX_MIN + node.getId());
					} else if (node instanceof UniteStructurelleNode) {
						suggestion.setId(STConstant.PREFIX_US + node.getId());
					} else if (node instanceof PosteNode) {
						suggestion.setId(STConstant.PREFIX_POSTE + node.getId());
					}
				}
				
				out.add(suggestion);
			}
		}

		return out;
	}

	public List<OrganigrammeNode> getOrganigrameLikeLabel(final String label, final List<OrganigrammeType> types)
			throws ClientException {
		List<OrganigrammeNode> out = new ArrayList<OrganigrammeNode>();
		for (OrganigrammeType type : types) {
			out.addAll(getOrganigrameLikeLabel(label, type));
		}
		return out;
	}

	@SuppressWarnings("unchecked")
	public List<OrganigrammeNode> getOrganigrameLikeLabel(final String label, final OrganigrammeType type)
			throws ClientException {

		return getOrCreatePersistenceProvider().run(true, new RunCallback<List<OrganigrammeNode>>() {

			@Override
			public List<OrganigrammeNode> runWith(EntityManager manager) throws ClientException {
				Query query = null;
				switch (type) {
					case GOUVERNEMENT:
						query = manager.createQuery(GVT_LIKE_LABEL_QUERY);
						break;
					case UNITE_STRUCTURELLE:
					case DIRECTION:
						query = manager.createQuery(UNITE_LIKE_LABEL_QUERY);
						break;
					case INSTITUTION:
						query = manager.createQuery(INSTIT_LIKE_LABEL_QUERY);
						break;
					case MINISTERE:
						query = manager.createQuery(ENTITE_LIKE_LABEL_QUERY);
						break;
					case POSTE:
						query = manager.createQuery(POSTE_LIKE_LABEL_QUERY);
						break;
					default:
						break;
				}

				if (query != null) {
					query.setParameter("label", "%" + label + "%");
					query.setParameter("curDate", Calendar.getInstance());
					List<OrganigrammeNode> groupModelList = query.getResultList();
					List<Object> params = new ArrayList<Object>();
					params.add(label);
					params.add(Calendar.getInstance());

					return groupModelList;
				}
				return new ArrayList<OrganigrammeNode>();
			}
		});

	}

	protected static PersistenceProvider getOrCreatePersistenceProvider() {
		if (persistenceProvider == null) {
			synchronized (OrganigrammeServiceImpl.class) {
				if (persistenceProvider == null) {
					activatePersistenceProvider();
				}
			}
		}
		return persistenceProvider;
	}

	protected static void activatePersistenceProvider() {
		Thread thread = Thread.currentThread();
		ClassLoader last = thread.getContextClassLoader();
		try {
			thread.setContextClassLoader(PersistenceProvider.class.getClassLoader());
			PersistenceProviderFactory persistenceProviderFactory = Framework
					.getLocalService(PersistenceProviderFactory.class);
			persistenceProvider = persistenceProviderFactory.newProvider("organigramme-provider");
			persistenceProvider.openPersistenceUnit();
		} finally {
			thread.setContextClassLoader(last);
		}
	}
}