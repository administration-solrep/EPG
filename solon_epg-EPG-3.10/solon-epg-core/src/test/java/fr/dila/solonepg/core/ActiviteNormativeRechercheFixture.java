package fr.dila.solonepg.core;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.PathRef;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.Decret;
import fr.dila.solonepg.api.cases.MesureApplicative;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.core.service.TestRechercheActiviteNormative;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.core.util.DocUtil;


/**
 * 
 * Création de documents d'application des lois, décrets et mesures pour tester les jointures de la recherche de l'activité 
 * normative 
 * @author jgomez
 * @param <E>
 *
 */
public class ActiviteNormativeRechercheFixture {

	private static final String DECRET = "Decret";

	private static final String MESURE = "Mesure";

	private static final String LOI = "Loi";

	/**
	 * La session sur laquelle on crée les documents
	 */
	private CoreSession session;
	
    private static final Log LOGGER = LogFactory.getLog(TestRechercheActiviteNormative.class);
	
	public ActiviteNormativeRechercheFixture(CoreSession session){
		this.session = session;
	}
    
	public String toString(TexteMaitre txtMaitre) {
		return txtMaitre.getMotCle() + " " + getId(txtMaitre);
	}
	
	public String getId(TexteMaitre txtMaitre) {
		return txtMaitre.getDocument().getName();
	}
	
	
    /**
     * On crée les liens entre les documents de l'activité normative
     * @throws Exception
     */
    public void initRepo() throws ClientException{
    	setUpLinksBetweenActiviteNormatives();
    	setUpDate();
     	session.save();
    }

	private void setUpDate() throws ClientException {
		TexteMaitre txtMaitre = getTexteMaitre(LOI,"loi01");
    	DateTime jodaDate = new DateTime();
    	jodaDate = jodaDate.minusDays(2);
    	Date date = jodaDate.toDate();
    	LOGGER.info("La date d.texm:dateReunionProgrammation est initialisé à : " + DateUtil.convert(jodaDate));
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	txtMaitre.setDateReunionProgrammation(cal);
    	txtMaitre.save(session);
	}

	private void setUpLinksBetweenActiviteNormatives() throws ClientException {
		for (TexteMaitre loi: getTexteMaitres(LOI,"loi%")){
    		LOGGER.info("Attachement des mesures de la loi " + toString(loi));
    		List<TexteMaitre> mesures = getTexteMaitres(MESURE,getId(loi) +"%");
    		createLinkMesures(getId(loi),mesures);
    		for (TexteMaitre mesure: mesures){
        		LOGGER.info("Attachement des décrets de la mesure " + toString(mesure));
    			List<TexteMaitre> decrets =  getTexteMaitres(DECRET,getId(mesure) +"%");
    			createLinkDecrets(getId(mesure),decrets);
    		}
    		
    	}
	}
    

	private void createLinkMesure(String intituleLoi, String intituleMesure) throws ClientException {
    	TexteMaitre loi = getTexteMaitre(LOI,intituleLoi);
    	MesureApplicative mesure = getMesureApplicative(intituleMesure);
    	List<String> mesureIds = loi.getMesuresIds();
    	mesureIds.add(mesure.getId());
    	loi.setMesuresIds(mesureIds);
    	loi.save(session);
	}
    
    private void createLinkMesures(String intituleLoi, List<TexteMaitre> mesures) throws ClientException {
    	for (TexteMaitre mesure: mesures){
    		createLinkMesure(intituleLoi, getId(mesure));
    	}
    }
    
    private void createLinkDecrets(String intituleMesure, List<TexteMaitre> decrets) throws ClientException {
    	for (TexteMaitre decret: decrets){
    		createLinkDecret(intituleMesure, getId(decret));
    	}
    }
    private void createLinkDecret(String intituleMesure, String intituleDecret) throws ClientException {
    	TexteMaitre mesure = getTexteMaitre(MESURE,intituleMesure);
    	Decret decret = getDecret(intituleDecret);
    	List<String> decretIds = mesure.getDecretIds();
    	decretIds.add(decret.getId());
    	mesure.setDecretIds(decretIds);
    	mesure.save(session);
	}

	public ActiviteNormative getActiviteNormative(String type,String intitule) throws ClientException{
    	return getActiviteNormative(type,intitule,ActiviteNormative.class);
    }
	
	public TexteMaitre getTexteMaitre(String type,String intitule) throws ClientException{
    	return getActiviteNormative(type,intitule,TexteMaitre.class);
    }

	public List<TexteMaitre> getTexteMaitres(String type,String intitule) throws ClientException{
    	return getActiviteNormatives(type,intitule,TexteMaitre.class);
    }
	
	public MesureApplicative getMesureApplicative(String intitule) throws ClientException{
    	return getActiviteNormative(MESURE,intitule,MesureApplicative.class);
    }
	
	public Decret getDecret(String intitule) throws ClientException{
    	return getActiviteNormative(DECRET,intitule,Decret.class);
    }
	
	private <E> E getActiviteNormative(String type,String intitule, Class<E> adapter) throws ClientException {
		DocumentModelList docs = getActiviteNormativeDocs(type, intitule);
    	return docs.get(0).getAdapter(adapter);
	}

	private DocumentModelList getActiviteNormativeDocs(String type,String intitule) throws ClientException {
		String query = "SELECT * FROM ActiviteNormative WHERE texm:observation = '" + type + "' AND ecm:name LIKE '" + intitule +"'";
		LOGGER.info("Get docs : [" + type +"," + intitule +"] " + query);
		DocumentModelList docs = session.query(query);
		return docs;
	}
	
	private <E> List<E> getActiviteNormatives(String type,String intitule, Class<E> adapter) throws ClientException {
		DocumentModelList docs = getActiviteNormativeDocs(type, intitule);
		return DocUtil.adapt(docs, adapter);
	}
    
	public ActiviteNormative getLoi() throws ClientException{
		return getActiviteNormative(LOI, "loi01");	
	}
	
    public int getActiviteNormativeCount() throws ClientException {
		return session.getChildren(getRootRef()).size();
	}

	public PathRef getRootRef() {
		return new PathRef("/case-management/activite-normative-root");
	}
}
