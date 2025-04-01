package domain;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
//import javax.activation.*;

import dto.BedrijfDto;
import jakarta.persistence.EntityNotFoundException;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.GebruikerDaoJpa;
import repository.GenericDao;
import repository.GenericDaoJpa;

public class AdminController extends DomeinController{
	
	private GebruikerDaoJpa gebruikerRepo;
	private B2BBedrijf selectedBedrijf;

	
    public AdminController(B2BPortaal portaal, B2BGebruiker gebruiker) {
        super(portaal, gebruiker);
        this.gebruikerRepo = new GebruikerDaoJpa();
    }

	@Override
    public FilteredList<Bedrijf> geefBedrijven() {
        return (FilteredList<Bedrijf>) (Object) portaal.geefBedrijven();
    }
	
	public void maakBedrijf(BedrijfDto bedrijf) throws InsertionError, MessagingException {
		String naam = bedrijf.naam();
		
		try {
			B2BBedrijf b = portaal.geefBedrijfBijNaam(naam);
			throw new InsertionError("Bedrijfsnaam moet uniek zijn");
		} catch (EntityNotFoundException e) {
			try {
				System.out.println("making bedrijf");
				B2BBedrijf newBedrijf = new B2BBedrijf(bedrijf); //aanpassing die moet gebeuren
			
				String hashPassword = Argon2PasswordEncoder.encode("wachtwoord");
				
			    B2BGebruiker klant = new B2BGebruiker(newBedrijf.getContactgegevens(), hashPassword, true, Rol.KLANT);
    		    B2BGebruiker leverancier = new B2BGebruiker(newBedrijf.getContactgegevens(), hashPassword, true, Rol.LEVERANCIER);
			
			    klant.setB(newBedrijf);
			    newBedrijf.voegGebruikerToe(klant);
    		
    		    leverancier.setB(newBedrijf);
    		    newBedrijf.voegGebruikerToe(leverancier);
    		
			    portaal.maakBedrijf(newBedrijf, klant, leverancier);
			    ExecutorService emailVerzendExecutor = Executors.newSingleThreadExecutor();
			
			    String content = "<h1>Uw account werd zonet aangemaakt</h1><p>Gelieve eens in te loggen met de volgende gegevens</p>"
					+ "<ul>"
					+ "<li><b>Klant account</b>		email: " + klant.getEmail() +", wachtwoord: " + klant.getWachtwoord() + "</li>"
					+ "<li><b>Leverancier account</b>		email: " + leverancier.getEmail() +", wachtwoord: " + leverancier.getWachtwoord() + "</li>"
					+ "</ul>";
			
			    emailVerzendExecutor.submit(() -> {
		            try {
		                GmailProvider.sendEmail(newBedrijf.getContactgegevens(), "Delaware portaal - Accounts aangemaakt", content);
		                System.out.println("Email verzonden op: " + Thread.currentThread().getName());
		            } catch (Exception f) {
		                f.printStackTrace();
		            } finally {
		                emailVerzendExecutor.shutdown();
		            }
		        });
			} catch (InsertionError e2) {
				System.out.println(e2.getMessage());
				throw e2;
			}  catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
		}
	}
	
	// TODO --> veranderen om met geselecteerde bedrijf te werken
	public void updateBedrijf(BedrijfDto bedrijf) throws InsertionError {
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
        
		try {
			
//			B2BBedrijf b = selectedBedrijf;
			selectedBedrijf.setNaam(bedrijf.naam());
			selectedBedrijf.setSector(bedrijf.sector());
			//AANPASSING TODO
			selectedBedrijf.setAdres(bedrijf.adres());
			selectedBedrijf.setLogo(bedrijf.logo());
			selectedBedrijf.setContactgegevens(bedrijf.contactgegevens());
			selectedBedrijf.setBTWnummer(bedrijf.BTWnummer());
			portaal.updateBedrijf(selectedBedrijf);
		} catch (InsertionError e) {
			throw e;
		}
	}
	
	// TODO lijst verandering --> methode veranderen om met geselecteerde bedrijf te werken
	public void updateBedrijfGebruikerStatus(boolean status) throws InsertionError {
		try {
			//GenericDaoJpa.startTransaction();
			selectedBedrijf.getGebruikers().forEach(gebruiker -> {
				gebruiker.setActief(status);
//				gebruikerRepo.update(gebruiker);
			});
			//GenericDaoJpa.commitTransaction();
		} catch (Exception e) {
			throw new InsertionError("Er is iets fout gegaan tijdens het updaten van een gebruiker\nDetails: " + e.getMessage());
		}
	}

	
	@Override
	public void setSelectedBedrijf(int bedrijf) {
		if(bedrijf >= 0) {
			selectedBedrijf = portaal.geefBedrijven().get(bedrijf);
		} else {
			selectedBedrijf = null;
		}
	}
	
	@Override
	public Bedrijf getSelectedBedrijf() {
		return (Bedrijf) selectedBedrijf;
	}
	
	@Override
	public List<Gebruiker> getSelectedBedrijfGebruikers() {
		return (List<Gebruiker>) (Object) selectedBedrijf.getGebruikers();
	}

	
	@Override
	public Bedrijf geefBedrijf(int id) {
		return portaal.geefBedrijf(id);
	}
	
	public B2BPortaal getPortaal() {
		return this.portaal;
	}

	@Override
	public void filter(String selectedOptie, String filterWaarde, String filterenOp) {
		Filter filter = filterFactory.CreateFilter(selectedOptie);
		
		filter.filter(geefBedrijven(), filterWaarde);
	}
}
