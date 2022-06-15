package fr.dila.solonepg.core.util;

import fr.dila.solonepg.api.constant.SolonEpgRequeteurConstant;
import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.core.recherche.query.ActiviteNormativeQueryAssembler;
import fr.dila.st.api.recherche.QueryAssembler;
import fr.dila.st.api.service.JointureService;
import fr.dila.st.api.service.RequeteurService;
import fr.dila.st.core.service.STServiceLocator;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Classe utilitaire pour les requêtes EPG
 *
 * @author jgomez
 *
 */
public class RequeteurUtils {

    public static QueryAssembler getQueryAssembler(String name) {
        final JointureService jointureService = STServiceLocator.getJointureService();
        return jointureService.getQueryAssembler(name);
    }

    /**
     * Retourne le query assembler utilisé pour la recherche de dossier experte
     *
     * @return l'assembler des dossiers
     */
    public static QueryAssembler getDossierAssembler() {
        return getQueryAssembler(SolonEpgRequeteurConstant.REQUETEUR_EXPERT_DOSSIER_ASSEMBLER);
    }

    /**
     * Retourne le query utilisé pour la recherche de l'activité normative experte
     *
     * @return l'assembler de l'activité normative
     *
     */
    public static QueryAssembler getRechercheActiviteNormativeAssembler() {
        return getQueryAssembler(SolonEpgRequeteurConstant.REQUETEUR_ACTIVITE_NORMATIVE_DOSSIER_ASSEMBLER);
    }

    /**
     * Retourne le query utilisé pour la recherche de l'activité normative experte
     *
     * @param anEnum l'Activité normative sur lequel l'assembleur est configuré
     * @param whereClause la contrainte de l'assembleur
     * @return l'assembler de l'activité normative, prêt à générer la requête
     *
     */
    public static QueryAssembler getRechercheActiviteNormativeAssembler(
        ActiviteNormativeEnum anEnum,
        String whereClause
    ) {
        ActiviteNormativeQueryAssembler assembler = (ActiviteNormativeQueryAssembler) getRechercheActiviteNormativeAssembler();
        assembler.setWhereClause(whereClause);
        assembler.setActiviteNormativeEnum(anEnum);
        return assembler;
    }

    /**
     * Retourne le query assembler de la recherche de l'activité normative, configuré pour la recherche des lois
     *
     * @param whereClause les contraintes de la requête
     * @return l'assembler de l'activité normative, paramétré sur les lois
     * @return
     */
    public static ActiviteNormativeQueryAssembler getApplicationLoiAssembler(String whereClause) {
        return (ActiviteNormativeQueryAssembler) getRechercheActiviteNormativeAssembler(
            ActiviteNormativeEnum.APPLICATION_DES_LOIS,
            whereClause
        );
    }

    /**
     * Retourne la requête générée par le requêteur de l'activité normative (avec la résolution des dates dynamiques)
     *
     * @param anEnum l'Activité normative sur lequel l'assembleur est configuré
     * @param queryPart la contrainte de l'assembleur
     * @return la requête générée par le requêteur
     *
     */
    public static String getPatternAN(CoreSession session, ActiviteNormativeEnum anEnum, String queryPart) {
        RequeteurService requeteurService = STServiceLocator.getRequeteurService();
        QueryAssembler assembler = getRechercheActiviteNormativeAssembler(anEnum, queryPart);
        return requeteurService.getPattern(session, assembler, queryPart);
    }

    /**
     * Retourne la requête générée par le requêteur de l'activité normative (configuré sur l'application des lois, avec la résolution des dates dynamiques)
     *
     * @param anEnum l'Activité normative sur lequel l'assembleur est configuré
     * @param queryPart la contrainte de l'assembleur
     * @return la requête générée par le requêteur
     *
     */
    public static String queryApplicationLoi(CoreSession session, String queryPart) {
        return getPatternAN(session, ActiviteNormativeEnum.APPLICATION_DES_LOIS, queryPart);
    }

    private RequeteurUtils() {
        // Private default constructor
    }
}
