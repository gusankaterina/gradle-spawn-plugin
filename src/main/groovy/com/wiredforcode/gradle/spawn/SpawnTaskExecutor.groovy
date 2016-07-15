package com.wiredforcode.gradle.spawn

import org.gradle.api.Task
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpawnTaskExecutor {

    static Logger logger = Logging.getLogger(Task.class)
    ExecutorService executor = Executors.newFixedThreadPool(1)

    Process process
    String ready
    String command
    File stdOutDir
    boolean isReady

    public SpawnTaskExecutor(Process process, String ready, String command, File stdOutDir){
        this.process = process
        this.ready = ready
        this.command = command
        this.stdOutDir = stdOutDir
    }

    void executeSpawnTask() {
        executor.execute(new SpawnRunnable())
    }

    class SpawnRunnable implements Runnable {

        @Override
        void run() {
            def line
            def reader = new BufferedReader(new InputStreamReader(process.getInputStream()))
            FileUtils.deleteCompletely(stdOutDir)
            while ((line = reader.readLine()) != null && !isReady) {
                logger.quiet line
                FileUtils.writeToFile(stdOutDir, line, true)
                if (line.contains(ready)) {
                    logger.quiet "$command is ready."
                    isReady = true
                    synchronized (this) {
                        this.notifyAll()
                    }
                }
            }
        }
    }
}