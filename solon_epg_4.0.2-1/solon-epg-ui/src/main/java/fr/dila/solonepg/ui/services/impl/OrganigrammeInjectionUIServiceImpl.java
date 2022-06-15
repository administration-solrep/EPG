package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.api.constant.SolonEpgConfigConstant.SOLON_INJECTION_GOUVERNEMENT_DIRECTORY;
import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getEpgInjectionGouvernementService;
import static fr.dila.solonepg.ui.jaxrs.webobject.ajax.admin.organigramme.EpgOrganigrammeAjax.ERROR_IMPORT_MESSAGE;
import static fr.dila.solonmgpp.core.service.SolonMgppServiceLocator.getMGPPInjectionGouvernementService;
import static fr.dila.st.core.service.STServiceLocator.getConfigService;
import static fr.dila.st.core.util.ResourceHelper.getString;
import static fr.dila.st.ui.helper.UserSessionHelper.putUserSessionParameter;
import static java.io.File.separator;
import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableList;
import com.sun.jersey.core.header.FormDataContentDisposition;
import fr.dila.solonepg.api.client.InjectionEpgGvtDTO;
import fr.dila.solonepg.api.constant.SolonEpgConfigConstant;
import fr.dila.solonepg.api.exception.EPGException;
import fr.dila.solonepg.api.exception.ImportOrganigrammeException;
import fr.dila.solonepg.api.service.EpgInjectionGouvernementService;
import fr.dila.solonepg.core.util.EpgExcelUtil;
import fr.dila.solonepg.ui.enums.EpgUserSessionKey;
import fr.dila.solonepg.ui.services.OrganigrammeInjectionUIService;
import fr.dila.ss.api.client.InjectionGvtDTO;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.ui.services.actions.SSActionsServiceLocator;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.FileUtils;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.core.util.STIOUtils;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.helper.UserSessionHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.NuxeoException;

public class OrganigrammeInjectionUIServiceImpl implements OrganigrammeInjectionUIService {
    protected static final String INJECTION_GOUVERNEMENT_XLS_FILENAME = "injection_gouvernement.xls";
    public static final String EXPORT_GOUVERNEMENT_FILE_NAME = "gouvernement.xls";

    private static final STLogger LOGGER = STLogFactory.getLog(OrganigrammeInjectionUIServiceImpl.class);

    private static final String ERROR_MSG_NO_FILE_SELECTED = "feedback.ss.document.tree.document.error.unselected.file";

    @Override
    public void saveExcelInjection(SpecificContext context) {
        InputStream is = context.getFromContextData(STContextDataKey.FILE_CONTENT);
        FormDataContentDisposition fileDetail = context.getFromContextData(STContextDataKey.FILE_DETAILS);
        String filename = FileUtils.sanitizePathTraversal(fileDetail.getFileName());
        try {
            byte[] bytes = STIOUtils.toByteArray(is);
            if (!FileUtils.equalsMimetype(bytes, filename)) {
                // Incompatibilité de mimetype
                throw new ImportOrganigrammeException(ImmutableList.of(getString(ERROR_IMPORT_MESSAGE)));
            }

            String injectionExcelDirPath = getConfigService().getValue(SOLON_INJECTION_GOUVERNEMENT_DIRECTORY);

            // Vérification de l'existence du répertoire
            File generatedFilesDir = new File(injectionExcelDirPath);
            if (!generatedFilesDir.exists()) {
                generatedFilesDir.mkdirs();
            }

            // Création du fichier
            final File injectionFile = new File(
                injectionExcelDirPath + separator + INJECTION_GOUVERNEMENT_XLS_FILENAME
            );
            if (!injectionFile.exists() && !injectionFile.createNewFile()) {
                throw new EPGException("Impossible de créer le fichier : " + injectionFile.getAbsolutePath());
            }

            // Enregistrement
            try (FileOutputStream outputStream = new FileOutputStream(injectionFile)) {
                IOUtils.copy(new ByteArrayInputStream(bytes), outputStream);
            }
        } catch (IOException e) {
            throw new NuxeoException(e);
        }
    }

    @Override
    public boolean isFichierInjectionDisponible() {
        String injectionExcelDirPath = STServiceLocator
            .getConfigService()
            .getValue(SolonEpgConfigConstant.SOLON_INJECTION_GOUVERNEMENT_DIRECTORY);
        final File report = new File(injectionExcelDirPath + "/" + INJECTION_GOUVERNEMENT_XLS_FILENAME);
        return report.exists();
    }

