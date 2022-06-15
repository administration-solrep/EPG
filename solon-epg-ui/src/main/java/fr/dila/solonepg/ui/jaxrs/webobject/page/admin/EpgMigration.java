package fr.dila.solonepg.ui.jaxrs.webobject.page.admin;

import com.google.common.collect.ImmutableMap;
import fr.dila.solonepg.ui.services.EpgUIServiceLocator;
import fr.dila.solonepg.ui.th.model.EpgAdminTemplate;
import fr.dila.solonepg.ui.th.model.EpgUtilisateurTemplate;
import fr.dila.ss.ui.jaxrs.webobject.page.admin.SSMigration;
import fr.dila.ss.ui.services.organigramme.SSMigrationGouvernementUIService;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.ui.th.model.SpecificContext;
import fr.dila.st.ui.th.model.ThTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.nuxeo.ecm.webengine.model.WebObject;

@WebObject(type = "Migration")
public class EpgMigration extends SSMigration {

    public EpgMigration() {
        super();
    }

    public static final Map<String, String> MIGRATION_TYPES = ImmutableMap.of(
        OrganigrammeType.MINISTERE.getValue(),
        "migration.structure.ministere",
        OrganigrammeType.DIRECTION.getValue(),
        "migration.structure.direction",
        OrganigrammeType.UNITE_STRUCTURELLE.getValue(),
        "migration.structure.autre.ust",
        OrganigrammeType.POSTE.getValue(),
        "migration.structure.poste"
    );

    public static final Map<String, List<String>> ACTIONS = ImmutableMap.of(
        OrganigrammeType.MINISTERE.getValue(),
        Arrays.asList(
            MIGRATION_DEPLACER_ELEMENT_FILS,
            MIGRATION_REATTRIBUER_NOR_INITIES_LANCES,
            MIGRATION_BULLETINS_OFFICIELS,
            MIGRATION_LISTE_MOTS_CLES_GESTION_INDEXATION
        ),
        OrganigrammeType.DIRECTION.getValue(),
        Arrays.asList(
            MIGRATION_DEPLACER_ELEMENT_FILS,
            MIGRATION_MODIFIER_MINISTERE_DIRECTION_RATTACHEMENT,
            MIGRATION_REATTRIBUER_NOR_INITIES_LANCES
        ),
        OrganigrammeType.UNITE_STRUCTURELLE.getValue(),
        Arrays.asList(MIGRATION_DEPLACER_ELEMENT_FILS),
        OrganigrammeType.POSTE.getValue(),
        Arrays.asList(
            MIGRATION_DEPLACER_ELEMENT_FILS,
            MIGRATION_MIGRER_ETAPES_FDR_MODELES,
            MIGRATION_MISE_A_JOUR_DROITS_DOSSIERS,
            MIGRATION_MISE_A_JOUR_CORBEILLE_POSTE
        )
    );

    @Override
    protected SSMigrationGouvernementUIService getMigrationGouvernementUIService() {
        return EpgUIServiceLocator.getEpgMigrationGouvernementUIService();
    }

    @Override
    public Map<String, String> getMigrationTypes() {
        return MIGRATION_TYPES;
    }

    @Override
    public Map<String, List<String>> getActions() {
        return ACTIONS;
    }

    @Override
    protected ThTemplate getMyTemplate(SpecificContext context) {
        if (context.getWebcontext().getPrincipal().isMemberOf("EspaceAdministrationReader")) {
            return new EpgAdminTemplate();
        } else {
            return new EpgUtilisateurTemplate();
        }
    }
}
