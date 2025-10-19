package controller;

import service.AccountService;
import model.BankAccount;
import model.User;
import model.Transaction;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import view.ConsoleUI;

import java.util.ArrayList;
import java.util.List;

// Контроллер для операций со счетами
public class AccountController {
    private final AccountService accountService;
    private final ConsoleUI consoleUI;
    
    public AccountController(AccountService accountService, ConsoleUI consoleUI) {
        this.accountService = accountService;
        this.consoleUI = consoleUI;
    }
    
    // Создание нового  счета
    public BankAccount handleCreateAccount(User user) {
        consoleUI.clearScreen();
        consoleUI.showHeader("Create New Account");
        
        String accountName = consoleUI.readString("Account name (optional, press Enter for 'Main Account'): ");
        if (accountName.trim().isEmpty()) {
            accountName = "Main Account";
        }

        try {
            BankAccount newAccount = accountService.createAccount(user.getUserId(), accountName);
            user.addAccount(newAccount);
            
            consoleUI.showSuccess("Account created successfully!");
            consoleUI.showInfo("Account ID: " + newAccount.getAccountId());
            consoleUI.showBalance(newAccount.getBalance());
            consoleUI.pressEnterToContinue();
            
            return newAccount;
            
        } 
        catch (Exception e) {
            consoleUI.showError("Failed to create account: " + e.getMessage());
            consoleUI.pressEnterToContinue();
            return null;
        }
    }
    
    // Пополнение  счета
    public boolean handleDeposit(BankAccount account) {
        consoleUI.clearScreen();
        consoleUI.showHeader("Deposit Money");
        consoleUI.showAccountDetails(account.getAccountName(), account.getAccountId(), account.getBalance());
        
        double amount = consoleUI.readDouble("Amount to deposit: ");
        String description = consoleUI.readString("Description (optional): ");

        try {
            // Подтверждение для больших сумм
            if (amount > 10000) {
                boolean confirm = consoleUI.askConfirmation("Deposit amount is large. Are you sure?");
                if (!confirm) {
                    consoleUI.showInfo("Deposit cancelled.");
                    consoleUI.pressEnterToContinue();
                    return false;
                }
            }
            
            accountService.deposit(account, amount, description);
            consoleUI.showSuccess("Deposit successful!");
            consoleUI.showBalance(account.getBalance());
            consoleUI.pressEnterToContinue();
            return true;
            
        } 
        catch (InvalidAmountException e) {
            consoleUI.showError("Deposit failed: " + e.getMessage());
            consoleUI.pressEnterToContinue();
            return false;
        } 
        catch (Exception e) {
            consoleUI.showError("Unexpected error: " + e.getMessage());
            consoleUI.pressEnterToContinue();
            return false;
        }
    }
    
    // Снятие средств со счета
    public boolean handleWithdraw(BankAccount account) {
        consoleUI.clearScreen();
        consoleUI.showHeader("Withdraw Money");
        consoleUI.showAccountDetails(account.getAccountName(), account.getAccountId(), account.getBalance());
        
        double amount = consoleUI.readDouble("Amount to withdraw: ");
        String description = consoleUI.readString("Description (optional): ");

        // Подтверждение для операций снятия
        boolean confirm = consoleUI.askConfirmation("Are you sure you want to withdraw " + consoleUI.formatAmount(amount) + "?");
        if (!confirm) {
            consoleUI.showInfo("Withdrawal cancelled.");
            consoleUI.pressEnterToContinue();
            return false;
        }

        try {
            accountService.withdraw(account, amount, description);
            consoleUI.showSuccess("Withdrawal successful!");
            consoleUI.showBalance(account.getBalance());
            consoleUI.pressEnterToContinue();
            return true;
            
        } 
        catch (InsufficientFundsException e) {
            consoleUI.showError("Withdrawal failed: " + e.getMessage());
            consoleUI.pressEnterToContinue();
            return false;
        } 
        catch (InvalidAmountException e) {
            consoleUI.showError("Withdrawal failed: " + e.getMessage());
            consoleUI.pressEnterToContinue();
            return false;
        } 
        catch (Exception e) {
            consoleUI.showError("Unexpected error: " + e.getMessage());
            consoleUI.pressEnterToContinue();
            return false;
        }
    }
    
    // Просмотр баланса счета
    public void handleShowBalance(BankAccount account) {
        consoleUI.showHeader("Account Balance");
        consoleUI.showAccountDetails(account.getAccountName(), account.getAccountId(), account.getBalance());
        consoleUI.pressEnterToContinue();
    }
    
    // История транзакций счета
    public void handleTransactionHistory(BankAccount account) {
        consoleUI.clearScreen();
        consoleUI.showHeader("Transaction History");
        
        consoleUI.showInfo("Account: " + account.getAccountName());
        consoleUI.showSeparator();
        
        List<Transaction> transactions = accountService.getTransactionHistory(account);

        if (transactions.isEmpty()) {
            consoleUI.showInfo("No transactions found.");
        } 
        else {
            consoleUI.showTransactionsHeader();
            for (Transaction transaction : transactions) {
                consoleUI.showTransaction(
                    transaction.getTimestamp().toLocalDate().toString(),
                    transaction.getType().toString(),
                    transaction.getAmount(),
                    transaction.getDescription()
                );
            }
        }
        
        consoleUI.showSeparator();
        consoleUI.pressEnterToContinue();
    }
    
    //Поиск счета по ID 
    public BankAccount findAccountById(List<BankAccount> userAccounts, String accountId) {
        try {
            return accountService.findAccountById(userAccounts, accountId);
        } 
        catch (Exception e) {
            return null;
        }
    }
    
    // Форматирование списка счетов для отображения
    public List<String> formatAccountsList(List<BankAccount> accounts) {
        List<String> accountSummaries = new ArrayList<>();
        for (BankAccount account : accounts) {
            String summary = account.getAccountName() + " - Balance: " + 
                           consoleUI.formatAmount(account.getBalance());
            accountSummaries.add(summary);
        }
        return accountSummaries;
    }
    
    public double getAccountBalance(BankAccount account) {
        return accountService.getBalance(account);
    }
}
