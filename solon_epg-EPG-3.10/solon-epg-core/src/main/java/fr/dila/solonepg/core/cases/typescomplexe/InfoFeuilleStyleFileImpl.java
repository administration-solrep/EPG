package fr.dila.solonepg.core.cases.typescomplexe;

import java.io.Serializable;
import java.util.Map;

import org.nuxeo.ecm.core.api.Blob;

import fr.dila.solonepg.api.cases.typescomplexe.InfoFeuilleStyleFile;
import fr.dila.solonepg.api.constant.DossierSolonEpgConstants;
import fr.dila.st.core.domain.ComplexeTypeImpl;

/**
 * @author admin
 */
public class InfoFeuilleStyleFileImpl extends ComplexeTypeImpl implements InfoFeuilleStyleFile {

    private static final long serialVersionUID = -2771545828606803367L;

    protected String id;
    
    protected String url;
    
    protected String fileName;
    
    protected String fileExtension;
    
    protected Blob blob;
    
    protected Integer pos;
    
    public InfoFeuilleStyleFileImpl(){
        super();
    }
    
    public InfoFeuilleStyleFileImpl(Map<String, Serializable> serializableMap) {
        if (serializableMap.get(DossierSolonEpgConstants.FILE_URL_PROPERTY) instanceof String) {
            this.url = (String) serializableMap.get(DossierSolonEpgConstants.FILE_URL_PROPERTY);
        }
        if (serializableMap.get(DossierSolonEpgConstants.FILE_NAME_PROPERTY) instanceof String) {
            this.fileName = (String) serializableMap.get(DossierSolonEpgConstants.FILE_NAME_PROPERTY);
        }
        if (serializableMap.get(DossierSolonEpgConstants.FILE_EXTENSION_PROPERTY) instanceof String) {
            this.fileExtension = (String) serializableMap.get(DossierSolonEpgConstants.FILE_EXTENSION_PROPERTY);
        }
        if (serializableMap.get("id") instanceof String) {
            this.id = (String) serializableMap.get("id");
        }
        if (serializableMap.get("blob") instanceof String) {
            this.blob = (Blob) serializableMap.get("blob");
        }
        if (serializableMap.get("pos") instanceof Integer) {
            this.pos = (Integer) serializableMap.get("pos");
        }
    }

    @Override
    public void setSerializableMap(Map<String, Serializable> serializableMap) {
        Serializable url = serializableMap.get(DossierSolonEpgConstants.FILE_URL_PROPERTY);
        Serializable fileName = serializableMap.get(DossierSolonEpgConstants.FILE_NAME_PROPERTY);
        Serializable fileExtension = serializableMap.get(DossierSolonEpgConstants.FILE_EXTENSION_PROPERTY);
        Serializable blob = serializableMap.get("blob");
        Serializable id = serializableMap.get("id");
        Serializable pos = serializableMap.get("pos");
        //set the map
        super.setSerializableMap(serializableMap);
        //set all the field
        setUrl((String)url);
        setFileName((String)fileName);
        setFileExtension((String)fileExtension);
        setBlob((Blob)blob);
        setId((String) id);
        setPos((Integer) pos); 
    }
    
    
    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
       this.url=url;
       put(DossierSolonEpgConstants.FILE_URL_PROPERTY,url);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName=fileName;
        put(DossierSolonEpgConstants.FILE_NAME_PROPERTY,fileName);
    }

    @Override
    public String getFileExtension() {
        return fileExtension;
    }

    @Override
    public void setFileExtension(String fileExtension) {
        this.fileExtension=fileExtension;
        put(DossierSolonEpgConstants.FILE_EXTENSION_PROPERTY,fileExtension);
    }

    @Override
    public Blob getBlob() {
        return blob;
    }

    @Override
    public void setBlob(Blob blob) {
        this.blob=blob;
        put("blob",(Serializable) blob);
    }
    
    @Override
    public Integer getPos() {
        return pos;
    }

    @Override
    public void setPos(Integer pos) {
        this.pos=pos;
        put("pos",pos);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id=id;
        put("id",id);
    }

}
