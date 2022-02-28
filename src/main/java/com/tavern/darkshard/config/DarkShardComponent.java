package com.tavern.darkshard.config;

import dagger.Component;
import io.grpc.Server;

import javax.inject.Singleton;

@Singleton // Needed since our module provides scoped bindings (i.e. the module has an @Singleton)
@Component(modules = DarkShardModule.class)
public interface DarkShardComponent {


    Server darkShardServer();
}
