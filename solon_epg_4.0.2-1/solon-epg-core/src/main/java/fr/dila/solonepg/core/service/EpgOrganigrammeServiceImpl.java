package fr.dila.solonepg.core.service;

import static fr.dila.st.core.service.STServiceLocator.getSTMinisteresService;
import static fr.dila.st.core.service.STServiceLocator.getSTUsAndDirectionService;

import fr.dila.solonepg.api.administration.BulletinOfficiel;
import fr.dila.solonepg.api.administration.IndexationMotCle;
import fr.dila.solonepg.api.cases.Dossier;
import fr.dila.solonepg.api.feuilleroute.SolonEpgFeuilleRoute;
import fr.dila.solonepg.api.service.BulletinOfficielService;
import fr.dila.solonepg.api.service.DossierService;
import fr.dila.solonepg.api.service.EPGFeuilleRouteService;
import fr.dila.solonepg.api.service.EpgOrganigrammeService;
import fr.dila.solonepg.api.service.FeuilleRouteModelService;
import fr.dila.solonepg.api.service.IndexationEpgService;
import fr.dila.ss.core.service.SSAbstractOrganigrammeService;
import fr.dila.st.api.constant.STConstant;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.GouvernementNode;
import fr.dila.st.api.organigramme.NorDirection;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.service.organigramme.OrganigrammeNodeDeletionProblem;
import fr.dila.st.api.service.organigramme.STMinisteresService;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.organigramme.EntiteNodeImpl;
import fr.dila.st.core.organigramme.GouvernementNodeImpl;
import fr.dila.st.core.organigramme.PosteNodeImpl;
import fr.dila.st.core.organigramme.UniteStructurelleNodeImpl;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.util.DirSessionUtil;
import fr.dila.st.core.util.ResourceHelper;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRoute;
import fr.sword.idl.naiad.nuxeo.feuilleroute.api.adapter.FeuilleRouteElement;
import fr.sword.naiad.nuxeo.commons.core.util.PropertyUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.NuxeoException;
import org.nuxeo.ecm.directory.Session;
import org.nuxeo.ecm.platform.usermanager.UserManager;

public class EpgOrganigrammeServiceImpl extends SSAbstractOrganigrammeService implements EpgOrganigrammeService {
    private static final String ALL_DIRECTION_QUERY =
        "SELECT u1 FROM UniteStructurelleNode u1 WHERE u1.type = '" + OrganigrammeType.DIRECTION.getValue() + "'";

    private static final Log LOG = LogFactory.getLog(EpgOrganigrammeServiceImpl.class);

    /**
     * UID
     */
    private static final long serialVersionUID = -5639299537909554785L;

    /**
     * Constructeur
     *
     * @
     */
    public EpgOrganigrammeServiceImpl() {
        super();
    }

    @Override
    public Set<String> findLettreNorByPoste(String posteId) {
        Set<String> lettreNORSet = new HashSet<>();

        // on récupère les directions des postes
        final STMinisteresService ministeresService = STServiceLocator.getSTMinisteresService();
        List<OrganigrammeNode> directionList = STServiceLocator
            .getSTUsAndDirectionService()
            .getDirectionFromPoste(posteId);
        for (OrganigrammeNode organigrammeNode : directionList) {
            if (organigrammeNode instanceof UniteStructurelleNode) {
                UniteStructurelleNode direction = (UniteStructurelleNode) organigrammeNode;
                List<NorDirection> norDirectionList = direction.getNorDirectionList();
                if (norDirectionList == null || norDirectionList.isEmpty()) {
                    continue;
                }

                // on récupère les ministère associée aux directions
                List<EntiteNode> ministereList = ministeresService.getMinistereParentFromUniteStructurelle(
                    direction.getId()
                );
                for (EntiteNode ministere : ministereList) {
                    String norMinistere = ministere.getNorMinistere();
                    if (norMinistere == null) {
                        continue;
                    }
                    String ministereId = ministere.getId();
                    String norDirection = direction.getNorDirectionForMinistereId(ministereId);
                    if (norDirection == null) {
                        continue;
                    }
                    lettreNORSet.add(norMinistere + norDirection);
                }
            }
        }

        return lettreNORSet;
    }

