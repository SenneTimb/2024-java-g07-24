package testen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import domain.B2BBedrijf;
import domain.B2BPortaal;
import domain.InsertionError;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import repository.BedrijvenDao;
import repository.BedrijvenDaoJpa;
import repository.BestellingDao;
import repository.BestellingDaoJpa;
import repository.BetalingsherinneringDao;
import repository.BetalingsherinneringDaoJpa;
import repository.GenericDaoJpa;

public class B2BPortaalTest {
	
	BedrijvenDao bd = new BedrijvenDaoJpa();
	BestellingDao bd2 = new BestellingDaoJpa();
	BetalingsherinneringDao bd3 = new BetalingsherinneringDaoJpa();
	B2BPortaal portaal = new B2BPortaal(bd, bd2, bd3);

	@Test
	public void aanmakenB2BPortaal() {
		BedrijvenDao bd = new BedrijvenDaoJpa();
		BestellingDao bd2 = new BestellingDaoJpa();

		Assertions.assertDoesNotThrow(() -> new B2BPortaal(bd, bd2, bd3));
	}
	
	/*@Test
	public void maakBedrijf() throws InsertionError {
		//GenericDaoJpa.startTransaction();
		B2BBedrijf bedrijf = new B2BBedrijf("testBedrijf", null, "testsector", "testAdres", "testGegevens",
				"BE0999999999");
		portaal.maakBedrijf(bedrijf);
		//GenericDaoJpa.commitTransaction();
	}
	
	@Test
	public void geefBedrijven() throws InsertionError {
		//GenericDaoJpa.startTransaction();
		B2BBedrijf bedrijf1 = new B2BBedrijf("testBedrijf", null, "testsector", "testAdres", "testGegevens", "BE0999999999");
		B2BBedrijf bedrijf2 = new B2BBedrijf("testBedrijf2", null, "testsector2", "testAdres2", "testGegevens2", "BE0999999999");
		
		portaal.maakBedrijf(bedrijf1);
		portaal.maakBedrijf(bedrijf2);
		
        //GenericDaoJpa.commitTransaction();
		
		ObservableList<B2BBedrijf> expectedList = FXCollections.observableArrayList(bedrijf1, bedrijf2);
		Assertions.assertEquals(expectedList, portaal.geefBedrijven());
	}*/
	
	
}
