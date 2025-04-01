package domain;

public class FilterFactory {

	public Filter CreateFilter(String filterType) {
		return switch (filterType) {
			case "Adres" -> new AdresFilter();
			case "Datum" -> new DatumFilter();
			case "Bedrag" -> new BedragFilter();
			case "Klant" -> new KlantFilter();
			case "Betalingsstatus" -> new BetalingsstatusFilter();
			case "Orderstatus" -> new OrderstatusFilter();
			case "Naam" -> new NaamFilter();
			case "Sector" -> new SectorFilter();
			case "ContactGegevens" -> new ContactGegevensFilter();
			case "BTWnummer" -> new BTWnummerFilter();
			default -> null;
		};
	}
}
