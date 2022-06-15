package fr.dila.solonepg.ui.th.bean;

import static fr.dila.solonepg.api.constant.ConseilEtatConstants.CONSEIL_ETAT_DATE_TRANSMISSION_SECTION_CE_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.COLUMN_LABEL_SUFFIX;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_BASE_LEGALE_ACTE_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_CATEGORIE_ACTE_PROPERTY;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_DATE_EFFET_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_DATE_PRECISEE_PUBLICATION_PROPERTY;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_DATE_PUBLICATION_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_DATE_SIGNATURE_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_DECRET_NUMEROTE_PROPERTY;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_DELAI_PUBLICATION_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_DIRECTION_ATTACHE_PROPERTY;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_DIRECTION_RESPONSABLE_PROPERTY;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_HABILITATION_COMMENTAIRE_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_HABILITATION_NUMERO_ARTICLES_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_HABILITATION_NUMERO_ORDRE_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_HABILITATION_REF_LOI_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_ID_CREATEUR_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_MAIL_RESPONSABLE_PROPERTY;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_MINISTERE_ATTACHE_PROPERTY;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_MINISTERE_RESPONSABLE_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_NOM_RESPONSABLE_PROPERTY;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_NUMERO_NOR_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_POUR_FOURNITURE_EPREUVE_PROPERTY;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_PRENOM_RESPONSABLE_PROPERTY;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_PUBLICATION_INTEGRALE_OU_EXTRAIT_PROPERTY;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_PUBLICATION_RAPPORT_PRESENTATION_PROPERTY;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_QUALITE_RESPONSABLE_PROPERTY;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_RETDILA_DATE_PARUTION_JORF_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_SCHEMA_PREFIX;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_STATUT_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_TEL_RESPONSABLE_PROPERTY;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_TEXTE_ENTREPRISE_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_TITRE_ACTE_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_TP_PRIORITE_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_TYPE_ACTE_XPATH;
import static fr.dila.solonepg.api.constant.DossierSolonEpgConstants.DOSSIER_ZONE_COM_SGG_DILA_PROPERTY;
import static fr.dila.solonepg.api.constant.RetourDilaConstants.RETOUR_DILA_MODE_PARUTION_PROPERTY;
import static fr.dila.solonepg.api.constant.RetourDilaConstants.RETOUR_DILA_NUMERO_TEXTE_PARUTION_JORF_PROPERTY;
import static fr.dila.solonepg.api.constant.RetourDilaConstants.RETOUR_DILA_PAGE_PARUTION_JORF_PROPERTY;
import static fr.dila.solonepg.api.constant.RetourDilaConstants.RETOUR_DILA_SCHEMA_PREFIX;
import static fr.dila.st.api.constant.STConstant.SORT_ORDER_SUFFIX;

import com.google.gson.Gson;
import fr.dila.solonepg.api.constant.ConseilEtatConstants;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.AbstractEpgSortablePaginationForm;
import fr.dila.solonepg.ui.enums.EpgColumnEnum;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.annot.SwBean;
import fr.dila.st.ui.bean.FormSort;
import fr.dila.st.ui.bean.IColonneInfo;
import fr.dila.st.ui.enums.SortOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webengine.WebEngine;

@SwBean(keepdefaultValue = true)
public class DossierListForm extends AbstractEpgSortablePaginationForm {
    private static final String DOSSIER_PREFIX = "d.";
    private static final String DOSSIER_LINK_PREFIX = "dl.";
    private static final String SORT_SUFFIX = "Sort";

    private boolean isDossier = false;
    private boolean rechercheES = false;

    @QueryParam(EpgColumnEnum.Constants.DATE_ARRIVEE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_ARRIVEE_NAME + SORT_SUFFIX)
    private SortOrder dateArriveeDossierLink;

    @QueryParam(EpgColumnEnum.Constants.DATE_PUBLICATION_SOUHAITEE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_PUBLICATION_SOUHAITEE_NAME + SORT_SUFFIX)
    private SortOrder datePublicationSouhaitee;

    @QueryParam(EpgColumnEnum.Constants.DATE_CREATION_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_CREATION_NAME + SORT_SUFFIX)
    private SortOrder dateCreationDossier;

    @QueryParam(EpgColumnEnum.Constants.DATE_PARUTION_JORF_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_PARUTION_JORF_NAME + SORT_SUFFIX)
    private SortOrder dateParutionJorf;

    @QueryParam(EpgColumnEnum.Constants.SECTION_CE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.SECTION_CE_NAME + SORT_SUFFIX)
    private SortOrder sectionCe;

    @QueryParam(EpgColumnEnum.Constants.DATE_SECTION_CE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_SECTION_CE_NAME + SORT_SUFFIX)
    private SortOrder dateSectionCe;

    @QueryParam(EpgColumnEnum.Constants.NUMERO_ISA_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.NUMERO_ISA_NAME + SORT_SUFFIX)
    private SortOrder numeroISA;

    @QueryParam(EpgColumnEnum.Constants.DATE_AG_CE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_AG_CE_NAME + SORT_SUFFIX)
    private SortOrder dateAgCe;

    @QueryParam(EpgColumnEnum.Constants.RAPPORTEUR_CE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.RAPPORTEUR_CE_NAME + SORT_SUFFIX)
    private SortOrder rapporteurCe;

    @QueryParam(EpgColumnEnum.Constants.DATE_TRANSM_SECTION_CE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_TRANSM_SECTION_CE_NAME + SORT_SUFFIX)
    private SortOrder dateTransmissionSectionCe;

    @QueryParam(EpgColumnEnum.Constants.PRIORITE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.PRIORITE_NAME + SORT_SUFFIX)
    private SortOrder priorite;

    @QueryParam(EpgColumnEnum.Constants.TEXTE_ENTREPRISE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.TEXTE_ENTREPRISE_NAME + SORT_SUFFIX)
    private SortOrder texteEntreprise;

    @QueryParam(EpgColumnEnum.Constants.COMPLET_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.COMPLET_NAME + SORT_SUFFIX)
    private SortOrder complet;

    @QueryParam(EpgColumnEnum.Constants.DATE_EFFET_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_EFFET_NAME + SORT_SUFFIX)
    private SortOrder dateEffet;

    @QueryParam(EpgColumnEnum.Constants.DATE_SIGNATURE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_SIGNATURE_NAME + SORT_SUFFIX)
    private SortOrder dateSignature;

    @QueryParam(EpgColumnEnum.Constants.HABILITATION_REF_LOI_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.HABILITATION_REF_LOI_NAME + SORT_SUFFIX)
    private SortOrder habilitationRefLoi;

    @QueryParam(EpgColumnEnum.Constants.HABILITATION_NUMERO_ARTICLE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.HABILITATION_NUMERO_ARTICLE_NAME + SORT_SUFFIX)
    private SortOrder habilitationNumeroArticles;

    @QueryParam(EpgColumnEnum.Constants.HABILITATION_COMMENTAIRE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.HABILITATION_COMMENTAIRE_NAME + SORT_SUFFIX)
    private SortOrder habilitationCommentaire;

    @QueryParam(EpgColumnEnum.Constants.HABILITATION_NUMERO_ORDRE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.HABILITATION_NUMERO_ORDRE_NAME + SORT_SUFFIX)
    private SortOrder habilitationNumeroOrdre;

    @QueryParam(EpgColumnEnum.Constants.DELAI_PUBLICATION_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DELAI_PUBLICATION_NAME + SORT_SUFFIX)
    private SortOrder delaiPublication;

    @QueryParam(EpgColumnEnum.Constants.NUMERO_NOR_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.NUMERO_NOR_NAME + SORT_SUFFIX)
    private SortOrder numeroNor;

    @QueryParam(EpgColumnEnum.Constants.TITRE_ACTE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.TITRE_ACTE_NAME + SORT_SUFFIX)
    private SortOrder titreActe;

    @QueryParam(EpgColumnEnum.Constants.STATUT_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.STATUT_NAME + SORT_SUFFIX)
    private SortOrder statut;

    @QueryParam(EpgColumnEnum.Constants.TYPE_ACT_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.TYPE_ACT_NAME + SORT_SUFFIX)
    private SortOrder typeActe;

    @QueryParam(EpgColumnEnum.Constants.DERNIER_CONTRIBUTEUR_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DERNIER_CONTRIBUTEUR_NAME + SORT_SUFFIX)
    private SortOrder dernierContributeur;

    @QueryParam(EpgColumnEnum.Constants.CREE_PAR_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.CREE_PAR_NAME + SORT_SUFFIX)
    private SortOrder idCreateurDossier;

    @QueryParam(EpgColumnEnum.Constants.MINISTERE_RESP_BORD_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.MINISTERE_RESP_BORD_NAME + SORT_SUFFIX)
    private SortOrder ministereResp;

