package fr.dila.solonepg.ui.enums;

import fr.dila.st.api.organigramme.OrganigrammeType;
import java.util.stream.Stream;

public enum EpgOrganigrammeCopyActionEnum {
    POSTE(OrganigrammeType.POSTE, "PASTE_NODE", EpgActionEnum.COPY_NODE),
    MINISTERE(OrganigrammeType.MINISTERE, "PASTE_ENTITY", EpgActionEnum.COPY_NODE_MINISTERE_US),
    UNITE_STRUCTURELLE(OrganigrammeType.UNITE_STRUCTURELLE, "PASTE_US", EpgActionEnum.COPY_NODE_MINISTERE_US),
    DIRECTION(OrganigrammeType.DIRECTION, "PASTE_DIRECTION", EpgActionEnum.COPY_NODE_MINISTERE_US);

    EpgOrganigrammeCopyActionEnum(OrganigrammeType type, String pasteActionPrefix, EpgActionEnum copyAction) {
        this.type = type;
        this.pasteActionPrefix = pasteActionPrefix;
        this.copyAction = copyAction;
    }

    private final OrganigrammeType type;
    private final String pasteActionPrefix;
    private final EpgActionEnum copyAction;

    public OrganigrammeType getType() {
        return type;
    }

    public String getPasteActionPrefix() {
        return pasteActionPrefix;
    }

    public EpgActionEnum getCopyAction() {
        return copyAction;
    }

    public static EpgOrganigrammeCopyActionEnum getByOrganigrammeType(OrganigrammeType enumValue) {
        return Stream.of(values()).filter(v -> v.getType().equals(enumValue)).findFirst().orElse(null);
    }
}
