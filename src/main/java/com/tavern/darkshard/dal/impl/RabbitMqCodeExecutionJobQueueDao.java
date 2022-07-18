package com.tavern.darkshard.dal.impl;

import com.rabbitmq.client.Channel;
import com.tavern.darkshard.dal.CodeExecutionJobQueueDao;
import com.tavern.darkshard.model.CodeExecutionJobInput;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;

import static com.tavern.darkshard.dal.constants.CodeExecutionDaoConstants.CODE_EXECUTION_JOB_QUEUE_NAME;

public class RabbitMqCodeExecutionJobQueueDao implements CodeExecutionJobQueueDao {

    private final Channel rabbitMqChannel;

    @Inject
    public RabbitMqCodeExecutionJobQueueDao(final Channel rabbitMqConnection) {
        this.rabbitMqChannel = rabbitMqConnection;
    }

    @Override
    public void pushCodeExecutionJobToQueue(final CodeExecutionJobInput input) {
        try {
            rabbitMqChannel.basicPublish(
                    "",
                    CODE_EXECUTION_JOB_QUEUE_NAME,
                    null,
                    input.rawProgramCode().getBytes(StandardCharsets.UTF_8)
            );
        } catch (final Exception ex) {
            throw new RuntimeException("Unable to publish message.", ex);
        }
    }
}
