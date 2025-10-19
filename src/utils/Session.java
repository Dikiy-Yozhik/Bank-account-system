package utils;

import model.User;
import model.BankAccount;

public class Session {
    private User currentUser;
    private BankAccount selectedAccount;
    
    public Session() {
        this.currentUser = null;
        this.selectedAccount = null;
    }
    
    public boolean isAuthenticated() {
        return currentUser != null;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.selectedAccount = null; 
    }
    
    public void logout() {
        this.currentUser = null;
        this.selectedAccount = null;
    }
    
    public BankAccount getSelectedAccount() {
        return selectedAccount;
    }
    
    public void setSelectedAccount(BankAccount account) {
        this.selectedAccount = account;
    }
    
    public boolean hasSelectedAccount() {
        return selectedAccount != null;
    }
}
