package fr.dila.solonepg.core.cases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.model.PropertyException;

import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication;
import fr.dila.solonepg.api.cases.typescomplexe.DonneesSignataire;
import fr.dila.solonepg.api.cases.typescomplexe.InfoEpreuve;
import fr.dila.solonepg.api.cases.typescomplexe.InfoHistoriqueAmpliation;
import fr.dila.solonepg.api.cases.typescomplexe.InfoNumeroListe;
import fr.dila.solonepg.api.cases.typescomplexe.ParutionBo;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.api.constant.SolonEpgTraitementPapierConstants;
import fr.dila.solonepg.core.SolonEpgRepositoryTestCase;
import fr.dila.solonepg.core.cases.typescomplexe.DestinataireCommunicationImpl;
import fr.dila.solonepg.core.cases.typescomplexe.DonneesSignataireImpl;
import fr.dila.solonepg.core.cases.typescomplexe.InfoEpreuveImpl;
import fr.dila.solonepg.core.cases.typescomplexe.InfoHistoriqueAmpliationImpl;
import fr.dila.solonepg.core.cases.typescomplexe.InfoNumeroListeImpl;
import fr.dila.solonepg.core.cases.typescomplexe.ParutionBoImpl;
import fr.dila.st.api.domain.ComplexeType;
import fr.dila.st.core.domain.ComplexeTypeImpl;

/**
 * Tests unitaires sur le dossier (metadonnées).
 * 
 * @author arolin
 */
public class TestDossier extends SolonEpgRepositoryTestCase {

    private static final Log log = LogFactory.getLog(TestDossier.class);

    protected DocumentRef dossierRef;

    @Override
    public void setUp() throws Exception {

        super.setUp();

        openSession();
        DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");

        dossierRef = docModel.getRef();
        Dossier dossier = retrieveDossier(dossierRef);
        assertNotNull(dossier);
    }
    
    @Override
    public void tearDown() throws Exception {
    	closeSession();
    	super.tearDown();
    }

    private Dossier retrieveDossier() throws ClientException {
        return retrieveDossier(dossierRef);
    }

    private Dossier retrieveDossier(DocumentRef ref) throws ClientException {
        DocumentModel docModel = session.getDocument(ref);
        Dossier dossier = docModel.getAdapter(Dossier.class);
        assertNotNull(dossier);
        return dossier;
    }

    public void testGetDocument() throws PropertyException, ClientException {

        // on verifie que le document est de type dossier, possède
        // possède le schéma "dossier"
        log.info("begin : test dossier type ");

        Dossier dossier = retrieveDossier();
        DocumentModel dossierModel = dossier.getDocument();
        assertNotNull(dossierModel);
        assertEquals(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, dossierModel.getType());
        String[] schemas = dossierModel.getSchemas();
        boolean foundDossierSchema = false;
        for (String schema : schemas) {
            if (schema.equals(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
                foundDossierSchema = true;
            }
        }
        assertTrue(foundDossierSchema);
    }

    public void testDiversAndCeDossierProperties() throws ClientException {

        // init metadata variable

        // get the diverse info property

        String categorieActe = "Reglementaire";
        String baseLegaleActe = "??";
        Calendar datePublication = GregorianCalendar.getInstance();
        datePublication.set(2009, 5, 14);

        Boolean publicationRapportPresentation = false;

        String sectionCe = "sectionCE";
        String rapporteurCe = "sectionCE";
        Calendar dateTransmissionSectionCe = GregorianCalendar.getInstance();
        dateTransmissionSectionCe.set(2009, 5, 14);
        Calendar dateSectionCe = GregorianCalendar.getInstance();
        dateSectionCe.set(2009, 6, 14);
        Calendar dateAgCe = GregorianCalendar.getInstance();
        dateAgCe.set(2009, 7, 14);

        String numeroISA = "sectionCE";
        //
        Dossier dossier = retrieveDossier();
        // get the diverse info property
        dossier.setCategorieActe(categorieActe);
        dossier.setBaseLegaleActe(baseLegaleActe);
        dossier.setDatePublication(datePublication);

        // get the Conseil Etat (CE) property
        dossier.setPublicationRapportPresentation(publicationRapportPresentation);

        ConseilEtat conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);
        conseilEtat.setSectionCe(sectionCe);
        conseilEtat.setRapporteurCe(rapporteurCe);
        conseilEtat.setDateTransmissionSectionCe(dateTransmissionSectionCe);
        conseilEtat.setDateSectionCe(dateSectionCe);
        conseilEtat.setDateAgCe(dateAgCe);
        conseilEtat.setNumeroISA(numeroISA);

        conseilEtat.save(session);
        session.save();

        closeSession();
        openSession();

        dossier = retrieveDossier();
        // check properties persistance
        Object test = dossier.getDocument().getPropertyValue("dos:titreActe");
        test = dossier.getDocument().getPropertyValue("consetat:dateSectionCe");
        test = dossier.getDocument().getPropertyValue("dos:rubriques");
        test = dossier.getDocument().getPropertyValue("dos:rubriques");
        if (test instanceof String && ((String) test).isEmpty()) {
            test = null;
        }
        test = "";
        if (test instanceof String && ((String) test).isEmpty()) {
            test = null;
        }
        test = dossier.getDocument().getPropertyValue("dos:transpositionOrdonnance");

        conseilEtat = dossier.getDocument().getAdapter(ConseilEtat.class);

        // assertEquals(dossier.getOriginedossier(), originedossier);
        assertEquals(dossier.getCategorieActe(), categorieActe);
        assertEquals(dossier.getBaseLegaleActe(), baseLegaleActe);
        assertEquals(dossier.getDatePublication(), datePublication);
        assertEquals(dossier.getPublicationRapportPresentation(), publicationRapportPresentation);
        assertEquals(conseilEtat.getSectionCe(), sectionCe);
        assertEquals(conseilEtat.getRapporteurCe(), rapporteurCe);
        assertEquals(conseilEtat.getDateTransmissionSectionCe(), dateTransmissionSectionCe);
        assertEquals(conseilEtat.getDateSectionCe(), dateSectionCe);
        assertEquals(conseilEtat.getDateAgCe(), dateAgCe);
        assertEquals(conseilEtat.getNumeroISA(), numeroISA);
    }

