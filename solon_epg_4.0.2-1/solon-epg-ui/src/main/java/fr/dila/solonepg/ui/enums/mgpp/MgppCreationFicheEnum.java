package fr.dila.solonepg.ui.enums.mgpp;

import fr.dila.solonepg.ui.bean.MgppDossierCommunicationConsultationFiche;
import fr.dila.solonepg.ui.services.mgpp.MgppUIServiceLocator;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.ui.enums.WidgetTypeEnum;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;

public enum MgppCreationFicheEnum {
    FICHE_DOC(FichePresentationDOC.DOC_TYPE, MgppCreationFicheEnum::initFicheDOC),
    FICHE_AVI(
        FichePresentationAVI.DOC_TYPE,
        MgppCreationFicheEnum::initFicheAVI,
        MgppCreationFicheEnum::changeWidgetTypeForAVI
    ),
    FICHE_OEP(
        FichePresentationOEPImpl.DOC_TYPE,
        MgppCreationFicheEnum::initFicheOEP,
        MgppCreationFicheEnum::changeWidgetTypeForOEP
    ),
    FICHE_AUD(FichePresentationAUD.DOC_TYPE, MgppCreationFicheEnum::initFicheAUD),
    FICHE_IE(FichePresentationIE.DOC_TYPE, MgppCreationFicheEnum::initFicheIE),
    FICHE_341(FichePresentation341.DOC_TYPE, MgppCreationFicheEnum::initFiche341);

    private final String ficheType;
    private final Consumer<SpecificContext> initFicheConsumer;
    private final Consumer<MgppDossierCommunicationConsultationFiche> changeWidgetTypeConsumer;

    MgppCreationFicheEnum(String ficheType, Consumer<SpecificContext> initFicheConsumer) {
        this(ficheType, initFicheConsumer, null);
    }

    MgppCreationFicheEnum(
        String ficheType,
        Consumer<SpecificContext> initFicheConsumer,
        Consumer<MgppDossierCommunicationConsultationFiche> changeWidgetTypeConsumer
    ) {
        this.ficheType = ficheType;
        this.initFicheConsumer = initFicheConsumer;
        this.changeWidgetTypeConsumer = changeWidgetTypeConsumer;
    }

    public String getFicheType() {
        return ficheType;
    }

    public void initFiche(SpecificContext context) {
        if (initFicheConsumer != null) {
            initFicheConsumer.accept(context);
        }
    }

    public void changeWidgetType(MgppDossierCommunicationConsultationFiche fiche) {
        if (changeWidgetTypeConsumer != null) {
            changeWidgetTypeConsumer.accept(fiche);
        }
    }

    public static MgppCreationFicheEnum getByFicheType(String ficheType) {
        return Stream
            .of(values())
            .filter(elem -> elem.getFicheType().equals(ficheType))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Type de fiche inattendu '" + ficheType + "'"));
    }

    private static void initFicheDOC(SpecificContext context) {
        FichePresentationDOC fichePresentationDOC = SolonMgppServiceLocator
            .getDossierService()
            .createFicheRepresentationDOC(context.getSession(), (EvenementDTO) null);
        context.setCurrentDocument(fichePresentationDOC.getDocument());
    }

    private static void initFicheAVI(SpecificContext context) {
        FichePresentationAVI fichePresentationAVI = SolonMgppServiceLocator
            .getDossierService()
            .createFicheRepresentationAVI(context.getSession(), getEvenementDTO(context));
        context.setCurrentDocument(fichePresentationAVI.getDocument());
    }

    private static void initFicheOEP(SpecificContext context) {
        FichePresentationOEP fichePresentationOEP = SolonMgppServiceLocator
            .getDossierService()
            .createFicheRepresentationOEP(context.getSession(), getEvenementDTO(context), false);
        context.setCurrentDocument(fichePresentationOEP.getDocument());
    }

    private static void initFicheAUD(SpecificContext context) {
        FichePresentationAUD fichePresentationAUD = SolonMgppServiceLocator
            .getDossierService()
            .createFicheRepresentationAUD(context.getSession(), null);
        context.setCurrentDocument(fichePresentationAUD.getDocument());
    }

    private static void initFicheIE(SpecificContext context) {
        FichePresentationIE fichePresentationIE = SolonMgppServiceLocator
            .getDossierService()
            .createEmptyFicheIE(null, context.getSession());
        context.setCurrentDocument(fichePresentationIE.getDocument());
    }

    private static void initFiche341(SpecificContext context) {
        FichePresentation341 fichePresentation341 = SolonMgppServiceLocator
            .getDossierService()
            .createEmptyFiche341(getEvenementDTO(context), context.getSession());
        context.setCurrentDocument(fichePresentation341.getDocument());
    }

    private static EvenementDTO getEvenementDTO(SpecificContext context) {
        if (StringUtils.isNotBlank(context.getFromContextData(MgppContextDataKey.COMMUNICATION_ID))) {
            return MgppUIServiceLocator.getMgppDossierUIService().getCurrentEvenementDTO(context);
        }
        return null;
    }

    private static void changeWidgetTypeForAVI(MgppDossierCommunicationConsultationFiche fiche) {
        fiche
            .getLstWidgetsPresentation()
            .stream()
            .filter(widget -> MgppFichePresentationMetadonneesEnum.AVI_ID_DOSSIER.getName().equals(widget.getName()))
            .findFirst()
            .ifPresent(widget -> widget.setTypeChamp(WidgetTypeEnum.INPUT_TEXT.toString()));
    }

    private static void changeWidgetTypeForOEP(MgppDossierCommunicationConsultationFiche fiche) {
        fiche
            .getLstWidgetsPresentation()
            .stream()
            .filter(widget -> MgppFichePresentationMetadonneesEnum.OEP_ID_COMMUN.getName().equals(widget.getName()))
            .findFirst()
            .ifPresent(widget -> widget.setTypeChamp(WidgetTypeEnum.INPUT_TEXT.toString()));
    }
}
