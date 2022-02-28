package com.tavern.darkshard.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.tavern.darkshard.dal.CodeExecutionJobDao;
import com.tavern.darkshard.dal.impl.MongoCodeExecutionJobDao;
import com.tavern.darkshard.interceptor.ExceptionInterceptor;
import com.tavern.darkshard.marshal.MongoCodeExecDataMarshaller;
import com.tavern.darkshard.service.DarkShardServiceImpl;
import dagger.Module;
import dagger.Provides;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

@Module
public class DarkShardModule {

    private static final String DEFAULT_PORT = "50051";

    private final String port;

    public DarkShardModule(final String port) {
        this.port = port;
    }

    public DarkShardModule() {
        this(DEFAULT_PORT);
    }

//    @Provides
//    public Channel provideRabbitMqChannel(final Connection rabbitMqConnection) {
//        try {
//            final Channel channel = rabbitMqConnection.createChannel();
//            channel.queueDeclare( // Idempotent operation is executed in case the queue doesn't exist
//                    CODE_EXECUTION_JOB_QUEUE_NAME, // Name of the queue
//                    false, // Queue will not survive a restart
//                    false, // Queue does not exclusively serve this connection
//                    false, // Queue will not be deleted when no longer in use
//                    null // No other properties/config for the queue
//            );
//            return channel;
//        } catch (IOException ex) {
//            throw new RuntimeException("Failed to create channel/queue", ex);
//        }
//    }

//    @Provides
//    @Singleton // Connections are expensive to setup so we should only have 1 for this process.
//    public Connection provideRabbitMqConnection() {
//        // Using the defaults here: https://www.rabbitmq.com/api-guide.html#connecting
//        // These are only valid for the local host
//        final ConnectionFactory connectionFactory = new ConnectionFactory();
//        try {
//            return connectionFactory.newConnection();
//        } catch (TimeoutException | IOException ex) {
//            throw new RuntimeException("Failed to create connection", ex);
//        }
//    }

    @Provides
    public CodeExecutionJobDao provideMongoCodeExecutionJobDao(final MongoDatabase mongo,
                                                               final MongoCodeExecDataMarshaller marshaller) {
        return new MongoCodeExecutionJobDao(mongo, marshaller);
    }

//    @Provides
//    public CodeExecutionJobQueueDao provideCodeExecutionJobQueueDao(final Channel rabbitMqChannel) {
//        return new RabbitMqCodeExecutionJobQueueDao(rabbitMqChannel);
//    }

    @Provides
    @Singleton
    public MongoDatabase provideMongoDataBase() {
        final ConnectionString connectionString =
                new ConnectionString("mongodb+srv://admin:<password>@darkshardcluster.kejc3.mongodb.net/<myFirstDb>?retryWrites=true&w=majority");
        final MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase("DarkShardDb");
    }

    @Provides
    @Singleton
    public Server provideDarkShardServer(final DarkShardServiceImpl darkShardService,
                                          final List<ServerInterceptor> interceptors) {
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(Integer.parseInt(port))
                .addService(darkShardService);

        interceptors.forEach(serverBuilder::intercept);

        return serverBuilder.build();
    }

    @Provides
    public List<ServerInterceptor> provideServiceInterceptors() {
        return Arrays.asList(new ExceptionInterceptor());
    }

}
