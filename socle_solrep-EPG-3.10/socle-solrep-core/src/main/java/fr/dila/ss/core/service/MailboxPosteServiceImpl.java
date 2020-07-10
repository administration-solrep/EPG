package fr.dila.ss.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.IdUtils;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

import fr.dila.cm.mailbox.Mailbox;
import fr.dila.cm.mailbox.MailboxConstants;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.ss.api.service.MailboxPosteService;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.security.principal.STPrincipal;
import fr.dila.st.api.service.MailboxService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.StringUtil;
import fr.dila.st.core.util.UnrestrictedCreateOrSaveDocumentRunner;

/**
 * Implémentation du service permettant de gérer les Mailbox des postes.
 * 
 * @author jtremeaux
 */
public class MailboxPosteServiceImpl implements MailboxPosteService {
	/**
	 * Serial UID
	 */
	private static final long	serialVersionUID	= 583183498516278951L;

	/**
	 * Logger.
	 */
	private static final Log	LOG					= LogFactory.getLog(MailboxPosteServiceImpl.class);

	/**
	 * Default constructor
	 */
	public MailboxPosteServiceImpl() {
		// do nothing
	}

	@Override
	public String getPosteMailboxId(String posteName) {
		return IdUtils.generateId(SSConstant.MAILBOX_POSTE_ID_PREFIX + posteName, "-", true, 50);
	}

	@Override
	public List<Mailbox> getMailboxPosteList(CoreSession session) throws ClientException {
		Set<String> posteIdSet = ((STPrincipal) session.getPrincipal()).getPosteIdSet();

		return getMailboxPosteList(session, posteIdSet);
	}

	@Override
	public List<Mailbox> getMailboxPosteList(CoreSession session, Collection<String> posteIdSet) throws ClientException {
		List<String> mailboxIdList = new ArrayList<String>();
		for (String posteId : posteIdSet) {
			String mailboxId = getPosteMailboxId(posteId);
			mailboxIdList.add(mailboxId);
		}

		return STServiceLocator.getMailboxService().getMailbox(session, mailboxIdList);
	}

