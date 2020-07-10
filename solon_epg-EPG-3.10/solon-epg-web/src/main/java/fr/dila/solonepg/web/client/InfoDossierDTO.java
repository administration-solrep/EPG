package fr.dila.solonepg.web.client;

import java.io.Serializable;

public class InfoDossierDTO implements Serializable {

	private static final long serialVersionUID = 78416213510924898L;

	private String lockInfo;
	private Boolean lock;
	private Boolean urgent;
	private Boolean retourPourModification;

	public InfoDossierDTO() {
		lockInfo = null;
		lock = Boolean.FALSE;
		urgent = Boolean.FALSE;
		retourPourModification = Boolean.FALSE;
	}

	public String getLockInfo() {
		return lockInfo;
	}

	public void setLockInfo(String lockInfo) {
		this.lockInfo = lockInfo;
	}

	public Boolean isUrgent() {
		return urgent;
	}

	public void setUrgent(Boolean urgent) {
		this.urgent = urgent;
	}

	public Boolean isRetourPourModification() {
		return retourPourModification;
	}

	public void setRetourPourModification(Boolean retourPourModification) {
		this.retourPourModification = retourPourModification;
	}

	public void setLock(Boolean lock) {
		this.lock = lock;
	}

	public Boolean isLock() {
		return lock;
	}
	
	@Override
	public String toString(){
		return "InfoDossierDTO : [lockInfo = "+lockInfo+", lock = "+lock+", urgent = "+urgent+", retourPourModification = "+retourPourModification+"]";
		
	}

}
