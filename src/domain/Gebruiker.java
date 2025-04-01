package domain;

public interface Gebruiker {

	public int getGebruikerID();
	public String getEmail();
	public String getWachtwoord();
	public boolean isActief();
	public Rol getRol();
	public Bedrijf getB();
	
}
