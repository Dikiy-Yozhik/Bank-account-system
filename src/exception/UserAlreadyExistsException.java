package exception;

// Исключение при попытке зарегистрировать существующего пользователя

public class UserAlreadyExistsException extends Exception{
    public UserAlreadyExistsException(String username) {
        super("User with username '" + username + "' already exists");
    }
}
