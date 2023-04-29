/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tspdevelopment.bluebeetle.services;
import com.tspdevelopment.bluebeetle.response.BaseResponse;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

@Service
public class AsyncJobsManager {

	private final ConcurrentMap<UUID, CompletableFuture<? extends BaseResponse>> mapOfJobs;

	public AsyncJobsManager() {
		mapOfJobs = new ConcurrentHashMap<>();
	}

	public void putJob(UUID jobId, CompletableFuture<? extends BaseResponse> theJob) {
		mapOfJobs.put(jobId, theJob);
	}

	public CompletableFuture<? extends BaseResponse> getJob(UUID jobId) {
		return mapOfJobs.get(jobId);
	}

	public void removeJob(UUID jobId) {
		mapOfJobs.remove(jobId);
	}
}