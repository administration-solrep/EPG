package fr.dila.solonepg.ui.services.pan;

import fr.dila.solonepg.api.enumeration.ActiviteNormativeEnum;
import fr.dila.solonepg.core.dto.activitenormative.FicheSignaletiqueApplicationOrdonnanceDTO;
import fr.dila.solonepg.core.dto.activitenormative.FicheSignaletiqueLoiDTO;
import fr.dila.solonepg.core.dto.activitenormative.FicheSignaletiqueOrdonnanceDTO;
import fr.dila.solonepg.ui.bean.pan.AbstractPanSortedList;
import fr.dila.solonepg.ui.bean.pan.ConsultTexteMaitreDTO;
import fr.dila.solonepg.ui.bean.pan.DecretsPanUnsortedList;
import fr.dila.solonepg.ui.bean.pan.MesuresApplicativesPanUnsortedList;
import fr.dila.solonepg.ui.bean.pan.PanOnglet;
import fr.dila.solonepg.ui.bean.pan.TextesMaitresListPan;
import fr.dila.solonepg.ui.contentview.PanMainTableauPageProvider;
import fr.dila.st.core.client.AbstractMapDTO;
import fr.dila.st.ui.bean.OngletConteneur;
import fr.dila.st.ui.bean.SelectValueDTO;
import fr.dila.st.ui.th.model.SpecificContext;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.nuxeo.ecm.platform.actions.Action;

public interface PanUIService {
    void addToActiviteNormative(SpecificContext context);

    void removeFromActiviteNormative(SpecificContext context);

    /**
     * Mode d'affichage des widgets dans les subTabs
     */
    boolean isEditMode(SpecificContext context);

    boolean isEditModeForBordereau(SpecificContext context);

    boolean isEditModeForBordereauAppOrdonnances(SpecificContext context);

    boolean isInApplicationDesLois(SpecificContext context);

    boolean isInApplicationDesOrdonnances(SpecificContext context);

    boolean isInApplicationDesLoisOrOrdonnances(SpecificContext context);

    boolean isInOrdonnances(SpecificContext context);

    boolean isInOrdonnances38C(SpecificContext context);

    boolean isInTransposition(SpecificContext context);

    boolean isInTraiteAccord(SpecificContext context);

    boolean isInTexteMaitre(SpecificContext context);

    boolean isInTableauLois(SpecificContext context);

    boolean isInTableauOrdonnances(SpecificContext context);

    boolean isInTableauProgrammation(SpecificContext context);

    boolean isInTableauSuivi(SpecificContext context);

    /**
     * Retourne vrai si l'utilisateur courant n'a que des droits du type ministère
     */
    boolean isOnlyUtilisateurMinistereLoiOrOrdonnance(SpecificContext context);

    /**
     * Construction de la fiche signaletique d'une loi
     */
    FicheSignaletiqueLoiDTO getCurrentFicheSignaletiqueLoi(SpecificContext context);

    /**
     * Construction de la fiche signaletique d'une application d'ordonnance
     */
    FicheSignaletiqueApplicationOrdonnanceDTO getCurrentFicheSignaletiqueApplicationOrdonnance(SpecificContext context);

    /**
     * Construction de la fiche signaletique d'une ordonnance
     */
    FicheSignaletiqueOrdonnanceDTO getCurrentFicheSignaletiqueOrdonnance(SpecificContext context);

    boolean isActiviteNormativeUpdater(SpecificContext context);

    /**
     * Verrouillage du document
     */
    void lockCurrentDocument(SpecificContext context);

    /**
     * Déverrouillage du document
     */
    void unlockCurrentDocument(SpecificContext context);

    boolean isTexteMaitreLocked(SpecificContext context);

    String getCurrentLockTime(SpecificContext context);

    String getCurrentLockOwnerInfo(SpecificContext context);

    MesuresApplicativesPanUnsortedList getMesures(SpecificContext context);

    DecretsPanUnsortedList getDecrets(SpecificContext context);

    Pair<File, String> genererXls(SpecificContext context);

    TextesMaitresListPan getLoisList(SpecificContext context);

    List<SelectValueDTO> getLegislaturesPublicationValues(SpecificContext context);

    SpecificContext putPanActionDTOInContext(SpecificContext context);

    OngletConteneur actionsToTabs(List<Action> actions, String current);

    PanOnglet getActiveTab(OngletConteneur content);

    <T extends AbstractMapDTO> AbstractPanSortedList<T> getTableauMaitre(SpecificContext context);

    ConsultTexteMaitreDTO getConsultTexteMaitreDTO(SpecificContext context);

    <T extends AbstractMapDTO> AbstractPanSortedList<T> callGenericProvider(
        SpecificContext context,
        PanMainTableauPageProvider genProvider,
        ActiviteNormativeEnum anEnum
    );

    void setTemplateUrls(String currentSection, String currentPanTab, Map<String, Object> templateMap);
}
