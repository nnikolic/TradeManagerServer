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
@Table(name = "supplies", catalog = "diplomski_rad_db")
public class Supplies implements Serializable, EntityObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 982112133548587712L;

	private Integer ID;
	
	private Product product;
	
	private Double quantity;
	
	//private Warehouse warehouse;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getID() {
		return ID;
	}
	
	public void setID(Integer iD) {
		ID = iD;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	public Product getProduct() {
		return product;
	}
	
	public void setProduct(Product product) {
		this.product = product;
	}
	
	@Column(name = "quantity", unique = false, nullable = false)
	public Double getQuantity() {
		return quantity;
	}
	
	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	
//	public Warehouse getWarehouse() {
//		return warehouse;
//	}
//	
//	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
//	public void setWarehouse(Warehouse warehouse) {
//		this.warehouse = warehouse;
//	}
	
	@Override
	@Transient
	public String getName() {
		return "";
	}
}
