package main;

import domain.LoginController;
import gui.ConsoleUi;

public class StartUpConsole {
    public static void main(String [] arg) {
        new StartUpConsole().run();
    }

    private void run() {
       new ConsoleUi(new LoginController(true)).run();
        //new ConsoleUi(new LoginController(false)).run(); //zonder DB populate
    }
    
}