    @Override
    public List<STUser> getUserFromProfil(String profilId) {
        List<STUser> usersList = new ArrayList<>();

        final UserManager userManager = STServiceLocator.getUserManager();

        try (Session profilSession = DirSessionUtil.getSession(STConstant.ORGANIGRAMME_PROFILE_DIR)) {
            DocumentModel profilDocument = profilSession.getEntry(profilId);
            List<String> userIdList = PropertyUtil.getStringListProperty(profilDocument, "group", "members");

            for (String userId : userIdList) {
                DocumentModel userModel = userManager.getUserModel(userId);
                if (userModel == null) {
                    LOG.error("Aucun utilisateur ayant pour identifiant " + userId + " n'existe, non ajouté");
                } else {
                    STUser user = userModel.getAdapter(STUser.class);
                    usersList.add(user);
                }
            }
        }

        return usersList;
    }

    @Override
    public void copyNodeWithoutUser(CoreSession coreSession, OrganigrammeNode nodeToCopy, OrganigrammeNode parentNode) {
        copyNode(coreSession, nodeToCopy, parentNode, Boolean.FALSE);
    }

    @Override
    public void copyNodeWithUsers(CoreSession coreSession, OrganigrammeNode nodeToCopy, OrganigrammeNode parentNode) {
        copyNode(coreSession, nodeToCopy, parentNode, Boolean.TRUE);
    }

    private void copyNode(
        CoreSession coreSession,
        OrganigrammeNode nodeToCopy,
        OrganigrammeNode parentNode,
        Boolean withUser
    ) {
        if (!isCopyAllowed(nodeToCopy, parentNode)) {
            throw new NuxeoException(ResourceHelper.getString("error.organigramme.service.copy.not.allowed"));
        }
        boolean addCopyOfLabel = false;

        // si nom identique sous le même parent : ajout de "Copie de" pour
        // le premier noeud
        String nodeToCopyLabel = nodeToCopy.getLabel();
        if (StringUtils.isNotEmpty(nodeToCopyLabel)) {
            List<OrganigrammeNode> childList = getChildrenList(null, parentNode, Boolean.TRUE);
            for (OrganigrammeNode child : childList) {
                if (nodeToCopyLabel.equals(child.getLabel())) {
                    addCopyOfLabel = true;
                    break;
                }
            }
        }

        // si copie sous le même parent : ajout de "Copie de" pour le
        // premier noeud
        if (getParentList(nodeToCopy).contains(parentNode)) {
            addCopyOfLabel = true;
        }

        Map<String, OrganigrammeNode> oldAndNewIdTable = new HashMap<>();
        recursivCopyNode(coreSession, nodeToCopy, parentNode, null, oldAndNewIdTable, withUser, addCopyOfLabel);
    }

    @Override
    public void migrateNode(CoreSession coreSession, OrganigrammeNode nodeToCopy, OrganigrammeNode destinationNode) {
        boolean canMigrateNode = nodeToCopy != null && !nodeToCopy.getId().equals(destinationNode.getId());

        if (canMigrateNode) {
            if (!isMigrateAllowed(nodeToCopy, destinationNode)) {
                throw new NuxeoException(ResourceHelper.getString("error.organigramme.service.migrate.not.allowed"));
            }

            // mise à jour des directions lors d'un migration de ministère
            if (nodeToCopy instanceof EntiteNode) {
                // on récupère les ids des ministères
                String oldMinistereId = nodeToCopy.getId();
                String newMinistereId = destinationNode.getId();

                if (!oldMinistereId.equals(newMinistereId)) {
                    // récupération des noeuds directions du ministères
                    EntiteNode epgEntite = (EntiteNode) nodeToCopy;
                    List<UniteStructurelleNode> uniteStructurelleList = STServiceLocator
                        .getSTUsAndDirectionService()
                        .getDirectionListFromMinistere(epgEntite);
                    for (UniteStructurelleNode epgUniteStructurelleNode : uniteStructurelleList) {
                        updateNorDirection(epgUniteStructurelleNode, oldMinistereId, newMinistereId);
                    }
                }
            }
            migrateNodeChildrenToDestinationNode(nodeToCopy, destinationNode);
        }
    }

