package fr.dila.solonepg.ui.enums.pan;

public enum PanBirtFieldTypeEnum {
    RADIO("input-optin"),
    SELECT("select"),
    DATE_SIMPLE("date-picker"),
    DATE_RANGE("date-range");

    private String key;

    PanBirtFieldTypeEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }
}
