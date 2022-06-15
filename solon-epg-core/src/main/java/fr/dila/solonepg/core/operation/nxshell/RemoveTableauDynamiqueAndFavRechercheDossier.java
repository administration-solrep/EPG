package fr.dila.solonepg.core.operation.nxshell;

import static fr.sword.naiad.nuxeo.ufnxql.core.query.QueryUtils.doUFNXQLQueryForIdsList;
import static java.lang.String.format;

import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.operation.STVersion;
import java.util.List;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;

/**
 * Suppression des anciens Tableaux Dynamique et Favoris de Recherche de
 * dossiers qui sont stockés avec des requêtes NXQL
 *
 * @author SCE
 *
 */
@Operation(id = RemoveTableauDynamiqueAndFavRechercheDossier.ID, category = "Document")
@STVersion(version = "4.0.0")
public class RemoveTableauDynamiqueAndFavRechercheDossier {
    public static final String ID = "Tableau.Dynamique.Favoris.Recherche.Dossier.Remove";

    private static final STLogger LOG = STLogFactory.getLog(RemoveTableauDynamiqueAndFavRechercheDossier.class);

    @Context
    private CoreSession session;

    @OperationMethod
    public void removeTableauxDynamiqueAndFavorisRechercheDossier() {
        String queryTDs = "Select d.ecm:uuid as id From TableauDynamique as d";
        List<String> ids = doUFNXQLQueryForIdsList(session, queryTDs, null);

        String queryFavsDossier =
            "Select d.ecm:uuid as id From FavorisRecherche as d WHERE d.favrec:type = 'Dossier' or d.favrec:type = 'DossierSimple'";
        ids.addAll(doUFNXQLQueryForIdsList(session, queryFavsDossier, null));
        LOG.info(
            session,
            STLogEnumImpl.DEFAULT,
            format("%d tableaux dynamique et favoris de recherche de dossier à supprimer : %s", ids.size(), ids)
        );

        session.removeDocuments(ids.stream().map(IdRef::new).toArray(DocumentRef[]::new));
    }
}