    /**
     * Met à jour les lettres NOR de la direction.
     *
     * @param coreSession
     * @param nodeOldMinistere
     * @param epgUniteStructurelleNode
     * @param destinationNodeMinistere @
     */
    protected void updateNorDirection(
        UniteStructurelleNode epgUniteStructurelleNode,
        String oldMinistereId,
        String newMinistereId
    ) {
        // récupère la lettre nor de la direction
        String lettreDirection = epgUniteStructurelleNode.getNorDirectionForMinistereId(oldMinistereId);

        // récupère les associations NOR - Ministere_id du noeud
        List<NorDirection> norOldDirectionList = epgUniteStructurelleNode.getNorDirectionList();

        // on créé les nouvelles associations NOR - Ministere_id du noeud en enlevant
        // l'id de l'ancien ministère et du
        // nouveau
        List<NorDirection> norNewDirectionList = new ArrayList<>();
        for (NorDirection norRef : norOldDirectionList) {
            if (!oldMinistereId.equals(norRef.getId()) && !newMinistereId.equals(norRef.getId())) {
                norNewDirectionList.add(norRef);
            }
        }

        norNewDirectionList.add(new NorDirection(newMinistereId, lettreDirection));

        epgUniteStructurelleNode.setNorDirectionList(norNewDirectionList);
        updateNode(epgUniteStructurelleNode, true);
    }

    /**
     * Check si la copie est possible dans le parent
     *
     * @param nodeToCopy
     * @param parentNode
     * @return
     */
    private boolean isCopyAllowed(OrganigrammeNode nodeToCopy, OrganigrammeNode parentNode) {
        boolean isCopyAllowedEntity = nodeToCopy instanceof EntiteNode && parentNode instanceof GouvernementNode;
        boolean isCopyAllowedUniteStructurelle =
            nodeToCopy instanceof UniteStructurelleNode &&
            (parentNode instanceof UniteStructurelleNode || parentNode instanceof EntiteNode);
        boolean isCopyAllowedPoste = nodeToCopy instanceof PosteNode && parentNode instanceof UniteStructurelleNode;
        return isCopyAllowedEntity || isCopyAllowedUniteStructurelle || isCopyAllowedPoste;
    }

    @Override
    public EntiteNode getMinistereFromNor(String nor) {
        List<EntiteNode> list = STServiceLocator.getSTMinisteresService().getCurrentMinisteres();
        EntiteNode result = null;
        for (EntiteNode entiteNode : list) {
            final String norMinistere = entiteNode.getNorMinistere();
            if (nor.equalsIgnoreCase(norMinistere)) {
                result = entiteNode;
                break;
            }
        }
        return result;
    }

    @Override
    public UniteStructurelleNode getDirectionFromMinistereAndNor(EntiteNode ministereNode, String nor) {
        List<UniteStructurelleNode> list = STServiceLocator
            .getSTUsAndDirectionService()
            .getDirectionListFromMinistere(ministereNode);
        String idMinistere = ministereNode.getId();
        UniteStructurelleNode result = null;
        for (UniteStructurelleNode uniteStructurelleNode : list) {
            if (nor.equalsIgnoreCase(uniteStructurelleNode.getNorDirectionForMinistereId(idMinistere))) {
                result = uniteStructurelleNode;
                break;
            }
        }
        return result;
    }