    public void testValidationEtPublicationDossierProperties() throws ClientException {

        // init metadata variable

        List<String> chargeMissionIds = new ArrayList<String>();
        chargeMissionIds.add("test1");
        chargeMissionIds.add("test2");

        List<String> conseillerPmIds = new ArrayList<String>();
        conseillerPmIds.add("conseillerPmIds1");
        conseillerPmIds.add("conseillerPmIds2");

        Calendar dateSignature = GregorianCalendar.getInstance();
        dateSignature.set(2009, 5, 14);
        Calendar pourFournitureEpreuve = GregorianCalendar.getInstance();
        pourFournitureEpreuve.set(2009, 8, 14);

        List<String> vecteurPublication = new ArrayList<String>();
        vecteurPublication.add("Journal Officiel");

        List<String> publicationsConjointes = new ArrayList<String>();
        publicationsConjointes.add("ACOR000006Y");
        String publicationIntegraleOuExtrait = "extrait";
        Boolean decretNumerote = false;

        String modeParution = "modeParution";
        String delaiPublication = "A date précisée";
        Calendar datePreciseePublication = GregorianCalendar.getInstance();
        datePreciseePublication.set(2009, 6, 14);

        Dossier dossier = retrieveDossier();
        // get the validation et signature property
        dossier.setNomCompletChargesMissions(chargeMissionIds);
        dossier.setNomCompletConseillersPms(conseillerPmIds);
        dossier.setDateSignature(dateSignature);

        // get the publication property
        dossier.setDatePourFournitureEpreuve(pourFournitureEpreuve);
        dossier.setVecteurPublication(vecteurPublication);
        dossier.setPublicationsConjointes(publicationsConjointes);
        dossier.setPublicationIntegraleOuExtrait(publicationIntegraleOuExtrait);
        dossier.setDecretNumerote(decretNumerote);
        RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
        retourDila.setModeParution(modeParution);
        dossier.setDelaiPublication(delaiPublication);
        dossier.setDatePreciseePublication(datePreciseePublication);

        dossier.save(session);
        session.save();
        closeSession();
        openSession();

        dossier = retrieveDossier();
        // check properties persistance
        retourDila = dossier.getDocument().getAdapter(RetourDila.class);

        assertEquals(dossier.getNomCompletChargesMissions(), chargeMissionIds);
        assertEquals(dossier.getNomCompletConseillersPms(), conseillerPmIds);
        assertEquals(dossier.getDateSignature(), dateSignature);
        assertEquals(dossier.getDatePourFournitureEpreuve(), pourFournitureEpreuve);
        assertEquals(dossier.getVecteurPublication(), vecteurPublication);
        assertEquals(dossier.getPublicationsConjointes(), publicationsConjointes);
        assertEquals(dossier.getPublicationIntegraleOuExtrait(), publicationIntegraleOuExtrait);
        assertEquals(dossier.getDecretNumerote(), decretNumerote);
        assertEquals(retourDila.getModeParution(), modeParution);
        assertEquals(dossier.getDelaiPublication(), delaiPublication);
        assertEquals(dossier.getDatePreciseePublication(), datePreciseePublication);

    }

