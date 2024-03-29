package fr.dila.solonepg.core.operation.livraison;

import com.google.common.collect.ImmutableList;
import fr.dila.cm.cases.CaseConstants;
import fr.dila.solonepg.api.service.SolonEpgUserManager;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.operation.STVersion;
import fr.dila.st.core.service.STServiceLocator;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoException;

@Operation(
    id = EpgDeleteUsersOperation.ID,
    category = CaseConstants.CASE_MANAGEMENT_OPERATION_CATEGORY,
    label = "Supprime des utilisateurs inutilisés",
    description = EpgDeleteUsersOperation.DESCRIPTION
)
@STVersion(version = "4.0.0")
public class EpgDeleteUsersOperation {
    public static final String ID = "SolonEpg.Livraison.Delete.Utilisateurs";

    private static final STLogger LOGGER = STLogFactory.getLog(EpgDeleteUsersOperation.class);

    public static final String DESCRIPTION = "Cette opération supprime des utilisateurs inutilisés";

    @Context
    protected OperationContext context;

    @OperationMethod
    public void deleteUsers() {
        if (!context.getPrincipal().isAdministrator()) {
            throw new NuxeoException(
                "Seul un administrateur Nuxeo peux supprimer des utilisateurs",
                HttpServletResponse.SC_FORBIDDEN
            );
        }
        LOGGER.info(STLogEnumImpl.DEFAULT, "Début de l'opération de suppression d'utilisateurs");

        CoreSession session = context.getCoreSession();

        getUserIds().forEach(user -> deleteUser(session, user));

        LOGGER.info(STLogEnumImpl.DEFAULT, "Fin de l'opération de suppression d'utilisateurs");
    }

    protected void deleteUser(@NotNull CoreSession session, String username) {
        SolonEpgUserManager userManager = (SolonEpgUserManager) STServiceLocator.getUserManager();
        DocumentModel userDoc = userManager.getUserModel(username);

        if (userDoc != null && userManager.canDeleteUser(session, username)) {
            userManager.deleteUser(userDoc);
            userManager.deleteUserConsultation(session, userDoc);
            LOGGER.info(STLogEnumImpl.DEL_USER_TEC, "Utilisateur supprimé : " + username);
        }
    }

