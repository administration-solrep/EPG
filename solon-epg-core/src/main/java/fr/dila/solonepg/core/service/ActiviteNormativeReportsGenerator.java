package fr.dila.solonepg.core.service;

import fr.dila.solon.birt.common.BirtOutputFormat;
import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.birt.ExportPANStat;
import fr.dila.solonepg.api.constant.ActiviteNormativeStatsConstants;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.api.service.ActiviteNormativeService;
import fr.dila.ss.core.service.SSServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.sword.naiad.nuxeo.commons.core.util.SessionUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.security.auth.login.LoginContext;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.nuxeo.ecm.core.api.CloseableCoreSession;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.runtime.api.Framework;

public class ActiviteNormativeReportsGenerator {
    protected Map<String, Serializable> inputValues;
    protected String user;
    private String tempDirectoryPath;
    private File tempDirectory;
    private final String resultFileName;
    protected List<EntiteNode> currentMinisteres;
    protected ExportPANStat adapter;
    protected CoreSession session;
    public static final String MAIN_DIRECTORY = "/AnStatsReports/";
    private static final STLogger LOG = STLogFactory.getLog(ActiviteNormativeReportsGenerator.class);

    public ActiviteNormativeReportsGenerator(
        Map<String, Serializable> inputValues,
        String user,
        ExportPANStat adapter,
        CoreSession session,
        String resultFileName
    ) {
        this.inputValues = inputValues;
        this.user = user;
        this.adapter = adapter;
        this.session = session;
        this.resultFileName = resultFileName;
        this.init();
    }

    private void compressFiles(List<File> files, File file) {
        try {
            FileOutputStream fos;

            fos = new FileOutputStream(new File(file.getCanonicalPath() + ".zip"));

            ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(new BufferedOutputStream(fos));
            zaos.setEncoding("Cp437");
            zaos.setFallbackToUTF8(true);
            zaos.setUseLanguageEncodingFlag(true);
            zaos.setCreateUnicodeExtraFields(ZipArchiveOutputStream.UnicodeExtraFieldPolicy.NOT_ENCODEABLE);

            // Get to putting all the files in the compressed output file
            for (File f : files) {
                addFilesToCompression(zaos, f, null);
            }
            // Close everything up
            zaos.close();
            fos.close();
        } catch (Exception e) {
            LOG.error(session, STLogEnumImpl.CREATE_FILE_TEC, "Erreur lors de la commpression" + e.getMessage(), e);
        }
    }

    // add entries to archive file...
    private void addFilesToCompression(ArchiveOutputStream taos, File file, String parent) throws IOException {
        String fileName = file.getName();

        StringBuilder filePathBuiler = new StringBuilder();
        if (parent != null) {
            filePathBuiler.append(parent).append(File.separator).append(fileName);

            String resultFileName = this.getResultFileName();
            if (filePathBuiler.indexOf(resultFileName) == -1) {
                filePathBuiler.insert(0, File.separator).insert(0, this.getResultFileName());
            }
        }

        taos.putArchiveEntry(new ZipArchiveEntry(file, filePathBuiler.toString()));

        if (file.isFile()) {
            // Add the file to the archive
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            IOUtils.copy(bis, taos);
            taos.closeArchiveEntry();
            bis.close();
        } else if (file.isDirectory()) {
            // close the archive entry

            taos.closeArchiveEntry();

            // go through all the files in the directory and using recursion, add them to the archive

            for (File childFile : file.listFiles()) {
                addFilesToCompression(taos, childFile, file.getName());
            }
        }
    }

    public void generateReports() {
        LOG.info(STLogEnumImpl.DEFAULT, "Commencer à générer les rapports");
        for (ANReportEnum archRep : ANReportEnum.archiAnReportEnumValues()) {
            if (archRep.isSupportMinistereParam()) {
                this.generateReportParMinistere(archRep);
            } else {
                this.generateReport(archRep, null);
            }
        }
        LOG.info(STLogEnumImpl.DEFAULT, "La génération des rapports est terminée");

        // Finally zip result
        LOG.info(STLogEnumImpl.DEFAULT, "Compresser le resultat ");
        this.toZip();
        LOG.info(STLogEnumImpl.DEFAULT, "compression terminée");

        // Remove Temp Directory
        LOG.info(STLogEnumImpl.DEFAULT, "supprimer le répertoire temporaire");
        this.removeTempDirectory();
        LOG.info(STLogEnumImpl.DEFAULT, "suppression terminée");
    }

    private void removeTempDirectory() {
        cleanDirectory(new File(this.tempDirectoryPath));
        this.tempDirectory.delete();
    }

