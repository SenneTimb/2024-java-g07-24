package domain;

import repository.GebruikerDaoJpa;
import repository.GenericDao;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import repository.BedrijvenDaoJpa;
import repository.BestellingDaoJpa;
import repository.BetalingsherinneringDaoJpa;
import repository.GebruikerDao;
import repository.GenericDaoJpa;
import repository.ProductDaoJpa;

public class PopulateDB {
    public void run() {
    	BestellingDaoJpa bestellingDaoJpa = new BestellingDaoJpa();
        GebruikerDaoJpa gebruikerDaoJpa = new GebruikerDaoJpa();
        ProductDaoJpa productDaoJpa = new ProductDaoJpa();
        BedrijvenDaoJpa bedrijvenDaoJpa = new BedrijvenDaoJpa();
        BetalingsherinneringDaoJpa betalingsherrineringDaoJpa = new BetalingsherinneringDaoJpa();
        
        
     // Start transaction for inserting users
        GenericDaoJpa.startTransaction();
        String hashPassword = Argon2PasswordEncoder.encode("1234");
        gebruikerDaoJpa.insert(new B2BGebruiker("admin@admin.com", hashPassword, true, Rol.ADMIN));
        
        
        // Add more users as needed
        GenericDaoJpa.commitTransaction();

        // Start transaction for inserting products
        GenericDaoJpa.startTransaction();
        B2BProduct p1 = new B2BProduct("Laptop", 10, true, 999.99);
        B2BProduct p2 = new B2BProduct("Smartphone", 15, true, 599.99);
        B2BProduct p3 = new B2BProduct("Smartphone", 4, true, 599.99);
        B2BProduct p4 = new B2BProduct("Tablet", 20, true, 349.99);
        B2BProduct p5 = new B2BProduct("Desktop Computer", 5, true, 1200.99);
        B2BProduct p6 = new B2BProduct("Printer", 15, true, 199.99);
        B2BProduct p7 = new B2BProduct("External Hard Drive", 30, true, 89.99);
        
        productDaoJpa.insert(p1);
        productDaoJpa.insert(p2);
        productDaoJpa.insert(p3);
        productDaoJpa.insert(p4);
        productDaoJpa.insert(p5);
        productDaoJpa.insert(p6);
        productDaoJpa.insert(p7);
  
        GenericDaoJpa.commitTransaction();

        B2BBedrijf leverancier = new B2BBedrijf("TechSolutionsLeverancier", new byte[]{}, "Technology", "123 Tech Street", "info@techsolutions.com", "BE0000111222");
        B2BGebruiker leverancierGebruiker = new B2BGebruiker("test@test.com", hashPassword, true, Rol.LEVERANCIER);
        B2BGebruiker leverancierKlant = new B2BGebruiker("test@test.com", hashPassword, true, Rol.KLANT);
        
        leverancierGebruiker.setB(leverancier);
        leverancier.voegGebruikerToe(leverancierGebruiker);
        
        leverancierKlant.setB(leverancier);
        leverancier.voegGebruikerToe(leverancierKlant);
        
        byte [] tech = new byte[] {};
        byte [] med = new byte[] {};
        byte [] green = new byte[] {};
        byte[][] images = new byte[][] {tech, med, green};
        try {
        	File file = new File("src/assets/TechSolutions.jpg");
        	File file2 = new File("src/assets/MedFusion.jpg");
        	File file3 = new File("src/assets/GreenHorizon.jpg");
        	
        	File[] files = new File[] {file, file2, file3};
        	
        	for (int i = 0; i < files.length ; i ++) {
        		BufferedImage bImage = ImageIO.read(files[i]);
        		ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bImage, "jpg", bos );
                byte [] image = bos.toByteArray();
                images[i] = image;

        	}

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        B2BBedrijf klant = new B2BBedrijf("TechSolutions", images[0], "Technology","123 Tech Street", "info@techsolution.com", "BE0000111222");
        
        B2BGebruiker klantLeveranvcier = new B2BGebruiker("info@techsolution.com", hashPassword, true, Rol.LEVERANCIER);
        B2BGebruiker klantKlant = new B2BGebruiker("info@techsolution.com", hashPassword, true, Rol.KLANT);
        
        klantLeveranvcier.setB(klant);
        klant.voegGebruikerToe(klantLeveranvcier);
        klantKlant.setB(klant);
        klant.voegGebruikerToe(klantKlant);
        
        B2BBedrijf klant2 = new B2BBedrijf("Medfusion Health", images[1], "Healthcare" , "62 Health Street", "noreplydelaware@gmail.com", "BE0000111245");
        
        B2BGebruiker klant2Leveranvcier = new B2BGebruiker("info@medfusion.com", hashPassword, true, Rol.LEVERANCIER);
        B2BGebruiker klant2Klant = new B2BGebruiker("info@medfusion.com", hashPassword, true, Rol.KLANT);
        
        klant2Leveranvcier.setB(klant2);
        klant2.voegGebruikerToe(klant2Leveranvcier);
        klant2Klant.setB(klant2);
        klant2.voegGebruikerToe(klant2Klant);
        
        B2BBedrijf klant3 = new B2BBedrijf("GreenHorizon Renewables", images[2], "Energy" , "28 Energy Street", "info@greenhorizon.com", "BE9900111220");

        B2BGebruiker klant3Leveranvcier = new B2BGebruiker("info@greenhorizon.com", hashPassword, true, Rol.LEVERANCIER);
        B2BGebruiker klant3Klant = new B2BGebruiker("info@greenhorizon.com", hashPassword, true, Rol.KLANT);
        
        klant3Leveranvcier.setB(klant3);
        klant3.voegGebruikerToe(klant3Leveranvcier);
        klant3Klant.setB(klant3);
        klant3.voegGebruikerToe(klant3Klant);
       
        // Start transaction for inserting orders
        GenericDaoJpa.startTransaction();
        gebruikerDaoJpa.insert(leverancierGebruiker);
        gebruikerDaoJpa.insert(leverancierKlant);
        
        gebruikerDaoJpa.insert(klantLeveranvcier);
        gebruikerDaoJpa.insert(klantKlant);
        
        gebruikerDaoJpa.insert(klant2Leveranvcier);
        gebruikerDaoJpa.insert(klant2Klant);
        
        gebruikerDaoJpa.insert(klant3Leveranvcier);
        gebruikerDaoJpa.insert(klant3Klant);
        
        B2BBestelling bestelling = new B2BBestelling(LocalDate.now(), "1234 Main St", Orderstatus.GELEVERD, Betalingsstatus.BETAALD, leverancier, klant, LocalDate.now().plusDays(10));
        bestelling.voegTestProductToe(p1, 1);
        bestelling.voegTestProductToe(p2, 2);
        
        B2BBestelling bestelling2 = new B2BBestelling(LocalDate.now(), "1234 Main St", Orderstatus.VERWERKT, Betalingsstatus.FACTUURVERZONDEN, leverancier, klant2, LocalDate.now().plusDays(2));
        bestelling2.voegTestProductToe(p3, 2);
        
        
        
        B2BBestelling bestelling3 = new B2BBestelling(LocalDate.now(), "1234 Main St", Orderstatus.GELEVERD, Betalingsstatus.FACTUURVERZONDEN, leverancier, klant2, LocalDate.now().plusDays(2));
        bestelling3.voegTestProductToe(p1, 5);
        
        B2BBestelling bestelling4 = new B2BBestelling(LocalDate.now(), "1234 Main St", Orderstatus.GELEVERD, Betalingsstatus.ONVERWERKT, leverancier, klant2, LocalDate.now().plusDays(2));
        bestelling4.voegTestProductToe(p2, 5);
        
        B2BBestelling bestelling5 = new B2BBestelling(LocalDate.now(), "1234 Main St", Orderstatus.VOLTOOID, Betalingsstatus.FACTUURVERZONDEN, leverancier, klant2, LocalDate.now().plusDays(2));
        bestelling5.voegTestProductToe(p3, 7);
        
        B2BBestelling bestelling6 = new B2BBestelling(LocalDate.now(), "1234 Main St", Orderstatus.VERZONDEN, Betalingsstatus.FACTUURVERZONDEN, leverancier, klant2, LocalDate.now().plusDays(2));
        bestelling6.voegTestProductToe(p1, 12);
        
        B2BBestelling bestelling7 = new B2BBestelling(LocalDate.now(), "1234 Main St", Orderstatus.VERZONDEN, Betalingsstatus.BETAALD, leverancier, klant, LocalDate.now().plusDays(10));
        bestelling7.voegTestProductToe(p1, 4);
        bestelling7.voegTestProductToe(p2, 5);
        
        B2BBestelling bestelling8 = new B2BBestelling(LocalDate.now(), "1234 Main St", Orderstatus.GELEVERD, Betalingsstatus.FACTUURVERZONDEN, leverancier, klant, LocalDate.now().plusDays(10));
        bestelling8.voegTestProductToe(p1, 6);
        bestelling8.voegTestProductToe(p2, 7);
        
        bestellingDaoJpa.insert(bestelling);
        bestellingDaoJpa.insert(bestelling2);
        bestellingDaoJpa.insert(bestelling3);
        bestellingDaoJpa.insert(bestelling4);
        bestellingDaoJpa.insert(bestelling5);
        bestellingDaoJpa.insert(bestelling6);
        bestellingDaoJpa.insert(bestelling7);
        bestellingDaoJpa.insert(bestelling8);

        bedrijvenDaoJpa.insert(klant);
        bedrijvenDaoJpa.insert(klant2);
        bedrijvenDaoJpa.insert(klant3);
        bedrijvenDaoJpa.insert(leverancier);
       
       
        GenericDaoJpa.commitTransaction();
    }
        
}