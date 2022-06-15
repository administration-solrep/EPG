package fr.dila.solonepg.webengine.wsutil;

import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

public class MockOrganigrammeNode implements OrganigrammeNode {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public OrganigrammeType getType() {
        return type;
    }

    public void setType(OrganigrammeType type) {
        this.type = type;
    }

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
    public String getLabelWithNor(String idMinistere) {
        return label;
    }
}
