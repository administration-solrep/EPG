package fr.dila.solonepg.ui.enums.mgpp;

import static fr.dila.st.ui.enums.WidgetTypeEnum.DATE;
import static fr.dila.st.ui.enums.WidgetTypeEnum.INPUT_TEXT;
import static fr.dila.st.ui.enums.WidgetTypeEnum.MULTIPLE_INPUT_TEXT;
import static fr.dila.st.ui.enums.WidgetTypeEnum.RADIO;
import static fr.dila.st.ui.enums.WidgetTypeEnum.SELECT;
import static fr.dila.st.ui.enums.WidgetTypeEnum.TEXT;
import static fr.dila.st.ui.enums.WidgetTypeEnum.TEXT_AREA;

import fr.dila.solonepg.ui.services.mgpp.impl.MgppFicheUIServiceImpl;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.api.domain.FichePresentationAUD;
import fr.dila.solonmgpp.api.domain.FichePresentationAVI;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDPG;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationJSS;
import fr.dila.solonmgpp.api.domain.FichePresentationSD;
import fr.dila.solonmgpp.core.domain.FichePresentationDRImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationDecretImpl;
import fr.dila.solonmgpp.core.domain.FichePresentationOEPImpl;
import fr.dila.st.core.util.SolonDateConverter;
import fr.dila.st.ui.enums.WidgetTypeEnum;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.DocumentModel;

