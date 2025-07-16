package nkwarealestate;
import nkwarealestate.managers.ExpenditureManager;
import nkwarealestate.models.Expenditure;
import java.util.Scanner;
import nkwarealestate.managers.CategoryManager;
import nkwarealestate.managers.BankAccountManager;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;


        CategoryManager categoryManager = new CategoryManager();
        // Load categories from file
        categoryManager.loadFromFile("categories.txt");

        ExpenditureManager expenditureManager = new ExpenditureManager();
        expenditureManager.loadFromFile("expenditures.txt");
        
        //load the receipt queue from file
        expenditureManager.loadReceiptQueue("Receipt.txt");


        BankAccountManager bankAccountManager = new BankAccountManager();
        // Load bank accounts from file
        bankAccountManager.loadAccountsFromFile("Accounts.txt");

        while (running) {
            System.out.println("******* NKWA REAL ESTATE EXPENDITURE TRACKER *********");
            System.out.println("1. Add a new expenditure");
            System.out.println("2. View all expenditure");
            System.out.println("3. Search all expenditures");
            System.out.println("4. Sort expenditures");
            System.out.println("5. Manage expenditure categories");
            System.out.println("6. Manage bank accounts");
            System.out.println("7. Upload/view receipts");
            System.out.println("8. View bank balance and alerts");
            System.out.println("9. Generate financial analysis report");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice){
                case "1":
                    System.out.print("Enter expenditure code:");
                    String code = scanner.nextLine();

                    System.out.print("Enter amount:");
                    double amount = Double.parseDouble(scanner.nextLine());

                    System.out.print("Enter date (e.g., YYYY-MM-DD):");
                    String date = scanner.nextLine();

                    System.out.print("Enter phase:");
                    String phase = scanner.nextLine();

                    System.out.print("Enter category (must exist):");
                    String category = scanner.nextLine();

                    if (!categoryManager.categoryExists(category)) {
                        System.out.println("Error: Category does not exist. Please create the category first.");
                        break; // Skip to the next iteration of the loop
                    }

                    System.out.println("Enter bank account number used:");
                    String accountUsed = scanner.nextLine();

                    // Check if the account exists
                    if (!bankAccountManager.accountExists(accountUsed)) {
                        System.out.println("Error: Account does not exist. Please create the account first.");
                        break; // Skip to the next iteration of the loop
                    }

                    // Try to withdraw the ammount
                    boolean withdrawalScuccess = bankAccountManager.withdrawFromAccount(accountUsed, amount, code);
                    if (!withdrawalScuccess) {
                        System.out.println("Expenditure not recorded due to insufficient funds.");
                        break;
                    }

                    System.out.println("Enter receipt file path or description:");
                    String receiptPath = scanner.nextLine();

                    Expenditure newExp= new Expenditure(code, amount, date, phase, category, accountUsed, receiptPath);
                    expenditureManager.addExpenditure(newExp);
                

                    break;
                    

                case "2":
                    expenditureManager.viewAllExpenditures();
                    break;

                case "3":
                    System.out.println("******* SEARCH EXPENDITURES *********");
                    System.out.println("1. Search by date range");
                    System.out.println("2. Search by category");
                    System.out.println("3. Search by cost range");
                    System.out.println("4. Search by bank account");
                    System.out.println("Choose option:");
                    String searchChoice = scanner.nextLine();

                    if (searchChoice.equals("1")) {
                        System.out.print("Enter start date (YYYY-MM-DD):");
                        String startStr = scanner.nextLine();
                        System.out.print("Enter end date (YYYY-MM-DD):");
                        String endStr = scanner.nextLine();

                        try {
                            LocalDate start = LocalDate.parse(startStr);
                            LocalDate end = LocalDate.parse(endStr);
                            expenditureManager.searchByDateRange(start, end);
                        }
                        catch (Exception e) {
                            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
                        }

                    } else if (searchChoice.equals("2")) {
                        System.out.print("Enter category name: ");
                        String searchCategory = scanner.nextLine();
                        expenditureManager.searchByCategory(searchCategory);
                    } else  if (searchChoice.equals("3")) {
                        System.out.print("Enter minimum amount: ");
                        double minAmount = Double.parseDouble(scanner.nextLine());

                        System.out.print("Enter maximum amount: ");
                        double maxAmount = Double.parseDouble(scanner.nextLine());

                        expenditureManager.searchByCostRange(minAmount, maxAmount);
                   
                    } else if (searchChoice.equals("4")) {
                        System.out.print("Enter bank account number: ");
                        String accountNumber = scanner.nextLine();
                        expenditureManager.searchByBankAccount(accountNumber );
                    } else {
                        System.out.println("Invalid choice. Please try again.");

                    }
                    
                case "4":
                    System.out.println("********** Sort Expenditures *********");
                    System.out.println("1. Sort by Category (A-Z)");
                    System.out.println("2. Sort by Date (Oldest-Newest)");
                    System.out.println("Choose sort option");
                    String sortChoice = scanner.nextLine();

                    if (sortChoice.equals("1")) {
                        expenditureManager.sortByCategory();
                    } else if (sortChoice.equals("2")) {
                        expenditureManager.sortByDate();
                    } else {
                        System.out.println("Invalid sort option. Please select 1 or 2");
                    }
                    break;


                case "5":
                    boolean categoryRunning = true;
                    while (categoryRunning){
                        System.out.println("******* CATEGORY MANAGEMENT *********");
                        System.out.println("1. Add a new category");
                        System.out.println("2. View all categories");
                        System.out.println("3. Check if a category exists");
                        System.out.println("0. Back to main menu");     
                        System.out.print("Enter your choice: ");
                        String categoryChoice = scanner.nextLine();

                        switch (categoryChoice) {
                            case "1":
                                System.out.print("Enter new category name: ");
                                String newCategory = scanner.nextLine();
                                categoryManager.addCategory(newCategory);
                                break;
                            case "2":
                                categoryManager.viewAllCategories();
                                break;
                            case "3":
                                System.out.println("Enter category name check: ");
                                String checkCat = scanner.nextLine();
                                if (categoryManager.categoryExists(checkCat)) {
                                    System.out.println("Category exists.");
                                } else {
                                    System.out.println("Category not found.");
                                }
                                break;
                            
                            case "0":
                                categoryRunning = false;
                                break;
                        
                            default:
                                System.out.print("Invalid input. Try again. ");
                                break;
                        }
                        System.out.println();
                    }
                    break;
                case "6":
                    boolean accountRunning = true;
                    while (accountRunning) {
                        System.out.println("******* BANK ACCOUNT MANAGEMENT *********");
                        System.out.println("1. Add a new bank account");
                        System.out.println("2. View all bank accounts");
                        System.out.println("3. Withdraw funds from an account");
                        System.out.println("4. Check if an account exists");
                        System.out.println("0. Back to main menu");
                        System.out.print("Enter your choice: ");
                        String accountChoice = scanner.nextLine();

                        switch (accountChoice) {
                            case "1":
                                System.out.print("Enter account name: ");
                                String accountName = scanner.nextLine();

                                System.out.print("Enter account number: ");
                                String accountNumber = scanner.nextLine();

                                System.out.print("Enter bank name: ");
                                String bankName = scanner.nextLine();

                                System.out.print("Enter initial balance: ");
                                double initialBalance = Double.parseDouble(scanner.nextLine());


                                bankAccountManager.addBankAccount(accountName, accountNumber, bankName, initialBalance);
                                break;
                            case "2":
                                bankAccountManager.viewAllAccounts();
                                break;
                            case "3":
                                System.out.print("Enter account number: ");
                                String accNum = scanner.nextLine();
                                if (bankAccountManager.accountExists(accNum)) {
                                    System.out.print("Enter amount to withdraw: ");
                                    double withdrawAmount = Double.parseDouble(scanner.nextLine());
                                    System.out.print("Enter expenditure code for this withdrawal: ");
                                    String expenditureCode = scanner.nextLine();
                                    bankAccountManager.withdrawFromAccount(accNum, withdrawAmount, expenditureCode);

                                } 
                                else {
                                    System.out.println("Error: Account not found.");
                                }
                                break;
                            case "4":
                                System.out.print("Enter account number to check: ");
                                String checkAccNum = scanner.nextLine();
                                if (bankAccountManager.accountExists(checkAccNum)) {
                                    System.out.println("Account exists.");
                                } else {
                                    System.out.println("Account does not exist.");
                                }
                                break;
                            case "0":
                                accountRunning = false;
                                break;
                            default:
                                System.out.println("Invalid input. Try again.");
                        }
                        System.out.println();
                    }
                    break;
                case "7":
                    System.out.println("----- Receipt Upload & Review -----");
                    System.out.println("1. Upload a receipt to queue");
                    System.out.println("2. Review next receipt");
                    System.out.println("3. View all pending receipts");
                    System.out.print("Choose option: ");
                    String receiptChoice = scanner.nextLine();

                    if (receiptChoice.equals("1")) {
                        System.out.print("Enter expenditure code to upload receipt: ");
                        String coded = scanner.nextLine();
                        expenditureManager.uploadReceipt(coded);
                    } else if (receiptChoice.equals("2")) {
                        expenditureManager.reviewNextReceipt();
                    } else if (receiptChoice.equals("3")) {
                        expenditureManager.reviewAllPendingReceipts();
                    } else {
                        System.out.println("Invalid receipt option.");
                    }
                    break;

                case "8":
                    System.out.print("Enter balance threshold (e.g., 1000.0): ");
                    double threshold = Double.parseDouble(scanner.nextLine());
                    bankAccountManager.showLowBalanceAccounts(threshold);
                    break;
                
                case "9":
                    System.out.println("----- Financial Analysis Report -----");
                    System.out.println("1. Show Monthly Burn Rate");
                    System.out.println("2. Forecast Profitability");
                    System.out.println("3. Analyze Building Material Prices");
                    System.out.print("Choose option: ");
                    String reportChoice = scanner.nextLine();

                    if (reportChoice.equals("1")) {
                        expenditureManager.showMonthlyBurnRate();
                    } else if (reportChoice.equals("2")) {
                        System.out.print("Enter expected income (e.g., 100000): ");
                        double income = Double.parseDouble(scanner.nextLine());
                        expenditureManager.forecastProfitability(income);
                    } else if (reportChoice.equals("3")) {
                        expenditureManager.analyzeMaterialImpact();
                    } else {
                        System.out.println("Invalid report option.");
                    }
                    break;
    
                case "0":
                    categoryManager.saveToFile("categories.txt");
                    running = false;
                    System.out.println("Exiting... Goodbye!");
                    break;

                default:
                    System.out.println("Invalid input. Try again.");

            }
            System.out.println();
        } 
        // save the receipt queue to file before exiting
        expenditureManager.saveReceiptQueue("Receipt.txt");

        // Save bank accounts to file before exiting
        bankAccountManager.saveAccountsToFile("Accounts.txt");
        // Save categories to file before exiting
        categoryManager.saveToFile("categories.txt");
        // Save expenditures to file before exiting
        expenditureManager.saveToFile("expenditures.txt");
        scanner.close();
    }
    
}
