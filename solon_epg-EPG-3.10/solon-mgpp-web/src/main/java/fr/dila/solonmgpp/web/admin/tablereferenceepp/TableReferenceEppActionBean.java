package fr.dila.solonmgpp.web.admin.tablereferenceepp;

import java.io.Serializable;
import java.util.Date;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonmgpp.api.constant.SolonMgppViewConstant;
import fr.dila.solonmgpp.api.node.GouvernementNode;
import fr.dila.solonmgpp.api.node.IdentiteNode;
import fr.dila.solonmgpp.api.node.MandatNode;
import fr.dila.solonmgpp.api.node.MinistereNode;
import fr.dila.solonmgpp.api.service.TableReferenceService;
import fr.dila.solonmgpp.core.node.ActeurNodeImpl;
import fr.dila.solonmgpp.core.node.GouvernementNodeImpl;
import fr.dila.solonmgpp.core.node.IdentiteNodeImpl;
import fr.dila.solonmgpp.core.node.MandatNodeImpl;
import fr.dila.solonmgpp.core.node.MinistereNodeImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.st.api.constant.STBaseFunctionConstant;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.security.principal.STPrincipal;
import fr.dila.st.core.factory.STLogFactory;

@Name("tableReferenceEppAction")
@Scope(ScopeType.CONVERSATION)
public class TableReferenceEppActionBean implements Serializable {
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -2730746116432347819L;

	/**
	 * Logger surcouche socle de log4j
	 */
	private static final STLogger LOGGER = STLogFactory.getLog(TableReferenceEppActionBean.class);

	@In(create = true, required = false)
	protected CoreSession documentManager;

	@In(create = true, required = false)
	protected transient FacesMessages facesMessages;

	@In(create = true, required = false)
	protected transient TableReferenceEppTreeBean tableReferenceEppTree;

	private String selectedNodeId;
	private String selectedNodeType;
	private GouvernementNode gouvernementNode;
	private MinistereNode ministereNode;
	private MandatNode mandatNode;
	private IdentiteNode identiteNode;
	private String mode;

	public String ajouterGouvernementView() {
		selectedNodeId = null;
		gouvernementNode = new GouvernementNodeImpl();
		gouvernementNode.setDateDebut(new Date());
		return SolonMgppViewConstant.TABLE_REFERENCE_GOUVERNEMENT_EPP;
	}

