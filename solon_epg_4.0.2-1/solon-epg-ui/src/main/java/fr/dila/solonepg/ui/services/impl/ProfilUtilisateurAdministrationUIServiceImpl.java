package fr.dila.solonepg.ui.services.impl;

import static fr.dila.solonepg.core.service.SolonEpgServiceLocator.getProfilUtilisateurService;

import fr.dila.solonepg.api.administration.ProfilUtilisateur;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.ui.services.ProfilUtilisateurAdministrationUIService;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Service de gestion du profil utilisateur : edition des propriétés du profil utilisateur .
 *
 * @author arolin
 */
public class ProfilUtilisateurAdministrationUIServiceImpl implements ProfilUtilisateurAdministrationUIService {

    @Override
    public void addTypeActe(SpecificContext context, String typeActe) {
        if (StringUtils.isEmpty(typeActe)) {
            return;
        }

        CoreSession session = context.getSession();
        ProfilUtilisateur profil = getProfilUtilisateurService().getProfilUtilisateurForCurrentUser(session);
        List<String> listTypeActe = ObjectHelper.requireNonNullElseGet(
            profil.getNotificationTypeActes(),
            ArrayList::new
        );
        // on ajoute le format à la liste des formats autorisés si il n'est pas déjà contenu dans la liste
        if (!listTypeActe.contains(typeActe)) {
            listTypeActe.add(typeActe);
            profil.setNotificationTypeActes(listTypeActe);
            profil.save(session);
        }
    }

    @Override
    public void deleteTypeActe(SpecificContext context, String idVocabulaire) {
        if (StringUtils.isEmpty(idVocabulaire)) {
            return;
        }

        CoreSession session = context.getSession();
        ProfilUtilisateur profil = getProfilUtilisateurService().getProfilUtilisateurForCurrentUser(session);
        List<String> listTypeActe = ObjectHelper.requireNonNullElseGet(
            profil.getNotificationTypeActes(),
            ArrayList::new
        );
        if (!listTypeActe.contains(idVocabulaire)) {
            listTypeActe.remove(idVocabulaire);
            profil.setNotificationTypeActes(listTypeActe);
            profil.save(session);
        }
    }

    @Override
    public boolean isVisibleMesureNominative(SpecificContext context) {
        return context
            .getSession()
            .getPrincipal()
            .isMemberOf(SolonEpgBaseFunctionConstant.DOSSIER_MESURE_NOMINATIVE_READER);
    }
}
