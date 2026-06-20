package com.vanguard.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WALEngine {

    // A thread-safe queue. Thousands of threads can add data here simultaneously without locking.
    private final ConcurrentLinkedQueue<String> logQueue = new ConcurrentLinkedQueue<>();
    private final Path logPath = Paths.get("ledger.log");

    public void logTransaction(String record) {
        logQueue.add(record + "\n");
    }

    public void flushToDisk() {
        // Appends to the ledger using OS-level File I/O
        try (FileChannel channel = FileChannel.open(logPath,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE)) {

            String record;
            // Drain the queue to the hard drive
            while ((record = logQueue.poll()) != null) {
                channel.write(ByteBuffer.wrap(record.getBytes()));
            }
        } catch (IOException e) {
            System.out.println("[CRITICAL] WAL Flush Failed: " + e.getMessage());
        }
    }
}