    /**
     * Copie l'élement nodeToCopy et les sous éléments de la branche récursivement
     *
     * @param coreSession
     * @param nodeToCopy
     * @param parentNode
     * @param oldAndNewIdTable
     * @param withUsers @
     */
    protected void recursivCopyNode(
        CoreSession coreSession,
        OrganigrammeNode nodeToCopy,
        OrganigrammeNode parentNode,
        String originalParentId,
        Map<String, OrganigrammeNode> oldAndNewIdTable,
        boolean withUsers,
        boolean addCopyOfLabel
    ) {
        OrganigrammeNode newNode = null;
        if (nodeToCopy instanceof GouvernementNode) {
            newNode = new GouvernementNodeImpl((GouvernementNode) nodeToCopy);
        } else if (nodeToCopy instanceof EntiteNode) {
            newNode = new EntiteNodeImpl((EntiteNode) nodeToCopy);
        } else if (nodeToCopy instanceof UniteStructurelleNode) {
            newNode = new UniteStructurelleNodeImpl((UniteStructurelleNode) nodeToCopy);
        } else if (nodeToCopy instanceof PosteNode) {
            newNode = new PosteNodeImpl((PosteNode) nodeToCopy);
        }

        List<OrganigrammeNode> parentList = new ArrayList<>();
        parentList.add(parentNode);
        newNode.setParentList(parentList);

        if (addCopyOfLabel) {
            newNode.setLabel("Copie de " + newNode.getLabel());
        }

        if (!withUsers && newNode instanceof PosteNode) {
            ((PosteNode) newNode).setMembers(null);
        }

        if (newNode instanceof EntiteNode || newNode instanceof UniteStructurelleNode) {
            if (newNode instanceof UniteStructurelleNode) {
                // Copie la lettre du nor
                if (originalParentId != null) {
                    String nor = ((UniteStructurelleNode) nodeToCopy).getNorDirectionForMinistereId(originalParentId);
                    ((UniteStructurelleNode) newNode).setNorDirectionForMinistereId(parentNode.getId(), nor);
                } else {
                    List<NorDirection> listNorNodeToCopy = ((UniteStructurelleNode) newNode).getNorDirectionList();
                    if (!listNorNodeToCopy.isEmpty()) {
                        // On prend arbitrairement la première
                        String lettreNor = listNorNodeToCopy.get(0).getNor();
                        if (lettreNor.length() > 1) {
                            lettreNor = lettreNor.substring(0, 1);
                        }
                        ((UniteStructurelleNode) newNode).setNorDirectionForMinistereId(parentNode.getId(), lettreNor);
                    }
                }
            }
            createNode(newNode);
        } else if (newNode instanceof PosteNode) {
            STServiceLocator.getSTPostesService().createPoste(coreSession, (PosteNode) newNode);
        }

        oldAndNewIdTable.put(nodeToCopy.getId(), newNode);

        List<OrganigrammeNode> nodeList = getChildrenList(null, nodeToCopy, Boolean.TRUE);

        for (OrganigrammeNode childNode : nodeList) {
            if (oldAndNewIdTable.get(childNode.getId()) != null) {
                OrganigrammeNode newChildNode = oldAndNewIdTable.get(childNode.getId());
                OrganigrammeNode newParentNode = oldAndNewIdTable.get(nodeToCopy.getId());
                // Add link
                parentList = getParentList(newChildNode);
                parentList.add(newParentNode);
                newChildNode.setParentList(parentList);
                updateNode(newChildNode, false);
            } else {
                recursivCopyNode(
                    coreSession,
                    childNode,
                    newNode,
                    nodeToCopy.getId(),
                    oldAndNewIdTable,
                    withUsers,
                    false
                );
            }
        }
    }

    /**
     * déplacement des postes et uniteStructurelle si les noeuds sont de type unités
     * structurelle.
     *
     * @param session
     * @param nodeToMove
     * @param destinationNode @
     */
    protected void moveUniteStructurelle(OrganigrammeNode nodeToMove, OrganigrammeNode destinationNode) {
        // déplacement des postes
        List<PosteNode> actualPoste = ((UniteStructurelleNode) destinationNode).getSubPostesList();
        if (actualPoste == null) {
            actualPoste = new ArrayList<>();
        }
        List<PosteNode> posteToAdd = ((UniteStructurelleNode) nodeToMove).getSubPostesList();
        if (posteToAdd != null && !posteToAdd.isEmpty()) {
            actualPoste.addAll(posteToAdd);
            // ajout des postes dans le nouveau noeud
            ((UniteStructurelleNode) destinationNode).setSubPostesList(actualPoste);
        }
        // suppression des postes dans l'ancien noeud
        ((UniteStructurelleNode) nodeToMove).setSubPostesList(null);

        // déplacement des unités structurelles
        List<UniteStructurelleNode> actualUniteStructurelle =
            ((UniteStructurelleNode) destinationNode).getSubUnitesStructurellesList();
        if (actualUniteStructurelle == null) {
            actualUniteStructurelle = new ArrayList<>();
        }
        List<UniteStructurelleNode> uniteStructurelleToAdd =
            ((UniteStructurelleNode) nodeToMove).getSubUnitesStructurellesList();
        if (uniteStructurelleToAdd != null && !uniteStructurelleToAdd.isEmpty()) {
            actualUniteStructurelle.addAll(uniteStructurelleToAdd);
            // ajout des unités structurelles dans le nouveau noeud
            ((UniteStructurelleNode) destinationNode).setSubUnitesStructurellesList(actualUniteStructurelle);
        }
        // enregistrement des nouvelles unites structurelles et postes
        updateNode(destinationNode, true);
        // suppression des unités structurelles dans l'ancien noeud
        ((UniteStructurelleNode) nodeToMove).setSubUnitesStructurellesList(null);
        updateNode(nodeToMove, true);
    }

