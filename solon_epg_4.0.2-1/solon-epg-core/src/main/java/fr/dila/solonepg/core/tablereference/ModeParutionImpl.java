package fr.dila.solonepg.core.tablereference;

import fr.dila.solonepg.api.constant.ModeParutionConstants;
import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.tablereference.ModeParution;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implementation de {@link ModeParution}
 *
 */
public class ModeParutionImpl implements ModeParution {
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -7159991104650963962L;

    private DocumentModel document;

    public ModeParutionImpl(DocumentModel doc) {
        this.document = doc;
    }

    @Override
    public DocumentModel getDocument() {
        return this.document;
    }

    @Override
    public DocumentModel save(CoreSession session) {
        return session.saveDocument(document);
    }

    @Override
    public String getId() {
        return getDocument().getId();
    }

    @Override
    public String getMode() {
        return PropertyUtil.getStringProperty(
            getDocument(),
            SolonEpgSchemaConstant.MODE_PARUTION_SCHEMA,
            ModeParutionConstants.MODE_PARUTION_MODE_PROPERTY
        );
    }

    @Override
    public void setMode(String mode) {
        PropertyUtil.setProperty(
            getDocument(),
            SolonEpgSchemaConstant.MODE_PARUTION_SCHEMA,
            ModeParutionConstants.MODE_PARUTION_MODE_PROPERTY,
            mode
        );
    }

    @Override
    public Calendar getDateDebut() {
        return PropertyUtil.getCalendarProperty(
            getDocument(),
            SolonEpgSchemaConstant.MODE_PARUTION_SCHEMA,
            ModeParutionConstants.MODE_PARUTION_DATE_DEBUT_PROPERTY
        );
    }

    @Override
    public void setDateDebut(Calendar dateDebut) {
        PropertyUtil.setProperty(
            getDocument(),
            SolonEpgSchemaConstant.MODE_PARUTION_SCHEMA,
            ModeParutionConstants.MODE_PARUTION_DATE_DEBUT_PROPERTY,
            dateDebut
        );
    }

    @Override
    public Calendar getDateFin() {
        return PropertyUtil.getCalendarProperty(
            getDocument(),
            SolonEpgSchemaConstant.MODE_PARUTION_SCHEMA,
            ModeParutionConstants.MODE_PARUTION_DATE_FIN_PROPERTY
        );
    }

    @Override
    public void setDateFin(Calendar dateFin) {
        PropertyUtil.setProperty(
            getDocument(),
            SolonEpgSchemaConstant.MODE_PARUTION_SCHEMA,
            ModeParutionConstants.MODE_PARUTION_DATE_FIN_PROPERTY,
            dateFin
        );
    }
}
