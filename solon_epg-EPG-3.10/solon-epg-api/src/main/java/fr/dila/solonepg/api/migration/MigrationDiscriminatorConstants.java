package fr.dila.solonepg.api.migration;

public final class MigrationDiscriminatorConstants {

    public static final String FDR = "FDR";
    public static final String STEP = "STEP";
    public static final String LANCE = "LANCE";
    public static final String CLOS = "CLOS";
    public static final String BO = "BO";
    public static final String MOTSCLES = "MOTSCLES";
    public static final String CREATOR = "CREATOR";
    public static final String MAILBOX = "MAILBOX";
    public static final String TABLEREF = "TABLEREF";

    public enum MigrationDiscirminatorEnum {
        FDR, STEP, LANCE, CLOS, BO, MOTSCLES, CREATOR, MAILBOX, TABLEREF;

        public static MigrationDiscirminatorEnum find(String name) {
            try {
                return MigrationDiscirminatorEnum.valueOf(name);
            } catch (IllegalArgumentException e) {
                // if the specified enum type has no constant with the specified name, or the specified class object does not represent an enum type
                return null;
            } catch (NullPointerException e) {
                // if enumType or name is null
                return null;
            }
        }
    }

    private MigrationDiscriminatorConstants() {
        // default private constructor
    }
}
