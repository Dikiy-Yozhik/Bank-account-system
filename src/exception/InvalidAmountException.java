package exception;

// Исключение для неверной суммы операций (отрицательная сумма, ноль, слишком большое значение)

public class InvalidAmountException extends Exception{
    public InvalidAmountException(String message) {
        super(message);
    }

    public InvalidAmountException(double amount) {
        super("Invalid amount: " + amount + ". Amount must be positive.");
    }
}
