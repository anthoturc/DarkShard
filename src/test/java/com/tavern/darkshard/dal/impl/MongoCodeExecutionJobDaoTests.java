package com.tavern.darkshard.dal.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.tavern.darkshard.exception.ResourceNotFoundException;
import com.tavern.darkshard.marshal.MongoCodeExecDataMarshaller;
import com.tavern.darkshard.model.CodeExecutionJobInput;
import com.tavern.darkshard.model.ImmutableCodeExecutionJobInput;
import com.tavern.darkshard.model.JobStatus;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static com.tavern.darkshard.dal.constants.CodeExecutionDaoConstants.CODE_EXECUTION_COLLECTION_NAME;
import static com.tavern.darkshard.dal.constants.CodeExecutionDaoConstants.JOB_ID;
import static com.tavern.darkshard.dal.constants.CodeExecutionDaoConstants.JOB_STATUS;

public class MongoCodeExecutionJobDaoTests {

    private final static String TEST_JOB_ID = "random-id";
    private final static String TEST_RAW_PROGRAM_CODE = "print('hello')";
    private final static JobStatus TEST_JOB_STATUS = JobStatus.NOT_STARTED;

    private final static CodeExecutionJobInput TEST_CODE_EXEC_JOB_INPUT = ImmutableCodeExecutionJobInput.builder()
            .jobId(TEST_JOB_ID)
            .rawProgramCode(TEST_RAW_PROGRAM_CODE)
            .jobStatus(TEST_JOB_STATUS)
            .build();

    private final static Document TEST_CODE_EXEC_JOB_INPUT_MONGO_DOC = new Document()
            .append("_id", TEST_JOB_ID)
            .append("file_data", TEST_RAW_PROGRAM_CODE)
            .append("status", TEST_JOB_STATUS.toString());

    private MongoCodeExecutionJobDao sut;
    private MongoDatabase db;
    private MongoCodeExecDataMarshaller marshaller;
    private MongoCollection<Document> collection;

    @BeforeEach
    public void beforeEachTest() {
        db = Mockito.mock(MongoDatabase.class);
        collection = (MongoCollection<Document>) Mockito.mock(MongoCollection.class);
        Mockito.when(db.getCollection(CODE_EXECUTION_COLLECTION_NAME))
                .thenReturn(collection);
        marshaller = new MongoCodeExecDataMarshaller();
        sut = new MongoCodeExecutionJobDao(db, marshaller);
    }

    @AfterEach
    public void afterEachTest() {
        Mockito.verifyNoMoreInteractions(db, collection);
    }

    @Test
    public void WHEN_putCodeExecutionJob_THEN_success() {
        sut.putCodeExecutionJob(TEST_CODE_EXEC_JOB_INPUT);

        Mockito.verify(collection, Mockito.times(1))
                .insertOne(TEST_CODE_EXEC_JOB_INPUT_MONGO_DOC);
        Mockito.verify(db, Mockito.times(1))
                .getCollection(CODE_EXECUTION_COLLECTION_NAME);
    }

    @Test
    public void WHEN_getCodeExecutionJobOutput_THEN_unsupported() {
        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> sut.getCodeExecutionJobOutput(TEST_JOB_ID)
        );
    }

    @Test
    public void WHEN_getCodeExecutionJobMetadata_WITH_existingJob_THEN_success() {

        final Document primaryKey = marshaller.makeMongoDocPrimaryKey(TEST_JOB_ID);
        final Document mockDocument = new Document()
                .append(JOB_ID, TEST_JOB_ID)
                .append(JOB_STATUS, TEST_JOB_STATUS.toString());

        FindIterable<Document> mockFindIterable = Mockito.mock(FindIterable.class);
        Mockito.when(mockFindIterable.first())
                .thenReturn(mockDocument);
        Mockito.when(collection.find(primaryKey))
                .thenReturn(mockFindIterable);

        Assertions.assertEquals(
                Optional.of(marshaller.makeCodeExecJobMetadata(mockDocument)),
                sut.getCodeExecutionJobMetadata(TEST_JOB_ID)
        );

        Mockito.verify(db, Mockito.times(1))
                .getCollection(CODE_EXECUTION_COLLECTION_NAME);
        Mockito.verify(collection, Mockito.times(1))
                .find(marshaller.makeMongoDocPrimaryKey(TEST_JOB_ID));
        Mockito.verify(mockFindIterable, Mockito.times(1))
                .first();
    }

    @Test
    public void WHEN_getCodeExecutionJobMetadata_WITH_nonExistingJob_THEN_success() {

        final Document primaryKey = marshaller.makeMongoDocPrimaryKey(TEST_JOB_ID);
        FindIterable<Document> mockFindIterable = Mockito.mock(FindIterable.class);
        Mockito.when(mockFindIterable.first())
                .thenReturn(null);
        Mockito.when(collection.find(primaryKey))
                .thenReturn(mockFindIterable);

        Assertions.assertEquals(
                Optional.empty(),
                sut.getCodeExecutionJobMetadata(TEST_JOB_ID)
        );

        Mockito.verify(collection, Mockito.times(1))
                .find(primaryKey);
        Mockito.verify(mockFindIterable, Mockito.times(1))
                .first();
        Mockito.verify(db, Mockito.times(1))
                .getCollection(CODE_EXECUTION_COLLECTION_NAME);
    }

    @Test
    public void WHEN_deleteCodeExecutionJob_WITH_existingJob_THEN_success() {
        final DeleteResult mockResult = Mockito.mock(DeleteResult.class);
        Mockito.when(mockResult.getDeletedCount())
                .thenReturn(1L);
        Mockito.when(collection.deleteOne(marshaller.makeMongoDocPrimaryKey(TEST_JOB_ID)))
                .thenReturn(mockResult);

        sut.deleteCodeExecutionJob(TEST_JOB_ID);

        Mockito.verify(db, Mockito.times(1))
                .getCollection(CODE_EXECUTION_COLLECTION_NAME);
        Mockito.verify(collection, Mockito.times(1))
                .deleteOne(marshaller.makeMongoDocPrimaryKey(TEST_JOB_ID));
    }

    @Test
    public void WHEN_deleteCodeExecutionJob_WITH_nonExistingJob_THEN_success() {
        final DeleteResult mockResult = Mockito.mock(DeleteResult.class);
        Mockito.when(mockResult.getDeletedCount())
                .thenReturn(0L);
        Mockito.when(collection.deleteOne(marshaller.makeMongoDocPrimaryKey(TEST_JOB_ID)))
                .thenReturn(mockResult);

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> sut.deleteCodeExecutionJob(TEST_JOB_ID)
        );

        Mockito.verify(db, Mockito.times(1))
                .getCollection(CODE_EXECUTION_COLLECTION_NAME);
        Mockito.verify(collection, Mockito.times(1))
                .deleteOne(marshaller.makeMongoDocPrimaryKey(TEST_JOB_ID));
    }

}
