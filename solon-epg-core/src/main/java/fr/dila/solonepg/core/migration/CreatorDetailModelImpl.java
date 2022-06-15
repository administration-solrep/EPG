package fr.dila.solonepg.core.migration;

import fr.dila.ss.api.migration.MigrationDiscriminatorConstants;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "CreatorDetail")
@DiscriminatorValue(value = MigrationDiscriminatorConstants.CREATOR)
public class CreatorDetailModelImpl extends MigrationDetailModelImpl {
    private static final long serialVersionUID = -2572673464616612932L;
}
