package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;


@Entity(name = "Product")
public class B2BProduct implements Serializable, Product {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
	@Column(nullable = false)
    private String naam;
	@Column(nullable = false)
    private int aantal;
	@Column(nullable = false)
    private boolean inStock;
    @Column(nullable = false)
    private double eenheidsprijs;

    
    @OneToMany(mappedBy = "product")
    private List<BestellingProduct> bestellingen = new ArrayList<>();

	public B2BProduct(String naam, int aantal, boolean inStock, double eenheidsprijs) {
       setNaam(naam);
       setAantal(aantal);
       this.inStock = inStock;
       setEenheidsprijs(eenheidsprijs);
	}
    
    protected B2BProduct() {}
    
    public int getProductId() {
    	return productId;
    }
    
    @Override
    public String getNaam() {
        return naam;
    }
    @Override
    public int getAantal() {
        return aantal;
    }
    @Override
    public boolean isInStock() {
        return inStock;
    }
    @Override
    public double getEenheidsprijs() {
        return eenheidsprijs;
    }
    
    private void setNaam(String naam) {
    	if(naam == null || naam.isBlank() || naam.isEmpty()) throw new IllegalArgumentException("Product naam mag niet leeg zijn");
    	this.naam = naam;
    }
    
    private void setAantal(int aantal) {
    	if(aantal < 0) throw new IllegalArgumentException("Product aantal mag niet leeg zijn");
    	this.aantal = aantal;
    }
    
    private void setEenheidsprijs(double eenheidsprijs) {
    	if(eenheidsprijs <= 0) throw new IllegalArgumentException("Product: eenheidsprijs mag niet negatief zijn of nul");
    	this.eenheidsprijs = eenheidsprijs;
    }
    
    @Override
   	public int hashCode() {
   		return Objects.hash(naam);
   	}

   	@Override
   	public boolean equals(Object obj) {
   		if (this == obj)
   			return true;
   		if (obj == null)
   			return false;
   		if (getClass() != obj.getClass())
   			return false;
   		B2BProduct other = (B2BProduct) obj;
   		return Objects.equals(naam, other.naam);
   	}
}
