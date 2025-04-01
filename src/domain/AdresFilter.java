package domain;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class AdresFilter implements Filter {

    @Override
    public void filter(FilteredList items, String filterWaarde) {
        items.setPredicate(item -> {
            if (item instanceof Bestelling) {
                return ((Bestelling) item).getLeverAdres().toLowerCase().contains(filterWaarde.toLowerCase());
            } else if (item instanceof Bedrijf) {
                return ((Bedrijf) item).getAdres().toLowerCase().contains(filterWaarde.toLowerCase());
            }
            return false; // In dit geval is het object geen Bestelling of Bedrijf
        });
    }
}
