package com.tspdevelopment.bluebeetle.services;

import com.tspdevelopment.bluebeetle.csv.CSVPreference;
import com.tspdevelopment.bluebeetle.csv.CSVReader;
import com.tspdevelopment.bluebeetle.data.model.ImportJob;
import com.tspdevelopment.bluebeetle.data.repository.ImportJobRepository;
import com.tspdevelopment.bluebeetle.response.ImportJobResponse;
import com.tspdevelopment.bluebeetle.response.RequestStatus;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ImportJobService extends BaseJobService {
	private static final Logger logger = LoggerFactory.getLogger(ImportJobService.class);
        
        @Autowired
        private ImportJobRepository repository;
        @Autowired
        private CSVImportService importService;
        
	
	@Async("asyncTaskExecutor")
	public <K> CompletableFuture<ImportJobResponse> postJobWithFile(UUID jobId, Class<K> clazz) {
		logger.info("Received request with job-id {}", jobId.toString());

		CompletableFuture<ImportJobResponse> task =  new CompletableFuture<>();
		
		Optional<ImportJob> optJob = repository.findById(jobId);
                if(!optJob.isPresent()) {
                    task.complete(new ImportJobResponse(jobId, RequestStatus.ERROR));
                    logger.info("Job {} not found!", jobId.toString());
                    return task;
                }
                ImportJob job = optJob.get();
                
                List<K> results = new ArrayList<>();
                
                InputStream jobStream = new ByteArrayInputStream(job.getContent());
                Reader reader = null;
                try {
                    reader = new InputStreamReader(jobStream,"UTF-8");
                    CSVReader csvReader = new CSVReader(reader, CSVPreference.STANDARD_PREFERENCE);
                    while(csvReader.hasRow()) {
                        results.add(csvReader.readItemRow(clazz));
                    }
                } catch (Exception ex) {
                    if(reader != null)
                        try {
                            reader.close();
                    } catch (IOException ex1) {
                        logger.error("Unable to close CSV file: " + job.getFileName(), ex);
                    }
                    logger.error("Unable to read CSV file: " + job.getFileName(), ex);
                    task.complete(new ImportJobResponse(jobId, RequestStatus.ERROR));
                    return task;
                }
                
                try {
                    importService.importItems(results, clazz);
                } catch (Exception ex) {
                    logger.error("Unable to process CSV file: " + job.getFileName(), ex);
                    task.complete(new ImportJobResponse(jobId, RequestStatus.ERROR));
                    return task;
                }
                
		task.complete(new ImportJobResponse(jobId, RequestStatus.COMPLETE));
		logger.info("Completed processing the request.");
		return task;
	}
	
	public ImportJobResponse getJobStatus(UUID jobId) throws Throwable {
		CompletableFuture<ImportJobResponse> completableFuture = fetchJobElseThrowException(jobId);

		if (!completableFuture.isDone()) {
			return new ImportJobResponse(jobId, RequestStatus.IN_PROGRESS);
		}

		Throwable[] errors = new Throwable[1];
		ImportJobResponse[] importJobResponses = new ImportJobResponse[1];
		completableFuture.whenComplete((response, ex) -> {
			if (ex != null) {
				errors[0] = ex.getCause();
			} else {
				response.setRequestStatus(RequestStatus.COMPLETE);
				importJobResponses[0] = response;
			}
		});

		if (errors[0] != null) {
			throw errors[0];
		}

		return importJobResponses[0];
	}

	public ImportJobResponse deleteJobAndAssociatedData(UUID jobId) {
		CompletableFuture<ImportJobResponse> completableFuture = fetchJob(jobId);

		if (null == completableFuture) {
		    Optional<ImportJob> job = repository.findById(jobId);
                    if(job.isPresent()) {
                        repository.delete(job.get());
                        return new ImportJobResponse(jobId, RequestStatus.DELETED);
                    }
			throw new NullPointerException(JOB_WITH_SUPPLIED_JOB_ID_NOT_FOUND);
		}
		
		if (!completableFuture.isDone()) {
			return new ImportJobResponse(jobId, RequestStatus.IN_PROGRESS);
		}

		completableFuture.whenComplete((response, ex) -> {
			if (ex != null) {
				logger.error("Job failed with exception.", ex);
			}

			Optional<ImportJob> job = repository.findById(jobId);
                        if(job.isPresent()) {
                            repository.delete(job.get());
                        }

			asyncJobsManager.removeJob(jobId);
		});

		return new ImportJobResponse(jobId, RequestStatus.DELETED);
	}
        
	public ImportJob addJobToDB(ImportJob job) {
		return this.repository.saveAndFlush(job);
	}

	public void deleteCompletedAsyncJobs() {
		List<UUID> allJobs = this.asyncJobsManager.getAllJobs();
		List<ImportJob> dbJobs = this.repository.findAll();
		if(dbJobs.isEmpty()) {
			return;
		}
		for(ImportJob job : dbJobs) {
			if(allJobs.contains(job.getId())) {
				try {
					if(this.getJobStatus(job.getId()).getRequestStatus() == RequestStatus.COMPLETE) {
						this.deleteJobAndAssociatedData(job.getId());
					}
				} catch (Throwable t) {
					logger.error("Failed to remove completed job.", t);
				}
			} else {
				this.repository.delete(job);
			}
		}
	}
}