package fr.dila.solonepg.ui.enums;

import com.google.common.collect.Lists;
import fr.dila.solonmgpp.api.enumeration.MgppCorbeilleName;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public enum DossierFiltreEnum {
    DOSSIERS(
        Arrays.asList(),
        Lists.newArrayList(
            EpgFiltreEnum.NUMERO_NOR,
            EpgFiltreEnum.TITRE_ACTE,
            EpgFiltreEnum.DATE_ARRIVEE_DOSSIER_LINK,
            EpgFiltreEnum.DATE_PUBLICATION_SOUHAITEE,
            EpgFiltreEnum.TYPE_ACTE
        ),
        "mgppMessageListPageProvider",
        ""
    );

    private final List<EpgFiltreEnum> lstFiltres;
    private final List<MgppCorbeilleName> lstCorbeilles;
    private final String providerName;
    private final String xpathPrefix;

    DossierFiltreEnum(
        List<MgppCorbeilleName> corbeilles,
        List<EpgFiltreEnum> allFiltres,
        String providerName,
        String xpathPrefix
    ) {
        this.lstFiltres = allFiltres;
        this.lstCorbeilles = corbeilles;
        this.providerName = providerName;
        this.xpathPrefix = xpathPrefix;
    }

    public List<EpgFiltreEnum> getLstFiltres() {
        return lstFiltres;
    }

    public List<String> getLstFiltresNuxeoFields() {
        return lstFiltres
            .stream()
            .filter(filtre -> StringUtils.isNotBlank(filtre.getXpath()))
            .map(filtre -> xpathPrefix + filtre.getXpath())
            .collect(Collectors.toList());
    }

    public List<MgppCorbeilleName> getLstCorbeilles() {
        return lstCorbeilles;
    }

    public static DossierFiltreEnum fromDossier(MgppCorbeilleName corbeille) {
        return Stream
            .of(values())
            .filter(filtre -> filtre.getLstCorbeilles().contains(corbeille))
            .findFirst()
            .orElse(DOSSIERS);
    }

    public String getProviderName() {
        return providerName;
    }

    public String getXpathPrefix() {
        return xpathPrefix;
    }
}
