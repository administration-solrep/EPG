package fr.dila.solonepg.core.migration;

import fr.dila.ss.api.migration.MigrationDiscriminatorConstants;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "BulletinDetail")
@DiscriminatorValue(value = MigrationDiscriminatorConstants.BO)
public class BulletinDetailModelImpl extends MigrationDetailModelImpl {
    private static final long serialVersionUID = -3796980555125968636L;
}
