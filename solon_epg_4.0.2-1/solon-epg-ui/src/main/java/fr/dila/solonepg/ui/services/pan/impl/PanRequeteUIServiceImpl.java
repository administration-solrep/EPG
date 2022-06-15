package fr.dila.solonepg.ui.services.pan.impl;

import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.ui.bean.pan.AbstractPanSortedList;
import fr.dila.solonepg.ui.contentview.PanMainTableauPageProvider;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.services.pan.PanRequeteUIService;
import fr.dila.solonepg.ui.services.pan.PanUIService;
import fr.dila.solonepg.ui.services.pan.PanUIServiceLocator;
import fr.dila.solonepg.ui.th.bean.LoisSuiviesForm;
import fr.dila.ss.core.enumeration.TypeChampEnum;
import fr.dila.ss.ui.bean.RequeteExperteDTO;
import fr.dila.ss.ui.bean.RequeteLigneDTO;
import fr.dila.ss.ui.enums.SSContextDataKey;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.core.requete.recherchechamp.descriptor.ChampDescriptor;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class PanRequeteUIServiceImpl implements PanRequeteUIService {

    @SuppressWarnings("unchecked")
    @Override
    public <T extends AbstractMapDTO> AbstractPanSortedList<T> doRechercheExperte(SpecificContext context) {
        LoisSuiviesForm loisSuiviesForm = ObjectHelper.requireNonNullElseGet(
            context.getFromContextData(PanContextDataKey.PAN_FORM),
            LoisSuiviesForm::new
        );
        RequeteExperteDTO dto = context.getFromContextData(SSContextDataKey.REQUETE_EXPERTE_DTO);

        PanMainTableauPageProvider genProvider = loisSuiviesForm.getPageProvider(
            context.getSession(),
            "panMainTableauPageProvider",
            "d.",
            null
        );

        Set<String> uniqueCategories = dto
            .getRequetes()
            .stream()
            .map(RequeteLigneDTO::getChamp)
            .map(ChampDescriptor::getCategorie)
            .collect(Collectors.toSet());

        if (uniqueCategories.contains("decret")) {
            genProvider.addSchema("ActiviteNormative AS m", "d.texm:mesureIds = m.ecm:uuid");
            genProvider.addSchema("ActiviteNormative AS de", "m.texm:decretIds = de.ecm:uuid");
        }
        if (uniqueCategories.contains("mesure")) {
            genProvider.addSchema("ActiviteNormative AS m", "d.texm:mesureIds = m.ecm:uuid");
        }
        String currentSection = context.getFromContextData(PanContextDataKey.CURRENT_SECTION);
        ActiviteNormativeEnum anEnum = ActiviteNormativeEnum.getById(currentSection);

        genProvider.setAttributSchema(anEnum.getAttributSchema());
        genProvider.setExtraWhere(getWhereClause(dto.getRequetes()));

        PanUIService panUIService = PanUIServiceLocator.getPanUIService();
        return panUIService.callGenericProvider(context, genProvider, anEnum);
    }

    /**
     * Retourne la clause where à partir d'une liste de RequeteLigneDTO
     *
     * @param requeteCriteria
     * @return
     */
    private String getWhereClause(List<RequeteLigneDTO> requeteCriteria) {
        return requeteCriteria.stream().map(this::getWhereConditionFromRequeteLigne).collect(Collectors.joining(" "));
    }

    /**
     * Retourne une requête NXQL à partie d'une RequeteLigneDTO
     *
     * @param ligne
     * @return
     */
    private String getWhereConditionFromRequeteLigne(RequeteLigneDTO ligne) {
        StringBuilder condition = new StringBuilder();
        if (StringUtils.isNotBlank(ligne.getAndOr())) {
            condition.append("ET".equals(ligne.getAndOr()) ? "AND " : "OR ");
        }
        condition.append(ligne.getChamp().getField() + " ");
        condition.append(ligne.getOperator().getOperator() + " ");
        if (
            TypeChampEnum.SIMPLE_SELECT_BOOLEAN.name().equals(ligne.getChamp().getTypeChamp()) ||
            TypeChampEnum.SIMPLE_SELECT_BOOLEAN_ES.name().equals(ligne.getChamp().getTypeChamp())
        ) {
            condition.append(ligne.getValue().get(0));
        } else {
            condition.append(ligne.getOperator().getDisplayFunction().apply(ligne.getValue()));
        }

        return condition.toString();
    }
}