	public String modifierGouvernementView() {
		try {
			gouvernementNode = SolonMgppServiceLocator.getTableReferenceService().getGouvernement(selectedNodeId, documentManager);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_GOUVERNEMENT_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return SolonMgppViewConstant.TABLE_REFERENCE_GOUVERNEMENT_EPP;
	}

	public String ajouterMinistereView() {
		ministereNode = new MinistereNodeImpl();
		ministereNode.setGouvernement(selectedNodeId);
		ministereNode.setDateDebut(new Date());
		mode = "add";
		return SolonMgppViewConstant.TABLE_REFERENCE_MINISTERE_EPP;
	}

	public String modifierMinistereView() {
		mode = "edit";
		try {
			ministereNode = SolonMgppServiceLocator.getTableReferenceService().getMinistere(selectedNodeId, documentManager);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_MINISTERE_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return SolonMgppViewConstant.TABLE_REFERENCE_MINISTERE_EPP;
	}

	public String ajouterMandatView() {
		mode = "add";
		mandatNode = new MandatNodeImpl();
		mandatNode.setMinistere(selectedNodeId);
		mandatNode.setDateDebut(new Date());
		IdentiteNode identite = new IdentiteNodeImpl();
		identite.setDateDebut(new Date());
		identite.setActeur(new ActeurNodeImpl());
		mandatNode.setIdentite(identite);
		return SolonMgppViewConstant.TABLE_REFERENCE_MANDAT_EPP;
	}

	public String modifierMandatView() {
		mode = "edit";
		try {
			mandatNode = SolonMgppServiceLocator.getTableReferenceService().getMandat(selectedNodeId, documentManager);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_MANDAT_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return SolonMgppViewConstant.TABLE_REFERENCE_MANDAT_EPP;
	}

	public String updateGouvernement() {
		try {
			TableReferenceService tableReferenceService = SolonMgppServiceLocator.getTableReferenceService();
			if (selectedNodeId != null) {
				tableReferenceService.updateGouvernement(gouvernementNode, documentManager);
			} else {
				tableReferenceService.createGouvernement(gouvernementNode, documentManager);
			}
		} catch (ClientException e) {
			if (selectedNodeId != null) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_UPDATE_GOUVERNEMENT_TEC, e);
			} else {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_CREATE_GOUVERNEMENT_TEC, e);
			}
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return returnToTree();
	}

	public String updateMinistere() {
		try {
			TableReferenceService tableReferenceService = SolonMgppServiceLocator.getTableReferenceService();
			if ("edit".equals(mode)) {
				tableReferenceService.updateMinistere(ministereNode, documentManager);
			} else {
				tableReferenceService.createMinistere(ministereNode, documentManager);
			}
		} catch (ClientException e) {
			if ("edit".equals(mode)) {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_UPDATE_MINISTERE_TEC, e);
			} else {
				LOGGER.error(documentManager, STLogEnumImpl.FAIL_CREATE_MINISTERE_TEC, e);
			}

			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return returnToTree();
	}

	public String createMandat() {
		try {
			SolonMgppServiceLocator.getTableReferenceService().createMandat(mandatNode, documentManager);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_CREATE_MANDAT_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return returnToTree();
	}

	public String updateMandat() {
		try {
			SolonMgppServiceLocator.getTableReferenceService().updateMandat(mandatNode, documentManager);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_UPDATE_MANDAT_TEC,	e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return SolonMgppViewConstant.TABLE_REFERENCE_MANDAT_EPP;
	}

	public void annulerLaCreation() {

	}

	public String createIdentite() {
		identiteNode.setActeur(mandatNode.getIdentite().getActeur());
		mandatNode.setIdentite(identiteNode);
		try {
			SolonMgppServiceLocator.getTableReferenceService().createIdentite(mandatNode, documentManager);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_CREATE_IDENTITE_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		identiteNode = null;
		return SolonMgppViewConstant.TABLE_REFERENCE_MANDAT_EPP;
	}

	public String updateIdentite() {
		try {
			SolonMgppServiceLocator.getTableReferenceService().updateIdentite(
					mandatNode, documentManager);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_UPDATE_IDENTITE_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return SolonMgppViewConstant.TABLE_REFERENCE_MANDAT_EPP;
	}

	public String createActeur() {
		try {
			SolonMgppServiceLocator.getTableReferenceService().createActeur(mandatNode, documentManager);
		} catch (ClientException e) {
			LOGGER.error(documentManager, STLogEnumImpl.FAIL_CREATE_ACTEUR_TEC, e);
			facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
		}
		return SolonMgppViewConstant.TABLE_REFERENCE_MANDAT_EPP;
	}

	public String cancelUpdate() {
		return returnToTree();
	}

	public String returnToTree() {
		tableReferenceEppTree.reloadTree();
		return SolonMgppViewConstant.TABLE_REFERENCE_EPP;
	}

	public boolean inEditMode() {
		return "edit".equals(this.mode);
	}

	public String ajouterMandatSansMinistrView() {
		mode = "add";
		mandatNode = new MandatNodeImpl();
		mandatNode.setDateDebut(new Date());
		IdentiteNode identite = new IdentiteNodeImpl();
		identite.setDateDebut(new Date());
		identite.setActeur(new ActeurNodeImpl());
		mandatNode.setIdentite(identite);
		return SolonMgppViewConstant.TABLE_REFERENCE_MANDAT_EPP;
	}

	/**
	 * Controle l'accès à la vue correspondante
	 * 
	 */
	public boolean isAccessAuthorized() {
		STPrincipal stPrincipal = (STPrincipal) documentManager.getPrincipal();
		return stPrincipal.isAdministrator() || stPrincipal.isMemberOf(STBaseFunctionConstant.ADMINISTRATION_PARAMETRE_APPLICATION_READER);
	}

	public String getSelectedNodeId() {
		return selectedNodeId;
	}

	public void setSelectedNodeId(String selectedNodeId) {
		this.selectedNodeId = selectedNodeId;
	}

	public String getSelectedNodeType() {
		return selectedNodeType;
	}

	public void setSelectedNodeType(String selectedNodeType) {
		this.selectedNodeType = selectedNodeType;
	}

	public GouvernementNode getGouvernementNode() {
		return gouvernementNode;
	}

	public void setGouvernementNode(GouvernementNode gouvernementNode) {
		this.gouvernementNode = gouvernementNode;
	}

	public MinistereNode getMinistereNode() {
		return ministereNode;
	}

	public void setMinistereNode(MinistereNode ministereNode) {
		this.ministereNode = ministereNode;
	}

	public MandatNode getMandatNode() {
		return mandatNode;
	}

	public void setMandatNode(MandatNode mandatNode) {
		this.mandatNode = mandatNode;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public IdentiteNode getIdentiteNode() {
		if (identiteNode == null) {
			identiteNode = new IdentiteNodeImpl();
			identiteNode.setDateDebut(new Date());
		}
		return identiteNode;
	}

	public void setIdentiteNode(IdentiteNode identiteNode) {
		this.identiteNode = identiteNode;
	}

}
