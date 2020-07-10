package fr.dila.solonepg.web.activitenormative;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;

import fr.dila.solonepg.api.cases.ActiviteNormative;
import fr.dila.solonepg.api.cases.ActiviteNormativeProgrammation;
import fr.dila.solonepg.api.cases.TexteMaitre;
import fr.dila.solonepg.api.cases.typescomplexe.LigneProgrammationHabilitation;
import fr.dila.solonepg.api.logging.enumerationCodes.EpgLogEnumImpl;
import fr.dila.solonepg.core.dto.activitenormative.LigneProgrammationHabilitationDTO;
import fr.dila.solonepg.core.dto.activitenormative.TableauDeProgrammationHabilitationDTO;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;
import fr.dila.ss.api.security.principal.SSPrincipal;
import fr.dila.st.api.logging.STLogger;
import fr.dila.st.core.factory.STLogFactory;
import fr.dila.st.core.util.DateUtil;
import fr.dila.st.web.context.NavigationContextBean;

/**
 * Bean Seam de gestion du tableau de programmation et de suivi du suivi des habilitations de l'activite normative 
 * 
 * @author asatre
 */
@Name("tableauProgrammation38CActions")
@Scope(ScopeType.CONVERSATION)
public class TableauProgrammation38CActionsBean implements Serializable {

    private static final long serialVersionUID = 2776349205588506388L;

    @In(create = true, required = false)
    protected transient NavigationContextBean navigationContext;

    @In(create = true, required = true)
    protected transient CoreSession documentManager;

    @In(required = true, create = true)
    protected SSPrincipal ssPrincipal;
    
    @In(create = true, required = false)
    protected transient ResourcesAccessor resourcesAccessor;

    private TableauDeProgrammationHabilitationDTO tableauDeProgrammationHabilitationDTO;
    
    /**
     * Logger formalisé en surcouche du logger apache/log4j 
     */
    private static final STLogger LOGGER = STLogFactory.getLog(TableauProgrammation38CActionsBean.class);
    
    
    /**
     * Ajouter pour f:convertDateTime
     * @return
     */
    public TimeZone getTimeZone(){
      return TimeZone.getDefault();
    }

    /**
     * Construction du tableau de programmation de la loi
     * @return
     * @throws ClientException
     */
    public List<LigneProgrammationHabilitationDTO> getCurrentListProgrammationHabilitation() throws ClientException {
        ActiviteNormativeProgrammation activiteNormativeProgrammation = navigationContext.getCurrentDocument().getAdapter(ActiviteNormativeProgrammation.class);
        // si le tableau est défigé, on le vide et on le recalcule
        if(StringUtils.isEmpty(activiteNormativeProgrammation.getLockHabUser())){
        	SolonEpgServiceLocator.getActiviteNormativeService().removeCurrentProgrammationHabilitation(navigationContext.getCurrentDocument().getAdapter(ActiviteNormative.class), documentManager);
            tableauDeProgrammationHabilitationDTO = new TableauDeProgrammationHabilitationDTO(activiteNormativeProgrammation, documentManager, Boolean.TRUE, Boolean.FALSE);
        	
            //on save le tableau à exporter 
            List<LigneProgrammationHabilitation> lignesProgrammations = tableauDeProgrammationHabilitationDTO.remapField(new LinkedList<LigneProgrammationHabilitation>());
            SolonEpgServiceLocator.getActiviteNormativeService().saveCurrentProgrammationHabilitation(lignesProgrammations, navigationContext.getCurrentDocument().getAdapter(ActiviteNormative.class), documentManager);

        }
        // si le tableau figé
        else{
           tableauDeProgrammationHabilitationDTO = new TableauDeProgrammationHabilitationDTO(activiteNormativeProgrammation, documentManager, Boolean.TRUE, Boolean.FALSE);
        }
        return tableauDeProgrammationHabilitationDTO.getListProgrammation();
    }  
    
    /**
     * Construction du tableau de suivi de la loi
     * @return
     * @throws ClientException
     */
    public List<LigneProgrammationHabilitationDTO> getCurrentListSuiviHabilitation() throws ClientException {
        ActiviteNormativeProgrammation activiteNormativeProgrammation = navigationContext.getCurrentDocument().getAdapter(ActiviteNormativeProgrammation.class);
        tableauDeProgrammationHabilitationDTO = new TableauDeProgrammationHabilitationDTO(activiteNormativeProgrammation, documentManager, Boolean.FALSE, Boolean.FALSE);
        return tableauDeProgrammationHabilitationDTO.getListProgrammation();

    } 
    
