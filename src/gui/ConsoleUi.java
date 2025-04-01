package gui;

import domain.LoginController;
import domain.UserDataInvalidException;
import jakarta.persistence.EntityNotFoundException;

import java.util.Scanner;

public class ConsoleUi {
    private final LoginController loginController;
    private final Scanner in = new Scanner(System.in);
    public ConsoleUi(LoginController dc) {
       loginController = dc;
    }

    public void run() {
        doStandardJob();
        loginController.close();
    }

    private void doStandardJob() {
    	
    	System.out.println("--- Meld aan ---");
    	System.out.print("Email: ");
    	String email = in.nextLine();
    	System.out.print("Wachtwoord: ");
    	String ww = in.nextLine();
    	
    	try {
    		System.out.println(email + " " + ww);
    		if(loginController.meldAan(email, ww) != null) System.out.println("SUCCES !!");
    		else System.out.println("FAILED !!");
    	} catch (IllegalArgumentException ex) {
    		System.out.println("Operatie mislukt " + ex.getMessage());
    	} catch (EntityNotFoundException | UserDataInvalidException e) {
			System.out.println("Gebruiker bestaat niet");
		}
    	
    	
//        System.out.println("BierWinkelSysteem Actief");
//        System.out.println("Winkels :");
//        System.out.println(domeinController.geefWinkelList());
//        System.out.println("Geef winkelnaam voor toe te voegen bier");
//        String winkelNaam = in.nextLine();
//        System.out.println("Geef naam van toe te voegen bier");
//        String bierNaam = in.nextLine();
//        try {
//            System.out.println(domeinController.getGebruikerByEmail());
//        } catch (IllegalArgumentException ex) {
//            System.out.println("Operatie mislukt " + ex.getMessage());
//        }
    }
    
}
