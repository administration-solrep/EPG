package fr.dila.solonepg.api.service;

import fr.dila.solonepg.api.dto.JobDTO;
import java.util.List;
import org.nuxeo.ecm.core.api.CoreSession;

public interface SchedulerService {
    List<JobDTO> findAllCronJob();

    void execute(JobDTO jobDTO, CoreSession session);
}
