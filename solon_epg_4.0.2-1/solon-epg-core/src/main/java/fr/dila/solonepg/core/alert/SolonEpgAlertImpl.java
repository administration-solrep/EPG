package fr.dila.solonepg.core.alert;

import static fr.dila.st.api.constant.STAlertConstant.ALERT_SCHEMA;

import fr.dila.solonepg.api.alert.SolonEpgAlert;
import fr.dila.st.api.constant.STAlertConstant;
import fr.dila.st.core.alert.AlertImpl;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implementation de l'Interface SolonEpgAlert.
 *
 * @author arn
 */
public class SolonEpgAlertImpl extends AlertImpl implements SolonEpgAlert {
    private static final long serialVersionUID = 1L;

    public SolonEpgAlertImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public Calendar getDateDemandeConfirmation() {
        return PropertyUtil.getCalendarProperty(
            getDocument(),
            ALERT_SCHEMA,
            STAlertConstant.ALERT_DATE_DEMANDE_CONFIRMATION
        );
    }

    @Override
    public void setDateDemandeConfirmation(Calendar dateDemandeConfirmation) {
        PropertyUtil.setProperty(
            getDocument(),
            ALERT_SCHEMA,
            STAlertConstant.ALERT_DATE_DEMANDE_CONFIRMATION,
            dateDemandeConfirmation
        );
    }

    @Override
    public Boolean getHasDemandeConfirmation() {
        return PropertyUtil.getBooleanProperty(
            getDocument(),
            STAlertConstant.ALERT_SCHEMA,
            STAlertConstant.ALERT_HAS_DEMANDE_CONFIRMATION
        );
    }

    @Override
    public void setHasDemandeConfirmation(Boolean hasDemandeConfirmation) {
        PropertyUtil.setProperty(
            getDocument(),
            ALERT_SCHEMA,
            STAlertConstant.ALERT_HAS_DEMANDE_CONFIRMATION,
            hasDemandeConfirmation
        );
    }
}
