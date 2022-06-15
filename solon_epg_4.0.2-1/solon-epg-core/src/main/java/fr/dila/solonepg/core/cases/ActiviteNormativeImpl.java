package fr.dila.solonepg.core.cases;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.constant.ActiviteNormativeConstants;
import fr.dila.st.core.domain.STDomainObjectImpl;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implementation de {@link ActiviteNormative} .
 *
 * @author asatre
 *
 */
public class ActiviteNormativeImpl extends STDomainObjectImpl implements ActiviteNormative {
    private static final long serialVersionUID = -2648569632965678122L;

    public ActiviteNormativeImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    public String getApplicationLoi() {
        return PropertyUtil.getStringProperty(
            document,
            ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA,
            ActiviteNormativeConstants.P_APPLICATION_LOI
        );
    }

    @Override
    public void setApplicationLoi(String applicationLoi) {
        PropertyUtil.setProperty(
            document,
            ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA,
            ActiviteNormativeConstants.P_APPLICATION_LOI,
            applicationLoi
        );
    }

    @Override
    public String getOrdonnance() {
        return PropertyUtil.getStringProperty(
            document,
            ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA,
            ActiviteNormativeConstants.P_ORDONNANCE
        );
    }

    @Override
    public void setOrdonnance(String ordonnance) {
        PropertyUtil.setProperty(
            document,
            ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA,
            ActiviteNormativeConstants.P_ORDONNANCE,
            ordonnance
        );
    }

    @Override
    public String getTraite() {
        return PropertyUtil.getStringProperty(
            document,
            ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA,
            ActiviteNormativeConstants.P_TRAITE
        );
    }

    @Override
    public void setTraite(String traite) {
        PropertyUtil.setProperty(
            document,
            ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA,
            ActiviteNormativeConstants.P_TRAITE,
            traite
        );
    }

    @Override
    public String getTransposition() {
        return PropertyUtil.getStringProperty(
            document,
            ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA,
            ActiviteNormativeConstants.P_TRANSPOSITION
        );
    }

    @Override
    public void setTransposition(String transposition) {
        PropertyUtil.setProperty(
            document,
            ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA,
            ActiviteNormativeConstants.P_TRANSPOSITION,
            transposition
        );
    }

    @Override
    public String getOrdonnance38C() {
        return PropertyUtil.getStringProperty(
            document,
            ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA,
            ActiviteNormativeConstants.P_ORDONNANCE_38C
        );
    }

    @Override
    public void setOrdonnance38C(String ordonnance38C) {
        PropertyUtil.setProperty(
            document,
            ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA,
            ActiviteNormativeConstants.P_ORDONNANCE_38C,
            ordonnance38C
        );
    }

    @Override
    public <T> T getAdapter(Class<T> itf) {
        return getDocument().getAdapter(itf);
    }

    @Override
    public String getApplicationOrdonnance() {
        return PropertyUtil.getStringProperty(
            document,
            ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA,
            ActiviteNormativeConstants.P_APPLICATION_ORDONNANCE
        );
    }

    @Override
    public void setApplicationOrdonnance(String applicationOrdonnance) {
        PropertyUtil.setProperty(
            document,
            ActiviteNormativeConstants.ACTIVITE_NORMATIVE_SCHEMA,
            ActiviteNormativeConstants.P_APPLICATION_ORDONNANCE,
            applicationOrdonnance
        );
    }
}
