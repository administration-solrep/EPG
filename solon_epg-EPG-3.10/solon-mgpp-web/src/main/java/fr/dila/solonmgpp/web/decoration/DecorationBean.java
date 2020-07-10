package fr.dila.solonmgpp.web.decoration;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Classe abstraite de decoration de la ligne sélectionné dans une liste
 * 
 * @author asatre
 * 
 */
public abstract class DecorationBean {

    /**
     * décoration de la ligne sélectionnée
     * 
     * @param doc
     * @param defaultClass
     * @return
     * @throws ClientException
     */
    public String decorate(DocumentModel doc, String defaultClass) throws ClientException {

        DocumentModel currentDoc = getCurrentDocument();
        if (currentDoc != null && currentDoc.getId() != null) {
            if (currentDoc.getId().equals(doc.getId())) {
                defaultClass = "dataRowSelected";
            }
        }

        return addDecorationClass(doc, defaultClass);

    }

    /**
     * Ajout de classe css spécifique
     * 
     * @param defaultClass
     * @return
     */
    protected abstract String addDecorationClass(DocumentModel doc, String defaultClass);

    /**
     * retourne le document sélectionné courant
     * 
     * @return
     */
    protected abstract DocumentModel getCurrentDocument();

}
