package fr.dila.solonepg.web.feuillestyle;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.core.api.Blob;

import fr.dila.solonepg.api.cases.typescomplexe.InfoFeuilleStyleFile;
import fr.dila.solonepg.api.service.FeuilleStyleService;
import fr.dila.solonepg.core.service.SolonEpgServiceLocator;

/**
 * WebBean qui permet de télécharger une feuille de style depuis la page d'accueil de Solon Epg.
 * 
 * @author arolin
 */
public class FeuilleStyleBean {
    
    
    private static final Log log = LogFactory.getLog(FeuilleStyleBean.class);

    public static void getFeuilleStyleFile(HttpServletResponse response, String fileId, String fileName) throws Exception {

        if (response == null || fileId == null || fileId.isEmpty() || fileName == null || fileName.isEmpty()) {
            return;
        }
        
        OutputStream outputStream = null; 
        InputStream inputStream = null;
        BufferedInputStream fif = null;
        
        try {
            
            // récupération réponse
            outputStream = response.getOutputStream();

            // Récupération fichier
            final FeuilleStyleService feuilleStyleService = SolonEpgServiceLocator.getFeuilleStyleService();
            InfoFeuilleStyleFile infoFeuilleStyle = feuilleStyleService.getFeuilleStyleInfo(fileId, fileName);
            Blob blob = infoFeuilleStyle.getBlob();
            response.reset();
            response.setContentType(infoFeuilleStyle.getFileExtension());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + infoFeuilleStyle.getFileName() + "\";");
            // transfert du fichier dans la réponse
            inputStream = blob.getStream();
            fif = new BufferedInputStream(inputStream);
            // copie le fichier dans le flux de sortie
            int data;
            while ((data = fif.read()) != -1) {
                outputStream.write(data);
            }
            
            
        } catch (Exception e) {
            log.error("Erreur lors de la recuperation du feuille de style " + e.getMessage()) ;
        }finally{
            if(inputStream != null){
                inputStream.close() ;
            }            
            if(fif != null){
                fif.close() ;
            }
        }
        
        if(outputStream != null){
            outputStream.flush();
            outputStream.close();            
        }

    }
    
    private FeuilleStyleBean() {
     //Private default constructor
    }
}
