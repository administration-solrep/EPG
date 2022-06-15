package fr.dila.solonepg.api.birt;

import java.util.Calendar;

public interface BirtResultatFichier extends BirtReportContent {
    String getReportName();

    void setReportName(String reportName);

    Calendar getDateModified();

    void setDateModified(Calendar date);
}
