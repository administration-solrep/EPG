package fr.dila.solonepg.core.recherche.dossier;

import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.APPLICATION_LOI;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.CURRENT_LIFE_CYCLE_STATE;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.DANS_ACTE;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.DANS_AUTRE_PIECE_PARAPHEUR;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.DANS_EXTRAIT;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.DANS_FOND_DOSSIER;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.DATE_ACTIVATION_1;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.DATE_ACTIVATION_2;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.DATE_PUBLICATION_1;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.DATE_PUBLICATION_2;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.DATE_SIGNATURE_1;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.DATE_SIGNATURE_2;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.DATE_VALIDATION_1;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.DATE_VALIDATION_2;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.DISTRIBUTION_MAILBOX_ID;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.ID_AUTEUR;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.ID_CATEGORY_ACTE;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.ID_DIRECTION_RESPONSABLE;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.ID_MINISTERE_RESPONSABLE;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.ID_POSTE;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.ID_STATUT;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.ID_STATUT_ARCHIVAGE_DOSSIER;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.ID_STATUT_DOSSIER;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.ID_TYPE_ACTE;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.INDEXATION_CHAMP_LIBRE;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.INDEXATION_CHAMP_LIBRE_MODIFIED;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.INDEXATION_MOTS_CLEFS;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.INDEXATION_MOTS_CLEFS_MODIFIED;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.INDEXATION_RUBRIQUE;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.INDEXATION_RUBRIQUE_MODIFIED;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.NOTE;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.NOTE_MODIFIED;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.NUMERO_NOR;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.NUMERO_NOR_MODIFIED;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.NUMERO_TEXTE;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.REQUETE_DOSSIER_SIMPLE_CRITERES_ETAPES_SCHEMA;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.REQUETE_DOSSIER_SIMPLE_CRITERES_PRINCIPAUX_SCHEMA;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.REQUETE_DOSSIER_SIMPLE_CRITERES_SECONDAIRES_SCHEMA;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.REQUETE_DOSSIER_SIMPLE_TEXTE_INTEGRAL_SCHEMA;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.SOUS_CLAUSE_TEXTE_INTEGRAL;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.TEXTE_INTEGRAL;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.TITRE_ACTE;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.TITRE_ACTE_MODIFIED;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.TRANSPOSITION_DIRECTIVE;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.TRANSPOSITION_ORDONNANCE;
import static fr.dila.solonepg.api.constant.SolonEpgRequeteDossierSimpleConstants.VALIDATION_STATUT_ID;

import fr.dila.solonepg.api.recherche.dossier.RequeteDossierSimple;
import fr.dila.solonepg.core.recherche.dossier.traitement.EPGFulltextIndexEnum;
import fr.dila.solonepg.core.recherche.dossier.traitement.EPGGeneralRequeteTraitement;
import fr.dila.st.api.constant.STQueryConstant;
import fr.dila.st.api.recherche.RequeteTraitement;
import fr.dila.st.core.domain.STDomainObjectImpl;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 *
 * L'implementation d'une requete, c'est-à-dire l'objet créé pour effectuer une recherche
 * @author jgomez
 *
 */
public class RequeteDossierSimpleImpl extends STDomainObjectImpl implements RequeteDossierSimple {
    private static final long serialVersionUID = 6160682333116646611L;

    private transient RequeteTraitement<RequeteDossierSimple> traitement;

    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(RequeteDossierSimpleImpl.class);

    public RequeteDossierSimpleImpl(DocumentModel doc) {
        super(doc);
        this.traitement = new EPGGeneralRequeteTraitement();
    }

    /** Méthode de EPG RequeteTraitement **/
    @Override
    public void init() {
        this.traitement.init(this);
    }

    @Override
    public void doBeforeQuery() {
        this.traitement.doBeforeQuery(this);
    }

    @Override
    public String getQueryType() {
        return STQueryConstant.UFNXQL;
    }

    private void setPropertyCP(String propertyName, Object value) {
        super.setProperty(REQUETE_DOSSIER_SIMPLE_CRITERES_PRINCIPAUX_SCHEMA, propertyName, value);
    }

