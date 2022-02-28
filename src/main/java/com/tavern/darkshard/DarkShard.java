package com.tavern.darkshard;

import com.tavern.darkshard.config.DaggerDarkShardComponent;
import com.tavern.darkshard.config.DarkShardComponent;
import com.tavern.darkshard.config.DarkShardModule;
import io.grpc.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DarkShard {

    private static final Logger LOG = LogManager.getLogger(DarkShard.class);

    public static void main(String[] args) throws Exception {
        LOG.info("Staring darkshard server.");

        final DarkShardComponent component = DaggerDarkShardComponent.builder()
                .darkShardModule(new DarkShardModule(System.getenv("PORT")))
                .build();

        final Server server = component.darkShardServer();

        server.start();

        LOG.info(String.format("Successfully started on port %s.", System.getenv("PORT")));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Shutting down server.");
            server.shutdown();
        }));

        server.awaitTermination();
    }
}
