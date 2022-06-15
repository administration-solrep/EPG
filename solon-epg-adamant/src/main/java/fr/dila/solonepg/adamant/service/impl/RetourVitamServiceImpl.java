package fr.dila.solonepg.adamant.service.impl;

import fr.dila.solonepg.adamant.SolonEpgAdamantConstant;
import fr.dila.solonepg.adamant.SolonEpgAdamantServiceLocator;
import fr.dila.solonepg.adamant.model.RetourVitamBordereauLivraison;
import fr.dila.solonepg.adamant.model.RetourVitamRapportVersement;
import fr.dila.solonepg.adamant.service.RetourVitamService;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.constant.SolonEpgFondDeDossierConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.solonepg.api.service.FondDeDossierService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.st.api.constant.STPathConstant;
import fr.dila.st.api.service.ConfigService;
import fr.dila.st.core.service.STServiceLocator;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.impl.blob.ByteArrayBlob;

public class RetourVitamServiceImpl implements RetourVitamService {

    @Override
    public void saveLineBorderauLivraison(RetourVitamBordereauLivraison bordereau) {
        SolonEpgAdamantServiceLocator.getDossierExtractionDao().saveLineBordereauLivraison(bordereau);
    }

    @Override
    public void updateStatutDossierFromBordereauLivraison(
        CoreSession session,
        RetourVitamBordereauLivraison bordereau,
        int indexLine
    ) {
        String nor = getNorFromNomSip(bordereau.getNomSip());

        if (StringUtils.isNotBlank(bordereau.getStatutLivraison())) {
            DocumentModel dossierDoc = SolonEpgServiceLocator.getNORService().getDossierFromNOR(session, nor);
            if (dossierDoc == null) {
                throw new IllegalArgumentException(
                    String.format(
                        "Erreur à la ligne %s. Aucun dossier correspondant au numéro NOR %s. ",
                        indexLine,
                        nor
                    )
                );
            }
            Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            ConfigService configService = STServiceLocator.getConfigService();
            if (
                configService
                    .getValue(SolonEpgAdamantConstant.RETOUR_VITAM_LIVRAISON_OK)
                    .equals(bordereau.getStatutLivraison())
            ) {
                dossier.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_LIVRE);
                dossier.save(session);
            } else if (
                configService
                    .getValue(SolonEpgAdamantConstant.RETOUR_VITAM_LIVRAISON_KO)
                    .equals(bordereau.getStatutLivraison())
            ) {
                dossier.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_ERREUR_LIVRAISON);
                dossier.save(session);
            }
        }
    }

    @Override
    public void saveLineRapportVersement(RetourVitamRapportVersement rapport) {
        SolonEpgAdamantServiceLocator.getDossierExtractionDao().saveLineRapportVersement(rapport);
    }

    @Override
    public void updateStatutDossierFromRapportVersement(
        CoreSession session,
        RetourVitamRapportVersement rapport,
        int indexLine
    ) {
        String nor = getNorFromNomSip(rapport.getNomSip());

        if (StringUtils.isNoneBlank(rapport.getStatutVersement())) {
            DocumentModel dossierDoc = SolonEpgServiceLocator.getNORService().getDossierFromNOR(session, nor);
            if (dossierDoc == null) {
                throw new IllegalArgumentException(
                    String.format(
                        "Erreur à la ligne %s. Aucun dossier correspondant au numéro NOR %s. ",
                        indexLine,
                        nor
                    )
                );
            }
            Dossier dossier = dossierDoc.getAdapter(Dossier.class);
            ConfigService configService = STServiceLocator.getConfigService();
            if (
                configService
                    .getValue(SolonEpgAdamantConstant.RETOUR_VITAM_VERSEMENT_OK)
                    .equals(rapport.getStatutVersement())
            ) {
                dossier.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_ARCHIVE);
                dossier.save(session);
            } else {
                dossier.setStatutArchivage(VocabularyConstants.STATUT_ARCHIVAGE_DOSSIER_ERREUR_ARCHIVAGE);
                dossier.save(session);
            }
        }
    }

    @Override
    public void saveBordereauVersementDossier(CoreSession session, String fileName, byte[] content) {
        String nor = extractNumeroNor(fileName);
        DocumentModel dossierDoc = SolonEpgServiceLocator.getNORService().getDossierFromNOR(session, nor);
        if (dossierDoc == null) {
            throw new IllegalArgumentException(String.format("Aucun dossier correspondant au numéro NOR %s. ", nor));
        }
        Dossier dossier = dossierDoc.getAdapter(Dossier.class);
        FondDeDossierService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
        DocumentModel folder = fondDeDossierService.getFondDossierFolder(
            dossierDoc,
            session,
            SolonEpgFondDeDossierConstants.FOND_DE_DOSSIER_FOLDER_NAME_BORDEREAU_VERSEMENT,
            true
        );
        clearFddFolderBordereauVersement(session, dossier, folder);
        fondDeDossierService.createFondDeDossierFile(
            session,
            fileName,
            new ByteArrayBlob(content),
            folder.getName(),
            dossier.getDocument()
        );
    }

    private String getNorFromNomSip(String nomSip) {
        return nomSip.substring(0, nomSip.lastIndexOf('.'));
    }

    @Override
    public void moveFileToDone(CoreSession session, String fileName) throws IOException {
        Files.move(
            Paths.get(getPathDirRetourVitamTodo() + STPathConstant.PATH_SEP + fileName),
            Paths.get(getPathDirRetourVitamDone() + STPathConstant.PATH_SEP + fileName),
            StandardCopyOption.REPLACE_EXISTING
        );
    }

    @Override
    public void moveFileToError(CoreSession session, String fileName) throws IOException {
        Files.move(
            Paths.get(getPathDirRetourVitamTodo() + STPathConstant.PATH_SEP + fileName),
            Paths.get(getPathDirRetourVitamError() + STPathConstant.PATH_SEP + fileName),
            StandardCopyOption.REPLACE_EXISTING
        );
    }

    /**
     * get le path du répertoire de dépôt des retours VITAM
     *
     * @param session
     * @return
     */
    @Override
    public String getPathDirRetourVitamTodo() {
        return getOrCreatePathDir(SolonEpgAdamantConstant.SOLON_RETOUR_VITAM_TODO_REP);
    }

    /**
     * get le path du répertoire done des retours VITAM
     *
     * @param session
     * @return
     */
    private String getPathDirRetourVitamDone() {
        return getOrCreatePathDir(SolonEpgAdamantConstant.SOLON_RETOUR_VITAM_DONE_REP);
    }

    /**
     * get le path du répertoire error des retours VITAM
     *
     * @param session
     * @return
     */
    private String getPathDirRetourVitamError() {
        return getOrCreatePathDir(SolonEpgAdamantConstant.SOLON_RETOUR_VITAM_ERROR_REP);
    }

    private String getOrCreatePathDir(String dirName) {
        ConfigService configService = STServiceLocator.getConfigService();
        String retourVitamErrorPath = configService.getValue(dirName);

        File retourVitamErrorDir = new File(retourVitamErrorPath);
        if (!retourVitamErrorDir.exists()) {
            retourVitamErrorDir.mkdirs();
        }
        return retourVitamErrorPath;
    }

    private String extractNumeroNor(String fileName) {
        Pattern pattern = Pattern.compile("[A-Z]{4}[0-9]{7}[A-Z]{1}");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            throw new IllegalArgumentException(
                "Erreur lors de la récupération du numéro NOR du fichier %s contenue dans le zip."
            );
        }
    }

    private void clearFddFolderBordereauVersement(CoreSession session, Dossier dossier, DocumentModel folder) {
        FondDeDossierService fondDeDossierService = SolonEpgServiceLocator.getFondDeDossierService();
        List<DocumentModel> lstDocs = fondDeDossierService.getFilesFromFondDossierFolder(session, dossier, folder);
        STServiceLocator.getTrashService().trashDocuments(lstDocs);
    }
}
