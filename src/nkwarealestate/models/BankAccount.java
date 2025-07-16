package nkwarealestate.models;
import java.util.LinkedList;
import nkwarealestate.models.BankAccount;


public class BankAccount {
    private String accountName;
    private String accountNumber;
    private double balance;
    private String bankName;
    private LinkedList<String> expenditureCodes;

    public BankAccount(String accountName, String accountNumber, double balance, String bankName) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.bankName = bankName;
        this.expenditureCodes = new LinkedList<>();
    }

    // Getters and Setters
    public String getAccountName() {
        return accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getBankName() {
        return bankName;
    }

    public LinkedList<String> getExpenditureCodes() {
        return expenditureCodes;
    }

    public void addExpenditureCode(String code) {
        expenditureCodes.add(code);
    }

    // Update balance after expenditure (withdraw funds)
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Error: Withdrawal amount must be positive.");
            return false;
        }

        if (balance >= amount) {
            balance -= amount;
            System.out.println("Withdrawal successful! New balance: " + balance);
            return true;
        } else {
            System.out.println("Error: Insufficient funds in account" + accountName + ".");
            return false;
        }
    }

    // Deposit funds into the account
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;

        }
    }

    @Override
    public String toString() {
        return "Account Name: " + accountName + ", Bank: " + bankName + ", Account Number: " + accountNumber + ", Balance: GH₵" + balance;
               
    }
}
