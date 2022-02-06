package com.tavern.darkshard.dal.impl;

import com.mongodb.client.MongoDatabase;
import com.tavern.darkshard.dal.CodeExecutionJobDao;
import com.tavern.darkshard.dal.marshal.MongoCodeExecDataMarshaller;
import com.tavern.darkshard.model.CodeExecutionJobInput;
import com.tavern.darkshard.model.CodeExecutionJobOutput;
import com.tavern.darkshard.model.JobStatus;

import javax.inject.Inject;

import static com.tavern.darkshard.dal.constants.CodeExecutionDaoConstants.CODE_EXECUTION_COLLECTION_NAME;

public class MongoCodeExecutionJobDao implements CodeExecutionJobDao {



    private final MongoDatabase codeExecDb;
    private final MongoCodeExecDataMarshaller marshaller;

    @Inject
    public MongoCodeExecutionJobDao(final MongoDatabase codeExecDb, final MongoCodeExecDataMarshaller marshaller) {
        this.codeExecDb = codeExecDb;
        this.marshaller = marshaller;
    }

    @Override
    public void putCodeExecutionJob(final CodeExecutionJobInput codeExecJobInput) {
        codeExecDb.getCollection(CODE_EXECUTION_COLLECTION_NAME)
                .insertOne(marshaller.makeMongoDocFromCodeExecInput(codeExecJobInput));
    }

    @Override
    public void deleteCodeExecutionJob() {
        throw new UnsupportedOperationException("DeleteCodeExecutionJob is not available yet.");
    }

    @Override
    public JobStatus getCodeExecutionJobStatus() {
        throw new UnsupportedOperationException("GetCodeExecutionJob is not available yet.");
    }

    @Override
    public CodeExecutionJobOutput getCodeExecutionJobOutput() {
        throw new UnsupportedOperationException("GetCodeExecutionJobOutput is not available yet.");
    }
}
