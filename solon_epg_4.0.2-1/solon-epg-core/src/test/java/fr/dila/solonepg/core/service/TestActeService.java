package fr.dila.solonepg.core.service;

import fr.dila.solonepg.api.constant.ActeVisibilityConstants;
import fr.dila.solonepg.api.constant.TypeActe;
import fr.dila.solonepg.api.constant.TypeActeConstants;
import fr.dila.solonepg.api.service.ActeService;
import fr.dila.solonepg.core.test.AbstractEPGTest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests unitaires sur le service des modèles de parapheur.
 *
 * @author arolin
 */
public class TestActeService extends AbstractEPGTest {
    private static final String SUFFIXE_A = "A";
    private static final String SUFFIXE_X = "X";
    private static final String SUFFIXE_D = "D";
    private static final String SUFFIXE_L = "L";

    private ActeService acteService;

    private Map<String, TypeActe> actes;
    private Set<String> actesLoi;
    private Set<String> actesArrete;
    private Set<String> actesDecret;
    private Set<String> typesActesExtendFDD;
    private Set<String> typesActesExtendParaph;
    private Set<String> typesActesPublicationNonVisible;
    private static final Set<String> actesOrdonnance = Collections.unmodifiableSet(
        Collections.singleton(TypeActeConstants.TYPE_ACTE_ORDONNANCE)
    );
    private static final Set<String> actesRectificatif = Collections.unmodifiableSet(
        Collections.singleton(TypeActeConstants.TYPE_ACTE_RECTIFICATIF)
    );
    private static final Set<String> actesNominatif = Collections.unmodifiableSet(
        Collections.singleton(TypeActeConstants.TYPE_ACTE_DECRET_CM_IND)
    );

