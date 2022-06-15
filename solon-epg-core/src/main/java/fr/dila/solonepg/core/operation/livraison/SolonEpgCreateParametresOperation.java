package fr.dila.solonepg.core.operation.livraison;

import static fr.dila.solonepg.api.constant.SolonEpgParametreConstant.DUA_17;
import static fr.dila.solonepg.api.constant.SolonEpgParametreConstant.DUA_51;
import static fr.dila.solonepg.api.constant.SolonEpgParametreConstant.SOLON_EDIT_VERSION_DESCRIPTION;
import static fr.dila.solonepg.api.constant.SolonEpgParametreConstant.SOLON_EDIT_VERSION_NAME;
import static fr.dila.solonepg.api.constant.SolonEpgParametreConstant.SOLON_EDIT_VERSION_TITRE;
import static fr.dila.solonepg.api.constant.SolonEpgParametreConstant.SOLON_EDIT_VERSION_UNIT;
import static fr.dila.solonepg.api.constant.SolonEpgParametreConstant.SOLON_EDIT_VERSION_VALUE;
import static fr.dila.st.api.constant.STParametreConstant.ADRESSE_MEL_EMISSION_PARAMETER_NAME;
import static fr.dila.st.api.constant.STParametreConstant.ADRESSE_MEL_EMISSION_PARAMETER_NAME_DESCRIPTION;
import static fr.dila.st.api.constant.STParametreConstant.ADRESSE_MEL_EMISSION_PARAMETER_NAME_TITRE;
import static fr.dila.st.api.constant.STParametreConstant.ADRESSE_MEL_EMISSION_PARAMETER_NAME_UNIT;
import static fr.dila.st.api.constant.STParametreConstant.ADRESSE_MEL_EMISSION_PARAMETER_NAME_VALUE;
import static fr.dila.st.api.constant.STParametreConstant.MESSAGE_ACCESSIBILITE_LOGIN_PARAMETER_DESCRIPTION;
import static fr.dila.st.api.constant.STParametreConstant.MESSAGE_ACCESSIBILITE_LOGIN_PARAMETER_NAME;
import static fr.dila.st.api.constant.STParametreConstant.MESSAGE_ACCESSIBILITE_LOGIN_PARAMETER_TITRE;
import static fr.dila.st.api.constant.STParametreConstant.MESSAGE_ACCESSIBILITE_LOGIN_PARAMETER_UNIT;
import static fr.dila.st.api.constant.STParametreConstant.MESSAGE_ACCESSIBILITE_LOGIN_PARAMETER_VALUE;
import static fr.dila.st.api.constant.STParametreConstant.NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_OBJET;
import static fr.dila.st.api.constant.STParametreConstant.NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_OBJET_DESCRIPTION;
import static fr.dila.st.api.constant.STParametreConstant.NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_OBJET_TITLE;
import static fr.dila.st.api.constant.STParametreConstant.NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_OBJET_UNIT;
import static fr.dila.st.api.constant.STParametreConstant.NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_OBJET_VALUE;
import static fr.dila.st.api.constant.STParametreConstant.NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_TEXT;
import static fr.dila.st.api.constant.STParametreConstant.NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_TEXT_DESCRIPTION;
import static fr.dila.st.api.constant.STParametreConstant.NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_TEXT_TITLE;
import static fr.dila.st.api.constant.STParametreConstant.NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_TEXT_UNIT;
import static fr.dila.st.api.constant.STParametreConstant.NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_TEXT_VALUE;
import static fr.dila.st.api.constant.STParametreConstant.OBJET_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN;
import static fr.dila.st.api.constant.STParametreConstant.OBJET_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN_DESCRIPTION;
import static fr.dila.st.api.constant.STParametreConstant.OBJET_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN_TITRE;
import static fr.dila.st.api.constant.STParametreConstant.OBJET_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN_UNIT;
import static fr.dila.st.api.constant.STParametreConstant.OBJET_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN_VALUE;
import static fr.dila.st.api.constant.STParametreConstant.PAGE_INTERNET_DILA_PARAMETER_DESCRIPTION;
import static fr.dila.st.api.constant.STParametreConstant.PAGE_INTERNET_DILA_PARAMETER_NAME;
import static fr.dila.st.api.constant.STParametreConstant.PAGE_INTERNET_DILA_PARAMETER_TITRE;
import static fr.dila.st.api.constant.STParametreConstant.PAGE_INTERNET_DILA_PARAMETER_UNIT;
import static fr.dila.st.api.constant.STParametreConstant.PAGE_INTERNET_DILA_PARAMETER_VALUE;
import static fr.dila.st.api.constant.STParametreConstant.TEXT_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN;
import static fr.dila.st.api.constant.STParametreConstant.TEXT_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN_DESCRIPTION;
import static fr.dila.st.api.constant.STParametreConstant.TEXT_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN_TITRE;
import static fr.dila.st.api.constant.STParametreConstant.TEXT_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN_UNIT;
import static fr.dila.st.api.constant.STParametreConstant.TEXT_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN_VALUE;
import static fr.dila.st.api.constant.STParametreConstant.USER_DECONNEXION_DESACTIVATION_DELAI;
import static fr.dila.st.api.constant.STParametreConstant.USER_DECONNEXION_DESACTIVATION_DESCRIPTION;
import static fr.dila.st.api.constant.STParametreConstant.USER_DECONNEXION_DESACTIVATION_TITLE;
import static fr.dila.st.api.constant.STParametreConstant.USER_DECONNEXION_DESACTIVATION_UNIT;
import static fr.dila.st.api.constant.STParametreConstant.USER_DECONNEXION_DESACTIVATION_VALUE;
import static fr.dila.st.api.constant.STParametreConstant.USER_DECONNEXION_INFORMATION_DELAI;
import static fr.dila.st.api.constant.STParametreConstant.USER_DECONNEXION_INFORMATION_DESCRIPTION;
import static fr.dila.st.api.constant.STParametreConstant.USER_DECONNEXION_INFORMATION_TITLE;
import static fr.dila.st.api.constant.STParametreConstant.USER_DECONNEXION_INFORMATION_UNIT;
import static fr.dila.st.api.constant.STParametreConstant.USER_DECONNEXION_INFORMATION_VALUE;

