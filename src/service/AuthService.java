package service;

import model.User;
import utils.PasswordHasher;
import exception.UserAlreadyExistsException;
import exception.AuthenticationException;
import java.util.Map;

public class AuthService {
    private Map<String,User> users;
    private final PasswordHasher passwordHasher;

    public AuthService(Map<String,User> users) {
        this.users = users;
        this.passwordHasher = new PasswordHasher();
    }

    public User register(String username, String password) throws UserAlreadyExistsException, IllegalArgumentException {
        // Проверка ввода
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.length() < 3) {
            throw new IllegalArgumentException("Password must be at least 3 characters long");
        }

        // Проверка на существования пользователя
        if (users.containsKey(username)) {
            throw new UserAlreadyExistsException(username);
        }

        // Создаем нового пользователя
        String passwordHash = passwordHasher.hashPassword(password);
        User newUser = new User(username, passwordHash);
        
        users.put(username, newUser);
        
        return newUser;
    }

    public User login(String username, String password) throws AuthenticationException{
        if (username == null || password == null) {
            throw new AuthenticationException("Username and password cannot be null");
        }

        User user = findUserByUsername(username);
        if (user == null) {
            throw new AuthenticationException(username, "user not found");
        }

        boolean isValidPassword = passwordHasher.verifyPassword(password, user.getPasswordHash());
        if (!isValidPassword) {
            throw new AuthenticationException(username, "invalid password");
        }

        return user;
    }

    public User findUserByUsername(String username) {
        return users.get(username);
    }

    public boolean isUsernameAvailable(String username) {
        return !users.containsKey(username);
    }
}
