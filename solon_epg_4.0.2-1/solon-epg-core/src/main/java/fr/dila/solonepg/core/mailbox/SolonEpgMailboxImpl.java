package fr.dila.solonepg.core.mailbox;

import fr.dila.cm.mailbox.MailboxImpl;
import fr.dila.solonepg.api.constant.SolonEpgConstant;
import fr.dila.solonepg.api.mailbox.SolonEpgMailbox;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implémentation des Mailbox de l'application SOLON EPG.
 *
 * @author jtremeax
 */
public class SolonEpgMailboxImpl extends MailboxImpl implements SolonEpgMailbox {
    private static final long serialVersionUID = 8820645351060312648L;

    /**
     * Constructeur de SolonEpgMailboxImpl.
     *
     * @param doc
     *            Modèle de document
     */
    public SolonEpgMailboxImpl(DocumentModel doc) {
        super(doc);
    }

    // ///////////////////////////////////////
    // Generic property getter & setter
    // ///////////////////////////////////////

    protected void setProperty(String schema, String property, Object value) {
        PropertyUtil.setProperty(doc, schema, property, value);
    }

    protected Long getLongProperty(String property) {
        return (Long) doc.getPropertyValue(property);
    }

    protected Integer getIntegerProperty(String property) {
        Long value = getLongProperty(property); // int property are stored ad long in nuxeo
        if (value != null) {
            return value.intValue();
        }
        return null;
    }

    // ///////////////////////////////////////
    // SolonEpgMailbox properties
    // ///////////////////////////////////////
    @Override
    public Integer getCountPourRedaction() {
        return getIntegerProperty(SolonEpgConstant.MAILBOX_POUR_REDACTION);
    }

    @Override
    public void setCountPourRedaction(Integer count) {
        setProperty(SolonEpgConstant.SOLON_EPG_MAILBOX_SCHEMA, SolonEpgConstant.MAILBOX_POUR_REDACTION, count);
    }

    @Override
    public Integer getCountPourAttribution() {
        return getIntegerProperty(SolonEpgConstant.MAILBOX_POUR_ATTRIBUTION);
    }

    @Override
    public void setCountPourAttribution(Integer count) {
        setProperty(SolonEpgConstant.SOLON_EPG_MAILBOX_SCHEMA, SolonEpgConstant.MAILBOX_POUR_ATTRIBUTION, count);
    }

    @Override
    public Integer getCountPourSignature() {
        return getIntegerProperty(SolonEpgConstant.MAILBOX_POUR_SIGNATURE);
    }

    @Override
    public void setCountPourSignature(Integer count) {
        setProperty(SolonEpgConstant.SOLON_EPG_MAILBOX_SCHEMA, SolonEpgConstant.MAILBOX_POUR_SIGNATURE, count);
    }

    @Override
    public Integer getCountPourInformation() {
        return getIntegerProperty(SolonEpgConstant.MAILBOX_POUR_INFORMATION);
    }

    @Override
    public void setCountPourInformation(Integer count) {
        setProperty(SolonEpgConstant.SOLON_EPG_MAILBOX_SCHEMA, SolonEpgConstant.MAILBOX_POUR_INFORMATION, count);
    }

    @Override
    public Integer getCountPourAvis() {
        return getIntegerProperty(SolonEpgConstant.MAILBOX_POUR_AVIS);
    }

    @Override
    public void setCountPourAvis(Integer count) {
        setProperty(SolonEpgConstant.SOLON_EPG_MAILBOX_SCHEMA, SolonEpgConstant.MAILBOX_POUR_AVIS, count);
    }

    @Override
    public Integer getCountPourValidationPM() {
        return getIntegerProperty(SolonEpgConstant.MAILBOX_POUR_VALIDATION_PM);
    }

    @Override
    public void setCountPourValidationPM(Integer count) {
        setProperty(SolonEpgConstant.SOLON_EPG_MAILBOX_SCHEMA, SolonEpgConstant.MAILBOX_POUR_VALIDATION_PM, count);
    }

    @Override
    public Integer getCountPourReattribution() {
        return getIntegerProperty(SolonEpgConstant.MAILBOX_POUR_REATTRIBUTION);
    }

    @Override
    public void setCountPourReattribution(Integer count) {
        setProperty(SolonEpgConstant.SOLON_EPG_MAILBOX_SCHEMA, SolonEpgConstant.MAILBOX_POUR_REATTRIBUTION, count);
    }

    @Override
    public Integer getCountPourReorientation() {
        return getIntegerProperty(SolonEpgConstant.MAILBOX_POUR_REORIENTATION);
    }

    @Override
    public void setCountPourReorientation(Integer count) {
        setProperty(SolonEpgConstant.SOLON_EPG_MAILBOX_SCHEMA, SolonEpgConstant.MAILBOX_POUR_REORIENTATION, count);
    }

    @Override
    public Integer getCountPourImpression() {
        return getIntegerProperty(SolonEpgConstant.MAILBOX_POUR_IMPRESSION);
    }

    @Override
    public void setCountPourImpression(Integer count) {
        setProperty(SolonEpgConstant.SOLON_EPG_MAILBOX_SCHEMA, SolonEpgConstant.MAILBOX_POUR_IMPRESSION, count);
    }

    @Override
    public Integer getCountPourTransmissionAuxAssemblees() {
        return getIntegerProperty(SolonEpgConstant.MAILBOX_POUR_TRANSMISSION_ASSEMBLEES);
    }

    @Override
    public void setCountPourTransmissionAuxAssemblees(Integer count) {
        setProperty(
            SolonEpgConstant.SOLON_EPG_MAILBOX_SCHEMA,
            SolonEpgConstant.MAILBOX_POUR_TRANSMISSION_ASSEMBLEES,
            count
        );
    }

    @Override
    public Integer getCountPourArbitrage() {
        return getIntegerProperty(SolonEpgConstant.MAILBOX_POUR_ARBITRAGE);
    }

    @Override
    public void setCountPourArbitrage(Integer count) {
        setProperty(SolonEpgConstant.SOLON_EPG_MAILBOX_SCHEMA, SolonEpgConstant.MAILBOX_POUR_ARBITRAGE, count);
    }

    @Override
    public Integer getCountPourRedactionInterfacee() {
        return getIntegerProperty(SolonEpgConstant.MAILBOX_POUR_REDACTION_INTERFACEE);
    }

    @Override
    public void setCountPourRedactionInterfacee(Integer count) {
        setProperty(
            SolonEpgConstant.SOLON_EPG_MAILBOX_SCHEMA,
            SolonEpgConstant.MAILBOX_POUR_REDACTION_INTERFACEE,
            count
        );
    }
}
