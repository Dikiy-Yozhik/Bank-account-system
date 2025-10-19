package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;

public class BankAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    private String accountId;
    private String accountName;
    private double balance;
    private LocalDateTime createdDate;
    private String ownerId;
    private List<Transaction> transactions;

    public BankAccount(String ownerId, String accountName) {
        this.accountId = UUID.randomUUID().toString();
        this.accountName = accountName;
        this.balance = 0.0;
        this.createdDate = LocalDateTime.now();
        this.ownerId = ownerId;
        this.transactions = new ArrayList<>();
    }

    // Геттеры
    public String getAccountId() {return accountId;}
    public String getAccountName() {return accountName;}
    public double getBalance() {return balance;}
    public LocalDateTime getCreatedDate() {return createdDate;}
    public String getOwnerId() {return ownerId;}
    public List<Transaction> getTransactions() {return transactions;}

    // Сеттеры
    public void setAccountName(String accountName) {
        if (accountName != null && !accountName.trim().isEmpty()) this.accountName = accountName;
    }

    public void setBalance(double balance) {
        if (balance >= 0) this.balance = balance;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            this.transactions.add(transaction);
        }
    }

    @Override
    public String toString() {
        return String.format("BankAccount[%s: %s, Balance: %.2f, Transactions: %d]", 
            accountId, accountName, balance, transactions.size());
    }
}
