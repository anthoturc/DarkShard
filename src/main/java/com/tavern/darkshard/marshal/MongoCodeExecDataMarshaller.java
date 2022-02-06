package com.tavern.darkshard.marshal;

import com.tavern.darkshard.model.CodeExecutionJobInput;
import org.bson.Document;

import javax.inject.Inject;

import static com.tavern.darkshard.dal.constants.CodeExecutionDaoConstants.FILE_DATA;
import static com.tavern.darkshard.dal.constants.CodeExecutionDaoConstants.JOB_ID;
import static com.tavern.darkshard.dal.constants.CodeExecutionDaoConstants.JOB_STATUS;

public class MongoCodeExecDataMarshaller {

    @Inject
    public MongoCodeExecDataMarshaller() {
    }

    public Document makeMongoDocFromCodeExecInput(final CodeExecutionJobInput jobInput) {
        return new Document()
                .append(JOB_ID, jobInput.jobId())
                .append(FILE_DATA, jobInput.rawProgramCode())
                .append(JOB_STATUS, jobInput.jobStatus().toString());
    }
}
