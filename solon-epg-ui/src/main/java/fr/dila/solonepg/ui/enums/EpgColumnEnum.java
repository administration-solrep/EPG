package fr.dila.solonepg.ui.enums;

import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.BASE_LEGALE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.CATEGORIE_ACTE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.CHAMPS_LIBRES_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.CHARGE_MISSION_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.COMPLET_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.CONSEILLER_PM_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.CREE_PAR_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.DATE_AG_CE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.DATE_ARRIVEE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.DATE_CREATION_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.DATE_EFFET_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.DATE_PARUTION_JORF_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.DATE_PUBLICATION_SOUHAITEE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.DATE_SECTION_CE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.DATE_SIGNATURE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.DATE_TRANSM_SECTION_CE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.DECRET_NUMEROTE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.DELAI_PUBLICATION_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.DERNIER_CONTRIBUTEUR_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.DIRECTION_RATTACH_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.DIRECTION_RESPONSABLE_BORD_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.HABILITATION_COMMENTAIRE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.HABILITATION_NUMERO_ORDRE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.HABILITATION_REF_LOI_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.INFO_COMPLEMENTAIRE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.MEL_RESP_DOSSIER_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.MINISTERE_RATTACH_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.MINISTERE_RESP_BORD_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.MODE_PARUTION_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.MOTS_CLES_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.NOM_RESP_DOSSIER_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.NUMERO_ISA_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.NUMERO_NOR_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.NUMERO_TEXTE_JO_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.PAGE_JO_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.PARUTION_BO_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.POUR_FOURNITURE_EPREUVE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.PRENOM_RESP_DOSSIER_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.PRIORITE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.PUBLICATION_CONJOINTES_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.PUBLICATION_DATE_PRECISEE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.PUBLICATION_INTEGRALE_EXTRAIT_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.PUBLICATION_RAPPORT_PRESENTATION_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.QUALITE_RESP_DOSSIER_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.RAPPORTEUR_CE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.REF_DIRECTIVES_EURO_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.REF_LOI_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.REF_ORDONNANCE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.RUBRIQUES_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.SECTION_CE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.STATUT_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.TEL_RESP_DOSSIER_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.TEXTE_ENTREPRISE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.TITRE_ACTE_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.TYPE_ACT_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.VECTEUR_PUBLICATION_NAME;
import static fr.dila.solonepg.ui.enums.EpgColumnEnum.Constants.ZONE_COMM_DILA_SGG_NAME;

