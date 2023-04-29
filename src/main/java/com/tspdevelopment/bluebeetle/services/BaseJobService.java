/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.bluebeetle.services;

import com.tspdevelopment.bluebeetle.response.ImportJobResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseJobService {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	protected static final String JOB_WITH_SUPPLIED_JOB_ID_NOT_FOUND = "Job with supplied job-id not found!";

	@Autowired
	protected AsyncJobsManager asyncJobsManager;

	/**
	 * 
	 * @param jobId
	 * @return the CompletableFuture associated with the jobId. This method will
	 *         throw an exception if the job does not exist.
	 */
	public CompletableFuture<ImportJobResponse> fetchJobElseThrowException(UUID jobId) {
		CompletableFuture<ImportJobResponse> job = fetchJob(jobId);
		if(null == job) {
			LOGGER.error("Job-id {} not found.", jobId);
			throw new NullPointerException(JOB_WITH_SUPPLIED_JOB_ID_NOT_FOUND);
		}
		return job;
	}

	/**
	 * 
	 * @param jobId
	 * @return the CompletableFuture associated with the jobId. This method will
	 *         throw an exception if the job does not exist and throwErrorIfNotFound
	 *         is supplied as true.
	 */
	public CompletableFuture<ImportJobResponse> fetchJob(UUID jobId) {
		CompletableFuture<ImportJobResponse> completableFuture = (CompletableFuture<ImportJobResponse>) asyncJobsManager.getJob(jobId);
		return completableFuture;
	}
}
