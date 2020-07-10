package fr.dila.solonepg.api.activitenormative;

import java.io.Serializable;

/**
 * classe qui represenete un report des statistiques pour les activites normatives 
 * 
 *
 */
public class ANReport implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    
    private String name;

    private ANReportEnum type;

    public ANReport(String name, ANReportEnum type) {
        this.name = name;
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(ANReportEnum type) {
        this.type = type;
    }

    public ANReportEnum getType() {
        return type;
    }

}
