package hibernate.entityBeans;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import util.EntityObject;

@Entity
@Table(name = "salespriceitem", catalog = "diplomski_rad_db")
public class SalesPriceItem implements Serializable, EntityObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6948782887070224759L;

	private Integer ID;
	private Product product;
	private Double retailPrice, wholesalePrice, taxRate;
	
	public SalesPriceItem(){
		retailPrice = 0.0;
		wholesalePrice = 0.0;
		taxRate = 0.0;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getID() {
		return ID;
	}
	
	public void setID(Integer iD) {
		ID = iD;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch=FetchType.EAGER)
	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	@Column(name = "retailprice", unique = false, nullable = false)
	public Double getRetailPrice() {
		return retailPrice;
	}
	
	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}
	
	@Column(name = "wholesaleprice", unique = false, nullable = false)
	public Double getWholesalePrice() {
		return wholesalePrice;
	}
	
	public void setWholesalePrice(Double wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}

	@Column(name = "taxrate", unique = false, nullable = false)
	public Double getTaxRate() {
		return taxRate;
	}
	
	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}
	
	@Override
	@Transient
	public String getName() {
		return "";
	}
	
	@Override
	@Transient
	public boolean equals(Object arg0) {
		SalesPriceItem spi = (SalesPriceItem)arg0;
		if(getProduct()!=null && spi.getProduct()!=null){
			boolean b = getProduct().getID().intValue() == spi.getProduct().getID().intValue();
			return b;
		}
		return super.equals(arg0);
	}
	
	@Transient
	public int hashCode() {
		if(getProduct()!=null){
			return 1;
		}
		return super.hashCode();
	}
}
