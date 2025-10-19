package model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.io.Serializable;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private String transactionId;
    private String accountId;
    private TransactionType type;
    private double amount;
    private LocalDateTime timestamp;
    private String description;

    public Transaction(String accountId, TransactionType type, double amount, String description) {
        this.accountId = accountId;
        this.amount = amount;
        this.description = description;
        this.timestamp = LocalDateTime.now();
        this.transactionId = UUID.randomUUID().toString();
        this.type = type;
    }

    // Геттеры
    public String getTransactionId() {return transactionId;}
    public String getAccountId() {return accountId;}
    public TransactionType getType() {return type;}
    public double getAmount() {return amount;}
    public LocalDateTime getTimestamp() {return timestamp;}
    public String getDescription() {return description;}

    // Сеттеры
    public void setDescription(String description) {this.description = description;}

    @Override
    public String toString() {
        return String.format("Transaction: [%s, %s, %.2f, %s]", type, accountId, amount, timestamp);
    }
}
