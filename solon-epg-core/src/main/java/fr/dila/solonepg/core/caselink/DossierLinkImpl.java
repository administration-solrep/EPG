package fr.dila.solonepg.core.caselink;

import fr.dila.solonepg.api.caselink.DossierLink;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.solonepg.api.constant.VocabularyConstants;
import fr.dila.st.api.constant.STSchemaConstant;
import fr.dila.st.core.caselink.STDossierLinkImpl;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.Calendar;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Dossier Link Implementation.
 *
 * @author antoine Rolin
 *
 */
public class DossierLinkImpl extends STDossierLinkImpl implements DossierLink {
    private static final long serialVersionUID = -94563234903621891L;

    // ///////////////////////////////////////
    // Constructeurs
    // ///////////////////////////////////////

    public DossierLinkImpl(DocumentModel doc) {
        super(doc);
    }

    @Override
    protected String getDossierLinkSchema() {
        return DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA;
    }

    @Override
    public String getCaseLifeCycleState() {
        return getStringProperty(
            DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA,
            DossierSolonEpgConstants.DOSSIER_LINK_CASE_LIFE_CYCLE_STATE_PROPERTY
        );
    }

    @Override
    public void setCaseLifeCycleState(String caseLifeCycleState) {
        setProperty(
            DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA,
            DossierSolonEpgConstants.DOSSIER_LINK_CASE_LIFE_CYCLE_STATE_PROPERTY,
            caseLifeCycleState
        );
    }

    @Override
    public String getStatutArchivage() {
        return getStringProperty(
            DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA,
            DossierSolonEpgConstants.DOSSIER_LINK_STATUT_ARCHIVAGE_PROPERTY
        );
    }

    @Override
    public void setStatutArchivage(String statutArchivage) {
        // dénormalisation
        setArchive(
            !VocabularyConstants.STATUT_ARCHIVAGE_AUCUN.equals(statutArchivage) &&
            !VocabularyConstants.STATUT_ARCHIVAGE_CANDIDAT_BASE_INTERMEDIAIRE.equals(statutArchivage)
        );
        setProperty(
            DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA,
            DossierSolonEpgConstants.DOSSIER_LINK_STATUT_ARCHIVAGE_PROPERTY,
            statutArchivage
        );
    }

    @Override
    public String getTitreActe() {
        return getStringProperty(
            DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA,
            DossierSolonEpgConstants.DOSSIER_LINK__TITRE_ACTE_PROPERTY
        );
    }

    @Override
    public void setTitreActe(String titreActe) {
        setProperty(
            DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA,
            DossierSolonEpgConstants.DOSSIER_LINK__TITRE_ACTE_PROPERTY,
            titreActe
        );
    }

    @Override
    public Calendar getDateCreation() {
        return PropertyUtil.getCalendarProperty(
            document,
            DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA,
            STSchemaConstant.DUBLINCORE_CREATED_PROPERTY
        );
    }

    @Override
    public void setDateCreation(Calendar dateCreation) {
        PropertyUtil.setProperty(
            document,
            DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA,
            STSchemaConstant.DUBLINCORE_CREATED_PROPERTY,
            dateCreation
        );
    }

    @Override
    public String getCaseDocumentId() {
        return PropertyUtil.getStringProperty(
            document,
            DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA,
            DossierSolonEpgConstants.DOSSIER_LINK_CASE_DOCUMENT_ID
        );
    }

    @Override
    public void setCaseDocumentId(String caseDocumentId) {
        PropertyUtil.setProperty(
            document,
            DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA,
            DossierSolonEpgConstants.DOSSIER_LINK_CASE_DOCUMENT_ID,
            caseDocumentId
        );
    }

    @Override
    public void setArchive(Boolean archive) {
        setProperty(
            DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA,
            DossierSolonEpgConstants.ARCHIVE_DOSSIER_PROPERTY,
            archive
        );
    }

    @Override
    public Boolean getDeleted() {
        return PropertyUtil.getBooleanProperty(
            document,
            DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA,
            DossierSolonEpgConstants.DELETED
        );
    }

    @Override
    public void setDeleted(Boolean deleted) {
        setProperty(DossierSolonEpgConstants.DOSSIER_LINK_ACTIONABLE_SCHEMA, DossierSolonEpgConstants.DELETED, deleted);
    }
}
