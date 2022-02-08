package com.tavern.darkshard.dal.impl;

import com.rabbitmq.client.Channel;
import com.tavern.darkshard.dal.CodeExecutionJobQueueDao;

import javax.inject.Inject;

public class RabbitMqCodeExecutionJobQueueDao implements CodeExecutionJobQueueDao {

    private final Channel rabbitMqChannel;

    @Inject
    public RabbitMqCodeExecutionJobQueueDao(final Channel rabbitMqConnection) {
        this.rabbitMqChannel = rabbitMqConnection;
    }

    @Override
    public void pushCodeExecutionJobToQueue() {
    }
}