    protected List<String> getUserIds() {
        return ImmutableList.of(
            "ACBAZEROLLE",
            "BDEMONTBRON",
            "BGASSEMY",
            "BHERBRETEAU",
            "CLENGRAND",
            "DELPHINEPOSIN",
            "DLARAIRIE",
            "EBLAZQUEZ",
            "ERICDUMOND",
            "FPUIGSERVER",
            "MBEUCHARD",
            "ODANIAUD",
            "PCHESNOT",
            "SMONTEIL",
            "SOPHIEFERAH",
            "TPEREIRA",
            "VERONIQUEJUILLARD",
            "mjdacosta",
            "fjuttin-adc",
            "BDERYROSOT",
            "ALANHUET",
            "GLEFEBVRE",
            "ccarpentier-adc",
            "cgavaud-adc",
            "TSTAROZYNSKI",
            "LaurentSTIRNEMANN",
            "MaudPlanchenault",
            "PatriciaPEREZ",
            "PierreHUREAUX",
            "ANPARRALO",
            "ARDESCHAMPS",
            "BLASSERRE",
            "EMDUBOIS",
            "FRCLAUDE",
            "JLEMAITRE",
            "MCDAMAS",
            "NPLANCHETTE",
            "RELHAFA",
            "TBRETON",
            "adumont",
            "bastiennonque",
            "cllefebvre",
            "fbaptiste",
            "fbenkimoun",
            "IBOH",
            "jbdarracq",
            "jcbedague",
            "lucile.dubernard",
            "nalbinportier",
            "SOTAHIRI",
            "AORFANOS",
            "elisseck",
            "ABADOUARD",
            "ABAILLEUL",
            "ABERTRAND",
            "ABOUISSOU",
            "ACLANTHEAUME",
            "ADECAMBIAIRE",
            "AGUILLEMOT",
            "AJUNG",
            "AKAVAJ",
            "AKERAUDREN",
            "ALELIEPAULT",
            "ALEMENIS",
            "ALMENU",
            "ALOUZONDEJONG",
            "AMLALAMI",
            "ANTIKARA",
            "APINARDEAU",
            "AUDMILON",
            "AUDPALCY",
            "AVONICOL",
            "BERNPAUL",
            "BRODRIGUES",
            "CANDIAYE",
            "CARLOUIS",
            "CARMENDY",
            "CAROLEASSI",
            "CAUTOCUS",
            "CCAMPEON",
            "CCESTO",
            "CCHABRIER",
            "CCHEVALERIAS",
            "CCOUTANT",
            "CDANIELOU",
            "CECRAFAT",
            "CELESGIL",
            "CFROMAIN",
            "CHDUBOIS",
            "CHRAMONT",
            "CHRISAMAT",
            "CHRPAYEN",
            "CKUCHARSKI",
            "CLAIREADAM",
            "CLECAMUS",
            "CPFAUVADEL",
            "CPISARZ",
            "CSCHOPPHOVEN",
            "CVOLANTE",
            "DAJSGMPIN",
            "DBONDON",
            "DCHAYRIGUES",
            "DELAUNAY",
            "DGALLOYER",
            "DGUILLON",
            "EBARESARRU",
            "EDELANVERSIN",
            "EDMRINUY",
            "EGALLOUET",
            "EHALLUIN",
            "EISSERMANN",
            "EJORETGALAIS",
            "ELEBLOND",
            "ELEPOIVRE",
            "EMAISONNAT",
            "EMMAYOUT",
            "EPHILIPP",
            "ERQUINIO",
            "EWERMELINGER",
            "FABOUNET",
            "FAMCHIN",
            "FDELORME",
            "FLASSOT",
            "FLEGRAIN",
            "FLOCATELLI",
            "FLORBIAS",
            "FLPINEAUD",
            "FMAZZOLENI",
            "FMEHDEB",
            "FRCARBON",
            "FRESSEL",
            "FRRICLES",
            "FSIGOBINE",
            "GCOURTEMANCHE",
            "GHODOUL",
            "GMARTINE",
            "GVIRIEUX",
            "HABIAYAD",
            "HBERTHELE",
            "HEHERBER",
            "HERMENIER",
            "HRUBIETTO",
            "HYAHYAOUI",
            "ISHEBRAS",
            "JBREZILLON",
            "JIMMBRUN",
            "JJRICHARD",
            "JLEJELOUX",
            "JLEPLANOIS",
            "JPELLETANGE",
            "JUROUDET",
            "JVANHECKE",
            "KANOUVEL",
            "KEVLOPEZ",
            "KHDJITTE",
            "KVIZY",
            "LAUBONIN",
            "LBECQUECORCOS",
            "LDANIELDITANDRIEU",
            "LDEMICHELI",
            "LDJENNADI",
            "LFRESSYNET",
            "LGAUCHET",
            "LHAUDUROY",
            "LKHELLADI",
            "LTSCHESNO",
            "LURACLET",
            "MABANBUCK",
            "MABOUVIER",
            "MACOPPRY",
            "MAGNERET",
            "MANDREADAKIS",
            "MATHOUMY",
            "MBERNARDI",
            "MBESANCON",
            "MBLIRANDO",
            "MCFARINEAUX",
            "MDUHALDE",
            "MEBECQUET",
            "MHABASQUE",
            "MHAEFFELE",
            "MLATCHIMY",
            "MPAPINUTTI",
            "MPGASTON",
            "MPTISSOTPOLI",
            "NABILAMIRI",
            "NADKLEIN",
            "NGILAD",
            "NMARQUEFAVE",
            "NMICHELDITLABOELLE",
            "OBOISSON",
            "OLIVGRAS",
            "PASOFFRET",
            "PDEVILLE",
            "PEFEIGNA",
            "PHAURADE",
            "PIEBOQUEL",
            "PLACROIX",
            "PMETIVIER",
            "PREBUFFO",
            "PTOITOT",
            "REDEBOTH",
            "RLANCELOT",
            "RSAUGNAC",
            "SABOUADI",
            "SBAUDET",
            "SBRIQUET",
            "SDESCHAMPS",
            "SDUFOURG",
            "SELGERAUT",
            "SFLAHAUT",
            "SFROMENT",
            "SGIRAUDINEAU",
            "SGOUPIL",
            "SHOLLIER",
            "SKAMAROPOULOS",
            "SREMORINI",
            "STHOMASBREBOU",
            "STURBIER",
            "SYFERNANDES",
            "THAINAUT",
            "THIBSPOR",
            "TPETROVA",
            "TVRATNIK",
            "VANTIPHONAUBANELLE",
            "VGOUGAUDVILLE",
            "VIDUBAIL",
            "VIGIE-DAJ",
            "VJERKHMOUM",
            "VRATHOUIS",
            "YALEXIS-VICAIRE",
            "YDUCLERE",
            "YOTHOMAS",
            "alagetgr-adc",
            "jdenat-adc",
            "jpetitni-adc",
            "JeanmarcLESCURE",
            "JulieDELAIDDE",
            "abilloue-adc",
            "aternoveanudeladessa-adc",
            "awolff-adc",
            "bcasseron-adc",
            "cabgrall-adc",
            "cchampenois-adc",
            "cdelorme-adc",
            "cdeshayes-adc",
            "cflamme-adc",
            "clegendre-adc",
            "dbaratto-adc",
            "dgiry-adc",
            "estrohl-adc",
            "fruzgar-adc",
            "gbarreto-adc",
            "ggestin-adc",
            "jmarfond-adc",
            "jsilvagnoli-adc",
            "ksamjee-adc",
            "lclemente-adc",
            "lderue-adc",
            "lpuvis-adc",
            "mdasque-adc",
            "mheulot-adc",
            "nmahuzier-adc",
            "pfavaretto-adc",
            "pleclerc-adc",
            "pmichel-adc",
            "sdenjeanmichard-adc",
            "shascoet-adc",
            "smalepert-adc",
            "solive-adc",
            "tdeniz-adc",
            "vlaudouar-adc",
            "vmarthos-adc",
            "adelaunay-adc",
            "aguenaoui-adc",
            "amachover-adc",
            "anavarro-adc",
            "aploujoux-adc",
            "asalvini-adc",
            "cchretien-adc",
            "cdulude-adc",
            "elarge-adc",
            "esalvi-adc",
            "everdure",
            "fgleizes-adc",
            "fmonfroy-adc",
            "hrolland-adc",
            "jgiglione-adc",
            "ksailly-adc",
            "mbaldin-adc",
            "mlandais-adc",
            "nlopez-adc",
            "pbeauvois",
            "pderomanet-adc",
            "pfrehaut-adc",
            "pmerle-adc",
            "rlelte-adc",
            "sbertrand-adc",
            "ternoult-adc",
            "tsiproudhis-adc",
            "vcluzel-adc",
            "blacomme",
            "cathgond",
            "CONTRIBCSA",
            "hebergementDILA",
            "rotrepos",
            "ABALUTCH",
            "AlineNDONGO",
            "AMOCOEUR",
            "BLAUTARD",
            "CATHERINEHYVER",
            "CATHERINESCHMIDT",
            "CAUBARRERE",
            "CELINEBALAN",
            "CELINEGIUSTI",
            "DJINADASA",
            "DominiqueMENEZ",
            "JARDILLIER",
            "KDIDELOT",
            "LIANAGOCAN",
            "LUCILECARREZ",
            "MADIONIS",
            "MarcPELTOT",
            "MHARBOUI",
            "MJDECOQUEREAUMONT",
            "MLGRONDIN",
            "NATHANROBERT",
            "NHOSSARD",
            "PatrickCOMOY",
            "PGUITEAU",
            "SCROUZIER",
            "SGANACHAUD",
            "StephaniePUJADE",
            "SYLVIELECARO",
            "THOMASLOIRAT",
            "URBIETA-MARTIN",
            "VHOMMERIL",
            "YANNICKMEVEL",
            "ANGANKOU",
            "ampasco-adc",
            "vmalfant-adc",
            "adelinechaumont",
            "annepiguet",
            "AVANDESTOC",
            "CATHLAMAIN",
            "CEGOUJON",
            "CFICHEUX",
            "cfrancois",
            "CGRAZIANI",
            "CHABANNE",
            "CHARLOTTECLERTE",
            "chdaroux",
            "DBELASCAIN",
            "dbienaime",
            "EDUBOISDIMACARIO",
            "FADIAFRIH",
            "FCOMA",
            "IKORTIAN",
            "ISABELLEOGER",
            "ISAICORD",
            "JCLEFEBVRE",
            "JLUCREZIA",
            "JULIEBOUDON",
            "JUSTINEFERTE",
            "ksauteret",
            "LCAUVINSELVES",
            "LMMESURE",
            "LRIBEIRO",
            "lsalvigni",
            "M-A.GOBA",
            "MARIANNEALLEN",
            "MaryleneIANNASCOLI",
            "M-FBAUER",
            "mhuguet",
            "mpatte-samama",
            "MPBESSONE",
            "MROUSSEAUX",
            "nchalumeau",
            "romainizoird",
            "sdolleman",
            "SKADDOURBEY",
            "srascar",
            "VALBREUIL",
            "MLVINCENT",
            "NATDJANI",
            "SCHAPIRO",
            "STAICLET",
            "AELOUALI",
            "CHENNION",
            "DPHALIPPOUX",
            "LUCIEBLON",
            "MCDEROUET",
            "MFCATONI",
            "NBENHARZALLAH",
            "NLATOUCHE",
            "PTRABELSI",
            "achersouly",
            "bamzil-adc",
            "bjoly-adc",
            "cborie-adc",
            "cdufour-adc",
            "cmalbrouck-adc",
            "cnavatte-adc",
            "ctanguy-adc",
            "cvecchio-adc",
            "edelplanque-adc",
            "empetit-adc",
            "eranuccini-adc",
            "etheurier-adc",
            "gdelachapelle-adc",
            "gpapastephanakis-adc",
            "hlunion-adc",
            "jdumas-adc",
            "kbenali-kerroumi-adc",
            "mckerambellec-adc",
            "mlebreton-adc",
            "mperier-adc",
            "ogrollet-adc",
            "paudoin-adc",
            "pvanessche-adc",
            "sblenski-adc",
            "sdouamin-adc",
            "SMAHEU",
            "smorin-adc",
            "svignaud-adc",
            "LucilleCHAMPENOIS",
            "PascaleBOUCLIER",
            "VincentCAPUTO",
            "lalepape",
            "ALEBLANC",
            "AUMOREAU",
            "JUSFABRE",
            "MECOUFFI",
            "OUGILLET",
            "AurelieBochard",
            "JeannieGUENO",
            "CelineLANNES",
            "AlexandraLEFEBVRE",
            "MarionCHINCHILLA",
            "ValerieBoutheon",
            "lrouviere-adc",
            "ANELEMVA",
            "AUMALNUIT",
            "BRIMAUREL",
            "CarlaSANTOS",
            "CATGAUTIER",
            "delchavagne",
            "emmaguyot",
            "ERICHARD",
            "FCHARLOTTIN",
            "francksaillard",
            "gcostier",
            "LauraGOMIS",
            "LAURMENA",
            "MarieDominiqueCAZIERLOMBARD",
            "MATGARNESSON",
            "Pierre-YvesCABAL",
            "RidhaBACCARI",
            "ROMALEAL",
            "SABMOJON",
            "PGAILLARD",
            "stephsendra",
            "vdroulle",
            "CALAPOIX",
            "CAMARTIN",
            "djambrun",
            "HDJEBALI",
            "JACQUEMIN",
            "NASEPREZ",
            "SYLGOTTI",
            "ACHAMPION",
            "AGAGNAIRE",
            "ANIJABRI",
            "BOYERCEL",
            "CELIANEEL",
            "CHAUVETC",
            "CLAMBLIN",
            "DAMBRACH",
            "EDLAUNAY",
            "EGRONDIN",
            "EMACIEJEWSKI",
            "FLCOLNET",
            "FTHEODORE",
            "HPLAILLY",
            "IHENRY",
            "JBGALVIN",
            "JMBERNIGAUD",
            "JMHUET",
            "JRAVENNE",
            "JSENECHAL",
            "KEMORTIER",
            "LAUWERLI",
            "LVAGNIER",
            "LVASSEUR",
            "LVIGNE",
            "MAEPORLIER",
            "MARIPIET",
            "MATESTUD",
            "MBENAKLI",
            "MBERNARD",
            "MGARNIER",
            "MVUILLAUME",
            "PAUBAUDRY",
            "PCAFABRE",
            "PGMARCHAND",
            "RKERVELLA",
            "RMACHIRE",
            "SCASSIUS",
            "SCHEVTCHENKO",
            "SHABBOUCHI",
            "SOREYNES",
            "TBOUZEMBRAK",
            "VAMANIER",
            "ChristopheCARLUS",
            "VirginieCHAUMONT",
            "CharlotteFalize",
            "FlorianDellizotti",
            "IsaureDestaintot",
            "LGlikson",
            "jmdeguerdavid",
            "phmechet-adc",
            "mcharreau",
            "amelie-jonnet",
            "BenjaminGOUESLARD",
            "BenoitDURIEUX",
            "CarolePUIGCHEVRIER",
            "CASTIER",
            "CharlesLEMORE",
            "ChristianFALCONNET",
            "EliseADEVAHPOEUF",
            "Elodieelhaddad",
            "Ericjalon",
            "fhermite",
            "FRPETIT",
            "JeromePHILIPPON",
            "MLANDAIS",
            "NicoleCIVATTE",
            "Paolabergs",
            "PatrickGONET",
            "pntambani",
            "Yanngandriau",
            "COLRHINAN",
            "EDUCROCQ",
            "GSEROUSSI",
            "LMontantin",
            "odgreaud",
            "pbreton",
            "SROBERTGRANELL",
            "ALACROIX",
            "cdupuits",
            "CMASCOTO",
            "COMATHIEU",
            "CRAVOUNA",
            "FAWENGER",
            "GUICHERON",
            "kdenepoux",
            "LAVILLAUROY",
            "MACULGIA",
            "mbonheur",
            "nroyer",
            "pgarnier",
            "PMERCIER",
            "CYRPERIE",
            "ClemenceOLSINA",
            "JoannaHOTTIAUX",
            "MLORIN",
            "VFOULARD",
            "Catherinenaitsaada",
            "edouardcrepey",
            "ejullien",
            "ctagliana",
            "EMUNGANGA",
            "LASKOWSKI",
            "MCBOCAGE",
            "ALBIZEUL",
            "BENJREDT",
            "FLOPICOT",
            "GDEBELLE",
            "LUCIENNEDEJEAN",
            "MOULINAL",
            "SMARCATO"
        );
    }
}
