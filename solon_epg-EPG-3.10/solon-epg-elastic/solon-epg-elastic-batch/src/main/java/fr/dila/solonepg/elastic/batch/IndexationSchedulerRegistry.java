package fr.dila.solonepg.elastic.batch;

import java.io.Serializable;
import java.util.Map;

import org.nuxeo.ecm.platform.scheduler.core.interfaces.Schedule;

/**
 * Issue de Nuxeo ; copiée pour ne pas entrer en conflit avec le service Nuxeo
 */
public interface IndexationSchedulerRegistry {

    /**
     * Registers a schedule.
     *
     * @param schedule the schedule
     */
    void registerSchedule(Schedule schedule);


    /**
     * Registers a schedule.
     * Add all parameters to eventContext.
     *
     * @param schedule
     * @param parameters
     */
    void registerSchedule(Schedule schedule, Map<String, Serializable> parameters);

    /**
     * UnRegisters a schedule.
     *
     * @param scheduleId the schedule id
     * @return true if schedule has been successfully removed.
     */
    boolean unregisterSchedule(String scheduleId);

    /**
     * UnRegisters a schedule.
     *
     * @param schedule to be unregistered
     * @return true if schedule has been successfully removed.
     */
    boolean unregisterSchedule(Schedule schedule);
}