    @QueryParam(EpgColumnEnum.Constants.BASE_LEGALE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.BASE_LEGALE_NAME + SORT_SUFFIX)
    private SortOrder baseLegaleActe;

    @QueryParam(EpgColumnEnum.Constants.DIRECTION_RESPONSABLE_BORD_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DIRECTION_RESPONSABLE_BORD_NAME + SORT_SUFFIX)
    private SortOrder directionResponsable;

    @QueryParam(EpgColumnEnum.Constants.MINISTERE_RATTACH_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.MINISTERE_RATTACH_NAME + SORT_SUFFIX)
    private SortOrder ministereRattach;

    @QueryParam(EpgColumnEnum.Constants.DIRECTION_RATTACH_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DIRECTION_RATTACH_NAME + SORT_SUFFIX)
    private SortOrder directionRattach;

    @QueryParam(EpgColumnEnum.Constants.NOM_RESP_DOSSIER_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.NOM_RESP_DOSSIER_NAME + SORT_SUFFIX)
    private SortOrder nomRespDossier;

    @QueryParam(EpgColumnEnum.Constants.PRENOM_RESP_DOSSIER_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.PRENOM_RESP_DOSSIER_NAME + SORT_SUFFIX)
    private SortOrder prenomRespDossier;

    @QueryParam(EpgColumnEnum.Constants.QUALITE_RESP_DOSSIER_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.QUALITE_RESP_DOSSIER_NAME + SORT_SUFFIX)
    private SortOrder qualiteRespDossier;

    @QueryParam(EpgColumnEnum.Constants.TEL_RESP_DOSSIER_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.TEL_RESP_DOSSIER_NAME + SORT_SUFFIX)
    private SortOrder telRespDossier;

    @QueryParam(EpgColumnEnum.Constants.MEL_RESP_DOSSIER_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.MEL_RESP_DOSSIER_NAME + SORT_SUFFIX)
    private SortOrder mailRespDossier;

    @QueryParam(EpgColumnEnum.Constants.CATEGORIE_ACTE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.CATEGORIE_ACTE_NAME + SORT_SUFFIX)
    private SortOrder categorieActe;

    @QueryParam(EpgColumnEnum.Constants.PUBLICATION_RAPPORT_PRESENTATION_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.PUBLICATION_RAPPORT_PRESENTATION_NAME + SORT_SUFFIX)
    private SortOrder publicationRapportPresentation;

    @QueryParam(EpgColumnEnum.Constants.POUR_FOURNITURE_EPREUVE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.POUR_FOURNITURE_EPREUVE_NAME + SORT_SUFFIX)
    private SortOrder pourFournitureEpreuve;

    @QueryParam(EpgColumnEnum.Constants.PUBLICATION_INTEGRALE_EXTRAIT_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.PUBLICATION_INTEGRALE_EXTRAIT_NAME + SORT_SUFFIX)
    private SortOrder publicationIntegraleOuExtrait;

    @QueryParam(EpgColumnEnum.Constants.DECRET_NUMEROTE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DECRET_NUMEROTE_NAME + SORT_SUFFIX)
    private SortOrder decretNumerote;

    @QueryParam(EpgColumnEnum.Constants.MODE_PARUTION_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.MODE_PARUTION_NAME + SORT_SUFFIX)
    private SortOrder modeParution;

    @QueryParam(EpgColumnEnum.Constants.PUBLICATION_DATE_PRECISEE_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.PUBLICATION_DATE_PRECISEE_NAME + SORT_SUFFIX)
    private SortOrder datePreciseePublication;

    @QueryParam(EpgColumnEnum.Constants.NUMERO_TEXTE_JO_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.NUMERO_TEXTE_JO_NAME + SORT_SUFFIX)
    private SortOrder numeroTexteParutionJorf;

    @QueryParam(EpgColumnEnum.Constants.PAGE_JO_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.PAGE_JO_NAME + SORT_SUFFIX)
    private SortOrder pageParutionJorf;

    @QueryParam(EpgColumnEnum.Constants.ZONE_COMM_DILA_SGG_NAME + SORT_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.ZONE_COMM_DILA_SGG_NAME + SORT_SUFFIX)
    private SortOrder zoneComSggDila;

