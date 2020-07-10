package fr.dila.solonepg.core.migration;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import fr.dila.solonepg.api.migration.MigrationDiscriminatorConstants;

@Entity(name = "ClosDetail")
@DiscriminatorValue(value = MigrationDiscriminatorConstants.CLOS)
public class ClosDetailModelImpl extends MigrationDetailModelImpl {

    private static final long serialVersionUID = 7263210755328333820L;

}
