package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import dto.BedrijfDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity(name="Bedrijf")
@NamedQuery(name = "B2BBedrijf.findByName", query = "SELECT b FROM Bedrijf b WHERE b.naam = :bedrijfNaam")
public class B2BBedrijf implements Serializable, Bedrijf {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bedrijfID;
	@Column(unique = true, nullable = false)
    private String naam;
    private byte[] logo;
	@Column(nullable = false)
    private String adres;
	@Column(nullable = false)
    private String sector;
	@Column(nullable = false)
    private String contactgegevens;
	@Column(nullable = false)
    private String BTWnummer;
    
    
    @OneToMany(mappedBy = "b")
    private Set<B2BGebruiker> g;
   
    protected B2BBedrijf() {}

    public B2BBedrijf(String naam, byte[] logo, String sector, String adres, String contactgegevens, String BTWnummer) {
    	this.naam = naam;
    	this.logo = logo;
    	this.sector = sector;
    	this.adres = adres;
    	this.contactgegevens = contactgegevens;
    	this.BTWnummer = BTWnummer;
    	this.g = new HashSet<B2BGebruiker>();
    }
    
    //TODO Validatie aanpassen
    public B2BBedrijf(BedrijfDto bedrijf) throws InsertionError{
    	if(bedrijf.naam() == null || bedrijf.naam().isBlank() || bedrijf.naam().isEmpty()) throw new IllegalArgumentException("Naam van bedrijf mag niet leeg zijn");
		if(bedrijf.sector() == null || bedrijf.sector().isEmpty() || bedrijf.sector().isBlank()) throw new IllegalArgumentException("Sector van bedrijf mag niet leeg zijn");
		if(bedrijf.adres() == null || bedrijf.adres().isBlank() || bedrijf.adres().isEmpty()) throw new IllegalArgumentException("Adres van bedrijf mag niet leeg zijn");
		if(bedrijf.contactgegevens() == null || bedrijf.contactgegevens().isBlank() || bedrijf.contactgegevens().isEmpty()) throw new IllegalArgumentException("Contactgegevens van bedrijf mag niet leeg zijn");
		if(bedrijf.BTWnummer() == null || bedrijf.BTWnummer().isBlank() || bedrijf.BTWnummer().isEmpty()) throw new IllegalArgumentException("BTW nummer van bedrijf mag niet leeg zijn");

		Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(bedrijf.contactgegevens());
        
        if(!mat.matches()) throw new IllegalArgumentException("Contactgegevens van bedrijf kloppen niet zoals een email formaat");
		
        Pattern patternBtwNummer = Pattern.compile("BE[0-9]{10}");
        Matcher matBtw = patternBtwNummer.matcher(bedrijf.BTWnummer());
        
        if(!matBtw.matches()) throw new IllegalArgumentException("BTW nummer moet er als volgt uitzien BE0000111222");
        
        setNaam(bedrijf.naam());
        setSector(bedrijf.sector());
        setAdres(bedrijf.adres());
        setContactgegevens(bedrijf.contactgegevens());
        setBTWnummer(bedrijf.BTWnummer());
        setLogo(bedrijf.logo());
    	this.g = new HashSet<B2BGebruiker>();
	}

    
    public int getId() {
    	return bedrijfID;
    }
    
	public String getNaam() {
		return naam;
	}

	public void setNaam(String naam) {
		this.naam = naam;
	}

	public byte[] getLogo() {
		return logo;
	}

	public String getAdres() {
		return adres;
	}

	public String getSector() {
		return sector;
	}

	public String getContactgegevens() {
		return contactgegevens;
	}

	public String getBTWnummer() {
		return BTWnummer;
	}
	
	public List<B2BGebruiker> getGebruikers() {
		return g.stream().collect(Collectors.toList());
	}
	
	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	public void setAdres(String adres) {
		this.adres = adres;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public void setContactgegevens(String contactgegevens) {
		this.contactgegevens = contactgegevens;
	}

	public void setBTWnummer(String bTWnummer) {
		BTWnummer = bTWnummer;
	}
    
	public void voegGebruikerToe(B2BGebruiker gebruiker) {
		g.add(gebruiker);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((BTWnummer == null) ? 0 : BTWnummer.hashCode());
		result = prime * result + ((adres == null) ? 0 : adres.hashCode());
		result = prime * result + ((contactgegevens == null) ? 0 : contactgegevens.hashCode());
		result = prime * result + Arrays.hashCode(logo);
		result = prime * result + ((naam == null) ? 0 : naam.hashCode());
		result = prime * result + ((sector == null) ? 0 : sector.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		B2BBedrijf other = (B2BBedrijf) obj;
		if (BTWnummer == null) {
			if (other.BTWnummer != null)
				return false;
		} else if (!BTWnummer.equals(other.BTWnummer))
			return false;
		if (adres == null) {
			if (other.adres != null)
				return false;
		} else if (!adres.equals(other.adres))
			return false;
		if (contactgegevens == null) {
			if (other.contactgegevens != null)
				return false;
		} else if (!contactgegevens.equals(other.contactgegevens))
			return false;
		if (!Arrays.equals(logo, other.logo))
			return false;
		if (naam == null) {
			if (other.naam != null)
				return false;
		} else if (!naam.equals(other.naam))
			return false;
		if (sector == null) {
			if (other.sector != null)
				return false;
		} else if (!sector.equals(other.sector))
			return false;
		return true;
	}

}
