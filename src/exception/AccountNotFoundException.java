package exception;

// Исключение когда счёт не найден

public class AccountNotFoundException extends Exception{
    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String accountId, String userId) {
        super("Account with ID '" + accountId + "' not found for user '" + userId + "'");
    }
}
