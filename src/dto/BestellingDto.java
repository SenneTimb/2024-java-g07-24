package dto;

import java.time.LocalDate;
import java.util.Date;

import domain.Betalingsstatus;
import domain.Orderstatus;

public record BestellingDto(LocalDate datumGeplaatst, String leverAdres, Orderstatus orderStatus, Betalingsstatus betalingsstatus, BedrijfDto leverancier, BedrijfDto klant, LocalDate bestellingsdeadline) {

}
