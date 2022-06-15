package fr.dila.solonepg.core.dto;

import fr.dila.solonepg.api.dto.EpreuvePostesDto;

public class EpreuvePostesDtoImpl implements EpreuvePostesDto {
    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -990986518528783207L;

    private String posteBdcId;
    private String postePublicationId;
    private String posteDanId;

    @Override
    public String getEpreuvePosteBdc() {
        return posteBdcId;
    }

    @Override
    public void setEpreuvePosteBdc(String posteBdc) {
        this.posteBdcId = posteBdc;
    }

    @Override
    public String getEpreuvePostePublication() {
        return postePublicationId;
    }

    @Override
    public void setEpreuvePostePublication(String postePublication) {
        this.postePublicationId = postePublication;
    }

    @Override
    public String getEpreuvePosteDan() {
        return posteDanId;
    }

    @Override
    public void setEpreuvePosteDan(String posteDan) {
        this.posteDanId = posteDan;
    }
}
