package domain;

import java.util.List;

import javax.mail.MessagingException;

import dto.BedrijfDto;
import dto.BestellingDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;


public abstract class DomeinController<E> {
	
	protected B2BPortaal portaal;
	protected B2BGebruiker gebruiker;
	protected FilterFactory filterFactory = new FilterFactory();
	
	public DomeinController(B2BPortaal portaal, B2BGebruiker gebruiker) {
		this.portaal = portaal;
		this.gebruiker = gebruiker;
	}
	
	public boolean isAdmin() {
		return gebruiker.getRol() == Rol.ADMIN;
	}
	
	public void logOut() {
		this.gebruiker = null;
	}
	
	public abstract FilteredList<Bedrijf> geefBedrijven();
	
	public void maakBedrijf(BedrijfDto bedrijf) throws InsertionError, MessagingException {
		throw new UnsupportedOperationException("geen toegang tot deze operatie");
	};
	
	public void updateBedrijf(BedrijfDto bedrijf) throws InsertionError {
		throw new UnsupportedOperationException("geen toegang tot deze operatie");
	}
	
	public void updateBedrijfGebruikerStatus(boolean status) throws InsertionError {
		throw new UnsupportedOperationException("geen toegang tot deze operatie");
	}
	
	public abstract Bedrijf geefBedrijf(int id);
	
	public void setSelectedBedrijf(int bedrijf) {
		throw new UnsupportedOperationException("geen toegang tot deze operatie");
	}
	
	public Bedrijf getSelectedBedrijf() {
		throw new UnsupportedOperationException("geen toegang tot deze operatie");
	}

	public List<Gebruiker> getSelectedBedrijfGebruikers() {
		throw new UnsupportedOperationException("geen toegang tot deze operatie");
	}
	
	
	public List<Betalingsherinnering> getBetalingsherrineringenByBestelling() {
		throw new UnsupportedOperationException("geen toegang tot deze operatie");
	}
	
	
	
	public FilteredList<Bestelling> geefBestellingen(){
		throw new UnsupportedOperationException("geen toegang tot deze operatie");
	};
	
	public void setSelectedBestelling(int bedrijf) {
		throw new UnsupportedOperationException("geen toegang tot deze operatie");
	}
	public Bestelling getSelectedBestelling() {
		throw new UnsupportedOperationException("geen toegang tot deze operatie");
	}

	public B2BBedrijf geefBestelling() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public B2BGebruiker getGebruiker() {
		return this.gebruiker;
	}

	public FilteredList<Bestelling> geefBestellingenByLeverancier() {
		throw new UnsupportedOperationException("geen toegang tot deze operatie");
	}
	
	public void updateBestelling(Bestelling bestelling) throws InsertionError {
		throw new UnsupportedOperationException("geen toegang tot deze operatie");
	}

	public void updateBestelling(BestellingDto dto) throws InsertionError {
		throw new UnsupportedOperationException("geen toegang tot deze operatie");	
	}
	
	public abstract B2BPortaal getPortaal();

	public abstract void filter(String selectedOptie, String filterWaarde, String filterenOp);
	
	public void stuurBetalingsherinnering() throws InsertionError {
		throw new UnsupportedOperationException("geen toegang tot deze operatie");	
	}
}
