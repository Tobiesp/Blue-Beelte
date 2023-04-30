package com.tspdevelopment.bluebeetle.Schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.tspdevelopment.bluebeetle.services.ImportJobService;

@Component
public class ScheduledTasks {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

	@Autowired
	private ImportJobService importJobService;

	@Scheduled(fixedDelay = 5000)
	public void deleteCompletedAsyncJobs() {
		this.importJobService.deleteCompletedAsyncJobs();
	}
}