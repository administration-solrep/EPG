package fr.dila.solonmgpp.web.metadonnees;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.platform.mimetype.interfaces.MimetypeEntry;
import org.nuxeo.ecm.platform.mimetype.interfaces.MimetypeRegistry;
import org.nuxeo.ecm.platform.ui.web.tag.fn.DocumentModelFunctions;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonmgpp.api.constant.VocabularyConstants;
import fr.dila.solonmgpp.api.dto.EvenementDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeDTO;
import fr.dila.solonmgpp.api.dto.PieceJointeFichierDTO;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.solonmgpp.web.context.NavigationContextBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;
import fr.dila.st.core.service.VocabularyServiceImpl;

/**
 * bean de gestion des corbeilles
 * 
 * @author asatre
 * 
 */
@Name("pieceJointeActions")
@Scope(ScopeType.CONVERSATION)
public class PieceJointeActionsBean implements Serializable {

    private static final long serialVersionUID = 6117576332902478866L;

    private static final String PIECE_JOINTE_ERREUR_RECUPERATION = "piece.jointe.erreur.recuperation";
    
    /**
     * Logger surcouche socle de log4j
     */
    private static final STLogger LOGGER = STLogFactory.getLog(PieceJointeActionsBean.class);    
    
    private static MimetypeRegistry mimetypeRegistry;

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true, required = false)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    private String indexFichier;

    private String indexPJ;

    private String typePJ;

    private static MimetypeRegistry getMimetypeRegistry() {
        if (mimetypeRegistry == null) {
            try {
                mimetypeRegistry = Framework.getService(MimetypeRegistry.class);
            } catch (Exception e) {
                //log.error("Unable to get mimetype service : " + e.getMessage());
            }
        }
        return mimetypeRegistry;
    }

    public Set<String> getListTypePieceJointe() {
        return getListTypePieceJointe(navigationContext.getCurrentEvenement());
    }

    public Set<String> getListTypePieceJointe(EvenementDTO evenementDTO) {
        if (evenementDTO != null) {

            try {
                return SolonMgppServiceLocator.getEvenementTypeService().getEvenementType(evenementDTO.getTypeEvenementName()).getPieceJointe()
                        .keySet();
            } catch (ClientException e) {
                String message = resourcesAccessor.getMessages().get(PIECE_JOINTE_ERREUR_RECUPERATION);
                LOGGER.error(documentManager, STLogEnumImpl.FAIL_GET_EVENT_TYPE_TEC,e) ;                
                facesMessages.add(StatusMessage.Severity.WARN, message);
                TransactionHelper.setTransactionRollbackOnly();
            }
        }
        return null;
    }

    /**
     * from {@link DocumentModelFunctions}
     * 
     * @param mimeType
     * @return
     */
    public static String fileIconPath(String mimeType) {
		String iconPath = "";
		if (mimeType != null && StringUtils.isNotBlank(mimeType)) {
			try {
				MimetypeRegistry mimeTypeService = getMimetypeRegistry();
				if (mimeTypeService != null) {
					MimetypeEntry mimeEntry = mimeTypeService.getMimetypeEntryByMimeType(mimeType);
					if (mimeEntry != null && mimeEntry.getIconPath() != null) {
						iconPath = "/icons/" + mimeEntry.getIconPath();
					}
				}
			} catch (Exception e) {
				// log.warn(e);
			}
		}
		return iconPath;
    }

    public String getFile() {
        EvenementDTO evenementDTO = navigationContext.getCurrentEvenement();
        return getFile(evenementDTO);
    }

    public String getFile(EvenementDTO evenementDTO) {

        if (evenementDTO == null || indexFichier == null || indexPJ == null || typePJ == null) {
            return null;
        }
        PieceJointeDTO pieceJointeDTO = evenementDTO.getListPieceJointe(typePJ).get(Integer.parseInt(indexPJ));
        

        if (pieceJointeDTO == null) {
            return null;
        }

        PieceJointeFichierDTO pieceJointeFichierDTO = pieceJointeDTO.getFichier().get(Integer.parseInt(indexFichier));

        if (pieceJointeFichierDTO == null) {
            return null;
        }

        
        String nomfichier = pieceJointeFichierDTO.getNomFichier();

        if (pieceJointeFichierDTO.getContenu() == null) {
            try {
                // recuperation du contenu dans fichier dans EPP
                SolonMgppServiceLocator.getPieceJointeService().setContentFromEpp(pieceJointeFichierDTO, evenementDTO, pieceJointeDTO,
                        documentManager);
            } catch (ClientException e) {
                LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_PIECE_JOINTE_TEC,e) ;
                facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
                TransactionHelper.setTransactionRollbackOnly();
            }
        }

        if (pieceJointeFichierDTO.getContenu() == null) {
            facesMessages.add(StatusMessage.Severity.WARN, "Le fichier n'a pas été trouvé dans SOLON EPP.");
            TransactionHelper.setTransactionRollbackOnly();
        } else {
            try {
                HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                response.reset();
                response.setHeader("Content-disposition", "attachment;filename=" + nomfichier);
                response.setContentLength(pieceJointeFichierDTO.getContenu().length);
                response.getOutputStream().write(pieceJointeFichierDTO.getContenu());
                response.setContentType(pieceJointeFichierDTO.getMimeType());
                response.getOutputStream().flush();
                response.getOutputStream().close();
                FacesContext.getCurrentInstance().responseComplete();
            } catch (Exception e) {
                LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GET_PIECE_JOINTE_TEC,e) ;
                facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
                TransactionHelper.setTransactionRollbackOnly();
            }
        }

        return null;
    }

    public String getPieceJointeType(String pieceJointeType) {
        String label = STServiceLocator.getVocabularyService().getEntryLabel(VocabularyConstants.VOCABULARY_PIECE_JOINTE_DIRECTORY, pieceJointeType);
        if (VocabularyServiceImpl.UNKNOWN_ENTRY.equals(label)) {
            return pieceJointeType;
        } else {
            return label;
        }
    }

    public List<PieceJointeDTO> getListPieceJointeFichier(String typePieceJointe) {
        return navigationContext.getCurrentEvenement().getListPieceJointe(typePieceJointe);
    }

    public String getIndexFichier() {
        return indexFichier;
    }

    public void setIndexFichier(String indexFichier) {
        this.indexFichier = indexFichier;
    }

    public String getIndexPJ() {
        return indexPJ;
    }

    public void setIndexPJ(String indexPJ) {
        this.indexPJ = indexPJ;
    }

    public String getTypePJ() {
        return typePJ;
    }

    public void setTypePJ(String typePJ) {
        this.typePJ = typePJ;
    }

}