    public void testGeneralesAndResponsablesDossierProperties() throws ClientException {

        // init metadata variable
        // get the generales dossier property
        String numeroNor = "ECEI1013141D";
        String typeActe = "Exequatur";
        String titreActe = "titreActe";

        // get the responsables dossier property
        String ministereResp = "Justice";
        String directionResp = "magistrature";
        String ministereAttache = "Travail";
        String directionAttache = "Industrie";

        String nomRespDossier = "Michel";
        String prenomRespDossier = "Edouard";
        String nomCompletRespDossier = "Michel Edouard";
        String qualiteRespDossier = "M";
        String telRespDossier = "0404040404";
        String mailRespDossier = "michel.edouard@test.gouv";
        String idCreateurDossier = "123456";

        Dossier dossier = retrieveDossier();
        // set the generales dossier property
        dossier.setNumeroNor(numeroNor);
        dossier.setTypeActe(typeActe);
        dossier.setTitreActe(titreActe);

        // get the responsables dossier property
        dossier.setMinistereResp(ministereResp);
        dossier.setDirectionResp(directionResp);
        dossier.setMinistereAttache(ministereAttache);
        dossier.setDirectionAttache(directionAttache);
        dossier.setNomRespDossier(nomRespDossier);
        dossier.setPrenomRespDossier(prenomRespDossier);
        dossier.setQualiteRespDossier(qualiteRespDossier);
        dossier.setTelephoneRespDossier(telRespDossier);
        dossier.setMailRespDossier(mailRespDossier);
        dossier.setNomCompletRespDossier(nomCompletRespDossier);
        dossier.setIdCreateurDossier(idCreateurDossier);

        dossier.save(session);
        session.save();
        closeSession();
        openSession();
        dossier = retrieveDossier();
        // check properties persistance

        assertEquals(dossier.getNumeroNor(), numeroNor);
        assertEquals(dossier.getTypeActe(), typeActe);
        assertEquals(dossier.getTitreActe(), titreActe);

        assertEquals(dossier.getMinistereResp(), ministereResp);
        assertEquals(dossier.getDirectionResp(), directionResp);
        assertEquals(dossier.getMinistereAttache(), ministereAttache);
        assertEquals(dossier.getDirectionAttache(), directionAttache);
        assertEquals(dossier.getNomRespDossier(), nomRespDossier);
        assertEquals(dossier.getPrenomRespDossier(), prenomRespDossier);
        assertEquals(dossier.getQualiteRespDossier(), qualiteRespDossier);
        assertEquals(dossier.getTelephoneRespDossier(), telRespDossier);
        assertEquals(dossier.getMailRespDossier(), mailRespDossier);
        assertEquals(dossier.getNomCompletRespDossier(), nomCompletRespDossier);
        assertEquals(dossier.getIdCreateurDossier(), idCreateurDossier);

    }

    /**
     * Test de la méthode getDossierMetadata
     * 
     * @throws Exception
     */
    public void testGetDossierMetadataMethod() throws Exception {

        // init metadata variable
        // get the generales dossier property
        String numeroNor = "ECEI1013141D";
        String typeActe = "Exequatur";
        String titreActe = "titreActe";

        // get the responsables dossier property
        String ministereResp = "Justice";
        String directionResp = "magistrature";
        String ministereAttache = "Travail";
        String directionAttache = "Industrie";

        String nomRespDossier = "Michel";
        String prenomRespDossier = "Edouard";
        String nomCompletRespDossier = "Michel Edouard";
        String qualiteRespDossier = "M";
        String telRespDossier = "0404040404";
        String mailRespDossier = "michel.edouard@test.gouv";
        String idCreateurDossier = "123456";

        Dossier dossier = retrieveDossier();
        // set the generales dossier property
        dossier.setNumeroNor(numeroNor);
        dossier.setTypeActe(typeActe);
        dossier.setTitreActe(titreActe);

        // get the responsables dossier property
        dossier.setMinistereResp(ministereResp);
        dossier.setDirectionResp(directionResp);
        dossier.setMinistereAttache(ministereAttache);
        dossier.setDirectionAttache(directionAttache);
        dossier.setNomRespDossier(nomRespDossier);
        dossier.setPrenomRespDossier(prenomRespDossier);
        dossier.setQualiteRespDossier(qualiteRespDossier);
        dossier.setTelephoneRespDossier(telRespDossier);
        dossier.setMailRespDossier(mailRespDossier);
        dossier.setNomCompletRespDossier(nomCompletRespDossier);
        dossier.setIdCreateurDossier(idCreateurDossier);

        dossier.save(session);
        session.save();
        closeSession();
        openSession();
        dossier = retrieveDossier();

        // Créé un nouveau dossier et copie des données de l'ancien dossier
        DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossier");
        Dossier newDossier = docModel.getAdapter(Dossier.class);
        newDossier.getDossierMetadata(dossier);

        // check properties persistance

        assertEquals(newDossier.getNumeroNor(), numeroNor);
        assertEquals(newDossier.getTypeActe(), typeActe);
        assertEquals(newDossier.getTitreActe(), titreActe);

        assertEquals(newDossier.getMinistereResp(), ministereResp);
        assertEquals(newDossier.getDirectionResp(), directionResp);
        assertEquals(newDossier.getMinistereAttache(), ministereAttache);
        assertEquals(newDossier.getDirectionAttache(), directionAttache);
        assertEquals(newDossier.getNomRespDossier(), nomRespDossier);
        assertEquals(newDossier.getPrenomRespDossier(), prenomRespDossier);
        assertEquals(newDossier.getQualiteRespDossier(), qualiteRespDossier);
        assertEquals(newDossier.getTelephoneRespDossier(), telRespDossier);
        assertEquals(newDossier.getMailRespDossier(), mailRespDossier);
        assertEquals(newDossier.getNomCompletRespDossier(), nomCompletRespDossier);
        assertEquals(newDossier.getIdCreateurDossier(), idCreateurDossier);

    }

