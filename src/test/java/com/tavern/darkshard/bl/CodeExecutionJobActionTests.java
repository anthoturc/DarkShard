package com.tavern.darkshard.bl;

import com.tavern.darkshard.dal.CodeExecutionJobDao;
import com.tavern.darkshard.model.CodeExecutionJobInput;
import com.tavern.darkshard.model.ImmutableCodeExecutionJobInput;
import com.tavern.darkshard.model.JobStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class CodeExecutionJobActionTests {


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
                .jobId("random-id")
                .jobStatus(JobStatus.NOT_STARTED)
                .rawProgramCode("print('hello')")
                .build();
        Mockito.doNothing().when(dao).putCodeExecutionJob(codeExecutionJob);

        sut.submitCodeExecutionJob(codeExecutionJob);

        Mockito.verify(dao, Mockito.times(1))
                .putCodeExecutionJob(codeExecutionJob);
    }

    @Test
    public void WHEN_getCodeExecutionJobStatus_THEN_success() {
        Assertions.assertEquals(
                Optional.empty(),
                sut.getCodeExecutionJobStatus("random-id")
        );
    }

    @Test
    public void WHEN_getCodeExecutionJobOutput_THEN_success() {
        Assertions.assertEquals(
                Optional.empty(),
                sut.getCodeExecutionJobOutput("random-id")
        );
    }

    @Test
    public void WHEN_deleteCodeExecutionJob_THEN_success() {
        sut.deleteCodeExecutionJob("random-id");
    }

}
