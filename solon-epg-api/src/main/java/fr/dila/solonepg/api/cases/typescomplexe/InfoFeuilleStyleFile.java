package fr.dila.solonepg.api.cases.typescomplexe;

import fr.dila.st.api.domain.ComplexeType;
import org.nuxeo.ecm.core.api.Blob;

/**
 * Interface contenant les informations nécessaire pour afficher un lien vers une feuille de style.
 * (interface utilisé pour afficher les feuilles de styles dans la page d'accueil)
 *
 * @author admin
 */
public interface InfoFeuilleStyleFile extends ComplexeType {
    //getter & setter

    /**
     * récupère l'url du fichier feuille de style
     */
    String getUrl();

    /**
     * définit l'url de fichier feuille de style
     *
     * @param url
     */
    void setUrl(String url);

    String getFileName();
    void setFileName(String fileName);

    String getFileExtension();
    void setFileExtension(String fileExtension);

    Blob getBlob();
    void setBlob(Blob blob);

    /**
     * récupère l'id du fichier feuille de style
     */
    String getId();

    /**
     * définit l'id du fichier feuille de style
     *
     * @param id
     */
    void setId(String id);

    /**
     * récupère la position du fichier dans la liste des fichiers
     *
     * @return
     */
    Integer getPos();

    /**
     * définit la position du fichier feuille de style
     *
     * @param pos
     */
    void setPos(Integer pos);
}
