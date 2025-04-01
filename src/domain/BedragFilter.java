package domain;

import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class BedragFilter implements Filter<Bestelling>{

	public void filter(FilteredList<Bestelling> bestellingen, String filterWaarde) {
        double bedrag = Double.parseDouble(filterWaarde);
        Predicate<Bestelling> filterPredicate = bestelling -> bestelling.getTotaleOrderBedrag() == bedrag;
        bestellingen.setPredicate(filterPredicate);
    }
}