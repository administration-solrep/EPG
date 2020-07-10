// Problème de dépendance avec des sources provenant des tests solon, a voir à tête reposée

//package fr.dila.solonepg.web.recherche;
//
//import java.io.Serializable;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.el.ELException;
//import javax.faces.context.FacesContext;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//import fr.dila.cm.test.CaseManagementRepositoryTestCase;
//import fr.dila.cm.test.CaseManagementTestConstants;
//import org.nuxeo.ecm.core.api.ClientException;
//import org.nuxeo.ecm.core.api.DocumentModel;
//import org.nuxeo.ecm.platform.contentview.jsf.ContentView;
//import org.nuxeo.ecm.platform.contentview.jsf.ContentViewService;
//import org.nuxeo.ecm.platform.ui.web.jsf.MockFacesContext;
//import org.nuxeo.runtime.api.Framework;
//
//import fr.dila.solonepg.api.cases.Dossier;
//import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
//import fr.dila.solonepg.api.service.RequeteurDossierSimpleService;
//import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
//import fr.dila.st.web.contentview.PaginatedPageDocumentProvider;
//
///**
// * 
// * @author jgomez
// * 
// */
//public class TestContentView extends CaseManagementRepositoryTestCase {
//
//    public static Log log = LogFactory.getLog(TestContentView.class);
//
//    protected ContentViewService cvs;
//    
//    protected RequeteurDossierSimpleService rds;
//    
//    protected Dossier dossier1;
//    
//    protected ContentView requeteContentView;
//    
//    private MockFacesContext facesContext;
//    
//    private DocumentModel searchDocument;
//
//    
//    @Override
//    public void setUp() throws Exception {
//        super.setUp();
//
//        deployBundle(CaseManagementTestConstants.CASE_MANAGEMENT_API_BUNDLE);
//        deployBundle(CaseManagementTestConstants.CASE_MANAGEMENT_CORE_BUNDLE);
//        deployBundle("org.nuxeo.ecm.platform.routing.core");
//        deployBundle("org.nuxeo.ecm.platform.ui");
//
//        deployBundle("org.nuxeo.ecm.platform.contentview.jsf");
//
//        deployContrib("org.nuxeo.ecm.platform.query.api", "OSGI-INF/pageprovider-framework.xml");
//        deployContrib("org.nuxeo.ecm.platform.contentview.jsf", "OSGI-INF/contentview-framework.xml");
//
//        deployContrib("fr.dila.st.core", "OSGI-INF/st-schema-contrib.xml");
//        deployContrib("fr.dila.st.core", "OSGI-INF/st-adapter-contrib.xml");
//        deployContrib("fr.dila.st.core", "OSGI-INF/st-core-type-contrib.xml");
//        deployContrib("fr.dila.st.core", "OSGI-INF/service/st-jointure-framework.xml");
//        deployBundle("fr.dila.st.web");
//
//        deployContrib("fr.dila.st.core", "OSGI-INF/service/vocabulary-framework.xml");
//        deployContrib("fr.dila.st.core", "OSGI-INF/querymaker-contrib.xml");
//
//        deployBundle("fr.dila.st.api");
//        deployBundle("fr.dila.ss.api");
//        deployBundle("fr.dila.ss.core");
//        deployBundle("fr.dila.solonepg.core");
//        deployBundle("fr.dila.solonepg.web");
//        deployContrib("fr.dila.solonepg.web.vocabulary.tests", "OSGI-INF/test-requete-contentviews-contrib.xml");
//        openSession();
//        
//        searchDocument = session.createDocumentModel("File");
//        final DocumentModel rootDoc = session.getRootDocument();
//     // set mock faces context for needed properties resolution
//        facesContext = new MockFacesContext() {
//            @SuppressWarnings("rawtypes")
//            @Override
//            public Object evaluateExpressionGet(FacesContext context,
//                    String expression, Class expectedType) throws ELException {
//                if ("#{documentManager}".equals(expression)) {
//                    return session;
//                }
//                if ("#{searchDocument}".equals(expression)) {
//                    return searchDocument;
//                }
//                if ("#{currentDocument.id}".equals(expression)) {
//                    return rootDoc.getId();
//                } else {
//                    log.error("Cannot evaluate expression: " + expression);
//                }
//                return null;
//            }
//        };
//        facesContext.setCurrent();
//        assertNotNull(FacesContext.getCurrentInstance());
//        
//        cvs = Framework.getService(ContentViewService.class);
//        rds = SolonEpgServiceLocator.getRequeteurDossierSimpleService();
//        dossier1 =  createDossier1();
//        setUpContentView();
//    }
//    
//    private Dossier createDossier1() throws ClientException {
//        Dossier dossier = createDossier();
//        dossier.setNumeroNor("ECOX9800017A");
//        dossier.setMinistereResp("1");
//        dossier.setDirectionResp("2");
//        dossier.setStatut("4");
//        dossier.setIdCreateurDossier("JPAUL");
//        session.saveDocument(dossier.getDocument());
//        session.save();
//        return dossier;
//    } 
//    
//    
//    public void setUpContentView() throws Exception{
//        assertNotNull(cvs);
//        assertNotSame(0,cvs.getContentViewNames().size());
//        requeteContentView = cvs.getContentView("requeteur_dossier_simple", session);
//        assertNotNull(requeteContentView);
//    }    
//    
//    /** Tests **/
//    public void testGetService() {
//        assertNotNull(cvs);
//    }
//    
//    public void testBasicSetup() throws ClientException {
//        assertNotNull(requeteContentView);
//        assertNotNull(requeteContentView.getPageProvider());
//    }
//    
//    public void testEmptyRequete() throws Exception{
//        RequeteDossierSimple requete = rds.getRequete(session);
//        requete.init();
//        requete.doBeforeQuery();
//        updateProvider(requete);
//        PaginatedPageDocumentProvider provider = (PaginatedPageDocumentProvider) requeteContentView.getCurrentPageProvider();
//        List<DocumentModel> docList = provider.getCurrentPage();
//        assertNotNull(docList);
//        assertEquals(1,docList.size());
//    }
//    
//    public void testRequete1() throws Exception{
//        RequeteDossierSimple requete = rds.getRequete(session);
//        requete.init();
//        requete.doBeforeQuery();
//        requete.setNumeroNor("E*");
//        updateProvider(requete);
//        PaginatedPageDocumentProvider provider = (PaginatedPageDocumentProvider) requeteContentView.getCurrentPageProvider();
//        List<DocumentModel> docList = provider.getCurrentPage();
//        assertNotNull(docList);
//        assertEquals(1,docList.size());
//    }
//    
//    public void testRequete2() throws Exception{
//        RequeteDossierSimple requete = rds.getRequete(session);
//        requete.init();
//        requete.doBeforeQuery();
//        requete.setNumeroNor("C*");
//        updateProvider(requete);
//        PaginatedPageDocumentProvider provider = (PaginatedPageDocumentProvider) requeteContentView.getCurrentPageProvider();
//        List<DocumentModel> docList = provider.getCurrentPage();
//        assertNotNull(docList);
//        assertEquals(0,docList.size());
//    }
//    
//    private PaginatedPageDocumentProvider updateProvider(RequeteDossierSimple requete) throws ClientException {
//        PaginatedPageDocumentProvider provider = (PaginatedPageDocumentProvider) requeteContentView.getPageProvider();
//        Map<String, Serializable> props = new HashMap<String, Serializable>();
//        props.put(PaginatedPageDocumentProvider.CORE_SESSION_PROPERTY, (Serializable) session);
//        provider.setProperties(props);
//        Object[] params = new Object[1];
//        params[0] =  rds.getFullQuery(requete);
//        log.error("La requête " + params[0]);
//        provider.setParameters(params);
//        return provider;
//    }
//    
//    
//}