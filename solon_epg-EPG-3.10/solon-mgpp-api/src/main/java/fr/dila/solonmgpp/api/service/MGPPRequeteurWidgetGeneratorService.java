package fr.dila.solonmgpp.api.service;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import fr.dila.st.api.requeteur.RequeteurWidgetDescription;

public interface MGPPRequeteurWidgetGeneratorService{
    /**
     * Génére les champs à partir des données fournies dans le point d'extension selecteddocument
     * @return la liste des descriptions de widgets
     * @throws Exception
     */
    public Collection<RequeteurWidgetDescription> getFieldsDescription() throws Exception;

    /**
     * Récupére la liste des descriptions de widget que l'utilisateur a entré.
     * @return
     */
    public Map<String, RequeteurWidgetDescription> getWigetDescriptionRegistry();

    /**
     * Génère les contrib de layout et de widget pour le requêteur
     * @return 
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws Exception 
     */
    URL generateWidgets() throws ParserConfigurationException, IOException, Exception;

    /**
     * Récupère les catégories utilisées dans la contribution
     * @return un ensemble de noms de catégories
     */
    public List<String> getCategories();

}
