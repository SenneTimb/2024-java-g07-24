package domain;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class OrderstatusFilter implements Filter<Bestelling>{

	public void filter(FilteredList<Bestelling> bestellingen, String filterWaarde) {
        bestellingen.setPredicate(bestelling -> bestelling.getOrderStatus().toString().toLowerCase().contains(filterWaarde.toLowerCase()));
    }

}
