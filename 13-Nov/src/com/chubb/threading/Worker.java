package com.chubb.threading;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Worker extends Thread {
    private static final Logger logger = LogManager.getLogger(Worker.class);

    @Override
    public void run() {
        logger.trace("Worker started on thread: " + Thread.currentThread().getName());

        try {
            Thread.sleep(1000); // simulate work
            logger.info("Worker completed task on thread: " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            logger.error("Worker interrupted", e);
        }
    }
}