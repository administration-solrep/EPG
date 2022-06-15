package fr.dila.solonepg.core.cases;

import fr.dila.solonepg.api.cases.ConseilEtat;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.cases.RetourDila;
import fr.dila.solonepg.api.cases.TraitementPapier;
import fr.dila.solonepg.api.cases.typescomplexe.DestinataireCommunication;
import fr.dila.solonepg.api.cases.typescomplexe.DonneesSignataire;
import fr.dila.solonepg.api.cases.typescomplexe.DossierTransposition;
import fr.dila.solonepg.api.cases.typescomplexe.InfoEpreuve;
import fr.dila.solonepg.api.cases.typescomplexe.InfoHistoriqueAmpliation;
import fr.dila.solonepg.api.cases.typescomplexe.InfoNumeroListe;
import fr.dila.solonepg.api.cases.typescomplexe.ParutionBo;
import fr.dila.solonepg.api.cases.typescomplexe.TranspositionsContainer;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.RetourDilaConstants;
import fr.dila.solonepg.api.constant.SolonEpgTraitementPapierConstants;
import fr.dila.solonepg.core.cases.typescomplexe.DestinataireCommunicationImpl;
import fr.dila.solonepg.core.cases.typescomplexe.DonneesSignataireImpl;
import fr.dila.solonepg.core.cases.typescomplexe.DossierTranspositionImpl;
import fr.dila.solonepg.core.cases.typescomplexe.InfoEpreuveImpl;
import fr.dila.solonepg.core.cases.typescomplexe.InfoHistoriqueAmpliationImpl;
import fr.dila.solonepg.core.cases.typescomplexe.InfoNumeroListeImpl;
import fr.dila.solonepg.core.cases.typescomplexe.ParutionBoImpl;
import fr.dila.solonepg.core.cases.typescomplexe.TranspositionsContainerImpl;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.PropertyException;

/**
 * Tests unitaires sur le dossier (metadonnées).
 *
 * @author arolin
 */
public class TestDossier extends AbstractEPGTest {
    private static final Log log = LogFactory.getLog(TestDossier.class);

    protected DocumentRef dossierRef;

    @Before
    public void setUp() throws Exception {
        DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossierTest");

        dossierRef = docModel.getRef();
        Dossier dossier = retrieveDossier(dossierRef);
        Assert.assertNotNull(dossier);
    }

    private Dossier retrieveDossier() {
        return retrieveDossier(dossierRef);
    }

    private Dossier retrieveDossier(DocumentRef ref) {
        DocumentModel docModel = session.getDocument(ref);
        Dossier dossier = docModel.getAdapter(Dossier.class);
        Assert.assertNotNull(dossier);
        return dossier;
    }

    @Test
    public void testGetDocument() throws PropertyException {
        // on verifie que le document est de type dossier, possède
        // possède le schéma "dossier"
        log.info("begin : test dossier type ");

        Dossier dossier = retrieveDossier();
        DocumentModel dossierModel = dossier.getDocument();
        Assert.assertNotNull(dossierModel);
        Assert.assertEquals(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, dossierModel.getType());
        String[] schemas = dossierModel.getSchemas();
        boolean foundDossierSchema = false;
        for (String schema : schemas) {
            if (schema.equals(DossierSolonEpgConstants.DOSSIER_SCHEMA)) {
                foundDossierSchema = true;
            }
        }
        Assert.assertTrue(foundDossierSchema);
    }

    @Test
    public void testDiversAndCeDossierProperties() {
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

        //        closeSession();
        //        openSession();

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

        // Assert.assertEquals(dossier.getOriginedossier(), originedossier);
        Assert.assertEquals(dossier.getCategorieActe(), categorieActe);
        Assert.assertEquals(dossier.getBaseLegaleActe(), baseLegaleActe);
        Assert.assertEquals(dossier.getDatePublication(), datePublication);
        Assert.assertEquals(dossier.getPublicationRapportPresentation(), publicationRapportPresentation);
        Assert.assertEquals(conseilEtat.getSectionCe(), sectionCe);
        Assert.assertEquals(conseilEtat.getRapporteurCe(), rapporteurCe);
        Assert.assertEquals(conseilEtat.getDateTransmissionSectionCe(), dateTransmissionSectionCe);
        Assert.assertEquals(conseilEtat.getDateSectionCe(), dateSectionCe);
        Assert.assertEquals(conseilEtat.getDateAgCe(), dateAgCe);
        Assert.assertEquals(conseilEtat.getNumeroISA(), numeroISA);
    }

