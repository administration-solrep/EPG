package fr.dila.solonepg.ui.enums;

import fr.dila.solonepg.api.recherche.FavorisConsultation;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.nuxeo.ecm.core.api.DocumentModel;

public enum EPGFavorisConsultationEnum {
    DOSSIER(
        FavorisConsultation::getDossiersId,
        (context, documentModelIdList) -> context.getSession().getDocuments(documentModelIdList, null)
    ),
    USER(
        FavorisConsultation::getUsers,
        (context, documentModelIdList) -> STServiceLocator.getSTUserSearchService().getUsersFromIds(documentModelIdList)
    ),
    FDR(
        FavorisConsultation::getFdrsId,
        (context, documentModelIdList) -> context.getSession().getDocuments(documentModelIdList, null)
    );

    private final Function<FavorisConsultation, List<String>> nbFavorisFunction;
    private final BiFunction<SpecificContext, List<String>, List<DocumentModel>> documentModelListFunction;

    EPGFavorisConsultationEnum(
        Function<FavorisConsultation, List<String>> nbFavorisFunction,
        BiFunction<SpecificContext, List<String>, List<DocumentModel>> documentModelListFunction
    ) {
        this.nbFavorisFunction = nbFavorisFunction;
        this.documentModelListFunction = documentModelListFunction;
    }

    public int getNbFavoris(FavorisConsultation favorisConsultation) {
        return nbFavorisFunction.apply(favorisConsultation).size();
    }

    public List<DocumentModel> getDocumentModelList(SpecificContext context, List<String> documentModelIdList) {
        return documentModelListFunction.apply(context, documentModelIdList);
    }
}
