package fr.dila.solonepg.core.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.element.office.OfficeTextElement;
import org.odftoolkit.odfdom.incubator.meta.OdfOfficeMeta;
import org.odftoolkit.odfdom.pkg.OdfPackage;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.SolonEpgParapheurConstants;
import fr.dila.solonepg.api.service.FeuilleStyleService;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.st.core.schema.DublincoreSchemaUtils;
import fr.dila.st.utils.SecureEntityResolver;

/**
 * Tests unitaires sur le service des feuilles de style.
 * 
 * @author arolin
 */
public class TestFeuilleStyleService extends SolonEpgRepositoryTestCase {

    private static final Log log = LogFactory.getLog(TestFeuilleStyleService.class);
    
    private FeuilleStyleService feuilleStyleService;
    
    @Override
    public void setUp() throws Exception {

        super.setUp();
        
        openSession();
        
        //import du Service de gestion des feuilles de styles 
        feuilleStyleService = SolonEpgServiceLocator.getFeuilleStyleService();
        assertNotNull(feuilleStyleService);
    }
    
    @Override
    public void tearDown() throws Exception {
    	closeSession();
    	super.tearDown();
    }

    /**
     * test simple de la création et la récupération d'un document openOffice.
     * 
     * @author antoine Rolin
     *
     */
    public void testGetOpenOfficeDocumentVariableAndMetaDataMethod() throws Exception {
        log.info("testCreateAndGetOpenOfficeDocumentMethod");
        
        //identifiant unique de la feuille de style stockée dans les propriétés du document openOffice 
        String identifiantFeuilleStyleAttendu = "m123456";
        //nom et valeur de la variable contenant le titre du document
        String titreVariableAttendu = "titreDocumentVariable";
        String titreValeurAttendu="ceciestuntitre variable";
        //nom et valeur de la variable contenant la date de signature
        String dateVariableAttendu = "dateSignature1";
        String dateValeurAttendu="12/31/2015";
        
        //identifiant unique de la feuille de style stockée dans les propriétés du document openOffice 
        String identifiantFeuilleStyle;
        //nom et valeur de la variable contenant le titre du document
        String titreValeur= null;
        //nom et valeur de la variable contenant la date de signature
        String dateValeur= null;
        
        //Récupération d'un fichier
        File docOpenOfficeFile = new File("target/test-classes/FeuilleStyleArreteMinistere.odt");
        
        // Récupération du fichier openOffice writer
        
        OdfPackage odfPackage = OdfPackage.loadPackage(docOpenOfficeFile);
        
        OdfTextDocument odfTextDoc = (OdfTextDocument) OdfTextDocument.loadDocument(odfPackage);

        //récupèration dans les metadonnées openoffice la propriété personnalisée contenant l'identifiant unqiue
        OdfOfficeMeta odfOfficeMeta = odfTextDoc.getOfficeMetadata();
        identifiantFeuilleStyle = odfOfficeMeta.getUserDefinedDataValue("IdentifiantUnique");
        
        //récupération du contenu texte du document openOffice
        OfficeTextElement  officeText = odfTextDoc.getContentRoot();
        
        // parsage du contenu texte du document openOffice
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        // création d'un constructeur de documents
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        docBuilder.setEntityResolver(new SecureEntityResolver());
        //definition de la source 
        ByteArrayInputStream bais = new ByteArrayInputStream(officeText.toString().getBytes());
        // lecture du contenu du fichier XML avec DOM
        Document document = docBuilder.parse(bais);
        
      //récupération des éléments
        NodeList listVariable = document.getElementsByTagName("text:variable-set");
        int listVariableLength = listVariable.getLength();
        if(listVariableLength>0){
            //on parcourt la liste des variables du document
           for(int i=0;i<listVariableLength;i++){
               Node variable = listVariable.item(i);
               NamedNodeMap  nameNodeMap = variable.getAttributes();
               Node name = nameNodeMap.getNamedItem("text:name");
               String tempVariableName = name.getNodeValue();
               if(titreVariableAttendu.equals(tempVariableName)){
                   titreValeur = variable.getFirstChild().getNodeValue();
               } else if (dateVariableAttendu.equals(tempVariableName)){
                   dateValeur = variable.getFirstChild().getNodeValue();
               }
           }
        }
        //sauvegarde du document
        odfTextDoc.save(docOpenOfficeFile);
        
        //on vérifie que l'on a bien récupéré les propriétés souhaitées
        assertEquals(identifiantFeuilleStyleAttendu,identifiantFeuilleStyle);
        assertEquals(titreValeurAttendu,titreValeur);
        assertEquals(dateValeurAttendu,dateValeur);
    }
    
    /**
     * test de la methode "checkFeuilleStyleValidAndTag" du service de feuille de route avec une feuille de style valide.
     * 
     * @throws Exception
     */
    public void testCheckFeuilleStyleValidAndTagWithValideFeuillestyle() throws Exception {
        log.info("testCheckFeuilleStyleValidAndTagWithValideFeuillestyle");
        //Récupération d'un fichier
        File docOpenOfficeFile = new File("target/test-classes/FeuilleStyleValideTest.odt");
        
        //on récupère l'identifiant unique de la feuille de style si il existe
        String oldUserDefinedDatavalue = getFeuilleStyleIdentifier(docOpenOfficeFile);
        
        //on vérifie que la feuille de style possède les bons attributs et on change l'identifiant unique de la feuille de style
        docOpenOfficeFile = feuilleStyleService.checkFeuilleStyleValidAndTag(docOpenOfficeFile, session,true);
        assertNotNull(docOpenOfficeFile);
        
        //on récupère l'identifiant unique de la feuille de style si il existe
        String newUserDefinedDatavalue = getFeuilleStyleIdentifier(docOpenOfficeFile);
        
        //on vérifie que l'identifiant n'est pas null et n'est pas égal à l'ancien indentifiant
        assertNotNull(newUserDefinedDatavalue);
        assertTrue(!newUserDefinedDatavalue.isEmpty());
        assertNotSame(oldUserDefinedDatavalue, newUserDefinedDatavalue);
    }
    