    public void testParutionEtIndexationDossierProperties() throws ClientException {

        // init metadata variable

        List<String> rubrique = new ArrayList<String>();
        rubrique.add("rubrique");
        rubrique.add("rubrique2");

        List<String> motsCles = new ArrayList<String>();
        motsCles.add("motsCles");
        motsCles.add("motsCles2");

        List<String> champLibre = new ArrayList<String>();
        champLibre.add("champLibre");
        champLibre.add("champLibre2");

        Calendar dateParutionJorf = GregorianCalendar.getInstance();
        dateParutionJorf.set(2009, 5, 14);

        Calendar dateParutionBo = GregorianCalendar.getInstance();
        dateParutionBo.set(2009, 6, 14);

        String numeroTexteParutionJorf = "2001-456";
        Long pageParutionJorf = 5L;

        String numeroTexteParutionBo = "2001-457";

        ParutionBo parutionBo = new ParutionBoImpl();
        Map<String, Serializable> serializableMap = new HashMap<String, Serializable>();
        serializableMap.put(RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_BO_PROPERTY, dateParutionBo);
        serializableMap.put(RetourDilaConstants.RETOUR_DILA_NUMERO_TEXTE_PARUTION_BO_PROPERTY, numeroTexteParutionBo);
        serializableMap.put(RetourDilaConstants.RETOUR_DILA_PAGE_BO_PROPERTY, pageParutionJorf);
        parutionBo.setSerializableMap(serializableMap);
        List<ParutionBo> parutionBoList = new ArrayList<ParutionBo>();
        parutionBoList.add(parutionBo);
        String zoneComDila = "commentaire";

        Dossier dossier = retrieveDossier();
        RetourDila retourDila = dossier.getDocument().getAdapter(RetourDila.class);
        // get the parution property
        retourDila.setDateParutionJorf(dateParutionJorf);
        retourDila.setNumeroTexteParutionJorf(numeroTexteParutionJorf);
        retourDila.setPageParutionJorf(pageParutionJorf);
        retourDila.setParutionBo(parutionBoList);
        dossier.setZoneComSggDila(zoneComDila);

        // get the indexation property
        dossier.setIndexationRubrique(rubrique);
        dossier.setIndexationMotsCles(motsCles);
        dossier.setIndexationChampLibre(champLibre);

        dossier.save(session);
        session.save();
        closeSession();
        openSession();

        dossier = retrieveDossier();

        // check properties persistance
        retourDila = dossier.getDocument().getAdapter(RetourDila.class);
        assertEquals(retourDila.getDateParutionJorf(), dateParutionJorf);
        assertEquals(retourDila.getNumeroTexteParutionJorf(), numeroTexteParutionJorf);
        assertEquals(retourDila.getPageParutionJorf(), pageParutionJorf);
        parutionBoList = retourDila.getParutionBo();
        assertNotNull(parutionBoList);
        assertEquals(parutionBoList.size(), 1);
        assertEquals(parutionBoList.get(0).getNumeroTexteParutionBo(), numeroTexteParutionBo);
        assertEquals(parutionBoList.get(0).getDateParutionBo(), dateParutionBo);
        assertEquals(parutionBoList.get(0).getPageParutionBo(), pageParutionJorf);
        assertEquals(dossier.getZoneComSggDila(), zoneComDila);
        assertEquals(dossier.getIndexationRubrique(), rubrique);
        assertEquals(dossier.getIndexationMotsCles(), motsCles);
        assertEquals(dossier.getIndexationChampLibre(), champLibre);
    }

    public void testTransposition() throws ClientException {

        log.info("------------TEST Transposition--------------- ");

        // check properties
        String ref = "ref";
        String titre = "titre";
        String numeroArticles = "numeroArticles";
        String refMesure = "refMesure";
        String commentaire = "commentaire";

        ComplexeType transposition = new ComplexeTypeImpl();
        Map<String, Serializable> serializableMap = new HashMap<String, Serializable>();
        serializableMap.put(ref, "ref");
        serializableMap.put(titre, "titre");
        serializableMap.put(numeroArticles, "num article");
        serializableMap.put(refMesure, "ref mesure");
        serializableMap.put(commentaire, "commentaire");
        transposition.setSerializableMap(serializableMap);

        assertNotNull(transposition.getSerializableMap());

        List<ComplexeType> list = new ArrayList<ComplexeType>();
        list.add(transposition);

        Dossier dossier = retrieveDossier();
        dossier.setApplicationLoi(list);

        dossier.save(session);
        session.save();

        closeSession();
        openSession();
        dossier = retrieveDossier();

        // check properties persistance
        list = dossier.getApplicationLoi();
        assertNotNull(list);
        assertEquals(1, list.size());

        transposition = list.get(0);
        assertNotNull(transposition);

        log.info("------------FIN TEST Transposition -------------");
    }

