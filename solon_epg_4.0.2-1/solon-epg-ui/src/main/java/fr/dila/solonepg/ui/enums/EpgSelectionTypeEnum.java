package fr.dila.solonepg.ui.enums;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.ui.bean.SelectionDto;
import fr.dila.ss.api.constant.SSConstant;
import fr.dila.st.api.constant.STConstant;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import org.nuxeo.ecm.core.api.DocumentModel;

public enum EpgSelectionTypeEnum {
    DOSSIER(
        STConstant.DOSSIER_DOCUMENT_TYPE,
        "outilSelection.dossier.ajoute",
        EpgSelectionTypeEnum::addDossierToSelectionMap
    ),
    MODELE(
        SSConstant.FEUILLE_ROUTE_DOCUMENT_TYPE,
        "outilSelection.fdr.ajoutee",
        EpgSelectionTypeEnum::addModeleToSelectionMap
    );

    private String docType;
    private String message;
    private final BiConsumer<DocumentModel, Map<String, SelectionDto>> addFunction;

    EpgSelectionTypeEnum(
        String docType,
        String message,
        BiConsumer<DocumentModel, Map<String, SelectionDto>> addFunction
    ) {
        this.docType = docType;
        this.message = message;
        this.addFunction = addFunction;
    }

    public String getDocType() {
        return docType;
    }

    public String getMessage() {
        return message;
    }

    public BiConsumer<DocumentModel, Map<String, SelectionDto>> getAddFunction() {
        return addFunction;
    }

    public static EpgSelectionTypeEnum getByDocType(String docType) {
        return Stream.of(values()).filter(o -> o.getDocType().equals(docType)).findFirst().orElse(null);
    }

    private static void addModeleToSelectionMap(DocumentModel doc, Map<String, SelectionDto> selectionMap) {
        SolonEpgFeuilleRoute modele = doc.getAdapter(SolonEpgFeuilleRoute.class);
        selectionMap.put(doc.getId(), new SelectionDto(doc.getId(), modele.getName()));
    }

    private static void addDossierToSelectionMap(DocumentModel doc, Map<String, SelectionDto> selectionMap) {
        Dossier dossier = doc.getAdapter(Dossier.class);
        selectionMap.put(doc.getId(), new SelectionDto(doc.getId(), dossier.getNumeroNor()));
    }
}
