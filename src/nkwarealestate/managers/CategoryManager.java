package nkwarealestate.managers;
import java.util.TreeSet;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



public class CategoryManager {
    private TreeSet<String> categories;

    public CategoryManager() {
        categories = new TreeSet<>();
    }

    // Add new category (returns false if it already exists)
    public boolean addCategory(String category) {
        if (categories.contains(category)) {
            System.out.println("Error: Category already exists.");
            return false;
        }

        categories.add(category);
        System.out.println("Category added successfully!");
        return true;
    }

    // view all categories
    public void viewAllCategories() {
        if (categories.isEmpty()) {
            System.out.println("No categories available yet.");
            return;
        }

        System.out.println("******* Expenditure Categories *******");
        for (String category : categories) {
            System.out.println("-" + category);
        }

    }

        // check if a category exists
        public boolean categoryExists(String category) {
            for (String existisng : categories) {
                if (existisng.equalsIgnoreCase(category)) {
                    return true;
                }
            }
            return false;
        
    }

    // Save categories to a file (not implemented)
    public void saveToFile(String filename) {
        try(FileWriter writer = new FileWriter(filename)) {
            for (String category : categories) {
                writer.write(category + "\n");
            }
        } catch (IOException e) {
            System.out.println("Failed to save categories to file.");
        }
    }

    // Load categories from a file (not implemented)
    public void loadFromFile(String filename) {
        try(BufferedReader reader = new BufferedReader (new FileReader (filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                categories.add(line.trim());
            }
            System.out.println("Categories loaded from " + filename);
        } catch (IOException e) {
            System.out.println("No categories found in file or file not found.");
        }
    }
} 
