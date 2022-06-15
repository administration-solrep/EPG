package fr.dila.solonepg.ui.services.actions;

import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.fonddossier.FondDeDossierFile;
import fr.dila.st.api.vocabulary.VocabularyEntry;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.Calendar;
import java.util.List;

/**
 * Service permettant de gérer la distribution des dossiers.
 *
 * @author YGU
 */
public interface EpgDossierDistributionActionService {
    /*
     * Vider l'outil de sélection
     */
    void viderSelectionTool(SpecificContext context);
    /**
     * Valide l'étape correspondant au DossierLink en cours.
     *
     */
    void validerEtapeAndAddMessage(SpecificContext context);

    /**
     * Valide plusieur étapes provenant de la selection.
     *
     */
    void validerEtapeMassSelection(SpecificContext context);

    /**
     * Valide avec correction l'étape correspondant au DossierLink en cours.
     *
     */
    void validerEtapeCorrection(SpecificContext context);

    /**
     * Valide l'étape correspondant au DossierLink en cours avec la sortie "non concerné".
     */
    void validerEtapeNonConcerne(SpecificContext context);

    /**
     * Valide avec refus l'étape correspondant au DossierLink en cours.
     *
     */
    void validerEtapeRefus(SpecificContext context);

    /**
     * Valide l'étape correspondant au DossierLink en cours avec la sortie "retour pour modification".
     * @param retourDAN
     * 			id du poste du DAN sélectionné
     */
    void validerEtapeRetourModification(SpecificContext contex);

    /**
     * Retourne vrai si l'utilisateur a les droits pour afficher les boutons saisine rectificative et envoi de pièces
     * complémentaires
     */
    boolean canSendSaisineOrPieceComplementaire(SpecificContext context);

    /**
     * FEV507 : Contrôle des avis des chargés de mission avant envoi en publication. Cette méthode est appelée à chaque
     * action sur le dossier.
     */
    boolean checkChargesDeMissionAvantDILAJO(SpecificContext context);

    /**
     * Appelée lorsqu'on affiche un dossier
     *
     */
    boolean checkPublicationConjointeAvantDILAJO(SpecificContext context);

    /**
     * Coper la feuille de route du dossier saisi
     */
    void copierFdrDossier(SpecificContext context);

    /**
     * Effectue l'envoi de pièce complémentaire - avec la création d'un jeton pour chaque pièce complémentaire envoyée
     *
     */
    void doEnvoiPieceComplementaire(SpecificContext context);

    /**
     * Effectue la saisine rectificative - avec la création d'un jeton pour chaque fichier envoyé
     *
     */
    void doSaisineRectificative(SpecificContext context);

    /**
     * Retourne le libellé des boutons d'action de la feuille de route Si un message spécifique au type d'étape existe,
     * il est retourné, sinon le message correspondant à labelKey est retourné.
     *
     */
    String getActionFeuilleRouteButtonLabel(SpecificContext context, final String labelKey);

    /**
     * Retourne le libellé des boutons d'action de la feuille de route Si un message spécifique au type d'étpae existe,
     * il est retourné, sinon le message correspondant à labelKey est retourné.
     */
    String getActionFeuilleRouteButtonLabel(
        SpecificContext context,
        final String routingTaskType,
        final String labelKey
    );

    /**
     * Méthode qui renvoi la date de saisine rectificative la plus récente. Si date de saisine rectificative non
     * trouvée, renvoi la date de début de l'étape Pour avis CE
     *
     */
    Calendar getDateDerniereSaisineRectificative(SpecificContext context, Dossier dossier);

    /**
     * Méthode qui renvoi la date de transmission des pièces complémentaires la plus récente. Si date de transmission de
     * pièces complémentaires non trouvée, renvoi la date de début de l'étape Pour avis CE
     *
     */
    Calendar getDateDerniereTransmissionPiecesComplementaires(SpecificContext context, Dossier dossier);

    /**
     * Getter de retourDANList.
     */
    List<VocabularyEntry> getRetourDANList(SpecificContext context);

    /**
     * Retourne vrai si le dossier en cours est à l'état pour initialisation.
     *
     */
    boolean isCurrentDossierPourInitialisation(SpecificContext context);

    /**
     * Détermine si l'utilisateur a le droit de modifier un dossier distribué à l'état "Pour impression".
     *
     */
    boolean isEtapePourImpressionUpdater(SpecificContext context);

    /**
     * Verifie que le nor saisie pour la copie de la fdr est valide
     *
     */
    boolean isNorFdrCopieValide(SpecificContext context, String norDossierCopieFdr);

    /**
     * Lance un dossier.
     *
     */
    void lancerDossierAndAddMessage(SpecificContext context);

    void norAttribue(SpecificContext context);

    /**
     * Redémarre un dossier dont la feuille de route a été terminée.
     */
    void redemarrerDossier(SpecificContext context);

    /**
     * Redémarre un dossier abandonne
     *
     */
    void relancerDossierAbandonne(SpecificContext context);

    /**
     * Substitution de la feuille de route en cours : - Annule la feuille de route ; - Démarre une nouvelle feuille de
     * route.
     */
    void substituerRoute(SpecificContext context);

    List<FondDeDossierFile> prepareSaisineRectificative(SpecificContext context);

    List<FondDeDossierFile> prepareEnvoiPieceComplementaire(SpecificContext context);
}
