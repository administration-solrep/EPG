package fr.dila.solonepg.api.util;

import fr.dila.solonepg.api.constant.SolonEpgAclConstant;

public final class EpgAclUtil {

    public static String getDossierRattachementMinAceUsername(String id) {
        return SolonEpgAclConstant.DOSSIER_RATTACH_MIN_ACE_PREFIX + id;
    }

    public static String getDossierRattachementDirAceUsername(String id) {
        return SolonEpgAclConstant.DOSSIER_RATTACH_DIR_ACE_PREFIX + id;
    }

    private EpgAclUtil() {}
}
