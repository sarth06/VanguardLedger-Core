package com.vanguard.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {

        System.out.println("Booting VanguardLedger Concurrency Engine...");

        Account acc1 = new Account("ACC-001", 5000);
        Account acc2 = new Account("ACC-002", 5000);

        WALEngine wal = new WALEngine();
        TransactionEngine engine = new TransactionEngine(wal);

        // System total MUST remain exactly $10,000 at all times.
        int totalExpectedMoney = acc1.getBalance().get() + acc2.getBalance().get();
        int numberOfTransactions = 10000;

        long startTime = System.currentTimeMillis();

        // Unleash 10,000 Virtual Threads concurrently
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < numberOfTransactions; i++) {
                executor.submit(() -> {
                    // Everyone tries to take $1 from acc1 and give it to acc2 simultaneously
                    engine.transfer(acc1, acc2, 1);
                });
            }
        } // The try-with-resources block automatically waits for ALL 10,000 threads to finish here.

        // Flush all transaction logs to the hard drive
        wal.flushToDisk();
        long endTime = System.currentTimeMillis();

        // The Mathematical Audit
        int actualMoney = acc1.getBalance().get() + acc2.getBalance().get();
        System.out.println("--------------------------------------------------");
        System.out.println("Executed " + numberOfTransactions + " transactions in " + (endTime - startTime) + "ms.");
        System.out.println("System Initial Funds: $" + totalExpectedMoney);
        System.out.println("System Final Funds:   $" + actualMoney);

        if (actualMoney == totalExpectedMoney) {
            System.out.println("[SUCCESS] Zero Race Conditions Detected. CAS Loop is mathematically perfect.");
        } else {
            System.out.println("[CRITICAL ERROR] Race Condition Detected. Money was lost or duplicated.");
        }
    }
}