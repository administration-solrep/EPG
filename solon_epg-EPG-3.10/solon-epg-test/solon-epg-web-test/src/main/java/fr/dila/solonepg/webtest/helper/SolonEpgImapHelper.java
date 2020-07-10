package fr.dila.solonepg.webtest.helper;


import java.io.IOException;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.dila.solonepg.webtest.utils.MailUtils;
import fr.sword.naiad.commons.webtest.helper.AbstractImapHelper;
import fr.sword.naiad.commons.webtest.mail.ImapConsult;

public class SolonEpgImapHelper extends AbstractImapHelper{
	
	private static final String SUBJECT_CREATION_TACHE = "Un nouveau dossier est disponible dans l'espace de traitement";
	private static final String MAILBOX_HOST_DEFAULT = "idlv-mail-hms.lyon-dev2.local";
	public static final String MAILBOX_USER_DEFAULT = "postmaster@solon-epg.com";
	private static final String MAILBOX_PASSWORD_DEFAULT = "postmaster";
	
	private static final SolonEpgImapHelper instance = new SolonEpgImapHelper();
	
    private static final Log LOGGER = LogFactory.getLog(SolonEpgImapHelper.class);
			
	protected SolonEpgImapHelper(){
		super(MAILBOX_HOST_DEFAULT, MAILBOX_USER_DEFAULT, MAILBOX_PASSWORD_DEFAULT);
	}
	
	public static SolonEpgImapHelper getInstance(){
		return instance;
	}
	
	
	public void outputMessages(Message[] messages) throws MessagingException, IOException {
		if (messages == null){
			return;
		}
		for ( Message message : messages){
			if (message == null){
				LOGGER.warn("Le message est nul, passe au suivant");
				continue;
			}
			LOGGER.info("Description : " + message.getDescription());
			LOGGER.info("Sujet : " + message.getSubject());
			LOGGER.info("From : " + output(message.getFrom()));
			LOGGER.info("To : " + output(message.getReplyTo()));
			LOGGER.info("Contenu : " + message.getContent().toString());
		}
	}
	
	public Message[] waitForTaskMessages(ImapConsult imap) throws MessagingException, IOException, InterruptedException{
		imap.close();
		imap.connect();
		Thread.sleep(15000);
		Message[] messages = imap.searchInboxMessage(getSearchForSubjectTask());
		outputMessages(messages);
		return messages;
	}
	
	/**
	 * Retourne l'url de la tache
	 * 
	 * @param message
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public String getDossierLink(Message message) throws MessagingException, IOException {
		if ( message == null || message.getContent() == null ){
			return null;
		}
		return MailUtils.getUrl(message.getContent().toString());
	}
	
	public void clearInbox(ImapConsult imap) throws MessagingException{
		imap.clearInboxMail();
	}
	
	private String output(Address[] addresses) throws MessagingException {
		String result = "";
		for (Address address : addresses){
			result += address.toString();
		}
		return result;
	}
	
	public SearchTerm getSearchForSubjectTask(){
		SearchTerm term = getSearchTermForSubject(SUBJECT_CREATION_TACHE);
		return term;
	}

	private SearchTerm getSearchTermForSubject(String subject) {
		FlagTerm nonDeletedTerm = new FlagTerm(new Flags(Flags.Flag.DELETED), false);
		SubjectTerm subjectTerm = new SubjectTerm(subject);
		SearchTerm term = new AndTerm(subjectTerm, nonDeletedTerm);
		return term;
	}

	/**
	 * Retourne vrai si le message contient la phrase 1 résultat(s)
	 * @param alert
	 * @return
	 * @throws MessagingException 
	 * @throws IOException 
	 */
	public boolean mailContientUnResultat(Message message) throws IOException, MessagingException {
		if ( message == null || message.getContent() == null ){
			return false;
		}
		String content = StringUtils.EMPTY;
		if (message.isMimeType("multipart/*")){
			Multipart multiPart = (Multipart) message.getContent();
			BodyPart bodyPart = multiPart.getBodyPart(0);
			content = (String) bodyPart.getContent();
		}
		Boolean result = content.toString().contains("1 résultat(s)");
		return result;
	}
	
}