    @Override
    public void injecterGouvernementEPG(SpecificContext context) {
        // Récupération des données du fichier Excel
        CoreSession session = context.getSession();
        LOGGER.info(session, STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC);
        // Récupération du fichier sur le serveur
        String injectionExcelDirPath = STServiceLocator
            .getConfigService()
            .getValue(SolonEpgConfigConstant.SOLON_INJECTION_GOUVERNEMENT_DIRECTORY);
        final File report = new File(injectionExcelDirPath + "/" + INJECTION_GOUVERNEMENT_XLS_FILENAME);
        if (!report.exists()) {
            context.getMessageQueue().addErrorToQueue(ResourceHelper.getString(ERROR_MSG_NO_FILE_SELECTED));
            throw new EPGException("Fichier Excel d'injection non présent");
        }

        EpgInjectionGouvernementService epgInjectionGouvernementService = getEpgInjectionGouvernementService();
        try {
            List<InjectionGvtDTO> listInjection = epgInjectionGouvernementService.prepareInjection(session, report);
            LOGGER.info(session, STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC);
            // Création du DTO de comparaison pour l'affichage
            List<InjectionEpgGvtDTO> listInjectionEPG = getEpgInjectionGouvernementService()
                .createComparedDTO(session, listInjection);
            putUserSessionParameter(context, EpgUserSessionKey.INJECTIONS, listInjection);
            putUserSessionParameter(
                context,
                EpgUserSessionKey.INJECTIONS_MODIFICATIONS,
                listInjectionEPG.stream().map(InjectionEpgGvtDTO::getModification).collect(Collectors.toList())
            );
            putUserSessionParameter(context, EpgUserSessionKey.MAP_KEY_INJECTIONS_OLD, getBaseGvt(listInjectionEPG));
            putUserSessionParameter(
                context,
                EpgUserSessionKey.MAP_KEY_INJECTIONS_NEW.name(),
                getImportedGvt(listInjectionEPG)
            );
        } catch (EPGException ex) {
            context.getMessageQueue().addErrorToQueue(ex.getMessage());
        }
    }

    private List<InjectionGvtDTO> getBaseGvt(List<InjectionEpgGvtDTO> listInjectionEPG) {
        return listInjectionEPG.stream().map(InjectionEpgGvtDTO::getBaseGvt).collect(toList());
    }

    private List<InjectionGvtDTO> getImportedGvt(List<InjectionEpgGvtDTO> listInjectionEPG) {
        return listInjectionEPG.stream().map(InjectionEpgGvtDTO::getImportedGvt).collect(toList());
    }

    @Override
    public void exportGouvernement(CoreSession session) {
        try {
            org.apache.commons.io.FileUtils.copyInputStreamToFile(
                EpgExcelUtil.createExportGvt(session).getInputStream(),
                new File(FileUtils.getAppTmpFilePath(EXPORT_GOUVERNEMENT_FILE_NAME))
            );
        } catch (IOException e) {
            throw new NuxeoException(e);
        }
    }

    @Override
    public void executeInjectionEPG(SpecificContext context) {
        if (checkInjectionPermissions(context)) {
            LOGGER.info(context.getSession(), STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC);
            getEpgInjectionGouvernementService().executeInjection(context.getSession(), getInjectionDtos(context));
        }
    }

    @Override
    public void executeInjectionEPP(SpecificContext context) {
        if (checkInjectionPermissions(context)) {
            LOGGER.info(context.getSession(), STLogEnumImpl.PROCESS_INJECTION_GOUVERNEMENT_TEC);
            getMGPPInjectionGouvernementService().executeInjection(context.getSession(), getInjectionDtos(context));
        }
    }

    private boolean checkInjectionPermissions(SpecificContext context) {
        return SSActionsServiceLocator
            .getSSOrganigrammeManagerActionService()
            .allowUpdateOrganigramme((SSPrincipal) context.getSession().getPrincipal(), null);
    }

    private List<InjectionGvtDTO> getInjectionDtos(SpecificContext context) {
        return UserSessionHelper.getUserSessionParameter(context, EpgUserSessionKey.INJECTIONS);
    }
}
