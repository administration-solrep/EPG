package fr.dila.solonepg.api.dto;
import org.apache.commons.lang3.StringUtils;

public class CopyFileStatusDTO {
    public CopyFileStatusDTO() {}

    public CopyFileStatusDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

     private boolean success = false;
     private String message = StringUtils.EMPTY;

     public boolean isSuccess() {
         return success;
     }

     public void setSuccess(boolean success) {
         this.success = success;
     }

     public String getMessage() {
         return message;
     }

     public void setMessage(String message) {
         this.message = message;
     }
}