    @Before
    public void setUp() {
        acteService = SolonEpgServiceLocator.getActeService();
        actes = new HashMap<>();
        actes.put(
            TypeActeConstants.TYPE_ACTE_AMNISTIE,
            new TypeActe(TypeActeConstants.TYPE_ACTE_AMNISTIE, "Amnistie", "Y")
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL,
            new TypeActe(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL, "Arrêté ministériel", SUFFIXE_A)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL,
            new TypeActe(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL, "Arrêté interministériel", SUFFIXE_A)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_ARRETE_PM,
            new TypeActe(TypeActeConstants.TYPE_ACTE_ARRETE_PM, "Arrêté PM", SUFFIXE_A)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_ARRETE_PR,
            new TypeActe(TypeActeConstants.TYPE_ACTE_ARRETE_PR, "Arrêté PR", SUFFIXE_A)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_AVENANT,
            new TypeActe(TypeActeConstants.TYPE_ACTE_AVENANT, "Avenant", SUFFIXE_X)
        );
        actes.put(TypeActeConstants.TYPE_ACTE_AVIS, new TypeActe(TypeActeConstants.TYPE_ACTE_AVIS, "Avis", "V"));
        actes.put(
            TypeActeConstants.TYPE_ACTE_CIRCULAIRE,
            new TypeActe(TypeActeConstants.TYPE_ACTE_CIRCULAIRE, "Circulaire", "C")
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_CITATION,
            new TypeActe(TypeActeConstants.TYPE_ACTE_CITATION, "Citation", "T")
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_COMMUNIQUE,
            new TypeActe(TypeActeConstants.TYPE_ACTE_COMMUNIQUE, "Communiqué", SUFFIXE_X)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_CONVENTION,
            new TypeActe(TypeActeConstants.TYPE_ACTE_CONVENTION, "Convention", SUFFIXE_X)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DECISION,
            new TypeActe(TypeActeConstants.TYPE_ACTE_DECISION, "Décision", "S")
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37,
            new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37, "Décret CE art. 37", SUFFIXE_D)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DECRET_CE,
            new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CE, "Décret CE", SUFFIXE_D)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DECRET_CE_CM,
            new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM, "Décret CE CM", SUFFIXE_D)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DECRET_CM,
            new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CM, "Décret CM", SUFFIXE_D)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DECRET_PR,
            new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_PR, "Décret PR", SUFFIXE_D)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE,
            new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE, "Décret simple", SUFFIXE_D)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DECRET_DE_PUBLICATION_DE_TRAITE_OU_ACCORD,
            new TypeActe(
                TypeActeConstants.TYPE_ACTE_DECRET_DE_PUBLICATION_DE_TRAITE_OU_ACCORD,
                "Décret de publication de traité ou accord",
                SUFFIXE_D
            )
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DELIBERATION,
            new TypeActe(TypeActeConstants.TYPE_ACTE_DELIBERATION, "Délibération", SUFFIXE_X)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DEMANDE_AVIS_CE,
            new TypeActe(TypeActeConstants.TYPE_ACTE_DEMANDE_AVIS_CE, "Demande Avis CE", SUFFIXE_X)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DIVERS,
            new TypeActe(TypeActeConstants.TYPE_ACTE_DIVERS, "Divers", SUFFIXE_X)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_EXEQUATUR,
            new TypeActe(TypeActeConstants.TYPE_ACTE_EXEQUATUR, "Exequatur", "E")
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_INSTRUCTION,
            new TypeActe(TypeActeConstants.TYPE_ACTE_INSTRUCTION, "Instruction", "J")
        );
        actes.put(TypeActeConstants.TYPE_ACTE_LISTE, new TypeActe(TypeActeConstants.TYPE_ACTE_LISTE, "Liste", "K"));
        actes.put(TypeActeConstants.TYPE_ACTE_LOI, new TypeActe(TypeActeConstants.TYPE_ACTE_LOI, "Loi", SUFFIXE_L));
        actes.put(
            TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION,
            new TypeActe(
                TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION,
                "Loi art. 53 de la Constitution",
                SUFFIXE_L
            )
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE,
            new TypeActe(TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE, "Loi constitutionnelle", SUFFIXE_L)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE,
            new TypeActe(TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE, "Loi organique", SUFFIXE_L)
        );
        actes.put(TypeActeConstants.TYPE_ACTE_NOTE, new TypeActe(TypeActeConstants.TYPE_ACTE_NOTE, "Note", "N"));
        actes.put(
            TypeActeConstants.TYPE_ACTE_ORDONNANCE,
            new TypeActe(TypeActeConstants.TYPE_ACTE_ORDONNANCE, "Ordonnance", "R")
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_RAPPORT,
            new TypeActe(TypeActeConstants.TYPE_ACTE_RAPPORT, "Rapport", "P")
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_RECTIFICATIF,
            new TypeActe(TypeActeConstants.TYPE_ACTE_RECTIFICATIF, "Rectificatif", "Z")
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_TABLEAU,
            new TypeActe(TypeActeConstants.TYPE_ACTE_TABLEAU, "Tableau", "B")
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_TACHE_GENERIQUE,
            new TypeActe(TypeActeConstants.TYPE_ACTE_TACHE_GENERIQUE, "Tâche Générique", SUFFIXE_X)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND,
            new TypeActe(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND, "Arrêté ministériel individuel", SUFFIXE_A)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND,
            new TypeActe(
                TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND,
                "Arrêté interministériel individuel",
                SUFFIXE_A
            )
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND,
            new TypeActe(TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND, "Arrêté PM individuel", SUFFIXE_A)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND,
            new TypeActe(TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND, "Arrêté PR individuel", SUFFIXE_A)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DECRET_CE_IND,
            new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND, "Décret CE individuel", SUFFIXE_D)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND,
            new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND, "Décret CE CM individuel", SUFFIXE_D)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DECRET_CM_IND,
            new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_CM_IND, "Décret CM individuel", SUFFIXE_D)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DECRET_PR_IND,
            new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND, "Décret PR individuel", SUFFIXE_D)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND,
            new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND, "Décret simple individuel", SUFFIXE_D)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_RAPPORT_AU_PARLEMENT,
            new TypeActe(TypeActeConstants.TYPE_ACTE_RAPPORT_AU_PARLEMENT, "Rapport au Parlement", SUFFIXE_X)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_DECRET_PR_CE,
            new TypeActe(TypeActeConstants.TYPE_ACTE_DECRET_PR_CE, "Décret PR CE", SUFFIXE_D)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_ARRETE_CE,
            new TypeActe(TypeActeConstants.TYPE_ACTE_ARRETE_CE, "Arrêté CE", SUFFIXE_A)
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_TEXTE_NON_PUBLIE,
            new TypeActe(TypeActeConstants.TYPE_ACTE_TEXTE_NON_PUBLIE, "Texte non publié", "U")
        );
        actes.put(
            TypeActeConstants.TYPE_ACTE_INFORMATIONS_PARLEMENTAIRES,
            new TypeActe(
                TypeActeConstants.TYPE_ACTE_INFORMATIONS_PARLEMENTAIRES,
                "Informations parlementaires",
                SUFFIXE_X
            )
        );

        actes.put(
            TypeActeConstants.TYPE_ACTE_ACCORD_COLLECTIF_PUBLIC,
            new TypeActe(
                TypeActeConstants.TYPE_ACTE_ACCORD_COLLECTIF_PUBLIC,
                "Accord collectif dans la fonction publique",
                "O"
            )
        );

        // Actes des lois
        actesLoi = new HashSet<>();
        actesLoi.add(TypeActeConstants.TYPE_ACTE_LOI);
        actesLoi.add(TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE);
        actesLoi.add(TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE);
        actesLoi.add(TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION);

        // Actes des arrêtés
        actesArrete = new HashSet<>();
        actesArrete.add(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL);
        actesArrete.add(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL);
        actesArrete.add(TypeActeConstants.TYPE_ACTE_ARRETE_PM);
        actesArrete.add(TypeActeConstants.TYPE_ACTE_ARRETE_PR);
        actesArrete.add(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND);
        actesArrete.add(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND);
        actesArrete.add(TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND);
        actesArrete.add(TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND);

        // Actes des décrets
        actesDecret = new HashSet<>();
        actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_CE);
        actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37);
        actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM);
        actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_CM);
        actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_DE_PUBLICATION_DE_TRAITE_OU_ACCORD);
        actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_PR);
        actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE);
        actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND);
        actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND);
        actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_CM_IND);
        actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND);
        actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND);
        actesDecret.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_CE);

        // Actes Fdd extended
        typesActesExtendFDD = new HashSet<>();
        typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_AMNISTIE);
        typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37);
        typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_DECRET_CE);
        typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_CE);
        typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM);
        typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_DEMANDE_AVIS_CE);
        typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_LOI);
        typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION);
        typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE);
        typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE);
        typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_ORDONNANCE);
        typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND);
        typesActesExtendFDD.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND);

        // Actes parapheur extended
        typesActesExtendParaph = new HashSet<>();
        typesActesExtendParaph.add(TypeActeConstants.TYPE_ACTE_LOI);
        typesActesExtendParaph.add(TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION);
        typesActesExtendParaph.add(TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE);
        typesActesExtendParaph.add(TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE);

        // Actes visibles publication
        typesActesPublicationNonVisible = new HashSet<>();
        typesActesPublicationNonVisible.add(TypeActeConstants.TYPE_ACTE_RAPPORT_AU_PARLEMENT);
        typesActesPublicationNonVisible.add(TypeActeConstants.TYPE_ACTE_TEXTE_NON_PUBLIE);
    }

    /**
     * Ajoute les différentes visibilités pour les actes dans actesVisibilites
     *
     * @param typeActe
     * @param visibilities :
     *                     01 : CE
     *                     02 : BASE_LEGALE
     *                     03 : PUBLICATION_RAPPORT
     *                     04 : PUBLICATION_INTEGRALE
     *                     05 : DECRET_NUMEROTE
     *                     06 : NUMERO_TEXTE_JO
     *                     07 : APPLICATION_LOI
     *                     08 : HABILITATION
     *                     09 : TRANSPOSITION_DIRECTIVE
     *                     10 : TRANSPOSITION_ORDONNANCE
     *                     11 : DATE_SIGNATURE
     *                     12 : PUBLICATION
     *                     13 : INDEXATION
     *                     14 : TRANSPOSITION_APPLICATION
     *                     15 : PARUTION
     *                     16 : JORF
     *                     17 : PARUTION_BO
     *                     18 : SGG_DILA
     *                     19 : PERIODICITE_RAPPORT
     *                     20 : CATEGORY_ACTE
     *                     21 : PUBLICATION_RAPPORT_PRESENTATION
     */
    private void addVisibiliteActe(
        Map<String, Map<String, Boolean>> actesVisibilities,
        String typeActe,
        boolean... visibilities
    ) {
        Map<String, Boolean> acteVisibility = new HashMap<>();
        if (StringUtils.isNotBlank(typeActe)) {
            acteVisibility.put(ActeVisibilityConstants.CE, visibilities[0]);
            acteVisibility.put(ActeVisibilityConstants.BASE_LEGALE, visibilities[1]);
            acteVisibility.put(ActeVisibilityConstants.PUBLICATION_RAPPORT, visibilities[2]);
            acteVisibility.put(ActeVisibilityConstants.PUBLICATION_INTEGRALE, visibilities[3]);
            acteVisibility.put(ActeVisibilityConstants.DECRET_NUMEROTE, visibilities[4]);
            acteVisibility.put(ActeVisibilityConstants.NUMERO_TEXTE_JO, visibilities[5]);
            acteVisibility.put(ActeVisibilityConstants.APPLICATION_LOI, visibilities[6]);
            acteVisibility.put(ActeVisibilityConstants.HABILITATION, visibilities[7]);
            acteVisibility.put(ActeVisibilityConstants.TRANSPOSITION_DIRECTIVE, visibilities[8]);
            acteVisibility.put(ActeVisibilityConstants.TRANSPOSITION_ORDONNANCE, visibilities[9]);

            acteVisibility.put(ActeVisibilityConstants.DATE_SIGNATURE, visibilities[10]);
            acteVisibility.put(ActeVisibilityConstants.PUBLICATION, visibilities[11]);
            acteVisibility.put(ActeVisibilityConstants.INDEXATION, visibilities[12]);
            acteVisibility.put(ActeVisibilityConstants.TRANSPOSITION_APPLICATION, visibilities[13]);
            acteVisibility.put(ActeVisibilityConstants.PARUTION, visibilities[14]);
            acteVisibility.put(ActeVisibilityConstants.JORF, visibilities[15]);
            acteVisibility.put(ActeVisibilityConstants.PARUTION_BO, visibilities[16]);
            acteVisibility.put(ActeVisibilityConstants.SGG_DILA, visibilities[17]);

            acteVisibility.put(ActeVisibilityConstants.PERIODICITE_RAPPORT, visibilities[18]);
            acteVisibility.put(ActeVisibilityConstants.CATEGORY_ACTE, visibilities[19]);

            acteVisibility.put(ActeVisibilityConstants.PUBLICATION_RAPPORT_PRESENTATION, visibilities[20]);
        }
        actesVisibilities.put(typeActe, acteVisibility);
    }

    @Test
    public void testGetLoiList() {
        Set<String> listLois = acteService.getLoiList();

        Assert.assertEquals(actesLoi.size(), listLois.size());
        for (String acte : listLois) {
            Assert.assertTrue(acte + " should be true", actesLoi.contains(acte));
        }
    }

    @Test
    public void testGetActe() {
        for (TypeActe acte : actes.values()) {
            TypeActe acteGet = acteService.getActe(acte.getId());
            Assert.assertNotNull(acteGet);
            Assert.assertEquals(acte, acteGet);
        }
    }

    @Test
    public void testFindAll() {
        Collection<TypeActe> allActes = acteService.findAll();
        Assert.assertEquals(actes.values().size(), allActes.size());
        for (TypeActe acteGet : allActes) {
            TypeActe acte = actes.get(acteGet.getId());
            Assert.assertNotNull(acte);
            Assert.assertEquals(acte, acteGet);
        }
    }

    @Test
    public void testGetActeByLabel() {
        for (TypeActe acte : actes.values()) {
            TypeActe acteGet = acteService.getActeByLabel(acte.getLabel());
            Assert.assertNotNull(acteGet);
            Assert.assertEquals(acte, acteGet);
        }
    }

    @Test
    public void testIsLoi() {
        for (TypeActe typeActe : actes.values()) {
            if (actesLoi.contains(typeActe.getId())) {
                Assert.assertTrue(typeActe.getId() + " should be true", acteService.isLoi(typeActe.getId()));
            } else {
                Assert.assertFalse(typeActe.getId() + " should be false", acteService.isLoi(typeActe.getId()));
            }
        }
    }

    @Test
    public void testIsOrdonnance() {
        for (TypeActe typeActe : actes.values()) {
            if (actesOrdonnance.contains(typeActe.getId())) {
                Assert.assertTrue(typeActe.getId() + " should be true", acteService.isOrdonnance(typeActe.getId()));
            } else {
                Assert.assertFalse(typeActe.getId() + " should be false", acteService.isOrdonnance(typeActe.getId()));
            }
        }
    }

    @Test
    public void testIsArrete() {
        Set<String> arretes = new HashSet<>();
        arretes.addAll(actesArrete);
        arretes.add(TypeActeConstants.TYPE_ACTE_ARRETE_CE);
        for (TypeActe typeActe : actes.values()) {
            if (arretes.contains(typeActe.getId())) {
                Assert.assertTrue(typeActe.getId() + " should be true", acteService.isArrete(typeActe.getId()));
            } else {
                Assert.assertFalse(typeActe.getId() + " should be false", acteService.isArrete(typeActe.getId()));
            }
        }
    }

    @Test
    public void testIsDecret() {
        for (TypeActe typeActe : actes.values()) {
            if (actesDecret.contains(typeActe.getId())) {
                Assert.assertTrue(typeActe.getId() + " should be true", acteService.isDecret(typeActe.getId()));
            } else {
                Assert.assertFalse(typeActe.getId() + " should be false", acteService.isDecret(typeActe.getId()));
            }
        }
    }

    @Test
    public void testIsNonReglementaire() {
        // Actes non réglementaires
        Set<String> nonReglementaire = new HashSet<>();
        nonReglementaire.add(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND);
        nonReglementaire.add(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND);
        nonReglementaire.add(TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND);
        nonReglementaire.add(TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND);
        nonReglementaire.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND);
        nonReglementaire.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND);
        nonReglementaire.add(TypeActeConstants.TYPE_ACTE_DECRET_CM_IND);
        nonReglementaire.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND);
        nonReglementaire.add(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND);

        for (TypeActe typeActe : actes.values()) {
            if (nonReglementaire.contains(typeActe.getId())) {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.isNonReglementaire(typeActe.getId())
                );
            } else {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.isNonReglementaire(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testIsRectificatif() {
        for (TypeActe typeActe : actes.values()) {
            if (actesRectificatif.contains(typeActe.getId())) {
                Assert.assertTrue(typeActe.getId() + " should be true", acteService.isRectificatif(typeActe.getId()));
            } else {
                Assert.assertFalse(typeActe.getId() + " should be false", acteService.isRectificatif(typeActe.getId()));
            }
        }
    }

    @Test
    public void testGetTypeActeListExtendFolderFondDeDossier() {
        Set<String> actesGet = acteService.getTypeActeListExtendFolderFondDeDossier();
        Assert.assertEquals(typesActesExtendFDD.size(), actesGet.size());
        for (String acte : typesActesExtendFDD) {
            actesGet.contains(acte);
        }
    }

    @Test
    public void testHasTypeActeExtendedFolder() {
        for (TypeActe typeActe : actes.values()) {
            if (typesActesExtendFDD.contains(typeActe.getId())) {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.hasTypeActeExtendedFolder(typeActe.getId())
                );
            } else {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.hasTypeActeExtendedFolder(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testGetTypeActeListExtendFormatParapheur() {
        Set<String> actesGet = acteService.getTypeActeListExtendFormatParapheur();
        Assert.assertEquals(typesActesExtendParaph.size(), actesGet.size());
        for (String acte : typesActesExtendParaph) {
            actesGet.contains(acte);
        }
    }

    @Test
    public void testHasTypeActeExtendedFormat() {
        for (TypeActe typeActe : actes.values()) {
            if (typesActesExtendParaph.contains(typeActe.getId())) {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.hasTypeActeExtendedFormat(typeActe.getId())
                );
            } else {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.hasTypeActeExtendedFormat(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testHasExtraitObligatoire() {
        // Actes parapheur extrait obligatoire
        Set<String> typeActeListExtraitObligatoire = new HashSet<>();
        typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND);
        typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND);
        typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND);
        typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND);
        typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND);
        typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND);
        typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_DECRET_CM_IND);
        typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND);
        typeActeListExtraitObligatoire.add(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND);
        for (TypeActe typeActe : actes.values()) {
            if (typeActeListExtraitObligatoire.contains(typeActe.getId())) {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.hasExtraitObligatoire(typeActe.getId())
                );
            } else {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.hasExtraitObligatoire(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testHasTypeActeMesureNominative() {
        for (TypeActe typeActe : actes.values()) {
            if (actesNominatif.contains(typeActe.getId())) {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.hasTypeActeMesureNominative(typeActe.getId())
                );
            } else {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.hasTypeActeMesureNominative(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testIsVisibleCE() {
        // Actes visibles CE
        Set<String> typesActesVisiblesCE = new HashSet<>();
        typesActesVisiblesCE.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37);
        typesActesVisiblesCE.add(TypeActeConstants.TYPE_ACTE_DECRET_CE);
        typesActesVisiblesCE.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_CE);
        typesActesVisiblesCE.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM);
        typesActesVisiblesCE.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND);
        typesActesVisiblesCE.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND);
        typesActesVisiblesCE.add(TypeActeConstants.TYPE_ACTE_DEMANDE_AVIS_CE);
        typesActesVisiblesCE.add(TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION);
        typesActesVisiblesCE.add(TypeActeConstants.TYPE_ACTE_ORDONNANCE);
        typesActesVisiblesCE.add(TypeActeConstants.TYPE_ACTE_ARRETE_CE);
        typesActesVisiblesCE.addAll(actesLoi);

        for (TypeActe typeActe : actes.values()) {
            if (typesActesVisiblesCE.contains(typeActe.getId())) {
                Assert.assertTrue(typeActe.getId() + " should be true", acteService.isVisibleCE(typeActe.getId()));
            } else {
                Assert.assertFalse(typeActe.getId() + " should be false", acteService.isVisibleCE(typeActe.getId()));
            }
        }
    }

    @Test
    public void testIsVisibleBaseLegale() {
        for (TypeActe typeActe : actes.values()) {
            if (actesRectificatif.contains(typeActe.getId())) {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.isVisibleBaseLegale(typeActe.getId())
                );
            } else {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.isVisibleBaseLegale(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testIsVisibleApplicationLoi() {
        for (TypeActe typeActe : actes.values()) {
            if (actesDecret.contains(typeActe.getId())) {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.isVisibleApplicationLoi(typeActe.getId())
                );
            } else {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.isVisibleApplicationLoi(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testIsVisibleTranspositionOrdonnance() {
        for (TypeActe typeActe : actes.values()) {
            if (actesDecret.contains(typeActe.getId())) {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.isVisibleApplicationLoi(typeActe.getId())
                );
            } else {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.isVisibleApplicationLoi(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testIsVisibleHabilitation() {
        for (TypeActe typeActe : actes.values()) {
            if (actesOrdonnance.contains(typeActe.getId())) {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.isVisibleHabilitation(typeActe.getId())
                );
            } else {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.isVisibleHabilitation(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testIsVisibleTranspositionDirective() {
        // Actes visibles transpositions directives
        Set<String> typesActesVisibleTranspositionDirective = new HashSet<>();
        typesActesVisibleTranspositionDirective.add(TypeActeConstants.TYPE_ACTE_ORDONNANCE);
        typesActesVisibleTranspositionDirective.addAll(actesDecret);
        typesActesVisibleTranspositionDirective.addAll(actesArrete);
        typesActesVisibleTranspositionDirective.addAll(actesLoi);
        typesActesVisibleTranspositionDirective.add(TypeActeConstants.TYPE_ACTE_TEXTE_NON_PUBLIE);

        for (TypeActe typeActe : actes.values()) {
            if (typesActesVisibleTranspositionDirective.contains(typeActe.getId())) {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.isVisibleTranspositionDirective(typeActe.getId())
                );
            } else {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.isVisibleTranspositionDirective(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testIsVisiblePublicationRapport() {
        // Actes visibles publication rapport
        Set<String> typesActesVisiblePublicationRapport = new HashSet<>();
        typesActesVisiblePublicationRapport.add(TypeActeConstants.TYPE_ACTE_ORDONNANCE);
        typesActesVisiblePublicationRapport.addAll(actesDecret);

        for (TypeActe typeActe : actes.values()) {
            if (typesActesVisiblePublicationRapport.contains(typeActe.getId())) {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.isVisiblePublicationRapport(typeActe.getId())
                );
            } else {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.isVisiblePublicationRapport(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testIsVisiblePublicationIntegrale() {
        // Actes visibles publication intégrale
        Set<String> typesActesNonVisiblePublicationIntegrale = new HashSet<>();
        typesActesNonVisiblePublicationIntegrale.add(TypeActeConstants.TYPE_ACTE_NOTE);
        typesActesNonVisiblePublicationIntegrale.add(TypeActeConstants.TYPE_ACTE_TABLEAU);
        typesActesNonVisiblePublicationIntegrale.add(TypeActeConstants.TYPE_ACTE_AMNISTIE);
        typesActesNonVisiblePublicationIntegrale.add(TypeActeConstants.TYPE_ACTE_TEXTE_NON_PUBLIE);
        typesActesNonVisiblePublicationIntegrale.addAll(actesLoi);

        for (TypeActe typeActe : actes.values()) {
            if (typesActesNonVisiblePublicationIntegrale.contains(typeActe.getId())) {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.isVisiblePublicationIntegrale(typeActe.getId())
                );
            } else {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.isVisiblePublicationIntegrale(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testIsVisibleDecretNumerote() {
        for (TypeActe typeActe : actes.values()) {
            if (actesDecret.contains(typeActe.getId())) {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.isVisibleDecretNumerote(typeActe.getId())
                );
            } else {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.isVisibleDecretNumerote(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testIsVisibleNumeroTexteJO() {
        // Actes visibles numéro JO
        Set<String> typesActesVisibleNumeroJO = new HashSet<>();
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_CIRCULAIRE);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_CITATION);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_DIVERS);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_EXEQUATUR);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_INSTRUCTION);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_NOTE);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_ORDONNANCE);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_RECTIFICATIF);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_LOI);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_DECRET_CE);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_DECRET_CM);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_DECRET_DE_PUBLICATION_DE_TRAITE_OU_ACCORD);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_DECRET_PR);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_IND);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_DECRET_CM_IND);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_CE);
        typesActesVisibleNumeroJO.add(TypeActeConstants.TYPE_ACTE_ACCORD_COLLECTIF_PUBLIC);

        for (TypeActe typeActe : actes.values()) {
            if (typesActesVisibleNumeroJO.contains(typeActe.getId())) {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.isVisibleNumeroTexteJO(typeActe.getId())
                );
            } else {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.isVisibleNumeroTexteJO(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testHasInjectionType() {
        for (TypeActe typeActe : actes.values()) {
            if (TypeActeConstants.TYPE_ACTE_INJECTION.equals(typeActe.getId())) {
                Assert.assertTrue(typeActe.getId() + " should be true", acteService.hasInjectionType(typeActe.getId()));
            } else {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.hasInjectionType(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testGetAllTypeActe() {
        List<TypeActe> typeActeList = acteService.getAllTypeActe();
        List<TypeActe> typesActesTest = new ArrayList<>(actes.values());
        // tri par ordre par label
        Collections.sort(typesActesTest);

        Assert.assertEquals(typesActesTest.size(), typeActeList.size());
        for (int i = 0; i < typesActesTest.size(); i++) {
            Assert.assertEquals(typesActesTest.get(i), typeActeList.get(i));
        }
    }

    @Test
    public void testIsDecretPR() {
        // Actes des decrets PR
        Set<String> actesDecretPR = new HashSet<>();
        actesDecretPR.add(TypeActeConstants.TYPE_ACTE_DECRET_PR);
        actesDecretPR.add(TypeActeConstants.TYPE_ACTE_DECRET_PR_IND);

        for (TypeActe typeActe : actes.values()) {
            if (actesDecretPR.contains(typeActe.getId())) {
                Assert.assertTrue(typeActe.getId() + " should be true", acteService.isDecretPR(typeActe.getId()));
            } else {
                Assert.assertFalse(typeActe.getId() + " should be false", acteService.isDecretPR(typeActe.getId()));
            }
        }
    }

    @Test
    public void testIsRapportAuParlement() {
        for (TypeActe typeActe : actes.values()) {
            if (TypeActeConstants.TYPE_ACTE_RAPPORT_AU_PARLEMENT.equals(typeActe.getId())) {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.isRapportAuParlement(typeActe.getId())
                );
            } else {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.isRapportAuParlement(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testIsPublicationVisible() {
        for (TypeActe typeActe : actes.values()) {
            if (typesActesPublicationNonVisible.contains(typeActe.getId())) {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.isPublicationVisible(typeActe.getId())
                );
            } else {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.isPublicationVisible(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testIsSggDilaVisible() {
        for (TypeActe typeActe : actes.values()) {
            if (typesActesPublicationNonVisible.contains(typeActe.getId())) {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.isPublicationVisible(typeActe.getId())
                );
            } else {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.isPublicationVisible(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testIsParutionVisible() {
        for (TypeActe typeActe : actes.values()) {
            if (typesActesPublicationNonVisible.contains(typeActe.getId())) {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.isPublicationVisible(typeActe.getId())
                );
            } else {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.isPublicationVisible(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testIsJorfVisible() {
        for (TypeActe typeActe : actes.values()) {
            if (typesActesPublicationNonVisible.contains(typeActe.getId())) {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.isPublicationVisible(typeActe.getId())
                );
            } else {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.isPublicationVisible(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testIsActeTexteNonPublie() {
        for (TypeActe typeActe : actes.values()) {
            if (TypeActeConstants.TYPE_ACTE_TEXTE_NON_PUBLIE.equals(typeActe.getId())) {
                Assert.assertTrue(
                    typeActe.getId() + " should be true",
                    acteService.isActeTexteNonPublie(typeActe.getId())
                );
            } else {
                Assert.assertFalse(
                    typeActe.getId() + " should be false",
                    acteService.isActeTexteNonPublie(typeActe.getId())
                );
            }
        }
    }

    @Test
    public void testGetActeVisibility() {
        // Actes visibilités
        Map<String, Map<String, Boolean>> actesVisibilities = new HashMap<>();
        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_AMNISTIE,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_ARRETE_CE,
            true,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_ARRETE_INTERMINISTERIEL_IND,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_ARRETE_MINISTERIEL_IND,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_ARRETE_PM,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_ARRETE_PM_IND,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_ARRETE_PR,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_ARRETE_PR_IND,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_AVENANT,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_AVIS,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_CIRCULAIRE,
            false,
            true,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_CITATION,
            false,
            true,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_COMMUNIQUE,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_CONVENTION,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_DECISION,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_DECRET_CE,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            true
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_DECRET_CE_ART_37,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            true
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_DECRET_CE_CM,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            true
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_DECRET_CE_CM_IND,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            true
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_DECRET_DE_PUBLICATION_DE_TRAITE_OU_ACCORD,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            true
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_DECRET_PR,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            true
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_DECRET_PR_CE,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            true
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_DECRET_PR_IND,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            true
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            true
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_DECRET_SIMPLE_IND,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            true
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_DELIBERATION,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_DEMANDE_AVIS_CE,
            true,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_DIVERS,
            false,
            true,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_EXEQUATUR,
            false,
            true,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_INJECTION,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_INSTRUCTION,
            false,
            true,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_LISTE,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_LOI,
            true,
            true,
            false,
            false,
            false,
            true,
            false,
            false,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_LOI_ART_53_CONSTITUTION,
            true,
            true,
            false,
            false,
            false,
            true,
            false,
            false,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_LOI_CONSTITUTIONNELLE,
            true,
            true,
            false,
            false,
            false,
            true,
            false,
            false,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_LOI_ORGANIQUE,
            true,
            true,
            false,
            false,
            false,
            true,
            false,
            false,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_NOTE,
            false,
            true,
            false,
            false,
            false,
            true,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_ORDONNANCE,
            true,
            true,
            true,
            true,
            false,
            true,
            false,
            true,
            true,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            true
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_RAPPORT,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_RAPPORT_AU_PARLEMENT,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_RECTIFICATIF,
            false,
            false,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_TABLEAU,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_TACHE_GENERIQUE,
            false,
            true,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            false,
            false,
            false
        );

        addVisibiliteActe(
            actesVisibilities,
            TypeActeConstants.TYPE_ACTE_TEXTE_NON_PUBLIE,
            false,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            true,
            false,
            true,
            false,
            true,
            true,
            false,
            false,
            false,
            false,
            false,
            false,
            false
        );

        for (Entry<String, Map<String, Boolean>> entry : actesVisibilities.entrySet()) {
            Map<String, Boolean> visibilityGet = acteService.getActeVisibility(entry.getKey());
            for (Entry<String, Boolean> entryVisibility : entry.getValue().entrySet()) {
                Assert.assertEquals(
                    "Should be " +
                    entryVisibility.getValue() +
                    " for " +
                    entryVisibility.getKey() +
                    " of acte " +
                    entry.getKey(),
                    entryVisibility.getValue(),
                    visibilityGet.get(entryVisibility.getKey())
                );
            }
        }
    }
}
