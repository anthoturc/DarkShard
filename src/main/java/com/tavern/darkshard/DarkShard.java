package com.tavern.darkshard;

import com.tavern.darkshard.config.DaggerDarkShardComponent;
import com.tavern.darkshard.config.DarkShardComponent;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DarkShard {

    private static final Logger LOG = LogManager.getLogger(DarkShard.class);

    public static void main(String[] args) throws Exception {
        LOG.info("Staring darkshard server.");

        DarkShardComponent component = DaggerDarkShardComponent.create();

        Server server = ServerBuilder.forPort(50051)
                .addService(component.darkShardServiceImpl())
                .build();

        server.start();

        LOG.info("Successfully started on port 50051.");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Shutting down server.");
            server.shutdown();
        }));

        server.awaitTermination();
    }
}
