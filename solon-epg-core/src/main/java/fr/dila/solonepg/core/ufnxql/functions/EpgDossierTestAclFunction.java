package fr.dila.solonepg.core.ufnxql.functions;

import static fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_DENY_READER;
import static fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_READER;
import static fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant.DOSSIER_RECHERCHE_READER;

import java.util.List;

/**
 * Fonction pour tester les acls.<br>
 * Gère les cas spécifiques pour les dossiers EPG.
 *
 * testDossierAcl(id) = 1,
 *   test l'acces en lecture sur le document avec l'id id
 *
 * testDossierAcl(id, 'READ') = 1,
 *
 *
 */
public class EpgDossierTestAclFunction extends EpgTestAclFunction {

    public EpgDossierTestAclFunction() {
        super();
    }

    @Override
    public String getName() {
        return "testDossierAcl";
    }

    @Override
    protected boolean skipTestAcl(List<String> paramPrincipals) {
        // pas la peine de tester les droits si l'utilisateur a le droit de
        // consulter tous les dossiers
        return (
            paramPrincipals.contains(DOSSIER_MESURE_NOMINATIVE_READER) &&
            paramPrincipals.contains(DOSSIER_RECHERCHE_READER)
        );
    }

    @Override
    protected boolean checkNegativeAcls(List<String> principals) {
        // teste si l'utilisateur n'a pas le droit de consulter les dossiers à
        // accès restreint
        return principals.contains(DOSSIER_MESURE_NOMINATIVE_DENY_READER);
    }
}
