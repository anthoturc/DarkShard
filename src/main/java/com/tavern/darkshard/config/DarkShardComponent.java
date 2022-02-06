package com.tavern.darkshard.config;

import com.tavern.darkshard.service.DarkShardServiceImpl;
import dagger.Component;

import javax.inject.Singleton;

@Singleton // Needed since our module provides scoped bindings (i.e. the module has an @Singleton)
@Component(modules = DarkShardModule.class)
public interface DarkShardComponent {

    DarkShardServiceImpl darkShardServiceImpl();
}
