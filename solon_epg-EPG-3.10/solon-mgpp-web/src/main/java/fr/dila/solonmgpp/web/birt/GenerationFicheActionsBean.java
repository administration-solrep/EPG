package fr.dila.solonmgpp.web.birt;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import fr.dila.solonmgpp.api.constant.SolonMgppBaseFunctionConstant;
import fr.dila.solonmgpp.api.domain.FicheLoi;
import fr.dila.solonmgpp.api.domain.FichePresentation341;
import fr.dila.solonmgpp.api.domain.FichePresentationDOC;
import fr.dila.solonmgpp.api.domain.FichePresentationDR;
import fr.dila.solonmgpp.api.domain.FichePresentationIE;
import fr.dila.solonmgpp.api.domain.FichePresentationOEP;
import fr.dila.solonmgpp.api.domain.FichePresentationSD;
import fr.dila.solonmgpp.api.domain.RepresentantOEP;
import fr.dila.solonmgpp.api.dto.TableReferenceDTO;
import fr.dila.solonmgpp.api.enumeration.FicheReportsEnum;
import fr.dila.solonmgpp.api.logging.enumerationCodes.MgppLogEnumImpl;
import fr.dila.solonmgpp.api.node.IdentiteNode;
import fr.dila.solonmgpp.api.node.MandatNode;
import fr.dila.solonmgpp.api.service.DossierService;
import fr.dila.solonmgpp.api.service.TableReferenceService;
import fr.dila.solonmgpp.core.service.SolonMgppServiceLocator;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.ss.web.birt.BirtReportActionsBean;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.api.logging.enumerationCodes.STLogEnumImpl;
import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.service.STServiceLocator;

/**
 * bean de gestion des impressions courriers
 * 
 * @author
 * 
 */
@Name("generationFicheActions")
@Scope(ScopeType.CONVERSATION)
public class GenerationFicheActionsBean implements Serializable {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 7068910137038916043L;

	/**
	 * ST Logger
	 */
  private static final STLogger LOG = STLogFactory.getLog(GenerationFicheActionsBean.class);

  @In(create = true, required = false)
  protected transient FacesMessages facesMessages;

  @In(create = true, required = false)
  protected transient BirtReportActionsBean birtReportingActions;

  @In(create = true, required = false)
  protected transient CoreSession documentManager;

  @In(required = true, create = false)
  protected SSPrincipal ssPrincipal;

    public String genererFicheXLS(String currentFiche, DocumentModel doc) throws Exception {
        if (StringUtils.isBlank(currentFiche)) {
            facesMessages.add(StatusMessage.Severity.WARN, "Le type de fiche est obligatoire.");
            TransactionHelper.setTransactionRollbackOnly();
        } else {
            Map<String, String> inputValues = new HashMap<String, String>();
            putValues(currentFiche, doc, inputValues);
            birtReportingActions.generateXls(FicheReportsEnum.getFileById(currentFiche), inputValues, FicheReportsEnum.getNameById(currentFiche));
        }
        return null;
    }

    public String genererFichePDF(String currentFiche, DocumentModel doc) throws Exception {
        if (StringUtils.isBlank(currentFiche)) {
            facesMessages.add(StatusMessage.Severity.WARN, "Le type de fiche est obligatoire.");
            TransactionHelper.setTransactionRollbackOnly();
        } else {
            Map<String, String> inputValues = new HashMap<String, String>();
            putValues(currentFiche, doc, inputValues);
            birtReportingActions.generatePdf(FicheReportsEnum.getFileById(currentFiche), inputValues, FicheReportsEnum.getNameById(currentFiche));
        }
        return null;
    }

