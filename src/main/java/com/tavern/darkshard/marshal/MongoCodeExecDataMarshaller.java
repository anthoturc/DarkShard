package com.tavern.darkshard.marshal;

import com.tavern.darkshard.model.CodeExecutionJobInput;
import com.tavern.darkshard.model.CodeExecutionJobMetadata;
import com.tavern.darkshard.model.ImmutableCodeExecutionJobMetadata;
import com.tavern.darkshard.model.JobStatus;
import org.bson.Document;

import javax.inject.Inject;

import static com.tavern.darkshard.dal.constants.CodeExecutionDaoConstants.FILE_DATA;
import static com.tavern.darkshard.dal.constants.CodeExecutionDaoConstants.JOB_ID;
import static com.tavern.darkshard.dal.constants.CodeExecutionDaoConstants.JOB_STATUS;

public class MongoCodeExecDataMarshaller {

    @Inject
    public MongoCodeExecDataMarshaller() {
    }

    public Document makeMongoDocFromCodeExecInput(final CodeExecutionJobInput job) {
        return new Document()
                .append(JOB_ID, job.jobId())
                .append(FILE_DATA, job.rawProgramCode())
                .append(JOB_STATUS, job.jobStatus().toString());
    }

    public Document makeMongoDocPrimaryKey(final String jobId) {
        return new Document()
                .append(JOB_ID, jobId);
    }

    public CodeExecutionJobMetadata makeCodeExecJobMetadata(final Document codeExecDoc) {
        return ImmutableCodeExecutionJobMetadata.builder()
                .jobId(codeExecDoc.getString(JOB_ID))
                .status(JobStatus.valueOf(codeExecDoc.getString(JOB_STATUS)))
                .build();
    }
}
