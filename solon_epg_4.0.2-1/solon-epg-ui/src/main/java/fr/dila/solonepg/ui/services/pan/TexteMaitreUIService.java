package fr.dila.solonepg.ui.services.pan;

import fr.dila.solonepg.api.dto.DecretDTO;
import fr.dila.solonepg.api.dto.MesureApplicativeDTO;
import fr.dila.solonepg.ui.bean.pan.DecretsPanUnsortedList;
import fr.dila.solonepg.ui.bean.pan.MesuresApplicativesPanUnsortedList;
import fr.dila.solonepg.ui.enums.pan.PanContextDataKey;
import fr.dila.st.ui.th.model.SpecificContext;
import java.text.ParseException;
import java.util.Map;

public interface TexteMaitreUIService {
    /**
     * Sauvegarde du texte-maitre
     *
     * @param context contient :
     *                <ul>
     *                <li>{@link PanContextDataKey#TEXTE_MAITRE_ID} : id du texte
     *                maitre à sauvegarder</li>
     *                <li>{@link PanContextDataKey#TEXTE_MAITRE_DTO} : dto à
     *                sauvegarder</li>
     *                </ul>
     */
    void saveTexteMaitre(SpecificContext context);

    /**
     * Reload du texte maitre
     * @param context contient :
     *                <ul>
     *                <li>{@link PanContextDataKey#TEXTE_MAITRE_ID} : id du texte
     *                maitre à recharger</li>
     *                <li>{@link PanContextDataKey#TEXTE_MAITRE_DTO} : dto à
     *                recharger</li></ul>
     */
    String reloadTexteMaitre(SpecificContext context);

    /**
     * Reload des décrets
     * @param context contient :
     *                <ul>
     *                <li>{@link PanContextDataKey#RELOAD_FROM_DOSSIER} : boolean indiquant si on recharge depuis le dossier</li>
     *                <li>{@link PanContextDataKey#FIRST_TABLE_ID} : id du de la mesure applicative</li>
     *                <li>{@link PanContextDataKey#TEXTE_MAITRE_ID} : id du texte maitre</li>
     *                </ul>
     */
    Map<String, DecretDTO> reloadDecrets(SpecificContext context);

    /**
     * Mise à jour de la mesure
     * @param context contient :
     *                <ul>
     *                <li>{@link PanContextDataKey#FIRST_TABLE_ELT_DTO} : DTO de la mesure applicative</li>
     *                <li>{@link PanContextDataKey#TEXTE_MAITRE_ID} : id du texte maitre</li>
     *                </ul>
     */
    MesureApplicativeDTO setMesure(SpecificContext context);

    /**
     * Suppression de la mesure
     * @param context contient :
     *                <ul>
     *                <li>{@link PanContextDataKey#FIRST_TABLE_ID} : Id de la mesure applicative</li>
     *                <li>{@link PanContextDataKey#TEXTE_MAITRE_ID} : id du texte maitre</li>
     *                </ul>
     */
    Map<String, DecretDTO> removeMesure(SpecificContext context);

    /**
     * @param context contient :
     *                <ul>
     *                <li>{@link PanContextDataKey#TEXTE_MAITRE_ID} : Id de l'activité normative</li>
     *                <li>{@link PanContextDataKey#FIRST_TABLE_LIST} : Liste des mesures applicatives</li>
     *                <li>{@link PanContextDataKey#MESURE_APPLICATIVE_NUMBER} : Nombre de mesures applicatives vides à ajouter</li>
     *                <li>{@link PanContextDataKey#TEXTE_MAITRE_ID} : id du texte maitre</li>
     *                </ul>
     */
    String addNewMesure(SpecificContext context);

    String addNewDecret(SpecificContext context);

    DecretsPanUnsortedList getDecrets(SpecificContext context);

    void updateDecret(SpecificContext context) throws ParseException;

    String saveMesure(SpecificContext context);

    String saveDecret(SpecificContext context);

    String removeDecret(SpecificContext context);

    /**
     * Decoration de la ligne de la mesure selectionnée dans la table des mesure
     */
    String decorate(SpecificContext context);

    String computeLegifranceLink(SpecificContext context);

    String getTitreDivMesure(SpecificContext context);

    String getTitreDivMesureAppOrdo(SpecificContext context);

    String getTitreDivDecret(SpecificContext context);

    String getTitreDivDecretAppOrdo(SpecificContext context);

    void validerMesure(SpecificContext context);

    void updateMesure(SpecificContext context) throws ParseException;

    String getLienLegifranceFromJORF(String jorfLegifrance);

    DecretsPanUnsortedList getDecretsList(SpecificContext context);

    MesuresApplicativesPanUnsortedList getMesuresList(SpecificContext context);
}
