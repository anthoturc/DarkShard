package com.tavern.darkshard.dal.impl;

import com.rabbitmq.client.Channel;
import com.tavern.darkshard.dal.CodeExecutionJobQueueDao;
import com.tavern.darkshard.exception.UnsupportedOperationException;
import com.tavern.darkshard.model.CodeExecutionJobInput;

import javax.inject.Inject;

public class RabbitMqCodeExecutionJobQueueDao implements CodeExecutionJobQueueDao {

    private final Channel rabbitMqChannel;

    @Inject
    public RabbitMqCodeExecutionJobQueueDao(final Channel rabbitMqConnection) {
        this.rabbitMqChannel = rabbitMqConnection;
    }

    @Override
    public void pushCodeExecutionJobToQueue(final CodeExecutionJobInput input) {
        throw new UnsupportedOperationException("TaskQueue is not available yet");
    }
}
