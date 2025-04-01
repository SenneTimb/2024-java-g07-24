package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;
import repository.BedrijvenDao;
import repository.BedrijvenDaoJpa;
import repository.BestellingDaoJpa;
import repository.BetalingsherinneringDao;
import repository.BetalingsherinneringDaoJpa;
import repository.GebruikerDao;
import repository.GebruikerDaoJpa;
import repository.GenericDao;
import repository.GenericDaoJpa;

public class LoginController {
	private B2BGebruiker gebruiker;
	private GebruikerDao gebruikerRepo;

	public LoginController() {
		this(false);
	}

	public LoginController(boolean withInit) {
		if (withInit) {
			new PopulateDB().run();
		}
		setGebruikerRepo(new GebruikerDaoJpa());
	}

	public void setGebruikerRepo(GebruikerDao mock) {
		gebruikerRepo = mock;
	}
	
	public DomeinController meldAan(String email, String wachtwoord) throws UserDataInvalidException {
		if(email == null || email.isBlank() || email.isEmpty()) throw new IllegalArgumentException("Email mag niet leeg zijn!");
		if(wachtwoord == null || wachtwoord.isBlank() || wachtwoord.isEmpty()) throw new IllegalArgumentException("Wachtwoord mag niet leeg zijn!");
		
		B2BGebruiker g = gebruikerRepo.getGebruikerByEmail(email);
		
		if(!g.isActief()) {
			throw new IllegalArgumentException("Gebruiker is gedeactiveert");
		}
		
		if(!Argon2PasswordEncoder.matches(wachtwoord, g.getWachtwoord())) {
			throw new UserDataInvalidException("Wachtwoord klopt niet met gegevens.");
		}
		
		
		this.gebruiker = g;
		
		if(gebruiker.getRol() == Rol.ADMIN)
			return new AdminController(new B2BPortaal(new BedrijvenDaoJpa(), new BestellingDaoJpa(), new BetalingsherinneringDaoJpa()), g);
		
		return new LeverancierController(new B2BPortaal(new BedrijvenDaoJpa(), new BestellingDaoJpa(), new BetalingsherinneringDaoJpa()), g);
	}
	
//	private String getGebruikerByEmail() {
//		Gebruiker g = gebruikerRepo.getGebruikerByEmail("admin@admin.com");
//		
//		return String.format("%s %s", g.getEmail(), g.getRol().toString());
//	}
	
	public void close() {
		GenericDaoJpa.closePersistency();
	}

}
