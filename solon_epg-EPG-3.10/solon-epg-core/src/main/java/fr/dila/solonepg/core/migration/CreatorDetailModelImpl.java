package fr.dila.solonepg.core.migration;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import fr.dila.solonepg.api.migration.MigrationDiscriminatorConstants;

@Entity(name = "CreatorDetail")
@DiscriminatorValue(value = MigrationDiscriminatorConstants.CREATOR)
public class CreatorDetailModelImpl extends MigrationDetailModelImpl {

    private static final long serialVersionUID = -2572673464616612932L;

}