    @Override
    public Set<OrganigrammeNodeDeletionProblem> validateDeleteNode(CoreSession coreSession, OrganigrammeNode node) {
        // Cherche les postes dans les enfants si des FDR
        // actives sont liées à ces postes ou si un des utilisateurs du poste
        // n'est associé à aucun autre

        OrganigrammeNode nodeUpToDate = getOrganigrammeNodeById(node.getId(), node.getType());
        List<OrganigrammeNode> nodeList = getChildrenList(coreSession, nodeUpToDate, Boolean.TRUE);
        Set<OrganigrammeNodeDeletionProblem> problems = new HashSet<>();

        if (node instanceof EntiteNode) {
            // Aucun dossier ne doit être rattaché à l'entité
            problems.addAll(validateDeleteDirectionMinistereDossiers(node, coreSession));

            // Aucun modèle de feuille de route ne doit être rattaché à l'entité
            problems.addAll(validateDeleteDirectionMinistereFdr(node, coreSession));

            // Aucun bulletin officiel ne doit être rattaché à l'entité
            problems.addAll(validateDeleteBulletinOfficiel(node, coreSession));

            // Aucune liste de mots clés ne doit être rattachée à l'entité
            problems.addAll(validateDeleteIndexationMotCle(node, coreSession));
            // RG_ADM_USR_016 : verifier les règles RG_ADM_USR_014 et RG_ADM_USR_015 pour
            // les postes fils

        } else if (node instanceof UniteStructurelleNode && node.getType() == OrganigrammeType.DIRECTION) {
            problems.addAll(validateDeleteDirectionMinistereDossiers(node, coreSession));

            // Aucun modèle de feuille de route ne doit être rattaché à la direction
            problems.addAll(validateDeleteDirectionMinistereFdr(node, coreSession));
            // RG_ADM_USR_016 : verifier les règles RG_ADM_USR_014 et RG_ADM_USR_015 pour
            // les postes fils

        } else if (node instanceof UniteStructurelleNode && node.getType() == OrganigrammeType.OTHER) {
            // RG_ADM_USR_016 : verifier les règles RG_ADM_USR_014 et RG_ADM_USR_015 pour
            // les postes fils

        } else if (node instanceof PosteNode) {
            problems.addAll(validateDeletePosteNode(coreSession, node));
        }

        for (OrganigrammeNode childNode : nodeList) {
            problems.addAll(validateDeleteNode(coreSession, childNode));
        }

        return problems;
    }

    @Override
    public PosteNode createDesactivatePoste(CoreSession coreSession, String id, String label) {
        PosteNode node = new PosteNodeImpl();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, -2);
        node.setDateDebut(cal.getTime());
        cal.set(Calendar.MONTH, -1);
        node.setDateFin(cal.getTime());
        node.setId(id);
        node.setLabel(label);
        STServiceLocator.getSTPostesService().createPoste(coreSession, node);

