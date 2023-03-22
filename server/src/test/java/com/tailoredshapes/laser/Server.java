package com.tailoredshapes.laser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.port;

public class Server {
    static boolean started = false;
    static Logger logger = LoggerFactory.getLogger(Server.class);

    public synchronized static boolean isStarted() {
        return started;
    }

    public synchronized static void start(MetaRepository repo) {
        var port = 6969;
        logger.info("Starting test server on port " + port);
        if (!isStarted()) {
            started = true;
            port(port);
            Router.route(repo);
        }
    }
}
