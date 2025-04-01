package domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "bestelling_product")
public class BestellingProduct {
    @EmbeddedId
    private BestellingProductKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "bestellingId")
    private B2BBestelling bestelling;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "productId")
    private B2BProduct product;

    private double eenheidsprijs; // Additional field
    private int aantal;

    public BestellingProduct() {}

    public BestellingProduct(B2BBestelling bestelling, B2BProduct product, double eenheidsprijs, int aantal) {
        // Set both parts of the embedded id
        this.id = new BestellingProductKey(bestelling.getOrderId(), product.getProductId());
        this.bestelling = bestelling;
        this.product = product;
        this.eenheidsprijs = eenheidsprijs;
        this.aantal = aantal;
    }
    
    // Getters and setters for fields and for id
    public BestellingProductKey getId() {
        return id;
    }

    public void setId(BestellingProductKey id) {
        this.id = id;
    }
    
    // Getters and setters
    public B2BBestelling getBestelling() {
        return bestelling;
    }

    public void setBestelling(B2BBestelling bestelling) {
        this.bestelling = bestelling;
    }

    public B2BProduct getProduct() {
        return product;
    }

    public void setProduct(B2BProduct product) {
        this.product = product;
    }

    public double getEenheidsprijs() {
        return eenheidsprijs;
    }

    public void setEenheidsprijs(double eenheidsprijs) {
        this.eenheidsprijs = eenheidsprijs;
    }
    
    public int getAantal() {
    	return this.aantal;
    }
    
    public void setAantal(int aantal) {
    	this.aantal = aantal;
    }
}