    public void testDestinataireCommunication() throws ClientException {

        log.info("------------TEST Destinataire Communication --------------- ");

        // check properties
        String texte1 = "texte1";
        String texte2 = "texte2";
        String texte3 = "texte3";
        Calendar date1 = GregorianCalendar.getInstance();
        date1.set(2009, 6, 14);
        Calendar date2 = GregorianCalendar.getInstance();
        date2.set(2010, 7, 15);
        Calendar date3 = GregorianCalendar.getInstance();
        date3.set(2011, 8, 16);

        DestinataireCommunication destinataireCommunication = new DestinataireCommunicationImpl();
        destinataireCommunication.setNomDestinataireCommunication(texte1);
        destinataireCommunication.setObjet(texte2);
        destinataireCommunication.setSensAvis(texte3);
        destinataireCommunication.setDateEnvoi(date1);
        destinataireCommunication.setDateRelance(date2);
        destinataireCommunication.setDateRetour(date3);

        assertEquals(texte1, destinataireCommunication.getNomDestinataireCommunication());
        assertEquals(texte2, destinataireCommunication.getObjet());
        assertEquals(texte3, destinataireCommunication.getSensAvis());
        assertEquals(date1, destinataireCommunication.getDateEnvoi());
        assertEquals(date2, destinataireCommunication.getDateRelance());
        assertEquals(date3, destinataireCommunication.getDateRetour());

        List<DestinataireCommunication> destinataireCommunicationListe = new ArrayList<DestinataireCommunication>();
        destinataireCommunicationListe.add(destinataireCommunication);
        destinataireCommunicationListe.add(destinataireCommunication);

        Dossier dossier = retrieveDossier();
        TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        traitementPapier.setChargeMissionCommunication(destinataireCommunicationListe);

        dossier.save(session);
        session.save();

        closeSession();
        openSession();
        dossier = retrieveDossier();

        // check properties persistance
        traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        destinataireCommunicationListe = traitementPapier.getChargeMissionCommunication();
        assertNotNull(destinataireCommunicationListe);
        assertEquals(2, destinataireCommunicationListe.size());
        destinataireCommunication = destinataireCommunicationListe.get(0);
        assertEquals(texte1, destinataireCommunication.getNomDestinataireCommunication());
        assertEquals(texte2, destinataireCommunication.getObjet());
        assertEquals(texte3, destinataireCommunication.getSensAvis());
        assertEquals(date1, destinataireCommunication.getDateEnvoi());
        assertEquals(date2, destinataireCommunication.getDateRelance());
        assertEquals(date3, destinataireCommunication.getDateRetour());
        destinataireCommunication = destinataireCommunicationListe.get(1);
        assertEquals(texte1, destinataireCommunication.getNomDestinataireCommunication());
        assertEquals(texte2, destinataireCommunication.getObjet());
        assertEquals(texte3, destinataireCommunication.getSensAvis());
        assertEquals(date1, destinataireCommunication.getDateEnvoi());
        assertEquals(date2, destinataireCommunication.getDateRelance());
        assertEquals(date3, destinataireCommunication.getDateRetour());

        log.info("------------FIN TEST Destinataire Communication -------------");
    }

    public void testDonneesSignataire() throws ClientException {

        log.info("------------TEST Donnees Signataire --------------- ");

        // check properties
        String texte1 = "texte1";
        Calendar date1 = GregorianCalendar.getInstance();
        date1.set(2009, 6, 14);
        Calendar date2 = GregorianCalendar.getInstance();
        date2.set(2009, 6, 14);
        Calendar date3 = GregorianCalendar.getInstance();
        date3.set(2009, 6, 14);

        DonneesSignataire donneesSignataire = new DonneesSignataireImpl();
        donneesSignataire.setDateEnvoiSignature(date1);
        donneesSignataire.setDateRetourSignature(date2);
        donneesSignataire.setCommentaireSignature(texte1);

        assertNotNull(donneesSignataire.getDateEnvoiSignature());
        assertNotNull(donneesSignataire.getDateRetourSignature());
        assertNotNull(donneesSignataire.getCommentaireSignature());

        Dossier dossier = retrieveDossier();
        TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        traitementPapier.setSignaturePm(donneesSignataire);
        dossier.save(session);

        session.save();
        closeSession();
        openSession();
        dossier = retrieveDossier();
        traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        // check properties persistance
        donneesSignataire = traitementPapier.getSignaturePm();
        assertNotNull(donneesSignataire);
        assertEquals(date1, donneesSignataire.getDateEnvoiSignature());
        assertEquals(date2, donneesSignataire.getDateRetourSignature());
        assertEquals(texte1, donneesSignataire.getCommentaireSignature());

        log.info("------------FIN TEST Donnees Signataire -------------");
    }