        return node;
    }

    @Override
    public UniteStructurelleNode createDesactivateUniteStructurelleModel(String id) {
        UniteStructurelleNode node = new UniteStructurelleNodeImpl();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, -1);
        node.setDateDebut(cal.getTime());
        node.setDateFin(cal.getTime());
        node.setId(id);

        STServiceLocator.getSTUsAndDirectionService().createUniteStructurelle(node);

        return node;
    }

    @Override
    public List<OrganigrammeNode> getAllDirectionList() {
        return apply(
            true,
            em -> {
                return getAllDirectionList(em);
            }
        );
    }

    @SuppressWarnings("unchecked")
    private List<OrganigrammeNode> getAllDirectionList(EntityManager manager) {
        List<OrganigrammeNode> directionList = new LinkedList<>();
        Query query = manager.createQuery(ALL_DIRECTION_QUERY);

        directionList.addAll(query.getResultList());

        return directionList;
    }

    @Override
    protected String getDossierIdentification(String dossierId, CoreSession session) {
        return session.getDocument(new IdRef(dossierId)).getAdapter(Dossier.class).getNumeroNor();
    }

    @Override
    protected String getFeuilleRouteIdentification(FeuilleRoute fdr, CoreSession session) {
        SolonEpgFeuilleRoute feuilleRoute = session
            .getDocument(new IdRef(fdr.getDocument().getId()))
            .getAdapter(SolonEpgFeuilleRoute.class);
        return feuilleRoute.getNumero() + " - " + feuilleRoute.getName();
    }

    @Override
    protected Collection<OrganigrammeNodeDeletionProblem> validateDeleteDirectionDossiers(
        UniteStructurelleNode node,
        CoreSession coreSession
    ) {
        return new ArrayList<>();
    }

    private Collection<OrganigrammeNodeDeletionProblem> validateDeleteDirectionMinistereDossiers(
        OrganigrammeNode node,
        CoreSession coreSession
    ) {
        boolean isEntite = node instanceof EntiteNode;
        OrganigrammeNodeDeletionProblem.ProblemType problemType = isEntite
            ? OrganigrammeNodeDeletionProblem.ProblemType.DOSSIER_ATTACHED_TO_MINISTERE
            : OrganigrammeNodeDeletionProblem.ProblemType.DOSSIER_ATTACHED_TO_DIRECTION;

        DossierService dossierService = SolonEpgServiceLocator.getDossierService();
        List<DocumentModel> listeDocumentAttacheEntite = dossierService.getDossierRattacheToMinistereOrDirection(
            coreSession,
            node.getId(),
            !isEntite
        );

        Collection<OrganigrammeNodeDeletionProblem> problems = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(listeDocumentAttacheEntite)) {
            problems =
                listeDocumentAttacheEntite
                    .stream()
                    .map(doc -> doc.getAdapter(Dossier.class))
                    .distinct()
                    .map(
                        dos ->
                            new OrganigrammeNodeDeletionProblem(
                                problemType,
                                node.getLabel(),
                                dos.getNumeroNor(),
                                getMinDirElemSuppression(dos.getMinistereAttache(), dos.getDirectionAttache())
                            )
                    )
                    .collect(Collectors.toList());
        }
        return problems;
    }

    private Collection<OrganigrammeNodeDeletionProblem> validateDeleteDirectionMinistereFdr(
        OrganigrammeNode node,
        CoreSession coreSession
    ) {
        boolean isEntite = node instanceof EntiteNode;
        OrganigrammeNodeDeletionProblem.ProblemType problemType = isEntite
            ? OrganigrammeNodeDeletionProblem.ProblemType.MODELE_FDR_ATTACHED_TO_MINISTERE
            : OrganigrammeNodeDeletionProblem.ProblemType.MODELE_FDR_ATTACHED_TO_DIRECTION;

        FeuilleRouteModelService feuilleRouteModelService = SolonEpgServiceLocator.getFeuilleRouteModelService();
        List<DocumentModel> listeDocumentAttacheEntite = feuilleRouteModelService.getFdrModelFromMinistereAndDirection(
            coreSession,
            isEntite ? node.getId() : null,
            !isEntite ? node.getId() : null,
            !isEntite
        );

        Collection<OrganigrammeNodeDeletionProblem> problems = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(listeDocumentAttacheEntite)) {
            problems =
                listeDocumentAttacheEntite
                    .stream()
                    .map(doc -> doc.getAdapter(SolonEpgFeuilleRoute.class))
                    .distinct()
                    .map(
                        modele ->
                            new OrganigrammeNodeDeletionProblem(
                                problemType,
                                node.getLabel(),
                                modele.getNumero() + " - " + modele.getTitle(),
                                getMinDirElemSuppression(modele.getMinistere(), modele.getDirection())
                            )
                    )
                    .collect(Collectors.toList());
        }
        return problems;
    }

    @Override
    protected Collection<OrganigrammeNodeDeletionProblem> validateDeletePosteNode(
        CoreSession coreSession,
        OrganigrammeNode node
    ) {
        Collection<OrganigrammeNodeDeletionProblem> problems = new ArrayList<>();

        // RG_ADM_USR_014
        //Modèle
        problems.addAll(validateDeletePosteNodeForModele(coreSession, node));

        //Instance
        problems.addAll(validateDeletePosteNodeForInstance(coreSession, node));

        //Squelette
        problems.addAll(validateDeletePosteNodeForSquelette(coreSession, node));

        // RG_ADM_USR_015
        boolean onePosteOnly = STServiceLocator.getSTPostesService().userHasOnePosteOnly((PosteNode) node);
        if (onePosteOnly) {
            problems.addAll(
                ((PosteNode) node).getUserList()
                    .stream()
                    .map(
                        user ->
                            new OrganigrammeNodeDeletionProblem(
                                OrganigrammeNodeDeletionProblem.ProblemType.USER_IS_LINKED_TO_POSTE,
                                node.getLabel(),
                                user.getFullName(),
                                null
                            )
                    )
                    .collect(Collectors.toList())
            );
        }

        return problems;
    }

    private static Collection<OrganigrammeNodeDeletionProblem> validateDeletePosteNodeForModele(
        CoreSession coreSession,
        OrganigrammeNode node
    ) {
        EPGFeuilleRouteService fdrService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
        Collection<DocumentModel> activeFdrModeleList = fdrService.getFeuilleRouteWithStepsInModeleFdrForPosteId(
            coreSession,
            node.getId()
        );
        if (CollectionUtils.isNotEmpty(activeFdrModeleList)) {
            return activeFdrModeleList
                .stream()
                .map(doc -> doc.getAdapter(SolonEpgFeuilleRoute.class))
                .filter(fdr -> !fdr.isSqueletteFeuilleRoute())
                .map(
                    modele ->
                        new OrganigrammeNodeDeletionProblem(
                            OrganigrammeNodeDeletionProblem.ProblemType.MODELE_FDR_ATTACHED_TO_POSTE,
                            node.getLabel(),
                            modele.getNumero() + " - " + modele.getTitle(),
                            getMinDirElemSuppression(modele.getMinistere(), modele.getDirection())
                        )
                )
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private static Collection<OrganigrammeNodeDeletionProblem> validateDeletePosteNodeForInstance(
        CoreSession coreSession,
        OrganigrammeNode node
    ) {
        EPGFeuilleRouteService fdrService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
        Collection<DocumentModel> activeFdrInstanceList = fdrService.getFeuilleRouteWithActiveOrFutureRouteStepsInInstanceForPosteId(
            coreSession,
            node.getId()
        );
        Collection<OrganigrammeNodeDeletionProblem> problems = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(activeFdrInstanceList)) {
            activeFdrInstanceList
                .stream()
                .map(doc -> doc.getAdapter(SolonEpgFeuilleRoute.class))
                .forEach(
                    fdr -> {
                        DocumentModelList docList = fdr.getAttachedDocuments(coreSession);
                        // si pas de document rattaché -> feuille de route non active (probablement suite à substitution)
                        if (CollectionUtils.isNotEmpty(docList)) {
                            String numeroNor = docList
                                .stream()
                                .map(doc -> doc.getAdapter(Dossier.class))
                                .map(Dossier::getNumeroNor)
                                .collect(Collectors.joining(","));

                            String minDirDoc = docList
                                .stream()
                                .map(doc -> doc.getAdapter(Dossier.class))
                                .map(
                                    dos ->
                                        getMinDirElemSuppression(dos.getMinistereAttache(), dos.getDirectionAttache())
                                )
                                .distinct()
                                .collect(Collectors.joining(","));

                            problems.add(
                                new OrganigrammeNodeDeletionProblem(
                                    OrganigrammeNodeDeletionProblem.ProblemType.INSTANCE_FDR_ATTACHED_TO_POSTE,
                                    node.getLabel(),
                                    numeroNor,
                                    minDirDoc
                                )
                            );
                        }
                    }
                );
        }

        return problems;
    }

    private static Collection<OrganigrammeNodeDeletionProblem> validateDeletePosteNodeForSquelette(
        CoreSession coreSession,
        OrganigrammeNode node
    ) {
        EPGFeuilleRouteService fdrService = SolonEpgServiceLocator.getEPGFeuilleRouteService();
        Collection<DocumentModel> activeFdrSqueletteList = fdrService.getRouteStepsInSqueletteFdrForPosteId(
            coreSession,
            node.getId()
        );
        if (CollectionUtils.isNotEmpty(activeFdrSqueletteList)) {
            return activeFdrSqueletteList
                .stream()
                .map(doc -> getFeuilleRouteDocFromRouteStepDoc(coreSession, doc))
                .distinct()
                .map(doc -> doc.getAdapter(SolonEpgFeuilleRoute.class))
                .map(
                    fdr ->
                        new OrganigrammeNodeDeletionProblem(
                            OrganigrammeNodeDeletionProblem.ProblemType.SQUELETTE_ATTACHED_TO_POSTE,
                            node.getLabel(),
                            fdr.getTitle(),
                            getMinDirElemSuppression(fdr.getMinistere(), fdr.getDirection())
                        )
                )
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    private static String getMinDirElemSuppression(String minId, String dirId) {
        return Stream
            .of(
                getOrganigrammeNodeLabelOrNull(getSTMinisteresService().getEntiteNode(minId)),
                getOrganigrammeNodeLabelOrNull(getSTUsAndDirectionService().getUniteStructurelleNode(dirId))
            )
            .filter(Objects::nonNull)
            .collect(Collectors.joining(", "));
    }

    private static String getOrganigrammeNodeLabelOrNull(OrganigrammeNode node) {
        return Optional.ofNullable(node).map(OrganigrammeNode::getLabel).orElse(null);
    }

    private static DocumentModel getFeuilleRouteDocFromRouteStepDoc(CoreSession coreSession, DocumentModel doc) {
        return doc.getAdapter(FeuilleRouteElement.class).getFeuilleRoute(coreSession).getDocument();
    }

    private Collection<OrganigrammeNodeDeletionProblem> validateDeleteBulletinOfficiel(
        OrganigrammeNode node,
        CoreSession coreSession
    ) {
        Collection<OrganigrammeNodeDeletionProblem> problems = new ArrayList<>();
        BulletinOfficielService bulletinOfficielService = SolonEpgServiceLocator.getBulletinOfficielService();
        List<DocumentModel> listeBulletinsOfficiel = bulletinOfficielService.getBulletinOfficielQueryFromNor(
            coreSession,
            ((EntiteNode) node).getNorMinistere()
        );
        if (CollectionUtils.isNotEmpty(listeBulletinsOfficiel)) {
            for (DocumentModel bulletin : listeBulletinsOfficiel) {
                OrganigrammeNodeDeletionProblem problem = new OrganigrammeNodeDeletionProblem(
                    OrganigrammeNodeDeletionProblem.ProblemType.BULLETIN_ATTACHED_TO_MINISTERE,
                    node.getLabel()
                );
                String intitule = bulletin.getAdapter(BulletinOfficiel.class).getIntitule();
                problem.setBlockingObjIdentification(intitule);
                problems.add(problem);
            }
        }
        return problems;
    }

    private Collection<OrganigrammeNodeDeletionProblem> validateDeleteIndexationMotCle(
        OrganigrammeNode node,
        CoreSession coreSession
    ) {
        IndexationEpgService indexationEpgService = SolonEpgServiceLocator.getIndexationEpgService();
        Collection<OrganigrammeNodeDeletionProblem> problems = new ArrayList<>();
        List<DocumentModel> listeDocumentAttacheEntite = indexationEpgService.getIndexationMotCleQueryFromIdMinistere(
            coreSession,
            node.getId()
        );
        if (CollectionUtils.isNotEmpty(listeDocumentAttacheEntite)) {
            for (DocumentModel docModel : listeDocumentAttacheEntite) {
                OrganigrammeNodeDeletionProblem problem = new OrganigrammeNodeDeletionProblem(
                    OrganigrammeNodeDeletionProblem.ProblemType.MOTSCLES_ATTACHED_TO_MINISTERE,
                    node.getLabel()
                );
                problem.setBlockingObjIdentification(getIndexationMotCleIdentification(docModel));
                problems.add(problem);
            }
        }

        return problems;
    }

    private String getIndexationMotCleIdentification(DocumentModel doc) {
        return doc.getAdapter(IndexationMotCle.class).getIntitule();
    }
}
