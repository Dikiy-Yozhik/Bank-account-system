package service;

import model.Transaction;
import model.TransactionType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Сервис для поиска и фильтрации транзакций
public class TransactionService {
    
    public TransactionService() {}

    // Получает транзакции отсортированные по дате
    public List<Transaction> getTransactionsSorted(List<Transaction> transactions, boolean newestFirst) {
        if (transactions == null) {
            return new ArrayList<>();
        }
        
        List<Transaction> sortedTransactions = new ArrayList<>(transactions);
        
        if (newestFirst) {
            sortedTransactions.sort((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()));
        } 
        else {
            sortedTransactions.sort((t1, t2) -> t1.getTimestamp().compareTo(t2.getTimestamp()));
        }
        
        return sortedTransactions;
    }

    // Находит транзакции по типу (DEPOSIT/WITHDRAWAL)
    public List<Transaction> findTransactionsByType(List<Transaction> transactions, TransactionType type) {
        if (transactions == null) {
            return new ArrayList<>();
        }
        
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getType() == type) {
                result.add(transaction);
            }
        }
        return result;
    }

    // Находит транзакции в диапазоне дат из списка
    public List<Transaction> findTransactionsByDateRange(List<Transaction> transactions, LocalDateTime startDate, LocalDateTime endDate) {
        if (transactions == null) {
            return new ArrayList<>();
        }
        
        validateDateRange(startDate, endDate);
        
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            LocalDateTime timestamp = transaction.getTimestamp();
            if (isDateInRange(timestamp, startDate, endDate)) {
                result.add(transaction);
            }
        }
        return result;
    }

    // Находит транзакции в диапазоне сумм из списка
    public List<Transaction> findTransactionsByAmountRange(List<Transaction> transactions, double minAmount, double maxAmount) {
        if (transactions == null) {
            return new ArrayList<>();
        }
        
        validateAmountRange(minAmount, maxAmount);
        
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions) {
            double amount = transaction.getAmount();
            if (amount >= minAmount && amount <= maxAmount) {
                result.add(transaction);
            }
        }
        return result;
    }

    // Ищет транзакции по ключевому слову в описании из списка
    public List<Transaction> searchTransactionsByDescription(List<Transaction> transactions, String keyword) {
        if (transactions == null) {
            return new ArrayList<>();
        }
        
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>(transactions);
        }
        
        String searchTerm = keyword.toLowerCase().trim();
        List<Transaction> result = new ArrayList<>();
        
        for (Transaction transaction : transactions) {
            String description = transaction.getDescription().toLowerCase();
            if (description.contains(searchTerm)) {
                result.add(transaction);
            }
        }
        return result;
    }

    // Получает общую сумму депозитов из списка транзакций
    public double getTotalDeposits(List<Transaction> transactions) {
        if (transactions == null) {
            return 0.0;
        }
        
        double total = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getType() == TransactionType.DEPOSIT) {
                total += transaction.getAmount();
            }
        }
        return total;
    }

    // Получает общую сумму снятий из списка транзакций
    public double getTotalWithdrawals(List<Transaction> transactions) {
        if (transactions == null) {
            return 0.0;
        }
        
        double total = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getType() == TransactionType.WITHDRAWAL) {
                total += transaction.getAmount();
            }
        }
        return total;
    }

    // Получает транзакцию по ID из списка
    public Transaction getTransactionById(List<Transaction> transactions, String transactionId) {
        if (transactions == null || transactionId == null || transactionId.trim().isEmpty()) {
            return null;
        }
        
        for (Transaction transaction : transactions) {
            if (transactionId.equals(transaction.getTransactionId())) {
                return transaction;
            }
        }
        return null;
    }

    // Получает количество транзакций в списке
    public int getTransactionsCount(List<Transaction> transactions) {
        return transactions != null ? transactions.size() : 0;
    }

    // ===== Служебные методы =====

    // Проверяет корректность диапазона дат
    private void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }

    // Проверяет корректность диапазона сумм
    private void validateAmountRange(double minAmount, double maxAmount) {
        if (minAmount < 0 || maxAmount < 0) {
            throw new IllegalArgumentException("Amounts cannot be negative");
        }
        if (minAmount > maxAmount) {
            throw new IllegalArgumentException("Min amount cannot be greater than max amount");
        }
    }

    // Проверяет находится ли дата в указанном диапазоне
    private boolean isDateInRange(LocalDateTime date, LocalDateTime start, LocalDateTime end) {
        return !date.isBefore(start) && !date.isAfter(end);
    }
}
