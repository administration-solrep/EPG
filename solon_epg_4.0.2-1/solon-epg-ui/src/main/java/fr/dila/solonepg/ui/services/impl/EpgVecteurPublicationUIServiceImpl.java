package fr.dila.solonepg.ui.services.impl;

import fr.dila.solonepg.api.administration.VecteurPublication;
import fr.dila.solonepg.api.constant.SolonEpgBaseFunctionConstant;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonepg.ui.bean.dossier.vecteurpublication.AdminPublicationMinisterielleDTO;
import fr.dila.solonepg.ui.bean.dossier.vecteurpublication.AdminVecteurPublicationDTO;
import fr.dila.solonepg.ui.enums.EpgContextDataKey;
import fr.dila.solonepg.ui.services.EpgVecteurPublicationUIService;
import fr.dila.solonepg.ui.th.bean.AdminPublicationMinisterielleForm;
import fr.dila.solonepg.ui.th.bean.AdminVecteurPublicationForm;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.core.util.ObjectHelper;
import fr.dila.st.core.util.ResourceHelper;
import fr.dila.st.ui.enums.STContextDataKey;
import fr.dila.st.ui.mapper.MapDoc2Bean;
import fr.dila.st.ui.th.model.SpecificContext;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;

public class EpgVecteurPublicationUIServiceImpl implements EpgVecteurPublicationUIService {
    private static final String ACCESS_DENIED_KEY = "error.403.shortmessage";

    public boolean isAccessAuthorized(SpecificContext context) {
        SSPrincipal ssPrincipal = (SSPrincipal) context.getSession().getPrincipal();
        return (
            ssPrincipal.isAdministrator() ||
            ssPrincipal.isMemberOf(SolonEpgBaseFunctionConstant.ADMINISTRATION_BULLETIN_READER)
        );
    }

    @Override
    public void createPublicationMinisterielle(SpecificContext context) {
        if (!isAccessAuthorized(context)) {
            context.getMessageQueue().addErrorToQueue(ACCESS_DENIED_KEY);
            return;
        }

        AdminPublicationMinisterielleDTO publicationMinisterielle = context.getFromContextData(
            EpgContextDataKey.ADMIN_PUBLICATION_MINISTERIELLE
        );

        CoreSession session = context.getSession();
        SolonEpgServiceLocator
            .getBulletinOfficielService()
            .create(session, publicationMinisterielle.getIdMinistere(), publicationMinisterielle.getIntitule());
        context.getMessageQueue().addSuccessToQueue(ResourceHelper.getString("admin.bulletin.officiel.add.ok"));
    }

    @Override
    public List<AdminPublicationMinisterielleDTO> getBulletinsOfficiels(SpecificContext context) {
        if (!isAccessAuthorized(context)) {
            context.getMessageQueue().addErrorToQueue(ACCESS_DENIED_KEY);
            return Collections.emptyList();
        }

        AdminPublicationMinisterielleForm form = context.getFromContextData(
            EpgContextDataKey.ADMIN_PUBLICATION_MINISTERIELLE_FORM
        );
        form = ObjectHelper.requireNonNullElseGet(form, AdminPublicationMinisterielleForm::newForm);
        BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
        DocumentModelList bulletins = bulletinOfficielService.getAllActiveBulletinOfficielOrdered(
            context.getSession(),
            form.getSortInfos()
        );
        return bulletins
            .stream()
            .map(d -> MapDoc2Bean.docToBean(d, AdminPublicationMinisterielleDTO.class))
            .collect(Collectors.toList());
    }

    @Override
    public void deleteBulletinOfficiel(SpecificContext context) {
        if (!isAccessAuthorized(context)) {
            context.getMessageQueue().addErrorToQueue(ACCESS_DENIED_KEY);
            return;
        }

        String bulletinOfficielId = context.getFromContextData(STContextDataKey.ID);
        SolonEpgServiceLocator.getBulletinOfficielService().delete(context.getSession(), bulletinOfficielId);
        context.getMessageQueue().addSuccessToQueue(ResourceHelper.getString("admin.bulletin.officiel.delete.ok"));
    }

    @Override
    public void addVecteur(SpecificContext context) {
        if (!isAccessAuthorized(context)) {
            context.getMessageQueue().addErrorToQueue(ACCESS_DENIED_KEY);
            return;
        }

        CoreSession session = context.getSession();
        BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
        DocumentModel vecteurDoc = bulletinOfficielService.initVecteurPublication(session);
        AdminVecteurPublicationDTO vecteur = context.getFromContextData(EpgContextDataKey.ADMIN_VECTEUR_PUBLICATION);

        saveVecteur(session, vecteurDoc, vecteur, true);
        context.getMessageQueue().addSuccessToQueue("admin.vecteur.publication.add.ok");
    }

    @Override
    public void editVecteur(SpecificContext context) {
        if (!isAccessAuthorized(context)) {
            context.getMessageQueue().addErrorToQueue(ACCESS_DENIED_KEY);
            return;
        }

        CoreSession session = context.getSession();
        AdminVecteurPublicationDTO vecteur = context.getFromContextData(EpgContextDataKey.ADMIN_VECTEUR_PUBLICATION);
        DocumentModel vecteurDoc = session.getDocument(new IdRef(vecteur.getId()));

        saveVecteur(session, vecteurDoc, vecteur, false);
        context.getMessageQueue().addSuccessToQueue("admin.vecteur.publication.edit.ok");
    }

    private void saveVecteur(
        CoreSession session,
        DocumentModel vecteurDoc,
        AdminVecteurPublicationDTO vecteur,
        boolean create
    ) {
        MapDoc2Bean.beanToDoc(vecteur, vecteurDoc);
        BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
        bulletinOfficielService.saveVecteurPublication(
            session,
            vecteurDoc.getAdapter(VecteurPublication.class),
            create
        );
    }

    @Override
    public List<AdminVecteurPublicationDTO> getVecteurPublications(SpecificContext context) {
        if (!isAccessAuthorized(context)) {
            context.getMessageQueue().addErrorToQueue(ACCESS_DENIED_KEY);
        }

        AdminVecteurPublicationForm form = context.getFromContextData(EpgContextDataKey.ADMIN_VECTEUR_PUBLICATION_FORM);
        form = ObjectHelper.requireNonNullElseGet(form, AdminVecteurPublicationForm::newForm);

        BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
        List<DocumentModel> vecteurs = bulletinOfficielService.getAllVecteurPublication(
            context.getSession(),
            form.getSortInfos()
        );
        return vecteurs
            .stream()
            .map(d -> MapDoc2Bean.docToBean(d, AdminVecteurPublicationDTO.class))
            .collect(Collectors.toList());
    }
}
