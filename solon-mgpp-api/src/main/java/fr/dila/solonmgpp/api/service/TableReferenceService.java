package fr.dila.solonmgpp.api.service;

import fr.dila.solonmgpp.api.dto.TableReferenceDTO;
import fr.dila.solonmgpp.api.node.GouvernementNode;
import fr.dila.solonmgpp.api.node.IdentiteNode;
import fr.dila.solonmgpp.api.node.MandatNode;
import fr.dila.solonmgpp.api.node.MinistereNode;
import fr.dila.solonmgpp.api.node.TableReferenceEppNode;
import fr.sword.xsd.solon.epp.Mandat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

/**
 * Interface du service des tables de references pour l'interaction epp/mgpp
 *
 * @author asatre
 */
public interface TableReferenceService {
    /**
     * chargement et mise en cache des tables de references nécessaires pour le MGPP <br/>
     * <br/>
     * <b>Méthode synchronisée</b>
     */
    void loadAllTableReference(CoreSession session);

    /**
     * chargement et mise en cache des tables de references nécessaires pour le MGPP en prenant aussi les éléments
     * inactifs <br/>
     * <br/>
     * <b>Méthode synchronisée</b>
     */
    void loadAllTableReferenceActifAndInactif(CoreSession session);

    /**
     * recherche dans les tables de references de l'id
     */
    TableReferenceDTO findTableReferenceByIdAndType(String identifiant, String tableRef, Date date);

    /**
     * recherche dans les tables de references de l'id
     *
     * @param identifiant
     * @param tableRef
     * @return
     */
    TableReferenceDTO findTableReferenceByIdAndType(
        String identifiant,
        String tableRef,
        Date date,
        CoreSession session
    );

    /**
     * @param identifiant
     * @param tableRef
     * @param date
     * @param session
     * @param skipDate
     * @return
     */
    TableReferenceDTO findTableReferenceByIdAndType(
        String identifiant,
        String tableRef,
        Date date,
        CoreSession session,
        boolean skipDate
    );

    /**
     * Recherche dans une table de reference
     *
     * @param search
     * @param tableReference
     * @return
     */
    List<TableReferenceDTO> searchTableReference(String search, String tableReference, CoreSession session);

    /**
     * Recherche d'une table de ref dans EPP
     *
     * @param identifiant
     * @param tableRef
     * @param date
     * @return
     */
    TableReferenceDTO findTableReferenceByIdAndTypeWS(
        String identifiant,
        String tableReference,
        Date date,
        CoreSession session
    );

    /**
     * Recherche dans les tables de references (avec appartenance au proprietaire seulement)
     *
     * @param search
     * @param tableReference
     * @param session
     * @param proprietaire
     * @return
     */
    List<TableReferenceDTO> searchTableReference(
        String search,
        boolean basicSearch,
        String tableReference,
        CoreSession session,
        String proprietaire,
        String organismeType
    );

    List<TableReferenceEppNode> getGouvernementList(CoreSession session);

    List<TableReferenceEppNode> getChildrenList(TableReferenceEppNode parentNode, CoreSession session);

    List<TableReferenceDTO> searchTableReferenceAuteurSuggestion(
        String search,
        String tableReference,
        CoreSession session,
        String proprietaire,
        String organismeType
    );

    void createGouvernement(GouvernementNode gouvernement, CoreSession session);

    void updateGouvernement(GouvernementNode gouvernement, CoreSession session);

    void createMinistere(MinistereNode ministere, CoreSession session);

    void updateMinistere(MinistereNode ministere, CoreSession session);

    void createMandat(MandatNode mandatNode, CoreSession session);

    void updateMandat(MandatNode mandatNode, CoreSession session);

    void createIdentite(IdentiteNode identiteNode, CoreSession session);

    void createIdentite(MandatNode mandatNode, CoreSession session);

    void updateIdentite(MandatNode mandatNode, CoreSession session);

    void createActeur(MandatNode mandatNode, CoreSession session);

    void updateActeur(MandatNode mandatNode, CoreSession session);

    GouvernementNode getGouvernement(String gouvernementId, CoreSession session);

    GouvernementNode getCurrentGouvernement(CoreSession session);

    MinistereNode getMinistere(String ministereId, CoreSession session);

    MandatNode getMandat(String mandatId, CoreSession session);

    /**
     * Récupère les mandats actifs en fonction du nor associé
     *
     * @param nor
     * @param session
     * @return List mandats actifs rattachés au nor ; liste vide si aucun résultat
     */
    List<Mandat> getMandatsByNor(String nor, CoreSession session);

    IdentiteNode getIdentiteNode(CoreSession session, String prenomNom);

    MinistereNode getMinistereNode(CoreSession session, String nom, String libelle, String appellation, String edition);

    /**
     * Met à jour le cache des identités et des organismes
     */
    void updateCaches(CoreSession documentManager);

    Calendar getLastUpdateCache();

    /**
     * Récupère une identité à partir de son id
     *
     * @param session
     * @param id      identifiant de l'identité
     * @return
     */
    IdentiteNode getIdentiteNodeFromId(CoreSession session, String id);
}