    public void testInfoEpreuve() throws ClientException {

        log.info("------------TEST Info Epreuve --------------- ");

        // check properties
        String texte1 = "texte1";
        String texte2 = "texte2";
        Calendar date1 = GregorianCalendar.getInstance();
        date1.set(2009, 6, 14);
        Calendar date2 = GregorianCalendar.getInstance();
        date2.set(2009, 6, 14);
        Calendar date3 = GregorianCalendar.getInstance();
        date3.set(2009, 6, 14);

        InfoEpreuve infoEpreuve = new InfoEpreuveImpl();
        infoEpreuve.setEpreuveDemandeLe(date1);
        infoEpreuve.setEpreuvePourLe(date2);
        infoEpreuve.setNumeroListe(texte1);
        infoEpreuve.setEnvoiRelectureLe(date3);
        infoEpreuve.setDestinataireEntete(texte2);

        assertNotNull(infoEpreuve.getEpreuveDemandeLe());
        assertNotNull(infoEpreuve.getEpreuvePourLe());
        assertNotNull(infoEpreuve.getNumeroListe());
        assertNotNull(infoEpreuve.getEnvoiRelectureLe());
        assertNotNull(infoEpreuve.getDestinataireEntete());

        Dossier dossier = retrieveDossier();
        TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        traitementPapier.setEpreuve(infoEpreuve);
        dossier.save(session);

        session.save();
        closeSession();
        openSession();
        dossier = retrieveDossier();

        // check properties persistance
        traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        infoEpreuve = traitementPapier.getEpreuve();
        assertEquals(date1, infoEpreuve.getEpreuveDemandeLe());
        assertEquals(date2, infoEpreuve.getEpreuvePourLe());
        assertEquals(texte1, infoEpreuve.getNumeroListe());
        assertEquals(date3, infoEpreuve.getEnvoiRelectureLe());
        assertEquals(texte2, infoEpreuve.getDestinataireEntete());

        log.info("------------FIN TEST Info Epreuve -------------");
    }

    public void testInfoNumeroListe() throws ClientException {

        log.info("------------TEST Info Numero Liste --------------- ");

        // check properties
        String texte1 = "texte1";
        Calendar date1 = GregorianCalendar.getInstance();
        date1.set(2009, 6, 14);

        InfoNumeroListe infoNumeroListe = new InfoNumeroListeImpl();
        infoNumeroListe.setDateGenerationListe(date1);
        infoNumeroListe.setNumeroListe(texte1);

        assertNotNull(infoNumeroListe.getDateGenerationListe());
        assertNotNull(infoNumeroListe.getNumeroListe());

        List<InfoNumeroListe> infoNumeroListeSignature = new ArrayList<InfoNumeroListe>();
        infoNumeroListeSignature.add(infoNumeroListe);
        infoNumeroListeSignature.add(infoNumeroListe);

        Dossier dossier = retrieveDossier();
        TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        traitementPapier.setNumerosListeSignature(infoNumeroListeSignature);
        dossier.save(session);

        session.save();
        closeSession();
        openSession();
        dossier = retrieveDossier();
        traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        // check properties persistance
        infoNumeroListeSignature = traitementPapier.getNumerosListeSignature();
        assertNotNull(infoNumeroListeSignature);
        assertEquals(2, infoNumeroListeSignature.size());
        infoNumeroListe = infoNumeroListeSignature.get(0);
        assertEquals(date1, infoNumeroListe.getDateGenerationListe());
        assertEquals(texte1, infoNumeroListe.getNumeroListe());

        log.info("------------FIN TEST Info Numero Liste -------------");
    }

    public void testInfoHistoriqueAmpliation() throws ClientException {

        log.info("------------TEST Info Historique Ampliation --------------- ");

        // check properties
        List<String> listeString = new ArrayList<String>();
        listeString.add("mail1@test.com");
        listeString.add("mail2@test.com");
        // "texte1";
        Calendar date1 = GregorianCalendar.getInstance();
        date1.set(2009, 6, 14);

        InfoHistoriqueAmpliation infoHistoriqueAmpliation = new InfoHistoriqueAmpliationImpl();
        infoHistoriqueAmpliation.setDateEnvoiAmpliation(date1);
        infoHistoriqueAmpliation.setAmpliationDestinataireMails(listeString);

        assertNotNull(infoHistoriqueAmpliation.getDateEnvoiAmpliation());
        assertNotNull(infoHistoriqueAmpliation.getAmpliationDestinataireMails());

        List<InfoHistoriqueAmpliation> infoHistoriqueAmpliationSignature = new ArrayList<InfoHistoriqueAmpliation>();
        infoHistoriqueAmpliationSignature.add(infoHistoriqueAmpliation);
        infoHistoriqueAmpliationSignature.add(infoHistoriqueAmpliation);

        Dossier dossier = retrieveDossier();
        TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        traitementPapier.setAmpliationHistorique(infoHistoriqueAmpliationSignature);
        dossier.save(session);

        session.save();
        closeSession();
        openSession();
        dossier = retrieveDossier();

        // check properties persistance
        traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        infoHistoriqueAmpliationSignature = traitementPapier.getAmpliationHistorique();
        assertNotNull(infoHistoriqueAmpliationSignature);
        assertEquals(2, infoHistoriqueAmpliationSignature.size());
        infoHistoriqueAmpliation = infoHistoriqueAmpliationSignature.get(0);
        assertEquals(date1, infoHistoriqueAmpliation.getDateEnvoiAmpliation());
        assertEquals(listeString, infoHistoriqueAmpliation.getAmpliationDestinataireMails());

        log.info("------------FIN TEST Info Historique Ampliation -------------");
    }

