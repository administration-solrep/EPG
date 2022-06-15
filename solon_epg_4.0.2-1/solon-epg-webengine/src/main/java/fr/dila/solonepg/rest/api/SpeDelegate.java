package fr.dila.solonepg.rest.api;

import fr.sword.xsd.solon.spe.EnvoyerPremiereDemandePERequest;
import fr.sword.xsd.solon.spe.EnvoyerPremiereDemandePEResponse;
import fr.sword.xsd.solon.spe.EnvoyerRetourPERequest;
import fr.sword.xsd.solon.spe.EnvoyerRetourPEResponse;

/**
 * Interface permettant de gerer toutes les operations sur spe
 */
public interface SpeDelegate {
    /**
     * service envoyerPremiereDemandePE :
     *
     *  message correspondant à une premiere demande de publication ou d'epreuvage.
     *  typeDemande : specifie le type de demande (PUBLICATION ou EPREUVAGE)
     *  bordereau : une liste de metadonnée associees à la demande
     *  parapheur : les fichiers qui constituent la demande
     *
     * @param request
     * @return EnvoyerPremiereDemandePEResponse
     * @throws Exception
     */
    EnvoyerPremiereDemandePEResponse envoyerPremiereDemandePE(EnvoyerPremiereDemandePERequest request) throws Exception;

    /**
     * service envoyerRetourPE :
     *
     * message de retour de publication
     *      type : type du retour (PUBLICATION ou EPREUVAGE)
     *      retourPublication : si type=PUBLICATION
     *      retourEpreuvage : si type=EPREUVAGE
     *
     * @param request
     * @return EnvoyerRetourPEResponse
     * @throws Exception
     */
    public EnvoyerRetourPEResponse envoyerRetourPE(EnvoyerRetourPERequest request) throws Exception;
}
