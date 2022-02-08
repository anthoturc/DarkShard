package com.tavern.darkshard.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.tavern.darkshard.dal.CodeExecutionJobDao;
import com.tavern.darkshard.dal.impl.MongoCodeExecutionJobDao;
import com.tavern.darkshard.marshal.MongoCodeExecDataMarshaller;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.tavern.darkshard.dal.constants.CodeExecutionDaoConstants.CODE_EXECUTION_JOB_QUEUE_NAME;

@Module
public class DarkShardModule {

    @Provides
    public Channel provideRabbitMqChannel(final Connection rabbitMqConnection) {
        try {
            final Channel channel = rabbitMqConnection.createChannel();
            channel.queueDeclare( // Idempotent operation is executed in case the queue doesn't exist
                    CODE_EXECUTION_JOB_QUEUE_NAME, // Name of the queue
                    false, // Queue will not survive a restart
                    false, // Queue does not exclusively serve this connection
                    false, // Queue will not be deleted when no longer in use
                    null // No other properties/config for the queue
            );
            return channel;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to create channel/queue", ex);
        }
    }

    @Provides
    @Singleton // Connections are expensive to setup so we should only have 1 for this process.
    public Connection provideRabbitMqConnection() {
        final ConnectionFactory connectionFactory = new  ConnectionFactory();
        try {
            return connectionFactory.newConnection();
        } catch (TimeoutException | IOException ex) {
            throw new RuntimeException("Failed to create connection", ex);
        }
    }

    @Provides
    public CodeExecutionJobDao provideMongoCodeExecutionJobDao(final MongoDatabase mongo,
                                                               final MongoCodeExecDataMarshaller marshaller) {
        return new MongoCodeExecutionJobDao(mongo, marshaller);
    }

    @Provides
    @Singleton
    public MongoDatabase provideMongoDataBase() {
        final ConnectionString connectionString = new ConnectionString("mongodb+srv://admin:<password>@cluster0.kejc3.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
        final MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase("test");
    }
}
