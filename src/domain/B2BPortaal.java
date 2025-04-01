package domain;



import static org.mockito.ArgumentMatchers.endsWith;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dto.BedrijfDto;
import dto.BestellingDto;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import repository.BedrijvenDao;
import repository.BedrijvenDaoJpa;
import repository.BestellingDao;
import repository.BestellingDaoJpa;
import repository.BetalingsherinneringDao;
import repository.BetalingsherinneringDaoJpa;
import repository.GebruikerDao;
import repository.GebruikerDaoJpa;
import repository.GenericDaoJpa;

public class B2BPortaal {

	private BedrijvenDao bedrijvenDao;
	private BestellingDao bestellingDao;
	private BetalingsherinneringDao betalingsherrineringDao;
	private ObservableList<B2BBedrijf> bedrijvenSource;
	private FilteredList<B2BBedrijf> bedrijven;
	private ObservableList<Bedrijf> klantenSource;
	private FilteredList<Bedrijf> klanten;
	private ObservableList<B2BBestelling> bestellingenSource;
	private FilteredList<B2BBestelling> bestellingen;
	private GebruikerDao gebruikerDao;
	private ObservableList<B2BBetalingsherinnering> herinneringenSource;
	private FilteredList<B2BBetalingsherinnering> herinneringen;

    public B2BPortaal(BedrijvenDao bd, BestellingDao bd2, BetalingsherinneringDao bd3) {
    	this.bedrijvenDao = bd;
    	this.bestellingDao = bd2;
    	this.betalingsherrineringDao = bd3;
    	this.gebruikerDao = new GebruikerDaoJpa();
        //this.bedrijvenDao = new BedrijvenDaoJpa();
        //this.bestellingDao = new BestellingDaoJpa();
    }

    public FilteredList<B2BBedrijf> geefBedrijven() {
    	if (bedrijven == null) {
    		this.bedrijvenSource = FXCollections.observableArrayList(bedrijvenDao.findAll());
        	bedrijven = new FilteredList<>(bedrijvenSource, null);
    	}
        return bedrijven;
    }
    
    public void maakBedrijf(B2BBedrijf bedrijf, B2BGebruiker klant, B2BGebruiker leverancier) throws InsertionError {
    	try {
    		GenericDaoJpa.startTransaction();
    		gebruikerDao.insert(klant);
    		gebruikerDao.insert(leverancier);
    		bedrijvenDao.insert(bedrijf);
    		GenericDaoJpa.commitTransaction();
    		bedrijvenSource.add(bedrijf);
		} catch (Error | Exception e) {
//			GenericDaoJpa.rollbackTransaction();
			throw new InsertionError("Er is iets fout gegaan tijdens het toevoegen van een bedrijf\nDetails: " + e.getMessage());
		}
    }
    
    public B2BBedrijf geefBedrijf(int bedrijfId) {
    	return bedrijvenDao.get(bedrijfId);
    }
    
    public B2BBedrijf geefBedrijfBijNaam(String naam) {
    	return bedrijvenDao.getBedrijfByName(naam);
    }

	public void updateBedrijf(B2BBedrijf b) throws InsertionError {
		try {
    		GenericDaoJpa.startTransaction();
    		bedrijvenDao.update(b);
    		GenericDaoJpa.commitTransaction();
    		// TODO update item in observableList
    		bedrijvenSource.remove(b);
    		bedrijvenSource.add(b.getId()-1, b);
    		bedrijven.setPredicate(bedrijf -> true);
		} 
		catch (Exception e) {
			GenericDaoJpa.rollbackTransaction();
			throw new InsertionError("Er is iets fout gegaan tijdens het updaten van een bedrijf\nDetails: " + e.getMessage());
		}
	}
	 public FilteredList<Bedrijf> geefBedrijvenByLeverancier(Bedrijf leverancier){
	    	List<B2BBestelling> bestellingenBijLeverancier  = bestellingDao.getBestellingByLeverancier(leverancier);
	    	Set<Bedrijf> klantenSet = new HashSet<>();
	    	 for (Bestelling bestelling : bestellingenBijLeverancier) {
	             klantenSet.add(bestelling.getKlant());
	         }
	    	if(this.klanten== null) {
	    		this.klantenSource = FXCollections.observableArrayList(klantenSet).sorted();
	    		this.klanten = new FilteredList<>(klantenSource);
	    	}
	    	return this.klanten;
	    }
	
	public void updateBestelling(B2BBestelling selectedBestelling) throws InsertionError{
		try {
    		GenericDaoJpa.startTransaction();
    		bestellingDao.update(selectedBestelling);
    		GenericDaoJpa.commitTransaction();
    		
    		bestellingenSource.remove(selectedBestelling);
    		bestellingenSource.add(selectedBestelling.getOrderId()-1, selectedBestelling);
    		bestellingen.setPredicate(bestelling -> true);
		} catch (UnsupportedOperationException e) {
			throw e;
		} catch (Exception e) {
			GenericDaoJpa.rollbackTransaction();
			throw new InsertionError("Er is iets fout gegaan tijdens het updaten van een bestelling\nDetails: " + e.getMessage());
		}
	}
	
	
    public ObservableList<B2BBestelling> geefBestellingen() {
        List<B2BBestelling> bestellingen = bestellingDao.findAll();
        return FXCollections.observableArrayList(bestellingen);
    }
    
    public FilteredList<B2BBestelling> geefBestellingenByLeverancier(Bedrijf leverancier){
    	if(this.bestellingen == null) {
    		List<B2BBestelling> bestellingenList = bestellingDao.getBestellingByLeverancier(leverancier);
    		this.bestellingenSource = FXCollections.observableArrayList(bestellingenList);
    		this.bestellingen = new FilteredList<>(bestellingenSource);
    	}
    	return this.bestellingen;
    }
    
    public List<B2BBetalingsherinnering> getBetalingsherrineringenByBestelling(Bestelling bestelling) {
    	List<B2BBetalingsherinnering> betalingsherrineringen = betalingsherrineringDao.getBetalingsherinneringenByBestelling(bestelling);
    	return betalingsherrineringen;
    }
    
    public void stuurBetalingsherinnering(B2BBetalingsherinnering betalingsherinnering) throws InsertionError {
    	try {
    		GenericDaoJpa.startTransaction();
        	betalingsherrineringDao.insert(betalingsherinnering);
    		GenericDaoJpa.commitTransaction();
		} catch (UnsupportedOperationException e) {
			throw e;
		} catch (Exception e) {
			GenericDaoJpa.rollbackTransaction();
			throw new InsertionError("Er is iets fout gegaan tijdens bij het toevoegen van een betalingsherinnering \nDetails: " + e.getMessage());
		}
    }
}