    @QueryParam(EpgColumnEnum.Constants.DATE_ARRIVEE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_ARRIVEE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer dateArriveeDossierLinkOrder;

    @QueryParam(EpgColumnEnum.Constants.DATE_PUBLICATION_SOUHAITEE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_PUBLICATION_SOUHAITEE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer datePublicationSouhaiteeOrder;

    @QueryParam(EpgColumnEnum.Constants.DATE_CREATION_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_CREATION_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer dateCreationDossierOrder;

    @QueryParam(EpgColumnEnum.Constants.DATE_PARUTION_JORF_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_PARUTION_JORF_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer dateParutionJorfOrder;

    @QueryParam(EpgColumnEnum.Constants.SECTION_CE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.SECTION_CE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer sectionCeOrder;

    @QueryParam(EpgColumnEnum.Constants.DATE_SECTION_CE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_SECTION_CE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer dateSectionCeOrder;

    @QueryParam(EpgColumnEnum.Constants.NUMERO_ISA_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.NUMERO_ISA_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer numeroISAOrder;

    @QueryParam(EpgColumnEnum.Constants.DATE_AG_CE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_AG_CE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer dateAgCeOrder;

    @QueryParam(EpgColumnEnum.Constants.RAPPORTEUR_CE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.RAPPORTEUR_CE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer rapporteurCeOrder;

    @QueryParam(EpgColumnEnum.Constants.DATE_TRANSM_SECTION_CE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_TRANSM_SECTION_CE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer dateTransmissionSectionCeOrder;

    @QueryParam(EpgColumnEnum.Constants.PRIORITE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.PRIORITE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer prioriteOrder;

    @QueryParam(EpgColumnEnum.Constants.TEXTE_ENTREPRISE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.TEXTE_ENTREPRISE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer texteEntrepriseOrder;

    @QueryParam(EpgColumnEnum.Constants.COMPLET_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.COMPLET_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer completOrder;

    @QueryParam(EpgColumnEnum.Constants.DATE_EFFET_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_EFFET_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer dateEffetOrder;

    @QueryParam(EpgColumnEnum.Constants.DATE_SIGNATURE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DATE_SIGNATURE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer dateSignatureOrder;

    @QueryParam(EpgColumnEnum.Constants.HABILITATION_REF_LOI_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.HABILITATION_REF_LOI_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer habilitationRefLoiOrder;

    @QueryParam(EpgColumnEnum.Constants.HABILITATION_NUMERO_ARTICLE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.HABILITATION_NUMERO_ARTICLE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer habilitationNumeroArticlesOrder;

    @QueryParam(EpgColumnEnum.Constants.HABILITATION_COMMENTAIRE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.HABILITATION_COMMENTAIRE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer habilitationCommentaireOrder;

    @QueryParam(EpgColumnEnum.Constants.HABILITATION_NUMERO_ORDRE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.HABILITATION_NUMERO_ORDRE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer habilitationNumeroOrdreOrder;

    @QueryParam(EpgColumnEnum.Constants.DELAI_PUBLICATION_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DELAI_PUBLICATION_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer delaiPublicationOrder;

    @QueryParam(EpgColumnEnum.Constants.NUMERO_NOR_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.NUMERO_NOR_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer numeroNorOrder = 1;

    @QueryParam(EpgColumnEnum.Constants.TITRE_ACTE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.TITRE_ACTE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer titreActeOrder;

    @QueryParam(EpgColumnEnum.Constants.STATUT_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.STATUT_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer statutOrder;

    @QueryParam(EpgColumnEnum.Constants.TYPE_ACT_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.TYPE_ACT_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer typeActeOrder;

    @QueryParam(EpgColumnEnum.Constants.DERNIER_CONTRIBUTEUR_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DERNIER_CONTRIBUTEUR_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer dernierContributeurOrder;

    @QueryParam(EpgColumnEnum.Constants.CREE_PAR_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.CREE_PAR_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer idCreateurDossierOrder;

    @QueryParam(EpgColumnEnum.Constants.MINISTERE_RESP_BORD_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.MINISTERE_RESP_BORD_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer ministereRespOrder;

    @QueryParam(EpgColumnEnum.Constants.BASE_LEGALE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.BASE_LEGALE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer baseLegaleOrder;

    @QueryParam(EpgColumnEnum.Constants.DIRECTION_RESPONSABLE_BORD_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DIRECTION_RESPONSABLE_BORD_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer directionResponsableOrder;

    @QueryParam(EpgColumnEnum.Constants.MINISTERE_RATTACH_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.MINISTERE_RATTACH_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer ministereRattachOrder;

    @QueryParam(EpgColumnEnum.Constants.DIRECTION_RATTACH_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DIRECTION_RATTACH_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer directionRattachOrder;

    @QueryParam(EpgColumnEnum.Constants.NOM_RESP_DOSSIER_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.NOM_RESP_DOSSIER_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer nomRespDossierOrder;

    @QueryParam(EpgColumnEnum.Constants.PRENOM_RESP_DOSSIER_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.PRENOM_RESP_DOSSIER_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer prenomRespDossierOrder;

    @QueryParam(EpgColumnEnum.Constants.QUALITE_RESP_DOSSIER_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.QUALITE_RESP_DOSSIER_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer qualiteRespDossierOrder;

    @QueryParam(EpgColumnEnum.Constants.TEL_RESP_DOSSIER_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.TEL_RESP_DOSSIER_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer telRespDossierOrder;

    @QueryParam(EpgColumnEnum.Constants.MEL_RESP_DOSSIER_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.MEL_RESP_DOSSIER_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer mailRespDossierOrder;

    @QueryParam(EpgColumnEnum.Constants.CATEGORIE_ACTE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.CATEGORIE_ACTE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer categorieActeOrder;

    @QueryParam(EpgColumnEnum.Constants.PUBLICATION_RAPPORT_PRESENTATION_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.PUBLICATION_RAPPORT_PRESENTATION_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer publicationRapportPresentationOrder;

    @QueryParam(EpgColumnEnum.Constants.POUR_FOURNITURE_EPREUVE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.POUR_FOURNITURE_EPREUVE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer pourFournitureEpreuveOrder;

    @QueryParam(EpgColumnEnum.Constants.PUBLICATION_INTEGRALE_EXTRAIT_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.PUBLICATION_INTEGRALE_EXTRAIT_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer publicationIntegraleOuExtraitOrder;

    @QueryParam(EpgColumnEnum.Constants.DECRET_NUMEROTE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.DECRET_NUMEROTE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer decretNumeroteOrder;

    @QueryParam(EpgColumnEnum.Constants.MODE_PARUTION_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.MODE_PARUTION_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer modeParutionOrder;

    @QueryParam(EpgColumnEnum.Constants.PUBLICATION_DATE_PRECISEE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.PUBLICATION_DATE_PRECISEE_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer datePreciseePublicationOrder;

    @QueryParam(EpgColumnEnum.Constants.NUMERO_TEXTE_JO_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.NUMERO_TEXTE_JO_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer numeroTexteParutionJorfOrder;

    @QueryParam(EpgColumnEnum.Constants.PAGE_JO_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.PAGE_JO_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer pageParutionJorfOrder;

    @QueryParam(EpgColumnEnum.Constants.ZONE_COMM_DILA_SGG_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    @FormParam(EpgColumnEnum.Constants.ZONE_COMM_DILA_SGG_NAME + SORT_SUFFIX + SORT_ORDER_SUFFIX)
    private Integer zoneComSggDilaOrder;

    private boolean dateCreationVisible;
    private boolean publicationsConjointesVisible;
    private boolean vecteurPublicationVisible;
    private boolean dateParutionJorfVisible;
    private boolean sectionCeVisible;
    private boolean dateSectionCeVisible;
    private boolean numeroISAVisible;
    private boolean dateAgCeVisible;
    private boolean rapporteurCeVisible;
    private boolean dateTransmissionSectionCeVisible;
    private boolean prioriteVisible;
    private boolean texteEntrepriseVisible;
    private boolean completVisible;
    private boolean dateEffetVisible;
    private boolean nomCompletChargesMissionsVisible;
    private boolean nomCompletConseillersPmsVisible;
    private boolean dateSignatureVisible;
    private boolean applicationLoiVisible;
    private boolean transpositionOrdonnanceVisible;
    private boolean transpositionDirectiveVisible;
    private boolean habilitationRefLoiVisible;
    private boolean habilitationNumeroArticlesVisible;
    private boolean habilitationCommentaireVisible;
    private boolean habilitationNumeroOrdreVisible;
    private boolean delaiPublicationVisible;
    private boolean titreActeVisible;
    private boolean statutVisible;
    private boolean typeActeVisible;
    private boolean dernierContributeurVisible;
    private boolean idCreateurDossierVisible;
    private boolean ministereRespVisible;
    private boolean baseLegaleVisible;
    private boolean directionResponsableVisible;
    private boolean ministereRattachVisible;
    private boolean directionRattachVisible;
    private boolean nomRespDossierVisible;
    private boolean prenomRespDossierVisible;
    private boolean qualiteRespDossierVisible;
    private boolean telRespDossierVisible;
    private boolean mailRespDossierVisible;
    private boolean categorieActeVisible;
    private boolean publicationRapportPresentationVisible;
    private boolean pourFournitureEpreuveVisible;
    private boolean publicationIntegraleOuExtraitVisible;
    private boolean decretNumeroteVisible;
    private boolean modeParutionVisible;
    private boolean datePreciseePublicationVisible;
    private boolean numeroTexteParutionJorfVisible;
    private boolean pageParutionJorfVisible;
    private boolean parutionBoVisible;
    private boolean zoneComSggDilaVisible;
    private boolean rubriquesVisible;
    private boolean motsclesVisible;
    private boolean champsLibresVisible;
    private boolean dateArriveeDossierLinkVisible;
    private boolean datePublicationSouhaiteeVisible;

    private Map<String, Object> formData;

    public DossierListForm() {
        super();
        CoreSession coreSession = WebEngine.getActiveContext().getCoreSession();
        Integer nbDossierMax = SolonEpgServiceLocator
            .getProfilUtilisateurService()
            .getProfilUtilisateurForCurrentUser(coreSession)
            .getNbDossiersVisiblesMax();
        if (nbDossierMax != null) {
            this.setSize(nbDossierMax);
        }
    }

    public DossierListForm(String json) {
        this(json, false);
    }

    @SuppressWarnings("unchecked")
    public DossierListForm(String json, boolean sortByNorByDefault) {
        Gson gson = new Gson();
        formData = gson.fromJson(json, Map.class);

        if (formData != null) {
            setPage(
                (String) ObjectHelper.requireNonNullElse(formData.get(PAGE_PARAM_NAME), String.valueOf(DEFAULT_PAGE))
            );
            setSize(
                (String) ObjectHelper.requireNonNullElse(formData.get(SIZE_PARAM_NAME), String.valueOf(DEFAULT_SIZE))
            );

            Stream
                .of(EpgColumnEnum.values())
                .filter(col -> col.getOrderSetter() != null)
                .forEach(
                    col -> {
                        col
                            .getOrderSetter()
                            .accept(this, SortOrder.fromValue((String) formData.get(col.getSortName())));
                        col
                            .getSortPrioritySetter()
                            .accept(
                                this,
                                Optional
                                    .ofNullable((String) formData.get(col.getSortPriorityName()))
                                    .map(order -> StringUtils.unwrap(order, "'"))
                                    .map(Integer::valueOf)
                                    .orElse(null)
                            );
                    }
                );

            if (
                ObjectHelper.allNull(Stream.of(EpgColumnEnum.values()).map(col -> col.getSortOrder(this))) &&
                sortByNorByDefault
            ) {
                numeroNor = SortOrder.ASC;
            }
        }
    }

    public boolean isDossier() {
        return isDossier;
    }

    public void setDossier(boolean isDossier) {
        this.isDossier = isDossier;
    }

    public boolean isRechercheES() {
        return rechercheES;
    }

    public void setRechercheES(boolean rechercheES) {
        this.rechercheES = rechercheES;
    }

    public SortOrder getDateArriveeDossierLink() {
        return dateArriveeDossierLink;
    }

    public void setDateArriveeDossierLink(SortOrder dateArriveeDossierLink) {
        this.dateArriveeDossierLink = dateArriveeDossierLink;
    }

    public SortOrder getDatePublicationSouhaitee() {
        return datePublicationSouhaitee;
    }

    public void setDatePublicationSouhaitee(SortOrder datePublicationSouhaitee) {
        this.datePublicationSouhaitee = datePublicationSouhaitee;
    }

    public Integer getDatePublicationSouhaiteeOrder() {
        return datePublicationSouhaiteeOrder;
    }

    public void setDatePublicationSouhaiteeOrder(Integer datePublicationSouhaiteeOrder) {
        this.datePublicationSouhaiteeOrder = datePublicationSouhaiteeOrder;
    }

    public SortOrder getDateCreationDossier() {
        return dateCreationDossier;
    }

    public void setDateCreationDossier(SortOrder dateCreationDossier) {
        this.dateCreationDossier = dateCreationDossier;
    }

    public SortOrder getDateParutionJorf() {
        return dateParutionJorf;
    }

    public void setDateParutionJorf(SortOrder dateParutionJorf) {
        this.dateParutionJorf = dateParutionJorf;
    }

    public SortOrder getSectionCe() {
        return sectionCe;
    }

    public void setSectionCe(SortOrder sectionCe) {
        this.sectionCe = sectionCe;
    }

    public SortOrder getDateSectionCe() {
        return dateSectionCe;
    }

    public void setDateSectionCe(SortOrder dateSectionCe) {
        this.dateSectionCe = dateSectionCe;
    }

    public SortOrder getNumeroISA() {
        return numeroISA;
    }

    public void setNumeroISA(SortOrder numeroISA) {
        this.numeroISA = numeroISA;
    }

    public SortOrder getDateAgCe() {
        return dateAgCe;
    }

    public void setDateAgCe(SortOrder dateAgCe) {
        this.dateAgCe = dateAgCe;
    }

    public SortOrder getRapporteurCe() {
        return rapporteurCe;
    }

    public void setRapporteurCe(SortOrder rapporteurCe) {
        this.rapporteurCe = rapporteurCe;
    }

    public SortOrder getDateTransmissionSectionCe() {
        return dateTransmissionSectionCe;
    }

    public void setDateTransmissionSectionCe(SortOrder dateTransmissionSectionCe) {
        this.dateTransmissionSectionCe = dateTransmissionSectionCe;
    }

    public SortOrder getPriorite() {
        return priorite;
    }

    public void setPriorite(SortOrder priorite) {
        this.priorite = priorite;
    }

    public SortOrder getTexteEntreprise() {
        return texteEntreprise;
    }

    public void setTexteEntreprise(SortOrder texteEntreprise) {
        this.texteEntreprise = texteEntreprise;
    }

    public SortOrder getDateEffet() {
        return dateEffet;
    }

    public void setDateEffet(SortOrder dateEffet) {
        this.dateEffet = dateEffet;
    }

    public SortOrder getDateSignature() {
        return dateSignature;
    }

    public void setDateSignature(SortOrder dateSignature) {
        this.dateSignature = dateSignature;
    }

    public SortOrder getHabilitationRefLoi() {
        return habilitationRefLoi;
    }

    public void setHabilitationRefLoi(SortOrder habilitationRefLoi) {
        this.habilitationRefLoi = habilitationRefLoi;
    }

    public SortOrder getHabilitationNumeroArticles() {
        return habilitationNumeroArticles;
    }

    public void setHabilitationNumeroArticles(SortOrder habilitationNumeroArticles) {
        this.habilitationNumeroArticles = habilitationNumeroArticles;
    }

    public SortOrder getHabilitationCommentaire() {
        return habilitationCommentaire;
    }

    public void setHabilitationCommentaire(SortOrder habilitationCommentaire) {
        this.habilitationCommentaire = habilitationCommentaire;
    }

    public SortOrder getHabilitationNumeroOrdre() {
        return habilitationNumeroOrdre;
    }

    public void setHabilitationNumeroOrdre(SortOrder habilitationNumeroOrdre) {
        this.habilitationNumeroOrdre = habilitationNumeroOrdre;
    }

    public SortOrder getDelaiPublication() {
        return delaiPublication;
    }

    public void setDelaiPublication(SortOrder delaiPublication) {
        this.delaiPublication = delaiPublication;
    }

    public SortOrder getNumeroNor() {
        return numeroNor;
    }

    public void setNumeroNor(SortOrder numeroNor) {
        this.numeroNor = numeroNor;
    }

    public SortOrder getTitreActe() {
        return titreActe;
    }

    public void setTitreActe(SortOrder titreActe) {
        this.titreActe = titreActe;
    }

    public SortOrder getStatut() {
        return statut;
    }

    public void setStatut(SortOrder statut) {
        this.statut = statut;
    }

    public SortOrder getTypeActe() {
        return typeActe;
    }

    public void setTypeActe(SortOrder typeActe) {
        this.typeActe = typeActe;
    }

    public SortOrder getDernierContributeur() {
        return dernierContributeur;
    }

    public void setDernierContributeur(SortOrder dernierContributeur) {
        this.dernierContributeur = dernierContributeur;
    }

    public SortOrder getIdCreateurDossier() {
        return idCreateurDossier;
    }

    public void setIdCreateurDossier(SortOrder idCreateurDossier) {
        this.idCreateurDossier = idCreateurDossier;
    }

    public SortOrder getMinistereResp() {
        return ministereResp;
    }

    public void setMinistereResp(SortOrder ministereResp) {
        this.ministereResp = ministereResp;
    }

    public Integer getDateArriveeDossierLinkOrder() {
        return dateArriveeDossierLinkOrder;
    }

    public void setDateArriveeDossierLinkOrder(Integer dateArriveeDossierLinkOrder) {
        this.dateArriveeDossierLinkOrder = dateArriveeDossierLinkOrder;
    }

    public Integer getDateCreationDossierOrder() {
        return dateCreationDossierOrder;
    }

    public void setDateCreationDossierOrder(Integer dateCreationDossierOrder) {
        this.dateCreationDossierOrder = dateCreationDossierOrder;
    }

    public Integer getDateParutionJorfOrder() {
        return dateParutionJorfOrder;
    }

    public void setDateParutionJorfOrder(Integer dateParutionJorfOrder) {
        this.dateParutionJorfOrder = dateParutionJorfOrder;
    }

    public Integer getSectionCeOrder() {
        return sectionCeOrder;
    }

    public void setSectionCeOrder(Integer sectionCeOrder) {
        this.sectionCeOrder = sectionCeOrder;
    }

    public Integer getDateSectionCeOrder() {
        return dateSectionCeOrder;
    }

    public void setDateSectionCeOrder(Integer dateSectionCeOrder) {
        this.dateSectionCeOrder = dateSectionCeOrder;
    }

    public Integer getNumeroISAOrder() {
        return numeroISAOrder;
    }

    public void setNumeroISAOrder(Integer numeroISAOrder) {
        this.numeroISAOrder = numeroISAOrder;
    }

    public Integer getDateAgCeOrder() {
        return dateAgCeOrder;
    }

    public void setDateAgCeOrder(Integer dateAgCeOrder) {
        this.dateAgCeOrder = dateAgCeOrder;
    }

    public Integer getRapporteurCeOrder() {
        return rapporteurCeOrder;
    }

    public void setRapporteurCeOrder(Integer rapporteurCeOrder) {
        this.rapporteurCeOrder = rapporteurCeOrder;
    }

    public Integer getDateTransmissionSectionCeOrder() {
        return dateTransmissionSectionCeOrder;
    }

    public void setDateTransmissionSectionCeOrder(Integer dateTransmissionSectionCeOrder) {
        this.dateTransmissionSectionCeOrder = dateTransmissionSectionCeOrder;
    }

    public Integer getPrioriteOrder() {
        return prioriteOrder;
    }

    public void setPrioriteOrder(Integer prioriteOrder) {
        this.prioriteOrder = prioriteOrder;
    }

    public Integer getTexteEntrepriseOrder() {
        return texteEntrepriseOrder;
    }

    public void setTexteEntrepriseOrder(Integer texteEntrepriseOrder) {
        this.texteEntrepriseOrder = texteEntrepriseOrder;
    }

    public Integer getDateEffetOrder() {
        return dateEffetOrder;
    }

    public void setDateEffetOrder(Integer dateEffetOrder) {
        this.dateEffetOrder = dateEffetOrder;
    }

    public Integer getDateSignatureOrder() {
        return dateSignatureOrder;
    }

    public void setDateSignatureOrder(Integer dateSignatureOrder) {
        this.dateSignatureOrder = dateSignatureOrder;
    }

    public Integer getHabilitationRefLoiOrder() {
        return habilitationRefLoiOrder;
    }

    public void setHabilitationRefLoiOrder(Integer habilitationRefLoiOrder) {
        this.habilitationRefLoiOrder = habilitationRefLoiOrder;
    }

    public Integer getHabilitationNumeroArticlesOrder() {
        return habilitationNumeroArticlesOrder;
    }

    public void setHabilitationNumeroArticlesOrder(Integer habilitationNumeroArticlesOrder) {
        this.habilitationNumeroArticlesOrder = habilitationNumeroArticlesOrder;
    }

    public Integer getHabilitationCommentaireOrder() {
        return habilitationCommentaireOrder;
    }

    public void setHabilitationCommentaireOrder(Integer habilitationCommentaireOrder) {
        this.habilitationCommentaireOrder = habilitationCommentaireOrder;
    }

    public Integer getHabilitationNumeroOrdreOrder() {
        return habilitationNumeroOrdreOrder;
    }

    public void setHabilitationNumeroOrdreOrder(Integer habilitationNumeroOrdreOrder) {
        this.habilitationNumeroOrdreOrder = habilitationNumeroOrdreOrder;
    }

    public Integer getDelaiPublicationOrder() {
        return delaiPublicationOrder;
    }

    public void setDelaiPublicationOrder(Integer delaiPublicationOrder) {
        this.delaiPublicationOrder = delaiPublicationOrder;
    }

    public Integer getNumeroNorOrder() {
        return numeroNorOrder;
    }

    public void setNumeroNorOrder(Integer numeroNorOrder) {
        this.numeroNorOrder = numeroNorOrder;
    }

    public Integer getTitreActeOrder() {
        return titreActeOrder;
    }

    public void setTitreActeOrder(Integer titreActeOrder) {
        this.titreActeOrder = titreActeOrder;
    }

    public Integer getStatutOrder() {
        return statutOrder;
    }

    public void setStatutOrder(Integer statutOrder) {
        this.statutOrder = statutOrder;
    }

    public Integer getTypeActeOrder() {
        return typeActeOrder;
    }

    public void setTypeActeOrder(Integer typeActeOrder) {
        this.typeActeOrder = typeActeOrder;
    }

    public Integer getDernierContributeurOrder() {
        return dernierContributeurOrder;
    }

    public void setDernierContributeurOrder(Integer dernierContributeurOrder) {
        this.dernierContributeurOrder = dernierContributeurOrder;
    }

    public Integer getIdCreateurDossierOrder() {
        return idCreateurDossierOrder;
    }

    public void setIdCreateurDossierOrder(Integer idCreateurDossierOrder) {
        this.idCreateurDossierOrder = idCreateurDossierOrder;
    }

    public Integer getMinistereRespOrder() {
        return ministereRespOrder;
    }

    public void setMinistereRespOrder(Integer ministereRespOrder) {
        this.ministereRespOrder = ministereRespOrder;
    }

    public boolean isDateCreationVisible() {
        return dateCreationVisible;
    }

    public boolean isPublicationsConjointesVisible() {
        return publicationsConjointesVisible;
    }

    public boolean isVecteurPublicationVisible() {
        return vecteurPublicationVisible;
    }

    public boolean isDateParutionJorfVisible() {
        return dateParutionJorfVisible;
    }

    public boolean isSectionCeVisible() {
        return sectionCeVisible;
    }

    public boolean isDateSectionCeVisible() {
        return dateSectionCeVisible;
    }

    public boolean isNumeroISAVisible() {
        return numeroISAVisible;
    }

    public boolean isDateAgCeVisible() {
        return dateAgCeVisible;
    }

    public boolean isRapporteurCeVisible() {
        return rapporteurCeVisible;
    }

    public boolean isDateTransmissionSectionCeVisible() {
        return dateTransmissionSectionCeVisible;
    }

    public boolean isPrioriteVisible() {
        return prioriteVisible;
    }

    public boolean isTexteEntrepriseVisible() {
        return texteEntrepriseVisible;
    }

    public boolean isCompletVisible() {
        return completVisible;
    }

    public boolean isDateEffetVisible() {
        return dateEffetVisible;
    }

    public boolean isNomCompletChargesMissionsVisible() {
        return nomCompletChargesMissionsVisible;
    }

    public boolean isNomCompletConseillersPmsVisible() {
        return nomCompletConseillersPmsVisible;
    }

    public boolean isDateSignatureVisible() {
        return dateSignatureVisible;
    }

    public boolean isApplicationLoiVisible() {
        return applicationLoiVisible;
    }

    public boolean isTranspositionOrdonnanceVisible() {
        return transpositionOrdonnanceVisible;
    }

    public boolean isTranspositionDirectiveVisible() {
        return transpositionDirectiveVisible;
    }

    public boolean isHabilitationRefLoiVisible() {
        return habilitationRefLoiVisible;
    }

    public boolean isHabilitationNumeroArticlesVisible() {
        return habilitationNumeroArticlesVisible;
    }

    public boolean isHabilitationCommentaireVisible() {
        return habilitationCommentaireVisible;
    }

    public boolean isHabilitationNumeroOrdreVisible() {
        return habilitationNumeroOrdreVisible;
    }

    public boolean isDelaiPublicationVisible() {
        return delaiPublicationVisible;
    }

    public boolean isTitreActeVisible() {
        return titreActeVisible;
    }

    public boolean isStatutVisible() {
        return statutVisible;
    }

    public boolean isTypeActeVisible() {
        return typeActeVisible;
    }

    public boolean isDernierContributeurVisible() {
        return dernierContributeurVisible;
    }

    public boolean isIdCreateurDossierVisible() {
        return idCreateurDossierVisible;
    }

    public boolean isMinistereRespVisible() {
        return ministereRespVisible;
    }

    public SortOrder getComplet() {
        return complet;
    }

    public void setComplet(SortOrder complet) {
        this.complet = complet;
    }

    public Integer getCompletOrder() {
        return completOrder;
    }

    public void setCompletOrder(Integer completOrder) {
        this.completOrder = completOrder;
    }

    public void setDateCreationVisible(boolean dateCreationVisible) {
        this.dateCreationVisible = dateCreationVisible;
    }

    public void setPublicationsConjointesVisible(boolean publicationsConjointesVisible) {
        this.publicationsConjointesVisible = publicationsConjointesVisible;
    }

    public void setVecteurPublicationVisible(boolean vecteurPublicationVisible) {
        this.vecteurPublicationVisible = vecteurPublicationVisible;
    }

    public void setDateParutionJorfVisible(boolean dateParutionJorfVisible) {
        this.dateParutionJorfVisible = dateParutionJorfVisible;
    }

    public void setSectionCeVisible(boolean sectionCeVisible) {
        this.sectionCeVisible = sectionCeVisible;
    }

    public void setDateSectionCeVisible(boolean dateSectionCeVisible) {
        this.dateSectionCeVisible = dateSectionCeVisible;
    }

    public void setNumeroISAVisible(boolean numeroISAVisible) {
        this.numeroISAVisible = numeroISAVisible;
    }

    public void setDateAgCeVisible(boolean dateAgCeVisible) {
        this.dateAgCeVisible = dateAgCeVisible;
    }

    public void setRapporteurCeVisible(boolean rapporteurCeVisible) {
        this.rapporteurCeVisible = rapporteurCeVisible;
    }

    public void setDateTransmissionSectionCeVisible(boolean dateTransmissionSectionCeVisible) {
        this.dateTransmissionSectionCeVisible = dateTransmissionSectionCeVisible;
    }

    public void setPrioriteVisible(boolean prioriteVisible) {
        this.prioriteVisible = prioriteVisible;
    }

    public void setTexteEntrepriseVisible(boolean texteEntrepriseVisible) {
        this.texteEntrepriseVisible = texteEntrepriseVisible;
    }

    public void setCompletVisible(boolean completVisible) {
        this.completVisible = completVisible;
    }

    public void setDateEffetVisible(boolean dateEffetVisible) {
        this.dateEffetVisible = dateEffetVisible;
    }

    public void setNomCompletChargesMissionsVisible(boolean nomCompletChargesMissionsVisible) {
        this.nomCompletChargesMissionsVisible = nomCompletChargesMissionsVisible;
    }

    public void setNomCompletConseillersPmsVisible(boolean nomCompletConseillersPmsVisible) {
        this.nomCompletConseillersPmsVisible = nomCompletConseillersPmsVisible;
    }

    public void setDateSignatureVisible(boolean dateSignatureVisible) {
        this.dateSignatureVisible = dateSignatureVisible;
    }

    public void setApplicationLoiVisible(boolean applicationLoiVisible) {
        this.applicationLoiVisible = applicationLoiVisible;
    }

    public void setTranspositionOrdonnanceVisible(boolean transpositionOrdonnanceVisible) {
        this.transpositionOrdonnanceVisible = transpositionOrdonnanceVisible;
    }

    public void setTranspositionDirectiveVisible(boolean transpositionDirectiveVisible) {
        this.transpositionDirectiveVisible = transpositionDirectiveVisible;
    }

    public void setHabilitationRefLoiVisible(boolean habilitationRefLoiVisible) {
        this.habilitationRefLoiVisible = habilitationRefLoiVisible;
    }

    public void setHabilitationNumeroArticlesVisible(boolean habilitationNumeroArticlesVisible) {
        this.habilitationNumeroArticlesVisible = habilitationNumeroArticlesVisible;
    }

    public void setHabilitationCommentaireVisible(boolean habilitationCommentaireVisible) {
        this.habilitationCommentaireVisible = habilitationCommentaireVisible;
    }

    public void setHabilitationNumeroOrdreVisible(boolean habilitationNumeroOrdreVisible) {
        this.habilitationNumeroOrdreVisible = habilitationNumeroOrdreVisible;
    }

    public void setDelaiPublicationVisible(boolean delaiPublicationVisible) {
        this.delaiPublicationVisible = delaiPublicationVisible;
    }

    public void setTitreActeVisible(boolean titreActeVisible) {
        this.titreActeVisible = titreActeVisible;
    }

    public void setStatutVisible(boolean statutVisible) {
        this.statutVisible = statutVisible;
    }

    public void setTypeActeVisible(boolean typeActeVisible) {
        this.typeActeVisible = typeActeVisible;
    }

    public void setDernierContributeurVisible(boolean dernierContributeurVisible) {
        this.dernierContributeurVisible = dernierContributeurVisible;
    }

    public void setIdCreateurDossierVisible(boolean idCreateurDossierVisible) {
        this.idCreateurDossierVisible = idCreateurDossierVisible;
    }

    public void setMinistereRespVisible(boolean ministereRespVisible) {
        this.ministereRespVisible = ministereRespVisible;
    }

    public void setFormData(Map<String, Object> formData) {
        this.formData = formData;
    }

    public SortOrder getBaseLegaleActe() {
        return baseLegaleActe;
    }

    public void setBaseLegaleActe(SortOrder baseLegaleActe) {
        this.baseLegaleActe = baseLegaleActe;
    }

    public Integer getBaseLegaleOrder() {
        return baseLegaleOrder;
    }

    public void setBaseLegaleOrder(Integer baseLegaleOrder) {
        this.baseLegaleOrder = baseLegaleOrder;
    }

    public boolean isBaseLegaleVisible() {
        return baseLegaleVisible;
    }

    public void setBaseLegaleVisible(boolean baseLegaleVisible) {
        this.baseLegaleVisible = baseLegaleVisible;
    }

    public SortOrder getDirectionResponsable() {
        return directionResponsable;
    }

    public void setDirectionResponsable(SortOrder directionResponsable) {
        this.directionResponsable = directionResponsable;
    }

    public Integer getDirectionResponsableOrder() {
        return directionResponsableOrder;
    }

    public void setDirectionResponsableOrder(Integer directionResponsableOrder) {
        this.directionResponsableOrder = directionResponsableOrder;
    }

    public boolean isDirectionResponsableVisible() {
        return directionResponsableVisible;
    }

    public void setDirectionResponsableVisible(boolean directionResponsableVisible) {
        this.directionResponsableVisible = directionResponsableVisible;
    }

    public SortOrder getMinistereRattach() {
        return ministereRattach;
    }

    public void setMinistereRattach(SortOrder ministereRattach) {
        this.ministereRattach = ministereRattach;
    }

    public Integer getMinistereRattachOrder() {
        return ministereRattachOrder;
    }

    public void setMinistereRattachOrder(Integer ministereRattachOrder) {
        this.ministereRattachOrder = ministereRattachOrder;
    }

    public boolean isMinistereRattachVisible() {
        return ministereRattachVisible;
    }

    public void setMinistereRattachVisible(boolean ministereRattachVisible) {
        this.ministereRattachVisible = ministereRattachVisible;
    }

    public SortOrder getDirectionRattach() {
        return directionRattach;
    }

    public void setDirectionRattach(SortOrder directionRattach) {
        this.directionRattach = directionRattach;
    }

    public Integer getDirectionRattachOrder() {
        return directionRattachOrder;
    }

    public void setDirectionRattachOrder(Integer directionRattachOrder) {
        this.directionRattachOrder = directionRattachOrder;
    }

    public boolean isDirectionRattachVisible() {
        return directionRattachVisible;
    }

    public void setDirectionRattachVisible(boolean directionRattachVisible) {
        this.directionRattachVisible = directionRattachVisible;
    }

    public SortOrder getNomRespDossier() {
        return nomRespDossier;
    }

    public void setNomRespDossier(SortOrder nomRespDossier) {
        this.nomRespDossier = nomRespDossier;
    }

    public Integer getNomRespDossierOrder() {
        return nomRespDossierOrder;
    }

    public void setNomRespDossierOrder(Integer nomRespDossierOrder) {
        this.nomRespDossierOrder = nomRespDossierOrder;
    }

    public boolean isNomRespDossierVisible() {
        return nomRespDossierVisible;
    }

    public void setNomRespDossierVisible(boolean nomRespDossierVisible) {
        this.nomRespDossierVisible = nomRespDossierVisible;
    }

    public SortOrder getPrenomRespDossier() {
        return prenomRespDossier;
    }

    public void setPrenomRespDossier(SortOrder prenomRespDossier) {
        this.prenomRespDossier = prenomRespDossier;
    }

    public Integer getPrenomRespDossierOrder() {
        return prenomRespDossierOrder;
    }

    public void setPrenomRespDossierOrder(Integer prenomRespDossierOrder) {
        this.prenomRespDossierOrder = prenomRespDossierOrder;
    }

    public boolean isPrenomRespDossierVisible() {
        return prenomRespDossierVisible;
    }

    public void setPrenomRespDossierVisible(boolean prenomRespDossierVisible) {
        this.prenomRespDossierVisible = prenomRespDossierVisible;
    }

    public SortOrder getQualiteRespDossier() {
        return qualiteRespDossier;
    }

    public void setQualiteRespDossier(SortOrder qualiteRespDossier) {
        this.qualiteRespDossier = qualiteRespDossier;
    }

    public SortOrder getTelRespDossier() {
        return telRespDossier;
    }

    public void setTelRespDossier(SortOrder telRespDossier) {
        this.telRespDossier = telRespDossier;
    }

    public SortOrder getMailRespDossier() {
        return mailRespDossier;
    }

    public void setMailRespDossier(SortOrder mailRespDossier) {
        this.mailRespDossier = mailRespDossier;
    }

    public Integer getQualiteRespDossierOrder() {
        return qualiteRespDossierOrder;
    }

    public void setQualiteRespDossierOrder(Integer qualiteRespDossierOrder) {
        this.qualiteRespDossierOrder = qualiteRespDossierOrder;
    }

    public Integer getTelRespDossierOrder() {
        return telRespDossierOrder;
    }

    public void setTelRespDossierOrder(Integer telRespDossierOrder) {
        this.telRespDossierOrder = telRespDossierOrder;
    }

    public Integer getMailRespDossierOrder() {
        return mailRespDossierOrder;
    }

    public void setMailRespDossierOrder(Integer mailRespDossierOrder) {
        this.mailRespDossierOrder = mailRespDossierOrder;
    }

    public boolean isQualiteRespDossierVisible() {
        return qualiteRespDossierVisible;
    }

    public void setQualiteRespDossierVisible(boolean qualiteRespDossierVisible) {
        this.qualiteRespDossierVisible = qualiteRespDossierVisible;
    }

    public boolean isTelRespDossierVisible() {
        return telRespDossierVisible;
    }

    public void setTelRespDossierVisible(boolean telRespDossierVisible) {
        this.telRespDossierVisible = telRespDossierVisible;
    }

    public boolean isMailRespDossierVisible() {
        return mailRespDossierVisible;
    }

    public void setMailRespDossierVisible(boolean mailRespDossierVisible) {
        this.mailRespDossierVisible = mailRespDossierVisible;
    }

    public SortOrder getCategorieActe() {
        return categorieActe;
    }

    public void setCategorieActe(SortOrder categorieActe) {
        this.categorieActe = categorieActe;
    }

    public Integer getCategorieActeOrder() {
        return categorieActeOrder;
    }

    public void setCategorieActeOrder(Integer categorieActeOrder) {
        this.categorieActeOrder = categorieActeOrder;
    }

    public boolean isCategorieActeVisible() {
        return categorieActeVisible;
    }

    public void setCategorieActeVisible(boolean categorieActeVisible) {
        this.categorieActeVisible = categorieActeVisible;
    }

    public SortOrder getPublicationRapportPresentation() {
        return publicationRapportPresentation;
    }

    public void setPublicationRapportPresentation(SortOrder publicationRapportPresentation) {
        this.publicationRapportPresentation = publicationRapportPresentation;
    }

    public Integer getPublicationRapportPresentationOrder() {
        return publicationRapportPresentationOrder;
    }

    public void setPublicationRapportPresentationOrder(Integer publicationRapportPresentationOrder) {
        this.publicationRapportPresentationOrder = publicationRapportPresentationOrder;
    }

    public boolean isPublicationRapportPresentationVisible() {
        return publicationRapportPresentationVisible;
    }

    public void setPublicationRapportPresentationVisible(boolean publicationRapportPresentationVisible) {
        this.publicationRapportPresentationVisible = publicationRapportPresentationVisible;
    }

    public SortOrder getPourFournitureEpreuve() {
        return pourFournitureEpreuve;
    }

    public void setPourFournitureEpreuve(SortOrder pourFournitureEpreuve) {
        this.pourFournitureEpreuve = pourFournitureEpreuve;
    }

    public Integer getPourFournitureEpreuveOrder() {
        return pourFournitureEpreuveOrder;
    }

    public void setPourFournitureEpreuveOrder(Integer pourFournitureEpreuveOrder) {
        this.pourFournitureEpreuveOrder = pourFournitureEpreuveOrder;
    }

    public boolean isPourFournitureEpreuveVisible() {
        return pourFournitureEpreuveVisible;
    }

    public void setPourFournitureEpreuveVisible(boolean pourFournitureEpreuveVisible) {
        this.pourFournitureEpreuveVisible = pourFournitureEpreuveVisible;
    }

    public SortOrder getPublicationIntegraleOuExtrait() {
        return publicationIntegraleOuExtrait;
    }

    public void setPublicationIntegraleOuExtrait(SortOrder publicationIntegraleOuExtrait) {
        this.publicationIntegraleOuExtrait = publicationIntegraleOuExtrait;
    }

    public Integer getPublicationIntegraleOuExtraitOrder() {
        return publicationIntegraleOuExtraitOrder;
    }

    public void setPublicationIntegraleOuExtraitOrder(Integer publicationIntegraleOuExtraitOrder) {
        this.publicationIntegraleOuExtraitOrder = publicationIntegraleOuExtraitOrder;
    }

    public boolean isPublicationIntegraleOuExtraitVisible() {
        return publicationIntegraleOuExtraitVisible;
    }

    public SortOrder getDecretNumerote() {
        return decretNumerote;
    }

    public void setDecretNumerote(SortOrder decretNumerote) {
        this.decretNumerote = decretNumerote;
    }

    public Integer getDecretNumeroteOrder() {
        return decretNumeroteOrder;
    }

    public void setDecretNumeroteOrder(Integer decretNumeroteOrder) {
        this.decretNumeroteOrder = decretNumeroteOrder;
    }

    public boolean isDecretNumeroteVisible() {
        return decretNumeroteVisible;
    }

    public void setDecretNumeroteVisible(boolean decretNumeroteVisible) {
        this.decretNumeroteVisible = decretNumeroteVisible;
    }

    public void setPublicationIntegraleOuExtraitVisible(boolean publicationIntegraleOuExtraitVisible) {
        this.publicationIntegraleOuExtraitVisible = publicationIntegraleOuExtraitVisible;
    }

    public SortOrder getModeParution() {
        return modeParution;
    }

    public void setModeParution(SortOrder modeParution) {
        this.modeParution = modeParution;
    }

    public Integer getModeParutionOrder() {
        return modeParutionOrder;
    }

    public void setModeParutionOrder(Integer modeParutionOrder) {
        this.modeParutionOrder = modeParutionOrder;
    }

    public boolean isModeParutionVisible() {
        return modeParutionVisible;
    }

    public void setModeParutionVisible(boolean modeParutionVisible) {
        this.modeParutionVisible = modeParutionVisible;
    }

    public SortOrder getDatePreciseePublication() {
        return datePreciseePublication;
    }

    public void setDatePreciseePublication(SortOrder datePreciseePublication) {
        this.datePreciseePublication = datePreciseePublication;
    }

    public Integer getDatePreciseePublicationOrder() {
        return datePreciseePublicationOrder;
    }

    public void setDatePreciseePublicationOrder(Integer datePreciseePublicationOrder) {
        this.datePreciseePublicationOrder = datePreciseePublicationOrder;
    }

    public boolean isDatePreciseePublicationVisible() {
        return datePreciseePublicationVisible;
    }

    public void setDatePreciseePublicationVisible(boolean datePreciseePublicationVisible) {
        this.datePreciseePublicationVisible = datePreciseePublicationVisible;
    }

    public SortOrder getNumeroTexteParutionJorf() {
        return numeroTexteParutionJorf;
    }

    public void setNumeroTexteParutionJorf(SortOrder numeroTexteParutionJorf) {
        this.numeroTexteParutionJorf = numeroTexteParutionJorf;
    }

    public Integer getNumeroTexteParutionJorfOrder() {
        return numeroTexteParutionJorfOrder;
    }

    public void setNumeroTexteParutionJorfOrder(Integer numeroTexteParutionJorfOrder) {
        this.numeroTexteParutionJorfOrder = numeroTexteParutionJorfOrder;
    }

    public boolean isNumeroTexteParutionJorfVisible() {
        return numeroTexteParutionJorfVisible;
    }

    public void setNumeroTexteParutionJorfVisible(boolean numeroTexteParutionJorfVisible) {
        this.numeroTexteParutionJorfVisible = numeroTexteParutionJorfVisible;
    }

    public SortOrder getPageParutionJorf() {
        return pageParutionJorf;
    }

    public void setPageParutionJorf(SortOrder pageParutionJorf) {
        this.pageParutionJorf = pageParutionJorf;
    }

    public Integer getPageParutionJorfOrder() {
        return pageParutionJorfOrder;
    }

    public void setPageParutionJorfOrder(Integer pageParutionJorfOrder) {
        this.pageParutionJorfOrder = pageParutionJorfOrder;
    }

    public boolean isPageParutionJorfVisible() {
        return pageParutionJorfVisible;
    }

    public void setPageParutionJorfVisible(boolean pageParutionJorfVisible) {
        this.pageParutionJorfVisible = pageParutionJorfVisible;
    }

    public boolean isParutionBoVisible() {
        return parutionBoVisible;
    }

    public void setParutionBoVisible(boolean parutionBoVisible) {
        this.parutionBoVisible = parutionBoVisible;
    }

    public SortOrder getZoneComSggDila() {
        return zoneComSggDila;
    }

    public void setZoneComSggDila(SortOrder zoneComSggDila) {
        this.zoneComSggDila = zoneComSggDila;
    }

    public Integer getZoneComSggDilaOrder() {
        return zoneComSggDilaOrder;
    }

    public void setZoneComSggDilaOrder(Integer zoneComSggDilaOrder) {
        this.zoneComSggDilaOrder = zoneComSggDilaOrder;
    }

    public boolean isZoneComSggDilaVisible() {
        return zoneComSggDilaVisible;
    }

    public void setZoneComSggDilaVisible(boolean zoneComSggDilaVisible) {
        this.zoneComSggDilaVisible = zoneComSggDilaVisible;
    }

    public boolean isRubriquesVisible() {
        return rubriquesVisible;
    }

    public void setRubriquesVisible(boolean rubriquesVisible) {
        this.rubriquesVisible = rubriquesVisible;
    }

    public boolean isMotsclesVisible() {
        return motsclesVisible;
    }

    public void setMotsclesVisible(boolean motsclesVisible) {
        this.motsclesVisible = motsclesVisible;
    }

    public boolean isChampsLibresVisible() {
        return champsLibresVisible;
    }

    public void setChampsLibresVisible(boolean champsLibresVisible) {
        this.champsLibresVisible = champsLibresVisible;
    }

    public void setColumnVisibility(List<IColonneInfo> lstUserColumns) {
        lstUserColumns.forEach(
            col -> EpgColumnEnum.getColumnByLabel(col.getLabel()).getVisibilitySetter().accept(this, col)
        );
    }

    @Override
    protected Map<String, FormSort> getSortForm() {
        Map<String, FormSort> map = new HashMap<>();
        map.put(
            DOSSIER_LINK_PREFIX +
            STSchemaConstant.CASE_LINK_SCHEMA_PREFIX +
            ":" +
            STSchemaConstant.CASE_LINK_DATE_PROPERTY,
            new FormSort(dateArriveeDossierLink, dateArriveeDossierLinkOrder)
        );
        map.put(
            getPrefix() + DossierSolonEpgConstants.DOSSIER_CREATED_XPATH,
            new FormSort(dateCreationDossier, dateCreationDossierOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_RETDILA_DATE_PARUTION_JORF_XPATH,
            new FormSort(dateParutionJorf, dateParutionJorfOrder)
        );
        map.put(
            DOSSIER_PREFIX + ConseilEtatConstants.CONSEIL_ETAT_SECTION_CE_XPATH,
            new FormSort(sectionCe, sectionCeOrder)
        );
        map.put(
            DOSSIER_PREFIX + ConseilEtatConstants.CONSEIL_ETAT_DATE_SECTION_CE_XPATH,
            new FormSort(dateSectionCe, dateSectionCeOrder)
        );
        map.put(
            DOSSIER_PREFIX + ConseilEtatConstants.CONSEIL_ETAT_NUMERO_ISA_XPATH,
            new FormSort(numeroISA, numeroISAOrder)
        );
        map.put(
            DOSSIER_PREFIX + ConseilEtatConstants.CONSEIL_ETAT_DATE_AG_CE_XPATH,
            new FormSort(dateAgCe, dateAgCeOrder)
        );
        map.put(
            DOSSIER_PREFIX + ConseilEtatConstants.CONSEIL_ETAT_RAPPORTEUR_CE_XPATH,
            new FormSort(rapporteurCe, rapporteurCeOrder)
        );
        map.put(
            DOSSIER_PREFIX + CONSEIL_ETAT_DATE_TRANSMISSION_SECTION_CE_XPATH,
            new FormSort(dateTransmissionSectionCe, dateTransmissionSectionCeOrder)
        );
        map.put(DOSSIER_PREFIX + DOSSIER_TP_PRIORITE_XPATH, new FormSort(priorite, prioriteOrder));
        map.put(DOSSIER_PREFIX + DOSSIER_TEXTE_ENTREPRISE_XPATH, new FormSort(texteEntreprise, texteEntrepriseOrder));
        map.put(DOSSIER_PREFIX + DOSSIER_DATE_EFFET_XPATH, new FormSort(dateEffet, dateEffetOrder));
        map.put(DOSSIER_PREFIX + DOSSIER_DATE_SIGNATURE_XPATH, new FormSort(dateSignature, dateSignatureOrder));
        map.put(
            DOSSIER_PREFIX + DOSSIER_HABILITATION_REF_LOI_XPATH,
            new FormSort(habilitationRefLoi, habilitationRefLoiOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_HABILITATION_NUMERO_ARTICLES_XPATH,
            new FormSort(habilitationNumeroArticles, habilitationNumeroArticlesOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_HABILITATION_COMMENTAIRE_XPATH,
            new FormSort(habilitationCommentaire, habilitationCommentaireOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_HABILITATION_NUMERO_ORDRE_XPATH,
            new FormSort(habilitationNumeroOrdre, habilitationNumeroOrdreOrder)
        );
        map.put(
            DOSSIER_PREFIX + getSortXpathWithLabel(DOSSIER_DELAI_PUBLICATION_XPATH),
            new FormSort(delaiPublication, delaiPublicationOrder)
        );
        map.put(DOSSIER_PREFIX + DOSSIER_NUMERO_NOR_XPATH, new FormSort(numeroNor, numeroNorOrder));
        map.put(DOSSIER_PREFIX + DOSSIER_TITRE_ACTE_XPATH, new FormSort(titreActe, titreActeOrder));
        map.put(DOSSIER_PREFIX + getSortXpathWithLabel(DOSSIER_STATUT_XPATH), new FormSort(statut, statutOrder));
        map.put(DOSSIER_PREFIX + getSortXpathWithLabel(DOSSIER_TYPE_ACTE_XPATH), new FormSort(typeActe, typeActeOrder));
        map.put(
            STSchemaConstant.DUBLINCORE_LAST_CONTRIBUTOR_PROPERTY,
            new FormSort(dernierContributeur, dernierContributeurOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_DATE_PUBLICATION_XPATH,
            new FormSort(datePublicationSouhaitee, datePublicationSouhaiteeOrder)
        );
        map.put(DOSSIER_PREFIX + DOSSIER_ID_CREATEUR_XPATH, new FormSort(idCreateurDossier, idCreateurDossierOrder));
        map.put(DOSSIER_PREFIX + DOSSIER_MINISTERE_RESPONSABLE_XPATH, new FormSort(ministereResp, ministereRespOrder));
        map.put(
            DOSSIER_PREFIX + DOSSIER_SCHEMA_PREFIX + ":" + DOSSIER_DIRECTION_RESPONSABLE_PROPERTY,
            new FormSort(directionResponsable, directionResponsableOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_SCHEMA_PREFIX + ":" + DOSSIER_MINISTERE_ATTACHE_PROPERTY,
            new FormSort(ministereRattach, ministereRattachOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_SCHEMA_PREFIX + ":" + DOSSIER_DIRECTION_ATTACHE_PROPERTY,
            new FormSort(directionRattach, directionRattachOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_SCHEMA_PREFIX + ":" + DOSSIER_NOM_RESPONSABLE_PROPERTY,
            new FormSort(nomRespDossier, nomRespDossierOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_SCHEMA_PREFIX + ":" + DOSSIER_PRENOM_RESPONSABLE_PROPERTY,
            new FormSort(prenomRespDossier, prenomRespDossierOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_SCHEMA_PREFIX + ":" + DOSSIER_QUALITE_RESPONSABLE_PROPERTY,
            new FormSort(qualiteRespDossier, qualiteRespDossierOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_SCHEMA_PREFIX + ":" + DOSSIER_TEL_RESPONSABLE_PROPERTY,
            new FormSort(telRespDossier, telRespDossierOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_SCHEMA_PREFIX + ":" + DOSSIER_MAIL_RESPONSABLE_PROPERTY,
            new FormSort(mailRespDossier, mailRespDossierOrder)
        );
        map.put(
            DOSSIER_PREFIX + getSortXpathWithLabel(DOSSIER_SCHEMA_PREFIX + ":" + DOSSIER_CATEGORIE_ACTE_PROPERTY),
            new FormSort(categorieActe, categorieActeOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_SCHEMA_PREFIX + ":" + DOSSIER_PUBLICATION_RAPPORT_PRESENTATION_PROPERTY,
            new FormSort(publicationRapportPresentation, publicationRapportPresentationOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_SCHEMA_PREFIX + ":" + DOSSIER_POUR_FOURNITURE_EPREUVE_PROPERTY,
            new FormSort(pourFournitureEpreuve, pourFournitureEpreuveOrder)
        );
        map.put(
            DOSSIER_PREFIX +
            getSortXpathWithLabel(DOSSIER_SCHEMA_PREFIX + ":" + DOSSIER_PUBLICATION_INTEGRALE_OU_EXTRAIT_PROPERTY),
            new FormSort(publicationIntegraleOuExtrait, publicationIntegraleOuExtraitOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_SCHEMA_PREFIX + ":" + DOSSIER_DECRET_NUMEROTE_PROPERTY,
            new FormSort(decretNumerote, decretNumeroteOrder)
        );
        map.put(
            DOSSIER_PREFIX +
            getSortXpathWithLabel(RETOUR_DILA_SCHEMA_PREFIX + ":" + RETOUR_DILA_MODE_PARUTION_PROPERTY),
            new FormSort(modeParution, modeParutionOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_SCHEMA_PREFIX + ":" + DOSSIER_DATE_PRECISEE_PUBLICATION_PROPERTY,
            new FormSort(datePreciseePublication, datePreciseePublicationOrder)
        );
        map.put(
            DOSSIER_PREFIX + RETOUR_DILA_SCHEMA_PREFIX + ":" + RETOUR_DILA_NUMERO_TEXTE_PARUTION_JORF_PROPERTY,
            new FormSort(numeroTexteParutionJorf, numeroTexteParutionJorfOrder)
        );
        map.put(
            DOSSIER_PREFIX + RETOUR_DILA_SCHEMA_PREFIX + ":" + RETOUR_DILA_PAGE_PARUTION_JORF_PROPERTY,
            new FormSort(pageParutionJorf, pageParutionJorfOrder)
        );
        map.put(
            DOSSIER_PREFIX + DOSSIER_SCHEMA_PREFIX + ":" + DOSSIER_ZONE_COM_SGG_DILA_PROPERTY,
            new FormSort(zoneComSggDila, zoneComSggDilaOrder)
        );
        map.put(DOSSIER_PREFIX + DOSSIER_BASE_LEGALE_ACTE_XPATH, new FormSort(baseLegaleActe, baseLegaleOrder));

        return map;
    }

    private String getSortXpathWithLabel(String xpath) {
        return rechercheES ? xpath : xpath + COLUMN_LABEL_SUFFIX;
    }

    private String getPrefix() {
        return isDossier ? DOSSIER_PREFIX : DOSSIER_LINK_PREFIX;
    }

    @Override
    public void setDefaultSort() {
        numeroNor = SortOrder.ASC;
        numeroNorOrder = 1;
    }

    public static DossierListForm newFormSortedByCreatedDateDesc() {
        DossierListForm form = new DossierListForm();
        form.setDateCreationDossier(SortOrder.DESC);
        form.setNumeroNorOrder(null);
        form.setNumeroNor(null);
        return form;
    }

    public static DossierListForm newForm() {
        return initForm(new DossierListForm());
    }

    public boolean isDateArriveeDossierLinkVisible() {
        return dateArriveeDossierLinkVisible;
    }

    public void setDateArriveeDossierLinkVisible(boolean dateArriveeDossierLinkVisible) {
        this.dateArriveeDossierLinkVisible = dateArriveeDossierLinkVisible;
    }

    public boolean isDatePublicationSouhaiteeVisible() {
        return datePublicationSouhaiteeVisible;
    }

    public void setDatePublicationSouhaiteeVisible(boolean datePublicationSouhaiteeVisible) {
        this.datePublicationSouhaiteeVisible = datePublicationSouhaiteeVisible;
    }
}
