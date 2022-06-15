package fr.dila.solonepg.api.birt;

import fr.dila.st.api.domain.STDomainObject;
import java.util.Calendar;

public interface BirtRefreshFichier extends BirtReportContent, STDomainObject {
    String getOwner();

    void setOwner(String owner);

    String getFileName();

    void setFileName(String fileName);

    Calendar getDateRequest();

    void setDateRequest(Calendar date);

    boolean isRefreshing();

    void setRefreshing(boolean refreshing);

    String getParamList();

    void setParamList(String paramList);
}