    protected void generateReport(ANReportEnum archAnReportEnum, String ministereTitle) {
        try {
            StringBuilder titleInfo = new StringBuilder("Commencer à générer le rapport ");
            titleInfo.append("Title : ");
            titleInfo.append(archAnReportEnum.getTitle());
            if (ministereTitle != null) {
                titleInfo.append(" ,ministère : ");
                titleInfo.append(ministereTitle);
            }
            LOG.info(STLogEnumImpl.DEFAULT, titleInfo.toString());

            // Assign Parameters
            SolonEpgServiceLocator
                .getSolonEpgParametreService()
                .getDocAnParametre(session)
                .assignParameters(
                    archAnReportEnum.getPublishPanParamAnType(),
                    inputValues,
                    adapter.isCurLegis(session)
                );

            SSServiceLocator
                .getSSBirtService()
                .generateReportResults(
                    getFile(archAnReportEnum, ministereTitle),
                    null,
                    archAnReportEnum.getRptdesignId(),
                    null,
                    inputValues,
                    Collections.singletonList(BirtOutputFormat.XLS)
                );
            LOG.info(STLogEnumImpl.DEFAULT, "Génération terminée");
        } catch (Exception e) {
            LOG.error(
                session,
                EpgLogEnumImpl.FAIL_GENERATE_BIRT_REPORT_TEC,
                "Erreur lors de la generation du rapport : " + archAnReportEnum.getTitle()
            );
        }
    }

    protected File getFile(ANReportEnum rep, String ministereTitle) {
        String fileName = rep.getFileName(ministereTitle);
        File file = new File(
            directoryPath(this.tempDirectoryPath, this.user, this.getResultFileName(), rep.getTheme()),
            fileName
        );
        try {
            file.createNewFile();
        } catch (IOException e) {
            LOG.error(
                session,
                STLogEnumImpl.CREATE_FILE_TEC,
                "Erreur lors de la creation d'un fichier " + file.getName() + " " + e.getMessage()
            );
        }
        return file;
    }

    protected void initDirectories() {
        LOG.info(STLogEnumImpl.DEFAULT, "Initialisations des répertoires");
        this.createDirectory(getMainDirectory(adapter.isCurLegis(session)), this.user);
        this.createDirectory(this.tempDirectoryPath, this.user, this.getResultFileName());
        this.createDirectory(
                this.tempDirectoryPath,
                this.user,
                this.getResultFileName(),
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_LOIS
            );
        this.createDirectory(
                this.tempDirectoryPath,
                this.user,
                this.getResultFileName(),
                ActiviteNormativeStatsConstants.AN_THEME_TRANSPOSITION_DES_DIRECTIVES
            );
        this.createDirectory(
                this.tempDirectoryPath,
                this.user,
                this.getResultFileName(),
                ActiviteNormativeStatsConstants.AN_THEME_SUIVI_DES_HABILITATIONS
            );
        this.createDirectory(
                this.tempDirectoryPath,
                this.user,
                this.getResultFileName(),
                ActiviteNormativeStatsConstants.AN_THEME_RATIFICATION_DES_ORDONNANCES
            );
        this.createDirectory(
                this.tempDirectoryPath,
                this.user,
                this.getResultFileName(),
                ActiviteNormativeStatsConstants.AN_THEME_APPLICATION_DES_ORDONNANCES
            );
        // this.createDirectory(this.tempDirectoryPath, this.user, this.getResultFileName(),
        // ActiviteNormativeStatsConstants.AN_THEME_TRAITES_ET_ACCORD);
        LOG.info(STLogEnumImpl.DEFAULT, "Initialisations des répertoires terminée");
    }

    protected List<EntiteNode> findCurrentMinisteres() {
        if (currentMinisteres == null) {
            currentMinisteres = STServiceLocator.getSTMinisteresService().getCurrentMinisteres();
        }
        return currentMinisteres;
    }

    private void init() {
        createTempDirectory();
        cleanUserDirectory();
        initDirectories();
    }

    private void cleanUserDirectory() {
        String userDirecPath = getMainDirectory(adapter.isCurLegis(session)) + File.separator + this.user;
        LOG.info(STLogEnumImpl.DEFAULT, "Nettoyage du répertoire " + userDirecPath);
        cleanDirectory(new File(userDirecPath));
    }

    private static void fullClean(boolean enCours) {
        String repertoirePrinc = getMainDirectory(enCours);
        LOG.info(STLogEnumImpl.DEFAULT, "Nettoyage du répertoire " + repertoirePrinc);
        cleanDirectory(new File(repertoirePrinc));
    }

