package fr.dila.solonepg.ui.enums;

import static java.util.function.Function.identity;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EpgAdminFddPositionEnum {
    AFTER,
    BEFORE,
    IN;

    private static final Map<String, EpgAdminFddPositionEnum> STRING_TO_ENUM = Stream
        .of(values())
        .collect(Collectors.toMap(EpgAdminFddPositionEnum::getKey, identity()));

    public String getKey() {
        return name().toLowerCase();
    }

    public static Optional<EpgAdminFddPositionEnum> fromString(String key) {
        return Optional.ofNullable(STRING_TO_ENUM.get(key));
    }
}
