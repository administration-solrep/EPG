package fr.dila.solonepg.ui.services;

import fr.dila.st.ui.th.model.SpecificContext;
import java.util.List;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface FavorisRechercheModeleFeuilleRouteUIService {
    /**
     * Retourne les critères de recherche des modèles de feuille de route sous forme textuelle.
     *
     * @return Critères de recherche des modèles de feuille de route sous forme textuelle
     */
    String getSearchQueryString(SpecificContext context);

    /**
     * Retourne les critères de recherche des modèles de feuille de route sous forme textuelle.
     *
     * @return Critères de recherche des modèles de feuille de route sous forme textuelle
     */
    List<Object> getSearchQueryParameter(SpecificContext context);

    /**
     * Retourne les critères de recherche des modèles de feuille de route pour l'affichage.
     *
     * @return Critères de recherche pour l'affichage
     */
    String getSearchQueryStringForDisplay(SpecificContext context, DocumentModel favoriRechercheDoc);

    DocumentModel mapFormToFavorisDoc(SpecificContext context);
}
