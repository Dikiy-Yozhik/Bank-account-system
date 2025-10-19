package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String userName;
    private String passwordHash;
    private List<BankAccount> accounts;

    public User(String userName, String passwordHash) {
        this.accounts = new ArrayList<>();
        this.passwordHash = passwordHash;
        this.userId = UUID.randomUUID().toString();
        this.userName = userName;
    }

    // Геттеры
    public String getUserId() {return userId;}
    public String getUserName() {return userName;}
    public List<BankAccount>getAccounts() {return accounts;}
    public String getPasswordHash() {return passwordHash;}

    // Рабочие методы
    public void addAccount(BankAccount account) {
        if (account != null) accounts.add(account);
    }

    public boolean hasAccounts() {return !accounts.isEmpty();}

    public BankAccount findAccountById(String accountId) {
        return accounts.stream()
                .filter(acc -> acc.getAccountId().equals(accountId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return String.format("User[%s: %s, accounts: %d]", userId, userName, accounts.size());
    }
}
