package nkwarealestate.managers;
import nkwarealestate.models.BankAccount;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.PriorityQueue;



public class BankAccountManager {
    private HashMap<String, BankAccount> accounts;

    public BankAccountManager() {
        accounts = new HashMap<>();
    }


    // show all accounts with balance below threshold using min-heap
    public void showLowBalanceAccounts(double threshold) {
        PriorityQueue<BankAccount> lowBalanceHeap = new PriorityQueue<>(
            (a, b) -> Double.compare(a.getBalance(), b.getBalance())
        );

        for (BankAccount acc : accounts.values()) {
            if (acc.getBalance() < threshold) {
                lowBalanceHeap.add(acc);
            }
        }

        if (lowBalanceHeap.isEmpty()) {
            System.out.println("No low-balance accounts at moment");
            return;
        }
    }

    // Add a new bank account
    public boolean addBankAccount(String accountName, String accountNumber, String bankName, double initialBalance) {
        if (accounts.containsKey(accountNumber)) {
            System.out.println("Error: Account with this number already exists.");
            return false;
        }

        BankAccount account = new BankAccount(accountName, accountNumber, initialBalance, bankName);
        accounts.put(accountNumber, account);
        System.out.println("Bank account added successfully!");
        return true;
    }

    // Get account by account number
    public BankAccount getBankAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    // view all bank accounts
    public void viewAllAccounts(){
        if (accounts.isEmpty()) {
            System.out.println("No bank account found.");
            return;
        }

        System.out.println("******* Bank Accounts *******");
        for (BankAccount acc : accounts.values()) {
            System.out.println(acc);
        }
    }

    // Withdraw funds from account (used for expenditures)
    public boolean withdrawFromAccount(String accountNumber, double amount, String expenditureCode) {
        BankAccount account = accounts.get(accountNumber);
        if (account == null) {
            System.out.println("Error: Account not found.");
            return false;
        }

        boolean success = account.withdraw(amount);
        if (success) {
            account.addExpenditureCode(expenditureCode);
            System.out.println("Expenditure code " + expenditureCode + " added to account " + account.getAccountName() + ".");
        }
        return success;
    }

    // Check if account exists
    public boolean accountExists(String accountNumber) {
        return accounts.containsKey(accountNumber);
    
    }

    // Save Account text
    public void saveAccountsToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (BankAccount account : accounts.values()) {
                writer.write(account.getAccountName() + "," +
                    account.getAccountNumber() + "," +
                    account.getBankName() + "," +
                    account.getBalance() + "\n");
                }
                System.out.println(" Accounts saved to " + filename);
            } catch (IOException e) {
        System.out.println(" Failed to save accounts.");
        }
    }


    // Load accounts from file
    public void loadAccountsFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 4) {
                    String accountName = parts[0].trim();
                    String accountNumber = parts[1].trim();
                    String bankName = parts[2].trim();
                    double balance = Double.parseDouble(parts[3].trim());

                    BankAccount account = new BankAccount(accountName, accountNumber, balance, bankName);
                    accounts.put(accountNumber, account);
                }
            }
            System.out.println("Bank accounts loaded from " + filename);
        } catch (IOException e) {
        System.out.println(" No account file found. Starting fresh.");
        }
    }

}

