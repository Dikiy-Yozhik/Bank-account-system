package service;

import model.User;
import java.util.*;
import java.io.*;

// Сервис для сохранения и загрузки данных приложения
public class StorageService {
    private static final String DATA_FILE = "bank_data.dat";
    
    // Загружает всех пользователей из файла
    // Возвращает мапу пользователей или пустую мапу если файла нет
    @SuppressWarnings("unchecked")
    public Map<String, User> loadUsers() {
        File file = new File(DATA_FILE);
        
        if (!file.exists()) {
            System.out.println("Data file not found. Starting with empty database.");
            return new HashMap<>();
        }
        
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            
            // Читаем объект из файла
            Map<String, User> users = (Map<String, User>) ois.readObject();
            System.out.println("Loaded " + users.size() + " users from storage.");
            return users;  
        } 
        catch (FileNotFoundException e) {
            System.out.println("Data file not found. Starting with empty database.");
            return new HashMap<>();
        }
        catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
            System.out.println("Starting with empty database due to error.");
            return new HashMap<>();
        } 
        finally {
            // Всегда закрываем ресурсы
            closeQuietly(ois);
            closeQuietly(fis);
        }
    }
    
    // Сохраняет всех пользователей в файл
    public void saveUsers(Map<String, User> users) {
        if (users == null) {
            System.out.println("No data to save.");
            return;
        }
        
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        
        try {
            fos = new FileOutputStream(DATA_FILE);
            oos = new ObjectOutputStream(fos);
            
            // Записываем объект в файл
            oos.writeObject(users);
            System.out.println("Saved " + users.size() + " users to storage.");
        } 
        catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            throw new RuntimeException("Failed to save user data", e);
        } 
        finally {
            // Всегда закрываем ресурсы
            closeQuietly(oos);
            closeQuietly(fos);
        }
    }
    
    // Аварийное сохранение, которое не выбрасывает исключения (для блоков finally)
    public void emergencySave(Map<String, User> users) {
        try {
            saveUsers(users);
        } 
        catch (Exception e) {
            System.err.println("Emergency save failed: " + e.getMessage());
            // В аварийном режиме НЕ бросаем исключения
        }
    }
    
    // Безопасное закрытие ресурсов без выброса исключений
    private void closeQuietly(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } 
            catch (IOException e) {
                System.err.println("Warning: Failed to close resource: " + e.getMessage());
            }
        }
    }
}