    /**
     * test de la methode "checkFeuilleStyleValidAndTag" du service de feuille de route avec une feuille de style non valide.
     * 
     * @throws Exception
     */
    public void testCheckFeuilleStyleValidAndTagWithNonValideFeuillestyle() throws Exception {
        log.info("testCheckFeuilleStyleValidAndTagWithNonValideFeuillestyle");
        //Récupération d'un fichier
        File docOpenOfficeFile = new File("target/test-classes/FeuilleStyleNonValide.odt");
        //on vérifie que le fichier n'est pas null
        assertNotNull(docOpenOfficeFile);
       
        //on vérifie que le fichier est null car la feuille de style est non valide
        docOpenOfficeFile = feuilleStyleService.checkFeuilleStyleValidAndTag(docOpenOfficeFile, session,true);
        assertNull(docOpenOfficeFile);
    }
    
    /**
     * test de la methode "updateDossierMetadataFromParapheurFile" du service de feuille de route avec une feuille de style valide.
     * 
     * @throws Exception
     */
    public void testUpdateDossierMetadataFromParapheurFileWithValideFeuillestyle() throws Exception {
        String nomFichier = "src/test/resources/FeuilleStyleValideTest.odt";
        String titreActeAttendu = "titre de l'acte à saisir ici !";
        Calendar calendarAttendu = new GregorianCalendar();
        calendarAttendu.set(Calendar.YEAR, 2011);
        calendarAttendu.set(Calendar.MONTH, Calendar.DECEMBER);
        calendarAttendu.set(Calendar.DAY_OF_MONTH, 12);
        log.info("test method UpdateDossierMetadataFromParapheurFile With Valide FeuilleStyle");
        //Récupération d'un fichier
        File docOpenOfficeFile = new File(nomFichier);
        //on vérifie que le fichier n'est pas null
        assertNotNull(docOpenOfficeFile);
       
        //création d'un dossier
        DocumentModel dossierDocModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");
        DocumentRef dossierRef = dossierDocModel.getRef();
        //création d'un parapheur
        DocumentModel parapheur = session.createDocumentModel(dossierDocModel.getPathAsString(), "Parapheur", SolonEpgParapheurConstants.PARAPHEUR_DOCUMENT_TYPE);
        parapheur = session.createDocument(parapheur);
        //création du répertoire extrait du parapheur : 
        DocumentModel parapheurFolder = session.createDocumentModel(parapheur.getPathAsString(), SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EXTRAIT_NAME, SolonEpgParapheurConstants.PARAPHEUR_FOLDER_DOCUMENT_TYPE);
        DublincoreSchemaUtils.setTitle(parapheurFolder, SolonEpgParapheurConstants.PARAPHEUR_FOLDER_EXTRAIT_NAME);
        parapheurFolder = session.createDocument(parapheurFolder);
        
        //mise à jour des données du dossier
        feuilleStyleService.updateDossierMetadataFromParapheurFile(session, new FileInputStream(docOpenOfficeFile), nomFichier, parapheurFolder, dossierDocModel);
        
        dossierDocModel = session.getDocument(dossierRef);
        Dossier dossier = dossierDocModel.getAdapter(Dossier.class);
        
      //TODO reparer
        //on vérifie que l'on a bien récupéré les valeurs souhaitées 
        assertEquals(titreActeAttendu, dossier.getTitreActe());
//        assertEquals(calendarAttendu.get(Calendar.YEAR), dossier.getDateSignature().get(Calendar.YEAR));
//        assertEquals(calendarAttendu.get(Calendar.DAY_OF_YEAR), dossier.getDateSignature().get(Calendar.DAY_OF_YEAR));
//        assertEquals(calendarAttendu.get(Calendar.MONTH), dossier.getDateSignature().get(Calendar.MONTH));
        
    }
    
    //getFeuilleStyleListFromTypeActe

    protected String getFeuilleStyleIdentifier(File feuilleStyleFile) {
        String identifiantUniqueFeuilleStyle = null;
        // Récupération du fichier openOffice writer
        OdfPackage odfPackage;
        try {
            odfPackage = OdfPackage.loadPackage(feuilleStyleFile);
            OdfTextDocument odfTextDoc = (OdfTextDocument) OdfTextDocument.loadDocument(odfPackage);
            //récupèration dans les metadonnées openoffice de la propriété personnalisée contenant l'identifiant unique
            OdfOfficeMeta odfOfficeMeta = odfTextDoc.getOfficeMetadata();
            identifiantUniqueFeuilleStyle = odfOfficeMeta.getUserDefinedDataValue("IdentifiantUnique");
        } catch (Exception e) {
            log.error("erreur lors de la récupération de l'identifiant unique",e);
        }
        return identifiantUniqueFeuilleStyle;
    }

}