    public void testTraitementPapierReference() throws ClientException {

        Dossier dossier = retrieveDossier();
        TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        // valeur par defaut
        assertEquals(Boolean.TRUE, traitementPapier.getTexteAPublier());
        String signataire = traitementPapier.getSignataire();
        assertEquals(SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_AUCUN, signataire);

        // set & get

        Calendar date1 = GregorianCalendar.getInstance();
        date1.set(2011, 7, 15);
        final String valueSignataire = "TEST_SIGNATAIRE";
        final Boolean valueTexteAPublier = Boolean.FALSE;

        traitementPapier.setDateArrivePapier(date1);
        traitementPapier.setSignataire(valueSignataire);
        traitementPapier.setTexteAPublier(valueTexteAPublier);

        dossier.save(session);
        session.save();

        // check persistance
        closeSession();
        openSession();
        dossier = retrieveDossier();
        traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        assertEquals(date1, traitementPapier.getDateArrivePapier());
        assertEquals(valueTexteAPublier, traitementPapier.getTexteAPublier());
        assertEquals(valueSignataire, traitementPapier.getSignataire());

    }

    public void testTraitementPapierCommunication() throws ClientException {

        Dossier dossier = retrieveDossier();
        TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        final String priorite = "Urgent";
        String textes[] = new String[18];
        Calendar dates[] = new Calendar[18];

        for (int i = 0; i < 18; ++i) {
            String texte = "texte_" + i;
            Calendar a_date = GregorianCalendar.getInstance();
            a_date.set(2009, 1 + i, 1 + i);
            textes[i] = texte;
            dates[i] = a_date;
        }

        String nomsSignataires = "signataire_1";

        // check default
        assertEquals(SolonEpgTraitementPapierConstants.COMMUNICATION_PRIORITE_NORMAL, traitementPapier.getPriorite());

        // check properties
        DestinataireCommunication[] dests = new DestinataireCommunication[6];
        for (int i = 0; i < 6; ++i) {
            DestinataireCommunication destinataireCommunication = new DestinataireCommunicationImpl();
            destinataireCommunication.setNomDestinataireCommunication(textes[i * 3]);
            destinataireCommunication.setObjet(textes[i * 3 + 1]);
            destinataireCommunication.setSensAvis(textes[i * 3 + 2]);
            destinataireCommunication.setDateEnvoi(dates[i * 3]);
            destinataireCommunication.setDateRelance(dates[i * 3 + 1]);
            destinataireCommunication.setDateRetour(dates[i * 3 + 2]);
            dests[i] = destinataireCommunication;
        }

        List<DestinataireCommunication> destinataireCommunicationListe1 = new ArrayList<DestinataireCommunication>();
        destinataireCommunicationListe1.add(dests[0]);
        destinataireCommunicationListe1.add(dests[1]);

        List<DestinataireCommunication> destinataireCommunicationListe2 = new ArrayList<DestinataireCommunication>();
        destinataireCommunicationListe2.add(dests[2]);
        destinataireCommunicationListe2.add(dests[3]);

        List<DestinataireCommunication> destinataireCommunicationListe3 = new ArrayList<DestinataireCommunication>();
        destinataireCommunicationListe3.add(dests[4]);
        destinataireCommunicationListe3.add(dests[5]);

        traitementPapier.setCabinetPmCommunication(destinataireCommunicationListe1);
        traitementPapier.setChargeMissionCommunication(destinataireCommunicationListe2);
        traitementPapier.setAutresDestinatairesCommunication(destinataireCommunicationListe3);
        traitementPapier.setNomsSignatairesCommunication(nomsSignataires);
        traitementPapier.setPriorite(priorite);

        dossier.save(session);
        session.save();

        closeSession();
        openSession();
        dossier = retrieveDossier();
        traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        int index = 0;
        // check properties persistance
        List<DestinataireCommunication> destinataireCommunicationListe = traitementPapier.getCabinetPmCommunication();
        assertNotNull(destinataireCommunicationListe);
        assertEquals(2, destinataireCommunicationListe.size());

        index = 0;
        DestinataireCommunication destinataireCommunication = destinataireCommunicationListe.get(0);
        assertEquals(textes[index * 3], destinataireCommunication.getNomDestinataireCommunication());
        assertEquals(textes[index * 3 + 1], destinataireCommunication.getObjet());
        assertEquals(textes[index * 3 + 2], destinataireCommunication.getSensAvis());
        assertEquals(dates[index * 3], destinataireCommunication.getDateEnvoi());
        assertEquals(dates[index * 3 + 1], destinataireCommunication.getDateRelance());
        assertEquals(dates[index * 3 + 2], destinataireCommunication.getDateRetour());
        index = 1;
        destinataireCommunication = destinataireCommunicationListe.get(1);
        assertEquals(textes[index * 3], destinataireCommunication.getNomDestinataireCommunication());
        assertEquals(textes[index * 3 + 1], destinataireCommunication.getObjet());
        assertEquals(textes[index * 3 + 2], destinataireCommunication.getSensAvis());
        assertEquals(dates[index * 3], destinataireCommunication.getDateEnvoi());
        assertEquals(dates[index * 3 + 1], destinataireCommunication.getDateRelance());
        assertEquals(dates[index * 3 + 2], destinataireCommunication.getDateRetour());

        destinataireCommunicationListe = traitementPapier.getChargeMissionCommunication();
        assertNotNull(destinataireCommunicationListe);
        assertEquals(2, destinataireCommunicationListe.size());

        index = 2;
        destinataireCommunication = destinataireCommunicationListe.get(0);
        assertEquals(textes[index * 3], destinataireCommunication.getNomDestinataireCommunication());
        assertEquals(textes[index * 3 + 1], destinataireCommunication.getObjet());
        assertEquals(textes[index * 3 + 2], destinataireCommunication.getSensAvis());
        assertEquals(dates[index * 3], destinataireCommunication.getDateEnvoi());
        assertEquals(dates[index * 3 + 1], destinataireCommunication.getDateRelance());
        assertEquals(dates[index * 3 + 2], destinataireCommunication.getDateRetour());
        index = 3;
        destinataireCommunication = destinataireCommunicationListe.get(1);
        assertEquals(textes[index * 3], destinataireCommunication.getNomDestinataireCommunication());
        assertEquals(textes[index * 3 + 1], destinataireCommunication.getObjet());
        assertEquals(textes[index * 3 + 2], destinataireCommunication.getSensAvis());
        assertEquals(dates[index * 3], destinataireCommunication.getDateEnvoi());
        assertEquals(dates[index * 3 + 1], destinataireCommunication.getDateRelance());
        assertEquals(dates[index * 3 + 2], destinataireCommunication.getDateRetour());

        destinataireCommunicationListe = traitementPapier.getAutresDestinatairesCommunication();
        assertNotNull(destinataireCommunicationListe);
        assertEquals(2, destinataireCommunicationListe.size());

        index = 4;
        destinataireCommunication = destinataireCommunicationListe.get(0);
        assertEquals(textes[index * 3], destinataireCommunication.getNomDestinataireCommunication());
        assertEquals(textes[index * 3 + 1], destinataireCommunication.getObjet());
        assertEquals(textes[index * 3 + 2], destinataireCommunication.getSensAvis());
        assertEquals(dates[index * 3], destinataireCommunication.getDateEnvoi());
        assertEquals(dates[index * 3 + 1], destinataireCommunication.getDateRelance());
        assertEquals(dates[index * 3 + 2], destinataireCommunication.getDateRetour());
        index = 5;
        destinataireCommunication = destinataireCommunicationListe.get(1);
        assertEquals(textes[index * 3], destinataireCommunication.getNomDestinataireCommunication());
        assertEquals(textes[index * 3 + 1], destinataireCommunication.getObjet());
        assertEquals(textes[index * 3 + 2], destinataireCommunication.getSensAvis());
        assertEquals(dates[index * 3], destinataireCommunication.getDateEnvoi());
        assertEquals(dates[index * 3 + 1], destinataireCommunication.getDateRelance());
        assertEquals(dates[index * 3 + 2], destinataireCommunication.getDateRetour());

        assertEquals(nomsSignataires, traitementPapier.getNomsSignatairesCommunication());
        assertEquals(priorite, traitementPapier.getPriorite());

    }

