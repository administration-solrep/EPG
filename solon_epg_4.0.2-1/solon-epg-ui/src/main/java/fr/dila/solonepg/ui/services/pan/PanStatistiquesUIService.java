package fr.dila.solonepg.ui.services.pan;

import fr.dila.solonepg.api.activitenormative.ANReportEnum;
import fr.dila.solonepg.api.birt.BirtResultatFichier;
import fr.dila.solonepg.ui.bean.SuiviLibreReportDTO;
import fr.dila.solonepg.ui.bean.pan.PANExportParametreDTO;
import fr.dila.solonepg.ui.bean.pan.PanStatistiquesParamDTO;
import fr.dila.solonepg.ui.bean.pan.birt.PanBirtDTO;
import fr.dila.solonepg.ui.bean.pan.birt.PanBirtParamForm;
import fr.dila.solonepg.ui.th.model.bean.pan.PanStatistiquesParamForm;
import fr.dila.solonepg.ui.th.model.bean.pan.PanStatistiquesParamLegisEnCoursForm;
import fr.dila.solonepg.ui.th.model.bean.pan.PanStatistiquesParamLegisPrecForm;
import fr.dila.ss.api.birt.BirtReport;
import fr.dila.ss.ui.services.SSStatistiquesUIService;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.core.api.Blob;

public interface PanStatistiquesUIService extends SSStatistiquesUIService {
    /*
     * Génère le rapport avec les paramètres spécifiés, puis le stocke pour
     * l'utilisateur courant
     */
    void refreshReport(String statId, Map<String, String> birtParams, SpecificContext context);

    /*
     * Génère un DTO contenant d'une part les paramètres valorisés dans le
     * formulaire, d'autre part les valeurs back par défaut pour un rapport
     * donné
     */
    PanBirtDTO getBirtDTOFromContext(SpecificContext context, PanBirtParamForm form);

    boolean isRefreshExisting(SpecificContext context);

    boolean isResultatExisting(SpecificContext context);

    /*
     * Renvoie le blob de l'actuelle version du rapport stocké pour
     * l'utilisateur et le birt id spécifié
     */
    Blob getBlobCurrentReport(SpecificContext context);

    /*
     * Sauvegarde le rapport courant
     */
    BirtResultatFichier saveBirtRefreshAsResultat(String statId, String suffix, SpecificContext context);

    /*
     * Publie le rapport courant
     */
    boolean publishReport(String statId, String suffix, SpecificContext context);

    /*
     * Indique si le rapport courant a une demande de rafraichissement en cours
     * Renvoie le message à afficher
     */
    String isCurrentReportRefreshing(SpecificContext context);

    /*
     * Charge le DTO avec le paramétrage par défaut des statistiques
     */
    PanStatistiquesParamDTO getStatParams(SpecificContext context);

    /*
     * Enregistre le paramétrage par défaut des statistiques
     */
    void updateStatParams(
        PanStatistiquesParamForm legisForm,
        PanStatistiquesParamLegisEnCoursForm legisEnCoursForm,
        PanStatistiquesParamLegisPrecForm legisPrecForm,
        SpecificContext context
    );

    /*
     * Prépare le DTO back d'export de législature en cours
     */
    PANExportParametreDTO prepareExport(PanStatistiquesParamLegisEnCoursForm legisEnCoursForm);

    /*
     * Ecran hors connexion : renvoie les rapports correspondant à une catégorie
     */
    List<SuiviLibreReportDTO> getReportsSuiviLibre(SpecificContext context);

    /*
     * Ecran hors connexion : renvoie les rapports principaux unitaires à une catégorie
     */
    List<SuiviLibreReportDTO> getMainReportsSuiviLibre(SpecificContext context);

    /*
     * Ecran hors connexion : renvoie les rapports secondaires unitaires à une catégorie
     */
    List<SuiviLibreReportDTO> getSecondaryReportsSuiviLibre(SpecificContext context);

    /*
     * Ecran hors connexion : renvoie les rapports additionnels à une catégorie
     */
    Map<String, List<ANReportEnum>> getAdditionnalReportsSuiviLibre(SpecificContext context);

    /*
     * Ecran hors connexion : renvoie la date d'actualisation de la liste des lois/ordonnances/lois habilitation
     */
    Date getSuiviDateActualisation(SpecificContext context);

    boolean isReportPublished(SpecificContext context, String link);

    String getReportUrl(BirtReport birtReport, Map<String, String> params, String idContextuel);
}
