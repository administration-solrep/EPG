package fr.dila.solonepg.core.util;

import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * Surcharge de la classe utilitaire pour le fulltext.
 *
 * @author arolin
 */
public class FullTextUtil extends fr.dila.st.core.util.FullTextUtil {
    private static Pattern beginWithStarFullTextField;

    private static Pattern hasNumeroNorFieldWithNoFullText;

    private static Pattern hasNumeroNorField;

    /**
     * Pattern
     *
     * @return
     */
    public static Pattern getHasNumeroNorField() {
        if (hasNumeroNorField == null) {
            hasNumeroNorField =
                Pattern.compile(
                    ".*" + DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_QUERY_LIKE + "{1}.*",
                    Pattern.CANON_EQ
                );
        }
        return hasNumeroNorField;
    }

    public static Pattern getHasNumeroNorFieldWithNoFullText() {
        if (hasNumeroNorFieldWithNoFullText == null) {
            hasNumeroNorFieldWithNoFullText =
                Pattern.compile(
                    ".*([ \\(]|[d][\\.])" + DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_QUERY_LIKE + "{1}.*",
                    Pattern.CANON_EQ
                );
        }
        return hasNumeroNorFieldWithNoFullText;
    }

    public static Pattern getBeginWithStarFullTextField() {
        if (beginWithStarFullTextField == null) {
            beginWithStarFullTextField =
                Pattern.compile(
                    ".*" + DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_QUERY_LIKE + "+[ ]*['\"]\\*{1}.*",
                    Pattern.CANON_EQ
                );
        }
        return beginWithStarFullTextField;
    }

    /**
     * Signale si une requête fulltext commence par le caractère '*'.
     */
    public static boolean beginWithStarQuery(String query) {
        if (StringUtils.isEmpty(query)) {
            return false;
        }
        return getBeginWithStarFullTextField().matcher(query).find();
    }

    /**
     * Marque les champs "numeroNor" de la requête comme étant des champs de type fulltexte.
     *
     * @param query
     * @return query
     */
    public static String defineNumeroNorFieldAsFullTextField(String query) {
        if (hasNumeroNorFieldWithNoFullText(query)) {
            query =
                StringUtils.replace(
                    query,
                    " " + DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_QUERY_LIKE,
                    " " + DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PREFIX_FULL_TEXT
                );
            query =
                StringUtils.replace(
                    query,
                    "(" + DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_QUERY_LIKE,
                    "(" + DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PREFIX_FULL_TEXT
                );
            query =
                StringUtils.replace(
                    query,
                    "d." + DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_QUERY_LIKE,
                    "d." + DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_PREFIX_FULL_TEXT
                );
        }
        return query;
    }

    /**
     * Signale si une requête fulltext contient un champ "NumeroNor" qui n'est pas mappé comme champ fullText
     */
    public static boolean hasNumeroNorFieldWithNoFullText(String query) {
        if (StringUtils.isEmpty(query)) {
            return false;
        }

        return getHasNumeroNorFieldWithNoFullText().matcher(query).find();
    }

    /**
     * Met en majuscule le champ numéroNor.
     *
     * @param query
     * @return query
     */
    public static String upperNumeroNorField(String query) {
        if (hasNumeroNorField(query)) {
            // on récupère la dernière occurence du champ numéro nor
            int numeroNorIndex = query.lastIndexOf(DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_QUERY_LIKE);
            String queryUpdated = upperStringAndReplaceStarByPercent(query, numeroNorIndex, "'");
            if (queryUpdated != null) {
                return queryUpdated;
            }
            queryUpdated = upperStringAndReplaceStarByPercent(query, numeroNorIndex, "\"");
            if (queryUpdated != null) {
                return queryUpdated;
            }
        }
        return query;
    }

    /**
     * Signale si une requête contient un champ "NumeroNor".
     */
    public static boolean hasNumeroNorField(String query) {
        if (StringUtils.isEmpty(query)) {
            return false;
        }
        return getHasNumeroNorField().matcher(query).find();
    }

    /**
     * Met en majuscule et replace les '*' par des '%' une chaîne de caractère délimitée par un motif.
     * Renvoie null si il n'y pas de chaîne de caractère délimitée par le motif.
     *
     * @param query
     * @param beginIndex
     * @param endIndex
     * @param motif
     * @return
     */
    protected static String upperStringAndReplaceStarByPercent(String query, int beginIndex, String motif) {
        int quoteBeginIndex = query.indexOf(motif, beginIndex + 1);
        if (quoteBeginIndex == -1) {
            return null;
        }
        int endBeginIndex = query.indexOf(motif, quoteBeginIndex + 1);
        if (endBeginIndex == -1) {
            return null;
        }

        String queryBegin = query.substring(0, quoteBeginIndex);
        String queryToUpperCase = query.substring(quoteBeginIndex, endBeginIndex).toUpperCase();
        queryToUpperCase = replaceStarByPercent(queryToUpperCase);
        String queryEnd = query.substring(endBeginIndex, query.length());
        query = queryBegin + queryToUpperCase;
        if (!StringUtils.isEmpty(queryEnd)) {
            query = query + queryEnd;
        }
        return query;
    }
}