    private String getStringPropertyCP(String propertyName) {
        return super.getStringProperty(REQUETE_DOSSIER_SIMPLE_CRITERES_PRINCIPAUX_SCHEMA, propertyName);
    }

    private List<String> getListStringPropertyCP(String propertyName) {
        return super.getListStringProperty(REQUETE_DOSSIER_SIMPLE_CRITERES_PRINCIPAUX_SCHEMA, propertyName);
    }

    private void setPropertyCS(String propertyName, Object value) {
        super.setProperty(REQUETE_DOSSIER_SIMPLE_CRITERES_SECONDAIRES_SCHEMA, propertyName, value);
    }

    private String getStringPropertyCS(String propertyName) {
        return super.getStringProperty(REQUETE_DOSSIER_SIMPLE_CRITERES_SECONDAIRES_SCHEMA, propertyName);
    }

    private Calendar getDatePropertyCS(String propertyName) {
        return super.getDateProperty(REQUETE_DOSSIER_SIMPLE_CRITERES_SECONDAIRES_SCHEMA, propertyName);
    }

    private void setPropertyCE(String propertyName, Object value) {
        super.setProperty(REQUETE_DOSSIER_SIMPLE_CRITERES_ETAPES_SCHEMA, propertyName, value);
    }

    private Calendar getDatePropertyCE(String propertyName) {
        return super.getDateProperty(REQUETE_DOSSIER_SIMPLE_CRITERES_ETAPES_SCHEMA, propertyName);
    }

    private String getStringPropertyCE(String propertyName) {
        return super.getStringProperty(REQUETE_DOSSIER_SIMPLE_CRITERES_ETAPES_SCHEMA, propertyName);
    }

    private List<String> getListStringPropertyCE(String propertyName) {
        return super.getListStringProperty(REQUETE_DOSSIER_SIMPLE_CRITERES_ETAPES_SCHEMA, propertyName);
    }

    private void setPropertyTI(String propertyName, Object value) {
        super.setProperty(REQUETE_DOSSIER_SIMPLE_TEXTE_INTEGRAL_SCHEMA, propertyName, value);
    }

    private String getStringPropertyTI(String propertyName) {
        return super.getStringProperty(REQUETE_DOSSIER_SIMPLE_TEXTE_INTEGRAL_SCHEMA, propertyName);
    }

    private Boolean getBooleanPropertyTI(String propertyName) {
        return super.getBooleanProperty(REQUETE_DOSSIER_SIMPLE_TEXTE_INTEGRAL_SCHEMA, propertyName);
    }

    @Override
    public void setIdStatutDossier(List<String> idStatutDossier) {
        setPropertyCP(ID_STATUT_DOSSIER, idStatutDossier);
    }

    @Override
    public List<String> getIdStatutDossier() {
        return getListStringPropertyCP(ID_STATUT_DOSSIER);
    }

    @Override
    public void setNumeroNor(String numeroNor) {
        setPropertyCP(NUMERO_NOR, numeroNor);
    }

    @Override
    public String getNumeroNor() {
        return getStringPropertyCP(NUMERO_NOR);
    }

    @Override
    public void setNumeroNorModified(String numeroNor) {
        setPropertyCP(NUMERO_NOR_MODIFIED, numeroNor);
    }

    @Override
    public String getNumeroNorModified() {
        return getStringPropertyCP(NUMERO_NOR_MODIFIED);
    }

    @Override
    public void setIdAuteur(String idAuteur) {
        setPropertyCP(ID_AUTEUR, idAuteur);
    }

    @Override
    public String getIdAuteur() {
        return getStringPropertyCP(ID_AUTEUR);
    }

    @Override
    public void setIdMinistereResponsable(List<String> idMinistereResponsable) {
        setPropertyCP(ID_MINISTERE_RESPONSABLE, idMinistereResponsable);
    }

    @Override
    public List<String> getIdMinistereResponsable() {
        return getListStringPropertyCP(ID_MINISTERE_RESPONSABLE);
    }

