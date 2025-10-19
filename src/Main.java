import controller.MenuController;
import controller.AuthController;
import controller.AccountController;
import controller.TransactionController;
import service.AuthService;
import service.AccountService;
import service.TransactionService;
import service.StorageService;
import view.ConsoleUI;
import model.User;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
        ConsoleUI consoleUI = new ConsoleUI();
        StorageService storageService = null;
        Map<String, User> users = null;
        MenuController menuController = null;
        
        try {
            consoleUI.showMessage("Starting Bank Account System...");
            
            // 1. Инициализация и загрузка данных
            storageService = new StorageService();
            users = storageService.loadUsers();
            
            // 2. Инициализация сервисов
            AuthService authService = new AuthService(users);
            AccountService accountService = new AccountService();
            TransactionService transactionService = new TransactionService();
            
            // 3. Инициализация контроллеров
            AuthController authController = new AuthController(authService, consoleUI);
            AccountController accountController = new AccountController(accountService, consoleUI);
            TransactionController transactionController = new TransactionController(transactionService, consoleUI);
            
            // 4. Запуск приложения
            menuController = new MenuController(
                authController, 
                accountController, 
                transactionController, 
                consoleUI, 
                storageService, 
                users
            );
            
            menuController.start();
            
        } 
        catch (Exception e) {
            consoleUI.showError("Critical error: " + e.getMessage());
            e.printStackTrace();
        } 
        finally {
            // 5. Финальное сохранение данных
            if (storageService != null && users != null) {
                consoleUI.showMessage("Final data saving...");
                storageService.emergencySave(users);
            }
            
            consoleUI.showMessage("Application stopped");
            consoleUI.close();
        }
    }
}
