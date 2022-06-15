package fr.dila.solonepg.api.feuilleroute;

import java.util.Objects;
import java.util.stream.Stream;

public enum SqueletteStepTypeDestinataire {
    /** Type de destinataire de l'étape : organigramme. */
    ORGANIGRAMME("admin.squelette.label.mailbox.organigramme"),
    /**
     * Type de destinataire de l'étape : bureau du cabinet d'un ministère
     * (générique).
     */
    BUREAU_DU_CABINET("admin.squelette.label.mailbox.bdc"),
    /** Type de destinataire de l'étape : chargé de mission (générique). */
    CHARGE_DE_MISSION("admin.squelette.label.mailbox.cdm"),
    /** Type de destinataire de l'étape : secrétariat chargé de mission (générique). */
    SECRETARIAT_CHARGE_DE_MISSION("admin.squelette.label.mailbox.scdm"),
    /** Type de destinataire de l'étape : conseiller PM (générique). */
    CONSEILLER_PM("admin.squelette.label.mailbox.cpm");

    private final String value;

    SqueletteStepTypeDestinataire(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SqueletteStepTypeDestinataire fromValue(String name) {
        return Stream.of(values()).filter(type -> Objects.equals(type.name(), name)).findFirst().orElse(null);
    }
}
