package fr.dila.solonepg.core.migration;

import fr.dila.ss.api.migration.MigrationDiscriminatorConstants;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "TableRefDetail")
@DiscriminatorValue(value = MigrationDiscriminatorConstants.TABLEREF)
public class TableRefDetailModelImpl extends MigrationDetailModelImpl {
    private static final long serialVersionUID = -1016794076608387924L;
}