    @Override
    public void setIdDirectionResponsable(String idDirectionResponsable) {
        setPropertyCP(ID_DIRECTION_RESPONSABLE, idDirectionResponsable);
    }

    @Override
    public String getIdDirectionResponsable() {
        return getStringPropertyCP(ID_DIRECTION_RESPONSABLE);
    }

    @Override
    public void setTitreActe(String titreActe) {
        setPropertyCP(TITRE_ACTE, titreActe);
    }

    @Override
    public String getTitreActe() {
        return getStringPropertyCP(TITRE_ACTE);
    }

    @Override
    public void setTitreActeModified(String titreActeModified) {
        setPropertyCP(TITRE_ACTE_MODIFIED, titreActeModified);
    }

    @Override
    public String getTitreActeModifie() {
        return getStringPropertyCP(TITRE_ACTE_MODIFIED);
    }

    @Override
    public void setIdTypeActe(List<String> idTypeActe) {
        setPropertyCP(ID_TYPE_ACTE, idTypeActe);
    }

    @Override
    public List<String> getIdTypeActe() {
        return getListStringPropertyCP(ID_TYPE_ACTE);
    }

    @Override
    public String getIdCategoryActe() {
        return getStringPropertyCS(ID_CATEGORY_ACTE);
    }

    @Override
    public void setIdCategorieActe(String idCategoryActe) {
        setPropertyCS(ID_CATEGORY_ACTE, idCategoryActe);
    }

    @Override
    public String getTranspositionDirective() {
        return getStringPropertyCS(TRANSPOSITION_DIRECTIVE);
    }

    @Override
    public void setTranspositionDirective(String transpositionDirective) {
        setPropertyCS(TRANSPOSITION_DIRECTIVE, transpositionDirective);
    }

    @Override
    public String getTranspositionOrdonnance() {
        return getStringPropertyCS(TRANSPOSITION_ORDONNANCE);
    }

    @Override
    public void setTranspositionOrdonnance(String transpositionOrdonnance) {
        setPropertyCS(TRANSPOSITION_ORDONNANCE, transpositionOrdonnance);
    }

    @Override
    public String getApplicationLoi() {
        return getStringPropertyCS(APPLICATION_LOI);
    }

    @Override
    public void setApplicationLoi(String applicationLoi) {
        setPropertyCS(APPLICATION_LOI, applicationLoi);
    }

    @Override
    public String getIndexationRubrique() {
        return getStringPropertyCS(INDEXATION_RUBRIQUE);
    }

    @Override
    public void setIndexationRubrique(String indexationRubrique) {
        setPropertyCS(INDEXATION_RUBRIQUE, indexationRubrique);
    }

    @Override
    public String getIndexationRubriqueModified() {
        return getStringPropertyCS(INDEXATION_RUBRIQUE_MODIFIED);
    }

    @Override
    public void setIndexationRubriqueModified(String indexationRubrique) {
        setPropertyCS(INDEXATION_RUBRIQUE_MODIFIED, indexationRubrique);
    }

    @Override
    public String getIndexationMotsClefs() {
        return getStringPropertyCS(INDEXATION_MOTS_CLEFS);
    }

    @Override
    public void setIndexationMotsClefs(String indexationMotsClef) {
        setPropertyCS(INDEXATION_MOTS_CLEFS, indexationMotsClef);
    }

    @Override
    public String getIndexationMotsClefsModified() {
        return getStringPropertyCS(INDEXATION_MOTS_CLEFS_MODIFIED);
    }

    @Override
    public void setIndexationMotsClefsModified(String indexationMotsClefs) {
        setPropertyCS(INDEXATION_MOTS_CLEFS_MODIFIED, indexationMotsClefs);
    }

    @Override
    public String getIndexationChampLibre() {
        return getStringPropertyCS(INDEXATION_CHAMP_LIBRE);
    }

    @Override
    public void setIndexationChampLibre(String indexationChampLibre) {
        setPropertyCS(INDEXATION_CHAMP_LIBRE, indexationChampLibre);
    }

    @Override
    public String getIndexationChampLibreModified() {
        return getStringPropertyCS(INDEXATION_CHAMP_LIBRE_MODIFIED);
    }

