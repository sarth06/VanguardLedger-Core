package com.vanguard.core;

import java.util.concurrent.atomic.AtomicInteger;

public class Account {
    private final String accountId;

    // Maps the balance directly to CPU-level atomic memory space
    private final AtomicInteger balance;

    public Account(String accountId, int initialBalance) {
        this.accountId = accountId;
        this.balance = new AtomicInteger(initialBalance);
    }

    public String getAccountId() {
        return accountId;
    }

    public AtomicInteger getBalance() {
        return balance;
    }
}