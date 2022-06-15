package fr.dila.solon.birt.common;

public class RunResult {

    private int status;
    private String filename;

    public RunResult(int status, String filename) {
        this.status = status;
        this.filename = filename;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}