    /**
     * Renseigne les données pour le rapport (id du documentModel à utiliser, et auteurs, représentants suivant les cas)
     * 
     * @param currentFiche id de la fiche à générer
     * @param doc documentModel de la fiche
     * @param inputValues Map qui contiendra les valeurs pour le rapport
     * @throws ClientException 
     */
    private void putValues(String currentFiche, DocumentModel doc, Map<String, String> inputValues) throws ClientException {
        String ficheId = doc.getId();
        inputValues.put("FICHEID_PARAM", ficheId);
        // On ajoute les données liées aux mandats (auteurs, représentants...)
        if (currentFiche.equals(FicheReportsEnum.FICHE_IE.getId())) {
            FichePresentationIE fiche = doc.getAdapter(FichePresentationIE.class);
            String mandatAuteur = fiche.getAuteur();
            if (mandatAuteur != null && !mandatAuteur.isEmpty()) {
                inputValues.put("AUTEUR_PARAM", getAuteurFromMandat(mandatAuteur));
            }
        } else if (currentFiche.equals(FicheReportsEnum.FICHE_LOI.getId())) {
            FicheLoi fiche = doc.getAdapter(FicheLoi.class);
            String nomMinistere;
            String idMin = "";
            try {
                idMin = fiche.getMinistereResp();
                OrganigrammeNode entite = STServiceLocator.getSTMinisteresService().getEntiteNode(idMin);
                if (entite == null) {
                    nomMinistere = "";
                } else {
                    nomMinistere = entite.getLabel();
                }
            } catch (ClientException e) {
                nomMinistere = "** Ministère inconnu **";
                LOG.error(documentManager, STLogEnumImpl.FAIL_GET_ENTITE_NODE_TEC, "node id : " + idMin);
            }
            inputValues.put("MINISTERE_PARAM", nomMinistere);
        } else if (currentFiche.equals(FicheReportsEnum.FICHE_OEP.getId())) {
            FichePresentationOEP oep = doc.getAdapter(FichePresentationOEP.class);
            StringBuilder representantsAN = new StringBuilder();
            StringBuilder representantsSE = new StringBuilder();
            // Récupération des représentants AN et SENAT
            try {
                final DossierService dossierService = SolonMgppServiceLocator.getDossierService();
                final List<DocumentModel> docsAN = dossierService.fetchRepresentantOEP(documentManager, "AN", ficheId);
                final List<DocumentModel> docsSE = dossierService.fetchRepresentantOEP(documentManager, "SE", ficheId);
                putRepresentants(docsAN, representantsAN);
                putRepresentants(docsSE, representantsSE);
            } catch (ClientException e) {
                LOG.error(documentManager, MgppLogEnumImpl.FAIL_GET_REPRESENTANT_OEP_TEC, e);
            }
            if (representantsAN.length() != 0) {
                inputValues.put("REP_AN_PARAM", representantsAN.toString());
            }

            if (representantsSE.length() != 0) {
                inputValues.put("REP_SE_PARAM", representantsSE.toString());
            }
            inputValues.put("VIEW_UNRESTRICTED_PARAM", Boolean.toString(ssPrincipal.isMemberOf(SolonMgppBaseFunctionConstant.DROIT_VUE_MGPP)));
            List<String> chargesMission = oep.getChargesMission();
            if (chargesMission.size() > 0) {
                inputValues.put("CHARGES_MISSION_PARAM", StringUtils.join(chargesMission, "/"));
            }

        } else if (currentFiche.equals(FicheReportsEnum.FICHE_RESOLUTION.getId())) {
            FichePresentation341 fiche = doc.getAdapter(FichePresentation341.class);
            String mandatAuteur = fiche.getAuteur();
            List<String> mandatCoauteurList = fiche.getCoAuteur();
            if (mandatAuteur != null && !mandatAuteur.isEmpty()) {
                inputValues.put("AUTEUR_PARAM", getAuteurFromMandat(mandatAuteur));
            }
            if (!mandatCoauteurList.isEmpty()) {
            	inputValues.put("COAUTEUR_PARAM", getCoauteurFromMandat(mandatCoauteurList));
            }
        } else if (currentFiche.equals(FicheReportsEnum.FICHE_RAPPORT.getId())) {
            FichePresentationDR fiche = doc.getAdapter(FichePresentationDR.class);
            String ministeres = fiche.getMinisteres();
            if (ministeres != null && !ministeres.isEmpty()) {
                String label = ministeres;
                OrganigrammeNode node;
                try {
                    node = STServiceLocator.getSTMinisteresService().getEntiteNode(ministeres);
                    if (node != null) {
                        label = ((EntiteNode) node).getLabel();
                    }
                } catch (ClientException e) {
                	LOG.error(documentManager, STLogEnumImpl.FAIL_GET_ENTITE_NODE_TEC, "Impossible de retrouver le ministère demandé.", e);
                }
                inputValues.put("MINISTERE_LABEL", label);
            }
        }else if (currentFiche.equals(FicheReportsEnum.FICHE_SD.getId())){
        	FichePresentationSD fiche = doc.getAdapter(FichePresentationSD.class);
        	List<String> grpsParlementaires = fiche.getGroupeParlementaire();
        	StringBuilder result = new StringBuilder();
        	if(grpsParlementaires != null && !grpsParlementaires.isEmpty()){
        		final TableReferenceService trService = SolonMgppServiceLocator.getTableReferenceService();
        		for (String grpParlementaire : grpsParlementaires) {
                    TableReferenceDTO nomGrpParlementaire = trService.findTableReferenceByIdAndType(grpParlementaire, "Organisme",null, documentManager);
        			if(nomGrpParlementaire != null){
						result.append(nomGrpParlementaire.getNom());
						result.append(";");
					}
				}
        	}else{
        		result.append("");
        	}
        	inputValues.put("GR_PARLEMENTAIRE", result.toString());
        }
        else if (currentFiche.equals(FicheReportsEnum.FICHE_DOC.getId())){
        	FichePresentationDOC fiche = doc.getAdapter(FichePresentationDOC.class);
        	List<String> commissions = fiche.getCommissions();
        	StringBuilder result = new StringBuilder();
        	if(commissions != null && !commissions.isEmpty()){
        		final TableReferenceService trService = SolonMgppServiceLocator.getTableReferenceService();
        		for (String commission : commissions) {
                    TableReferenceDTO organisme = trService.findTableReferenceByIdAndType(commission, "Organisme",null, documentManager);
        			if(organisme != null){
						result.append(organisme.getNom());
						result.append(";");
					}
				}
        	}else{
        		result.append("");
        	}
        	inputValues.put("COMMISSION", result.toString());
        }
    }

