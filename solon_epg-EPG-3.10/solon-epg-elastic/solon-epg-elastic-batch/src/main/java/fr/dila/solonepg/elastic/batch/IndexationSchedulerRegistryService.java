package fr.dila.solonepg.elastic.batch;

import java.io.Serializable;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.platform.scheduler.core.interfaces.Schedule;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.DefaultComponent;
import org.nuxeo.runtime.model.Extension;
import org.nuxeo.runtime.model.RuntimeContext;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ObjectAlreadyExistsException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;

/**
 * Spécialisation du service Nuxeo qui s'assure de configurer les jobs comme étant non concurrents.
 * 
 * Récupération par copie de la classe d'origine, chargement d'une configuration spécifique
 * et utilisation d'une classe de job spécialisée.
 * 
 * Les attributs private ne permettent pas de spécialiser par héritage.
 */
public class IndexationSchedulerRegistryService extends DefaultComponent implements
        IndexationSchedulerRegistry {

    private static final Log log = LogFactory.getLog(IndexationSchedulerRegistryService.class);

    private RuntimeContext bundle;

    private Scheduler scheduler;

    public void activate(ComponentContext context) throws Exception {
        log.debug("Activate");
        bundle = context.getRuntimeContext();

        // Find a scheduler
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        
        // SOLON: configuration spécifique
        URL cfg = context.getRuntimeContext().getResource(
                "config/quartz-indexation.properties");
        if (cfg != null) {
            schedulerFactory.initialize(cfg.openStream());
        }
        scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        // server = MBeanServerFactory.createMBeanServer();
        // server.createMBean("org.quartz.ee.jmx.jboss.QuartzService",
        // quartzObjectName);

        // clean up all nuxeo jobs
        // https://jira.nuxeo.com/browse/NXP-7303
        GroupMatcher<JobKey> matcher = GroupMatcher.jobGroupEquals("nuxeo") ;
        Set<JobKey> jobs = scheduler.getJobKeys(matcher );
        scheduler.deleteJobs(new ArrayList<JobKey>(jobs));
    }

    private Map<String, JobKey> jobKeys = new HashMap<String, JobKey>();

    @Override
    public void deactivate(ComponentContext context) throws Exception {
        log.debug("Deactivate");
        scheduler.shutdown();
    }

    @Override
    public void registerExtension(Extension extension) {
        Object[] contribs = extension.getContributions();
        for (Object contrib : contribs) {
            Schedule schedule = (Schedule) contrib;
            registerSchedule(schedule);
        }
    }

    @Override
    public void unregisterExtension(Extension extension) {
        // do nothing to do ;
        // clean up will be done when service is activated
        // see https://jira.nuxeo.com/browse/NXP-7303
    }

    public RuntimeContext getContext() {
        return bundle;
    }

    @Override
    public void registerSchedule(Schedule schedule) {
        registerSchedule(schedule, null);
    }

    @Override
    public void registerSchedule(Schedule schedule,
            Map<String, Serializable> parameters) {
        log.info("Registering " + schedule);
        JobDetail job = new JobDetailImpl(schedule.getId(), "nuxeo", DisallowConcurrentExecutionEventJob.class);
        JobDataMap map = job.getJobDataMap();
        map.put("eventId", schedule.getEventId());
        map.put("eventCategory", schedule.getEventCategory());
        map.put("username", schedule.getUsername());

        if (parameters != null) {
            map.putAll(parameters);
        }

        Trigger trigger;
        try {
            trigger = new CronTriggerImpl(schedule.getId(), "nuxeo",
                    schedule.getCronExpression());
        } catch (ParseException e) {
            log.error(String.format(
                    "invalid cron expresion '%s' for schedule '%s'",
                    schedule.getCronExpression(), schedule.getId()), e);
            return;
        }
        // This is useful when testing to avoid multiple threads:
        // trigger = new SimpleTrigger(schedule.getId(), "nuxeo");

        try {
            scheduler.scheduleJob(job, trigger);
            jobKeys.put(schedule.getId(), job.getKey());
        } catch (ObjectAlreadyExistsException e) {
            ; // when jobs are persisted in a database, the job should already
              // be there
        } catch (SchedulerException e) {
            log.error(String.format("failed to schedule job with id '%s': %s",
                    schedule.getId(), e.getMessage()), e);
        }
    }

    public boolean unregisterSchedule(String scheduleId) {
        log.info("Unregistering schedule with id" + scheduleId);
        try {
            return scheduler.deleteJob(jobKeys.get(scheduleId));
        } catch (SchedulerException e) {
            log.error(String.format("failed to unschedule job with '%s': %s",
                    scheduleId, e.getMessage()), e);
        }
        return false;
    }

    @Override
    public boolean unregisterSchedule(Schedule schedule) {
        return unregisterSchedule(schedule.getId());
    }

}
