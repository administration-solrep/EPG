package fr.dila.solonepg.core.recherche.dossier.traitement;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.st.api.recherche.RequeteTraitement;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.NuxeoException;

/**
 *  Le champ des statuts d'étape proposé dans la recherche est un mixte de 2 métadonnées de l'étape :
 *  - le cycle de vie d'une étape (en cours, à venir)
 *  - et le statut de validation d'une étape (uniquement dans le case d'une étape validé) : validé automatiquement, non concerné, etc ...
 *
 *  Le traitement suivant récupère l'information donnée par l'utilisateur, et place ces 2 champs pour effectuer
 *  des recherches dessus.
 *
 * @author jgomez
 *
 */
public class EPGEtapeValidationStatutTraitement implements RequeteTraitement<RequeteDossierSimple> {
    public static final Log LOGGER = LogFactory.getLog(EPGTexteIntegralTraitement.class);

    public static final String DEFAULT_ID = StringUtils.EMPTY;

    public static final String ALL_CHOICE = "all";

    @Override
    public void doBeforeQuery(RequeteDossierSimple requeteSimple) {
        String rechercheEtapeStatut = requeteSimple.getEtapeIdStatut();
        if (DEFAULT_ID.equals(rechercheEtapeStatut) || rechercheEtapeStatut == null) {
            requeteSimple.setEtapeCurrentCycleState(null);
            requeteSimple.setValidationStatutId(null);
            return;
        }
        String[] statuts = rechercheEtapeStatut.split("_");
        if (statuts.length != 2) {
            LOGGER.error(
                "Le traitement EPGEtapeValidationStatutTraitement a échoué, vérifier la forme des identifiants dans le fichier statut_etape_recherche_statut.csv"
            );
            LOGGER.error("Reçu : " + rechercheEtapeStatut);
            throw new NuxeoException(
                "Le vocabulaire statut_etape_recherche_statut doit posséder des identifiants de la forme <cycleState>-<validationStatutId>"
            );
        }
        String etapeCurrentCycleState = statuts[0];
        String validationStatutId = statuts[1];

        if (ALL_CHOICE.equals(validationStatutId)) {
            requeteSimple.setValidationStatutId(null);
        } else {
            requeteSimple.setValidationStatutId(validationStatutId);
        }
        requeteSimple.setEtapeCurrentCycleState(etapeCurrentCycleState);
    }

    @Override
    public void doAfterQuery(RequeteDossierSimple requeteSimple) {
        return;
    }

    @Override
    public void init(RequeteDossierSimple requeteSimple) {
        return;
    }
}
