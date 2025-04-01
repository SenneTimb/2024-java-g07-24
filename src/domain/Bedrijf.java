package domain;

import java.util.List;
import java.util.Set;

public interface Bedrijf {

	public int getId();
	public String getNaam();
	public byte[] getLogo();
	public String getAdres();
	public String getSector();
	public String getContactgegevens();
	public String getBTWnummer();
	public List<B2BGebruiker> getGebruikers();
	
}