    /**
     * Verouillage du tableau de programmation
     * @return
     */
    public String lockCurrentProgrammationHabilitation() throws ClientException {
        if(tableauDeProgrammationHabilitationDTO != null){
            List<LigneProgrammationHabilitation> lignesProgrammations = tableauDeProgrammationHabilitationDTO.remapField(new LinkedList<LigneProgrammationHabilitation>());
            SolonEpgServiceLocator.getActiviteNormativeService().saveCurrentProgrammationHabilitation(lignesProgrammations, navigationContext.getCurrentDocument().getAdapter(ActiviteNormative.class), documentManager);
            SolonEpgServiceLocator.getActiviteNormativeService().lockCurrentProgrammationHabilitation( navigationContext.getCurrentDocument().getAdapter(ActiviteNormative.class), documentManager);
        }
        return null;
    }
    
    /**
     * Deverouillage du tableau de programmation
     * @return
     */
    public String unlockCurrentProgrammationHabilitation() throws ClientException {
        if(tableauDeProgrammationHabilitationDTO != null){
            SolonEpgServiceLocator.getActiviteNormativeService().removeCurrentProgrammationHabilitation(navigationContext.getCurrentDocument().getAdapter(ActiviteNormative.class), documentManager);
            SolonEpgServiceLocator.getActiviteNormativeService().unlockCurrentProgrammationHabilitation(navigationContext.getCurrentDocument().getAdapter(ActiviteNormative.class), documentManager);
        }
        
        return null;
    }
    
    public String getTableauProgrammationLockInfo() {
        ActiviteNormativeProgrammation activiteNormativeProgrammation = navigationContext.getCurrentDocument().getAdapter(ActiviteNormativeProgrammation.class);
        if(StringUtils.isNotEmpty(activiteNormativeProgrammation.getLockHabUser())){
            return "Figé le " + DateUtil.formatForClient(activiteNormativeProgrammation.getLockHabDate().getTime()) + ", par " + activiteNormativeProgrammation.getLockHabUser();
        }
        return "";
    }
    
    public Boolean isCurrentProgrammationHabilitationLocked() {
        ActiviteNormativeProgrammation activiteNormativeProgrammation = navigationContext.getCurrentDocument().getAdapter(ActiviteNormativeProgrammation.class);
        return StringUtils.isEmpty(activiteNormativeProgrammation.getLockHabUser());
    }
    
