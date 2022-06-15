package fr.dila.solonepg.core.recherche.query;

import fr.dila.solonepg.api.service.RequeteurDossierSimpleService;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.SortInfo;
import org.nuxeo.ecm.platform.query.api.PageProviderDefinition;
import org.nuxeo.ecm.platform.query.api.PageProviderService;
import org.nuxeo.ecm.platform.query.nxql.NXQLQueryBuilder;

/**
 *
 * Classe utilitaire pour donner la clause WHERE d'un model nuxeo.
 *
 * @author jgomez
 *
 */
public class EPGQueryModel {

    /**
     * Retourne la clause d'un model Nuxeo.
     * @param qmService
     * @param model
     * @param requeteModelName
     * @return
     */
    public String getRequetePart(
        PageProviderService pageproviderService,
        DocumentModel model,
        String requeteModelName
    ) {
        PageProviderDefinition def = pageproviderService.getPageProviderDefinition(requeteModelName);
        if (def == null) {
            return StringUtils.EMPTY;
        }

        SortInfo[] sortInfos = def.getSortInfos() != null
            ? def.getSortInfos().toArray(new SortInfo[0])
            : new SortInfo[0];
        String query = NXQLQueryBuilder.getQuery(model, def.getWhereClause(), null, sortInfos);

        // Analyser la partie de la requête concernant le numéro NOR
        Pattern norPattern = Pattern.compile("d.dos:numeroNor LIKE '[^']*'");
        Matcher norMatcher = norPattern.matcher(query);
        if (norMatcher.find() && query.contains(";")) {
            String norQueryOld = query.substring(norMatcher.start(), norMatcher.end());
            String norPart = norQueryOld.split(" ", 3)[2];
            String[] nors = norPart.substring(1, norPart.length() - 1).split(";");
            String norQueryNew = "";
            for (String nor : nors) {
                if (!nor.trim().isEmpty()) norQueryNew += "d.dos:numeroNor LIKE '" + nor.trim() + "' OR ";
            }
            if (!norQueryNew.isEmpty()) {
                norQueryNew = "(" + norQueryNew.substring(0, norQueryNew.length() - 4) + ")";
                query = query.replace(norQueryOld, norQueryNew);
            }
        }

        StringBuilder modifiedQuery = new StringBuilder(
            query.replace("SELECT * FROM Document", StringUtils.EMPTY).replace(" WHERE ", StringUtils.EMPTY)
        );

        // Bug nuxeo sur les subclause.
        if (
            RequeteurDossierSimpleService.REQUETE_TEXTE_INTEGRAL.equals(requeteModelName) && modifiedQuery.length() != 0
        ) {
            modifiedQuery.insert(0, "(");
            modifiedQuery.append(")");
        }
        return modifiedQuery.toString();
    }

    /**
     * Retourne les clauses de tous les models donnés en argument, liées par un AND (et avec des parenthèses).
     * @param qmService
     * @param model
     * @param modelNames
     * @return
     */
    public String getAndRequeteParts(
        PageProviderService pageproviderService,
        DocumentModel model,
        String... modelNames
    ) {
        List<String> clauses = new ArrayList<String>();
        for (String modelName : modelNames) {
            String requetePart = getRequetePart(pageproviderService, model, modelName);
            if (!StringUtils.isBlank(requetePart)) {
                clauses.add("(" + requetePart + ")");
            }
        }
        return StringUtils.join(clauses, " AND ");
    }
}
