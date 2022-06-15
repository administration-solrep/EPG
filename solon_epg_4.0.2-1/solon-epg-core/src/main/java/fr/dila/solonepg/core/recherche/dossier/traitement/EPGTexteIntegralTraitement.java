package fr.dila.solonepg.core.recherche.dossier.traitement;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.st.api.recherche.RequeteTraitement;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Classe pour le traitement des index full text.
 * On prend le texte entré par l'utilisateur et on construit une requête avec les index fulltext que
 * celui-ci a demandé dans la sélection.
 * @author jgomez
 *
 */
public class EPGTexteIntegralTraitement implements RequeteTraitement<RequeteDossierSimple> {
    public static final Log log = LogFactory.getLog(EPGTexteIntegralTraitement.class);

    @Override
    public void doBeforeQuery(RequeteDossierSimple requete) {
        String texteIntegral = requete.getTexteIntegral();
        if (StringUtils.isBlank(texteIntegral)) {
            requete.setSousClauseTexteIntegral(null);
            return;
        }
        List<String> filetypeIds = requete.getFiletypeIds();
        if (filetypeIds.isEmpty()) {
            return;
        }
        FulltextSubClauseGenerator generator = new FulltextSubClauseGenerator();
        requete.setSousClauseTexteIntegral(generator.getRechercheExacte(texteIntegral, filetypeIds));
    }

    @Override
    public void doAfterQuery(RequeteDossierSimple requete) {}

    @Override
    public void init(RequeteDossierSimple requete) {}
}