import fr.dila.solonepg.ui.th.bean.DossierListForm;
import fr.dila.st.ui.bean.IColonneInfo;
import fr.dila.st.ui.enums.SortOrder;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EpgColumnEnum implements IColumnEnum<DossierListForm, EpgFiltreEnum> {
    BASE_LEGALE(
        new Builder(
            "label.content.header.baseLegaleActe",
            BASE_LEGALE_NAME,
            180,
            (dossierListForm, column) -> dossierListForm.setBaseLegaleVisible(column.isVisible())
        )
            .setOrderingInfos(
                BASE_LEGALE_NAME,
                DossierListForm::getBaseLegaleActe,
                DossierListForm::getBaseLegaleOrder,
                (dossierListForm, order) -> dossierListForm.setBaseLegaleActe(order),
                (form, priority) -> form.setBaseLegaleOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.BASE_LEGALE_ACTE)
    ),
    CATEGORIE_ACTE(
        new Builder(
            "label.content.header.categorieActe",
            CATEGORIE_ACTE_NAME,
            170,
            (dossierListForm, column) -> dossierListForm.setCategorieActeVisible(column.isVisible())
        )
            .setOrderingInfos(
                CATEGORIE_ACTE_NAME,
                DossierListForm::getCategorieActe,
                DossierListForm::getCategorieActeOrder,
                (dossierListForm, order) -> dossierListForm.setCategorieActe(order),
                (form, priority) -> form.setCategorieActeOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.CATEGORIE_ACTE)
    ),
    CHAMPS_LIBRES(
        new Builder(
            "label.content.header.libre",
            CHAMPS_LIBRES_NAME,
            460,
            (dossierListForm, column) -> dossierListForm.setChampsLibresVisible(column.isVisible())
        )
    ),
    CHARGE_MISSION(
        new Builder(
            "label.content.header.nomCompletChargesMissions",
            CHARGE_MISSION_NAME,
            280,
            (dossierListForm, column) -> dossierListForm.setNomCompletChargesMissionsVisible(column.isVisible())
        )
    ),
    COMPLET(
        new Builder(
            "label.content.header.complet",
            COMPLET_NAME,
            630,
            (dossierListForm, column) -> dossierListForm.setCompletVisible(column.isVisible())
        )
        .setOrderingInfos(
                COMPLET_NAME,
                DossierListForm::getComplet,
                DossierListForm::getCompletOrder,
                (dossierListForm, order) -> dossierListForm.setComplet(order),
                (form, priority) -> form.setCompletOrder(priority)
            )
    ),
    CONSEILLER_PM(
        new Builder(
            "label.content.header.nomCompletConseillersPms",
            CONSEILLER_PM_NAME,
            290,
            (dossierListForm, column) -> dossierListForm.setNomCompletConseillersPmsVisible(column.isVisible())
        )
    ),
    CREE_PAR(
        new Builder(
            "label.content.header.creePar",
            CREE_PAR_NAME,
            50,
            (dossierListForm, column) -> dossierListForm.setIdCreateurDossierVisible(column.isVisible())
        )
            .setOrderingInfos(
                CREE_PAR_NAME,
                DossierListForm::getIdCreateurDossier,
                DossierListForm::getIdCreateurDossierOrder,
                (dossierListForm, order) -> dossierListForm.setIdCreateurDossier(order),
                (form, priority) -> form.setIdCreateurDossierOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.ID_CREATEUR_DOSSIER)
    ),
    DATE_AG_CE(
        new Builder(
            "label.content.header.dateAgCe",
            DATE_AG_CE_NAME,
            260,
            (dossierListForm, column) -> dossierListForm.setDateAgCeVisible(column.isVisible())
        )
            .setOrderingInfos(
                DATE_AG_CE_NAME,
                DossierListForm::getDateAgCe,
                DossierListForm::getDateAgCeOrder,
                (dossierListForm, order) -> dossierListForm.setDateAgCe(order),
                (form, priority) -> form.setDateAgCeOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.DATE_AG_CE)
    ),
    DATE_ARRIVEE(
        new Builder(
            "label.content.header.dateArriveeDossierLink",
            DATE_ARRIVEE_NAME,
            560,
            (dossierListForm, column) -> dossierListForm.setDateArriveeDossierLinkVisible(column.isVisible())
        )
            .setOrderingInfos(
                DATE_ARRIVEE_NAME,
                DossierListForm::getDateArriveeDossierLink,
                DossierListForm::getDateArriveeDossierLinkOrder,
                (dossierListForm, order) -> dossierListForm.setDateArriveeDossierLink(order),
                (form, priority) -> form.setDateArriveeDossierLinkOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.DATE_ARRIVEE_DOSSIER_LINK)
            .setHiddenInEspaceUtilisateur(true)
    ),
    DATE_CREATION(
        new Builder(
            "label.content.header.dateCreation",
            DATE_CREATION_NAME,
            30,
            (dossierListForm, column) -> dossierListForm.setDateCreationVisible(column.isVisible())
        )
            .setOrderingInfos(
                DATE_CREATION_NAME,
                DossierListForm::getDateCreationDossier,
                DossierListForm::getDateCreationDossierOrder,
                (dossierListForm, order) -> dossierListForm.setDateCreationDossier(order),
                (form, priority) -> form.setDateCreationDossierOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.DATE_CREATION_DOSSIER)
    ),
    DATE_EFFET(
        new Builder(
            "label.content.header.date.effet",
            DATE_EFFET_NAME,
            570,
            (dossierListForm, column) -> dossierListForm.setDateEffetVisible(column.isVisible())
        )
        .setOrderingInfos(
                DATE_EFFET_NAME,
                DossierListForm::getDateEffet,
                DossierListForm::getDateEffetOrder,
                (dossierListForm, order) -> dossierListForm.setDateEffet(order),
                (form, priority) -> form.setDateEffetOrder(priority)
            )
    ),
    DATE_PARUTION_JORF(
        new Builder(
            "label.content.header.dateParutionJorf",
            DATE_PARUTION_JORF_NAME,
            390,
            (dossierListForm, column) -> dossierListForm.setDateParutionJorfVisible(column.isVisible())
        )
            .setOrderingInfos(
                DATE_PARUTION_JORF_NAME,
                DossierListForm::getDateParutionJorf,
                DossierListForm::getDateParutionJorfOrder,
                (dossierListForm, order) -> dossierListForm.setDateParutionJorf(order),
                (form, priority) -> form.setDateParutionJorfOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.DATE_PUBLICATION)
    ),
    DATE_PUBLICATION_SOUHAITEE(
        new Builder(
            "label.content.header.datePublicationSouhaitee",
            DATE_PUBLICATION_SOUHAITEE_NAME,
            190,
            (dossierListForm, column) -> dossierListForm.setDatePublicationSouhaiteeVisible(column.isVisible())
        )
            .setOrderingInfos(
                DATE_PUBLICATION_SOUHAITEE_NAME,
                DossierListForm::getDatePublicationSouhaitee,
                DossierListForm::getDatePublicationSouhaiteeOrder,
                (dossierListForm, order) -> dossierListForm.setDatePublicationSouhaitee(order),
                (form, priority) -> form.setDatePublicationSouhaiteeOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.DATE_PUBLICATION_SOUHAITEE)
    ),
    DATE_SECTION_CE(
        new Builder(
            "label.content.header.dateSectionCe",
            DATE_SECTION_CE_NAME,
            250,
            (dossierListForm, column) -> dossierListForm.setDateSectionCeVisible(column.isVisible())
        )
            .setOrderingInfos(
                DATE_SECTION_CE_NAME,
                DossierListForm::getDateSectionCe,
                DossierListForm::getDateSectionCeOrder,
                (dossierListForm, order) -> dossierListForm.setDateSectionCe(order),
                (form, priority) -> form.setDateSectionCeOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.DATE_SECTION_CE)
    ),
    DATE_SIGNATURE(
        new Builder(
            "label.content.header.dateSignature",
            DATE_SIGNATURE_NAME,
            300,
            (dossierListForm, column) -> dossierListForm.setDateSignatureVisible(column.isVisible())
        )
            .setOrderingInfos(
                DATE_SIGNATURE_NAME,
                DossierListForm::getDateSignature,
                DossierListForm::getDateSignatureOrder,
                (dossierListForm, order) -> dossierListForm.setDateSignature(order),
                (form, priority) -> form.setDateSignatureOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.DATE_SIGNATURE)
    ),
    DATE_TRANSM_SECTION_CE(
        new Builder(
            "label.content.header.dateTransmissionSectionCe",
            DATE_TRANSM_SECTION_CE_NAME,
            240,
            (dossierListForm, column) -> dossierListForm.setDateTransmissionSectionCeVisible(column.isVisible())
        )
            .setOrderingInfos(
                DATE_TRANSM_SECTION_CE_NAME,
                DossierListForm::getDateTransmissionSectionCe,
                DossierListForm::getDateTransmissionSectionCeOrder,
                (dossierListForm, order) -> dossierListForm.setDateTransmissionSectionCe(order),
                (form, priority) -> form.setDateTransmissionSectionCeOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.DATE_TRANSMISSION_SECTION_CE)
    ),
    DECRET_NUMEROTE(
        new Builder(
            "label.content.header.decretNumerote",
            DECRET_NUMEROTE_NAME,
            350,
            (dossierListForm, column) -> dossierListForm.setDecretNumeroteVisible(column.isVisible())
        )
        .setOrderingInfos(
                DECRET_NUMEROTE_NAME,
                DossierListForm::getDecretNumerote,
                DossierListForm::getDecretNumeroteOrder,
                (dossierListForm, order) -> dossierListForm.setDecretNumerote(order),
                (form, priority) -> form.setDecretNumeroteOrder(priority)
            )
    ),
    DELAI_PUBLICATION(
        new Builder(
            "label.content.header.delaiPublication",
            DELAI_PUBLICATION_NAME,
            370,
            (dossierListForm, column) -> dossierListForm.setDelaiPublicationVisible(column.isVisible())
        )
            .setOrderingInfos(
                DELAI_PUBLICATION_NAME,
                DossierListForm::getDelaiPublication,
                DossierListForm::getDelaiPublicationOrder,
                (dossierListForm, order) -> dossierListForm.setDelaiPublication(order),
                (form, priority) -> form.setDelaiPublicationOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.DELAI_PUBLICATION)
    ),
    DERNIER_CONTRIBUTEUR(
        new Builder(
            "label.content.header.dernierContributeur",
            DERNIER_CONTRIBUTEUR_NAME,
            40,
            (dossierListForm, column) -> dossierListForm.setDernierContributeurVisible(column.isVisible())
        )
            .setOrderingInfos(
                DERNIER_CONTRIBUTEUR_NAME,
                DossierListForm::getDernierContributeur,
                DossierListForm::getDernierContributeurOrder,
                (dossierListForm, order) -> dossierListForm.setDernierContributeur(order),
                (form, priority) -> form.setDernierContributeurOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.DERNIER_CONTRIBUTEUR)
    ),
    DIRECTION_ATTACH(
        new Builder(
            "label.content.header.directionAttache",
            DIRECTION_RATTACH_NAME,
            110,
            (dossierListForm, column) -> dossierListForm.setDirectionRattachVisible(column.isVisible())
        )
            .setOrderingInfos(
                null,
                DossierListForm::getDirectionRattach,
                DossierListForm::getDirectionRattachOrder,
                (dossierListForm, order) -> dossierListForm.setDirectionRattach(order),
                (form, priority) -> form.setDirectionRattachOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.DIRECTION_ATTACHE)
    ),
    DIRECTION_RESPONSABLE_BORD(
        new Builder(
            "label.content.header.directionResp",
            DIRECTION_RESPONSABLE_BORD_NAME,
            90,
            (dossierListForm, column) -> dossierListForm.setDirectionResponsableVisible(column.isVisible())
        )
            .setOrderingInfos(
                null,
                DossierListForm::getDirectionResponsable,
                DossierListForm::getDirectionResponsableOrder,
                (dossierListForm, order) -> dossierListForm.setDirectionResponsable(order),
                (form, priority) -> form.setDirectionResponsableOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.DIRECTION_RESP)
    ),
    HABILITATION_COMMENTAIRE(
        new Builder(
            "label.content.header.habilitationCommentaire",
            HABILITATION_COMMENTAIRE_NAME,
            520,
            (dossierListForm, column) -> dossierListForm.setHabilitationCommentaireVisible(column.isVisible())
        )
            .setOrderingInfos(
                HABILITATION_COMMENTAIRE_NAME,
                DossierListForm::getHabilitationCommentaire,
                DossierListForm::getHabilitationCommentaireOrder,
                (dossierListForm, order) -> dossierListForm.setHabilitationCommentaire(order),
                (form, priority) -> form.setHabilitationCommentaireOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.HABILITATION_COMMENTAIRE)
    ),
    HABILITATION_NUMERO_ARTICLE(
        new Builder(
            "label.content.header.habilitationNumeroArticles",
            Constants.HABILITATION_NUMERO_ARTICLE_NAME,
            510,
            (dossierListForm, column) -> dossierListForm.setHabilitationNumeroArticlesVisible(column.isVisible())
        )
            .setOrderingInfos(
                Constants.HABILITATION_NUMERO_ARTICLE_NAME,
                DossierListForm::getHabilitationNumeroArticles,
                DossierListForm::getHabilitationNumeroArticlesOrder,
                (dossierListForm, order) -> dossierListForm.setHabilitationNumeroArticles(order),
                (form, priority) -> form.setHabilitationNumeroArticlesOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.HABILITATION_NUMERO_ARTICLES)
    ),
    HABILITATION_NUMERO_ORDRE(
        new Builder(
            "label.content.header.habilitationNumeroOrdre",
            HABILITATION_NUMERO_ORDRE_NAME,
            530,
            (dossierListForm, column) -> dossierListForm.setHabilitationNumeroOrdreVisible(column.isVisible())
        )
            .setOrderingInfos(
                HABILITATION_NUMERO_ORDRE_NAME,
                DossierListForm::getHabilitationNumeroOrdre,
                DossierListForm::getHabilitationNumeroOrdreOrder,
                (dossierListForm, order) -> dossierListForm.setHabilitationNumeroOrdre(order),
                (form, priority) -> form.setHabilitationNumeroOrdreOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.HABILITATION_NUMERO_ORDRE)
    ),
    HABILITATION_REF_LOI(
        new Builder(
            "label.content.header.habilitationRefLoi",
            HABILITATION_REF_LOI_NAME,
            500,
            (dossierListForm, column) -> dossierListForm.setHabilitationRefLoiVisible(column.isVisible())
        )
            .setOrderingInfos(
                HABILITATION_REF_LOI_NAME,
                DossierListForm::getHabilitationRefLoi,
                DossierListForm::getHabilitationRefLoiOrder,
                (dossierListForm, order) -> dossierListForm.setHabilitationRefLoi(order),
                (form, priority) -> form.setHabilitationRefLoiOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.HABILITATION_REF_LOI)
    ),
    INFO_COMPLEMENTAIRE(
        new Builder(
            "label.content.header.info.complementaire",
            INFO_COMPLEMENTAIRE_NAME,
            10000,
            (dossierListForm, column) -> {}
        )
            .setOrderingInfos(INFO_COMPLEMENTAIRE_NAME)
            .setInfoComplementaire(true)
    ),
    MAIL_RESP_DOSSIER(
        new Builder(
            "label.content.header.mailRespDossier",
            MEL_RESP_DOSSIER_NAME,
            160,
            (dossierListForm, column) -> dossierListForm.setMailRespDossierVisible(column.isVisible())
        )
            .setOrderingInfos(
                MEL_RESP_DOSSIER_NAME,
                DossierListForm::getMailRespDossier,
                DossierListForm::getMailRespDossierOrder,
                (dossierListForm, order) -> dossierListForm.setMailRespDossier(order),
                (form, priority) -> form.setMailRespDossierOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.MAIL_RESP_DOSSIER)
    ),
    MINISTERE_ATTACH(
        new Builder(
            "label.content.header.ministereAttache",
            MINISTERE_RATTACH_NAME,
            100,
            (dossierListForm, column) -> dossierListForm.setMinistereRattachVisible(column.isVisible())
        )
            .setOrderingInfos(
                null,
                DossierListForm::getMinistereRattach,
                DossierListForm::getMinistereRattachOrder,
                (dossierListForm, order) -> dossierListForm.setMinistereRattach(order),
                (form, priority) -> form.setMinistereRattachOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.MINISTERE_ATTACHE)
    ),
    MINISTERE_RESP_BORD(
        new Builder(
            "label.content.header.ministereRespBord",
            MINISTERE_RESP_BORD_NAME,
            80,
            (dossierListForm, column) -> dossierListForm.setMinistereRespVisible(column.isVisible())
        )
            .setOrderingInfos(
                null,
                DossierListForm::getMinistereResp,
                DossierListForm::getMinistereRespOrder,
                (dossierListForm, order) -> dossierListForm.setMinistereResp(order),
                (form, priority) -> form.setMinistereRespOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.MINISTERE_RESP_BORD)
    ),
    MODE_PARUTION(
        new Builder(
            "label.content.header.modeParution",
            MODE_PARUTION_NAME,
            360,
            (dossierListForm, column) -> dossierListForm.setModeParutionVisible(column.isVisible())
        )
            .setOrderingInfos(
                null,
                DossierListForm::getModeParution,
                DossierListForm::getModeParutionOrder,
                (dossierListForm, order) -> dossierListForm.setModeParution(order),
                (form, priority) -> form.setModeParutionOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.MODE_PARUTION)
    ),
    MOTS_CLES(
        new Builder(
            "label.content.header.motscles",
            MOTS_CLES_NAME,
            450,
            (dossierListForm, column) -> dossierListForm.setMotsclesVisible(column.isVisible())
        )
    ),
    NOM_RESP_DOSSIER(
        new Builder(
            "label.content.header.nomRespDossier",
            NOM_RESP_DOSSIER_NAME,
            120,
            (dossierListForm, column) -> dossierListForm.setNomRespDossierVisible(column.isVisible())
        )
            .setOrderingInfos(
                NOM_RESP_DOSSIER_NAME,
                DossierListForm::getNomRespDossier,
                DossierListForm::getNomRespDossierOrder,
                (dossierListForm, order) -> dossierListForm.setNomRespDossier(order),
                (form, priority) -> form.setNomRespDossierOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.NOM_RESP_DOSSIER)
    ),
    NUMERO_ISA(
        new Builder(
            "label.content.header.numeroISA",
            NUMERO_ISA_NAME,
            270,
            (dossierListForm, column) -> dossierListForm.setNumeroISAVisible(column.isVisible())
        )
            .setOrderingInfos(
                NUMERO_ISA_NAME,
                DossierListForm::getNumeroISA,
                DossierListForm::getNumeroISAOrder,
                (dossierListForm, order) -> dossierListForm.setNumeroISA(order),
                (form, priority) -> form.setNumeroISAOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.NUMERO_I_S_A)
    ),
    NUMERO_NOR(
        new Builder("label.content.header.numeroNor", null, 10, (dossierListForm, column) -> {})
            .setOrderingInfos(
                NUMERO_NOR_NAME,
                DossierListForm::getNumeroNor,
                DossierListForm::getNumeroNorOrder,
                (dossierListForm, order) -> dossierListForm.setNumeroNor(order),
                (form, priority) -> form.setNumeroNorOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.NUMERO_NOR)
            .setAlwaysDisplayed(true)
    ),
    NUMERO_TEXTE_JO(
        new Builder(
            "label.content.header.numeroTexteParutionJorf",
            NUMERO_TEXTE_JO_NAME,
            400,
            (dossierListForm, column) -> dossierListForm.setNumeroTexteParutionJorfVisible(column.isVisible())
        )
            .setOrderingInfos(
                NUMERO_TEXTE_JO_NAME,
                DossierListForm::getNumeroTexteParutionJorf,
                DossierListForm::getNumeroTexteParutionJorfOrder,
                (dossierListForm, order) -> dossierListForm.setNumeroTexteParutionJorf(order),
                (form, priority) -> form.setNumeroTexteParutionJorfOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.NUMERO_TEXTE_PARUTION_JORF)
    ),
    PAGE_JO(
        new Builder(
            "label.content.header.pageParutionJorf",
            PAGE_JO_NAME,
            410,
            (dossierListForm, column) -> dossierListForm.setPageParutionJorfVisible(column.isVisible())
        )
            .setOrderingInfos(
                PAGE_JO_NAME,
                DossierListForm::getPageParutionJorf,
                DossierListForm::getPageParutionJorfOrder,
                (dossierListForm, order) -> dossierListForm.setPageParutionJorf(order),
                (form, priority) -> form.setPageParutionJorfOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.PAGE_PARUTION_JORF)
    ),
    PARUTION_BO(
        new Builder(
            "label.content.header.parutionBo",
            PARUTION_BO_NAME,
            420,
            (dossierListForm, column) -> dossierListForm.setParutionBoVisible(column.isVisible())
        )
    ),
    POUR_FOURNITURE_EPREUVE(
        new Builder(
            "label.content.header.pourFournitureEpreuve",
            POUR_FOURNITURE_EPREUVE_NAME,
            310,
            (dossierListForm, column) -> dossierListForm.setPourFournitureEpreuveVisible(column.isVisible())
        )
            .setOrderingInfos(
                POUR_FOURNITURE_EPREUVE_NAME,
                DossierListForm::getPourFournitureEpreuve,
                DossierListForm::getPourFournitureEpreuveOrder,
                (dossierListForm, order) -> dossierListForm.setPourFournitureEpreuve(order),
                (form, priority) -> form.setPourFournitureEpreuveOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.POUR_FOURNITURE_EPREUVE)
    ),
    PRENOM_RESP_DOSSIER(
        new Builder(
            "label.content.header.prenomRespDossier",
            PRENOM_RESP_DOSSIER_NAME,
            130,
            (dossierListForm, column) -> dossierListForm.setPrenomRespDossierVisible(column.isVisible())
        )
            .setOrderingInfos(
                PRENOM_RESP_DOSSIER_NAME,
                DossierListForm::getPrenomRespDossier,
                DossierListForm::getPrenomRespDossierOrder,
                (dossierListForm, order) -> dossierListForm.setPrenomRespDossier(order),
                (form, priority) -> form.setNomRespDossierOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.PRENOM_RESP_DOSSIER)
    ),
    PRIORITE(
        new Builder(
            "label.content.header.priorite",
            PRIORITE_NAME,
            210,
            (dossierListForm, column) -> dossierListForm.setPrioriteVisible(column.isVisible())
        )
            .setOrderingInfos(
                PRIORITE_NAME,
                DossierListForm::getPriorite,
                DossierListForm::getPrioriteOrder,
                (dossierListForm, order) -> dossierListForm.setPriorite(order),
                (form, priority) -> form.setPrioriteOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.PRIORITE)
    ),
    PUBLICATION_CONJOINTES(
        new Builder(
            "label.content.header.publicationsConjointes",
            PUBLICATION_CONJOINTES_NAME,
            330,
            (dossierListForm, column) -> dossierListForm.setPublicationsConjointesVisible(column.isVisible())
        )
    ),
    PUBLICATION_DATE_PRECISEE(
        new Builder(
            "label.content.header.datePreciseePublication",
            PUBLICATION_DATE_PRECISEE_NAME,
            380,
            (dossierListForm, column) -> dossierListForm.setDatePreciseePublicationVisible(column.isVisible())
        )
            .setOrderingInfos(
                PUBLICATION_DATE_PRECISEE_NAME,
                DossierListForm::getDatePreciseePublication,
                DossierListForm::getDatePreciseePublicationOrder,
                (dossierListForm, order) -> dossierListForm.setDatePreciseePublication(order),
                (form, priority) -> form.setDatePreciseePublicationOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.DATE_PRECISEE_PUBLICATION)
    ),
    PUBLICATION_INTEGRALE_EXTRAIT(
        new Builder(
            "label.content.header.publicationIntegraleOuExtrait",
            PUBLICATION_INTEGRALE_EXTRAIT_NAME,
            340,
            (dossierListForm, column) -> dossierListForm.setPublicationIntegraleOuExtraitVisible(column.isVisible())
        )
            .setOrderingInfos(
                PUBLICATION_INTEGRALE_EXTRAIT_NAME,
                DossierListForm::getPublicationIntegraleOuExtrait,
                DossierListForm::getPublicationIntegraleOuExtraitOrder,
                (dossierListForm, order) -> dossierListForm.setPublicationIntegraleOuExtrait(order),
                (form, priority) -> form.setPublicationIntegraleOuExtraitOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.PUBLICATION_INTEGRALE_OU_EXTRAIT)
    ),
    PUBLICATION_RAPPORT_PRESENTATION(
        new Builder(
            "label.content.header.publicationRapportPresentation",
            PUBLICATION_RAPPORT_PRESENTATION_NAME,
            200,
            (dossierListForm, column) -> dossierListForm.setPublicationRapportPresentationVisible(column.isVisible())
        )
            .setOrderingInfos(
                PUBLICATION_RAPPORT_PRESENTATION_NAME,
                DossierListForm::getPublicationRapportPresentation,
                DossierListForm::getPublicationRapportPresentationOrder,
                (dossierListForm, order) -> dossierListForm.setPublicationRapportPresentation(order),
                (form, priority) -> form.setPublicationRapportPresentationOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.PUBLICATION_RAPPORT_PRESENTATION)
    ),
    QUALITE_RESP_DOSSIER(
        new Builder(
            "label.content.header.qualiteRespDossier",
            QUALITE_RESP_DOSSIER_NAME,
            140,
            (dossierListForm, column) -> dossierListForm.setQualiteRespDossierVisible(column.isVisible())
        )
            .setOrderingInfos(
                QUALITE_RESP_DOSSIER_NAME,
                DossierListForm::getQualiteRespDossier,
                DossierListForm::getQualiteRespDossierOrder,
                (dossierListForm, order) -> dossierListForm.setQualiteRespDossier(order),
                (form, priority) -> form.setQualiteRespDossierOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.QUALITE_RESP_DOSSIER)
    ),
    RAPPORTEUR_CE(
        new Builder(
            "label.content.header.rapporteurCe",
            RAPPORTEUR_CE_NAME,
            230,
            (dossierListForm, column) -> dossierListForm.setRapporteurCeVisible(column.isVisible())
        )
            .setOrderingInfos(
                RAPPORTEUR_CE_NAME,
                DossierListForm::getRapporteurCe,
                DossierListForm::getRapporteurCeOrder,
                (dossierListForm, order) -> dossierListForm.setRapporteurCe(order),
                (form, priority) -> form.setRapporteurCeOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.RAPPORTEUR_CE)
    ),
    REF_DIRECTIVES_EURO(
        new Builder(
            "label.content.header.transpositionDirective",
            REF_DIRECTIVES_EURO_NAME,
            490,
            (dossierListForm, column) -> dossierListForm.setTranspositionDirectiveVisible(column.isVisible())
        )
    ),
    REF_LOI(
        new Builder(
            "label.content.header.applicationLoi",
            REF_LOI_NAME,
            470,
            (dossierListForm, column) -> dossierListForm.setApplicationLoiVisible(column.isVisible())
        )
    ),
    REF_ORDONNANCE(
        new Builder(
            "label.content.header.transpositionOrdonnance",
            REF_ORDONNANCE_NAME,
            480,
            (dossierListForm, column) -> dossierListForm.setTranspositionOrdonnanceVisible(column.isVisible())
        )
    ),
    RUBRIQUES(
        new Builder(
            "label.content.header.rubriques",
            RUBRIQUES_NAME,
            440,
            (dossierListForm, column) -> dossierListForm.setRubriquesVisible(column.isVisible())
        )
    ),
    SECTION_CE(
        new Builder(
            "label.content.header.sectionCe",
            SECTION_CE_NAME,
            220,
            (dossierListForm, column) -> dossierListForm.setSectionCeVisible(column.isVisible())
        )
            .setOrderingInfos(
                SECTION_CE_NAME,
                DossierListForm::getSectionCe,
                DossierListForm::getSectionCeOrder,
                (dossierListForm, order) -> dossierListForm.setSectionCe(order),
                (form, priority) -> form.setSectionCeOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.SECTION_CE)
    ),
    STATUT(
        new Builder(
            "label.content.header.statut",
            STATUT_NAME,
            60,
            (dossierListForm, column) -> dossierListForm.setStatutVisible(column.isVisible())
        )
            .setOrderingInfos(
                STATUT_NAME,
                DossierListForm::getStatut,
                DossierListForm::getStatutOrder,
                (dossierListForm, order) -> dossierListForm.setStatut(order),
                (form, priority) -> form.setStatutOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.STATUT)
    ),
    TEL_RESP_DOSSIER(
        new Builder(
            "label.content.header.telRespDossier",
            TEL_RESP_DOSSIER_NAME,
            150,
            (dossierListForm, column) -> dossierListForm.setTelRespDossierVisible(column.isVisible())
        )
            .setOrderingInfos(
                TEL_RESP_DOSSIER_NAME,
                DossierListForm::getTelRespDossier,
                DossierListForm::getTelRespDossierOrder,
                (dossierListForm, order) -> dossierListForm.setTelRespDossier(order),
                (form, priority) -> form.setTelRespDossierOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.TEL_RESP_DOSSIER)
    ),
    TEXTE_ENTREPRISE(
        new Builder(
            "label.content.header.texteEntreprise",
            TEXTE_ENTREPRISE_NAME,
            580,
            (dossierListForm, column) -> dossierListForm.setTexteEntrepriseVisible(column.isVisible())
        )
        .setOrderingInfos(
                TEXTE_ENTREPRISE_NAME,
                DossierListForm::getTexteEntreprise,
                DossierListForm::getTexteEntrepriseOrder,
                (dossierListForm, order) -> dossierListForm.setTexteEntreprise(order),
                (form, priority) -> form.setTexteEntrepriseOrder(priority)
            )
    ),
    TITRE_ACTE(
        new Builder(
            "label.content.header.titreActe",
            TITRE_ACTE_NAME,
            20,
            (dossierListForm, column) -> dossierListForm.setTitreActeVisible(column.isVisible())
        )
            .setOrderingInfos(
                TITRE_ACTE_NAME,
                DossierListForm::getTitreActe,
                DossierListForm::getTitreActeOrder,
                (dossierListForm, order) -> dossierListForm.setTitreActe(order),
                (form, priority) -> form.setTitreActeOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.TITRE_ACTE)
            .setAlwaysDisplayed(true)
    ),
    TYPE_ACTE(
        new Builder(
            "label.content.header.typeActe",
            TYPE_ACT_NAME,
            70,
            (dossierListForm, column) -> dossierListForm.setTypeActeVisible(column.isVisible())
        )
            .setOrderingInfos(
                TYPE_ACT_NAME,
                DossierListForm::getTypeActe,
                DossierListForm::getTypeActeOrder,
                (dossierListForm, order) -> dossierListForm.setTypeActe(order),
                (form, priority) -> form.setTypeActeOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.TYPE_ACTE)
    ),
    VECTEUR_PUBLICATION(
        new Builder(
            "label.content.header.vecteurPublication",
            VECTEUR_PUBLICATION_NAME,
            320,
            (dossierListForm, column) -> dossierListForm.setVecteurPublicationVisible(column.isVisible())
        )
    ),
    ZONE_COMM_DILA_SGG(
        new Builder(
            "label.content.header.zoneComSggDila",
            ZONE_COMM_DILA_SGG_NAME,
            430,
            (dossierListForm, column) -> dossierListForm.setZoneComSggDilaVisible(column.isVisible())
        )
            .setOrderingInfos(
                ZONE_COMM_DILA_SGG_NAME,
                DossierListForm::getZoneComSggDila,
                DossierListForm::getZoneComSggDilaOrder,
                (dossierListForm, order) -> dossierListForm.setZoneComSggDila(order),
                (form, priority) -> form.setZoneComSggDilaOrder(priority)
            )
            .setFiltre(EpgFiltreEnum.ZONE_COM_SGG_DILA)
    );

    /** Clé de property associée */
    private final String label;
    /** name nuxeo associé (peut être null) */
    private final String nxPropName;
    /** sort column priority */
    private final String sortPriorityName;
    /** name nuxeo associé (peut être null) */
    private final String sortName;
    /** La fonction de récupération du SortOrder associé (peut être null) */
    private final Function<DossierListForm, SortOrder> sortOrderGetter;

    /** La fonction de récupération du sens de tri associé (peut être null) */
    private final Function<DossierListForm, Integer> sortPriorityGetter;

    /**
     * Est-ce l'info complémentaire (nécessite un mécanisme de construction
     * spécifique) ?
     */
    private final boolean infoComplementaire;

    /** Filtre associé */
    private final EpgFiltreEnum filtre;

    /** Ordre d'affichage de la colonne */
    private final Integer displayOrder;

    /** La fonction pour définir la visibilité de chaque colonne */
    private final BiConsumer<DossierListForm, IColonneInfo> visibilitySetter;

    private final BiConsumer<DossierListForm, SortOrder> orderSetter;
    private final BiConsumer<DossierListForm, Integer> sortPrioritySetter;

    /**
     * Cette colonne est-elle toujours affichée dans les tableaux (et donc absente
     * des sélecteurs).
     */
    private final boolean alwaysDisplayed;

    /**
     * Cette colonne est-elle cachée dans les sélecteurs de colonnes (espace
     * utilisateur et administration). Cela correspond aux colonnes spécifiques à un
     * provider.
     */
    private final boolean hiddenInColumnSelectors;

    /** Suffix order */
    private static final String ORDER_SUFFIX = "Order";

    /** Suffix Sort */
    private static final String SORT_SUFFIX = "Sort";

    public static class Constants {
        public static final String COMMENTAIRE_NAME = "commentaire";
        public static final String DATE_ARRIVEE_NAME = "dateArriveeDossierLink";
        public static final String DATE_PUBLICATION_SOUHAITEE_NAME = "datePublicationSouhaitee";
        public static final String DATE_CREATION_NAME = "dateCreationDossier";
        public static final String PUBLICATION_CONJOINTES_NAME = "publicationsConjointes";
        public static final String VECTEUR_PUBLICATION_NAME = "vecteurPublication";
        public static final String DATE_PARUTION_JORF_NAME = "dateParutionJorf";
        public static final String SECTION_CE_NAME = "sectionCe";
        public static final String DATE_SECTION_CE_NAME = "dateSectionCe";
        public static final String NUMERO_ISA_NAME = "numeroISA";
        public static final String DATE_AG_CE_NAME = "dateAgCe";
        public static final String RAPPORTEUR_CE_NAME = "rapporteurCe";
        public static final String DATE_TRANSM_SECTION_CE_NAME = "dateTransmissionSectionCe";
        public static final String PRIORITE_NAME = "priorite";
        public static final String TEXTE_ENTREPRISE_NAME = "texteEntreprise";
        public static final String DATE_EFFET_NAME = "dateEffet";
        public static final String CHARGE_MISSION_NAME = "nomCompletChargesMissions";
        public static final String CONSEILLER_PM_NAME = "nomCompletConseillersPms";
        public static final String DATE_SIGNATURE_NAME = "dateSignature";
        public static final String REF_LOI_NAME = "applicationLoi";
        public static final String REF_ORDONNANCE_NAME = "transpositionOrdonnance";
        public static final String REF_DIRECTIVES_EURO_NAME = "transpositionDirective";
        public static final String HABILITATION_REF_LOI_NAME = "habilitationRefLoi";
        public static final String HABILITATION_NUMERO_ARTICLE_NAME = "habilitationNumeroArticles";
        public static final String HABILITATION_COMMENTAIRE_NAME = "habilitationCommentaire";
        public static final String HABILITATION_NUMERO_ORDRE_NAME = "habilitationNumeroOrdre";
        public static final String DELAI_PUBLICATION_NAME = "delaiPublication";
        public static final String NUMERO_NOR_NAME = "numeroNor";
        public static final String TITRE_ACTE_NAME = "titreActe";
        public static final String STATUT_NAME = "statut";
        public static final String TYPE_ACT_NAME = "typeActe";
        public static final String DERNIER_CONTRIBUTEUR_NAME = "dernierContributeur";
        public static final String CREE_PAR_NAME = "idCreateurDossier";
        public static final String MINISTERE_RESP_BORD_NAME = "ministereRespBord";
        public static final String COMPLET_NAME = "complet";
        public static final String INFO_COMPLEMENTAIRE_NAME = "infoComplementaire";
        public static final String BASE_LEGALE_NAME = "baseLegaleActe";
        public static final String DIRECTION_RESPONSABLE_BORD_NAME = "directionRespBord";
        public static final String MINISTERE_RATTACH_NAME = "ministereAttache";
        public static final String DIRECTION_RATTACH_NAME = "directionAttache";
        public static final String NOM_RESP_DOSSIER_NAME = "nomRespDossier";
        public static final String PRENOM_RESP_DOSSIER_NAME = "prenomRespDossier";
        public static final String QUALITE_RESP_DOSSIER_NAME = "qualiteRespDossier";
        public static final String MEL_RESP_DOSSIER_NAME = "mailRespDossier";
        public static final String TEL_RESP_DOSSIER_NAME = "telRespDossier";
        public static final String CATEGORIE_ACTE_NAME = "categorieActe";
        public static final String PUBLICATION_RAPPORT_PRESENTATION_NAME = "publicationRapportPresentation";
        public static final String POUR_FOURNITURE_EPREUVE_NAME = "pourFournitureEpreuve";
        public static final String PUBLICATION_INTEGRALE_EXTRAIT_NAME = "publicationIntegraleOuExtrait";
        public static final String DECRET_NUMEROTE_NAME = "decretNumerote";
        public static final String MODE_PARUTION_NAME = "modeParution";
        public static final String PUBLICATION_DATE_PRECISEE_NAME = "datePreciseePublication";
        public static final String NUMERO_TEXTE_JO_NAME = "numeroTexteParutionJorf";
        public static final String PAGE_JO_NAME = "pageParutionJorf";
        public static final String PARUTION_BO_NAME = "parutionBo";
        public static final String ZONE_COMM_DILA_SGG_NAME = "zoneComSggDila";
        public static final String RUBRIQUES_NAME = "rubriques";
        public static final String MOTS_CLES_NAME = "motscles";
        public static final String CHAMPS_LIBRES_NAME = "libre";
    }

    EpgColumnEnum(Builder builder) {
        this.label = builder.getLabel();
        this.nxPropName = builder.getNxPropName();
        this.sortPriorityName = builder.getSortPriorityName();
        this.sortName = builder.getSortName();
        this.sortOrderGetter = builder.getSortOrderGetter();
        this.infoComplementaire = builder.isInfoComplementaire();
        this.filtre = builder.getFiltre();
        this.displayOrder = builder.getDisplayOrder();
        this.visibilitySetter = builder.getVisibilitySetter();
        this.sortPriorityGetter = builder.getSortPriorityGetter();
        this.sortPrioritySetter = builder.getSortPrioritySetter();
        this.orderSetter = builder.getOrderSetter();
        this.alwaysDisplayed = builder.isAlwaysDisplayed();
        this.hiddenInColumnSelectors = builder.isHiddenInColumnSelectors();
    }

    public static List<EpgColumnEnum> listNonSortable() {
        return Stream
            .of(values())
            .filter(val -> val.sortPriorityGetter == null && val.sortOrderGetter == null)
            .collect(Collectors.toList());
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getNxPropName() {
        return nxPropName;
    }

    public String getSortPriorityName() {
        return sortPriorityName;
    }

    public String getSortName() {
        return sortName;
    }

    public SortOrder getSortOrder(DossierListForm form) {
        if (sortOrderGetter == null) {
            return null;
        }
        return sortOrderGetter.apply(form);
    }

    public Integer getOrder(DossierListForm form) {
        if (sortPriorityGetter == null) {
            return null;
        }
        return sortPriorityGetter.apply(form);
    }

    public boolean isInfoComplementaire() {
        return infoComplementaire;
    }

    @Override
    public EpgFiltreEnum getFiltre() {
        return filtre;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public BiConsumer<DossierListForm, IColonneInfo> getVisibilitySetter() {
        return visibilitySetter;
    }

    public BiConsumer<DossierListForm, SortOrder> getOrderSetter() {
        return orderSetter;
    }

    public BiConsumer<DossierListForm, Integer> getSortPrioritySetter() {
        return sortPrioritySetter;
    }

    public static EpgColumnEnum getColumnByLabel(String label) {
        return Stream.of(values()).filter(col -> label.equals(col.label)).findFirst().orElse(NUMERO_NOR);
    }

    public static EpgColumnEnum getColumnByNxPropName(String nxPropName) {
        return Stream.of(values()).filter(col -> nxPropName.equals(col.nxPropName)).findFirst().orElse(NUMERO_NOR);
    }

    @Override
    public Function<DossierListForm, SortOrder> getSortOrderGetter() {
        return sortOrderGetter;
    }

    @Override
    public Function<DossierListForm, Integer> getOrderGetter() {
        return sortPriorityGetter;
    }

    private static class Builder {
        private String label;
        private String nxPropName;
        private String sortName;
        private String sortPriorityName;
        private Function<DossierListForm, SortOrder> sortOrderGetter;
        private Function<DossierListForm, Integer> sortPriorityGetter;
        private boolean infoComplementaire;
        private EpgFiltreEnum filtre;
        private Integer displayOrder;
        private BiConsumer<DossierListForm, IColonneInfo> visibilitySetter;
        private BiConsumer<DossierListForm, Integer> sortPrioritySetter;
        private BiConsumer<DossierListForm, SortOrder> orderSetter;
        private boolean alwaysDisplayed;
        private boolean hiddenInColumnSelectors;

        private Builder(
            String label,
            String nxPropName,
            Integer displayOrder,
            BiConsumer<DossierListForm, IColonneInfo> visibilitySetter
        ) {
            super();
            this.label = label;
            this.nxPropName = nxPropName;
            this.displayOrder = displayOrder;
            this.visibilitySetter = visibilitySetter;
        }

        private String getLabel() {
            return label;
        }

        private String getNxPropName() {
            return nxPropName;
        }

        private String getSortPriorityName() {
            return sortPriorityName;
        }

        private String getSortName() {
            return sortName;
        }

        private Function<DossierListForm, SortOrder> getSortOrderGetter() {
            return sortOrderGetter;
        }

        private Function<DossierListForm, Integer> getSortPriorityGetter() {
            return sortPriorityGetter;
        }

        private boolean isInfoComplementaire() {
            return infoComplementaire;
        }

        private Builder setInfoComplementaire(boolean infoComplementaire) {
            this.infoComplementaire = infoComplementaire;
            return this;
        }

        private EpgFiltreEnum getFiltre() {
            return filtre;
        }

        private Builder setFiltre(EpgFiltreEnum filtre) {
            this.filtre = filtre;
            return this;
        }

        private Integer getDisplayOrder() {
            return displayOrder;
        }

        private BiConsumer<DossierListForm, IColonneInfo> getVisibilitySetter() {
            return visibilitySetter;
        }

        private BiConsumer<DossierListForm, Integer> getSortPrioritySetter() {
            return sortPrioritySetter;
        }

        private BiConsumer<DossierListForm, SortOrder> getOrderSetter() {
            return orderSetter;
        }

        private Builder setOrderingInfos(String sortName) {
            this.sortName = sortName != null ? sortName + SORT_SUFFIX : null;
            return this;
        }

        private Builder setOrderingInfos(
            String sortName,
            Function<DossierListForm, SortOrder> sortOrderGetter,
            Function<DossierListForm, Integer> sortPriorityGetter,
            BiConsumer<DossierListForm, SortOrder> orderSetter,
            BiConsumer<DossierListForm, Integer> sortPrioritySetter
        ) {
            setOrderingInfos(sortName);
            this.sortOrderGetter = sortOrderGetter;
            this.orderSetter = orderSetter;
            this.sortPriorityName = this.sortName + ORDER_SUFFIX;
            this.sortPriorityGetter = sortPriorityGetter;
            this.sortPrioritySetter = sortPrioritySetter;
            return this;
        }

        private boolean isAlwaysDisplayed() {
            return alwaysDisplayed;
        }

        private Builder setAlwaysDisplayed(boolean alwaysDisplayed) {
            this.alwaysDisplayed = alwaysDisplayed;
            return this;
        }

        public boolean isHiddenInColumnSelectors() {
            return hiddenInColumnSelectors;
        }

        public Builder setHiddenInEspaceUtilisateur(boolean hiddenInEspaceUtilisateur) {
            this.hiddenInColumnSelectors = hiddenInEspaceUtilisateur;
            return this;
        }
    }

    public boolean isAlwaysDisplayed() {
        return alwaysDisplayed;
    }

    public boolean isHiddenInColumnSelectors() {
        return hiddenInColumnSelectors;
    }
}
