package fr.dila.solonepg.core.migration;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import fr.dila.solonepg.api.migration.MigrationDiscriminatorConstants;

@Entity(name = "ModeleFDRDetail")
@DiscriminatorValue(value = MigrationDiscriminatorConstants.FDR)
public class ModeleFDRDetailModelImpl extends MigrationDetailModelImpl {

    private static final long serialVersionUID = -5282990752404203710L;

}
