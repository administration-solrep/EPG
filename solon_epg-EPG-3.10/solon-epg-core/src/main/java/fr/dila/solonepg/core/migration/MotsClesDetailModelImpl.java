package fr.dila.solonepg.core.migration;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import fr.dila.solonepg.api.migration.MigrationDiscriminatorConstants;

@Entity(name = "MotsClesDetail")
@DiscriminatorValue(value = MigrationDiscriminatorConstants.MOTSCLES)
public class MotsClesDetailModelImpl extends MigrationDetailModelImpl {

    private static final long serialVersionUID = 8422350014628073709L;

}