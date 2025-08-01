package nkwarealestate.managers;

import nkwarealestate.models.Expenditure;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class SearchAlgorithmClass {

    public static List<Expenditure> searchByCategory(List<Expenditure> data, String category) {
        List<Expenditure> result = new ArrayList<>();
        for (Expenditure e : data) {
            if (e.getCategory().equalsIgnoreCase(category)) {
                result.add(e);
            }
        }
        return result;
    }

    public static List<Expenditure> searchByDateRange(List<Expenditure> data, LocalDate start, LocalDate end) {
        List<Expenditure> result = new ArrayList<>();
        for (Expenditure e : data) {
            if (!e.getDate().isBefore(start) && !e.getDate().isAfter(end)) {
                result.add(e);
            }
        }
        return result;
    }

    public static List<Expenditure> searchByCostRange(List<Expenditure> data, double min, double max) {
        List<Expenditure> result = new ArrayList<>();
        for (Expenditure e : data) {
            if (e.getAmount() >= min && e.getAmount() <= max) {
                result.add(e);
            }
        }
        return result;
    }

    public static List<Expenditure> searchByAccountNumber(List<Expenditure> data, String accNo) {
        List<Expenditure> result = new ArrayList<>();
        for (Expenditure e : data) {
            if (e.getAccountNumber().equalsIgnoreCase(accNo)) {
                result.add(e);
            }
        }
        return result;
    }
}
