package com.tavern.darkshard.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.tavern.darkshard.dal.CodeExecutionJobDao;
import com.tavern.darkshard.dal.impl.MongoCodeExecutionJobDao;
import com.tavern.darkshard.dal.marshal.MongoCodeExecDataMarshaller;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class DarkShardModule {

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
