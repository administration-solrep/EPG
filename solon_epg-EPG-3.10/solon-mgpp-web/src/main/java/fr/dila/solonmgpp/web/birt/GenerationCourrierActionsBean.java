package fr.dila.solonmgpp.web.birt;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.transaction.TransactionHelper;

import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.solonmgpp.api.constant.SolonMgppCourrierConstant;
import fr.dila.solonmgpp.api.domain.FichePresentationDecret;
import fr.dila.solonmgpp.api.domain.ParametrageMgpp;
import fr.dila.solonmgpp.api.dto.IdentiteDTO;
import fr.dila.solonmgpp.api.dto.TableReferenceDTO;
import fr.dila.solonmgpp.api.enumeration.CourrierReportsEnum;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.ss.web.birt.BirtReportActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;

/**
 * bean de gestion des impressions courriers
 * 
 * @author asatre
 * 
 */
@Name("generationCourrierActions")
@Scope(ScopeType.CONVERSATION)
public class GenerationCourrierActionsBean implements Serializable {

    private static final long serialVersionUID = -1537687267215388166L;
    
    private static final STLogger LOGGER = STLogFactory.getLog(GenerationCourrierActionsBean.class);

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true, required = true)
    protected transient BirtReportActionsBean birtReportingActions;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    private static final String IDENTITE = "Identite";
    
    private static final List<String> listeCourriersPM = Arrays.asList(
    		CourrierReportsEnum.COURRIER_10_PA.getName(),
    		CourrierReportsEnum.COURRIER_10_PA_RECTIFICATIVE.getName(),
    		CourrierReportsEnum.COURRIER_19_CMP.getName(),
    		CourrierReportsEnum.COURRIER_19_CMP_COMMUN.getName(),
    		CourrierReportsEnum.COURRIER_23_BIS_ECHEC_CMP_COMMUN.getName(),
    		CourrierReportsEnum.COURRIER_23_BIS_REJET_AN.getName(),
    		CourrierReportsEnum.COURRIER_23_BIS_REJET_SENAT.getName(),
    		CourrierReportsEnum.COURRIER_23_BIS_REJET_SENAT_AMDTS.getName(),
    		CourrierReportsEnum.COURRIER_23_DERNIER_LECTURE_REJET.getName(),
    		CourrierReportsEnum.COURRIER_23_DERNIER_LECTURE.getName(),
    		CourrierReportsEnum.COURRIER_LETTRES_AMPLIATION_CONGRES.getName(),
    		CourrierReportsEnum.COURRIER_D_CLO_AMPLI.getName(),
    		CourrierReportsEnum.COURRIER_D_OUV_AMPLI.getName(),
    		CourrierReportsEnum.COURRIER_LETTRES_DECRET_CONGRES.getName(),
    		CourrierReportsEnum.COURRIER_L_CLO_AN_SE.getName(),
    		CourrierReportsEnum.COURRIER_L_OUV_AN_S.getName(),
    		CourrierReportsEnum.COURRIER_LETTRES_IE_EXTRA.getName(),
    		CourrierReportsEnum.COURRIER_LETTRES_IE.getName(),
    		CourrierReportsEnum.COURRIER_AUDITION.getName(),
    		CourrierReportsEnum.COURRIER_APPLICATION_ARTICLE_28.getName(),
    		CourrierReportsEnum.COURRIER_DECLARATION_POLITIQUE_GENERALE.getName(),
    		CourrierReportsEnum.COURRIER_DECLARATION_POLITIQUE_GENERALE_APPROBATION.getName(),
    		CourrierReportsEnum.COURRIER_DECLARATION_ARTICLE_50_1.getName());

    public String genererCourier(String currentCourrier, DocumentModel doc, String table) throws Exception {
        return genererCourier(currentCourrier, doc, null, table);
    }

    public String genererCourier(String currentCourrier, DocumentModel doc, FichePresentationDecret decret, String table) throws Exception {
        if (StringUtils.isBlank(currentCourrier)) {
            //facesMessages.add(StatusMessage.Severity.WARN, "Le type de courier est obligatoire.");
            TransactionHelper.setTransactionRollbackOnly();
            return null;
        } else {
            HashMap<String, String> inputValues = new HashMap<String, String>();
            String ficheId = doc.getId();
            inputValues.put("FICHEID_PARAM", ficheId);
            try {

                if(this.supportAuteurLex01Param(currentCourrier,table)){
                    String auteurLex01 = this.findAuteurCommunicationLex01();
                    inputValues.put("AUTEUR_LEX01", auteurLex01);
                }
                
                if (CourrierReportsEnum.COURRIER_APPLICATION_ARTICLE_28.getTable().equals(table) && CourrierReportsEnum.COURRIER_APPLICATION_ARTICLE_28.getId().equals(currentCourrier)) {
                	inputValues.put("IS_MINISTRE", isMinistreOrSecretaireEtat().toString());
                }
                
                if (table.equals(CourrierReportsEnum.COURRIER_CHOIX_MULTIPLES_DECES.getTable()) && currentCourrier.equals(CourrierReportsEnum.COURRIER_CHOIX_MULTIPLES_DECES.getId())) {
                    inputValues.put("RAISON_PARAM", SolonMgppCourrierConstant.COURRIER_CHOIX_MULTIPLES_DECES_PARAM);
                } else if (table.equals(CourrierReportsEnum.COURRIER_CHOIX_MULTIPLES_DEMISSION.getTable()) && currentCourrier.equals(CourrierReportsEnum.COURRIER_CHOIX_MULTIPLES_DEMISSION.getId())) {
                    inputValues.put("RAISON_PARAM", SolonMgppCourrierConstant.COURRIER_CHOIX_MULTIPLES_DEMISSION_PARAM);
                } else if (table.equals(CourrierReportsEnum.COURRIER_CHOIX_MULTIPLES_EXPIRATION.getTable()) &&  currentCourrier.equals(CourrierReportsEnum.COURRIER_CHOIX_MULTIPLES_EXPIRATION.getId())) {
                    inputValues.put("RAISON_PARAM", SolonMgppCourrierConstant.COURRIER_CHOIX_MULTIPLES_EXPIRATION_PARAM);

                } else if (table.equals(CourrierReportsEnum.COURRIER_L_OUV_AN_S.getTable()) && currentCourrier.equals(CourrierReportsEnum.COURRIER_L_OUV_AN_S.getId())) {
                    SolonMgppServiceLocator.getEvenementService().createEvenementEppEvt29Brouillon(documentManager, decret);
                } else if (table.equals(CourrierReportsEnum.COURRIER_L_CLO_AN_SE.getTable()) && currentCourrier.equals(CourrierReportsEnum.COURRIER_L_CLO_AN_SE.getId())) {
                    SolonMgppServiceLocator.getEvenementService().createEvenementEppEvt30Brouillon(documentManager, decret);

                } else if (table.equals(CourrierReportsEnum.COURRIER_LETTRES_DECRETS.getTable()) && currentCourrier.equals(CourrierReportsEnum.COURRIER_LETTRES_DECRETS.getId())) {
                    SolonMgppServiceLocator.getEvenementService().createEvenementEppEvt31Brouillon(documentManager, decret);
                } else if (table.equals(CourrierReportsEnum.COURRIER_LETTRES_DECRET_CONGRES_ARTICLE18C.getTable()) && currentCourrier.equals(CourrierReportsEnum.COURRIER_LETTRES_DECRET_CONGRES_ARTICLE18C.getId())) {
                    SolonMgppServiceLocator.getEvenementService().createEvenementEppEvt35Brouillon(documentManager, decret);
                } else if (table.equals(CourrierReportsEnum.COURRIER_LETTRE_DR_APPLICTION.getTable()) && currentCourrier.equals(CourrierReportsEnum.COURRIER_LETTRE_DR_APPLICTION.getId())) {
                    String ministeresParam = SolonEpgServiceLocator.getStatsGenerationResultatService().getMinisteresCourantListBirtReportParam();
                    inputValues.put("MINISTERES_PARAM", ministeresParam);
                }
            } catch (ClientException e) {
            	LOGGER.error(documentManager, MgppLogEnumImpl.FAIL_GENERATE_COURRIER_FONC, currentCourrier);
                facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            }
            birtReportingActions.generateWord(CourrierReportsEnum.getFileByIdAndTable(currentCourrier,table), inputValues, CourrierReportsEnum.getNameByIdAndTable(currentCourrier,table));
            return null;
        }
    }

    private boolean supportAuteurLex01Param(String currentCourier, String table) {
        return listeCourriersPM.contains(CourrierReportsEnum.getNameByIdAndTable(currentCourier, table));
    }

    private String findAuteurCommunicationLex01() throws ClientException {
        final ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator.getParametreMgppService().findParametrageMgpp(documentManager);
        String auteurLex01 = parametrageMgpp.getAuteurLex01();
		if (auteurLex01 != null) {
			return getTitleFromTableReference(auteurLex01);
		}
        return "";
    }

    private Boolean isMinistreOrSecretaireEtat() throws ClientException {
        final ParametrageMgpp parametrageMgpp = SolonMgppServiceLocator.getParametreMgppService().findParametrageMgpp(documentManager);
        Boolean isMinistre = parametrageMgpp.isMinistre();
        if (isMinistre != null) {
            return isMinistre;
        }
        return false;
    }

    private String getTitleFromTableReference(String identifiant) {

        TableReferenceDTO tableReferenceDTO = null;
        try {
            tableReferenceDTO = SolonMgppServiceLocator.getTableReferenceService().findTableReferenceByIdAndType(identifiant, IDENTITE, null, documentManager, true);
        } catch (ClientException e) {
            facesMessages.add(StatusMessage.Severity.WARN, e.getMessage());
            TransactionHelper.setTransactionRollbackOnly();
        }
		if (tableReferenceDTO != null) {
			if (tableReferenceDTO instanceof IdentiteDTO) {
				String nom = tableReferenceDTO.getNom();
				return ((IdentiteDTO) tableReferenceDTO).getPrenom() + " " + (nom == null ? "" : nom.toUpperCase());
			}
			return tableReferenceDTO.getTitle();
		} else {
			return "**inconnu** " + identifiant;
		}
    }


}
