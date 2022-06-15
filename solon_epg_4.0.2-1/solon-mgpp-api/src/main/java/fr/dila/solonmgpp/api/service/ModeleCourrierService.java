package fr.dila.solonmgpp.api.service;

import fr.dila.solonmgpp.api.domain.ModeleCourrier;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

public interface ModeleCourrierService {
    ModeleCourrier createModeleCourrier(
        CoreSession session,
        String modeleName,
        InputStream in,
        String fileName,
        List<String> typesCommunication
    );

    Optional<ModeleCourrier> getModeleCourrierFromModeleName(CoreSession session, String modele);

    Optional<ModeleCourrier> getModeleCourrierFromTemplateName(CoreSession session, String templateName);

    /**
     * @param session CoreSession
     * @return la liste des modèle de courrier qui ne sont pas supprimés
     */
    List<ModeleCourrier> getModelesCourrier(CoreSession session);

    /**
     * @param session           CoreSession
     * @param typeCommunication String
     * @return la liste des modèle de courrier disponnibles pour ce type de communication qui sont validés
     */
    List<DocumentModel> getModelesCourrierForCom(CoreSession session, String typeCommunication);

    /**
     * Valide un modèle de courrier en vue de sa création
     *
     * @param session CoreSession
     * @param modele  modele de courrier à créer
     */
    void validateModeleCourrier(CoreSession session, ModeleCourrier modele);

    /**
     * Valide un modèle de courrier en vue de sa création ou de sa mise à jour
     *
     * @param session   CoreSession
     * @param newModele modèle de courrier à créer ou à mettre à jour
     * @param oldModele ancien modèle, peut être null dans le cas d'une création
     */
    void validateModeleCourrier(CoreSession session, ModeleCourrier newModele, ModeleCourrier oldModele);

    ModeleCourrier getModeleCourrierFromTemplateNameOrThrow(CoreSession session, String modeleName);

    ModeleCourrier getModeleCourrierFromModeleNameOrThrow(CoreSession session, String modeleName);
}
