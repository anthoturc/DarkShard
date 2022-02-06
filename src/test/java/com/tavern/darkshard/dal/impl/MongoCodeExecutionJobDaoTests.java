package com.tavern.darkshard.dal.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.tavern.darkshard.dal.marshal.MongoCodeExecDataMarshaller;
import com.tavern.darkshard.model.CodeExecutionJobInput;
import com.tavern.darkshard.model.ImmutableCodeExecutionJobInput;
import com.tavern.darkshard.model.JobStatus;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.tavern.darkshard.dal.constants.CodeExecutionDaoConstants.CODE_EXECUTION_COLLECTION_NAME;

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
    private MongoCollection<Document> collection;

    @BeforeEach
    public void beforeEachTest() {
        db = Mockito.mock(MongoDatabase.class);
        collection = (MongoCollection<Document>) Mockito.mock(MongoCollection.class);
        Mockito.when(db.getCollection(CODE_EXECUTION_COLLECTION_NAME))
                .thenReturn(collection);
        sut = new MongoCodeExecutionJobDao(db, new MongoCodeExecDataMarshaller());
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
    public void WHEN_deleteCodeExecutionJob_THEN_unsupported() {
        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> sut.deleteCodeExecutionJob()
        );
    }

    @Test
    public void WHEN_getCodeExecutionJob_THEN_unsupported() {
        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> sut.getCodeExecutionJobStatus()
        );
    }

    @Test
    public void WHEN_getCodeExecutionJobOutput_THEN_unsupported() {
        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> sut.getCodeExecutionJobOutput()
        );
    }

}