    public void testTraitementPapierSignature() throws ClientException {

        String textes[] = new String[3];
        Calendar dates[] = new Calendar[6];
        DonneesSignataire[] signs = new DonneesSignataire[3];
        for (int i = 0; i < 3; ++i) {
            dates[2 * i] = GregorianCalendar.getInstance();
            dates[2 * i].set(2009, 1 + 2 * i, 1 + 2 * i);
            dates[2 * i + 1] = GregorianCalendar.getInstance();
            dates[2 * i + 1].set(2009, 1 + 2 * i, 1 + 2 * i);

            textes[i] = "texte_" + i;

            signs[i] = new DonneesSignataireImpl();
            signs[i].setDateEnvoiSignature(dates[2 * i]);
            signs[i].setDateRetourSignature(dates[2 * i + 1]);
            signs[i].setCommentaireSignature(textes[i]);

        }

        Dossier dossier = retrieveDossier();
        TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        traitementPapier.setSignaturePm(signs[0]);
        traitementPapier.setSignaturePr(signs[1]);
        traitementPapier.setSignatureSgg(signs[2]);

        dossier.save(session);
        session.save();
        closeSession();
        openSession();

        dossier = retrieveDossier();

        int index = 0;
        DonneesSignataire sign = traitementPapier.getSignaturePm();
        assertNotNull(sign);

        assertEquals(dates[2 * index], sign.getDateEnvoiSignature());
        assertEquals(dates[2 * index + 1], sign.getDateRetourSignature());
        assertEquals(textes[index], sign.getCommentaireSignature());

        index = 1;
        sign = traitementPapier.getSignaturePr();
        assertEquals(dates[2 * index], sign.getDateEnvoiSignature());
        assertEquals(dates[2 * index + 1], sign.getDateRetourSignature());
        assertEquals(textes[index], sign.getCommentaireSignature());

        index = 2;
        sign = traitementPapier.getSignatureSgg();
        assertEquals(dates[2 * index], sign.getDateEnvoiSignature());
        assertEquals(dates[2 * index + 1], sign.getDateRetourSignature());
        assertEquals(textes[index], sign.getCommentaireSignature());
    }

}