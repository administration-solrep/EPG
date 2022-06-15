package fr.dila.solonepg.ui.enums;

import java.util.stream.Stream;

public enum EpgTypeActeFragmentEnum {
    ARRETE("arrete", "fragments/travail/creation-dossier-arrete"),
    LOI("loi", "fragments/travail/creation-dossier-loi"),
    DECRTET("decret", "fragments/travail/creation-dossier-decret"),
    ORDONNANCE("ordonnance", "fragments/travail/creation-dossier-ordonnance");

    private String familleTypeActe;
    private String fragment;

    EpgTypeActeFragmentEnum(String familleTypeActe, String fragment) {
        this.familleTypeActe = familleTypeActe;
        this.fragment = fragment;
    }

    public String getFamilleTypeActe() {
        return familleTypeActe;
    }

    public String getFragment() {
        return fragment;
    }

    public static EpgTypeActeFragmentEnum startsWithFamilleTypeActe(String value) {
        return Stream.of(values()).filter(o -> value.startsWith(o.getFamilleTypeActe())).findFirst().orElse(null);
    }
}
