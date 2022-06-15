package fr.dila.solonepg.ui.services;

import fr.dila.solonepg.ui.bean.SelectionDto;
import fr.dila.solonepg.ui.enums.EpgSelectionTypeEnum;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface EpgSelectionToolUIService {
    void addDocToSelection(SpecificContext context);

    void removeDocFromSelection(SpecificContext context);

    void setSelectionList(SpecificContext context, Map<String, SelectionDto> selectionList);

    void setSelectionType(SpecificContext context, EpgSelectionTypeEnum type);

    EpgSelectionTypeEnum getSelectionType(SpecificContext context);

    List<SelectionDto> getSelectionList(SpecificContext context);

    boolean isInSelectionTool(SpecificContext context, String id);

    void addMultipleItemsToSelection(SpecificContext context);

    void createListDemandeEpreuve(SpecificContext context);

    void createListDemandePublication(SpecificContext context);

    void createListMiseEnSignature(SpecificContext context);

    String removeDocIdFromMap(SpecificContext context);

    void initSelectionActionContext(SpecificContext context);

    void addDossiersToDossiersSuivis(SpecificContext context);

    void abandonnerDossiers(SpecificContext context);

    void reattribuerDossiers(SpecificContext context);

    void deleteIdFromSelectionTool(SpecificContext context);

    void substituerMassRoute(SpecificContext context);

    void substituerPostes(SpecificContext context);

    void createListeSignatureIfAllow(SpecificContext context, List<DocumentModel> docs, boolean isSelectionTools);

    void transfererDossiers(SpecificContext context);
}
