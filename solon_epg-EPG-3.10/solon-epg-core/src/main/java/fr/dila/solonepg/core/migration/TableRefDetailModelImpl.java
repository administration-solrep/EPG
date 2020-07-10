package fr.dila.solonepg.core.migration;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import fr.dila.solonepg.api.migration.MigrationDiscriminatorConstants;

@Entity(name = "TableRefDetail")
@DiscriminatorValue(value = MigrationDiscriminatorConstants.TABLEREF)
public class TableRefDetailModelImpl extends MigrationDetailModelImpl {

    private static final long serialVersionUID = -1016794076608387924L;

}
