package service;

import model.BankAccount;
import model.Transaction;
import model.TransactionType;
import exception.InsufficientFundsException;
import exception.AccountNotFoundException;
import exception.InvalidAmountException;

import java.util.List;

public class AccountService {    
    public AccountService() {}

    public BankAccount createAccount(String userId, String accountName) {
        if (accountName == null || accountName.trim().isEmpty()) {
            accountName = "Main Account"; 
        }
        
        BankAccount newAccount = new BankAccount(userId, accountName);
        return newAccount;
    }

    public void deposit(BankAccount account, double amount, String description) throws InvalidAmountException {
        validateAmount(amount);
        
        // Обновляем баланс
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        
        // Создаём и добавляем транзакцию в счёт
        Transaction transaction = new Transaction(account.getAccountId(), TransactionType.DEPOSIT, amount, 
            description != null ? description : "Deposit");
        account.addTransaction(transaction); 
    }

    public void withdraw(BankAccount account, double amount, String description) throws InsufficientFundsException, InvalidAmountException {
        validateAmount(amount);
        
        // Проверяем достаточно ли средств
        if (amount > account.getBalance()) {
            throw new InsufficientFundsException(account.getBalance(), amount);
        }
        
        // Обновляем баланс
        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);
        
        // Создаём и добавляем транзакцию в счёт
        Transaction transaction = new Transaction(account.getAccountId(), TransactionType.WITHDRAWAL, amount, 
            description != null ? description : "Withdrawal");
        account.addTransaction(transaction);
    }

   
    public double getBalance(BankAccount account) {
        return account.getBalance();
    }

    
    public List<Transaction> getTransactionHistory(BankAccount account) {
        return account.getTransactions(); 
    }

   
    public BankAccount findAccountById(List<BankAccount> userAccounts, String accountId) 
        throws AccountNotFoundException {
        
        for (BankAccount account : userAccounts) {
            if (account.getAccountId().equals(accountId)) {
                return account;
            }
        }
        throw new AccountNotFoundException(accountId, "current user");
    }

   
    private void validateAmount(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
        
        if (amount > 1_000_000_000) {
            throw new InvalidAmountException("Amount too large");
        }
    }
}