import com.google.common.collect.ImmutableList;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.core.operation.STVersion;
import fr.dila.st.core.operation.utils.AbstractCreateParametersOperation;
import fr.dila.st.core.operation.utils.ArchivageParametreBean;
import fr.dila.st.core.operation.utils.ParametreBean;
import java.util.List;
import org.nuxeo.ecm.automation.OperationContext;
import org.nuxeo.ecm.automation.core.annotations.Context;
import org.nuxeo.ecm.automation.core.annotations.Operation;

/**
 * Operation pour ajouter des nouveaux paramètres dans EPG
 *
 */
@STVersion(version = "4.0.0")
@Operation(id = SolonEpgCreateParametresOperation.ID, category = STConstant.PARAMETRE_DOCUMENT_TYPE)
public class SolonEpgCreateParametresOperation extends AbstractCreateParametersOperation {
    public static final String ID = "SolonEpg.Parametre.Creation";

    @Context
    private OperationContext context;

    @Override
    protected List<ParametreBean> getParametreBeans() {
        return ImmutableList.of(
            new ParametreBean(
                ADRESSE_MEL_EMISSION_PARAMETER_NAME,
                ADRESSE_MEL_EMISSION_PARAMETER_NAME_TITRE,
                ADRESSE_MEL_EMISSION_PARAMETER_NAME_DESCRIPTION,
                ADRESSE_MEL_EMISSION_PARAMETER_NAME_UNIT,
                ADRESSE_MEL_EMISSION_PARAMETER_NAME_VALUE
            ),
            new ParametreBean(
                PAGE_INTERNET_DILA_PARAMETER_NAME,
                PAGE_INTERNET_DILA_PARAMETER_TITRE,
                PAGE_INTERNET_DILA_PARAMETER_DESCRIPTION,
                PAGE_INTERNET_DILA_PARAMETER_UNIT,
                PAGE_INTERNET_DILA_PARAMETER_VALUE
            ),
            new ParametreBean(
                MESSAGE_ACCESSIBILITE_LOGIN_PARAMETER_NAME,
                MESSAGE_ACCESSIBILITE_LOGIN_PARAMETER_TITRE,
                MESSAGE_ACCESSIBILITE_LOGIN_PARAMETER_DESCRIPTION,
                MESSAGE_ACCESSIBILITE_LOGIN_PARAMETER_UNIT,
                MESSAGE_ACCESSIBILITE_LOGIN_PARAMETER_VALUE
            ),
            new ArchivageParametreBean(
                DUA_17,
                "Durée d'utilité administrative (Décret du Président de la République)",
                "Durée d'utilité administrative (Décret du Président de la République) - les dossiers de la base d'archivage intermédiaire qui ont atteint leur DUA sont proposés pour être versés dans la base d'archivage définitive",
                "Mois",
                "120"
            ),
            new ArchivageParametreBean(
                DUA_51,
                "Durée d'utilité administrative (Accord collectif dans la fonction publique)",
                "Durée d'utilité administrative (Accord collectif dans la fonction publique) - les dossiers de la base d'archivage intermédiaire qui ont atteint leur DUA sont proposés pour être versés dans la base d'archivage définitive",
                "Mois",
                "120"
            ),
            new ParametreBean(
                USER_DECONNEXION_INFORMATION_DELAI,
                USER_DECONNEXION_INFORMATION_TITLE,
                USER_DECONNEXION_INFORMATION_DESCRIPTION,
                USER_DECONNEXION_INFORMATION_UNIT,
                USER_DECONNEXION_INFORMATION_VALUE
            ),
            new ParametreBean(
                USER_DECONNEXION_DESACTIVATION_DELAI,
                USER_DECONNEXION_DESACTIVATION_TITLE,
                USER_DECONNEXION_DESACTIVATION_DESCRIPTION,
                USER_DECONNEXION_DESACTIVATION_UNIT,
                USER_DECONNEXION_DESACTIVATION_VALUE
            ),
            new ParametreBean(
                NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_OBJET,
                NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_OBJET_TITLE,
                NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_OBJET_DESCRIPTION,
                NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_OBJET_UNIT,
                NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_OBJET_VALUE
            ),
            new ParametreBean(
                NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_TEXT,
                NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_TEXT_TITLE,
                NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_TEXT_DESCRIPTION,
                NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_TEXT_UNIT,
                NOTIFICATION_MAIL_USER_DECONNEXION_INFORMATION_DELAI_TEXT_VALUE
            ),
            new ParametreBean(
                OBJET_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN,
                OBJET_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN_TITRE,
                OBJET_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN_DESCRIPTION,
                OBJET_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN_UNIT,
                OBJET_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN_VALUE
            ),
            new ParametreBean(
                TEXT_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN,
                TEXT_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN_TITRE,
                TEXT_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN_DESCRIPTION,
                TEXT_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN_UNIT,
                TEXT_MAIL_DOSSIER_ELIMINATION_ADMIN_MIN_VALUE
            ),
            new ParametreBean(
                SOLON_EDIT_VERSION_NAME,
                SOLON_EDIT_VERSION_TITRE,
                SOLON_EDIT_VERSION_DESCRIPTION,
                SOLON_EDIT_VERSION_UNIT,
                SOLON_EDIT_VERSION_VALUE
            )
        );
    }

    @Override
    protected OperationContext getContext() {
        return context;
    }
}