    @Override
    public void setIndexationChampLibreModified(String indexationChampLibre) {
        setPropertyCS(INDEXATION_CHAMP_LIBRE_MODIFIED, indexationChampLibre);
    }

    @Override
    public Calendar getDateSignature_1() {
        return getDatePropertyCS(DATE_SIGNATURE_1);
    }

    @Override
    public void setDateSignature_1(Calendar dateSignature_1) {
        setPropertyCS(DATE_SIGNATURE_1, dateSignature_1);
    }

    @Override
    public Calendar getDateSignature_2() {
        return getDatePropertyCS(DATE_SIGNATURE_2);
    }

    @Override
    public void setDateSignature_2(Calendar dateSignature_2) {
        setPropertyCS(DATE_SIGNATURE_2, dateSignature_2);
    }

    @Override
    public Calendar getDatePublication_1() {
        return getDatePropertyCS(DATE_PUBLICATION_1);
    }

    @Override
    public void setDatePublication_1(Calendar datePublication_1) {
        setPropertyCS(DATE_PUBLICATION_1, datePublication_1);
    }

    @Override
    public Calendar getDatePublication_2() {
        return getDatePropertyCS(DATE_PUBLICATION_2);
    }

    @Override
    public void setDatePublication_2(Calendar datePublication_2) {
        setPropertyCS(DATE_PUBLICATION_2, datePublication_2);
    }

    @Override
    public String getNumeroTexte() {
        return getStringPropertyCS(NUMERO_TEXTE);
    }

    @Override
    public void setNumeroTexte(String numeroTexte) {
        setPropertyCS(NUMERO_TEXTE, numeroTexte);
    }

    @Override
    public Long getIdStatutArchivageDossier() {
        return PropertyUtil.getLongProperty(
            this.document,
            REQUETE_DOSSIER_SIMPLE_CRITERES_PRINCIPAUX_SCHEMA,
            ID_STATUT_ARCHIVAGE_DOSSIER
        );
    }

    @Override
    public void setIdStatutArchivageDossier(Long idStatutArchivageDossier) {
        setPropertyCP(ID_STATUT_ARCHIVAGE_DOSSIER, idStatutArchivageDossier);
    }

    @Override
    public String getEtapeIdAction() {
        return null;
    }

    @Override
    public void setEtapeIdAction(String idAction) {
        return;
    }

    @Override
    public void setEtapeCurrentCycleState(String etapeCurrentCycleState) {
        setPropertyCE(CURRENT_LIFE_CYCLE_STATE, etapeCurrentCycleState);
    }

    @Override
    public void setValidationStatutId(String validationStatutId) {
        setPropertyCE(VALIDATION_STATUT_ID, validationStatutId);
    }

    @Override
    public String getEtapeIdStatut() {
        return getStringPropertyCE(ID_STATUT);
    }

    @Override
    public void setEtapeIdStatut(String idStatut) {
        setPropertyCE(ID_STATUT, idStatut);
    }

    @Override
    public Calendar getEtapeDateActivation_1() {
        return getDatePropertyCE(DATE_ACTIVATION_1);
    }

    @Override
    public void setEtapeDateActivation_1(Calendar dateActivation1) {
        setPropertyCE(DATE_ACTIVATION_1, dateActivation1);
    }

    @Override
    public Calendar getEtapeDateActivation_2() {
        return getDatePropertyCE(DATE_ACTIVATION_2);
    }

    @Override
    public void setEtapeDateActivation_2(Calendar dateActivation2) {
        setPropertyCE(DATE_ACTIVATION_2, dateActivation2);
    }

    @Override
    public Calendar getEtapeDateValidation_1() {
        return getDatePropertyCE(DATE_VALIDATION_1);
    }

    @Override
    public void setEtapeDateValidation_1(Calendar dateValidation1) {
        setPropertyCE(DATE_VALIDATION_1, dateValidation1);
    }

    @Override
    public Calendar getEtapeDateValidation_2() {
        return getDatePropertyCE(DATE_VALIDATION_2);
    }

