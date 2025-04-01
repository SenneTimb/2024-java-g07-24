package domain;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class BTWnummerFilter implements Filter<Bedrijf>{

	public void filter(FilteredList<Bedrijf> bestellingen, String filterWaarde) {
        bestellingen.setPredicate(bedrijf -> bedrijf.getBTWnummer().contains(filterWaarde));
    }


}
