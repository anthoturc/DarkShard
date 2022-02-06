package com.tavern.darkshard.marshal;

import com.tavern.darkshard.model.CodeExecutionJobInput;
import com.tavern.darkshard.model.ImmutableCodeExecutionJobInput;
import com.tavern.darkshard.model.JobStatus;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.tavern.darkshard.dal.constants.CodeExecutionDaoConstants.FILE_DATA;
import static com.tavern.darkshard.dal.constants.CodeExecutionDaoConstants.JOB_ID;
import static com.tavern.darkshard.dal.constants.CodeExecutionDaoConstants.JOB_STATUS;

public class MongoCodeExecDataMarshallerTests {

    private static final String TEST_JOB_ID = "";
    private static final String TEST_RAW_PROGRAM_CODE =
            "public class Solution { public String sayHello() { return \"Hi!\"; } }";
    private static final JobStatus TEST_JOB_STATUS = JobStatus.NOT_STARTED;

    private MongoCodeExecDataMarshaller sut;

    @BeforeEach
    public void beforeEachTest() {
        sut = new MongoCodeExecDataMarshaller();
    }

    @Test
    public void WHEN_makeMongoDocFromCodeExecInput_THEN_succeed() {
        final CodeExecutionJobInput testInput = ImmutableCodeExecutionJobInput.builder()
                .jobId(TEST_JOB_ID)
                .rawProgramCode(TEST_RAW_PROGRAM_CODE)
                .jobStatus(TEST_JOB_STATUS)
                .build();

        final Document expectedDocument = new Document()
                .append(JOB_ID, TEST_JOB_ID)
                .append(FILE_DATA, TEST_RAW_PROGRAM_CODE)
                .append(JOB_STATUS, TEST_JOB_STATUS.toString());

        Assertions.assertEquals(expectedDocument, sut.makeMongoDocFromCodeExecInput(testInput));
    }
}