	@Override
	public Mailbox getMailboxPoste(CoreSession session, String posteId) throws ClientException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Récupération de la mailbox du poste=<" + posteId + ">");
		}

		String mailboxId = getPosteMailboxId(posteId);
		Mailbox mailbox = STServiceLocator.getMailboxService().getMailboxUnrestricted(session, mailboxId);
		if (mailbox == null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Pas de mailbox avec l'id=<" + mailboxId + ">");
			}
		}

		return mailbox;
	}

	@Override
	public String getMailboxSggId() throws ClientException {
		OrganigrammeNode posteSGG = STServiceLocator.getSTPostesService().getSGGPosteNode();
		if (posteSGG == null) {
			throw new ClientException("Pas de poste pour le SGG");
		}
		return getPosteMailboxId(posteSGG.getId());
	}

	@Override
	public Mailbox getOrCreateMailboxPoste(CoreSession session, String posteId) throws ClientException {
		Mailbox mailbox = getMailboxPoste(session, posteId);
		if (mailbox == null) {

			OrganigrammeNode poste = STServiceLocator.getSTPostesService().getPoste(posteId);
			mailbox = createPosteMailbox(session, posteId, poste.getLabel());
		}

		return mailbox;
	}

	@Override
	public Mailbox getOrCreateMailboxPosteNotUnrestricted(CoreSession session, String mailboxId) throws ClientException {
		Mailbox mailbox = getMailboxPoste(session, mailboxId);
		if (mailbox == null) {
			OrganigrammeNode poste = STServiceLocator.getSTPostesService().getPoste(mailboxId);
			mailbox = createPosteMailboxNotUnrestricted(session, mailboxId, poste.getLabel());
		}

		return mailbox;
	}

	/**
	 * Renvoie un Id de mailbox unique.
	 * 
	 * @param session
	 *            session
	 * @param posteId
	 *            id du poste
	 * @return id de la mailbox
	 * @throws ClientException
	 */
	protected String getUniqueMailboxId(CoreSession session, String posteId) throws ClientException {
		final MailboxService mailboxService = STServiceLocator.getMailboxService();
		String mailboxId = getPosteMailboxId(posteId);

		String uniqueMailboxId = mailboxId;

		int count = 0;
		String mailboxDocId = null;
		do {
			mailboxDocId = mailboxService.getMailboxDocIdUnrestricted(session, uniqueMailboxId);
			if (mailboxDocId != null) {
				if (mailboxId.length() == STConstant.MAILBOX_POSTE_ID_MAX_LENGTH) {
					mailboxId = mailboxId.substring(0, mailboxId.length() - 2);
				}
				if (StringUtil.isNotEmpty(mailboxId) && count != 0 && count % 10 == 0) {
					mailboxId = mailboxId.substring(0, mailboxId.length() - 1);
				}
				uniqueMailboxId = mailboxId + "_" + count;
				count++;
			}
		} while (mailboxDocId != null);

		return uniqueMailboxId;
	}

	protected Mailbox createPosteMailbox(CoreSession session, String posteId, String posteLabel, boolean isUnrestricted)
			throws ClientException {

		final MailboxService mailboxService = STServiceLocator.getMailboxService();
		String mailboxId = getUniqueMailboxId(session, posteId);

		// Sauvegarde la Mailbox dans la première MailboxRoot trouvée
		DocumentModel mailboxRoot = mailboxService.getMailboxRoot(session);

		// Crée la Mailbox poste
		DocumentModel mailboxModel = session.createDocumentModel(mailboxService.getMailboxType());
		final Mailbox mailbox = mailboxModel.getAdapter(Mailbox.class);

		// Détermine le nom de l'utilisateur qui possède les Mailbox poste
		String mailboxPosteOwner = getMailboxPosteOwner();

		// Renseigne les propriétés de la Mailbox
		mailbox.setId(mailboxId);
		mailbox.setTitle(posteLabel);
		mailbox.setOwner(mailboxPosteOwner);
		mailbox.setType(MailboxConstants.type.generic.name());

		// Ajoute l'ID de la Mailbox en tant que groupe. Cela permet d'accéder à la mailbox via les ACL.
		List<String> groups = Collections.singletonList(mailboxId);
		mailbox.setGroups(groups);

		mailboxModel.setPathInfo(mailboxRoot.getPathAsString(), IdUtils.generateId(mailbox.getTitle(), "-", true, 24));

		if (LOG.isInfoEnabled()) {
			String mode = (isUnrestricted ? "mode non restreint" : "mode restreint");
			LOG.info("Création de la MailBox " + mailboxId + " en " + mode + " par l'utilisateur "
					+ session.getPrincipal().getName());
		}

		if (isUnrestricted) {
			// Crée la Mailbox
			mailboxModel = new UnrestrictedCreateOrSaveDocumentRunner(session).createDocument(mailboxModel);

		} else {
			mailboxModel = session.createDocument(mailboxModel);
		}

		return mailboxModel.getAdapter(Mailbox.class);
	}

	@Override
	public Mailbox createPosteMailboxNotUnrestricted(CoreSession session, String posteId, String posteLabel)
			throws ClientException {
		return createPosteMailbox(session, posteId, posteLabel, Boolean.FALSE);
	}

	@Override
	public Mailbox createPosteMailbox(CoreSession session, String posteId, String posteLabel) throws ClientException {
		return createPosteMailbox(session, posteId, posteLabel, Boolean.TRUE);
	}

	@Override
	public List<EntiteNode> getMinistereListFromMailbox(String mailboxId) throws ClientException {
		String posteId = getPosteIdFromMailboxId(mailboxId);
		return STServiceLocator.getSTMinisteresService().getMinistereParentFromPoste(posteId);
	}

	@Override
	public String getIdDirectionFromMailbox(String mailboxId) throws ClientException {
		String posteId = getPosteIdFromMailboxId(mailboxId);
		List<OrganigrammeNode> directions = STServiceLocator.getSTUsAndDirectionService()
				.getDirectionFromPoste(posteId);

		if (directions == null || directions.isEmpty()) {
			return null;
		}

		return directions.get(0).getLabel();
	}

	/**
	 * Retourne le propriétaire des mailbox poste.
	 * 
	 * @return Propriétaire des mailbox poste
	 */
	protected String getMailboxPosteOwner() {
		return STConstant.NUXEO_SYSTEM_USERNAME;
	}

	@Override
	public String getPosteIdFromMailboxId(String mailboxId) {
		return mailboxId.substring(SSConstant.MAILBOX_POSTE_ID_PREFIX.length());
	}

	@Override
	public Set<String> getMailboxPosteIdSetFromPosteIdSet(Collection<String> posteIdSet) {
		Set<String> mailboxIdSet = new HashSet<String>();
		for (String posteId : posteIdSet) {
			mailboxIdSet.add(getPosteMailboxId(posteId));
		}
		return mailboxIdSet;
	}

	@Override
	public List<Mailbox> createPosteMailboxes(CoreSession session, DocumentModel userDoc) throws ClientException {
		// Récupère l'ensemble des postes de l'utilisateur
		STUser user = userDoc.getAdapter(STUser.class);
		List<String> posteIdList = user.getPostes();

		// Crée (si nécessaire) une Mailbox pour chacun des postes
		List<Mailbox> mailboxList = new ArrayList<Mailbox>();
		for (String posteId : posteIdList) {

			Mailbox mailbox = getOrCreateMailboxPoste(session, posteId);
			mailboxList.add(mailbox);
		}

		return mailboxList;
	}

	@Override
	public String getMinisteresFromMailboxId(String mailboxId) {
		// Étapes sans destinataire
		if (StringUtil.isEmpty(mailboxId)) {
			return "";
		}

		final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
		String ministeres = "**nom du ministère inconnu**";
		try {
			List<EntiteNode> organigrammeList = mailboxPosteService.getMinistereListFromMailbox(mailboxId);
			if (organigrammeList != null && !organigrammeList.isEmpty()) {
				List<String> ministereList = new ArrayList<String>();
				for (OrganigrammeNode node : organigrammeList) {
					ministereList.add(node.getLabel());
				}
				ministeres = StringUtil.join(ministereList, ", ");
			}
		} catch (Exception e) {
			LOG.warn(e, e);
		}
		return ministeres;
	}

	@Override
	public String getMinisteresEditionFromMailboxId(String mailboxId) {
		// Étapes sans destinataire
		if (StringUtil.isEmpty(mailboxId)) {
			return "";
		}

		final MailboxPosteService mailboxPosteService = SSServiceLocator.getMailboxPosteService();
		String ministeres = "**nom du ministère inconnu**";
		try {
			List<EntiteNode> organigrammeList = mailboxPosteService.getMinistereListFromMailbox(mailboxId);
			if (organigrammeList != null && !organigrammeList.isEmpty()) {
				List<String> ministereList = new ArrayList<String>();
				for (EntiteNode node : organigrammeList) {
					ministereList.add(node.getEdition());
				}
				ministeres = StringUtil.join(ministereList, ", ");
			}
		} catch (Exception e) {
			LOG.warn(e, e);
		}
		return ministeres;
	}

}
