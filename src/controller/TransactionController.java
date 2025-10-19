package controller;

import service.TransactionService;
import model.Transaction;
import model.TransactionType;
import model.BankAccount;
import view.ConsoleUI;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Контроллер для поиска и фильтрации транзакций
public class TransactionController {
    private final TransactionService transactionService;
    private final ConsoleUI consoleUI;
    
    public TransactionController(TransactionService transactionService, ConsoleUI consoleUI) {
        this.transactionService = transactionService;
        this.consoleUI = consoleUI;
    }
    
    // Главное меню поиска
    public void handleSearchTransactions(BankAccount account) {
        List<Transaction> transactions = account.getTransactions();
        
        if (transactions.isEmpty()) {
            consoleUI.showInfo("No transactions found for searching.");
            consoleUI.pressEnterToContinue();
            return;
        }
        
        consoleUI.showTransactionSearchMenu(account.getAccountName(), transactions.size());
        int choice = consoleUI.readInt("Choose search type: ");
        
        List<Transaction> searchResults = performSearch(transactions, choice);
        
        if (!searchResults.isEmpty()) {
            showSearchResults(searchResults);
        }
    }
    
    // Поиск 
    private List<Transaction> performSearch(List<Transaction> transactions, int choice) {
        switch (choice) {
            case 1:
                return handleSearchByType(transactions);
            case 2:
                return handleSearchByAmount(transactions);
            case 3:
                return handleSearchByDate(transactions);
            case 4:
                return handleSearchByDescription(transactions);
            case 5:
                return handleShowAllSorted(transactions);
            case 6:
                return new ArrayList<>(); 
            default:
                consoleUI.showError("Invalid option.");
                consoleUI.pressEnterToContinue();
                return new ArrayList<>();
        }
    }
    
    // Поиск по типу
    private List<Transaction> handleSearchByType(List<Transaction> transactions) {
        int typeChoice = consoleUI.showTransactionTypeMenu();
        
        TransactionType searchType = null;
        switch (typeChoice) {
            case 1:
                searchType = TransactionType.DEPOSIT;
                break;
            case 2:
                searchType = TransactionType.WITHDRAWAL;
                break;
            case 3:
                return transactions; 
            default:
                consoleUI.showError("Invalid type selection.");
                consoleUI.pressEnterToContinue();
                return new ArrayList<>();
        }
        
        return transactionService.findTransactionsByType(transactions, searchType);
    }
    
    // Поиск по диапазону сумм
    private List<Transaction> handleSearchByAmount(List<Transaction> transactions) {
        double[] amountRange = consoleUI.readAmountRange();
        
        try {
            return transactionService.findTransactionsByAmountRange(transactions, amountRange[0], amountRange[1]);
        } 
        catch (IllegalArgumentException e) {
            consoleUI.showError("Invalid amount range: " + e.getMessage());
            consoleUI.pressEnterToContinue();
            return new ArrayList<>();
        }
    }
    
    // Поиск по диапазону дат
    private List<Transaction> handleSearchByDate(List<Transaction> transactions) {
        LocalDateTime[] dateRange = consoleUI.readDateRange();
        
        try {
            return transactionService.findTransactionsByDateRange(transactions, dateRange[0], dateRange[1]);
        } 
        catch (IllegalArgumentException e) {
            consoleUI.showError("Invalid date range: " + e.getMessage());
            consoleUI.pressEnterToContinue();
            return new ArrayList<>();
        }
    }
    
    // Поиск по описанию
    private List<Transaction> handleSearchByDescription(List<Transaction> transactions) {
        String keyword = consoleUI.readSearchKeyword();
        return transactionService.searchTransactionsByDescription(transactions, keyword);
    }
    
    // Сортированные транзакции
    private List<Transaction> handleShowAllSorted(List<Transaction> transactions) {
        return transactionService.getTransactionsSorted(transactions, true);
    }
    
    // Показываем результат
    private void showSearchResults(List<Transaction> results) {
        consoleUI.clearScreen();
        consoleUI.showHeader("Search Results");
        
        consoleUI.showSuccess("Found " + results.size() + " transactions:");
        consoleUI.showSeparator();
        
        // Показываем транзакции
        consoleUI.showTransactionsHeader();
        for (Transaction transaction : results) {
            consoleUI.showTransaction(
                transaction.getTimestamp().toLocalDate().toString(),
                transaction.getType().toString(),
                transaction.getAmount(),
                transaction.getDescription()
            );
        }
        
        // Показываем статистику
        consoleUI.showSeparator();
        double totalDeposits = transactionService.getTotalDeposits(results);
        double totalWithdrawals = transactionService.getTotalWithdrawals(results);
        consoleUI.showSearchStatistics(results.size(), totalDeposits, totalWithdrawals);
        
        consoleUI.pressEnterToContinue();
    }
    
    public double getTotalDeposits(List<Transaction> transactions) {
        return transactionService.getTotalDeposits(transactions);
    }
    
    public double getTotalWithdrawals(List<Transaction> transactions) {
        return transactionService.getTotalWithdrawals(transactions);
    }
    
    public int getTransactionsCount(List<Transaction> transactions) {
        return transactionService.getTransactionsCount(transactions);
    }
}
