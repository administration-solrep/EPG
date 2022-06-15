package fr.dila.solonepg.ui.event;

import fr.dila.solonepg.core.export.dto.ExportRechercheDossierDTO;
import fr.dila.solonepg.core.util.EpgExcelUtil;
import fr.dila.solonepg.elastic.models.ElasticDossier;
import fr.dila.ss.core.event.AbstractExportDossierListener;
import fr.dila.st.core.util.FileUtils;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import javax.activation.DataSource;
import org.nuxeo.ecm.core.api.CoreSession;

public abstract class AbstractExportDossierRechercheListener extends AbstractExportDossierListener {
    private static final String EXPORT_FILENAME = FileUtils.generateCompleteXlsFilename("export_dossiers");

    protected AbstractExportDossierRechercheListener(String eventName) {
        super(eventName, EXPORT_FILENAME);
    }

    @Override
    protected DataSource processExport(CoreSession session, Map<String, Serializable> eventProperties)
        throws Exception {
        Collection<ElasticDossier> results = getSearchResults(session, eventProperties);

        return EpgExcelUtil.exportRechercheDossiers(
            session,
            results.stream().map(AbstractExportDossierRechercheListener::convertTo).collect(Collectors.toList())
        );
    }

    protected abstract Collection<ElasticDossier> getSearchResults(
        CoreSession session,
        Map<String, Serializable> eventProperties
    )
        throws Exception;

    private static ExportRechercheDossierDTO convertTo(ElasticDossier dossier) {
        return new ExportRechercheDossierDTO(
            dossier.getDosNumeroNor(),
            dossier.getDosTitreActe(),
            dossier.getDosCreated(),
            dossier.getDcLastContributor(),
            dossier.getDosIdCreateurDossier(),
            dossier.getDosStatut(),
            dossier.getDosTypeActe()
        );
    }
}
