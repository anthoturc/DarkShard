package com.tavern.darkshard.bl;

import com.tavern.darkshard.dal.CodeExecutionJobDao;
import com.tavern.darkshard.exception.ResourceNotFoundException;
import com.tavern.darkshard.exception.UnsupportedOperationException;
import com.tavern.darkshard.model.CodeExecutionJobInput;
import com.tavern.darkshard.model.CodeExecutionJobMetadata;
import com.tavern.darkshard.model.ImmutableCodeExecutionJobInput;
import com.tavern.darkshard.model.ImmutableCodeExecutionJobMetadata;
import com.tavern.darkshard.model.JobStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class CodeExecutionJobActionTests {

    private static final String TEST_JOB_ID = "job-id";


    private CodeExecutionJobDao dao;
    private CodeExecutionJobAction sut;


    @BeforeEach
    public void beforeEachTest() {
        dao = Mockito.mock(CodeExecutionJobDao.class);
        sut = new CodeExecutionJobAction(dao);
    }

    @AfterEach
    public void afterEachTest() {
        Mockito.verifyNoMoreInteractions(dao);
    }

    @Test
    public void WHEN_submitCodeExecutionJob_THEN_success() {
        final CodeExecutionJobInput codeExecutionJob = ImmutableCodeExecutionJobInput.builder()
                .jobId(TEST_JOB_ID)
                .jobStatus(JobStatus.NOT_STARTED)
                .rawProgramCode("print('hello')")
                .build();
        Mockito.doNothing().when(dao).putCodeExecutionJob(codeExecutionJob);

        sut.submitCodeExecutionJob(codeExecutionJob);

        Mockito.verify(dao, Mockito.times(1))
                .putCodeExecutionJob(codeExecutionJob);
    }

    @Test
    public void WHEN_getCodeExecutionJobStatus_WITH_nonExistingJob_THEN_resourceNotFoundException() {

        Mockito.when(dao.getCodeExecutionJobMetadata(TEST_JOB_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> sut.getCodeExecutionJobStatus(TEST_JOB_ID)
        );

        Mockito.verify(dao, Mockito.times(1))
                .getCodeExecutionJobMetadata(TEST_JOB_ID);
    }

    @Test
    public void WHEN_getCodeExecutionJobStatus_WITH_existingJob_THEN_success() {
        final CodeExecutionJobMetadata metadata = ImmutableCodeExecutionJobMetadata.builder()
                .jobId(TEST_JOB_ID)
                .status(JobStatus.DONE)
                .build();

        Mockito.when(dao.getCodeExecutionJobMetadata(TEST_JOB_ID))
                .thenReturn(Optional.of(metadata));

        Assertions.assertEquals(
                JobStatus.DONE,
                sut.getCodeExecutionJobStatus(TEST_JOB_ID)
        );

        Mockito.verify(dao, Mockito.times(1))
                .getCodeExecutionJobMetadata(TEST_JOB_ID);
    }

    @Test
    public void WHEN_getCodeExecutionJobOutput_THEN_unsupportedOperationException() {
        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> sut.getCodeExecutionJobOutput(TEST_JOB_ID)
        );
    }

    @Test
    public void WHEN_deleteCodeExecutionJob_THEN_unsupportedOperationException() {
        sut.deleteCodeExecutionJob(TEST_JOB_ID);

        Mockito.verify(dao, Mockito.times(1))
                .deleteCodeExecutionJob(TEST_JOB_ID);
    }

}
