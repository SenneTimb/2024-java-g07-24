package domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.mockito.internal.matchers.Or;

import dto.BestellingDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity(name="Bestelling")
@NamedQuery(name = "B2BBestelling.findByLeverancier", query = "SELECT b FROM Bestelling b WHERE b.leverancier = :leverancier")
public class B2BBestelling implements Serializable , Bestelling  {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderId;
	
	private LocalDate datumGeplaatst;
    private String leverAdres;
    private Orderstatus orderStatus; // Bijvoorbeeld: verwerkt, verzonden, geleverd, etc.
    private Betalingsstatus betalingsstatus;
	private LocalDate betalingsdeadline;

    @ManyToOne
    @JoinColumn(name = "klantId")
    private B2BBedrijf klant;
   
    @ManyToOne
    @JoinColumn(name = "leverancierId")
    private B2BBedrijf leverancier;

    @OneToMany(mappedBy = "bestelling", cascade = CascadeType.ALL)
    private List<BestellingProduct> producten;
    
    protected B2BBestelling() {}
	
    public B2BBestelling(LocalDate datumGeplaatst, String leverAdres, Orderstatus orderStatus, Betalingsstatus betalingsstatus, B2BBedrijf leverancier, B2BBedrijf klant, LocalDate betalingsdeadline) {
        this.datumGeplaatst = datumGeplaatst;
        this.leverAdres = leverAdres;
        this.orderStatus = orderStatus;
        this.producten = new ArrayList<>();
        this.betalingsstatus = betalingsstatus;
        this.leverancier = leverancier;
        this.klant = klant;
        this.betalingsdeadline = betalingsdeadline;
    }
    
    @Override
    public Betalingsstatus getBetalingsstatus() {
		return betalingsstatus;
	}
    @Override
    public void setBetalingsstatus(Betalingsstatus betalingsstatus) {
		this.betalingsstatus = betalingsstatus;
	}
    @Override
    public void setOrderStatus(Orderstatus orderStatus) {
    	this.orderStatus = orderStatus;
    }
    @Override
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
    @Override
	public void setDatumGeplaatst(LocalDate datumGeplaatst) {
		this.datumGeplaatst = datumGeplaatst;
	}
    @Override
	public void setLeverAdres(String leverAdres) {
		this.leverAdres = leverAdres;
	}

    @Override
	public void setKlant(B2BBedrijf klant) {
		this.klant = klant;
	}
    @Override
	public void setLeverancier(B2BBedrijf leverancier) {
		this.leverancier = leverancier;
	}
//	public void setProducten(List<B2BProduct> producten, ) {
//		producten.forEach(product -> {
//			BestellingProduct bestProd = new BestellingProduct(this, product, product.getEenheidsprijs());
//			this.producten.add(bestProd);
//		});
//	}
    
    public void voegTestProductToe(B2BProduct p, int aantal) {
    	BestellingProduct bestProd = new BestellingProduct(this, p, p.getEenheidsprijs(), aantal);
    	this.producten.add(bestProd);
    }

    public LocalDate getDatumGeplaatst() {
        return datumGeplaatst;
    }

    public String getLeverAdres() {
        return leverAdres;
    }

    public Orderstatus getOrderStatus() {
        return orderStatus;
    }


    public List<BestellingProduct> getProducten() {
        return producten;
    }

    public double getTotaleOrderBedrag() {
        return producten.stream().mapToDouble((item) -> (item.getEenheidsprijs() * item.getAantal())).sum();
    }

	@Override
	public Bedrijf getLeverancier() {
		return (Bedrijf) this.leverancier;
	}
	
	@Override
	public LocalDate getBetalingsDeadline() {
		return betalingsdeadline;
	}
    
    @Override
	public int hashCode() {
		return Objects.hash(datumGeplaatst);
	}
    
    
    @Override
	public Bedrijf getKlant() {
		return (Bedrijf) this.klant;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		B2BBestelling other = (B2BBestelling) obj;
		return Objects.equals(datumGeplaatst, other.datumGeplaatst);
	}
	
	public int getOrderId() {
	    return orderId;
	}

	@Override
	public void setBetalingsDeadline(LocalDate betalingsdeadline) {
		this.betalingsdeadline = betalingsdeadline; 
	}

}
