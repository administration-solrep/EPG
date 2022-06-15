package fr.dila.solonepg.rest.api;

import fr.sword.xsd.solon.epg.AttribuerNorRequest;
import fr.sword.xsd.solon.epg.AttribuerNorResponse;
import fr.sword.xsd.solon.epg.ChercherDossierRequest;
import fr.sword.xsd.solon.epg.ChercherDossierResponse;
import fr.sword.xsd.solon.epg.ChercherModificationDossierRequest;
import fr.sword.xsd.solon.epg.ChercherModificationDossierResponse;
import fr.sword.xsd.solon.epg.CreerDossierRequest;
import fr.sword.xsd.solon.epg.CreerDossierResponse;
import fr.sword.xsd.solon.epg.DonnerAvisCERequest;
import fr.sword.xsd.solon.epg.DonnerAvisCEResponse;
import fr.sword.xsd.solon.epg.ModifierDossierCERequest;
import fr.sword.xsd.solon.epg.ModifierDossierCEResponse;
import fr.sword.xsd.solon.epg.ModifierDossierRequest;
import fr.sword.xsd.solon.epg.ModifierDossierResponse;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Interface gerant toutes les operations sur epg.
 */
public interface EpgDelegate {
    /**
     * service attribuerNor :
     *
     * Service d’attribution de NOR permettant à une application tierce de créer un dossier S.O.L.O.N. II avec un socle
     * minimum de métadonnées requises pour l’établissement du bordereau d’un dossier S.O.L.O.N. et d’obtenir un NOR.
     * Remarque : l'année est connue
     *
     * @param request
     * @return
     * @throws Exception
     */
    AttribuerNorResponse attribuerNor(AttribuerNorRequest request) throws Exception;

    /**
     * service chercherDossierEpg :
     *
     * Service de mise à disposition des dossier : Requête par jeton Requête par liste de nor.
     *
     * @param request
     * @return
     * @throws Exception
     */
    ChercherDossierResponse chercherDossierEpg(ChercherDossierRequest request) throws Exception;

    /**
     * service donnerAvisCE : Service de transmission d'envoi des documents finaux, une fois que l'avis du Conseil
     * d'Etat est rendu.
     *
     * @param request
     * @return
     * @throws Exception
     */
    DonnerAvisCEResponse donnerAvisCE(DonnerAvisCERequest request) throws Exception;

    /**
     * service modifierDossierCE : Permet au CE de modifier les donnees qui lui sont propre dans un dossier : - nor
     * [1..1] : l'identifiant du dossier - section_ce [1..1] : la section
     *
     * @param request
     * @return
     * @throws Exception
     */
    ModifierDossierCEResponse modifierDossierCE(ModifierDossierCERequest request) throws Exception;

    /**
     * Service creerDossier Permet de créer un dossier A la création, les informations de responsable de l'acte sont
     * repris du compte utilisateur - type_acte [1..1] : type d'acte à créer - uniquement AVIS à court terme -
     * code_entite [1..1] : code de NOR de l'entité (ex : FDJ) - code_direction [1..1] : code de la direction de
     * l'entité (ex : J)
     *
     * @param request
     * @return response
     */
    CreerDossierResponse creerDossier(CreerDossierRequest request);

    /**
     * Service modifierDossier Finalise la transmition des informations et valide l'étape courante - dossier [1..1] :
     * complément à apporter au dossier epg (métadonnée, fichier)
     *
     * @param request
     * @return response
     */
    ModifierDossierResponse modifierDossier(ModifierDossierRequest request);

    /**
     * service chercherModificationDossier :
     *
     * Service de mise à disposition des modifications des dossier : Requête par jeton Requête par nor Requête par
     * numéro isa
     *
     * @param request
     * @return
     * @throws Exception
     */
    ChercherModificationDossierResponse chercherModificationDossierEpg(ChercherModificationDossierRequest request)
        throws Exception;

    /**
     * Indique si la fonctionnalité d'envoi des informations de publication au CE
     * est activée.
     *
     * @return
     */
    boolean isChercherModificationDossierPublicationActivated(CoreSession session);
}
