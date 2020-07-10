package fr.dila.solonepg.core.migration;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import fr.dila.solonepg.api.migration.MigrationDiscriminatorConstants;

@Entity(name = "StepDetail")
@DiscriminatorValue(value = MigrationDiscriminatorConstants.STEP)
public class StepDetailModelImpl extends MigrationDetailModelImpl {

    private static final long serialVersionUID = -1985271719571753180L;

}
