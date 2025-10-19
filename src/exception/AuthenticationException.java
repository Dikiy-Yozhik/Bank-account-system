package exception;

// Исключение при ошибке аутентификации (неверный логин или пароль)

public class AuthenticationException extends Exception{
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String username, String reason) {
        super("Authentication failed for user '" + username + "': " + reason);
    }
}
