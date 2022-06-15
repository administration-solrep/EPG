package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants;
import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.solonepg.api.service.RequeteurDossierSimpleService;
import fr.dila.solonepg.core.recherche.query.EPGQueryModel;
import fr.dila.solonepg.core.util.RequeteurUtils;
import fr.dila.st.api.recherche.QueryAssembler;
import fr.dila.st.core.recherche.STRechercheServiceImpl;
import fr.sword.naiad.nuxeo.commons.core.util.ServiceUtil;
import fr.sword.naiad.nuxeo.ufnxql.core.query.FlexibleQueryMaker;
import fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.platform.query.api.PageProviderService;

/**
 *
 * @author jgomez
 * L'implementation du service requêteur de dossier simple
 *
 */
public class RequeteurDossierSimpleServiceImpl extends STRechercheServiceImpl implements RequeteurDossierSimpleService {

    public RequeteurDossierSimpleServiceImpl() {
        super();
    }

    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(STRechercheServiceImpl.class);

    @Override
    public RequeteDossierSimple getRequete(CoreSession session) {
        DocumentModel requeteDoc = session.createDocumentModel(
            "/",
            UUID.randomUUID().toString(),
            SolonEpgRequeteDossierSimpleConstants.REQUETE_DOSSIER_SIMPLE_DOCUMENT_TYPE
        );
        return requeteDoc.getAdapter(RequeteDossierSimple.class);
    }

    @Override
    public DocumentModelList query(CoreSession session, RequeteDossierSimple requete) {
        String pattern = getFullQuery(requete);

        if (!pattern.startsWith(FlexibleQueryMaker.KeyCode.UFXNQL.getKey())) {
            pattern = QueryUtils.ufnxqlToFnxqlQuery(pattern);
        }

        DocumentRef[] refs = QueryUtils.doUFNXQLQueryForIds(session, pattern, null);
        return session.getDocuments(refs);
    }

    @Override
    public String getFullQuery(RequeteDossierSimple requete, String... modelNames) {
        // Exécute les post-traitements de la requête
        requete.doBeforeQuery();

        // Génère la requête à partir du model et des données
        QueryAssembler assembler = RequeteurUtils.getDossierAssembler();
        // Si on ne spécifie rien, on prend les requetes models enregistrés par défaut
        if (modelNames.length == 0) {
            modelNames = this.getRequeteParts(requete);
        }
        EPGQueryModel queryModel = new EPGQueryModel();
        PageProviderService ppService = ServiceUtil.getRequiredService(PageProviderService.class);
        String clause = queryModel.getAndRequeteParts(ppService, requete.getDocument(), modelNames);
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
