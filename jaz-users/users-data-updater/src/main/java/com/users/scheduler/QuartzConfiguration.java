package com.users.scheduler;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfiguration {
    @Bean
    public JobDetail updaterJobDetail() {
        return JobBuilder.newJob(UpdaterJob.class)
                .withIdentity("updaterJob")
                .storeDurably()
                .build();
    }

    // Definiowanie wyzwalacza (Trigger)
    @Bean
    public Trigger updaterJobTrigger(JobDetail sampleJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(sampleJobDetail)
                .withIdentity("updaterTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(10)
                        .repeatForever())
                .build();
    }

//    Przykład wyrażenia cron:
//    0/15 * * * * ? – wykonuj co 15 sekund.
//    0 0/5 * * * ? – wykonuj co 5 minut.
//    0 0 12 * * ? – wykonuj codziennie o 12:00.
    @Bean
    public Trigger cronJobTrigger(JobDetail sampleJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(sampleJobDetail)
                .withIdentity("cronTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/15 * * * * ?"))
                .build();
    }
}