    @Override
    public void setEtapeDateValidation_2(Calendar dateValidation2) {
        setPropertyCE(DATE_VALIDATION_2, dateValidation2);
    }

    @Override
    public List<String> getEtapeIdPoste() {
        return getListStringPropertyCE(ID_POSTE);
    }

    @Override
    public void setEtapeIdPoste(List<String> idPoste) {
        setPropertyCE(ID_POSTE, idPoste);
    }

    @Override
    public List<String> getEtapeDistributionMailboxId() {
        return getListStringPropertyCE(DISTRIBUTION_MAILBOX_ID);
    }

    @Override
    public void setEtapeDistributionMailboxId(List<String> idMailbox) {
        setPropertyCE(DISTRIBUTION_MAILBOX_ID, idMailbox);
    }

    @Override
    public String getEtapeIdAuteur() {
        return getStringPropertyCE(ID_AUTEUR);
    }

    @Override
    public void setEtapeIdAuteur(String idAuteur) {
        setPropertyCE(ID_AUTEUR, idAuteur);
    }

    @Override
    public String getEtapeNote() {
        return getStringPropertyCE(NOTE);
    }

    @Override
    public void setEtapeNote(String note) {
        setPropertyCE(NOTE, note);
    }

    @Override
    public String getEtapeNoteModified() {
        return getStringPropertyCE(NOTE_MODIFIED);
    }

    @Override
    public void setEtapeNoteModified(String note) {
        setPropertyCE(NOTE_MODIFIED, note);
    }

    @Override
    public String getTexteIntegral() {
        return getStringPropertyTI(TEXTE_INTEGRAL);
    }

    @Override
    public void setTexteIntegral(String texte) {
        setPropertyTI(TEXTE_INTEGRAL, texte);
    }

    @Override
    public Boolean getTIDansActe() {
        return getBooleanPropertyTI(DANS_ACTE);
    }

    @Override
    public void setTIDansActe(Boolean dansActe) {
        setPropertyTI(DANS_ACTE, dansActe);
    }

    @Override
    public Boolean getTIDansExtrait() {
        return getBooleanPropertyTI(DANS_EXTRAIT);
    }

    @Override
    public void setTIDansExtrait(Boolean dansExtrait) {
        setPropertyTI(DANS_EXTRAIT, dansExtrait);
    }

    @Override
    public Boolean getTIDansAutrePieceParapheur() {
        return getBooleanPropertyTI(DANS_AUTRE_PIECE_PARAPHEUR);
    }

    @Override
    public void setTIDansAutrePieceParapheur(Boolean dansAutrePieceParapheur) {
        setPropertyTI(DANS_AUTRE_PIECE_PARAPHEUR, dansAutrePieceParapheur);
    }

    @Override
    public Boolean getTIDansFondDossier() {
        return getBooleanPropertyTI(DANS_FOND_DOSSIER);
    }

    @Override
    public void setTIDansFondDossier(Boolean dansFondDossier) {
        setPropertyTI(DANS_FOND_DOSSIER, dansFondDossier);
    }

    @Override
    public void setSousClauseTexteIntegral(String rawSubClause) {
        setPropertyTI(SOUS_CLAUSE_TEXTE_INTEGRAL, rawSubClause);
    }

    @Override
    public String getSousClauseTexteIntegral() {
        return getStringPropertyTI(SOUS_CLAUSE_TEXTE_INTEGRAL);
    }

    public void setTraitement(RequeteTraitement<RequeteDossierSimple> traitement) {
        this.traitement = traitement;
    }

    public RequeteTraitement<RequeteDossierSimple> getTraitement() {
        return traitement;
    }

    @Override
    public List<String> getFiletypeIds() {
        List<String> chosenFiletypeIds = new ArrayList<>();
        for (EPGFulltextIndexEnum index : EPGFulltextIndexEnum.values()) {
            if (index.isNeededBy(this)) {
                chosenFiletypeIds.add(String.valueOf(index.getFiletypeId()));
            }
        }
        return chosenFiletypeIds;
    }
}
