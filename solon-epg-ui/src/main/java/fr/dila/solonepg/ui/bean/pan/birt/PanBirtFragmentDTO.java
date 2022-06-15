package fr.dila.solonepg.ui.bean.pan.birt;

import fr.dila.solonepg.ui.enums.pan.PanBirtFieldTypeEnum;
import java.util.Map;

/*
 *
 * DTO utilisé pour valoriser les composants Thymeleaf
 *
 */
public class PanBirtFragmentDTO {
    private PanBirtFieldTypeEnum type;
    private Map<String, Object> fragmentParams;

    public PanBirtFragmentDTO() {
        super();
    }

    public PanBirtFragmentDTO(PanBirtFieldTypeEnum type, Map<String, Object> fragmentParams) {
        super();
        this.type = type;
        this.fragmentParams = fragmentParams;
    }

    public PanBirtFieldTypeEnum getType() {
        return type;
    }

    public void setType(PanBirtFieldTypeEnum type) {
        this.type = type;
    }

    public Map<String, Object> getFragmentParams() {
        return fragmentParams;
    }

    public void setFragmentsParams(Map<String, Object> fragmentParams) {
        this.fragmentParams = fragmentParams;
    }
}
