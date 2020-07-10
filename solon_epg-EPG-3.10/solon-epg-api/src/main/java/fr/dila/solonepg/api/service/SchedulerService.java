package fr.dila.solonepg.api.service;

import java.util.List;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;

import fr.dila.solonepg.api.dto.JobDTO;

public interface SchedulerService {
	
	List<JobDTO> findAllCronJob() throws ClientException;

	void execute(JobDTO jobDTO, CoreSession session) throws ClientException;

}
