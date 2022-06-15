package fr.dila.solonepg.api.dto;

import java.io.Serializable;

public interface EpreuvePostesDto extends Serializable {
    String getEpreuvePosteBdc();

    void setEpreuvePosteBdc(String posteBdc);

    String getEpreuvePostePublication();

    void setEpreuvePostePublication(String postePublication);

    String getEpreuvePosteDan();

    void setEpreuvePosteDan(String posteDan);
}
