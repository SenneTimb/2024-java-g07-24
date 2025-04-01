package domain;

import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class DatumFilter implements Filter<Bestelling>{

	@Override
	public void filter(FilteredList<Bestelling> bestellingen, String filterWaarde) {
	    bestellingen.setPredicate(bestelling -> (bestelling.getDatumGeplaatst()).toString().contains(filterWaarde));
	}

}