package fr.dila.solonepg.core.migration;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import fr.dila.solonepg.api.migration.MigrationDiscriminatorConstants;

@Entity(name = "BulletinDetail")
@DiscriminatorValue(value = MigrationDiscriminatorConstants.BO)
public class BulletinDetailModelImpl extends MigrationDetailModelImpl {

    private static final long serialVersionUID = -3796980555125968636L;

}
