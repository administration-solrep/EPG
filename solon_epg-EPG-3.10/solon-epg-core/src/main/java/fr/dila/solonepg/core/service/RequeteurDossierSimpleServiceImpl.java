package fr.dila.solonepg.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.impl.DocumentModelImpl;
import org.nuxeo.ecm.core.search.api.client.querymodel.QueryModelService;
import org.nuxeo.runtime.api.Framework;

import fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants;
import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.solonepg.api.service.RequeteurDossierSimpleService;
import fr.dila.solonepg.core.recherche.query.EPGQueryModel;
import fr.dila.solonepg.core.util.RequeteurUtils;
import fr.dila.st.api.recherche.QueryAssembler;
import fr.dila.st.core.query.FlexibleQueryMaker;
import fr.dila.st.core.query.QueryUtils;
import fr.dila.st.core.recherche.STRechercheServiceImpl;
/**
 * 
 * @author jgomez
 * L'implementation du service requêteur de dossier simple 
 *
 */
@SuppressWarnings("deprecation")
public class RequeteurDossierSimpleServiceImpl extends STRechercheServiceImpl implements RequeteurDossierSimpleService {



    protected QueryModelService qmService;
    
    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(STRechercheServiceImpl.class);
	
	public RequeteurDossierSimpleServiceImpl(){
		super();
        qmService = (QueryModelService) Framework.getRuntime().getComponent(QueryModelService.NAME);
	}
	
    @Override
    public RequeteDossierSimple getRequete(CoreSession session) throws ClientException {
        DocumentModel requeteDoc = new DocumentModelImpl("/",UUID.randomUUID().toString(), SolonEpgRequeteDossierSimpleConstants.REQUETE_DOSSIER_SIMPLE_DOCUMENT_TYPE);
        return requeteDoc.getAdapter(RequeteDossierSimple.class);
    }
    
    
    @Override
    public DocumentModelList query(CoreSession session ,RequeteDossierSimple requete) throws ClientException {
        String pattern = getFullQuery(requete);
        
        if (!pattern.startsWith(FlexibleQueryMaker.KeyCode.UFXNQL.key)) {
            pattern = QueryUtils.ufnxqlToFnxqlQuery(pattern);
        }
                
        DocumentRef[] refs =  QueryUtils.doUFNXQLQueryForIds(session, pattern, null);
        return  session.getDocuments(refs);
    }
    
    @Override
    public String getFullQuery(RequeteDossierSimple requete,String... modelNames) throws ClientException {
        // Exécute les post-traitements de la requête
        requete.doBeforeQuery();
        
        // Génère la requête à partir du model et des données
      	QueryAssembler assembler = RequeteurUtils.getDossierAssembler();
        // Si on ne spécifie rien, on prend les requetes models enregistrés par défaut
        if (modelNames.length == 0){
            modelNames = this.getRequeteParts(requete);
        }
        EPGQueryModel queryModel = new EPGQueryModel();
        String clause = queryModel.getAndRequeteParts(qmService,requete.getDocument(),modelNames);
        assembler.setWhereClause(clause);        
        return assembler.getFullQuery();
    }
    

    /**
     * Retourne la liste des identifiants des requêtes model
     * @return
     */
    public String[] getRequeteParts(RequeteDossierSimple requete) {
        List<String> requeteModels = new ArrayList<String>();
        requeteModels.add(REQUETE_CRITERES_PRINCIPAUX);
        requeteModels.add(REQUETE_CRITERES_SECONDAIRES);
        requeteModels.add(REQUETE_CRITERES_ETAPES);
        requeteModels.add(REQUETE_TEXTE_INTEGRAL);        
        return requeteModels.toArray(new String[requeteModels.size()]);
    }
}