    /**
     * publication du tableau de suivi
     * @return
     */
    public String publierTableauSuiviHabilitation() throws ClientException {
        if(tableauDeProgrammationHabilitationDTO != null){
            List<LigneProgrammationHabilitation> lignesProgrammations = tableauDeProgrammationHabilitationDTO.remapField(new LinkedList<LigneProgrammationHabilitation>());
            SolonEpgServiceLocator.getActiviteNormativeService().publierTableauSuiviHab(lignesProgrammations, navigationContext.getCurrentDocument().getAdapter(ActiviteNormative.class), documentManager);
            
            String loiNb = navigationContext.getCurrentDocument().getAdapter(TexteMaitre.class).getNumeroNor();
            try{
                String generatedReportPath = SolonEpgServiceLocator.getActiviteNormativeService().getPathDirANStatistiquesPublie();
                SimpleDateFormat sdf = DateUtil.simpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat mySdf = DateUtil.simpleDateFormat("MMMM yyyy");

                File htmlReport = new File(generatedReportPath+"/"+"tableauDeSuiviHab-"+loiNb+".html");
                if(!htmlReport.exists()){
                    htmlReport.createNewFile();
                }
                FileOutputStream  outputStream  = new FileOutputStream(htmlReport);
                outputStream.write("<style>.suiviTbl{font: normal 12px \"Lucida Grande\", sans-serif;border-collapse:collapse;width:2000px}\n.suiviTbl td,.suiviTbl th{border:thin solid black;}</style>".getBytes());
                outputStream.write(("<table class='suiviTbl' cellpadding='2' cellspacing='0'>" +
                        "<colgroup><col style='width:20px'/><col style='width:100px'/><col style='width:120px'/><col style='width:250px'/><col style='width:100px'/>" +
                        "<col style='width:100px'/><col style='width:100px'/><col style='width:100px'/><col style='width:80px'/><col style='width:80px'/>" +
                        "<col style='width:80px'/><col style='width:80px'/><col style='width:80px'/><col style='width:300px'/><col style='width:100px'/>" +
                        "<col style='width:100px'/><col style='width:120px'/><col style='width:80px'/></colgroup>"+
                    "<thead><tr>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.numeroOrdre")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.article")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.objetRIM")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.ministerePilote")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.convention")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.dateTerme")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.conventionDepot")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.datePrevisionnelleSaisineCE")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.datePrevisionnelleCM")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.observation")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.legislature")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.numeroNorOrdo")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.objetOrdo")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.dateSaisineCEOrdo")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.datePassageCMOrdo")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.datePublicationOrdo")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.titreOfficielOrdo")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.numeroOrdo")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.conventionDepotOrdo")+"</th>").getBytes());
                outputStream.write(("<th>"+resourcesAccessor.getMessages().get("activite.normative.tableau.programmation.hab.dateLimiteDepotOrdo")+"</th>").getBytes());
                outputStream.write("</tr></thead><tbody>".getBytes());

                for(LigneProgrammationHabilitationDTO lp: tableauDeProgrammationHabilitationDTO.getListProgrammation())
                {
                    outputStream.write(("<tr>").getBytes());

                    outputStream.write(("<td>"+(lp.getNumeroOrdre()!=null?lp.getNumeroOrdre():"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getArticle()!=null?lp.getArticle():"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getObjetRIM()!=null?lp.getObjetRIM():"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getMinisterePilote()!=null?lp.getMinisterePilote():"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getConvention()!=null?lp.getConvention():"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getDateTerme()!=null?sdf.format(lp.getDateTerme()):"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getConventionDepot()!=null?lp.getConventionDepot():"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getDatePrevisionnelleSaisineCE()!=null?mySdf.format(lp.getDatePrevisionnelleSaisineCE()):"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getDatePrevisionnelleCM()!=null?mySdf.format(lp.getDatePrevisionnelleCM()):"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getObservation()!=null?lp.getObservation():"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getLegislature()!=null?lp.getLegislature():"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getNumeroNorOrdo()!=null?lp.getNumeroNorOrdo():"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getObjetOrdo()!=null?lp.getObjetOrdo():"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getDateSaisineCEOrdo()!=null?sdf.format(lp.getDateSaisineCEOrdo()):"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getDatePassageCMOrdo()!=null?sdf.format(lp.getDatePassageCMOrdo()):"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getDatePublicationOrdo()!=null?sdf.format(lp.getDatePublicationOrdo()):"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getTitreOfficielOrdo()!=null?lp.getTitreOfficielOrdo():"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getNumeroOrdo()!=null?lp.getNumeroOrdo():"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getConventionDepotOrdo()!=null?lp.getConventionDepotOrdo():"")+"</td>").getBytes());
                    outputStream.write(("<td>"+(lp.getDateLimiteDepotOrdo()!=null?sdf.format(lp.getDateLimiteDepotOrdo()):"")+"</td>").getBytes());
                    outputStream.write(("</tr>").getBytes());
                }
                outputStream.write(("</tbody></table>").getBytes());
                outputStream.close();
            }
            catch(IOException ioe){
                LOGGER.error(documentManager, EpgLogEnumImpl.FAIL_PUBLIER_TABLEAU_SUIVI_HABILITATION_TEC) ;
            }
        }
        
        return null;
    }
    
    public String getTableauSuiviPublicationInfo() {
        ActiviteNormativeProgrammation activiteNormativeProgrammation = navigationContext.getCurrentDocument().getAdapter(ActiviteNormativeProgrammation.class);
        if(StringUtils.isNotEmpty(activiteNormativeProgrammation.getTableauSuiviHabPublicationUser())){
            return "Publié le " + DateUtil.formatForClient(activiteNormativeProgrammation.getTableauSuiviHabPublicationDate().getTime()) + ", par " + activiteNormativeProgrammation.getTableauSuiviHabPublicationUser();
        }
        return "";
    }
    
    public String refreshCurrentProgrammationHabilitation() {
        return null;
    }

}
