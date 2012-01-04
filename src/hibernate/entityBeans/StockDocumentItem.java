package hibernate.entityBeans;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;

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
@Table(name = "stockdocumentitem", catalog = "diplomski_rad_db")
public class StockDocumentItem implements Serializable, EntityObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7056951832187593112L;
	private Integer ID;
	private SalesPriceItem salesPriceItem;
	private Double quantity, pdvPrice, basicPrice, discount;
	
	public StockDocumentItem(){
		quantity = 0.0;
		pdvPrice = 0.0;
		basicPrice = 0.0;
		discount = 0.0;
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
	
	@Column(name = "quantity", unique = false, nullable = false)
	public Double getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	
	@Column(name = "pdvprice", unique = false, nullable = false)
	public Double getPdvPrice() {
		return pdvPrice;
	}
	
	public void setPdvPrice(Double pdvPrice) {
		this.pdvPrice = pdvPrice;
	}
	
	@Column(name = "basicprice", unique = false, nullable = false)
	public Double getBasicPrice() {
		return basicPrice;
	}
	
	public void setBasicPrice(Double basicPrice) {
		this.basicPrice = basicPrice;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch=FetchType.EAGER)
	public SalesPriceItem getSalesPriceItem() {
		return salesPriceItem;
	}
	
	public void setSalesPriceItem(SalesPriceItem salesPriceItem) {
		this.salesPriceItem = salesPriceItem;
	}

	@Column(name = "discount", unique = false, nullable = false, columnDefinition="double default 0")
	public Double getDiscount() {
		return discount;
	}
	
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	
	@Override
	@Transient
	public String getName() {
		return getSalesPriceItem().getProduct().getName();
	}
	
	@Override
	@Transient
	public boolean equals(Object arg0) {
		StockDocumentItem sdi = (StockDocumentItem)arg0;
		if(getSalesPriceItem()!=null && sdi.getSalesPriceItem()!=null){
			boolean b = getSalesPriceItem().getID().intValue() == sdi.getSalesPriceItem().getID().intValue();
			return b;
		}
		return super.equals(arg0);
	}
	
	@Transient
	public int hashCode() {
		if(getSalesPriceItem()!=null){
			return 1;
		}
		return super.hashCode();
	}
	
	@Transient
	public BigDecimal getTotal(){
		return new BigDecimal(getBasicPrice()).add(new BigDecimal(getPdvPrice()));
	}
	
	@Transient
	public void calculate(){
		basicPrice = quantity * salesPriceItem.getWholesalePrice();
		basicPrice = basicPrice - (basicPrice/100*discount);
		pdvPrice = basicPrice/100*salesPriceItem.getTaxRate();
	}
}