    private void generateReportParMinistere(ANReportEnum archRep) {
        for (OrganigrammeNode node : this.findCurrentMinisteres()) {
            final EntiteNode entiteNode = (EntiteNode) node;

            if (entiteNode.isSuiviActiviteNormative()) {
                this.inputValues.put("MINISTEREPILOTE_PARAM", entiteNode.getId());
                this.inputValues.put("MINISTEREPILOTELABEL_PARAM", entiteNode.getLabel());
                this.generateReport(archRep, entiteNode.getNorMinistere());
            }
        }
    }

    private String directoryPath(String... names) {
        return Stream.of(names).collect(Collectors.joining(File.separator, File.separator, ""));
    }

    private void createDirectory(String... names) {
        String directoryName = this.directoryPath(names);
        LOG.info(STLogEnumImpl.DEFAULT, "Création d'une répertoire " + directoryName);
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdirs();
            LOG.info(STLogEnumImpl.DEFAULT, "Création du répertoire " + directoryName + " terminée");
        }
    }

    private File createDestinationFile() {
        File dest = new File(
            getMainDirectory(adapter.isCurLegis(session)) + this.user + File.separator + this.getResultFileName()
        );
        try {
            dest.createNewFile();
        } catch (IOException e) {
            LOG.error(
                session,
                STLogEnumImpl.CREATE_FILE_TEC,
                "Erreur lors de la création du ficher zip destinataire." + e.getMessage()
            );
        }
        return dest;
    }

    private void toZip() {
        String sourceFolder = this.getSourceFoder();
        File source = new File(sourceFolder + File.separator + this.getResultFileName());
        File dest = this.createDestinationFile();
        List<File> files = new ArrayList<>();
        files.add(source);
        this.compressFiles(files, dest);
    }

    private static void cleanDirectory(File file) {
        try {
            if (file.isDirectory()) {
                if (file.list().length == 0) {
                    file.delete();
                } else {
                    // list all the directory contents
                    String files[] = file.list();

                    for (String temp : files) {
                        // construct the file structure
                        File fileDelete = new File(file, temp);

                        // recursive delete
                        cleanDirectory(fileDelete);
                    }

                    // check the directory again, if empty then delete it
                    if (file.list().length == 0) {
                        file.delete();
                    }
                }
            } else {
                file.delete();
            }
        } catch (Exception e) {
            LOG.error(STLogEnumImpl.DEL_FOLDER_TEC, e);
        }
    }

    private String getSourceFoder() {
        return this.tempDirectoryPath + this.user;
    }

    private String getResultFileName() {
        return resultFileName;
    }

    private void createTempDirectory() {
        try {
            LOG.info(STLogEnumImpl.DEFAULT, "créer un répertoire temporaire");
            this.tempDirectory = File.createTempFile("result", "");
            this.tempDirectory.delete();
            this.tempDirectory.mkdir();
            this.tempDirectoryPath = this.tempDirectory.getPath() + File.separator;
            LOG.info(STLogEnumImpl.DEFAULT, "création terminée");
        } catch (IOException e) {
            LOG.error(session, STLogEnumImpl.CREATE_FILE_TEC, "Erreur lors de la création d'un fichier temporaire");
        }
    }

    public static String getMainDirectory(boolean enCours) {
        ActiviteNormativeService anService = SolonEpgServiceLocator.getActiviteNormativeService();
        String generatedReportPath;
        if (enCours) {
            generatedReportPath = anService.getPathDirANStatistiquesPublie();
        } else {
            try {
                LoginContext loginContext = Framework.login();
                try (CloseableCoreSession coreSession = SessionUtil.openSession()) {
                    generatedReportPath = anService.getPathDirANStatistiquesLegisPrecPublie(coreSession);
                }
                loginContext.logout();
            } catch (Exception e) {
                // Si on n'est pas parvenu à récupérer
                generatedReportPath = anService.getPathDirANStatistiquesPublie();
            }
        }

        return generatedReportPath + MAIN_DIRECTORY;
    }

    public static void removeMainDirectory(boolean enCours) {
        fullClean(enCours);
    }

    public static void removeFolderAndZip(String folderPath) {
        File folder = new File(folderPath);
        File zipFile = new File(folderPath + ".zip");
        folder.delete();
        zipFile.delete();
    }

    public static String getFilePath(final boolean currentLegislature, final String userName, final String docName) {
        return getMainDirectory(currentLegislature) + userName + File.separator + docName;
    }
}