    @Test
    public void testValidationEtPublicationDossierProperties() {
        // init metadata variable

        List<String> chargeMissionIds = new ArrayList<>();
        chargeMissionIds.add("test1");
        chargeMissionIds.add("test2");

        List<String> conseillerPmIds = new ArrayList<>();
        conseillerPmIds.add("conseillerPmIds1");
        conseillerPmIds.add("conseillerPmIds2");

        Calendar dateSignature = GregorianCalendar.getInstance();
        dateSignature.set(2009, 5, 14);
        Calendar pourFournitureEpreuve = GregorianCalendar.getInstance();
        pourFournitureEpreuve.set(2009, 8, 14);

        List<String> vecteurPublication = new ArrayList<>();
        vecteurPublication.add("Journal Officiel");

        List<String> publicationsConjointes = new ArrayList<>();
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
        //        closeSession();
        //        openSession();

        dossier = retrieveDossier();
        // check properties persistance
        retourDila = dossier.getDocument().getAdapter(RetourDila.class);

        Assert.assertEquals(dossier.getNomCompletChargesMissions(), chargeMissionIds);
        Assert.assertEquals(dossier.getNomCompletConseillersPms(), conseillerPmIds);
        Assert.assertEquals(dossier.getDateSignature(), dateSignature);
        Assert.assertEquals(dossier.getDatePourFournitureEpreuve(), pourFournitureEpreuve);
        Assert.assertEquals(dossier.getVecteurPublication(), vecteurPublication);
        Assert.assertEquals(dossier.getPublicationsConjointes(), publicationsConjointes);
        Assert.assertEquals(dossier.getPublicationIntegraleOuExtrait(), publicationIntegraleOuExtrait);
        Assert.assertEquals(dossier.getDecretNumerote(), decretNumerote);
        Assert.assertEquals(retourDila.getModeParution(), modeParution);
        Assert.assertEquals(dossier.getDelaiPublication(), delaiPublication);
        Assert.assertEquals(dossier.getDatePreciseePublication(), datePreciseePublication);
    }

    @Test
    public void testGeneralesAndResponsablesDossierProperties() {
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
        //        closeSession();
        //        openSession();
        dossier = retrieveDossier();
        // check properties persistance

        Assert.assertEquals(dossier.getNumeroNor(), numeroNor);
        Assert.assertEquals(dossier.getTypeActe(), typeActe);
        Assert.assertEquals(dossier.getTitreActe(), titreActe);

        Assert.assertEquals(dossier.getMinistereResp(), ministereResp);
        Assert.assertEquals(dossier.getDirectionResp(), directionResp);
        Assert.assertEquals(dossier.getMinistereAttache(), ministereAttache);
        Assert.assertEquals(dossier.getDirectionAttache(), directionAttache);
        Assert.assertEquals(dossier.getNomRespDossier(), nomRespDossier);
        Assert.assertEquals(dossier.getPrenomRespDossier(), prenomRespDossier);
        Assert.assertEquals(dossier.getQualiteRespDossier(), qualiteRespDossier);
        Assert.assertEquals(dossier.getTelephoneRespDossier(), telRespDossier);
        Assert.assertEquals(dossier.getMailRespDossier(), mailRespDossier);
        Assert.assertEquals(dossier.getNomCompletRespDossier(), nomCompletRespDossier);
        Assert.assertEquals(dossier.getIdCreateurDossier(), idCreateurDossier);
    }

    /**
     * Test de la méthode getDossierMetadata
     *
     * @throws Exception
     */
    @Test
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
        //        closeSession();
        //        openSession();
        dossier = retrieveDossier();

        // Créé un nouveau dossier et copie des données de l'ancien dossier
        DocumentModel docModel = createDocument(DossierSolonEpgConstants.DOSSIER_DOCUMENT_TYPE, "newDossier");
        Dossier newDossier = docModel.getAdapter(Dossier.class);
        newDossier.getDossierMetadata(dossier);

        // check properties persistance

        Assert.assertEquals(newDossier.getNumeroNor(), numeroNor);
        Assert.assertEquals(newDossier.getTypeActe(), typeActe);
        Assert.assertEquals(newDossier.getTitreActe(), titreActe);

