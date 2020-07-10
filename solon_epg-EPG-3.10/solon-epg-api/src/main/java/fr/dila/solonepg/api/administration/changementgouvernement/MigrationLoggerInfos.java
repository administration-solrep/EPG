package fr.dila.solonepg.api.administration.changementgouvernement;

import java.io.Serializable;

import fr.dila.solonepg.api.constant.SolonEpgConstant;

@SuppressWarnings("serial")
public class MigrationLoggerInfos implements Serializable {

  private long info0;

  private long info1;

  private long info2;

  private String status;

  public MigrationLoggerInfos() {
  }

  public MigrationLoggerInfos(long info0, long info1, long info2, String status) {
    this.info0 = info0;
    this.info1 = info1;
    this.info2 = info2;
    this.status = status;
  }

  public long getInfo0() {
    return info0;
  }

  public void setInfo0(long info0) {
    this.info0 = info0;
  }

  public long getInfo1() {
    return info1;
  }

  public void setInfo1(long info1) {
    this.info1 = info1;
  }

  public long getInfo2() {
    return info2;
  }

  public void setInfo2(long info2) {
    this.info2 = info2;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  public boolean enCours() {
    return this.getStatus() != null && this.getStatus().equals(SolonEpgConstant.EN_COURS_STATUS);
  }

  public boolean editStatus() {
    return this.enCours() || this.terminee();
  }

  public boolean terminee() {
    return this.getStatus() != null && this.getStatus().equals(SolonEpgConstant.TERMINEE_STATUS);
  }

}
