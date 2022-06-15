package fr.dila.solonepg.ui.bean.pan;

import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.solonepg.ui.helper.EpgDossierListHelper;
import fr.dila.ss.ui.bean.SSDossierList;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.ui.bean.ColonneInfo;
import fr.dila.st.ui.enums.SortOrder;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPanList<T extends AbstractMapDTO> extends SSDossierList<T> {
    protected String currentSection;
    protected String currentTab;
    protected String currentSubtab;

    public AbstractPanList() {
        super();
    }

    public AbstractPanList(SpecificContext context, List<T> dtoList) {
        super();
        currentSection = context.getFromContextData(PanContextDataKey.CURRENT_SECTION);
        currentTab = context.getFromContextData(PanContextDataKey.CURRENT_TAB);
        currentSubtab = context.getFromContextData(PanContextDataKey.CURRENT_SUB_TAB);

        setListe(new ArrayList<>(dtoList));
        setNbTotal(dtoList.size());
        buildColonnes(context);
    }

    public abstract void buildColonnes(SpecificContext context);

    @Override
    public ColonneInfo buildColonne(
        String label,
        String userColumn,
        List<String> lstUserVisibleColumns,
        boolean sortable,
        String name,
        SortOrder value,
        Boolean isLabelVisible,
        Integer order
    ) {
        return EpgDossierListHelper.buildColonne(
            label,
            userColumn,
            lstUserVisibleColumns,
            sortable,
            name,
            value,
            isLabelVisible,
            order
        );
    }
}