public enum MgppFichePresentationMetadonneesEnum {
    // Fiche présentation DR
    DR_ID_DOSSIER(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.ID_DOSSIER, String.class, INPUT_TEXT)
        .setName("identifiant")
    ),
    DR_NUMERO_ORDRE(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.NUMERO_ORDRE, Long.class, INPUT_TEXT)
    ),
    DR_NATURE(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.NATURE, String.class, SELECT)
        .setSelectValueType(MgppSelectValueType.NATURE_FDR)
    ),
    DR_RAPPORT_PARLEMENT(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.RAPPORT_PARLEMENT, String.class, SELECT)
        .setSelectValueType(MgppSelectValueType.RAPPORT_PARLEMENT)
    ),
    DR_LEGISLATURE(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.LEGISLATURE, String.class, SELECT)
        .setSelectValueType(MgppSelectValueType.LEGISLATURE)
    ),
    DR_SESSION(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.SESSION, String.class, SELECT)
        .setSelectValueType(MgppSelectValueType.SESSION)
    ),
    DR_TEXTE_REF(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.TEXTE_REF, String.class, INPUT_TEXT)
    ),
    DR_DATE_PUBLICATION_TEXTE_REF(
        new Builder(
            FichePresentationDRImpl.SCHEMA,
            FichePresentationDRImpl.DATE_PUBLICATION_TEXTE_REF,
            Calendar.class,
            DATE
        )
    ),
    DR_ARTICLE_TEXTE_REF(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.ARTICLE_TEXTE_REF, String.class, INPUT_TEXT)
    ),
    DR_OBJET(new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.OBJET, String.class, TEXT_AREA)),
    DR_DATE_RELANCE_SGG(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.DATE_RELANCE_SGG, Calendar.class, DATE)
    ),
    DR_DATE_DEPOT_EFFECTIF(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.DATE_DEPOT_EFFECTIF, Calendar.class, DATE)
    ),
    DR_NUMERO_DEPOT_JO_SENAT(
        new Builder(
            FichePresentationDRImpl.SCHEMA,
            FichePresentationDRImpl.NUMERO_DEPOT_JO_SENAT,
            Long.class,
            INPUT_TEXT
        )
    ),
    DR_OBSERVATION(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.OBSERVATION, String.class, TEXT_AREA)
    ),
    DR_RESPONSABILITE_ELABORATION(
        new Builder(
            FichePresentationDRImpl.SCHEMA,
            FichePresentationDRImpl.RESPONSABILITE_ELABORATION,
            String.class,
            INPUT_TEXT
        )
    ),
    DR_POLE(new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.POLE, List.class, MULTIPLE_INPUT_TEXT)),
    DR_RUBRIQUE(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.RUBRIQUE, String.class, INPUT_TEXT)
    ),
    DR_CREATEUR_DEPOT(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.CREATEUR_DEPOT, String.class, INPUT_TEXT)
    ),
    DR_DESTINATAIRE_1_RAPPORT(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.DESTINATAIRE_1_RAPPORT, List.class, SELECT)
        .setSelectValueType(MgppSelectValueType.DESTINATAIRE)
    ),
    DR_DESTINATAIRE_2_RAPPORT(
        new Builder(
            FichePresentationDRImpl.SCHEMA,
            FichePresentationDRImpl.DESTINATAIRE_2_RAPPORT,
            String.class,
            INPUT_TEXT
        )
    ),
    DR_PERIODICITE(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.PERIODICITE, String.class, INPUT_TEXT)
    ),
    DR_CONVENTION_CALCUL(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.CONVENTION_CALCUL, String.class, TEXT_AREA)
    ),
    DR_RAPPORT_SENAT(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.RAPPORT_SENAT, Boolean.class, RADIO)
    ),
    DR_MINISTERES(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.MINISTERES, String.class, SELECT)
        .setSelectValueType(MgppSelectValueType.MINISTERE)
    ),
    DR_DIRECTEUR_CM(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.DIRECTEUR_CM, String.class, INPUT_TEXT)
    ),
    DR_DAJ_MINISTERE(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.DAJ_MINISTERE, String.class, INPUT_TEXT)
    ),
    DR_ANNEE_DUPLIQUEE(
        new Builder(FichePresentationDRImpl.SCHEMA, FichePresentationDRImpl.ANNEE_DUPLIQUEE, Long.class, INPUT_TEXT)
    ),
    // Fiche présentation DOC
    DOC_ID_DOSSIER(
        new Builder(FichePresentationDOC.SCHEMA, FichePresentationDOC.ID_DOSSIER, String.class, INPUT_TEXT)
        .setName(MgppFicheUIServiceImpl.NAME_IDENTIFIANT_PROPOSITION)
    ),
    DOC_OBJET(new Builder(FichePresentationDOC.SCHEMA, FichePresentationDOC.OBJET, String.class, TEXT_AREA)),
    DOC_DATE_LETTRE_PM(
        new Builder(FichePresentationDOC.SCHEMA, FichePresentationDOC.DATE_LETTRE_PM, Calendar.class, DATE)
        .setName(MgppFicheUIServiceImpl.NAME_DATE_LETTRE_PM)
    ),
    DOC_BASE_LEGALE(
        new Builder(FichePresentationDOC.SCHEMA, FichePresentationDOC.BASE_LEGALE, String.class, INPUT_TEXT)
    ),
    DOC_COMMISSIONS(
        new Builder(FichePresentationDOC.SCHEMA, FichePresentationDOC.COMMISSIONS, List.class, MULTIPLE_INPUT_TEXT)
        .setSuggestionType(MgppSuggestionType.COMISSIONS)
    ),
    // Fiche présentation AVI
    AVI_ID_DOSSIER(
        new Builder(FichePresentationAVI.SCHEMA, FichePresentationAVI.ID_DOSSIER, String.class, TEXT).setRequired(true)
    ),
    AVI_NOM_ORGANISME(
        new Builder(FichePresentationAVI.SCHEMA, FichePresentationAVI.NOM_ORGANISME, String.class, INPUT_TEXT)
        .setRequired(true)
    ),
    AVI_BASE_LEGALE(
        new Builder(FichePresentationAVI.SCHEMA, FichePresentationAVI.BASE_LEGALE, String.class, INPUT_TEXT)
    ),
    AVI_DATE_DEBUT(new Builder(FichePresentationAVI.SCHEMA, FichePresentationAVI.DATE, Calendar.class, TEXT)),
    AVI_DATE_FIN(new Builder(FichePresentationAVI.SCHEMA, FichePresentationAVI.DATE_FIN, Calendar.class, DATE)),
    // Fiche présentation OEP
    OEP_ID_COMMUN(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.ID_COMMUN, String.class, TEXT)
        .setRequired(true)
    ),
    OEP_NOM_ORGANISME(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.NOM_ORGANISME, String.class, TEXT_AREA)
        .setRequired(true)
    ),
    OEP_MINISTERE_RATTACHEMENT(
        new Builder(
            FichePresentationOEPImpl.SCHEMA,
            FichePresentationOEPImpl.MINISTERE_RATTACHEMENT,
            String.class,
            INPUT_TEXT
        )
        .setRequired(true)
    ),
    OEP_MINISTERE_RATTACHEMENT_2(
        new Builder(
            FichePresentationOEPImpl.SCHEMA,
            FichePresentationOEPImpl.MINISTERE_RATTACHEMENT_2,
            String.class,
            INPUT_TEXT
        )
    ),
    OEP_MINISTERE_RATTACHEMENT_3(
        new Builder(
            FichePresentationOEPImpl.SCHEMA,
            FichePresentationOEPImpl.MINISTERE_RATTACHEMENT_3,
            String.class,
            INPUT_TEXT
        )
    ),
    OEP_TEXTE_REF(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.TEXTE_REF, String.class, INPUT_TEXT)
    ),
    OEP_TEXTE_DUREE(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.TEXTE_DUREE, String.class, INPUT_TEXT)
    ),
    OEP_COMMENTAIRE(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.COMMENTAIRE, String.class, TEXT_AREA)
    ),
    OEP_OBSERVATIONS(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.OBSERVATION, String.class, TEXT_AREA)
    ),
    OEP_NB_DEPUTE(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.NB_DEPUTE, Long.class, INPUT_TEXT)
    ),
    OEP_NB_SENATEUR(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.NB_SENATEUR, Long.class, INPUT_TEXT)
    ),
    OEP_DATE(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.DATE, Calendar.class, DATE)
        .setRequired(true)
    ),
    OEP_DATE_FIN(new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.DATE_FIN, Calendar.class, DATE)),
    OEP_MOTIF_FIN(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.MOTIF_FIN, String.class, INPUT_TEXT)
        .setRestricted(true)
    ),
    OEP_CHARGE_MISSION(
        new Builder(
            FichePresentationOEPImpl.SCHEMA,
            FichePresentationOEPImpl.CHARGE_MISSION,
            List.class,
            MULTIPLE_INPUT_TEXT
        )
            .setRequired(true)
            .setRestricted(true)
    ),
    OEP_INFORMER_CHARGE_MISSION(
        new Builder(
            FichePresentationOEPImpl.SCHEMA,
            FichePresentationOEPImpl.INFORMER_CHARGE_MISSION,
            Boolean.class,
            RADIO
        )
        .setRestricted(true)
    ),
    OEP_NATURE_JURIDIQUE(
        new Builder(
            FichePresentationOEPImpl.SCHEMA,
            FichePresentationOEPImpl.NATURE_JURIDIQUE,
            String.class,
            INPUT_TEXT
        )
        .setRestricted(true)
    ),
    OEP_COMMISSION_DU_DECRET2006(
        new Builder(
            FichePresentationOEPImpl.SCHEMA,
            FichePresentationOEPImpl.COMMISSION_DU_DECRET2006,
            Boolean.class,
            RADIO
        )
            .setName("commissionRelevantDuDecret2006")
            .setRestricted(true)
    ),
    OEP_IS_SUIVIE_PAR_DQD(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.IS_SUIVIE_PAR_DQD, Boolean.class, RADIO)
            .setName("instanceSuivieParDQD")
            .setRestricted(true)
    ),
    OEP_IS_SUIVIE_PAR_APP_SUIVI_MANDATS(
        new Builder(
            FichePresentationOEPImpl.SCHEMA,
            FichePresentationOEPImpl.IS_SUIVIE_PAR_APP_SUIVI_MANDATS,
            Boolean.class,
            RADIO
        )
            .setName("instanceSuivieParApplication")
            .setRestricted(true)
    ),
    OEP_ADRESSE(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.ADRESSE, String.class, INPUT_TEXT)
    ),
    OEP_TEL(new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.TEL, String.class, INPUT_TEXT)),
    OEP_FAX(new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.FAX, String.class, INPUT_TEXT)),
    OEP_MAIL(new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.MAIL, String.class, INPUT_TEXT)),
    OEP_NOM_INTERLOCUTEUR_REF(
        new Builder(
            FichePresentationOEPImpl.SCHEMA,
            FichePresentationOEPImpl.NOM_INTERLOCUTEUR_REF,
            String.class,
            INPUT_TEXT
        )
    ),
    OEP_FONCTION_INTERLOCUTEUR_REF(
        new Builder(
            FichePresentationOEPImpl.SCHEMA,
            FichePresentationOEPImpl.FONCTION_INTERLOCUTEUR_REF,
            String.class,
            INPUT_TEXT
        )
    ),
    OEP_DUREE_MANDAT_AN(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.DUREE_MANDAT_AN, String.class, INPUT_TEXT)
    ),
    OEP_RENOUVELABLE_AN(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.RENOUVELABLE_AN, Boolean.class, RADIO)
        .setRestricted(true)
    ),
    OEP_NB_MANDATS_AN(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.NB_MANDATS_AN, Long.class, INPUT_TEXT)
        .setRestricted(true)
    ),
    OEP_DUREE_MANDAT_SE(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.DUREE_MANDAT_SE, String.class, INPUT_TEXT)
    ),
    OEP_RENOUVELABLE_SE(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.RENOUVELABLE_SE, Boolean.class, RADIO)
        .setRestricted(true)
    ),
    OEP_NB_MANDATS_SE(
        new Builder(FichePresentationOEPImpl.SCHEMA, FichePresentationOEPImpl.NB_MANDATS_SE, Long.class, INPUT_TEXT)
        .setRestricted(true)
    ),
    // Fiche présentation AUD
    AUD_ID_DOSSIER(
        new Builder(FichePresentationAUD.SCHEMA, FichePresentationAUD.ID_DOSSIER, String.class, TEXT).setRequired(true)
    ),
    AUD_NOM_ORGANISME(
        new Builder(FichePresentationAUD.SCHEMA, FichePresentationAUD.NOM_ORGANISME, String.class, INPUT_TEXT)
        .setRequired(true)
    ),
    AUD_BASE_LEGALE(
        new Builder(FichePresentationAUD.SCHEMA, FichePresentationAUD.BASE_LEGALE, String.class, INPUT_TEXT)
    ),
    // Fiche présentation décret
    DECRET_NOR(new Builder(FichePresentationDecretImpl.SCHEMA, FichePresentationDecretImpl.NOR, String.class, TEXT)),
    DECRET_DATE_JO(
        new Builder(FichePresentationDecretImpl.SCHEMA, FichePresentationDecretImpl.DATE_JO, Calendar.class, DATE)
    ),
    DECRET_NOR_PUBLICATION(
        new Builder(
            FichePresentationDecretImpl.SCHEMA,
            FichePresentationDecretImpl.NOR_PUBLICATION,
            String.class,
            INPUT_TEXT
        )
    ),
    DECRET_NOR_OUVERTURE_SESSION_EXT(
        new Builder(
            FichePresentationDecretImpl.SCHEMA,
            FichePresentationDecretImpl.NOR_DECRET_OUVERTURE_SESSION_EXTRAORDINAIRE,
            String.class,
            INPUT_TEXT
        )
    ),
    DECRET_NOR_LOI(
        new Builder(FichePresentationDecretImpl.SCHEMA, FichePresentationDecretImpl.NOR_LOI, String.class, INPUT_TEXT)
    ),
    DECRET_PAGE_JO(
        new Builder(FichePresentationDecretImpl.SCHEMA, FichePresentationDecretImpl.PAGE_JO, String.class, INPUT_TEXT)
    ),
    DECRET_RUBRIQUE(
        new Builder(FichePresentationDecretImpl.SCHEMA, FichePresentationDecretImpl.RUBRIQUE, String.class, INPUT_TEXT)
    ),
    DECRET_URL_PUBLICATION(
        new Builder(
            FichePresentationDecretImpl.SCHEMA,
            FichePresentationDecretImpl.URL_PUBLICATION,
            String.class,
            INPUT_TEXT
        )
    ),
    DECRET_NUM_JO(
        new Builder(FichePresentationDecretImpl.SCHEMA, FichePresentationDecretImpl.NUM_JO, String.class, INPUT_TEXT)
    ),
    DECRET_INTITULE(
        new Builder(FichePresentationDecretImpl.SCHEMA, FichePresentationDecretImpl.INTITULE, String.class, INPUT_TEXT)
    ),
    DECRET_NUMERO_ACTE(
        new Builder(
            FichePresentationDecretImpl.SCHEMA,
            FichePresentationDecretImpl.NUMERO_ACTE,
            String.class,
            INPUT_TEXT
        )
    ),
    DECRET_OBJET(
        new Builder(FichePresentationDecretImpl.SCHEMA, FichePresentationDecretImpl.OBJET, String.class, INPUT_TEXT)
    ),
    // Fiche présentation JSS
    JSS_ID_DOSSIER(
        new Builder(FichePresentationJSS.SCHEMA, FichePresentationJSS.ID_DOSSIER, String.class, INPUT_TEXT)
        .setName(MgppFicheUIServiceImpl.NAME_IDENTIFIANT_PROPOSITION)
    ),
    JSS_OBJET(new Builder(FichePresentationJSS.SCHEMA, FichePresentationJSS.OBJET, String.class, TEXT_AREA)),
    JSS_DATE_LETTRE_PM(
        new Builder(FichePresentationJSS.SCHEMA, FichePresentationJSS.DATE_LETTRE_PM, Calendar.class, DATE)
        .setName(MgppFicheUIServiceImpl.NAME_DATE_LETTRE_PM)
    ),
    // Fiche présentation IE
    IE_IDENTIFIANT_PROPOSITION(
        new Builder(FichePresentationIE.SCHEMA, FichePresentationIE.IDENTIFIANT_PROPOSITION, String.class, TEXT)
        .setRequired(true)
    ),
    IE_AUTEUR(
        new Builder(FichePresentationIE.SCHEMA, FichePresentationIE.AUTEUR, String.class, INPUT_TEXT)
            .setSuggestionType(MgppSuggestionType.AUTEUR)
            .setRequired(true)
    ),
    IE_INTITULE(
        new Builder(FichePresentationIE.SCHEMA, FichePresentationIE.INTITULE, String.class, INPUT_TEXT)
        .setRequired(true)
    ),
    IE_DATE(new Builder(FichePresentationIE.SCHEMA, FichePresentationIE.DATE, Calendar.class, DATE)),
    IE_OBSERVATION(new Builder(FichePresentationIE.SCHEMA, FichePresentationIE.OBSERVATION, String.class, TEXT_AREA)),
    // Fiche présentation DPG
    DPG_ID_DOSSIER(
        new Builder(FichePresentationDPG.SCHEMA, FichePresentationDPG.ID_DOSSIER, String.class, INPUT_TEXT)
        .setName(MgppFicheUIServiceImpl.NAME_IDENTIFIANT_PROPOSITION)
    ),
    DPG_OBJET(new Builder(FichePresentationDPG.SCHEMA, FichePresentationDPG.OBJET, String.class, TEXT_AREA)),
    DPG_DATE_PRESENTATION(
        new Builder(FichePresentationDPG.SCHEMA, FichePresentationDPG.DATE_PRESENTATION, Calendar.class, DATE)
        .setName("datepresentation")
    ),
    DPG_DATE_LETTRE_PM(
        new Builder(FichePresentationDPG.SCHEMA, FichePresentationDPG.DATE_LETTRE_PM, Calendar.class, DATE)
        .setName(MgppFicheUIServiceImpl.NAME_DATE_LETTRE_PM)
    ),
    DPG_SENS_AVIS(
        new Builder(FichePresentationDPG.SCHEMA, FichePresentationDPG.SENS_AVIS, String.class, SELECT)
        .setSelectValueType(MgppSelectValueType.SENS_AVIS)
    ),
    DPG_DATE_VOTE(
        new Builder(FichePresentationDPG.SCHEMA, FichePresentationDPG.DATE_VOTE, Calendar.class, DATE)
        .setName("datevote")
    ),
    DPG_SUFFRAGE_EXPRIME(
        new Builder(FichePresentationDPG.SCHEMA, FichePresentationDPG.SUFFRAGE_EXPRIME, Long.class, INPUT_TEXT)
    ),
    DPG_VOTE_POUR(new Builder(FichePresentationDPG.SCHEMA, FichePresentationDPG.VOTE_POUR, Long.class, INPUT_TEXT)),
    DPG_VOTE_CONTRE(new Builder(FichePresentationDPG.SCHEMA, FichePresentationDPG.VOTE_CONTRE, Long.class, INPUT_TEXT)),
    DPG_ABSTENTION(new Builder(FichePresentationDPG.SCHEMA, FichePresentationDPG.ABSTENTION, Long.class, INPUT_TEXT)),
    // Fiche présentation SD
    SD_ID_DOSSIER(
        new Builder(FichePresentationSD.SCHEMA, FichePresentationSD.ID_DOSSIER, String.class, INPUT_TEXT)
        .setName("identifiant")
    ),
    SD_OBJET(new Builder(FichePresentationSD.SCHEMA, FichePresentationSD.OBJET, String.class, TEXT_AREA)),
    SD_DATE_DEMANDE(new Builder(FichePresentationSD.SCHEMA, FichePresentationSD.DATE_DEMANDE, Calendar.class, DATE)),
    SD_GROUPE_PARLEMENTAIRE(
        new Builder(
            FichePresentationSD.SCHEMA,
            FichePresentationSD.GROUPE_PARLEMENTAIRE,
            List.class,
            MULTIPLE_INPUT_TEXT
        )
            .setName("groupeparlementaire")
            .setSelectValueType(MgppSelectValueType.DEFAULT)
            .setSuggestionType(MgppSuggestionType.GROUPE_PARLEMENTAIRE)
    ),
    SD_DATE_DECLARATION(
        new Builder(FichePresentationSD.SCHEMA, FichePresentationSD.DATE_DECLARATION, Calendar.class, DATE)
        .setName("datedeclaration")
    ),
    SD_DATE_LETTRE_PM(
        new Builder(FichePresentationSD.SCHEMA, FichePresentationSD.DATE_LETTRE_PM, Calendar.class, DATE)
        .setName(MgppFicheUIServiceImpl.NAME_DATE_LETTRE_PM)
    ),
    SD_DEMANDE_VOTE(
        new Builder(FichePresentationSD.SCHEMA, FichePresentationSD.DEMANDE_VOTE, Boolean.class, RADIO)
        .setName("demandevote")
    ),
    SD_DATE_VOTE(
        new Builder(FichePresentationSD.SCHEMA, FichePresentationSD.DATE_VOTE, Calendar.class, DATE).setName("datevote")
    ),
    SD_SENS_AVIS(
        new Builder(FichePresentationSD.SCHEMA, FichePresentationSD.SENS_AVIS, String.class, SELECT)
        .setSelectValueType(MgppSelectValueType.SENS_AVIS)
    ),
    SD_SUFFRAGE_EXPRIME(
        new Builder(FichePresentationSD.SCHEMA, FichePresentationSD.SUFFRAGE_EXPRIME, Long.class, INPUT_TEXT)
    ),
    SD_VOTE_POUR(new Builder(FichePresentationSD.SCHEMA, FichePresentationSD.VOTE_POUR, Long.class, INPUT_TEXT)),
    SD_VOTE_CONTRE(new Builder(FichePresentationSD.SCHEMA, FichePresentationSD.VOTE_CONTRE, Long.class, INPUT_TEXT)),
    SD_ABSTENTION(new Builder(FichePresentationSD.SCHEMA, FichePresentationSD.ABSTENTION, Long.class, INPUT_TEXT)),
    // Fiche présentation resolution article 34-1
    RES_IDENTIFIANT_PROPOSITION(
        new Builder(FichePresentation341.SCHEMA, FichePresentation341.IDENTIFIANT_PROPOSITION, String.class, TEXT)
        .setRequired(true)
    ),
    RES_AUTEUR(
        new Builder(FichePresentation341.SCHEMA, FichePresentation341.AUTEUR, String.class, INPUT_TEXT)
            .setSuggestionType(MgppSuggestionType.AUTEUR)
            .setRequired(true)
    ),
    RES_CO_AUTEUR(
        new Builder(FichePresentation341.SCHEMA, FichePresentation341.CO_AUTEUR, List.class, MULTIPLE_INPUT_TEXT)
        .setSuggestionType(MgppSuggestionType.COAUTEUR)
    ),
    RES_INTITULE(
        new Builder(FichePresentation341.SCHEMA, FichePresentation341.INTITULE, String.class, INPUT_TEXT)
        .setRequired(true)
    ),
    RES_OBJET(new Builder(FichePresentation341.SCHEMA, FichePresentation341.OBJET, String.class, INPUT_TEXT)),
    RES_NUMERO_DEPOT(
        new Builder(FichePresentation341.SCHEMA, FichePresentation341.NUMERO_DEPOT, String.class, INPUT_TEXT)
    ),
    RES_DATE_DEPOT(new Builder(FichePresentation341.SCHEMA, FichePresentation341.DATE_DEPOT, Calendar.class, DATE));

    private final String schema;
    private final String property;
    private final String name;
    private final Class<?> valueType;
    private final WidgetTypeEnum editWidgetType;
    private final MgppSelectValueType selectValueType;
    private final MgppSuggestionType suggestionType;
    private final boolean required;
    private final boolean restricted;

    MgppFichePresentationMetadonneesEnum(Builder builder) {
        this.schema = builder.schema;
        this.property = builder.property;
        this.name = builder.name;
        this.valueType = builder.valueType;
        this.editWidgetType = builder.editWidgetType;
        this.selectValueType = builder.selectValueType;
        this.suggestionType = builder.suggestionType;
        this.required = builder.required;
        this.restricted = builder.restricted;
    }

    public String getSchema() {
        return schema;
    }

    public String getProperty() {
        return property;
    }

    public String getName() {
        return name;
    }

    public Class<?> getValueType() {
        return valueType;
    }

    public WidgetTypeEnum getEditWidgetType() {
        return editWidgetType;
    }

    public MgppSelectValueType getSelectValueType() {
        return selectValueType;
    }

    public MgppSuggestionType getSuggestionType() {
        return suggestionType;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isRestricted() {
        return restricted;
    }

    private static class Builder {
        private String schema;
        private String property;
        private String name;
        private Class<?> valueType;
        private WidgetTypeEnum editWidgetType;
        private MgppSelectValueType selectValueType = MgppSelectValueType.DEFAULT;
        private MgppSuggestionType suggestionType = MgppSuggestionType.DEFAULT;
        private boolean required = false;
        private boolean restricted = false;

        private Builder(String schema, String property, Class<?> valueType, WidgetTypeEnum editWidgetType) {
            this.schema = schema;
            this.property = property;
            this.name = property;
            this.valueType = valueType;
            this.editWidgetType = editWidgetType;
        }

        private Builder setName(String name) {
            this.name = name;
            return this;
        }

        private Builder setSelectValueType(MgppSelectValueType selectValueType) {
            this.selectValueType = selectValueType;
            return this;
        }

        private Builder setSuggestionType(MgppSuggestionType suggestionType) {
            this.suggestionType = suggestionType;
            return this;
        }

        private Builder setRequired(boolean required) {
            this.required = required;
            return this;
        }

        private Builder setRestricted(boolean restricted) {
            this.restricted = restricted;
            return this;
        }
    }

    public Object getValue(DocumentModel document) {
        Object value;
        switch (valueType.getSimpleName()) {
            case "String":
                value = PropertyUtil.getStringProperty(document, schema, property);
                break;
            case "Long":
                value = PropertyUtil.getLongProperty(document, schema, property);
                break;
            case "Boolean":
                value = PropertyUtil.getBooleanProperty(document, schema, property);
                break;
            case "Calendar":
                value = PropertyUtil.getCalendarProperty(document, schema, property);
                break;
            case "List":
                value = PropertyUtil.getStringListProperty(document, schema, property);
                break;
            default:
                value = null;
                break;
        }
        return value;
    }

    public void setValue(DocumentModel document, Object value) {
        if (value instanceof String && StringUtils.isEmpty((String) value)) {
            value = null;
        }
        if (valueType == Calendar.class) {
            value = SolonDateConverter.DATE_SLASH.parseToCalendarOrNull((String) value);
        } else if (valueType == List.class && value instanceof String) {
            value = Collections.singletonList(value);
        }
        PropertyUtil.setProperty(document, schema, property, value);
    }

    public static List<MgppFichePresentationMetadonneesEnum> getMetadonneesForFichePresentation(String schema) {
        return Arrays.stream(values()).filter(meta -> schema.equals(meta.getSchema())).collect(Collectors.toList());
    }
}
