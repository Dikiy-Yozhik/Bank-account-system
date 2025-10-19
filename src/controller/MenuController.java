package controller;

import service.StorageService;
import utils.Session;
import model.User;
import model.BankAccount;
import view.ConsoleUI;

import java.util.List;
import java.util.Map;

// Главный контроллер-координатор приложения
public class MenuController {
    private final AuthController authController;
    private final AccountController accountController;
    private final TransactionController transactionController;
    private final ConsoleUI consoleUI;
    private final StorageService storageService;
    private final Map<String, User> users;
    private final Session session;
    private boolean running;

    public MenuController(AuthController authController, AccountController accountController,
                         TransactionController transactionController, ConsoleUI consoleUI,
                         StorageService storageService, Map<String, User> users) {
        this.authController = authController;
        this.accountController = accountController;
        this.transactionController = transactionController;
        this.consoleUI = consoleUI;
        this.storageService = storageService;
        this.users = users;
        this.session = new Session();
        this.running = true;
    }
    
    public void start() {
        consoleUI.clearScreen();
        consoleUI.showWelcome("Welcome to Bank Account System!");
        
        while (running) {
            if (session.isAuthenticated()) {
                showUserMenu();
            } 
            else {
                showMainMenu();
            }
        }
        
        consoleUI.showGoodbye();
    }
    
    private void showMainMenu() {
        consoleUI.clearScreen();
        consoleUI.showMainMenu();
        int choice = consoleUI.readInt("");
        
        switch (choice) {
            case 1:
                User user = authController.handleLogin();
                if (user != null) session.setCurrentUser(user);
                break;
            case 2:
                authController.handleRegistration();
                break;
            case 3:
                handleExit();
                break;
            default:
                consoleUI.showError("Invalid option.");
                consoleUI.waitForEnter();
        }
    }
    
    private void showUserMenu() {
        consoleUI.clearScreen();
        User currentUser = session.getCurrentUser();
        double totalBalance = calculateTotalBalance(currentUser);
        
        consoleUI.showUserMenu(currentUser.getUserName(), totalBalance);
        int choice = consoleUI.readInt("");
        
        switch (choice) {
            case 1: handleCreateAccount(); break;
            case 2: handleSelectAccount(); break;
            case 3: handleAccountOperation(accountController::handleDeposit); break;
            case 4: handleAccountOperation(accountController::handleWithdraw); break;
            case 5: handleAccountOperation(accountController::handleShowBalance); break;
            case 6: handleAccountOperation(accountController::handleTransactionHistory); break;
            case 7: handleAccountOperation(transactionController::handleSearchTransactions); break;
            case 8: handleLogout(); break;
            default:
                consoleUI.showError("Invalid option.");
                consoleUI.waitForEnter();
        }
    }
    
    private void handleCreateAccount() {
        BankAccount newAccount = accountController.handleCreateAccount(session.getCurrentUser());
        if (newAccount != null) {
            session.setSelectedAccount(newAccount);
        }
    }
    
    private void handleSelectAccount() {
        List<BankAccount> accounts = session.getCurrentUser().getAccounts();
        if (accounts.isEmpty()) {
            consoleUI.showError("No accounts found. Create an account first.");
            consoleUI.pressEnterToContinue();
            return;
        }
        
        List<String> accountSummaries = accountController.formatAccountsList(accounts);
        consoleUI.showAccountsList(accountSummaries);
        
        int choice = consoleUI.readInt("Select account (number): ");
        if (choice < 1 || choice > accounts.size()) {
            consoleUI.showError("Invalid selection.");
            consoleUI.pressEnterToContinue();
            return;
        }

        session.setSelectedAccount(accounts.get(choice - 1));
        consoleUI.showSuccess("Account selected: " + session.getSelectedAccount().getAccountName());
        consoleUI.pressEnterToContinue();
    }
    
    private void handleAccountOperation(AccountOperation operation) {
        if (ensureAccountSelected()) {
            operation.execute(session.getSelectedAccount());
        }
    }
    
    private void handleLogout() {
        boolean confirm = consoleUI.askConfirmation("Are you sure you want to logout?");
        if (confirm) {
            saveUserData();
            session.logout();
            consoleUI.showSuccess("Logged out successfully!");
            consoleUI.waitForEnter();
        }
    }
    
    private void handleExit() {
        boolean confirm = consoleUI.askConfirmation("Exit application?");
        if (confirm) {
            saveUserData();
            running = false;
        }
    }
    
    private boolean ensureAccountSelected() {
        if (!session.hasSelectedAccount()) {
            consoleUI.showError("No account selected. Select an account first.");
            consoleUI.pressEnterToContinue();
            return false;
        }
        return true;
    }
    
    private void saveUserData() {
        try {
            storageService.saveUsers(users);
        } 
        catch (Exception e) {
            consoleUI.showError("Failed to save data: " + e.getMessage());
        }
    }

    private double calculateTotalBalance(User user) {
        double total = 0.0;
        for (BankAccount account : user.getAccounts()) {
            total += account.getBalance();
        }
        return total;
    }
    
    @FunctionalInterface
    private interface AccountOperation {
        void execute(BankAccount account);
    }
}
