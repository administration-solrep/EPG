package fr.dila.solonepg.core.migration;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import fr.dila.solonepg.api.migration.MigrationDiscriminatorConstants;

@Entity(name = "MailBoxDetail")
@DiscriminatorValue(value = MigrationDiscriminatorConstants.MAILBOX)
public class MailBoxDetailModelImpl extends MigrationDetailModelImpl {

    private static final long serialVersionUID = 1911904197107085940L;

}
