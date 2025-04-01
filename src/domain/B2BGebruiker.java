package domain;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity(name = "Gebruiker")
@NamedQueries({
    @NamedQuery(name = "Gebruiker.findByEmail",
                    query = "select g from Gebruiker g where g.email = :gebruikerEmail and g.rol <> :gebruikerRol"),          
    @NamedQuery(name = "Gebruiker.findByBedrijfId",
    				query = "select g from Gebruiker g where g.b = :bedrijfId")
})
public class B2BGebruiker implements Serializable, Gebruiker{

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gebruikerID;
	@Column(nullable = false)
    private String email;
	@Column(nullable = false)
    private String wachtwoord;
	@Column(nullable = false)
    private boolean isActief;
	@Column(nullable = false)
    private Rol rol;
    @ManyToOne
    @JoinColumn(name = "bedrijfId")
    private B2BBedrijf b;
    
    protected B2BGebruiker() {}
    
    public B2BGebruiker(String email, String wachtwoord, boolean isActief, Rol rol) {
    	this.email = email;
    	this.wachtwoord = wachtwoord;
    	this.isActief = isActief;
    	this.rol = rol;
    }
    
	
    public int getGebruikerID() {
		return gebruikerID;
	}

	public String getEmail() {
		return email;
	}

	public String getWachtwoord() {
		return wachtwoord;
	}

	public boolean isActief() {
		return isActief;
	}

	public Rol getRol() {
		return rol;
	}

	public Bedrijf getB() {
		return b;
	}

	public void setGebruikerID(int gebruikerID) {
		this.gebruikerID = gebruikerID;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setWachtwoord(String wachtwoord) {
		this.wachtwoord = wachtwoord;
	}


	public void setActief(boolean isActief) {
		this.isActief = isActief;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public void setB(B2BBedrijf b) {
		this.b = b;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (isActief ? 1231 : 1237);
		result = prime * result + ((rol == null) ? 0 : rol.hashCode());
		result = prime * result + ((wachtwoord == null) ? 0 : wachtwoord.hashCode());
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
		B2BGebruiker other = (B2BGebruiker) obj;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (isActief != other.isActief)
			return false;
		if (rol != other.rol)
			return false;
		if (wachtwoord == null) {
			if (other.wachtwoord != null)
				return false;
		} else if (!wachtwoord.equals(other.wachtwoord))
			return false;
		return true;
	}
	
}
