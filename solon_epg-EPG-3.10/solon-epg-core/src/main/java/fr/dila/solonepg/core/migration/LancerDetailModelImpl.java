package fr.dila.solonepg.core.migration;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import fr.dila.solonepg.api.migration.MigrationDiscriminatorConstants;

@Entity(name = "LancerDetail")
@DiscriminatorValue(value = MigrationDiscriminatorConstants.LANCE)
public class LancerDetailModelImpl extends MigrationDetailModelImpl {

    private static final long serialVersionUID = -2781120053887590699L;

}
