package com.tavern.darkshard.dal.impl;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.tavern.darkshard.dal.CodeExecutionJobDao;
import com.tavern.darkshard.exception.ResourceNotFoundException;
import com.tavern.darkshard.marshal.MongoCodeExecDataMarshaller;
import com.tavern.darkshard.model.CodeExecutionJobInput;
import com.tavern.darkshard.model.CodeExecutionJobMetadata;
import com.tavern.darkshard.model.CodeExecutionJobOutput;
import org.bson.Document;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

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
    public Optional<CodeExecutionJobMetadata> getCodeExecutionJobMetadata(final String jobId) {

        final Document codeExecDoc = codeExecDb.getCollection(CODE_EXECUTION_COLLECTION_NAME)
                .find(marshaller.makeMongoDocPrimaryKey(jobId))
                .first();

        if (Objects.isNull(codeExecDoc)) {
            return Optional.empty();
        }

        return Optional.of(marshaller.makeCodeExecJobMetadata(codeExecDoc));
    }

    @Override
    public Optional<CodeExecutionJobOutput> getCodeExecutionJobOutput(final String jobId) {
        throw new UnsupportedOperationException("GetCodeExecutionJobOutput is not available yet.");
    }

    @Override
    public void deleteCodeExecutionJob(final String jobId) {
        final DeleteResult result = codeExecDb.getCollection(CODE_EXECUTION_COLLECTION_NAME)
                .deleteOne(marshaller.makeMongoDocPrimaryKey(jobId));

        if (result.getDeletedCount() == 0) {
            throw new ResourceNotFoundException(String.format("Code execution job (%s) does not exist", jobId));
        }
    }
}
