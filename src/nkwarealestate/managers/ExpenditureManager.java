package nkwarealestate.managers;
import nkwarealestate.models.Expenditure;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.ArrayList;
//import java.util.Collections;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Queue;
//import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import java.time.Month;
 




public class ExpenditureManager {
    // store expenditures using code as key for fast lookup
    private Map<String, Expenditure> expenditures;
    private HashMap<String, Expenditure> expenditureMap;
    private Queue<String> receiptQueue = new LinkedList<>();
    //private Queue<Expenditure> receiptQueueExpenditures = new LinkedList<>();  
    private Set<String> categories = new HashSet<>(); // to store unique categories
    private LinkedList<Expenditure> expenditureList;


    public ExpenditureManager(){
        expenditureMap = new HashMap<>();
        expenditures = new HashMap<>();
        expenditureList = new LinkedList<>();
    }

    

    //Saving to file
    public void saveToFile(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Expenditure exp : expenditures.values()) {
                writer.write(String.format("%s,%s,%s,%.2f,%s,%s%n",
                        exp.getCode(),
                        exp.getAmount(),
                        exp.getDate(),
                        exp.getPhase(),
                        exp.getCategory(),
                        exp.getAccountUsed(),
                        exp.getReceiptPath()));
            }
            System.out.println("Expenditures saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving expenditures to file: " + e.getMessage());
        }
     }

     // Loading from file
    public void loadFromFile(String filename) {
        try(BufferedReader reader = new BufferedReader ( new FileReader(filename))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1); // allows empty values

                if (parts.length == 7) {
                    String code = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    String date = parts[2];
                    String phase = parts[3];
                    String category = parts[4];
                    String accountUsed = parts[5];
                    String receiptPath = parts[6];
                    
                    System.out.println("Loading: " + code + " | Category: " + category);


                    loadExpenditureDirectly(code, amount, date, phase, category, accountUsed, receiptPath);



                }

            }
            System.out.println("Expenditures loaded from " + filename);
        } catch (IOException e) {
            System.out.println("No expenditures file found yet. Starting fresh.");
        }
    }

    public void loadExpenditureDirectly(String code, double amount, String date, String phase, String category, String accountUsed, String receiptPath) {
        categories.add(category); // ensure category is registered
        Expenditure exp = new Expenditure(code, amount, date, phase, category, accountUsed, receiptPath);
        expenditures.put(code, exp);
    }
 

    // Monthly burn rate
    public void showMonthlyBurnRate() {
        Map<Month, Double> monthlyTotals = new HashMap<>();

        for( Expenditure exp : expenditures.values()) {
            Month month = exp.getDate().getMonth();
            monthlyTotals.put(month, monthlyTotals.getOrDefault(month, 0.0) + exp.getAmount());
        }

        System.out.println("Monthly Burn Rate:");
        for (Month m : monthlyTotals.keySet()) {
            System.out.printf("%-10s: GH₵%.2f%n", m, monthlyTotals.get(m));
        }
    }

    // Profitable Forecasts
    public void forecastProfitability(double expectedIncome) {
        double totalExpenditure = 0;

        for (Expenditure exp : expenditures.values()) {
            totalExpenditure += exp.getAmount();
        }

        double profit = expectedIncome - totalExpenditure;
        System.out.println("Forecasted Profitability:");
        System.out.println(" Expected Income: GH₵" + expectedIncome);
        System.out.println(" Total Expenditure: GH₵" + totalExpenditure);
        System.out.println(" Forecasted Profit: GH₵" + profit);

        if (profit > 0) {
            System.out.println(" The project is profitable.");
        } else  {
            System.out.println(" The project is not profitable. Consider reducing expenditures.");
        }
    }


    // Analyze how much was spent on material categories like Cement, Bricks, etc.
    public void analyzeMaterialImpact(){
        String[] materials = { "Cement", "Bricks", "Iron Rods", "Sand", "Paint" };
        double totalMaterialCost = 0;

        System.out.println(" Building Material Cost Breakdown:");

        for (String material : materials) {
            double sum = 0;
            for (Expenditure exp : expenditures.values()) {
                if (exp.getCategory().equalsIgnoreCase(material)) {
                    sum += exp.getAmount();
                }
            }

            if (sum > 0) {
                System.out.printf("%-10s → GH₵ %.2f\n", material, sum);
                totalMaterialCost += sum;
            }
        }

        System.out.println("Total Material Cost: GH₵ " + totalMaterialCost);
    
    }


    // Receipt queue 
    public void uploadReceipt(String code) {
        if (expenditures.containsKey(code)) {
            receiptQueue.add(code);
            System.out.println("Receipt uploaded to processing queue.");
        } else {
            System.out.println("Expenditure code not found.");
        }
    }

    // Review next receipt in the queue
    public void reviewNextReceipt() {
        if (receiptQueue.isEmpty()) {
            System.out.println(" No receipts to review.");
            return;
        }

        String nextCode = receiptQueue.poll();
        Expenditure exp = expenditures.get(nextCode);

        System.out.println(" Reviewing next receipt:");
        System.out.println(exp);

    }


    // View All Pending Receipts
    public void reviewAllPendingReceipts(){
        if (receiptQueue.isEmpty()) {
            System.out.println(" No pending receipts to review.");
            return;
        }

        System.out.println(" Reviewing all pending receipts:");
        for (String code : receiptQueue) {
            System.out.println("." + code);
        }
        // Here you can add logic to process all receipts, e.g., mark as reviewed, etc.
    }

    // Save Receipt Queue
    public void saveReceiptQueue(String filename) {
        try (FileWriter writer = new FileWriter (filename)) {
            for (String code : receiptQueue) {
                writer.write(code + "\n");
            }
            System.out.println("Receipt queue saved to " + filename);

        } catch (IOException e) {
            System.out.println("Error saving receipt queue: " + e.getMessage());
        }
    }

    // Load Receipt Queue
    public void loadReceiptQueue(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (expenditures.containsKey(line.trim())) {
                    receiptQueue.add(line.trim());
                }
            }
            System.out.println("Receipt queue loaded from " + filename);
    
        } catch (IOException e) {
            System.out.println("Error loading receipt queue: " + e.getMessage());
        }
    }



    // search category
    public void searchByCategory(String category) {
        boolean found = false;
        System.out.println("🔍 Expenditures under category: " + category);

        for (Expenditure exp : expenditures.values()) {
            if (exp.getCategory().equalsIgnoreCase(category)) {
                System.out.println(exp);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No expenditures found under this category.");
        }
    }

    // search by bank account Used
    public void searchByBankAccount(String account) {
        boolean found = false;
        System.out.println("🔍 Expenditures using account: " + account);

        for (Expenditure exp : expenditures.values()) {
            if (exp.getAccountUsed().equalsIgnoreCase(account)) {
                System.out.println(exp);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No expenditures found using this account.");
        }
    }


    // search by Cost
    public void searchByCostRange(double minAmount, double maxAmount) {
        boolean found = false;
        System.out.println("Expenditures between GH₵" + minAmount + " and GH₵" + maxAmount + ":");

        for (Expenditure exp : expenditures.values()) {
            double amt = exp.getAmount();
        if (amt >= minAmount && amt <= maxAmount) {
            System.out.println(exp);
            found = true;
        }
    }

    if (!found) {
        System.out.println("No expenditures found in the given cost range.");
        }
    }



    // Add a new expenditure
    public boolean addExpenditure(Expenditure exp) {
        if (expenditureMap.containsKey(exp.getCode())) {
            System.out.println(" Error: An Expenditure with this code already exists.");
            return false;
        } 
        
        expenditures.put(exp.getCode(), exp);
        expenditureMap.put(exp.getCode(), exp);
        expenditureList.add(exp);
        System.out.println("Expenditure added successfully!");


        System.out.println("Expenditure added succesfully!");
        return true; 

    }

    // View all expenditures
    public void viewAllExpenditures(){
        if (expenditureList.isEmpty()) {
            System.out.println("No expenditure recorded yet.");
            return;
        }

        System.out.println("******* All Expenditures *******" );
        for (Expenditure expenditure : expenditureList) {
            System.out.println(expenditure.toString());
        }

    }

    public Expenditure getExpenditureByCode(String code) {
        return expenditureMap.getOrDefault(code, null);
    }


    // Search by date 
    public void searchByDateRange(LocalDate start, LocalDate end) {
    boolean found = false;
    System.out.println("Expenditures between " + start + " and " + end + ":");

    for (Expenditure exp : expenditures.values()) {
        if (!exp.getDate().isBefore(start) && !exp.getDate().isAfter(end)) {
            System.out.println(exp);
            found = true;
        }
    }

    if (!found) {
        System.out.println("No expenditures found in the given date range.");
        return;
    }
    }


    // Search by bank account
    public void searcchByBankAccount(String account) {
        boolean found = false;
        System.out.println("Expenditures using account: " + account);

        for (Expenditure exp : expenditures.values()) {
            if (exp.getAccountUsed().equalsIgnoreCase(account)) {
                System.out.println(exp);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No expenditures found in the given range.");
        }
    }

    // SORTING BY CATEGORY (ALPHABETICAL ORDER)
    public void sortByCategory() {
        ArrayList<Expenditure> expList = new ArrayList<>(expenditures.values());
        expList.sort(Comparator.comparing(Expenditure::getCategory, String.CASE_INSENSITIVE_ORDER));

        System.out.println("Expenditures sorted by Category:");
        for (Expenditure exp : expList) {
            System.out.println(exp);
        }
    }

    // SORTING BY DATE (CHRONOLOGICAL ORDER)
    public void sortByDate() {
        ArrayList<Expenditure> expList = new ArrayList<>(expenditures.values());
        expList.sort(Comparator.comparing(Expenditure::getDate));

        System.out.println("Expenditures sorted by Date");
        for (Expenditure exp : expList) {
            System.out.println(exp);
        }
    }

}

