package fr.dila.solonepg.web.pdf;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Install;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.platform.contentview.jsf.ContentView;
import org.nuxeo.ecm.platform.query.api.PageProvider;
import org.nuxeo.ecm.platform.query.api.PageSelections;
import org.nuxeo.ecm.platform.query.nxql.CoreQueryDocumentPageProvider;

import fr.dila.solonepg.web.contentview.DossierPageProvider;
import fr.dila.st.core.util.DateUtil;

/**
 * WebBean de gestion de l'affichage et de l'impression en pdf.
 * 
 * @author arolin
 */
@Name("pdfPrintActions")
@Scope(ScopeType.EVENT)
@Install(precedence = Install.FRAMEWORK + 1)
public class PdfPrintActionsBean extends fr.dila.st.web.pdf.PdfPrintActionsBean implements Serializable {
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;
    
    private String exportName;

    /**
     * Affiche la liste des résultats dans un pdf en récupérant tout les résultats et pas uniquement les résultats affichés.
     * 
     * @param view_name
     * @throws Exception
     */
    @Override
    public void doRenderViewList(String view_name) throws Exception {
        ContentView contentView = contentViewActions.getCurrentContentView();
        if (contentView != null && contentView.getCurrentPageProvider() != null) {
            PageProvider<?> provider = contentView.getCurrentPageProvider();
            PageSelections<?> pageSelection = provider.getCurrentSelectPage();
            if (!provider.hasError() && pageSelection != null && pageSelection.getEntries() != null && pageSelection.getEntries().size() > 0) {
                
              if (provider instanceof CoreQueryDocumentPageProvider) {
                    
                    // récupération de tous les résultats
                    CoreQueryDocumentPageProvider coreProvider = (CoreQueryDocumentPageProvider) provider;
                    coreProvider.setPageSize(provider.getResultsCount());
                    coreProvider.setSelectedEntries(coreProvider.setCurrentPage(1));
                    Map<String, Serializable> props = coreProvider.getProperties();
                    props.put("exportName", exportName);
                    coreProvider.setProperties(props);
                } else if (provider instanceof DossierPageProvider) {
                    
                    // récupération de tous les résultats
                    DossierPageProvider dossierPageProvider = (DossierPageProvider) provider;
                    dossierPageProvider.setPageSize(provider.getResultsCount());
                    dossierPageProvider.setSelectedEntries(dossierPageProvider.setCurrentPage(1));
                    Map<String, Serializable> props = dossierPageProvider.getProperties();
                    props.put("exportName", exportName);
                    dossierPageProvider.setProperties(props);
                }
            }
        }

        super.doRenderView(view_name);
    }

    public void doRenderViewList(String view_name, String exportName) throws Exception {
      this.exportName = exportName;
      doRenderViewList(view_name);
    }
    
    public String getCurrentDate() {
      SimpleDateFormat formatter = DateUtil.simpleDateFormat("dd/MM/yyyy");
      return "("+formatter.format(new Date())+")";
    }

}
