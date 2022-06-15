package fr.dila.solonepg.core.recherche;

import fr.dila.solonepg.api.constant.SolonEpgSchemaConstant;
import fr.dila.solonepg.api.enums.FavorisRechercheType;
import fr.dila.solonepg.api.recherche.FavorisRecherche;
import fr.dila.st.core.domain.STDomainObjectImpl;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.Calendar;
import java.util.Optional;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Implémentation de l'objet métier favoris de recherche.
 *
 * @author jgomez
 * @author jtremeaux
 */
public class FavorisRechercheImpl extends STDomainObjectImpl implements FavorisRecherche {
    /**
     * Serial UD.
     */
    private static final long serialVersionUID = -95774957199970885L;

    /**
     * Constructeur de FavorisRechercheImpl.
     *
     * @param document Document
     */
    public FavorisRechercheImpl(DocumentModel document) {
        super(document);
    }

    // *************************************************************
    // Propriétés communes aux favoris de recherche.
    // *************************************************************
    @Override
    public String getQueryPart() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_QUERY_PROPERTY
        );
    }

    @Override
    public void setQueryPart(String query) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_QUERY_PROPERTY,
            query
        );
    }

    @Override
    public String getType() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_TYPE_PROPERTY
        );
    }

    @Override
    public FavorisRechercheType getTypeEnum() {
        return Optional
            .ofNullable(
                PropertyUtil.getStringProperty(
                    document,
                    SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
                    SolonEpgSchemaConstant.FAVORIS_RECHERCHE_TYPE_PROPERTY
                )
            )
            .map(FavorisRechercheType::fromType)
            .orElse(null);
    }

    @Override
    public void setType(FavorisRechercheType type) {
        String typeStr = null;
        if (type != null) {
            typeStr = type.getType();
        }
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_TYPE_PROPERTY,
            typeStr
        );
    }

    // *************************************************************
    // Critères de recherche des modèles de feuilles de route.
    // *************************************************************

    @Override
    public String getFeuilleRouteTitle() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_TITLE_PROPERTY
        );
    }

    @Override
    public void setFeuilleRouteTitle(String feuilleRouteTitle) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_TITLE_PROPERTY,
            feuilleRouteTitle
        );
    }

    @Override
    public String getFeuilleRouteCreationUtilisateur() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_CREATION_UTILISATEUR_PROPERTY
        );
    }

    @Override
    public void setFeuilleRouteCreationUtilisateur(String feuilleRouteCreationUtilisateur) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_CREATION_UTILISATEUR_PROPERTY,
            feuilleRouteCreationUtilisateur
        );
    }

    @Override
    public Calendar getFeuilleRouteCreationDateMin() {
        return PropertyUtil.getCalendarProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_CREATION_DATE_MIN_PROPERTY
        );
    }

    @Override
    public void setFeuilleRouteCreationDateMin(Calendar feuilleRouteCreationDateMin) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_CREATION_DATE_MIN_PROPERTY,
            feuilleRouteCreationDateMin
        );
    }

    @Override
    public Calendar getFeuilleRouteCreationDateMax() {
        return PropertyUtil.getCalendarProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_CREATION_DATE_MAX_PROPERTY
        );
    }

    @Override
    public void setFeuilleRouteCreationDateMax(Calendar feuilleRouteCreationDateMax) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_CREATION_DATE_MAX_PROPERTY,
            feuilleRouteCreationDateMax
        );
    }

    @Override
    public String getFeuilleRouteNumero() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_NUMERO_PROPERTY
        );
    }

    @Override
    public void setFeuilleRouteNumero(String feuilleRouteNumero) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_NUMERO_PROPERTY,
            feuilleRouteNumero
        );
    }

    @Override
    public String getFeuilleRouteTypeActe() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_TYPE_ACTE_PROPERTY
        );
    }

    @Override
    public void setFeuilleRouteTypeActe(String typeActe) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_TYPE_ACTE_PROPERTY,
            typeActe
        );
    }

    @Override
    public String getFeuilleRouteMinistere() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_MINISTERE_PROPERTY
        );
    }

    @Override
    public void setFeuilleRouteMinistere(String feuilleRouteMinistere) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_MINISTERE_PROPERTY,
            feuilleRouteMinistere
        );
    }

    @Override
    public String getFeuilleRouteDirection() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_DIRECTION_PROPERTY
        );
    }

    @Override
    public void setFeuilleRouteDirection(String feuilleRouteDirection) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_MINISTERE_PROPERTY,
            feuilleRouteDirection
        );
    }

    @Override
    public String getFeuilleRouteValidee() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_VALIDEE_PROPERTY
        );
    }

    @Override
    public void setFeuilleRouteValidee(String feuilleRouteValidee) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_FEUILLE_ROUTE_VALIDEE_PROPERTY,
            feuilleRouteValidee
        );
    }

    @Override
    public String getRouteStepRoutingTaskType() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_ROUTING_TASK_TYPE_PROPERTY
        );
    }

    @Override
    public void setRouteStepRoutingTaskType(String routeStepRoutingTaskType) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_ROUTING_TASK_TYPE_PROPERTY,
            routeStepRoutingTaskType
        );
    }

    @Override
    public String getRouteStepDistributionMailboxId() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_DISTRIBUTION_MAILBOX_ID_PROPERTY
        );
    }

    @Override
    public void setRouteStepDistributionMailboxId(String routeStepDistributionMailboxId) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_DISTRIBUTION_MAILBOX_ID_PROPERTY,
            routeStepDistributionMailboxId
        );
    }

    @Override
    public Long getRouteStepEcheanceIndicative() {
        return PropertyUtil.getLongProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_ECHEANCE_INDICATIVE_PROPERTY
        );
    }

    @Override
    public void setRouteStepEcheanceIndicative(Long routeStepEcheanceIndicative) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_ECHEANCE_INDICATIVE_PROPERTY,
            routeStepEcheanceIndicative
        );
    }

    @Override
    public String getRouteStepAutomaticValidation() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_AUTOMATIC_VALIDATION_PROPERTY
        );
    }

    @Override
    public void setRouteStepAutomaticValidation(String routeStepAutomaticValidation) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_AUTOMATIC_VALIDATION_PROPERTY,
            routeStepAutomaticValidation
        );
    }

    @Override
    public String getRouteStepObligatoireSgg() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_OBLIGATOIRE_SGG_PROPERTY
        );
    }

    @Override
    public void setRouteStepObligatoireSgg(String routeStepObligatoireSgg) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_OBLIGATOIRE_SGG_PROPERTY,
            routeStepObligatoireSgg
        );
    }

    @Override
    public String getRouteStepObligatoireMinistere() {
        return PropertyUtil.getStringProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_OBLIGATOIRE_MINISTERE_PROPERTY
        );
    }

    @Override
    public void setRouteStepObligatoireMinistere(String routeStepObligatoireMinistere) {
        PropertyUtil.setProperty(
            document,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_SCHEMA,
            SolonEpgSchemaConstant.FAVORIS_RECHERCHE_ROUTE_STEP_OBLIGATOIRE_MINISTERE_PROPERTY,
            routeStepObligatoireMinistere
        );
    }

    // *************************************************************
    // Propriétés calculées.
    // *************************************************************
    @Override
    public boolean isTypeDossier() {
        return FavorisRechercheType.ES_LIBRE.getType().equals(getType());
    }

    @Override
    public boolean isTypeDossierSimple() {
        return FavorisRechercheType.ES_EXPERTE.getType().equals(getType());
    }

    @Override
    public boolean isTypeUser() {
        return FavorisRechercheType.USER.getType().equals(getType());
    }

    @Override
    public boolean isTypeModeleFeuilleRoute() {
        return FavorisRechercheType.MODELE_FEUILLE_ROUTE.getType().equals(getType());
    }

    @Override
    public boolean isTypeActiviteNormative() {
        return FavorisRechercheType.ACTIVITE_NORMATIVE.getType().equals(getType().split("\\.")[0]);
    }

    @Override
    public String getActiviteNormativeTarget() {
        return getType().split("\\.")[1];
    }
}
