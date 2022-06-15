package fr.dila.solonepg.core.recherche.dossier.traitement;

import fr.dila.st.core.util.FullTextUtil;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Génère la clause pour la recherche fulltext à partir de la liste des EPGFulltextIndexEnum utilisés.
 *
 * @author jgomez
 *
 */
public class FulltextSubClauseGenerator {
    private static final String FILE_ECM_FULLTEXT = "f.ecm:fulltext";

    /**
     * Donne une recherche fulltext sur l'index (opérateur de stemming appliqué)
     *
     * @param content
     * @return
     */
    public String getFullText(String content, List<String> filetypeIds) {
        StringBuffer fulltext = new StringBuffer("");
        fulltext
            .append("(")
            .append(FILE_ECM_FULLTEXT)
            .append(" = \"")
            .append(FullTextUtil.parseContent(content))
            .append("\"")
            .append(getRestriction(filetypeIds))
            .append(")");
        return fulltext.toString();
    }

    /**
     * Restreint la recherche uniquement aux fichiers du bon type.
     *
     * @return
     */
    private String getRestriction(List<String> filetypeIds) {
        StringBuffer buffer = new StringBuffer(" AND f.filepg:filetypeId ");
        if (filetypeIds.size() == 1) {
            buffer.append(" = ").append(filetypeIds.get(0));
        } else {
            String inFiletypeIds = StringUtils.join(filetypeIds, ",");
            buffer.append(" IN (").append(inFiletypeIds).append(")");
        }
        //		Opti recherche fulltext desactivée
        //		buffer.append(" AND f.ver:islatest = 1");
        return buffer.toString();
    }

    /**
     * Donne une recherche exacte sur l'index
     *
     * @param content
     * @return
     */
    public String getRechercheExacte(final String content, List<String> filetypeIds) {
        // On enlève les sauts de lignes, attention : cette ligne rend la recherche exacte erronée, mais l'enlever
        // provoque des erreurs.
        String contentCleaned = FullTextUtil.cleanContent(content);
        // On supprime les accolades fermantes
        contentCleaned = contentCleaned.replaceAll("}", "");

        StringBuffer rechercheExacte = new StringBuffer("");
        rechercheExacte
            .append("(")
            .append(FILE_ECM_FULLTEXT)
            .append(" = \"")
            .append(contentCleaned)
            .append("\"")
            .append(getRestriction(filetypeIds))
            .append(")");
        return rechercheExacte.toString();
    }
}
