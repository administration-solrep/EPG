package fr.dila.solonepg.api.dto;

import fr.dila.solonepg.api.dto.JobDTO;

public class JobDTOImpl implements JobDTO{
	
	private String name;
	private String eventName;
	
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public String getEventName() {
		return eventName;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
