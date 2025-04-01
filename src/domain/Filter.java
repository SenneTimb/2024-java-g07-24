package domain;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public interface Filter<E> {

	public void filter(FilteredList<E> bestellingen, String filterWaarde);
		
}
