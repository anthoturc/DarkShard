package com.tavern.darkshard.dal.impl;

import com.rabbitmq.client.Channel;
import com.tavern.darkshard.exception.UnsupportedOperationException;
import com.tavern.darkshard.model.CodeExecutionJobInput;
import com.tavern.darkshard.model.ImmutableCodeExecutionJobInput;
import com.tavern.darkshard.model.JobStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RabbitMqCodeExecutionJobQueueDaoTests {

    private static final String TEST_JOB_ID = "job-id";
    private static final String TEST_RAW_PROGRAM_CODE = "print('hello')";
    private static final JobStatus TEST_JOB_STATUS = JobStatus.NOT_STARTED;

    private RabbitMqCodeExecutionJobQueueDao sut;

    @BeforeEach
    public void beforeEachTest() {
        sut = new RabbitMqCodeExecutionJobQueueDao(Mockito.mock(Channel.class));
    }

    @Test
    public void WHEN_pushCodeExecutionJobToQueue_THEN_unsupportedOperationException() {
        final CodeExecutionJobInput input = ImmutableCodeExecutionJobInput.builder()
                .jobId(TEST_JOB_ID)
                .rawProgramCode(TEST_RAW_PROGRAM_CODE)
                .jobStatus(TEST_JOB_STATUS)
                .build();

        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> sut.pushCodeExecutionJobToQueue(input)
        );
    }
}
