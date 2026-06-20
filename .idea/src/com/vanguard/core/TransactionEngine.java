package com.vanguard.core;

public class TransactionEngine {

    private final WALEngine wal;

    public TransactionEngine(WALEngine wal) {
        this.wal = wal;
    }

    public boolean transfer(Account from, Account to, int amount) {
        // The Spin-Lock: Keeps retrying instantly without putting the thread to sleep
        while (true) {
            int currentBalance = from.getBalance().get();

            if (currentBalance < amount) {
                return false; // Insufficient funds
            }

            int targetBalance = currentBalance - amount;

            // The Hardware Strike
            if (from.getBalance().compareAndSet(currentBalance, targetBalance)) {
                // We won the race condition. Credit the target account.
                to.getBalance().addAndGet(amount);

                // Asynchronously drop the log into the WAL queue
                wal.logTransaction("TXN: " + from.getAccountId() + " -> " + to.getAccountId() + " | $" + amount);

                return true;
            }
            // If compareAndSet fails, another thread beat us. The loop restarts immediately.
        }
    }
}