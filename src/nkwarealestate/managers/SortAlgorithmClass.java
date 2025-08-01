package nkwarealestate.managers;

import nkwarealestate.models.Expenditure;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class SortAlgorithmClass {

    public static void sortByCategory(List<Expenditure> data) {
        Collections.sort(data, Comparator.comparing(e -> e.getCategory().toLowerCase()));
    }

    public static void sortByDate(List<Expenditure> data) {
        Collections.sort(data, Comparator.comparing(Expenditure::getDate));
    }
}
