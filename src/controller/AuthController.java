package controller;

import service.AuthService;
import model.User;
import exception.UserAlreadyExistsException;
import exception.AuthenticationException;
import view.ConsoleUI;

// Контроллер для аутентификации и регистрации пользователей
public class AuthController {
    private final AuthService authService;
    private final ConsoleUI consoleUI;
    
    public AuthController(AuthService authService, ConsoleUI consoleUI) {
        this.authService = authService;
        this.consoleUI = consoleUI;
    }
    
    // Обработка входа пользователя в систему
    public User handleLogin() {
        consoleUI.clearScreen();
        consoleUI.showHeader("Login");
        
        String username = consoleUI.readString("Username: ");
        String password = consoleUI.readString("Password: ");

        try {
            User user = authService.login(username, password);
            consoleUI.showSuccess("Login successful! Welcome back, " + username + "!");
            consoleUI.waitForEnter();
            return user;
            
        } 
        catch (AuthenticationException e) {
            consoleUI.showError("Login failed: " + e.getMessage());
            consoleUI.waitForEnter();
            return null;
        } 
        catch (Exception e) {
            consoleUI.showError("Unexpected error during login: " + e.getMessage());
            consoleUI.waitForEnter();
            return null;
        }
    }
    
    // Обработка регистрации нового пользователя
    public boolean handleRegistration() {
        consoleUI.clearScreen();
        consoleUI.showHeader("Registration");
        
        String username = consoleUI.readString("Username: ");
        String password = consoleUI.readString("Password: ");

        try {
            authService.register(username, password);
            consoleUI.showSuccess("Registration successful! You can now login.");
            consoleUI.waitForEnter();
            return true;
            
        } 
        catch (UserAlreadyExistsException e) {
            consoleUI.showError("Registration failed: " + e.getMessage());
            consoleUI.waitForEnter();
            return false;
        } 
        catch (IllegalArgumentException e) {
            consoleUI.showError("Registration failed: " + e.getMessage());
            consoleUI.waitForEnter();
            return false;
        } 
        catch (Exception e) {
            consoleUI.showError("Unexpected error during registration: " + e.getMessage());
            consoleUI.waitForEnter();
            return false;
        }
    }
    
    // Проверка доступности имени пользователя
    public boolean isUsernameAvailable(String username) {
        return authService.isUsernameAvailable(username);
    }
    
    // Поиск пользователя по имени
    public User findUserByUsername(String username) {
        return authService.findUserByUsername(username);
    }
}
