package exception;

// Исключение для случая, когда недостаточно средств на счёте

public class InsufficientFundsException extends Exception{
    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException(double currentBalance, double requestedAmount) {
        super("Insufficient funds. Balance: " + currentBalance + ", requested: " + requestedAmount);
    }
}
