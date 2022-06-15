package fr.dila.solonepg.core.mock;

import fr.dila.st.api.organigramme.GouvernementNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.PosteNode;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.core.organigramme.EntiteNodeImpl;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

public class MockEntiteNode extends EntiteNodeImpl {
    private static final long serialVersionUID = -302897040234862380L;

    private String id;

    private String label;

    private String typeValue;

    private OrganigrammeType type;

    @Override
    public Date getDateDebut() {
        return null;
    }

    @Override
    public void setDateDebut(Date dateDebut) {}

    @Override
    public Date getDateFin() {
        return null;
    }

    @Override
    public void setDateFin(Date dateFin) {}

    @Override
    public int getParentListSize() {
        return 0;
    }

    @Override
    public void setParentList(List<OrganigrammeNode> parentList) {}

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public boolean getDeleted() {
        return false;
    }

    @Override
    public void setDeleted(boolean deleted) {}

    @Override
    public String getLockUserName() {
        return null;
    }

    @Override
    public void setLockUserName(String lockUserName) {}

    @Override
    public Date getLockDate() {
        return null;
    }

    @Override
    public void setLockDate(Date lockDate) {}

    @Override
    public List<String> getFunctionRead() {
        return null;
    }

    @Override
    public boolean isReadGranted(CoreSession coreSession) {
        return false;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    @Override
    public OrganigrammeType getType() {
        return type;
    }

    public void setType(OrganigrammeType type) {
        this.type = type;
    }

    @Override
    public List<UniteStructurelleNode> getSubUnitesStructurellesList() {
        return null;
    }

    @Override
    public void setSubUnitesStructurellesList(List<UniteStructurelleNode> subUnitesStructurellesList) {}

    @Override
    public List<PosteNode> getSubPostesList() {
        return null;
    }

    @Override
    public void setSubPostesList(List<PosteNode> subPostesList) {}

    @Override
    public String getEdition() {
        return null;
    }

    @Override
    public void setEdition(String edition) {}

    @Override
    public Long getOrdre() {
        return null;
    }

    @Override
    public void setOrdre(Long ordre) {}

    @Override
    public String getFormule() {
        return null;
    }

    @Override
    public void setFormule(String formule) {}

    @Override
    public String getMembreGouvernement() {
        return null;
    }

    @Override
    public String getMembreGouvernementNom() {
        return null;
    }

    @Override
    public void setMembreGouvernementNom(String membreGouvernementNom) {}

    @Override
    public String getMembreGouvernementPrenom() {
        return null;
    }

    @Override
    public void setMembreGouvernementPrenom(String membreGouvernementPrenom) {}

    @Override
    public String getMembreGouvernementCivilite() {
        return null;
    }

    @Override
    public void setMembreGouvernementCivilite(String membreGouvernementCivilite) {}

    @Override
    public GouvernementNode getParentGouvernement() {
        return null;
    }

    @Override
    public void setParentGouvernement(String parentGouvernement) {}

    @Override
    public void setDateDebut(Calendar dateDebut) {}

    @Override
    public void setDateFin(Calendar dateFin) {}

    @Override
    public void setLockDate(Calendar lockDate) {}

    @Override
    public void setFunctionRead(List<String> functions) {}

    @Override
    public boolean isNext() {
        return false;
    }

    @Override
    public String getParentId() {
        return null;
    }

    @Override
    public String getNorMinistere() {
        return null;
    }

    @Override
    public void setNorMinistere(String norMinistere) {}

    @Override
    public boolean isSuiviActiviteNormative() {
        return false;
    }

    @Override
    public void setSuiviActiviteNormative(boolean suiviActiviteNormative) {}

    @Override
    public void setMembreGouvernement(String membreGouvernement) {}

    @Override
    public String getLabelWithNor(String idMinistere) {
        return label;
    }
}
