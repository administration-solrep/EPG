package fr.dila.solonepg.core.mock;

import fr.dila.st.api.organigramme.EntiteNode;
import fr.dila.st.api.organigramme.InstitutionNode;
import fr.dila.st.api.organigramme.OrganigrammeNode;
import fr.dila.st.api.organigramme.OrganigrammeType;
import fr.dila.st.api.organigramme.UniteStructurelleNode;
import fr.dila.st.api.user.STUser;
import fr.dila.st.core.organigramme.PosteNodeImpl;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

public class MockPosteNode extends PosteNodeImpl {
    private static final long serialVersionUID = 98288058938742311L;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(final String anid) {}

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public void setLabel(final String label) {}

    @Override
    public OrganigrammeType getType() {
        return null;
    }

    @Override
    public Date getDateDebut() {
        return null;
    }

    @Override
    public void setDateDebut(final Date dateDebut) {}

    @Override
    public void setDateDebut(final Calendar dateDebut) {}

    @Override
    public Date getDateFin() {
        return null;
    }

    @Override
    public void setDateFin(final Date dateFin) {}

    @Override
    public void setDateFin(final Calendar dateFin) {}

    @Override
    public int getParentListSize() {
        return 0;
    }

    @Override
    public void setParentList(final List<OrganigrammeNode> parentList) {}

    @Override
    public boolean getDeleted() {
        return false;
    }

    @Override
    public void setDeleted(final boolean deleted) {}

    @Override
    public String getLockUserName() {
        return null;
    }

    @Override
    public void setLockUserName(final String lockUserName) {}

    @Override
    public Date getLockDate() {
        return null;
    }

    @Override
    public void setLockDate(final Date lockDate) {}

    @Override
    public void setLockDate(final Calendar lockDate) {}

    @Override
    public List<String> getFunctionRead() {
        return null;
    }

    @Override
    public void setFunctionRead(final List<String> functions) {}

    @Override
    public boolean isReadGranted(final CoreSession coreSession) {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public boolean isNext() {
        return false;
    }

    @Override
    public List<EntiteNode> getEntiteParentList() {
        return null;
    }

    @Override
    public void setEntiteParentList(final List<EntiteNode> entiteParentList) {}

    @Override
    public List<UniteStructurelleNode> getUniteStructurelleParentList() {
        return null;
    }

    @Override
    public void setUniteStructurelleParentList(final List<UniteStructurelleNode> parentList) {}

    @Override
    public String getParentId() {
        return null;
    }

    @Override
    public String getParentEntiteId() {
        return null;
    }

    @Override
    public void setParentEntiteId(final String parentEntiteId) {}

    @Override
    public String getParentUniteId() {
        return null;
    }

    @Override
    public void setParentUniteId(final String parentUniteId) {}

    @Override
    public void setParentEntiteIds(final List<String> list) {}

    @Override
    public List<InstitutionNode> getInstitutionParentList() {
        return null;
    }

    @Override
    public void setInstitutionParentList(final List<InstitutionNode> instututionParentList) {}

    @Override
    public List<String> getParentInstitIds() {
        return null;
    }

    @Override
    public void setParentInstitIds(List<String> list) {}

    @Override
    public List<String> getMembers() {
        return null;
    }

    @Override
    public void setMembers(List<String> members) {}

    @Override
    public List<STUser> getUserList() {
        return null;
    }

    @Override
    public int getUserListSize() {
        return 0;
    }

    @Override
    public boolean isPosteBdc() {
        return false;
    }

    @Override
    public void setPosteBdc(boolean posteBdc) {}

    @Override
    public boolean isChargeMissionSGG() {
        return false;
    }

    @Override
    public void setChargeMissionSGG(boolean chargeMissionSGG) {}

    @Override
    public boolean isConseillerPM() {
        return false;
    }

    @Override
    public void setConseillerPM(boolean conseillerPM) {}

    @Override
    public void setWsUrl(String wsUrl) {}

    @Override
    public void setWsUser(String wsUser) {}

    @Override
    public void setWsPassword(String wsPassword) {}

    @Override
    public void setWsKeyAlias(String keyAlias) {}

    @Override
    public String getWsUrl() {
        return null;
    }

    @Override
    public String getWsUser() {
        return null;
    }

    @Override
    public String getWsPassword() {
        return null;
    }

    @Override
    public String getWsKeyAlias() {
        return null;
    }

    @Override
    public boolean isPosteWs() {
        return false;
    }

    @Override
    public void setPosteWs(boolean posteWs) {}

    @Override
    public boolean isSuperviseurSGG() {
        return false;
    }

    @Override
    public void setSuperviseurSGG(boolean superviseurSGG) {}

    @Override
    public List<String> getParentEntiteIds() {
        return null;
    }

    @Override
    public List<String> getParentUnitIds() {
        return null;
    }

    @Override
    public void setParentUnitIds(List<String> list) {}

    @Override
    public String getLabelWithNor(String idMinistere) {
        return null;
    }

    @Override
    public EntiteNode getFirstEntiteParent() {
        return null;
    }

    @Override
    public UniteStructurelleNode getFirstUSParent() {
        return null;
    }
}
