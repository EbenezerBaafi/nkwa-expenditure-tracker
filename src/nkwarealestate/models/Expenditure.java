package nkwarealestate.models;
import java.time.LocalDate;

public class Expenditure {

    private String code;
    private double amount;
    private LocalDate date;
    private String phase;
    private String category;
    private String accountUsed;
    private String receiptPath;

    public Expenditure(String code, double amount, String date, String phase, String category, String accountUsed, String receiptPath) {
        this.code = code;
        this.amount = amount;
        this.date = LocalDate.parse(date);
        this.phase = phase;
        this.category = category;
        this.accountUsed = accountUsed;
        this.receiptPath = receiptPath;
    }

    // Getters and Setters

    public LocalDate getDate() {
        return date;
    }

    public String getCode() {
        return code;
    }

    public double getAmount() {
        return amount;
    }

    public String getPhase() {
        return phase;
    }

    public String getCategory() {
        return category;
    }
    
    public String getAccountUsed() {
        return accountUsed;
    }

    public String getReceiptPath() {
        return receiptPath;
    }

    public String toString() {
        return String.format("Code: %s | Amount: %.2f | Date: %s | Phase: %s | Category: %s | Account: %s | Receipt: %s",
                code, amount, date, phase, category, accountUsed, receiptPath);
    }
}
    


