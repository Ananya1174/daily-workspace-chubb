package com.chubb.threading;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Application started");

        Worker worker = new Worker();

        int cores = Runtime.getRuntime().availableProcessors();
        logger.debug("Number of cores: " + cores);

        ExecutorService service = Executors.newFixedThreadPool(cores);

        service.execute(worker);
        service.execute(worker);
        service.execute(worker);

        service.shutdown();
        logger.info("Application ended");
    }
}