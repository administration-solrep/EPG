package fr.dila.solonepg.core.migration;

import fr.dila.ss.api.migration.MigrationDiscriminatorConstants;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "MotsClesDetail")
@DiscriminatorValue(value = MigrationDiscriminatorConstants.MOTSCLES)
public class MotsClesDetailModelImpl extends MigrationDetailModelImpl {
    private static final long serialVersionUID = 8422350014628073709L;
}
