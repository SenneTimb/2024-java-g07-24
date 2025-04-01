package domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import jakarta.persistence.MappedSuperclass;

public interface Bestelling {

    public void voegTestProductToe(B2BProduct p, int aantal);
    public LocalDate getDatumGeplaatst();
    public String getLeverAdres();
    public Orderstatus getOrderStatus();
    public List<BestellingProduct> getProducten();
    public Bedrijf getLeverancier();
    public Bedrijf getKlant();
    public int getOrderId();
    public Betalingsstatus getBetalingsstatus();
	public LocalDate getBetalingsDeadline();
	public void setBetalingsstatus(Betalingsstatus bool);
	public void setOrderStatus(Orderstatus orderStatus);
	public void setLeverancier(B2BBedrijf leverancier);
	public void setKlant(B2BBedrijf klant);
	public void setLeverAdres(String leverAdres);
	public void setDatumGeplaatst(LocalDate datumGeplaatst);
	public void setOrderId(int orderId);
	public void setBetalingsDeadline(LocalDate betalingsdeadline);
	public double getTotaleOrderBedrag();

}
