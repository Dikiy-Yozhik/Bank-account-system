package view;

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.List;

// Обработка ввода и вывода в консоль
public class ConsoleUI {
    private Scanner scanner;
    
    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }
    
    // === УПРАВЛЕНИЕ ЭКРАНОМ ===
    
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    public void showSeparator() {
        System.out.println("----------------------------------------");
    }
    
    // === ЗАГОЛОВКИ И СООБЩЕНИЯ ===
    
    public void showHeader(String title) {
        System.out.println("=== " + title + " ===");
    }
    
    public void showSubheader(String subtitle) {
        System.out.println("--- " + subtitle + " ---");
    }
    
    public void showWelcome(String message) {
        System.out.println(message);
    }
    
    public void showGoodbye() {
        System.out.println("Thank you for using Bank Account System!");
    }
    
    public void showMessage(String message) {
        System.out.println(message);
    }
    
    public void showSuccess(String message) {
        System.out.println("SUCCESS: " + message);
    }
    
    public void showError(String message) {
        System.out.println("ERROR: " + message);
    }
    
    public void showInfo(String message) {
        System.out.println("INFO: " + message);
    }
    
    public void showWarning(String message) {
        System.out.println("WARNING: " + message);
    }
    
    // === ОТОБРАЖЕНИЕ МЕНЮ ===
    
    public void showMainMenu() {
        showHeader("Main Menu");
        String[] menuItems = {"Login", "Register", "Exit Application"};
        showCompactMenu(menuItems);
    }
    
    public void showUserMenu(String username, double balance) {
        showHeader("User Menu");
        showInfo("Welcome, " + username + "!");
        
        if (balance > 0) {
            showBalance(balance);
        }
        
        showSeparator();
        
        String[] menuItems = {
            "Create New Account",
            "Select Account", 
            "Deposit Money",
            "Withdraw Money",
            "Show Balance",
            "Transaction History", 
            "Search Transactions",
            "Logout"
        };
        showCompactMenu(menuItems);
    }
    
    public void showCompactMenu(String[] menuItems) {
        for (int i = 0; i < menuItems.length; i++) {
            System.out.println((i + 1) + ". " + menuItems[i]);
        }
        System.out.print("Choose option: ");
    }
    
    public void showMenu(String title, String[] menuItems) {
        showHeader(title);
        for (int i = 0; i < menuItems.length; i++) {
            System.out.println((i + 1) + ". " + menuItems[i]);
        }
        System.out.println();
    }
    
    public void showMenu(String title, List<String> menuItems) {
        showHeader(title);
        for (int i = 0; i < menuItems.size(); i++) {
            System.out.println((i + 1) + ". " + menuItems.get(i));
        }
        System.out.println();
    }
    
    // === ОТОБРАЖЕНИЕ ДАННЫХ ===
    
    public void showAccountDetails(String accountName, String accountId, double balance) {
        showSubheader("Account Information");
        System.out.println("Name: " + accountName);
        System.out.println("ID: " + accountId);
        System.out.println("Balance: " + formatAmount(balance));
        showSeparator();
    }
    
    public void showBalance(double balance) {
        System.out.println("Current balance: " + formatAmount(balance));
    }
    
    public void showTransaction(String date, String type, double amount, String description) {
        System.out.printf("%-12s %-10s %10s %s%n", 
            date, type, formatAmount(amount), description);
    }
    
    public void showTransactionsHeader() {
        showSubheader("Transaction History");
        System.out.printf("%-12s %-10s %10s %s%n", 
            "Date", "Type", "Amount", "Description");
        System.out.printf("%-12s %-10s %10s %s%n", 
            "-----------", "----------", "----------", "-----------");
    }
    
    public void showAccountsList(List<String> accountSummaries) {
        if (accountSummaries.isEmpty()) {
            showInfo("No accounts found.");
            return;
        }
        
        showSubheader("Your Accounts");
        for (int i = 0; i < accountSummaries.size(); i++) {
            System.out.println((i + 1) + ". " + accountSummaries.get(i));
        }
        showSeparator();
    }
    
    // === ПОИСК ТРАНЗАКЦИЙ ===
    
    public void showTransactionSearchMenu(String accountName, int transactionCount) {
        showHeader("Search Transactions");
        showInfo("Account: " + accountName);
        showInfo("Total transactions: " + transactionCount);
        showSeparator();
        
        String[] searchMenu = {
            "Search by type (DEPOSIT/WITHDRAWAL)",
            "Search by amount range", 
            "Search by date range",
            "Search by description",
            "Show all transactions (sorted)",
            "Back to main menu"
        };
        showMenu("Search Options", searchMenu);
    }
    
    public int showTransactionTypeMenu() {
        showHeader("Search by Transaction Type");
        String[] typeMenu = {"DEPOSIT", "WITHDRAWAL", "Both"};
        showMenu("Select transaction type", typeMenu);
        return readInt("Choose type: ");
    }
    
    public LocalDateTime[] readDateRange() {
        showHeader("Search by Date Range");
        showInfo("Enter start date:");
        int startYear = readInt("Year (e.g., 2024): ");
        int startMonth = readInt("Month (1-12): ");
        int startDay = readInt("Day (1-31): ");
        
        showInfo("Enter end date:");
        int endYear = readInt("Year (e.g., 2024): ");
        int endMonth = readInt("Month (1-12): ");
        int endDay = readInt("Day (1-31): ");
        
        LocalDateTime startDate = LocalDateTime.of(startYear, startMonth, startDay, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(endYear, endMonth, endDay, 23, 59);
        
        return new LocalDateTime[]{startDate, endDate};
    }
    
    public double[] readAmountRange() {
        showHeader("Search by Amount Range");
        double minAmount = readDouble("Minimum amount: ");
        double maxAmount = readDouble("Maximum amount: ");
        return new double[]{minAmount, maxAmount};
    }
    
    public String readSearchKeyword() {
        showHeader("Search by Description");
        return readString("Enter search keyword: ");
    }
    
    public void showSearchStatistics(int resultCount, double totalDeposits, double totalWithdrawals) {
        double netChange = totalDeposits - totalWithdrawals;
        double averageAmount = resultCount > 0 ? (totalDeposits + totalWithdrawals) / resultCount : 0;
        
        showInfo("Statistics for found transactions:");
        System.out.println("Total deposits: " + formatAmount(totalDeposits));
        System.out.println("Total withdrawals: " + formatAmount(totalWithdrawals));
        System.out.println("Net change: " + formatAmount(netChange));
        System.out.println("Average transaction amount: " + formatAmount(averageAmount));
    }
    
    // === ВВОД ДАННЫХ ===
    
    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    public int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } 
            catch (NumberFormatException e) {
                showError("Please enter a valid number");
            }
        }
    }
    
    public double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } 
            catch (NumberFormatException e) {
                showError("Please enter a valid amount");
            }
        }
    }
    
    public boolean askConfirmation(String question) {
        System.out.print(question + " (y/n): ");
        String answer = scanner.nextLine().trim().toLowerCase();
        return answer.equals("y") || answer.equals("yes");
    }
    
    // === УТИЛИТЫ ===
    
    public void waitForEnter() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
    
    public void pressEnterToContinue() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
    
    public String formatAmount(double amount) {
        return String.format("%.2f", amount);
    }
    
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