        Assert.assertEquals(newDossier.getMinistereResp(), ministereResp);
        Assert.assertEquals(newDossier.getDirectionResp(), directionResp);
        Assert.assertEquals(newDossier.getMinistereAttache(), ministereAttache);
        Assert.assertEquals(newDossier.getDirectionAttache(), directionAttache);
        Assert.assertEquals(newDossier.getNomRespDossier(), nomRespDossier);
        Assert.assertEquals(newDossier.getPrenomRespDossier(), prenomRespDossier);
        Assert.assertEquals(newDossier.getQualiteRespDossier(), qualiteRespDossier);
        Assert.assertEquals(newDossier.getTelephoneRespDossier(), telRespDossier);
        Assert.assertEquals(newDossier.getMailRespDossier(), mailRespDossier);
        Assert.assertEquals(newDossier.getNomCompletRespDossier(), nomCompletRespDossier);
        Assert.assertEquals(newDossier.getIdCreateurDossier(), idCreateurDossier);
    }

    @Test
    public void testParutionEtIndexationDossierProperties() {
        // init metadata variable

        List<String> rubrique = new ArrayList<>();
        rubrique.add("rubrique");
        rubrique.add("rubrique2");

        List<String> motsCles = new ArrayList<>();
        motsCles.add("motsCles");
        motsCles.add("motsCles2");

        List<String> champLibre = new ArrayList<>();
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
        Map<String, Serializable> serializableMap = new HashMap<>();
        serializableMap.put(RetourDilaConstants.RETOUR_DILA_DATE_PARUTION_BO_PROPERTY, dateParutionBo);
        serializableMap.put(RetourDilaConstants.RETOUR_DILA_NUMERO_TEXTE_PARUTION_BO_PROPERTY, numeroTexteParutionBo);
        serializableMap.put(RetourDilaConstants.RETOUR_DILA_PAGE_BO_PROPERTY, pageParutionJorf);
        parutionBo.setSerializableMap(serializableMap);
        List<ParutionBo> parutionBoList = new ArrayList<>();
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
        //        closeSession();
        //        openSession();

        dossier = retrieveDossier();

        // check properties persistance
        retourDila = dossier.getDocument().getAdapter(RetourDila.class);
        Assert.assertEquals(retourDila.getDateParutionJorf(), dateParutionJorf);
        Assert.assertEquals(retourDila.getNumeroTexteParutionJorf(), numeroTexteParutionJorf);
        Assert.assertEquals(retourDila.getPageParutionJorf(), pageParutionJorf);
        parutionBoList = retourDila.getParutionBo();
        Assert.assertNotNull(parutionBoList);
        Assert.assertEquals(parutionBoList.size(), 1);
        Assert.assertEquals(parutionBoList.get(0).getNumeroTexteParutionBo(), numeroTexteParutionBo);
        Assert.assertEquals(parutionBoList.get(0).getDateParutionBo(), dateParutionBo);
        Assert.assertEquals(parutionBoList.get(0).getPageParutionBo(), pageParutionJorf);
        Assert.assertEquals(dossier.getZoneComSggDila(), zoneComDila);
        Assert.assertEquals(dossier.getIndexationRubrique(), rubrique);
        Assert.assertEquals(dossier.getIndexationMotsCles(), motsCles);
        Assert.assertEquals(dossier.getIndexationChampLibre(), champLibre);
    }

    @Test
    public void testTransposition() {
        log.info("------------TEST Transposition--------------- ");

        // check properties
        String ref = "ref";
        String titre = "titre";
        String numeroArticles = "numeroArticles";
        String refMesure = "refMesure";
        String commentaire = "commentaire";

        Map<String, Serializable> serializableMap = new HashMap<>();
        serializableMap.put(ref, "ref");
        serializableMap.put(titre, "titre");
        serializableMap.put(numeroArticles, "num article");
        serializableMap.put(refMesure, "ref mesure");
        serializableMap.put(commentaire, "commentaire");
        DossierTransposition transposition = new DossierTranspositionImpl(serializableMap);

        Assert.assertNotNull(transposition.getSerializableMap());

        TranspositionsContainer dossierApplicationLoi = new TranspositionsContainerImpl();
        dossierApplicationLoi.setTranspositions(Arrays.asList(transposition));

        Dossier dossier = retrieveDossier();
        dossier.setApplicationLoi(dossierApplicationLoi);

        dossier.save(session);
        session.save();

        //        closeSession();
        //        openSession();
        dossier = retrieveDossier();

        // check properties persistance
        List<DossierTransposition> list = dossier.getApplicationLoi().getTranspositions();
        Assert.assertNotNull(list);
        Assert.assertEquals(1, list.size());

        transposition = list.get(0);
        Assert.assertNotNull(transposition);

        log.info("------------FIN TEST Transposition -------------");
    }

    @Test
    public void testDestinataireCommunication() {
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

        Assert.assertEquals(texte1, destinataireCommunication.getNomDestinataireCommunication());
        Assert.assertEquals(texte2, destinataireCommunication.getObjet());
        Assert.assertEquals(texte3, destinataireCommunication.getSensAvis());
        Assert.assertEquals(date1, destinataireCommunication.getDateEnvoi());
        Assert.assertEquals(date2, destinataireCommunication.getDateRelance());
        Assert.assertEquals(date3, destinataireCommunication.getDateRetour());

        List<DestinataireCommunication> destinataireCommunicationListe = new ArrayList<>();
        destinataireCommunicationListe.add(destinataireCommunication);
        destinataireCommunicationListe.add(destinataireCommunication);

        Dossier dossier = retrieveDossier();
        TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        traitementPapier.setChargeMissionCommunication(destinataireCommunicationListe);

        dossier.save(session);
        session.save();

        //        closeSession();
        //        openSession();
        dossier = retrieveDossier();

        // check properties persistance
        traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        destinataireCommunicationListe = traitementPapier.getChargeMissionCommunication();
        Assert.assertNotNull(destinataireCommunicationListe);
        Assert.assertEquals(2, destinataireCommunicationListe.size());
        destinataireCommunication = destinataireCommunicationListe.get(0);
        Assert.assertEquals(texte1, destinataireCommunication.getNomDestinataireCommunication());
        Assert.assertEquals(texte2, destinataireCommunication.getObjet());
        Assert.assertEquals(texte3, destinataireCommunication.getSensAvis());
        Assert.assertEquals(date1, destinataireCommunication.getDateEnvoi());
        Assert.assertEquals(date2, destinataireCommunication.getDateRelance());
        Assert.assertEquals(date3, destinataireCommunication.getDateRetour());
        destinataireCommunication = destinataireCommunicationListe.get(1);
        Assert.assertEquals(texte1, destinataireCommunication.getNomDestinataireCommunication());
        Assert.assertEquals(texte2, destinataireCommunication.getObjet());
        Assert.assertEquals(texte3, destinataireCommunication.getSensAvis());
        Assert.assertEquals(date1, destinataireCommunication.getDateEnvoi());
        Assert.assertEquals(date2, destinataireCommunication.getDateRelance());
        Assert.assertEquals(date3, destinataireCommunication.getDateRetour());

        log.info("------------FIN TEST Destinataire Communication -------------");
    }

    @Test
    public void testDonneesSignataire() {
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

        Assert.assertNotNull(donneesSignataire.getDateEnvoiSignature());
        Assert.assertNotNull(donneesSignataire.getDateRetourSignature());
        Assert.assertNotNull(donneesSignataire.getCommentaireSignature());

        Dossier dossier = retrieveDossier();
        TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        traitementPapier.setSignaturePm(donneesSignataire);
        dossier.save(session);

        session.save();
        //        closeSession();
        //        openSession();
        dossier = retrieveDossier();
        traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        // check properties persistance
        donneesSignataire = traitementPapier.getSignaturePm();
        Assert.assertNotNull(donneesSignataire);
        Assert.assertEquals(date1, donneesSignataire.getDateEnvoiSignature());
        Assert.assertEquals(date2, donneesSignataire.getDateRetourSignature());
        Assert.assertEquals(texte1, donneesSignataire.getCommentaireSignature());

        log.info("------------FIN TEST Donnees Signataire -------------");
    }

    @Test
    public void testInfoEpreuve() {
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

        Assert.assertNotNull(infoEpreuve.getEpreuveDemandeLe());
        Assert.assertNotNull(infoEpreuve.getEpreuvePourLe());
        Assert.assertNotNull(infoEpreuve.getNumeroListe());
        Assert.assertNotNull(infoEpreuve.getEnvoiRelectureLe());
        Assert.assertNotNull(infoEpreuve.getDestinataireEntete());

        Dossier dossier = retrieveDossier();
        TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        traitementPapier.setEpreuve(infoEpreuve);
        dossier.save(session);

        session.save();
        //        closeSession();
        //        openSession();
        dossier = retrieveDossier();

        // check properties persistance
        traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        infoEpreuve = traitementPapier.getEpreuve();
        Assert.assertEquals(date1, infoEpreuve.getEpreuveDemandeLe());
        Assert.assertEquals(date2, infoEpreuve.getEpreuvePourLe());
        Assert.assertEquals(texte1, infoEpreuve.getNumeroListe());
        Assert.assertEquals(date3, infoEpreuve.getEnvoiRelectureLe());
        Assert.assertEquals(texte2, infoEpreuve.getDestinataireEntete());

        log.info("------------FIN TEST Info Epreuve -------------");
    }

    @Test
    public void testInfoNumeroListe() {
        log.info("------------TEST Info Numero Liste --------------- ");

        // check properties
        String texte1 = "texte1";
        Calendar date1 = GregorianCalendar.getInstance();
        date1.set(2009, 6, 14);

        InfoNumeroListe infoNumeroListe = new InfoNumeroListeImpl();
        infoNumeroListe.setDateGenerationListe(date1);
        infoNumeroListe.setNumeroListe(texte1);

        Assert.assertNotNull(infoNumeroListe.getDateGenerationListe());
        Assert.assertNotNull(infoNumeroListe.getNumeroListe());

        List<InfoNumeroListe> infoNumeroListeSignature = new ArrayList<>();
        infoNumeroListeSignature.add(infoNumeroListe);
        infoNumeroListeSignature.add(infoNumeroListe);

        Dossier dossier = retrieveDossier();
        TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        traitementPapier.setNumerosListeSignature(infoNumeroListeSignature);
        dossier.save(session);

        session.save();
        //        closeSession();
        //        openSession();
        dossier = retrieveDossier();
        traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        // check properties persistance
        infoNumeroListeSignature = traitementPapier.getNumerosListeSignature();
        Assert.assertNotNull(infoNumeroListeSignature);
        Assert.assertEquals(2, infoNumeroListeSignature.size());
        infoNumeroListe = infoNumeroListeSignature.get(0);
        Assert.assertEquals(date1, infoNumeroListe.getDateGenerationListe());
        Assert.assertEquals(texte1, infoNumeroListe.getNumeroListe());

        log.info("------------FIN TEST Info Numero Liste -------------");
    }

    @Test
    public void testInfoHistoriqueAmpliation() {
        log.info("------------TEST Info Historique Ampliation --------------- ");

        // check properties
        List<String> listeString = new ArrayList<>();
        listeString.add("mail1@test.com");
        listeString.add("mail2@test.com");
        // "texte1";
        Calendar date1 = GregorianCalendar.getInstance();
        date1.set(2009, 6, 14);

        InfoHistoriqueAmpliation infoHistoriqueAmpliation = new InfoHistoriqueAmpliationImpl();
        infoHistoriqueAmpliation.setDateEnvoiAmpliation(date1);
        infoHistoriqueAmpliation.setAmpliationDestinataireMails(listeString);

        Assert.assertNotNull(infoHistoriqueAmpliation.getDateEnvoiAmpliation());
        Assert.assertNotNull(infoHistoriqueAmpliation.getAmpliationDestinataireMails());

        List<InfoHistoriqueAmpliation> infoHistoriqueAmpliationSignature = new ArrayList<>();
        infoHistoriqueAmpliationSignature.add(infoHistoriqueAmpliation);
        infoHistoriqueAmpliationSignature.add(infoHistoriqueAmpliation);

        Dossier dossier = retrieveDossier();
        TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        traitementPapier.setAmpliationHistorique(infoHistoriqueAmpliationSignature);
        dossier.save(session);

        session.save();
        //        closeSession();
        //        openSession();
        dossier = retrieveDossier();

        // check properties persistance
        traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        infoHistoriqueAmpliationSignature = traitementPapier.getAmpliationHistorique();
        Assert.assertNotNull(infoHistoriqueAmpliationSignature);
        Assert.assertEquals(2, infoHistoriqueAmpliationSignature.size());
        infoHistoriqueAmpliation = infoHistoriqueAmpliationSignature.get(0);
        Assert.assertEquals(date1, infoHistoriqueAmpliation.getDateEnvoiAmpliation());
        Assert.assertEquals(listeString, infoHistoriqueAmpliation.getAmpliationDestinataireMails());

        log.info("------------FIN TEST Info Historique Ampliation -------------");
    }

    @Test
    public void testTraitementPapierReference() {
        Dossier dossier = retrieveDossier();
        TraitementPapier traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        // valeur par defaut
        Assert.assertEquals(Boolean.TRUE, traitementPapier.getTexteAPublier());
        String signataire = traitementPapier.getSignataire();
        Assert.assertEquals(SolonEpgTraitementPapierConstants.REFERENCE_SIGNATAIRE_AUCUN, signataire);

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
        //        closeSession();
        //        openSession();
        dossier = retrieveDossier();
        traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        Assert.assertEquals(date1, traitementPapier.getDateArrivePapier());
        Assert.assertEquals(valueTexteAPublier, traitementPapier.getTexteAPublier());
        Assert.assertEquals(valueSignataire, traitementPapier.getSignataire());
    }

    @Test
    public void testTraitementPapierCommunication() {
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
        Assert.assertEquals(
            SolonEpgTraitementPapierConstants.COMMUNICATION_PRIORITE_NORMAL,
            traitementPapier.getPriorite()
        );

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

        List<DestinataireCommunication> destinataireCommunicationListe1 = new ArrayList<>();
        destinataireCommunicationListe1.add(dests[0]);
        destinataireCommunicationListe1.add(dests[1]);

        List<DestinataireCommunication> destinataireCommunicationListe2 = new ArrayList<>();
        destinataireCommunicationListe2.add(dests[2]);
        destinataireCommunicationListe2.add(dests[3]);

        List<DestinataireCommunication> destinataireCommunicationListe3 = new ArrayList<>();
        destinataireCommunicationListe3.add(dests[4]);
        destinataireCommunicationListe3.add(dests[5]);

        traitementPapier.setCabinetPmCommunication(destinataireCommunicationListe1);
        traitementPapier.setChargeMissionCommunication(destinataireCommunicationListe2);
        traitementPapier.setAutresDestinatairesCommunication(destinataireCommunicationListe3);
        traitementPapier.setNomsSignatairesCommunication(nomsSignataires);
        traitementPapier.setPriorite(priorite);

        dossier.save(session);
        session.save();

        //        closeSession();
        //        openSession();
        dossier = retrieveDossier();
        traitementPapier = dossier.getDocument().getAdapter(TraitementPapier.class);
        int index = 0;
        // check properties persistance
        List<DestinataireCommunication> destinataireCommunicationListe = traitementPapier.getCabinetPmCommunication();
        Assert.assertNotNull(destinataireCommunicationListe);
        Assert.assertEquals(2, destinataireCommunicationListe.size());

        index = 0;
        DestinataireCommunication destinataireCommunication = destinataireCommunicationListe.get(0);
        Assert.assertEquals(textes[index * 3], destinataireCommunication.getNomDestinataireCommunication());
        Assert.assertEquals(textes[index * 3 + 1], destinataireCommunication.getObjet());
        Assert.assertEquals(textes[index * 3 + 2], destinataireCommunication.getSensAvis());
        Assert.assertEquals(dates[index * 3], destinataireCommunication.getDateEnvoi());
        Assert.assertEquals(dates[index * 3 + 1], destinataireCommunication.getDateRelance());
        Assert.assertEquals(dates[index * 3 + 2], destinataireCommunication.getDateRetour());
        index = 1;
        destinataireCommunication = destinataireCommunicationListe.get(1);
        Assert.assertEquals(textes[index * 3], destinataireCommunication.getNomDestinataireCommunication());
        Assert.assertEquals(textes[index * 3 + 1], destinataireCommunication.getObjet());
        Assert.assertEquals(textes[index * 3 + 2], destinataireCommunication.getSensAvis());
        Assert.assertEquals(dates[index * 3], destinataireCommunication.getDateEnvoi());
        Assert.assertEquals(dates[index * 3 + 1], destinataireCommunication.getDateRelance());
        Assert.assertEquals(dates[index * 3 + 2], destinataireCommunication.getDateRetour());

        destinataireCommunicationListe = traitementPapier.getChargeMissionCommunication();
        Assert.assertNotNull(destinataireCommunicationListe);
        Assert.assertEquals(2, destinataireCommunicationListe.size());

        index = 2;
        destinataireCommunication = destinataireCommunicationListe.get(0);
        Assert.assertEquals(textes[index * 3], destinataireCommunication.getNomDestinataireCommunication());
        Assert.assertEquals(textes[index * 3 + 1], destinataireCommunication.getObjet());
        Assert.assertEquals(textes[index * 3 + 2], destinataireCommunication.getSensAvis());
        Assert.assertEquals(dates[index * 3], destinataireCommunication.getDateEnvoi());
        Assert.assertEquals(dates[index * 3 + 1], destinataireCommunication.getDateRelance());
        Assert.assertEquals(dates[index * 3 + 2], destinataireCommunication.getDateRetour());
        index = 3;
        destinataireCommunication = destinataireCommunicationListe.get(1);
        Assert.assertEquals(textes[index * 3], destinataireCommunication.getNomDestinataireCommunication());
        Assert.assertEquals(textes[index * 3 + 1], destinataireCommunication.getObjet());
        Assert.assertEquals(textes[index * 3 + 2], destinataireCommunication.getSensAvis());
        Assert.assertEquals(dates[index * 3], destinataireCommunication.getDateEnvoi());
        Assert.assertEquals(dates[index * 3 + 1], destinataireCommunication.getDateRelance());
        Assert.assertEquals(dates[index * 3 + 2], destinataireCommunication.getDateRetour());

        destinataireCommunicationListe = traitementPapier.getAutresDestinatairesCommunication();
        Assert.assertNotNull(destinataireCommunicationListe);
        Assert.assertEquals(2, destinataireCommunicationListe.size());

        index = 4;
        destinataireCommunication = destinataireCommunicationListe.get(0);
        Assert.assertEquals(textes[index * 3], destinataireCommunication.getNomDestinataireCommunication());
        Assert.assertEquals(textes[index * 3 + 1], destinataireCommunication.getObjet());
        Assert.assertEquals(textes[index * 3 + 2], destinataireCommunication.getSensAvis());
        Assert.assertEquals(dates[index * 3], destinataireCommunication.getDateEnvoi());
        Assert.assertEquals(dates[index * 3 + 1], destinataireCommunication.getDateRelance());
        Assert.assertEquals(dates[index * 3 + 2], destinataireCommunication.getDateRetour());
        index = 5;
        destinataireCommunication = destinataireCommunicationListe.get(1);
        Assert.assertEquals(textes[index * 3], destinataireCommunication.getNomDestinataireCommunication());
        Assert.assertEquals(textes[index * 3 + 1], destinataireCommunication.getObjet());
        Assert.assertEquals(textes[index * 3 + 2], destinataireCommunication.getSensAvis());
        Assert.assertEquals(dates[index * 3], destinataireCommunication.getDateEnvoi());
        Assert.assertEquals(dates[index * 3 + 1], destinataireCommunication.getDateRelance());
        Assert.assertEquals(dates[index * 3 + 2], destinataireCommunication.getDateRetour());

        Assert.assertEquals(nomsSignataires, traitementPapier.getNomsSignatairesCommunication());
        Assert.assertEquals(priorite, traitementPapier.getPriorite());
    }

    @Test
    public void testTraitementPapierSignature() {
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
        //        closeSession();
        //        openSession();

        dossier = retrieveDossier();

        int index = 0;
        DonneesSignataire sign = traitementPapier.getSignaturePm();
        Assert.assertNotNull(sign);

        Assert.assertEquals(dates[2 * index], sign.getDateEnvoiSignature());
        Assert.assertEquals(dates[2 * index + 1], sign.getDateRetourSignature());
        Assert.assertEquals(textes[index], sign.getCommentaireSignature());

        index = 1;
        sign = traitementPapier.getSignaturePr();
        Assert.assertEquals(dates[2 * index], sign.getDateEnvoiSignature());
        Assert.assertEquals(dates[2 * index + 1], sign.getDateRetourSignature());
        Assert.assertEquals(textes[index], sign.getCommentaireSignature());

        index = 2;
        sign = traitementPapier.getSignatureSgg();
        Assert.assertEquals(dates[2 * index], sign.getDateEnvoiSignature());
        Assert.assertEquals(dates[2 * index + 1], sign.getDateRetourSignature());
        Assert.assertEquals(textes[index], sign.getCommentaireSignature());
    }
}
