package testen;

import domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.GebruikerDao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {
	private B2BGebruiker adminUser;
	private B2BGebruiker leverancierUser;


    @Mock
    private GebruikerDao gebruikerDaoDummy;

    @InjectMocks
    private LoginController loginController;
    
    @BeforeEach
    public void setup() {
        String adminEmail = "admin@admin.com";
        String adminPassword = "1234";
        Rol adminRole = Rol.ADMIN;
        this.adminUser = new B2BGebruiker(adminEmail, adminPassword, true, adminRole);

        String leverancierEmail = "leverancier@leverancier.com";
        String leverancierPassword = "1234";
        Rol leverancierRole = Rol.LEVERANCIER;
        this.leverancierUser = new B2BGebruiker(leverancierEmail, leverancierPassword, true, leverancierRole);
    }

    @ParameterizedTest
    @CsvSource({
            "admin@admin.com, 1234, ADMIN, domain.AdminController",
            "admin@admin.com, invalidPassword, ADMIN, domain.UserDataInvalidException",
            "admin@admin.com, 1234, LEVERANCIER, domain.LeverancierController",
            "admin@admin.com, invalidPassword, LEVERANCIER, domain.UserDataInvalidException"
    })
    public void testMeldAanAndVerify(String email, String password, Rol role, Class<? extends Object> expectedController) throws UserDataInvalidException {
        if (role == Rol.ADMIN) {
            Mockito.when(gebruikerDaoDummy.getGebruikerByEmail(email)).thenReturn(this.adminUser);
        } else {
            Mockito.when(gebruikerDaoDummy.getGebruikerByEmail(email)).thenReturn(this.leverancierUser);
        }

        if (password.equals("invalidPassword")) {
            Assertions.assertThrows(UserDataInvalidException.class, () -> {
                loginController.meldAan(email, password);
            });
        } else {
            assertInstanceOf(expectedController, loginController.meldAan(email, password));
        }

        Mockito.verify(gebruikerDaoDummy).getGebruikerByEmail(email);
    }
}