  /**
   * recherche l'identité des représentants contenus dans une liste de DocumentModel de représentants et insere dans une String le résultat sous la forme : numero mandat/[Monsieur-Madame] Nom Prenom;
   * 
   * @param representantsDoc liste de documentModel de représentant
   * @param representants StringBuilder à laquelle on ajoute les résultats des identités trouvées
   * @throws ClientException
   */
    private void putRepresentants(List<DocumentModel> representantsDoc, StringBuilder representants) throws ClientException {
        final TableReferenceService trService = SolonMgppServiceLocator.getTableReferenceService();
        for (DocumentModel repDoc : representantsDoc) {
            RepresentantOEP rep = repDoc.getAdapter(RepresentantOEP.class);
            String mandat = rep.getRepresentant();
            MandatNode mandatNode = trService.getMandat(mandat, documentManager);
            if (mandatNode == null) {
                representants.append(mandat).append("/").append("** Mandat inconnu **").append(";");
            } else {
                IdentiteNode identite = mandatNode.getIdentite();
                representants.append(mandat).append("/").append(identite.getCivilite()).append(" ").append(identite.getNom()).append(" ").append(identite.getPrenom()).append(";");
            }
        }
    }

  /**
   * Retourne une chaine de caractère de la forme [Monsieur-Madame] Nom Prénom à partir d'un mandat donné
   * 
   * @param mandat
   * @return
   */
    private String getAuteurFromMandat(String mandat) {
        if (mandat == null) {
            return "";
        } else {
            final TableReferenceService trService = SolonMgppServiceLocator.getTableReferenceService();
            StringBuilder auteur = new StringBuilder();
            try {
                MandatNode mandatNode = trService.getMandat(mandat, documentManager);
                if (mandatNode == null) {
                    auteur.append("** Mandat inconnu **");
                } else {
                    IdentiteNode identite = trService.getMandat(mandat, documentManager).getIdentite();
                    auteur.append(identite.getCivilite()).append(" ").append(identite.getNom()).append(" ").append(identite.getPrenom());
                }
            } catch (ClientException e) {
                LOG.error(documentManager, STLogEnumImpl.FAIL_GET_MANDAT_TEC, "Identité de l'auteur de la fiche résolution non trouvée", e);
            }
            return auteur.toString();
        }
    }
    
    /**
     * Retourne une list de chaine de caractère de la forme [Monsieur-Madame] Nom Prénom à partir d'une lost de mandat donné
     * 
     * @param mandat
     * @return
     */
	private String getCoauteurFromMandat(List<String> mandatList) {
          if (mandatList == null) {
              return null;
          } else {
        	  final TableReferenceService trService = SolonMgppServiceLocator.getTableReferenceService();
			  StringBuilder coauteur = new StringBuilder();
        	  try {
        		  for (String mandat : mandatList) {
        			  MandatNode mandatNode = trService.getMandat(mandat, documentManager);
        			  if (mandatNode == null) {
        				  coauteur.append("** Mandat inconnu **");
        			  } else if (coauteur.length()==0){
        				  IdentiteNode identite = trService.getMandat(mandat, documentManager).getIdentite();
        				  coauteur.append(identite.getCivilite()).append(" ").append(identite.getNom()).append(" ").append(identite.getPrenom());
        			  } else {
        				  IdentiteNode identite = trService.getMandat(mandat, documentManager).getIdentite();
        				  coauteur.append(", ").append(identite.getCivilite()).append(" ").append(identite.getNom()).append(" ").append(identite.getPrenom());
        			  }
        		  }
              } catch (ClientException e) {
                  LOG.error(documentManager, STLogEnumImpl.FAIL_GET_MANDAT_TEC, "Identité de l'auteur de la fiche résolution non trouvée", e);
              }
              return coauteur.toString();
          }
      }

}
