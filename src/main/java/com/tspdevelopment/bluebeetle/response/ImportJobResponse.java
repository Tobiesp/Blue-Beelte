package com.tspdevelopment.bluebeetle.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.UUID;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ImportJobResponse extends BaseResponse {

	private UUID jobId;

    public ImportJobResponse(UUID jobId, RequestStatus status) {
        this.jobId = jobId;
        this.setRequestStatus(status);
    }

        @Override
    public int hashCode() {
        return jobId.hashCode();
    }

        @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(obj.getClass() == UUID.class) {
            return jobId.equals(obj);
        }
        return false;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder
                        .append("ImportJobResponse {jobId=")
                        .append(this.jobId)
                        .append(", requestStatus=")
                        .append(this.getRequestStatus().name())
                        .append("}");
		return builder.toString();
	}

}
