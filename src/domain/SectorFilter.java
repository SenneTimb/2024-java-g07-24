package domain;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class SectorFilter implements Filter<Bedrijf>{

	@Override
	public void filter(FilteredList<Bedrijf> bestellingen, String filterWaarde) {
        bestellingen.setPredicate(bestelling -> bestelling.getSector().toString().toLowerCase().equals(filterWaarde.toLowerCase()));
    }


}
