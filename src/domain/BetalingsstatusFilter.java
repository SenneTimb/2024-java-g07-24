package domain;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class BetalingsstatusFilter implements Filter<Bestelling>{

	public void filter(FilteredList<Bestelling> bestellingen, String filterWaarde) {
       bestellingen.setPredicate(bestelling -> bestelling.getBetalingsstatus().toString().toLowerCase().contains(filterWaarde.toLowerCase()));
    }

}
